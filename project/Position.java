import java.util.ArrayList;
import java.util.List;

/**
 * Represents a chessboard consisting of 8x8 squares.
 *
 * @author Vovencio
 * @version 01/01/2024
 */
public class Position {

    //#region Attributes
    // 8x8 grid of squares
    private Square[][] squares = new Square[8][8];

    // Metadata for proper function
    private boolean isActiveWhite = true;
    private byte halfMoveClock = 0;
    private int moveCounter = 0;
    private byte[] enPassant = {-1, -1};
    private boolean canWhiteCastleQueen = true;
    private boolean canWhiteCastleKing = true;
    private boolean canBlackCastleQueen = true;
    private boolean canBlackCastleKing = true;

    // King positions, so that they must not be searched for
    private byte[] kingPosWhite = new byte[2];
    private byte[] kingPosBlack = new byte[2];

    //#endregion

    //#region Getters and Setters

    public byte[] getKingPosWhite() {
        return kingPosWhite;
    }

    public byte[] getKingPosBlack() {
        return kingPosBlack;
    }

    public boolean isActiveWhite() {
        return isActiveWhite;
    }

    public void setActiveWhite(boolean activeWhite) {
        isActiveWhite = activeWhite;
    }

    public byte getHalfMoveClock() {
        return halfMoveClock;
    }

    public void setHalfMoveClock(byte halfMoveClock) {
        this.halfMoveClock = halfMoveClock;
    }

    public int getMoveCounter() {
        return moveCounter;
    }

    public void setMoveCounter(int moveCounter) {
        this.moveCounter = moveCounter;
    }

    public byte[] getEnPassant() {
        return enPassant;
    }

    public void setEnPassant(byte[] enPassant) {
        this.enPassant = enPassant;
    }

    public boolean isCanWhiteCastleQueen() {
        return canWhiteCastleQueen;
    }

    public void setCanWhiteCastleQueen(boolean canWhiteCastleQueen) {
        this.canWhiteCastleQueen = canWhiteCastleQueen;
    }

    public boolean isCanWhiteCastleKing() {
        return canWhiteCastleKing;
    }

    public void setCanWhiteCastleKing(boolean canWhiteCastleKing) {
        this.canWhiteCastleKing = canWhiteCastleKing;
    }

    public boolean isCanBlackCastleQueen() {
        return canBlackCastleQueen;
    }

    public void setCanBlackCastleQueen(boolean canBlackCastleQueen) {
        this.canBlackCastleQueen = canBlackCastleQueen;
    }

    public boolean isCanBlackCastleKing() {
        return canBlackCastleKing;
    }

    public void setCanBlackCastleKing(boolean canBlackCastleKing) {
        this.canBlackCastleKing = canBlackCastleKing;
    }
    //#endregion

    /**
     * Initializes the board with empty squares.
     */
    public Position() {
        for (byte x = 0; x < 8; x++) {
            for (byte y = 0; y < 8; y++) {
                squares[x][y] = new Square(x, y, (byte) 0); // Initialize as empty
            }
        }
    }

    /**
     * Places a piece on a given square.
     *
     * @param x       X-coordinate of the square (0-7)
     * @param y       Y-coordinate of the square (0-7)
     * @param content Content to set (0 = Empty, 1 = Pawn, ..., 6 = King; +6 for black)
     */
    public void setSquareContent(byte x, byte y, byte content) {
        squares[x][y].setContent(content);

        // Update White King Position
        if (content == 6) {
            kingPosWhite[0] = x; kingPosWhite[1] = y;
        }
        else if (content == 12) {
            kingPosBlack[0] = x; kingPosBlack[1] = y;
        }
    }

    /**
     * Retrieves a specific square on the board.
     *
     * @param x X-coordinate of the square (0-7)
     * @param y Y-coordinate of the square (0-7)
     * @return The square at the specified coordinates.
     */
    public Square getSquare(byte x, byte y) {
        return squares[x][y];
    }

    /**
     * Converts castling rights into a string representation.
     *
     * @return A string representing the current castling rights.
     */
    public String getCastlingRights() {
        StringBuilder castlingRights = new StringBuilder();

        if (canWhiteCastleKing) castlingRights.append("K");
        if (canWhiteCastleQueen) castlingRights.append("Q");
        if (canBlackCastleKing) castlingRights.append("k");
        if (canBlackCastleQueen) castlingRights.append("q");

        // If no castling rights exist, return "-"
        if (castlingRights.isEmpty()) {
            return "-";
        }

        return castlingRights.toString();
    }

