import java.util.ArrayList;
import java.util.Collections;

// Note: Alphabet Shifter is the component to alphabetize the output
// Note: We use a private helper class to modularize it so that we don't need more files :)
public class AlphabetShifter {
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
