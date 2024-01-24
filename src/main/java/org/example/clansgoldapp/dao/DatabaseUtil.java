package org.example.clansgoldapp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtil {

    private static final String URL = "jdbc:h2:./data/clansgold";
    private static final String USER = "your_username";
    private static final String PASSWORD = "your_password";

    public static Connection getConnection() {
        try {
            // Загружаем JDBC-драйвер H2
            Class.forName("org.h2.Driver");

            // Получаем соединение с базой данных
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            // Логирование ошибки вместо вывода стека трейса
            System.err.println("Error getting connection to the database: " + e.getMessage());
            return null;
        }
    }

    public static void createTables() {
        // Получаем соединение с базой данных
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            // Создаем таблицу clans (если её еще нет)
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS clans ("
                    + "id BIGINT AUTO_INCREMENT PRIMARY KEY,"
                    + "name VARCHAR(255) UNIQUE,"
                    + "gold INT"
                    + ")");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS operation_logs ("
                    + "id BIGINT AUTO_INCREMENT PRIMARY KEY,"
                    + "clan_id BIGINT,"
                    + "previous_gold INT,"
                    + "current_gold INT,"
                    + "change_amount INT,"
                    + "reason VARCHAR(255),"
                    + "timestamp TIMESTAMP"
                    + ")");

            // Вставляем данные в таблицу clans, если она пуста
            try (ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM clans")) {
                resultSet.next();
                int rowCount = resultSet.getInt(1);

                if (rowCount == 0) {
                    statement.executeUpdate("INSERT INTO clans (name, gold) VALUES ('Clan 1', 100)");
                    statement.executeUpdate("INSERT INTO clans (name, gold) VALUES ('Clan 2', 200)");
                }
            }

        } catch (Exception e) {
            // Логирование ошибки вместо вывода стека трейса
            System.err.println("Error creating tables: " + e.getMessage());
        }
    }
}
