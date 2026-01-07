package com.example.typingapppro;

import com.jfoenix.controls.JFXButton;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class for Quick Test
 *
 * @author Anay
 */
public class QuickTestController implements Initializable {

    @FXML private JFXButton abortButton;
    @FXML private Label timeLabel;
    @FXML private ImageView refreshButton;
    @FXML private TextArea textDisplay;
    @FXML private JFXButton resultButton;
    @FXML private JFXButton timerButton;

    private static final AudioClip ALERT_AUDIOCLIP = new AudioClip(TutorialController.class.getResource("/alert.mp3").toString());
    private static final AudioClip TYPING_AUDIOCLIP = new AudioClip(TutorialController.class.getResource("/typing.wav").toString());

    private int errorCount;
    private int totalChar;
    private char expectedKey;
    private char typedKey;
    int indexOfLine = 0;
    String arr = "";
    int wordCount = 0;

    Timeline timeline;
    int mins = 0, secs = 0, millis = 0;
    boolean sos = true;
    boolean timerStarted = false;

    /**
     * Upgraded: Returns to Main Page without resizing the window.
     */
    @FXML
    void goToMainPage(ActionEvent event) {
        try {
            Stage theStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("LandingPage.fxml"));

            // FIX: Use setRoot to stay in Full Screen
            theStage.getScene().setRoot(root);
            theStage.setFullScreen(true);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Upgraded: Transitions to Result Page smoothly.
     */
    @FXML
    void switchSceneToResult(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Result.fxml"));
            Parent root = loader.load();

            ResultController controller = loader.getController();
            controller.initializeMyData(++wordCount, errorCount, totalChar);

            Stage theStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // FIX: Use setRoot to prevent flicker
            theStage.getScene().setRoot(root);
            theStage.setFullScreen(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Upgraded: Restarts the test by reloading the root.
     */
    @FXML
    void restartTest(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("QuickTest.fxml"));
            Parent root = loader.load();

            // Get stage from the ImageView (refreshButton)
            Stage theStage = (Stage) refreshButton.getScene().getWindow();

            // FIX: Use setRoot
            theStage.getScene().setRoot(root);
            theStage.setFullScreen(true);

            QuickTestController controller = loader.getController();
            controller.loadTest();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void loadTest() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File("files\\Test.txt")));
            String line;
            textDisplay.setText("");
            arr = "";
            while ((line = reader.readLine()) != null) {
                textDisplay.setText(textDisplay.getText() + line);
                arr += line;
            }
            reader.close();
            textDisplay.requestFocus();
            errorCount = 0; indexOfLine = 0; wordCount = 0; totalChar = arr.length();

            textDisplay.setStyle("-fx-highlight-fill: #bbdefb; -fx-highlight-text-fill: #2196f3;");
            textDisplay.selectRange(indexOfLine, indexOfLine + 1);

            textDisplay.setOnKeyTyped(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if (!timerStarted) {
                        timerStarted = true;
                        timerButton.fire();
                    }

                    if (indexOfLine < arr.length()) {
                        expectedKey = arr.charAt(indexOfLine);
                        typedKey = event.getCharacter().charAt(0);

                        if (typedKey != expectedKey) {
                            errorCount++;
                            indexOfLine++;
                            textDisplay.setStyle("-fx-background-color: #ffcdd2;-fx-highlight-fill: #bbdefb; -fx-highlight-text-fill: #2196f3;");
                        } else {
                            if (typedKey == ' ') wordCount++;
                            indexOfLine++;
                            textDisplay.setStyle("-fx-background-color: white;-fx-highlight-fill: #bbdefb; -fx-highlight-text-fill: #2196f3;");
                        }
                        textDisplay.selectRange(indexOfLine, indexOfLine + 1);
                    }

                    if (indexOfLine == arr.length()) {
                        timerButton.fire();
                        resultButton.fire();
                    }
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    void change() {
        if (millis == 1000) { secs++; millis = 0; }
        if (secs == 60) { mins++; secs = 0; }
        timeLabel.setText((((mins / 10) == 0) ? "0" : "") + mins + ":" + (((secs / 10) == 0) ? "0" : "") + secs);
        millis++;
        if (timeLabel.getText().equals("01:00")) {
            timerButton.fire();
            resultButton.fire();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        timeLabel.setText("00:00");
        timeline = new Timeline(new KeyFrame(Duration.millis(1), e -> change()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(false);

        timerButton.setOnAction(e -> {
            if (sos) {
                timeline.play();
                sos = false;
                timerButton.setText("Stop");
            } else {
                timeline.pause();
                sos = true;
                timerButton.setText("Start");
            }
        });
    }
}