import java.io.IOException;
import java.util.Scanner;

public class MainGUI {
    private static Position mainPosition;
    private static EngineAlg engine;

    public static void main(String[] args) throws IOException {
        // loadConfig();
        mainPosition = new Position();
        engine = new EngineAlg();

        Main.initializeHistoryTable();

        Scanner scanner = new Scanner(System.in);

        // Set up the initial board
        mainPosition.setupInitialBoard();
    }
}
