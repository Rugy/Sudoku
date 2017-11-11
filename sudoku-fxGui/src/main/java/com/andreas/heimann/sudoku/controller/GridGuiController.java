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
	private ViewUpdateListener view;

	public GridGuiController(ViewUpdateListener view) {
		this.view = view;

		sudokuGrid = new SudokuGrid();
		GridHelper.generateGrid(sudokuGrid);
		GridClearer.clearIncrementally(sudokuGrid, Difficulty.ONE);
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
	public void makeEntry(int number, int cellId) {
		sudokuGrid.getListGrid().get(cellId).setNumber(number);
		view.updateGrid();
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
	public void solveGrid() {
		GridSolver.solveGrid(sudokuGrid, Difficulty.ONE);
		view.updateGrid();
	}
}
