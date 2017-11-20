package com.andreas.heimann.sudoku.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
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
	private Button uniqueEntriesButton;
	private Button uniqueRowColumnButton;
	private Button entryCombinationButton;
	private Button xWingButton;
	private CheckBox showWrong;
	private Label excludeCount;
	private Button excludeCountButton;
	private int size = 60;

	@Override
	public void start(Stage primaryStage) throws Exception {
		GridGuiController controller = new GridGuiController(this);
		setGridListener(controller);

		makeWindowGridPane();
		makeGridCells();
		addSolveGridButton();
		addExcludeEntriesButton();
		addUniqueEntriesButton();
		addUniqueRowColumnButton();
		addEntryCombinationButton();
		addxWingButton();
		addShowWrongCheckBox();
		addExcludeCount();

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
				entryPane.getChildren().get(0).getStyleClass()
						.add("initial-entry");
			}
			entryPane.getStyleClass().add("entry-pane");
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
		solveGridButton.getStyleClass().add("button");
		solveGridButton.setOnAction(e -> {
			gridListener.solveGrid();
		});

		gridPane.add(solveGridButton, 0, 3);
	}

	private void addExcludeEntriesButton() {
		excludeEntriesButton = new Button("Exclude Entries");
		excludeEntriesButton.setId("excludeEntriesButton");
		excludeEntriesButton.getStyleClass().add("button");
		excludeEntriesButton.setOnAction(e -> {
			gridListener.checkExcludeEntries();
		});

		gridPane.add(excludeEntriesButton, 1, 3);
	}

	private void addUniqueEntriesButton() {
		uniqueEntriesButton = new Button("Unique Entries");
		uniqueEntriesButton.setId("uniqueEntriesButton");
		uniqueEntriesButton.getStyleClass().add("button");
		uniqueEntriesButton.setOnAction(e -> {
			gridListener.checkUniqueEntries();
		});

		gridPane.add(uniqueEntriesButton, 2, 3);
	}

	private void addUniqueRowColumnButton() {
		uniqueRowColumnButton = new Button("Unique Row or Column");
		uniqueRowColumnButton.setId("uniqueRowColumnButton");
		uniqueRowColumnButton.getStyleClass().add("button");
		uniqueRowColumnButton.setOnAction(e -> {
			gridListener.checkUniqueRowColumn();
		});

		gridPane.add(uniqueRowColumnButton, 0, 4);
	}

	private void addEntryCombinationButton() {
		entryCombinationButton = new Button("Entry Combinations");
		entryCombinationButton.setId("entryCombinationButton");
		entryCombinationButton.getStyleClass().add("button");
		entryCombinationButton.setOnAction(e -> {
			gridListener.checkEntryCombinations();
		});

		gridPane.add(entryCombinationButton, 1, 4);
	}

	private void addxWingButton() {
		xWingButton = new Button("X Wing");
		xWingButton.setId("xWingButton");
		xWingButton.getStyleClass().add("button");
		xWingButton.setOnAction(e -> {
			gridListener.checkFish(2);
			gridListener.checkFish(3);
		});

		gridPane.add(xWingButton, 0, 5);
	}

	private void addShowWrongCheckBox() {
		showWrong = new CheckBox("Reveal Wrong Entries");
		showWrong.setOnAction((e) -> {
			if (showWrong.isSelected()) {
				showWrongEntries();
			} else {
				hideWrongEntries();
			}
		});

		gridPane.add(showWrong, 2, 4);
	}

	private void addExcludeCount() {
		excludeCount = new Label();
		gridPane.add(excludeCount, 1, 5);

		excludeCountButton = new Button("ExcludeCount");
		excludeCountButton.setOnAction(e -> {
			gridListener.excludeCount();
		});

		gridPane.add(excludeCountButton, 2, 5);
	}

	@Override
	public void updateExcludes(int count) {
		excludeCount.setText(String.valueOf(count));
	}

	private void showWrongEntries() {
		List<Cell> listGrid = gridListener.getListGridCopy();
		List<Cell> solvedGrid = gridListener.getSolvedGridCopy();

		for (int i = 0; i < listGrid.size(); i++) {
			boolean isZero = listGrid.get(i).getNumber() == 0;
			boolean hasSameEntry = listGrid.get(i).getNumber() == solvedGrid
					.get(i).getNumber();
			boolean hasZeroAsEntry = false;

			if (cells.get(i).getChildren().size() == 1) {
				Label label = (Label) cells.get(i).getChildren().get(0);
				if ("0".equals(label.getText())) {
					hasZeroAsEntry = true;
				}
			}

			if ((!isZero && !hasSameEntry) || hasZeroAsEntry) {
				cells.get(i).getStyleClass().add("wrong-entry");
			}
		}
	}

	public void showWrongEntry(int cellId) {
		if (showWrong.isSelected()) {
			cells.get(cellId).getStyleClass().add("wrong-entry");
		}
	}

	private void hideWrongEntries() {
		for (EntryPane aPane : cells) {
			aPane.getStyleClass().removeAll("wrong-entry");
		}
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

		if (showWrong.isSelected()) {
			showWrongEntries();
		}
	}

	@Override
	public void updateCell(int cellId, int number) {
		cells.get(cellId).changeToEntryPane(number, size);
	}

	@Override
	public void updateCell(int cellId, Set<Integer> possibleEntries) {
		EntryPane entryPane = cells.get(cellId);

		if (entryPane.getChildren().size() == 1) {
			entryPane.changeToPossibleEntryPane(possibleEntries, size);
			cells.get(cellId).getStyleClass().remove("wrong-entry");
		} else {
			entryPane.changePossibleEntries(possibleEntries);
		}
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

	@Override
	public void removeEntry(int cellId) {
		gridListener.removeEntry(cellId);
	}
}
