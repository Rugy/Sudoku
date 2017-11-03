package com.andreas.heimann.sudoku;

import java.util.ArrayList;
import java.util.List;

import com.andreas.heimann.sudoku.items.Difficulty;
import com.andreas.heimann.sudoku.items.SudokuGrid;

public class GridClearer {

	public static void clearIncrementally(SudokuGrid sudokuGrid,
			Difficulty difficulty) {
		SudokuGrid copyGrid;
		List<Integer> removedCells;

		do {
			copyGrid = sudokuGrid.cloneSudokuGrid();
			removedCells = clear(copyGrid, sudokuGrid, difficulty);
		} while (copyGrid.getDifficulty() != difficulty);

		for (int i = 0; i < removedCells.size(); i++) {
			int removedListCell = removedCells.get(i);
			sudokuGrid.getListGrid().get(removedListCell).clearCell();
		}
	}

	private static List<Integer> clear(SudokuGrid copyGrid,
			SudokuGrid sudokuGrid, Difficulty difficulty) {
		List<Integer> filledCells = new ArrayList<>();
		List<Integer> removedCells = new ArrayList<>();

		for (int i = 0; i < 81; i++) {
			filledCells.add(i);
		}

		int attempt = 0;
		int attemptLimit = 10;

		while (attempt < attemptLimit) {
			int clearedCellNumber = (int) (Math.random() * filledCells.size());
			int clearedCellValue = filledCells.get(clearedCellNumber);
			removedCells.add(clearedCellValue);
			filledCells.remove(clearedCellNumber);

			for (Integer clearedCell : removedCells) {
				copyGrid.getListGrid().get(clearedCell).clearCell();
			}

			Difficulty prevSolvedDifficulty = copyGrid.getDifficulty();
			if (!GridSolver.solveGrid(copyGrid, difficulty)) {
				copyGrid.setDifficulty(prevSolvedDifficulty);
				attempt++;
				filledCells.add(clearedCellValue);
				removedCells.remove(removedCells.size() - 1);
				int originalValue = sudokuGrid.getListGrid()
						.get(clearedCellValue).getNumber();
				copyGrid.getListGrid().get(clearedCellValue)
						.setNumber(originalValue);
			} else {
				attempt = 0;
			}
		}

		return removedCells;
	}

}
