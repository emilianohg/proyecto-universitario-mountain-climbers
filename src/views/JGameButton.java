/*
    Author: Emiliano Hernández Guerrero
    No. control: 18170410
    User: emilianohg
*/
package views;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class JGameButton extends JButton {
    protected BufferedImage image;

    public JGameButton (String icon, String text) {
        setIcon(icon);
        setText(text);
        Color colorYellow = new Color(255, 247, 74, 255);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 3),
            BorderFactory.createLineBorder(colorYellow, 5))
        );
        setFont(new Font("Courier", Font.BOLD, 36));
        setBackground(colorYellow);
        setFocusable(false);
        setVisible(true);
    }

    public void setIcon(String icon) {
        try {
            image = ImageIO.read(new File(icon));
            super.setIcon(new ImageIcon(image));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
