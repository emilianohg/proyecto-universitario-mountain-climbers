package domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Game {

    private final int[] positions;
    private int lastPosition;
    private boolean endGame;
    private final List<GameListener> listeners;
    private int totalGameLoaded = 0;

    private static Game instance;

    private Game(int numberPlayer) {
        positions = new int[numberPlayer];
        listeners = new ArrayList<>();
        reset();
    }

    public static synchronized Game getInstance(int numberPlayer) {
        if (instance == null) {
            instance = new Game(numberPlayer);
        }
        return instance;
    }

    public void loadGame () {
        totalGameLoaded++;
        listeners.forEach(GameListener::notifyNewGameLoad);
    }

    public synchronized int getPosition (int number) {
        number--;

        int pos = positions[number];
        if (pos == 0) {
            lastPosition++;
            positions[number] = lastPosition;
        }

        if (lastPosition == positions.length) {
            endGame = true;
            listeners.forEach(GameListener::notifyEndGame);
        }

        return lastPosition;
    }

    public int[] getAllPositions() {
        return positions;
    }

    public void reset() {
        lastPosition = 0;
        Arrays.fill(positions, 0);
        endGame = false;
        listeners.forEach(GameListener::notifyReset);
    }

    public boolean isEndGame () {
        return endGame;
    }

    public void addListener (GameListener listener) {
        listeners.add(listener);
    }

    public void removeAllListeners () {
        listeners.clear();
    }
}
