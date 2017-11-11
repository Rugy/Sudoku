package com.andreas.heimann.sudoku.gui;

public interface CellListener {
	public void updateValue(int number, int cellId);

	public void removeEntryOption(int number, int cellId);
}
