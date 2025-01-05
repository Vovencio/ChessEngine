import java.util.ArrayList;
import java.util.List;

/**
 * Branch class, which demonstrates a chess board.
 *
 * @author Vovencio
 * @version 01/01/2024
 */

public class Branch {
    private double evaluation;

    private Move move;
    private Branch parent;
    private List<Branch> children;
    private final Position position;
    private final Engine engine;
    private boolean notDeeper = false;

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
        position.playMove(move); // Apply the move
        evaluation = engine.evalBoard(); // Evaluate the board after the move
        position.reverseMove(move);
        return evaluation;
    }

    public double maxi(double alpha, double beta) {
        if (notDeeper){
            return evaluation;
        }
        // If it's a leaf node (no children), perform the evaluation directly
        if (children.isEmpty()) {
            return evaluate();
        }
        double max = -Double.MAX_VALUE;

        for (Branch child : this.children) {
            position.playMove(child.getMove());
            double eval = child.mini(alpha, beta);
            position.reverseMove(child.getMove());

            if (eval > max) {
                max = eval;
                bestChild = child;
            }
            if (max > alpha) {
                alpha = max;
            }
            if (alpha >= beta) {
                break; // Beta cutoff
            }
        }

        this.evaluation = max;
        return max;
    }

    public double mini(double alpha, double beta) {
        if (notDeeper){
            return evaluation;
        }
        // If it's a leaf node (no children), perform the evaluation directly
        if (children.isEmpty()) {
            return evaluate();
        }
        double min = Double.MAX_VALUE;

        for (Branch child : children) {
            position.playMove(child.getMove());
            double eval = child.maxi(alpha, beta);
            position.reverseMove(child.getMove());

            if (eval < min) {
                min = eval;
                bestChild = child;
            }
            if (min < beta) {
                beta = min;
            }
            if (beta <= alpha) {
                break; // Alpha cutoff
            }
        }

        this.evaluation = min;
        return min;
    }

    public void generateChildren() {
        if (parent != null) position.playMove(move);
        // Generate the child branches based on all possible moves if no children exist
        if (children.isEmpty()) {
            List<Move> possibleMoves = position.getPossibleMovesBoard(position.isActiveWhite());
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
        } else {
            // Recurse into each child and generate their children as well
            for (Branch child : children) {
                child.generateChildren();
            }
        }
        if (parent != null) position.reverseMove(move);
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
