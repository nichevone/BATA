// Main swing & awt libraries
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.*;
import java.awt.*;
// Reading icon
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class GuiAssembler implements ActionListener {
    private final int windowWidth;
    private final int windowHeight;

    private String port;
    private String address;

    private final String HOST_CARD = "HOST";
    private final String CONNECT_CARD = "CONNECT";
    private final String START_CARD = "START";

    private final Font titleLabelFont = new Font("SansSerif", Font.BOLD, 28);
    private final Font textLabelFont = new Font("SansSerif", Font.PLAIN, 20);
    private final Font textAreaFont = new Font("SansSerif", Font.PLAIN, 12);
    private final Font textFieldFont = new Font("SansSerif", Font.PLAIN, 20);
    private final Font buttonFont = new Font("SansSerif", Font.ITALIC, 20);

    private final Border EMPTY_BORDER = BorderFactory.createEmptyBorder(20, 20, 20, 20);
    private final Border BOTTOM_BORDER = BorderFactory.createMatteBorder(0, 0, 2, 0, Color.GRAY);
    private final int LAYOUT_GAP = 10;

    private JFrame frame;
    private JPanel cardsPanel;
    private CardLayout cardLayout;
    private JPanel startCard;
    private JPanel hostingCard;
    private JPanel connectingCard;

    private JTextField portTextField;
    private JTextField addressTextField;
    private JTextArea infoTextArea;
    private JButton hostNavButton;
    private JButton connectNavButton;
    private JButton actionButton;


    public GuiAssembler(int windowWidth, int windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        initUI();
    }

    private void initUI() {
        frame = new JFrame();
        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);

        try {
            startCard = createStartPanel();
            hostingCard = createPanel("HOSTING");
            connectingCard = createPanel("CONNECTING");
        } catch (IllegalArgumentException e) {
            System.err.println("IllegalArgumentException in method createPanel: actionType is invalid.");
        }

        cardsPanel.add(hostingCard, HOST_CARD);
        cardsPanel.add(connectingCard, CONNECT_CARD);
        cardsPanel.add(startCard, START_CARD);
        frame.add(cardsPanel, BorderLayout.CENTER);

        frame.setTitle("BATA");
        frame.setIconImage(getIconImage());
        frame.setSize(windowWidth, windowHeight);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        cardLayout.show(cardsPanel, START_CARD);
    }

    private JPanel createStartPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        JPanel actionChoicePanel = new JPanel(new GridLayout(2, 1, LAYOUT_GAP, LAYOUT_GAP));

        JLabel chooseActionLabel = new JLabel("Choose action:", JLabel.CENTER);
        chooseActionLabel.setFont(titleLabelFont);

        hostNavButton = new JButton("HOST");
        hostNavButton.setFont(buttonFont);
        hostNavButton.addActionListener(this);
        connectNavButton = new JButton("CONNECT");
        connectNavButton.setFont(buttonFont);
        connectNavButton.addActionListener(this);

        actionChoicePanel.add(hostNavButton);
        actionChoicePanel.add(connectNavButton);

        panel.add(chooseActionLabel); // Label first
        panel.add(actionChoicePanel); // Panel w/ buttons after the label
        panel.setBorder(EMPTY_BORDER);

        return panel;
    }

    private JPanel createPanel(String actionType) throws IllegalArgumentException {
        if (!(actionType.equals("HOSTING") || actionType.equals("CONNECTING"))) {
            throw new IllegalArgumentException();
        }

        JLabel titleLabel = new JLabel(actionType, JLabel.CENTER);
        titleLabel.setFont(titleLabelFont);
        titleLabel.setBorder(BOTTOM_BORDER);

        // Left part - text Area
        JPanel leftPane = new JPanel(new BorderLayout());
        infoTextArea = new JTextArea();
        infoTextArea.setFont(textAreaFont);

        JScrollPane scrollPane = new JScrollPane(infoTextArea);
        leftPane.add(scrollPane);

        // Right part - port input and the button
        JPanel rightPane = new JPanel();
        JLabel portLabel = new JLabel("Enter port:", JLabel.CENTER);
        portTextField = new JTextField();
        actionButton = new JButton("Host");

        portTextField.setHorizontalAlignment(JTextField.CENTER);
        actionButton.addActionListener(this);

        portLabel.setFont(textLabelFont);
        portTextField.setFont(textFieldFont);
        actionButton.setFont(buttonFont);

        rightPane.add(portLabel);
        rightPane.add(portTextField);

        int componentsNumber = 0;
        switch (actionType) {
            case "HOSTING" -> {
                componentsNumber = 5;
                actionButton.setText("Host");
                // Making some space between the portTextField and the hostButton
                rightPane.add(new JLabel(""));
                rightPane.add(new JLabel(""));
            }
            case "CONNECTING" -> {
                componentsNumber = 5;
                JLabel addressLabel = new JLabel("Enter host's IP-address:", JLabel.CENTER);
                addressLabel.setFont(textLabelFont);
                addressTextField = new JTextField();
                addressTextField.setFont(textFieldFont);
                addressTextField.setHorizontalAlignment(JTextField.CENTER);
                actionButton.setText("Connect");

                rightPane.add(addressLabel);
                rightPane.add(addressTextField);
            }
        }
        rightPane.add(actionButton);
        rightPane.setLayout(new GridLayout(componentsNumber, 1, LAYOUT_GAP, LAYOUT_GAP));

        // Assembling title, leftPane and the rightPane
        JPanel hostPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, LAYOUT_GAP, LAYOUT_GAP, LAYOUT_GAP);

        c.anchor = GridBagConstraints.PAGE_START;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.weightx = 0;
        c.weighty = 0; // to make title take less screen space
        hostPanel.add(titleLabel, c);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.weightx = 2;
        c.weighty = 1;
        hostPanel.add(leftPane, c);

        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.weightx = 2;
        c.weighty = 1;
        hostPanel.add(rightPane, c);

        return hostPanel;
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
                actionButton.setText("Connecting...");
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
            GuiAssembler gui = new GuiAssembler(width, height);
        });
    }
}
