/**
 * Engine Class used for all computing.
 *
 * @author Vovencio
 * @version 01/01/2024
 */

public class EngineOld {
    public Position getEnginePosition() {
        return enginePosition;
    }

    public void setEnginePosition(Position enginePosition) {
        this.enginePosition = enginePosition;
    }

    Position enginePosition;

    public static final double[][] pawnBaseValues = {
            {1.4500000000, 1.4500000000, 1.4500000000, 1.4500000000, 1.4500000000, 1.4500000000, 1.4500000000, 1.4500000000},
            {1.4500000000, 1.4500000000, 1.4500000000, 1.4500000000, 1.4500000000, 1.4500000000, 1.4500000000, 1.4500000000},
            {1.3362745098, 1.3362745098, 1.3362745098, 1.3362745098, 1.3362745098, 1.3362745098, 1.3362745098, 1.3362745098},
            {1.2850980392, 1.2850980392, 1.2850980392, 1.2850980392, 1.2850980392, 1.2850980392, 1.2850980392, 1.2850980392},
            {1.2168627451, 1.2168627451, 1.2168627451, 1.2168627451, 1.2168627451, 1.2168627451, 1.2168627451, 1.2168627451},
            {1.0462745098, 1.0462745098, 1.0462745098, 1.0462745098, 1.0462745098, 1.0462745098, 1.0462745098, 1.0462745098},
            {1.0007843137, 1.0007843137, 1.0007843137, 1.0007843137, 1.0007843137, 1.0007843137, 1.0007843137, 1.0007843137},
            {1.0007843137, 1.0007843137, 1.0007843137, 1.0007843137, 1.0007843137, 1.0007843137, 1.0007843137, 1.0007843137},
    };

