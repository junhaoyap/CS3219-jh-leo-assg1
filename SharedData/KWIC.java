import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;


public class KWIC{
  
  // shared data
  private ArrayList<String> listOfWords;
  private ArrayList<String> wordsToIgnore;
  
  private ArrayList<String> kwicResult;
  
  String outputFilename;
  
  
  // subroutines
  
  public void getInput() throws IOException {
	Scanner sc = new Scanner(System.in);
    this.getInputListOfWords(sc);
    this.getInputWordsToIgnore(sc);
    this.determineOutputFile(sc);
    sc.close();
  }
  
  public void getInputListOfWords(Scanner sc) throws IOException {
	  listOfWords = new ArrayList<String>();
    System.out.println("Please input the filename of the list of entries to be KWIC-ed:");
    String fileName = sc.next();
    
    listOfWords = (ArrayList<String>) Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);

  }
  
  public void getInputWordsToIgnore(Scanner sc) throws IOException {
	  wordsToIgnore = new ArrayList<String>();
	    System.out.println("Please input the filename of the list of words to be ignored:");
	    String fileName = sc.next();
	    
	    wordsToIgnore = (ArrayList<String>) Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
  }
  
  public void determineOutputFile(Scanner sc) {
	    System.out.println("Please input the filename where the KWIC result will be written in:");
	  outputFilename = sc.next();
  }

  public void circularShift(){
	  String title;
	  String firstWord;
	  kwicResult = new ArrayList<String>();
	  for(int i=0; i<listOfWords.size(); i++) {
		  title = listOfWords.get(i);
		  int wordCount = getNumberOfWords(title);
		  for(int j=0; j<wordCount; j++) {
			  firstWord = getFirstWord(title);
			  if(!wordsToIgnore.contains(firstWord.toLowerCase())) { // only process this permutation if the keyword is not ignored
				  this.kwicResult.add(title);
			  }
			  title = swapFirstWordToLast(title);
		  }
	  }
  }
  
  public static String getFirstWord(String title) {
	  return title.substring(0, title.indexOf(" "));
  }
  
  public static int getNumberOfWords(String title) {
	  String trimmed = title.trim();
	  int wordCount = trimmed.isEmpty() ? 0 : trimmed.split("\\s+").length;
	  return wordCount;
  }
  
  public static String swapFirstWordToLast (String title) {
	  String swappedTitle;
	  swappedTitle = title.substring(title.indexOf(" ") + 1, title.length()) + " " + getFirstWord(title);
	  return swappedTitle;
  }

  public void sortArrayAlphabetically(){
	  Collections.sort(this.kwicResult);
  }

  public void printOutput(){
    Path file = Paths.get(outputFilename);
    try {
		Files.write(file, this.kwicResult, StandardCharsets.UTF_8);
		System.out.println("KWIC successfully done. Please check the output file for the result.");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }

  public static void main(String[] args) throws IOException{
    KWIC kwic = new KWIC();
    
    kwic.getInput();
    kwic.circularShift();
    kwic.sortArrayAlphabetically();
    kwic.printOutput();
  }

//----------------------------------------------------------------------
/**
 * Inner classes
 *
 */
//----------------------------------------------------------------------

}