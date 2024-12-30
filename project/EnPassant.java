/**
 * Represents a generic chess move without any validation.
 * @author Vovencio
 * @version 12/23/24
 */

public class EnPassant extends Move{
    /**
     * Constructs a Move object with the given positions.
     *
     * @param fromPositionX The X position of the piece.
     * @param fromPositionY The Y position of the piece.
     * @param toPositionX   The X position where the piece should go.
     * @param toPositionY   The Y position where the piece should go.
     * @param position  The position.
     */

    public EnPassant(byte fromPositionX, byte fromPositionY, byte toPositionX, byte toPositionY, Position position) {
        super(fromPositionX, fromPositionY, toPositionX, toPositionY,
                position.getSquare(fromPositionX, fromPositionY).getContent(),
                position.getSquare(toPositionX, toPositionY).getContent(), position.isActiveWhite(),
                position.getHalfMoveClock(), position.getMoveCounter(), position.getEnPassant(),
                position.isCanWhiteCastleQueen(), position.isCanWhiteCastleKing(), position.isCanBlackCastleQueen(),
                position.isCanBlackCastleKing());
    }

    @Override
    public void Play(Position position) {
        position.setSquareContent(getToPositionX(), getToPositionY(), getFromPiece());
        position.setSquareContent(getFromPositionX(), getFromPositionY(), (byte) 0);
        position.setSquareContent(getToPositionX(), getFromPositionY(), (byte) 0);
    }

    @Override
    public void Reverse(Position position) {
        position.setSquareContent(getToPositionX(), getToPositionY(), getToPiece());
        position.setSquareContent(getFromPositionX(), getFromPositionY(), getFromPiece());

        if (getFromPiece() == 1) position.setSquareContent(getToPositionX(), getFromPositionY(), (byte) 7);
        else position.setSquareContent(getToPositionX(), getFromPositionY(), (byte) 1);
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
        char startFile = (char) (getFromPositionX() + 'a');
        char endFile = (char) (getToPositionX() + 'a');
        int startRank = getFromPositionY() + 1;
        int endRank = getToPositionY() + 1;

        return "enp-" + pieceSymbol(getFromPiece()) + startFile + startRank + endFile + endRank;
    }
}
