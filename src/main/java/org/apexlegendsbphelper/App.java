package org.apexlegendsbphelper;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
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