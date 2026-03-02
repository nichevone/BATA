import javax.sound.sampled.*;
import java.net.*;

import java.io.IOException;

public class Receiver implements Loggable {
    private final int TIMEOUT_AMOUNT_MILLIS = 1000; // 1 second
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

    private final Object addressLock = new Object();

    public void receive(int port) {
        // Reset isOpened state
        isOpened = true;

        try (DatagramSocket socket = new DatagramSocket(port)) {
            log(receiverType, "Socket for receiving is on port: " + port);
            log(receiverType, "Your IP-address:\n" + InetAddress.getLocalHost().getHostAddress());
            socket.setSoTimeout(TIMEOUT_AMOUNT_MILLIS);

            // Setting speakers
            DataLine.Info speakerInfo = new DataLine.Info(SourceDataLine.class, format);
            SourceDataLine speaker = (SourceDataLine) AudioSystem.getLine(speakerInfo);

            // Buffer var. for receiving audio
            byte[] buffer = new byte[BUFFER_SIZE];
            DatagramPacket receivePacket = new DatagramPacket(
                    buffer, 0, BUFFER_SIZE
            );

            // Receive first packet to establish connection
            log(receiverType, "Waiting for first packet...");
            boolean gotFirstPackage = false;
            while (isOpened && !gotFirstPackage) {
                try {
                    socket.receive(receivePacket);
                    gotFirstPackage = true;
                }
                catch (SocketTimeoutException e) { /* Continue the loop */ }
            }

            if (!isOpened) {
                log(receiverType, "Connection wasn't established");
                synchronized (addressLock) {
                    addressLock.notify();
                }
                return;
            }

            log(receiverType, "Connection established");

            // Set sender address so handler could see it
            synchronized (addressLock) {
                senderAddress = receivePacket.getAddress();
                addressLock.notify();
            }

            // Starting speakers
            speaker.open(format);
            speaker.start();

            log(receiverType, "Receiving from:\n" + senderAddress);
            while (isOpened) {
                try {
                    socket.receive(receivePacket);
                }
                catch (SocketTimeoutException e) { /* Continue the loop */ }
                speaker.write(buffer, 0, receivePacket.getLength());
            }

            speaker.stop();
            speaker.close();
            senderAddress = null;
            log(receiverType, "Closed receive socket");

        }
        catch (SocketException e) {
            System.err.println("SocketException in receiver:\n" + e.getMessage());
        }
        catch (LineUnavailableException e) {
            System.err.println("LineUnavailableException in receiver:\n" + e.getMessage());
        }
        catch (IOException e) {
            System.err.println("IOException in receiver:\n" + e.getMessage());
        }
    }

    public void close() {
        isOpened = false;
    }

    public InetAddress waitForSenderAddress() throws InterruptedException {
        synchronized (addressLock) {
            // Wait for address availability
            while (senderAddress == null && isOpened) {
                addressLock.wait();
            }
            return senderAddress;
        }
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
