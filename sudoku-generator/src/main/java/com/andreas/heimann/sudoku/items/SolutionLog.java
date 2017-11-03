package com.andreas.heimann.sudoku.items;

import java.util.ArrayList;
import java.util.List;

public class SolutionLog {

	List<SolutionStep> solutionSteps;

	public SolutionLog() {
		solutionSteps = new ArrayList<>();
	}

	public List<SolutionStep> getSolutionSteps() {
		return solutionSteps;
	}

	public void setSolutionSteps(List<SolutionStep> solutionSteps) {
		this.solutionSteps = solutionSteps;
	}

	public void addSolutionStep(SolutionStep solutionStep) {
		solutionSteps.add(solutionStep);
	}

}
