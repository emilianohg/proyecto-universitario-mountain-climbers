package views;

import domain.*;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class GameCanvas extends Canvas {
    private Scene scene;
    private Player player;
    private final int STEPS = 10;
    private final int BASE_Y = 300;
    private Game game;
    private int session;
    private int position;

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

    public GameCanvas(int width, int height, int session) {
        setSize(width, height);
        setVisible(true);

        this.session = session;
        game = Game.getInstance(this.session);

        new Timer(80, actionEvent -> {

            if (player == null) {
                return;
            }

            Coordinate nextCoord = player.nextMove();
            if (currentStatus.equals("JUMPING") && !endGame) {
                endGame = true;
                player.moveTo(70, 230, 5);
                player.moveTo(90, 250, 5);
                position = game.getPosition(this.session);
                System.out.println(position);
            }

            if (!endGame && currentStatus.equals("CLIMBING") && nextCoord != null && nextCoord.getY() <= BASE_Y) {
                player.resetMovements();
                player.moveTo(player.getX(), BASE_Y);
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
                    player.up(increment, STEPS);
                }
                break;
            case "JUMPING":
                break;
        }

    }

    public void start () {
        currentStatus = "CLIMBING";
        player.setAnimation("climb");
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
        if (position > 0) {
            g.setFont(new Font("Courier", Font.BOLD, 46));
            g.setColor(new Color(66, 167, 56, 255));
            FontMetrics fm = g.getFontMetrics();
            String text = String.valueOf(position);
            int w = fm.stringWidth(text);
            int h = fm.getAscent();
            g.drawString(text, getWidth() - w - 10, 30 + (h / 4));


            if (position <= 3) {
                int posX = (getWidth()/2)-64;
                int posY = 40;
                Item award = new Item("src/assets/awards/win-"+position+".png", posX, posY);
                g.drawImage(award.getImage(), award.getX(), award.getY(), this);
            }

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