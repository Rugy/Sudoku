package com.andreas.heimann.sudoku;

import com.andreas.heimann.sudoku.items.Difficulty;
import com.andreas.heimann.sudoku.items.SudokuGrid;

/**
 *
 */
public class SudokuMain {

	public static void main(String[] args) {

		Difficulty difficulty = Difficulty.THREE;

		SudokuGrid sudokuGrid = new SudokuGrid();
		GridHelper.generateGrid(sudokuGrid);
		GridClearer.clearIncrementally(sudokuGrid, difficulty);
		GridHelper.printGrid(sudokuGrid.getArrayGrid());
		GridSolver.solveGrid(sudokuGrid, difficulty);

		GridHelper.printGrid(sudokuGrid.getArrayGrid());

	}
}
