import java.net.InetAddress;
import java.util.Scanner;

import java.net.UnknownHostException;
import java.util.InputMismatchException;

public class ConnectionHandler {
    private static final Scanner sc = new Scanner(System.in);
    private final Receiver receiver = new Receiver();
    private final Sender sender = new Sender();

    public void host(int port) {
        try {
            new Thread(() -> receiver.receive(port)).start();
            while (receiver.getSenderAddress() == null) {
                // If user disconnects while waiting for receiver
                if (!receiver.isOpened()) {
                    return;
                }
                Thread.sleep(2000);
            }
            System.out.println();
            new Thread(() -> sender.send(port, receiver.getSenderAddress())).start();

        } catch (InterruptedException e) {
            System.err.println("InterruptedException in main. Thread got interrupted while waiting for sender address.\n" + e.getMessage());
        }
    }

    public void connect(int port, String address) {
        try {
            InetAddress receiverAddress = InetAddress.getByName(address);

            new Thread(() -> sender.send(port, receiverAddress)).start();
            new Thread(() -> receiver.receive(port)).start();

        } catch (InputMismatchException e) {
            System.err.println("InputMismatchException in main. Port input may be invalid.\n" + e.getMessage());
        } catch (UnknownHostException e) {
            System.err.println("UnknownHostException in main. Host's IP-address is unknown.\n" + e.getMessage());
        }
    }

    public void disconnect() {
        System.out.println("Closing sockets:");
        sender.close();
        receiver.close();
    }

}
