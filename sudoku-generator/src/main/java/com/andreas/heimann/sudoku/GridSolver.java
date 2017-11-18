package com.andreas.heimann.sudoku;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.andreas.heimann.sudoku.items.Cell;
import com.andreas.heimann.sudoku.items.Difficulty;
import com.andreas.heimann.sudoku.items.SolutionStep;
import com.andreas.heimann.sudoku.items.StepType;
import com.andreas.heimann.sudoku.items.SudokuGrid;

public class GridSolver {

	public static boolean solveGrid(SudokuGrid sudokuGrid, Difficulty difficulty) {
		List<Cell> emptyCellsStart = getEmptyCells(sudokuGrid);
		List<Cell> emptyCellsCurrent = new ArrayList<>();

		while (emptyCellsStart.size() != emptyCellsCurrent.size()
				&& emptyCellsStart.size() != 0) {
			boolean madeEntries = makeEntries(emptyCellsStart);

			// Try Easy
			checkExcludeEntries(sudokuGrid, emptyCellsStart);
			madeEntries = makeEntries(emptyCellsStart);

			// Try Medium
			if (!madeEntries && difficulty.ordinal() > 0) {
				checkUniqueEntry(sudokuGrid, emptyCellsStart);
				madeEntries = makeEntries(emptyCellsStart);
				if (madeEntries) {
					if (sudokuGrid.getDifficulty().ordinal() < 1) {
						sudokuGrid.setDifficulty(Difficulty.TWO);
					}
				}
			}

			// Try Hard1
			if (!madeEntries && difficulty.ordinal() > 1) {
				checkUniqueRowColumn(sudokuGrid, emptyCellsStart);
				madeEntries = makeEntries(emptyCellsStart);
				if (madeEntries) {
					if (sudokuGrid.getDifficulty().ordinal() < 2) {
						sudokuGrid.setDifficulty(Difficulty.THREE);
					}
				}
			}

			// Try Hard2
			if (!madeEntries && difficulty.ordinal() > 2) {
				checkEntryCombinations(sudokuGrid, emptyCellsStart);
				madeEntries = makeEntries(emptyCellsStart);
				if (madeEntries) {
					if (sudokuGrid.getDifficulty().ordinal() < 3) {
						sudokuGrid.setDifficulty(Difficulty.FOUR);
					}
				}
			}

			// Try Hard3
			if (!madeEntries && difficulty.ordinal() > 3) {
				checkXWing(sudokuGrid, emptyCellsStart);
				madeEntries = makeEntries(emptyCellsStart);
				if (madeEntries) {
					if (sudokuGrid.getDifficulty().ordinal() < 4) {
						sudokuGrid.setDifficulty(Difficulty.FIVE);
					}
				}
			}

			emptyCellsCurrent = emptyCellsStart;
			emptyCellsStart = getEmptyCells(sudokuGrid);
		}

		return emptyCellsStart.size() == 0;
	}

	public static void checkExcludeEntries(SudokuGrid sudokuGrid,
			List<Cell> emptyCells) {
		Cell[][] arrayGrid = sudokuGrid.getArrayGrid();
		List<List<Cell>> segmentGrid = sudokuGrid.getSegmentGrid();
		List<SolutionStep> solutionSteps = sudokuGrid.getSolutionSteps();

		for (Cell aCell : emptyCells) {
			int row = aCell.getRow();
			int col = aCell.getColumn();
			int segment = aCell.getSegment();

			for (int i = 0; i < 9; i++) {
				int rowNumber = arrayGrid[row][i].getNumber();
				int colNumber = arrayGrid[i][col].getNumber();
				int segNumber = segmentGrid.get(segment).get(i).getNumber();

				if (rowNumber != 0) {
					if (aCell.deletePossibleEntry(rowNumber)) {
						solutionSteps.add(new SolutionStep(aCell,
								StepType.EXCLUDE_ROW, rowNumber));
					}
				}
				if (colNumber != 0) {
					if (aCell.deletePossibleEntry(colNumber)) {
						solutionSteps.add(new SolutionStep(aCell,
								StepType.EXCLUDE_COLUMN, colNumber));
					}
				}
				if (segNumber != 0) {
					if (aCell.deletePossibleEntry(segNumber)) {
						solutionSteps.add(new SolutionStep(aCell,
								StepType.EXCLUDE_SEGMENT, segNumber));
					}
				}
			}
		}

	}

