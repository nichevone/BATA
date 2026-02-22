import javax.sound.sampled.*;
import java.net.*;

import java.io.IOException;

public class Receiver implements Loggable {
    private final int BUFFER_SIZE = 1024;
    private final AudioFormat format = new AudioFormat(
            8000.0f, // Sample rate,
            16, // Sample size in bits
            1, // Channels
            true, // Signed
            false // Little endian
    );

    private InfoLogger logger;
    private InetAddress senderAddress = null;
    private volatile boolean isOpened = true;

    public void receive(int port) {
        // Reset isOpened state
        isOpened = true;

        try (DatagramSocket socket = new DatagramSocket(port)) {
            log(receiverType, "Socket for receiving's on port: " + port);

            // Setting speakers
            DataLine.Info speakerInfo = new DataLine.Info(SourceDataLine.class, format);
            SourceDataLine speaker = (SourceDataLine) AudioSystem.getLine(speakerInfo);

            log(receiverType, "Your IP-address:\n" + InetAddress.getLocalHost().getHostAddress());

            // Buffer var. for receiving audio
            byte[] buffer = new byte[BUFFER_SIZE];
            DatagramPacket receivePacket = new DatagramPacket(
                    buffer, 0, BUFFER_SIZE
            );

            // Receive first packet to establish connection
            log(receiverType, "Waiting for first packet...");
            socket.receive(receivePacket);

            // Set sender address so handler could see it
            senderAddress = receivePacket.getAddress();
            log(receiverType, "Receiving from:\n" + senderAddress);

            // Starting speakers
            speaker.open(format);
            speaker.start();

            log(receiverType, "Receiving is started");

            while (isOpened) {
                socket.receive(receivePacket);
                speaker.write(buffer, 0, receivePacket.getLength());
            }

            speaker.stop();
            speaker.close();
            senderAddress = null;
            log(receiverType, "Closed receive socket");

        }
        catch (SocketException e) {
            System.err.println("SocketException:\n" + e.getMessage());
            Thread.currentThread().interrupt();
        }
        catch (LineUnavailableException e) {
            System.err.println("LineUnavailableException:\n" + e.getMessage());
        }
        catch (IOException e) {
            System.err.println("IOException:\n" + e.getMessage());
        }
    }

    public boolean isOpened() {
        return isOpened;
    }
    public void close() {
        isOpened = false;
    }
    public InetAddress getSenderAddress() {
        return senderAddress;
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
