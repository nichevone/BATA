import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;

// Exceptions
import java.net.SocketException;
import java.io.IOException;

public class TestClient {
    public static void main(String[] args) {
        int port = 8080;

        try {
            // Creating socket
            DatagramSocket socket = new DatagramSocket();
            System.out.println("Sending on port: " + port);

            // Sending some test message to server
            String message = "This is the test message, program should not shut down this moment";
            byte[] data = message.getBytes();
            DatagramPacket packet = new DatagramPacket(
                    data, data.length, InetAddress.getLocalHost(), port);
            socket.send(packet);

            // Receiving response from server
            byte[] buffer = new byte[1024];
            DatagramPacket response = new DatagramPacket(buffer, buffer.length);
            socket.receive(response);
            String received = new String(response.getData(), 0, response.getLength());
            System.out.println("Got response: " + received);

            // Testing sending message again
            message = "quit";
            data = message.getBytes();
            packet = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 8080);
            socket.send(packet);

            socket.close();

        } catch (SocketException e) {
            System.out.println("SocketException-main: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException-main: " + e.getMessage());
        }
    }
}
