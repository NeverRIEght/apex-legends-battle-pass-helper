package org.apexlegendsbphelper;


import org.apache.commons.text.similarity.LevenshteinDistance;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TextUtil {
    public static String correctSentence(String inputSentence, String dictionaryPath) throws IOException {
        inputSentence = inputSentence.replaceAll(" ", ""); // Удаляем пробелы

        List<String> dictionary = loadDictionary(dictionaryPath);

        if (dictionary.isEmpty()) {
            return inputSentence;
        }

        int minDistance = Integer.MAX_VALUE;
        String correctedSentence = inputSentence;

        for (String dictionarySentence : dictionary) {
            String cleanedDictionarySentence = dictionarySentence.replaceAll(" ", "");

            int distance = LevenshteinDistance.getDefaultInstance().apply(inputSentence, cleanedDictionarySentence);

            if (distance < minDistance) {
                minDistance = distance;
                correctedSentence = dictionarySentence;
            }
        }

        return correctedSentence;
    }

    public static List<String> loadDictionary(String dictionaryPath) throws IOException {
        List<String> dictionary = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(dictionaryPath));

        String line;
        while ((line = br.readLine()) != null) {
            dictionary.add(line);
        }

        return dictionary;
    }
}










