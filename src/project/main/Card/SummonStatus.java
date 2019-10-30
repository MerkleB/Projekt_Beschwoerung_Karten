package project.main.Card;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.UUID;

public class SummonStatus {
	private int magicPreservationValue;
	private int summoningPoints;
	private int attack;
	private int heal;
	private int vitality;
	private int initiative;
	private int maxVitality;
	private String summonClass;
	private String element;
	private int magicWastageOnDefeat;
	private TreeMap<UUID, StatusChange> statusChanges;
	private TreeMap<String, ArrayList<StatusChange>> statusChangesByStatus;
	private HashMap<UUID, Integer> statusChangeIndex;
	
	public SummonStatus(int preserVationValue, int summoningPoints, int attack, 
						int heal, int vitality, int initiative, String summonClass, 
						String element, int magicWastageOnDefeat) {
		magicPreservationValue = preserVationValue;
		this.summoningPoints = summoningPoints;
		this.attack = attack;
		this.heal = heal;
		this.maxVitality = vitality;
		this.vitality = vitality;
		this.initiative = initiative;
		this.summonClass = summonClass;
		this.element = element;
		this.magicWastageOnDefeat = magicWastageOnDefeat;
		statusChanges = new TreeMap<UUID, StatusChange>();
		statusChangesByStatus = new TreeMap<String, ArrayList<StatusChange>>();
		statusChangeIndex = new HashMap<UUID, Integer>();
	}
	
	public void addStatusChange(StatusChange change) {
		statusChanges.put(change.getChangeKey(), change);
		
		ArrayList<StatusChange> changes;
		if(statusChangesByStatus.containsKey(change.getStatus())) {
			changes = statusChangesByStatus.get(change.getStatus());
		}else {
			changes = new ArrayList<StatusChange>();
			statusChangesByStatus.put(change.getStatus(), changes);
		}
		statusChangeIndex.put(change.getChangeKey(), new Integer(changes.size()));
		changes.add(change);
	}
	
	public void removeStatus(UUID key) {
		StatusChange change = statusChanges.get(key);
		if(change == null) return;
		statusChanges.remove(key);
		int index = statusChangeIndex.get(key).intValue();
		statusChangeIndex.remove(key);
		statusChangesByStatus.get(change.getStatus()).remove(index);
	}
	
	public void removeAll() {
		statusChangeIndex.clear();
		statusChanges.clear();
		statusChangesByStatus.clear();
	}
	
	public void removeAll(String status) {
		ArrayList<StatusChange> changes = statusChangesByStatus.get(status);
		if(changes == null) return;
		
		changes.clear();
		ArrayList<UUID> uuidsToRemove = new ArrayList<UUID>();
		statusChanges.forEach((k,e)->{
			if(e.getStatus().equals(status)) {
				uuidsToRemove.add(k);
			}
		});
		for(UUID id : uuidsToRemove) {
			statusChanges.remove(id);
			statusChangeIndex.remove(id);
		}
	}
	
	public ArrayList<StatusChange> getChanges(String status){
		return statusChangesByStatus.get(status);
	}
	
	public int getMagicPreservationValue(){
		int scMagicPreservationValue = getChangedStatus(magicPreservationValue, StatusChange.MAGICPRESERVATION);
		return scMagicPreservationValue;
	}

	public int getSummoningPoints(){
		int scSummoningPoints = getChangedStatus(summoningPoints, StatusChange.SUMMONINGPOINT);
		return scSummoningPoints;
	}

	public int getAttack(){
		int scAttack = getChangedStatus(attack, StatusChange.ATTACK);
		return scAttack;
	}
	
	public int getInitiative() {
		int scInit = getChangedStatus(initiative, StatusChange.INITIATIVE);
		return scInit; 
	}

	public int getHeal(){
		int scHeal = getChangedStatus(heal, StatusChange.HEAL);
		return scHeal;
	}

	public int getVitality(){
		return vitality;
	}
	
	public int decreaseVitality(int damage){
		//TODO: Raise damageEvent
		int scDamage = getChangedStatus(damage, StatusChange.DAMAGE);
		if(vitality > scDamage) {
			vitality = vitality - scDamage;
		}else {
			vitality = 0;
		}
		return vitality;
	}
	
	public int increaseVitality(int heal){
		//TODO: Raise healEvent
		int scMaxVitality = getChangedStatus(maxVitality, StatusChange.MAXVITALITY);
		if(scMaxVitality > vitality + heal) {
			vitality = vitality + heal;
		}else {
			vitality = scMaxVitality;
		}
		return vitality;
	}

	public int getMaxVitality() {
		int scMaxVitality = getChangedStatus(maxVitality, StatusChange.MAXVITALITY);
		return scMaxVitality;
	}

	public String getSummonClass() {
		String scSummonClass = getChangedStatus(summonClass, StatusChange.SUMMONCLASS); 
		return scSummonClass;
	}
	
	public String getElement() {
		String scElement = getChangedStatus(element, StatusChange.ELEMENT);
		return scElement;
	}

	public int getMagicWastageOnDefeat() {
		int scMagicWastageOnDefeat = getChangedStatus(magicWastageOnDefeat, StatusChange.MAGICWASTAGE);
		return scMagicWastageOnDefeat;
	}
	
	private int getChangedStatus(int statusValue, String status) {
		ArrayList<StatusChange> statusChanges = statusChangesByStatus.get(status);
		if(statusChanges != null) {
			ArrayList<StatusChange> additiveChanges = new ArrayList<StatusChange>();
			ArrayList<StatusChange> multiplicativChanges = new ArrayList<StatusChange>();
			for(StatusChange change : statusChanges) {
				if(change.getChangeType().equals("A")) {
					additiveChanges.add(change);
				}else {
					multiplicativChanges.add(change);
				}
			}
			statusValue = multiplicateStatus(statusValue, multiplicativChanges);
			statusValue = sumStatus(statusValue, additiveChanges);
		}
		return statusValue;
	}
	
	private int multiplicateStatus(int statusValue, ArrayList<StatusChange> mulitplicators) {
		for(StatusChange change : mulitplicators) {
			if(change.getChangeType().equals("*")) {
				statusValue = (int)(statusValue * change.getValue());
			}else {
				statusValue = (int)(statusValue / change.getValue());
			}
		}
		return statusValue;
	}
	
	private int sumStatus(int statusValue, ArrayList<StatusChange> additors) {
		for(StatusChange change : additors) {
			statusValue = statusValue + change.getValue();
		}
		return statusValue;
	}
	
	private String getChangedStatus(String statusValue, String status) {
		ArrayList<StatusChange> changes = statusChangesByStatus.get(status);
		if(changes != null) {
			if(changes.size() > 0) {
				statusValue = changes.get(changes.size()-1).getValueString();
			}
		}
		
		return statusValue;
	}
}
