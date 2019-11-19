package project.main.GameApplication;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import project.main.Action.GameAction;
import project.main.Card.Card;
import project.main.Effect.Effect;
import project.main.exception.NoCardException;
import project.main.jsonObjects.MessageInLanguage;
import project.main.util.RankLevelMapper;
import project.main.util.GameMessageProvider;
import project.main.util.MapsRankAndLevel;

public class CardGame implements Game, TimerListener {
	
	private static String[] PHASE_NAMES = {"RefreshmentPhase", "DrawPhase", "Main", "Endphase"};
	private MapsRankAndLevel rankAndLevelMapper; 
	private boolean[] stackProceed;
	private Player[] players;
	private boolean[] proceed;
	private boolean waitingForStackProceed;
	//private boolean waitingForProceed;
	private Player activPlayer;
	private ArrayList<IsPhaseInGame> phases;
	private IsPhaseInGame activPhase;
	private int playerIndex;
	private boolean started;
	private boolean ended;
	private int round;
	private String roundCode;
	private ProcessesBattle activeBattle;
	private Condition condition;
	private ReentrantLock lock;
	private Thread timerThread;
	private CountsTimeUntilProceed timer;
	private Player timerRequestor;
	
	public CardGame(Player player1, Player player2) {
		players = new Player[2];
		players[0] = player1;
		player1.setGame(this);
		players[1] = player2;
		player2.setGame(this);
		stackProceed = new boolean[2];
		stackProceed[0] = false;
		stackProceed[1] = false;
		proceed = new boolean[2];
		proceed[0] = false;
		proceed[1] = false;
		started = false;
		ended = false;
		round = 0;
		playerIndex = 0;
		rankAndLevelMapper = RankLevelMapper.getInstance();
		phases = getPhases(PHASE_NAMES);
		for(IsPhaseInGame phase : phases) {
			phase.setGame(this);
		}
		lock = new ReentrantLock();
		condition = lock.newCondition(); 
//		waitingForProceed = false;
		waitingForStackProceed = false;
	}
	
	private ArrayList<IsPhaseInGame> getPhases(String[] phaseNames){
		ArrayList<IsPhaseInGame> phases = new ArrayList<IsPhaseInGame>();
		for(String name : phaseNames) {
			switch(name) {
			case "DrawPhase":
				phases.add(new DrawPhase());
				break;
			case "RefreshmentPhase":
				phases.add(new RefreshmentPhase());
				break;
			default:
				phases.add(new SimplePhase(name));
				break;
			}
		}
		return phases;
	}
	
