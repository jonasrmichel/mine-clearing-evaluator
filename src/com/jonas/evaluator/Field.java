package com.jonas.evaluator;

/**
 * This class represents the current state of the mine field cuboid space, which
 * is initially provided by an input field file. The Field class delegates
 * storage and retrieval of active mines to its {@link MineIndex}.
 * 
 * @author Jonas Michel, jonas.r.michel@gmail.com
 * 
 */
public class Field extends InputFileModel {
	/** The field's x and y dimensions. */
	private int xDimension, yDimension;

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

		// build the mine index
		mineIndex.build();
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

	/**
	 * Returns the number of active mines in the mine field.
	 * 
	 * @return the number of active mines
	 */
	public int getNumMines() {
		return mineIndex.count();
	}

	/**
	 * Returns whether or not there are any active mines at or above a
	 * particular depth.
	 * 
	 * @param depth
	 *            a depth at which to test
	 * @return true if mines exist at or above the depth, false otherwise
	 */
	public boolean minesAbove(int depth) {
		return mineIndex.getNumMinesAtOrAbove(depth) > 0;
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
			c = line.charAt(x);
			if (c == Settings.EMPTY_POSITION_CHARACTER)
				continue;

			// add the mine at the appropriate depth
			if (mineIndex == null)
				mineIndex = new CustomMineIndex();

			mineIndex.addMine(new Position(x, y, Util.translateToRange(c)));
		}
	}

	/**
	 * Destroys all mines located at a particular xy-coordinate.
	 * 
	 * @param position
	 *            xy-coordinates to destroy field mines
	 */
	public void destroyMines(Position position) {
		mineIndex.removeMineAtXY(position);
	}

	/**
	 * Generates a string representing the current state of the mine field
	 * centered at a particular position within the field.
	 * 
	 * @param viewPosition
	 *            the position from which to view the field
	 * @return a string representing the current state of the field
	 */
	public String toString(Position viewPosition) {
		StringBuilder sb = new StringBuilder();

		// retrieve the maximum x- and y-axis distances of mines from the
		// viewing position
		int maxX = mineIndex.getMaxXDistance(viewPosition);
		int maxY = mineIndex.getMaxYDistance(viewPosition);

		// center the field of view at the view position
		Position start = new Position(viewPosition.getX() - maxX,
				viewPosition.getY() - maxY);
		Position stop = new Position(viewPosition.getX() + maxX,
				viewPosition.getY() + maxY);

		// build the string representing the state of the XY-plane of the field
		Position fieldPosition, minePosition;
		int mineRange;
		for (int y = start.getY(); y <= stop.getY(); y++) {
			for (int x = start.getX(); x <= stop.getX(); x++) {
				// evaluate this (x,y) position
				fieldPosition = new Position(x, y);
				if ((minePosition = mineIndex.getMineAtXY(fieldPosition)) != null) {
					// mined field position
					mineRange = minePosition.getZ() - viewPosition.getZ();
					if (mineRange <= 0) {
						// missed mine
						sb.append(Settings.MISSED_MINE_CHARACTER);

					} else {
						// active mine
						sb.append(Util.translateToLetter(mineRange));
					}
				} else {
					// empty field position
					sb.append(Settings.EMPTY_POSITION_CHARACTER);
				}
			}
			sb.append(System.getProperty("line.separator"));
		}

		return sb.toString();
	}
}
