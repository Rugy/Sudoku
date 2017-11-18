package com.andreas.heimann.sudoku;

import com.andreas.heimann.sudoku.items.Difficulty;
import com.andreas.heimann.sudoku.items.SudokuGrid;

/**
 *
 */
public class SudokuMain {

	public static void main(String[] args) {

		Difficulty difficulty = Difficulty.FIVE;

		SudokuGrid sudokuGrid = new SudokuGrid();
		GridHelper.importGrid(sudokuGrid);
		GridSolver.solveGrid(sudokuGrid, difficulty);

		GridHelper.printGrid(sudokuGrid.getArrayGrid());
		System.out.println(sudokuGrid.getDifficulty());

	}
}
