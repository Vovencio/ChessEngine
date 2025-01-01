/**
 * Abstract class, which represents a move without any validation and so on.
 * @author Vovencio
 * @version 12/23/24
 */
public abstract class Move {
    private final byte fromPositionX;
    private final byte fromPositionY;
    private final byte toPositionX;
    private final byte toPositionY;
    private final byte fromPiece;
    private final byte toPiece;

    // Metadata for proper function
    protected final boolean isActiveWhite;
    protected final byte halfMoveClock;
    protected final int moveCounter;
    protected final byte[] enPassant;
    protected final boolean canWhiteCastleQueen;
    protected final boolean canWhiteCastleKing;
    protected final boolean canBlackCastleQueen;
    protected final boolean canBlackCastleKing;

    /**
     * Constructs a Move object with the given positions.
     *
     * @param fromPositionX The X position of the piece.
     * @param fromPositionY The Y position of the piece.
     * @param toPositionX   The X position where the piece should go.
     * @param toPositionY   The Y position where the piece should go.
     */
    public Move(byte fromPositionX, byte fromPositionY, byte toPositionX, byte toPositionY, byte fromPiece, byte toPiece, boolean isActiveWhite, byte halfMoveClock, int moveCounter, byte[] enPassant, boolean canWhiteCastleQueen, boolean canWhiteCastleKing, boolean canBlackCastleQueen, boolean canBlackCastleKing) {
        this.fromPositionX = fromPositionX;
        this.fromPositionY = fromPositionY;
        this.toPositionX = toPositionX;
        this.toPositionY = toPositionY;
        this.fromPiece = fromPiece;
        this.toPiece = toPiece;
        this.isActiveWhite = isActiveWhite;
        this.halfMoveClock = halfMoveClock;
        this.moveCounter = moveCounter;
        this.enPassant = enPassant;
        this.canWhiteCastleQueen = canWhiteCastleQueen;
        this.canWhiteCastleKing = canWhiteCastleKing;
        this.canBlackCastleQueen = canBlackCastleQueen;
        this.canBlackCastleKing = canBlackCastleKing;
    }

    public byte getToPiece(){
        return toPiece;
    }

    public byte getFromPiece(){
        return fromPiece;
    }

    // Getters for the positions (optional, depending on use case)
    public byte getFromPositionX() {
        return fromPositionX;
    }

    public byte getFromPositionY() {
        return fromPositionY;
    }

    public byte getToPositionX() {
        return toPositionX;
    }

    public byte getToPositionY() {
        return toPositionY;
    }

    /**
     * Abstract method to play the move. Must be implemented by subclasses.
     */
    public abstract void Play(Position position);

    /**
     * Abstract method to play the move. Must be implemented by subclasses.
     */
    public abstract void Reverse(Position position);

    @Override
    public abstract String toString();

    public static char pieceSymbol(byte piece) {
        return switch (piece) {
            case 1 -> 'P'; // White Pawn
            case 2 -> 'N'; // White Knight
            case 3 -> 'B'; // White Bishop
            case 4 -> 'R'; // White Rook
            case 5 -> 'Q'; // White Queen
            case 6 -> 'K'; // White King
            case 7 -> 'p'; // Black Pawn
            case 8 -> 'n'; // Black Knight
            case 9 -> 'b'; // Black Bishop
            case 10 -> 'r'; // Black Rook
            case 11 -> 'q'; // Black Queen
            case 12 -> 'k'; // Black King
            default -> ' '; // Default for empty
        };
    }

}
