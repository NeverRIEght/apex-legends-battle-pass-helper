package org.apexlegendsbphelper.Model;

import javafx.stage.FileChooser;

import java.io.File;

public abstract class FXUtil {
    public String pathToWeekImages = "";
    public static String openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a week image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png"));
        //fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        File selectedFile = fileChooser.showOpenDialog(null);


        if (selectedFile != null) {
            return pathFixerLinux(selectedFile.getPath());
        } else {
            return null;
        }
    }

    public static String pathFixerLinux (String inputPath) {
        String outputPath = inputPath;
        if(!outputPath.contains("file:")) {
            outputPath = "file:" + outputPath;
        }
        outputPath.replace("\\", "/");
        return outputPath;
    }

    public static String pathFixerWindows (String inputPath) {
        String outputPath = inputPath;
        if(outputPath.contains("file:")) {
            outputPath = outputPath.substring(5);
        }
        outputPath.replace("/", "\\");
        return outputPath;
    }
}
