// Audio libraries
import javax.sound.sampled.*;

// Network libraries
import java.net.*;

// Exceptions
import java.io.IOException;

public class AudioSender {
    private static final int CLIENT_PORT = 8080;
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) {
        AudioFormat format = new AudioFormat(
                8000.0f, // Sample rate,
                16, // Sample size in bits
                1, // Channels
                true, // Signed
                false // Little endian
        );

        try {
            DatagramSocket socket = new DatagramSocket();
            System.out.println("Socket for sending created successfully");

            // Setting microphone
            DataLine.Info micInfo = new DataLine.Info(TargetDataLine.class, format);
            TargetDataLine microphone = (TargetDataLine) AudioSystem.getLine(micInfo);
            microphone.open(format);
            microphone.start();

            final InetAddress localAddress = InetAddress.getLocalHost();
            InetAddress receiverAddress = InetAddress.getByName("192.168.0.103");
            System.out.println("Your IP address: " + localAddress);
            System.out.println("Socket is sending data to address: " + receiverAddress);

            byte[] buffer = new byte[BUFFER_SIZE];
            DatagramPacket packetSend = new DatagramPacket(
                    buffer, BUFFER_SIZE,
                    receiverAddress, CLIENT_PORT
            );

            System.out.println("Sending is started");

            long startTime = System.currentTimeMillis();
            long duration = 10_000; // In millis => 10 seconds
            while (System.currentTimeMillis() - startTime < duration) {
                int bytesRead = microphone.read(buffer, 0, BUFFER_SIZE);
                if (bytesRead > 0) {
                    socket.send(packetSend);
                }
            }

            System.out.println("Closing socket...");
            microphone.stop();
            microphone.close();
            socket.close();

        } catch (SocketException e) {
            System.out.println("SocketException-main: " + e.getMessage());
        } catch (LineUnavailableException e) {
            System.out.println("LineUnavailableException-main: " + e.getMessage());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println("IOException-main: " + e.getMessage());
        }
    }
}
