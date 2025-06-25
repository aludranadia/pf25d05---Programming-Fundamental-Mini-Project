import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class MainMenuPanel extends JPanel {
    private Image backgroundImage;
    private JFrame frame;

    public MainMenuPanel(JFrame frame) {
        this.frame = frame;
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        setPreferredSize(new Dimension(500, 500)); // Ukuran yang diperbarui

        URL imageUrl = getClass().getClassLoader().getResource("images/background ttc.jpg");
        if (imageUrl != null) {
            backgroundImage = new ImageIcon(imageUrl).getImage();
        } else {
            System.err.println("Couldn't find background image: images/background ttc.jpg");
            setBackground(Color.DARK_GRAY);
        }

        JLabel title = new JLabel("Tic Tac Toe");
        title.setFont(new Font("Arial", Font.BOLD, 48)); // Ukuran font lebih besar
        title.setForeground(Color.WHITE);
        title.setOpaque(false);

        JButton startButton = new JButton("Mulai Permainan");
        startButton.addActionListener(e -> {
            String playerX = JOptionPane.showInputDialog(this, "Masukkan nama untuk pemain X:");
            if (playerX == null || playerX.trim().isEmpty()) playerX = "Player X";

            String playerO = JOptionPane.showInputDialog(this, "Masukkan nama untuk pemain O:");
            if (playerO == null || playerO.trim().isEmpty()) playerO = "Player O";

            GameMain gamePanel = new GameMain();
            gamePanel.setPlayerNames(playerX, playerO);

            frame.setContentPane(gamePanel);
            frame.revalidate();
            frame.pack();
            frame.setLocationRelativeTo(null);
        });

        JButton exitButton = new JButton("Keluar");

        for (JButton btn : new JButton[]{startButton, exitButton}) {
            btn.setPreferredSize(new Dimension(250, 50)); // Ukuran tombol lebih besar
            btn.setFont(new Font("Arial", Font.BOLD, 18)); // Font tombol
            btn.setForeground(Color.WHITE);
            btn.setOpaque(false);
            btn.setContentAreaFilled(false);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        }

        exitButton.addActionListener(e -> System.exit(0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 0, 20, 0); // Spasi lebih besar antara komponen
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

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
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}