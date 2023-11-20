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
        FXMLLoader fxmlLoader = new FXMLLoader(new File("D:\\Java\\apex-legends-battle-pass-helper\\src\\main\\java\\org\\apexlegendsbphelper\\View\\main-page.fxml").toURI().toURL());
        Scene scene = new Scene(fxmlLoader.load(), 850, 478);


//        Group root = new Group();
//        Scene scene = new Scene(root, 850, 478);
//        stage.setTitle("Hello!");
//
//
//        File file = new File("D:\\Java\\apex-legends-battle-pass-helper\\src\\main\\java\\org\\apexlegendsbphelper\\ignite_poster.jpg");
//        Image image = new Image(file.toURI().toURL().toString());
//        ImageView imageView = new ImageView(image);
//        imageView.setX(0);
//        imageView.setY(0);
//        imageView.fitWidthProperty().bind(scene.widthProperty());
//        imageView.fitHeightProperty().bind(scene.heightProperty());
//
//
//        root.getChildren().add(imageView);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}