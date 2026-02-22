import javax.sound.sampled.*;
import java.net.*;

import java.io.IOException;

public class Sender implements Loggable {
    final int BUFFER_SIZE = 1024;
    final AudioFormat format = new AudioFormat(
            8000.0f, // Sample rate,
            16, // Sample size in bits
            1, // Channels
            true, // Signed
            false // Little endian
    );

    private InfoLogger logger;
    private volatile boolean isOpened = true;

    public void send(int port, InetAddress receiverAddress) {
        // Reset isOpened state
        isOpened = true;

        try (DatagramSocket socket = new DatagramSocket()) {
            log(senderType, "Socket for sending created");

            // Setting microphone
            DataLine.Info micInfo = new DataLine.Info(TargetDataLine.class, format);
            TargetDataLine microphone = (TargetDataLine) AudioSystem.getLine(micInfo);

            log(senderType, "Sending to address:\n" + receiverAddress);

            // Buffer var. for receiving audio
            byte[] buffer = new byte[BUFFER_SIZE];
            DatagramPacket sendPacket = new DatagramPacket(
                    buffer, BUFFER_SIZE,
                    receiverAddress, port
            );

            // Starting microphone
            microphone.open(format);
            microphone.start();

            log(senderType, "Sending is started");

            while (isOpened) {
                int bytesRead = microphone.read(buffer, 0, BUFFER_SIZE);
                if (bytesRead > 0) {
                    socket.send(sendPacket);
                }
            }

            microphone.stop();
            microphone.close();
            log(senderType, "Closed send socket");

        }
        // FIXME: sending not starting in some cases.
        // When SocketException is thrown, is stops sending
        catch (SocketException e) {
            System.err.println("SocketException:\n" + e.getMessage());
        }
        catch (LineUnavailableException e) {
            System.err.println("LineUnavailableException:\n" + e.getMessage());
        }
        catch (IOException e) {
            System.err.println("IOException:\n" + e.getMessage());
        }
    }
    
    public void close() {
        isOpened = false;
    }

    @Override
    public void log(char type, String message) {
        logger.log(type, message);
    }
    @Override
    public void setLogger(InfoLogger logger) {
        this.logger = logger;
    }
}
