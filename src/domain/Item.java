package domain;

public class Item extends GameGraphics {

    public Item(String urlImage, int x, int y) {
        super(urlImage);
        this.setCoordinates(x, y);
    }

}