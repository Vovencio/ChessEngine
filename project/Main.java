import sum.kern.*;
import java.util.List;
import java.util.ArrayList;
import java.lang.Math;;
/**
 * @author Vladimir
 * @version 12/18/2024
 */
public class Main
{
    //#region Attributes, Constructor and Getters

    // Screen size
    private int size = 900;

    // Animation Duration
    double animationTimer = 0;
    double animationDuration = 500;
    double animationProgress = 0;

    // Used for Rendering
    private Bildschirm meinBildschirm;
    private Buntstift meinStift;

    // Used for DeltaTime
    private double deltaTime = 0;
    private long oldTime = System.nanoTime();

    // Used for Input
    private Tastatur meinTastatur;
    private Keyboard meinKeyboard;
    private Maus meinMaus;

    // Constructor
    public Main()
    {
        oldTime = System.nanoTime();
        meinBildschirm = new Bildschirm(size, size, true);
        meinStift = new Buntstift();
        meinMaus = new Maus();
        meinStift.normal();
        meinStift.runter();
        meinStift.setzeFarbe(Farbe.SCHWARZ);
        meinTastatur = new Tastatur();
        meinKeyboard = new Keyboard(meinTastatur);

        int status = main();
        System.out.println(exitCodeString(status));
        System.exit(status);
    }

    public Bildschirm getBildschirm(){
        return meinBildschirm;
    }

    public Stift getStift(){
        return meinStift;
    }
    public Tastatur getTastatur(){
        return meinTastatur;
    }
    //#endregion

    /**
 * Handle input for a keypress.
 * 0 = Normal Function.
 * 1 = Keyboard exit.
 * 2 = Unknown Key.
 */
    public int handleInput(Character input){
        switch (input) {
            case 'ȏ':
                return 1;
        
            default:
                return 2;
        }
    }

    /**
 * Render Everything.
 */
    public void render(){
        int size = 60; // Size of each square on the chessboard
        int boardSize = 8; // 8x8 chessboard

        // Loop through each row and column to draw the squares
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                int x = col * size; // X position of the square
                int y = row * size; // Y position of the square
                
                // Alternate between the two square drawing methods
                if ((row + col) % 2 == 0) {
                    drawSquare(x, y, size); // Draw white square
                } else {
                    drawSquareAlt(x, y, size); // Draw black square
                }
            }
        }
    }

    /**
 * Frame update and renderer.
 * @return Error code or 0 for normal function.
 */
    public int update(){
        //#region Deltatime update
        deltaTime = (System.nanoTime() - oldTime) / 1_000_000;
        oldTime = System.nanoTime();
        //#endregion

        // Handle Input
        for (Character item: meinKeyboard.update()){

            if (handleInput(item) == 1){
                return 1;
            }
        }

        //#region Update everything
        animationTimer += deltaTime;
        if (animationTimer > animationDuration) {animationTimer = 0;}
        animationProgress = animationTimer / animationDuration;
        System.out.println("" + deltaTime);
        //#endregion

        //Render everything
        render();
        meinBildschirm.zeichneDich();
        
        return 0;
    }

    /**
 * Main method.
 */
    public int main(){
        // Error code handling.
        int val = 0;
        while (val == 0){
            val = update();
        }
        return val;
    }

    /**
 * @return Error code for input.
 */
    String exitCodeString(int err){
        switch (err) {
            case 0:
                return "Error code 0. Something is very wrong.";
            case 1:
                return "Normal function: Keyboard exit.";
            default:
                return "Unknown error! Error code: " + err;
        }
    }
    
    void drawSquare(int x, int y, int size){
        meinStift.hoch();
        meinStift.bewegeBis(x, y);
        double mx = meinMaus.hPosition(); double my = meinMaus.vPosition();
        
        for (int dx = x; dx <= x + size; dx++){
            int val = clamp((int)Math.round(Math.sin(animationProgress*Math.PI)*255), 0, 255);
            meinStift.setzeFarbe(Farbe.rgb(val, val, val));
            meinStift.hoch();
            meinStift.bewegeBis(dx, y);
            meinStift.runter();
            meinStift.bewegeBis(dx, y + size);
        }
    }
    
    void drawSquareAlt(int x, int y, int size){
        meinStift.hoch();
        meinStift.bewegeBis(x, y);
        double mx = meinMaus.hPosition(); double my = meinMaus.vPosition();
        
        for (int dx = x; dx <= x + size; dx++){
            int val = clamp((int)Math.round(Math.sin((animationProgress+0.5)*Math.PI)*255), 0, 255);
            meinStift.setzeFarbe(Farbe.rgb(val, val, val));
            meinStift.hoch();
            meinStift.bewegeBis(dx, y);
            meinStift.runter();
            meinStift.bewegeBis(dx, y + size);
        }
    }
    
    public int clamp(int value, int min, int max) {
        if (value < min) {
            return min;  // Return the minimum value if the input is less than min
        } else if (value > max) {
            return max;  // Return the maximum value if the input is greater than max
        } else {
            return value;  // Return the original value if it's within the range
        }
    }
}