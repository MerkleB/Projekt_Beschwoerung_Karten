package project.test.mok;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.plaf.SliderUI;

import project.main.Action.BlockCollectorAttack;
import project.main.Action.GameAction;
import project.main.Action.Stackable;
import project.main.Card.Card;
import project.main.GameApplication.AcceptPromptAnswers;
import project.main.GameApplication.Application;
import project.main.GameApplication.ControlsStackables;
import project.main.GameApplication.Game;
import project.main.GameApplication.IsAreaInGame;
import project.main.GameApplication.OwnsGameStack;
import project.main.GameApplication.Player;
import project.main.GameApplication.SummonZone;
import project.main.GameApplication.SummoningCircle;
import project.main.exception.NotActivableException;
import project.main.jsonObjects.MessageInLanguage;
import project.main.util.GameMessageProvider;

/**
 * Moks the actions of a physical existing player for testing purposes
 * @author D054525
 *
 */
public class PhysicalTestPlayer implements Runnable, ControlsStackables{
	private Player player;
	private Game game;
	public boolean hasControl;
	public boolean waitForPromptsFinished;
	private ArrayList<PhysicalPlayerAction> actionsToPerform;
	private Hashtable<String, PhysicalPlayerAction> expectedPrompts;
	private Hashtable<String, Integer> keyIndexOfExpectedPrompt;
	private Hashtable<String, Integer> currentKeyIndexOfExpectedPrompt;
	private Condition gameCondition;
	private Condition testCondition;
	private Condition controllerCondition;
	private ReentrantLock lock;
	private ReentrantLock lockTest;
	private ReentrantLock controllerLock; 
	
	public PhysicalTestPlayer(Player p, Game g, Condition cg, Condition ct, ReentrantLock lg, ReentrantLock lt) {
		player = p;
		game = g;
		actionsToPerform = new ArrayList<PhysicalPlayerAction>();
		expectedPrompts = new Hashtable<String, PhysicalPlayerAction>();
		keyIndexOfExpectedPrompt = new Hashtable<String, Integer>();
		currentKeyIndexOfExpectedPrompt = new Hashtable<String, Integer>();
		gameCondition = cg;
		testCondition = ct;
		lock = lg;
		lockTest = lt;
		controllerLock = new ReentrantLock();
		controllerCondition = controllerLock.newCondition();
		waitForPromptsFinished = false;
	}
	
	public void giveControl() {
		if(!waitForPromptsFinished) {
			try {
				controllerLock.lock();
				System.out.println("Controller: Got control.");
				hasControl = true;
				controllerCondition.signal();
			} finally {
				controllerLock.unlock();
			}
		}
	}
	
	private void allPromptsAreFinished() {
		if(waitForPromptsFinished) {
			try {
				controllerLock.lock();
				System.out.println("Controller: All expected prompts are answered.");
				controllerCondition.signal();
				waitForPromptsFinished = false;
			} finally {
				controllerLock.unlock();
			}
		}
	}
	
	public void addAction(String actionName, UUID card_id, String zoneName, Player player) {
		Player affectedPlayer;
		Player activatingPlayer = this.player;
		if(this.player == player || player == null) {
			affectedPlayer = this.player;
		}else affectedPlayer = player; 
		PhysicalPlayerAction playerAction = new PhysicalPlayerAction() {
		
			@Override
			public void perform() throws NotActivableException {
				
				IsAreaInGame zone = affectedPlayer.getGameZone(zoneName);
				if(card_id != null) {
					System.out.println("Controller: Execute Action "+actionName+" on Card "+ card_id.toString() + " in Zone " + zoneName+" (Player-"+activatingPlayer.getID()+", Thread"+Thread.currentThread().getName()+")");
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
										action.activate(activatingPlayer);
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
						circles.get(2).getAction().activate(activatingPlayer);
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
				game.processGameStack(player);
				ReentrantLock lock = game.getActivePhase().getActiveGameStack().getLock();
				Condition cond = game.getActivePhase().getActiveGameStack().getCondition();
				try {
					lock.lock();
					Thread.sleep(10);
					System.out.println("Controller: Wait...");
					cond.await();
					System.out.println("Controller: Stack finished.");
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					lock.unlock();
				}
			}
		});
	}
	
	public void addStackWait() {
		actionsToPerform.add(new PhysicalPlayerAction() {
			
			@Override
			public void perform() throws NotActivableException {
				System.out.println("Controller: Wait until stack finished."+" (Player-"+player.getID()+", Thread"+Thread.currentThread().getName()+")");
				OwnsGameStack stack = game.getActivePhase().getActiveGameStack();
				ReentrantLock lock = stack.getLock();
				Condition cond = stack.getCondition();
				try {
					lock.lock();
					cond.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					lock.unlock();
				}
			}
		});
	}
	
	public void addPhaseEndAction() {
		PhysicalPlayerAction action = new PhysicalPlayerAction() {
			
			@Override
			public void perform() throws NotActivableException {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Controller: Execute end phase"+" (Player-"+player.getID()+", Thread"+Thread.currentThread().getName()+")");
				game.proceed(player);				
			}
		};
		actionsToPerform.add(action);
	}
	
