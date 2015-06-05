package com.jonas.evaluator.test;

import java.io.OutputStream;
import java.io.PrintStream;

import com.jonas.evaluator.Evaluator;
import com.jonas.evaluator.InputFileModel;

/**
 * Run the TestEvaluator to compare the Evaluator's output to a valid output
 * file. Usage: $ java TestEvaluator <field-file> <script-file> <test-output-file>
 * 
 * @author Jonas Michel, jonas.r.michel@gmail.com
 * 
 */
public class TestEvaluator extends InputFileModel {
	private StringBuilder testOutput;

	public TestEvaluator(String testOutputFile) {
		super(testOutputFile);
	}

	@Override
	public void processLine(String line) {
		if (testOutput == null)
			testOutput = new StringBuilder();

		testOutput.append(line.trim());
		testOutput.append(System.getProperty("line.separator"));
	}

	@Override
	public void validate() {
		// nothing to do
	}

	public String getString() {
		return testOutput.toString();
	}

	public static void main(String[] args) {
		if (args.length != 3) {
			System.err.println("Invalid number of command line arguments");
			System.err
					.println("Usage: java TestEvaluator <field-file> <script-file> <test-output-file>");

			System.exit(1);
		}

		// intercept System.out data
		PrintStream origOut = System.out;
		Interceptor interceptor = new Interceptor(origOut);
		System.setOut(interceptor);

		// run the evaluator, capturing its output
		new Evaluator(args[0], args[1]).evaluate();

		// read in the test output
		TestEvaluator testEvaluator = new TestEvaluator(args[2]);

		// detach the System.out interceptor
		if (origOut != null)
			System.setOut(origOut);

		// compare the evaluator-generated and test output
		if (!testEvaluator.getString().trim()
				.equals(interceptor.getString().trim()))
			System.out.println("FAIL: generated and test output differ");
		else
			System.out.println("PASS: generated and test output are identical");
	}

	/**
	 * An inner utility class to intercept a print stream's data.
	 * 
	 * @author Jonas Michel, jonas.r.michel@gmail.com
	 * 
	 */
	private static class Interceptor extends PrintStream {
		private StringBuilder sb;

		public Interceptor(OutputStream out) {
			super(out, true);

			sb = new StringBuilder();
		}

		@Override
		public void print(String s) {
			sb.append(s);
		}

		@Override
		public void println() {
			sb.append(System.getProperty("line.separator"));
		}

		@Override
		public void println(String s) {
			sb.append(s);
			sb.append(System.getProperty("line.separator"));
		}

		public String getString() {
			return sb.toString();
		}
	}

}
