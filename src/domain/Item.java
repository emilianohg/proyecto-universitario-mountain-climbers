/*
    Author: Emiliano Hernández Guerrero
    No. control: 18170410
    User: emilianohg
*/
package domain;

public class Item extends GameGraphics {

    public Item(String urlImage, int x, int y) {
        super(urlImage);
        this.setCoordinates(x, y);
    }

}