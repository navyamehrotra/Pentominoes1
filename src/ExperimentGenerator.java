import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ExperimentGenerator {

	private static String experimentResults = "";

	public static void main(String[] args) {
		Search.useUI = false;
		generateExperiments();
	}

    /**
     * Generate an output for every input and store it in a file.
     */
    public static void generateExperiments() {

        // Iterate over all grid sizes that make sense
		for (int xSize = 1; xSize <= 12; xSize++) {
			for (int ySize = 1; ySize <= 12; ySize++) {
				Search.verticalGridSize = ySize;
				Search.horizontalGridSize = xSize;

				if ((xSize * ySize) % 5 == 0 && xSize * ySize <= 60) {
					System.out.println("Progress: " + xSize + "x" + ySize);
					allInputCombinations(xSize * ySize / 5);
				}
			}
		}

		// Write the results to a file
		try {
			FileWriter myWriter = new FileWriter("experiments.txt");
			myWriter.write(experimentResults);
			myWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void allInputCombinations(int piecesToCollect) {
		allInputCombinations(new ArrayList<Character>(), 0, piecesToCollect);
	}

	/**
	 * Recursively finds every combination of the Search inputsPossible with a desired cardinality.
	 * @param inputTemp input so far
	 * @param currentlyConsidered the index of the next piece to be added (or not)
     * @param piecesToCollect pieces left to collect
	 */
	public static void allInputCombinations(ArrayList<Character> inputTemp, int currentlyConsidered, int piecesToCollect) {
		if (piecesToCollect == 0) {
			Search.input = new char[inputTemp.size()];
			for (int i = 0; i < inputTemp.size(); i++) {
				Search.input[i] = inputTemp.get(i);
			}

			double timeBefore = System.currentTimeMillis();
			boolean found = Search.search();
			double elapsed = System.currentTimeMillis() - timeBefore;

			// Collection of the experiment data
			if (found) {
				experimentResults += inputTemp.size() + " " + elapsed + "\n";
			}

			return;
		}

		if (currentlyConsidered == Search.inputsPossible.length) {
			return;
		}

		allInputCombinations(inputTemp, currentlyConsidered + 1, piecesToCollect);

		inputTemp.add(Search.inputsPossible[currentlyConsidered]);
		allInputCombinations(inputTemp, currentlyConsidered + 1, piecesToCollect - 1);
		inputTemp.remove(inputTemp.size() - 1);
	}
}
