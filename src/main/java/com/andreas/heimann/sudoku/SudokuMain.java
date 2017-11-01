package com.andreas.heimann.sudoku;

import com.andreas.heimann.sudoku.items.Difficulty;
import com.andreas.heimann.sudoku.items.SudokuGrid;

/**
 *
 */
public class SudokuMain {

	public static void main(String[] args) {

		Difficulty difficulty = Difficulty.THREE;
		int iter = 0;

		SudokuGrid sudokuGrid = new SudokuGrid();
		while (sudokuGrid.getDifficulty() != difficulty) {
			GridHelper.generateGrid(sudokuGrid);
			GridClearer.clearIncrementally(sudokuGrid, difficulty);
			GridSolver.solveGrid(sudokuGrid, difficulty);
			System.out.println(sudokuGrid.getDifficulty());
			iter++;
		}

		System.out.println(iter);

		GridHelper.printGrid(sudokuGrid.getArrayGrid());

	}

}
