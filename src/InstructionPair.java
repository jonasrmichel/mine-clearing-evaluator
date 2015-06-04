
public class InstructionPair {

	/** Optional instructions. */
	private String firingPattern = null, move = null;

	public InstructionPair() {
		// no-arg constructor
	}

	public InstructionPair(String firingPattern, String move) {
		this.firingPattern = firingPattern;
		this.move = move;
	}

	public String getFiringPattern() {
		return firingPattern;
	}

	public void setFiringPattern(String firingPattern) {
		this.firingPattern = firingPattern;
	}

	public String getMove() {
		return move;
	}

	public void setMove(String move) {
		this.move = move;
	}

	@Override
	public String toString() {
		return Util.join(" ", firingPattern, move);
	}
}
