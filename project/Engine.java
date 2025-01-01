public class Engine {
    public Position getEnginePosition() {
        return enginePosition;
    }

    public void setEnginePosition(Position enginePosition) {
        this.enginePosition = enginePosition;
    }

    Position enginePosition;

    public static final double[][] pawnBaseValues = {
            {3.7176470588, 3.8431372549, 3.9058823529, 4.0000000000, 4.0000000000, 3.9058823529, 3.8431372549, 3.7176470588},
            {3.4666666667, 3.8431372549, 3.9058823529, 4.0000000000, 4.0000000000, 3.9058823529, 3.8431372549, 3.4666666667},
            {1.7516339869, 1.8143790850, 1.8457516340, 1.8928104575, 1.8928104575, 1.8457516340, 1.8143790850, 1.7516339869},
            {1.3908496732, 1.4379084967, 1.4692810458, 1.4849673203, 1.4849673203, 1.4692810458, 1.4379084967, 1.3908496732},
            {1.2130718954, 1.2496732026, 1.2810457516, 1.3124183007, 1.3124183007, 1.2810457516, 1.2496732026, 1.2130718954},
            {0.9777777778, 1.0143790850, 1.0300653595, 1.0614379085, 1.0614379085, 1.0300653595, 1.0143790850, 0.9777777778},
            {0.8732026144, 0.9045751634, 0.9202614379, 0.9516339869, 0.9516339869, 0.9202614379, 0.9045751634, 0.8732026144},
            {0.8732026144, 0.9045751634, 0.9202614379, 0.9516339869, 0.9516339869, 0.9202614379, 0.9045751634, 0.8732026144},
    };

    public static final double[][] knightBaseValues = {
            {1.2035294118, 1.3737254902, 1.3737254902, 1.3737254902, 1.3737254902, 1.3737254902, 1.3737254902, 1.2035294118},
            {1.3737254902, 1.3068627451, 1.4770588235, 1.4770588235, 1.4770588235, 1.4770588235, 1.3068627451, 1.3737254902},
            {1.3737254902, 1.4770588235, 1.5500000000, 1.5500000000, 1.5500000000, 1.5500000000, 1.4770588235, 1.3737254902},
            {1.3737254902, 1.4770588235, 1.5500000000, 1.5500000000, 1.5500000000, 1.5500000000, 1.4770588235, 1.3737254902},
            {1.3737254902, 1.4770588235, 1.5500000000, 1.5500000000, 1.5500000000, 1.5500000000, 1.4770588235, 1.3737254902},
            {1.3737254902, 1.4770588235, 1.5500000000, 1.5500000000, 1.5500000000, 1.5500000000, 1.4770588235, 1.3737254902},
            {1.3737254902, 1.3068627451, 1.4770588235, 1.4770588235, 1.4770588235, 1.4770588235, 1.3068627451, 1.3737254902},
            {1.2035294118, 1.3737254902, 1.3737254902, 1.3737254902, 1.3737254902, 1.3737254902, 1.3737254902, 1.2035294118},
    };

    public static final double[] knightClosedBonus  = {
            0.000000000000000,
            0.000007629394531,
            0.000122070312500,
            0.000617980957031,
            0.001953125000000,
            0.004768371582031,
            0.009887695312500,
            0.018318176269531,
            0.031250000000000,
            0.050056457519531,
            0.076293945312500,
            0.111701965332031,
            0.158203125000000,
            0.217903137207031,
            0.293090820312500,
            0.386238098144531,
            0.500000000000000,
    };

    public static final double[] bishopOpenBonus = {
            0.400000000000000,
            0.375000000000000,
            0.350000000000000,
            0.325000000000000,
            0.300000000000000,
            0.275000000000000,
            0.250000000000000,
            0.225000000000000,
            0.200000000000000,
            0.175000000000000,
            0.150000000000000,
            0.125000000000000,
            0.100000000000000,
            0.075000000000000,
            0.050000000000000,
            0.025000000000000,
            0.000000000000000,
    };

    public static final double[] bishopColorBonus = {
            0.750000000000000,
            0.579357147216797,
            0.439636230468750,
            0.326854705810547,
            0.237304687500000,
            0.167552947998047,
            0.114440917968750,
            0.075084686279297,
            0.046875000000000,
            0.027477264404297,
            0.014831542968750,
            0.007152557373047,
            0.002929687500000,
            0.000926971435547,
            0.000183105468750,
            0.000011444091797,
            0.000000000000000,
    };

    public static final double[] rookOpenBonus = {
            1.000000000000000,
            0.900000000000000,
            0.800000000000000,
            0.700000000000000,
            0.600000000000000,
            0.500000000000000,
            0.400000000000000,
            0.300000000000000,
            0.200000000000000,
            0.100000000000000,
            0.000000000000000,
            0.000000000000000,
            0.000000000000000,
            0.000000000000000,
            0.000000000000000,
            0.000000000000000,
            0.000000000000000,
    };

    public static final double[] kingDistanceBonusBasic = {
            0.500000000000000,
            0.250000000000000,
            0.166666666666667,
            0.125000000000000,
            0.100000000000000,
            0.083333333333333,
            0.071428571428571,
            0.062500000000000,
            0.055555555555556,
            0.050000000000000,
            0.045454545454545,
            0.041666666666667,
            0.038461538461538,
            0.035714285714286,
            0.033333333333333,
            0.031250000000000,
    };

    public static final double[] kingDistanceBonusQueen = {
            0.850000000000000,
            0.425000000000000,
            0.283333333333333,
            0.212500000000000,
            0.170000000000000,
            0.141666666666667,
            0.121428571428571,
            0.106250000000000,
            0.094444444444444,
            0.085000000000000,
            0.077272727272727,
            0.070833333333333,
            0.065384615384615,
            0.060714285714286,
            0.056666666666667,
            0.053125000000000,
    };


    public Engine () {
        enginePosition = new Position();
        enginePosition.setupInitialBoard();
    }

    public double evalBoard() {
        double eval = 0;
        byte pawns = 0;
        byte blackPawns = 0;
        byte whitePawns = 0;

        for (byte x = 0; x < 8; x++) {
            for (byte y = 0; y < 8; y++) {
                boolean isBlackSquare = ((x + y) % 2) == 0;
                if (enginePosition.getSquare(x, y).getContent() == 1
                        || enginePosition.getSquare(x, y).getContent() == 7) {
                    pawns++;
                    if (isBlackSquare) blackPawns++;
                    else whitePawns++;
                }
            }
        }

        for (byte x = 0; x < 8; x++) {
            for (byte y = 0; y < 8; y++) {
                eval += evalSquare(x, y, pawns, whitePawns, blackPawns);
            }
        }

        return eval;
    }

    public double evalSquare(byte x, byte y, byte pawns, byte pawnsWhite, byte pawnsBlack) {
        byte content = enginePosition.getSquare(x, y).getContent();
        if (content == 0) return 0;

        boolean isBlack = (content > 6);

        byte piece = isBlack ? (byte) (content - 6) : content;

        return switch (piece) {
            case 1 -> evalPawn(x, y, isBlack);
            case 2 -> evalKnight(x, y, isBlack, pawns);
            case 3 -> evalBishop(x, y, isBlack, pawns, pawnsWhite, pawnsBlack);
            case 4 -> evalRook(x, y, isBlack, pawns);
            case 5 -> evalQueen(x, y, isBlack, pawns);
            default -> 0;
        };
    }

    public double evalPawn(byte x, byte y, boolean isBlack) {
        double baseValue = isBlack ? -pawnBaseValues[x][y] : pawnBaseValues[x][7-y];

        return baseValue;
    }

    public double evalKnight(byte x, byte y, boolean isBlack, byte pawns) {
        final double baseValue = isBlack ? -knightBaseValues[x][y] : knightBaseValues[x][y];

        // Evaluate Move ability
        // This is 50% of the pieces worth
        final int maxMoves = 8;
        int moves = enginePosition.getPossibleMovesSquare(x, y).size();

        double moveAbility = (double) (moves / maxMoves);

        // The knight is better in closed positions, which is evaluated in knightClosedBonus
        double bonus = isBlack ? -knightClosedBonus[pawns] : knightClosedBonus[pawns];

        double kingBonus = isBlack ? -kingDistanceBonusBasic[getDistanceToEnemyKing(true, x, y)] :
                kingDistanceBonusBasic[getDistanceToEnemyKing(false, x, y)];

        return baseValue * moveAbility + baseValue + bonus + kingBonus;
    }

    public double evalBishop(byte x, byte y, boolean isBlack, byte pawns, byte whitePawns, byte blackPawns) {
        final double baseValue = isBlack ? -1.8 : 1.8;

        // Evaluate Move ability
        // This is 50% of the pieces worth
        final int maxMoves = 13;
        int moves = enginePosition.getPossibleMovesSquare(x, y).size();

        double moveAbility = (double) (moves / maxMoves);

        // The Bishop is better in closed positions, which is bishopOpenBonus in bishopOpenBonus
        double bonus = isBlack ? -bishopOpenBonus[pawns] : bishopOpenBonus[pawns];

        byte sameColoredPawns = (((x + y) % 2) == 0) ? blackPawns : whitePawns;

        double colorBonus = isBlack ? -bishopColorBonus[sameColoredPawns] : bishopColorBonus[sameColoredPawns];

        return baseValue * moveAbility + baseValue + bonus + colorBonus;
    }

    public double evalRook(byte x, byte y, boolean isBlack, byte pawns) {
        final double baseValue = isBlack ? -5.5 : 5.5;

        // Evaluate Move ability
        // This is 50% of the pieces worth
        final int maxMoves = 14;
        int moves = enginePosition.getPossibleMovesSquare(x, y).size();

        double moveAbility = (double) (moves / maxMoves);

        // The rook much is better in open positions, which is evaluated in rookOpenBonus
        double bonus = isBlack ? -rookOpenBonus[pawns] : rookOpenBonus[pawns];

        double kingBonus = isBlack ? -kingDistanceBonusBasic[getDistanceToEnemyKing(true, x, y)] :
                kingDistanceBonusBasic[getDistanceToEnemyKing(false, x, y)];

        return baseValue * moveAbility + baseValue + bonus + kingBonus;
    }

    public double evalQueen(byte x, byte y, boolean isBlack, byte pawns) {
        final double baseValue = isBlack ? -9.5 : 9.5;

        // Evaluate Move ability
        // This is 25% of the pieces worth
        final int maxMoves = 27;
        int moves = enginePosition.getPossibleMovesSquare(x, y).size();

        double moveAbility = (double) (moves / maxMoves);

        // The Queen much is better in open positions, which is evaluated in rookOpenBonus
        double bonus = isBlack ? -rookOpenBonus[pawns] : rookOpenBonus[pawns];

        double kingBonus = isBlack ? -kingDistanceBonusQueen[getDistanceToEnemyKing(true, x, y)] :
                kingDistanceBonusQueen[getDistanceToEnemyKing(false, x, y)];

        return baseValue * moveAbility * 0.25 + baseValue * 0.75 + bonus + kingBonus;
    }

    public int getDistanceToEnemyKing(boolean isBlack, byte x, byte y) {
        byte[] kingPos = isBlack ? enginePosition.getKingPosWhite() : enginePosition.getKingPosBlack();
        return Math.abs(kingPos[0] - x) + Math.abs(kingPos[1] - y);
    }
}
