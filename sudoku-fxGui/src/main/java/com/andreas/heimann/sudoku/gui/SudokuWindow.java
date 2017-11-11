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

public class SudokuWindow extends Application implements ViewUpdateListener,
		CellListener {

	private GridListener gridListener;
	private List<EntryPane> cells;
	private GridPane gridPane;
	private Button solveGridButton;
	private Button excludeEntriesButton;
	private int size = 60;

	@Override
	public void start(Stage primaryStage) throws Exception {
		GridGuiController controller = new GridGuiController(this);
		setGridListener(controller);

		makeWindowGridPane();
		makeGridCells();
		addSolveGridButton();
		addExcludeEntriesButton();

		Scene scene = new Scene(gridPane);
		primaryStage.setScene(scene);
		scene.getStylesheets().add(
				getClass().getResource("Cell.css").toExternalForm());
		primaryStage.show();
	}

	private void makeWindowGridPane() {
		gridPane = new GridPane();
		gridPane.setPadding(new Insets(10));

		for (int i = 0; i < 9; i++) {
			GridPane segmentGridPane = new GridPane();
			segmentGridPane.getStyleClass().add("segment");
			gridPane.add(segmentGridPane, i % 3, i / 3);
		}
	}

	private void makeGridCells() {
		cells = new ArrayList<>();
		List<Cell> listGrid = gridListener.getListGridCopy();

		for (int i = 0; i < 81; i++) {
			Cell cell = listGrid.get(i);
			int number = cell.getNumber();
			int row = cell.getRow();
			int column = cell.getColumn();
			int segment = cell.getSegment();
			Set<Integer> possibleEntries = cell.getPossibleEntries();
			EntryPane entryPane;

			if (number == 0) {
				entryPane = new EntryPane(size, possibleEntries, row, column,
						segment);
			} else {
				entryPane = new EntryPane(size, number, row, column, segment);
			}
			entryPane.getStyleClass().add("entryPane");
			entryPane.setCellListener(this);
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
				entryPane.setStyle("-fx-background-color: "
						+ EntryPane.COLOR_HOVER + ";");
			});
			entryPane.setOnMouseExited(e -> {
				for (EntryPane aPane : cells) {
					if (aPane == entryPane) {
						continue;
					}
					if (aPane.getRow() == entryPane.getRow()) {
						aPane.setStyle("-fx-background-color: "
								+ EntryPane.COLOR_NEUTRAL + ";");
					}
					if (aPane.getColumn() == entryPane.getColumn()) {
						aPane.setStyle("-fx-background-color: "
								+ EntryPane.COLOR_NEUTRAL + ";");
					}
					if (aPane.getSegment() == entryPane.getSegment()) {
						aPane.setStyle("-fx-background-color: "
								+ EntryPane.COLOR_NEUTRAL + ";");
					}
				}
				entryPane.setStyle("-fx-background-color: "
						+ EntryPane.COLOR_NEUTRAL + ";");
			});
		}
	}

	private void addSolveGridButton() {
		solveGridButton = new Button("Solve Grid");
		solveGridButton.setId("solveGridButton");
		solveGridButton.setOnAction(e -> {
			gridListener.solveGrid();
		});

		HBox buttonBox = new HBox();
		buttonBox.setPadding(new Insets(25, 0, 0, 0));
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.getChildren().add(solveGridButton);

		gridPane.add(buttonBox, 0, 3, 2, 1);
	}

	private void addExcludeEntriesButton() {
		excludeEntriesButton = new Button("Exclude Entries");
		excludeEntriesButton.setId("excludeEntriesButton");
		excludeEntriesButton.setOnAction(e -> {
			gridListener.checkExcludeEntries();
		});

		HBox buttonBox = new HBox();
		buttonBox.setPadding(new Insets(25, 0, 0, 0));
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.getChildren().add(excludeEntriesButton);

		gridPane.add(buttonBox, 1, 3, 2, 1);
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
		List<Cell> listGrid = gridListener.getListGridCopy();

		for (int i = 0; i < 81; i++) {
			Cell cell = listGrid.get(i);
			EntryPane entryPane = cells.get(i);
			int number = cell.getNumber();
			Set<Integer> possibleEntries = cell.getPossibleEntries();
			int childrenCount = entryPane.getChildren().size();

			if (childrenCount == 1) {
				entryPane.changeEntry(number);
			} else if (childrenCount == 9 && possibleEntries.size() == 0) {
				entryPane.changeToEntryPane(number, size);
			} else if (childrenCount == 9 && possibleEntries.size() > 0) {
				entryPane.changePossibleEntries(possibleEntries);
			}
		}
	}

	@Override
	public void updateCell(int cellId, Set<Integer> possibleEntries) {
		cells.get(cellId).changePossibleEntries(possibleEntries);
	}

	@Override
	public void makeEntry(int number, int cellId) {
		gridListener.makeEntry(number, cellId);
	}

	@Override
	public void removeEntryOption(int number, int cellId) {
		gridListener.removePossibleEntry(number, cellId);
	}

	@Override
	public void addEntryOption(int number, int cellId) {
		gridListener.addPossibleEntry(number, cellId);
	}
}
