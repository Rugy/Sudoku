package com.andreas.heimann.sudoku.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainWindow extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(25));

		Scene scene = new Scene(gridPane, 300, 275);

		Text titleText = new Text("Welcome");
		titleText.setId("welcome-text");
		HBox hBoxTitleText = new HBox(0);
		hBoxTitleText.setAlignment(Pos.CENTER);
		hBoxTitleText.getChildren().add(titleText);
		gridPane.add(hBoxTitleText, 0, 0, 2, 1);

		Label nameLabel = new Label("User Name:");
		gridPane.add(nameLabel, 0, 1);
		TextField userTextField = new TextField();
		gridPane.add(userTextField, 1, 1);

		Label pw = new Label("Password:");
		gridPane.add(pw, 0, 2);
		PasswordField pwBox = new PasswordField();
		gridPane.add(pwBox, 1, 2);

		Button button = new Button("Sign in");
		HBox hBoxButton = new HBox(10);
		hBoxButton.setAlignment(Pos.BOTTOM_RIGHT);
		hBoxButton.getChildren().add(button);
		gridPane.add(hBoxButton, 1, 4);

		Text actionTarget = new Text();
		actionTarget.setId("actiontarget");
		gridPane.add(actionTarget, 1, 6);

		button.setOnMousePressed(e -> {
			actionTarget.setText("Sign in Button pressed!");
		});
		button.setOnMouseReleased(e -> {
			actionTarget.setText("Sign in Button Released!");
		});

		primaryStage.setTitle("Form Tutorial");
		primaryStage.setScene(scene);
		scene.getStylesheets().add(
				MainWindow.class.getResource("MainWindow2.css")
						.toExternalForm());
		primaryStage.show();
	}
}
