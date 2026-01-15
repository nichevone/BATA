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
    private final Border BOTTOM_BORDER = BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK);
    private final int BORDER_GAP = 10;

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
    private JButton returnButton;
    private JButton disconnectButtton;


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
        JPanel actionChoicePanel = new JPanel(new GridLayout(2, 1, BORDER_GAP, BORDER_GAP));

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
        JPanel rightPane = new JPanel(new GridBagLayout());

        JLabel portLabel = new JLabel("Enter port:", JLabel.CENTER);
        portTextField = new JTextField();
        actionButton = new JButton(); // text's assigned in the switch statement
        disconnectButtton = new JButton("Disconnect");
        returnButton = new JButton("Return");

        portTextField.setHorizontalAlignment(JTextField.CENTER);
        actionButton.addActionListener(this);
        disconnectButtton.addActionListener(this);
        returnButton.addActionListener(this);

        portLabel.setFont(textLabelFont);
        portTextField.setFont(textFieldFont);
        actionButton.setFont(buttonFont);
        disconnectButtton.setFont(buttonFont);
        returnButton.setFont(buttonFont);

        GridBagConstraints rightGbc = new GridBagConstraints();
        rightGbc.fill = GridBagConstraints.BOTH;
        rightGbc.insets = new Insets(BORDER_GAP, 0, BORDER_GAP, BORDER_GAP);

        rightGbc.gridx = 0;
        rightGbc.gridy = 0;
        // Width, weight x & y will be used for all 
        // components in the right pane except the buttons
        rightGbc.gridwidth = 3;
        rightGbc.weightx = 1;
        rightGbc.weighty = 2;
        rightPane.add(portLabel, rightGbc);
        
        rightGbc.gridx = 0;
        rightGbc.gridy = 1;
        rightPane.add(portTextField, rightGbc);

        switch (actionType) {
            case "HOSTING" -> {
                actionButton.setText("Host");
                
                // Adding blank JLabels to make some space 
                // between the portTextField and the hostButton
                rightGbc.gridx = 0;
                rightGbc.gridy = 2;
                rightPane.add(new JLabel(""), rightGbc);
                
                rightGbc.gridx = 0;
                rightGbc.gridy = 3;
                rightPane.add(new JLabel(""), rightGbc);
            }
            case "CONNECTING" -> {
                actionButton.setText("Connect");
                
                JLabel addressLabel = new JLabel("Enter host's IP-address:", JLabel.CENTER);
                addressLabel.setFont(textLabelFont);
                
                addressTextField = new JTextField();
                addressTextField.setFont(textFieldFont);
                addressTextField.setHorizontalAlignment(JTextField.CENTER);

                rightGbc.gridx = 0;
                rightGbc.gridy = 2;
                rightPane.add(addressLabel, rightGbc);
                
                rightGbc.gridx = 0;
                rightGbc.gridy = 3;
                rightPane.add(addressTextField, rightGbc);
            }
        }
        
        rightGbc.gridx = 0;
        rightGbc.gridy = 4;
        rightGbc.gridwidth = 1;
        rightGbc.weightx = 0;
        
        rightGbc.weighty = 1;
        rightPane.add(returnButton, rightGbc);
        
        rightGbc.gridx = 1;
        rightGbc.gridy = 4;
        rightPane.add(actionButton, rightGbc);
        
        rightGbc.gridx = 2;
        rightGbc.gridy = 4;
        rightPane.add(disconnectButtton, rightGbc);

        // Assembling title, leftPane and the rightPane
        JPanel hostPanel = new JPanel(new GridBagLayout());
        GridBagConstraints hostGbc = new GridBagConstraints();
        hostGbc.fill = GridBagConstraints.BOTH;
        hostGbc.insets = new Insets(0, BORDER_GAP, BORDER_GAP, BORDER_GAP);

        hostGbc.anchor = GridBagConstraints.PAGE_START;
        hostGbc.gridx = 0;
        hostGbc.gridy = 0;
        hostGbc.gridwidth = 2;
        hostGbc.weightx = 0;
        hostGbc.weighty = 0; // to make title take less screen space
        hostPanel.add(titleLabel, hostGbc);

        hostGbc.gridx = 0;
        hostGbc.gridy = 1;
        hostGbc.gridwidth = 1;
        hostGbc.weightx = 18;
        hostGbc.weighty = 1;
        hostPanel.add(leftPane, hostGbc);

        hostGbc.gridx = 1;
        hostGbc.gridy = 1;
        hostGbc.weightx = 1;
        hostPanel.add(rightPane, hostGbc);

        return hostPanel;
    }

    // TODO: organize this thing to readable format
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.paramString());
        
        String command;
        switch (e.getActionCommand()) {
            case "HOST" -> command = HOST_CARD;
            case "CONNECT" -> command = CONNECT_CARD;
            case "Return" -> command = START_CARD;
            default -> { return; }
        }
        
        cardLayout.show(cardsPanel, command);
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
