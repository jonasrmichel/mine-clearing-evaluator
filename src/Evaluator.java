public class Evaluator {
	/** Holds the state of the field cuboid. */
	private Field field;

	/** Holds the instructions of a mine clearing script. */
	private Script script;

	/** The current simulation step. */
	int step;

	/** Holds the current position of the script-controlled vessel. */
	private Position vessel;

	/** Counts for scoring function. */
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
				|| field.getNumMinesAbove(vessel.getZ()) > 0; // passed mines
	}

	/**
	 * Prints the state of the field prior to executing the current step's
	 * instruction.
	 */
	public void printPreStep() {
		// print the step number
		System.out.println("Step " + step);
		System.out.println();

		// print the state of the field
		System.out.println(field.toString(vessel));
		System.out.println();
	}

	/**
	 * Prints the state of the field after executing the current step's
	 * instruction.
	 */
	public void printPostStep() {
		// print the instruction executed at this step
		System.out.println(script.getInstruction(step));
		System.out.println();

		// print the state of the field
		System.out.println(field.toString(vessel));
		System.out.println();
	}

	/**
	 * Simulations the execution of the current step's instruction on the mine
	 * field.
	 */
	public void performStep() {
		// TODO Auto-generated method stub

		// update depth, kmsMoved, volleysFired
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