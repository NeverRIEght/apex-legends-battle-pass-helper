package org.apexlegendsbphelper.Model;

import java.io.*;

import static org.apexlegendsbphelper.Model.DictionaryUtil.createDictionaryFile;
import static org.apexlegendsbphelper.App.*;
import static org.apexlegendsbphelper.Model.StringUtil.*;

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

        tempFolderPath = System.getProperty("user.dir") + File.separator + "tmp";
        grayscaleImagePath = tempFolderPath + File.separator + "input_grayscale.png";
        blackWhiteImagePath = tempFolderPath + File.separator + "input_blackwhite.png";

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

    public static File loadNewFile(String pathToFile) {
        return new File(pathFixer(pathToFile));
    }
}
