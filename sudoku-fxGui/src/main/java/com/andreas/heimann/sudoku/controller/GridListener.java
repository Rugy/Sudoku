package com.andreas.heimann.sudoku.controller;

import java.util.List;

import com.andreas.heimann.sudoku.items.Cell;

public interface GridListener {
	public List<Cell> getListGridCopy();

	public void makeEntry(int number, int cellId);

	public void removePossibleEntry(int number, int cellId);

	public void addPossibleEntry(int number, int cellId);

	public void checkExcludeEntries();

	public void solveGrid();
}
