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
        // Do not exit program on close, just dispose dialog
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setAlwaysOnTop(true);
    }

    public void show() {
        dialog.setVisible(true);
    }

    public void setSize(int width, int height) {
        dialog.setSize(width, height);
    }

    public void dispose() {
        dialog.dispose();
    }
}
