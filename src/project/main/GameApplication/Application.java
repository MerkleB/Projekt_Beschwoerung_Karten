package project.main.GameApplication;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import project.main.Card.Card;
import project.main.Card.MagicCollector;
import project.main.build_cards.CardFactory;
import project.main.build_cards.CardTypes;
import project.main.build_cards.CreatesCards;
import project.main.exception.CardCreationException;
import project.main.exception.InvalidCardException;
import project.main.exception.NoCardException;
import project.main.exception.NotAllowedCardException;
import project.main.jsonObjects.CardDefinition;
import project.main.jsonObjects.CardDefinitionLibrary;
import project.main.jsonObjects.HoldsCardDefinitions;
import project.main.util.MapsRankAndLevel;
import project.main.util.RankLevelMapper;

public class Application implements HostsGame {

	
	private static HostsGame instance;
	private Game game;
	private String language; 
	private Thread gameThread;
	
	/**
	 * Get instance and set Game if not null
	 * @return instance
	 */
	public static HostsGame getInstance() {
		if(instance == null) {
			instance = new Application();
		}
		return instance;
	}
	
	@Override
	public void startGame() {
		if(language == null || language.equals("")) {
			System.err.println("GameHost: Game could not be started. Language not set.");
			return;
		}
		
		if(game == null) {
			System.err.println("GameHost: Game could not be started. Game not set.");
			return;
		}
		Player[] players = game.getPlayers();
		for(Player player : players) {
			if(player == null) {
				System.err.println("GameHost: Game could not be started. Not all players set.");
				return;
			}
			if(player.getController() == null) {
				System.err.println("GameHost: Game could not be started. Player "+player.getID()+" has not controller.");
				return;
			}
		}
		gameThread = new Thread(game);
		gameThread.start();
	}

	@Override
	public void setGame(Game game) {
		if(game != null) {
			this.game = game;
		}
	}
	
	@Override
	public Game getGame() {
		return game;
	}

	@Override
	public void setLanguage(String language) {
		this.language = language;
	}

	@Override
	public String getLanguage() {
		return this.language;
	}
	
