/*
    Author: Emiliano Hernández Guerrero
    No. control: 18170410
    User: emilianohg
*/
package views;

import domain.*;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.util.Random;

public class GameCanvas extends Canvas {
    private Scene scene;
    private Player player;
    private Coordinate positionInitial;
    private final Game game;
    private final int session;
    private int position;
    private int increment;
    Item arrowUp, arrowDown;

    private boolean load = false;

    private GameStatus currentStatus = GameStatus.WAITING;

    Image offscreen;
    Graphics currentGraphic;

    boolean endGame = false;

    public GameCanvas(int width, int height, int session) {
        setSize(width, height);
        setVisible(false);

        this.session = session;
        game = Game.getInstance(this.session);

        arrowDown = new Item("src/assets/extras/arrow-down.png", getWidth()-40, getHeight()-52);
        arrowUp = new Item("src/assets/extras/arrow-up.png", getWidth()-40, getHeight()-52);

        positionInitial = new Coordinate(30, getHeight() - 120);
        positionInitial = new Coordinate(30, getHeight() - 120);

        new Timer(80, actionEvent -> {

            if (player == null) {
                return;
            }

            game();
            repaint();

            if (!load) {
                game.loadGame();
                load = true;
            }

        }).start();
    }

    private void game () {

        Coordinate nextCoord = player.nextMove();

        if (currentStatus.equals(GameStatus.GLIDE) && player.getY() == positionInitial.getY()) {
            player.setAnimation("idle");
            currentStatus = GameStatus.WAITING;
            game.reset();
        }

        if (currentStatus.equals(GameStatus.JUMPING) && !endGame) {
            endGame = true;
            player.moveTo(70, 230, 5);
            player.moveTo(90, 250, 5);
            position = game.getPosition(this.session);
        }

        int BASE_Y = 300;

        if (!endGame && currentStatus.equals(GameStatus.CLIMBING) && nextCoord != null && nextCoord.getY() <= BASE_Y) {
            player.resetMovements();
            player.moveTo(player.getX(), BASE_Y);
            currentStatus = GameStatus.JUMPING;
            player.setAnimation("jump");
        }

        if (currentStatus.equals(GameStatus.CLIMBING)) {
            if (player.nextMove() == null) {
                increment = calcIncrement();
                player.up(increment, 10);
            }
        }

    }

    public void start () {
        currentStatus = GameStatus.CLIMBING;
        player.setAnimation("climb");
    }

    public void restart () {
        currentStatus = GameStatus.GLIDE;
        player.setAnimation("glide");
        player.moveTo(positionInitial.getX()-10, positionInitial.getY(), 50);
        player.moveTo(positionInitial.getX(), positionInitial.getY());
        endGame = false;
        position = 0;
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
        this.player.setCoordinates(positionInitial);
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

        Font font = new Font("Courier", Font.BOLD, 26);
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();
        String text = String.valueOf(increment);
        int w = fm.stringWidth(text);
        int h = fm.getAscent();

        if (currentStatus.equals(GameStatus.CLIMBING)) {

            g.setStroke(new BasicStroke(4.0f));
            FontRenderContext frc = g.getFontRenderContext();
            GlyphVector gv = font.createGlyphVector(frc, text);

            int currentX = getWidth() - w - 42;
            int currentY = getHeight() - h;

            if (increment > 0) {
                g.translate(currentX, currentY);

                g.setColor(Color.WHITE);
                g.draw(gv.getOutline());
                g.setColor(new Color(11, 188, 201, 255));
                g.fill(gv.getOutline());

                g.translate(-currentX, -currentY);
                g.drawImage(arrowUp.getImage(), arrowUp.getX(), arrowUp.getY(), this);
            }

            if (increment < 0 && currentStatus.equals(GameStatus.CLIMBING)) {
                g.translate(currentX, currentY);

                g.setColor(Color.BLACK);
                g.draw(gv.getOutline());
                g.setColor(new Color(244, 67, 54, 255));
                g.fill(gv.getOutline());

                g.translate(-currentX, -currentY);
                g.drawImage(arrowDown.getImage(), arrowDown.getX(), arrowDown.getY(), this);
            }

        }

        if (position > 1) {
            int posX = (getWidth()/2)-32;
            int posY = 60;
            Item award = new Item("src/assets/awards/win-"+position+".png", posX, posY);
            g.drawImage(award.getImage(), award.getX(), award.getY(), this);
        }

        if (position == 1) {
            int posX = (getWidth()/2)-64;
            int posY = 40;
            Item award = new Item("src/assets/awards/win-"+position+".png", posX, posY);
            g.drawImage(award.getImage(), award.getX(), award.getY(), this);
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

        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        super.paint(g2d);
        draw(g2d);
        g.drawImage(offscreen, 0, 0, getWidth(), getHeight(), this);
    }

}