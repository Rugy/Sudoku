package com.andreas.heimann.sudoku.items;

import java.util.HashSet;
import java.util.Set;

public class Cell {

	private int number;
	private Set<Integer> entries = new HashSet<>();
	private int row;
	private int column;
	private int segment;
	private int adjacentEmptyCells;
	private int gridNumber;

	public Cell(int number, int row, int column) {
		this.number = number;
		this.row = row;
		this.column = column;
		gridNumber = row * 9 + column;

		if (number == 0) {
			clearCell();
		}
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
		entries.clear();
	}

	public Set<Integer> getEntries() {
		return entries;
	}

	public void setEntries(Set<Integer> entries) {
		this.entries = entries;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getSegment() {
		return segment;
	}

	public void setSegment(int segment) {
		this.segment = segment;
	}

	public int getAdjacentEmptyCells() {
		return adjacentEmptyCells;
	}

	public void setAdjacentEmptyCells(int adjacentEmptyCells) {
		this.adjacentEmptyCells = adjacentEmptyCells;
	}

	public int getGridNumber() {
		return gridNumber;
	}

	public void setGridNumber(int gridNumber) {
		this.gridNumber = gridNumber;
	}

	public void clearCell() {
		number = 0;
		for (int i = 0; i < 9; i++) {
			entries.add(new Integer(i + 1));
		}
	}

	public boolean deletePossibleEntry(Integer entry) {
		return entries.remove(entry);
	}

	public Cell copyCell() {
		Cell copiedCell = new Cell(number, row, column);
		copiedCell.setAdjacentEmptyCells(adjacentEmptyCells);
		copiedCell.setGridNumber(gridNumber);
		copiedCell.setSegment(segment);
		Set<Integer> entriesCopy = new HashSet<>(entries);
		copiedCell.setEntries(entriesCopy);

		return copiedCell;
	}

	@Override
	public String toString() {
		return "Cell: " + row + "," + column + " in segment " + segment;
	}

}
