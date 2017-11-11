package com.andreas.heimann.sudoku.gui;

import java.util.Set;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;

public class EntryPane extends GridPane {

	public static final String COLOR_NEUTRAL = "#F0F8FF";
	public static final String COLOR_HOVER = "#ADD8E6";
	public static final String COLOR_HOVER_ADJACENT = "#CFFCFC";

	private int row;
	private int column;
	private int segment;

	private CellListener cellListener;

	public EntryPane(int size, Set<Integer> possibleEntries, int row,
			int column, int segment) {
		super();
		this.row = row;
		this.column = column;
		this.segment = segment;

		setMaxSize(size, size);
		setMinSize(size, size);
		setAlignment(Pos.CENTER);

		for (int i = 0; i < 9; i++) {
			String entry;
			if (possibleEntries.contains(i)) {
				entry = String.valueOf(i);
			} else {
				entry = "";
			}
			Label label = new Label(entry);
			label.setMinSize(size / 3 - 2, size / 3 - 2);
			label.setAlignment(Pos.CENTER);
			label.getStyleClass().add("entry-option");
			addMouseClick(label);
			add(label, i % 3, i / 3);
		}
	}

	public EntryPane(int entry, int row, int column, int segment) {
		super();
		this.row = row;
		this.column = column;
		this.segment = segment;

		Label label = new Label(String.valueOf(entry));
		label.setMinSize(60, 60);
		label.setAlignment(Pos.CENTER);
		label.getStyleClass().add("entry");
		add(label, 0, 0);
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

	public void changeToPossibleEntryPane(Set<Integer> possibleEntries, int size) {
		getChildren().removeAll(getChildren());

		setMaxSize(size, size);
		setMinSize(size, size);
		setAlignment(Pos.CENTER);

		for (int i = 0; i < 9; i++) {
			String entry;
			if (possibleEntries.contains(i + 1)) {
				entry = String.valueOf(i + 1);
			} else {
				entry = "";
			}
			Label label = new Label(entry);
			label.setMinSize(size / 3 - 2, size / 3 - 2);
			label.setAlignment(Pos.CENTER);
			label.getStyleClass().add("entry-option");
			addMouseClick(label);
			add(label, i % 3, i / 3);
		}
	}

	public void changeToEntryPane(int number, int size) {
		getChildren().removeAll(getChildren());

		setMaxSize(size, size);
		setMinSize(size, size);
		setAlignment(Pos.CENTER);

		Label label = new Label(String.valueOf(number));
		label.setMinSize(60, 60);
		label.setAlignment(Pos.CENTER);
		label.getStyleClass().add("entry");
		add(label, 0, 0);
	}

	private void addMouseClick(Label label) {
		label.setOnMouseClicked(e -> {
			String numberString = ((Label) e.getSource()).getText();

			if ("".equals(numberString)) {
				return;
			} else if (e.getButton() == MouseButton.PRIMARY) {
				int number = Integer.valueOf(numberString);
				cellListener.updateValue(number, row * 9 + column);
			} else if (e.getButton() == MouseButton.SECONDARY) {
				int number = Integer.valueOf(numberString);
				cellListener.removeEntryOption(number, row * 9 + column);
			}
		});
	}

	public CellListener getCellListener() {
		return cellListener;
	}

	public void setCellListener(CellListener cellListener) {
		this.cellListener = cellListener;
	}
}
