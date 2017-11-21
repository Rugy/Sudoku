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
		List<Cell> emptyCells = getEmptyCells(sudokuGrid);
		int entriesCountStart = getEntriesCount(emptyCells);
		int entriesCountEnd = 0;

		while (entriesCountStart != 0 && entriesCountStart != entriesCountEnd) {
			int currentEntries = getEntriesCount(emptyCells);
			boolean removedEntries = entriesCountStart != currentEntries;

			// Try Easy
			checkExcludeEntries(sudokuGrid, emptyCells);
			currentEntries = getEntriesCount(emptyCells);
			removedEntries = entriesCountStart != currentEntries;

			// Try Medium
			if (!removedEntries && difficulty.ordinal() > 0) {
				checkUniqueEntry(sudokuGrid, emptyCells);
				currentEntries = getEntriesCount(emptyCells);
				removedEntries = entriesCountStart != currentEntries;
				if (removedEntries) {
					if (sudokuGrid.getDifficulty().ordinal() < 1) {
						sudokuGrid.setDifficulty(Difficulty.TWO);
					}
				}
			}

			// Try Hard 1
			if (!removedEntries && difficulty.ordinal() > 1) {
				checkUniqueRowColumn(sudokuGrid, emptyCells);
				currentEntries = getEntriesCount(emptyCells);
				removedEntries = entriesCountStart != currentEntries;
				if (removedEntries) {
					if (sudokuGrid.getDifficulty().ordinal() < 2) {
						sudokuGrid.setDifficulty(Difficulty.THREE);
					}
				}
			}

			// Try Hard 2
			if (!removedEntries && difficulty.ordinal() > 2) {
				checkEntryCombinations(sudokuGrid, emptyCells);
				currentEntries = getEntriesCount(emptyCells);
				removedEntries = entriesCountStart != currentEntries;
				if (removedEntries) {
					if (sudokuGrid.getDifficulty().ordinal() < 3) {
						sudokuGrid.setDifficulty(Difficulty.FOUR);
					}
				}
			}

			// Try Small Fishes
			if (!removedEntries && difficulty.ordinal() > 3) {
				checkSmallFish(sudokuGrid, emptyCells, 2);
				checkSmallFish(sudokuGrid, emptyCells, 3);
				checkSmallFish(sudokuGrid, emptyCells, 4);
				currentEntries = getEntriesCount(emptyCells);
				removedEntries = entriesCountStart != currentEntries;
				if (removedEntries) {
					if (sudokuGrid.getDifficulty().ordinal() < 4) {
						sudokuGrid.setDifficulty(Difficulty.FIVE);
					}
				}
			}

			// Try Remote Pairs
			if (!removedEntries && difficulty.ordinal() > 4) {
				checkRemotePairs(sudokuGrid, emptyCells);
				currentEntries = getEntriesCount(emptyCells);
				removedEntries = entriesCountStart != currentEntries;
				if (removedEntries) {
					if (sudokuGrid.getDifficulty().ordinal() < 5) {
						sudokuGrid.setDifficulty(Difficulty.SIX);
					}
				}
			}

			// Try Unique Rectangle
			if (!removedEntries && difficulty.ordinal() > 4) {
				checkUniqueRectangle(sudokuGrid, emptyCells);
				currentEntries = getEntriesCount(emptyCells);
				removedEntries = entriesCountStart != currentEntries;
				if (removedEntries) {
					if (sudokuGrid.getDifficulty().ordinal() < 5) {
						sudokuGrid.setDifficulty(Difficulty.SIX);
					}
				}
			}

			makeEntries(emptyCells);
			emptyCells = getEmptyCells(sudokuGrid);
			entriesCountEnd = entriesCountStart;
			entriesCountStart = getEntriesCount(emptyCells);
		}

		return emptyCells.size() == 0;
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
						List<Cell> reason = new ArrayList<>();
						reason.add(arrayGrid[row][i]);
						solutionSteps.add(new SolutionStep(aCell, rowNumber,
								reason));
					}
				}
				if (colNumber != 0) {
					if (aCell.deletePossibleEntry(colNumber)) {
						List<Cell> reason = new ArrayList<>();
						reason.add(arrayGrid[i][col]);
						solutionSteps.add(new SolutionStep(aCell, colNumber,
								reason));
					}
				}
				if (segNumber != 0) {
					if (aCell.deletePossibleEntry(segNumber)) {
						List<Cell> reason = new ArrayList<>();
						reason.add(segmentGrid.get(segment).get(i));
						solutionSteps.add(new SolutionStep(aCell, segNumber,
								reason));
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
				excludeEntryCombinations(sudokuGrid, emptyCellsInRow,
						solutionSteps, StepType.ENTRYCOMBINATION_ROW);
			}
			if (emptyCellsInColumn.size() > 1) {
				excludeEntryCombinations(sudokuGrid, emptyCellsInColumn,
						solutionSteps, StepType.ENTRYCOMBINATION_COLUMN);
			}
			if (emptyCellsInSegment.size() > 1) {
				excludeEntryCombinations(sudokuGrid, emptyCellsInSegment,
						solutionSteps, StepType.ENTRYCOMBINATION_SEGMENT);
			}

			emptyCellsInRow.clear();
			emptyCellsInColumn.clear();
			emptyCellsInSegment.clear();
		}

	}

	private static void excludeEntryCombinations(SudokuGrid sudokuGrid,
			List<Cell> emptyCells, List<SolutionStep> solutionSteps,
			StepType stepType) {
		List<Integer> emptyCellsId = new ArrayList<>();
		for (Cell aCell : emptyCells) {
			emptyCellsId.add(aCell.getGridNumber());
		}
		List<List<Integer>> idPermutation = getPermutation(emptyCellsId);

		List<List<Cell>> permutation = new ArrayList<>();
		for (List<Integer> idList : idPermutation) {
			permutation.add(getCellsFromId(sudokuGrid, idList));
		}

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

	public static void checkSmallFish(SudokuGrid sudokuGrid,
			List<Cell> emptyCells, int size) {
		List<Cell> emptyCellsInRow = new ArrayList<>();
		List<Cell> emptyCellsInColumn = new ArrayList<>();
		List<Cell> xRow = new ArrayList<>();
		List<Cell> xColumn = new ArrayList<>();

		for (int entry = 1; entry <= 9; entry++) {

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
				int rowSize = emptyCellsInRow.size();
				int colSize = emptyCellsInColumn.size();

				if (rowSize >= 2 && rowSize <= size) {
					xRow.addAll(emptyCellsInRow);
				}
				if (colSize >= 2 && colSize <= size) {
					xColumn.addAll(emptyCellsInColumn);
				}

				emptyCellsInRow.clear();
				emptyCellsInColumn.clear();
			}
			int xRowSize = xRow.size() / 2;
			int xColSize = xColumn.size() / 2;

			if (xRowSize >= size) {
				checkXRowRemoval(sudokuGrid, entry, xRow, emptyCells, size);
			}
			if (xColSize >= size) {
				checkXColRemoval(sudokuGrid, entry, xColumn,
						emptyCellsInColumn, size);
			}

			xRow.clear();
			xColumn.clear();
		}
	}

	private static void checkXRowRemoval(SudokuGrid sudokuGrid, int entry,
			List<Cell> xRow, List<Cell> emptyCells, int size) {
		Set<Integer> affectedRows = new HashSet<>();
		for (Cell aCell : xRow) {
			affectedRows.add(aCell.getRow());
		}

		List<List<Integer>> rowPerms = getPermutation(new ArrayList<>(
				affectedRows));
		for (int i = 0; i < rowPerms.size(); i++) {
			if (rowPerms.get(i).size() != size) {
				rowPerms.remove(i);
				i--;
			}
		}

		for (List<Integer> rowList : rowPerms) {
			Set<Integer> cols = new HashSet<>();
			Set<Integer> rows = new HashSet<>(rowList);

			for (Integer row : rowList) {
				for (Cell aCell : xRow) {
					if (aCell.getRow() == row) {
						cols.add(aCell.getColumn());
					}
				}
			}

			if (cols.size() == size) {
				for (Cell aCell : emptyCells) {
					boolean inColumn = cols.contains(aCell.getColumn());
					boolean inRow = rows.contains(aCell.getRow());
					boolean containsEntry = aCell.getPossibleEntries()
							.contains(entry);

					if (inColumn && !inRow && containsEntry) {
						aCell.getPossibleEntries().remove(entry);
					}
				}
			}
		}
	}

	private static void checkXColRemoval(SudokuGrid sudokuGrid, int entry,
			List<Cell> xColumn, List<Cell> emptyCells, int size) {
		Set<Integer> affectedColumns = new HashSet<>();
		for (Cell aCell : xColumn) {
			affectedColumns.add(aCell.getColumn());
		}

		List<List<Integer>> colPerms = getPermutation(new ArrayList<>(
				affectedColumns));
		for (int i = 0; i < colPerms.size(); i++) {
			if (colPerms.get(i).size() != size) {
				colPerms.remove(i);
				i--;
			}
		}

		for (List<Integer> colList : colPerms) {
			Set<Integer> cols = new HashSet<>(colList);
			Set<Integer> rows = new HashSet<>();

			for (Integer col : colList) {
				for (Cell aCell : xColumn) {
					if (aCell.getRow() == col) {
						rows.add(aCell.getRow());
					}
				}
			}

			if (rows.size() == size) {
				for (Cell aCell : emptyCells) {
					boolean inRow = rows.contains(aCell.getRow());
					boolean inColumn = cols.contains(aCell.getColumn());
					boolean containsEntry = aCell.getPossibleEntries()
							.contains(entry);

					if (inRow && !inColumn && containsEntry) {
						aCell.getPossibleEntries().remove(entry);
					}
				}
			}
		}
	}

	public static void checkRemotePairs(SudokuGrid sudokuGrid,
			List<Cell> emptyCells) {
		List<Cell> pairs = new ArrayList<>();

		for (Cell aCell : emptyCells) {
			if (aCell.getPossibleEntries().size() == 2) {
				pairs.add(aCell);
			}
		}

		while (pairs.size() != 0) {
			List<Cell> uniquePairs = new ArrayList<>();
			uniquePairs.add(pairs.remove(0));
			Set<Integer> entriesOne = uniquePairs.get(0).getPossibleEntries();

			for (int i = 0; i < pairs.size(); i++) {
				Set<Integer> entriesTwo = pairs.get(i).getPossibleEntries();

				if (entriesOne.containsAll(entriesTwo)) {
					uniquePairs.add(pairs.remove(i));
					i--;
				}
			}

			for (int i = 0; i < uniquePairs.size(); i++) {
				searchRemotePairs(uniquePairs, new ArrayList<>(), i, emptyCells);
			}
		}
	}

	private static void searchRemotePairs(List<Cell> pairs,
			List<Cell> linkedCells, int position, List<Cell> emptyCells) {
		List<Cell> pairsCopy = new ArrayList<>(pairs);
		int row = pairsCopy.get(position).getRow();
		int col = pairsCopy.get(position).getColumn();
		int seg = pairsCopy.get(position).getSegment();

		linkedCells.add(pairsCopy.remove(position));
		compareRemotePairs(linkedCells, emptyCells);

		for (int i = 0; i < pairsCopy.size(); i++) {
			boolean sameRow = row == pairsCopy.get(i).getRow();
			boolean sameCol = col == pairsCopy.get(i).getColumn();
			boolean sameSeg = seg == pairsCopy.get(i).getSegment();

			if (sameRow || sameCol || sameSeg) {
				searchRemotePairs(pairsCopy, linkedCells, i, emptyCells);
			}
		}

		linkedCells.remove(linkedCells.size() - 1);
	}

	private static void compareRemotePairs(List<Cell> linkedCells,
			List<Cell> emptyCells) {
		if (linkedCells.size() % 2 == 0) {
			Cell cellOne = linkedCells.get(0);
			Cell cellTwo = linkedCells.get(linkedCells.size() - 1);

			for (Cell aCell : emptyCells) {
				if (linkedCells.contains(aCell)) {
					continue;
				}

				boolean sameRowOne = aCell.getRow() == cellOne.getRow();
				boolean sameColOne = aCell.getColumn() == cellOne.getColumn();
				boolean sameSegOne = aCell.getSegment() == cellOne.getSegment();

				boolean sameRowTwo = aCell.getRow() == cellTwo.getRow();
				boolean sameColTwo = aCell.getColumn() == cellTwo.getColumn();
				boolean sameSegTwo = aCell.getSegment() == cellTwo.getSegment();

				if (sameRowOne && (sameColTwo || sameSegTwo)) {
					aCell.getPossibleEntries().removeAll(
							cellOne.getPossibleEntries());
				} else if (sameColOne && (sameRowTwo || sameSegTwo)) {
					aCell.getPossibleEntries().removeAll(
							cellOne.getPossibleEntries());
				} else if (sameSegOne && (sameRowTwo || sameColTwo)) {
					aCell.getPossibleEntries().removeAll(
							cellOne.getPossibleEntries());
				}
			}
		}
	}

	public static void checkUniqueRectangle(SudokuGrid sudokuGrid,
			List<Cell> emptyCells) {
		List<Cell> pairs = new ArrayList<>();

		for (Cell aCell : emptyCells) {
			if (aCell.getPossibleEntries().size() == 2) {
				pairs.add(aCell);
			}
		}

		while (pairs.size() != 0) {
			List<Cell> uniquePairs = new ArrayList<>();
			uniquePairs.add(pairs.remove(0));
			Set<Integer> entriesOne = uniquePairs.get(0).getPossibleEntries();

			for (int i = 0; i < pairs.size(); i++) {
				Set<Integer> entriesTwo = pairs.get(i).getPossibleEntries();

				if (entriesOne.containsAll(entriesTwo)) {
					uniquePairs.add(pairs.remove(i));
					i--;
				}
			}

			for (int i = 0; i < uniquePairs.size(); i++) {
				Cell cellOne = uniquePairs.get(i);

				for (int j = i + 1; j < uniquePairs.size(); j++) {
					Cell cellTwo = uniquePairs.get(j);

					searchRectangle(cellOne, cellTwo, emptyCells);
				}
			}

		}
	}

	private static void searchRectangle(Cell cellOne, Cell cellTwo,
			List<Cell> emptyCells) {
		List<Cell> rectangleCells = new ArrayList<>();
		rectangleCells.add(cellOne);
		rectangleCells.add(cellTwo);

		for (int i = 0; i < emptyCells.size(); i++) {
			Cell cellThree = emptyCells.get(i);
			boolean containsEntries = cellThree.getPossibleEntries()
					.containsAll(cellOne.getPossibleEntries());
			int twoEntries = 2;

			if (!containsEntries || rectangleCells.contains(cellThree)) {
				continue;
			}

			Set<Integer> rows = new HashSet<>();
			Set<Integer> cols = new HashSet<>();
			Set<Integer> segs = new HashSet<>();
			Set<Integer> entries = new HashSet<>();
			rectangleCells.add(cellThree);

			for (Cell aCell : rectangleCells) {
				rows.add(aCell.getRow());
				cols.add(aCell.getColumn());
				segs.add(aCell.getSegment());
				entries.addAll(aCell.getPossibleEntries());
			}

			if (cellThree.getPossibleEntries().size() == 2) {
				twoEntries++;
			}

			for (int j = i + 1; j < emptyCells.size(); j++) {
				Cell cellFour = emptyCells.get(j);
				containsEntries = cellFour.getPossibleEntries().containsAll(
						cellOne.getPossibleEntries());
				int twoEntriesNext = twoEntries;

				if (!containsEntries || rectangleCells.contains(cellFour)) {
					continue;
				}

				rectangleCells.add(cellFour);

				rows.add(cellFour.getRow());
				cols.add(cellFour.getColumn());
				segs.add(cellFour.getSegment());
				entries.addAll(cellFour.getPossibleEntries());

				if (cellFour.getPossibleEntries().size() == 2) {
					twoEntriesNext++;
				}

				if (rows.size() == 2 && cols.size() == 2 && segs.size() == 2) {

					for (Cell aCell : rectangleCells) {
						if (aCell.getPossibleEntries().size() > 2
								&& twoEntriesNext == 3) {
							aCell.getPossibleEntries().removeAll(
									cellOne.getPossibleEntries());
						}
					}

					if (twoEntriesNext == 2 && entries.size() == 3) {
						removeOutsideRectangle(rectangleCells, emptyCells);
					}
				}

				rectangleCells.remove(cellFour);
			}

			rectangleCells.remove(cellThree);
		}
	}

	private static void removeOutsideRectangle(List<Cell> rectangleCells,
			List<Cell> emptyCells) {
		Cell cellOne = rectangleCells.get(0);
		Cell cellThree = rectangleCells.get(2);
		Cell cellFour = rectangleCells.get(3);

		Set<Integer> removal = new HashSet<>(cellThree.getPossibleEntries());
		removal.removeAll(cellOne.getPossibleEntries());

		if (cellThree.getRow() == cellFour.getRow()) {
			for (Cell aCell : emptyCells) {
				if (aCell.getRow() == cellThree.getRow()
						&& !rectangleCells.contains(aCell)) {
					aCell.getPossibleEntries().removeAll(removal);
				}
			}
		}

		if (cellThree.getColumn() == cellFour.getColumn()) {
			for (Cell aCell : emptyCells) {
				if (aCell.getColumn() == cellThree.getColumn()
						&& !rectangleCells.contains(aCell)) {
					aCell.getPossibleEntries().removeAll(removal);
				}
			}
		}

		if (cellThree.getSegment() == cellFour.getSegment()) {
			for (Cell aCell : emptyCells) {
				if (aCell.getSegment() == cellThree.getSegment()
						&& !rectangleCells.contains(aCell)) {
					aCell.getPossibleEntries().removeAll(removal);
				}
			}
		}
	}

	private static List<List<Integer>> getPermutation(List<Integer> emptyCells) {
		List<List<Integer>> permutations = new ArrayList<>();

		for (int i = 0; i < emptyCells.size(); i++) {
			List<Integer> list = new ArrayList<>();
			list.add(emptyCells.get(i));
			permutations.add(list);
			permute(list, i, emptyCells, permutations);
		}

		return permutations;
	}

	private static void permute(List<Integer> prefixList, int start,
			List<Integer> emptyCells, List<List<Integer>> permutations) {
		if (start + 1 >= emptyCells.size()) {
			return;
		}

		for (int i = start + 1; i < emptyCells.size(); i++) {
			List<Integer> permutatedList = new ArrayList<>();
			permutatedList.addAll(prefixList);
			permutatedList.add(emptyCells.get(i));
			permutations.add(permutatedList);
			if (i < emptyCells.size() - 1) {
				permute(permutatedList, i, emptyCells, permutations);
			}
		}
	}

	private static List<Cell> getCellsFromId(SudokuGrid sudokuGrid,
			List<Integer> cellIds) {
		List<Cell> cells = new ArrayList<>();

		for (Integer anId : cellIds) {
			cells.add(sudokuGrid.getListGrid().get(anId));
		}

		return cells;
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

	public static int getEntriesCount(List<Cell> cells) {
		int entriesCount = 0;

		for (Cell aCell : cells) {
			entriesCount += aCell.getPossibleEntries().size();
		}

		return entriesCount;
	}
}
