import java.net.InetAddress;
import java.net.UnknownHostException;

public class ConnectionHandler implements Loggable {
    private final Receiver receiver = new Receiver();
    private final Sender sender = new Sender();
    private InfoLogger logger;

    private Thread receiverThread;
    private Thread senderThread;

    public void host(int port) {
        try {
            // Start thread for receiving
            receiverThread = new Thread(() -> {
                receiver.receive(port);
            }, "host-receiver");
            receiverThread.start();

            // TODO: make this not busy-waiting
            while (receiver.getSenderAddress() == null) {
                // If user disconnects while waiting for receiver
                if (!receiver.isOpened()) {
                    return;
                }
                Thread.sleep(1000);
            }

            // Start thread for sending
            senderThread = new Thread(() -> {
                sender.send(port, receiver.getSenderAddress());
            }, "host-sender");
            senderThread.start();

        } catch (InterruptedException e) {
            log(exceptType, "InterruptedException in main. Thread got interrupted while waiting for sender address.\n" + e.getMessage());
        }
    }

    public void connect(int port, String address) {
        try {
            InetAddress receiverAddress = InetAddress.getByName(address);

            // Start thread for sending
            senderThread = new Thread(() -> {
                sender.send(port, receiverAddress);
            }, "connect-sender");
            senderThread.start();

            // Start thread for receiving
            receiverThread = new Thread(() -> {
                receiver.receive(port);
            }, "connect-receiver");
            receiverThread.start();

        } catch (UnknownHostException e) {
            log(exceptType, "UnknownHostException in main. Host's IP-address is unknown.\n" + e.getMessage());
        }
    }

    public void disconnect() {
        log(handlerType, "Closing sockets...");
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
