package com.andreas.heimann.sudoku.items;

import java.util.List;
import java.util.Set;

public class SolutionStep {

	private Cell cell;
	private StepType stepType;
	private String description;
	private int entry;
	private int uniqueField;
	private int segment;
	private Set<Integer> entries;
	private List<Cell> reason;

	public SolutionStep(Cell cell, StepType stepType, int entry) {
		this.cell = cell;
		this.stepType = stepType;
		this.entry = entry;

		if (stepType == StepType.EXCLUDE_ROW
				|| stepType == StepType.EXCLUDE_COLUMN
				|| stepType == StepType.EXCLUDE_SEGMENT) {
			description = "Number " + entry + " already in "
					+ stepType.toString().substring(8).toLowerCase()
					+ ". Removed from Cell(" + cell.getRow() + ","
					+ cell.getColumn() + ")";
		}
	}

	public SolutionStep(Cell cell, StepType stepType, int entry, int uniqueField) {
		this.cell = cell;
		this.stepType = stepType;
		this.entry = entry;
		this.uniqueField = uniqueField;
		String fieldDescription = stepType.toString().substring(7)
				.toLowerCase();

		if (stepType == StepType.UNIQUE_ROW
				|| stepType == StepType.UNIQUE_COLUMN
				|| stepType == StepType.UNIQUE_SEGMENT) {
			description = "Number " + entry + " is the only option in "
					+ fieldDescription + " " + uniqueField
					+ ". Removed from Cells around Cell(" + cell.getRow() + ","
					+ cell.getColumn() + ")";
		}
	}

	public SolutionStep(Cell cell, StepType stepType, int entry,
			int uniqueField, int segment) {
		this.cell = cell;
		this.stepType = stepType;
		this.entry = entry;
		this.uniqueField = uniqueField;
		this.segment = segment;
		String fieldDescription = stepType.toString().substring(14)
				.toLowerCase();

		if (stepType == StepType.UNIQUE_SEGMENTROW
				|| stepType == StepType.UNIQUE_SEGMENTCOLUMN) {
			description = "Number " + entry + " has to be in "
					+ fieldDescription + " " + uniqueField + " in segment "
					+ segment + ". Removed from Cell in same "
					+ fieldDescription + " Cell(" + cell.getRow() + ","
					+ cell.getColumn() + ")";
		}
	}

	public SolutionStep(Cell cell, StepType stepType, int uniqueField,
			Set<Integer> entries, List<Cell> cells) {
		this.cell = cell;
		this.stepType = stepType;
		this.uniqueField = uniqueField;
		this.entries = entries;
		this.reason = cells;
		String fieldDescription = stepType.toString().substring(17)
				.toLowerCase();
		StringBuilder cellDescription = new StringBuilder();
		for (Cell aCell : cells) {
			cellDescription.append("(" + aCell.getRow() + ","
					+ aCell.getColumn() + ")");
		}

		if (stepType == StepType.ENTRYCOMBINATION_ROW
				|| stepType == StepType.ENTRYCOMBINATION_COLUMN
				|| stepType == StepType.ENTRYCOMBINATION_SEGMENT) {
			description = "Numbers " + entries + " have to be in "
					+ fieldDescription + " " + uniqueField + " in Cells: "
					+ cellDescription + ". Removed from Cell(" + cell.getRow()
					+ "," + cell.getColumn() + ")";
		}
	}

	public SolutionStep(Cell cell, int entry, List<Cell> reason) {
		this.cell = cell;
		this.entry = entry;
		this.reason = reason;
	}

	public Cell getCell() {
		return cell;
	}

	public void setCell(Cell cell) {
		this.cell = cell;
	}

	public StepType getStepType() {
		return stepType;
	}

	public void setStepType(StepType stepType) {
		this.stepType = stepType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getEntry() {
		return entry;
	}

	public void setEntry(int entry) {
		this.entry = entry;
	}

	public int getUniqueField() {
		return uniqueField;
	}

	public void setUniqueField(int uniqueField) {
		this.uniqueField = uniqueField;
	}

	public int getSegment() {
		return segment;
	}

	public void setSegment(int segment) {
		this.segment = segment;
	}

	public Set<Integer> getEntries() {
		return entries;
	}

	public void setEntries(Set<Integer> entries) {
		this.entries = entries;
	}

	public List<Cell> getReason() {
		return reason;
	}

	public void setReason(List<Cell> reason) {
		this.reason = reason;
	}

}