package com.andreas.heimann.sudoku.controller;

import java.util.List;

import com.andreas.heimann.sudoku.items.Cell;

public interface GridListener {
	public List<Cell> getListGrid();

	public void updateGrid(int number, int cellId);
}
