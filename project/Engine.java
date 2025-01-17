/**
 * Abstract Engine class to serve as a base for all computing engines.
 *
 * @author Vovencio
 * @version 01/01/2024
 */
public abstract class Engine {

    protected Position enginePosition;

    public Engine() {
        enginePosition = new Position();
        enginePosition.setupInitialBoard();
    }

    public Position getEnginePosition() {
        return enginePosition;
    }

    public void setEnginePosition(Position enginePosition) {
        this.enginePosition = enginePosition;
    }

    /**
     * Abstract method to evaluate the board.
     * Must be implemented by subclasses.
     *
     * @return the evaluation of the board.
     */
    public abstract double evalBoard();

    /**
     * Get the base evaluation value of a piece based on its content.
     *
     * @param content the piece content (1-12, where 1-6 are white and 7-12 are black).
     * @return the base evaluation value.
     */
    public static int getBaseEval(byte content) {
        int factor = (content > 6) ? -1 : 1;
        boolean isBlack = (content > 6);
        byte piece = isBlack ? (byte) (content - 6) : content;

        return switch (piece) {
            case 1 -> factor;         // Pawn
            case 2 -> 3 * factor;     // Knight
            case 3 -> 4 * factor;     // Bishop
            case 4 -> 5 * factor;     // Rook
            case 5 -> 9 * factor;     // Queen
            case 6 -> 69 * factor;    // King
            default -> 0;
        };
    }

    /**
     * Get the worth of a piece as an integer.
     *
     * @param content the piece content (1-12, where 1-6 are white and 7-12 are black).
     * @return the worth of the piece.
     */
    public static int getWorthInt(byte content) {
        boolean isBlack = (content > 6);
        byte piece = isBlack ? (byte) (content - 6) : content;

        return switch (piece) {
            case 1 -> 1;  // Pawn
            case 2 -> 3;  // Knight
            case 3 -> 4;  // Bishop
            case 4 -> 5;  // Rook
            case 5 -> 9;  // Queen
            default -> 0; // King or empty square
        };
    }

    /**
     * Check if only pawns remain on the board.
     *
     * @return true if only pawns remain; false otherwise.
     */
    public boolean isOnlyPawns() {
        for (byte x = 0; x < 8; x++) {
            for (byte y = 0; y < 8; y++) {
                byte content = enginePosition.getSquare(x, y).getContent();
                if (content != 0 && content != 1 && content != 7) { // Non-pawn pieces
                    return false;
                }
            }
        }
        return true;
    }

    public abstract double qSearch(int depth, Position position);

    public abstract Branch generateBestMove(int depth, Position position);
}
