import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StepInstructions {
	/** Holds mappings from firing pattern names to offest positions. */
	public static final Map<String, List<Position>> FIRING_PATTERN_MAP;
	static {
		Map<String, List<Position>> fpMap = new HashMap<String, List<Position>>();
		fpMap.put("alpha", new ArrayList<Position>() {
			{
				add(new Position(-1, -1));
				add(new Position(-1, 1));
				add(new Position(1, -1));
				add(new Position(1, 1));
			}
		});
		fpMap.put("beta", new ArrayList<Position>() {
			{
				add(new Position(-1, 0));
				add(new Position(0, -1));
				add(new Position(0, 1));
				add(new Position(1, 0));
			}
		});
		fpMap.put("gamma", new ArrayList<Position>() {
			{
				add(new Position(-1, 0));
				add(new Position(0, 0));
				add(new Position(1, 0));
			}
		});
		fpMap.put("delta", new ArrayList<Position>() {
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
		mMap.put("north", new Position(0, -1)); // up
		mMap.put("south", new Position(0, 1)); // down
		mMap.put("east", new Position(1, 0)); // right
		mMap.put("west", new Position(-1, 0)); // left

		MOVE_MAP = Collections.unmodifiableMap(mMap);
	}

	/** Holds the set of instructions. */
	private List<String> instructions;

	/** Validation flags. */
	private boolean hasFire = false, hasMove = false;

	public StepInstructions() {
		instructions = new ArrayList<String>();
	}

	public StepInstructions(String... instructions) {
		this();

		for (String instruction : instructions)
			addInstruction(instruction);
	}

	public List<String> getInstructions() {
		return instructions;
	}

	public static boolean isFiringPattern(String instruction) {
		return FIRING_PATTERN_MAP.containsKey(instruction);
	}

	public static boolean isMove(String instruction) {
		return MOVE_MAP.containsKey(instruction);
	}

	/**
	 * Adds an instruction after performing some validation steps.
	 * 
	 * @param instruction
	 *            a step instruction (move or fire)
	 */
	public void addInstruction(String instruction) {
		if (instruction.isEmpty()) {
			// blank instruction, ignore
			return;

		} else if (instructions.size() == Settings.MAX_STEP_INSTRUCTIONS) {
			// too many instructions
			Logger.printErrorAndExit(Script.class, "Too many instructions");

		} else if (!isFiringPattern(instruction) && !isMove(instruction)) {
			// invalid instruction
			Logger.printErrorAndExit(Script.class, "Invalid instruction "
					+ instruction);

		} else if (hasFire && isFiringPattern(instruction)) {
			// too many fire instructions
			Logger.printErrorAndExit(Script.class,
					"Too many firing pattern instructions");

		} else if (hasMove && isMove(instruction)) {
			// too many move instructions
			Logger.printErrorAndExit(Script.class, "Too many move instructions");

		} else {
			// set appropriate validation flag
			if (FIRING_PATTERN_MAP.containsKey(instruction))
				hasFire = true;
			else
				hasMove = true;

			// the instruction is valid
			instructions.add(instruction);
		}
	}

	@Override
	public String toString() {
		return Util.join(" ",
				instructions.toArray(new String[instructions.size()]));
	}
}
