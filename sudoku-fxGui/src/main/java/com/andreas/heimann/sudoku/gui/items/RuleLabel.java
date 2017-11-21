package com.andreas.heimann.sudoku.gui.items;

import javafx.geometry.Pos;
import javafx.scene.control.Label;

import com.andreas.heimann.sudoku.items.RuleType;

public class RuleLabel extends Label {

	private RuleType ruleType;
	private static final int MIN_SIZE = 50;

	public RuleLabel(RuleType ruleType) {
		super();

		this.ruleType = ruleType;
		setMinSize(MIN_SIZE, MIN_SIZE);
		setAlignment(Pos.CENTER);
	}

	public RuleLabel(String text, RuleType ruleType) {
		super(text);

		this.ruleType = ruleType;
		setMinSize(MIN_SIZE, MIN_SIZE);
		setAlignment(Pos.CENTER);
	}

	public RuleType getRuleType() {
		return ruleType;
	}

	public void setRuleType(RuleType ruleType) {
		this.ruleType = ruleType;
	}

}
