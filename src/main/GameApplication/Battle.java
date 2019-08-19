package main.GameApplication;

import java.util.ArrayList;
import java.util.UUID;

import main.Card.Summon;
import main.Card.SummonStatus;
import main.Listeners.GameListener;

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
	private boolean processSlow;
	private boolean finished;
	
	public static ProcessesBattle getInstance() {
		return new Battle();
	}
	
	@Override
	public void proceed(Player player) {
		if(player == attackingPlayer) {
			proceedOne = true;
		}else proceedTwo = true;
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
		determineFastSummon();
		status = ProcessesBattle.RUNNING;
		while(!finished) {
			activateBattlePhase("ClashBegin", attackingPlayer);
			activateBattlePhase("ClashBegin", defendingPlayer);
			proceedOne = false;
			proceedTwo = false;
			while(!proceedOne || !proceedTwo); //Wait
			deactivate(attackingPlayer);
			deactivate(defendingPlayer);
			if(fastSummon != null && slowSummon != null) {
				//TODO: Raise attack event
				int fastSummonAttack = fastSummon.getStatus().getAttack();
				int slowSummonVitality = slowSummon.getStatus().decreaseVitality(fastSummonAttack);
				if(slowSummonVitality == 0) {
					setBattleResult(fastSummon, slowSummon);
				}
				//TODO: Raise attack event				
				int slowSummonAttack = slowSummon.getStatus().getAttack();
				int fastSummonVitality = fastSummon.getStatus().decreaseVitality(slowSummonAttack);
				if(fastSummonVitality == 0) {
					setBattleResult(slowSummon, fastSummon);
				}
				
				activateBattlePhase("ClashEnd", fastSummon.getOwningPlayer());
				while(!proceedFast);
				if(fastSummon == null) end();
				activateBattlePhase("ClashEnd", slowSummon.getOwningPlayer());
				while(!processSlow);
				deactivate(attackingPlayer);
				deactivate(defendingPlayer);
				if(slowSummon == null) {
					end();
				}
			}else{
				end();
			};
		}
	}

	@Override
	public void end() {
		finished = true;
		status = ABRUPT;
		if(fastSummon == null) {
			fastSummon.setActivityStatus(Summon.IMMOBILIZED);
		}else slowSummon.setActivityStatus(Summon.IMMOBILIZED);
		//TODO: Raise battle abort event
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Summon getLooser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStatus() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void determineFastSummon() {
		SummonStatus statusAttack = attackingSummon.getStatus();
		SummonStatus statusDefend = defendingSummon.getStatus();
		if(statusAttack.getInitiative() > statusDefend.getInitiative()) {
			fastSummon = attackingSummon;
			slowSummon = defendingSummon;
		}else if(statusDefend.getInitiative() > statusAttack.getInitiative()) {
			fastSummon = defendingSummon;
			slowSummon = attackingSummon;
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
		GameListener.getInstance().phaseStarted(battlePhase);
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
		//TODO: Raise battle end event
	}

}
