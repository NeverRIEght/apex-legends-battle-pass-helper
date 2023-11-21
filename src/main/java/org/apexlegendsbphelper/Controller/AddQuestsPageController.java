package org.apexlegendsbphelper.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.File;
import java.io.IOException;

import static org.apexlegendsbphelper.Model.FXUtil.openFileChooser;

public class AddQuestsPageController {
    @FXML
    private Button btnAddImage1;
    @FXML
    private Button btnAddImage2;
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
    @FXML
    protected void btnClickLoadImage(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String buttonId = clickedButton.getId();

        Image weekImage = openFileChooser();

        switch (buttonId) {
            case "btnAddImage1" -> {

            }
            case "btnAddImage2" -> {

            }
            default -> {

            }
        }
    }
}
