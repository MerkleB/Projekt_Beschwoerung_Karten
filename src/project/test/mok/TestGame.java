package project.test.mok;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import project.main.GameApplication.AcceptPromptAnswers;
import project.main.GameApplication.Game;
import project.main.GameApplication.IsPhaseInGame;
import project.main.GameApplication.Player;
import project.main.jsonObjects.MessageInLanguage;
import project.main.util.RankLevelMapper;
import project.main.util.MapsRankAndLevel;

public class TestGame implements Game {
	
	private Player[] players;
	private Player activPlayer;
	private ArrayList<IsPhaseInGame> phases;
	private IsPhaseInGame activPhase;
	private int playerIndex;
	private boolean started;
	private boolean ended;
	private boolean phaseEnd;
	private Condition condition;
	private ReentrantLock lock;
	
	public TestGame(Player player1, Player player2, ArrayList<IsPhaseInGame> p, Condition condition, ReentrantLock lock) {
		players = new Player[2];
		players[0] = player1;
		players[1] = player2;
		phases = p;
		started = false;
		ended = false;
		phaseEnd = false;
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
			System.out.println("Game: Set activ player: "+activPlayer.getID().toString());
			for(int i=0; i<phases.size(); i++) {
				activPhase = phases.get(i);
				System.out.println("Game: Process phase "+activPhase.getName()+".");
				phaseEnd = false;
				activPhase.process();
				System.out.println("Game: Wake-Up Controller");
				lock.lock();
				try {
					condition.signal();
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
	public Player getPlayer(UUID id) {
		Player foundPlayer = null;
		for(Player p : players) {
			if(p.getID().equals(id)) {
				foundPlayer = p;
			}
		}
		return foundPlayer;
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
	
	public void phaseEnd() {
		phaseEnd = true;
	}

	@Override
	public boolean processGameStack() {
		// TODO Auto-generated method stub
		return false;
	}

}
