/* WORD LADDER Main.java
 * EE422C Project 3 submission by
 * Replace <...> with your actual data.
 * <Student1 Name>
 * <Student1 EID>
 * <Student1 5-digit Unique No.>
 * <Student2 Name>
 * <Student2 EID>
 * <Student2 5-digit Unique No.>
 * Slip days used: <0>
 * Git URL:
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

		ArrayList<String> ret = getWordLadderBFS("ALBAS", "ZYMES");
		System.out.println(ret.toString());
		
		// TODO methods to read in words, output ladder
		kb.close();
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

	public static ArrayList<String> getWordLadderDFS(String start, String end) {

		// Returned list should be ordered start to end. Include start and end.
		// Return empty list if no ladder.
		// TODO some code
		Set<String> dict = makeDictionary();
		// TODO more code
		HashMap<String, Boolean> searched = new HashMap<String, Boolean>();
		Iterator<String> i = dict.iterator();
		while (i.hasNext()) {
			searched.put(i.next(), false);
		}

		String result = dfsRecur(dict, searched, start, end);
		result.toLowerCase();
		String[] resultArray = result.split("\n");
		ArrayList<String> toReturn = new ArrayList<String>(Arrays.asList(resultArray));

		return toReturn; // replace this line later with real return
	}

	public static ArrayList<String> getWordLadderBFS(String start, String end) {
		boolean notFinished = true;
		String currentWord = start;
		int finalIndex = 1;
		Set<String> dict = makeDictionary();
		// TODO some code
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
				System.out.println(row.toString());
				tree.set(k, row);
				for(int j = 1; j < row.size(); j++) {
					if((row.get(j)).equalsIgnoreCase(end)) {
						System.out.println("Found matching StringList: " + row.toString());
						notFinished = false;
						finalIndex = j;
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
				//System.out.println(row.toString());	
			}
			
			if(notFinished) {
				//TODO Whatever happens if no word list is found
				return null;
			}
			
			
		}
		//ArrayList<String> finalArray = new ArrayList<String>();
		//finalArray.add(start);
		//for(int j = 0; j < r)
		return readTree(tree, start, end);
	}
		
	
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
