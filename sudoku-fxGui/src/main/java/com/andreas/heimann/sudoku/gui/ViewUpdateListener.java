package com.andreas.heimann.sudoku.gui;

import java.util.Set;

public interface ViewUpdateListener {
	public void updateGrid();

	public void updateCell(int cellId, Set<Integer> possibleEntries);

	public void updateCell(int cellId, int number);

	public void showWrongEntry(int cellId);

	public void updateExcludes(int count);
}
