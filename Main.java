/* WORD LADDER Main.java
 * EE422C Project 3 submission by
 * Replace <...> with your actual data.
 * Joshua Rothfus
 * jjr3263
 * 16445
 * Ahsan Khan
 * ajk2723
 * 16445
 * Slip days used: <0>
 * Git URL: https://github.com/ajkhan96/422C-assignment3
 * Fall 2016
 */

package assignment3;

import java.util.*;
import java.io.*;
import java.lang.reflect.Array;

public class Main {

	// static variables and constants only here.

	public static void main(String[] args) throws Exception {

		Scanner kb; // input Scanner for commands
		PrintStream ps; // output file
		// If arguments are specified, read/write from/to files instead of Std
		// IO.
		if (args.length != 0) {
			kb = new Scanner(new File(args[0]));
			ps = new PrintStream(new File(args[1]));
			System.setOut(ps); // redirect output to ps
		} else {
			kb = new Scanner(System.in);// default from Stdin
			ps = System.out; // default to Stdout
		}
		initialize();
		
		// TODO methods to read in words, output ladder
	}

	public static void initialize() {
		// initialize your static variables or constants here.
		// We will call this method before running our JUNIT tests. So call it
		// only once at the start of main.

	}

	/**
	 * @param keyboard
	 *            Scanner connected to System.in
	 * @return ArrayList of 2 Strings containing start word and end word. If
	 *         command is /quit, return empty ArrayList.
	 */
	public static ArrayList<String> parse(Scanner keyboard) {
		// TO DO
		String start = keyboard.next();
		if (start.equals("/quit"))
			return new ArrayList<String>(0);
		String end = keyboard.next();
		ArrayList<String> toReturn = new ArrayList<String>(2);
		toReturn.set(0, start);
		toReturn.set(1, end);
		return toReturn;
	}

	/**
	 * This is the main method for finding the Word Ladder using DFS.
	 * This method start by making a dictionary of the words we will be searching for.
	 * That dictionary is used to make a HashMap that will keep track of words we already used.
	 * Using these tool we enter a recursion loop that searches all the way through branches
	 * until it finds a match. When a match is found the recursion loop ends and returns an
	 * ArrayList.
	 * 
	 * @param start
	 * 			The start word for the word ladder
	 * @param end
	 * 			The end word for the word ladder
	 * @return
	 * 			Returns the ArrayList of the completed word ladder including both the start and end.
	 */
	
	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		start = start.toUpperCase();
		end = end.toUpperCase();
		// Returned list should be ordered start to end. Include start and end.
		// Return empty list if no ladder.
		Set<String> dict = makeDictionary();
		HashMap<String, Boolean> searched = new HashMap<String, Boolean>();
		Iterator<String> i = dict.iterator();
		while (i.hasNext()) {
			searched.put(i.next(), false);
		}
		
		

		String result = dfsRecur(dict, searched, start, end);
		if(result.equalsIgnoreCase("dead end")) {
			ArrayList<String> toReturn = new ArrayList<String>();
			//toReturn.add(start);
			//toReturn.add(end);
			return toReturn;
		}
		result = result.toLowerCase();
		String[] resultArray = result.split("\n");
		ArrayList<String> toReturn = new ArrayList<String>(Arrays.asList(resultArray));

