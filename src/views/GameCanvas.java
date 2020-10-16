package views;

import domain.Coordinate;
import domain.Player;
import domain.Scene;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class GameCanvas extends Canvas {
    private Scene scene;
    private Player player;
    private final int steps = 10;
    private int wait = 40;

    private String[] status = new String[] {
        "WAITING",
        "CLIMBING",
        "JUMPING",
        "ATTACKING",
    };
    private String currentStatus = "WAITING";

    Image offscreen;
    Graphics currentGraphic;

    boolean endGame = false;

    public GameCanvas(int width, int height) {
        setSize(width, height);
        setVisible(true);

        new Timer(80, actionEvent -> {

            if (player == null) {
                return;
            }

            if (wait == 0) {
                player.setAnimation("climb");
                currentStatus = "CLIMBING";
            }
            wait--;

            Coordinate nextCoord = player.nextMove();
            if (currentStatus.equals("JUMPING")) {
                player.moveTo(150, 210);
                System.out.println("jumping");
            }


            if (currentStatus.equals("CLIMBING") && nextCoord != null && nextCoord.getY() < 250) {
                player.resetMovements();
                player.moveTo(player.getX(), 250);
                endGame = true;
                currentStatus = "JUMPING";
                player.setAnimation("jump");
            }

            game();
            repaint();

        }).start();
    }

    private void game () {

        switch (currentStatus) {
            case "WAITING":
                break;
            case "CLIMBING":
                if (player.nextMove() == null) {
                    int increment = calcIncrement();
                    player.up(increment, steps);
                }
                break;
            case "JUMPING":
                break;
        }

    }

    private int calcIncrement () {
        int n = (new Random()).nextInt(10);

        int increment;

        if (n == 0) {
            increment= -((new Random()).nextInt(20)+10);
        } else {
            increment= (new Random()).nextInt(50)+10;
        }

        return increment;
    }

    public GameCanvas addScene(Scene scene) {
        this.scene = scene;
        repaint();
        setBackground(new Color(255,255,255, 0));
        return this;
    }

    public GameCanvas addPlayer (Player player) {
        this.player = player;
        repaint();
        return this;
    }

    public void draw(Graphics2D g) {
        if (scene != null) {
            g.drawImage(scene.getImage(), 0, 0, this);
        }
        if (player != null) {
            g.drawImage(player.getImage(), player.getX(), player.getY(), this);
        }
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }

    @Override
    public void paint(Graphics g) {
        if (offscreen == null) {
            offscreen = createImage(getWidth(), getHeight());
            repaint();
            return;
        }
        if (currentGraphic == null) {
            currentGraphic = offscreen.getGraphics();
        }
        Graphics2D g2d = (Graphics2D) currentGraphic;

        // g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        // g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        super.paint(g2d);
        draw(g2d);
        g.drawImage(offscreen, 0, 0, getWidth(), getHeight(), this);
    }

}