import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;
import javax.swing.SwingWorker;

public class GameMain extends JPanel {
    private static final long serialVersionUID = 1L;

    public static final String TITLE = "Tic Tac Toe";
    public static final Color COLOR_BG = Color.WHITE;
    public static final Color COLOR_BG_STATUS = new Color(216, 216, 216);
    public static final Color COLOR_CROSS = new Color(239, 105, 80);
    public static final Color COLOR_NOUGHT = new Color(64, 154, 225);
    public static final Font FONT_STATUS = new Font("OCR A Extended", Font.PLAIN, 14);

    private Board board;
    private State currentState;
    private Seed currentPlayer;
    private JLabel statusBar;
    private String playerXName = "Player X";
    private String playerOName = "Player O";
    private JLabel scoreBoard;
    private int crossWins = 0;
    private int noughtWins = 0;

    private JPanel topPanel;
    private JPanel boardPanel;

    public GameMain() {
        SoundEffect.initGame();

        statusBar = new JLabel();
        statusBar.setFont(FONT_STATUS);
        statusBar.setBackground(COLOR_BG_STATUS);
        statusBar.setOpaque(true);
        statusBar.setPreferredSize(new Dimension(300, 30));
        statusBar.setHorizontalAlignment(JLabel.LEFT);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));

        scoreBoard = new JLabel();
        scoreBoard.setFont(FONT_STATUS);
        scoreBoard.setBackground(COLOR_BG_STATUS);
        scoreBoard.setOpaque(true);
        scoreBoard.setHorizontalAlignment(JLabel.LEFT);
        scoreBoard.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));

        super.setLayout(new BorderLayout());

        topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(COLOR_BG_STATUS);
        topPanel.setPreferredSize(new Dimension(Board.CANVAS_WIDTH, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 0, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        topPanel.add(scoreBoard, gbc);

        JButton resetButton = new JButton("Reset Score");
        resetButton.setFont(FONT_STATUS);
        resetButton.setMargin(new Insets(5, 10, 5, 10));
        resetButton.addActionListener(e -> {
            crossWins = 0;
            noughtWins = 0;
            updateScoreBoard();
            newGame();
        });
        gbc.gridx = 1;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        topPanel.add(resetButton, gbc);

        JButton leaderboardButton = new JButton("Leaderboard");
        leaderboardButton.setFont(FONT_STATUS);
        leaderboardButton.setMargin(new Insets(5, 10, 5, 10));
        leaderboardButton.addActionListener(e -> {
            new SwingWorker<List<PlayerScore>, Void>() {
                @Override
                protected List<PlayerScore> doInBackground() throws Exception {
                    return LeaderboardManager.loadLeaderboard();
                }

                @Override
                protected void done() {
                    try {
                        List<PlayerScore> scores = get();
                        showLeaderboardDialog(scores);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(GameMain.this, "Gagal memuat leaderboard.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }.execute();
        });
        gbc.gridx = 2;
        topPanel.add(leaderboardButton, gbc);

        super.add(topPanel, BorderLayout.NORTH);
        super.add(statusBar, BorderLayout.SOUTH);

        boardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setBackground(COLOR_BG);
                board.paint(g);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT);
            }
        };
        super.add(boardPanel, BorderLayout.CENTER);
        super.setBorder(BorderFactory.createLineBorder(COLOR_BG_STATUS, 2, false));

        initGame();
        newGame();

        boardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();
                int row = mouseY / Cell.SIZE;
                int col = mouseX / Cell.SIZE;

                if (currentState == State.PLAYING) {
                    if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS
                            && board.cells[row][col].content == Seed.NO_SEED) {
                        currentState = board.stepGame(currentPlayer, row, col);
                        currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;

                        SoundEffect.EAT_FOOD.play();

                        if (currentState == State.CROSS_WON) {
                            crossWins++;
                            new SwingWorker<Void, Void>() {
                                @Override
                                protected Void doInBackground() throws Exception {
                                    LeaderboardManager.updateScore(playerXName);
                                    // AKTIFKAN GameStatistics untuk pemain X
                                    new GameStatistics(playerXName).recordWin('X');
                                    return null;
                                }
                            }.execute();
                            SoundEffect.DIE.play();
                        } else if (currentState == State.NOUGHT_WON) {
                            noughtWins++;
                            new SwingWorker<Void, Void>() {
                                @Override
                                protected Void doInBackground() throws Exception {
                                    LeaderboardManager.updateScore(playerOName);
                                    // AKTIFKAN GameStatistics untuk pemain O
                                    new GameStatistics(playerOName).recordWin('O');
                                    return null;
                                }
                            }.execute();
                            SoundEffect.DIE.play();
                        } else if (currentState == State.DRAW) {
                            new SwingWorker<Void, Void>() {
                                @Override
                                protected Void doInBackground() throws Exception {
                                    // AKTIFKAN GameStatistics untuk seri (jika Anda memiliki userId khusus untuk seri)
                                    // Contoh: Gunakan "DRAW_STATS" sebagai userId untuk mencatat seri global
                                    new GameStatistics("DRAW_STATS").recordDraw();
                                    return null;
                                }
                            }.execute();
                            SoundEffect.DIE.play();
                        }

                        updateScoreBoard();
                        boardPanel.repaint();
                        repaint();
                    }
                } else {
                    int option = JOptionPane.showConfirmDialog(
                            GameMain.this,
                            "Permainan berakhir. Mau main lagi?",
                            "Tic Tac Toe",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                    );

                    if (option == JOptionPane.YES_OPTION) {
                        newGame();
                    } else {
                        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(GameMain.this);
                        if (topFrame != null) {
                            topFrame.dispose();
                        }
                    }
                }
            }
        });
    }

    public void setPlayerNames(String xName, String oName) {
        this.playerXName = xName;
        this.playerOName = oName;
        updateScoreBoard();
    }

    public void initGame() {
        board = new Board();
    }

    public void newGame() {
        board.newGame();
        currentPlayer = Seed.CROSS;
        currentState = State.PLAYING;
        updateScoreBoard();
        boardPanel.repaint();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (currentState == State.PLAYING) {
            statusBar.setForeground(Color.BLACK);
            statusBar.setText((currentPlayer == Seed.CROSS)
                    ? playerXName + "'s Turn (X)"
                    : playerOName + "'s Turn (O)");
        } else if (currentState == State.DRAW) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("It's a Draw! Click to play again.");
        } else if (currentState == State.CROSS_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText(playerXName + " (X) Won! Click to play again.");
        } else if (currentState == State.NOUGHT_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText(playerOName + " (O) Won! Click to play again.");
        }
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(TITLE);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(true);
            frame.setLocationRelativeTo(null);

            frame.setContentPane(new MainMenuPanel(frame));
            frame.setVisible(true);
            frame.pack();
        });
    }

    private void updateScoreBoard() {
        scoreBoard.setText(playerXName + " Wins: " + crossWins + "    " + playerOName + " Wins: " + noughtWins);
    }

    private void showLeaderboardDialog(List<PlayerScore> scores) {
        String[] columnNames = {"Nama", "Skor"};
        Object[][] data = new Object[scores.size()][2];

        for (int i = 0; i < scores.size(); i++) {
            data[i][0] = scores.get(i).name;
            data[i][1] = scores.get(i).score;
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        JOptionPane.showMessageDialog(this, scrollPane, "Leaderboard", JOptionPane.PLAIN_MESSAGE);
    }
}