import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Tic-Tac-Toe: Two-player Graphic version with better OO design.
 * The Board and Cell classes are separated in their own classes.
 */
public class GameMain extends JPanel {
    private static final long serialVersionUID = 1L; // to prevent serializable warning

    // Define named constants for the drawing graphics
    public static final String TITLE = "Tic Tac Toe";
    public static final Color COLOR_BG = Color.WHITE;
    public static final Color COLOR_BG_STATUS = new Color(216, 216, 216);
    public static final Color COLOR_CROSS = new Color(239, 105, 80); // Red #EF6950
    public static final Color COLOR_NOUGHT = new Color(64, 154, 225); // Blue #409AE1
    public static final Font FONT_STATUS = new Font("OCR A Extended", Font.PLAIN, 14);

    // Define game objects
    private Board board;         // the game board
    private State currentState;  // the current state of the game
    private Seed currentPlayer;  // the current player
    private JLabel statusBar;
    private String playerXName = "Player X";
    private String playerOName = "Player O";
    private JLabel scoreBoard;   // for displaying status message
    private int crossWins = 0;
    private int noughtWins = 0;

    // Tambahkan topPanel dan boardPanel sebagai anggota kelas
    private JPanel topPanel;
    private JPanel boardPanel;

    /** Constructor to setup the UI and game components */
    public GameMain() {
        // Setup the status bar (JLabel) to display status message
        statusBar = new JLabel();
        statusBar.setFont(FONT_STATUS);
        statusBar.setBackground(COLOR_BG_STATUS);
        statusBar.setOpaque(true);
        statusBar.setPreferredSize(new Dimension(300, 30)); // Ukuran yang sesuai
        statusBar.setHorizontalAlignment(JLabel.LEFT);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));

        scoreBoard = new JLabel();
        scoreBoard.setFont(FONT_STATUS);
        scoreBoard.setBackground(COLOR_BG_STATUS);
        scoreBoard.setOpaque(true);
        scoreBoard.setPreferredSize(new Dimension(200, 30)); // Sesuaikan lebar agar tidak terlalu dominan
        scoreBoard.setHorizontalAlignment(JLabel.LEFT);
        scoreBoard.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));

        // Set layout utama dulu
        super.setLayout(new BorderLayout());

        // Panel atas untuk skor dan tombol reset
        topPanel = new JPanel(new BorderLayout()); // Inisialisasi di sini
        topPanel.setBackground(COLOR_BG_STATUS);
        topPanel.setPreferredSize(new Dimension(Board.CANVAS_WIDTH, 40)); // Berikan preferred size untuk topPanel

        // Tambahkan scoreBoard ke kiri
        topPanel.add(scoreBoard, BorderLayout.WEST);

        // Tambahkan tombol reset ke kanan
        JButton resetButton = new JButton("Reset Score");
        resetButton.setFont(FONT_STATUS);
        resetButton.addActionListener(e -> {
            crossWins = 0;
            noughtWins = 0;
            updateScoreBoard();
            newGame(); // Mulai game baru juga setelah reset skor
        });
        topPanel.add(resetButton, BorderLayout.EAST);

        // Tambahkan panel atas ke PAGE_START
        super.add(topPanel, BorderLayout.NORTH); // Gunakan BorderLayout.NORTH untuk kejelasan

        // Tambahkan statusBar ke PAGE_END
        super.add(statusBar, BorderLayout.SOUTH);

        // Buat panel terpisah untuk board agar penempatan lebih mudah
        // Ini akan menempati area CENTER di BorderLayout
        boardPanel = new JPanel() { // Inisialisasi di sini
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


        // Hapus setPreferredSize pada super, biarkan BorderLayout yang mengatur
        super.setBorder(BorderFactory.createLineBorder(COLOR_BG_STATUS, 2, false));

        // Set up Game
        initGame();
        newGame();

        // This JPanel fires MouseEvent
        // Letakkan MouseListener setelah semua komponen diinisialisasi dan diatur
        boardPanel.addMouseListener(new MouseAdapter() { // Mouse listener dipasang di boardPanel
            @Override
            public void mouseClicked(MouseEvent e) { // mouse-clicked handler
                int mouseX = e.getX();
                int mouseY = e.getY();
                // Karena listener ada di boardPanel, koordinat sudah relatif ke boardPanel
                // Jadi tidak perlu offset dengan topPanel atau statusBar
                int row = mouseY / Cell.SIZE;
                int col = mouseX / Cell.SIZE;

                if (currentState == State.PLAYING) {
                    if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS
                            && board.cells[row][col].content == Seed.NO_SEED) {
                        currentState = board.stepGame(currentPlayer, row, col);
                        currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                    }
                } else { // game over
                    newGame(); // restart the game
                }

                // Play appropriate sound clip
                if (currentState == State.PLAYING) {
                    SoundEffect.EAT_FOOD.play();
                } else {
                    SoundEffect.DIE.play();
                }

                if (currentState == State.CROSS_WON) {
                    crossWins++;
                } else if (currentState == State.NOUGHT_WON) {
                    noughtWins++;
                }
                updateScoreBoard(); // panggil method untuk update tampilan

                // Refresh the drawing canvas
                boardPanel.repaint(); // Callback paintComponent() dari boardPanel
                repaint(); // Repaint GameMain untuk update status bar
            }
        });
    }

    public void setPlayerNames(String xName, String oName) {
        this.playerXName = xName;
        this.playerOName = oName;
        updateScoreBoard(); // Update tampilan nama pemain di scoreboard
    }

    /** Initialize the game (run once) */
    public void initGame() {
        board = new Board(); // allocate the game-board
    }

    /** Reset the game-board contents and the current-state, ready for new game */
    public void newGame() {
        board.newGame(); // Panggil newGame() milik Board untuk mereset semua sel dan winningLineInfo
        currentPlayer = Seed.CROSS; // cross plays first
        currentState = State.PLAYING; // ready to play
        updateScoreBoard(); // Pastikan scoreboard diupdate
        boardPanel.repaint(); // Penting untuk me-repaint board setelah newGame
        repaint(); // Repaint GameMain untuk update status bar
    }

    /** Custom painting codes on this JPanel */
    @Override
    public void paintComponent(Graphics g) { // Callback via repaint()
        super.paintComponent(g);
        // board.paint(g); // Ini dipindahkan ke boardPanel.paintComponent()

        // Print status-bar message
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

    /** The entry "main" method */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(TITLE);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(true); // supaya responsive bekerja
            frame.setLocationRelativeTo(null); // center

            frame.setContentPane(new MainMenuPanel(frame));
            frame.setVisible(true);
            frame.pack(); // Tambahkan ini agar frame menyesuaikan ukuran kontennya
        });
    }

    private void updateScoreBoard() {
        scoreBoard.setText(playerXName + " Wins: " + crossWins + " " + playerOName + " Wins: " + noughtWins);
    }
}