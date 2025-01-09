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

    //#region Tables
    public static final double[][] pawnBaseValues = {
            {1.3470588235, 1.3882352941, 1.4235294118, 1.5000000000, 1.5000000000, 1.4235294118, 1.3882352941, 1.3352941176},
            {1.2117647059, 1.3411764706, 1.3705882353, 1.4941176471, 1.4941176471, 1.3705882353, 1.3529411765, 1.2000000000},
            {1.1823529412, 1.2117647059, 1.2235294118, 1.3764705882, 1.3764705882, 1.2235294118, 1.2235294118, 1.1705882353},
            {1.1117647059, 1.1411764706, 1.1529411765, 1.3235294118, 1.3235294118, 1.1529411765, 1.1470588235, 1.1117647059},
            {1.0470588235, 1.0764705882, 1.0941176471, 1.2529411765, 1.2529411765, 1.0941176471, 1.0882352941, 1.0470588235},
            {0.9705882353, 1.0058823529, 1.0235294118, 1.0823529412, 1.0823529412, 1.0235294118, 1.0058823529, 0.9705882353},
            {0.9647058824, 0.9941176471, 1.0117647059, 1.0352941176, 1.0352941176, 1.0117647059, 0.9941176471, 0.9647058824},
            {0.9647058824, 0.9941176471, 1.0117647059, 1.0352941176, 1.0352941176, 1.0117647059, 0.9941176471, 0.9647058824},
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
            0.153893051668115,
            0.287174588749259,
            0.413645344658276,
            0.535886731268147,
            0.655076493418099,
            0.771889506723570,
            0.886762354257207,
            1.000000000000000,
    };

    public static final double[] bishopMoveValues = {
            0.000000000000000,
            0.099414786213910,
            0.185514550768313,
            0.267214556224841,
            0.346182392553933,
            0.423178881963853,
            0.498639993553287,
            0.572847759632084,
            0.645999186683931,
            0.718239426726723,
            0.789679716387871,
            0.860407920355484,
            0.930495129769836,
            1.000000000000000,
    };

    public static final double[] rookMoveValues = {
            0.000000000000000,
            0.093000389594105,
            0.173544863434152,
            0.249973457475975,
            0.323846166191628,
            0.395874722357240,
            0.466466965667216,
            0.535886731268147,
            0.604318314479001,
            0.671897501883735,
            0.738728352949557,
            0.804893063198253,
            0.870458136859159,
            0.935478444765716,
            1.000000000000000,
    };

    public static final double[] queenMoveValues = {
            0.000000000000000,
            0.051495895196886,
            0.096094738294832,
            0.138414548846169,
            0.179319122284347,
            0.219202557135275,
            0.258290681164316,
            0.296729584373003,
            0.334621314209439,
            0.372041058011301,
            0.409046435272887,
            0.445682959050580,
            0.481987453865644,
            0.517990302630457,
            0.553716983570032,
            0.589189156452233,
            0.624425451657621,
            0.659442056944192,
            0.694253162661607,
            0.728871305550240,
            0.763307638360257,
            0.797572144212075,
            0.831673809119878,
            0.865620762376747,
            0.899420391926941,
            0.933079440039514,
            0.966604083300704,
            1.000000000000000,
    };
    //#endregion

    public double[][][] pieceWorthArray = new double[12][8][8];
    public long[][][] pieceUsedArray = new long[12][8][8];
    public double[][][] processed = new double[12][8][8];

    static final double MB_FACTOR = 0.3;
    static final double BV_FACTOR = 0.9;

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

    public static int getBaseEval(byte content){
        int factor = (content > 6) ? -1 : 1;

        boolean isBlack = (content > 6);

        byte piece = isBlack ? (byte) (content - 6) : content;

        return switch (piece) {
            case 1 -> factor;
            case 2 -> 3 * factor;
            case 3 -> 4 * factor;
            case 4 -> 5 * factor;
            case 5 -> 9 * factor;
            case 6 -> 69 * factor;
            default -> 0;
        };
    }

    public static int getWorthInt(byte content){
        boolean isBlack = (content > 6);

        byte piece = isBlack ? (byte) (content - 6) : content;

        return switch (piece) {
            case 1 -> 1;
            case 2 -> 3;
            case 3 -> 4;
            case 4 -> 5;
            case 5 -> 9;
            default -> 0;
        };
    }

    public double evalPawn(byte x, byte y, boolean isBlack) {
        double baseValue = 1.0;

        // Bonus for advancing pawns
        double advancementBonus = isBlack ? (7 - y) * 0.1 : y * 0.1;

        // Central control bonus
        double centralControlBonus = (x > 2 && x < 6) ? 0.2 : 0;

        // Adjust for black pawns
        double pawnValue = baseValue + advancementBonus + centralControlBonus;
        return isBlack ? -pawnValue : pawnValue;
    }

    public double evalKnight(byte x, byte y, boolean isBlack, byte pawns) {
        final double baseValue = isBlack ? -3.1 : 3.1; //-knightBaseValues[y][x] : knightBaseValues[y][x];

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
        Branch.currentSearch = depth;
        Main.initializeHistoryTable();
        Branch.evaluationCount = 0;
        enginePosition.loadFEN(position.generateFEN());
        Branch root = new Branch(enginePosition, this);

        if (enginePosition.getPossibleMovesBoard(enginePosition.isActiveWhite()).isEmpty()) {
            System.out.println("No moves available!");
            return null;
        }

        for (int d = 1; d <= depth; d+=1){
            if (position.isActiveWhite()) root.maxi(-Double.MAX_VALUE, Double.MAX_VALUE, d, true);
            else root.mini(-Double.MAX_VALUE, Double.MAX_VALUE, d, true);
            System.out.printf("Engine reached depth %,d. With %,d evaluations.%n", d, Branch.evaluationCount);
        }

        return root.getBestChild();
    }

    public double qSearch(int depth, Position position){
        Branch root = new Branch(enginePosition, this);
        return root.qSearch(-Double.MAX_VALUE, Double.MAX_VALUE);
    }

    public boolean isOnlyPawns(){
        boolean isOnly = true;
        for (byte x = 0; x<8; x++){
            for (byte y = 0; y<8; y++){
                byte content = enginePosition.getSquare(x, y).getContent();
                if (content != 1 && content != 7 && content != 6 && content != 12){
                    isOnly = false;
                    break;
                }
            }
        }
        return isOnly;
    }
}
