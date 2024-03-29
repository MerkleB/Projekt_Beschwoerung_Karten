package project.test.mok;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import project.main.Action.GameAction;
import project.main.Card.Card;
import project.main.Effect.Effect;
import project.main.GameApplication.AcceptPromptAnswers;
import project.main.GameApplication.Game;
import project.main.GameApplication.IsAreaInGame;
import project.main.GameApplication.IsPhaseInGame;
import project.main.GameApplication.Player;
import project.main.GameApplication.ProcessesBattle;
import project.main.exception.NoCardException;
import project.main.jsonObjects.MessageInLanguage;
import project.main.util.RankLevelMapper;
import project.main.util.MapsRankAndLevel;

public class TestGame implements Game {
	
	private Player[] players;
	private boolean[] proceed;
	private boolean[] stackProceed;
	private Player activPlayer;
	private ArrayList<IsPhaseInGame> phases;
	private IsPhaseInGame activPhase;
	private int playerIndex;
	private boolean started;
	private boolean ended;
	private int round;
	private ProcessesBattle activeBattle;
	private Condition condition;
	private ReentrantLock lock;
	
	public TestGame(Player player1, Player player2, ArrayList<IsPhaseInGame> p, Condition condition, ReentrantLock lock) {
		players = new Player[2];
		players[0] = player1;
		players[1] = player2;
		stackProceed = new boolean[2];
		stackProceed[0] = false;
		stackProceed[1] = false;
		proceed = new boolean[2];
		proceed[0] = false;
		proceed[1] = false;
		phases = p;
		started = false;
		ended = false;
		round = 0;
		playerIndex = 0;
		for(IsPhaseInGame phase : phases) {
			phase.setGame(this);
		}
		this.condition = condition; 
		this.lock = lock;
	}
	
	@Override
	public void run() {
		started = true;
		while(!ended) {
			activPlayer = players[playerIndex];
			System.out.println("Start round "+round+"-"+playerIndex);
			System.out.println("Game: Set activ player: "+activPlayer.getID().toString());
			for(int i=0; i<phases.size(); i++) {
				activPhase = phases.get(i);
				System.out.println("Game: Process phase "+activPhase.getName()+".");
				activPhase.process();
				System.out.println("Game: Wake-Up Controller");
				lock.lock();
				try {
					((TestPlayer)activPlayer).getController().giveControl();
					System.out.println("Game: Await Controller signal");
				} finally {
				    lock.unlock();
				}
				lock.lock();
				try {
					condition.await();
					System.out.println("Game: Woken-up.");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}finally {
					lock.unlock();
				}
				activPhase.leave();
				System.out.println("Game: phase "+activPhase.getName()+" ended.");
			}
			
			for(int i=0; i<2; i++) {
				if(players[i].getHealthPoints()<=0) {
					ended = true;
					System.out.println("Player "+players[i].getID().toString()+" lost.");
					if(i==0) {
						System.out.println("Player "+players[i+1].getID().toString()+" won.");
					}else System.out.println("Player "+players[i-1].getID().toString()+" won.");
				}
			}
			if(playerIndex == 1) {
				playerIndex = 0;
				round++;
			}else {
				playerIndex = 1;
			}
		}
		System.out.println("Game ended, tell controller");
		lock.lock();
		try {
			condition.signal();
		} finally {
		    lock.unlock();
		}
	}

	@Override
	public MapsRankAndLevel getRankMapper() {
		return RankLevelMapper.getInstance();
	}

	@Override
	public Player getActivePlayer() {
		return activPlayer;
	}

	@Override
	public Player[] getPlayers() {
		return players;
	}

	@Override
	public Player getPlayer(String id) {
		Player foundPlayer = null;
		for(Player p : players) {
			if(p.getID().equals(id)) {
				foundPlayer = p;
			}
		}
		return foundPlayer;
	}
	
	@Override
	public Player getOtherPlayer(Player player) {
		if(player == players[0]) {
			return players[1];
		}else {
			return players[0];
		}
	}

	@Override
	public IsPhaseInGame getActivePhase() {
		return activPhase;
	}

	@Override
	public boolean hasEnded() {
		return ended;
	}

	@Override
	public boolean hasStarted() {
		return started;
	}

