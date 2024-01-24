package org.example.clansgoldapp.service;

import org.example.clansgoldapp.dao.ClanDAO;
import org.example.clansgoldapp.dao.DatabaseUtil;
import org.example.clansgoldapp.dao.OperationLogDAO;
import org.example.clansgoldapp.entity.ClanEntity;
import org.example.clansgoldapp.entity.OperationLogEntity;
import org.example.clansgoldapp.util.LockUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.locks.Lock;

public class ClanService {

    private final ClanDAO clanDAO;
    private final OperationLogDAO operationLogDAO;


    public ClanService() {
        this.clanDAO = new ClanDAO();
        this.operationLogDAO = new OperationLogDAO();
    }

    public ClanEntity getClanById(Long clanId) {
        return clanDAO.getClanById(clanId);
    }

    public List<ClanEntity> getAllClans() {
        return clanDAO.getAllClans();
    }
    public List<OperationLogEntity> getAllOperationLogs(){
        return operationLogDAO.getAllOperationLogs();
    }

    public void addGoldToClan(Long clanId, int gold, String reason) {
        // Получаем замок для данного клана
        Lock lock = LockUtil.getLock(clanId);

        lock.lock();
        try {
            // Получаем копию ClanEntity для данного клана
            ClanEntity clan = clanDAO.getClanById(clanId);

            int previousGold = clan.getGold();
            int currentGold = previousGold + gold;

            // Обновляем количество золота в клане
            clanDAO.updateClanGold(clanId, currentGold);

            // Создаем запись в логах операций
            OperationLogEntity log = createOperationLog(clanId, previousGold, currentGold, gold, reason);

            // Сохраняем запись в логах операций
            try (Connection connection = DatabaseUtil.getConnection()) {
                operationLogDAO.insertOperationLog(connection, log);
            } catch (SQLException e) {
                // Логирование ошибки
                System.err.println("Error adding gold to clan: " + e.getMessage());
                e.printStackTrace();
            }
        } finally {
            lock.unlock();
        }
    }




    public ClanEntity getClan(Long clanId) {
        try (Connection connection = DatabaseUtil.getConnection()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM CLANS WHERE id = ?")) {

                // Устанавливаем параметр для id
                preparedStatement.setLong(1, clanId);

                // Выполняем запрос
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        // Создаем объект ClanEntity и заполняем его данными из результата запроса
                        ClanEntity clan = new ClanEntity();
                        clan.setId(resultSet.getLong("id"));
                        clan.setName(resultSet.getString("name"));
                        clan.setGold(resultSet.getInt("gold"));
                        return clan;
                    }
                }

            }
        } catch (SQLException e) {
            // Логирование ошибки вместо вывода стека трейса
            System.err.println("Error retrieving clan: " + e.getMessage());
        }
        return null;
    }


    private OperationLogEntity createOperationLog(Long clanId, int previousGold, int currentGold, int goldChange, String reason) {
        OperationLogEntity log = new OperationLogEntity();
        log.setClanId(clanId);
        log.setPreviousGold(previousGold);
        log.setCurrentGold(currentGold);
        log.setGoldChange(goldChange); // Используем параметр goldChange вместо change
        log.setReason(reason);
        log.setTimestamp(new java.util.Date());
        return log;
    }

}
