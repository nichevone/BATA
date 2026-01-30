/*
    Welcome to BATA (Basic Audio Transmitting Application)!
 */

public class VoiceChat {
    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 400;

    public static void main(String[] args) {
        ConnectionController controller = new ConnectionController();
        GuiAssembler gui = new GuiAssembler(WINDOW_WIDTH, WINDOW_HEIGHT);

        controller.setGui(gui);
        gui.setController(controller);

        controller.initiateGui();
    }
}
