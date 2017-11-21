package com.andreas.heimann.sudoku.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.andreas.heimann.sudoku.GridClearer;
import com.andreas.heimann.sudoku.GridHelper;
import com.andreas.heimann.sudoku.GridSolver;
import com.andreas.heimann.sudoku.gui.ViewUpdateListener;
import com.andreas.heimann.sudoku.gui.items.RuleLabel;
import com.andreas.heimann.sudoku.items.Cell;
import com.andreas.heimann.sudoku.items.Difficulty;
import com.andreas.heimann.sudoku.items.RuleType;
import com.andreas.heimann.sudoku.items.SudokuGrid;

public class GridGuiController implements GridListener {

	private SudokuGrid sudokuGrid;
	private SudokuGrid solvedGrid;
	private ViewUpdateListener view;

	public GridGuiController(ViewUpdateListener view) {
		this.view = view;
		Difficulty difficulty = Difficulty.FIVE;

		sudokuGrid = new SudokuGrid();
		GridHelper.generateGrid(sudokuGrid);
		GridClearer.clearIncrementally(sudokuGrid, difficulty);

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
		view.updateCell(cellId, cell.getPossibleEntries());
	}

	@Override
	public void removePossibleEntry(int number, int cellId) {
		Cell cell = sudokuGrid.getListGrid().get(cellId);
		cell.deletePossibleEntry(number);
		Set<Integer> possibleEntries = new HashSet<>(cell.getPossibleEntries());

		view.updateCell(cellId, possibleEntries);
	}

	@Override
	public void addPossibleEntry(int number, int cellId) {
		Cell cell = sudokuGrid.getListGrid().get(cellId);
		cell.getPossibleEntries().add(number);
		Set<Integer> possibleEntries = new HashSet<>(cell.getPossibleEntries());

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
			if (aCell.getPossibleEntries().size() == 1) {
				aCell.setNumber((int) aCell.getPossibleEntries().toArray()[0]);
			}
		}

		view.updateGrid();
	}

	@Override
	public void getRuleExclusions(List<RuleLabel> ruleLabels) {

		for (int i = 0; i < ruleLabels.size(); i++) {
			int entriesCount = 0;
			RuleType ruleType = ruleLabels.get(i).getRuleType();

			SudokuGrid copyGrid = sudokuGrid.cloneSudokuGrid();
			GridSolver.applyRule(copyGrid, ruleType);

			entriesCount = sudokuGrid.getEntriesCount()
					- copyGrid.getEntriesCount();
			view.updateRuleLabels(entriesCount, ruleType);
		}
	}
}
