import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {
    private Image backgroundImage;
    private JFrame frame; // Menambahkan referensi ke JFrame

    public MainMenuPanel(JFrame frame) {
        this.frame = frame; // Simpan referensi frame
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        // --- TAMBAHKAN BARIS INI ---
        // Atur ukuran preferred untuk MainMenuPanel agar tidak terlalu kecil
        // Ukuran ini harus setidaknya sama atau lebih besar dari gambar latar belakang
        // atau cukup besar untuk menampung semua komponen.
        setPreferredSize(new Dimension(400, 450)); // Contoh ukuran, sesuaikan jika perlu
        // --- AKHIR TAMBAH ---

        // Load background image from resource folder
        backgroundImage = new ImageIcon(getClass().getClassLoader().getResource("images/background ttc.jpg")).getImage();


        JLabel title = new JLabel("Tic Tac Toe");
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        title.setOpaque(false);

        JButton startButton = new JButton("Mulai Permainan");
        startButton.addActionListener(e -> {
            // Tambahkan input nama di sini
            String playerX = JOptionPane.showInputDialog(this, "Masukkan nama untuk pemain X:");
            if (playerX == null || playerX.trim().isEmpty()) playerX = "Player X";

            String playerO = JOptionPane.showInputDialog(this, "Masukkan nama untuk pemain O:");
            if (playerO == null || playerO.trim().isEmpty()) playerO = "Player O";

            GameMain gamePanel = new GameMain();
            gamePanel.setPlayerNames(playerX, playerO); // ← panggil method setter

            frame.setContentPane(gamePanel);
            frame.revalidate();
            // frame.repaint(); // repaint tidak selalu diperlukan setelah setContentPane dan revalidate
            frame.pack(); // Panggil pack() di sini agar frame menyesuaikan ukuran panel game
        });

        JButton exitButton = new JButton("Keluar");

        // Styling buttons transparan
        for (JButton btn : new JButton[]{startButton, exitButton}) {
            btn.setPreferredSize(new Dimension(200, 40));
            btn.setForeground(Color.WHITE);
            btn.setOpaque(false);
            btn.setContentAreaFilled(false);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        }

        exitButton.addActionListener(e -> System.exit(0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 0, 15, 0); // Spasi antara komponen
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 0;
        add(title, gbc);
        gbc.gridy = 1;
        add(startButton, gbc);
        gbc.gridy = 2;
        add(exitButton, gbc);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            // Gambar latar belakang mengisi seluruh area panel
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}