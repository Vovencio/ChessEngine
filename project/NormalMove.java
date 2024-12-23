/**
 * Represents a generic chess move without any validation.
 * @author Vovencio
 * @version 12/23/24
 */

public class NormalMove extends Move{
    /**
     * Constructs a Move object with the given positions.
     *
     * @param fromPositionX The X position of the piece.
     * @param fromPositionY The Y position of the piece.
     * @param toPositionX   The X position where the piece should go.
     * @param toPositionY   The Y position where the piece should go.
     */
    public NormalMove(byte fromPositionX, byte fromPositionY, byte toPositionX, byte toPositionY) {
        super(fromPositionX, fromPositionY, toPositionX, toPositionY);
    }

    @Override
    public void Play(Position position) {
        position.setSquareContent(getFromPositionX(), getToPositionY(), position.getSquare(getToPositionX(), getFromPositionY()).getContent());
        position.setSquareContent(getFromPositionX(), getFromPositionY(), (byte) 0);
    }
}
