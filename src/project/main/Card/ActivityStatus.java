package project.main.Card;

public class ActivityStatus {
	public static final String READY = "r";
	public static final String USED = "u";
	public static final String IMMOBILIZED = "i";
	public static final String NOT_IN_GAME = "n";
	
	private String status;
	private int durability;
	
	public ActivityStatus() {
		status = READY;
		durability = -1;
	}
	
	/**
	 * Retrieves the status
	 * @return 	r = READY 		: Can be used in any possible way
	 * 			u = USED 		: Only actions possible which are in used state allowed; 
	 * 					   		if not otherwise declared this valid until next Refreshment Phase of owner 
	 * 			i = IMMOBILIZED : The Summon can only defend itself. 
	 * 							If set this status is permanent until it is changed or the durability reaches 0 
	 * 			n = NOT_IN_GAME : The Summon is only part of Hierarchy but not active in game. 
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * Sets the status and the durability of the status
	 * @param status of a summon
	 * 			r = READY 		: Can be used in any possible way
	 * 			u = USED 		: Only actions possible which are in used state allowed; 
	 * 					   		if not otherwise declared this valid until next Refreshment Phase of owner 
	 * 			i = IMMOBILIZED : The Summon can only defend itself. 
	 * 							If set this status is permanent until it is changed or the durability reaches 0 
	 * 			n = NOT_IN_GAME : The Summon is only part of Hierarchy but not active in game. This is normally permanent until it is changed.
	 * @param durability of the status which defines how many rounds the status is valid. If -1 the status is permanent
	 */
	public void setStatus(String status, int durability) {
		this.status = status;
		this.durability = durability;
	}
	/**
	 * Retrieves the durability of the status
	 * @return int value = 0..n: Number of rounds where the status is valid; -1: The status is permanent. 
	 */
	public int getDurability() {
		return durability;
	}
	/**
	 * Decreases the durability by 1
	 */
	public void decreaseDurability() {
		if(durability > 0) {
			durability--;
		}
	}
}
