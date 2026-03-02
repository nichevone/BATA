import java.awt.EventQueue;

public class ConnectionController {
    private final ConnectionHandler handler = new ConnectionHandler();
    private InfoLogger logger;
    private GuiAssembler gui;

    StringBuffer info = new StringBuffer();

    public void initiateGui() {
        EventQueue.invokeLater(gui::initUI);
    }

    public void initiateConnection() {
        int port = getPort();
        String address = getAddress();

        if (gui.isCurrentCardHosting()) {
            new Thread(() -> handler.host(port)).start();
        }
        else {
            new Thread(() -> handler.connect(port, address)).start();
        }

    }

    public void initiateDisconnection() {
        handler.disconnect();
        gui.setAddress(null);
    }

    public void initiateMute() {
        handler.mute();
    }

    public void cleanInfoArea() {
        info.setLength(0);
    }

    public void updateInfoArea(char type, String message) {
        // Match "logType: message"
        info.append(type)
                .append(": ")
                .append(message)
                .append("\n");

        gui.setInfoAreaText(info);
    }

    public int getPort() {
        return gui.getPort();
    }
    public String getAddress() {
        return gui.getAddress();
    }

    public void setGui(GuiAssembler gui) {
        this.gui = gui;
    }
    public void setLogger(InfoLogger logger) {
        this.logger = logger;
        // Push logger instance to handler
        handler.setLogger(logger);
    }
}
