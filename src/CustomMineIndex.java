import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.TreeMap;

public class CustomMineIndex implements MineIndex {
	/** A list for collecting unsorted mine positions. */
	private List<Position> mineList;

	/**
	 * Holds the positions of currently active mines in the field ordered by
	 * z-coordinate.
	 */
	private LinkedHashMap<Position, Position> mineMap = null;

	/**
	 * Holds x- and y-coordinate values mapped to counts of mines at those
	 * values ordered by the x- and y-coordinate key, respectively.
	 */
	private TreeMap<Integer, Integer> xCountMap = null, yCountMap = null;

	public CustomMineIndex() {
		mineList = new ArrayList<Position>();
	}

	@Override
	public void build() {
		Logger.printDebug(CustomMineIndex.class, "Builidng mine index");

		// initialize data structures
		mineMap = new LinkedHashMap<Position, Position>();
		xCountMap = new TreeMap<Integer, Integer>();
		yCountMap = new TreeMap<Integer, Integer>();

		// order mine positions by z-coordinate
		Collections.sort(mineList, new Comparator<Position>() {

			@Override
			public int compare(Position p1, Position p2) {
				return Integer.compare(p1.getZ(), p2.getZ());
			}

		});

		// loop through the ordered mine positions and build data structures
		int x, y;
		for (Position mine : mineList) {
			mineMap.put(mine, mine);

			x = mine.getX();
			if (!xCountMap.containsKey(x))
				xCountMap.put(x, 1);
			else
				xCountMap.put(x, xCountMap.get(x) + 1);

			y = mine.getY();
			if (!yCountMap.containsKey(y))
				yCountMap.put(y, 1);
			else
				yCountMap.put(y, yCountMap.get(y) + 1);
		}

		Logger.printDebug(CustomMineIndex.class, mineMap.toString());
		Logger.printDebug(CustomMineIndex.class, xCountMap.toString());
		Logger.printDebug(CustomMineIndex.class, yCountMap.toString());
	}

	@Override
	public void addMine(Position position) {
		mineList.add(position);
	}

	@Override
	public Position getMineAtXY(Position position) {
		return mineMap.get(position);
	}

	@Override
	public void removeMineAtXY(Position position) {
		// remove the mine at this xy-position
		mineMap.remove(position);

		// update the count for this mine's x-coordinate
		int x = position.getX();
		if (xCountMap.containsKey(x)) {
			int xCount = xCountMap.get(x);
			if (--xCount == 0)
				xCountMap.remove(x);
			else
				xCountMap.put(x, xCount);
		}

		// update the count for this mine's y-coordinate
		int y = position.getY();
		if (yCountMap.containsKey(y)) {
			int yCount = yCountMap.get(y);
			if (--yCount == 0)
				yCountMap.remove(y);
			else
				yCountMap.put(y, yCount);
		}
	}

	@Override
	public int getMaxXDistance(Position position) {
		try {
			// get min and max mine x-coordinates
			int minX = xCountMap.firstKey();
			int maxX = xCountMap.lastKey();

			// calculate which is farthest from the provided position
			return Math.max(Math.abs(position.getX() - maxX),
					Math.abs(position.getX() - minX));

		} catch (NoSuchElementException e) {
			// no mines
			return 0;
		}
	}

	@Override
	public int getMaxYDistance(Position position) {
		try {
			// get min and max mine x-coordinates
			int minY = yCountMap.firstKey();
			int maxY = yCountMap.lastKey();

			// calculate which is farthest from the provided position
			return Math.max(Math.abs(position.getY() - maxY),
					Math.abs(position.getY() - minY));

		} catch (NoSuchElementException e) {
			// no mines
			return 0;
		}
	}

	@Override
	public int count() {
		return mineMap.size();
	}

	@Override
	public int getNumMinesAtOrAbove(int depth) {
		int numMines = 0;

		// iterate through mines in order of increasing depth
		for (Position mine : mineMap.values()) {
			if (mine.getZ() <= depth)
				numMines++;
			else
				break;
		}

		return numMines;

	}
}
