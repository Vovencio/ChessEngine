/**
 * Represents a chessboard consisting of 8x8 squares.
 *
 * @author Vovencio
 * @version 12/20/24
 */
public class Board {

    // 8x8 grid of squares
    private final Square[][] squares = new Square[8][8];

    /**
     * Initializes the board with empty squares.
     */
    public Board() {
        for (byte x = 0; x < 8; x++) {
            for (byte y = 0; y < 8; y++) {
                squares[x][y] = new Square(x, y, (byte) 0); // Initialize as empty
            }
        }
    }

    /**
     * Places a piece on a given square.
     *
     * @param x       X-coordinate of the square (0-7)
     * @param y       Y-coordinate of the square (0-7)
     * @param content Content to set (0 = Empty, 1 = Pawn, ..., 6 = King; +6 for black)
     */
    public void setSquareContent(byte x, byte y, byte content) {
        squares[x][y].setContent(content);
    }

    /**
     * Retrieves a specific square on the board.
     *
     * @param x X-coordinate of the square (0-7)
     * @param y Y-coordinate of the square (0-7)
     * @return The square at the specified coordinates.
     */
    public Square getSquare(byte x, byte y) {
        return squares[x][y];
    }

    /**
     * Displays the board's current state as a simple text representation.
     */
    public void printBoard() {
        for (int y = 7; y >= 0; y--) { // Print from top to bottom (chessboard perspective)
            for (int x = 0; x < 8; x++) {
                Square square = squares[x][y];
                String content = square.hasPiece()
                        ? (square.isWhiteTeam() ? "W" : "B") + getPieceType(square)
                        : ".";
                System.out.print(content + " ");
            }
            System.out.println();
        }
    }

    /**
     * Maps piece content to a type for display purposes.
     *
     * @param square The square containing the piece.
     * @return The character representing the piece type (e.g., P, N, B, R, Q, K).
     */
    private String getPieceType(Square square) {
        byte content = (byte) (square.get); // Example content
        return switch (content % 6) {
            case 1 -> "P"; // Pawn
            case 2 -> "N"; // Knight
            case 3 -> "B"; // Bishop
            case 4 -> "R"; // Rook
            case 5 -> "Q"; // Queen
            case 0 -> "K"; // King
            default -> "?";
        };
    }

    /**
     * Resets the board to its initial state with standard chess piece positions.
     */
    public void setupInitialBoard() {
        // Setup pawns
        for (byte x = 0; x < 8; x++) {
            setSquareContent(x, (byte) 1, (byte) 1); // White pawns
            setSquareContent(x, (byte) 6, (byte) 7); // Black pawns
        }

        // Setup major pieces (rooks, knights, bishops, etc.)
        byte[] pieceOrder = {4, 2, 3, 5, 6, 3, 2, 4};
        for (byte x = 0; x < 8; x++) {
            setSquareContent(x, (byte) 0, pieceOrder[x]); // White pieces
            setSquareContent(x, (byte) 7, (byte) (pieceOrder[x] + 6)); // Black pieces
        }
    }
}
