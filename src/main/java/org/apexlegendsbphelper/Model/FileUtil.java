package org.apexlegendsbphelper.Model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static org.apexlegendsbphelper.Model.DictionaryUtil.createDictionaryFile;
import static org.apexlegendsbphelper.App.*;
import static org.apexlegendsbphelper.Model.StringUtil.*;

import static java.nio.file.StandardCopyOption.*;

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

    public static void recreateTempDirectories() throws IOException {

        dataFolderPath = System.getProperty("user.dir") + File.separator + "data";
        weekImagesFolderPath = dataFolderPath + File.separator + "weekimages";
        tempFolderPath = System.getProperty("user.dir") + File.separator + "tmp";
        grayscaleImagePath = tempFolderPath + File.separator + "input_grayscale.png";
        blackWhiteImagePath = tempFolderPath + File.separator + "input_blackwhite.png";

        if(new File(tempFolderPath).exists()) {
            deleteDirectory(new File(tempFolderPath));
            new File(tempFolderPath).mkdirs();
        }

        if(!new File(dataFolderPath).exists()) {
            new File(dataFolderPath).mkdirs();
        }

        if(!new File(weekImagesFolderPath).exists()) {
            new File(weekImagesFolderPath).mkdirs();
            for (int i = 1; i <= 12; i++) {
                StringBuilder weekImagesPathBuilder = new StringBuilder();
                weekImagesPathBuilder.
                        append(weekImagesFolderPath).
                        append(File.separator).
                        append("week").
                        append(i);
                new File(weekImagesPathBuilder.toString()).mkdirs();
            }
        }


        if (new File(tempFolderPath).exists()) {
            deleteDirectory(new File(tempFolderPath));
            new File(tempFolderPath).mkdirs();
        }

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

    public static File loadNewFile(String pathToFile) {
        if(pathToFile.isEmpty()) {
            System.out.println("loadNewFile: File not found");
            return null;
        } else {
            return new File(Objects.requireNonNull(pathFixer(pathToFile)));
        }
    }

    public static File copyToWeekImages(String inputPath, int weekNumber, int questNumber) throws IOException {
        if(new File(weekImagesFolderPath).exists()) {
            Path originalPath = Paths.get(inputPath);
            Path copiedPath = Paths.get(weekImagesFolderPath + File.separator + "week" + weekNumber + File.separator + "quest" + questNumber + ".png");
            Files.copy(originalPath, copiedPath, REPLACE_EXISTING);
            return new File(copiedPath.toString());
        } else {
            System.out.println("copyToWeekImages: Week images folder not found");
            return new File("user.dir");
        }
    }
}
