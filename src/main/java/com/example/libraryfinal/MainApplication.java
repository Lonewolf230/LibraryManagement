

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
        showMainApplication(primaryStage);
    }



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



