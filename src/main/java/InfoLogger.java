public class InfoLogger {
    private ConnectionController controller;

    public void setController(ConnectionController controller) {
        this.controller = controller;
    }

    public void log(char type, String message) {
        controller.updateInfoArea(type, message);
    }
}
