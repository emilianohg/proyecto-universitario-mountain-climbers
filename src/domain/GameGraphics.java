/*
    Author: Emiliano Hernández Guerrero
    No. control: 18170410
    User: emilianohg
*/
package domain;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameGraphics {
    protected BufferedImage image;

    protected int x, y;
    protected float scale = 1;
    protected int height, width;

    public GameGraphics(String urlImage) {
        try {
            image = ImageIO.read(new File(urlImage));
            width = image.getWidth();
            height = image.getHeight();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getImage() {
        return image;
    }

    public GameGraphics setCoordinates (int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public GameGraphics setCoordinates (Coordinate coordinates) {
        return setCoordinates(coordinates.getX(), coordinates.getY());
    }

    public GameGraphics setScale (float scale) {
        this.scale = scale;
        return this;
    }

    public GameGraphics setHeightMaintainScale (int height) {
        return setScale((float) height/ (float) this.height);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHeight() {
        return (int)(height * scale);
    }

    public int getWidth() {
        return (int)(width * scale);
    }
}
