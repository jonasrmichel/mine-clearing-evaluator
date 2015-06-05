package com.jonas.evaluator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * This is an abstract class that represents a data model read line-by-line from
 * an input file.
 * 
 * @author Jonas Michel, jonas.r.michel@gmail.com
 * 
 */
public abstract class InputFileModel {

	public InputFileModel(String filePath) {
		Logger.printDebug(InputFileModel.class, "Processing file " + filePath);

		try {
			// process each line of the input file
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			for (String line; (line = br.readLine()) != null;)
				processLine(line);

			br.close();

		} catch (FileNotFoundException e) {
			Logger.printErrorAndExit(InputFileModel.class, "File not found "
					+ filePath);

		} catch (IOException e) {
			Logger.printErrorAndExit(InputFileModel.class,
					"Could not close file " + filePath);
		}

		// validate the model
		validate();
	}

	/**
	 * Processes a single line of the input file.
	 * 
	 * @param line
	 */
	public abstract void processLine(String line);

	/**
	 * Validates the model after the input file is read.
	 */
	public abstract void validate();
}
