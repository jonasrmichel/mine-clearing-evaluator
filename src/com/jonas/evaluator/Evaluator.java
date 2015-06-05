package com.jonas.evaluator;

/**
 * This class is the entry point for the mine clearing evaluator program. Usage:
 * $ java Evaluator <field-file> <script-file>
 * 
 * The evaluator simulates the execution of the input script file on the input
 * field file and scores the script's mine clearing performance.
 * 
 * @author Jonas Michel, jonas.r.michel@gmail.com
 * 
 */
public class Evaluator {
	/** Holds the state of the field cuboid. */
	private Field field;

	/** Holds the instructions of a mine clearing script. */
	private Script script;

	/** The current simulation step. */
	int step;

	/** Holds the current position of the script-instructed vessel. */
	private Position vessel;

	/** Stats for the scoring function. */
	private int initialMines = 0, kmsMoved = 0, volleysFired = 0;

	public Evaluator(String fieldFile, String scriptFile) {
		field = new Field(fieldFile);
		script = new Script(scriptFile);
	}

	/**
	 * Simulates the actions of a mine clearing vessel driven by the
	 * instructions in the input script file as it falls through the cuboid
	 * space defined by the input field file.
	 */
	public void evaluate() {
		// initializations
		initialMines = field.getNumMines();
		step = 1;
		vessel = field.getCenter();
		vessel.setZ(Settings.STARTING_DEPTH);

		while (!completed()) {
			printPreStep();
			performStep();
			printPostStep();

			// increment step count
			step++;
		}

		// score the script's mine clearing performance
		printScore();
	}

	/**
	 * Determines whether the simulation is complete. An simulation is over when
	 * (a) all mines are cleared; (b) the script is completed; or (c) the vessel
	 * passed a mine.
	 * 
	 * @return whether or not the simulation is over
	 */
	public boolean completed() {
		return field.getNumMines() == 0 // no mines remaining
				|| step > script.getNumInstructions() // script completed
				|| field.minesAbove(vessel.getZ()); // passed mines
	}

	/**
	 * Prints the state of the field prior to executing the current step's
	 * instruction.
	 */
	public void printPreStep() {
		// print the step number
		System.out.println("Step " + step
				+ System.getProperty("line.separator"));

		Logger.printDebug(Evaluator.class, vessel.toString());

		// print the state of the field
		System.out.println(field.toString(vessel));
	}

	/**
	 * Prints the state of the field after executing the current step's
	 * instruction.
	 */
	public void printPostStep() {
		// print the instruction executed at this step
		System.out.println(script.getInstructions(step)
				+ System.getProperty("line.separator"));

		Logger.printDebug(Evaluator.class, vessel.toString());

		// print the state of the field
		System.out.println(field.toString(vessel));
	}

	/**
	 * Simulates the execution of the current step's instruction on the mine
	 * field.
	 */
	public void performStep() {
		StepInstructions stepInstrunctions = script.getInstructions(step);
		for (String instruction : stepInstrunctions.getInstructions()) {
			if (StepInstructions.isFiringPattern(instruction))
				performFiringPattern(instruction); // fire
			else
				performMove(instruction); // move
		}

		// dive!
		vessel.translate(new Position(0, 0, Settings.DIVE_RATE));
	}

	/**
	 * Destroys any active mines at any of the firing pattern's xy-coordinates.
	 * 
	 * @param pattern
	 *            a firing pattern instruction
	 */
	private void performFiringPattern(String pattern) {
		for (Position position : StepInstructions.FIRING_PATTERN_MAP
				.get(pattern))
			field.destroyMines(vessel.relativePosition(position));

		volleysFired++;
	}

	/**
	 * Translates the vessel's xy-coordinates per a move instruction.
	 * 
	 * @param move
	 *            a move instruction
	 */
	private void performMove(String move) {
		Position translation = StepInstructions.MOVE_MAP.get(move);
		vessel.translate(Util.scale(translation, Settings.MOVE_RATE));

		kmsMoved += Settings.MOVE_RATE;
	}

	/**
	 * Prints the score of the script's execution performance.
	 */
	public void printScore() {
		int score = calculateScore();
		if (score > 0)
			System.out.println("pass (" + score + ")");
		else
			System.out.println("fail (0)");
	}

	/**
	 * Calculates a score indicating the script's execution performance.
	 * 
	 * @return the script's score
	 */
	public int calculateScore() {
		int score = 0;

		if (field.getNumMines() > 0) {
			// mine(s) passed or mine(s) remaining
			score = 0;

		} else if ((step - 1) < script.getNumInstructions()) {
			// mines cleared, but script instructions remaining
			score = 1;

		} else {
			// mines cleared, no script instructions remaining
			score = 10 * initialMines
					- Math.min(5 * volleysFired, 5 * initialMines)
					- Math.min(2 * kmsMoved, 3 * initialMines);
		}

		return score;
	}

	public static void main(String[] args) {
		// validate command line arguments
		if (args.length != 2) {
			Logger.printHelp();
			Logger.printErrorAndExit(Evaluator.class,
					"Invalid number of command line arguments");
		}

		// kick off evaluation
		new Evaluator(args[0], args[1]).evaluate();
	}

}
