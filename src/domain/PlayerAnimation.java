package domain;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerAnimation {

    private final String name;
    private List<BufferedImage> images;
    private int duration;
    private int cursor;
    private boolean stoped = false;

    public PlayerAnimation (String name, String[] urlImages, int duration) {
        this.name = name;
        this.duration = duration;

        images = new ArrayList<>();

        try {
            for (String urlImage : urlImages) {
                BufferedImage image = ImageIO.read(new File(urlImage));
                images.add(image);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getFrame () {
        BufferedImage image = images.get(cursor);

        if (stoped) {
            return image;
        }

        cursor++;
        if (cursor >= images.size()) {
            cursor = 0;
        }

        return image;
    }

    public String getName() {
        return name;
    }

    public void stop () {
        this.stoped = true;
    }
}
