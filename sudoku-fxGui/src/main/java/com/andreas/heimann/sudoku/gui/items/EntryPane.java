package com.andreas.heimann.sudoku.gui.items;

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

		setMinSize(size, size);
		setMaxSize(size, size);
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

	public EntryPane(int size, int entry, int row, int column, int segment) {
		super();
		this.row = row;
		this.column = column;
		this.segment = segment;

		setMinSize(size, size);
		setMaxSize(size, size);
		setAlignment(Pos.CENTER);

		Label label = new Label(String.valueOf(entry));
		label.setMinSize(size, size);
		label.setMaxSize(size, size);
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

		Label label = new Label(String.valueOf(number));
		label.setAlignment(Pos.CENTER);
		label.setMaxSize(size, size);
		label.setMinSize(size, size);
		label.getStyleClass().add("entry");
		label.setOnMouseClicked(e -> {
			if (e.getButton() == MouseButton.SECONDARY) {
				cellListener.removeEntry(row * 9 + column);
			}
		});

		add(label, 0, 0);
	}

	public void changePossibleEntries(Set<Integer> possibleEntries) {
		if (getChildren().size() != 9) {
			return;
		}

		for (int i = 0; i < 9; i++) {
			Label label = (Label) getChildren().get(i);
			String entry;

			if (possibleEntries.contains(i + 1)) {
				entry = String.valueOf(i + 1);
			} else {
				entry = "";
			}

			label.setText(entry);
		}
	}

	public void changeEntry(int number) {
		if (getChildren().size() != 1) {
			return;
		}

		Label label = (Label) getChildren().get(0);
		label.setText(String.valueOf(number));
	}

	private void addMouseClick(Label label) {
		label.setOnMouseClicked(e -> {
			Label clickedLabel = (Label) e.getSource();
			String numberString = clickedLabel.getText();
			boolean isLeftClick = e.getButton() == MouseButton.PRIMARY;
			boolean isRightClick = e.getButton() == MouseButton.SECONDARY;
			int cellId = row * 9 + column;
			int labelPosition = getChildren().indexOf(clickedLabel);

			if ("".equals(numberString) && !isLeftClick) {
				return;
			} else if ("".equals(numberString) && isLeftClick) {
				cellListener.addEntryOption(labelPosition + 1, cellId);
			} else if (isLeftClick) {
				int number = Integer.valueOf(numberString);
				cellListener.makeEntry(number, cellId);
			} else if (isRightClick) {
				int number = Integer.valueOf(numberString);
				cellListener.removeEntryOption(number, cellId);
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
