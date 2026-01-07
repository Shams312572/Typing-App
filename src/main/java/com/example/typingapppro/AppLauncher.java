package com.example.typingapppro;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


    public class    AppLauncher extends Application {

        @Override
        public void start(Stage stage) throws Exception {
            Parent root = FXMLLoader.load(getClass().getResource("LandingPage.fxml"));
            Scene scene = new Scene(root);
             stage.setFullScreen(true);
            stage.setScene(scene);
            stage.sizeToScene();
            stage.show();

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent t) {
                    Platform.exit();
                    System.exit(0);
                }
            });
        }


        public static void main(String[] args) {
            launch(args);
        }

    }

