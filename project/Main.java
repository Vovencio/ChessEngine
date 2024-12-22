import java.util.Scanner;

/**
 * Main class, where everything is sewed together.
 *
 * @author Vovencio
 * @version 12/22/24
 */
public class Main {
    private static Board mainBoard;

    public static void main(String[] args) {
        mainBoard = new Board();
        Scanner scanner = new Scanner(System.in);
        mainBoard.setupInitialBoard();
        mainBoard.printBoard();
        String input = scanner.nextLine();

        playMoveFromNotation(input);
        mainBoard.printBoard();
    }

    // Helper to validate coordinates
    private static boolean isValidCoordinate(byte x, byte y) {
        return x < 0 || x > 7 || y < 0 || y > 7;
    }

    public static void playMoveFromNotation(String notation) {
        // Validate input length
        if (notation.length() != 4) {
            System.out.println("Invalid notation! Must be in the form 'e2e4'.");
            return;
        }

        try {
            // Parse the notation
            byte startX = (byte) (notation.charAt(0) - 'a');
            byte startY = (byte) (Character.getNumericValue(notation.charAt(1)) - 1);
            byte endX = (byte) (notation.charAt(2) - 'a');
            byte endY = (byte) (Character.getNumericValue(notation.charAt(3)) - 1);

            // Validate coordinates
            if (isValidCoordinate(startX, startY) || isValidCoordinate(endX, endY)) {
                System.out.println("Invalid move! Coordinates out of bounds.");
                return;
            }

            // Play the move
            Move move = new Move(startX, startY, endX, endY);
            mainBoard.playMove(move);

            System.out.println("Move played: " + move);

        } catch (Exception e) {
            System.out.println("Error parsing notation. Please ensure it's valid.");
        }
    }
}
