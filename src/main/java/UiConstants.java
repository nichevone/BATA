import javax.swing.border.Border;
import javax.swing.BorderFactory;
import java.awt.Font;
import java.awt.Color;

public interface UiConstants {
    // Buttons' text on the start card
    String HOST_NAV_TEXT = "HOST";
    String CONNECT_NAV_TEXT = "CONNECT";
    String START_NAV_TEXT = "START";
    // Buttons' text on the other cards
    String CONNECT_BUTTON_TEXT = "Connect";
    String DISCONNECT_BUTTON_TEXT = "Disconnect";
    String HOST_BUTTON_TEXT = "Host";
    String RETURN_BUTTON_TEXT = "Return";

    Font titleLabelFont = new Font("SansSerif", Font.BOLD, 28);
    Font textLabelFont = new Font("SansSerif", Font.PLAIN, 20);
    Font textAreaFont = new Font("SansSerif", Font.PLAIN, 12);
    Font textFieldFont = new Font("SansSerif", Font.PLAIN, 20);
    Font buttonFont = new Font("SansSerif", Font.ITALIC, 20);

    Border EMPTY_BORDER = BorderFactory.createEmptyBorder(20, 20, 20, 20);
    Border BOTTOM_BORDER = BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK);
    int BORDER_GAP = 10;
}
