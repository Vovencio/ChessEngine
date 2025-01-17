import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

/**
 * Branch class, which demonstrates a chess board.
 *
 * @author Vovencio
 * @version 01/11/2024
 */

public class Branch {

    static final double MATE_VALUE = 1000_000;
    static final double MATE_THRES = 900_000;

    static final int MAX_HISTORY = Integer.MAX_VALUE;

    public static int[][][] historyTable;
    public static int[][][] takeTable;
    public static long takeCount;

    private boolean isCuttoff = false;

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
    private final Engine engine;
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

    public Branch(Move move, Branch parent, Position position, Engine engine) {
        this.move = move;
        this.parent = parent;
        this.position = position;
        this.engine = engine;
        this.children = new ArrayList<>();
        this.depth = parent.getDepth() + 1;
    }

    public Branch(Position position, Engine engine) {
        this.position = position;
        this.engine = engine;
        this.children = new ArrayList<>();
        this.depth = 0;
    }

    private double evaluate(){
        if (parent != null){
            position.playMove(move); // Apply the move
            this.evaluation = engine.evalBoard(); // Evaluate the board after the move
            position.reverseMove(move);
            return evaluation;
        }
        this.evaluation = engine.evalBoard(); // Evaluate the board
        return evaluation;
    }

    public List<Branch> sortMoves(List<Branch> toSort){

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
            for (Branch child : toSort){
                for (Move killerMove : this.parent.killerMoves) {
                    if (Move.isEqual(child.getMove(), killerMove)) {
                        killerBranches.add(child);
                        child.setKiller(true);
                        break;
                    }
                }
            }
        }

