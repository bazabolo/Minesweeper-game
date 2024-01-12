import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class MinesweeperGame {
    class MineTile extends JButton {
        int r;
        int c;

        public MineTile(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }

    int tileSize = 30;
    int numRows = 20;
    int numCols = numRows;
    int broadWidth = numCols * tileSize;
    int broadHeight = numRows * tileSize;
    int minesRemaining;

    JFrame frame = new JFrame("Minesweeper");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JButton resetButton = new JButton("Reset Game");

    int mineCount = 50;
    MineTile[][] board = new MineTile[numRows][numCols];
    ArrayList<MineTile> mineList;
    Random random = new Random();
    int tilesClicked = 0;
    boolean gameOver = false;
    boolean firstClick = true;

    MinesweeperGame(int numRows, int numCols, int mineCount) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.mineCount = mineCount;
        this.broadWidth = numCols * tileSize;
        this.broadHeight = numRows * tileSize;

        frame.setSize(broadWidth, broadHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        textLabel.setFont(new Font("Arial", Font.BOLD, 15));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Minesweeper: " + Integer.toString(mineCount));
        textLabel.setOpaque(true);

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(numRows, numCols));
        frame.add(boardPanel);

        JButton resetButton = new JButton("Reset Game");
        LevelChangerButton levelChangerButton = new LevelChangerButton(this);

        resetButton.setFont(new Font("Arial", Font.BOLD, 12));
        resetButton.setFocusable(false);
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGame();
            }
        });

        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.add(textPanel, BorderLayout.CENTER);
        controlPanel.add(resetButton, BorderLayout.WEST);
        controlPanel.add(levelChangerButton, BorderLayout.EAST);
        frame.add(controlPanel, BorderLayout.NORTH);

        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                MineTile tile = new MineTile(r, c);
                board[r][c] = tile;

                tile.setFocusable(false);
                tile.setMargin(new Insets(0, 0, 0, 0));
                tile.setFont(new Font("Arial Unicode MS", Font.PLAIN, 15));

                tile.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (gameOver) {
                            return;
                        }

                        MineTile tile = (MineTile) e.getSource();

                        if (firstClick) {
                            handleFirstClick(tile.r, tile.c);
                            firstClick = false;
                        } else {
                            handleRegularClick(tile, e.getButton());
                        }
                    }
                });

                boardPanel.add(tile);
            }
        }
        frame.setVisible(true);

        setMine();
        updateMinesRemaining();
    }

    void handleFirstClick(int r, int c) {

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                board[i][j].setEnabled(true);
                board[i][j].setText("");
            }
        }
        setMinesExcluding(r, c);
        checkMines(r, c);
    }

    void handleRegularClick(MineTile tile, int button) {
        if (button == MouseEvent.BUTTON1) {
            // Left click
            if (tile.getText().equals("")) {
                if (mineList.contains(tile)) {
                    revealMines();
                } else {
                    checkMines(tile.r, tile.c);
                }
            }
        } else if (button == MouseEvent.BUTTON3) {
            // Right click
            if (tile.getText().equals("")) {
                tile.setText("🚩");
            } else if (tile.getText().equals("🚩")) {
                tile.setText("");
            }
            updateMinesRemaining();
        }
    }

    void setMinesExcluding(int clickedRow, int clickedCol) {
        mineList = new ArrayList<>();

        int mineLeft = mineCount;
        while (mineLeft > 0) {
            int r = random.nextInt(numRows);
            int c = random.nextInt(numCols);

            MineTile tile = board[r][c];

            if (!(Math.abs(r - clickedRow) <= 1 && Math.abs(c - clickedCol) <= 1) && !mineList.contains(tile)) {
                mineList.add(tile);
                mineLeft--;
            }
        }
    }

    void updateMinesRemaining() {
        minesRemaining = mineCount - getFlaggedTilesCount();
        textLabel.setText("Minesweeper: " + minesRemaining);
    }

    int getFlaggedTilesCount() {
        int flaggedCount = 0;
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                if (board[r][c].getText().equals("🚩")) {
                    flaggedCount++;
                }
            }
        }
        return flaggedCount;
    }

    void setMine() {
        mineList = new ArrayList<>();

        int mineLeft = mineCount;
        while (mineLeft > 0) {
            int r = random.nextInt(numRows);
            int c = random.nextInt(numCols);

            MineTile tile = board[r][c];
            if (!mineList.contains(tile)) {
                mineList.add(tile);
                mineLeft--;
            }
        }
    }

    void revealMines() {
        for (int i = 0; i < mineList.size(); i++) {
            MineTile tile = mineList.get(i);
            tile.setText("💣");
        }

        gameOver = true;
        textLabel.setText("Game Over!");
    }

    void checkMines(int r, int c) {
        if (r < 0 || r >= numRows || c < 0 || c >= numCols) {
            return;
        }

        MineTile tile = board[r][c];
        if (!tile.isEnabled()) {
            return;
        }
        tile.setEnabled(false);
        tilesClicked += 1;

        int minesFound = 0;

        minesFound += countMine(r - 1, c - 1);
        minesFound += countMine(r - 1, c);
        minesFound += countMine(r - 1, c + 1);

        // left and right
        minesFound += countMine(r, c - 1);
        minesFound += countMine(r, c + 1);

        // bottom 3
        minesFound += countMine(r + 1, c - 1);
        minesFound += countMine(r + 1, c);
        minesFound += countMine(r + 1, c + 1);

        if (minesFound > 0) {
            tile.setText(Integer.toString(minesFound));
        } else {
            tile.setText("");

            checkMines(r - 1, c - 1);
            checkMines(r - 1, c);
            checkMines(r - 1, c + 1);

            checkMines(r, c - 1);
            checkMines(r, c + 1);

            checkMines(r + 1, c - 1);
            checkMines(r + 1, c);
            checkMines(r + 1, c + 1);
        }
        if (tilesClicked == numRows * numCols - mineList.size()) {
            gameOver = true;
            textLabel.setText("Mine Clear");
        }
    }

    int countMine(int r, int c) {
        if (r < 0 || r >= numRows || c < 0 || c >= numCols) {
            return 0;
        }
        if (mineList.contains(board[r][c])) {
            return 1;
        }
        return 0;
    }

    void resetGame() {

        firstClick = true;
        gameOver = false;
        tilesClicked = 0;
        textLabel.setText("Minesweeper: " + Integer.toString(mineCount));

        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                MineTile tile = board[r][c];
                tile.setEnabled(true);
                tile.setText("");
            }
        }
        setMine();
    }
}
