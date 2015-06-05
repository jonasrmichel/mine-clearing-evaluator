package com.jonas.evaluator;

/**
 * This class represents a position in the three dimensional mine field cuboid.
 * It's important to note that a Position is hashed and identified using only
 * its x- and y- coordinates (i.e., its z-coordinate is ignored). This
 * functionality exists to support efficient position indexing (see
 * {@link MineIndex}).
 * 
 * @author Jonas Michel, jonas.r.michel@gmail.com
 * 
 */
public class Position {
	private int x = 0, y = 0, z = 0;

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Position(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	/**
	 * Translates the position's coordinates.
	 * 
	 * @param offset
	 *            the amount to translate the (x,y,z) coordinates
	 */
	public void translate(Position offset) {
		x = x + offset.getX();
		y = y + offset.getY();
		z = z + offset.getZ();
	}

	/**
	 * Returns a position relative to this position.
	 * 
	 * @param offset
	 *            the amount to translate the relative position's (x,y,z)
	 *            coordinates
	 * @return the relative position
	 */
	public Position relativePosition(Position offset) {
		Position relative = new Position(x, y, z);
		relative.translate(offset);

		return relative;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Position [x=").append(x).append(", y=").append(y)
				.append(", z=").append(z).append("]");
		return builder.toString();
	}

}
