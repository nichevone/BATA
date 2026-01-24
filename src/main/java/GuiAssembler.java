import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
// Libraries for reading the icon
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class GuiAssembler implements ActionListener, UiConstants {
    private final int windowWidth;
    private final int windowHeight;

    private final int HOSTING_CARD_INDEX = 0;
    private final int CONNECTING_CARD_INDEX = 1;

    private JFrame frame;
    private JPanel cardsPanel;
    private CardLayout cardLayout;
    private JPanel startCard;
    private JPanel hostingCard;
    private JPanel connectingCard;

    private JButton hostNavButton;
    private JButton connectNavButton;
    private JButton returnButton;
    private JTextField addressTextField;
    // Arrays are used for these to modify objects on the different cards
    // 0 - for host card, 1 - for connecting card
    private JTextField[] portTextField = new JTextField[2];
    private JButton[] actionButton = new JButton[2];
    private JButton[] disconnectButton = new JButton[2];
    private JTextArea[] infoTextArea = new JTextArea[2];



    public GuiAssembler(int windowWidth, int windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
    }

    public void initUI() {
        frame = new JFrame();
        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);

        try {
            startCard = createStartPanel();
            hostingCard = createPanel("HOSTING");
            connectingCard = createPanel("CONNECTING");

            cardsPanel.add(hostingCard, HOST_NAV_TEXT);
            cardsPanel.add(connectingCard, CONNECT_NAV_TEXT);
            cardsPanel.add(startCard, START_NAV_TEXT);
            frame.add(cardsPanel, BorderLayout.CENTER);

            frame.setTitle("BATA");
            frame.setIconImage(getIconImage());
            frame.setSize(windowWidth, windowHeight);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);

            cardLayout.show(cardsPanel, START_NAV_TEXT);
        } catch (IllegalArgumentException e) {
            System.err.println("IllegalArgumentException in method createPanel.\n" + e.getMessage());
        } catch (NullPointerException e) {
            System.err.println("NullPointerException.\n" + e.getMessage());
        }
    }

    private JPanel createStartPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        JPanel actionChoicePanel = new JPanel(new GridLayout(2, 1, BORDER_GAP, BORDER_GAP));

        JLabel chooseActionLabel = new JLabel("Choose action:", JLabel.CENTER);
        chooseActionLabel.setFont(titleLabelFont);

        hostNavButton = new JButton(HOST_NAV_TEXT);
        hostNavButton.setFont(buttonFont);
        hostNavButton.addActionListener(this);
        connectNavButton = new JButton(CONNECT_NAV_TEXT);
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
        int cardIndex;
        if (actionType.equals("HOSTING")) {
            cardIndex = HOSTING_CARD_INDEX;
        }
        else if (actionType.equals("CONNECTING")) {
            cardIndex = CONNECTING_CARD_INDEX;
        }
        else {
            throw new IllegalArgumentException("actionType is invalid");
        }

        JLabel titleLabel = new JLabel(actionType, JLabel.CENTER);
        titleLabel.setFont(titleLabelFont);
        titleLabel.setBorder(BOTTOM_BORDER);

        // Left part - text Area
        JPanel leftPane = new JPanel(new BorderLayout());
        infoTextArea[cardIndex] = new JTextArea();
        infoTextArea[cardIndex].setFont(textAreaFont);

        JScrollPane scrollPane = new JScrollPane(infoTextArea[cardIndex]);
        leftPane.add(scrollPane);

        // Right part - port input and the button
        JPanel rightPane = new JPanel(new GridBagLayout());

        JLabel portLabel = new JLabel("Enter port:", JLabel.CENTER);
        portTextField[cardIndex] = new JTextField();
        actionButton[cardIndex] = new JButton(); // text's assigned in the switch statement
        disconnectButton[cardIndex] = new JButton(DISCONNECT_BUTTON_TEXT);
        returnButton = new JButton(RETURN_BUTTON_TEXT);

        portTextField[cardIndex].setHorizontalAlignment(JTextField.CENTER);
        actionButton[cardIndex].addActionListener(this);
        disconnectButton[cardIndex].addActionListener(this);
        returnButton.addActionListener(this);

        disconnectButton[cardIndex].setEnabled(false);

        portLabel.setFont(textLabelFont);
        portTextField[cardIndex].setFont(textFieldFont);
        actionButton[cardIndex].setFont(buttonFont);
        disconnectButton[cardIndex].setFont(buttonFont);
        returnButton.setFont(buttonFont);

        GridBagConstraints rightGbc = new GridBagConstraints();
        rightGbc.fill = GridBagConstraints.BOTH;
        rightGbc.anchor = GridBagConstraints.CENTER;
        rightGbc.insets = new Insets(BORDER_GAP, 0, BORDER_GAP, BORDER_GAP);

        rightGbc.gridx = 0;
        rightGbc.gridy = 0;
        rightGbc.gridwidth = 3;
        rightGbc.gridheight = 1;
        rightGbc.weightx = 0;
        rightGbc.weighty = 0.2;
        rightPane.add(portLabel, rightGbc);
        
        rightGbc.gridx = 0;
        rightGbc.gridy = 1;
        rightGbc.weighty = 1;
        rightPane.add(portTextField[cardIndex], rightGbc);

        switch (actionType) {
            case "HOSTING" -> {
                actionButton[cardIndex].setText(HOST_BUTTON_TEXT);

                rightGbc.gridx = 0;
                rightGbc.gridy = 2;
                rightGbc.gridheight = 1;
                rightGbc.weighty = 4;
                // Empty label to create some empty space
                rightPane.add(new JLabel(""), rightGbc);
            }
            case "CONNECTING" -> {
                actionButton[cardIndex].setText(CONNECT_BUTTON_TEXT);
                
                JLabel addressLabel = new JLabel("Enter host's IP-address:", JLabel.CENTER);
                addressLabel.setFont(textLabelFont);
                
                addressTextField = new JTextField();
                addressTextField.setFont(textFieldFont);
                addressTextField.setHorizontalAlignment(JTextField.CENTER);

                rightGbc.gridx = 0;
                rightGbc.gridy = 2;
                rightGbc.weighty = 0.2;
                rightPane.add(addressLabel, rightGbc);
                
                rightGbc.gridx = 0;
                rightGbc.gridy = 3;
                rightGbc.weighty = 1;
                rightPane.add(addressTextField, rightGbc);
            }
        }
        
        rightGbc.gridx = 0;
        rightGbc.gridy = 4;
        rightGbc.gridwidth = 1;
        rightGbc.weightx = 0;
        rightGbc.weighty = 0.2;
        rightPane.add(returnButton, rightGbc);
        
        rightGbc.gridx = 1;
        rightGbc.gridy = 4;
        rightPane.add(actionButton[cardIndex], rightGbc);
        
        rightGbc.gridx = 2;
        rightGbc.gridy = 4;
        rightPane.add(disconnectButton[cardIndex], rightGbc);

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
        hostGbc.weightx = 4;
        hostGbc.weighty = 1;
        hostPanel.add(leftPane, hostGbc);

        hostGbc.gridx = 1;
        hostGbc.gridy = 1;
        hostGbc.weightx = 0.1;
        hostPanel.add(rightPane, hostGbc);

        return hostPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.paramString());
        
        String command = "";
        switch (e.getActionCommand()) {
            case HOST_NAV_TEXT -> command = HOST_NAV_TEXT;
            case HOST_BUTTON_TEXT -> host();
            case CONNECT_NAV_TEXT -> command = CONNECT_NAV_TEXT;
            case CONNECT_BUTTON_TEXT -> connect();
            case DISCONNECT_BUTTON_TEXT ->
            {
                disconnectButton[HOSTING_CARD_INDEX].setEnabled(false);
                disconnectButton[CONNECTING_CARD_INDEX].setEnabled(false);
                
                actionButton[HOSTING_CARD_INDEX].setEnabled(true);
                actionButton[CONNECTING_CARD_INDEX].setEnabled(true);
            }
            case RETURN_BUTTON_TEXT ->
            {
                command = START_NAV_TEXT;
                updateUi();
            }
            default -> { return; }
        }
        
        cardLayout.show(cardsPanel, command);
    }

    private void host() {
        String port = portTextField[HOSTING_CARD_INDEX].getText();
        
        if (isPortValid(port)) {
            System.out.println(port);
            disconnectButton[HOSTING_CARD_INDEX].setEnabled(true);
            actionButton[HOSTING_CARD_INDEX].setEnabled(false);
        }
    }

    private void connect() {
        String address = addressTextField.getText();
        String port = portTextField[CONNECTING_CARD_INDEX].getText();
        
        if (isAddressValid(address) && isPortValid(port)) {
            System.out.println(port+"\n"+address);
            disconnectButton[CONNECTING_CARD_INDEX].setEnabled(true);
            actionButton[CONNECTING_CARD_INDEX].setEnabled(false);
        }
        else {
            addressTextField.setText("Invalid address");
        }
    }

    private void updateUi() {
        if (addressTextField != null) { addressTextField.setText(""); }

        portTextField[HOSTING_CARD_INDEX].setText("");
        portTextField[CONNECTING_CARD_INDEX].setText("");

        infoTextArea[HOSTING_CARD_INDEX].setText("");
        infoTextArea[CONNECTING_CARD_INDEX].setText("");

        disconnectButton[HOSTING_CARD_INDEX].setEnabled(false);
        disconnectButton[CONNECTING_CARD_INDEX].setEnabled(false);
    }

    private boolean isPortValid(String port) {
        return !port.isBlank();
    }

    private boolean isAddressValid(String address) {
        // Matches IP-address
        return address.matches(
                "^((25[0-5]|2[0-4]\\d|[01]?\\d?\\d)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d?\\d)$"
        );
    }

    private Image getIconImage() {
        try {
            return ImageIO.read(new File("src/main/resources/icon.png"));
        } catch (IOException e) {
            System.err.println("Error reading icon image.\n"+e.getMessage());
            return null;
        }
    }
}
