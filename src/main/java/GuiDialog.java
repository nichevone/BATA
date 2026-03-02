import javax.swing.*;
import java.awt.*;

public class GuiDialog {
    private final JDialog dialog;

    public GuiDialog(String title, Component content, Component parent) {
        JOptionPane optionPane = new JOptionPane(
                content, // What dialog contains
                JOptionPane.PLAIN_MESSAGE, // Type of dialog
                JOptionPane.DEFAULT_OPTION, // Type of option (see below)
                null, // Icon
                new Object[]{}, // Options. To make "OK" button disappear
                null // Initial value
        );

        dialog = optionPane.createDialog(parent, title);
        dialog.setModalityType(Dialog.ModalityType.MODELESS);
        // No need to close dialog, handled manually
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    }

    /* Calculate the dialog like this:
     * ------------------
     * |                |
     * |   main frame   |  --------
     * |                |  |dialog|
     * ------------------  --------
    */
    private int[] calculateLocation(Component parent) {
        Point frameLocation = parent.getLocation();
        Dimension frameSize = parent.getSize();

        int x = (int)(frameLocation.getX() + frameSize.getWidth());
        int y = (int)(frameLocation.getY() + frameSize.getHeight()) - dialog.getHeight();

        return new int[]{x, y};
    }

    public void setLocation() {
        final int[] coordinates = calculateLocation(dialog.getParent());
        dialog.setLocation(coordinates[0], coordinates[1]); // x, y
    }

    public void setSize(int width, int height) {
        dialog.setSize(width, height);
    }

    public void show() {
        dialog.setVisible(true);
    }

    public void hide() {
        dialog.setVisible(false);
    }
}
