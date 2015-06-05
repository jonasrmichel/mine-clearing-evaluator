package com.jonas.evaluator;

/**
 * This is a utility class containing helpful CLI output methods for debug and
 * error handling purposes.
 * 
 * @author Jonas Michel, jonas.r.michel@gmail.com
 * 
 */
public class Logger {

	public static void printHelp() {
		System.out
				.println("Usage: $ java Evaluator <field-file> <script-file>");
	}

	public static void printErrorAndExit(Class<?> clazz, String err) {
		System.err.println("Error (" + clazz.getName() + ") : " + err);
		System.err.println();
		System.out.println("fail (0)");

		System.exit(1);
	}

	public static void printDebug(Class<?> clazz, String str) {
		if (!Settings.DEBUG_MODE)
			return;

		System.out.println(clazz.getName() + ": " + str);
	}
}
