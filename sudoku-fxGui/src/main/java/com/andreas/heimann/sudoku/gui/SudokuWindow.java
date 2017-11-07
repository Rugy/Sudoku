package com.andreas.heimann.sudoku.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import com.andreas.heimann.sudoku.controller.GridGuiController;
import com.andreas.heimann.sudoku.controller.GridListener;
import com.andreas.heimann.sudoku.items.Cell;

public class SudokuWindow extends Application implements ViewUpdateListener {

	private GridListener gridListener;
	public static final int CELL_SIZE = 200;

	@Override
	public void start(Stage primaryStage) throws Exception {

		GridGuiController controller = new GridGuiController(this);
		setGridListener(controller);

		List<Cell> listGrid = gridListener.getListGrid();

		GridPane gridPane = new GridPane();
		gridPane.setPadding(new Insets(10));
		for (int i = 0; i < 9; i++) {
			GridPane segmentGridPane = new GridPane();
			segmentGridPane.getStyleClass().add("segment");
			gridPane.add(segmentGridPane, i % 3, i / 3);
		}

		List<EntryPane> cells = new ArrayList<>();
		for (int i = 0; i < 81; i++) {
			Cell cell = listGrid.get(i);
			int number = cell.getNumber();
			int row = cell.getRow();
			int column = cell.getColumn();
			int segment = cell.getSegment();

			EntryPane entryPane = new EntryPane(number, row, column, segment);
			entryPane.getStyleClass().add("entryPane");
			cells.add(entryPane);
			GridPane segmentGridPane = (GridPane) gridPane.getChildren().get(
					segment);
			segmentGridPane.add(entryPane, column % 3, row % 3);
		}

		for (int j = 0; j < 81; j++) {
			EntryPane entryPane = cells.get(j);

			entryPane.setOnMouseEntered(e -> {
				for (EntryPane aPane : cells) {
					if (aPane == entryPane) {
						continue;
					}
					if (aPane.getRow() == entryPane.getRow()) {
						aPane.setStyle("-fx-background-color: "
								+ EntryPane.COLOR_HOVER_ADJACENT + ";");
					}
					if (aPane.getColumn() == entryPane.getColumn()) {
						aPane.setStyle("-fx-background-color: "
								+ EntryPane.COLOR_HOVER_ADJACENT + ";");
					}
					if (aPane.getSegment() == entryPane.getSegment()) {
						aPane.setStyle("-fx-background-color: "
								+ EntryPane.COLOR_HOVER_ADJACENT + ";");
					}
				}
			});
			entryPane.setOnMouseExited(e -> {
				for (EntryPane aLabel : cells) {
					if (aLabel == entryPane) {
						continue;
					}
					if (aLabel.getRow() == entryPane.getRow()) {
						aLabel.setStyle("-fx-background-color: "
								+ EntryPane.COLOR_NEUTRAL + ";");
					}
					if (aLabel.getColumn() == entryPane.getColumn()) {
						aLabel.setStyle("-fx-background-color: "
								+ EntryPane.COLOR_NEUTRAL + ";");
					}
					if (aLabel.getSegment() == entryPane.getSegment()) {
						aLabel.setStyle("-fx-background-color: "
								+ EntryPane.COLOR_NEUTRAL + ";");
					}
				}
			});
		}

		Button showEntriesButton = new Button("Show Entries");
		showEntriesButton.setId("showEntriesButton");
		showEntriesButton.setOnAction(e -> {
			for (int j = 0; j < 81; j++) {
				int row = cells.get(j).getRow();
				int column = cells.get(j).getColumn();
				int segment = cells.get(j).getSegment();
				Set<Integer> possibleEntries = listGrid.get(j)
						.getPossibleEntries();

				if (listGrid.get(row * 9 + column).getNumber() == 0) {
					int size = (int) ((GridPane) gridPane.getChildren().get(
							segment)).getHeight();
					cells.get(row * 9 + column).changeEntryPane(
							possibleEntries, size / 3);
					// EntryPane entriesPane = new EntryPane(size / 3,
					// possibleEntries, row, column, segment);
					// entriesPane.getStyleClass().add("segment");
					// ((GridPane) gridPane.getChildren().get(segment)).add(
				// entriesPane, column % 3, row % 3);
			}
		}
	})	;
		HBox buttonBox = new HBox();
		buttonBox.setPadding(new Insets(25, 0, 0, 0));
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.getChildren().add(showEntriesButton);

		gridPane.add(buttonBox, 0, 3, 3, 1);

		Scene scene = new Scene(gridPane);

		primaryStage.setScene(scene);
		scene.getStylesheets().add(
				getClass().getResource("Cell.css").toExternalForm());
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	public GridListener getGridListener() {
		return gridListener;
	}

	public void setGridListener(GridListener gridListener) {
		this.gridListener = gridListener;
	}

	@Override
	public void updateGrid() {

	}

}