	public static void main(String[] args) {
		/**
		 * Setup Game
		 */
		HostsGame app = Application.getInstance();
		app.setLanguage("EN");
		CreatesCards cardFactory = CardFactory.getInstance();
		ArrayList<String> sets = CardDefinitionLibrary.getInstance().getCardSetNames();
		ArrayList<Card> deckPlayer1 = new ArrayList<Card>();
		ArrayList<Card> deckPlayer2 = new ArrayList<Card>();
		HoldsCardDefinitions cardLibrary = CardDefinitionLibrary.getInstance();
		MapsRankAndLevel mapper = RankLevelMapper.getInstance();
		for(String cardSetName : sets) {
			ArrayList<String> cardIds = cardLibrary.getCardIdsInSet(cardSetName);
			for(String cardId : cardIds) {
				CardDefinition definition = cardLibrary.getCardDefinition(cardId);
				for(int i=0; i<6; i++) {
					if(definition.type.equals("Summon")) {
						if(mapper.mapRankToLevel(definition.rank) != 0) {
							break;
						}
					}
					try {
						Card card1 = cardFactory.createCard(cardId, app.getGame());
						card1.setID("Player1_"+card1.getName()+"_"+i);
						deckPlayer1.add(card1);
						Card card2 = cardFactory.createCard(cardId, app.getGame());
						card2.setID("Player2_"+card2.getName()+"_"+i);
						deckPlayer2.add(card2);
					} catch (InvalidCardException | NotAllowedCardException | CardCreationException | NoCardException e) {
						System.out.println("Card could not be added: "+e.getMessage());
					}
				}
			}
		}
		Player player1 = new CardPlayer("Player1", deckPlayer1);
		player1.setController(new SimpleController(player1));
		Player player2 = new CardPlayer("Player2", deckPlayer2);
		player2.setController(new SimpleController(player2));
		app.setGame(new CardGame(player1, player2));
		/**
		 * Setup start status
		 */
		Card cardInGamePlayer1 = null;
		Card cardInGamePlayer2 = null;
		Card cardTwoInGamePlayer2 = null;
		MagicCollector collectorOfPlayer1 = null;
		MagicCollector collectorOfPlayer2 = null;
		IsAreaInGame deckZonePlayer1 = player1.getGameZone("DeckZone");
		IsAreaInGame deckZonePlayer2 = player2.getGameZone("DeckZone");
		IsAreaInGame handZonePlayer1 = player1.getGameZone("HandZone");
		IsAreaInGame handZonePlayer2 = player2.getGameZone("HandZone");
		while(cardInGamePlayer1 == null || collectorOfPlayer1 == null) {
			Card card = deckZonePlayer1.getCards().get(0);
			if(card.getType().equals(CardTypes.Summon) && cardInGamePlayer1 == null) {
				cardInGamePlayer1 = card;
				deckZonePlayer1.removeCard(card);
				handZonePlayer1.addCard(card);
			}else {
				collectorOfPlayer1 = card.getCollector();
				deckZonePlayer1.removeCard(card);
				handZonePlayer1.addCard(card);
			}
		}
		
		while(cardInGamePlayer2 == null || collectorOfPlayer2 == null || cardTwoInGamePlayer2 == null) {
			Card card = deckZonePlayer2.getCards().get(0);
			if(card.getType().equals(CardTypes.Summon) && (cardInGamePlayer2 == null || cardTwoInGamePlayer2 == null)) {
				if(cardInGamePlayer2 == null) {
					cardInGamePlayer2 = card;
					deckZonePlayer2.removeCard(card);
					handZonePlayer2.addCard(card);
				}else { //Definetely no dead code. Reached if: card is Summon AND cardInGamePlayer2 is not null AND cardTwoIsGamePlayer is null
					cardTwoInGamePlayer2 = card;
					deckZonePlayer2.removeCard(card);
					handZonePlayer2.addCard(card);
				}
			}else {
				collectorOfPlayer2 = card.getCollector();
				deckZonePlayer2.removeCard(card);
				handZonePlayer2.addCard(card);
			}
		}
		final String idOfCardInGameOfPlayer1 = cardInGamePlayer1.getID();
		final String idOfCollectorOfPlayer1 = collectorOfPlayer1.getID();
		final String idOfCardInGameOfPlayer2 = cardInGamePlayer2.getID();
		final String idOfCardTwoInGameOfPlayer2 = cardTwoInGamePlayer2.getID();
		final String idOfCollectorOfPlayer2 = collectorOfPlayer2.getID();
		/**
		 * Setup controlling
		 */
		ReentrantLock lockCPU_Player = new ReentrantLock();
		Condition conditionCPU_Player = lockCPU_Player.newCondition();
		Thread cpuPlayerControllThread = new Thread(()->{
			ControlsStackables controller = player2.getController();
			try {
				lockCPU_Player.lock();
				conditionCPU_Player.await();
			} catch (InterruptedException e) {
				System.err.println(Thread.currentThread().getName()+": "+e.getMessage());
			} finally {
				lockCPU_Player.unlock();
			}
			try {
				lockCPU_Player.lock();
				String roundCode = app.getGame().getRound();
				Thread.sleep(20);
				switch (roundCode) {
				case "0-1":
					controller.executeCommand("Action:Proceed");//Proceed after RefreshmentPhase
					Thread.sleep(20);
					controller.executeCommand("Action:Draw@Zone=DeckZone");
					Thread.sleep(20);
					controller.executeCommand("Action:Proceed");//Proceed after Draw Phase
					Thread.sleep(20);
					controller.executeCommand("Action:SetAsCollector@Zone=HandZone-Card="+idOfCollectorOfPlayer2);
					Thread.sleep(20);
					controller.executeCommand("Action:EvokeSummon@Zone=HandZone-Card="+idOfCardInGameOfPlayer2);
					Thread.sleep(20);
					controller.executeCommand("Action:SelectSummoningCircle@Zone=SummonZone-SummoningCircle=Player2_0_1");
					Thread.sleep(20);
					controller.executeCommand("Action:AttackPlayer@Zone=SummonZone-Card="+idOfCardInGameOfPlayer2);
					Thread.sleep(120000);
					controller.executeCommand("Action:Proceed");//Proceed after Main
					Thread.sleep(20);
					controller.executeCommand("Action:Proceed");//Proceed after End Phase
					break;
				case "1-1":
					controller.executeCommand("Action:Proceed");//Proceed after RefreshmentPhase
					Thread.sleep(20);
					controller.executeCommand("Action:Draw@Zone=DeckZone");
					Thread.sleep(20);
					controller.executeCommand("Action:Proceed");//Proceed after Draw Phase
					Thread.sleep(20);
					controller.executeCommand("Action:EvokeSummon@Zone=HandZone-Card="+idOfCardTwoInGameOfPlayer2);
					Thread.sleep(20);
					controller.executeCommand("Action:SelectSummoningCircle@Zone=SummonZone-SummoningCircle=Player2_0_2");
					Thread.sleep(20);
					controller.executeCommand("Action:AttackCollector@Zone=SummonZone-Card="+idOfCardTwoInGameOfPlayer2);
					Thread.sleep(20);
					controller.executeCommand("Action:SelectCollector@Zone=CollectorZone-Card="+idOfCollectorOfPlayer1);
					Thread.sleep(60000);
					controller.executeCommand("Action:Proceed");//Proceed after Main
					Thread.sleep(20);
					controller.executeCommand("Action:Proceed");//Proceed after End Phase
					break;
				default:
					controller.executeCommand("Action:Proceed");//Proceed after RefreshmentPhase
					Thread.sleep(20);
					controller.executeCommand("Action:Draw@DeckZone");
					Thread.sleep(20);
					controller.executeCommand("Action:Proceed");//Proceed after Draw Phase
					Thread.sleep(20);
					controller.executeCommand("Action:Proceed");//Proceed after Main
					Thread.sleep(20);
					controller.executeCommand("Action:Proceed");//Proceed after End Phase
					break;
				}
				conditionCPU_Player.signal();
			} catch (InterruptedException e) {
				System.err.println(Thread.currentThread().getName()+": "+e.getMessage());
			} finally {
				lockCPU_Player.unlock();
			}
		}, "cpuPlayerControll");
		Thread cpuPlayerWakeningThread = new Thread(()->{
			while(!Thread.currentThread().isInterrupted() && !app.getGame().hasEnded()) {
				if(player2 == app.getGame().getActivePlayer()) {
					try {
						lockCPU_Player.lock();
						conditionCPU_Player.signal();
						conditionCPU_Player.await();
					} catch (InterruptedException e) {
						System.err.println(Thread.currentThread().getName()+": "+e.getMessage());
					} finally {
						lockCPU_Player.unlock();
					}
				}
			}
		}, "cpuPlayerWakening");
		Thread realPlayerControlThread = new Thread(()->{
			System.out.println("Hi Player. I'm waiting for your input.");
			ControlsStackables controller = player1.getController();
			Scanner scanner = new Scanner(System.in);
			while(!app.getGame().hasEnded()) {
				String command = scanner.nextLine();
				controller.executeCommand(command);
			}
			scanner.close();
			
		}, "realPlayerControl");
		System.out.println("Player1 ("+player1.getID()+") starts with following cards: "+idOfCardInGameOfPlayer1+", "+idOfCollectorOfPlayer1);
		System.out.println("Player2 ("+player2.getID()+") starts with following cards: "+idOfCardInGameOfPlayer2+", "+idOfCardTwoInGameOfPlayer2+", "+idOfCollectorOfPlayer1);
		System.out.println("Player1 begins.");
		cpuPlayerControllThread.start();
		try {
			Thread.sleep(20);
			cpuPlayerWakeningThread.start();
			Thread.sleep(20);
			app.startGame();
			realPlayerControlThread.start();
		} catch (InterruptedException e) {
			System.err.println(Thread.currentThread().getName()+": "+e.getMessage());
		}
	}
}
