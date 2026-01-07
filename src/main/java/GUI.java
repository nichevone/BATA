// Main swing & awt libraries
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.*;
import java.awt.*;
// Reading icon
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class GUI implements ActionListener {
    private final int windowWidth;
    private final int windowHeight;

    private final String HOST_CARD = "HOST";
    private final String CONNECT_CARD = "CONNECT";
    private final String START_CARD = "START";

    private String port;
    private String address;

    private final Font titleLabelFont = new Font("SansSerif", Font.BOLD, 28);
    private final Font textLabelFont = new Font("SansSerif", Font.PLAIN, 20);
    private final Font textFieldFont = new Font("SansSerif", Font.PLAIN, 20);
    private final Font buttonFont = new Font("SansSerif", Font.ITALIC, 20);

    private final Border APP_BORDER = BorderFactory.createEmptyBorder(20, 20, 20, 20);

    private JFrame frame;
    private JPanel cardsPanel;
    private CardLayout cardLayout;

    private JPanel startCard;
    private JPanel hostingCard;
    private JPanel connectingCard;

    private JTextField portTextField;
    private JTextField addressTextField;
    private JButton connectButton;
    private JButton hostNavButton;
    private JButton connectNavButton;

    public GUI(int windowWidth, int windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        initUI();
    }

    private void initUI() {
        frame = new JFrame();
        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);

        startCard = createStartCard();
        hostingCard = createHostCard();
        connectingCard = createConnectCard();

        cardsPanel.add(hostingCard, HOST_CARD);
        cardsPanel.add(connectingCard, CONNECT_CARD);
        cardsPanel.add(startCard, START_CARD);
        frame.add(cardsPanel, BorderLayout.CENTER);

        frame.setTitle("BATA");
        frame.setIconImage(getIconImage());
        frame.setSize(windowWidth, windowHeight);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        cardLayout.show(cardsPanel, START_CARD);
    }

    private JPanel createStartCard() {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        JPanel actionChoicePanel = new JPanel(new GridLayout(2, 1, 10, 10));

        JLabel chooseActionLabel = new JLabel("Choose action:", JLabel.CENTER);
        chooseActionLabel.setFont(titleLabelFont);

        hostNavButton = new JButton(HOST_CARD);
        hostNavButton.setFont(buttonFont);
        hostNavButton.addActionListener(this);
        connectNavButton = new JButton(CONNECT_CARD);
        connectNavButton.setFont(buttonFont);
        connectNavButton.addActionListener(this);

        actionChoicePanel.add(hostNavButton);
        actionChoicePanel.add(connectNavButton);

        panel.add(chooseActionLabel, BorderLayout.NORTH); // Label first
        panel.add(actionChoicePanel, BorderLayout.CENTER); // Panel w/ buttons after the label
        panel.setBorder(APP_BORDER);

        return panel;
    }

    // TODO: host card
    private JPanel createHostCard() {
        JPanel hostPanel = new JPanel();
        JLabel titleLabel = new JLabel("HOSTING");
        titleLabel.setFont(textLabelFont);
        hostPanel.add(titleLabel);

        return hostPanel;
    }

    private JPanel createConnectCard() {
        JPanel connectPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        JLabel titleLabel = new JLabel("CONNECTING", JLabel.CENTER);
        JLabel portLabel = new JLabel("Enter port:", JLabel.CENTER);
        JLabel addressLabel = new JLabel("Enter host IP-address (IPv4):", JLabel.CENTER);
        portTextField = new JTextField(15);
        addressTextField = new JTextField(15);
        connectButton = new JButton("Connect");

        portTextField.setHorizontalAlignment(JTextField.CENTER);
        addressTextField.setHorizontalAlignment(JTextField.CENTER);
        connectButton.addActionListener(this);

        titleLabel.setFont(titleLabelFont);
        portLabel.setFont(textLabelFont);
        addressLabel.setFont(textLabelFont);
        portTextField.setFont(textFieldFont);
        addressTextField.setFont(textFieldFont);
        connectButton.setFont(buttonFont);

        connectPanel.add(titleLabel);
        connectPanel.add(portLabel);
        connectPanel.add(portTextField);
        connectPanel.add(addressLabel);
        connectPanel.add(addressTextField);
        connectPanel.add(connectButton);
        connectPanel.setBorder(APP_BORDER);

        return connectPanel;
    }

    // TODO: organize this thing to readable format
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.paramString());
        String command = e.getActionCommand();
        if (command.equals("Connect")) {
            port = portTextField.getText();
            address = addressTextField.getText();
            if (!isAddressValid(address)) {
                addressTextField.setText("Invalid address");
            } else {
                connectButton.setText("Connecting...");
            }
        } else {
            cardLayout.show(cardsPanel, command);
        }
    }

    private boolean isAddressValid(String address) {
        return address.matches("^\\d{3}[.]\\d+[.]\\d[.]\\d+$"); // Matches IP-address, e.g. 127.0.0.1
    }

    private Image getIconImage() {
        try {
            return ImageIO.read(new File("src/main/resources/icon.png"));
        } catch (IOException e) {
            System.err.println("Error reading icon image.\n"+e.getMessage());
        }
        return null;
    }

    public static void main(String[] args) {
        int width = 600, height = 400;

        EventQueue.invokeLater(() -> {
            GUI gui = new GUI(width, height);
        });
    }
}
