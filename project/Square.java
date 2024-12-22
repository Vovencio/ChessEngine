/**
 * Represents a single tile on an 8x8 chessboard.
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
    private boolean hasPiece;

    // Indicates if the tile's piece is part of the white team (true) or black team (false)
    private boolean isWhite;

    // Edge indicators
    private final boolean onEdgeX;
    private final boolean onEdgeY;

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
     * @return whether this square contains a piece.
     */
    public boolean hasPiece() {
        return hasPiece;
    }

    /**
     * @return whether the piece on this square belongs to the white team.
     */
    public boolean isWhiteTeam() {
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

    public byte getContent() {
        return content;
    }

    //#endregion

    /**
     * Updates the content of the square.
     *
     * @param content New content to set (0 = Empty, 1 = Pawn, ..., 6 = King; +6 for black pieces)
     */
    public void setContent(byte content) {
        hasPiece = content != 0;
        isWhite = content <= 6 && content != 0; // White pieces are 1-6; black are 7-12
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
    }

    /**
     * @return a string representation of the square's state.
     */
    @Override
    public String toString() {
        String position = String.format("Square (%d, %d)", x, y);
        String pieceInfo = hasPiece
                ? String.format(", contains a %s piece", isWhite ? "white" : "black")
                : ", is empty";
        return position + pieceInfo;
    }
}
