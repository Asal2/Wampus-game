
/**
 * 
 * Incomplete class that models a maze of caves for the "Hunt the Wumpus" game.
 * The maze of caves is defined in a file where each line is the cave number, the number of
 * adjacent caves followed by the cave number of each adjacent cave and the 
 * name of the cave. The caves will be stored in an ArrayList where the index of 
 * the cave in the list is the cave's number. It is assumed that the file is valid.
 * 
 * One cave will be initialized to store CaveContents.BAT.
 * One cave will be initialized to store CaveContents.PIT.
 * Two caves will be initialized to store CaveContents.TREASURE_BOX_FULL
 * Randomly one to three caves will be initialized to store CaveContents.WUMPUS_FREE.
 * 
 * @author Dave Reed
 * @revised by Diane Mueller
 * 
 * Document Assistance(who and describe what assistance was given or received; if no assistance, declare that fact): Took assistance from  Mueller for show location. 
 * Helped me with that strategies. Took assistance form stackoverflow on ArrayList, on collection, and on how to change arrayList to string.
 * Tutor Nhi helped me with debugging my program. Before that my program couldn't end the gave even if I had caught the wampus. 
 * The first cave i visited was false i.e unknown, 
 * Similarly the first cave should always be empty but mine was not the case.
 * Also she helped me debug showLocation, move and toss method.
 */
import java.util.*;
import java.io.*;

public class CaveMaze {
	private static final int START_CAVE_NUM = 0; // the cave the player starts from

	private Random randGen = new Random(); // used to generate random numbers

	private Cave currentCave; // The cave the player is in
	private ArrayList<Cave> caveList; // All of the caves in the maze
	private int numWumpi; // Number of uncaught wumpi in the cave maze
	
	/**
	 * 
	 * @return empty cave 
	 */
	private int getEmptyCave() {
		
		int a = randGen.nextInt(caveList.size() - 1) + 1;
		
		while (caveList.get(a).getContents() != CaveContents.EMPTY) {
			a = randGen.nextInt(caveList.size() - 1) + 1;
		}
		return a;

	}

	/**
	 * Constructs a CaveMaze from the data found in a file.
	 * 
	 * @param filename the name of the cave data file
	 * @throws FileNotFoundException if the filename received as a parameter is not
	 *                               found in the project.
	 */
	public CaveMaze(String filename) throws FileNotFoundException {
		Scanner infile = new Scanner(new File(filename));

		this.caveList = new ArrayList<Cave>();

		while (infile.hasNextLine()) {
			int caveNum = infile.nextInt();
			int numAdj = infile.nextInt();
			int[] adjCaveList = new int[numAdj];
			for (int a = 0; a < adjCaveList.length; a++) {
				adjCaveList[a] = infile.nextInt();
			}
			String caveName = infile.nextLine().trim();
			Cave cave = new Cave(caveName, adjCaveList);
			this.caveList.add(caveNum, cave);
		}
		infile.close();

		this.currentCave = caveList.get(START_CAVE_NUM);
		this.currentCave.markAsVisited();

		caveList.get(getEmptyCave()).setContents(CaveContents.BATS);
		caveList.get(getEmptyCave()).setContents(CaveContents.PIT);
		caveList.get(getEmptyCave()).setContents(CaveContents.TREASURE_BOX_FULL);
		caveList.get(getEmptyCave()).setContents(CaveContents.TREASURE_BOX_FULL);
		this.numWumpi = randGen.nextInt(3) + 1;
		for (int i = 1; i <= numWumpi; i++) {
			caveList.get(getEmptyCave()).setContents(CaveContents.WUMPUS_FREE);

		}
//		System.out.println(cavesAsString());

	}

	/**
	 * Moves the player from the current cave along the specified tunnel, marking
	 * the new cave as visited.
	 * 
	 * @param tunnelNum - the number of the tunnel to be traversed (1 through number
	 *                  of tunnels)
	 * @param player    - the player roaming the caves
	 * 
	 * @return the message depending on the tunnel and the result depending on the
	 *         contents of the cave the tunnel leads to.
	 */
	public String move(int tunnelNum, Player player) {
		if (tunnelNum < 1 || tunnelNum > currentCave.getNumAdjacent()) {
			return "There is no tunnel number " + tunnelNum;
		} else {
			int caveNum = currentCave.getAdjCaveNumber(tunnelNum);
			currentCave = caveList.get(caveNum);
			currentCave.markAsVisited();
			String message = "Moving down tunnel " + tunnelNum + "...";
			

			if (currentCave.getContents() == CaveContents.WUMPUS_FREE) {
				player.die();
				message += "You've entered a cave with a wumpus...CHOMP CHOMP CHOMP!";
			} else if (currentCave.getContents() == CaveContents.PIT) {
				player.die();
				message += "You've fallen into the bottomless pit!";
			} else if (currentCave.getContents() == CaveContents.BATS) {
				caveList.get(getEmptyCave()).setContents(CaveContents.BATS);
				currentCave = caveList.get(getEmptyCave());
				currentCave.setContents(CaveContents.EMPTY);
				currentCave.markAsVisited();
				message += "The bats dropped you into another cave!";

			} else if (currentCave.getContents() == CaveContents.TREASURE_BOX_FULL) {
				player.addGrenades(CaveContents.NUM_GRENADES_IN_TREASURE_BOX);
				message += "You found a treasure box with 2 stun grenades!";

			}

			return message;
		}
	}

