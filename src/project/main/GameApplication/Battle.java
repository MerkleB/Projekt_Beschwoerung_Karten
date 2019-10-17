package project.main.GameApplication;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.UUID;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import project.main.Card.ActivityStatus;
import project.main.Card.Summon;
import project.main.Card.SummonStatus;
import project.main.Listeners.BattleEventObject;
import project.main.Listeners.GameListener;
import project.main.build_cards.KnowsSummonAscentHierarchy;
import project.main.util.CalculatesElementEffectivities;
import project.main.util.ElementDefinitionLibrary;
import project.main.util.GameMessageProvider;

public class Battle implements ProcessesBattle {
	
	private Game game;
	private IsPhaseInGame activeBattlePhase;
	private Hashtable<String, IsPhaseInGame> battlePhases;
	private Player attackingPlayer;
	private Summon attackingSummon;
	private Player defendingPlayer;
	private Summon defendingSummon;
	private Player activePlayer;
	private Summon fastSummon;
	private Summon slowSummon;
	private Summon winner;
	private Summon looser;
	private String status;
	private boolean proceedOne;
	private boolean proceedTwo;
	private boolean waitForBoth;
	private boolean finished;
	private ReentrantLock lock;
	private Condition cond;
	private int round;
	
	private Battle() {
		lock = new ReentrantLock();
		cond = lock.newCondition();
		proceedOne = false;
		proceedTwo = false;
		battlePhases = new Hashtable<String, IsPhaseInGame>();
		BattlePhase phase = new BattlePhase("ClashBegin");
		battlePhases.put(phase.getName(), phase);
		phase = new BattlePhase("ClashEnd");
		battlePhases.put(phase.getName(), phase);
		round = 0;
	}
	
	public static ProcessesBattle getInstance() {
		ProcessesBattle instance = new Battle();
		((Battle)instance).status = ProcessesBattle.INIT;
		return instance;
	}
	
	@Override
	public void setGame(Game game) {
		this.game = game;
		game.setActiveBattle(this);
		battlePhases.get("ClashBegin").setGame(game);
		battlePhases.get("ClashEnd").setGame(game);
	}

	@Override
	public void proceed(Player player) {
		lock.lock();
		try {
			if(player == activePlayer) {
				if(player == attackingPlayer) {
					proceedOne = true;
				}else proceedTwo = true;
				
				if(waitForBoth) {
					if(proceedOne && proceedTwo) {
						getActivePhase().getActiveGameStack().run();
						proceedOne = false;
						proceedTwo = false;
						waitForBoth = false;
					}
				}else {
					getActivePhase().getActiveGameStack().run();
					proceedOne = false;
					proceedTwo = false;
				}
				cond.signal();
				
			}
		}finally {
			lock.unlock();
		}
		
	}

	@Override
	public void setCombatants(Summon attacker, Summon defender) {
		attackingPlayer = attacker.getOwningPlayer();
		attackingSummon = attacker;
		defendingPlayer = defender.getOwningPlayer();
		defendingSummon = defender;
	}

	@Override
	public Summon[] getCombatants() {
		Summon[] summons = {attackingSummon, defendingSummon};
		return summons;
	}

	@Override
	public void start() {
		lock.lock();
		try {
			GameListener.getInstance().battleStarted(this);
			status = ProcessesBattle.RUNNING;
			determineFastSummon();
			System.out.println("Fast summon "+fastSummon.getName()+"#"+fastSummon.getID());
			System.out.println("Fast summon "+slowSummon.getName()+"#"+slowSummon.getID());
			while(!finished) {
				round++;
				System.out.println("-==Start round "+round+"==-");
				waitForBoth = true;
				activateBattlePhase("ClashBegin", attackingPlayer);
				promptPlayer("#5", attackingPlayer);
				cond.await(); //Wait until proceed
				deactivate(attackingPlayer);
				activateBattlePhase("ClashBegin", defendingPlayer);
				promptPlayer("#5", defendingPlayer);
				cond.await(); //Wait until proceed
				deactivate(defendingPlayer);
				if(fastSummon != null && slowSummon != null) {
					int fastSummonAttack = calculateAttack(fastSummon, slowSummon);
					int slowSummonVitality = slowSummon.getStatus().decreaseVitality(fastSummonAttack);
					System.out.println("Slower Summon "+slowSummon.getName()+" gets "+fastSummonAttack+" damage. Current vitality: "+slowSummonVitality);
					GameListener.getInstance().attackHappened(new BattleEventObject(BattleEventObject.ATTACK, fastSummon, slowSummon, fastSummonAttack));
					if(slowSummonVitality == 0) {
						setBattleResult(fastSummon, slowSummon);
						break;
					}
					
					int slowSummonAttack = calculateAttack(slowSummon, fastSummon);
					int fastSummonVitality = fastSummon.getStatus().decreaseVitality(slowSummonAttack);
					System.out.println("Faster Summon "+fastSummon.getName()+" gets "+slowSummonAttack+" damage. Current vitality: "+fastSummonVitality);
					GameListener.getInstance().attackHappened(new BattleEventObject(BattleEventObject.ATTACK, slowSummon, fastSummon, slowSummonAttack));
					if(fastSummonVitality == 0) {
						setBattleResult(slowSummon, fastSummon);
						break;
					}
					
					activateBattlePhase("ClashEnd", fastSummon.getOwningPlayer());
					promptPlayer("#6", fastSummon.getOwningPlayer());
					cond.await(); // Wait until "fast" player proceeds
					deactivate(attackingPlayer);
					if(fastSummon == null) end();
					activateBattlePhase("ClashEnd", slowSummon.getOwningPlayer());
					promptPlayer("#6", slowSummon.getOwningPlayer());
					cond.await(); // Wait until "slow" player proceeds
					deactivate(defendingPlayer);
					if(slowSummon == null) end();
				}else{
					end();
				};
			}
			GameListener.getInstance().battleEnded(this);
		} catch (InterruptedException e) {
			System.out.println("Game was interrupted.");
		}finally {
			lock.unlock();
		}
	}

