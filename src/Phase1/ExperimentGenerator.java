package Phase1;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ExperimentGenerator {

	private static String experimentResults = "";

	// The generator will go to the next size if it goes through this many tests, regardless of how they went
	private static final int testsPerDimension = 1000000;	
	// The generator will go to the next size if it goes through this many solvable tests
	private static final int validTestsPerDimension = 10;
	// The solver will not generate tests with more than this pentominos
	private static final int maxPentominos = 12;
	// Choice of algorithm
	private static final int algorithm = 1;


	public static void main(String[] args) {
		Search.useUI = false;
		Search.choice = algorithm;
		generateExperiments();
	}

    /**
     * Generate an output for every input and store it in a file.
     */
    public static void generateExperiments() {
        // Iterate over all grid sizes that make sense
		for (int xSize = 1; xSize <= 12; xSize++) {
			for (int ySize = 1; ySize <= 12; ySize++) {
				if ((xSize * ySize) % 5 == 0 && xSize * ySize <= 60 && (xSize * ySize / 5) <= maxPentominos) {
					System.out.println("Progress: " + xSize + "x" + ySize);
					
					for (int i = 0, j = 0; i < testsPerDimension && j < validTestsPerDimension ; i++) {
						Search.input = randomInput(xSize * ySize / 5);
						Search.verticalGridSize = ySize;
						Search.horizontalGridSize = xSize;

						// If it's the basic solver - first check if the input is possible using the recursive one
						boolean worthTrying = true;
						if (Search.choice == 0) {
							Search.choice = 1;
							if (!Search.search()) {
								worthTrying = false;
							}
							Search.choice = 0;
						}

						if (worthTrying) {
							j++;
							double timeBefore = System.currentTimeMillis();
							boolean found = Search.search();
							double elapsed = System.currentTimeMillis() - timeBefore;

							// Collection of the experiment data
							if (found) {
								experimentResults += Search.input.length + " " + elapsed + "\n";
							}
						}
					}

				}
			}
		}
		
		// Write the results to a file
		try {
			FileWriter myWriter = new FileWriter("experiments.UwU");
			myWriter.write(experimentResults);
			myWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static char[] randomInput(int piecesToCollect) {
		// Convert the possible characters array into an arraylist
		ArrayList<Character> possibleCharacters = new ArrayList<>();
		for (int i = 0; i < Search.inputsPossible.length; i++) {
			possibleCharacters.add(Search.inputsPossible[i]);
		}

		// Fill an array with random pentominos
		char[] randomPentominos = new char[piecesToCollect];
		for (int i = 0; i < piecesToCollect; i++) {
			int randomIndex = (int)(Math.random() * possibleCharacters.size());
			randomPentominos[i] = possibleCharacters.get(randomIndex);
			possibleCharacters.remove(randomIndex);
		}

		return randomPentominos;
	}
}
