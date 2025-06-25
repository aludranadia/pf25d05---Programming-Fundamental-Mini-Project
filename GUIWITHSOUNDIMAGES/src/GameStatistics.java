import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class GameStatistics {

    // --- Detail Koneksi Database Aiven.io (MySQL) ---
    // GANTI placeholder ini dengan detail koneksi aktual dari layanan Aiven MySQL Anda!
    // Pastikan ini cocok dengan yang ada di LeaderboardManager.java
    private static final String DB_URL = "jdbc:mysql://mysql-tictactoed05-pf25d05.f.aivencloud.com:19289/tictactoe";
    private static final String DB_USER = "avnadmin";
    private static final String DB_PASSWORD = "AVNS_Tv_cBPeS9me6-ToZ53E";

    private static final String TABLE_NAME = "game_stats"; // Nama tabel untuk statistik game

    private String userId; // Identifikasi unik untuk setiap pemain (misal: username)
    private int crossWins;
    private int noughtWins;
    private int draws;

    public GameStatistics(String userId) {
        this.userId = userId;
        this.crossWins = 0;
        this.noughtWins = 0;
        this.draws = 0;
        initializeDatabase();
        loadStatisticsFromDatabase();
    }

    private void initializeDatabase() {
        try {
            // Memuat driver JDBC MySQL
            // Untuk MySQL Connector/J 8.x ke atas, pendaftaran driver tidak lagi diperlukan secara eksplisit
            // Class.forName("com.mysql.cj.jdbc.Driver"); // Baris ini tidak wajib tapi bisa membantu debug
            System.out.println("MySQL JDBC Driver should be available!");
            createTableIfNotExists();
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createTableIfNotExists() throws SQLException {
        // SQL untuk membuat tabel jika belum ada, disesuaikan untuk MySQL
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + "user_id VARCHAR(255) PRIMARY KEY,"
                + "cross_wins INT DEFAULT 0,"
                + "nought_wins INT DEFAULT 0,"
                + "draws INT DEFAULT 0"
                + ");";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
            System.out.println("Table '" + TABLE_NAME + "' ensured to exist (MySQL).");
        }
    }

    private void loadStatisticsFromDatabase() {
        String selectSQL = "SELECT cross_wins, nought_wins, draws FROM " + TABLE_NAME + " WHERE user_id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setString(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                this.crossWins = resultSet.getInt("cross_wins");
                this.noughtWins = resultSet.getInt("nought_wins");
                this.draws = resultSet.getInt("draws");
                System.out.println("Statistics loaded for user: " + userId);
            } else {
                System.out.println("No existing statistics for user: " + userId + ". Creating new entry.");
                // Jika user_id tidak ada, simpan statistik awal (semua 0)
                saveStatisticsToDatabase();
            }
        } catch (SQLException e) {
            System.err.println("Error loading statistics: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void saveStatisticsToDatabase() {
        // Menggunakan ON DUPLICATE KEY UPDATE untuk INSERT atau UPDATE baris pada MySQL
        String upsertSQL = "INSERT INTO " + TABLE_NAME + " (user_id, cross_wins, nought_wins, draws) "
                + "VALUES (?, ?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE "
                + "cross_wins = VALUES(cross_wins), "
                + "nought_wins = VALUES(nought_wins), "
                + "draws = VALUES(draws);";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(upsertSQL)) {

            preparedStatement.setString(1, userId);
            preparedStatement.setInt(2, crossWins);
            preparedStatement.setInt(3, noughtWins);
            preparedStatement.setInt(4, draws);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Statistics saved for user: " + userId);
            } else {
                System.err.println("Failed to save statistics for user: " + userId);
            }
        } catch (SQLException e) {
            System.err.println("Error saving statistics: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void recordWin(char winner) {
        if (winner == 'X') {
            crossWins++;
        } else if (winner == 'O') {
            noughtWins++;
        }
        saveStatisticsToDatabase(); // Simpan setelah setiap perubahan
    }

    public void recordDraw() {
        draws++;
        saveStatisticsToDatabase(); // Simpan setelah setiap perubahan
    }

    public int getCrossWins() {
        return crossWins;
    }

    public int getNoughtWins() {
        return noughtWins;
    }

    public int getDraws() {
        return draws;
    }

    // Metode utama untuk pengujian sederhana
    public static void main(String[] args) {
        // Contoh penggunaan
        GameStatistics stats = new GameStatistics("player123");
        System.out.println("Initial stats for player123: X=" + stats.getCrossWins() + ", O=" + stats.getNoughtWins() + ", Draws=" + stats.getDraws());

        stats.recordWin('X');
        stats.recordDraw();
        stats.recordWin('O');

        System.out.println("Updated stats for player123: X=" + stats.getCrossWins() + ", O=" + stats.getNoughtWins() + ", Draws=" + stats.getDraws());

        GameStatistics stats2 = new GameStatistics("newPlayer");
        System.out.println("Stats for newPlayer: X=" + stats2.getCrossWins() + ", O=" + stats2.getNoughtWins() + ", Draws=" + stats2.getDraws());
        stats2.recordWin('X');
        System.out.println("Updated stats for newPlayer: X=" + stats2.getCrossWins() + ", O=" + stats2.getNoughtWins() + ", Draws=" + stats2.getDraws());
    }
}
