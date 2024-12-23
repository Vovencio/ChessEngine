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

    /**
     * Constructs a Move object with the given positions.
     *
     * @param fromPositionX The X position of the piece.
     * @param fromPositionY The Y position of the piece.
     * @param toPositionX   The X position where the piece should go.
     * @param toPositionY   The Y position where the piece should go.
     */
    public Move(byte fromPositionX, byte fromPositionY, byte toPositionX, byte toPositionY) {
        this.fromPositionX = fromPositionX;
        this.fromPositionY = fromPositionY;
        this.toPositionX = toPositionX;
        this.toPositionY = toPositionY;
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
}
