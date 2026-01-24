import javax.swing.*;
import java.awt.*;

public class Main {
    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 400;
    
    public static void main(String args[]) {
        GuiAssembler gui = new GuiAssembler(WINDOW_WIDTH, WINDOW_HEIGHT);
        
        EventQueue.invokeLater(() -> {
            gui.initUI();
        });
    }
    
    
}
