/**
 * Die Klasse Square beschreibt das Verhalten eines Kastchens auf dem 8x8 Schachbrett
 * 
 * @author Vovencio
 * @version 12/20/24
 */
public class Square
{
    //#region Attribute
    // Koordinaten
    private byte myIndex;
    private byte myX;
    private byte myY;
    public byte getX(){
        return myX;
    }
    public byte getY(){
        return myY;
    }

    // Figur
    private byte myFigur;
    public boolean istFigur;

    // AmRand
    public boolean amRandX;
    public boolean amRandY;

    // Team
    public boolean myTeam;
    //#endregion

    /**
     * Ersetzt die Figur im Feld.
     * @param  figur Die Figur, die da sein soll
     * @implNote 0 = Bauer, 1 = Springer, 2 = Laufer, 3 = Turm, 4 = Dame, 5 = Konig. +6 wenn weiss.
     */
    public void setFigur(byte figur){
        myFigur = figur;
        istFigur = myFigur != 0;
        myTeam = figur > 5;
    }

    public Square(byte x, byte y, byte content)
    {
        myIndex = (byte) (x + y * 8);
        myX = x; myY = y;
        setFigur(content);

        // Wenn die Figur am Rand ist, ohne Branching
        amRandX = (myX == 0) | (myX == 7);
        amRandY = (myY == 0) | (myY == 7);
    }
}