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
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 */
public class TutorialController implements Initializable {

    @FXML private ChoiceBox<String> lessonChoiceBox;
    @FXML private CheckBox soundCheckBox;
    @FXML private Label ETLabel;
    @FXML private Label displayArea;
    @FXML private TextArea textInputArea;
    @FXML private JFXButton timerButton;
    @FXML private JFXButton keybefore1, key1, key2, key3, key4, key5, key6, key7, key8, key9, key0, keyminus, keyplus, backspace;
    @FXML private JFXButton tab, q, w, e, r, t, y, u, i, o, p, keyBoxBracketL, keyBoxBracketR, keyBackslash;
    @FXML private JFXButton capslock, a, s, d, f, g, h, j, k, l, keySemicolon, keyAphostrophe, enter;
    @FXML private JFXButton shiftl, z, x, c, v, b, n, m, keyComma, keyDot, keyForwardslash, shiftr;
    @FXML private JFXButton ctrll, altl, space, altr, ctrlr, HomeButton, goButton, goToResultBtn;

    private static AudioClip ALERT_AUDIOCLIP;
    private static AudioClip TYPING_AUDIOCLIP;

    private int errorCountWithoutBackspace, errorCountWithBackspace, totalChar, indexOfLine = 0, wordCount = 0;
    private char expectedKey, typedKey;
    private String timeToComplete, line;
    private int[] problemKeyArray = new int[26];
    private char[] problemCharArray = {'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n', 'm'};

    Timeline timeline;
    int mins = 0, secs = 0, millis = 0;
    boolean sos = true, timerStarted = false;