	@Override
	public void end() {
		finished = true;
		status = ABRUPT;
		if(fastSummon == null) {
			slowSummon.getSummonHierarchy().addExperience();
		}else {
			fastSummon.getSummonHierarchy().addExperience();
		}
		System.out.println("Battle was abrupt.");
		GameListener.getInstance().battleAbrupt(this);
	}

	@Override
	public void remove(UUID summonID) {
		if(!status.equals(ProcessesBattle.RUNNING))return;
		if(fastSummon.getID() == summonID) {
			if(fastSummon == attackingSummon) {
				fastSummon.setActivityStatus(ActivityStatus.IMMOBILIZED, 1);
			}
			fastSummon = null;
		}else {
			if(slowSummon == attackingSummon) {
				slowSummon.setActivityStatus(ActivityStatus.IMMOBILIZED, 1);
			}
			slowSummon = null;
		}
	}

	@Override
	public Player getActivePlayer() {
		return activePlayer;
	}

	@Override
	public Summon getWinner() {
		return winner;
	}

	@Override
	public Summon getLooser() {
		return looser;
	}

	@Override
	public String getStatus() {
		return status;
	}

	@Override
	public IsPhaseInGame getActivePhase() {
		return activeBattlePhase;
	}
	
	private void determineFastSummon() {
		SummonStatus statusAttack = attackingSummon.getStatus();
		SummonStatus statusDefend = defendingSummon.getStatus();
		if(statusAttack.getInitiative() > statusDefend.getInitiative()) {
			fastSummon = attackingSummon;
			slowSummon = defendingSummon;
			System.out.println("Attacking summon is fast summon.");
		}else if(statusDefend.getInitiative() > statusAttack.getInitiative()) {
			fastSummon = defendingSummon;
			slowSummon = attackingSummon;
			System.out.println("Defending summon is fast summon.");
		}else {
			boolean fasterSummonDetermined = false;
			while(!fasterSummonDetermined) {
				double attackRandom = Math.random();
				double defendRandom = Math.random();
				if(attackRandom > defendRandom) {
					fastSummon = attackingSummon;
					slowSummon = defendingSummon;
					fasterSummonDetermined = true;
				}else if(defendRandom > attackRandom) {
					fastSummon = defendingSummon;
					slowSummon = attackingSummon;
					fasterSummonDetermined = true;
				}
			}
		}
	}
	
	private void promptPlayer(String messageID, Player player) {
		game.prompt(player, GameMessageProvider.getInstance().getMessage(messageID, Application.getInstance().getLanguage()));
	}
	
	private void activateBattlePhase(String phase, Player player) {
		activePlayer = player;
		activeBattlePhase = battlePhases.get(phase);
		activeBattlePhase.process();
	}
	
	private void deactivate(Player player) {
		ArrayList<IsAreaInGame> zones = player.getGameZones();
		for(IsAreaInGame zone : zones) {
			zone.deavtivateAll();
		}
	}
	
	private void setBattleResult(Summon winner, Summon looser) {
		this.winner = winner;
		KnowsSummonAscentHierarchy hierarchy = this.winner.getSummonHierarchy();
		hierarchy.addExperience();
		hierarchy.addExperience();
		hierarchy.addExperience();
		this.looser = looser;
		looser.getOwningPlayer().getGameZone("SummonZone").removeCard(looser);
		looser.getOwningPlayer().getGameZone("DiscardPile").addCard(looser);
		status = ProcessesBattle.ENDED;
		System.out.println("Winning card "+winner.getName()+"#"+winner.getID());
		System.out.println("Loosing card "+looser.getName()+"#"+looser.getID());
	}
	
	private int calculateAttack(Summon summon, Summon attackedSummon) {
		int summonAttack = fastSummon.getStatus().getAttack();
		String attackingElement = summon.getStatus().getElement();
		String defendingElement = attackedSummon.getStatus().getElement();
		CalculatesElementEffectivities calculator = ElementDefinitionLibrary.getInstance();
		double elementFactor = calculator.getEffectivity(attackingElement, defendingElement);
		double attackConsideringElement = summonAttack * elementFactor;
		System.out.println("Calculate Attack: Attack ("+summonAttack+") * Element Factor ("+elementFactor+") = "+attackConsideringElement+" round down to "+(int)attackConsideringElement);
		return (int)attackConsideringElement;
	}

}
