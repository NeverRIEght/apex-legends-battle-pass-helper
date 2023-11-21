package org.apexlegendsbphelper.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.apexlegendsbphelper.Model.FXUtil.openFileChooser;

public class AddQuestsPageController {
    @FXML
    private Button btnRepeatable;
    @FXML
    private Button btnAddImage1;
    @FXML
    private Button btnAddImage2;
    @FXML
    private ImageView imageViewAddImage1;
    @FXML
    private ImageView imageViewAddImage2;
    @FXML
    private Button btnResetImages;

    @FXML
    protected void onMouseClicked() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(new File("D:\\Java\\apex-legends-battle-pass-helper\\src\\main\\java\\org\\apexlegendsbphelper\\View\\add-quests-page.fxml").toURI().toURL());
        Scene scene = new Scene(fxmlLoader.load(), 850, 478);

        Stage window = (Stage) btnRepeatable.getScene().getWindow();
        window.setScene(scene);
        btnRepeatable.setText("Clicked!");
    }
    @FXML
    protected void btnClickLoadImage(ActionEvent event) throws MalformedURLException {
        Button clickedButton = (Button) event.getSource();
        String buttonId = clickedButton.getId();

        Image weekImage = openFileChooser();
        if(weekImage != null) {
            switch (buttonId) {
                case "btnAddImage1" -> {
                    btnAddImage1.setVisible(false);
                    imageViewAddImage1.setImage(weekImage);
                    imageViewAddImage1.setVisible(true);
                }
                case "btnAddImage2" -> {
                    btnAddImage2.setVisible(false);
                    imageViewAddImage2.setImage(weekImage);
                    imageViewAddImage2.setVisible(true);
                }
            }
        }
    }

    @FXML
    protected void btnClickResetImages(ActionEvent event) {
        btnAddImage1.setVisible(true);
        imageViewAddImage1.setVisible(false);
        btnAddImage2.setVisible(true);
        imageViewAddImage2.setVisible(false);
    }
}
