import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
 * Represents a chessboard consisting of 8x8 squares.
 *
 * @author Vovencio
 * @version 12/23/24
 */
public class Position {

    // 8x8 grid of squares
    private Square[][] squares = new Square[8][8];

    // Arrays of all coordinates of certain pieces
    private ArrayList<byte[]> whitePieces = new ArrayList<>();
    private ArrayList<byte[]> blackPieces = new ArrayList<>();
    private ArrayList<byte[]> pieces = new ArrayList<>();

    // King positions, so that they must not be searched for
    private byte[] kingPosWhite = new byte[2];
    private byte[] kingPosBlack = new byte[2];

    /**
     * Initializes the board with empty squares.
     */
    public Position() {
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

        updatePieceLists(x, y, content);
        // Update White King Position
        if (content == 6) {
            kingPosWhite[0] = x; kingPosWhite[1] = y;
        }
        else if (content == 12) {
            kingPosBlack[0] = x; kingPosBlack[1] = y;
        }
    }

    private void updatePieceLists(byte x, byte y, byte content) {
        byte[] position = {x, y};

        // Remove the given position from all lists.
        whitePieces.removeIf(pos -> Arrays.equals(pos, position));
        blackPieces.removeIf(pos -> Arrays.equals(pos, position));
        pieces.removeIf(pos -> Arrays.equals(pos, position));

        // Add the position to the correct list based on the content
        if (content > 0) { // Not empty
            if (content < 7) { // White Pieces
                whitePieces.add(position);
                pieces.add(position);
                return;
            } // Black Pieces
            blackPieces.add(position);
            pieces.add(position);
        }
        // If content == 0, it is empty and should not be added.
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
                String content = (square.hasPiece() != 0)
                        ? ((square.isWhiteTeam() == 1) ? "W" : "B") + getPieceType(square)
                        : "--";
                System.out.print(content + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Maps piece content to a type for display purposes.
     *
     * @param square The square containing the piece.
     * @return The character representing the piece type (e.g., P, N, B, R, Q, K).
     */
    private String getPieceType(Square square) {
        byte pieceType = (byte) (square.getContent() > 6 ? square.getContent() - 6 : square.getContent()); // Normalize black content
        return switch (pieceType) {
            case 0 -> "-"; // Empty
            case 1 -> "P"; // Pawn
            case 2 -> "N"; // Knight
            case 3 -> "B"; // Bishop
            case 4 -> "R"; // Rook
            case 5 -> "Q"; // Queen
            case 6 -> "K"; // King
            default -> "?"; // Unknown content
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


    public List<Move> getPossibleMovesBoard(){
        return new ArrayList<>();
    }

    public List<Move> getPossibleMovesTile(byte x, byte y){
        List<Move> moves = new ArrayList<>();

        switch (getSquare(x, y).getContent()){
            case 0:
                System.out.println("Checking an empty square, this should not be happening...");
                break;
                // White pawn
            case 1:
                // First Double Move
                if (y < 6) {
                    if (getSquare(x, (byte) (y + 1)).hasPiece() == 0) {
                        // Normal Move
                        moves.add(new NormalMove(x, y, x, (byte) (y + 1)));
                        if (y == 1 & getSquare(x, (byte) (y + 1)).hasPiece() == 0) {
                            moves.add(new NormalMove(x, y, x, (byte) (y + 1)));
                        }
                    }
                }

                break;
            default:
                System.out.printf("Unknown piece type %d :(%n", getSquare(x, y).getContent());
        }

        return moves;
    }

    /**
     * Plays a move on the chessboard. This method does not prove the move's correctness.
     *
     * @param move The move, which should be played.
     */
    public void playMove(Move move) {
        move.Play(this);
    }
}
