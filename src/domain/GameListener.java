package domain;

public interface GameListener {
    public void notifyEndGame();

    public void notifyReset();

    public void notifyNewGameLoad();
}
