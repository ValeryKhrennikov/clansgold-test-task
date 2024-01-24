package org.example.clansgoldapp.dao;

import org.example.clansgoldapp.entity.OperationLogEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OperationLogDAO {

    public void insertOperationLog(Connection connection, OperationLogEntity log) {
        if (connection == null) {
            // Обработка случая, когда соединение равно null
            System.err.println("Connection is null. Cannot insert operation log.");
            return;
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO operation_logs (clan_id, previous_gold, current_gold, change_amount, reason, timestamp) " +
                        "VALUES (?, ?, ?, ?, ?, ?)")) {

            preparedStatement.setLong(1, log.getClanId());
            preparedStatement.setInt(2, log.getPreviousGold());
            preparedStatement.setInt(3, log.getCurrentGold());
            preparedStatement.setInt(4, log.getGoldChange());
            preparedStatement.setString(5, log.getReason());
            preparedStatement.setTimestamp(6, new java.sql.Timestamp(log.getTimestamp().getTime()));

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            // Логирование ошибки вместо вывода стека трейса
            System.err.println("Error inserting operation log: " + e.getMessage());
        }
    }
    public List<OperationLogEntity> getAllOperationLogs() {
        List<OperationLogEntity> logs = new ArrayList<>();

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM operation_logs");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                OperationLogEntity log = new OperationLogEntity();
                log.setId(resultSet.getLong("id"));
                log.setClanId(resultSet.getLong("clan_id"));
                log.setPreviousGold(resultSet.getInt("previous_gold"));
                log.setCurrentGold(resultSet.getInt("current_gold"));
                log.setGoldChange(resultSet.getInt("change_amount"));
                log.setReason(resultSet.getString("reason"));
                log.setTimestamp(resultSet.getTimestamp("timestamp"));
                logs.add(log);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving operation logs: " + e.getMessage());
        }

        return logs;
    }

}
