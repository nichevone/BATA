import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class GuiAssembler implements ActionListener, UiConstants {
    private final int windowWidth;
    private final int windowHeight;

    private ConnectionController controller;
    private final GuiHelper guiHelper = new GuiHelper();

    private final int HOSTING_CARD_INDEX = 0;
    private final int CONNECTING_CARD_INDEX = 1;

    private String currentCard;
    private boolean isMuted = false;
    private int port;
    private String address;
    
    private JFrame frame;
    private JPanel cardsPanel;
    private CardLayout cardLayout;
    private JPanel startCard;
    private JPanel hostingCard;
    private JPanel connectingCard;

    GuiDialog controlPanelDialog;

    private JButton hostNavButton;
    private JButton connectNavButton;
    private JButton returnButton;
    private JButton muteButton;
    private JTextField addressTextField;
    // Arrays are used for these to modify objects on the different cards
    // 0 - for host card, 1 - for connecting card
    private final JTextField[] portTextField = new JTextField[2];
    private final JButton[] actionChoiceButton = new JButton[2];
    private final JButton[] disconnectButton = new JButton[2];
    private final JTextArea[] infoTextArea = new JTextArea[2];



    public GuiAssembler(int windowWidth, int windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
    }

    public void initUI() {
        frame = new JFrame();
        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);
        setLookAndFeel();

        try {
            startCard = createStartPanel();
            hostingCard = createPanel("HOSTING");
            connectingCard = createPanel("CONNECTING");

            cardsPanel.add(hostingCard, HOST_NAV_TEXT);
            cardsPanel.add(connectingCard, CONNECT_NAV_TEXT);
            cardsPanel.add(startCard, START_NAV_TEXT);
            frame.add(cardsPanel, BorderLayout.CENTER);

            frame.setTitle("BATA");
            frame.setIconImage(guiHelper.getIconImage());
            frame.setSize(windowWidth, windowHeight);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);

            JPanel controlJPanel = createControlPanel();
            controlPanelDialog = new GuiDialog("Control Panel", controlJPanel, frame);
            controlPanelDialog.setSize(150, 90); // Dialog width and height
            controlPanelDialog.setLocation();

            cardLayout.show(cardsPanel, START_NAV_TEXT);
        }
        catch (IllegalArgumentException e) {
            System.err.println("IllegalArgumentException:\n" + e.getMessage());
        }
        catch (NullPointerException e) {
            System.err.println("NullPointerException.\n" + e.getMessage());
        }
    }

    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
            );
        }
        catch (UnsupportedLookAndFeelException e) {
            System.err.println("ClassNotFoundException:\n" + e.getMessage());
        }
        catch (ClassNotFoundException e) {
            System.err.println("InstantiationException:\n" + e.getMessage());
        }
        catch (InstantiationException e) {
            System.err.println("IllegalAccessException:\n" + e.getMessage());
        }
        catch (IllegalAccessException e) {
            System.err.println("UnsupportedLookAndFeelException:\n" + e.getMessage());
        }
    }

    private JPanel createStartPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        JPanel actionChoicePanel = new JPanel(new GridLayout(2, 1, BORDER_GAP, BORDER_GAP));

        JLabel actionChoiceLabel = new JLabel("Choose action", JLabel.CENTER);
        actionChoiceLabel.setFont(actionChoiceLabelFont);

        hostNavButton = new JButton(HOST_NAV_TEXT);
        hostNavButton.setFont(buttonFont);
        hostNavButton.addActionListener(this);
        connectNavButton = new JButton(CONNECT_NAV_TEXT);
        connectNavButton.setFont(buttonFont);
        connectNavButton.addActionListener(this);

        actionChoicePanel.add(hostNavButton);
        actionChoicePanel.add(connectNavButton);

        panel.add(actionChoiceLabel); // Label first
        panel.add(actionChoicePanel); // Panel w/ buttons after the label
        panel.setBorder(EMPTY_BORDER);

        return panel;
    }

    private JPanel createPanel(String actionType) throws IllegalArgumentException {
        int cardIndex;

        switch (actionType) {
            case "HOSTING" -> cardIndex = HOSTING_CARD_INDEX;
            case "CONNECTING" -> cardIndex = CONNECTING_CARD_INDEX;
            default -> throw new IllegalArgumentException("actionType is invalid");
        }

        JLabel titleLabel = new JLabel(actionType, JLabel.CENTER);
        titleLabel.setFont(titleLabelFont);
        titleLabel.setBorder(BOTTOM_BORDER);

        // Left part - text Area
        JPanel leftPane = new JPanel(new BorderLayout());
        infoTextArea[cardIndex] = new JTextArea();
        infoTextArea[cardIndex].setLineWrap(true);
        infoTextArea[cardIndex].setFont(textAreaFont);

        JScrollPane scrollPane = new JScrollPane(infoTextArea[cardIndex]);
        scrollPane.setBorder(TITLED_BORDER);
        leftPane.add(scrollPane);

        // Right part - port input and the button
        JPanel rightPane = new JPanel(new GridBagLayout());

        JLabel portLabel = new JLabel("Port", JLabel.CENTER);
        portTextField[cardIndex] = new JTextField();
        actionChoiceButton[cardIndex] = new JButton(); // text's assigned in the switch statement
        disconnectButton[cardIndex] = new JButton(DISCONNECT_BUTTON_TEXT);
        returnButton = new JButton(RETURN_BUTTON_TEXT);

        portTextField[cardIndex].setHorizontalAlignment(JTextField.CENTER);
        actionChoiceButton[cardIndex].addActionListener(this);
        disconnectButton[cardIndex].addActionListener(this);
        returnButton.addActionListener(this);

        disconnectButton[cardIndex].setEnabled(false);

        portLabel.setFont(textLabelFont);
        portTextField[cardIndex].setFont(textFieldFont);
        actionChoiceButton[cardIndex].setFont(buttonFont);
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
                actionChoiceButton[cardIndex].setText(HOST_BUTTON_TEXT);

                rightGbc.gridx = 0;
                rightGbc.gridy = 2;
                rightGbc.gridheight = 1;
                rightGbc.weighty = 4;
                // Empty label to create some empty space
                rightPane.add(new JLabel(""), rightGbc);
            }
            case "CONNECTING" -> {
                actionChoiceButton[cardIndex].setText(CONNECT_BUTTON_TEXT);
                
                JLabel addressLabel = new JLabel("Host's IP-address", JLabel.CENTER);
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
        rightPane.add(actionChoiceButton[cardIndex], rightGbc);
        
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
        hostGbc.weightx = 5;
        hostGbc.weighty = 1;
        hostPanel.add(leftPane, hostGbc);

        hostGbc.gridx = 1;
        hostGbc.gridy = 1;
        hostGbc.weightx = 1;
        hostPanel.add(rightPane, hostGbc);

        return hostPanel;
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 1));
        muteButton = new JButton(MUTE_BUTTON_TEXT);

        muteButton.setFont(buttonFont);
        muteButton.setBackground(Color.GREEN);
        muteButton.addActionListener(this);
        // Disabled by default before user hosts or connects
        muteButton.setEnabled(false);

        panel.add(muteButton, JPanel.CENTER_ALIGNMENT);
        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case HOST_NAV_TEXT ->
            {
                currentCard = HOST_NAV_TEXT;
                command = HOST_NAV_TEXT;
                prepareUi();
            }

            case CONNECT_NAV_TEXT ->
            {
                currentCard = CONNECT_NAV_TEXT;
                command = CONNECT_NAV_TEXT;
                prepareUi();
            }
            case HOST_BUTTON_TEXT -> host();
            case CONNECT_BUTTON_TEXT -> connect();
            case DISCONNECT_BUTTON_TEXT -> disconnect();
            case RETURN_BUTTON_TEXT ->
            {
                command = START_NAV_TEXT;
                disconnect();
                updateUi();
            }
            case MUTE_BUTTON_TEXT -> mute();
            default -> { return; }
        }

        cardLayout.show(cardsPanel, command);
    }

    private void host() {
        String port = portTextField[HOSTING_CARD_INDEX].getText();

        if (!guiHelper.isPortValid(port)) {
            portTextField[HOSTING_CARD_INDEX].setText("Invalid port");
            return;
        }

        disconnectButton[HOSTING_CARD_INDEX].setEnabled(true);
        actionChoiceButton[HOSTING_CARD_INDEX].setEnabled(false);
        muteButton.setEnabled(true);

        this.port = Integer.parseInt(port);
        controller.initiateConnection();
    }

    private void connect() {
        String address = addressTextField.getText();
        String port = portTextField[CONNECTING_CARD_INDEX].getText();

        if (!guiHelper.isPortValid(port)) {
            portTextField[CONNECTING_CARD_INDEX].setText("Invalid port");
            return;
        }

        if (!guiHelper.isAddressValid(address)) {
            addressTextField.setText("Invalid address");
            return;
        }

        disconnectButton[CONNECTING_CARD_INDEX].setEnabled(true);
        actionChoiceButton[CONNECTING_CARD_INDEX].setEnabled(false);
        muteButton.setEnabled(true);

        this.port = Integer.parseInt(port);
        this.address = address;
        controller.initiateConnection();
    }

    private void disconnect() {
        disconnectButton[HOSTING_CARD_INDEX].setEnabled(false);
        disconnectButton[CONNECTING_CARD_INDEX].setEnabled(false);

        actionChoiceButton[HOSTING_CARD_INDEX].setEnabled(true);
        actionChoiceButton[CONNECTING_CARD_INDEX].setEnabled(true);
        muteButton.setEnabled(false);

        // Unmute if muted
        if (isMuted) { mute(); }
        controller.initiateDisconnection();
    }

    private void mute() {
        controller.initiateMute();

        isMuted = !isMuted;
        if (isMuted) {
            muteButton.setBackground(Color.RED);
        } else {
            muteButton.setBackground(Color.GREEN);
        }
    }

    private void prepareUi() {
        // Update dialog location in case user moved the window
        controlPanelDialog.setLocation();
        controlPanelDialog.show();

        controller.cleanInfoArea();
    }

    private void updateUi() {
        if (addressTextField != null) {
            addressTextField.setText("");
        }

        portTextField[HOSTING_CARD_INDEX].setText("");
        portTextField[CONNECTING_CARD_INDEX].setText("");
        
        controlPanelDialog.hide();
    }

    public void setInfoAreaText(StringBuffer info) {
        switch (currentCard) {
            case HOST_NAV_TEXT -> infoTextArea[HOSTING_CARD_INDEX].setText(info.toString());
            case CONNECT_NAV_TEXT -> infoTextArea[CONNECTING_CARD_INDEX].setText(info.toString());
        }
    }



    public boolean isCurrentCardHosting() {
        return currentCard.equals(HOST_NAV_TEXT);
    }
    public int getPort() {
        return port;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String value) {
        address = value;
    }

    public void setController(ConnectionController controller) {
        this.controller = controller;
    }
}
