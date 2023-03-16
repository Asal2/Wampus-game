/**
 * 
 * Player defines the "human" player for the Hunt the Wumpus game. The player
 * gets a certain number of grenades for every wumpus in the maze.
 * 
 * Document Assistance(who and describe; if no assistance, declare that fact): no assistance was taken
 * 
 */
public class Player {
	public static final int NUM_GRENADES_PER_WUMPI = 2; // number of stun grenades given for each wumpus in the maze

	private int numGrenades; // the current number of stun grenades this player has
	private boolean isAlive; // whether this player is alive or not
	
	/**
	 * Player is a constructor that creates player that is alive with number of grenades left to kill the wumpi.
	 * @param numWumpi
	 */
	public Player(int numWumpi) {
		this.isAlive = true;
		this.numGrenades = NUM_GRENADES_PER_WUMPI * numWumpi;
	}
	
	/** alive checks if the player is alive or not.
	 * 
	 * @return if the player is alive or not.
	 */
	public boolean alive() {
		return isAlive;
	}
	
	/** die set the player life to false
	 * 
	 */
	public void die() {
		this.isAlive = false;
	}
	
	/** getNumGrenades get the number of grenades that is available to the player
	 * 
	 * @return the current number of grenade available
	 */
	public int getNumGrenades() {
		return numGrenades;
	}
	
	/** addGrenades adds the total number of grenade when collected by the player
	 * 
	 * @param additionalGrenades
	 */
	public void addGrenades(int additionalGrenades) {
		this.numGrenades = this.numGrenades + additionalGrenades;
	}
	
	/** throwGrenade check if the player is alive and has the grenade to throw.
	 * 
	 * @return the number of grenade left if the player is alive and has more that 0 grenade.
	 */
	public boolean throwGrenade() {
		if (isAlive == true && numGrenades > 0) {
			numGrenades--;
			return true;
		} else {
			return false;
		}
	}
	
	/** toString converts other type of parameter to String to avoid over-ride and get a proper output
	 * 
	 * @return the output to String with proper format
	 */
	public String toString() {
		return "alive: " + isAlive + ", num grenades: " + numGrenades;
	}

}
