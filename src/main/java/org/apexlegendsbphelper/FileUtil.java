package org.apexlegendsbphelper;

import java.io.File;

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

    public static void recreateDirectories(String tempFolderPath) {

        if (new File(tempFolderPath).exists()) {
            deleteDirectory(new File(tempFolderPath));
            new File(tempFolderPath).mkdirs();
            new File(tempFolderPath + File.separator + "tmpOR").mkdirs();
            new File(tempFolderPath + File.separator + "tmpBR").mkdirs();
            new File(tempFolderPath + File.separator + "tmpNBR").mkdirs();
            new File(tempFolderPath + File.separator + "tmpName").mkdirs();
            new File(tempFolderPath + File.separator + "tmpPr").mkdirs();
            new File(tempFolderPath + File.separator + "tmpRe").mkdirs();
        } else {
            new File(tempFolderPath).mkdirs();
            new File(tempFolderPath + File.separator + "tmpOR").mkdirs();
            new File(tempFolderPath + File.separator + "tmpBR").mkdirs();
            new File(tempFolderPath + File.separator + "tmpNBR").mkdirs();
            new File(tempFolderPath + File.separator + "tmpName").mkdirs();
            new File(tempFolderPath + File.separator + "tmpPr").mkdirs();
            new File(tempFolderPath + File.separator + "tmpRe").mkdirs();
        }
    }
}