    public static final double[][] knightBaseValues = {
            {2.9176470588, 3.0027450980, 3.0027450980, 3.0027450980, 3.0027450980, 3.0027450980, 3.0027450980, 2.9176470588},
            {3.0027450980, 2.9662745098, 3.0513725490, 3.0513725490, 3.0513725490, 3.0513725490, 2.9662745098, 3.0027450980},
            {3.0027450980, 3.0513725490, 3.1000000000, 3.1000000000, 3.1000000000, 3.1000000000, 3.0513725490, 3.0027450980},
            {3.0027450980, 3.0513725490, 3.1000000000, 3.1000000000, 3.1000000000, 3.1000000000, 3.0513725490, 3.0027450980},
            {3.0027450980, 3.0513725490, 3.1000000000, 3.1000000000, 3.1000000000, 3.1000000000, 3.0513725490, 3.0027450980},
            {3.0027450980, 3.0513725490, 3.1000000000, 3.1000000000, 3.1000000000, 3.1000000000, 3.0513725490, 3.0027450980},
            {3.0027450980, 2.9662745098, 3.0513725490, 3.0513725490, 3.0513725490, 3.0513725490, 2.9662745098, 3.0027450980},
            {2.9176470588, 3.0027450980, 3.0027450980, 3.0027450980, 3.0027450980, 3.0027450980, 3.0027450980, 2.9176470588},
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

    public static final double[] knightMoveValues = {
            0.000000000000000,
            0.353553390593274,
            0.500000000000000,
            0.612372435695794,
            0.707106781186548,
            0.790569415042095,
            0.866025403784439,
            0.935414346693485,
            1.000000000000000,
    };

    public static final double[] bishopMoveValues = {
            0.000000000000000,
            0.358445109233544,
            0.472971157211515,
            0.556251456255682,
            0.624089183508004,
            0.682355444005878,
            0.733978196908315,
            0.780659906224166,
            0.823490614666608,
            0.863216360374354,
            0.900373406324707,
            0.935362023367318,
            0.968490037155347,
            1.000000000000000,
    };

    public static final double[] rookMoveValues = {
            0.000000000000000,
            0.347975594697637,
            0.459156549959434,
            0.540004386462093,
            0.605860699954663,
            0.662425111423156,
            0.712540059788795,
            0.757858283255199,
            0.799437986416581,
            0.838003417026078,
            0.874075174817471,
            0.908041839473837,
            0.940202245633905,
            0.970791861107288,
            1.000000000000000,
    };

    public static final double[] queenMoveValues = {
            0.000000000000000,
            0.438691337650831,
            0.521694860024429,
            0.577350269189626,
            0.620403239401400,
            0.655996557088477,
            0.686589047969039,
            0.713565047642691,
            0.737787946466881,
            0.759835685651593,
            0.780115773106905,
            0.798927242309477,
            0.816496580927726,
            0.832999799813128,
            0.848576631673943,
            0.863340021370451,
            0.877382675301662,
            0.890781705927929,
            0.903602003609845,
            0.915898735018158,
            0.927719227904580,
            0.939104415753752,
            0.950089960923933,
            0.960707139034002,
            0.970983543414647,
            0.980943652127571,
            0.990609288733614,
            1.000000000000000,
    };



    public double[][][] pieceWorthArray = new double[12][8][8];
    public long[][][] pieceUsedArray = new long[12][8][8];
    public double[][][] processed = new double[12][8][8];

    static final double MB_FACTOR = 0.4;
    static final double BV_FACTOR = 1;

    public EngineOld() {
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

        double eval = switch (piece) {
            case 1 -> evalPawn(x, y, isBlack);
            case 2 -> evalKnight(x, y, isBlack, pawns);
            case 3 -> evalBishop(x, y, isBlack, pawns, pawnsWhite, pawnsBlack);
            case 4 -> evalRook(x, y, isBlack, pawns);
            case 5 -> evalQueen(x, y, isBlack, pawns);
            default -> 0;
        };

        pieceWorthArray[content-1][x][y] += eval;
        pieceUsedArray[content-1][x][y] += 1;

        return eval;
    }

    public static double getBaseEval(byte content){
        double factor = (content > 6) ? -1 : 1;

        boolean isBlack = (content > 6);

        byte piece = isBlack ? (byte) (content - 6) : content;

        return switch (piece) {
            case 1 -> 1 * factor;
            case 2 -> 2.8 * factor;
            case 3 -> 3.3 * factor;
            case 4 -> 5 * factor;
            case 5 -> 9 * factor;
            default -> 0;
        };
    }

    public double evalPawn(byte x, byte y, boolean isBlack) {
        double baseValue = isBlack ? -pawnBaseValues[y][x] : pawnBaseValues[7-y][x];
        return baseValue;
    }

    public double evalKnight(byte x, byte y, boolean isBlack, byte pawns) {
        final double baseValue = isBlack ? -knightBaseValues[y][x] : knightBaseValues[y][x];

        // final int maxMoves = 8;
        int moves = enginePosition.getPossibleMovesSquare(x, y, true).size();
        double moveAbility = knightMoveValues[moves];

        double bonus = 0.25 * (isBlack ? -knightClosedBonus[pawns] : knightClosedBonus[pawns]);
        double kingBonus = isBlack ? -kingDistanceBonusBasic[getDistanceToEnemyKing(true, x, y)] :
                kingDistanceBonusBasic[getDistanceToEnemyKing(false, x, y)];

        return BV_FACTOR * baseValue + MB_FACTOR * baseValue * moveAbility + bonus + kingBonus;
    }

    public double evalBishop(byte x, byte y, boolean isBlack, byte pawns, byte whitePawns, byte blackPawns) {
        final double baseValue = isBlack ? -3.6 : 3.6;

        final int maxMoves = 13;
        // int moves = enginePosition.getPossibleMovesSquare(x, y, true).size();
        double moveAbility = bishopMoveValues[maxMoves];

        double bonus = 0.25 * (isBlack ? -bishopOpenBonus[pawns] : bishopOpenBonus[pawns]);
        byte sameColoredPawns = (((x + y) % 2) == 0) ? blackPawns : whitePawns;
        double colorBonus = 0.25 * (isBlack ? -bishopColorBonus[sameColoredPawns] : bishopColorBonus[sameColoredPawns]);

        return BV_FACTOR * baseValue + MB_FACTOR * baseValue * moveAbility + bonus + colorBonus;
    }

    public double evalRook(byte x, byte y, boolean isBlack, byte pawns) {
        final double baseValue = isBlack ? -5.5 : 5.5;

        // final int maxMoves = 14;
        int moves = enginePosition.getPossibleMovesSquare(x, y, true).size();
        double moveAbility = rookMoveValues[moves];

        double bonus = 0.25 * (isBlack ? -rookOpenBonus[pawns] : rookOpenBonus[pawns]);
        double kingBonus = isBlack ? -kingDistanceBonusBasic[getDistanceToEnemyKing(true, x, y)] :
                kingDistanceBonusBasic[getDistanceToEnemyKing(false, x, y)];

        return BV_FACTOR * baseValue + MB_FACTOR * baseValue * moveAbility + bonus + kingBonus;
    }

    public double evalQueen(byte x, byte y, boolean isBlack, byte pawns) {
        final double baseValue = isBlack ? -9.5 : 9.5;

        // final int maxMoves = 27;
        int moves = enginePosition.getPossibleMovesSquare(x, y, true).size();
        double moveAbility = queenMoveValues[moves];

        double bonus = 0.25 * (isBlack ? -rookOpenBonus[pawns] : rookOpenBonus[pawns]);
        double kingBonus = isBlack ? -kingDistanceBonusQueen[getDistanceToEnemyKing(true, x, y)] :
                kingDistanceBonusQueen[getDistanceToEnemyKing(false, x, y)];

        return BV_FACTOR * baseValue + MB_FACTOR * baseValue * moveAbility + bonus + kingBonus;
    }

    public int getDistanceToEnemyKing(boolean isBlack, byte x, byte y) {
        byte[] kingPos = isBlack ? enginePosition.getKingPosWhite() : enginePosition.getKingPosBlack();
        return Math.abs(kingPos[0] - x) + Math.abs(kingPos[1] - y);
    }

    public Branch generateBestMove(int depth, Position position){
        enginePosition.loadFEN(position.generateFEN());
        Branch root = new Branch(enginePosition, this);

        if (position.isActiveWhite()) root.maxi(-Double.MAX_VALUE, Double.MAX_VALUE, depth);
        else root.mini(-Double.MAX_VALUE, Double.MAX_VALUE, depth);

        return root.getBestChild();
    }
}
