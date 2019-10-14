package project.test;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import project.main.Card.Card;
import project.main.Card.Spell;
import project.main.Card.Summon;
import project.main.Effect.Effect;
import project.main.GameApplication.Application;
import project.main.GameApplication.DrawPhase;
import project.main.GameApplication.GamePhase;
import project.main.GameApplication.HostsGame;
import project.main.GameApplication.IsAreaInGame;
import project.main.GameApplication.IsPhaseInGame;
import project.main.GameApplication.RefreshmentPhase;
import project.main.build_cards.CardFactory;
import project.main.build_cards.CardTypes;
import project.main.exception.NoCardException;
import project.main.jsonObjects.ActionDefinitionLibrary;
import project.main.jsonObjects.CardDefinitionLibrary;
import project.test.mok.PhysicalTestPlayer;
import project.test.mok.TestGame;
import project.test.mok.TestPlayer;

public class TestActions {
	
	private static String[] cardIDs = {"bsc-su-00-0","bsc-su-00-0","bsc-su-00-0","bsc-sp-00"};
	private ArrayList<Card> deck1;
	private ArrayList<Card> deck2;
	private TestPlayer player1;
	private TestPlayer player2;
	private HostsGame app;
	
	@BeforeClass
	public static void setUpBeforeClass() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field actionLibrary = ActionDefinitionLibrary.class.getDeclaredField("instance");
		actionLibrary.setAccessible(true);
		actionLibrary.set(null, null);
	}
	
	@Before
	public void setUp() throws Exception{
		deck1 = new ArrayList<Card>();
		deck2 = new ArrayList<Card>();
		for(String id : cardIDs) {
			deck1.add(CardFactory.getInstance().createCard(id, null));
			deck2.add(CardFactory.getInstance().createCard(id, null));
		}
		player1 = new TestPlayer(3, 3, deck1);
		player2 = new TestPlayer(3, 3, deck2);
		app = Application.getInstance();
		app.setLanguage("EN");
	}
	
	@AfterClass
	public static void teardownAterClass() throws Exception{
		Field actionLibrary = ActionDefinitionLibrary.class.getDeclaredField("instance");
		actionLibrary.setAccessible(true);
		actionLibrary.set(null, null);
		Field cardLibrary = CardDefinitionLibrary.class.getDeclaredField("instance");
		cardLibrary.setAccessible(true);
		cardLibrary.set(null, null);
	}
	
	public ArrayList<IsPhaseInGame> getPhases(String[] phaseNames){
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
				phases.add(new GamePhase(name));
				break;
			}
		}
		return phases;
	}
	
	private void setPlayerAndGameForCards(TestGame g) throws NoCardException {
		for(int i=0; i<deck1.size(); i++) {
			Card card1 = deck1.get(i);
			Card card2 = deck2.get(i);
			card1.setOwningPlayer(player1);
			card2.setOwningPlayer(player2);
			for(int j=0; j<card1.getActions().size(); j++) {
				card1.getActions().get(j).setGame(g);
			}
			for(int j=0; j<card2.getActions().size(); j++) {
				card2.getActions().get(j).setGame(g);
			}
			for(Effect effect : card1.getEffects()) {
				effect.setGame(g);
			}
			for(Effect effect : card2.getEffects()) {
				effect.setGame(g);
			}
		}
	}
	
	private Summon[] getFirstSummonFromZone(IsAreaInGame zone, int number) {
		Summon[] summons = new Summon[number];
		int selected = 0;
		ArrayList<Card> cards = zone.getCards();
		for(Card card : cards) {
			if(card.getType().equals(CardTypes.Summon)) {
				selected++;
				summons[selected-1] = (Summon)card;
			}
			if(selected == number) break;
		}
		return summons;
	}
	
	private Spell[] getFirstSpellFromZone(IsAreaInGame zone, int number) {
		Spell[] spells = new Spell[number];
		int selected = 0;
		ArrayList<Card> cards = zone.getCards();
		for(Card card : cards) {
			if(card.getType().equals(CardTypes.Spell)) {
				selected++;
				spells[selected-1] = (Spell)card;
			}
			if(selected == number) break;
		}
		return spells;
	}
	
	@Test
	public void testEvokeSummon() {
		/**
		 * Preparation 
		 */
		System.out.println("-=Test Evoke Summon=-");
		ReentrantLock lockGame = new ReentrantLock();
		ReentrantLock lockTest = new ReentrantLock();
		Condition gameCond = lockGame.newCondition();
		Condition testCond = lockTest.newCondition();
		String[] phases = {"Main"};
		TestGame game = new TestGame(player1, player2, getPhases(phases), gameCond, lockGame);
		player2.decreaseHealthPoints(3); //Ensure game ends after first round
		try {
			setPlayerAndGameForCards(game);
		} catch (NoCardException e1) {
			fail("Fail during preparation: set player and game for cards");
		}
		app.setGame(game);
		PhysicalTestPlayer controller1 = new PhysicalTestPlayer(player1, game, gameCond, testCond, lockGame, lockTest);
		PhysicalTestPlayer controller2 = new PhysicalTestPlayer(player2, game, gameCond, testCond, lockGame, lockTest);
		player1.setController(controller1);
		player2.setController(controller2);
		IsAreaInGame hand = player1.getGameZone("HandZone");
		IsAreaInGame deck = player1.getGameZone("DeckZone");
		IsAreaInGame collectorZone = player1.getGameZone("CollectorZone");
		Summon[] summons = getFirstSummonFromZone(deck, 1);
		Card card1 = summons[0];
		Card collector1 = deck.getCards().get(1);
		Card collector2 = deck.getCards().get(2);
		deck.removeCard(card1);
		deck.removeCard(collector1);
		deck.removeCard(collector2);
		hand.addCard(card1);
		collectorZone.addCard(collector1);
		collectorZone.addCard(collector2);
		controller1.addAction("EvokeSummon", card1.getID(), "HandZone");
		controller1.addStackStart();
		controller1.addPhaseEndAction();
		Thread gameThread = new Thread(game, "Game");
		Thread ctrlThread1 = new Thread(controller1, "Control1");		
		/**
		 * Test
		 */
		try {
			ctrlThread1.start();
			gameThread.start();
			//ctrlThread2.start();
			System.out.println("Test: Waiting for end game");
			lockTest.lock();
			testCond.await();
			System.out.println("Test: Got signal - Game ended");
		}catch(Exception e) {
			fail("An error happened - "+ e.getMessage());
		}finally {
			lockTest.unlock();
		}
		
		/**
		 * Check result
		 */
		if(hand.findCard(card1.getID()) != null) {
			fail("Card1 is still in hand");
		}
		if(player1.getGameZone("SummonZone").findCard(card1.getID()) == null) {
			fail("Card2 is not in SummonZone!");
		}
		
	}
	
	@Test
	public void testDraw() {
		/**
		 * Preparation 
		 */
		System.out.println("-=Test Draw=-");
		ReentrantLock lockGame = new ReentrantLock();
		ReentrantLock lockTest = new ReentrantLock();
		Condition gameCond = lockGame.newCondition();
		Condition testCond = lockTest.newCondition();
		String[] phases = {"DrawPhase"};
		TestGame game = new TestGame(player1, player2, getPhases(phases), gameCond, lockGame);
		player2.decreaseHealthPoints(3); //Ensure game ends after first round
		try {
			setPlayerAndGameForCards(game);
		} catch (NoCardException e1) {
			fail("Fail during preparation: set player and game for cards");
		}
		app.setGame(game);
		PhysicalTestPlayer controller1 = new PhysicalTestPlayer(player1, game, gameCond, testCond, lockGame, lockTest);
		player1.setController(controller1);
		IsAreaInGame hand = player1.getGameZone("HandZone");
		IsAreaInGame deck = player1.getGameZone("DeckZone");
		Card card1 = deck.getCards().get(3);
		controller1.addAction("Draw", card1.getID(), "DeckZone");
		controller1.addStackStart();
		controller1.addPhaseEndAction();
		Thread gameThread = new Thread(game, "Game");
		Thread ctrlThread1 = new Thread(controller1, "Control1");
		/**
		 * Test
		 */
		try {
			ctrlThread1.start();
			gameThread.start();
			System.out.println("Test: Waiting for end game");
			lockTest.lock();
			testCond.await();
			System.out.println("Test: Got signal - Game ended");
		}catch(Exception e) {
			fail("An error happened - "+ e.getMessage());
		}finally {
			lockTest.unlock();
		}
		/**
		 * Check result
		 */
		if(deck.findCard(card1.getID()) != null) {
			fail("Card1 is still in hand");
		}
		if(hand.findCard(card1.getID()) == null) {
			fail("Card2 is not in SummonZone!");
		}
		
	}
	
	@Test
	public void testHeal() {
		/**
		 * Preparation 
		 */
		System.out.println("-=Test Heal=-");
		ReentrantLock lockGame = new ReentrantLock();
		ReentrantLock lockTest = new ReentrantLock();
		Condition gameCond = lockGame.newCondition();
		Condition testCond = lockTest.newCondition();
		String[] phases = {"Main"};
		TestGame game = new TestGame(player1, player2, getPhases(phases), gameCond, lockGame);
		player2.decreaseHealthPoints(3); //Ensure game ends after first round
		try {
			setPlayerAndGameForCards(game);
		} catch (NoCardException e1) {
			fail("Fail during preparation: set player and game for cards");
		}
		app.setGame(game);
		PhysicalTestPlayer controller1 = new PhysicalTestPlayer(player1, game, gameCond, testCond, lockGame, lockTest);
		PhysicalTestPlayer controller2 = new PhysicalTestPlayer(player2, game, gameCond, testCond, lockGame, lockTest);
		player1.setController(controller1);
		player2.setController(controller2);
		IsAreaInGame summonZone = player1.getGameZone("SummonZone");
		IsAreaInGame deck = player1.getGameZone("DeckZone");
		Summon[] summons = getFirstSummonFromZone(deck, 2);
		Card card1 = summons[0];
		Card card2 = summons[1];
		if(card2.getType().equals(CardTypes.Spell)) {
			card2 = deck.getCards().get(2);
		}
		((Summon)card2).getStatus().decreaseVitality(2);
		int card2Vitality = ((Summon)card2).getStatus().getVitality();
		deck.removeCard(card1);
		deck.removeCard(card2);
		summonZone.addCard(card1);
		summonZone.addCard(card2);
		controller1.addAction("Heal", card1.getID(), "SummonZone");
		controller1.addAction("SelectSummon", card2.getID(), "SummonZone");
		controller1.addStackStart();
		controller1.addPhaseEndAction();
		Thread gameThread = new Thread(game, "Game");
		Thread ctrlThread1 = new Thread(controller1, "Control1");
		
		/**
		 * Test
		 */
		try {
			ctrlThread1.start();
			gameThread.start();
			System.out.println("Test: Waiting for end game");
			lockTest.lock();
			testCond.await();
			System.out.println("Test: Got signal - Game ended");
		}catch(Exception e) {
			fail("An error happened - "+ e.getMessage());
		}finally {
			lockTest.unlock();
		}
		
		/**
		 * Check result
		 */
		if(((Summon)card2).getStatus().getVitality() == card2Vitality) {
			fail("Card2 wasn't healed");
		}
		
		if(((Summon)card2).getStatus().getMaxVitality() != ((Summon)card2).getStatus().getVitality()) {
			fail("Card2 wan't healed to full health");
		}
		if(!((Summon)card1).getActivityStatus().equals(Summon.USED)) {
			fail("Card1's status was not set to used");
		}
		
	}
	
	@Test
	public void testCast() {
		/**
		 * Preparation 
		 */
		System.out.println("-=Test Cast=-");
		ReentrantLock lockGame = new ReentrantLock();
		ReentrantLock lockTest = new ReentrantLock();
		Condition gameCond = lockGame.newCondition();
		Condition testCond = lockTest.newCondition();
		String[] phases = {"Main"};
		TestGame game = new TestGame(player1, player2, getPhases(phases), gameCond, lockGame);
		try {
			setPlayerAndGameForCards(game);
		} catch (NoCardException e1) {
			fail("Fail during preparation: set player and game for cards");
		}
		app.setGame(game);
		PhysicalTestPlayer controller1 = new PhysicalTestPlayer(player1, game, gameCond, testCond, lockGame, lockTest);
		PhysicalTestPlayer controller2 = new PhysicalTestPlayer(player2, game, gameCond, testCond, lockGame, lockTest);
		player1.setController(controller1);
		player2.setController(controller2);
		IsAreaInGame hand = player1.getGameZone("HandZone");
		IsAreaInGame deck = player1.getGameZone("DeckZone");
		IsAreaInGame collectorZone = player1.getGameZone("CollectorZone");
		Spell[] spells = getFirstSpellFromZone(deck, 1);
		Card card1 = spells[0];
		deck.removeCard(card1);
		hand.addCard(card1);
		Card collector1 = deck.getCards().get(1);
		Card collector2 = deck.getCards().get(2);
		deck.removeCard(collector1);
		deck.removeCard(collector2);
		hand.addCard(card1);
		collectorZone.addCard(collector1);
		collectorZone.addCard(collector2);
		controller1.addAction("Cast", card1.getID(), "HandZone");
		controller1.addStackStart();
		controller1.addPhaseEndAction();
		Thread gameThread = new Thread(game, "Game");
		Thread ctrlThread1 = new Thread(controller1, "Control1");
		/**
		 * Test
		 */
		try {
			ctrlThread1.start();
			gameThread.start();
			System.out.println("Test: Waiting for end game");
			lockTest.lock();
			testCond.await();
			System.out.println("Test: Got signal - Game ended");
		}catch(Exception e) {
			fail("An error happened - "+ e.getMessage());
		}finally {
			lockTest.unlock();
		}
		
		/**
		 * Check result
		 */
		if(player1.getGameZone("HandZone").findCard(card1.getID()) != null) {
			fail("Card1 is still in HandZone");
		}
		
		if(player1.getGameZone("DiscardPile").findCard(card1.getID()) == null) {
			fail("Card1 is not in Discard Pile");
		}
		
		if(player2.getHealthPoints() != 0) {
			fail("Player2's shield points are unequal to 0 ("+player2.getHealthPoints()+")");
		}
		
	}

}
