

package com.example.libraryfinal;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainApplication extends Application {
    @Override
    public void start(Stage primaryStage) {
//        // Create the intro screen
//        StackPane introRoot = new StackPane();
//        Label introLabel = new Label("Welcome to the Library Management System");
//
//        introLabel.setStyle("-fx-text-fill: #4CAF50; -fx-font-size: 24px; -fx-font-weight: bold;");
//
//        introRoot.getChildren().add(introLabel);
//        Scene introScene = new Scene(introRoot, 800, 600);
//
//        // Set the intro scene
//        primaryStage.setScene(introScene);
//        primaryStage.setTitle("Library Management System");
//        primaryStage.show();
//
//        FadeTransition fadeIn=new FadeTransition(Duration.seconds(2),introLabel);
//        fadeIn.setFromValue(0);
//        fadeIn.setToValue(1);
//        fadeIn.setCycleCount(1);
//        fadeIn.play();
//
//        // Set up the pause transition for 3 seconds
//        PauseTransition pause = new PauseTransition(Duration.seconds(1));
//        pause.setOnFinished(e -> fadeOutAndShowMain(primaryStage)); // Show main application after pause
//        fadeIn.setOnFinished(e->pause.play());
    showMainApplication(primaryStage);

    }

//    private void fadeOutAndShowMain(Stage primaryStage) {
//        // Create the intro screen again to fade it out
//        StackPane introRoot = new StackPane();
//        Label introLabel = new Label("Welcome to the Library Management System");
//
//        introLabel.setStyle("-fx-text-fill: #4CAF50; -fx-font-size: 24px; -fx-font-weight: bold;");
//
//        introRoot.getChildren().add(introLabel);
//
//        FadeTransition fadeOut = new FadeTransition(Duration.seconds(2), introLabel);
//        fadeOut.setFromValue(1); // Start fully opaque
//        fadeOut.setToValue(0); // End fully transparent
//        fadeOut.setCycleCount(1);
//        fadeOut.setOnFinished(e -> showMainApplication(primaryStage)); // Show main application after fade-out
//        fadeOut.play();
//    }


    private void showMainApplication(Stage primaryStage) {
        // Create the main application UI
        TabPane tabPane = new TabPane();

        Tab usersTab = new Tab("Users", new UserManagementView());
        Tab booksTab = new Tab("Books", new BookManagementView());
        Tab borrowTab = new Tab("Borrow Books", new BookBorrowView());
        Tab returnTab = new Tab("Return Books", new BookReturnView());

        tabPane.getTabs().addAll(usersTab, booksTab, borrowTab, returnTab);

        Scene mainScene = new Scene(tabPane, 800, 600);
        primaryStage.setScene(mainScene); // Switch to main application scene
        primaryStage.setTitle("Library Management System");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}



