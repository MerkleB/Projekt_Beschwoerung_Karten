package project.main.GameApplication;

import java.util.ArrayList;

import project.main.Action.GameAction;
import project.main.Action.Stackable;
import project.main.Card.Card;
import project.main.Effect.Effect;
import project.main.Listeners.EffectListener;
import project.main.Listeners.GameActionListener;
import project.main.Listeners.GameListener;
import project.main.exception.NoCardException;
import project.main.jsonObjects.ActionDefinitionLibrary;

public class BattlePhase implements IsPhaseInGame, GameActionListener, EffectListener {
	
	private Game game;
	private String name;
	private ArrayList<String> actionsToActivate;
	private ArrayList<OwnsGameStack> finishedStacks;
	private OwnsGameStack activeGameStack;
	
	public BattlePhase(String name) {
		this.name = name;
		finishedStacks = new ArrayList<OwnsGameStack>();
		GameListener.getInstance().addGameActionListener(this);
		GameListener.getInstance().addEffectListener(this);
	}
	
	@Override
	public Game getGame() {
		return game;
	}

	@Override
	public void setGame(Game game) {
		this.game = game;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void restorePhaseStatus() {
		inactivateAll();
		Player player = game.getActiveBattle().getActivePlayer();
		if(player != null) {
			ArrayList<IsAreaInGame> zones = player.getGameZones();
			for(IsAreaInGame zone : zones) {
				zone.activate(player, this);
			}
		}
	}
	
	private void inactivateAll() {
		Player[] players = game.getPlayers();
		for(Player player : players) {
			ArrayList<IsAreaInGame> zones = player.getGameZones();
			for(IsAreaInGame zone : zones) {
				zone.deavtivateAll();
			}
		}
	}

	@Override
	public ArrayList<String> getActionsToActivate() {
		if(actionsToActivate == null) {
			actionsToActivate = ActionDefinitionLibrary.getInstance().getPhaseActions(name);
		}
		return actionsToActivate;
	}

	@Override
	public void process() {
		restorePhaseStatus();
		GameListener.getInstance().phaseStarted(this);
	}

	@Override
	public void leave() {
		finishedStacks.add(activeGameStack);
		activeGameStack = null;
		Player activPlayer = game.getActivePlayer();
		ArrayList<IsAreaInGame> zones = activPlayer.getGameZones();
		for(IsAreaInGame zone : zones) {
			zone.deavtivateAll();
		}
		GameListener.getInstance().phaseEnded(this);
	}

	@Override
	public OwnsGameStack getActiveGameStack() {
		if(activeGameStack == null) {
			activeGameStack = GameStack.getInstance(this);
		}
		if(activeGameStack.hasFinished()) {
			finishedStacks.add(activeGameStack);
			activeGameStack = GameStack.getInstance(this);
		}
		return activeGameStack;
	}

	@Override
	public ArrayList<OwnsGameStack> getFinisheGameStacks() {
		return finishedStacks;
	}

	@Override
	public void effectActivated(Effect effect) {
		if(effect.getActivator() == game.getActiveBattle().getActivePlayer()) {
			Player activePlayer = game.getActiveBattle().getActivePlayer();
			deactivateAllOtherStackables(activePlayer, effect);
		}
	}

	@Override
	public void effectExecuted(Effect effect) {/*Do nothing*/}
	
	@Override
	public void actionActivated(GameAction action) {
		if(action.getActivator() == game.getActivePlayer()) {
			Player activePlayer = game.getActiveBattle().getActivePlayer();
			deactivateAllOtherStackables(activePlayer, action);
		}
	}

	@Override
	public void actionExecuted(GameAction action) {/*Do nothing*/}
	
	private void deactivateAllOtherStackables(Player player, Stackable stackable) {
		if(player != null) {
			ArrayList<IsAreaInGame> zones = player.getGameZones();
			for(IsAreaInGame zone : zones) {
				ArrayList<Card> cards = zone.getCards();
				for(Card card : cards) {
					try {
						Effect[] effects = card.getEffects();
						for(Effect cardEffect : effects) {
							if(stackable != cardEffect) {
								cardEffect.setInactiv();
							}
						}
						ArrayList<GameAction> actions = card.getActions();
						for(GameAction cardAction : actions) {
							if(stackable != cardAction) {
								cardAction.setInactiv();
							}
						}
					} catch (NoCardException e) {
						System.out.println(name+": Collector effect. Ignored.");
					}
					
				}
			}
		}
	}

}
