import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardManager {
    private static final String DB_URL = "jdbc:mysql://mysql-tictactoed05-pf25d05.f.aivencloud.com:19289/tictactoe";
    private static final String DB_USER = "avnadmin";
    private static final String DB_PASSWORD = "AVNS_Tv_cBPeS9me6-ToZ53E";

    public static void updateScore(String name) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String checkQuery = "SELECT score FROM leaderboard WHERE name = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, name);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                int currentScore = rs.getInt("score");
                String updateQuery = "UPDATE leaderboard SET score = ? WHERE name = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                updateStmt.setInt(1, currentScore + 1);
                updateStmt.setString(2, name);
                updateStmt.executeUpdate();
            } else {
                String insertQuery = "INSERT INTO leaderboard (name, score) VALUES (?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                insertStmt.setString(1, name);
                insertStmt.setInt(2, 1);
                insertStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<PlayerScore> loadLeaderboard() {
        List<PlayerScore> scores = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT name, score FROM leaderboard ORDER BY score DESC LIMIT 10";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                scores.add(new PlayerScore(rs.getString("name"), rs.getInt("score")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return scores;
    }
}