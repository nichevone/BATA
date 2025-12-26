// Audio libraries
import javax.sound.sampled.*;

// Network libraries
import java.net.*;

// Exceptions
import java.io.IOException;

public class AudioReceiver {
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
            DatagramSocket socket = new DatagramSocket(CLIENT_PORT);
            System.out.println("Socket is listening on port: " + CLIENT_PORT);

            // Setting speakers
            DataLine.Info speakerInfo = new DataLine.Info(SourceDataLine.class, format);
            SourceDataLine speaker = (SourceDataLine) AudioSystem.getLine(speakerInfo);
            speaker.open(format);
            speaker.start();

            byte[] buffer = new byte[BUFFER_SIZE];
            DatagramPacket packetReceive = new DatagramPacket(
                    buffer, 0, BUFFER_SIZE
            );

            System.out.println("Waiting for first packet...");
            socket.receive(packetReceive);

            final InetAddress localAddress = InetAddress.getLocalHost();
            System.out.println("Your IP address: " + localAddress);
            System.out.println("Receiving data from: " + packetReceive.getAddress());

            System.out.println("Receiving is started");

            while (socket.isBound()) {
                socket.receive(packetReceive);
                speaker.write(buffer, 0, BUFFER_SIZE);
            }

            System.out.println("Closing socket...");
            speaker.stop();
            speaker.close();
            socket.close();

        } catch (SocketException e) {
            System.out.println("SocketException-main: " + e.getMessage());
        } catch (LineUnavailableException e) {
            System.out.println("LineUnavailableException-main: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException-main: " + e.getMessage());
        }
    }
}
