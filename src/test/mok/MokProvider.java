package test.mok;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.TreeMap;
import java.util.UUID;

import main.Action.Effect;
import main.Action.GameAction;
import main.Action.Stackable;
import main.Card.Card;
import main.GameApplication.IsAreaInGame;
import main.GameApplication.IsPhaseInGame;
import main.GameApplication.Player;
import main.build_cards.CreatesActions;
import main.build_cards.CreatesEffects;
import main.exception.NotActivableException;
import main.jsonObjects.CardDefinition;
import main.jsonObjects.EffectDefinition;
import main.jsonObjects.HoldsActionDefinitions;
import main.jsonObjects.HoldsCardDefinitions;
import main.jsonObjects.SummonAscentHierarchyDefinition;
import main.util.mapsRankAndLevel;

public class MokProvider {
	public static Effect getEffect() {
		return new Effect(){
		
			@Override
			public void execute() {
				
			}
		
			@Override
			public boolean isExecutable() {
				return false;
			}
		
			@Override
			public String getDescription() {
				return "This is an effect.";
			}

			@Override
			public String getName() {
				return null;
			}

			@Override
			public Card getOwningCard() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void setOwningCard(Card owningCard) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void withdraw() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setInactiv() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public Card getCard() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void setCard(Card card) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public Hashtable<String, String> getMetaData() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public boolean equals(Object obj) {
				return true;
			}

			@Override
			public boolean isWithdrawn() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void activate(Player activator) throws NotActivableException {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean activateable(Player activator) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void setActiv(Player activFor) {
				// TODO Auto-generated method stub
				
			}
		};
	}
	
	public static Player getPlayer() {
		return new Player() {
			
			private ArrayList<IsAreaInGame> zones;
			private UUID id = UUID.randomUUID();
			
			@Override
			public IsAreaInGame getGameZone(String zoneName) {
				for(IsAreaInGame zone : zones) {
					if(zone.getName().equals(zoneName)) {
						return zone;
					}
				}
				return null;
			}
			
			@Override
			public boolean equals(Object obj) {
				return true;
			}

			@Override
			public int getFreeEnergy() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int getUsedEnergy() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int getDepletedEnergy() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int getSummoningPoints() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int restoreFreeEnergy() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int increaseFreeEnergy(int magicEnergy) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int decreaseFreeEnergy(int magicEnergy) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int increaseFreeEnergyFromUsed(int magicEnergy) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int increaseFreeEnergyFromDepleted(int magicEnergy) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int useEnergy(int magicEnergy) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int depleteEnergyFromFree(int magicEnergy) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int depleteEnergyFromUsed(int magicEnergy) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public UUID getID() {
				return id;
			}

			@Override
			public int decreaseSummonigPoints(int costs) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int addSummoningPoints(int points) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public ArrayList<IsAreaInGame> getGameZones() {
				return zones;
			}
		};
	}

	public static GameAction getGameAction(String actionName){
		return new GameAction(){
			
			public boolean withdrawn = false;
			public boolean activ = false;
			public boolean isActivated = false;
			
			@Override
			public String getName() {
				return actionName;
			}

			@Override
			public void execute() {
				if(activ && isActivated);
				//do nothing
			}

			@Override
			public void withdraw() {
				withdrawn = true;
			}

			@Override
			public void setInactiv() {
				activ = false;
			}

			@Override
			public Card getCard() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void setCard(Card card) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public Hashtable<String, String> getMetaData() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public boolean equals(Object obj) {
				return true;
			}

			@Override
			public boolean isWithdrawn() {
				// TODO Auto-generated method stub
				return withdrawn;
			}

			@Override
			public void activate(Player activator) throws NotActivableException {
				isActivated = true;
				
			}

			@Override
			public boolean activateable(Player activator) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void setActiv(Player activFor) {
				activ = true;
			}

			@Override
			public void activateBy(Stackable stackable, Player activator) throws NotActivableException {
				isActivated = true;
			}

			@Override
			public void setActivBy(Stackable stackable, Player player) {
				activ = true;
			}

			@Override
			public Stackable getActivatingStackable() {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}
	
	public static HoldsActionDefinitions getActionDefinitions() {
		return new HoldsActionDefinitions() {
			
			private boolean initialized;
			private ArrayList<String> zoneActions1;
			private ArrayList<String> zoneActions2;
			private ArrayList<String> zoneActions3;
			private TreeMap<String, ArrayList<String>> zoneActions;
			private ArrayList<String> phaseActions1;
			private ArrayList<String> phaseActions2;
			private ArrayList<String> phaseActions3;
			private ArrayList<String> phaseActions4;
			private TreeMap<String, ArrayList<String>> phaseActions;
			private ArrayList<String> cardActions1;
			private ArrayList<String> cardActions2;
			private ArrayList<String> cardActions3;
			private TreeMap<String, ArrayList<String>> cardActions;
			
			private void init() {
				if(!initialized) {
					initialized = true;
					zoneActions = new TreeMap<String, ArrayList<String>>();
					phaseActions = new TreeMap<String, ArrayList<String>>();
					cardActions = new TreeMap<String, ArrayList<String>>();
					
					zoneActions1 = new ArrayList<String>();
					zoneActions1.add("Summon");
					zoneActions1.add("Cast");
					zoneActions1.add("SetAsCollector");
					zoneActions2 = new ArrayList<String>();
					zoneActions2.add("Promote");
					zoneActions2.add("AttackPlayer");
					zoneActions2.add("Retract");
					zoneActions3 = new ArrayList<String>();
					zoneActions3.add("Draw");
					zoneActions.put("HandZone", zoneActions1);
					zoneActions.put("SummonZone", zoneActions2);
					zoneActions.put("DeckZone", zoneActions3);
					
					phaseActions1 = new ArrayList<String>();
					phaseActions1.add("Summon");
					phaseActions1.add("Cast");
					phaseActions1.add("SetAsCollector");
					phaseActions1.add("AttackPlayer");
					phaseActions1.add("Promote");
					phaseActions1.add("CallBack");
					phaseActions2 = new ArrayList<String>();
					phaseActions2.add("Cast");
					phaseActions3 = new ArrayList<String>();
					phaseActions3.add("Draw");
					phaseActions4 = new ArrayList<String>();
					phaseActions4.add("Retract");
					phaseActions.put("MainPhase", phaseActions1);
					phaseActions.put("CombatPhase", phaseActions2);
					phaseActions.put("DrawPhase", phaseActions3);
					phaseActions.put("ClashEndPhase", phaseActions4);
					
					cardActions1 = new ArrayList<String>();
					cardActions1.add("Summon");
					cardActions1.add("SetAsCollector");
					cardActions1.add("Promote");
					cardActions1.add("AttackPlayer");
					cardActions1.add("Draw");
					cardActions1.add("Retract");
					cardActions2 = new ArrayList<String>();
					cardActions2.add("Cast");
					cardActions2.add("SetAsCollector");
					cardActions2.add("Draw");
					cardActions3 = new ArrayList<String>();
					cardActions3.add("CallBack");
					cardActions.put("Summon", cardActions1);
					cardActions.put("Spell", cardActions2);
					cardActions.put("MagicCollector", cardActions3);
				}
			}
			
			@Override
			public ArrayList<String> getZoneActions(String zone) {
				init();
				return zoneActions.get(zone);
			}
			
			@Override
			public TreeMap<String, ArrayList<String>> getZoneActions() {
				init();
				return zoneActions;
			}
			
			@Override
			public ArrayList<String> getPhaseActions(String phase) {
				init();
				return phaseActions.get(phase);
			}
			
			@Override
			public TreeMap<String, ArrayList<String>> getPhaseActions() {
				init();
				return phaseActions;
			}
			
			@Override
			public ArrayList<String> getCardActions(String cardType) {
				init();
				return cardActions.get(cardType);
			}
			
			@Override
			public TreeMap<String, ArrayList<String>> getCardActions() {
				init();
				return cardActions;
			}
		};
	}
	
	public static HoldsCardDefinitions getCardDefinitions() {
		return new HoldsCardDefinitions() {
			
			private CardDefinition cardDefinition;
			private CardDefinition cardDefinition2;
			private CardDefinition cardDefinition3;
			private CardDefinition cardDefinition4;
			private TreeMap<String, CardDefinition> set;
			private boolean initialized;
			
			private void init() {
				if(!initialized) {
					initialized = true;
					
					EffectDefinition ed = new EffectDefinition();
					ed.effectClass = "effect_000";
					EffectDefinition[] eds = {ed};
					SummonAscentHierarchyDefinition sahd1 = new SummonAscentHierarchyDefinition();
					sahd1.level = 1;
					sahd1.card_id = "bsc-su-00-1";
					SummonAscentHierarchyDefinition sahd2 = new SummonAscentHierarchyDefinition();
					sahd2.level = 2;
					sahd2.card_id = "bsc-su-00-2";
					SummonAscentHierarchyDefinition[] sahd = {sahd1, sahd2};
					
					cardDefinition = new CardDefinition(); 
					cardDefinition.name = "Aries";
					cardDefinition.card_id = "bsc-su-00-0";
					cardDefinition.type = "Summon";
					cardDefinition.maxEnergy = 5;
					cardDefinition.maxHealth = 8;
					cardDefinition.trivia = "Ein sagenhafter Widder der einst Phrixos und seine Schwester Helle vor ihrer Stiefmutter Ino rettete";
					cardDefinition.effects = eds;
					cardDefinition.magicPreservationValue = 2;
					cardDefinition.summoningPoints = 1;
					cardDefinition.attack = 2;
					cardDefinition.heal = 3;
					cardDefinition.maxVitality = 8;
					cardDefinition.summonClass = "NaturalBeast";
					cardDefinition.rank = "Cub";
					cardDefinition.summonHierarchy = sahd;
					cardDefinition.element = "Fire";
					cardDefinition.magicWastageOnDefeat = 1;
					cardDefinition.neededMagicEnergy = 0;
					
					cardDefinition2 = new CardDefinition(); 
					cardDefinition2.name = "Aries";
					cardDefinition2.card_id = "bsc-su-00-1";
					cardDefinition2.type = "Summon";
					cardDefinition2.maxEnergy = 5;
					cardDefinition2.maxHealth = 8;
					cardDefinition2.trivia = "Ein sagenhafter Widder der einst Phrixos und seine Schwester Helle vor ihrer Stiefmutter Ino rettete";
					cardDefinition2.effects = eds;
					cardDefinition2.magicPreservationValue = 2;
					cardDefinition2.summoningPoints = 1;
					cardDefinition2.attack = 3;
					cardDefinition2.heal = 6;
					cardDefinition2.maxVitality = 10;
					cardDefinition2.summonClass = "NaturalBeast";
					cardDefinition2.rank = "Adult";
					cardDefinition2.summonHierarchy = sahd;
					cardDefinition2.element = "Fire";
					cardDefinition2.magicWastageOnDefeat = 2;
					cardDefinition2.neededMagicEnergy = 0;
					
					cardDefinition3 = new CardDefinition(); 
					cardDefinition3.name = "Aries";
					cardDefinition3.card_id = "bsc-su-00-2";
					cardDefinition3.type = "Summon";
					cardDefinition3.maxEnergy = 5;
					cardDefinition3.maxHealth = 8;
					cardDefinition3.trivia = "Ein sagenhafter Widder der einst Phrixos und seine Schwester Helle vor ihrer Stiefmutter Ino rettete";
					cardDefinition3.effects = eds;
					cardDefinition3.magicPreservationValue = 2;
					cardDefinition3.summoningPoints = 1;
					cardDefinition3.attack = 4;
					cardDefinition3.heal = 7;
					cardDefinition3.maxVitality = 12;
					cardDefinition3.summonClass = "NaturalBeast";
					cardDefinition3.rank = "Legend";
					cardDefinition3.summonHierarchy = sahd;
					cardDefinition3.element = "Fire";
					cardDefinition3.magicWastageOnDefeat = 3;
					cardDefinition3.neededMagicEnergy = 0;
					
					cardDefinition4 = new CardDefinition(); 
					cardDefinition4.name = "Diamond Storm";
					cardDefinition4.card_id = "bsc-su-01";
					cardDefinition4.type = "Spell";
					cardDefinition4.maxEnergy = 5;
					cardDefinition4.maxHealth = 8;
					cardDefinition4.trivia = "Ein Zauber, der einen Sturm von Eiskristallen loslässt.";
					cardDefinition4.effects = eds;
					cardDefinition4.magicPreservationValue = 0;
					cardDefinition4.summoningPoints = 0;
					cardDefinition4.attack = 0;
					cardDefinition4.heal = 0;
					cardDefinition4.maxVitality = 0;
					cardDefinition4.summonClass = "";
					cardDefinition4.rank = "";
					cardDefinition4.summonHierarchy = null;
					cardDefinition4.element = "";
					cardDefinition4.magicWastageOnDefeat = 0;
					cardDefinition4.neededMagicEnergy = 5;
					set = new TreeMap<>();
					set.put(cardDefinition.card_id, cardDefinition);
					set.put(cardDefinition2.card_id, cardDefinition2);
					set.put(cardDefinition3.card_id, cardDefinition3);
					set.put(cardDefinition4.card_id, cardDefinition4);
				}
			}
			
			@Override
			public TreeMap<String, CardDefinition> getCardSet(String cardSetName) {
				init();
				return set;
			}
			
			@Override
			public CardDefinition getCardDefinition(String card_id) {
				init();
				if(set.containsKey(card_id) == false) {
					return null;
				}
				return set.get(card_id);
			}
		};
	}
	
	public static CreatesActions getActionFactoryMok() {
		return new CreatesActions() {
			
			@Override
			public GameAction createAction(String actionName) {
				return getGameAction(actionName);
			}
		};
	}
	
	public static CreatesEffects getEffectFactory() {
		return new CreatesEffects() {
			
			@Override
			public Effect createEffect(String effectName) {
				return getEffect();
			}
		};
	}
	
	public static mapsRankAndLevel getMapperMok() {
		return new mapsRankAndLevel() {
			
			@Override
			public int mapRankToLevel(String rank) {
				
				switch(rank) {
				case "Cub":
					return 0;
				case "Adult":
					return 1;
				case "Legend":
					return 2;
				}
				return -1;
			}
			
			@Override
			public String mapLevelToRank(int level, String summonClass) {
				String result = null;
				switch(summonClass) {
				case "NaturalBeast":
					switch(level) {
					case 0:
						result = "Cub";
						break;
					case 1:
						result =  "Adult";
						break;
					case 2:
						result =  "Legend";
						break;
					}
				}
				return result;
			}
	};
	}
	public static IsPhaseInGame getGamePhase(String phaseName) {
		return new IsPhaseInGame() {
			
			@Override
			public void restorePhaseStatus() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public String getName() {
				return phaseName;
			}
			
			@Override
			public ArrayList<String> getActionsToActivate() {
				return getActionDefinitions().getPhaseActions(getName());
			}
		};
	}
}
