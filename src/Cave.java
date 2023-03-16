import java.util.Arrays;

/**
 * Cave describes one cave in the cave maze including its name,
 * the caves that can be reaches via a tunnel from this cave,
 * whether the player has visited this maze yet, and the contents
 * of the cave.
 * 
 * 
 */


public class Cave {

	/**
	 * data fields
	 */
	private String caveName;
	private int[] caveArray;
	private CaveContents cave;
	private boolean hasVisited;

	/**
	 * constructor gives a name to a cave and array receives as a parameter
	 * 
	 * @param name
	 * @param adjCaveArray
	 */
	public Cave(String name, int[] adjCaveArray) {
		this.caveName = name;
		this.caveArray = adjCaveArray;
		this.cave = CaveContents.EMPTY;
		this.hasVisited = false;

	}

	/**
	 * getAdjCaveNumber checks the number of cave it can be reached via tunnel
	 * 
	 * @param tunnelNumif tunnelNum is invalid
	 * @return the number of cave visited or if tunnelNum is invalid return -1;
	 */
	public int getAdjCaveNumber(int tunnelNum) {

		if (tunnelNum > 0 && tunnelNum <= this.caveArray.length) {
			return caveArray[tunnelNum - 1];
		} else {
			return -1;
		}
	}

	/**
	 * getCaveName checks if the cave has been visited or not
	 * 
	 * @return true if visited else return unknown
	 */
	public String getCaveName() {
		if (hasVisited) {
			return caveName;
		} else {
			return "unknown";
		}
	}

	/**
	 * CaveContents return what is in the cave
	 * 
	 * @return returns what is in the cave
	 */
	public CaveContents getContents() {
		return this.cave;
	}
	
	public int getNumAdjacent() {
		return this.caveArray.length;
	}

	/**
	 * markAsVisited set the cave visited as true
	 */
	public void markAsVisited() {
		this.hasVisited = true;
	}

	/**
	 * setContents
	 */
	public void setContents(CaveContents contents) {
		cave = contents;
	}

	public String toString() {
		return "Cave name: " + caveName + ", Adj Cave Array: " + Arrays.toString(caveArray) + ", Visited: " + hasVisited
				+ ", Contents: " + cave;
	}
}
