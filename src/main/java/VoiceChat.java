public class VoiceChat {
    private static final int WINDOW_WIDTH = 650;
    private static final int WINDOW_HEIGHT = 400;

    public static void main(String[] args) {
        ConnectionController controller = new ConnectionController();
        GuiAssembler gui = new GuiAssembler(WINDOW_WIDTH, WINDOW_HEIGHT);
        InfoLogger logger = new InfoLogger();

        controller.setGui(gui);
        controller.setLogger(logger);
        gui.setController(controller);
        logger.setController(controller);

        controller.initiateGui();
    }
}
