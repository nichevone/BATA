import javax.sound.sampled.*;
import java.net.*;

import java.io.IOException;

public class Receiver implements Loggable {
    private final int TIMEOUT_AMOUNT_MILLIS = 500; // 0.5 seconds
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
            socket.setSoTimeout(TIMEOUT_AMOUNT_MILLIS);
            log(receiverType, "Socket for receiving is on port: " + port);
            log(receiverType, "Your IP-address:\n" + InetAddress.getLocalHost().getHostAddress());

            // Buffer array for receiving audio
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

            // Set sender address so handler could see it.
            // It can only be null or the real address
            senderAddress = receivePacket.getAddress();

            synchronized (addressLock) {
                addressLock.notify();
                if (!isOpened) {
                    // Connection was closed before it was established
                    log(receiverType, "Connection wasn't established");
                    return;
                }
            }
            log(receiverType, "Connection established");
            log(receiverType, "Receiving from:\n" + senderAddress);

            // Setting speakers
            DataLine.Info speakerInfo = new DataLine.Info(SourceDataLine.class, format);
            SourceDataLine speaker = (SourceDataLine) AudioSystem.getLine(speakerInfo);

            // Starting speakers
            speaker.open(format);
            speaker.start();

            int lastLength;
            while (isOpened) {
                try {
                    socket.receive(receivePacket);
                    lastLength = receivePacket.getLength();
                }
                catch (SocketTimeoutException e) {
                    lastLength = 0;
                }

                if (lastLength > 1) {
                    speaker.write(buffer, 0, receivePacket.getLength());
                }
            }

            speaker.drain();
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
