package org.apexlegendsbphelper.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainPageController {
    @FXML
    private Button btnRepeatable;

    @FXML
    protected void onMouseClicked() {
        btnRepeatable.setText("Clicked!");
    }
}