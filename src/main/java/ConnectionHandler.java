import java.net.InetAddress;
import java.util.Scanner;

import java.net.UnknownHostException;
import java.util.InputMismatchException;

public class ConnectionHandler {
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        Receiver receiver = new Receiver();
        
        Sender sender = new Sender();

        System.out.println("""
                
                Welcome to BATA (Basic Audio Transmitting Application)!
                
                --- Please, choose action: ---1
                
                1 - Host (receiver, people will connect to you)
                2 - Connect (sender, you will connect to host)""");

        int choice = sc.nextInt();

        try {
            System.out.println("Enter port:");
            int port = sc.nextInt();

            switch (choice) {

                case 1: // Host
                    new Thread(() -> receiver.receive(port)).start();
                    while (receiver.getSenderAddress() == null) {
                        Thread.sleep(2000);
                    }
                    System.out.println();
                    new Thread(() -> sender.send(port, receiver.getSenderAddress())).start();
                    break;

                case 2: // Connect
                    System.out.println("Enter host's IP-address (IPv4): ");
                    InetAddress receiverAddress = InetAddress.getByName(sc.next());

                    new Thread(() -> sender.send(port, receiverAddress)).start();
                    new Thread(() -> receiver.receive(port)).start();
                    break;

                default:
                    System.err.println("Invalid choice.");
            }

            initiateExit(sender, receiver);

        } catch (InputMismatchException e) {
            System.err.println("InputMismatchException in main. Port input may be invalid.\n" + e.getMessage());
        } catch (UnknownHostException e) {
            System.err.println("UnknownHostException in main. Host's IP-address is unknown.\n" + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("InterruptedException in main. Thread got interrupted while waiting for sender address.\n" + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    public static void initiateExit(Sender sender, Receiver receiver) {
        System.out.println("To quit enter: ':q'\n");
        while (sc.hasNext()) {
            if (sc.next().equals(":q")) {
                System.out.println("Closing sockets:");
                sender.close();
                receiver.close();
            }
        }
    }

}