    /**
     * Updated: Swaps Root for smooth full-screen transition.
     */
    @FXML
    void goHome(ActionEvent event) {
        try {
            Stage theStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("LandingPage.fxml"));

            // FIX: Swap Root instead of creating new Scene
            theStage.getScene().setRoot(root);
            theStage.setFullScreen(true);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void onPressGo(ActionEvent event) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File("files\\" + lessonChoiceBox.getSelectionModel().getSelectedItem().toString() + ".txt")));
            errorCountWithBackspace = 0; errorCountWithoutBackspace = 0; totalChar = 0; indexOfLine = 0;
            displayArea.setText("");
            textInputArea.setText("");
            textInputArea.requestFocus();

            TutorialController.ALERT_AUDIOCLIP.setRate(2.0);
            TutorialController.TYPING_AUDIOCLIP.setVolume(1.0);
            for (int i = 0; i < 26; i++) { problemKeyArray[i] = 0; }

            textInputArea.setOnKeyTyped(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    textInputArea.setId("normal");
                    expectedKey = line.charAt(indexOfLine);
                    typedKey = event.getCharacter().charAt(0);

                    if (!timerStarted) {
                        timerStarted = true;
                        timerButton.fire();
                    }

                    if (indexOfLine == line.length() - 1) {
                        if (typedKey != expectedKey) {
                            if (soundCheckBox.isSelected()) TutorialController.ALERT_AUDIOCLIP.play();
                            textInputArea.setId("warn");
                            incrementProblemKeyCount(Character.toString(expectedKey));
                            errorCountWithBackspace++;
                            errorCountWithoutBackspace++;
                        } else {
                            if (soundCheckBox.isSelected()) TutorialController.TYPING_AUDIOCLIP.play();
                        }
                        try {
                            if ((line = reader.readLine()) != null) {
                                displayArea.setText(line);
                                totalChar += line.length();
                                wordCount += countSpaces(line);
                                textInputArea.setText("");
                            } else {
                                timerButton.fire();
                                timeToComplete = ETLabel.getText();
                                reader.close();
                                goToResultBtn.fire();
                            }
                            indexOfLine = 0;
                        } catch (IOException ex) { ex.printStackTrace(); }
                    } else if (!(event.getCharacter().equals("\u0008"))) {
                        if (typedKey != expectedKey) {
                            if (soundCheckBox.isSelected()) TutorialController.ALERT_AUDIOCLIP.play();
                            textInputArea.setId("warn");
                            incrementProblemKeyCount(Character.toString(expectedKey));
                            errorCountWithBackspace++;
                            errorCountWithoutBackspace++;
                        } else {
                            if (soundCheckBox.isSelected()) TutorialController.TYPING_AUDIOCLIP.play();
                        }
                        indexOfLine++;
                    } else if (event.getCharacter().equals("\u0008") && textInputArea.getText() != null) {
                        indexOfLine--;
                        if (errorCountWithBackspace > 0) errorCountWithBackspace--;
                    }
                    showKeyPressed(event.getCharacter());
                }
            });

            if ((line = reader.readLine()) != null) {
                displayArea.setText(line);
                totalChar += line.length();
                wordCount += countSpaces(line);
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    public void showKeyPressed(String key) {
        if (Character.isLetter(key.charAt(0))) key = key.toLowerCase();
        switch (key) {
            case "q": q.arm(); q.disarm(); break;
            case "w": w.arm(); w.disarm(); break;
            case "e": e.arm(); e.disarm(); break;
            case "r": r.arm(); r.disarm(); break;
            case "t": t.arm(); t.disarm(); break;
            case "y": y.arm(); y.disarm(); break;
            case "u": u.arm(); u.disarm(); break;
            case "i": i.arm(); i.disarm(); break;
            case "o": o.arm(); o.disarm(); break;
            case "p": p.arm(); p.disarm(); break;
            case "a": a.arm(); a.disarm(); break;
            case "s": s.arm(); s.disarm(); break;
            case "d": d.arm(); d.disarm(); break;
            case "f": f.arm(); f.disarm(); break;
            case "g": g.arm(); g.disarm(); break;
            case "h": h.arm(); h.disarm(); break;
            case "j": j.arm(); j.disarm(); break;
            case "k": k.arm(); k.disarm(); break;
            case "l": l.arm(); l.disarm(); break;
            case "z": z.arm(); z.disarm(); break;
            case "x": x.arm(); x.disarm(); break;
            case "c": c.arm(); c.disarm(); break;
            case "v": v.arm(); v.disarm(); break;
            case "b": b.arm(); b.disarm(); break;
            case "n": n.arm(); n.disarm(); break;
            case "m": m.arm(); m.disarm(); break;
            case "1": case "!": key1.arm(); key1.disarm(); break;
            case "2": case "@": key2.arm(); key2.disarm(); break;
            case "3": case "#": key3.arm(); key3.disarm(); break;
            case "4": case "$": key4.arm(); key4.disarm(); break;
            case "5": case "%": key5.arm(); key5.disarm(); break;
            case "6": case "^": key6.arm(); key6.disarm(); break;
            case "7": case "&": key7.arm(); key7.disarm(); break;
            case "8": case "*": key8.arm(); key8.disarm(); break;
            case "9": case "(": key9.arm(); key9.disarm(); break;
            case "0": case ")": key0.arm(); key0.disarm(); break;
            case "-": case "_": keyminus.arm(); keyminus.disarm(); break;
            case "+": case "=": keyplus.arm(); keyplus.disarm(); break;
            case "{": case "[": keyBoxBracketL.arm(); keyBoxBracketL.disarm(); break;
            case "}": case "]": keyBoxBracketR.arm(); keyBoxBracketR.disarm(); break;
            case "|": case "\\": keyBackslash.arm(); keyBackslash.disarm(); break;
            case ";": case ":": keySemicolon.arm(); keySemicolon.disarm(); break;
            case "\"": case "'": keyAphostrophe.arm(); keyAphostrophe.disarm(); break;
            case ",": case "<": keyComma.arm(); keyComma.disarm(); break;
            case ">": case ".": keyDot.arm(); keyDot.disarm(); break;
            case "?": case "/": keyForwardslash.arm(); keyForwardslash.disarm(); break;
            case " ": space.arm(); space.disarm(); break;
            case "\u0008": backspace.arm(); backspace.disarm(); break;
        }
    }

    void incrementProblemKeyCount(String key) {
        if (Character.isLetter(key.charAt(0))) key = key.toLowerCase();
        int idx = key.charAt(0) - 'a';
        if (idx >= 0 && idx < 26) problemKeyArray[idx]++;
    }

    String generateProblemKeyString() {
        String string = "-";
        int first = 0, second = 0, third = 0;
        int firstIdx = -1, secondIdx = -1, thirdIdx = -1;
        for (int i = 0; i < 26; i++) {
            if (problemKeyArray[i] > first) {
                third = second; thirdIdx = secondIdx;
                second = first; secondIdx = firstIdx;
                first = problemKeyArray[i]; firstIdx = i;
            } else if (problemKeyArray[i] > second) {
                third = second; thirdIdx = secondIdx;
                second = problemKeyArray[i]; secondIdx = i;
            } else if (problemKeyArray[i] > third) {
                third = problemKeyArray[i]; thirdIdx = i;
            }
        }
        string = "";
        if (firstIdx != -1 && problemKeyArray[firstIdx] != 0) string += problemCharArray[firstIdx] + " ";
        if (secondIdx != -1 && problemKeyArray[secondIdx] != 0) string += problemCharArray[secondIdx] + " ";
        if (thirdIdx != -1 && problemKeyArray[thirdIdx] != 0) string += problemCharArray[thirdIdx];
        return string.isEmpty() ? "-" : string.toUpperCase();
    }

    int countSpaces(String myLine) {
        int counter = 0;
        for (int i = 0; i < myLine.length(); i++) { if (myLine.charAt(i) == ' ') counter++; }
        return counter;
    }

    void change() {
        if (millis == 1000) { secs++; millis = 0; }
        if (secs == 60) { mins++; secs = 0; }
        ETLabel.setText((((mins / 10) == 0) ? "0" : "") + mins + ":" + (((secs / 10) == 0) ? "0" : "") + secs);
        millis++;
    }

    /**
     * Updated: Swaps Root and passes data for smooth full-screen transition.
     */
    @FXML
    void switchSceneToResultPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LessonResult.fxml"));
            Parent root = loader.load();

            LessonResultController controller = loader.getController();
            String problemKeyString = generateProblemKeyString();
            int currentLessonChoice = lessonChoiceBox.getSelectionModel().getSelectedIndex();

            controller.initializeMyData(totalChar, errorCountWithBackspace, errorCountWithoutBackspace, timeToComplete, wordCount, problemKeyString, currentLessonChoice);

            Stage theStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // FIX: Swap Root instead of creating new Scene
            theStage.getScene().setRoot(root);
            theStage.setFullScreen(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void initializeLessonChoiceAndBegin(int choice) {
        if (choice <= 16 && 0 <= choice) lessonChoiceBox.getSelectionModel().select(choice);
        else if (choice == 17) lessonChoiceBox.getSelectionModel().select(16);
        goButton.fire();
    }

    public void initialize(URL url, ResourceBundle rb) {
        URL alertUrl = getClass().getResource("/alert.mp3");
        URL typingUrl = getClass().getResource("/typing.wav");

        if (alertUrl != null && typingUrl != null) {
            ALERT_AUDIOCLIP = new AudioClip(alertUrl.toExternalForm());
            TYPING_AUDIOCLIP = new AudioClip(typingUrl.toExternalForm());
        }

        lessonChoiceBox.getItems().addAll("Demo", "Lesson 1", "Lesson 2", "Lesson 3", "Lesson 4", "Lesson 5", "Lesson 6", "Lesson 7", "Lesson 8", "Lesson 9", "Lesson 10", "Lesson 11", "Lesson 12", "Lesson 13", "Lesson 14", "Lesson 15", "Common Words");
        lessonChoiceBox.getSelectionModel().select("Demo");
        ETLabel.setText("00:00");

        timeline = new Timeline(new KeyFrame(Duration.millis(1), e -> change()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(false);

        timerButton.setOnAction(e -> {
            if (sos) { timeline.play(); sos = false; timerButton.setText("Stop"); }
            else { timeline.pause(); sos = true; timerButton.setText("Start"); }
        });
    }
}