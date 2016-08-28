import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

// ADT is the Master Control
public class ADT {
	// Note: Alphabet Shifter is the component to alphabetize the output
	// Note: We use a private helper class to modularize it so that we don't need more files :)
	private static class AlphabetShifter {
		private ArrayList<String> linesToShift;

		public AlphabetShifter(ArrayList<String> linesToShift) {
			this.linesToShift = linesToShift;
		}

		private void sortLinesToShift() {
			Collections.sort(linesToShift);
		}

		public ArrayList<String> getSortedLines() {
			sortLinesToShift();
			return linesToShift;
		}
	}

	// Note: Circular Shifter is the component to produce all the possible "circularized" lines and
	// check if the first word is part of the words to ignore and only provide "circularized" lines
	// in which the first word is one of those that we do not ignore
	// Note: We use a private helper class to modularize it so that we don't need more files :)
	private static class CircularShifter {
		private ArrayList<String> linesProvided;
		private ArrayList<String> wordsToIgnore;

		public CircularShifter(ArrayList<String> linesProvided, ArrayList<String> wordsToIgnore) {
			this.linesProvided = linesProvided;
			this.wordsToIgnore = createWordsToIgnore(wordsToIgnore);
		}

		private ArrayList<String> createWordsToIgnore(ArrayList<String> wordsToIgnore) {
			ArrayList<String> lowercasedWordsToIgnore = new ArrayList<String>();

			for (String word : wordsToIgnore) {
				lowercasedWordsToIgnore.add(word.toLowerCase());
			}

			return lowercasedWordsToIgnore;
		}

		private ArrayList<String> createAllCircularShiftedLines() {
			ArrayList<String> circularShiftedLines = new ArrayList<String>();

			for (String line : linesProvided) {
				String[] words = line.split(" ");

				int numberOfWords = words.length;

				// Note: Technicality - if first word is ignored, we shift it to the back with it lower-cased
				// Note: Technicality - we ignore casing when comparing words to ignore, casing comes into play
				// only when we are building the string to be added (meaning that the casing of the original string
				// is preserved when building the output string unless it is lower-cased in the process of shifting
				// due to it being an ignored word [with equivalence check without considering casing])

				for (int i = 0; i < numberOfWords; i++) {
					String firstWord = words[i];

					if (wordsToIgnore.contains(firstWord.toLowerCase())) {
						words[i] = firstWord.toLowerCase();
						continue;
					} else {
						// Oh my gosh, why does Java not have .capitalize()?!
						words[i] = firstWord.substring(0, 1).toUpperCase() + firstWord.substring(1);
					}

					String circularLineToAdd = "";

					for (int j = i; j < numberOfWords; j++) {
						circularLineToAdd += words[j] + " ";
					}

					for (int k = 0; k < i; k++) {
						circularLineToAdd += words[k] + " ";
					}

					circularShiftedLines.add(circularLineToAdd.trim());
				}
			}

			return circularShiftedLines;
		}
		
		public ArrayList<String> getCircularShiftedLines() {
			return createAllCircularShiftedLines();
		}
	}

	private static void checkValidityOfArguments(String[] args) {
		if (!(args.length == 3)) {
			System.out.println("Usage of program: java ADT.java <input_file.name> <words_to_ignore_file.name> <output_file.name>");
			System.exit(0);
		}
	}

	private static ArrayList<String> readFile(String fileName) {
		ArrayList<String> linesToReturn = new ArrayList<String>();

		String line = null;

		try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {
            	line = line.trim().replaceAll("\\s+", " ");
            	linesToReturn.add(line);
            }

            bufferedReader.close();         
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "', make sure you have provided a correct file name");
            System.exit(0);
        } catch (IOException ioException) {
            System.out.println("Error reading file '" + fileName + "', IO exception, look below for the stack trace");                  
            ioException.printStackTrace();
        }

		return linesToReturn;
	}

	private static void writeFile(String fileName, ArrayList<String> linesToWrite) {
		try {
            FileWriter fileWriter = new FileWriter(fileName);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            for (String line : linesToWrite) {
            	bufferedWriter.write(line + "\n");
            }

            bufferedWriter.close();
        } catch (IOException ioException) {
            System.out.println("Error writing to file '"+ fileName + "', IO exception, look below for the stack trace");
            ioException.printStackTrace();
        }
	}

	private static void signalUserThatProgramHasCompleted(String fileName) {
		System.out.println("The program has compeleted, check " + fileName + " for output!");
	}

	public static void main(String [] args) {
		checkValidityOfArguments(args);

		// After checking for 3 arguments, we assume the file names are correct and of the right order
		// If file names are wrong, we will get exceptions
		// If they are not of the right order, we will get rubbish output and humans will verify :)

		// Note: We do not consider duplicates, if there are duplicates we just print them and accept them anyway
		String inputFilename = args[0];
		String wordsToIgnoreFilename = args[1];
		String outputFilename = args[2];

		ArrayList<String> lines = readFile(inputFilename);
		ArrayList<String> wordsToIgnore = readFile(wordsToIgnoreFilename);

		CircularShifter circularShifter = new CircularShifter(lines, wordsToIgnore);
		ArrayList<String> circularShiftedLines = circularShifter.getCircularShiftedLines();

		AlphabetShifter alphabetShifter = new AlphabetShifter(circularShiftedLines);
		ArrayList<String> alphabetShiftedLines = alphabetShifter.getSortedLines();

		writeFile(outputFilename, alphabetShiftedLines);

		signalUserThatProgramHasCompleted(outputFilename);
	}
}
