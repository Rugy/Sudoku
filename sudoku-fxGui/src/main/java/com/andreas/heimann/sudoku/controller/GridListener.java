package com.andreas.heimann.sudoku.controller;

import java.util.List;

import com.andreas.heimann.sudoku.items.Cell;

public interface GridListener {
	public List<Cell> getListGridCopy();

	public List<Cell> getSolvedGridCopy();

	public void makeEntry(int number, int cellId);

	public void removePossibleEntry(int number, int cellId);

	public void addPossibleEntry(int number, int cellId);

	public void checkExcludeEntries();

	public void checkUniqueEntries();

	public void checkUniqueRowColumn();

	public void checkEntryCombinations();

	public void solveGrid();
}
