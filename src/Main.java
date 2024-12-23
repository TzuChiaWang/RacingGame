
import java.awt.CardLayout;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Racing Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 900);

        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);

        MainMenuPanel mainMenu = new MainMenuPanel(cardLayout, mainPanel);
        RacingGame game = new RacingGame();

        mainPanel.add(mainMenu, "menu");
        mainPanel.add(game, "game");

        frame.add(mainPanel);
        frame.setVisible(true);
    }
}
