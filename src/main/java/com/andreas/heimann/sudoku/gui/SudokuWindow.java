package com.andreas.heimann.sudoku.gui;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import com.andreas.heimann.sudoku.GridClearer;
import com.andreas.heimann.sudoku.GridHelper;
import com.andreas.heimann.sudoku.GridSolver;
import com.andreas.heimann.sudoku.items.Cell;
import com.andreas.heimann.sudoku.items.Difficulty;
import com.andreas.heimann.sudoku.items.SudokuGrid;

public class SudokuWindow extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

		SudokuGrid sudokuGrid = new SudokuGrid();
		GridHelper.generateGrid(sudokuGrid);
		List<Cell> listGrid = sudokuGrid.getListGrid();
		GridClearer.clearIncrementally(sudokuGrid, Difficulty.FOUR);
		GridHelper.printGrid(sudokuGrid.getArrayGrid());
		GridSolver.checkExcludeEntries(sudokuGrid,
				GridSolver.getEmptyCells(sudokuGrid));

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

		Scene scene = new Scene(gridPane);

		primaryStage.setScene(scene);
		scene.getStylesheets().add(
				getClass().getResource("Cell.css").toExternalForm());
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
