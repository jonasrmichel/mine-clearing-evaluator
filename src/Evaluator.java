
public class Evaluator {
	/** Holds the state of the field cuboid. */
	private Field field;
	
	/** Holds the instructions of a mine clearing script. */
	private Script script;

	public Evaluator(String fieldFile, String scriptFile) {
		field = new Field(fieldFile);
		script = new Script(scriptFile);
	}
	
	public void evaluate() {
		// TODO Auto-generated method stub
	}

	public static void main(String[] args) {
		// validate command line arguments
		if (args.length != 2) {
			Logger.printHelp();
			Logger.printErrorAndExit(Evaluator.class, "Invalid number of command line arguments");
		}
		
		// kick off evaluation
		new Evaluator(args[0], args[1]).evaluate();
	}

}
