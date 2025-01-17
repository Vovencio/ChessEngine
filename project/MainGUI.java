import java.io.IOException;

import com.raylib.Raylib.Texture;

//import java.util.Scanner;
import static com.raylib.Colors.*;
import static com.raylib.Raylib.*;

public class MainGUI {
    private static Position mainPosition;
    private static EngineAlg engine;
    
    private static Texture boardTex;


    public static void main(String[] args) throws IOException {
        // loadConfig();
        
        SetConfigFlags(FLAG_WINDOW_RESIZABLE); 
        InitWindow(800, 450, "Demo");
        SetTargetFPS(60);

        loadTextures();

        mainPosition = new Position();
        engine = new EngineAlg();

        Main.initializeHistoryTable();

        //Scanner scanner = new Scanner(System.in);

        // Set up the initial board
        mainPosition.setupInitialBoard();

        System.out.println(mainPosition.getSquare((byte)0,(byte)1).getContent());
        System.out.println(mainPosition.getPossibleMovesSquare((byte)0, (byte)1, false));
        
        while (!WindowShouldClose()) {
            
            BeginDrawing();
            ClearBackground(RAYWHITE);

            DrawTexture(boardTex, GetScreenWidth()/2-boardTex.width()/2, GetScreenHeight()/2-boardTex.height()/2, RAYWHITE);
            DrawText("Hello world", 190, 200, 20, VIOLET);
            
            EndDrawing();
        }
        CloseWindow();

    }

    private static void loadTextures(){
        boardTex = LoadTexture("D:\\Personal\\ProgramingProjects\\JavaProjects\\ChessEngine\\ChessEngine\\project\\images\\board.png");
    }

    private byte[][] getBoard(){
        byte[][] board = new byte[8][8];

        for (int i = 0; i<8 ; i++) {
            for(int j = 0; j<8 ; j++){
                board[j][i] = mainPosition.getSquare((byte)i,(byte)j).getContent();
            }
        }

        return board;
    }
}
