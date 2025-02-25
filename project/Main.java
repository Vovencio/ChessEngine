import java.util.Scanner;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import org.json.JSONArray;

import java.io.*;
// import java.util.*;
import org.json.JSONObject;
import java.util.Properties;
import java.util.Arrays;

/**
 * Main class, where everything is sewed together.
 *
 * @author Vovencio
 * @version 01/24/2024
 */
public class Main {

    private static Position mainPosition;
    private static EngineAlg engine;
    private static double[][][] evalTable;

    static String evalsFilePath;
    static String historyFilePath;

    public static void main(String[] args) throws IOException {
        //JavaToPython.setUp();

        // loadConfig();
        mainPosition = new Position();
        engine = new EngineAlg();

        initializeHistoryTable();

        Scanner scanner = new Scanner(System.in);

        // Set up the initial board
        mainPosition.setupInitialBoard();

        printLines();
        System.out.println("Setup Complete!");
        printLines();

        System.out.println("╋╋╋┏┓   Cherry Engine V. 0.1");
        System.out.println("╋╋╋┃┃   Made by Vladimir K.");
        System.out.println("┏━━┫┗━┳━━┳━┳━┳┓╋┏┓╋╋┏━━┳━┓┏━━┳┳━┓┏━━┓");
        System.out.println("┃┏━┫┏┓┃┃━┫┏┫┏┫┃╋┃┣━━┫┃━┫┏┓┫┏┓┣┫┏┓┫┃━┫");
        System.out.println("┃┗━┫┃┃┃┃━┫┃┃┃┃┗━┛┣━━┫┃━┫┃┃┃┗┛┃┃┃┃┃┃━┫");
        System.out.println("┗━━┻┛┗┻━━┻┛┗┛┗━┓┏┛╋╋┗━━┻┛┗┻━┓┣┻┛┗┻━━┛");
        System.out.println("╋╋╋╋╋╋╋╋╋╋╋╋╋┏━┛┃╋╋╋╋╋╋╋╋╋┏━┛┃");
        System.out.println("╋╋╋╋╋╋╋╋╋╋╋╋╋┗━━┛╋╋╋╋╋╋╋╋╋┗━━┛");
        printLines();
        System.out.println("Welcome to my Engine!");

        while (true) {
            printLines();
            System.out.println("Enter a command, please.");
            String input = scanner.nextLine();
            printLines();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Exiting the game. Have a great day, sweetheart!");
                saveHistoryTable();
                break;
            }
            if (input.equalsIgnoreCase("help")) {
                showHelp();
            } else if (input.startsWith("execute ")) {
                handleExecuteCommand(input);
            } else {
                // Handle individual commands directly in the main loop
                handleCommand(input);
            }
        }
    }

    public static void handleExit(){
        saveHistoryTable();
    }

    public static void loadConfig() {
        String configFilePath = "config.properties";

        // Try to load the config file
        Properties config = new Properties();
        try {
            File configFile = new File(configFilePath);
            if (configFile.exists()) {
                try (FileReader reader = new FileReader(configFile)) {
                    config.load(reader);
                    evalsFilePath = config.getProperty("evalsFilePath");
                    historyFilePath = config.getProperty("historyFilePath");
                }
            } else {
                evalsFilePath = null;
                historyFilePath = null;
            }
        } catch (IOException e) {
            System.out.println("Error reading config file: " + e.getMessage());
            evalsFilePath = null;
            historyFilePath = null;
        }

        // Load evalTable
        evalsFilePath = loadFilePath(evalsFilePath, "evalsFilePath", config, configFilePath, "JSONL file for evalTable");
        loadEvalTable();

        // Load historyTable
        historyFilePath = loadFilePath(historyFilePath, "historyFilePath", config, configFilePath, "JSONL file for historyTable");
        loadHistoryTable();
    }

    private static String loadFilePath(String filePath, String propertyKey, Properties config, String configFilePath, String prompt) {
        Scanner scanner = new Scanner(System.in);
        File file = (filePath != null) ? new File(filePath) : null;

        if (file == null || !file.exists()) {
            System.out.println("The " + prompt + " could not be found.");
            System.out.print("Please enter the path: ");
            filePath = scanner.nextLine();
            file = new File(filePath);

            try (FileWriter writer = new FileWriter(configFilePath)) {
                config.setProperty(propertyKey, filePath);
                config.store(writer, "Configuration File for JSONL Path");
                System.out.println("Config file updated with new path!");
            } catch (IOException e) {
                System.out.println("Failed to save config file: " + e.getMessage());
            }
        }

        return filePath;
    }

    private static void loadEvalTable() {
        File evalsFile = new File(evalsFilePath);

        if (evalsFile.exists()) {
            System.out.println("Reading JSONL file from: " + evalsFilePath);

            try (BufferedReader reader = new BufferedReader(new FileReader(evalsFile))) {
                int layers = 13;
                int rows = 8;
                int cols = 8;
                evalTable = new double[layers][rows][cols];

                String line;
                int layerIndex = 0;

                while ((line = reader.readLine()) != null && layerIndex < layers) {
                    JSONArray jsonLayer = new JSONArray(line);

                    for (int i = 0; i < jsonLayer.length(); i++) {
                        JSONArray row = jsonLayer.getJSONArray(i);
                        for (int j = 0; j < row.length(); j++) {
                            evalTable[layerIndex][i][j] = row.getDouble(j);
                        }
                    }

                    layerIndex++;
                }

                System.out.println("Loaded evalTable successfully!");
            } catch (IOException e) {
                System.out.println("Failed to read the JSONL file for evalTable: " + e.getMessage());
            }
        } else {
            System.out.println("The JSONL file for evalTable could not be found at the given path.");
        }
    }

    private static void loadHistoryTable() {
        File historyFile = new File(historyFilePath);

        if (historyFile.exists()) {
            System.out.println("Reading historyTable from: " + historyFilePath);

            try (BufferedReader reader = new BufferedReader(new FileReader(historyFile))) {
                int layers = 12;
                int rows = 8;
                int cols = 8;
                Branch.historyTable = new int[layers][rows][cols];

                String line;
                int layerIndex = 0;

                while ((line = reader.readLine()) != null && layerIndex < layers) {
                    JSONArray jsonLayer = new JSONArray(line);

                    for (int i = 0; i < jsonLayer.length(); i++) {
                        JSONArray row = jsonLayer.getJSONArray(i);
                        for (int j = 0; j < row.length(); j++) {
                            Branch.historyTable[layerIndex][i][j] = row.getInt(j);
                        }
                    }

                    layerIndex++;
                }

                if (layerIndex == 11){
                    System.out.println("Loaded historyTable successfully!");
                }
                else {
                    System.out.println("HistoryTable seems to be corrupted. Initializing with minimum values.");
                    initializeHistoryTable();
                }
            } catch (IOException e) {
                System.out.println("Failed to read the JSONL file for historyTable: " + e.getMessage());
            }
        } else {
            System.out.println("The JSONL file for historyTable could not be found. Initializing with minimum values.");
            initializeHistoryTable();
        }
    }

    public static void initializeHistoryTable() {
        Branch.historyTable = new int[12][8][8];
        Branch.takeTable = new int[12][8][8];
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 8; j++) {
                Arrays.fill(Branch.historyTable[i][j], 0);
                Arrays.fill(Branch.takeTable[i][j], 0);
            }
        }
        //saveHistoryTable();
    }

    public static void saveHistoryTable() {
        File historyFile = new File(historyFilePath);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(historyFile))) {
            for (int[][] layer : Branch.historyTable) {
                JSONArray jsonLayer = new JSONArray();

                for (int[] row : layer) {
                    jsonLayer.put(new JSONArray(row));
                }

                writer.write(jsonLayer.toString());
                writer.newLine();
            }

            System.out.println("historyTable saved successfully to: " + historyFilePath);
        } catch (IOException e) {
            System.out.println("Failed to save historyTable: " + e.getMessage());
        }
    }

    public static void printLines() {
        System.out.println("-------------------------------------");
    }

    // Handle the 'load <fen>' command
    private static void handleLoadCommand(String input) {
        String fen = input.substring(5).trim(); // Extract the FEN string after "load "
        try {
            mainPosition.loadFEN(fen);
            System.out.println("FEN position loaded successfully.");
        } catch (Exception e) {
            System.out.println("Error loading FEN position. Please ensure it is valid.");
            System.out.println(e.getMessage());
        }
    }

    // Handle the 'load <fen>' command
    private static void handleFENCommand() {
        System.out.println("Here is the FEN Position:");
        System.out.println(mainPosition.generateFEN());
    }

    // Handle the 'see xy' command
    private static void handleSeeCommand(String input) {
        if (input.length() != 6) {
            System.out.println("Invalid command! Use 'see xy' where xy are board coordinates.");
            return;
        }

        try {
            // Parse the coordinates
            byte x = (byte) (input.charAt(4) - 'a'); // 'a'-'h' maps to 0-7
            byte y = (byte) (Character.getNumericValue(input.charAt(5)) - 1); // '1'-'8' maps to 0-7

            // Validate coordinates
            if (isInvalidCoordinate(x, y)) {
                System.out.println("Invalid coordinates! Must be within a1 to h8.");
                return;
            }

            // Display possible moves
            System.out.println("Possible moves for the piece at " + input.substring(4) + ":");
            mainPosition.drawPossibleMoves(x, y);

        } catch (Exception e) {
            System.out.println("Error parsing command. Please use 'see xy' where xy are valid board coordinates.");
            System.out.println(e.toString());
        }
    }

    // Handle the 'play <move_number>' command
    private static void handlePlayCommand(String input) {
        try {
            List<Move> moves = mainPosition.getPossibleMovesBoard(mainPosition.isActiveWhite());
            String moveInput = input.substring(5).trim(); // Extract input after "play "

            // Try to parse as a move index first
            try {
                int moveIndex = Integer.parseInt(moveInput);

                if (moveIndex < 1 || moveIndex > moves.size()) {
                    System.out.println("Invalid move number! Please choose a valid move from the list.");
                    return;
                }

                Move selectedMove = moves.get(moveIndex - 1); // Convert 1-based to 0-based index
                mainPosition.playMove(selectedMove);

                System.out.println("Move played: " + selectedMove.toString());
                return;
            } catch (NumberFormatException ignored) {
                // Not a number, fall through to string comparison logic
            }

            // Check if the input matches any move's .toString()
            for (Move move : moves) {
                if (move.toString().equalsIgnoreCase(moveInput)) {
                    mainPosition.playMove(move);

                    System.out.println("Move played: " + move.toString());
                    return;
                }
            }

            // If no match was found
            System.out.println("Invalid move! Please provide a valid move number or notation matching a possible move.");

        } catch (Exception e) {
            System.out.println("Error executing move. " + e.getMessage());
        }
    }

    // Handle the 'moves' command
    private static void handleMovesCommand() {
        List<Move> moves = mainPosition.getPossibleMovesBoard(mainPosition.isActiveWhite());

        if (moves.isEmpty()) {
            System.out.println("No possible moves available, sweetheart.");
            return;
        }

        System.out.println("Here are the possible moves:");
        for (int i = 0; i < moves.size(); i++) {
            String moveString = (i + 1) < 10 ? " " + (i + 1) : String.valueOf(i + 1); // Format 1-9 with leading space
            String formattedMove = moves.get(i).toString();

            System.out.printf("%-5s %-25s", moveString + ".", formattedMove);

            if ((i + 1) % 3 == 0) {
                System.out.println(); // New line after every third move
            }
        }

        // Ensure a new line if the last row is incomplete
        if (moves.size() % 3 != 0) {
            System.out.println();
        }
    }

    // Helper to validate coordinates
    private static boolean isInvalidCoordinate(byte x, byte y) {
        return x < 0 || x > 7 || y < 0 || y > 7;
    }

    public static void generateDataSet(String inputFilePath, String outputFilePath) {

        System.out.println("Input file path: " + inputFilePath);
        System.out.println("Output file path: " + outputFilePath);


        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {

            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    // Parse the JSON line to extract FEN and CP
                    JSONObject jsonObject = new JSONObject(line);
                    String fen = jsonObject.getString("fen") + " 1 0";
                    int cp = jsonObject.getInt("cp");

                    // Load the FEN string and generate training data
                    mainPosition.loadFEN(fen);
                    int[] trainingData = mainPosition.toTrainingData();

                    // Create the output JSON object
                    JSONObject outputJson = new JSONObject();
                    outputJson.put("data", trainingData);
                    outputJson.put("cp", cp * (mainPosition.isActiveWhite() ? 1 : -1));

                    // Write the output JSON to the file
                    writer.write(outputJson.toString());
                    writer.newLine();

                } catch (Exception e) {
                    System.err.println("Error processing line: " + line);
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading or writing files.");
        }
    }

    private static void handleGenerateDatasetCommand(String input) {
        // Remove the command prefix
        String paths = input.substring("dataset ".length()).trim();

        // Split the paths by space (assuming paths are separated by a space)
        String[] pathParts = paths.split(" ", 2);

        // Ensure there are exactly two parts
        if (pathParts.length != 2) {
            System.out.println("Error: Please provide exactly two paths for the command.");
            return;
        }

        String inputPath = pathParts[0];
        String outputPath = pathParts[1];

        // Call the method to generate the dataset
        generateDataSet(inputPath, outputPath);
        System.out.println("Generating Dataset...");
    }

    // Handle the 'engine' command (does nothing, just splits input)
    private static void handleEngineCommand(String input) {
        String[] parts = input.substring(7).split("\\s+");  // Strip the 'engine ' prefix and split by spaces

        if (parts.length == 0) {
            System.out.println("Engine command is empty after 'engine'.");
            return;
        }

        String subCommand = parts[0];

        switch (subCommand) {
            case "static":
                handleStaticCommand();
                break;

            case "best":
                handleBestCommand(parts);
                break;

            case "game":
                handleEngineGame();
                break;

            case "data":
                handleEngineDataCommand();
                break;

            default:
                System.out.println("Unknown engine subcommand: " + subCommand);
        }
    }

    private static void handleStaticCommand() {
        engine.enginePosition.loadFEN(mainPosition.generateFEN());
        double eval = engine.evalBoard();
        double qS = engine.qSearch(10, mainPosition);
        System.out.printf("The static function evaluates this position as %.3f%n", eval);
        System.out.printf("The qS function evaluates this position as %.3f%n", qS);
    }

    private static void handleBestCommand(String[] parts) {
        if (parts.length > 1) {
            try {
                int depth = Integer.parseInt(parts[1]);
                System.out.println("Best move command received with depth: " + depth);
                Branch best = engine.generateBestMove(depth, mainPosition);
                if (best != null) System.out.printf("Best move is " + best.getMove().toString() + ", eval: %.3f" + ".%n", best.getEvaluation());
            } catch (NumberFormatException e) {
                System.out.println("Invalid depth value: " + parts[1]);
            }
        } else {
            System.out.println("Best move command received with default depth (4).");
            Branch best = engine.generateBestMoveTime(40, mainPosition, 5000);
            if (best != null) System.out.printf("Best move at depth 4 is " + best.getMove().toString() + ", eval: %.3f" + ".%n", best.getEvaluation());
        }

        System.out.println(Branch.evaluationCount);
    }

    // Handle the 'execute' command to run multiple commands
    private static void handleExecuteCommand(String input) {
        String[] commands = input.substring(8).split(",\\s*");  // Strip the 'execute ' prefix and split by commas and optional spaces

        System.out.println("Executing commands...");
        for (String command : commands) {
            // Process each command as if it's entered by the user
            System.out.println("Executing: " + command);
            handleCommand(command); // Call the handleCommand method for each command
        }
    }

    private static void handleEngineGame() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Engine game started. Enter your moves in standard notation (e.g., e2e4).");
        System.out.println("Type 'exit' to quit the game.");
        boolean engineTurn = true;

        while (true) {
            if (engineTurn){
                // Engine's turn
                System.out.println("Engine is thinking...");
                Branch bestMove = engine.generateBestMoveTime(40, mainPosition, 5000);
                if (bestMove != null) {
                    Move engineMove = bestMove.getMove();
                    mainPosition.playMove(engineMove);
                    System.out.printf("Engine plays: %s (eval: %.3f)%n", engineMove, bestMove.getEvaluation());
                    engineTurn = false;
                } else {
                    System.out.println("Engine could not find a valid move. The game ends.");
                    break;
                }
            }
            else {
                try {
                    // Check if the current player (player or engine) has possible moves
                    List<Move> playerMoves = mainPosition.getPossibleMovesBoard(mainPosition.isActiveWhite());
                    if (playerMoves.isEmpty()) {
                        byte[] kingPos = mainPosition.isActiveWhite() ? mainPosition.getKingPosWhite() : mainPosition.getKingPosBlack();
                        if (mainPosition.isAttacked(kingPos[0], kingPos[1], mainPosition.isActiveWhite())){
                            System.out.println((mainPosition.isActiveWhite() ? "White" : "Black") + " is checkmated.");
                        } else {
                            System.out.println("Game over. It's a stalemate.");
                        }
                        break;
                    }
                    System.out.print("Your move: ");
                    String input = scanner.nextLine().trim();

                    if (input.equalsIgnoreCase("exit")) {
                        System.out.println("Exiting the engine game. Goodbye!");
                        break;
                    }

                    // Validate and play the player's move
                    boolean validMove = false;
                    for (Move move : playerMoves) {
                        if (move.toString().equalsIgnoreCase(input)) {
                            mainPosition.playMove(move);
                            System.out.println("You played: " + move);
                            engineTurn = true;
                            validMove = true;
                            break;
                        }
                    }

                    if (!validMove) {
                        System.out.println("Invalid move. Please try again. Type 'exit' to exit.");
                        continue;
                    }

                } catch (Exception e) {
                    System.out.println("An error occurred: " + e.getMessage());
                }
            }
        }
    }

    private static void handleTestMovesCommand(String input) {
        String filename = input.substring(11).trim(); // Extract the filename after "test-moves "

        try {
            Scanner fileScanner = new Scanner(new File(filename));
            int testCount = 0, passCount = 0;

            while (fileScanner.hasNextLine()) {
                String fen = fileScanner.nextLine().trim();
                if (!fileScanner.hasNextLine()) {
                    System.out.println("Malformed input: Missing expected move count after FEN: " + fen);
                    break;
                }

                int expectedMoveCount;
                try {
                    expectedMoveCount = Integer.parseInt(fileScanner.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("Malformed input: Expected move count is not a valid number.");
                    break;
                }

                try {
                    mainPosition.loadFEN(fen);
                    List<Move> possibleMoves = mainPosition.getPossibleMovesBoard(mainPosition.isActiveWhite());
                    testCount++;

                    if (possibleMoves.size() == expectedMoveCount) {
                        passCount++;
                    } else {
                        System.out.printf("Test %d failed: FEN [%s] Expected %d moves, got %d moves.%n",
                                testCount, fen, expectedMoveCount, possibleMoves.size());
                    }
                } catch (Exception e) {
                    System.out.println("Error processing FEN: " + fen);
                    System.out.println(e.getMessage());
                }
            }

            System.out.printf("Test Results: %d/%d passed.%n", passCount, testCount);
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        }
    }

    /*
    private static void generateWorthTables() {
        String filename = "C:\\Users\\vladi\\Downloads\\test-positions.txt"; // Extract the filename after "test-moves "

        try {
            Scanner fileScanner = new Scanner(new File(filename));

            while (fileScanner.hasNextLine()) {
                String fen = fileScanner.nextLine().trim();
                fileScanner.nextLine();

                try {
                    engine.enginePosition.loadFEN(fen);
                    engine.evalBoard();
                } catch (Exception e) {
                    System.out.println("Error processing FEN: " + fen);
                    System.out.println(e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        }

        for (int i = 0; i < 12; i++) {
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    if (engine.pieceUsedArray[i][x][y] == 0){
                        engine.pieceUsedArray[i][x][y] = 1;
                        engine.pieceWorthArray[i][x][y] = Engine.getBaseEval((byte) (i+1));
                        System.out.println("No value for " + i + ", at: " + x + ", " + y);
                    }
                    engine.processed[i][x][y] = (double) (engine.pieceWorthArray[i][x][y] / engine.pieceUsedArray[i][x][y]);
                }
            }
        }

        try (FileWriter writer = new FileWriter("C:\\Users\\vladi\\Downloads\\pieceEvals.jsonl")) {
            for (double[][] layer : engine.processed) {
                // Convert each 2D layer to a JSONArray
                JSONArray jsonLayer = new JSONArray(layer);
                writer.write(jsonLayer.toString() + "\n"); // Write it as a new line
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Saved!");
    }
    */

    private static void handleFlipCommand(){
        mainPosition = mainPosition.flipColor();
        System.out.println("Board Flipped!");
    }

    public static void printTrainingData(int[] data) {
        // Print the first 64 numbers in rows of 8
        System.out.println("Board Data:");
        for (int i = 0; i < 64; i++) {
            // Use String.format to align numbers in a 4-character wide column
            System.out.print(String.format("%4d", data[i]));
            if ((i + 1) % 8 == 0) {
                System.out.println(); // New line after every 8 numbers
            }
        }

        // Print the last 7 numbers (metadata)
        System.out.println("\nMetadata:");
        for (int i = 64; i < 70; i++) {
            System.out.print(String.format("%4d", data[i]));
        }
        System.out.println(); // New line after metadata
    }

    private static void handleEngineDataCommand(){
        int[] data = mainPosition.toTrainingData();

        printTrainingData(data);
    }

    // Method to handle individual commands, used in both the main loop and the execute command
    private static void handleCommand(String input) {
        if (input.startsWith("see ")) {
            handleSeeCommand(input);
        } else if (input.startsWith("dataset ")) {
            handleGenerateDatasetCommand(input);
        } else if (input.startsWith("play ")) {
            handlePlayCommand(input);
        } else if (input.startsWith("load ")) {
            handleLoadCommand(input);
        } else if (input.startsWith("fen")){
            handleFENCommand();
        } else if (input.equalsIgnoreCase("moves")) {
            handleMovesCommand();
        } else if (input.equalsIgnoreCase("board")) {
            mainPosition.printBoard();
        } else if (input.equalsIgnoreCase("skip")) {
            mainPosition.setActiveWhite(!mainPosition.isActiveWhite());
            System.out.println("Skipping the turn, sweetheart.");
        } else if (input.startsWith("engine ")) {
            handleEngineCommand(input);
        } else if (input.startsWith("flip")) {
            handleFlipCommand();
        } else if (input.startsWith("test-moves ")) {
            handleTestMovesCommand(input);
        } else {
            System.out.println("Invalid command. Use 'help' for help.");
        }
    }

    // Show help message
    private static void showHelp() {
        System.out.println("Available commands:");
        System.out.println("'see xy'             - View possible moves for a piece at coordinates (e.g., 'see a1').");
        System.out.println("'play <n>'           - Play a move as an index from the list of possible moves or play a move in notation (e.g., 'play 1').");
        System.out.println("'flip'               - Flips the board colors. Useful for AI training.");
        System.out.println("'moves'              - List possible moves.");
        System.out.println("'board'              - Print the current board.");
        System.out.println("'load <fen>'         - Load a position using a FEN string.");
        System.out.println("'fen'                - Generate a FEN.");
        System.out.println("'help'               - Show this help message.");
        System.out.println("'skip'               - Skip the turn.");
        System.out.println("'engine <args>'      - Split the input into parts (e.g., 'engine start 0t').");
        System.out.println("-> 'static'          - Evaluates the current position with the static eval. function.");
        System.out.println("-> 'best (depth)'    - Generates the best move at a given depth.");
        System.out.println("-> 'game'            - Back-and-forth game with the engine.");
        System.out.println("-> 'data'            - Print training data for the given position.");
        System.out.println("'execute <commands>' - Execute a series of commands, separated by commas (e.g., 'execute play Pe2e4, skip, play Pe4e5').");
        System.out.println("'test-moves <path>'  - Test the move generation with a file.");
        System.out.println("'exit'               - Quit the game.");
        System.out.println("'dataset <p1> <p2>'  - Generate a dataset from path 1 and output to path 2.");
    }
}
