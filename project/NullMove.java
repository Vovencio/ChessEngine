/**
 * Represents a generic chess move without any validation.
 * @author Vovencio
 * @version 12/23/24
 */

public class NullMove extends Move{
    /**
     * Constructs a Move object with the given positions.
     *
     * @param fromPositionX The X position of the piece.
     * @param fromPositionY The Y position of the piece.
     * @param toPositionX   The X position where the piece should go.
     * @param toPositionY   The Y position where the piece should go.
     * @param position  The position.
     */

    public NullMove(Position position) {
        super((byte) 0, (byte) 0, (byte) 0, (byte) 0,
                position.getSquare((byte) 0, (byte) 0).getContent(),
                position.getSquare((byte) 0, (byte) 0).getContent(), position.isActiveWhite(),
                position.getHalfMoveClock(), position.getMoveCounter(), position.getEnPassant(),
                position.isCanWhiteCastleQueen(), position.isCanWhiteCastleKing(), position.isCanBlackCastleQueen(),
                position.isCanBlackCastleKing());
    }

    @Override
    public void Play(Position position) {
    }

    @Override
    public void Reverse(Position position) {
        position.setActiveWhite(isActiveWhite);
        position.setHalfMoveClock(halfMoveClock);
        position.setMoveCounter(moveCounter);
        position.setEnPassant(enPassant);
        position.setCanWhiteCastleQueen(canWhiteCastleQueen);
        position.setCanWhiteCastleKing(canWhiteCastleKing);
        position.setCanBlackCastleQueen(canBlackCastleQueen);
        position.setCanBlackCastleKing(canBlackCastleKing);
    }

    @Override
    public String toString(){
        return "NullMove";
    }
}
