import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

/**
 * Branch class, which demonstrates a chess board.
 *
 * @author Vovencio
 * @version 01/01/2024
 */

public class Branch {

    static final double MATE_VALUE = 1000_000;
    static final double MATE_THRES = 900_000;

    static final int MAX_HISTORY = Integer.MAX_VALUE;

    public static int[][][] historyTable;
    public static int[][][] takeTable;
    public static long takeCount;

    public static int currentSearch;

    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    static void updateHistory(int depth, byte piece, byte x, byte y, boolean cutOff){
        int bonus = (cutOff) ? depth * depth : 0; // (int) (-depth / 1.8);

        int clampedBonus = clamp(bonus, -MAX_HISTORY, MAX_HISTORY);

        historyTable[piece-1][x][y] += clampedBonus - historyTable[piece-1][x][y] * Math.abs(clampedBonus) / MAX_HISTORY;
    }

    static void updateTake (int depth, byte piece, byte x, byte y, boolean cutOff){
        takeCount += depth;

        int bonus = (cutOff) ? depth * depth : 0; // (int) (-depth / 1.8);

        int clampedBonus = clamp(bonus, -MAX_HISTORY, MAX_HISTORY);

        takeTable[piece-1][x][y] += clampedBonus - historyTable[piece-1][x][y] * Math.abs(clampedBonus) / MAX_HISTORY;
    }

    static public int evaluationCount;

    private static double BASE_EVAL = -69;
    private double evaluation = BASE_EVAL;

    private Move move;
    private Branch parent;
    private List<Branch> children;
    private final Position position;
    private final EngineOld engine;
    private boolean notDeeper = false;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    private int score;

    public boolean isKiller() {
        return isKiller;
    }

    public void setKiller(boolean killer) {
        isKiller = killer;
    }

    public boolean isCapture() {
        return isCapture;
    }

    public void setCapture(boolean capture) {
        isCapture = capture;
    }

    private boolean isKiller;
    private boolean isCapture;

    public List<Move> getKillerMoves() {
        return killerMoves;
    }

    private List<Move> killerMoves = new ArrayList<>();

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    private int depth;

    private Branch bestChild;

    public Branch getBestChild() {
        return bestChild;
    }

    public Branch(Move move, Branch parent, Position position, EngineOld engine) {
        this.move = move;
        this.parent = parent;
        this.position = position;
        this.engine = engine;
        this.children = new ArrayList<>();
        this.depth = parent.getDepth() + 1;
    }

    public Branch(Position position, EngineOld engine) {
        this.position = position;
        this.engine = engine;
        this.children = new ArrayList<>();
        this.depth = 0;
    }

    private double evaluate(){
        evaluationCount++;
        position.playMove(move); // Apply the move
        this.evaluation = engine.evalBoard(); // Evaluate the board after the move
        position.reverseMove(move);
        return evaluation;
    }

    public List<Branch> sortMoves(){
        // Killer Branches are getting checked because these caused a cutoff in a sibling position.
        List<Branch> branchesToCheck = new ArrayList<>();

        List<Branch> killerBranches = new ArrayList<>();
        List<Branch> captureBranches = new ArrayList<>();
        List<Branch> normalBranches = new ArrayList<>();

        List<Branch> superCaptureBranches = new ArrayList<>();
        List<Branch> awfulCaptureBranches = new ArrayList<>();

        boolean killerAvailable = false;
        if (this.parent != null) {
            if (!parent.getKillerMoves().isEmpty()) {
                killerAvailable = true;
            }
        }

        if (killerAvailable) {
            for (Branch child : children){
                for (Move killerMove : this.parent.killerMoves) {
                    if (Move.isEqual(child.getMove(), killerMove)) {
                        killerBranches.add(child);
                        child.setKiller(true);
                        break;
                    }
                }
            }
        }

        for (Branch child : children) {
            if (!child.isKiller()){
                if (child.move.getToPiece() != 0) {
                    child.setCapture(true);
                    int capScore = EngineOld.getWorthInt(child.move.getToPiece()) - EngineOld.getWorthInt(child.move.getFromPiece());
                    child.score = takeTable[child.move.getFromPiece()-1][child.move.getToPositionX()][child.move.getToPositionY()];
                    if (capScore > 2) superCaptureBranches.add(child);
                    else if (capScore < -7) {
                        awfulCaptureBranches.add(child);
                    }
                    else captureBranches.add(child);
                } else {
                    normalBranches.add(child);
                    child.score = historyTable[child.move.getFromPiece()-1][child.move.getToPositionX()][child.move.getToPositionY()];
                }
            }
        }

        normalBranches.sort(Comparator.comparingInt(Branch::getScore).reversed());
        captureBranches.sort(Comparator.comparingInt(Branch::getScore).reversed());

        branchesToCheck.addAll(superCaptureBranches);
        branchesToCheck.addAll(killerBranches);
        branchesToCheck.addAll(captureBranches);
        branchesToCheck.addAll(awfulCaptureBranches);
        branchesToCheck.addAll(normalBranches);

        return branchesToCheck;
    }

