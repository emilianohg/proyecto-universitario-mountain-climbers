package domain;

import utils.ImageUtils;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Player extends GameGraphics {

    private final List<PlayerAnimation> animations;
    private PlayerAnimation defaultAnimation;
    private PlayerAnimation currentAnimation;
    boolean isMoved = false;
    List<Coordinate> moves;

    public Player(String urlImage) {
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

        Coordinate m = nextMove();
        if (m == null) {
            isMoved = false;
        } else {
            setCoordinates(m.getX(), m.getY());
            moves.remove(0);
        }

        if (defaultAnimation == null) {
            return super.getImage();
        }

        PlayerAnimation animation = currentAnimation;

        if (animation == null) {
            animation = defaultAnimation;
        }

        BufferedImage image = animation.getFrame();

        if (image == null) {
            defaultAnimation.reset();
            image = defaultAnimation.getFrame();
        }

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
        if (moves.size() > 0) {
            Coordinate currentCoordinate = moves.get(moves.size() - 1);
            currentX = currentCoordinate.getX();
            currentY = currentCoordinate.getY();
        }

        float stepX = ((float)x - (float)currentX) / (float)steps;
        float stepY = ((float)y - (float)currentY) / (float)steps;

        for (float i = 1; i <= steps; i++) {
            Coordinate coord = new Coordinate(currentX +  (int)(i*stepX), currentY + (int)(i*stepY));
            if (i == steps) {
                coord = new Coordinate(x, y);
            }
            moves.add(coord);
        }

    }

    public void moveTo (int x, int y) {
        moveTo(x, y, 1);
    }

    public Coordinate nextMove () {
        if (moves.size() == 0) {
            return null;
        }
        return moves.get(0);
    }

    public void resetMovements () {
        moves.clear();
    }

    public void setAnimation (String name) {
        Optional<PlayerAnimation> animation = animations.stream()
            .filter(playerAnimation -> playerAnimation.getName().equals(name))
            .findFirst();

        currentAnimation = animation.orElse(defaultAnimation);
        currentAnimation.reset();
    }

}
