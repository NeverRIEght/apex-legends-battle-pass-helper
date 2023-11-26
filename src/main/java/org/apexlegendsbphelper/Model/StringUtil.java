package org.apexlegendsbphelper.Model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class StringUtil {
    public static double stringsEqualsProbability(String string1, String string2) {
        double probability = 0;
        if(string1 == null || string2 == null) {
            return probability;
        }

        // Разбиваем строки на слова
        String[] string1Splitted = string1.split(" ");
        String[] string2Splitted = string2.split(" ");

        // Создаем множества для уникальных слов в каждой строке
        Set<String> set1 = new HashSet<>(Arrays.asList(string1Splitted));
        Set<String> set2 = new HashSet<>(Arrays.asList(string2Splitted));

        // Находим пересечение множеств
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        // Вычисляем вероятность на основе количества общих слов
        probability = (double) intersection.size() / Math.min(set1.size(), set2.size()) * 100;

        return probability;
    }

    public static int getNumberPosition(String inputString) {
        int numberPosition = -1;
        inputString = inputString.trim();
        String[] inputStringSplitted = inputString.split(" ");

        for(int i = 0; i < inputStringSplitted.length; i++) {
            String tempWord = inputStringSplitted[i];
            tempWord = tempWord.replace(",", "");
            tempWord = tempWord.replace(".", "");
            tempWord = tempWord.replace("o", "0");
            tempWord = tempWord.replace("O", "0");
            try {
                Integer.parseInt(tempWord);
            } catch (NumberFormatException e) {
                continue;
            }
            numberPosition = i;
            break;
        }

        return numberPosition;
    }

    public static boolean ifHasNumber(String unputString) {
        if(getNumberPosition(unputString) == -1) {
            return false;
        } else {
            return true;
        }
    }

    public static String replaceNumberWithDollar(String inputString) {
        String outputString = inputString;
        outputString = outputString.trim();

        int numberPosition = getNumberPosition(outputString);
        if(numberPosition != -1) {
            String[] outputStringSplitted = outputString.split(" ");
            outputStringSplitted[numberPosition] = "$";

            outputString = "";
            for(String word : outputStringSplitted) {
                outputString += word + " ";
            }
            outputString = outputString.trim();
            return outputString;
        } else {
            return null;
        }
    }
}
