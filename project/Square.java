/**
 * One tile of the 8x8 chess board
 * 
 * @author Vovencio
 * @version 12/20/24
 */
public class Square
{
    //#region Attribute
    // Coordinates
    private final byte myX;
    private final byte myY;
    public byte getX(){
        return myX;
    }
    public byte getY(){
        return myY;
    }

    public boolean isPiece;

    // These variables show, whether the tile is on an edge.
    public final boolean onEdgeX;
    public final boolean onEdgeY;

    // Defines piece team
    public boolean isWhite;
    //#endregion

    /**
     * Changes the tile content.
     * @param  content New content
     * @implNote 0 = Nothing, 1 = Pawn, 2 = Knight, 3 = Bishop, 4 = Rook, 5 = Queen, 6 = King. +6 when black
     */
    public void setContent(byte content){
        // Content of the tile
        isPiece = content != 0;
        isWhite = content > 6;
    }

    public Square(byte x, byte y, byte content)
    {
        myX = x; myY = y;
        setContent(content);

        // Calculating onEdge
        onEdgeX = (myX == 0) | (myX == 7);
        onEdgeY = (myY == 0) | (myY == 7);
    }
}