	/**
	 * Attempts to toss a stun grenade through the specified tunnel. The message
	 * returned depends on whether the tunnel number is valid, whether the player
	 * actually had any grenades, and finally the contents of the the bombed cave.
	 * 
	 * @param tunnelNum - the number of the tunnel(1 through number of tunnels)
	 *                  leading to cave to be bombed
	 * @param player    - the player roaming the caves
	 * 
	 * @return the message depending on the validity of the tunnel number, whether
	 *         the player had any grenades, the contents of the bombed cave.
	 */
	public String toss(int tunnelNum, Player player) {
		String message = "";
		
		if (tunnelNum >= 1 || tunnelNum <= currentCave.getNumAdjacent()) {
			
			if (player.getNumGrenades() >= 1) {
				player.throwGrenade();
				int index = currentCave.getAdjCaveNumber(tunnelNum);
				Cave newCave = caveList.get(index);
				
				if (newCave.getContents() == CaveContents.BATS) {
					
					caveList.get(getEmptyCave()).setContents(CaveContents.BATS);
					message = "Missed, dagnabit! The bats were startled and flew to another cave!";
					currentCave = caveList.get(getEmptyCave());
				} else if (newCave.getContents() == CaveContents.WUMPUS_FREE) {
					newCave.setContents(CaveContents.WUMPUS_CAPTURED);
					numWumpi--;
					message = "You caught a wumpus!";
				}

				else if (newCave.getContents() == CaveContents.WUMPUS_CAPTURED) {

					message = "What a waste. That wumpus was already captured";
				} else {
					message = "Missed, dagnabit!";
				}
			}else {
				message = "You have no stun grenades to throw!";
			}
		} 

		return message;

	}

	/**
	 * Displays the current cave name and the names of adjacent caves. Caves that
	 * have not yet been visited are displayed as "unknown". It also gives clues
	 * warning of the wumpus, bats, or pit in a cave that can be reached via a
	 * tunnel from the current cave.
	 * 
	 * @return the message giving the current location and clues from the adjacent
	 *         caves
	 */
	public String showLocation() {
		String message = "You are currently in " + this.currentCave.getCaveName();
		
		ArrayList<String> clue = new ArrayList<String>();

		for (int i = 1; i <= currentCave.getNumAdjacent(); i++) {
			int caveNum= currentCave.getAdjCaveNumber(i);
			Cave adjCave = caveList.get(caveNum);
			
			message += "\n    (" + i + ") " + adjCave.getCaveName();
			
			String msg = "";
			
			if (adjCave.getContents() == CaveContents.WUMPUS_FREE) {
				msg = "\n You smell an awful stench coming from somewhere nearby.";
				
			}
			else if (adjCave.getContents() == CaveContents.PIT) {
				msg = "\n You feel a draft coming from one of the tunnels.";
			}
			else if (adjCave.getContents() == CaveContents.BATS) {
				msg = "\n You hear the flapping of wings close by.";
			}
			
			if(clue.contains(msg) == false) {
				clue.add(msg);
			}
		}
		
		
		Collections.shuffle(clue);
		for(String message1 : clue) {
			
			message += message1;
			
		}
		return message;
	} 

	/**
	 * Reports the number of wumpi remaining in the maze
	 * 
	 * @return the number of wumpi remaining in the maze
	 */
	public int getNumWumpi() {
		return numWumpi;
	}

	/**
	 * Reports whether there are any wumpi remaining.
	 * 
	 * @return true if there is still a wumpus in some cave
	 */
	public boolean stillWumpi() {
		return (numWumpi != 0);
	}

	// returns a string with one cave's information per line
	// Prof Mueller used this to help in debugging her program!
	private String cavesAsString() {
		String caveInfo = "";
		for (int i = 0; i < caveList.size(); i++) {
			caveInfo = caveInfo + "Cave Num: " + i + ", " + caveList.get(i) + "\n";

		}
		return caveInfo;
	}
}