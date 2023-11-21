package org.apexlegendsbphelper.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import net.sourceforge.tess4j.TesseractException;
import org.apexlegendsbphelper.Model.Quest;

import java.io.File;
import java.io.IOException;

import static org.apexlegendsbphelper.Model.FXUtil.openFileChooser;
import static org.apexlegendsbphelper.Model.FXUtil.pathFixerWindows;
import static org.apexlegendsbphelper.Model.ImageUtil.processImage;
import static org.apexlegendsbphelper.Model.ImageUtil.removeQuestsDuplicates;

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
    private Button btnScanImages;
    private String imagePath1 = "";
    private String imagePath2 = "";

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

        String pathToWeekImage = openFileChooser();
        if(pathToWeekImage != null) {
            Image weekImage = new Image(pathToWeekImage);

            switch (buttonId) {
                case "btnAddImage1" -> {
                    imagePath1 = pathToWeekImage;
                    btnAddImage1.setVisible(false);
                    imageViewAddImage1.setImage(weekImage);
                    imageViewAddImage1.setVisible(true);
                    System.out.println(pathFixerWindows(imagePath1));
                }
                case "btnAddImage2" -> {
                    imagePath2 = pathToWeekImage;
                    btnAddImage2.setVisible(false);
                    imageViewAddImage2.setImage(weekImage);
                    imageViewAddImage2.setVisible(true);
                    System.out.println(pathFixerWindows(imagePath2));
                }
            }
        }


        // Quest[] questsFromImage1 = ProcessImage(pathToWeekImage)
        // Quest[] questsFromImage2 = ProcessImage(pathToWeekImage)
        // ...

    }

    @FXML
    protected void btnClickResetImages() {
        btnAddImage1.setVisible(true);
        imageViewAddImage1.setVisible(false);
        btnAddImage2.setVisible(true);
        imageViewAddImage2.setVisible(false);
    }

    @FXML
    protected void btnClickScanImages() throws TesseractException, IOException {
        System.out.println("btnclick");
        imagePath1 = pathFixerWindows(imagePath1);
        imagePath2 = pathFixerWindows(imagePath2);
        System.out.println(imagePath1);
        System.out.println(imagePath2);

        if(!imagePath1.isEmpty() && !imagePath2.isEmpty()) {
            Quest[] quests1 = processImage(imagePath1);
            Quest[] quests2 = processImage(imagePath2);
            Quest[] questsAll = new Quest[16];

            for(int i = 0; i < 8; i++) {
                questsAll[i] = quests1[i];
                questsAll[i + 8] = quests2[i];
            }

            Quest[] uniqueQuests = removeQuestsDuplicates(questsAll);
            for (int i = 0; i < questsAll.length; i++) {
                if(questsAll[i] != null) {
                    System.out.println(questsAll[i].toString());
                }
            }
        }
    }

}
