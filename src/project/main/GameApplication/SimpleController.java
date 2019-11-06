package project.main.GameApplication;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.UUID;

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
	private static final String COMMANDS = "Show";
	private Player player;
	private AcceptPromptAnswers prompter;
	private static final String[] standardActions = {COMMANDS+" Shows all currently possible commands. Action:Commands", 
													SHOW+": Shows all cards, all cards in a decent zone or one decent card. Action:Show@[Player=[{PlayerID}]-]Zone=[{ZoneName}|all]-Card=[{CardId}|all]", 
													SURRENDER+" Player wants to surrender. Action:Surrender", 
													PROCEED+" Proceed to next phase. Action:Proceed", 
													PROCESSSTACK+" Processes the GameStack. Action:ProcessStack"}; 
	private ArrayList<Stackable> activStackables;
	
	public SimpleController(Player owner) {
		player = owner;
		activStackables = new ArrayList<Stackable>();
		prompter = null;
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
			String[] commandStructure = parseCommand(command);
			Hashtable<String, String> path = parsePath(commandStructure[2].split("-"));
			Player targetPlayer = getTargetPlayer(path.get("Player"));
			if(!activateStandardAction(commandStructure, path, targetPlayer)) {
				switch(commandStructure[0]) {
				case "Action":
					ActionOwner aTarget = getActionOwner(targetPlayer, path.get("Zone"), path.get("Card"));
					activate(aTarget, commandStructure[1]);
					break;
				case "Effect":
					EffectOwner eTarget = getEffectOwner(targetPlayer, path.get("Zone"), path.get("Card"));
					activate(eTarget, commandStructure[1]);
					break;
				default:
					throw new InvalidCommandException(command, "Use type 'Action' or 'Effect'");
				}
			}
		} catch (InvalidCommandException e) {
			System.err.println(e.getMessage());
		}
	}
	@Override
	public void prompt(Player promptedPlayer, MessageInLanguage message, AcceptPromptAnswers prompter) {
		System.out.println(message.text);
		this.prompter = prompter; 
	}

	@Override
	public void prompt(Player promptedPlayer, MessageInLanguage message) {
		System.out.println(message.text);
	}

	/**
	 * Parses the comand into array of Structure:
	 * Array[0] : Type of Stackable
	 * Array[1] : Code of Stackable
	 * Array[2] : Path
	 * @param command
	 * @return Array representation of the command
	 * @throws InvalidCommandException
	 */
	private String[] parseCommand(String command) throws InvalidCommandException{
		try {
			String[] commandStructure = new String[3];
			String[] commandHeadAndPath = command.split("@");
			commandStructure[2] = commandHeadAndPath[1];
			String[] commandTypeAndCode = commandHeadAndPath[0].split(":");
			commandStructure[0] = commandTypeAndCode[0];
			commandStructure[1] = commandTypeAndCode[1];
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
			targetPlayer = player.getGame().getPlayer(UUID.fromString(id));
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
	private ActionOwner getActionOwner(Player targetPlayer, String zonePathPart, String cardPathPart) {
		ActionOwner target = null;
		if(targetPlayer != null) {
			if(cardPathPart.startsWith("Card=")) {
				String id = cardPathPart.substring(5);
				IsAreaInGame targetZone = targetPlayer.getGameZone(zonePathPart);
				target = targetZone.findCard(UUID.fromString(id));
			}else if(cardPathPart.startsWith("SummoningCircle=")) {
				String id = cardPathPart.substring(16);
				IsAreaInGame targetZone = targetPlayer.getGameZone("SummonZone");
				ArrayList<SummoningCircle> circles = ((SummonZone)targetZone).getCircles();
				for(SummoningCircle circle : circles) {
					if(UUID.fromString(id).equals(circle.getID())) {
						target = circle;
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
				target = targetZone.findCard(UUID.fromString(id));
			}
		}
		return target;
	}
	
	private boolean activateStandardAction(String[] commandHead, Hashtable<String, String> path, Player targetPlayer) throws InvalidCommandException {
		boolean executed = true;
		try {
			switch(commandHead[1]) {
			case "Show":
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
			default:
				executed = false;
				break;
			}
		} catch (Exception e) {
			throw new InvalidCommandException(commandHead[1], e.getMessage());
		}
		return executed;
	}
	
	private void show(Hashtable<String, String> path, Player targetPlayer) {
		String zoneName = path.get("Zone"); 
		if(zoneName.equalsIgnoreCase("all")) {
			ArrayList<IsAreaInGame> zones = targetPlayer.getGameZones();
			for(IsAreaInGame zone : zones) {
				showCards(targetPlayer, zone);
			}
		}else {
			IsAreaInGame zone = targetPlayer.getGameZone(zoneName);
			String cardId = path.get("Card"); 
			if(cardId.equalsIgnoreCase("all")) {
				showCards(targetPlayer, zone);
			}else {
				IsAreaInGame targetZone = targetPlayer.getGameZone(zoneName);
				Card card = targetZone.findCard(UUID.fromString(cardId));
				showCardDetails(card);
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
		System.out.println(text.getTerm(zone.getName(), language)+" -> "+card.getName()+", "
				+text.getTerm("Type", language)+": "+text.getTerm(card.getType().toString(), language)
				+" (ID="+card.getCardID()+")");
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
			System.out.println(entry);
		}
		for(Stackable entry : activStackables) {
			if(entry.activateable(player)) {
				String type = "Action";
				if(entry instanceof Effect) {
					type = "Effect";
				}
				System.out.println(type+": "+entry.getCode()+" ("+entry.getName()+", "+entry.getCard().getName()+"["+entry.getCard().getID()+"]) ");
			}
		}
	}
	
	private void activate(ActionOwner owner, String actionCode) throws InvalidCommandException {
		if(owner == null) {
			throw new InvalidCommandException("Action "+actionCode+"can't be activated.", "Target not found.");
		}
		owner.activateGameAction(actionCode, player);
		activStackables.clear();
		player.getGame().processGameStack(player);
	}
	
	private void activate(EffectOwner owner, String effectNumber) throws InvalidCommandException {
		if(owner == null) {
			throw new InvalidCommandException("Effect number "+effectNumber+"can't be activated.", "Target not found.");
		}
		int effectIndex = Integer.parseInt(effectNumber);
		try {
			owner.activateEffect(effectIndex);
			activStackables.clear();
			player.getGame().processGameStack(player);
		} catch (NoCardException e) {
			throw new InvalidCommandException("Effect number "+effectNumber+" could not be acitvated.", e.getMessage());
		}
	}

}
