package com.andreas.heimann.sudoku.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.andreas.heimann.sudoku.GridClearer;
import com.andreas.heimann.sudoku.GridHelper;
import com.andreas.heimann.sudoku.GridSolver;
import com.andreas.heimann.sudoku.gui.ViewUpdateListener;
import com.andreas.heimann.sudoku.items.Cell;
import com.andreas.heimann.sudoku.items.Difficulty;
import com.andreas.heimann.sudoku.items.SudokuGrid;

public class GridGuiController implements GridListener {

	private SudokuGrid sudokuGrid;
	private SudokuGrid solvedGrid;
	private ViewUpdateListener view;

	public GridGuiController(ViewUpdateListener view) {
		this.view = view;
		Difficulty difficulty = Difficulty.FOUR;

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
		sudokuGrid.getListGrid().get(cellId).setNumber(number);
		view.updateCell(cellId, number);
		if (solvedGrid.getListGrid().get(cellId).getNumber() != number) {
			view.showWrongEntry(cellId);
		}
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
	public void checkExcludeEntries() {
		GridSolver.checkExcludeEntries(sudokuGrid,
				GridSolver.getEmptyCells(sudokuGrid));
		view.updateGrid();
	}

	@Override
	public void checkUniqueEntries() {
		GridSolver.checkUniqueEntry(sudokuGrid,
				GridSolver.getEmptyCells(sudokuGrid));
		view.updateGrid();
	}

	@Override
	public void checkUniqueRowColumn() {
		GridSolver.checkUniqueRowColumn(sudokuGrid,
				GridSolver.getEmptyCells(sudokuGrid));
		view.updateGrid();
	}

	@Override
	public void checkEntryCombinations() {
		GridSolver.checkEntryCombinations(sudokuGrid,
				GridSolver.getEmptyCells(sudokuGrid));
		view.updateGrid();
	}

	@Override
	public void solveGrid() {
		GridSolver.solveGrid(sudokuGrid, Difficulty.FOUR);
		view.updateGrid();
	}
}
