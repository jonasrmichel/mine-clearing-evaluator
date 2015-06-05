package com.jonas.evaluator;

import java.util.ArrayList;
import java.util.List;

/**
 * This class holds the instructions that drive the actions of the mine clearing
 * vessel at each simulation step. The script's data is provided by an input
 * script file.
 * 
 * @author Jonas Michel, jonas.r.michel@gmail.com
 * 
 */
public class Script extends InputFileModel {
	/** Holds the in-order script instructions. */
	private List<StepInstructions> instructions;

	public Script(String scriptFile) {
		super(scriptFile);

		// Note: the instructions list will be initialized during parsing
	}

	@Override
	public void processLine(String line) {
		Logger.printDebug(Script.class, "processing line " + line);

		// tokenize and parse the line
		String[] lineInstructions = line.trim().toLowerCase().split("\\s+");
		parseLine(lineInstructions);
	}

	@Override
	public void validate() {
		Logger.printDebug(Script.class, "Validating script model");

		if (instructions == null
				|| (instructions != null && instructions.isEmpty()))
			Logger.printErrorAndExit(Field.class,
					"Please provide a non-empty script file");

		for (StepInstructions instruction : instructions)
			Logger.printDebug(Script.class, instruction.toString());
	}

	/**
	 * Returns the instructions for a particular step.
	 * 
	 * @param step
	 *            the step number
	 * @return the step's instruction
	 */
	public StepInstructions getInstructions(int step) {
		return instructions.get(step - 1);
	}

	public int getNumInstructions() {
		return instructions == null ? 0 : instructions.size();
	}

	/**
	 * Parses a single line of a script file.
	 * 
	 * @param lineInstructions
	 *            move and/or firing instructions for a single simulation step
	 */
	private void parseLine(String[] lineInstructions) {
		// extract the optional firing pattern and move instructions
		StepInstructions stepInstrunctions = new StepInstructions();
		for (int i = 0; i < lineInstructions.length; i++)
			stepInstrunctions.addInstruction(lineInstructions[i].trim());

		// store the parsed instruction pair
		if (instructions == null)
			instructions = new ArrayList<StepInstructions>();

		if (stepInstrunctions.getInstructions().size() > 0)
			instructions.add(stepInstrunctions);
	}
}