	public static void checkUniqueEntry(SudokuGrid sudokuGrid,
			List<Cell> emptyCells) {
		Cell[][] arrayGrid = sudokuGrid.getArrayGrid();
		List<List<Cell>> segmentGrid = sudokuGrid.getSegmentGrid();
		List<SolutionStep> solutionSteps = sudokuGrid.getSolutionSteps();

		for (Cell aCell : emptyCells) {
			int row = aCell.getRow();
			int col = aCell.getColumn();
			int segment = aCell.getSegment();

			for (int i = 0; i < aCell.getPossibleEntries().size(); i++) {
				int entry = (int) aCell.getPossibleEntries().toArray()[i];

				// Unique Rowentry
				int possibleEntries = 0;
				for (int j = 0; j < 9; j++) {
					if (arrayGrid[row][j].getPossibleEntries().contains(entry)) {
						possibleEntries++;
					}
				}

				if (possibleEntries == 1) {
					arrayGrid[row][col].getPossibleEntries().clear();
					arrayGrid[row][col].getPossibleEntries().add(
							new Integer(entry));
					solutionSteps.add(new SolutionStep(aCell,
							StepType.UNIQUE_ROW, entry, aCell.getRow()));
				}

				// Unique Columnentry
				possibleEntries = 0;
				for (int j = 0; j < 9; j++) {
					if (arrayGrid[j][col].getPossibleEntries().contains(entry)) {
						possibleEntries++;
					}
				}

				if (possibleEntries == 1) {
					arrayGrid[row][col].getPossibleEntries().clear();
					arrayGrid[row][col].getPossibleEntries().add(
							new Integer(entry));
					solutionSteps.add(new SolutionStep(aCell,
							StepType.UNIQUE_COLUMN, entry, aCell.getColumn()));
				}

				// Unique Blockentry
				possibleEntries = 0;
				for (int j = 0; j < 9; j++) {
					if (segmentGrid.get(segment).get(j).getPossibleEntries()
							.contains(entry)) {
						possibleEntries++;
					}
				}

				if (possibleEntries == 1) {
					arrayGrid[row][col].getPossibleEntries().clear();
					arrayGrid[row][col].getPossibleEntries().add(
							new Integer(entry));
					solutionSteps
							.add(new SolutionStep(aCell,
									StepType.UNIQUE_SEGMENT, entry, aCell
											.getSegment()));
				}
			}
		}

	}

	public static void checkUniqueRowColumn(SudokuGrid sudokuGrid,
			List<Cell> emptyCells) {
		List<Cell> emptyCellsInSegment = new ArrayList<>();
		List<SolutionStep> solutionSteps = sudokuGrid.getSolutionSteps();
		Set<Integer> possibleEntries = new HashSet<>();

		for (int segment = 1; segment <= 9; segment++) {
			for (Cell aCell : emptyCells) {
				if (aCell.getSegment() == segment) {
					emptyCellsInSegment.add(aCell);
					possibleEntries.addAll(aCell.getPossibleEntries());
				}
			}

			for (Integer anEntry : possibleEntries) {
				Set<Integer> rowsOccured = new HashSet<>();

				for (Cell aCell : emptyCellsInSegment) {
					if (aCell.getPossibleEntries().contains(anEntry)) {
						rowsOccured.add(aCell.getRow());
					}
				}

				if (rowsOccured.size() == 1) {
					int row = (int) rowsOccured.toArray()[0];

					for (Cell aCell : emptyCells) {
						if (aCell.getRow() == row
								&& aCell.getSegment() != segment) {
							aCell.deletePossibleEntry(anEntry);
							solutionSteps.add(new SolutionStep(aCell,
									StepType.UNIQUE_SEGMENTROW, anEntry, row,
									segment));
						}
					}

				}
			}

			for (Integer anEntry : possibleEntries) {
				Set<Integer> columnsOccured = new HashSet<>();

				for (Cell aCell : emptyCellsInSegment) {
					if (aCell.getPossibleEntries().contains(anEntry)) {
						columnsOccured.add(aCell.getColumn());
					}
				}

				if (columnsOccured.size() == 1) {
					int column = (int) columnsOccured.toArray()[0];

					for (Cell aCell : emptyCells) {
						if (aCell.getColumn() == column
								&& aCell.getSegment() != segment) {
							aCell.deletePossibleEntry(anEntry);
						}
					}

				}
			}

			emptyCellsInSegment.clear();
			possibleEntries.clear();
		}
	}

