import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Script extends InputFile {

	/** Holds mappings from firing pattern names to offest positions. */
	public static final Map<String, Set<Position>> FIRING_PATTERN_MAP;
	static {
		Map<String, Set<Position>> fpMap = new HashMap<String, Set<Position>>();
		fpMap.put("alpha", new HashSet<Position>() {
			{
				add(new Position(-1, -1));
				add(new Position(-1, 1));
				add(new Position(1, -1));
				add(new Position(1, 1));
			}
		});
		fpMap.put("beta", new HashSet<Position>() {
			{
				add(new Position(-1, 0));
				add(new Position(0, -1));
				add(new Position(0, 1));
				add(new Position(1, 0));
			}
		});
		fpMap.put("gamma", new HashSet<Position>() {
			{
				add(new Position(-1, 0));
				add(new Position(0, 0));
				add(new Position(1, 0));
			}
		});
		fpMap.put("delta", new HashSet<Position>() {
			{
				add(new Position(0, -1));
				add(new Position(0, 0));
				add(new Position(0, 1));
			}
		});

		FIRING_PATTERN_MAP = Collections.unmodifiableMap(fpMap);
	}

	/** Holds mappings from move names to a position translation. */
	public static final Map<String, Position> MOVE_MAP;
	static {
		Map<String, Position> mMap = new HashMap<String, Position>();
		mMap.put("north", new Position(0, 1));
		mMap.put("south", new Position(0, -1));
		mMap.put("east", new Position(1, 0));
		mMap.put("west", new Position(-1, 0));

		MOVE_MAP = Collections.unmodifiableMap(mMap);
	}

	/** Holds the in-order script instructions. */
	private List<InstructionPair> instructions;

	public Script(String scriptFile) {
		super(scriptFile);

		// Note: the instructions list will be initialized during parsing
	}

	@Override
	public void processLine(String line) {
		Logger.printDebug(Script.class, "processing line " + line);

		// tokenize and parse the line
		String[] lineTokens = line.trim().toLowerCase().split("\\s+");
		parseLine(lineTokens);
	}

	@Override
	public void validate() {
		Logger.printDebug(Script.class, "Validating script model");

		if (instructions.isEmpty())
			Logger.printErrorAndExit(Script.class,
					"Please provide a non-empty script file");
	}

	/**
	 * Returns the instruction for a particular step.
	 * 
	 * @param step
	 *            the step number
	 * @return the step's instruction
	 */
	public InstructionPair getInstruction(int step) {
		return instructions.get(step - 1);
	}

	public int getNumInstructions() {
		return instructions.size();
	}

	/**
	 * Parses a single line of a script file.
	 * 
	 * @param lineTokens
	 *            move and/or firing instructions for a single simulation step
	 */
	private void parseLine(String[] lineTokens) {
		// validate the instruction line
		if (lineTokens.length > 2)
			Logger.printErrorAndExit(
					Script.class,
					"Invalid number of script instructions at line "
							+ Integer.toString(instructions.size() + 1)
							+ " in script file");

		// extract the optional firing pattern and move instructions
		InstructionPair pair = new InstructionPair();
		String instruction;
		for (int i = 0; i < lineTokens.length; i++) {
			instruction = lineTokens[i].trim();
			if (instruction.isEmpty())
				continue;

			if (FIRING_PATTERN_MAP.containsKey(instruction)) {
				// validate the fire pattern instruction
				if (pair.getFiringPattern() != null)
					Logger.printErrorAndExit(Script.class,
							"Too many firing pattern instructions at line "
									+ Integer.toString(instructions.size() + 1)
									+ " in script file");

				pair.setFiringPattern(instruction);

			} else if (MOVE_MAP.containsKey(instruction)) {
				// validate the move instruction
				if (pair.getMove() != null)
					Logger.printErrorAndExit(
							Script.class,
							"Too many move instructions at line "
									+ Integer.toString(instructions.size() + 1)
									+ " in script file");

				pair.setMove(instruction);

			} else {
				// invalid instruction
				Logger.printErrorAndExit(Script.class, "Invalid instruction "
						+ instruction);
			}

			// store the parsed instruction pair
			if (instructions == null)
				instructions = new ArrayList<InstructionPair>();

			instructions.add(pair);
		}

	}
}
