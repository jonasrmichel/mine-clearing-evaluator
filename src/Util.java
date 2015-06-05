public class Util {
	/**
	 * Translates a character to an integer representing depth range. The
	 * letters a-z map to 1-26 and A-Z map to 27-52.
	 * 
	 * @param c
	 *            an alphabetical letter
	 * @return an integer representing depth range
	 */
	public static int translateToRange(char c) {
		// validate range character
		if (!String.valueOf(c).matches("[a-zA-Z]"))
			Logger.printErrorAndExit(Util.class, "Invalid range character " + c);

		int range = 0;
		if (Character.isLowerCase(c)) {
			// map a-z into 1-26
			range = (int) c - 'a' + 1;

		} else {
			// map A-Z into 27-52
			range = (int) c - 'A' + 27;
		}

		return range;
	}

	/**
	 * Translates an integer representing depth range to a character. The
	 * letters a-z map to 1-26 and A-Z map to 27-52.
	 * 
	 * @param range
	 *            an integer representing depth range
	 * @return a single alphabetical letter
	 */
	public static char translateToLetter(int range) {
		// validate range
		if (range < 1 || range > 52)
			Logger.printErrorAndExit(Util.class, "Invalid range " + range);

		char c = 'a';
		if (range < 27) {
			// map 1-26 into a-z
			c = Character.toChars(range + 'a' - 1)[0];

		} else {
			// map 27-52 into A-Z
			c = Character.toChars(range + 'A' - 27)[0];
		}

		return c;
	}

	/**
	 * Joins an arbitrary number of strings using a delimiter.
	 * 
	 * @param delim
	 *            a string delimiter
	 * @param strings
	 *            a string iterable
	 * @return the delimiter-joined string
	 */
	public static String join(String delim, String... strings) {
		StringBuilder sb = new StringBuilder();
		String loopDelim = "";
		for (String str : strings) {
			sb.append(loopDelim);
			sb.append(str);

			loopDelim = delim;
		}

		return sb.toString();
	}

	/**
	 * Scales a position by a scalar factor.
	 * 
	 * @param position
	 *            a position
	 * @param factor
	 *            a scalar factor
	 * @return the scaled position
	 */
	public static Position scale(Position position, int factor) {
		return new Position(position.getX() * factor, position.getY() * factor,
				position.getZ() * factor);
	}
}
