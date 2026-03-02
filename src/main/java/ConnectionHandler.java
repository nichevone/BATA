import java.net.InetAddress;
import java.net.UnknownHostException;

public class ConnectionHandler implements Loggable {
    private final Receiver receiver = new Receiver();
    private final Sender sender = new Sender();
    private InfoLogger logger;

    private boolean isMuted = false;

    private Thread receiverThread;
    private Thread senderThread;

    public void host(int port) {
        try {
            // Start thread for receiving
            receiverThread = new Thread(() ->
                    receiver.receive(port), "host-receiver");
            receiverThread.start();

            InetAddress senderAddress = receiver.waitForSenderAddress();
            if (senderAddress == null) {
                // Connection was closed before it was established
                return;
            }

            // Start thread for sending
            senderThread = new Thread(() ->
                sender.send(port, senderAddress), "host-sender");
            senderThread.start();

        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("InterruptedException in main. Thread got interrupted while waiting for sender address.\n" + e.getMessage());
        }
    }

    public void connect(int port, String address) {
        try {
            InetAddress receiverAddress = InetAddress.getByName(address);

            // Start thread for sending
            senderThread = new Thread(() ->
                sender.send(port, receiverAddress), "connect-sender");
            senderThread.start();

            // Start thread for receiving
            receiverThread = new Thread(() ->
                receiver.receive(port), "connect-receiver");
            receiverThread.start();

        }
        catch (UnknownHostException e) {
            System.err.println("UnknownHostException in main. Host's IP-address is unknown.\n" + e.getMessage());
        }
    }

    public void mute() {
        // Each click of mute button mutes or unmutes you
        // Just like it is in all voice chats
        isMuted = !isMuted;
        sender.setMuted(isMuted);
    }

    public void disconnect() {
        sender.close();
        receiver.close();

        interruptIfAlive(senderThread);
        interruptIfAlive(receiverThread);
    }

    private void interruptIfAlive(Thread t) {
        if (t != null && t.isAlive()) {
            t.interrupt();
        }
    }

    @Override
    public void log(char type, String message) {
        logger.log(type, message);
    }
    @Override
    public void setLogger(InfoLogger logger) {
        // Get logger instance from controller
        this.logger = logger;
        // Set logger instance to sender and receiver
        sender.setLogger(logger);
        receiver.setLogger(logger);
    }
}
