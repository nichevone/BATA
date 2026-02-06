import javax.sound.sampled.*;
import java.net.*;

import java.io.IOException;

public class Sender {
    final int BUFFER_SIZE = 1024;
    final AudioFormat format = new AudioFormat(
            8000.0f, // Sample rate,
            16, // Sample size in bits
            1, // Channels
            true, // Signed
            false // Little endian
    );
    private volatile boolean isOpened = true;

    public void close() {
        isOpened = false;
    }

    public void send(int port, InetAddress receiverAddress) {
        // Reset isOpened state
        isOpened = true;

        try (DatagramSocket socket = new DatagramSocket()) {
            System.out.println("S: Socket for sending created successfully");

            // Setting microphone
            DataLine.Info micInfo = new DataLine.Info(TargetDataLine.class, format);
            TargetDataLine microphone = (TargetDataLine) AudioSystem.getLine(micInfo);

            // Receiver information
            System.out.println("S: Socket is sending data to address: " + receiverAddress);

            // Buffer var. for receiving audio
            byte[] buffer = new byte[BUFFER_SIZE];
            DatagramPacket sendPacket = new DatagramPacket(
                    buffer, BUFFER_SIZE,
                    receiverAddress, port
            );

            // Starting microphone
            microphone.open(format);
            microphone.start();

            System.out.println("S: Sending is started");

            while (isOpened) {
                int bytesRead = microphone.read(buffer, 0, BUFFER_SIZE);
                if (bytesRead > 0) {
                    socket.send(sendPacket);
                }
            }

            microphone.stop();
            microphone.close();
            System.out.println("S: Closed send socket");

        } catch (SocketException e) {
            System.err.println("SocketException while sending.\n" + e.getMessage());
        } catch (LineUnavailableException e) {
            System.err.println("LineUnavailableException while sending.\n" + e.getMessage());
        } catch (IOException e) {
            System.err.println("IOException while sending.\n" + e.getMessage());
        }
    }
}
