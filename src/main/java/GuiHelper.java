import javax.swing.*;
import java.net.URL;
import java.awt.Image;

public class GuiHelper {
    public boolean isPortValid(String port) {
        try {
            Integer.parseInt(port);
            return true; // If parsing was successful
        } catch (NumberFormatException e) {
            return false; // If parsing fails
        }
    }

    public boolean isAddressValid(String address) {
        // Matches IP-address
        return address.matches(
                "^((25[0-5]|2[0-4]\\d|[01]?\\d?\\d)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d?\\d)$"
        );
    }

    public Image getIconImage() {
        try {
            // Using URL for proper read packaged in a .jar
            URL imageUrl = getClass().getResource("icon.png");
            if (imageUrl != null) {
                return new ImageIcon(imageUrl).getImage();
            }
        } catch (NullPointerException e) {
            System.err.println("NullPointerException while getting icon image:\n" + e.getMessage());
        }

        return null;
    }
}