    public double maxi(double alpha, double beta, int depth) {
        // byte[] kingPos = (position.isActiveWhite()) ? position.getKingPosWhite() : position.getKingPosBlack();

        if (notDeeper){
            return evaluation;
        }
        // If it's a leaf node, perform the evaluation directly
        if (depth == 0){
            return evaluate();
        }

        if (children.isEmpty()) {
            this.generateChildren();
            if (notDeeper){
                return evaluation;
            }
        }

        double max = -Double.MAX_VALUE;

        List<Branch> branchesToCheck = sortMoves();

        for (Branch child : branchesToCheck) {
            position.playMove(child.getMove());
            double eval = child.mini(alpha, beta, depth-1);
            position.reverseMove(child.getMove());

            // Mate Distance Pruning
            if (child.isMate()){
                if (eval < beta) {
                    beta = eval;
                    if (alpha >= eval) return eval;
                }
                if (-eval > alpha) {
                    alpha = -eval;
                    if (beta <= -eval) return -eval;
                }
            }
            if (eval > max) {
                max = eval;
                bestChild = child;
            }
            if (max > alpha) {
                alpha = max;
            }
            if (alpha >= beta) {
                if (parent != null) {
                    boolean isNew = true;
                    for (Move killerMove : this.parent.killerMoves) {
                        if (Move.isEqual(child.getMove(), killerMove)){
                            isNew = false;
                            break;
                        }
                    }
                    if (isNew) this.parent.getKillerMoves().add(child.move);
                }
                if (!child.isKiller()){
                    if (!child.isCapture())
                        updateHistory(depth, child.move.getFromPiece(), child.move.getToPositionX(), child.move.getToPositionY(), true);
                    else updateTake(depth, child.move.getFromPiece(), child.move.getToPositionX(), child.move.getToPositionY(), true);
                }
                break; // Beta cutoff
            }

            // Apply penalty.

            if (!child.isKiller()){
                if (!child.isCapture())
                    updateHistory(depth, child.move.getFromPiece(), child.move.getToPositionX(), child.move.getToPositionY(), false);
                else updateTake(depth, child.move.getFromPiece(), child.move.getToPositionX(), child.move.getToPositionY(), false);
            }
        }

        this.evaluation = max;

        // This is a mate-position, so we must not search deeper.
        if (isMate()) {
            this.notDeeper = true;
        }

        return max;
    }

    public double mini(double alpha, double beta, int depth) {

        if (notDeeper){
            return evaluation;
        }
        // If it's a leaf node, perform the evaluation directly
        if (depth == 0){
            return evaluate();
        }

        if (children.isEmpty()) {
            this.generateChildren();
            if (notDeeper){
                return evaluation;
            }
        }

        double min = Double.MAX_VALUE;

        List<Branch> branchesToCheck = sortMoves();

        for (Branch child : branchesToCheck) {
            position.playMove(child.getMove());
            double eval = child.maxi(alpha, beta, depth-1);
            position.reverseMove(child.getMove());

            // Mate Distance Pruning
            if (child.isMate()){
                if (eval < beta) {
                    beta = eval;
                    if (alpha >= eval) return eval;
                }
                if (-eval > alpha) {
                    alpha = -eval;
                    if (beta <= -eval) return -eval;
                }
            }

            if (eval < min) {
                min = eval;
                bestChild = child;
            }
            if (min < beta) {
                beta = min;
            }
            if (beta <= alpha) {
                if (parent != null) {
                    boolean isNew = true;
                    for (Move killerMove : this.parent.killerMoves) {
                        if (Move.isEqual(child.getMove(), killerMove)){
                            isNew = false;
                            break;
                        }
                    }
                    if (isNew) this.parent.getKillerMoves().add(child.move);
                }
                if (!child.isKiller()){
                    if (!child.isCapture())
                        updateHistory(depth, child.move.getFromPiece(), child.move.getToPositionX(), child.move.getToPositionY(), true);
                    else updateTake(depth, child.move.getFromPiece(), child.move.getToPositionX(), child.move.getToPositionY(), true);
                }
                break; // Alpha cutoff
            }

            // Apply penalty.

            if (!child.isKiller()){
                if (!child.isCapture())
                    updateHistory(depth, child.move.getFromPiece(), child.move.getToPositionX(), child.move.getToPositionY(), false);
                else updateTake(depth, child.move.getFromPiece(), child.move.getToPositionX(), child.move.getToPositionY(), false);
            }
        }

        this.evaluation = min;

        // This is a mate-position, so we must not search deeper.
        if (isMate()) {
            this.notDeeper = true;
        }

        return min;
    }

    public void generateChildren() {
        // Generate the child branches based on all possible moves if no children exist
        if (children.isEmpty()) {
            List<Move> possibleMoves = position.getPossibleMovesBoard(position.isActiveWhite());
            if (notDeeper){
                return;
            }
            if (possibleMoves.isEmpty()){
                this.notDeeper = true;
                byte[] kingPos = position.isActiveWhite() ? position.getKingPosWhite() : position.getKingPosBlack();
                if (position.isAttacked(kingPos[0], kingPos[1], position.isActiveWhite())){
                    evaluation = position.isActiveWhite() ? -(MATE_VALUE-depth) : (MATE_VALUE-depth);
                }
                else {
                    evaluation = 0;
                }
            }
            else {
                for (Move possibleMove : possibleMoves){
                    children.add(new Branch(possibleMove, this, this.position, this.engine));
                }
            }
        }
    }

    public double getEvaluation() {
        return evaluation;
    }

    public Move getMove() {
        return move;
    }

    public Branch getParent() {
        return parent;
    }

    public List<Branch> getChildren() {
        return children;
    }

    public Position getPosition() {
        return position;
    }

    public boolean isNotDeeper() {
        return notDeeper;
    }

    public void setNotDeeper(boolean notDeeper) {
        this.notDeeper = notDeeper;
    }

    public boolean isMate(){
        return Math.abs(this.evaluation) > MATE_THRES;
    }
}
