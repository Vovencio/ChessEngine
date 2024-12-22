/**
 * Represents a chess move without move validation.
 * @author Vovencio
 * @version 12/22/24
 *
 * @param fromPositionY The position of the piece
 * @param toPositionY   The position, where the piece should go
 */
public record Move(byte fromPositionX, byte fromPositionY, byte toPositionX, byte toPositionY) {

}
