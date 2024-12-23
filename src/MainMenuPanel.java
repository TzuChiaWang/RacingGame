import java.awt.*;
import javax.swing.*;

public class MainMenuPanel extends JPanel {
    private static Image loadbackgroundImage;
    private static Image loadSelectBackgroundImage;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private int currentCarIndex = 0;
    private JLabel carLabel;
    private String[] carImages = {
            "src/pic/RedCar.png", "src/pic/BlueCar.png", "src/pic/sheep.png",
            "src/pic/YellowCar.png", "src/pic/PurpleCar.png", "src/pic/WhiteCar.png",
            "src/pic/WhiteRocket.png", "src/pic/NASARocket.png"
    };

    public MainMenuPanel(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        setLayout(null);

        loadbackgroundImage = new ImageIcon("src/pic/loadbackground.png").getImage();
        loadSelectBackgroundImage = new ImageIcon("src/pic/SelectBackground.gif").getImage();

        JButton startButton = new JButton("Play");
        startButton.setBounds(100, 375, 300, 80);
        startButton.setFont(new Font("Arial", Font.BOLD, 50));
        startButton.addActionListener(e -> showCarSelection());
        add(startButton);
    }

    private void showCarSelection() {
        JPanel carSelectionPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(loadSelectBackgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        carSelectionPanel.setLayout(null);

        carLabel = new JLabel(new ImageIcon(
                new ImageIcon(carImages[currentCarIndex]).getImage().getScaledInstance(120, 220, Image.SCALE_SMOOTH)));
        carLabel.setBounds(150, 100, 200, 400);
        carSelectionPanel.add(carLabel);

        JButton leftButton = new JButton("<");
        leftButton.setBounds(50, 275, 80, 80);
        leftButton.setFont(new Font("Arial", Font.BOLD, 50));
        leftButton.addActionListener(e -> showPreviousCar());
        carSelectionPanel.add(leftButton);

        JButton rightButton = new JButton(">");
        rightButton.setBounds(370, 275, 80, 80);
        rightButton.setFont(new Font("Arial", Font.BOLD, 50));
        rightButton.addActionListener(e -> showNextCar());
        carSelectionPanel.add(rightButton);

        JButton selectButton = new JButton("Select");
        selectButton.setBounds(150, 500, 200, 80);
        selectButton.setFont(new Font("Arial", Font.BOLD, 50));
        selectButton.addActionListener(e -> selectCar());
        carSelectionPanel.add(selectButton);

        mainPanel.add(carSelectionPanel, "carSelection");
        cardLayout.show(mainPanel, "carSelection");
    }

    private void showPreviousCar() {
        currentCarIndex = (currentCarIndex - 1 + carImages.length) % carImages.length;
        carLabel.setIcon(new ImageIcon(
                new ImageIcon(carImages[currentCarIndex]).getImage().getScaledInstance(120, 220, Image.SCALE_SMOOTH)));
    }

    private void showNextCar() {
        currentCarIndex = (currentCarIndex + 1) % carImages.length;
        carLabel.setIcon(new ImageIcon(
                new ImageIcon(carImages[currentCarIndex]).getImage().getScaledInstance(120, 220, Image.SCALE_SMOOTH)));
    }

    private void selectCar() {
        ((RacingGame) mainPanel.getComponent(1)).setPlayerCarImage(carImages[currentCarIndex]);
        cardLayout.show(mainPanel, "game");
        ((RacingGame) mainPanel.getComponent(1)).showStartButton();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(loadbackgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}