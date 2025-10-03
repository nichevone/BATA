// UDP libraries
import java.net.DatagramSocket;
import java.net.DatagramPacket;

// Audio libraries
import javax.sound.sampled.AudioFormat;

// Exceptions
import java.net.SocketException;
import java.io.IOException;

public class Server {
    public static void main(String[] args) {
        int port = 8080;

        try {
            // Creating socket on port
            DatagramSocket socket = new DatagramSocket(port);
            System.out.println("Server is listening on port: " + port);

            while (true) {
                // Receiving data
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String message = new String(packet.getData(),0, packet.getLength());

                // Shutting down the server
                if (message.equals("quit")) {
                    System.out.println("Shutting down the server...");
                    break;
                }

                // Sending response
                DatagramPacket response = new DatagramPacket(
                        message.getBytes(),
                        message.length(),
                        packet.getAddress(),
                        packet.getPort()
                );
                socket.send(response);
            }

            socket.close();

        } catch (SocketException e) {
            System.out.println("SocketException-main: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException-main: " + e.getMessage());
        }
    }

}
