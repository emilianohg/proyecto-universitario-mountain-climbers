/*
    Author: Emiliano Hernández Guerrero
    No. control: 18170410
    User: emilianohg
*/
package domain;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Audio {
    public static final double STEP_VOLUME=0.1;
    Clip sound;
    double gain;

    public Audio (String urlFile) {
        try {
            sound = AudioSystem.getClip();
            sound.open(AudioSystem.getAudioInputStream(new File(urlFile)));
            sound.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

    public void start () {
        FloatControl gainControl = (FloatControl) sound.getControl(FloatControl.Type.MASTER_GAIN);
        gain = 0.05;
        float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
        gainControl.setValue(dB);
        sound.start();
    }

    public void stop () {
        sound.stop();
    }

    public void increaseVolume () {
        setVolume(gain + STEP_VOLUME);
    }

    public void decreaseVolume () {
        setVolume(gain - STEP_VOLUME);
    }

    public void setVolume (double gain) {
        this.gain = gain;
    }
}
