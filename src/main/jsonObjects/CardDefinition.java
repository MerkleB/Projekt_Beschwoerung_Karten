package main.jsonObjects;

public class CardDefinition {
	public String name;
	public String card_id;
	public String type;
	public int maxEnergy;
	public int maxHealth;
	public String trivia;
	public EffectDefinition[] effects;
	public int magicPreservationValue;
	public int summoningPoints;
	public int attack;
	public int heal;
	public int maxVitality;
	public String summonClass;
	public String rank;
	public SummonAscentHierarchyDefinition[] summonHierarchy;
	public String element;
	public int magicWastageOnDefeat;
	public int neededMagicEnergy;
	
	@Override
	public boolean equals(Object obj) {
		CardDefinition otherDefinition;
		
		if(obj instanceof CardDefinition) {
			otherDefinition = (CardDefinition) obj;
		}else return false;
		
		if(name.equals(otherDefinition.name) == false) return false;
		if(card_id.equals(otherDefinition.card_id) == false) return false;
		if(type.equals(otherDefinition.type) == false) return false;
		if(maxEnergy != otherDefinition.maxEnergy) return false;
		if(maxHealth != otherDefinition.maxHealth) return false;
		if(trivia.equals(otherDefinition.trivia) == false) return false;
		if(magicPreservationValue != otherDefinition.magicPreservationValue) return false;
		if(summoningPoints != otherDefinition.summoningPoints) return false;
		if(attack != otherDefinition.attack) return false;
		if(heal != otherDefinition.heal) return false;
		if(maxVitality != otherDefinition.maxVitality) return false;
		if(summonClass.equals(otherDefinition.summonClass) == false) return false;
		if(rank.equals(otherDefinition.rank) == false) return false;
		if(element.equals(otherDefinition.element) == false) return false;
		if(magicWastageOnDefeat != otherDefinition.magicWastageOnDefeat) return false;
		if(neededMagicEnergy != otherDefinition.neededMagicEnergy) return false;
	
		
		if(effects.length != otherDefinition.effects.length) {
			return false;
		}
		
		for(int i=0; i<effects.length; i++) {
			if(effects[i].effectClass.equals(otherDefinition.effects[i].effectClass) == false) {
				return false;
			}
		}
		
		if(summonHierarchy.length != otherDefinition.summonHierarchy.length) {
			return false;
		}
		
		for(int i=0; i<summonHierarchy.length; i++) {
			if(summonHierarchy[i].card_id.equals(otherDefinition.summonHierarchy[i].card_id) == false || summonHierarchy[i].level != otherDefinition.summonHierarchy[i].level) {
				return false;
			}
		}
		
		return true;
	}
	
	
}
