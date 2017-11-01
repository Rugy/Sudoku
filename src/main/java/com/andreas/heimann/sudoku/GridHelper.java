package com.andreas.heimann.sudoku;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.andreas.heimann.sudoku.items.Cell;
import com.andreas.heimann.sudoku.items.SudokuGrid;

public class GridHelper {

	public static final int FIELDSIZE = 9;

	public static void generateGrid(SudokuGrid sudokuGrid) {

		do {
			Cell[][] arrayGrid = new Cell[FIELDSIZE][FIELDSIZE];
			List<Cell> listGrid = new ArrayList<>();
			List<List<Cell>> segmentGrid = new ArrayList<List<Cell>>();
			for (int i = 0; i < FIELDSIZE; i++) {
				segmentGrid.add(new ArrayList<>());
			}

			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					arrayGrid[i][j] = new Cell(0, i + 1, j + 1);
				}
			}

			sudokuGrid.setArrayGrid(arrayGrid);
			sudokuGrid.setListGrid(listGrid);
			sudokuGrid.setSegmentGrid(segmentGrid);

			fillList(arrayGrid, listGrid);
			fillSegment(listGrid, segmentGrid);
		} while (!createRandomIncrementally(sudokuGrid));

		sudokuGrid.setSolutionSteps(new ArrayList<>());
	}

	private static boolean createRandomIncrementally(SudokuGrid sudokuGrid) {
		List<List<Cell>> segmentGrid = sudokuGrid.getSegmentGrid();
		int firstEntryNumber = (int) (Math.random() * 9) + 1;

		for (int entry = firstEntryNumber; entry <= firstEntryNumber + 9; entry++) {
			int actualEntry = (entry % 10) + 1;

			for (int iteration = 0; iteration < 9; iteration++) {
				LinkedList<LinkedList<Cell>> possibleGrid = new LinkedList<>();

				for (int segment = 0; segment < 9; segment++) {
					possibleGrid.add(new LinkedList<>());
					getNeighbouringZeroes(segmentGrid.get(segment));

					for (int i = 0; i < 9; i++) {
						if (segmentGrid.get(segment).get(i)
								.getPossibleEntries().contains(actualEntry)) {
							possibleGrid.get(segment).add(
									segmentGrid.get(segment).get(i));
						}

						possibleGrid
								.get(segment)
								.sort(Comparator
										.comparing(Cell::getAdjacentEmptyCells));

					}
				}

				possibleGrid.sort(Comparator.comparing(LinkedList::size));

				while (possibleGrid.size() > 0
						&& possibleGrid.peek().size() == 0) {
					possibleGrid.poll();
				}

				if (possibleGrid.size() == 0) {
					continue;
				}

				int leastPossibleEntriesCount = 0;
				for (int n = 0; n < possibleGrid.size(); n++) {
					if (possibleGrid.peek().size() == possibleGrid.get(n)
							.size()) {
						leastPossibleEntriesCount++;
					}
				}

				int randomLeastPossibleEntries = (int) (Math.random() * leastPossibleEntriesCount);

				possibleGrid.get(randomLeastPossibleEntries).get(0)
						.setNumber(actualEntry);
				GridSolver.checkExcludeEntries(sudokuGrid,
						GridSolver.getEmptyCells(sudokuGrid));
			}
		}
		if (sudokuGrid.getZeroesCount() == 0) {
			shuffleCells(sudokuGrid);
		}

		return sudokuGrid.getZeroesCount() == 0;
	}

	private static void getNeighbouringZeroes(List<Cell> segmentGrid) {
		for (int i = 0; i < 9; i++) {
			Cell cell = segmentGrid.get(i);
			cell.setAdjacentEmptyCells(0);

			for (int k = 0; k < 9; k++) {
				boolean identicalRow = segmentGrid.get(k).getRow() == cell
						.getRow();
				boolean identicalColumn = segmentGrid.get(k).getColumn() == cell
						.getColumn();
				boolean isZero = segmentGrid.get(k).getNumber() == 0;

				if ((identicalRow || identicalColumn) && isZero) {
					cell.setAdjacentEmptyCells(cell.getAdjacentEmptyCells() + 1);
				}
			}
		}
	}

	public static void generateSimpleGrid(SudokuGrid sudokuGrid) {
		Cell[][] arrayGrid = sudokuGrid.getArrayGrid();

		for (int i = 0; i < FIELDSIZE; i++) {
			for (int j = 0; j < FIELDSIZE; j++) {
				int offsetA = 0;
				int offsetB = 0;
				int row = i + 1;
				int column = j + 1;

				if (i % 3 == 1) {
					offsetA = 3;
				} else if (i % 3 == 2) {
					offsetA = 6;
				}
				if (i < 3) {
					offsetB = 0;
				} else if (i < 6) {
					offsetB = 1;
				} else {
					offsetB = 2;
				}
				arrayGrid[i][j] = new Cell((j + offsetA + offsetB) % 9 + 1,
						row, column);
			}
		}

		shuffleCells(sudokuGrid);
		fillList(arrayGrid, sudokuGrid.getListGrid());
		fillSegment(sudokuGrid.getListGrid(), sudokuGrid.getSegmentGrid());
	}

	public static void generateEmptyGrid(SudokuGrid sudokuGrid) {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				sudokuGrid.getArrayGrid()[i][j] = new Cell(0, i + 1, j + 1);
			}
		}
		fillList(sudokuGrid.getArrayGrid(), sudokuGrid.getListGrid());
		fillSegment(sudokuGrid.getListGrid(), sudokuGrid.getSegmentGrid());
	}

	private static void shuffleCells(SudokuGrid sudokuGrid) {
		Cell[][] arrayGrid = sudokuGrid.getArrayGrid();
		SudokuGrid tempSudoku = sudokuGrid.cloneSudokuGrid();
		Cell[][] tempGrid = tempSudoku.getArrayGrid();

		flipRowsAndColumns(tempGrid);
		shuffleRowBlocks(tempGrid);
		shuffleColumnBlocks(tempGrid);
		shuffleRows(tempGrid);
		shuffleColumns(tempGrid);

		int numberOfSwaps = 200;

		for (int i = 0; i < numberOfSwaps; i++) {
			swapNumbers(tempGrid);
		}

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				arrayGrid[i][j].setNumber(tempGrid[i][j].getNumber());
			}
		}
	}

	private static void shuffleColumns(Cell[][] arrayGrid) {
		Cell[][] tempGrid = new Cell[9][9];
		for (int i = 0; i < 3; i++) {
			LinkedList<Integer> order = getShuffledList(3);

			for (int j = 0; j < 9; j++) {

				for (int k = 0; k < 3; k++) {
					tempGrid[j][i * 3 + k] = arrayGrid[j][i * 3 + order.peek()];
					order.add(order.poll());
				}
			}
		}

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				arrayGrid[i][j] = tempGrid[i][j];
			}
		}
	}

	private static void shuffleRows(Cell[][] arrayGrid) {
		Cell[][] tempGrid = new Cell[9][9];
		for (int i = 0; i < 3; i++) {
			LinkedList<Integer> order = getShuffledList(3);

			for (int j = 0; j < 3; j++) {
				tempGrid[i * 3 + j] = arrayGrid[i * 3 + order.poll()];
			}
		}

		for (int i = 0; i < 9; i++) {
			arrayGrid[i] = tempGrid[i];
		}

	}

	private static void flipRowsAndColumns(Cell[][] arrayGrid) {
		Cell[][] tempGrid = new Cell[9][9];
		int coinflip = (int) (Math.random() * 2);
		if (coinflip == 0) {
			return;
		}

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				tempGrid[i][j] = arrayGrid[j][i];
			}
		}

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				arrayGrid[i][j] = tempGrid[i][j];
			}
		}
	}

	private static void shuffleRowBlocks(Cell[][] arrayGrid) {
		Cell[][] tempGrid = new Cell[9][9];
		LinkedList<Integer> order = getShuffledList(3);

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				tempGrid[i * 3 + j] = arrayGrid[order.peek() * 3 + j];

				if (j == 2) {
					order.poll();
				}
			}
		}

		for (int i = 0; i < 9; i++) {
			arrayGrid[i] = tempGrid[i];
		}
	}

	private static void shuffleColumnBlocks(Cell[][] arrayGrid) {
		Cell[][] tempGrid = new Cell[9][9];
		LinkedList<Integer> order = getShuffledList(3);

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 3; j++) {
				for (int k = 0; k < 3; k++) {
					tempGrid[i][j * 3 + k] = arrayGrid[i][order.peek() * 3 + k];

					if (k == 2) {
						order.add(order.poll());
					}

				}
			}
		}

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				arrayGrid[i][j] = tempGrid[i][j];
			}
		}
	}

	private static void swapNumbers(Cell[][] arrayGrid) {
		int firstNumber = (int) ((Math.random() * 9) + 1);
		int secondNumber;
		do {
			secondNumber = (int) ((Math.random() * 9) + 1);
		} while (firstNumber == secondNumber);

		for (int i = 0; i < FIELDSIZE; i++) {
			for (int j = 0; j < FIELDSIZE; j++) {
				if (arrayGrid[i][j].getNumber() == firstNumber) {
					arrayGrid[i][j].setNumber(secondNumber);
				} else if (arrayGrid[i][j].getNumber() == secondNumber) {
					arrayGrid[i][j].setNumber(firstNumber);
				}
			}
		}

	}

	public static LinkedList<Integer> getShuffledList(int amount) {
		LinkedList<Integer> list = new LinkedList<>();
		for (int i = 0; i < amount; i++) {
			list.add(i);
		}
		Collections.shuffle(list);

		return list;
	}

	public static void importGrid(SudokuGrid sudokuGrid) {
		Cell[][] arrayGrid = sudokuGrid.getArrayGrid();

		String gridString = "..24.16......8....9..7.2..37.5...3.6.8.....9.2.9...7.46..3.7..5....1......78.49..";

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				int row = i + 1;
				int column = j + 1;

				if (gridString.charAt(i * 9 + j) == '.') {
					arrayGrid[i][j] = new Cell(10, row, column);
					arrayGrid[i][j].clearCell();
				} else {
					arrayGrid[i][j] = new Cell(Integer.valueOf(gridString
							.charAt(i * 9 + j) - 48), row, column);
				}
			}
		}

		fillList(arrayGrid, sudokuGrid.getListGrid());
		fillSegment(sudokuGrid.getListGrid(), sudokuGrid.getSegmentGrid());
	}

	public static void fillList(Cell[][] arrayGrid, List<Cell> listGrid) {
		for (int i = 0; i < FIELDSIZE; i++) {
			for (int j = 0; j < FIELDSIZE; j++) {
				listGrid.add(arrayGrid[i][j]);
			}
		}
	}

	public static void fillArray(Cell[][] arrayGrid, List<Cell> listGrid) {
		for (int i = 0; i < FIELDSIZE; i++) {
			for (int j = 0; j < FIELDSIZE; j++) {
				arrayGrid[i][j] = listGrid.get(i * 9 + j);
			}
		}
	}

	public static void fillSegment(List<Cell> listGrid,
			List<List<Cell>> segmentGrid) {
		for (int m = 0; m < 3; m++) {
			int offset = 0;

			for (int l = 0; l < 3; l++) {
				for (int j = 0; j < 3; j++) {
					for (int k = 0; k < 3; k++) {
						segmentGrid.get(m * 3 + l).add(
								listGrid.get(j * 9 + offset + k + m * 27));
						listGrid.get(j * 9 + offset + k + m * 27).setSegment(
								m * 3 + l + 1);
						;
					}
				}

				offset += 3;
			}

		}
	}

	public static void printGrid(Cell[][] arrayGrid) {
		System.out.println("====STARTING GRID====");
		for (int i = 0; i < FIELDSIZE; i++) {
			if (i % 3 == 0 && i > 2) {
				System.out.println("---------------------");
			}

			for (int j = 0; j < FIELDSIZE; j++) {
				if (j % 3 == 0 && j > 2) {
					System.out.print("| ");
				}
				System.out.print(arrayGrid[i][j].getNumber() + " ");
			}

			System.out.println();
		}
		System.out.println("====FINISHED GRID====");
	}

	public static void printPossibleEntriesCount(Cell[][] arrayGrid) {
		System.out.println("====STARTING GRID====");
		for (int i = 0; i < FIELDSIZE; i++) {
			if (i % 3 == 0 && i > 2) {
				System.out.println("---------------------");
			}

			for (int j = 0; j < FIELDSIZE; j++) {
				if (j % 3 == 0 && j > 2) {
					System.out.print("| ");
				}
				System.out.print(arrayGrid[i][j].getPossibleEntries().size()
						+ " ");
			}

			System.out.println();
		}
		System.out.println("====FINISHED GRID====");
	}

	public static void printGrid(List<List<Cell>> segmentGrid) {
		System.out.println("==STARTING GRID==");
		for (int i = 0; i < FIELDSIZE; i++) {
			System.out.println("===NEW BLOCK:" + (i + 1) + "===");
			for (int j = 0; j < 3; j++) {
				for (int k = 0; k < 3; k++) {
					System.out.print(segmentGrid.get(i).get(j * 3 + k)
							.getNumber()
							+ " ");
				}
				System.out.println();
			}
		}
		System.out.println("==FINISHED GRID==");
	}

}
