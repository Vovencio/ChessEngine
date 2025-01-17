import java.io.IOException;

/**
 * Engine Class used for all computing.
 *
 * @author Vovencio
 * @version 01/01/2024
 */

public class EngineAI extends Engine {
    Position enginePosition;

    public EngineAI() {
        enginePosition = new Position();
        enginePosition.setupInitialBoard();
    }

    @Override
    public double evalBoard() {
        Branch.evaluationCount++;
        int[] inData = enginePosition.toTrainingData();

        try {
            return JavaToPython.comm.eval(inData) * (enginePosition.isActiveWhite() ? 1 : -1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Branch generateBestMove(int depth, Position position){
        Main.initializeHistoryTable();
        Branch.evaluationCount = 0;
        enginePosition.loadFEN(position.generateFEN());
        Branch root = new Branch(enginePosition, this);

        if (enginePosition.getPossibleMovesBoard(enginePosition.isActiveWhite()).isEmpty()) {
            System.out.println("No moves available!");
            return null;
        }

        for (int d = 1; d <= depth; d+=1){
            Branch.currentSearch = d;
            root.negaMax(-Double.MAX_VALUE, Double.MAX_VALUE, d, true);
            //else root.mini(-Double.MAX_VALUE, Double.MAX_VALUE, d, true);
            System.out.printf("Engine reached depth %,d. With %,d evaluations.%n", d, Branch.evaluationCount);
        }

        return root.getBestChild();
    }

    @Override
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
