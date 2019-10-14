package project.main.GameApplication;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import project.main.Card.Summon;
import project.main.Card.SummonStatus;
import project.main.Listeners.BattleEventObject;
import project.main.Listeners.GameListener;
import project.main.jsonObjects.ElementDefinition;
import project.main.util.CalculatesElementEffectivities;
import project.main.util.ElementDefinitionLibrary;

public class Battle implements ProcessesBattle {
	
	private Player attackingPlayer;
	private Summon attackingSummon;
	private Player defendingPlayer;
	private Summon defendingSummon;
	private Summon fastSummon;
	private Summon slowSummon;
	private Summon winner;
	private Summon looser;
	private String status;
	private boolean proceedOne;
	private boolean proceedTwo;
	private boolean proceedFast;
	private boolean proceedSlow;
	private boolean waitForBoth;
	private boolean waitForSlow;
	private boolean waitForFast;
	private boolean finished;
	private ReentrantLock lock;
	private Condition cond;
	
	private Battle() {
		lock = new ReentrantLock();
		cond = lock.newCondition();
		proceedFast = false;
		proceedSlow = false;
		proceedOne = false;
		proceedTwo = false;
	}
	
	public static ProcessesBattle getInstance() {
		ProcessesBattle instance = new Battle();
		((Battle)instance).status = ProcessesBattle.INIT;
		return instance;
	}
	
	@Override
	public void proceed(Player player) {
		lock.lock();
		try {
			if(waitForBoth) {
				if(player == attackingPlayer) {
					proceedOne = true;
				}else proceedTwo = true;
				if(proceedOne && proceedTwo) {
					proceedOne = false;
					proceedTwo = false;
					waitForBoth = false;
					cond.signal();
				}
			}
			
			if(waitForSlow || waitForFast) {
				if(fastSummon.getOwningPlayer() == player) {
					proceedFast = true;
				}else proceedSlow = true;
				
				if(proceedFast && waitForFast) {
					proceedFast = false;
					waitForFast = false;
					cond.signal();
				}
				
				if(proceedSlow && waitForSlow) {
					proceedSlow = false;
					waitForSlow = false;
					cond.signal();
				}
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
	public void start() {
		lock.lock();
		try {
			GameListener.getInstance().battleStarted(this);
			status = ProcessesBattle.RUNNING;
			determineFastSummon();
			System.out.println("Fast summon "+fastSummon.getName()+"#"+fastSummon.getID());
			System.out.println("Fast summon "+slowSummon.getName()+"#"+slowSummon.getID());
			while(!finished) {
				activateBattlePhase("ClashBegin", attackingPlayer);
				activateBattlePhase("ClashBegin", defendingPlayer);
				waitForBoth = true;
				cond.await(); //Wait until proceed
				deactivate(attackingPlayer);
				deactivate(defendingPlayer);
				if(fastSummon != null && slowSummon != null) {
					int fastSummonAttack = calculateAttack(fastSummon, slowSummon);
					int slowSummonVitality = slowSummon.getStatus().decreaseVitality(fastSummonAttack);
					GameListener.getInstance().attackHappened(new BattleEventObject(BattleEventObject.ATTACK, fastSummon, slowSummon, fastSummonAttack));
					if(slowSummonVitality == 0) {
						setBattleResult(fastSummon, slowSummon);
						break;
					}
					
					int slowSummonAttack = calculateAttack(slowSummon, fastSummon);
					int fastSummonVitality = fastSummon.getStatus().decreaseVitality(slowSummonAttack);
					GameListener.getInstance().attackHappened(new BattleEventObject(BattleEventObject.ATTACK, slowSummon, fastSummon, slowSummonAttack));
					if(fastSummonVitality == 0) {
						setBattleResult(slowSummon, fastSummon);
						break;
					}
					
					activateBattlePhase("ClashEnd", fastSummon.getOwningPlayer());
					waitForFast = true;
					cond.await(); // Wait until "fast" player proceeds
					if(fastSummon == null) end();
					activateBattlePhase("ClashEnd", slowSummon.getOwningPlayer());
					waitForSlow = true;
					cond.await(); // Wait until "slow" player proceeds
					deactivate(attackingPlayer);
					deactivate(defendingPlayer);
					if(slowSummon == null) {
						end();
					}
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
			fastSummon.setActivityStatus(Summon.IMMOBILIZED);
		}else slowSummon.setActivityStatus(Summon.IMMOBILIZED);
		System.out.println("Battle was abrupt.");
		GameListener.getInstance().battleAbrupt(this);
	}

	@Override
	public void remove(UUID summonID) {
		if(!status.equals(ProcessesBattle.RUNNING))return;
		if(fastSummon.getID() == summonID) {
			fastSummon = null;
		}else {
			slowSummon = null;
		}
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
	
	private void activateBattlePhase(String phase, Player player) {
		ArrayList<IsAreaInGame> zones = player.getGameZones();
		IsPhaseInGame battlePhase = new GamePhase(phase);
		for(IsAreaInGame zone : zones) {
			zone.activate(player, battlePhase);
		}
	}
	
	private void deactivate(Player player) {
		ArrayList<IsAreaInGame> zones = player.getGameZones();
		for(IsAreaInGame zone : zones) {
			zone.deavtivateAll();
		}
	}
	
	private void setBattleResult(Summon winner, Summon looser) {
		this.winner = winner;
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
