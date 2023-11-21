package org.apexlegendsbphelper.Model;

import javafx.scene.image.*;
import javafx.stage.FileChooser;

import java.awt.image.BufferedImage;
import java.io.File;

public abstract class FXUtil {
    public String pathToWeekImages = "";
    public static Image openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a week image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Изображения", "*.png"));
        //fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        File selectedFile = fileChooser.showOpenDialog(null);


        if (selectedFile != null) {
            String pathToFile = pathFixer(selectedFile.getPath());
            return new Image(pathToFile);
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
