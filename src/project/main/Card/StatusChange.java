package project.main.Card;

import java.util.UUID;

/**
 * Holds the status changes of a summon
 * @author Benjamin Merkle
 *
 */
public class StatusChange {
	
	public static final String DAMAGE = "damage";
	public static final String MAXVITALITY = "maxVitality";
	public static final String ATTACK = "attack";
	public static final String HEAL = "heal";
	public static final String INITIATIVE = "initiative";
	public static final String MAGICPRESERVATION = "magicPreservationValue";
	public static final String MAGICWASTAGE = "magicWastageOnDefeat";
	public static final String SUMMONINGPOINT = "summoningPoints";
	public static final String ELEMENT = "element";
	public static final String SUMMONCLASS = "summonClass";
	public static final String TYPE_ADDITION = "A";
	public static final String TYPE_POS_MULT = "*";
	public static final String TYPE_NEG_MULT = "/";
	
	/**
	 * The status which has been changed
	 */
	private String status;
	/**
	 * The key of the status change
	 */
	private UUID changeKey;
	/**
	 * The type of the change (A = Addition, * and / = Multiplicator)
	 */
	private String changeType;
	/**
	 * Value of the status change (int)
	 */
	private int value;
	/**
	 * Value of the status change (String)
	 */
	private String valueString;
	
	public StatusChange(String status, UUID key, String changeType, int value) {
		this.status = status;
		this.changeKey = key;
		this.changeType = changeType;
		this.value = value;
	}
	
	public StatusChange(String status, UUID key, String changeType, String value) {
		this.status = status;
		this.changeKey = key;
		this.changeType = changeType;
		this.valueString = value;
	}

	public String getStatus() {
		return status;
	}

	public UUID getChangeKey() {
		return changeKey;
	}

	public String getChangeType() {
		return changeType;
	}

	public int getValue() {
		return value;
	}
	
	public String getValueString() {
		return valueString;
	}
	
}
