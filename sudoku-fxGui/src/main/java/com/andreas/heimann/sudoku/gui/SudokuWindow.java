package com.andreas.heimann.sudoku.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

		List<CellLabel> cells = new ArrayList<>();
		for (int i = 0; i < 81; i++) {
			Cell cell = listGrid.get(i);
			int number = cell.getNumber();
			int row = cell.getRow();
			int column = cell.getColumn();
			int segment = cell.getSegment();

			cells.add(new CellLabel(String.valueOf(number), row, column,
					segment));
		}

		for (int i = 0; i < 9; i++) {
			GridPane segmentGridPane = new GridPane();
			segmentGridPane.getStyleClass().add("segment");
			int usedCells = 0;

			for (int j = 0; j < 81; j++) {
				if (cells.get(j).getSegment() == i) {
					CellLabel label = cells.get(j);

					label.setPadding(new Insets(15, 25, 15, 25));
					label.setOnMouseEntered(e -> {
						for (CellLabel aLabel : cells) {
							if (aLabel == label) {
								continue;
							}
							if (aLabel.getRow() == label.getRow()) {
								aLabel.setStyle("-fx-background-color: "
										+ CellLabel.COLOR_HOVER_ADJACENT + ";");
							}
							if (aLabel.getColumn() == label.getColumn()) {
								aLabel.setStyle("-fx-background-color: "
										+ CellLabel.COLOR_HOVER_ADJACENT + ";");
							}
							if (aLabel.getSegment() == label.getSegment()) {
								aLabel.setStyle("-fx-background-color: "
										+ CellLabel.COLOR_HOVER_ADJACENT + ";");
							}
						}
						label.setStyle("-fx-background-color: "
								+ CellLabel.COLOR_HOVER + ";");
					});
					label.setOnMouseExited(e -> {
						for (CellLabel aLabel : cells) {
							if (aLabel == label) {
								continue;
							}
							if (aLabel.getRow() == label.getRow()) {
								aLabel.setStyle("-fx-background-color: "
										+ CellLabel.COLOR_NEUTRAL + ";");
							}
							if (aLabel.getColumn() == label.getColumn()) {
								aLabel.setStyle("-fx-background-color: "
										+ CellLabel.COLOR_NEUTRAL + ";");
							}
							if (aLabel.getSegment() == label.getSegment()) {
								aLabel.setStyle("-fx-background-color: "
										+ CellLabel.COLOR_NEUTRAL + ";");
							}
						}
						label.setStyle("-fx-background-color: "
								+ CellLabel.COLOR_NEUTRAL + ";");
					});
					segmentGridPane.add(label, usedCells % 3, usedCells / 3);

					usedCells++;
				}
			}

			gridPane.add(segmentGridPane, i % 3, i / 3);
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
					((GridPane) gridPane.getChildren().get(segment))
							.getChildren().remove(cells.get(row * 9 + column));
					int size = (int) ((GridPane) gridPane.getChildren().get(
							segment)).getHeight();
					GridPane entriesPane = new GridPane();
					entriesPane.getStyleClass().add("segment");
					entriesPane.setMaxSize(size / 3, size / 3);
					entriesPane.setMinSize(size / 3, size / 3);
					for (int i = 0; i < 9; i++) {
						String entry;
						if (possibleEntries.contains(i)) {
							entry = String.valueOf(i);
						} else {
							entry = "";
						}
						Label label = new Label(entry);
						label.setMinSize(size / 9 - 1, size / 9 - 1);
						label.setAlignment(Pos.CENTER);
						label.getStyleClass().add("entry-option");
						entriesPane.add(label, i % 3, i / 3);
					}
					((GridPane) gridPane.getChildren().get(segment)).add(
							entriesPane, column % 3, row % 3);
				}
			}
		});
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
