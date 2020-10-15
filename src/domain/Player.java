package domain;

import utils.ImageUtils;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Player extends GameGraphics {

    private List<PlayerAnimation> animations;
    private PlayerAnimation defaultAnimation;
    boolean isMoved = false;
    List<int[]> moves;

    public Player(String urlImage) {
        this(urlImage, 0, 0);
    }

    public Player(String urlImage, int x, int y) {
        super(urlImage);
        animations = new ArrayList<>();
        moves = new ArrayList<>();
    }

    public Player addAnimations (PlayerAnimation[] animations) {
        if (animations.length == 0)
            return this;

        this.defaultAnimation = animations[0];
        this.animations.addAll(Arrays.asList(animations));
        return this;
    }

    @Override
    public BufferedImage getImage() {

        if (defaultAnimation == null) {
            return super.getImage();
        }

        Coordinate m = nextMove();
        if (m == null) {
            isMoved = false;
        } else {
            setCoordinates(m.getX(), m.getY());
            moves.remove(0);
        }

        BufferedImage image = defaultAnimation.getFrame();
        if (scale != 1) {
            image = ImageUtils.scale(
                image,
                (int) (image.getWidth() * scale),
                (int) (image.getHeight() * scale)
            );
        }
        return image;

    }

    public boolean isMoved() {
        return isMoved;
    }

    public void up (int inc, int steps) {
        moveTo(x, y - inc, steps);
    }

    public void down (int inc, int steps) {
        moveTo(x, y + inc, steps);
    }

    public void right (int inc, int steps) {
        moveTo(x + inc, y, steps);
    }

    public void left (int inc, int steps) {
        moveTo(x - inc, y + inc, steps);
    }

    public void moveTo(int x, int y, int steps) {
        isMoved = true;

        int currentX = getX();
        int currentY = getY();

        int stepX = (x - currentX) / steps;
        int stepY = (y - currentY) / steps;

        for (int i = 1; i <= steps; i++) {
            // TODO: PUEDE DAR SALTOS
            int[] coord = new int[] {currentX + i*stepX, currentY + i*stepY};
            if (i == steps) {
                coord = new int[] {x, y};
            }
            moves.add(coord);
            System.out.println(Arrays.toString(coord));
        }

    }

    public void moveTo (int x, int y) {
        moveTo(x, y, 1);
    }

    public Coordinate nextMove () {
        if (moves.size() == 0) {
            return null;
        }
        return new Coordinate(moves.get(0)[0], moves.get(0)[1]);
    }

    public void resetMovements () {
        moves.clear();
    }

    public void setAnimation (String name) {
        Optional<PlayerAnimation> animation = animations.stream()
            .filter(playerAnimation -> playerAnimation.getName().equals(name))
            .findFirst();

        defaultAnimation = animation.orElse(defaultAnimation);
    }

}
