public class Settings {

	/** Debug mode. */
	public static final boolean DEBUG_MODE = true;

	/** The rate at which a vessel "falls" through a cuboid in km/s. */
	public static final int FALL_RATE = 1;

	/**
	 * The rate at which a vessel can move on the current planar cross-section
	 * in km/s.
	 */
	public static final int MOVE_RATE = 1;

	/** The depth at which a vessel begins its descent. */
	public static final int STARTING_DEPTH = 0;

	/** The character representing an empty (non-explosive) position. */
	public static final char EMPTY_POSITION_CHARACTER = '.';

	/** The character representing a position with a missed mine. */
	public static final char MISSED_MINE_CHARACTER = '*';
	
	/** The maximum number of instructions per simulation step. */
	public static final int MAX_STEP_INSTRUCTIONS = 2;
}
