public class Engine {
    public Position getEnginePosition() {
        return enginePosition;
    }

    public void setEnginePosition(Position enginePosition) {
        this.enginePosition = enginePosition;
    }

    Position enginePosition;

    public Engine () {
        enginePosition = new Position();
        enginePosition.setupInitialBoard();
    }


}