	public void addSurrenderAction() {
		PhysicalPlayerAction action = new PhysicalPlayerAction() {
			
			@Override
			public void perform() throws NotActivableException {
				System.out.println("Controller: Surrender"+" (Player-"+player.getID()+", Thread"+Thread.currentThread().getName()+")");
				player.decreaseHealthPoints(player.getHealthPoints());		
			}
		};
		actionsToPerform.add(action);
	}
	
	public void addGameFinishWait() {
		PhysicalPlayerAction action = new PhysicalPlayerAction() {
			
			@Override
			public void perform() throws NotActivableException {
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
		};
		actionsToPerform.add(action);
	}
	
	public void addPromptWait() {
		PhysicalPlayerAction action = new PhysicalPlayerAction() {
			
			@Override
			public void perform() throws NotActivableException {
				try {
					controllerLock.lock();
					System.out.println("Controller: Wait all expected prompts are handled."+" (Player-"+player.getID()+", Thread"+Thread.currentThread().getName()+")");
					waitForPromptsFinished = true;
					controllerCondition.await();
					waitForPromptsFinished = false;
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					controllerLock.unlock();
				}		
			}
		};
		actionsToPerform.add(action);
	}
	
	public void addExpectedPrompt(String id, PhysicalPlayerAction answer) {
		String indexedID = id;
		if(keyIndexOfExpectedPrompt.containsKey(id)) {
			int addingIndex = keyIndexOfExpectedPrompt.get(id).intValue();
			indexedID = indexedID + "_" + addingIndex;
			keyIndexOfExpectedPrompt.put(id, new Integer(addingIndex+1));
		}else {
			indexedID = indexedID + "_" + 0;
			keyIndexOfExpectedPrompt.put(id, new Integer(1));
			currentKeyIndexOfExpectedPrompt.put(id, new Integer(0));
		}
		expectedPrompts.put(indexedID, answer);
	}
	
	public void prompt(Player promptedPlayer, MessageInLanguage message, AcceptPromptAnswers prompter) {
		System.out.println("Controller-prompt: "+message.text);
		
	}

	public void prompt(Player promptedPlayer, MessageInLanguage message) {
		System.out.println("Controller-prompt: "+message.text+" (Player-"+player.getID()+", Thread"+Thread.currentThread().getName()+")");
		int currentIndex = getCurrentPrompKeyIndex(message.id);
		if(currentIndex == -1) return;
		PhysicalPlayerAction answer = expectedPrompts.get(message.id+"_"+currentIndex);
		if(answer != null) {
			currentKeyIndexOfExpectedPrompt.put(message.id, new Integer(currentIndex+1));
			new Thread(()->{
				try {
					System.out.println("Controller: Wait 1 second until giving answer to the prompt"+" (Player-"+player.getID()+", Thread"+Thread.currentThread().getName()+")");
					Thread.sleep(1000);//To emulate the delay of a natural person
					System.out.println("Controller: Give answer to the prompt"+" (Player-"+player.getID()+", Thread"+Thread.currentThread().getName()+")");
					answer.perform();
					expectedPrompts.remove(message.id);
				} catch (InterruptedException | NotActivableException e) {
					e.printStackTrace();
				}
			}, "ControllerThread_"+player.getID()).start(); 
		}else {
			if(game.getActiveBattle() != null) {
				game.getActiveBattle().proceed(promptedPlayer);
			}
		}
		expectedPrompts.remove(message.id+"_"+currentIndex);
		if(expectedPrompts.size() == 0) {
			currentKeyIndexOfExpectedPrompt.clear();
			keyIndexOfExpectedPrompt.clear();
			allPromptsAreFinished();
		}
	}
	
	private int getCurrentPrompKeyIndex(String id) {
		Integer value = currentKeyIndexOfExpectedPrompt.get(id);
		if(value == null) {
			return -1;
		}else return value.intValue();
	}

	@Override
	public void run() {
		controllerLock.lock();
		System.out.println("Controller started"+" (Player-"+player.getID()+", Thread"+Thread.currentThread().getName()+")");
		System.out.println("Controller: Wait until game gives control"+" (Player-"+player.getID()+", Thread"+Thread.currentThread().getName()+")");
		try {
			//gameCondition.await(10, TimeUnit.SECONDS);
			controllerCondition.await();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}finally {
			controllerLock.unlock();
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
	}
	
	@FunctionalInterface
	public interface PhysicalPlayerAction{
		public void perform() throws NotActivableException;
	}

	@Override
	public void stackableWasSetActive(Stackable stackable) {
		String messageID = "";
		
		switch(stackable.getCode()) {
		case "BlockCollectorAttack":
			messageID = "#10";
			BlockCollectorAttack action = (BlockCollectorAttack)stackable;
			String[] parameters = {action.savedCollector.getRealCard().getName(), action.savedCollector.getFreeEnergy()+"", action.savedCollector.getBlockedEnergy()+"", action.savedCollector.getUsedEnergy()+"", action.savedCollector.getDepletedEnergy()+""};
			prompt(player, GameMessageProvider.getInstance().getMessage("#10", Application.getInstance().getLanguage(), parameters));
			break;
		}
	}

	@Override
	public void stackableWasSetInactive(Stackable stackable) {
		// TODO Auto-generated method stub
		
	}
}
