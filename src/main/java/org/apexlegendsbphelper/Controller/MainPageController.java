package org.apexlegendsbphelper.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MainPageController {
    @FXML
    private Button btnRepeatable;

    @FXML
    protected void onMouseClicked() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(new File("D:\\Java\\apex-legends-battle-pass-helper\\src\\main\\java\\org\\apexlegendsbphelper\\View\\add-quests-page.fxml").toURI().toURL());
        Scene scene = new Scene(fxmlLoader.load(), 850, 478);

        Stage window = (Stage) btnRepeatable.getScene().getWindow();
        window.setScene(scene);
        btnRepeatable.setText("Clicked!");
    }
}