	@Override
	public void prompt(Player promptedPlayer, MessageInLanguage message, AcceptPromptAnswers prompter) {
		((TestPlayer)promptedPlayer).getController().prompt(promptedPlayer, message, prompter);
	}

	@Override
	public void prompt(Player promptedPlayer, MessageInLanguage message) {
		((TestPlayer)promptedPlayer).getController().prompt(promptedPlayer, message);
	}
	
	public void end() {
		ended = true;
	}

	@Override
	public boolean proceed(Player player) {
		if(ended) return false;
		boolean proceeded = false;
		try {
			lock.lock();
			Player otherPlayer = getOtherPlayer(player);
			System.out.println("Game: Player "+player.getID()+" wants to proceed the game");
			setProceed(player);
			if(!playerIsRelevantForProceed(otherPlayer)) {
				System.out.println("Game: Game does not need to wait for player "+otherPlayer.getID()+" because non of his actions or effects are activatable.");
				setProceed(otherPlayer);
			}
			
			if(proceed[0] && proceed[1]) {
				System.out.println("Game: Both players agreed to proceed.");
				proceed[0] = false;
				proceed[1] = false;
				condition.signal();
				proceeded = true;
			}
			
		} finally {
			lock.unlock();
		}
		return proceeded;
	}
	
	private void setProceed(Player player) {
		if(player == players[0]) {
			proceed[0] = true;
		}else {
			proceed[1] = true;
		}
	}

	@Override
	public boolean processGameStack(Player player) {
		try {
			lock.lock();
			System.out.println("Game: Player "+player.getID()+" wants to process the current stack.");
			if(ended) return false;
			Player otherPlayer = getOtherPlayer(player);
			setStackProceed(player, true);
			if(!playerIsRelevantForProceed(otherPlayer)) {
				System.out.println("Game: Game does not need to wait for player "+otherPlayer.getID()+" because non of his actions or effects are activatable.");
				setStackProceed(otherPlayer, true);
			}
			if(stackProceed[0] && stackProceed[1]) {
				System.out.println("Game: Both players agreed to start the stack.");
				stackProceed[0] = false;
				stackProceed[1] = false;
				Thread stackThread = new Thread(activPhase.getActiveGameStack(), activPhase.getName()+"-Stack");
				System.out.println("Game: Intiating Stack-Run.");
				stackThread.start();
				return true;
			}else return false;
		} finally {
			lock.unlock();
		}
		
	}
	
	@Override
	public void forbidGameStackProcessing(Player player) {
		if(playerIsRelevantForProceed(player)) {
			setStackProceed(player, false);
		}
	}

	private void setStackProceed(Player player, boolean proceed) {
		if(player == players[0]) {
			stackProceed[0] = proceed;
		}else {
			stackProceed[1] = proceed;
		}
	}
	
	@Override
	public boolean playerIsRelevantForProceed(Player player) {
		boolean relevant = false;
		if(player == activPlayer) {
			relevant = true;
		}else {
			ArrayList<IsAreaInGame> zones = player.getGameZones();
			for(IsAreaInGame zone : zones) {
				ArrayList<Card> cards = zone.getCards();
				for(Card card : cards) {
					ArrayList<GameAction> actions = card.getActions();
					for(GameAction action : actions) {
						if(action.activateable(player)) {
							relevant = true;
						}
					}
					try {
						Effect[] effects = card.getEffects();
						for(Effect effect : effects) {
							if(effect.activateable(player)) {
								relevant = true;
							}
						}
					} catch (NoCardException e) {
						//Effects of MagicCollectors are ignored here
					}
					
				}
			}
		}
		return relevant;
	}
	
	@Override
	public ProcessesBattle getActiveBattle() {
		return activeBattle;
	}

	@Override
	public void setActiveBattle(ProcessesBattle battle) {
		this.activeBattle = battle;		
	}

	@Override
	public ReentrantLock getLock() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Condition getCondition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPlayer(Player player) {
		if(players[0] == null) {
			players[0] = player;
		}else {
			if(players[1] == null) {
				players[1] = player;
			}
		}
	}

	@Override
	public String getRound() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startTimer(Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopTimer(Player player) {
		// TODO Auto-generated method stub
		
	}

}
