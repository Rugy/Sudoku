package com.andreas.heimann.sudoku.gui;

import javafx.scene.control.Label;

public class CellLabel extends Label {

	private int row;
	private int column;
	private int segment;
	public static final String COLOR_NEUTRAL = "#F0F8FF";
	public static final String COLOR_HOVER = "#ADD8E6";
	public static final String COLOR_HOVER_ADJACENT = "#CFFCFC";

	public CellLabel(String text, int row, int column, int segment) {
		super(text);
		this.row = row;
		this.column = column;
		this.segment = segment;
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

}
