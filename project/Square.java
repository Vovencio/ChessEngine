/**
 * Represents a single tile on a chessboard.
 *
 * @author Vovencio
 * @version 12/20/24
 */
public class Square {

    //#region Attributes

    // Coordinates of the tile
    private final byte x;
    private final byte y;

    // The content of the tile, a.k.a. the piece, which is there
    private byte content;

    // Indicates if the tile contains a chess piece
    private byte hasPiece;

    // Indicates if the tile's piece is part of the white team (true) or black team (false)
    private byte isWhite;

    // Edge indicators
    private final boolean onEdgeX;
    private final boolean onEdgeY;
    private final boolean onEdge;

    //#endregion

    //#region Getters

    /**
     * @return the X-coordinate of the square.
     */
    public byte getX() {
        return x;
    }

    /**
     * @return the Y-coordinate of the square.
     */
    public byte getY() {
        return y;
    }

    /**
     * @return 0 when empty, 1 when white, 2 when black.
     */
    public byte hasPiece() {
        return hasPiece;
    }

    /**
     * @return whether the piece on this square belongs to the white team.
     */
    public byte isWhiteTeam() {
        return isWhite;
    }

    /**
     * @return whether this square is on the left or right edge of the board.
     */
    public boolean isOnEdgeX() {
        return onEdgeX;
    }

    /**
     * @return whether this square is on the top or bottom edge of the board.
     */
    public boolean isOnEdgeY() {
        return onEdgeY;
    }

    public boolean isOnEdge() {
        return onEdge;
    }

    public byte getContent() {
        return content;
    }

    //#endregion

    /**
     * Updates the content of the square.
     *
     * @param content New content to set (0 = Empty, one for each of these in the following order: P, N, B, R, Q, K; +6 for black pieces)
     */
    public void setContent(byte content) {
        this.content = content;
        // Every value besides 0 results in a 1
        hasPiece = (byte) ((content != 0) ? ((content < 7) ? 1 : 2) : 0);
        isWhite = (byte) ((content <= 6 && content != 0) ? 1 : 0); // White pieces are 1-6; black are 7-12
    }

    /**
     * Constructs a new Square object.
     *
     * @param x       X-coordinate of the square (0-7)
     * @param y       Y-coordinate of the square (0-7)
     * @param content Initial content of the square
     */
    public Square(byte x, byte y, byte content) {
        this.x = x;
        this.y = y;
        setContent(content);

        // Determine edge positions
        this.onEdgeX = (x == 0) | (x == 7);
        this.onEdgeY = (y == 0) | (y == 7);
        onEdge = onEdgeX | onEdgeY;
    }

    /**
     * @return a string representation of the square's state.
     */
    @Override
    public String toString() {
        String position = String.format("Square (%d, %d)", x, y);
        String pieceInfo = (hasPiece == 0)
                ? String.format(", contains a %s piece", (isWhite == 0) ? "white" : "black")
                : ", is empty";
        return position + pieceInfo;
    }
}
