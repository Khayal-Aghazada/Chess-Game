import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SoundPlayer {

    public void play(String fileName) {
        // Get the resource as a stream from the classpath
        try (InputStream resourceStream = getClass().getResourceAsStream("resources/sounds/" + fileName);
             BufferedInputStream buffer = new BufferedInputStream(resourceStream)) {

            if (resourceStream == null) {
                System.err.println("Could not find the file: " + fileName);
                return;
            }

            AdvancedPlayer player = new AdvancedPlayer(buffer);
            player.play();
        } catch (JavaLayerException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SoundPlayer soundPlayer = new SoundPlayer();
        soundPlayer.play("capture.mp3");
        soundPlayer.play("move-self.mp3");
    }
}