		return toReturn; // replace this line later with real return
	}

	/**
	 * 
	 * This is our main method. It creates our HashMap for preventing reuse of words and our master tree for storing data.
	 * The main loop of the program adds a new branch to our master tree, checks it for matches, and then breaks the loop if
	 * a match is found or moves on to adding more rows and checking for matches. Finally, once the match has been found 
	 * the method calls readTree() to read the master tree of data and print out the word list.
	 * 
	 * @param start
	 * 			This is our start word for the word ladder.
	 * @param end
	 * 			This is our end word for the word ladder.
	 * @return
	 * 			Returns the word ladder between start and end.
	 */
	
	public static ArrayList<String> getWordLadderBFS(String start, String end) {
		start = start.toUpperCase();
		end = end.toUpperCase();
		boolean notFinished = true;
		String currentWord = start;
		Set<String> dict = makeDictionary();
		HashMap<String, Boolean> searched = new HashMap<String, Boolean>();
		Iterator<String> i = dict.iterator();
		while (i.hasNext()) {
			searched.put(i.next(), false);
		}
		List<List<String>> tree = new ArrayList<List<String>>();
		ArrayList<String> firstWord = new ArrayList<String>();
		firstWord.add(currentWord);
		tree.add(firstWord);
		while(notFinished) {
			
			for(int k = 0; k < tree.size(); k++) {
				ArrayList<String> row = findNextList(dict,searched,tree.get(k).get(0));
				tree.set(k, row);
				for(int j = 1; j < row.size(); j++) {
					if((row.get(j)).equalsIgnoreCase(end)) {
						notFinished = false;
					}
				}
				if(!notFinished) {
					break;
				}
				for(int l = 1; l < row.size(); l++) {
					ArrayList<String> firstWordInRow = new ArrayList<String>();
					firstWordInRow.add(row.get(l));
					tree.add(firstWordInRow);
				}	
			}
			
			if(notFinished) {
				ArrayList<String> wordLadder = new ArrayList<String>();
				//wordLadder.add(start);
				//wordLadder.add(end);
				return wordLadder;
			}
			
			
		}
		ArrayList<String> wordLadder = readTree(tree, start, end);
		for (int j = 0; j < wordLadder.size(); j++) {
			wordLadder.set(j,wordLadder.get(j).toLowerCase());
		}
		return wordLadder;
	}

		
	/**
	 * This is the "read" method for our massive tree of data which is now complete.
	 * It reads through the tree finding all the branches that connect start to end.
	 * 
	 * @param tree
	 * 			This is our master tree that stores all our data of words and their branches
	 * @param start
	 * 			This is our start word for the word ladder.
	 * @param end
	 * 			This is our end word for the word ladder.
	 * @return
	 * 			This method returns an ArrayList containing the word ladder from start to end.
	 */
	
	private static ArrayList<String> readTree(List<List<String>> tree, String start, String end) {
		ArrayList<String> wordLadder = new ArrayList<String>();
		String endString = end;
		while(endString != start) {
			if(tree.get(tree.size()-1).contains(endString)) {
				wordLadder.add(endString);
				endString = tree.get(tree.size()-1).get(0);
				tree.remove(tree.size()-1);
			}
			else {
				tree.remove(tree.size()-1);
			}	
		}
		wordLadder.add(start);
		Collections.reverse(wordLadder);
		return wordLadder;
	}
	
	/**
	 * findNextList returns an ArrayList of Strings where index zero is the
	 * word to be matched and all the words after it are words that differ by a single letter.
	 * NOTE: All matches take into account words that have already been listed as matches.
	 * 
	 * @param dict
	 * 			The dictionary of words to be searched from.
	 * @param searched
	 * 			The HashMap of words we've already used.
	 * @param matchWord
	 * 			The word we are trying to find matches for
	 * @return
	 * 			Returning an ArrayList of all the words that match matchWord + matchWord itself at index 0;
	 */
	
	private static ArrayList<String> findNextList(Set<String> dict,
			HashMap<String, Boolean> searched, String matchWord) {

		ArrayList<String> row = new ArrayList<String>();
		row.add(matchWord);
		Iterator<String> i = dict.iterator();
		while (i.hasNext()) {
			String next = i.next();
			if (!(boolean) searched.get(next)) {
				int sum = 0;
				for (int j = 0; j < matchWord.length(); j++) {
					if (next.charAt(j) != matchWord.charAt(j)) {
						sum += 1;
					}
				}
				if (sum == 1) {
					row.add(next);
					searched.put(next, true);
				}
			}

		}
		return row; 
	}

	public static Set<String> makeDictionary() {
		Set<String> words = new HashSet<String>();
		Scanner infile = null;
		try {
			infile = new Scanner(new File("five_letter_words.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Dictionary File not Found!");
			e.printStackTrace();
			System.exit(1);
		}
		while (infile.hasNext()) {
			words.add(infile.next().toUpperCase());
		}
		return words;
	}

	public static void printLadder(ArrayList<String> ladder) {
		int sum = 0;
		for (int j = 0; j < ladder.get(0).length(); j++) {
			if (ladder.get(1).charAt(j) != ladder.get(0).charAt(j)) {
				sum += 1;
			}
		}
		if(sum == 1) {
			System.out.println("a " + (ladder.size()-2) + "-rung word ladder exists between " + ladder.get(0) + " and " + ladder.get(ladder.size()-1));
			for (int i = 0; i < ladder.size(); i++) {
				System.out.println(ladder.get(i));
			}
		}
		else if(ladder.size() == 0) {
			System.out.println("no word ladder can be found between " + ladder.get(0) + " and " + ladder.get(ladder.size()-1));
		}
		else {
			System.out.println("a " + (ladder.size()-2) + "-rung word ladder exists between " + ladder.get(0) + " and " + ladder.get(ladder.size()-1));
			for (int i = 0; i < ladder.size(); i++) {
				System.out.println(ladder.get(i));
			}
		}
		
	}

	// TODO
	// Other private static methods here

	public static String dfsRecur(Set<String> dict, HashMap<String, Boolean> searched, String start, String end) {
		searched.put(start, true);
		if (start.equalsIgnoreCase(end)) {
			return start;
		} else {
			String wordNext = findLadderNext(searched, dict, start, end);
			while (!wordNext.equals("NF")) {
				String result = dfsRecur(dict, searched, wordNext, end);
				if (result != "Dead End") {
					return (start + "\n" + result);
				} else {
					wordNext = findLadderNext(searched, dict, start, end);
				}
			}
			return "Dead End";
		}
	}

	/**
	 * Given a dictionary and a searched HashMap, returns the next word that
	 * hasn't been searched already and is one letter away
	 * 
	 * @param searched
	 *            HashMap describing which words have been searched already
	 * @param dict
	 *            Set of words
	 * @param startWord
	 *            Base word to find next of
	 * @return "NF" if next candidate is not found, next word if found
	 */
	private static String findLadderNext(HashMap searched, Set<String> dict, String startWord, String endWord) {
		Iterator<String> i = dict.iterator();
		String closest = startWord;
		while (i.hasNext()) {
			String next = i.next();
			if (!(boolean) searched.get(next)) {
				int sum = 0;
				for (int j = 0; j < startWord.length(); j++) {
					if (next.charAt(j) != startWord.charAt(j)) {
						sum += 1;
					}
				}
				if (sum == 1) {
					int closeDifferences = 0;
					int nextDifferences = 0;
					for (int j = 0; j < endWord.length(); j++) {
						if (closest.charAt(j) != endWord.charAt(j)) {
							closeDifferences += 1;
						}
						if (next.charAt(j) != endWord.charAt(j)) {
							nextDifferences += 1;
						}
					}
					if (closest.equals(startWord) || nextDifferences < closeDifferences) {
						closest = next;
					}
				}
			}
		}
		if (closest.equals(startWord)) {
			return "NF";
		} else {
			return closest;
		}
	}
}
