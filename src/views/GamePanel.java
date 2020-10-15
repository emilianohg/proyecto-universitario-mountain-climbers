package views;

import domain.Player;
import domain.Scene;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends Panel {
    private Scene scene;
    private Player player;

    Image offscreen;
    Graphics currentGraphic;

    public GamePanel (int width, int height) {
        setSize(width, height);
        setPreferredSize(new Dimension(width, height));
        setVisible(true);

        Timer timer=new Timer(100, actionEvent -> repaint());
        timer.start();
    }

    public GamePanel addScene(Scene scene) {
        this.scene = scene;
        repaint();
        return this;
    }

    public GamePanel addPlayer (Player player) {
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

//    @Override
//    public void paintComponents(Graphics g) {
//        super.paintComponents(g);
//        g.setColor(Color.YELLOW);
//        g.fillRect(0, 0, getWidth(), getHeight());
//    }


    @Override
    public void update(Graphics g) {
        paint(g);
    }

    public void paint (Graphics g) {
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