    /**
     * Displays the board's current state as a simple text representation.
     */
    public void printBoard() {
        System.out.println("   a  b  c  d  e  f  g  h"); // Column headers
        for (int y = 7; y >= 0; y--) { // Print from top to bottom (chessboard perspective)
            System.out.print((y + 1) + " "); // Row number
            for (int x = 0; x < 8; x++) {
                Square square = squares[x][y];
                String content = (square.hasPiece() != 0)
                        ? ((square.isWhiteTeam() == 1) ? "W" : "B") + getPieceType(square)
                        : "--";
                System.out.print(content + " ");
            }
            System.out.println((y + 1)); // Row number on the right
        }
        System.out.println("   a  b  c  d  e  f  g  h"); // Column headers
        System.out.println();
    }

    /**
     * Displays the board's state and highlights possible moves for a piece at the given coordinates.
     *
     * @param x The X-coordinate of the square (0-7).
     * @param y The Y-coordinate of the square (0-7).
     */
    public void drawPossibleMoves(byte x, byte y) {
        // Get all possible moves for the given square
        List<Move> possibleMoves = getPossibleMovesSquare(x, y, false);

        // Create a 2D array for the board state
        String[][] boardRepresentation = new String[8][8];

        // Fill the board with the current state
        for (int row = 7; row >= 0; row--) {
            for (int col = 0; col < 8; col++) {
                Square square = squares[col][row];
                boardRepresentation[col][row] = (square.hasPiece() != 0)
                        ? ((square.isWhiteTeam() == 1) ? "W" : "B") + getPieceType(square)
                        : "--";
            }
        }

        // Highlight possible moves
        for (Move move : possibleMoves) {
            byte targetX = move.getToPositionX();
            byte targetY = move.getToPositionY();

            // Check if the target square has a piece
            Square targetSquare = getSquare(targetX, targetY);
            if (targetSquare.hasPiece() == 0) {
                // Empty square
                boardRepresentation[targetX][targetY] = "##";
            } else {
                // Square with a piece to capture
                String pieceSymbol = getPieceType(targetSquare);
                boardRepresentation[targetX][targetY] = "#" + pieceSymbol;
            }
        }

        // Print the modified board with labels
        System.out.println("   a  b  c  d  e  f  g  h"); // Column headers
        for (int row = 7; row >= 0; row--) { // Print from top to bottom (chessboard perspective)
            System.out.print((row + 1) + " "); // Row number
            for (int col = 0; col < 8; col++) {
                System.out.print(boardRepresentation[col][row] + " ");
            }
            System.out.println((row + 1)); // Row number on the right
        }
        System.out.println("   a  b  c  d  e  f  g  h"); // Column headers
        System.out.println();
    }

    /**
     * Maps piece content to a type for display purposes.
     *
     * @param square The square containing the piece.
     * @return The character representing the piece type (e.g., P, N, B, R, Q, K).
     */
    private String getPieceType(Square square) {
        byte pieceType = (byte) (square.getContent() > 6 ? square.getContent() - 6 : square.getContent()); // Normalize black content
        return switch (pieceType) {
            case 0 -> "-"; // Empty
            case 1 -> "P"; // Pawn
            case 2 -> "N"; // Knight
            case 3 -> "B"; // Bishop
            case 4 -> "R"; // Rook
            case 5 -> "Q"; // Queen
            case 6 -> "K"; // King
            default -> "?"; // Unknown content
        };
    }

    /**
     * Resets the board to its initial state with standard chess piece positions.
     */
    public void setupInitialBoard() {
        // Setup pawns
        for (byte x = 0; x < 8; x++) {
            setSquareContent(x, (byte) 1, (byte) 1); // White pawns
            setSquareContent(x, (byte) 6, (byte) 7); // Black pawns
        }

        // Setup major pieces (rooks, knights, bishops, etc.)
        byte[] pieceOrder = {4, 2, 3, 5, 6, 3, 2, 4};
        for (byte x = 0; x < 8; x++) {
            setSquareContent(x, (byte) 0, pieceOrder[x]); // White pieces
            setSquareContent(x, (byte) 7, (byte) (pieceOrder[x] + 6)); // Black pieces
        }
    }

