/**
 * Represents a generic chess move without any validation.
 * @author Vovencio
 * @version 12/23/24
 */

public class CastleMove extends Move{
    /**
     * Constructs a Move object with the given positions.
     *
     * @param fromPositionX The X position of the piece.
     * @param fromPositionY The Y position of the piece.
     * @param toPositionX   The X position where the piece should go.
     * @param toPositionY   The Y position where the piece should go.
     * @param position  The position.
     */

    public CastleMove(byte fromPositionX, byte fromPositionY, byte toPositionX, byte toPositionY, Position position) {
        super(fromPositionX, fromPositionY, toPositionX, toPositionY,
                position.getSquare(fromPositionX, fromPositionY).getContent(),
                position.getSquare(toPositionX, toPositionY).getContent(), position.isActiveWhite(),
                position.getHalfMoveClock(), position.getMoveCounter(), position.getEnPassant(),
                position.isCanWhiteCastleQueen(), position.isCanWhiteCastleKing(), position.isCanBlackCastleQueen(),
                position.isCanBlackCastleKing());
    }

    @Override
    public void Play(Position position) {
        // Rook value for the board
        byte rook = (byte) ((getFromPiece() == 6) ? 4 : 10);

        // Place the king correctly
        position.setSquareContent(getToPositionX(), getToPositionY(), getFromPiece());
        position.setSquareContent(getFromPositionX(), getFromPositionY(), (byte) 0);

        // Queen-side Castling
        if (getToPositionX() == 2) {
            position.setSquareContent((byte) 0, getFromPositionY(), (byte) 0);
            position.setSquareContent((byte) 3, getFromPositionY(), rook);
        } // King-side Castling
        else {
            position.setSquareContent((byte) 7, getFromPositionY(), (byte) 0);
            position.setSquareContent((byte) 5, getFromPositionY(), rook);
        }

        // The player should not be able to castle again.
        if (getFromPiece() == 6) {position.setCanWhiteCastleKing(false); position.setCanWhiteCastleQueen(false);}
        else {position.setCanBlackCastleKing(false); position.setCanBlackCastleQueen(false);}
    }

    @Override
    public void Reverse(Position position) {
        // Rook value for the board
        byte rook = (byte) ((getFromPiece() == 6) ? 4 : 10);

        // Reset King
        position.setSquareContent(getToPositionX(), getToPositionY(), getToPiece());
        position.setSquareContent(getFromPositionX(), getFromPositionY(), getFromPiece());

        // Queen-side Castling
        if (getToPositionX() == 2) {
            position.setSquareContent((byte) 3, getFromPositionY(), (byte) 0);
            position.setSquareContent((byte) 0, getFromPositionY(), rook);
        } // King-side Castling
        else {
            position.setSquareContent((byte) 5, getFromPositionY(), (byte) 0);
            position.setSquareContent((byte) 7, getFromPositionY(), rook);
        }

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
        String moveString = "O-O";

        // Queen-side castling
        if (getToPositionX() == 2) moveString = "O-O-O";

        if (getFromPiece() == 12) moveString = moveString.toLowerCase();

        return moveString;
    }
}