	public static void checkEntryCombinations(SudokuGrid sudokuGrid,
			List<Cell> emptyCells) {
		List<SolutionStep> solutionSteps = sudokuGrid.getSolutionSteps();
		List<Cell> emptyCellsInRow = new ArrayList<>();
		List<Cell> emptyCellsInColumn = new ArrayList<>();
		List<Cell> emptyCellsInSegment = new ArrayList<>();

		for (int i = 1; i <= 9; i++) {
			for (Cell aCell : emptyCells) {
				if (aCell.getRow() == i) {
					emptyCellsInRow.add(aCell);
				}
				if (aCell.getColumn() == i) {
					emptyCellsInColumn.add(aCell);
				}
				if (aCell.getSegment() == i) {
					emptyCellsInSegment.add(aCell);
				}
			}

			if (emptyCellsInRow.size() > 1) {
				excludeEntryCombinations(emptyCellsInRow, solutionSteps,
						StepType.ENTRYCOMBINATION_ROW);
			}
			if (emptyCellsInColumn.size() > 1) {
				excludeEntryCombinations(emptyCellsInColumn, solutionSteps,
						StepType.ENTRYCOMBINATION_COLUMN);
			}
			if (emptyCellsInSegment.size() > 1) {
				excludeEntryCombinations(emptyCellsInSegment, solutionSteps,
						StepType.ENTRYCOMBINATION_SEGMENT);
			}

			emptyCellsInRow.clear();
			emptyCellsInColumn.clear();
			emptyCellsInSegment.clear();
		}

	}

	private static void excludeEntryCombinations(List<Cell> emptyCells,
			List<SolutionStep> solutionSteps, StepType stepType) {
		List<List<Cell>> permutation = getPermutation(emptyCells, solutionSteps);

		for (List<Cell> aCellList : permutation) {
			Set<Integer> possibleEntriesCombination = new HashSet<>();

			for (Cell aCell : aCellList) {
				possibleEntriesCombination.addAll(aCell.getPossibleEntries());
			}

			if (aCellList.size() == possibleEntriesCombination.size()
					&& aCellList.size() != emptyCells.size()) {
				emptyCells.removeAll(aCellList);

				for (Cell aCell : emptyCells) {
					int uniqueField = 0;
					if (stepType == StepType.ENTRYCOMBINATION_ROW) {
						uniqueField = aCell.getRow();
					}
					if (stepType == StepType.ENTRYCOMBINATION_COLUMN) {
						uniqueField = aCell.getColumn();
					}
					if (stepType == StepType.ENTRYCOMBINATION_SEGMENT) {
						uniqueField = aCell.getSegment();
					}

					aCell.getPossibleEntries().removeAll(
							possibleEntriesCombination);
					solutionSteps.add(new SolutionStep(aCell,
							StepType.ENTRYCOMBINATION_COLUMN, uniqueField,
							possibleEntriesCombination, aCellList));
				}
				break;
			}
		}
	}

	public static void checkXWing(SudokuGrid sudokuGrid, List<Cell> emptyCells) {
		List<Cell> emptyCellsInRow = new ArrayList<>();
		List<Cell> emptyCellsInColumn = new ArrayList<>();
		List<Cell> xRow = new ArrayList<>();
		List<Cell> xColumn = new ArrayList<>();

		for (int entry = 0; entry < 9; entry++) {

			for (int i = 0; i < 9; i++) {
				for (Cell aCell : emptyCells) {
					boolean hasEntry = aCell.getPossibleEntries().contains(
							entry);

					if (aCell.getRow() == i && hasEntry) {
						emptyCellsInRow.add(aCell);
					}
					if (aCell.getColumn() == i && hasEntry) {
						emptyCellsInColumn.add(aCell);
					}
				}

				if (emptyCellsInRow.size() == 2) {
					xRow.addAll(emptyCellsInRow);
				}
				if (emptyCellsInColumn.size() == 2) {
					xColumn.addAll(emptyCellsInColumn);
				}

				emptyCellsInRow.clear();
				emptyCellsInColumn.clear();
			}
			int xRowSize = xRow.size() / 2;
			int xColSize = xColumn.size() / 2;

			if (xRowSize > 1) {
				checkXRowRemoval(entry, xRow, emptyCells);
			}
			if (xColSize > 1) {
				checkXColRemoval(entry, xColumn, emptyCellsInColumn);
			}

			xRow.clear();
			xColumn.clear();
		}
	}

