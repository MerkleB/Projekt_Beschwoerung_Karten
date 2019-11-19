package project.main.GameApplication;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.UUID;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import project.main.Action.Stackable;
import project.main.Card.ActionOwner;
import project.main.Card.Card;
import project.main.Card.EffectOwner;
import project.main.Effect.Effect;
import project.main.exception.InvalidCommandException;
import project.main.exception.NoCardException;
import project.main.jsonObjects.MessageInLanguage;
import project.main.util.ManagesTextLanguages;
import project.main.util.TextProvider;

public class SimpleController implements ControlsStackables {
	
	private static final String SHOW = "Show";
	private static final String SURRENDER = "Surrender";
	private static final String PROCEED = "Proceed";
	private static final String PROCESSSTACK = "ProcessStack";
	private static final String TIMER = "Timer";
	private static final String STOP_TIMER = "StopTimer";
	private static final String COMMANDS = "Commands";
	private Player player;
	private AcceptPromptAnswers prompter;
	private boolean waitingForPromptAnswer;
	private static final String[] standardActions = {COMMANDS+" Shows all currently possible commands. Action:Commands", 
													SHOW+": Shows all cards, all cards in a decent zone or one decent card. Action:Show@[Player=[{PlayerID}]-]Zone=[{ZoneName}|all][-Card=[{CardId}|all]]"
															+"\r\n NOTE: If you want to show the SummoningCircleIds only refer nothing after refering the summoning zone." , 
													SURRENDER+" Player wants to surrender. Action:Surrender", 
													PROCEED+" Proceed to next phase. Action:Proceed", 
													PROCESSSTACK+" Processes the GameStack. Action:ProcessStack",
													TIMER+" Requests a timer which will initiate proceed. Action:Timer",
													STOP_TIMER+" Stops the timer. Action:StopTimer"}; 
	private ArrayList<Stackable> activStackables;
	
	public SimpleController(Player owner) {
		player = owner;
		activStackables = new ArrayList<Stackable>();
		prompter = null;
		waitingForPromptAnswer = false;
	}
	
	@Override
	public void stackableWasSetActive(Stackable stackable) {
		activStackables.add(stackable);
	}

	@Override
	public void stackableWasSetInactive(Stackable stackable) {
		activStackables.remove(stackable);
	}

	@Override
	public void executeCommand(String command) {
		if(prompter != null) {
			prompter.accept(command);
			prompter = null;
			return;
		}
		try {
			Hashtable<String, String> commandStructure = parseCommand(command);
			Hashtable<String, String> path = null; 
			if(commandStructure.get("Path") != null) {
				try {
					path = parsePath(commandStructure.get("Path").split("-"));
				} catch (Exception e) {
					System.out.println("'"+command+"' is not a valid command.");
				}
			}
			Player targetPlayer = null;
			if(path != null) {
				targetPlayer = getTargetPlayer(path.get("Player"));
			}
			if(!activateStandardAction(commandStructure, path, targetPlayer)) {
				switch(commandStructure.get("Type")) {
				case "Action":
					ActionOwner aTarget = getActionOwner(targetPlayer, path, commandStructure.get("Code"));
					activate(aTarget, commandStructure.get("Code"));
					break;
				case "Effect":
					EffectOwner eTarget = getEffectOwner(targetPlayer, path.get("Zone"), path.get("Card"));
					activate(eTarget, commandStructure.get("Code"));
					break;
				default:
					throw new InvalidCommandException(command, "Use type 'Action' or 'Effect'");
				}
				waitingForPromptAnswer = false;
			}
		} catch (InvalidCommandException e) {
			System.err.println(e.getMessage());
		}
	}
	@Override
	public void prompt(Player promptedPlayer, MessageInLanguage message, AcceptPromptAnswers prompter) {
		if(!waitingForPromptAnswer) {
			System.out.println(message.text+" ("+player.getID()+")");
			this.prompter = prompter; 
			waitingForPromptAnswer = true;
		}
	}

	@Override
	public void prompt(Player promptedPlayer, MessageInLanguage message) {
		if(!waitingForPromptAnswer) {
			System.out.println(message.text+" ("+player.getID()+")");
			waitingForPromptAnswer = true;
		}
	}

	@Override
	public void timerStarted(CountsTimeUntilProceed timer) {
		int time = timer.getRemainingTime() / 1000;
		System.out.println(time+"s");
	}

	@Override
	public void timerCountedDown(CountsTimeUntilProceed timer) {
		int time = timer.getRemainingTime() / 1000;
		System.out.println(time+"s");
	}

	@Override
	public void timerEnded(CountsTimeUntilProceed timer) {
		int time = timer.getRemainingTime() / 1000;
		System.out.println(time+"s");
	}

