import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainMenuPanel extends JPanel {
    private GameMain gamePanel;
    private JFrame frame;

    public MainMenuPanel(JFrame frame) {
        this.frame = frame;
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Tic Tac Toe");
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JButton startButton = new JButton("Mulai Permainan");
        JButton exitButton = new JButton("Keluar");

        startButton.setPreferredSize(new Dimension(200, 40));
        exitButton.setPreferredSize(new Dimension(200, 40));

        startButton.addActionListener(e -> {
            gamePanel = new GameMain();
            frame.setContentPane(gamePanel);
            frame.revalidate(); // Refresh content
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
}