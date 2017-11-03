package com.andreas.heimann.sudoku;

import com.andreas.heimann.sudoku.items.Difficulty;
import com.andreas.heimann.sudoku.items.SudokuGrid;

/**
 *
 */
public class SudokuMain {

	public static void main(String[] args) {

		Difficulty difficulty = Difficulty.FOUR;

		SudokuGrid sudokuGrid = new SudokuGrid();
		GridHelper.generateGrid(sudokuGrid);
		GridClearer.clearIncrementally(sudokuGrid, difficulty);
		GridSolver.solveGrid(sudokuGrid, Difficulty.FOUR);

		System.out.println(sudokuGrid.getDifficulty());

	}
}
