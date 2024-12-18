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
    double animationDuration = 5;

    // Used for Rendering
    private Bildschirm meinBildschirm;
    private Buntstift meinStift;

    // Used for DeltaTime
    private double deltaTime = 0;
    private long oldTime = System.nanoTime();

    // Used for Input
    private Tastatur meinTastatur;
    private Keyboard meinKeyboard;

    // Constructor
    public Main()
    {
        meinBildschirm = new Bildschirm(size, size, true);
        meinStift = new Buntstift();
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
        drawSquare(200, 200, 100);
    }

    /**
 * Frame update and renderer.
 * @return Error code or 0 for normal function.
 */
    public int update(){
        //#region Deltatime update
        deltaTime = (System.nanoTime() - oldTime) / 1_000_000_000;
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
        if (animationTimer > animationDuration) animationTimer = 0;
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
        for (int dx = x; dx <= x + size; dx++){
            for (int dy = y; dy <= y + size; dy++){
                int val = (int) Math.round(((double)(dx - x + dy - y) / (double)(2 * size)) * 255);
                meinStift.setzeFarbe(Farbe.rgb(val, val, val));
                meinStift.hoch();
                meinStift.bewegeBis(dx, dy);
                meinStift.runter();
                meinStift.bewegeBis(dx, dy);
            }
        }
    }
}