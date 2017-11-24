package com.andreas.heimann.sudoku.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import com.andreas.heimann.sudoku.controller.GridGuiController;
import com.andreas.heimann.sudoku.controller.GridListener;
import com.andreas.heimann.sudoku.gui.items.CellListener;
import com.andreas.heimann.sudoku.gui.items.EntryPane;
import com.andreas.heimann.sudoku.gui.items.RuleLabel;
import com.andreas.heimann.sudoku.items.Cell;
import com.andreas.heimann.sudoku.items.RuleType;

public class SudokuWindow extends Application implements ViewUpdateListener,
		CellListener {

	private static final int RULES_COUNT = 9;

	private GridListener gridListener;
	private List<EntryPane> cells;
	private GridPane windowGridPane;
	private GridPane sudokuGridPane;
	private GridPane rulesGridPane;
	private GridPane optionsGridPane;
	private Button solveGridButton;
	private RuleLabel excludeEntriesLabel;
	private RuleLabel uniqueEntriesLabel;
	private RuleLabel uniqueRowColumnLabel;
	private RuleLabel entryCombinationLabel;
	private RuleLabel xWingLabel;
	private RuleLabel swordFishLabel;
	private RuleLabel jellyFishLabel;
	private RuleLabel remotePairsLabel;
	private RuleLabel uniqueRectangleLabel;
	private HashMap<RuleType, RuleLabel> ruleLabels = new HashMap<>();
	private Button makeUniqueEntriesButton;
	private CheckBox showWrong;
	private CheckBox showExclusions;
	private int size = 60;
	private int markedCellId;
	private List<Integer> markingReasonIds = new ArrayList<>();

	@Override
	public void start(Stage primaryStage) throws Exception {
		GridGuiController controller = new GridGuiController(this);
		setGridListener(controller);

		makeWindowGridPane();
		makeGridCells();
		addExcludeEntriesLabel();
		addUniqueEntriesLabel();
		addUniqueRowColumnLabel();
		addEntryCombinationLabel();
		addxWingLabel();
		addSwordFishLabel();
		addJellyFishLabel();
		addRemotePairsLabel();
		addUniqueRectangleLabel();
		addSolveGridButton();
		addMakeUniqueEntriesButton();
		addShowWrongCheckBox();
		addShowExclusionsCheckBox();

		Scene scene = new Scene(windowGridPane);
		primaryStage.setScene(scene);
		scene.getStylesheets().add(
				getClass().getResource("/Cell.css").toExternalForm());
		primaryStage.show();
	}

	private void makeWindowGridPane() {
		windowGridPane = new GridPane();
		windowGridPane.setPadding(new Insets(0, 30, 0, 0));

		sudokuGridPane = new GridPane();
		sudokuGridPane.setPadding(new Insets(10));

		for (int i = 0; i < 9; i++) {
			GridPane segmentGridPane = new GridPane();
			segmentGridPane.getStyleClass().add("segment");
			sudokuGridPane.add(segmentGridPane, i % 3, i / 3);
		}
		windowGridPane.add(sudokuGridPane, 0, 0);

		rulesGridPane = new GridPane();
		rulesGridPane.setVgap(5);
		rulesGridPane.setPadding(new Insets(10));

		for (int i = 0; i < RULES_COUNT; i++) {
			RowConstraints rowConstrait = new RowConstraints();
			rowConstrait.setFillHeight(true);
			rowConstrait.setVgrow(Priority.ALWAYS);
			rulesGridPane.getRowConstraints().add(rowConstrait);
		}

		windowGridPane.add(rulesGridPane, 1, 0);

		optionsGridPane = new GridPane();
		optionsGridPane.setPadding(new Insets(10));
		optionsGridPane.setHgap(10);
		windowGridPane.add(optionsGridPane, 0, 1, 2, 1);
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
			Set<Integer> possibleEntries = cell.getEntries();
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
			GridPane segmentGridPane = (GridPane) sudokuGridPane.getChildren()
					.get(segment);
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
						aPane.getStyleClass().add("adjacent-hover");
					} else if (aPane.getColumn() == entryPane.getColumn()) {
						aPane.getStyleClass().add("adjacent-hover");
					} else if (aPane.getSegment() == entryPane.getSegment()) {
						aPane.getStyleClass().add("adjacent-hover");
					}
				}
				if (entryPane.getChildren().size() == 1) {
					entryPane.getChildren().get(0).getStyleClass()
							.add("hover-text");
				}
				entryPane.getStyleClass().add("hover");
			});
			entryPane.setOnMouseExited(e -> {
				for (EntryPane aPane : cells) {
					if (aPane == entryPane) {
						continue;
					}
					if (aPane.getRow() == entryPane.getRow()) {
						aPane.getStyleClass().remove("adjacent-hover");
					} else if (aPane.getColumn() == entryPane.getColumn()) {
						aPane.getStyleClass().remove("adjacent-hover");
					} else if (aPane.getSegment() == entryPane.getSegment()) {
						aPane.getStyleClass().remove("adjacent-hover");
					}
				}
				if (entryPane.getChildren().size() == 1) {
					entryPane.getChildren().get(0).getStyleClass()
							.remove("hover-text");
				}
				entryPane.getStyleClass().remove("hover");
			});
		}
	}

	private void addExcludeEntriesLabel() {
		RuleType ruleType = RuleType.EXCLUDE_ENTRIES;

		excludeEntriesLabel = new RuleLabel("-", ruleType);
		excludeEntriesLabel.setId("excludeEntriesLabel");
		excludeEntriesLabel.getStyleClass().add("rule-label");
		excludeEntriesLabel.setOnMouseClicked(e -> {
			SudokuWindow.this.addMouseClicks(excludeEntriesLabel, ruleType, e);
		});
		Tooltip tooltip = new Tooltip("Exclude Entries");
		excludeEntriesLabel.setTooltip(tooltip);

		ruleLabels.put(ruleType, excludeEntriesLabel);
		rulesGridPane.add(excludeEntriesLabel, 0, 0);
	}

	private void addUniqueEntriesLabel() {
		RuleType ruleType = RuleType.UNIQUE_ENTRY;

		uniqueEntriesLabel = new RuleLabel("-", ruleType);
		uniqueEntriesLabel.setId("uniqueEntriesLabel");
		uniqueEntriesLabel.getStyleClass().add("rule-label");
		uniqueEntriesLabel.setOnMouseClicked(e -> {
			SudokuWindow.this.addMouseClicks(uniqueEntriesLabel, ruleType, e);
		});
		Tooltip tooltip = new Tooltip("Unique Entries");
		uniqueEntriesLabel.setTooltip(tooltip);

		ruleLabels.put(ruleType, uniqueEntriesLabel);
		rulesGridPane.add(uniqueEntriesLabel, 0, 1);
	}

	private void addUniqueRowColumnLabel() {
		RuleType ruleType = RuleType.UNIQUE_ROW_COLUMN;

		uniqueRowColumnLabel = new RuleLabel("-", ruleType);
		uniqueRowColumnLabel.setId("uniqueRowColumnLabel");
		uniqueRowColumnLabel.getStyleClass().add("rule-label");
		uniqueRowColumnLabel
				.setOnMouseClicked(e -> {
					SudokuWindow.this.addMouseClicks(uniqueRowColumnLabel,
							ruleType, e);
				});
		Tooltip tooltip = new Tooltip("Unique Row or Column");
		uniqueRowColumnLabel.setTooltip(tooltip);

		ruleLabels.put(ruleType, uniqueRowColumnLabel);
		rulesGridPane.add(uniqueRowColumnLabel, 0, 2);
	}

	private void addEntryCombinationLabel() {
		RuleType ruleType = RuleType.ENTRY_COMBINATION;

		entryCombinationLabel = new RuleLabel("-", ruleType);
		entryCombinationLabel.setId("entryCombinationLabel");
		entryCombinationLabel.getStyleClass().add("rule-label");
		entryCombinationLabel.setOnMouseClicked(e -> {
			SudokuWindow.this
					.addMouseClicks(entryCombinationLabel, ruleType, e);
		});
		Tooltip tooltip = new Tooltip("Entry Combinations");
		entryCombinationLabel.setTooltip(tooltip);

		ruleLabels.put(ruleType, entryCombinationLabel);
		rulesGridPane.add(entryCombinationLabel, 0, 3);
	}

	private void addxWingLabel() {
		RuleType ruleType = RuleType.X_WING;

		xWingLabel = new RuleLabel("-", ruleType);
		xWingLabel.setId("xWingLabel");
		xWingLabel.getStyleClass().add("rule-label");
		xWingLabel.setOnMouseClicked(e -> {
			SudokuWindow.this.addMouseClicks(xWingLabel, ruleType, e);
		});
		Tooltip tooltip = new Tooltip("X-Wing");
		xWingLabel.setTooltip(tooltip);

		ruleLabels.put(ruleType, xWingLabel);
		rulesGridPane.add(xWingLabel, 0, 4);
	}

	private void addSwordFishLabel() {
		RuleType ruleType = RuleType.SWORDFISH;

		swordFishLabel = new RuleLabel("-", ruleType);
		swordFishLabel.setId("swordFishLabel");
		swordFishLabel.getStyleClass().add("rule-label");
		swordFishLabel.setOnMouseClicked(e -> {
			SudokuWindow.this.addMouseClicks(swordFishLabel, ruleType, e);
		});
		Tooltip tooltip = new Tooltip("Swordfish");
		swordFishLabel.setTooltip(tooltip);

		ruleLabels.put(ruleType, swordFishLabel);
		rulesGridPane.add(swordFishLabel, 0, 5);
	}

	private void addJellyFishLabel() {
		RuleType ruleType = RuleType.JELLYFISH;

		jellyFishLabel = new RuleLabel("-", ruleType);
		jellyFishLabel.setId("jellyFishLabel");
		jellyFishLabel.getStyleClass().add("rule-label");
		jellyFishLabel.setOnMouseClicked(e -> {
			SudokuWindow.this.addMouseClicks(jellyFishLabel, ruleType, e);
		});
		Tooltip tooltip = new Tooltip("Jellyfish");
		jellyFishLabel.setTooltip(tooltip);

		ruleLabels.put(ruleType, jellyFishLabel);
		rulesGridPane.add(jellyFishLabel, 0, 6);
	}

	private void addRemotePairsLabel() {
		RuleType ruleType = RuleType.REMOTE_PAIRS;

		remotePairsLabel = new RuleLabel("-", ruleType);
		remotePairsLabel.setId("remotePairsLabel");
		remotePairsLabel.getStyleClass().add("rule-label");
		remotePairsLabel.setOnMouseClicked(e -> {
			SudokuWindow.this.addMouseClicks(remotePairsLabel, ruleType, e);
		});
		Tooltip tooltip = new Tooltip("Remote Pairs");
		remotePairsLabel.setTooltip(tooltip);

		ruleLabels.put(ruleType, remotePairsLabel);
		rulesGridPane.add(remotePairsLabel, 0, 7);
	}

	private void addUniqueRectangleLabel() {
		RuleType ruleType = RuleType.UNIQUE_RECTANGLE;

		uniqueRectangleLabel = new RuleLabel("-", ruleType);
		uniqueRectangleLabel.setId("uniqueRectangleLabel");
		uniqueRectangleLabel.getStyleClass().add("rule-label");
		uniqueRectangleLabel
				.setOnMouseClicked(e -> {
					SudokuWindow.this.addMouseClicks(uniqueRectangleLabel,
							ruleType, e);
				});
		Tooltip tooltip = new Tooltip("Unique Rectangle");
		uniqueRectangleLabel.setTooltip(tooltip);

		ruleLabels.put(ruleType, uniqueRectangleLabel);
		rulesGridPane.add(uniqueRectangleLabel, 0, 8);
	}

	private void addMouseClicks(RuleLabel label, RuleType ruleType, MouseEvent e) {
		if ("-".equals(label.getText()) || "0".equals(label.getText())) {
			return;
		}

		if (label != excludeEntriesLabel
				&& !"0".equals(excludeEntriesLabel.getText())) {
			return;
		}

		if (e.getButton() == MouseButton.PRIMARY) {
			gridListener.markRuleExclusion(ruleType);
		} else if (e.getButton() == MouseButton.SECONDARY) {
			gridListener.applyRule(ruleType);
		}
	}

	private void addSolveGridButton() {
		solveGridButton = new Button("Solve Grid");
		solveGridButton.setId("solveGridButton");
		solveGridButton.getStyleClass().add("button");
		solveGridButton.setOnAction(e -> {
			gridListener.solveGrid();
		});

		optionsGridPane.add(solveGridButton, 0, 0);
	}

	private void addMakeUniqueEntriesButton() {
		makeUniqueEntriesButton = new Button("Make Unique Entries");
		makeUniqueEntriesButton.setId("uniqueRectangleButton");
		makeUniqueEntriesButton.getStyleClass().add("button");
		makeUniqueEntriesButton.setOnAction(e -> {
			gridListener.makeUniqueEntries();
		});

		optionsGridPane.add(makeUniqueEntriesButton, 1, 0);
	}

	private void addShowWrongCheckBox() {
		showWrong = new CheckBox("Reveal Wrong Entries");
		showWrong.setOnAction(e -> {
			if (showWrong.isSelected()) {
				showWrongEntries();
			} else {
				hideWrongEntries();
			}
		});

		optionsGridPane.add(showWrong, 2, 0);
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

	private void addShowExclusionsCheckBox() {
		showExclusions = new CheckBox("Show possible Exclusions");
		showExclusions.setOnAction(e -> {
			if (showExclusions.isSelected()) {
				gridListener.getRuleExclusions(ruleLabels);
			} else {
				hideExclusions();
			}
		});

		optionsGridPane.add(showExclusions, 3, 0);
	}

	private void hideExclusions() {
		for (int i = 0; i < RuleType.values().length; i++) {
			ruleLabels.get(RuleType.values()[i]).setText("-");
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
			Set<Integer> possibleEntries = cell.getEntries();
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

		if (showExclusions.isSelected()) {
			gridListener.getRuleExclusions(ruleLabels);
		}
		removeMarkings();
	}

	@Override
	public void updateCell(int cellId, int number) {
		cells.get(cellId).changeToEntryPane(number, size);

		if (showExclusions.isSelected()) {
			gridListener.getRuleExclusions(ruleLabels);
		}
		removeMarkings();
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

		if (showExclusions.isSelected()) {
			gridListener.getRuleExclusions(ruleLabels);
		}
		removeMarkings();
	}

	@Override
	public void updateRuleLabel(String entry, RuleType ruleType) {
		ruleLabels.get(ruleType).setText(entry);
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

	@Override
	public void markRuleExclusion(int cellId, int entry, List<Integer> reasonIds) {
		removeMarkings();

		for (Integer anId : reasonIds) {
			cells.get(anId).getStyleClass().add("exclude-reason");
			markingReasonIds.add(anId);
		}

		cells.get(cellId).getChildren().get(entry - 1).getStyleClass()
				.add("exclude-entry");
		markedCellId = cellId;
	}

	public void removeMarkings() {
		for (Integer anId : markingReasonIds) {
			cells.get(anId).getStyleClass().remove("exclude-reason");
		}
		for (Node aLabel : cells.get(markedCellId).getChildren()) {
			((Label) aLabel).getStyleClass().remove("exclude-entry");
		}
	}
}
