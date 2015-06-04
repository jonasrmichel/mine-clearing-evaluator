public class Field extends InputFile {
	/** The field's x and y dimensions. */
	private int xDimension = 0, yDimension = 0;

	/** Holds the currently active mines. */
	private MineIndex mineIndex;

	public Field(String fieldFile) {
		super(fieldFile);

		// Note: the mine index will be initialized during parsing
	}

	@Override
	public void processLine(String line) {
		Logger.printDebug(Field.class, "processing line " + line);

		// tokenize and parse the line
		line = line.trim().replace(" ", "");
		parseLine(line, yDimension);

		// update the field's x dimension
		if (line.length() > xDimension)
			xDimension = line.length();

		// update the field's y dimension
		yDimension++;
	}

	@Override
	public void validate() {
		Logger.printDebug(Field.class, "Validating field model");

		if (xDimension == 0 || yDimension == 0)
			Logger.printErrorAndExit(Field.class,
					"Please provide a non-empty field file");
	}

	public int getXDimension() {
		return xDimension;
	}

	public int getYDimension() {
		return yDimension;
	}

	/**
	 * Returns a position indicating the center of the XY-plane of the field.
	 * The center of an even dimension will be the lower of the two middle most
	 * points.
	 * 
	 * @return the center position of the field's XY-plane.
	 */
	public Position getCenter() {
		int centerX = (int) (Math.ceil((double) xDimension) / 2);
		int centerY = (int) (Math.ceil((double) yDimension) / 2);

		return new Position(centerX, centerY);
	}

	public int getNumMines() {
		return mineIndex.count();
	}

	public int getNumMinesAbove(int depth) {
		return mineIndex.getMinesAbove(depth).size();
	}

	/**
	 * Parses a single line of a field file.
	 * 
	 * @param lineTokens
	 *            a slice of the x dimension of a field
	 * @param y
	 *            the y value of the x slice
	 */
	private void parseLine(String line, int y) {
		char c;
		for (int x = 0; x < line.length(); x++) {
			c = line.charAt(0);
			if (c == Settings.EMPTY_POSITION_CHARACTER)
				continue;

			// add the mine at the appropriate depth
			if (mineIndex == null)
				mineIndex = new MineIndex();

			mineIndex.addMine(new Position(x, y, Util.translateToRange(c)));
		}
	}

	/**
	 * Generates a string representing the current state of the mine field from
	 * a particular point of view.
	 * 
	 * @param pov
	 *            the position from which to view the field
	 * @return a string representing the current state of the field
	 */
	public String toString(Position pov) {
		// TODO Auto-generated constructor stub
		return null;
	}
}
