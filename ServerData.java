package Client.Pro1;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServerData {
    private static Connection connection;
    private final String url = "jdbc:mysql://localhost:3306/chatappdb";
    private final String user = "root";
    private final String password = "Pea@#123";

    public ServerData() {
        try {
            connection = DriverManager.getConnection(url, user, password);
            createTableIfNotExists();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTableIfNotExists() {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, "messages", null);

            if (!tables.next()) {
                createTable();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS messages (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "sender VARCHAR(30)," +
                "content VARCHAR(255) NOT NULL," +
                "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create table");
        }
    }

    public void storeMessage(String sender, String content) {
        try {
            String query = "INSERT INTO messages (sender, content, timestamp) VALUES (?, ?, NOW())";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, sender);
                preparedStatement.setString(2, content);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getMessages() {
        List<String> messages = new ArrayList<>();
        try {
            String query = "SELECT * FROM messages";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next()) {
                    String sender = resultSet.getString("sender");
                    String content = resultSet.getString("content");
                    Timestamp timestamp = resultSet.getTimestamp("timestamp");
                    messages.add(sender + " (" + timestamp + "): " + content);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }
}
