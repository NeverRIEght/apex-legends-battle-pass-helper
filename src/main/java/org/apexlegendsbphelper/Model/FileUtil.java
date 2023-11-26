package org.apexlegendsbphelper.Model;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

import static org.apexlegendsbphelper.Model.DictionaryUtil.createDictionaryFile;
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
}
