package GUIOODESIGNWITHLINE;

import java.awt.*;
import java.awt.event.*;
import java.util.List; // Import List untuk menggunakan daftar titik
import javax.swing.*;

/**
 * Tic-Tac-Toe: Versi Grafis Dua Pemain dengan desain OO yang lebih baik.
 * Kelas Board dan Cell dipisahkan dalam kelasnya masing-masing.
 */
public class GameMain extends JPanel {
    private static final long serialVersionUID = 1L; // Untuk mencegah peringatan serializable

    // Mendefinisikan konstanta bernama untuk grafis penggambaran
    public static final String TITLE = "Tic Tac Toe";
    public static final Color COLOR_BG = Color.WHITE;
    public static final Color COLOR_BG_STATUS = new Color(216, 216, 216);
    public static final Color COLOR_CROSS = new Color(239, 105, 80);  // Merah #EF6950
    public static final Color COLOR_NOUGHT = new Color(64, 154, 225); // Biru #409AE1
    public static final Color COLOR_WIN_LINE = new Color(0, 0, 0); // Hitam untuk garis kemenangan
    public static final Font FONT_STATUS = new Font("OCR A Extended", Font.PLAIN, 14);

    // Mendefinisikan objek permainan
    private Board board;         // Papan permainan
    private State currentState;  // Status permainan saat ini
    private Seed currentPlayer;  // Pemain saat ini
    private JLabel statusBar;    // Untuk menampilkan pesan status

    /** Konstruktor untuk mengatur UI dan komponen permainan */
    public GameMain() {

        // JPanel ini akan memicu MouseEvent
        super.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {  // Handler klik mouse
                int mouseX = e.getX();
                int mouseY = e.getY();
                // Mendapatkan baris dan kolom yang diklik
                int row = mouseY / Cell.SIZE;
                int col = mouseX / Cell.SIZE;

                if (currentState == State.PLAYING) {
                    // Memastikan klik berada dalam batas papan dan sel kosong
                    if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS
                            && board.cells[row][col].content == Seed.NO_SEED) {
                        // Memperbarui cells[][] dan mengembalikan status permainan baru setelah langkah
                        currentState = board.stepGame(currentPlayer, row, col);
                        // Mengganti pemain
                        currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                    }
                } else {        // Permainan berakhir
                    newGame();  // Memulai ulang permainan
                }
                // Menyegarkan kanvas gambar
                repaint();  // Memanggil paintComponent().
            }
        });

        // Mengatur status bar (JLabel) untuk menampilkan pesan status
        statusBar = new JLabel();
        statusBar.setFont(FONT_STATUS);
        statusBar.setBackground(COLOR_BG_STATUS);
        statusBar.setOpaque(true);
        statusBar.setPreferredSize(new Dimension(300, 30));
        statusBar.setHorizontalAlignment(JLabel.LEFT);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));

        super.setLayout(new BorderLayout());
        super.add(statusBar, BorderLayout.PAGE_END); // Sama dengan SOUTH
        // Menyesuaikan ukuran JPanel agar sesuai dengan papan dan status bar
        super.setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + statusBar.getPreferredSize().height));
        super.setBorder(BorderFactory.createLineBorder(COLOR_BG_STATUS, 2, false));

        // Mengatur Permainan
        initGame();
        newGame();
    }

    /** Menginisialisasi permainan (berjalan sekali) */
    public void initGame() {
        board = new Board();  // Mengalokasikan papan permainan
    }

    /** Mengatur ulang konten papan permainan dan status saat ini, siap untuk permainan baru */
    public void newGame() {
        board.newGame(); // Mendelegasikan ke board untuk mereset sel-selnya dan menghapus garis kemenangan
        currentPlayer = Seed.CROSS;    // Cross bermain pertama
        currentState = State.PLAYING;  // Siap bermain
    }

    /** Kode penggambaran kustom pada JPanel ini */
    @Override
    public void paintComponent(Graphics g) {  // Dipanggil melalui repaint()
        super.paintComponent(g);
        setBackground(COLOR_BG); // Mengatur warna latar belakang

        board.paint(g);  // Meminta papan permainan untuk menggambar dirinya sendiri

        // BARU: Menggambar garis kemenangan jika permainan dimenangkan
        if (currentState == State.CROSS_WON || currentState == State.NOUGHT_WON) {
            List<Point> winningLine = board.getWinningLinePoints();
            if (winningLine != null && winningLine.size() == 2) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(COLOR_WIN_LINE); // Mengatur warna untuk garis kemenangan
                // Membuat garis lebih tebal dari garis grid
                g2d.setStroke(new BasicStroke(Board.GRID_WIDTH * 1.5f,
                        BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                Point p1 = winningLine.get(0);
                Point p2 = winningLine.get(1);
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }

        // Mencetak pesan status bar
        if (currentState == State.PLAYING) {
            statusBar.setForeground(Color.BLACK);
            statusBar.setText((currentPlayer == Seed.CROSS) ? "X's Turn" : "O's Turn");
        } else if (currentState == State.DRAW) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("It's a Draw! Click to play again.");
        } else if (currentState == State.CROSS_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("'X' Won! Click to play again.");
        } else if (currentState == State.NOUGHT_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("'O' Won! Click to play again.");
        }
    }

    /** Metode "main" sebagai titik masuk */
    public static void main(String[] args) {
        // Menjalankan kode konstruksi GUI di Event-Dispatching Thread untuk keamanan thread
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame(TITLE);
                // Mengatur content-pane dari JFrame ke instance dari JPanel utama
                frame.setContentPane(new GameMain());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack(); // Mengemas semua komponen dalam JFrame ini
                frame.setLocationRelativeTo(null); // Menempatkan jendela aplikasi di tengah
                frame.setVisible(true);            // Menampilkannya
            }
        });
    }
}
