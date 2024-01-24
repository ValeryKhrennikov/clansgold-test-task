package org.example.clansgoldapp.dao;

import org.example.clansgoldapp.entity.ClanEntity;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClanDAO {

    public ClanEntity getClanById(Long id) {
        ClanEntity clan = null;

        try (Connection connection = DatabaseUtil.getConnection()) {
            // Проверяем существование таблицы "CLANS"
            checkClansTableExistence(connection);

            String sql = "SELECT * FROM CLANS WHERE id = ?";
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setLong(1, id);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        clan = new ClanEntity();
                        clan.setId(resultSet.getLong("id"));
                        clan.setName(resultSet.getString("name"));
                        clan.setGold(resultSet.getInt("gold"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clan;
    }

    private void checkClansTableExistence(Connection connection) throws SQLException {
        // Запрос для проверки существования таблицы "CLANS"
        String query = "SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'CLANS'";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (!resultSet.next()) {
                throw new SQLException("Table 'CLANS' not found.");
            }
        }
    }

    public void updateClanGold(long clanId, int newGold) {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE CLANS SET gold = ? WHERE id = ?")) {

            preparedStatement.setInt(1, newGold);
            preparedStatement.setLong(2, clanId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            // Обработка ошибок
        }
    }

    public List<ClanEntity> getAllClans() {
        List<ClanEntity> clans = new ArrayList<>();

        String sql = "SELECT * FROM CLANS ORDER BY id";

        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                ClanEntity clan = new ClanEntity();
                clan.setId(resultSet.getLong("id"));
                clan.setName(resultSet.getString("name"));
                clan.setGold(resultSet.getInt("gold"));
                clans.add(clan);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Обработка ошибок может быть улучшена в реальном приложении
        }

        return clans;
    }
}
