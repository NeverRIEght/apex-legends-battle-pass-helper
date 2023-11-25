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

    public static boolean isStringContainsNumber(String string) {
        if(string == null) {
            return false;
        }


        for(int i = 0; i < string.length(); i++) {
            if(Character.isDigit(string.charAt(i))) {
                return true;
            }
        }

        return false;
    }
}
