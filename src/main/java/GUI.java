import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class GUI implements ActionListener {
    private final int windowWidth;
    private final int windowHeight;

    private final String HOST_CARD = "HOST";
    private final String CONNECT_CARD = "CONNECT";
    private final String START_CARD = "START";

    private final Font titleLabelFont = new Font("SansSerif", Font.BOLD, 32);
    private final Font textLabelFont = new Font("SansSerif", Font.PLAIN, 16);
    private final Font buttonFont = new Font("SansSerif", Font.ITALIC, 24);

    private JFrame frame;
    private JPanel cardsPanel;
    private CardLayout cardLayout;

	// StartCard 
    private JPanel startCard;
    private JButton hostCardButton;
    private JButton connectCardButton;
    
	// HostCard
    private JPanel hostingCard;
    
	// ConnectngCard
    private JPanel connectingCard;
    private JTextField portTextField;
    private JTextField addressTextField;
    private JButton connectButton;

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
        frame.setVisible(true);

        cardLayout.show(cardsPanel, START_CARD);
    }

    private JPanel createStartCard() {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        JPanel actionChoicePanel = new JPanel(new GridLayout(2, 1, 20, 20));

        JLabel chooseActionLabel = new JLabel("Choose action:", JLabel.CENTER);
        chooseActionLabel.setFont(titleLabelFont);

        hostCardButton = new JButton(HOST_CARD);
        hostCardButton.setFont(buttonFont);
        hostCardButton.addActionListener(this);
        connectCardButton = new JButton(CONNECT_CARD);
        connectCardButton.setFont(buttonFont);
        connectCardButton.addActionListener(this);

        actionChoicePanel.add(hostCardButton);
        actionChoicePanel.add(connectCardButton);

        panel.add(chooseActionLabel, BorderLayout.NORTH); // Label first
        panel.add(actionChoicePanel, BorderLayout.CENTER); // Panel w/ buttons after the label
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        return panel;
    }

    private JPanel createHostCard() {
        JPanel hostPanel = new JPanel();
        JLabel titleLabel = new JLabel("HOSTING");
        titleLabel.setFont(textLabelFont);
        hostPanel.add(titleLabel);

        return hostPanel;
    }

	// TODO: get text fields and buttons actions
    private JPanel createConnectCard() {
        JPanel connectPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        JLabel titleLabel = new JLabel("CONNECTING", JLabel.CENTER);
        JLabel portLabel = new JLabel("Enter port:", JLabel.CENTER);
        JLabel addressLabel = new JLabel("Enter host IP-address (IPv4):", JLabel.CENTER);
        
        titleLabel.setFont(titleLabelFont);
        portLabel.setFont(textLabelFont);
		addressLabel.setFont(textLabelFont);
		
		portTextField = new JTextField(15);
		addressTextField = new JTextField(15);
		connectButton = new JButton("Connect");
		
		// TODO: text fields font
		//portTextField.setFont();
		//addressTextField.setFont();
		connectButton.setFont(buttonFont);
		
        connectPanel.add(titleLabel);
        
		connectPanel.add(portLabel);
		connectPanel.add(portTextField);
		
		connectPanel.add(addressLabel);
		connectPanel.add(addressTextField);
		
		connectPanel.add(connectButton);

        return connectPanel;
    }

	// TODO: Taking strings from text fields
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        System.out.println(command);
        cardLayout.show(cardsPanel, command);
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