	/**
	 * Parses the comand into Hashtable with keys:
	 * Type : Type of Command
	 * Code : Code of Command
	 * Path : Path [Optional]
	 * @param command
	 * @return Hashtable representation of the command
	 * @throws InvalidCommandException
	 */
	private Hashtable<String, String> parseCommand(String command) throws InvalidCommandException{
		try {
			Hashtable<String, String> commandStructure = new Hashtable<String, String>();
			String[] commandHeadAndPath = command.split("@");
			if(commandHeadAndPath.length == 2) {
				commandStructure.put("Path", commandHeadAndPath[1]);
			}
			String[] commandTypeAndCode = commandHeadAndPath[0].split(":");
			commandStructure.put("Type", commandTypeAndCode[0]);
			commandStructure.put("Code", commandTypeAndCode[1]);
			return commandStructure;
		} catch (Exception e) {
			throw new InvalidCommandException(command, e.getMessage());
		}
	}
	
	private Hashtable<String, String> parsePath(String[] pathAsArray){
		Hashtable<String, String> path = new Hashtable<String, String>();
		String[] keyValue;
		for(String entry : pathAsArray) {
			keyValue = entry.split("=");
			path.put(keyValue[0], keyValue[1]);
		}
		return path;
	}
	/**
	 * Returns the target player
	 * @param playerPathPart
	 * @return Player targetPlayer
	 */
	private Player getTargetPlayer(String playerID) {
		Player targetPlayer = null;
		if(playerID != null) {
			String id = playerID.substring(7);
			targetPlayer = player.getGame().getPlayer(id);
		}else targetPlayer = player;
		return targetPlayer;
	}
	/**
	 * Retrieves the target action owner
	 * @param Player targetPlayer
	 * @param IsAreaInGame zonePathPart
	 * @param String cardPathPart
	 * @return ActionOwner
	 */
	private ActionOwner getActionOwner(Player targetPlayer, Hashtable<String, String> pathParts, String actionCode) {
		ActionOwner target = null;
		if(targetPlayer != null) {
			if("Draw".equals(actionCode)) {
				System.out.println("Controller: Draw from Deck.");
				IsAreaInGame targetZone = targetPlayer.getGameZone("DeckZone");
				int indexOfLastCard = targetZone.getCards().size()-1; 
				target = targetZone.getCards().get(indexOfLastCard);
			}else {
				String cardId = pathParts.get("Card");
				String circleId = pathParts.get("SummoningCircle");
				if(cardId != null) {
					IsAreaInGame targetZone = targetPlayer.getGameZone(pathParts.get("Zone"));
					target = targetZone.findCard(cardId);
				}else if(circleId != null) {
					IsAreaInGame targetZone = targetPlayer.getGameZone("SummonZone");
					ArrayList<SummoningCircle> circles = ((SummonZone)targetZone).getCircles();
					for(SummoningCircle circle : circles) {
						if(circleId.equals(circle.getID())) {
							target = circle;
						}
					}
				}
			}
		}
		return target;
	}
	
	/**
	 * Retrieves the target action owner
	 * @param Player targetPlayer
	 * @param IsAreaInGame zonePathPart
	 * @param String cardPathPart
	 * @return ActionOwner
	 */
	private EffectOwner getEffectOwner(Player targetPlayer, String zonePathPart, String cardPathPart) {
		EffectOwner target = null;
		if(targetPlayer != null) {
			if(cardPathPart.startsWith("Card=")) {
				String id = cardPathPart.substring(5);
				IsAreaInGame targetZone = targetPlayer.getGameZone(zonePathPart);
				target = targetZone.findCard(id);
			}
		}
		return target;
	}
	
	private boolean activateStandardAction(Hashtable<String, String> commandHead, Hashtable<String, String> path, Player targetPlayer) throws InvalidCommandException {
		boolean executed = true;
		String code = commandHead.get("Code");
		try {
			switch(code) {
			case "Show":
				if(path == null) throw new InvalidCommandException(code, "No target specified!");
				show(path, targetPlayer);
				break;
			case "Surrender":
				player.decreaseHealthPoints(player.getHealthPoints());
				player.getGame().proceed(player);
				break;
			case "Proceed":
				player.getGame().proceed(player);
				break;
			case "ProcessStack":
				player.getGame().processGameStack(player);
				break;
			case "Commands":
				displayCommands();
				break;
			case TIMER:
				player.getGame().startTimer(player);
				break;
			case STOP_TIMER:
				player.getGame().stopTimer(player);
				break;
			default:
				executed = false;
				break;
			}
		} catch (Exception e) {
			throw new InvalidCommandException(code, e.getMessage());
		}
		return executed;
	}
	
