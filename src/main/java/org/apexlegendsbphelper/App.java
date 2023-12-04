package org.apexlegendsbphelper;

import static org.apexlegendsbphelper.Model.FileUtil.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

import java.io.IOException;

public class App extends Application {
    public static String dataFolderPath;
    public static String weekImagesFolderPath;
    public static String tempFolderPath;
    public static String blackWhiteImagePath;
    public static String grayscaleImagePath;
    public static int imageTreshold = 175;
    public static int questsTresholdHigh = 150;
    public static int questsTresholdLow = 120;
    @Override
    public void start(Stage stage) throws IOException {
        recreateTempDirectories();
        String firstScenePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main"
                + File.separator + "java" + File.separator + "org" + File.separator + "apexlegendsbphelper"
                + File.separator + "View" + File.separator + "add-quests-page.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader(new File(firstScenePath).toURI().toURL());
        Scene scene = new Scene(fxmlLoader.load(), 850, 478);


        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}