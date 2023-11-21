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
            return pathFixer(selectedFile.getPath());
        } else {
            return null;
        }
    }

    public static String pathFixer (String inputPath) {
        String outputPath = inputPath;
        outputPath = "file:" + outputPath.replace("\\", "/");
        return outputPath;
    }
}
