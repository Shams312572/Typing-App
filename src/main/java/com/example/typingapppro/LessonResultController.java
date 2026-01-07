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
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class for Lesson Results
 *
 * @author Anay
 */
public class LessonResultController implements Initializable {

    @FXML private JFXButton goBackButton;
    @FXML private JFXButton redoButton;
    @FXML private JFXButton nextButtonn;
    @FXML private Label speedWPM, speedKPM, trueAccuracy, timeSpent, troubleKeys, accuracy;

    private int currentLessonChoice;

    /**
     * Upgraded: Switches back to Landing Page smoothly.
     */
    @FXML
    void goHome(ActionEvent event) {
        try {
            Stage theStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("LandingPage.fxml"));

            // FIX: Swap Root instead of creating a new Scene
            theStage.getScene().setRoot(root);
            theStage.setFullScreen(true);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Upgraded: Switches to Tutorial for next lesson smoothly.
     */
    @FXML
    void nextLesson(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Tutorial.fxml"));
            Parent root = loader.load();

            Stage theStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // FIX: Swap Root to stay in Full Screen
            theStage.getScene().setRoot(root);
            theStage.setFullScreen(true);

            TutorialController controller = loader.getController();
            controller.initializeLessonChoiceAndBegin(++currentLessonChoice);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Upgraded: Restarts the current lesson smoothly.
     */
    @FXML
    void redoLesson(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Tutorial.fxml"));
            Parent root = loader.load();

            Stage theStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // FIX: Swap Root to stay in Full Screen
            theStage.getScene().setRoot(root);
            theStage.setFullScreen(true);

            TutorialController controller = loader.getController();
            controller.initializeLessonChoiceAndBegin(currentLessonChoice);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void initializeMyData(int totalChar, int errorCountWithBackspace, int errorCountWithoutBackspace, String timeToComplete, int wordCount, String troubleKeyString, int currentLessonChoice) {
        try {
            Double timeInMin = (Double.parseDouble(timeToComplete.substring(0, 2)) + (Double.parseDouble(timeToComplete.substring(3, 5)) / 60.0));
            double tacc = (double) (100 - (errorCountWithoutBackspace * 100) / totalChar);
            double acc = (double) (100 - (errorCountWithBackspace * 100) / totalChar);

            speedKPM.setText(String.format("%.0f", (totalChar / timeInMin)));
            speedWPM.setText(String.format("%.0f", (wordCount / timeInMin)));
            trueAccuracy.setText(String.format("%.1f", tacc));
            accuracy.setText(String.format("%.1f", acc) + "%");
            timeSpent.setText(timeToComplete);
            troubleKeys.setText(troubleKeyString);

            this.currentLessonChoice = currentLessonChoice;
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialization logic
    }
}