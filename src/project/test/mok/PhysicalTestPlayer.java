package project.test.mok;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import project.main.Action.GameAction;
import project.main.Card.Card;
import project.main.GameApplication.AcceptPromptAnswers;
import project.main.GameApplication.Game;
import project.main.GameApplication.IsAreaInGame;
import project.main.GameApplication.Player;
import project.main.GameApplication.SummonZone;
import project.main.GameApplication.SummoningCircle;
import project.main.exception.NotActivableException;
import project.main.jsonObjects.MessageInLanguage;

/**
 * Moks the actions of a physical existing player for testing purposes
 * @author D054525
 *
 */
public class PhysicalTestPlayer implements Runnable{
	private Player player;
	private Game game;
	public boolean hasControl;
	private ArrayList<PhysicalPlayerAction> actionsToPerform;
	private Hashtable<String, PhysicalPlayerAction> expectedPrompts;
	private Condition gameCondition;
	private Condition testCondition;
	private ReentrantLock lock;
	private ReentrantLock lockTest;
	
	public PhysicalTestPlayer(Player p, Game g, Condition cg, Condition ct, ReentrantLock lg, ReentrantLock lt) {
		player = p;
		game = g;
		actionsToPerform = new ArrayList<PhysicalPlayerAction>();
		expectedPrompts = new Hashtable<String, PhysicalPlayerAction>();
		gameCondition = cg;
		testCondition = ct;
		lock = lg;
		lockTest = lt;
	}
	
	public void addAction(String actionName, UUID card_id, String zoneName) {
		PhysicalPlayerAction playerAction = new PhysicalPlayerAction() {
			
			@Override
			public void perform() throws NotActivableException {
				IsAreaInGame zone = player.getGameZone(zoneName);
				if(card_id != null) {
					System.out.println("Controller: Execute Action "+actionName+" on Card "+ card_id.toString() + " in Zone " + zoneName+" (Player-"+player.getID()+", Thread"+Thread.currentThread().getName()+")");
					System.out.println("Selected zone: "+zone.getName());
					ArrayList<Card> cards = zone.getCards();
					System.out.println("Search card "+card_id);
					for(Card card : cards) {
						System.out.println("Card "+card.getID()+"?");
						if(card.getID().equals(card_id)) {
							System.out.println("Found card "+card_id);
							ArrayList<GameAction> actions = card.getActions();
							System.out.println("|->Search action "+actionName);
							for(GameAction action : actions) {
								System.out.println("Is it action "+action.getCode()+"?");
								try {
									if(action.getCode().equals(actionName)) {
										System.out.println("Activate action "+actionName);
										action.activate(player);
										break;
									}
								}catch(NotActivableException e) {
									throw new NotActivableException("Action could not be activated: "+actionName+" ("+e.getMessage()+")");
								}
							}
							break;
						}
					}
				}else {
					if(zoneName.equals("SummonZone")) {
						ArrayList<SummoningCircle> circles = ((SummonZone)zone).getCircles();
						circles.get(2).getAction().activate(player);
					}
				}
			}
		};
		actionsToPerform.add(playerAction);
	}
	
	public void addStackStart() {
		actionsToPerform.add(new PhysicalPlayerAction() {
			
			@Override
			public void perform() throws NotActivableException {
				System.out.println("Controller: Start stack and wait for finish"+" (Player-"+player.getID()+", Thread"+Thread.currentThread().getName()+")");
				game.getActivePhase().getActiveGameStack().run();
			}
		});
	}
	
	public void addPhaseEndAction() {
		PhysicalPlayerAction action = new PhysicalPlayerAction() {
			
			@Override
			public void perform() throws NotActivableException {
				lock.lock();
				System.out.println("Controller: Execute end phase"+" (Player-"+player.getID()+", Thread"+Thread.currentThread().getName()+")");
				try {
					gameCondition.signal();
				} finally {
				    lock.unlock();
				}				
			}
		};
		actionsToPerform.add(action);
	}
	
	public void prompt(Player promptedPlayer, MessageInLanguage message, AcceptPromptAnswers prompter) {
		System.out.println("Controller-prompt: "+message.text);
	}

	public void prompt(Player promptedPlayer, MessageInLanguage message) {
		System.out.println("Controller-prompt: "+message.text);
	}

	@Override
	public void run() {
		lock.lock();
		System.out.println("Controller started"+" (Player-"+player.getID()+", Thread"+Thread.currentThread().getName()+")");
		System.out.println("Controller: Wait until game gives control"+" (Player-"+player.getID()+", Thread"+Thread.currentThread().getName()+")");
		try {
			gameCondition.await(10, TimeUnit.SECONDS);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}finally {
			lock.unlock();
		}
		System.out.println("Controller runs"+" (Player-"+player.getID()+", Thread"+Thread.currentThread().getName()+")");
		if(game.getActivePlayer() == player) {
			System.out.println("Controller: Perform defined actions."+" (Player-"+player.getID()+", Thread"+Thread.currentThread().getName()+")");
			for(PhysicalPlayerAction action : actionsToPerform) {
				try {
					action.perform();
				} catch (NotActivableException e) {
					throw new RuntimeException(e.getMessage());
				}
			}
			actionsToPerform.clear();
		}
		lock.lock();
		try {
			System.out.println("Controller: Wait until game finished"+" (Player-"+player.getID()+", Thread"+Thread.currentThread().getName()+")");
			gameCondition.await(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
		System.out.println("Controller: Wake-up test"+" (Player-"+player.getID()+", Thread"+Thread.currentThread().getName()+")");
		lockTest.lock();
		try {
			testCondition.signal();
		} finally {
		    lockTest.unlock();
		}
	}
	
	@FunctionalInterface
	public interface PhysicalPlayerAction{
		public void perform() throws NotActivableException;
	}
}
