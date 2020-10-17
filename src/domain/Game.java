package domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Game {

    private int[] positions;
    private int lastPosition;
    private boolean endGame;
    private List<GameListener> listeners;

    private static Game instance;

    private Game(int numberPlayer) {
        positions = new int[numberPlayer];
        listeners = new ArrayList<>();
        reset();
    }

    public static Game getInstance(int numberPlayer) {
        if (instance == null) {
            instance = new Game(numberPlayer);
        }
        return instance;
    }

    public int getPosition (int number) {
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