	@Override
	public void run() {
		started = true;
		while(!ended) {
			activPlayer = players[playerIndex];
			roundCode = round+"-"+playerIndex;
			System.out.println("Game: Start round "+roundCode);
			System.out.println("Game: Set activ player: "+activPlayer.getID().toString());
			PhaseProcessing:
			for(int i=0; i<phases.size(); i++) {
				activPhase = phases.get(i);
				System.out.println("Game: Process phase "+activPhase.getName()+".");
				activPhase.process();
				System.out.println("Game: Await Player signal to proceed");
				if(i+1 == phases.size()) {
					String[] parameters = {"30"};
					timer = new DelayableProceedTimer(30, 0);
					//waitingForProceed = true;
					timerThread = new Thread(timer, "Timer-End-Turn");
					activPlayer.getController().prompt(activPlayer, GameMessageProvider.getInstance().getMessage("#15", Application.getInstance().getLanguage(), parameters));
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
				for(int j=0; j<2; j++) {
					if(players[j].getHealthPoints()<=0) {
						ended = true;
						System.out.println("Player "+players[i].getID().toString()+" lost.");
						if(j==0) {
							System.out.println("Player "+players[i+1].getID().toString()+" won.");
						}else System.out.println("Player "+players[i-1].getID().toString()+" won.");
						break PhaseProcessing;
					}
				}
			}
			if(playerIndex == 1) {
				playerIndex = 0;
				round++;
			}else {
				playerIndex = 1;
			}
		}
		System.out.println("Game ended");
	}

	@Override
	public void setPlayer(Player player) {
		if(players[0] == null) {
			players[0] = player;
			player.setGame(this);
		}else {
			if(players[1] == null) {
				players[1] = player;
				player.setGame(this);
			}
		}
	}

	@Override
	public MapsRankAndLevel getRankMapper() {
		return rankAndLevelMapper;
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
		if(activeBattle != null) {
			if(activeBattle.getStatus().equals(Battle.RUNNING)) {
				return activeBattle.getActivePhase();
			}
		}
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
		promptedPlayer.getController().prompt(promptedPlayer, message, prompter);
	}

	@Override
	public void prompt(Player promptedPlayer, MessageInLanguage message) {
		promptedPlayer.getController().prompt(promptedPlayer, message);
	}
	
	public void end() {
		ended = true;
	}

	@Override
	public boolean proceed(Player player) {
		if(ended) return false;
		boolean proceeded = false;
		if(activeBattle != null) {
			if(activeBattle.getStatus().equals(Battle.RUNNING)) {
				activeBattle.proceed(player);
				return true;
			}
		}
		try {
			ReentrantLock lock = new ReentrantLock();
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
				condition.signalAll();
				proceeded = true;
				//waitingForProceed = false;
				if(timer != null) {
					if(timer.getStatus() != CountsTimeUntilProceed.ENDED) {
						timerThread.interrupt();
					}
				}
			}//else waitingForProceed = true;
			
		} finally {
			lock.unlock();
		}
		return proceeded;
	}
	
	@Override
	public void startTimer(Player player) {
		ReentrantLock lock = new ReentrantLock();
		lock.lock();
		try {
			if(timer.getStatus() != DelayableProceedTimer.STARTED) {
				timerRequestor = player;
				timer = new DelayableProceedTimer(30, 60);
				timer.addTimerListener(this);
				String[] parameters = {"60", "30"};
				Player otherPlayer = getOtherPlayer(player);
				otherPlayer.getController().prompt(otherPlayer, GameMessageProvider.getInstance().getMessage("#13", Application.getInstance().getLanguage(), parameters));
				player.getController().prompt(timerRequestor, GameMessageProvider.getInstance().getMessage("#14", Application.getInstance().getLanguage(), parameters));
				timerThread = new Thread(timer, "Timer");
				timerThread.start(); 
			}
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void stopTimer(Player player) {
		ReentrantLock lock = new ReentrantLock();
		lock.lock();
		try {
			if(player == timerRequestor) {
				if(timer.getStatus() != CountsTimeUntilProceed.ENDED) {
					timerThread.interrupt();
				}
			}
		}finally {
			lock.unlock();
		}
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
			ReentrantLock lock = new ReentrantLock();
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
				System.out.println("Game: Initiating Stack-Run.");
				stackThread.start();
				if(timer != null) {
					if(timer.getStatus() != CountsTimeUntilProceed.ENDED) {
						timerThread.interrupt();
					}
				}
				return true;
			}else {
				waitingForStackProceed = true;
				return false;
			}
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
		return lock;
	}

	@Override
	public Condition getCondition() {
		return condition;
	}

	@Override
	public String getRound() {
		return roundCode;
	}

	@Override
	public void timerStarted(CountsTimeUntilProceed timer) {
		players[0].getController().timerStarted(timer);
		players[1].getController().timerStarted(timer);
	}

	@Override
	public void timerCountedDown(CountsTimeUntilProceed timer) {
		players[0].getController().timerCountedDown(timer);
		players[1].getController().timerCountedDown(timer);
	}

	@Override
	public void timerEnded(CountsTimeUntilProceed timer) {
		if(waitingForStackProceed) {
			processGameStack(getOtherPlayer(timerRequestor));
			waitingForStackProceed = false;
		}else {
			proceed(getOtherPlayer(timerRequestor));
		}
		players[0].getController().timerCountedDown(timer);
		players[1].getController().timerCountedDown(timer);
		
		
	}

}
