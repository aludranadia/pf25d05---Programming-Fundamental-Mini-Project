import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {
    private Image backgroundImage;

    public MainMenuPanel(JFrame frame) {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        // Load background image from resource folder
        backgroundImage = new ImageIcon(getClass().getClassLoader().getResource("images/background ttc.jpg")).getImage();

        JLabel title = new JLabel("Tic Tac Toe");
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        title.setOpaque(false);

        JButton startButton = new JButton("Mulai Permainan");
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

        startButton.addActionListener(e -> {
            GameMain gamePanel = new GameMain();
            frame.setContentPane(gamePanel);
            frame.revalidate();
            frame.repaint();
        });

        exitButton.addActionListener(e -> System.exit(0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 0, 15, 0);
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
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}