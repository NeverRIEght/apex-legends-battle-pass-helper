package org.apexlegendsbphelper.Model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {
    public static boolean deleteDirectory(File directory) {
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        if (!deleteDirectory(file)) {
                            return false;
                        }
                    } else {
                        if (!file.delete()) {
                            return false;
                        }
                    }
                }
            }
            return directory.delete();
        }
        return false;
    }

    public static void recreateTempDirectories(String tempFolderPath) throws IOException {

        if (new File(tempFolderPath).exists()) {
            deleteDirectory(new File(tempFolderPath));
        }

        new File(tempFolderPath).mkdirs();
        new File(tempFolderPath + File.separator + "tmpQuests").mkdirs();
        new File(tempFolderPath + File.separator + "tmpQuestsBR").mkdirs();
        new File(tempFolderPath + File.separator + "tmpQuestsNBR").mkdirs();
        new File(tempFolderPath + File.separator + "tmpQuestsReward").mkdirs();
        new File(tempFolderPath + File.separator + "tmpQuestsReg").mkdirs();
        new File(tempFolderPath + File.separator + "tmpQuestsBRProgress").mkdirs();
        new File(tempFolderPath + File.separator + "tmpQuestsNBRProgress").mkdirs();
        new File(tempFolderPath + File.separator + "tmpQuestsRegProgress").mkdirs();

        createDictionaryFile();
    }

    public static void createDictionaryFile() throws IOException {
        File dictionaryFile = new File(System.getProperty("user.dir") + File.separator + "data", "questsDictionary.txt");
        File dataFolder = new File(System.getProperty("user.dir") + File.separator + "data");
        if(!dictionaryFile.exists()) {
            if(!dataFolder.exists()) {
                dataFolder.mkdirs();
            }
            dictionaryFile.createNewFile();
        }
    }

    public static void addQuestToDictionary(String questName) {
        try (FileWriter writer = new FileWriter(System.getProperty("user.dir") + File.separator + "data" + File.separator + "questsDictionary.txt", true);
             BufferedWriter bufferedWriter = new BufferedWriter(writer)) {

            String[] words = questName.split(" ");

            for (int word = 0; word < words.length; word++) {
                if(isNumeric(words[word])) {
                    words[word] = "$";
                }
            }

            String finalQuestName = "";
            for (String word : words) {
                finalQuestName += word + " ";
            }

            finalQuestName = finalQuestName.trim();

            bufferedWriter.newLine();
            bufferedWriter.write(finalQuestName);

            System.out.println("Фраза добавлена в файл.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
