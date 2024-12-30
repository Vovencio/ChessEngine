import java.util.Scanner;
import java.util.List;

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
        System.out.println("Here's the starting board, sweetheart.");
        mainPosition.printBoard();

        while (true) {
            System.out.println("Enter a command ('see xy' to view moves, 'play <move_number>' to play a move, 'moves' to list moves, 'board' to print the board, 'exit' to quit):");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Exiting the game. Have a great day, sweetheart!");
                break;
            }

            if (input.startsWith("see ")) {
                handleSeeCommand(input);
            } else if (input.startsWith("play ")) {
                handlePlayCommand(input);
            } else if (input.equalsIgnoreCase("moves")) {
                handleMovesCommand();
            } else if (input.equalsIgnoreCase("board")) {
                mainPosition.printBoard();
            } else {
                System.out.println("Invalid command. Use 'see xy', 'play <move_number>', 'moves', or 'board'.");
            }
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
            int moveIndex = Integer.parseInt(input.substring(5).trim());
            List<Move> moves = mainPosition.getPossibleMovesBoard(mainPosition.isActiveWhite());

            if (moveIndex < 1 || moveIndex > moves.size()) {
                System.out.println("Invalid move number! Please choose a valid move from the list.");
                return;
            }

            Move selectedMove = moves.get(moveIndex - 1); // Convert 1-based to 0-based index
            mainPosition.playMove(selectedMove);

            System.out.println("Move played: " + selectedMove.toString());
            mainPosition.printBoard();

        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Use 'play <move_number>' where <move_number> is a valid number.");
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
}
