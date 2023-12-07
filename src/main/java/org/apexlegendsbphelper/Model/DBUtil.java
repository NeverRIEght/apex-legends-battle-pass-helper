package org.apexlegendsbphelper.Model;
import java.io.File;
import java.sql.*;

public class DBUtil {
    public static void main(String[] a) throws Exception {
        StringBuilder dbUrlBuilder = new StringBuilder();
        dbUrlBuilder.append("jdbc:h2:file:");
        dbUrlBuilder.append(System.getProperty("user.dir"));
        dbUrlBuilder.append(File.separator);
        dbUrlBuilder.append("data");
        dbUrlBuilder.append(File.separator);
        dbUrlBuilder.append("apexlegendsbphelper");

        Connection dbConnection = DriverManager.getConnection(dbUrlBuilder.toString(), "sa", "");

        // add application code here
        Statement stmt = dbConnection.createStatement();

        String query1 =
                "DROP TABLE IF EXISTS Quests; " +
                "CREATE TABLE Quests" +
                "(" +
                    "questID INTEGER UNIQUE not NULL, " +
                    "weekNumber TINYINT NOT NULL CHECK (weekNumber > 0 AND weekNumber <= 12), " +
                    "questNameBR VARCHAR(255) NOT NULL, " +
                    "questNameNBR VARCHAR(255), " +
                    "isCompleted BOOLEAN, " +
                    "questReward TINYINT CHECK (questReward > 0 AND questReward <= 10), " +
                    "PRIMARY KEY (questID)" +
                ")";
        String query2 =
                "INSERT INTO Quests (questID, weekNumber, questNameBR, questNameNBR, isCompleted, questReward)" +
                "VALUES" +
                "(1, 5, 'Поиск сокровищ', 'Treasure Hunt', true, 8)," +
                "(2, 9, 'Спасение затонувшего корабля', 'Rescue Sunken Ship', false, 5)," +
                "(3, 3, 'Путешествие в древний лес', 'Journey to Ancient Forest', true, 10);";

        //String querry = "DROP TABLE Quests";

        stmt.executeUpdate(query1);
        stmt.executeUpdate(query2);

        dbConnection.close();
    }
}
