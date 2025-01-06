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

    public static int[][][] historyTable;

    static public int evals;

    private double evaluation;

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
        evals++;
        position.playMove(move); // Apply the move
        evaluation = engine.evalBoard(); // Evaluate the board after the move
        position.reverseMove(move);
        return evaluation;
    }

    public double maxi(double alpha, double beta, int depth) {
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

        // Killer Branches are getting checked because these caused a cutoff in a sibling position.
        List<Branch> branchesToCheck = new ArrayList<>();

        List<Branch> killerBranches = new ArrayList<>();
        List<Branch> captureBranches = new ArrayList<>();
        List<Branch> normalBranches = new ArrayList<>();

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
                    captureBranches.add(child);
                    child.setCapture(true);
                } else {
                    normalBranches.add(child);
                    child.score = historyTable[child.move.getFromPiece()-1][child.move.getToPositionX()][child.move.getToPositionY()];
                }
            }
        }

        normalBranches.sort(Comparator.comparingInt(Branch::getScore).reversed());

        branchesToCheck.addAll(killerBranches);
        branchesToCheck.addAll(captureBranches);
        branchesToCheck.addAll(normalBranches);


        for (Branch child : branchesToCheck) {
            position.playMove(child.getMove());
            double eval = child.mini(alpha, beta, depth-1);
            position.reverseMove(child.getMove());

            if (eval > max) {
                max = eval;
                bestChild = child;
            }
            if (max > alpha) {
                alpha = max;
            }
            if (alpha >= beta) {
                if (parent != null) this.parent.getKillerMoves().add(child.move);
                if (!child.isCapture())
                    historyTable[child.move.getFromPiece()-1][child.move.getToPositionX()][child.move.getToPositionY()] += depth*depth;
                break; // Beta cutoff
            }
        }

        this.evaluation = max;
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

        // Killer Branches are getting checked because these caused a cutoff in a sibling position.
        List<Branch> branchesToCheck = new ArrayList<>();

        List<Branch> killerBranches = new ArrayList<>();
        List<Branch> captureBranches = new ArrayList<>();
        List<Branch> normalBranches = new ArrayList<>();

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
                    captureBranches.add(child);
                    child.setCapture(true);
                } else {
                    normalBranches.add(child);
                    child.score = historyTable[child.move.getFromPiece()-1][child.move.getToPositionX()][child.move.getToPositionY()];
                }
            }
        }

        normalBranches.sort(Comparator.comparingInt(Branch::getScore).reversed());

        branchesToCheck.addAll(killerBranches);
        branchesToCheck.addAll(captureBranches);
        branchesToCheck.addAll(normalBranches);

        for (Branch child : branchesToCheck) {
            position.playMove(child.getMove());
            double eval = child.maxi(alpha, beta, depth-1);
            position.reverseMove(child.getMove());

            if (eval < min) {
                min = eval;
                bestChild = child;
            }
            if (min < beta) {
                beta = min;
            }
            if (beta <= alpha) {
                if (parent != null) this.parent.getKillerMoves().add(child.move);
                if (!child.isCapture())
                    historyTable[child.move.getFromPiece()-1][child.move.getToPositionX()][child.move.getToPositionY()] += depth*depth;
                break; // Alpha cutoff
            }
        }

        this.evaluation = min;
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
                    evaluation = position.isActiveWhite() ? -(1000-depth) : (1000-depth);
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

    // Removed the `isLeaf()` method, now we use children.isEmpty() directly
    public boolean hasChildren() {
        return !children.isEmpty();
    }
}
