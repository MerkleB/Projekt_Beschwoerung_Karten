package project.main.GameApplication;

import java.util.ArrayList;
import java.util.UUID;

import project.main.Action.Stackable;
import project.main.Card.ActionOwner;
import project.main.Card.Card;
import project.main.Card.EffectOwner;
import project.main.exception.InvalidCommandException;
import project.main.util.ManagesTextLanguages;
import project.main.util.TextProvider;

public class SimpleController implements ControlsStackables {
	
	private Player player;
	private ArrayList<Stackable> activStackables;
	
	public SimpleController(Player owner) {
		player = owner;
		activStackables = new ArrayList<Stackable>();
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
		try {
			String[] commandStructure = parseCommand(command);
			String[] path = commandStructure[2].split("-");
			Player targetPlayer = getTargetPlayer(path[0]);
			if(!activateStandardAction(commandStructure, path, targetPlayer)) {
				switch(commandStructure[0]) {
				case "Action":
					ActionOwner aTarget = getActionOwner(targetPlayer, path[1], path[2]);
					activate(aTarget, commandStructure[1]);
					break;
				case "Effect":
					EffectOwner eTarget = getEffectOwner(targetPlayer, path[1], path[2]);
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
	/**
	 * Returns the target player
	 * @param playerPathPart
	 * @return Player targetPlayer
	 */
	private Player getTargetPlayer(String playerPathPart) {
		Player targetPlayer = null;
		if(!playerPathPart.startsWith("Player=")) {
			String id = playerPathPart.substring(7);
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
	
	private boolean activateStandardAction(String[] commandHead, String[] path, Player targetPlayer) {
		boolean executed = true;
		ManagesTextLanguages text = TextProvider.getInstance();
		String language = Application.getInstance().getLanguage();
		switch(commandHead[1]) {
		case "Show":
			if(path[1].equalsIgnoreCase("all")) {
				ArrayList<IsAreaInGame> zones = targetPlayer.getGameZones();
				for(IsAreaInGame zone : zones) {
					if(!zone.getName().equals("DeckZone") && !(zone.getName().equals("HandZone") && player != targetPlayer)) {
						ArrayList<Card> cards = zone.getCards();
						for(Card card : cards) {
							System.out.println(text.getTerm(zone.getName(), language)+" -> "+card.getName()+", "
									+text.getTerm("Type", language)+": "+text.getTerm(card.getType().toString(), language)
									+" (ID="+card.getCardID()+")");
						}
					}
				}
			}else {
				
			}
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
		default:
			executed = false;
			break;
		}
		return executed;
	}
	
	private void activate(ActionOwner owner, String actionCode) throws InvalidCommandException {
		
	}
	
	private void activate(EffectOwner owner, String effectNumber) throws InvalidCommandException {
		if(owner == null) {
			throw new InvalidCommandException("Effect number "+effectNumber+"can't be activated.", "Target not found.");
		}
	}

}
