import java.util.ArrayList;

// Note: Circular Shifter is the component to produce all the possible "circularized" lines and
// check if the first word is part of the words to ignore and only provide "circularized" lines
// in which the first word is one of those that we do not ignore
// Note: We use a private helper class to modularize it so that we don't need more files :)
public class CircularShifter {
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