	private void show(Hashtable<String, String> path, Player targetPlayer) throws InvalidCommandException {
		String zoneName = path.get("Zone"); 
		if("all".equalsIgnoreCase(zoneName)) {
			ArrayList<IsAreaInGame> zones = targetPlayer.getGameZones();
			for(IsAreaInGame zone : zones) {
				showCards(targetPlayer, zone);
			}
		}else {
			IsAreaInGame zone = targetPlayer.getGameZone(zoneName);
			String cardId = path.get("Card"); 
			if(cardId != null) {
				if("all".equalsIgnoreCase(cardId)) {
					showCards(targetPlayer, zone);
				}else {
					IsAreaInGame targetZone = targetPlayer.getGameZone(zoneName);
					Card card = targetZone.findCard(cardId);
					showCardDetails(card);
				}
			}else {
				if("SummonZone".equals(zoneName)) {
					ArrayList<SummoningCircle> circles = ((SummonZone)player.getGameZone(zoneName)).getCircles();
					for(SummoningCircle circle : circles) {
						System.out.println(circle.getID());
					}
				}else {
					throw new InvalidCommandException("Show", "Invalid show command");
				}
			}
		}
	}
	
	private void showCards(Player targetPlayer, IsAreaInGame zone) {
		if(!zone.getName().equals("DeckZone") && !(zone.getName().equals("HandZone") && player != targetPlayer)) {
			ArrayList<Card> cards = zone.getCards();
			for(Card card : cards) {
				showCard(card, zone);
			}
		}
	}
	
	private void showCard(Card card, IsAreaInGame zone) {
		ManagesTextLanguages text = TextProvider.getInstance();
		String language = Application.getInstance().getLanguage();
		System.out.println(text.getTerm(zone.getName(), language).text+" -> "+card.getName()+", "
				+text.getTerm("Type", language).text+": "+text.getTerm(card.getType().toString(), language).text
				+" (ID="+card.getID()+")");
	}
	
	private void showCardDetails(Card card) {
		System.out.println(card.toString());
	}
	
	private void displayCommands() {
		System.out.println("Commands:");
		System.out.println("If no different description the format of a command is:");
		System.out.println("[Stackable-Type]:[Stackable-Code]@[Player={PlayerId}-]{ZoneName}-Card={CardId}]");
		System.out.println("[Action|Effect]:[Stackable-Code]@[Player={PlayerId}-]{ZoneName}-Card={CardId}]");
		for(String entry : standardActions) {
			System.out.println(" - "+entry);
		}
		for(Stackable entry : activStackables) {
			if(entry.activateable(player)) {
				String type = "Action";
				if(entry instanceof Effect) {
					type = "Effect";
				}
				System.out.println(" - "+type+": "+entry.getCode()+" ("+entry.getName()+", "+entry.getCard().getName()+"["+entry.getCard().getID()+"]) ");
			}
		}
	}
	
	private void activate(ActionOwner owner, String actionCode) throws InvalidCommandException {
		if(owner == null) {
			throw new InvalidCommandException("Action "+actionCode+"can't be activated.", "Target not found.");
		}
		owner.activateGameAction(actionCode, player);
		activStackables.clear();
		processStackIfNotWaitingForPromptAnswer();
		proceedIfPlayerCanNotDoAnything();
	}
	
	private void activate(EffectOwner owner, String effectNumber) throws InvalidCommandException {
		if(owner == null) {
			throw new InvalidCommandException("Effect number "+effectNumber+"can't be activated.", "Target not found.");
		}
		int effectIndex = Integer.parseInt(effectNumber);
		try {
			owner.activateEffect(effectIndex);
			activStackables.clear();
			processStackIfNotWaitingForPromptAnswer();
			proceedIfPlayerCanNotDoAnything();
		} catch (NoCardException e) {
			throw new InvalidCommandException("Effect number "+effectNumber+" could not be acitvated.", e.getMessage());
		}
	}
	
	private void processStackIfNotWaitingForPromptAnswer() {
		Game game = player.getGame();
		if(!waitingForPromptAnswer) {
			OwnsGameStack stack = game.getActivePhase().getActiveGameStack();
			ReentrantLock stackLock = stack.getLock();
			Condition stackCondition = stack.getCondition();
			try {
				stackLock.lock();
				System.out.println("Controller: Await stack finished processing.");
				player.getGame().processGameStack(player);
				stackCondition.await();
				System.out.println("Controller: Got recognized that stack finished processing.");
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			} finally {
				stackLock.unlock();
			}
		}
	}
	
	private void proceedIfPlayerCanNotDoAnything() {
		String phaseName = player.getGame().getActivePhase().getName();
		boolean playerCanNotDoAnything = true;
		if(phaseName.equals("RefreshmentPhase") || phaseName.equals("DrawPhase")) {
			for(Stackable stackable : activStackables) {
				if(stackable.activateable(player)) {
					playerCanNotDoAnything = false;
					break;
				}
			}
		}else playerCanNotDoAnything = false;
		playerCanNotDoAnything = false;
		if(playerCanNotDoAnything) {
			System.out.println("Controller: Automatic proceed because player can't do anything in this phase.");
			player.getGame().proceed(player);
		}
	}

}
