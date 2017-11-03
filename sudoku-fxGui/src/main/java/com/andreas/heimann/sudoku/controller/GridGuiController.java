package com.andreas.heimann.sudoku.controller;

import java.util.ArrayList;
import java.util.List;

import com.andreas.heimann.sudoku.GridClearer;
import com.andreas.heimann.sudoku.GridHelper;
import com.andreas.heimann.sudoku.gui.ViewUpdateListener;
import com.andreas.heimann.sudoku.items.Cell;
import com.andreas.heimann.sudoku.items.Difficulty;
import com.andreas.heimann.sudoku.items.SudokuGrid;

public class GridGuiController implements GridListener {

	SudokuGrid sudokuGrid;
	ViewUpdateListener view;

	public GridGuiController(ViewUpdateListener view) {
		this.view = view;

		sudokuGrid = new SudokuGrid();
		GridHelper.generateGrid(sudokuGrid);
		GridClearer.clearIncrementally(sudokuGrid, Difficulty.ONE);

	}

	@Override
	public List<Cell> getListGrid() {
		List<Cell> cells = new ArrayList<>();

		for (Cell aCell : sudokuGrid.getListGrid()) {
			cells.add(aCell.copyCell());
			aCell.setNumber(0);
		}

		return cells;
	}
}
