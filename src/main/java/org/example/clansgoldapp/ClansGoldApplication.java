package org.example.clansgoldapp;

import org.example.clansgoldapp.dao.DatabaseUtil;
import org.example.clansgoldapp.entity.ClanEntity;
import org.example.clansgoldapp.entity.OperationLogEntity;
import org.example.clansgoldapp.service.ClanService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClansGoldApplication {



    public static void main(String[] args) throws SQLException {
        // Создаем таблицы
        DatabaseUtil.createTables();



        // Выводим информацию о кланах до многопоточных операций
        printAllClansInformation("CLAN Information before Multithreaded Operations");

        // Запускаем многопоточные операции с использованием ThreadPool
        performMultithreadedOperations();
        printOperationLogsForClan("OperationLogsForClan");

        // Выводим информацию о кланах после многопоточных операций
        printAllClansInformation("CLAN Information after Multithreaded Operations");
        printClanInformation("CLAN 1 Information",1L);

    }

    private static void performMultithreadedOperations() {
        // Создаем сервис кланов
        ClanService clanService = new ClanService();

        // ID клана для демонстрации
        Long clanId = 1L;

        // Создаем ThreadPool с фиксированным числом потоков (например, 10)
        int threadPoolSize = 3;
        ExecutorService threadPool = Executors.newFixedThreadPool(threadPoolSize);

        // Создаем коллекцию задач
        List<Callable<Void>> tasks = new ArrayList<>();
        for (int i = 0; i < threadPoolSize; i++) {
            tasks.add(() -> {
                performClanOperation(clanService,clanId);
                return null;
            });
        }

        try {
            // Запускаем все задачи и дожидаемся их завершения
            threadPool.invokeAll(tasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Завершаем ThreadPool
            threadPool.shutdown();
        }
    }



    private static void performClanOperation(ClanService clanService, Long clanId) {
        int goldToAdd = 10;
        String donationReason = "Donation";

        // Выполняем операцию добавления золота в клан
        clanService.addGoldToClan(clanId, goldToAdd, donationReason);
    }
    private static void printClanInformation(String message, Long clanId) {
        // Создаем сервис кланов
        ClanService clanService = new ClanService();

        // Получаем информацию о конкретном клане
        ClanEntity clan = clanService.getClanById(clanId);

        // Печатаем информацию о клане
        System.out.println(message);
        if (clan != null) {
            System.out.println("Clan ID: " + clan.getId());
            System.out.println("Clan Name: " + clan.getName());
            System.out.println("Current Gold in Clan: " + clan.getGold());
            System.out.println("--------------");
        } else {
            System.out.println("Clan with ID " + clanId + " not found.");
        }
    }


    private static void printAllClansInformation(String message) {
        // Создаем сервис кланов
        ClanService clanService = new ClanService();

        // Получаем информацию о всех кланах
        List<ClanEntity> allClans = clanService.getAllClans();

        // Печатаем информацию о каждом клане
        System.out.println(message);
        for (ClanEntity clan : allClans) {
            System.out.println("Clan ID: " + clan.getId());
            System.out.println("Clan Name: " + clan.getName());
            System.out.println("Current Gold in Clan: " + clan.getGold());
            System.out.println("--------------");
        }
    }
    private static void printOperationLogsForClan(String message) {
        // Получаем информацию о логах для всех кланов
        ClanService clanService = new ClanService();
        List<OperationLogEntity> logs = clanService.getAllOperationLogs();

        // Выводим информацию о каждом логе
        System.out.println(message);
        for (OperationLogEntity log : logs) {
            System.out.println("Log ID: " + log.getId());
            System.out.println("Clan ID: " + log.getClanId());
            System.out.println("Previous Gold: " + log.getPreviousGold());
            System.out.println("Current Gold: " + log.getCurrentGold());
            System.out.println("Gold Change: " + log.getGoldChange());
            System.out.println("Reason: " + log.getReason());
            System.out.println("Timestamp: " + log.getTimestamp());
            System.out.println("--------------");
        }
    }


}