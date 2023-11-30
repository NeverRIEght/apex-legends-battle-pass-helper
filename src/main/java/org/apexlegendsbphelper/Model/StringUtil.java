package org.apexlegendsbphelper.Model;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class StringUtil {
    public static String pathFixerLinux (String inputPath) {
        String outputPath = inputPath;
        if(!outputPath.contains("file:")) {
            outputPath = "file:" + outputPath;
        }
        outputPath = outputPath.replace("\\", "/");
        return outputPath;
    }

    public static String pathFixerWindows (String inputPath) {
        String outputPath = inputPath;
        if(outputPath.contains("file:")) {
            outputPath = outputPath.substring(5);
        }
        outputPath = outputPath.replace("/", "\\");
        return outputPath;
    }

    public static String pathFixer (String inputPath) {
        File outputFile;
        String pathToFile;
        try {
            outputFile = new File(inputPath);
            pathToFile = outputFile.getPath();
        } catch (Exception e) {
            try {
                outputFile = new File(pathFixerWindows(inputPath));
                pathToFile = outputFile.getPath();
            } catch (Exception e1) {
                try {
                    outputFile = new File(pathFixerLinux(inputPath));
                    pathToFile = outputFile.getPath();
                } catch (Exception e2) {
                    return null;
                }
            }
        }
        return pathToFile;
    }

    public static double stringsEqualsProbability(String string1, String string2) {
        double probability = 0;
        if(string1 == null || string2 == null) {
            return probability;
        }

        String[] string1Splitted = string1.split(" ");
        String[] string2Splitted = string2.split(" ");

        Set<String> set1 = new HashSet<>(Arrays.asList(string1Splitted));
        Set<String> set2 = new HashSet<>(Arrays.asList(string2Splitted));

        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        probability = (double) intersection.size() / Math.min(set1.size(), set2.size()) * 100;
        return probability;
    }

    public static int getNumberPosition(String inputString) {
        int numberPosition = -1;

        if(inputString == null) {
            return numberPosition;
        }

        String tempString = inputString;
        tempString = tempString.trim();
        String[] tempStringSplitted = tempString.split(" ");

        for(int i = 0; i < tempStringSplitted.length; i++) {
            String tempWord = tempStringSplitted[i];
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

    public static boolean checkForNumber(String inputString) {
        return getNumberPosition(inputString) != -1 && inputString != null;
    }

    public static String replaceNumberWithDollar(String inputString) {
        if(inputString == null) {
            return null;
        }

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

    public static String replaceOWithZero (String inputString) {
        //if a symbol "O" has a number before it, replace it with "0"

        if(inputString == null) {
            return null;
        }

        String outputString = inputString;
        outputString = outputString.trim();
        String[] outputStringSplitted = outputString.split(" ");

        String charO = String.valueOf((char) 79);
        for(int i = 0; i < outputStringSplitted.length; i++) {
            if(i > 0 && outputStringSplitted[i].contains(charO) && checkForNumber(outputStringSplitted[i - 1])) {
                String tempWord = outputStringSplitted[i];
                tempWord = tempWord.replace(charO, "0");
                outputStringSplitted[i] = tempWord;
            }
        }

        outputString = "";
        for(String word : outputStringSplitted) {
            outputString += word + " ";
        }
        outputString = outputString.trim();

        return outputString;
    }

    public static String removeCommaFromNumber (String inputString) {
        if(inputString == null) {
            return null;
        }

        String outputString = inputString;
        outputString = outputString.trim();

        if(checkForNumber(outputString)) {
            int numberPosition = getNumberPosition(outputString);
            String[] outputStringSplitted = outputString.split(" ");
            outputStringSplitted[numberPosition] = outputStringSplitted[numberPosition].replace(",", "");
            outputString = "";
            for(String word : outputStringSplitted) {
                outputString += word + " ";
            }
            outputString = outputString.trim();
        }

        return outputString;
    }
}
