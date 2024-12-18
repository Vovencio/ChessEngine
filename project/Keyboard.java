import sum.kern.*;
import java.util.List;
import java.util.ArrayList;

/**
 * @author Vladimir
 * @version 12/18/2024
*/
public class Keyboard
{
    private Tastatur meinTastatur;
    private List<Character> chars = new ArrayList<>();

    public Keyboard(Tastatur tastatur){
        meinTastatur = tastatur;
    }

    /**
 * @return Array of all pressed keys in the frame.
 */
    public List<Character> update(){
        chars.clear();

        while (meinTastatur.wurdeGedrueckt()) {
            Character symbol = meinTastatur.zeichen();
            if (!chars.contains(symbol)) chars.add(symbol);
            meinTastatur.weiter();
        }
        return chars;
    }
}