	private static void checkXRowRemoval(int entry, List<Cell> xRow,
			List<Cell> emptyCells) {
		int xSize = xRow.size() / 2;

		for (int j = 0; j < xSize; j++) {
			int colAOne = xRow.get(j * 2).getColumn();
			int colATwo = xRow.get(j * 2 + 1).getColumn();
			int rowA = xRow.get(j * 2).getRow();

			for (int k = j + 1; k < xSize; k++) {
				int colBOne = xRow.get(k * 2).getColumn();
				int colBTwo = xRow.get(k * 2 + 1).getColumn();
				int rowB = xRow.get(k * 2).getRow();

				if (colAOne == colBOne && colATwo == colBTwo) {
					for (Cell aCell : emptyCells) {
						int col = aCell.getColumn();
						int row = aCell.getRow();

						if (col == colAOne && (row != rowA && row != rowB)) {
							aCell.getPossibleEntries().remove(entry);
						} else if (col == colATwo
								&& (row != rowA && row != rowB)) {
							aCell.getPossibleEntries().remove(entry);
						}
					}
				}
			}
		}
	}

	private static void checkXColRemoval(int entry, List<Cell> xColumn,
			List<Cell> emptyCells) {
		int xSize = xColumn.size() / 2;

		for (int j = 0; j < xSize; j++) {
			int rowAOne = xColumn.get(j * 2).getRow();
			int rowATwo = xColumn.get(j * 2 + 1).getRow();
			int colA = xColumn.get(j * 2).getRow();

			for (int k = j + 1; k < xSize; k++) {
				int rowBOne = xColumn.get(k * 2).getRow();
				int rowBTwo = xColumn.get(k * 2 + 1).getRow();
				int colB = xColumn.get(k * 2).getRow();

				if (rowAOne == rowBOne && rowATwo == rowBTwo) {
					for (Cell aCell : emptyCells) {
						int col = aCell.getColumn();
						int row = aCell.getRow();

						if (row == rowAOne && (col != colA && col != colB)) {
							aCell.getPossibleEntries().remove(entry);
						} else if (row == rowATwo
								&& (col != colA && col != colB)) {
							aCell.getPossibleEntries().remove(entry);
						}
					}
				}
			}
		}
	}

	private static List<List<Cell>> getPermutation(List<Cell> emptyCells,
			List<SolutionStep> solutionSteps) {
		List<List<Cell>> permutations = new ArrayList<>();

		for (int i = 0; i < emptyCells.size(); i++) {
			List<Cell> list = new ArrayList<>();
			list.add(emptyCells.get(i));
			permutations.add(list);
			permute(list, i, emptyCells, permutations);
		}

		return permutations;
	}

	private static void permute(List<Cell> prefixList, int start,
			List<Cell> emptyCells, List<List<Cell>> permutations) {
		if (start + 1 >= emptyCells.size()) {
			return;
		}

		for (int i = start + 1; i < emptyCells.size(); i++) {
			List<Cell> permutatedList = new ArrayList<>();
			permutatedList.addAll(prefixList);
			permutatedList.add(emptyCells.get(i));
			permutations.add(permutatedList);
			if (i < emptyCells.size() - 1) {
				permute(permutatedList, i, emptyCells, permutations);
			}
		}
	}

	public static List<Cell> getEmptyCells(SudokuGrid sudokuGrid) {
		List<Cell> cells = new ArrayList<>();

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (sudokuGrid.getArrayGrid()[i][j].getNumber() == 0) {
					cells.add(sudokuGrid.getArrayGrid()[i][j]);
				}
			}
		}

		return cells;
	}

	public static boolean makeEntries(List<Cell> cells) {
		boolean madeEntry = false;

		for (Cell aCell : cells) {
			if (aCell.getPossibleEntries().size() == 1) {
				Integer entry = (Integer) aCell.getPossibleEntries().toArray()[0];
				aCell.setNumber(entry);
				aCell.deletePossibleEntry(entry);
				madeEntry = true;
			}
		}

		return madeEntry;
	}

	public static boolean isEntryPossible(List<Cell> cells) {
		for (Cell aCell : cells) {
			if (aCell.getPossibleEntries().size() == 1) {
				return true;
			}
		}

		return false;
	}
}
