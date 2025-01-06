/**
 * Engine Class used for all computing.
 *
 * @author Vovencio
 * @version 01/01/2024
 */

public class Engine {
    public Position getEnginePosition() {
        return enginePosition;
    }

    public void setEnginePosition(Position enginePosition) {
        this.enginePosition = enginePosition;
    }

    private double[][][] evalTables;

    Position enginePosition;

    public Engine (double[][][] evalTables) {
        enginePosition = new Position();
        enginePosition.setupInitialBoard();

        this.evalTables = evalTables;
    }

    public double evalBoard() {
        double eval = 0;

        for (byte x = 0; x < 8; x++) {
            for (byte y = 0; y < 8; y++) {
                eval += evalSquare(x, y);
            }
        }

        return eval;
    }

    public double evalSquare(byte x, byte y) {
        byte content = enginePosition.getSquare(x, y).getContent();

        return evalTables[content][x][y];
    }

    public static double getBaseEval(byte content){
        double factor = (content > 6) ? -1 : 1;

        boolean isBlack = (content > 6);

        byte piece = isBlack ? (byte) (content - 6) : content;

        return switch (piece) {
            case 1 -> 1 * factor;
            case 2 -> 2.8 * factor;
            case 3 -> 3.3 * factor;
            case 4 -> 5 * factor;
            case 5 -> 9 * factor;
            default -> 0;
        };
    }

    public int getDistanceToEnemyKing(boolean isBlack, byte x, byte y) {
        byte[] kingPos = isBlack ? enginePosition.getKingPosWhite() : enginePosition.getKingPosBlack();
        return Math.abs(kingPos[0] - x) + Math.abs(kingPos[1] - y);
    }

    /*
    public Branch generateBestMove(int depth, Position position){
        enginePosition.loadFEN(position.generateFEN());
        Branch root = new Branch(enginePosition, this);

        if (position.isActiveWhite()) root.maxi(-Double.MAX_VALUE, Double.MAX_VALUE, depth);
        else root.mini(-Double.MAX_VALUE, Double.MAX_VALUE, depth);

        return root.getBestChild();
    }
     */
}
