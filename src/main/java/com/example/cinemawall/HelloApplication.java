package com.example.cinemawall;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Splash Screen Setup
        StackPane splashRoot = new StackPane();
        splashRoot.setStyle("-fx-background-color: linear-gradient(to bottom, #A3B8C9, #D4E3F1, #F1F9FF);");
        // Load Logo (corrected path)
        Image logoImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/cine2.png"))); // Corrected path
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(300); // Adjust size as needed
        logoView.setPreserveRatio(true);
        splashRoot.getChildren().add(logoView);

        // Create Splash Scene
        Scene splashScene = new Scene(splashRoot, 600, 400); // Adjust size to your liking
        Stage splashStage = new Stage();
        splashStage.initStyle(StageStyle.UNDECORATED); // No window decorations
        splashStage.setScene(splashScene);

        // Fade-In Animation
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(3), splashRoot);
        fadeIn.setFromValue(0); // Start fully transparent
        fadeIn.setToValue(1);   // End fully visible (corrected to 1.0)
        fadeIn.setOnFinished(e -> {
            // Close Splash Screen and Open Main App
            splashStage.close();
            Stage stage = new Stage();
            try {
                // Load the FXML (corrected path)
                Parent root = FXMLLoader.load(getClass().getResource("/com/example/cinemawall/hello-view.fxml"));
                Scene scene = new Scene(root); // Adjust size for the main window

                // Load App Icon (corrected path)
                Image icon = new Image(Objects.requireNonNull(HelloApplication.class.getResourceAsStream("/cine.png"))); // Corrected path

                stage.setTitle("CINEWALL");
                stage.getIcons().add(icon); // Add app icon
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        // Show Splash Screen and Play Animation
        splashStage.show();
        fadeIn.play();
    }

    public static void main(String[] args) {
        launch();
    }
}
