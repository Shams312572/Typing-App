package com.example.typingapppro;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class LandingPageController implements Initializable {

    @FXML private StackPane LPbackground;
    @FXML private HBox subpane;
    @FXML private JFXButton learnTypingButton;
    @FXML private JFXButton quickTestButton;

    /**
     * Upgraded: Switches to Tutorial using setRoot for smooth full-screen transition.
     */
    @FXML
    void goToLessons(ActionEvent event) {
        try {
            Stage theStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("Tutorial.fxml"));

            // FIX: Use setRoot to prevent the full-screen "blink" or resize
            theStage.getScene().setRoot(root);
            theStage.setFullScreen(true);

        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Upgraded: Switches to QuickTest using setRoot for smooth full-screen transition.
     */
    @FXML
    void goToTest(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("QuickTest.fxml"));
            Parent root = loader.load();

            Stage theStage = (Stage) ((Node)event.getSource()).getScene().getWindow();

            // FIX: Use setRoot to keep the stage in its current state
            theStage.getScene().setRoot(root);
            theStage.setFullScreen(true);

            // Access controller after root is set
            QuickTestController controller = loader.getController();
            controller.loadTest();

        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialization logic if needed
    }}