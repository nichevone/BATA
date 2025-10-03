// Audio libraries
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.SourceDataLine;

// File libraries for storing
import java.io.File;
import java.io.FileWriter;

// Exceptions
import javax.sound.sampled.LineUnavailableException;
import java.io.FileNotFoundException;
import java.io.IOException;

// Debugging
import java.util.Arrays;

public class AudioLoopback {
    public static void main(String[] args) {
        // Creating audio format
        AudioFormat format = new AudioFormat(
                8000.0f, // Sample rate,
                16, // Sample size in bits
                1, // Channels
                true, // Signed
                false // Little endian
        );
        try {
            // Setting microphone
            DataLine.Info micInfo = new DataLine.Info(TargetDataLine.class, format);
            TargetDataLine microphone = (TargetDataLine) AudioSystem.getLine(micInfo);
            microphone.open(format);
            microphone.start();

            // Setting speakers
            DataLine.Info speakerInfo = new DataLine.Info(SourceDataLine.class, format);
            SourceDataLine speaker = (SourceDataLine) AudioSystem.getLine(speakerInfo);
            speaker.open(format);
            speaker.start();

            System.out.println("Audio loopback starting...");

            // Some data for storing voice
            byte[] buffer = new byte[1024];

            File file = new File("src/main/java/voice_temp.txt");
            if (file.createNewFile()) {
                System.out.println("Temp file created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }
            //file.deleteOnExit();

            FileWriter fw = new FileWriter(file);

            long startTime = System.currentTimeMillis();
            long duration = 5_000; // In millis => 5 seconds

            System.out.println("Speak!");
            // Audio loopback itself
            while (System.currentTimeMillis() - startTime < duration) {
                int bytesRead = microphone.read(buffer, 0 , buffer.length);

                System.out.println(Arrays.toString(buffer));

                // Write buffer to the file
                for (byte data : buffer) {
                    fw.write(Byte.toString(data)+", ");
                }
                fw.append('\n');

                if (bytesRead > 0) {
                    speaker.write(buffer, 0, buffer.length);
                }
            }

            System.out.println("Audio loopback stopped");

            microphone.stop();
            microphone.close();
            speaker.stop();
            speaker.close();

            fw.close();

        } catch (LineUnavailableException e) {
            System.out.println("LineUnavailableException-main: " + e.getMessage());
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException-main: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException-main: " + e.getMessage());
        }
    }
}
