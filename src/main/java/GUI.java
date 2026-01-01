import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI implements ActionListener {
    private final int WINDOW_WIDTH = 600;
    private final int WINDOW_HEIGHT = 400;

    private final String HOST_CARD = "HOST";
    private final String CONNECT_CARD = "CONNECT";
    private final String MAIN_CARD = "MAIN";

    private final JFrame frame = new JFrame();
    private JPanel cards;
    private CardLayout cardLayout;

    public GUI() {
        initUI();
    }

    private void initUI() {
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        // Main window for choosing action
        JPanel actionChoice = new JPanel();
        JPanel startCard = new JPanel();

        JButton hostButton = new JButton("HOST");
        JButton connectButton = new JButton("CONNECT");
        hostButton.addActionListener(this);
        connectButton.addActionListener(this);

        actionChoice.add(hostButton);
        actionChoice.add(connectButton);

        // Main card
        startCard.add(new JLabel("Choose action:", JLabel.CENTER), BorderLayout.CENTER);
        startCard.add(actionChoice, BorderLayout.CENTER);

        // Hosting window
        JPanel hostingCard = new JPanel();
        hostingCard.add(new JLabel("HOSTING"));

        JButton returnButtonHost = new JButton("MAIN");
        returnButtonHost.addActionListener(this);
        hostingCard.add(returnButtonHost);

        // Connecting window
        JPanel connectingCard = new JPanel();
        connectingCard.add(new JLabel("CONNECTING"));

        JButton returnButtonConnect = new JButton("MAIN");
        returnButtonConnect.addActionListener(this);
        connectingCard.add(returnButtonConnect);

        cards.add(hostingCard, HOST_CARD);
        cards.add(connectingCard, CONNECT_CARD);
        cards.add(startCard, MAIN_CARD);

        // Adding panels to the frame
        frame.add(cards, BorderLayout.CENTER);

        frame.setTitle("BATA");
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        cardLayout.show(cards, MAIN_CARD);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        System.out.println(command);
        cardLayout.show(cards, command);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            GUI gui = new GUI();
        });
    }
}
