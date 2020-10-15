package domain;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Scene {
    List<SceneLayout> layouts;
    List<Tile> tiles;
    BufferedImage image;

    public Scene(SceneLayout[] layouts) {
        this(Arrays.asList(layouts));
    }

    public Scene(List<SceneLayout> layouts) {
        this.layouts = layouts;
        this.tiles = new ArrayList<>();
    }

    public void addTile (Tile tile) {
        tiles.add(tile);
    }

    public BufferedImage getImage () {

        if (image == null) {
            image = calcScene();
        }
        return image;

    }

    private BufferedImage calcScene () {
        if (layouts.size() == 0) {
            return null;
        }

        BufferedImage image = layouts.get(0).getImage();
        int w = image.getWidth();
        int h = image.getHeight();

        BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        layouts.forEach(sceneLayout -> blendImage(combined, sceneLayout));

        tiles.forEach(tile -> blendImage(combined, tile));

        return combined;
    }

    private void blendImage (BufferedImage combined, GameGraphics gg) {
        Graphics g = combined.getGraphics();

        g.drawImage(combined, 0, 0, null);
        g.drawImage(gg.getImage(), gg.getX(), gg.getY(), null);

        g.dispose();
    }


}