        for (Branch child : toSort) {
            if (!child.isKiller()){
                if (child.move.getToPiece() != 0) {
                    child.setCapture(true);
                    int capScore = EngineAlg.getWorthInt(child.move.getToPiece()) - EngineAlg.getWorthInt(child.move.getFromPiece());
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

    public double qMaxi(int depth, double alpha, double beta) {
        double startEval = evaluate();
        // byte[] kingPos = (position.isActiveWhite()) ? position.getKingPosWhite() : position.getKingPosBlack();

        if (notDeeper){
            return startEval;
        }
        // If it's a leaf node, perform the evaluation directly
        if (depth == 0){
            return startEval;
        }

        if (startEval >= beta){
            return startEval;
        }
        if (alpha < startEval)
            alpha = startEval;

        if (children.isEmpty()) {
            this.generateCaptureChildren();
            if (children.isEmpty()) {
                return startEval;
            }
        }

        double max = startEval;

        List<Branch> branchesToCheck = this.children; //sortMoves(this.children);

        for (Branch child : branchesToCheck) {

            if (EngineAlg.getWorthInt(child.move.getToPiece())- EngineAlg.getWorthInt(child.move.getFromPiece()) < 0)
                continue;
            if (child.isMate())
                continue;

            position.playMove(child.getMove());
            double staticEval = child.evaluate();

            if (staticEval > startEval){
                double eval = child.qMini(depth-1, alpha, beta);
                if (eval >= beta)
                    return max;
                if (eval > max)
                    max = eval;
                if (eval > alpha)
                    alpha = eval;
            }
            position.reverseMove(child.getMove());
        }

        this.evaluation = max;

        this.children.clear();

        return max;
    }

    public double qMini(int depth, double alpha, double beta) {
        double startEval = evaluate();
        // byte[] kingPos = (position.isActiveWhite()) ? position.getKingPosWhite() : position.getKingPosBlack();

        if (notDeeper) {
            return startEval;
        }
        // If it's a leaf node, perform the evaluation directly
        if (depth == 0) {
            return startEval;
        }

        if (startEval <= alpha) {
            return startEval;
        }
        if (beta > startEval) {
            beta = startEval;
        }

        if (children.isEmpty()) {
            this.generateCaptureChildren();
            if (children.isEmpty()) {
                return startEval;
            }
        }

        double min = startEval;

        List<Branch> branchesToCheck = this.children; // sortMoves(this.children);

        for (Branch child : branchesToCheck) {
            if (child.isMate())
                continue;
            if (EngineAlg.getWorthInt(child.move.getToPiece()) - EngineAlg.getWorthInt(child.move.getFromPiece()) < 0) {
                continue;
            }

            position.playMove(child.getMove());
            double staticEval = child.evaluate();

            if (staticEval < startEval) {
                double eval = child.qMaxi(depth - 1, alpha, beta);
                if (eval <= alpha) {
                    return min;
                }
                if (eval < min) {
                    min = eval;
                }
                if (eval < beta) {
                    beta = eval;
                }
            }
            position.reverseMove(child.getMove());
        }

        return min;
    }

    public double qSearch(double alpha, double beta){
        double al = alpha + 0; double be = beta + 0;
        if (position.isActiveWhite()) {
            double max = qMaxi(10, al, be);
            this.children.clear();
            return max;
        }
        else{
            double min = qMini(10, al, be);
            this.children.clear();
            return min;
        }
    }

    public double qSearch(double alpha, double beta, int d){
        double al = alpha + 0; double be = beta + 0;
        if (position.isActiveWhite()) {
            double max = qMaxi(d, al, be);
            this.children.clear();
            return max;
        }
        else{
            double min = qMini(d, al, be);
            this.children.clear();
            return min;
        }
    }

    static final double NMP_MARGIN = 3;

    private int reducedDepth(int d){
        return Math.max(d-3,0);
        //return Math.max(d/3, 0);
    }

    public double negaMax(double alpha, double beta, int depth, boolean checkPruning) {
        // byte[] kingPos = (position.isActiveWhite()) ? position.getKingPosWhite() : position.getKingPosBlack();

        if (notDeeper){
            return evaluation;
        }
        // If it's a leaf node, perform the evaluation directly
        if (depth == 0){
            return evaluate(); //return qSearch(alpha, beta); //qSearch(alpha, beta);
        }

        if (children.isEmpty()) {
            this.generateChildren();
            if (notDeeper){
                return evaluation;
            }
        }

        if (checkPruning && depth > 2){
            byte[] kingPos = position.isActiveWhite() ? position.getKingPosWhite() : position.getKingPosBlack();
            if (!position.isAttacked(kingPos[0], kingPos[1], position.isActiveWhite())
                    && !engine.isOnlyPawns()
                    && position.getPossibleMovesBoard(position.isActiveWhite()).size() > 3) {

                int r = reducedDepth(depth); // NMP reduction


                NullMove nullMove = new NullMove(position);
                Branch nullBranch = new Branch(nullMove, this, this.position, this.engine);
                position.playMove(nullMove);
                double v = -nullBranch.negaMax(-beta, -beta + 1, r, false); // Beta window for maximizing
                position.reverseMove(nullMove);

                if (v >= beta) {
                    this.evaluation = v;
                    return v;
                }
            }
        }

        double max = -Double.MAX_VALUE;

        List<Branch> branchesToCheck = sortMoves(this.children);

        int childId = 0;
        int childThirdAmount = children.size() / 3 * 4 + 1;
        for (Branch child : branchesToCheck) {
            position.playMove(child.getMove());
            boolean LMRWorked = false;
            double eval = 0;
            /*if (checkPruning){
                byte[] kingPos = position.isActiveWhite() ? position.getKingPosWhite() : position.getKingPosBlack();
                if (!position.isAttacked(kingPos[0], kingPos[1], position.isActiveWhite())
                        && !engine.isOnlyPawns()
                        && depth > 3
                        && childId > childThirdAmount
                        && position.getPossibleMovesBoard(position.isActiveWhite()).size() > 3) {
                    if (!position.isAttacked(kingPos[0], kingPos[1], position.isActiveWhite())
                            && !engine.isOnlyPawns()) {

                        int r = reducedDepth(depth); // Late Move Reduction

                        double v = child.mini(alpha, beta, r, false); // Beta window for maximizing

                        if (v >= beta) {
                            eval = v;
                            LMRWorked = true;
                        }
                    }
                }
            }
             */
            //if (!LMRWorked){
            eval = -child.negaMax(-beta, -alpha, depth-1, true);
            //}
            position.reverseMove(child.getMove());

            // Mate Distance Pruning
            //if (child.isMate() && eval > 0){
            //    if (eval < beta) {
            //        beta = eval;
            //        if (alpha >= eval) return eval;
            //    }
            //}
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

            childId++;
        }

        this.evaluation = max;

        // This is a mate-position, so we must not search deeper.
        //if (isMate()) {
        //    this.notDeeper = true;
        //}

        return max;
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
                    evaluation =-(MATE_VALUE-depth);
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

    public void generateCaptureChildren() {
        // Generate the child branches based on all possible captures if no children exist
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
                    if (possibleMove.getToPiece() != 0)
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

    public boolean isCuttoff() {
        return isCuttoff;
    }

    public void setCuttoff(boolean cuttoff) {
        isCuttoff = cuttoff;
    }
}
