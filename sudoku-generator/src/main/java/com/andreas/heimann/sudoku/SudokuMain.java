package com.andreas.heimann.sudoku;

import com.andreas.heimann.sudoku.items.Difficulty;
import com.andreas.heimann.sudoku.items.RuleType;
import com.andreas.heimann.sudoku.items.SolutionStep;
import com.andreas.heimann.sudoku.items.SudokuGrid;

/**
 *
 */
public class SudokuMain {

	public static void main(String[] args) {

		Difficulty difficulty = Difficulty.FIVE;

		SudokuGrid sudokuGrid = new SudokuGrid();
		GridHelper.generateGrid(sudokuGrid);
		GridClearer.clearIncrementally(sudokuGrid, difficulty);
		GridHelper.printGrid(sudokuGrid.getArrayGrid());
		GridSolver.solveGrid(sudokuGrid, difficulty);

		for (SolutionStep aStep : sudokuGrid.getSolutionSteps()) {
			if (aStep.getRuleType() == RuleType.EXCLUDE_ENTRIES) {
				System.out.println("EXCLUDED " + aStep.getEntry() + " In "
						+ aStep.getCell() + " BECAUSE: " + aStep.getReason()
						+ ", contains it");
			}
		}

		GridHelper.printGrid(sudokuGrid.getArrayGrid());

	}
}
