package project.main.Action;

import java.util.ArrayList;
import java.util.Collections;

import project.main.Card.Card;
import project.main.GameApplication.AcceptPromptAnswers;
import project.main.GameApplication.Application;
import project.main.GameApplication.DeckZone;
import project.main.GameApplication.GameStack;
import project.main.GameApplication.HandZone;
import project.main.GameApplication.IsAreaInGame;
import project.main.GameApplication.Player;
import project.main.Listeners.GameListener;
import project.main.exception.NotActivableException;
import project.main.util.GameMessageProvider;

public class Draw extends Action implements AcceptPromptAnswers {

	private Player promptedPlayer;

	@Override
	public String getCode() {
		return "Draw";
	}

	@Override
	public void activate(Player activator) throws NotActivableException {
		super.activate(activator);
		initMetadata();
		metadata.put("Target-ID", owningCard.getID().toString());
		game.getActivePhase().getActiveGameStack().addEntry(this);
		if(checkIfCardCanBeDrawnFromDeck()) {
			GameListener.getInstance().actionActivated(this);
		}else {
			withdrawn = true;
			promptedPlayer = activator;
			game.prompt(promptedPlayer, GameMessageProvider.getInstance().getMessage("#1", Application.getInstance().getLanguage()), this);
		}
		
	}

	@Override
	public void activateBy(Stackable activator, Player activatingPlayer) throws NotActivableException {
		super.activateBy(activator, activatingPlayer);
		initMetadata();
		metadata.put("Target-ID", owningCard.getID().toString());
		game.getActivePhase().getActiveGameStack().addEntry(this);
		GameListener.getInstance().actionActivated(this);
	}

	@Override
	public void execute() {
		if(isActivated && !withdrawn) {
			super.execute();
			Player player = owningCard.getOwningPlayer();
			DeckZone deck = (DeckZone)player.getGameZone("DeckZone");
			HandZone hand = (HandZone)player.getGameZone("HandZone");
			deck.removeCard(owningCard);
			hand.addCard(owningCard);
			GameListener.getInstance().actionExecuted(this);
		}
	}
	
	private boolean checkIfCardCanBeDrawnFromDeck() {
		boolean result = false;
		IsAreaInGame deck = owningCard.getOwningPlayer().getGameZone("DeckZone");
		if(deck.getCards().size() != 0) {
			result = true;
			if(owningCard.getCardID().equals("DUMMY")) {
				result = false;
			}
		}else {
			result = false;
		}
		return result;
	}

	@Override
	public void accept(String answer) {
		if(owningCard.getOwningPlayer().getHealthPoints() < 5) {
			
		}
		switch(answer) {
		case "pay":
			owningCard.getOwningPlayer().decreaseHealthPoints(5);
			IsAreaInGame discardPile = owningCard.getOwningPlayer().getGameZone("DiscardPile");
			ArrayList<Card> pile = (ArrayList<Card>) discardPile.getCards().clone();
			Collections.shuffle(pile);
			Card cardFromDiscardPile = pile.get(pile.size()-1);
			discardPile.removeCard(cardFromDiscardPile);
			IsAreaInGame deck = owningCard.getOwningPlayer().getGameZone("DeckZone");
			deck.addCard(cardFromDiscardPile);
			cardFromDiscardPile.activateGameAction("Draw", actionIsActivFor, this);
			break;
		case "damage":
			owningCard.getOwningPlayer().decreaseHealthPoints(1);
			break;
		default:
			game.prompt(promptedPlayer, GameMessageProvider.getInstance().getMessage("#1", Application.getInstance().getLanguage()), this);
			break;
		}
	}
}