    /**
     * Returns all possible moves in the current position.
     */
    public List<Move> getPossibleMovesBoard(boolean isWhiteTeam) {
        List<Move> possibleMoves = new ArrayList<>();

        for (byte x = 0; x < 8; x++) {
            for (byte y = 0; y < 8; y++) {
                Square square = squares[x][y];

                if (square.hasPiece() != 0 && square.hasPiece() == (byte) (isWhiteTeam ? 1 : 2)) {
                    possibleMoves.addAll(getPossibleMovesSquare(x, y, false));
                }
            }
        }

        return possibleMoves;
    }

    /**
     * Validates if a square is valid.
     * @return Whether the square is valid (boolean).
     * */
    private static boolean isValidSquare(byte x, byte y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    //#region Get possible moves

    public boolean isLegalMove(Move move) {
        boolean isWhiteTeam = move.getFromPiece() < 7;
        byte[] kingPos = isWhiteTeam ? kingPosWhite : kingPosBlack;

        playMove(move);
        boolean isLegal = !isAttacked(kingPos[0], kingPos[1], isWhiteTeam);
        reverseMove(move);
        return isLegal;
    }

    private void addMove(List<Move> moves, Move move, boolean noCheck){
        if (isLegalMove(move) || noCheck) moves.add(move);
    }
    private void addMoveNoCheck(List<Move> moves, Move move){
        moves.add(move);
    }

    private void addMovesPawn(byte x, byte y, List<Move> moves, boolean isWhite, boolean noCheck) {
        byte moveDir = (byte) (isWhite ? 1 : -1);
        byte startRow = (byte) (isWhite ? 1 : 6);
        byte enemy = (byte) (isWhite ? 2 : 1);
        boolean isNotDevelopment = (isWhite ? (y < 6) : (y > 1));

        // Normal move
        if (isNotDevelopment) {
            if (getSquare(x, (byte) (y + moveDir)).hasPiece() == 0) {
                addMove(moves, new NormalMove(x, y, x, (byte) (y + moveDir), this), noCheck);
                // Double move
                if (y == startRow && getSquare(x, (byte) (y + moveDir + moveDir)).hasPiece() == 0) {
                    addMove(moves, new DoublePushMove(x, y, x, (byte) (y + moveDir + moveDir), this), noCheck);
                }
            }
            // Capture
            if (x < 7) {
                if (getSquare((byte) (x+1), (byte) (y + moveDir)).hasPiece() == enemy) {
                    addMove(moves, new NormalMove(x, y, (byte) (x+1), (byte) (y + moveDir), this), noCheck);
                }
            }
            if (x > 0 ) {
                if (getSquare((byte) (x-1), (byte) (y + moveDir)).hasPiece() == enemy) {
                    addMove(moves, new NormalMove(x, y, (byte) (x-1), (byte) (y + moveDir), this), noCheck);
                }
            }
            // En passant
            if (x < 7) {
                if (getSquare((byte) (x+1), (byte) (y)).hasPiece() == enemy &&
                        (x+1) == enPassant[0] && (y + moveDir) == enPassant[1]) {
                    addMove(moves, new EnPassant(x, y, (byte) (x+1), (byte) (y + moveDir), this), noCheck);
                }
            }
            if (x > 0) {
                if (getSquare((byte) (x-1), (byte) (y)).hasPiece() == enemy &&
                        (x-1) == enPassant[0] && (y + moveDir) == enPassant[1]) {
                    addMove(moves, new EnPassant(x, y, (byte) (x-1), (byte) (y + moveDir), this), noCheck);
                }
            }
        }
        else {
            byte[] possibleDevelopments = isWhite ? new byte[] {2,3,4,5} : new byte[] {8,9,10,11};
            if (getSquare(x, (byte) (y + moveDir)).hasPiece() == 0) {
                for (byte development : possibleDevelopments) {
                    addMove(moves, new DevelopmentMove(x, y, x, (byte) (y + moveDir), this, development), noCheck);
                }
            }
            // Capture
            if (x < 7) {
                if (getSquare((byte) (x+1), (byte) (y + moveDir)).hasPiece() == enemy) {
                    for (byte development : possibleDevelopments) {
                        addMove(moves, new DevelopmentMove(x, y, (byte) (x + 1), (byte) (y + moveDir), this, development), noCheck);
                    }
                }
            }
            if (x > 0 ) {
                if (getSquare((byte) (x-1), (byte) (y + moveDir)).hasPiece() == enemy) {
                    for (byte development : possibleDevelopments) {
                        addMove(moves, new DevelopmentMove(x, y, (byte) (x - 1), (byte) (y + moveDir), this, development), noCheck);
                    }
                }
            }
        }
    }

    private void addMovesKnight(byte x, byte y, List<Move> moves, boolean isWhite, boolean noCheck) {
        byte enemy = (byte) (isWhite ? 2 : 1);

        // Possible knight move offsets
        byte[][] offsets = {
                {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
                {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };

        for (byte[] offset : offsets) {
            byte newX = (byte) (x + offset[0]);
            byte newY = (byte) (y + offset[1]);

            if (isValidSquare(newX, newY)) {
                Square targetSquare = getSquare(newX, newY);
                if (targetSquare.hasPiece() == 0) {
                    addMove(moves, new NormalMove(x, y, newX, newY, this), noCheck);
                }
                // Capture
                else if (targetSquare.hasPiece() == enemy) {
                    addMove(moves, new NormalMove(x, y, newX, newY, this), noCheck);
                }
            }
        }
    }

    private void addMovesBishop(byte x, byte y, List<Move> moves, boolean isWhite, boolean noCheck) {
        byte enemy = (byte) (isWhite ? 2 : 1);

        // Diagonal directions for bishop movement
        int[][] directions = {
                {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
        };

        byte newX;
        byte newY;

        for (int[] direction : directions) {
            byte dx = (byte) direction[0];
            byte dy = (byte) direction[1];
            newX = x;
            newY = y;

            // Move in the current direction until hitting the edge or an obstacle
            while (isValidSquare((byte) (newX + dx), (byte) (newY + dy))) {
                newX += dx;
                newY += dy;

                Square targetSquare = getSquare(newX, newY);
                if (targetSquare.hasPiece() == 0) {
                    // Empty square - normal move
                    addMove(moves, new NormalMove(x, y, newX, newY, this), noCheck);
                } else {
                    // Stop if it's an opponent piece (capture and stop movement)
                    if (targetSquare.hasPiece() == enemy) {
                        addMove(moves, new NormalMove(x, y, newX, newY, this), noCheck);
                    }
                    break; // Stop if there's any piece (opponent or ally)
                }
            }
        }
    }

    private void addMovesRook(byte x, byte y, List<Move> moves, boolean isWhite, boolean noCheck) {
        byte enemy = (byte) (isWhite ? 2 : 1);

        // Orthogonal movement directions for rook movement
        int[][] directions = {
                {0, 1}, {0, -1}, {-1, 0}, {1, 0}
        };

        byte newX;
        byte newY;

        for (int[] direction : directions) {
            byte dx = (byte) direction[0];
            byte dy = (byte) direction[1];
            newX = x;
            newY = y;

            // Move in the current direction until hitting the edge or an obstacle
            while (isValidSquare((byte) (newX + dx), (byte) (newY + dy))) {
                newX += dx;
                newY += dy;

                Square targetSquare = getSquare(newX, newY);
                if (targetSquare.hasPiece() == 0) {
                    // Empty square - normal move
                    addMove(moves, new NormalMove(x, y, newX, newY, this), noCheck);
                } else {
                    // Stop if it's an opponent piece (capture and stop movement)
                    if (targetSquare.hasPiece() == enemy) {
                        addMove(moves, new NormalMove(x, y, newX, newY, this), noCheck);
                    }
                    break; // Stop if there's any piece (opponent or ally)
                }
            }
        }
    }

    private void addMovesQueen(byte x, byte y, List<Move> moves, boolean isWhite, boolean noCheck) {
        byte enemy = (byte) (isWhite ? 2 : 1);

        // Combined movement of Rook and Bishop
        int[][] directions = {
                {1, 1}, {1, -1}, {-1, 1}, {-1, -1}, {0, 1}, {0, -1}, {-1, 0}, {1, 0}
        };

        byte newX;
        byte newY;

        for (int[] direction : directions) {
            byte dx = (byte) direction[0];
            byte dy = (byte) direction[1];
            newX = x;
            newY = y;

            // Move in the current direction until hitting the edge or an obstacle
            while (isValidSquare((byte) (newX + dx), (byte) (newY + dy))) {
                newX += dx;
                newY += dy;

                Square targetSquare = getSquare(newX, newY);
                if (targetSquare.hasPiece() == 0) {
                    // Empty square - normal move
                    addMove(moves, new NormalMove(x, y, newX, newY, this), noCheck);
                } else {
                    // Stop if it's an opponent piece (capture and stop movement)
                    if (targetSquare.hasPiece() == enemy) {
                        addMove(moves, new NormalMove(x, y, newX, newY, this), noCheck);
                    }
                    break; // Stop if there's any piece (opponent or ally)
                }
            }
        }
    }

    private void addMovesKing(byte x, byte y, List<Move> moves, boolean isWhite, boolean noCheck) {
        byte enemy = (byte) (isWhite ? 2 : 1);

        // Possible king move offsets
        byte[][] offsets = {
                {0, 1}, {0, -1}, {1, 1}, {1, -1},
                {-1, 1}, {-1, -1}, {-1, 0}, {1, 0}
        };

        for (byte[] offset : offsets) {
            byte newX = (byte) (x + offset[0]);
            byte newY = (byte) (y + offset[1]);

            if (isValidSquare(newX, newY)) {
                Square targetSquare = getSquare(newX, newY);
                if (targetSquare.hasPiece() == 0) {
                    addMove(moves, new NormalMove(x, y, newX, newY, this), noCheck);
                }
                // Capture
                else if (targetSquare.hasPiece() == enemy) {
                    addMove(moves, new NormalMove(x, y, newX, newY, this), noCheck);
                }
            }
        }

        // Castling
        if (isWhite) {
            if (canWhiteCastleKing || canWhiteCastleQueen) {
                // We assume that the king is at 4, 0
                if (!isAttacked((byte) 4, (byte) 0, true)) {
                    if (canWhiteCastleKing) {
                        if (getSquare((byte) 5, (byte) 0).hasPiece() == 0 &&
                                getSquare((byte) 6, (byte) 0).hasPiece() == 0) {
                            if (!isAttacked((byte) 5, (byte) 0, true)
                                    && !isAttacked((byte) 6, (byte) 0, true)){
                                addMoveNoCheck(moves, new CastleMove((byte) 4, (byte) 0, (byte) 6, (byte) 0, this));
                            }
                        }
                    }
                    if (canWhiteCastleQueen) {
                        if (getSquare((byte) 1, (byte) 0).hasPiece() == 0 &&
                                getSquare((byte) 2, (byte) 0).hasPiece() == 0 &&
                                getSquare((byte) 3, (byte) 0).hasPiece() == 0) {
                            if (!isAttacked((byte) 2, (byte) 0, true)
                                    && !isAttacked((byte) 3, (byte) 0, true)){
                                addMoveNoCheck(moves, new CastleMove((byte) 4, (byte) 0, (byte) 2, (byte) 0, this));
                            }
                        }
                    }
                }
            }
        }
        else {
            if (canBlackCastleKing || canBlackCastleQueen) {
                // We assume that the king is at 4, 7
                if (!isAttacked((byte) 4, (byte) 7, false)) {
                    if (canBlackCastleKing) {
                        if (getSquare((byte) 5, (byte) 7).hasPiece() == 0 &&
                                getSquare((byte) 6, (byte) 7).hasPiece() == 0) {
                            if (!isAttacked((byte) 5, (byte) 7, false)
                                    && !isAttacked((byte) 6, (byte) 7, false)){
                                addMoveNoCheck(moves, new CastleMove((byte) 4, (byte) 7, (byte) 6, (byte) 7, this));
                            }
                        }
                    }
                    if (canBlackCastleQueen) {
                        if (getSquare((byte) 1, (byte) 7).hasPiece() == 0 &&
                                getSquare((byte) 2, (byte) 7).hasPiece() == 0 &&
                                getSquare((byte) 3, (byte) 7).hasPiece() == 0) {
                            if (!isAttacked((byte) 2, (byte) 7, false)
                                    && !isAttacked((byte) 3, (byte) 7, false)){
                                addMoveNoCheck(moves, new CastleMove((byte) 4, (byte) 7, (byte) 2, (byte) 7, this));
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Gets all possible moves for a square.
     *
     * @param x       X-coordinate of the square (0-7)
     * @param y       Y-coordinate of the square (0-7)
     */
    public List<Move> getPossibleMovesSquare(byte x, byte y, boolean noCheck){
        List<Move> moves = new ArrayList<>();

        switch (getSquare(x, y).getContent()){
            case 0:
                System.out.println("Checking an empty square, this should not be happening...");
                break;
            // White pawn
            case 1:
                addMovesPawn(x, y, moves, true, noCheck);
                break;
            // Black pawn
            case 7:
                addMovesPawn(x, y, moves, false, noCheck);
                break;
            // White Knight
            case 2:
                addMovesKnight(x, y, moves, true, noCheck);
                break;
            // Black Knight
            case 8:
                addMovesKnight(x, y, moves, false, noCheck);
                break;
            // White Bishop
            case 3:
                addMovesBishop(x, y, moves, true, noCheck);
                break;
            // Black Bishop
            case 9:
                addMovesBishop(x, y, moves, false, noCheck);
                break;
            // White Rook
            case 4:
                addMovesRook(x, y, moves, true, noCheck);
                break;
            // Black Rook
            case 10:
                addMovesRook(x, y, moves, false, noCheck);
                break;
            // White Queen
            case 5:
                addMovesQueen(x, y, moves, true, noCheck);
                break;
            // Black Queen
            case 11:
                addMovesQueen(x, y, moves, false, noCheck);
                break;
            // White King
            case 6:
                addMovesKing(x, y, moves, true, noCheck);
                break;
            // Black King
            case 12:
                addMovesKing(x, y, moves, false, noCheck);
                break;

            default:
                System.out.printf("Unknown piece type %d :(%n", getSquare(x, y).getContent());
                break;
        }

        return moves;
    }

    //#endregion

    //#region Check Attacks
    private boolean checkPawnThreat(byte x, byte y, byte enemyPawn, byte pawnDirection) {
        for (byte dx : new byte[]{-1, 1}) { // Pawns attack diagonally
            byte nx = (byte) (x + dx);
            byte ny = (byte) (y + pawnDirection);
            if (isValidSquare(nx, ny)) {
                Square square = getSquare(nx, ny);
                if (square.getContent() == enemyPawn) {
                    return true; // Enemy Pawn
                }
            }
        }
        return false;
    }

    private boolean offsetThreat(byte x, byte y, byte[][] offsets, byte specificPiece) {
        for (byte[] offset : offsets) {
            byte nx = (byte) (x + offset[0]);
            byte ny = (byte) (y + offset[1]);
            if (isValidSquare(nx, ny)) {
                Square square = getSquare(nx, ny);
                if (square.getContent() == specificPiece) {
                    return true; // Threat found
                }
            }
        }
        return false;
    }

    private boolean checkSlidingThreat(byte x, byte y, byte[][] directions, byte specificPiece, byte queen) {
        for (byte[] dir : directions) {
            byte nx = x;
            byte ny = y;
            while (true) {
                nx += dir[0];
                ny += dir[1];
                if (!isValidSquare(nx, ny)) break; // Out of bounds

                Square square = getSquare(nx, ny);
                if (square.hasPiece() == 0) continue; // Empty square
                if (square.getContent() == specificPiece || square.getContent() == queen) {
                    return true; // Threat by specific piece or Queen
                }
                break; // Blocked by other piece
            }
        }
        return false;
    }

    /**
    * Checks whether a square is attacked by a team.
    *
    * @param isWhiteTeam True to check, if it's attacked by black, false for white.
    * */
    public boolean isAttacked(byte x, byte y, boolean isWhiteTeam) {
        // Enemy piece IDs based on team
        byte enemyPawn = (byte) (isWhiteTeam ? 7 : 1);
        byte enemyKnight = (byte) (isWhiteTeam ? 8 : 2);
        byte enemyBishop = (byte) (isWhiteTeam ? 9 : 3);
        byte enemyRook = (byte) (isWhiteTeam ? 10 : 4);
        byte enemyQueen = (byte) (isWhiteTeam ? 11 : 5);
        byte enemyKing = (byte) (isWhiteTeam ? 12 : 6);

        // Possible king move offsets
        byte[][] kingOffsets = {
                {0, 1}, {0, -1}, {1, 1}, {1, -1},
                {-1, 1}, {-1, -1}, {-1, 0}, {1, 0}
        };

        // Possible knight move offsets
        byte[][] knightOffsets = {
                {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
                {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };

        // Check for Pawn threats
        byte pawnDirection = (byte) (isWhiteTeam ? 1 : -1); // Direction pawns attack from
        if (checkPawnThreat(x, y, enemyPawn, pawnDirection)) return true;

        // Check for Knight threats
        if (offsetThreat(x, y, knightOffsets, enemyKnight)) return true;

        // Check for Bishop/Queen threats (Diagonals)
        byte[][] bishopDirections = {{1, 1}, {-1, -1}, {1, -1}, {-1, 1}};
        if (checkSlidingThreat(x, y, bishopDirections, enemyBishop, enemyQueen)) return true;

        // Check for Rook/Queen threats (Rows & Columns)
        byte[][] rookDirections = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        if (checkSlidingThreat(x, y, rookDirections, enemyRook, enemyQueen)) return true;

        // Check for King threats
        return offsetThreat(x, y, kingOffsets, enemyKing);

        // No threats found
    }
    //#endregion

    /**
     * Plays a move on the chessboard. This method does not prove the move's correctness.
     *
     * @param move The move, which should be played.
     */
    public void playMove(Move move) {
        if (!isActiveWhite) moveCounter += 1;
        enPassant = new byte[] {-1, -1};
        isActiveWhite = !isActiveWhite;

        if (move.getFromPiece() == 1 || move.getFromPiece() == 7 || move.getToPiece() != 0) {
            halfMoveClock = 0;
        }

        halfMoveClock += 1;
        move.Play(this);
    }

    public void reverseMove(Move move) {
        move.Reverse(this);
    }

    public void loadFEN(String fen) {
        String[] parts = fen.split(" ");
        if (parts.length != 6) {
            throw new IllegalArgumentException("Invalid FEN string.");
        }

        for (byte x = 0; x < 8; x++) {
            for (byte y = 0; y < 8; y++) {
                setSquareContent(x, y, (byte) 0);
            }
        }

        // 1. Set up the board squares
        String[] ranks = parts[0].split("/");
        for (byte y = 0; y < 8; y++) {
            byte x = 0;
            for (char c : ranks[7 - y].toCharArray()) {
                if (Character.isDigit(c)) {
                    x += c - '0';
                } else {
                    byte piece = fenCharToPiece(c);
                    setSquareContent((byte) x, (byte) y, piece);
                    x++;
                }
            }
        }

        // 2. Set active color
        isActiveWhite = parts[1].equals("w");

        // 3. Set castling rights
        canWhiteCastleKing = parts[2].contains("K");
        canWhiteCastleQueen = parts[2].contains("Q");
        canBlackCastleKing = parts[2].contains("k");
        canBlackCastleQueen = parts[2].contains("q");

        // 4. Set en passant target square
        if (parts[3].equals("-")) {
            enPassant = new byte[]{-1, -1};
        } else {
            enPassant = new byte[2];
            enPassant[0] = (byte) (parts[3].charAt(0) - 'a');
            enPassant[1] = (byte) (parts[3].charAt(1) - '1');
        }

        // 5. Set half move clock
        halfMoveClock = Byte.parseByte(parts[4]);

        // 6. Set full move number
        moveCounter = Integer.parseInt(parts[5]);
    }

    private byte fenCharToPiece(char c) {
        return switch (c) {
            case 'p' -> 7;  // Black Pawn
            case 'n' -> 8;  // Black Knight
            case 'b' -> 9;  // Black Bishop
            case 'r' -> 10; // Black Rook
            case 'q' -> 11; // Black Queen
            case 'k' -> 12; // Black King
            case 'P' -> 1;  // White Pawn
            case 'N' -> 2;  // White Knight
            case 'B' -> 3;  // White Bishop
            case 'R' -> 4;  // White Rook
            case 'Q' -> 5;  // White Queen
            case 'K' -> 6;  // White King
            default -> throw new IllegalArgumentException("Invalid FEN character: " + c);
        };
    }

    public String generateFEN() {
        StringBuilder fen = new StringBuilder();

        // 1. Board state
        for (byte y = 7; y >= 0; y--) {
            byte emptyCount = 0;
            for (byte x = 0; x < 8; x++) {
                byte piece = getSquare(x, y).getContent();
                if (piece == 0) {
                    emptyCount++;
                } else {
                    if (emptyCount > 0) {
                        fen.append(emptyCount);
                        emptyCount = 0;
                    }
                    fen.append(pieceToFenChar(piece));
                }
            }
            if (emptyCount > 0) {
                fen.append(emptyCount);
            }
            if (y > 0) {
                fen.append('/');
            }
        }
        fen.append(" ");

        // 2. Active color
        fen.append(isActiveWhite ? "w" : "b");
        fen.append(" ");

        // 3. Castling rights
        fen.append(getCastlingRights());
        fen.append(" ");

        // 4. En passant
        if (enPassant[0] == -1 && enPassant[1] == -1) {
            fen.append("-");
        } else {
            fen.append((char) (enPassant[0] + 'a'));
            fen.append((char) (enPassant[1] + '1'));
        }
        fen.append(" ");

        // 5. Half move clock
        fen.append(halfMoveClock);
        fen.append(" ");

        // 6. Full move number
        fen.append(moveCounter);

        return fen.toString();
    }

    private char pieceToFenChar(byte piece) {
        return switch (piece) {
            case 1 -> 'P';  // White Pawn
            case 2 -> 'N';  // White Knight
            case 3 -> 'B';  // White Bishop
            case 4 -> 'R';  // White Rook
            case 5 -> 'Q';  // White Queen
            case 6 -> 'K';  // White King
            case 7 -> 'p';  // Black Pawn
            case 8 -> 'n';  // Black Knight
            case 9 -> 'b';  // Black Bishop
            case 10 -> 'r'; // Black Rook
            case 11 -> 'q'; // Black Queen
            case 12 -> 'k'; // Black King
            default -> throw new IllegalArgumentException("Invalid piece code: " + piece);
        };
    }

    public Position flipColor(){
        Position flippedPosition = new Position();

        // Flip Y and color
        for (byte x = 0; x < 8; x++){
            for (byte y = 0; y < 8; y++){
                flippedPosition.setSquareContent(x, (byte) (7-y), oppositePiece(getSquare(x, y).getContent()));
            }
        }

        // Flip active player
        flippedPosition.setActiveWhite(!isActiveWhite);

        // Flip Castle Rights
        flippedPosition.setCanBlackCastleKing(canWhiteCastleKing);
        flippedPosition.setCanBlackCastleQueen(canWhiteCastleQueen);
        flippedPosition.setCanWhiteCastleKing(canBlackCastleKing);
        flippedPosition.setCanWhiteCastleQueen(canBlackCastleQueen);

        if (enPassant[0] != -1 && enPassant[1] != -1){
            // Flip En Passant Y
            flippedPosition.setEnPassant(new byte[] {enPassant[0], (byte) (7 - enPassant[1])});
        }

        flippedPosition.setHalfMoveClock(halfMoveClock);
        flippedPosition.setMoveCounter(moveCounter);

        return flippedPosition;
    }

    private byte oppositePiece(byte input){
        if (input == 0) return 0;
        return (byte) ((input > 6) ? input - 6 : input + 6);
    }

    public int[] toTrainingData(){
        Position trainingPosition = (isActiveWhite) ? this : flipColor();

        /*
        * The following byte array is done as follows:
        * First 64 are the board.
        * The next four are castling rights.
        * The next two are en passant squares.
        * The last one is the 50-move clock.
        * */

        int[] data = new int[70];

        for (byte x = 0; x < 8; x++){
            for (byte y = 0; y < 8; y++){
                byte content = trainingPosition.getSquare(x, y).getContent();
                if (content != 0){
                    int index = x + y * 8;
                    data[index] = EngineAlg.getBaseEval(content);
                }
            }
        }

        if (trainingPosition.canWhiteCastleKing) data[64] = 1;
        if (trainingPosition.canWhiteCastleQueen) data[65] = 1;
        if (trainingPosition.canBlackCastleKing) data[66] = 1;
        if (trainingPosition.canBlackCastleQueen) data[67] = 1;

        data[68] = trainingPosition.getEnPassant()[0]; data[69] = trainingPosition.getEnPassant()[1];

        return data;
    }
}
