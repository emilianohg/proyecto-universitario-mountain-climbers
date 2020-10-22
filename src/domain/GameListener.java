/*
    Author: Emiliano Hernández Guerrero
    No. control: 18170410
    User: emilianohg
*/
package domain;

public interface GameListener {
    void notifyEndGame();

    void notifyReset();

    void notifyNewGameLoad();
}
