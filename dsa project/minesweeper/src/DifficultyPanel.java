import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DifficultyPanel extends JPanel {
    private JPanel cardPanel;

    public DifficultyPanel(JPanel cardPanel) {
        this.cardPanel = cardPanel;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JButton easyButton = new JButton("Easy");
        JButton hardButton = new JButton("Hard");

        easyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MinesweeperGame game = new MinesweeperGame(8, 8, 10);
                hidePanel();
            }
        });

        hardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MinesweeperGame game = new MinesweeperGame(20, 20, 70);
                hidePanel();
            }
        });

        add(new JLabel("Choose Level:"));
        add(easyButton);
        add(hardButton);
    }

    private void hidePanel() {
        cardPanel.setVisible(false);
        SwingUtilities.getWindowAncestor(this).dispose();
    }
}
