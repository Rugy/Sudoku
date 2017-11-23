package com.andreas.heimann.sudoku.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.andreas.heimann.sudoku.GridHelper;
import com.andreas.heimann.sudoku.GridSolver;
import com.andreas.heimann.sudoku.gui.ViewUpdateListener;
import com.andreas.heimann.sudoku.gui.items.RuleLabel;
import com.andreas.heimann.sudoku.items.Cell;
import com.andreas.heimann.sudoku.items.Difficulty;
import com.andreas.heimann.sudoku.items.RuleType;
import com.andreas.heimann.sudoku.items.SolutionStep;
import com.andreas.heimann.sudoku.items.SudokuGrid;

public class GridGuiController implements GridListener {

	private SudokuGrid sudokuGrid;
	private SudokuGrid solvedGrid;
	private ViewUpdateListener view;

	public GridGuiController(ViewUpdateListener view) {
		this.view = view;
		Difficulty difficulty = Difficulty.FIVE;

		sudokuGrid = new SudokuGrid();
		GridHelper.importGrid(sudokuGrid);

		solvedGrid = sudokuGrid.cloneSudokuGrid();
		GridSolver.solveGrid(solvedGrid, difficulty);
	}

	@Override
	public List<Cell> getListGridCopy() {
		List<Cell> cells = new ArrayList<>();

		for (Cell aCell : sudokuGrid.getListGrid()) {
			cells.add(aCell.copyCell());
		}

		return cells;
	}

	@Override
	public List<Cell> getSolvedGridCopy() {
		List<Cell> cells = new ArrayList<>();

		for (Cell aCell : solvedGrid.getListGrid()) {
			cells.add(aCell.copyCell());
		}

		return cells;
	}

	@Override
	public void makeEntry(int number, int cellId) {
		Cell cell = sudokuGrid.getListGrid().get(cellId);
		cell.setNumber(number);
		view.updateCell(cellId, number);
		if (solvedGrid.getListGrid().get(cellId).getNumber() != number) {
			view.showWrongEntry(cellId);
		}
	}

	@Override
	public void removeEntry(int cellId) {
		Cell cell = sudokuGrid.getListGrid().get(cellId);
		cell.clearCell();
		view.updateCell(cellId, cell.getEntries());
	}

	@Override
	public void removePossibleEntry(int number, int cellId) {
		Cell cell = sudokuGrid.getListGrid().get(cellId);
		cell.deletePossibleEntry(number);
		Set<Integer> possibleEntries = new HashSet<>(cell.getEntries());

		view.updateCell(cellId, possibleEntries);
	}

	@Override
	public void addPossibleEntry(int number, int cellId) {
		Cell cell = sudokuGrid.getListGrid().get(cellId);
		cell.getEntries().add(number);
		Set<Integer> possibleEntries = new HashSet<>(cell.getEntries());

		view.updateCell(cellId, possibleEntries);
	}

	@Override
	public void applyRule(RuleType ruleType) {
		GridSolver.checkExcludeEntries(sudokuGrid,
				GridSolver.getEmptyCells(sudokuGrid));
		GridSolver.applyRule(sudokuGrid, ruleType);
		view.updateGrid();
	}

	@Override
	public void solveGrid() {
		GridSolver.solveGrid(sudokuGrid,
				Difficulty.values()[Difficulty.values().length - 1]);
		view.updateGrid();
	}

	@Override
	public void makeUniqueEntries() {
		List<Cell> emptyCells = sudokuGrid.getListGrid();

		for (Cell aCell : emptyCells) {
			if (aCell.getEntries().size() == 1) {
				aCell.setNumber((int) aCell.getEntries().toArray()[0]);
			}
		}

		view.updateGrid();
	}

	@Override
	public void getRuleExclusions(HashMap<RuleType, RuleLabel> ruleLabels) {
		for (int i = 0; i < RuleType.values().length; i++) {
			int entriesCount = 0;
			RuleType ruleType = RuleType.values()[i];
			boolean isExcludeRule = ruleType == RuleType.EXCLUDE_ENTRIES;
			boolean allExcluded = ruleLabels.get(RuleType.EXCLUDE_ENTRIES)
					.getText().equals("0");

			if (isExcludeRule || allExcluded) {
				SudokuGrid copyGrid = sudokuGrid.cloneSudokuGrid();
				GridSolver.applyRule(copyGrid, ruleType);

				entriesCount = sudokuGrid.getEntriesCount()
						- copyGrid.getEntriesCount();

				view.updateRuleLabel(String.valueOf(entriesCount), ruleType);
			} else {
				view.updateRuleLabel("-", ruleType);
			}
		}
	}

	@Override
	public void markRuleExclusion(RuleType ruleType) {
		SudokuGrid copyGrid = sudokuGrid.cloneSudokuGrid();
		copyGrid.setSolutionSteps(new ArrayList<>());
		GridSolver.applyRule(copyGrid, ruleType);

		if (copyGrid.getSolutionSteps().size() >= 1) {
			SolutionStep step = copyGrid.getSolutionSteps().get(0);
			int cellId = step.getCell().getGridNumber();
			List<Integer> reasonIds = new ArrayList<>();

			for (Cell aCell : copyGrid.getSolutionSteps().get(0).getReason()) {
				reasonIds.add(aCell.getGridNumber());
			}

			view.markRuleExclusion(cellId, step.getEntry(), reasonIds);
		}
	}

}
