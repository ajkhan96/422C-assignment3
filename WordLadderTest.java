package assignment3;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.junit.Test;

public class WordLadderTest {

	@Test
	public void testSimpleBFS() {
		Main testMain = new Main();
		ArrayList<String> recieved = testMain.getWordLadderBFS("MONEY", "BUNNY");
		assertFalse(recieved.size() <= 2);
		for (int i = 0; i < recieved.size(); i++) {
			for (int j = i+1; j < recieved.size(); j++) {
					assertFalse((recieved.get(i)).equalsIgnoreCase(recieved.get(j)));	
			}
		}
	}
	
	@Test
	public void testSimpleDFS() {
		Main testMain = new Main();
		ArrayList<String> recieved = testMain.getWordLadderDFS("MONEY", "BUNNY");
		assertFalse(recieved.size() <= 2);
		for (int i = 0; i < recieved.size(); i++) {
			for (int j = i+1; j < recieved.size(); j++) {
					assertFalse((recieved.get(i)).equalsIgnoreCase(recieved.get(j)));	
			}
		}
	}
	
	@Test
	public void testDFSWithNoLadder() {
		Main testMain = new Main();
		ArrayList<String> recieved = testMain.getWordLadderDFS("MONEY", "ZZZZZ");
		assertFalse(recieved.size() != 0);
	}
	
	@Test
	public void testBFSOneWordAway() {
		Main testMain = new Main();
		ArrayList<String> recieved = testMain.getWordLadderDFS("CORES", "CONES");
		assertFalse(recieved.size() != 2);
		ArrayList<String> expected = new ArrayList<String>();
		expected.add("cores");
		expected.add("cones");
		assertFalse(!recieved.get(0).equals(expected.get(0)));
		assertFalse(!recieved.get(1).equals(expected.get(1)));
	}
	
	@Test
	public void testPrintLadderGeneral() {
		Main testMain = new Main();
		ArrayList<String> givenList = new ArrayList<String>();
		givenList.add("money");
		givenList.add("boney");
		givenList.add("bonny");
		givenList.add("bunny");
		testMain.printLadder(givenList);
		
	}

}
