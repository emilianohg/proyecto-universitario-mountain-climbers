package views;

import javax.swing.*;
import java.awt.*;

public class GameGlass extends JPanel {

    Image offscreen;
    Graphics currentGraphic;

    public GameGlass () {
        setSize(new Dimension(1000, 726));
        setPreferredSize(new Dimension(1000, 726));
        setBackground(new Color(0, 0, 0, .5f));
        setVisible(true);
        update(getGraphics());
    }

    public void draw(Graphics2D g) {

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
