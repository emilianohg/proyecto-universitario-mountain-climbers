/*
    Author: Emiliano Hernández Guerrero
    No. control: 18170410
    User: emilianohg
*/
package domain;

public class Tile extends GameGraphics {

    public Tile(String urlImage, int x, int y) {
        super(urlImage);
        this.setCoordinates(x, y);
    }

}
