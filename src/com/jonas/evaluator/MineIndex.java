package com.jonas.evaluator;

/**
 * This interface defines the minimum required methods of a mine index employed
 * by a {@link Field} to store and efficiently retrieve mine positions.
 * 
 * @author Jonas Michel, jonas.r.michel@gmail.com
 * 
 */
public interface MineIndex {
	/**
	 * Builds the mine index. This must be performed before the index can be
	 * accessed (i.e., after adding all mine positions).
	 */
	public void build();

	/**
	 * Adds a mine at a particular XYZ-coordinate.
	 * 
	 * @param position
	 *            a position in the mine field
	 */
	public void addMine(Position position);

	/**
	 * Returns any mine located at a particular XY-coordinate.
	 * 
	 * @param position
	 *            an XY-coordinate in the mine field
	 * @return the position of any mine located at the provided XY-coordinate or
	 *         null if none such exists
	 */
	public Position getMineAtXY(Position position);

	/**
	 * Removes any mines located at a particular XY-coordinate.
	 * 
	 * @param position
	 *            an XY-coordinate in the mine field
	 */
	public void removeMineAtXY(Position position);

	/**
	 * Returns the maximum distance along the x-axis at which a mine exists from
	 * a particular location in the mine field.
	 * 
	 * @param position
	 *            a location in the mine field
	 * @return the x-axis distance to farthest mine from the position
	 */
	public int getMaxXDistance(Position position);

	/**
	 * Returns the maximum distance along the y-axis at which a mine exists from
	 * a particular location in the mine field.
	 * 
	 * @param position
	 *            a location in the mine field
	 * @return the y-axis distance to farthest mine from the position
	 */
	public int getMaxYDistance(Position position);

	/**
	 * Returns the current number of mines in the mine field.
	 * 
	 * @return the number of currently active mines
	 */
	public int count();

	/**
	 * Returns a list of the positions of any mines at or above a particular
	 * depth in the field.
	 * 
	 * @param depth
	 *            the depth to test
	 * @return a list of mine positions or null if none exist
	 */
	public int getNumMinesAtOrAbove(int depth);
}
