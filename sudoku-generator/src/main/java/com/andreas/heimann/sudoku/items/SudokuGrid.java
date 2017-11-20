package com.andreas.heimann.sudoku.items;

import java.util.ArrayList;
import java.util.List;

public class SudokuGrid {

	private static final int FIELDSIZE = 9;
	private Cell[][] arrayGrid;
	private List<Cell> listGrid;
	private List<List<Cell>> segmentGrid;
	private Difficulty difficulty;
	private int zeroesCount;
	private List<SolutionStep> solutionSteps;

	public SudokuGrid() {
		arrayGrid = new Cell[FIELDSIZE][FIELDSIZE];
		listGrid = new ArrayList<>();
		segmentGrid = new ArrayList<List<Cell>>();
		for (int i = 0; i < FIELDSIZE; i++) {
			segmentGrid.add(new ArrayList<>());
		}
		difficulty = Difficulty.ONE;
		zeroesCount = 0;
		solutionSteps = new ArrayList<>();
	}

	public Cell[][] getArrayGrid() {
		return arrayGrid;
	}

	public void setArrayGrid(Cell[][] arrayGrid) {
		this.arrayGrid = arrayGrid;
	}

	public List<Cell> getListGrid() {
		return listGrid;
	}

	public void setListGrid(List<Cell> listGrid) {
		this.listGrid = listGrid;
	}

	public List<List<Cell>> getSegmentGrid() {
		return segmentGrid;
	}

	public void setSegmentGrid(List<List<Cell>> segmentGrid) {
		this.segmentGrid = segmentGrid;
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
	}

	public int getZeroesCount() {
		zeroesCount = 0;

		for (Cell aCell : listGrid) {
			if (aCell.getNumber() == 0) {
				zeroesCount++;
			}
		}

		return zeroesCount;
	}

	public void setZeroesCount(int zeroesCount) {
		this.zeroesCount = zeroesCount;
	}

	public List<SolutionStep> getSolutionSteps() {
		return solutionSteps;
	}

	public void setSolutionSteps(List<SolutionStep> solutionSteps) {
		this.solutionSteps = solutionSteps;
	}

	public Cell getCellFromId(Integer cellId) {
		return listGrid.get(cellId);
	}

	public SudokuGrid cloneSudokuGrid() {
		SudokuGrid sudokuGrid = new SudokuGrid();
		Cell[][] arrayGrid = new Cell[FIELDSIZE][FIELDSIZE];
		List<Cell> listGrid = new ArrayList<>();
		List<List<Cell>> segmentGrid = new ArrayList<>();
		for (int i = 0; i < 9; i++) {
			segmentGrid.add(new ArrayList<>());
		}
		List<SolutionStep> solutionSteps = new ArrayList<>();

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				arrayGrid[i][j] = this.arrayGrid[i][j].copyCell();
			}
		}
		sudokuGrid.setArrayGrid(arrayGrid);

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				listGrid.add(arrayGrid[i][j]);
			}
		}
		sudokuGrid.setListGrid(listGrid);

		for (int m = 0; m < 3; m++) {
			int offset = 0;

			for (int l = 0; l < 3; l++) {
				for (int j = 0; j < 3; j++) {
					for (int k = 0; k < 3; k++) {
						segmentGrid.get(m * 3 + l).add(
								listGrid.get(j * 9 + offset + k + m * 27));
						listGrid.get(j * 9 + offset + k + m * 27).setSegment(
								m * 3 + l);
					}
				}

				offset += 3;
			}

		}
		sudokuGrid.setSegmentGrid(segmentGrid);
		sudokuGrid.setDifficulty(difficulty);
		sudokuGrid.setZeroesCount(zeroesCount);

		for (int i = 0; i < this.solutionSteps.size(); i++) {
			SolutionStep step = this.solutionSteps.get(i);

			Cell cell = step.getCell();
			StepType stepType = step.getStepType();
			int number = step.getEntry();

			solutionSteps.add(new SolutionStep(cell, stepType, number));
		}
		sudokuGrid.setSolutionSteps(solutionSteps);

		return sudokuGrid;
	}
}
