package org.apexlegendsbphelper.Model;
import java.io.File;
import java.sql.*;

public class DBUtil {
    public static void main(String[] a) throws Exception {
        wipeDB();
        createDB();
//        Connection dbConnection = openDBConnection();
//        Statement stmt = dbConnection.createStatement();
//        String query =
//                "INSERT INTO Quests (questID, weekNumber, questNameBR, questNameNBR, isCompleted, questReward)" +
//                        "VALUES" +
//                        "(1, 5, 'Поиск сокровищ', 'Treasure Hunt', true, 8)," +
//                        "(2, 8, 'Спасение затонувшего корабля', 'Rescue Sunken Ship', false, 5)," +
//                        "(3, 3, 'Путешествие в древний лес', 'Journey to Ancient Forest', true, 10);";
//
//        stmt.executeUpdate(query);
//        closeDBConnection(dbConnection);

        addQuestToDB(new Quest("Поиск сокровищ", "Treasure Hunt", "Completed", "8", 1));
    }

    public static Connection openDBConnection() throws SQLException {
        StringBuilder dbUrlBuilder = new StringBuilder();
        dbUrlBuilder.append("jdbc:h2:file:");
        dbUrlBuilder.append(System.getProperty("user.dir"));
        dbUrlBuilder.append(File.separator);
        dbUrlBuilder.append("data");
        dbUrlBuilder.append(File.separator);
        dbUrlBuilder.append("apexlegendsbphelper");

        try {
            return DriverManager.getConnection(dbUrlBuilder.toString(), "sa", "");
        } catch (SQLException e) {
            System.out.println("Error while opening DB connection: " + e.getMessage());
            throw e;
        }
    }
    public static void closeDBConnection(Connection dbConnection) throws SQLException {
        try {
            dbConnection.close();
        } catch (SQLException e) {
            System.out.println("Error while closing DB connection: " + e.getMessage());
            throw e;
        }
    }
    public static void createDB() throws SQLException {
        try {
            Connection dbConnection = openDBConnection();
            Statement stmt = dbConnection.createStatement();

            String query =
                    "CREATE TABLE IF NOT EXISTS Quests" +
                    "(" +
                    "questID BIGINT AUTO_INCREMENT UNIQUE not NULL, " +
                    "weekNumber TINYINT NOT NULL CHECK (weekNumber > 0 AND weekNumber <= 12), " +
                    "questNameBR VARCHAR(255) NOT NULL, " +
                    "questNameNBR VARCHAR(255), " +
                    "isCompleted BOOLEAN, " +
                    "questReward TINYINT CHECK (questReward > 0 AND questReward <= 10), " +
                    "PRIMARY KEY (questID)" +
                    ")";

            stmt.executeUpdate(query);
            closeDBConnection(dbConnection);
        } catch (SQLException e) {
            System.out.println("Error while creating DB: " + e.getMessage());
            throw e;
        }
    }
    public static void wipeDB() throws SQLException {
        try {
            Connection dbConnection = openDBConnection();
            Statement stmt = dbConnection.createStatement();

            String query =
                    "DROP TABLE IF EXISTS Quests;";

            stmt.executeUpdate(query);
            closeDBConnection(dbConnection);
        } catch (SQLException e) {
            System.out.println("Error while wiping DB: " + e.getMessage());
            throw e;
        }
    }
    public static void addQuestToDB(Quest inputQuest) throws SQLException {
        try {
            Connection dbConnection = openDBConnection();
            String questnameNBRString = inputQuest.getQuestNameNBR() != null ? inputQuest.getQuestNameNBR() : "";

            String query = "INSERT INTO Quests (weekNumber, questNameBR, questNameNBR, isCompleted, questReward) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = dbConnection.prepareStatement(query)) {
                preparedStatement.setInt(1, inputQuest.getWeekNumber());
                preparedStatement.setString(2, inputQuest.getQuestNameBR());
                preparedStatement.setString(3, questnameNBRString);
                preparedStatement.setBoolean(4, inputQuest.isCompleted());
                preparedStatement.setInt(5, inputQuest.getQuestReward());

                preparedStatement.executeUpdate();
            }

            closeDBConnection(dbConnection);
        } catch (SQLException e) {
            System.out.println("Error while adding quest to DB: " + e.getMessage());
            throw e;
        }
    }
}
