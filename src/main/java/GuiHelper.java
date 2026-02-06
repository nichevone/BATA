import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.File;
import java.nio.file.Path;
import java.io.IOException;

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
        Path imgPath = Path.of("src/main/resources/icon.png");

        try {
            return ImageIO.read(new File(imgPath.toUri()));
        } catch (IOException e) {
            System.err.println("Error reading icon image:\n"+e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("IllegalArgumentException while getting icon image:\n" + e.getMessage());
        }
        return null;
    }
}
