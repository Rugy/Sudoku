package com.andreas.heimann.sudoku.gui.items;

public interface CellListener {
	public void makeEntry(int number, int cellId);

	public void removeEntryOption(int number, int cellId);

	public void addEntryOption(int number, int cellId);

	public void removeEntry(int cellId);
}
