package domain;

import utils.ImageUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SceneLayout extends GameGraphics {

    public SceneLayout(String urlImage) {
        super(urlImage);
    }

    @Override
    public BufferedImage getImage() {

        BufferedImage image = ImageUtils.copyImage(super.getImage());
        width = image.getWidth();
        height = image.getHeight();

        Graphics2D g2d = image.createGraphics();

        g2d.drawImage(image, x, y, width, height, null);

        g2d.dispose();

        return image;
    }
}
