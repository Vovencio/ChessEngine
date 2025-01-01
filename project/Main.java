import java.util.Scanner;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Main class, where everything is sewed together.
 *
 * @author Vovencio
 * @version 12/23/24
 */
public class Main {
    private static Position mainPosition;

    public static void main(String[] args) {
        mainPosition = new Position();
        Scanner scanner = new Scanner(System.in);

        // Set up the initial board
        mainPosition.setupInitialBoard();
        System.out.println("╋╋╋┏┓   Cherry Engine V. 0.1");
        System.out.println("╋╋╋┃┃   Made by Vladimir K.");
        System.out.println("┏━━┫┗━┳━━┳━┳━┳┓╋┏┓╋╋┏━━┳━┓┏━━┳┳━┓┏━━┓");
        System.out.println("┃┏━┫┏┓┃┃━┫┏┫┏┫┃╋┃┣━━┫┃━┫┏┓┫┏┓┣┫┏┓┫┃━┫");
        System.out.println("┃┗━┫┃┃┃┃━┫┃┃┃┃┗━┛┣━━┫┃━┫┃┃┃┗┛┃┃┃┃┃┃━┫");
        System.out.println("┗━━┻┛┗┻━━┻┛┗┛┗━┓┏┛╋╋┗━━┻┛┗┻━┓┣┻┛┗┻━━┛");
        System.out.println("╋╋╋╋╋╋╋╋╋╋╋╋╋┏━┛┃╋╋╋╋╋╋╋╋╋┏━┛┃");
        System.out.println("╋╋╋╋╋╋╋╋╋╋╋╋╋┗━━┛╋╋╋╋╋╋╋╋╋┗━━┛");
        printLines();
        System.out.println("Welcome to my Engine, sweetheart.");

        while (true) {
            printLines();
            System.out.println("Enter a command, please.");
            String input = scanner.nextLine();
            printLines();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Exiting the game. Have a great day, sweetheart!");
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

    // Handle the 'engine' command (does nothing, just splits input)
    private static void handleEngineCommand(String input) {
        String[] parts = input.substring(7).split("\\s+");  // Strip the 'engine ' prefix and split by spaces
        System.out.println("Engine command received. Input split into parts:");
        for (String part : parts) {
            System.out.println(part);
        }
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

    // Method to handle individual commands, used in both the main loop and the execute command
    private static void handleCommand(String input) {
        if (input.startsWith("see ")) {
            handleSeeCommand(input);
        } else if (input.startsWith("play ")) {
            handlePlayCommand(input);
        } else if (input.startsWith("load ")) {
            handleLoadCommand(input);
        } else if (input.equalsIgnoreCase("moves")) {
            handleMovesCommand();
        } else if (input.equalsIgnoreCase("board")) {
            mainPosition.printBoard();
        } else if (input.equalsIgnoreCase("skip")) {
            mainPosition.setActiveWhite(!mainPosition.isActiveWhite());
            System.out.println("Skipping the turn, sweetheart.");
        } else if (input.startsWith("test-moves ")) {
            handleTestMovesCommand(input);
        } else {
            System.out.println("Invalid command. Use 'help' for help.");
        }
    }

    public static void handleTestMovesCommand(String input) {
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

    // Show help message
    private static void showHelp() {
        System.out.println("Available commands:");
        System.out.println("'see xy'             - View possible moves for a piece at coordinates (e.g., 'see a1').");
        System.out.println("'play <n>'           - Play a move from the list of possible moves (e.g., 'play 1').");
        System.out.println("'moves'              - List possible moves.");
        System.out.println("'board'              - Print the current board.");
        System.out.println("'load <fen>'         - Load a position using a FEN string.");
        System.out.println("'exit'               - Quit the game.");
        System.out.println("'help'               - Show this help message.");
        System.out.println("'skip'               - Skip the turn.");
        System.out.println("'engine <args>'      - Split the input into parts (e.g., 'engine start 0t').");
        System.out.println("'execute <commands>' - Execute a series of commands, separated by commas (e.g., 'execute play Pe2e4, skip, play Pe4e5').");
        System.out.println("'test-moves <path>'  - Test the move generation with a file.");

    }
}
