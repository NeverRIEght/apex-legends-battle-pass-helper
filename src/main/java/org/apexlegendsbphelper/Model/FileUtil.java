package org.apexlegendsbphelper.Model;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

import static org.apexlegendsbphelper.Model.StringUtil.stringsEqualsProbability;

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

            System.out.println("Added quest to dictionary");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sortDictionary() throws IOException {
        createDictionaryFile();
        try {
            // Читаем все строки из файла
            BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.dir") + File.separator + "data" + File.separator + "questsDictionary.txt"));
            Set<String> uniquePhrases = new HashSet<>();

            String line;
            while ((line = reader.readLine()) != null) {
                uniquePhrases.add(line);
            }
            reader.close();

            // Перезаписываем файл с уникальными строками
            BufferedWriter writer = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + File.separator + "data" + File.separator + "questsDictionary.txt"));
            for (String phrase : uniquePhrases) {
                writer.write(phrase);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String searchInDictionary(String questName) throws IOException {
        createDictionaryFile();
        try (BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.dir") + File.separator + "data" + File.separator + "questsDictionary.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if(stringsEqualsProbability(line, questName) >= 75) {
                    System.out.println("Строка \"" + questName + "\" найдена на картинке");
                    System.out.println("Строка \"" + line + "\" найдена в файле.");
                    System.out.println("Вероятность: " + stringsEqualsProbability(line, questName) + "%");
                    return line;
                }
            }
            System.out.println("Строка \"" + questName + "\" не найдена в файле.");
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
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
