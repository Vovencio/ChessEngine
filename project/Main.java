import java.util.Scanner;

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
            System.out.println("Enter a command ('see xy' to view moves, 'play xyxy' to play a move, 'exit' to quit):");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Exiting the game. Have a great day, sweetheart!");
                break;
            }

            if (input.startsWith("see ")) {
                handleSeeCommand(input);
            } else if (input.startsWith("play ")) {
                handlePlayCommand(input);
            } else if (input.startsWith("moves ")){
                handleMovesCommand(input);
            }
            else {
                System.out.println("Invalid command. Use 'see xy' or 'play xyxy'.");
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

    // Handle the 'play xyxy' command
    private static void handlePlayCommand(String input) {
        if (input.length() != 9) {
            System.out.println("Invalid command! Use 'play xyxy' where xyxy are board coordinates.");
            return;
        }

        try {
            // Parse the notation
            byte startX = (byte) (input.charAt(5) - 'a'); // 'a'-'h' maps to 0-7
            byte startY = (byte) (Character.getNumericValue(input.charAt(6)) - 1); // '1'-'8' maps to 0-7
            byte endX = (byte) (input.charAt(7) - 'a');
            byte endY = (byte) (Character.getNumericValue(input.charAt(8)) - 1);

            // Validate coordinates
            if (isInvalidCoordinate(startX, startY) || isInvalidCoordinate(endX, endY)) {
                System.out.println("Invalid move! Coordinates out of bounds.");
                return;
            }

            // Play the move
            Move move = new NormalMove(startX, startY, endX, endY, mainPosition);
            mainPosition.playMove(move);

            System.out.println("Move played: " + input.substring(5));
            mainPosition.printBoard();

        } catch (Exception e) {
            System.out.println("Error parsing command. Please use 'play xyxy' with valid board coordinates.");
            System.out.println(e.toString());
        }
    }

    private static void handleMovesCommand(String input) {
        if (input.length() != 7) {
            System.out.println("Invalid command! Use 'moves b/w' where b and w are the teams.");
            return;
        }
        boolean team = input.charAt(6) == 'w';

        System.out.println("Possible move amount for team: " + mainPosition.getPossibleMovesBoard(team).size());
    }

    // Helper to validate coordinates
    private static boolean isInvalidCoordinate(byte x, byte y) {
        return x < 0 || x > 7 || y < 0 || y > 7;
    }
}
