public class Field extends FileModel {
	/** The field's x and y dimensions. */
	private int xDimension = 0, yDimension = 0;

	/** Holds the currently active mines. */
	private MineIndex mineIndex;

	public Field(String fieldFile) {
		super(fieldFile);

		mineIndex = new MineIndex();
	}

	@Override
	public void processLine(String line) {
		Logger.printDebug(Field.class, "processing " + line);

		// tokenize and parse the line
		String[] lineTokens = line.trim().replace(" ", "").split("");
		parseLine(lineTokens, yDimension);

		// update the field's x dimension
		if (lineTokens.length > xDimension)
			xDimension = lineTokens.length;

		// update the field's y dimension
		yDimension++;
	}
	
	@Override
	public void validate() {
		Logger.printDebug(Field.class, "Validating field model");
		
		if (xDimension == 0 || yDimension == 0)
			Logger.printErrorAndExit(Field.class, "Please provide a non-empty field file");
	}

	/**
	 * Parses a single line of a field file.
	 * 
	 * @param lineTokens
	 *            a slice of the x dimension of a field
	 * @param y
	 *            the y value of the x slice
	 */
	private void parseLine(String[] lineTokens, int y) {
		char c;
		for (int x = 0; x < lineTokens.length; x++) {
			c = lineTokens[x].charAt(0);
			if (c == Settings.EMPTY_POSITION_CHARACTER)
				continue;

			// add the mine at the appropriate depth
			mineIndex.addMine(new Position(x, y, Util.translateToRange(c)));
		}
	}

}
