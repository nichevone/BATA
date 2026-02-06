import javax.sound.sampled.*;
import java.net.*;

import java.io.IOException;

public class Receiver {
    final int BUFFER_SIZE = 1024;
    final AudioFormat format = new AudioFormat(
            8000.0f, // Sample rate,
            16, // Sample size in bits
            1, // Channels
            true, // Signed
            false // Little endian
    );
    private InetAddress senderAddress = null;
    private volatile boolean isOpened = true;

    public boolean isOpened() {
        return isOpened;
    }

    public void close() {
        isOpened = false;
    }

    public InetAddress getSenderAddress() {
        return senderAddress;
    }

    public void receive(int port) {
        // Reset isOpened state
        isOpened = true;

        try (DatagramSocket socket = new DatagramSocket(port)) {
            System.out.println("R: Socket is listening on port: " + port);

            // Setting speakers
            DataLine.Info speakerInfo = new DataLine.Info(SourceDataLine.class, format);
            SourceDataLine speaker = (SourceDataLine) AudioSystem.getLine(speakerInfo);

            // Buffer var. for receiving audio
            byte[] buffer = new byte[BUFFER_SIZE];
            DatagramPacket receivePacket = new DatagramPacket(
                    buffer, 0, BUFFER_SIZE
            );

            System.out.println("R: Your IP-address: " + InetAddress.getLocalHost());

            // Receive first packet to establish connection
            System.out.println("R: Waiting for first packet...");
            socket.receive(receivePacket);

            // Sender information
            senderAddress = receivePacket.getAddress();
            System.out.println("R: Receiving data from: " + senderAddress);

            // Starting speakers
            speaker.open(format);
            speaker.start();

            System.out.println("R: Receiving is started");

            while (isOpened) {
                socket.receive(receivePacket);
                speaker.write(buffer, 0, receivePacket.getLength());
            }

            speaker.stop();
            speaker.close();
            senderAddress = null;
            System.out.println("R: Closed receive socket");

        } catch (SocketException e) {
            System.err.println("SocketException while receiving.\n" + e.getMessage());
            Thread.currentThread().interrupt();
        } catch (LineUnavailableException e) {
            System.err.println("LineUnavailableException while receiving.\n" + e.getMessage());
        } catch (IOException e) {
            System.err.println("IOException while receiving.\n" + e.getMessage());
        }
    }

}
