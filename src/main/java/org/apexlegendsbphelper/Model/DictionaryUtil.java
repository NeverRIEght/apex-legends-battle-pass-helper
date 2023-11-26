package org.apexlegendsbphelper.Model;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

import static org.apexlegendsbphelper.Model.StringUtil.*;

public abstract class DictionaryUtil {

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
                if(checkForNumber(words[word])) {
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

    public static String searchDictionary(String questName) throws IOException {
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
}
