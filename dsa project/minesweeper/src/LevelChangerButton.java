import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class LevelChangerButton extends JButton {
    private MinesweeperGame minesweeperGame;

    public LevelChangerButton(MinesweeperGame minesweeperGame) {
        this.minesweeperGame = minesweeperGame;
        setText("Change Level");
        setFont(new Font("Arial", Font.BOLD, 12));
        setFocusable(false);

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeLevel();
            }
        });
    }

    private void changeLevel() {
        Object[] options = { "Easy", "Hard" };
        int choice = JOptionPane.showOptionDialog(null, "Choose a level:", "Level Selection",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (choice == 0) {
            // User selected 8x8
            MinesweeperGame game = new MinesweeperGame(8, 8, 10);
        } else if (choice == 1) {
            // User selected 20x20
            MinesweeperGame game = new MinesweeperGame(20, 20, 70);
        }
        minesweeperGame.frame.dispose();
    }
}
