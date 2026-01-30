import java.awt.*;

public class ConnectionController {
    private final ConnectionHandler handler = new ConnectionHandler();
    private GuiAssembler gui;
    
    public void initiateGui() {
        EventQueue.invokeLater(gui::initUI);
    }

    public void initiateConnection() {
        int port = getPort();
        String address = getAddress();

        if (gui.isCurrentCardHosting()) {
            new Thread(() -> handler.host(port)).start();
        } else {
            new Thread(() -> handler.connect(port, address)).start();
        }

    }

    public void initiateDisconnection() {
        handler.disconnect();
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
}
