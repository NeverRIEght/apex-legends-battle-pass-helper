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
            String pathToFile = selectedFile.getPath();
            return new Image(pathToFile);
        } else {
            return null;
        }
    }
//    public static Image loadImageFromFile(File file) {
//        try {
//            BufferedImage bufferedImage = ImageIO.read(file);
//            WritableImage writableImage = new WritableImage(bufferedImage.getWidth(), bufferedImage.getHeight());
//            SwingFXUtils.toFXImage(bufferedImage, writableImage);
//            return writableImage;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
}
