import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class Launcher {
    private static final String START_PANEL = "start";
    private static final String DIFFICULTY_PANEL = "difficulty";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame mainFrame = new JFrame("Minesweeper");

            // Load background image
            URL imageURL = Launcher.class.getResource("background.jpg");
            ImageIcon backgroundImage = new ImageIcon(imageURL);

            // Create a JLabel with the background image
            JLabel backgroundLabel = new JLabel(backgroundImage);
            backgroundLabel.setLayout(new BorderLayout());

            JPanel cardPanel = new JPanel(new CardLayout());

            JButton startButton = new JButton("Start Game");

            startButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
                    cardLayout.show(cardPanel, DIFFICULTY_PANEL);
                }
            });

            JPanel startPanel = new JPanel();
            startPanel.add(startButton);

            cardPanel.add(startPanel, START_PANEL);
            cardPanel.add(new DifficultyPanel(cardPanel), DIFFICULTY_PANEL);

            // Add the background label as the content pane of the main frame
            mainFrame.setContentPane(backgroundLabel);

            // Set the layout for the background label
            backgroundLabel.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            backgroundLabel.add(cardPanel, gbc);

            mainFrame.setSize(800, 500);
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setLocationRelativeTo(null);
            mainFrame.setVisible(true);
        });
    }
}