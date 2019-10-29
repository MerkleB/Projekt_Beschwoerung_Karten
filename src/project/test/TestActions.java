package project.test;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import project.main.Action.GameAction;
import project.main.Card.ActivityStatus;
import project.main.Card.Card;
import project.main.Card.Spell;
import project.main.Card.StatusChange;
import project.main.Card.Summon;
import project.main.Effect.Effect;
import project.main.GameApplication.Application;
import project.main.GameApplication.DrawPhase;
import project.main.GameApplication.GamePhase;
import project.main.GameApplication.HostsGame;
import project.main.GameApplication.IsAreaInGame;
import project.main.GameApplication.IsPhaseInGame;
import project.main.GameApplication.RefreshmentPhase;
import project.main.Listeners.GameActionListener;
import project.main.Listeners.GameListener;
import project.main.build_cards.CardFactory;
import project.main.build_cards.CardTypes;
import project.main.exception.NoCardException;
import project.main.jsonObjects.ActionDefinitionLibrary;
import project.main.jsonObjects.CardDefinitionLibrary;
import project.main.jsonObjects.MessageInLanguage;
import project.main.util.GameMessageProvider;
import project.test.mok.PhysicalTestPlayer;
import project.test.mok.TestGame;
import project.test.mok.TestPlayer;
import project.test.util.ZoneCardProvider;

public class TestActions {
	
	private static String[] cardIDs = {"bsc-su-00-0","bsc-su-00-0","bsc-su-00-0","bsc-sp-00"};
	private ArrayList<Card> deck1;
	private ArrayList<Card> deck2;
	private TestPlayer player1;
	private TestPlayer player2;
	private HostsGame app;
	private ZoneCardProvider cardProvider;
	
	@BeforeClass
	public static void setUpBeforeClass() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field actionLibrary = ActionDefinitionLibrary.class.getDeclaredField("instance");
		actionLibrary.setAccessible(true);
		actionLibrary.set(null, null);
		Field messageProvider = GameMessageProvider.class.getDeclaredField("instance");
		messageProvider.setAccessible(true);
		messageProvider.set(null, null);
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
		cardProvider = new ZoneCardProvider();
	}
	
	@AfterClass
	public static void teardownAterClass() throws Exception{
		Field actionLibrary = ActionDefinitionLibrary.class.getDeclaredField("instance");
		actionLibrary.setAccessible(true);
		actionLibrary.set(null, null);
		Field cardLibrary = CardDefinitionLibrary.class.getDeclaredField("instance");
		cardLibrary.setAccessible(true);
		cardLibrary.set(null, null);
		Field messageProvider = GameMessageProvider.class.getDeclaredField("instance");
		messageProvider.setAccessible(true);
		messageProvider.set(null, null);
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
	
	private void setGameForZones(TestGame g) {
		ArrayList<IsAreaInGame> zones1 = player1.getGameZones();
		ArrayList<IsAreaInGame> zones2 = player1.getGameZones();
		for(IsAreaInGame zone : zones1) {
			zone.setGame(g);
		}
		for(IsAreaInGame zone : zones2) {
			zone.setGame(g);
		}
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
		setGameForZones(game);
		app.setGame(game);
		PhysicalTestPlayer controller1 = new PhysicalTestPlayer(player1, game, gameCond, testCond, lockGame, lockTest);
		PhysicalTestPlayer controller2 = new PhysicalTestPlayer(player2, game, gameCond, testCond, lockGame, lockTest);
		player1.setController(controller1);
		player2.setController(controller2);
		IsAreaInGame hand = player1.getGameZone("HandZone");
		IsAreaInGame deck = player1.getGameZone("DeckZone");
		IsAreaInGame collectorZone = player1.getGameZone("CollectorZone");
		Summon[] summons = cardProvider.getFirstSummonFromZone(deck, 1);
		Card card1 = summons[0];
		Card collector1 = deck.getCards().get(1);
		Card collector2 = deck.getCards().get(2);
		deck.removeCard(card1);
		deck.removeCard(collector1);
		deck.removeCard(collector2);
		hand.addCard(card1);
		collectorZone.addCard(collector1);
		collectorZone.addCard(collector2);
		controller1.addAction("EvokeSummon", card1.getID(), "HandZone", null);
		controller1.addAction("SelectSummoningCircle", null, "SummonZone", null);
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
		controller1.addAction("Draw", card1.getID(), "DeckZone", null);
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
		Summon[] summons = cardProvider.getFirstSummonFromZone(deck, 2);
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
		controller1.addAction("Heal", card1.getID(), "SummonZone", null);
		controller1.addAction("SelectSummon", card2.getID(), "SummonZone", null);
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
		if(!((Summon)card1).getActivityStatus().getStatus().equals(ActivityStatus.USED)) {
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
		Spell[] spells = cardProvider.getFirstSpellFromZone(deck, 1);
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
		controller1.addAction("Cast", card1.getID(), "HandZone", null);
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
	
	@Test
	public void testDeclareBattle() {
		/**
		 * Preparation 
		 */
		System.out.println("-=Test DeclareBattle=-");
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
		IsAreaInGame summonZone1 = player1.getGameZone("SummonZone");
		IsAreaInGame deck1 = player1.getGameZone("DeckZone");
		Summon[] summons = cardProvider.getFirstSummonFromZone(deck1, 1);
		Card card1 = summons[0];
		IsAreaInGame summonZone2 = player2.getGameZone("SummonZone");
		IsAreaInGame deck2 = player2.getGameZone("DeckZone");
		summons = cardProvider.getFirstSummonFromZone(deck2, 1);
		Card card2 = summons[0];
		deck1.removeCard(card1);
		deck2.removeCard(card2);
		summonZone1.addCard(card1);
		summonZone2.addCard(card2);
		controller1.addAction("DeclareBattle", card1.getID(), "SummonZone", null);
		//Ensure that card 1 is faster than card 2
		((Summon)card1).getStatus().addStatusChange(new StatusChange(StatusChange.INITIATIVE, UUID.randomUUID(), StatusChange.TYPE_ADDITION, 2));
		controller1.addAction("SelectSummon", card2.getID(), "SummonZone", player2);
		controller1.addStackStart();
		 //Round1
		controller1.addExpectedPrompt("#5", ()->{
			game.getActiveBattle().proceed(player1);
		});
		controller1.addExpectedPrompt("#6", ()->{
			game.getActiveBattle().proceed(player1);
		});
		controller2.addExpectedPrompt("#5", ()->{
			game.getActiveBattle().proceed(player2);
		});
		controller2.addExpectedPrompt("#6", ()->{
			game.getActiveBattle().proceed(player2);
		});
		 //Round2
		controller1.addExpectedPrompt("#5", ()->{
			game.getActiveBattle().proceed(player1);
		});
		controller1.addExpectedPrompt("#6", ()->{
			game.getActiveBattle().proceed(player1);
		});
		controller2.addExpectedPrompt("#5", ()->{
			game.getActiveBattle().proceed(player2);
		});
		controller2.addExpectedPrompt("#6", ()->{
			game.getActiveBattle().proceed(player2);
		});
		 //Round3
		controller1.addExpectedPrompt("#5", ()->{
			game.getActiveBattle().proceed(player1);
		});
		controller1.addExpectedPrompt("#6", ()->{
			game.getActiveBattle().proceed(player1);
		});
		controller2.addExpectedPrompt("#5", ()->{
			game.getActiveBattle().proceed(player2);
		});
		controller2.addExpectedPrompt("#6", ()->{
			game.getActiveBattle().proceed(player2);
		});
		 //Round4
		controller1.addExpectedPrompt("#5", ()->{
			game.getActiveBattle().proceed(player1);
		});
		controller1.addPhaseEndAction();
		controller2.addExpectedPrompt("#5", ()->{
			game.getActiveBattle().proceed(player2);
		});
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
		if(((Summon)card2).getStatus().getVitality() != 0) {
			fail("Card2 was not really defeated");
		}
		if(((Summon)card1).getStatus().getVitality() != 2) {
			fail("Card1 didn't got the expected damage");
		}
		if(!((Summon)card1).getActivityStatus().getStatus().equals(ActivityStatus.USED)) {
			fail("Card1's status was not set to used");
		}
		if(((Summon)card1).getSummonHierarchy().getExperience() != 3) {
			fail("Card1 got not 3 points of experience!");
		}
		if(player2.getGameZone("DiscardPile").findCard(card2.getID()) == null) {
			fail("Card2 was not added to discard pile.");
		}
		if(player2.getGameZone("SummonZone").findCard(card2.getID()) != null) {
			fail("Card2 was not removed from Summon zone.");
		}
		if(player1.getGameZone("DiscardPile").findCard(card1.getID()) != null) {
			fail("Card1 was added to discard pile.");
		}
		if(player1.getGameZone("SummonZone").findCard(card1.getID()) == null) {
			fail("Card1 was removed from Summon zone.");
		}
		
	}
	
	@Test
	public void testWithdrawFromBattle() {
		/**
		 * Preparation 
		 */
		System.out.println("-=Test WithdrawFromBattle=-");
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
		IsAreaInGame summonZone1 = player1.getGameZone("SummonZone");
		IsAreaInGame deck1 = player1.getGameZone("DeckZone");
		Summon[] summons = cardProvider.getFirstSummonFromZone(deck1, 1);
		Card card1 = summons[0];
		IsAreaInGame summonZone2 = player2.getGameZone("SummonZone");
		IsAreaInGame deck2 = player2.getGameZone("DeckZone");
		summons = cardProvider.getFirstSummonFromZone(deck2, 1);
		Card card2 = summons[0];
		deck1.removeCard(card1);
		deck2.removeCard(card2);
		summonZone1.addCard(card1);
		summonZone2.addCard(card2);
		controller1.addAction("DeclareBattle", card1.getID(), "SummonZone", null);
		//Ensure that card 1 is faster than card 2
		((Summon)card1).getStatus().addStatusChange(new StatusChange(StatusChange.INITIATIVE, UUID.randomUUID(), StatusChange.TYPE_ADDITION, 2));
		controller1.addAction("SelectSummon", card2.getID(), "SummonZone", player2);
		controller1.addStackStart();
		 //Round1
		controller1.addExpectedPrompt("#5", ()->{
			game.getActiveBattle().proceed(player1);
		});
		controller1.addExpectedPrompt("#6", ()->{
			game.getActiveBattle().proceed(player1);
		});
		controller2.addExpectedPrompt("#5", ()->{
			game.getActiveBattle().proceed(player2);
		});
		controller2.addExpectedPrompt("#6", ()->{
			game.getActiveBattle().proceed(player2);
		});
		 //Round2
		controller1.addExpectedPrompt("#5", ()->{
			game.getActiveBattle().proceed(player1);
		});
		controller1.addExpectedPrompt("#6", ()->{
			game.getActiveBattle().proceed(player1);
		});
		controller2.addExpectedPrompt("#5", ()->{
			game.getActiveBattle().proceed(player2);
		});
		controller2.addExpectedPrompt("#6", ()->{
			card2.activateGameAction("WithdrawFromBattle", player2);
			game.getActiveBattle().proceed(player2);
		});
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
		if(((Summon)card2).getActivityStatus().getStatus().equals(ActivityStatus.IMMOBILIZED)) {
			fail("Since Card2 was not the attacking summon it shouldn't be immobilized");
		}
		if(((Summon)card1).getSummonHierarchy().getExperience() != 1) {
			fail("Card1 got not 1 point of experience!");
		}
		if(((Summon)card2).getSummonHierarchy().getExperience() == 1) {
			fail("Card2 got 1 point of experience!");
		}
		if(player2.getGameZone("DiscardPile").findCard(card2.getID()) != null) {
			fail("Card2 was added to discard pile.");
		}
		if(player2.getGameZone("SummonZone").findCard(card2.getID()) == null) {
			fail("Card2 was removed from Summon Zone.");
		}
		if(player1.getGameZone("DiscardPile").findCard(card1.getID()) != null) {
			fail("Card1 was added to discard pile.");
		}
		if(player1.getGameZone("SummonZone").findCard(card1.getID()) == null) {
			fail("Card1 was removed from Summon zone.");
		}
		
	}
	
	@Test
	public void testWithdrawFromBattle_Attacker() {
		/**
		 * Preparation 
		 */
		System.out.println("-=Test WithdrawFromBattle (Attacker)=-");
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
		IsAreaInGame summonZone1 = player1.getGameZone("SummonZone");
		IsAreaInGame deck1 = player1.getGameZone("DeckZone");
		Summon[] summons = cardProvider.getFirstSummonFromZone(deck1, 1);
		Card card1 = summons[0];
		IsAreaInGame summonZone2 = player2.getGameZone("SummonZone");
		IsAreaInGame deck2 = player2.getGameZone("DeckZone");
		summons = cardProvider.getFirstSummonFromZone(deck2, 1);
		Card card2 = summons[0];
		deck1.removeCard(card1);
		deck2.removeCard(card2);
		summonZone1.addCard(card1);
		summonZone2.addCard(card2);
		controller1.addAction("DeclareBattle", card1.getID(), "SummonZone", null);
		//Ensure that card 1 is faster than card 2
		((Summon)card1).getStatus().addStatusChange(new StatusChange(StatusChange.INITIATIVE, UUID.randomUUID(), StatusChange.TYPE_ADDITION, 2));
		controller1.addAction("SelectSummon", card2.getID(), "SummonZone", player2);
		controller1.addStackStart();
		 //Round1
		controller1.addExpectedPrompt("#5", ()->{
			game.getActiveBattle().proceed(player1);
		});
		controller1.addExpectedPrompt("#6", ()->{
			game.getActiveBattle().proceed(player1);
		});
		controller2.addExpectedPrompt("#5", ()->{
			game.getActiveBattle().proceed(player2);
		});
		controller2.addExpectedPrompt("#6", ()->{
			game.getActiveBattle().proceed(player2);
		});
		 //Round2
		controller1.addExpectedPrompt("#5", ()->{
			game.getActiveBattle().proceed(player1);
		});
		controller1.addExpectedPrompt("#6", ()->{
			card1.activateGameAction("WithdrawFromBattle", player1);
			game.getActiveBattle().proceed(player1);
		});
		controller2.addExpectedPrompt("#5", ()->{
			game.getActiveBattle().proceed(player2);
		});
		controller2.addExpectedPrompt("#6", ()->{
			game.getActiveBattle().proceed(player2);
		});
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
		if(!((Summon)card1).getActivityStatus().getStatus().equals(ActivityStatus.IMMOBILIZED)) {
			fail("Since Card1 was the attacking summon it should be immobilized.");
		}
		if(((Summon)card2).getSummonHierarchy().getExperience() != 1) {
			fail("Card2 got not 1 point of experience!");
		}
		if(((Summon)card1).getSummonHierarchy().getExperience() == 1) {
			fail("Card1 got 1 point of experience!");
		}
		if(player2.getGameZone("DiscardPile").findCard(card2.getID()) != null) {
			fail("Card2 was added to discard pile.");
		}
		if(player2.getGameZone("SummonZone").findCard(card2.getID()) == null) {
			fail("Card2 was removed from Summon Zone.");
		}
		if(player1.getGameZone("DiscardPile").findCard(card1.getID()) != null) {
			fail("Card1 was added to discard pile.");
		}
		if(player1.getGameZone("SummonZone").findCard(card1.getID()) == null) {
			fail("Card1 was removed from Summon zone.");
		}
	}
	
	@Test
	public void testAttackPlayer() {
		/**
		 * Preparation 
		 */
		System.out.println("-=Test AttackPlayer=-");
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
		IsAreaInGame summonZone1 = player1.getGameZone("SummonZone");
		IsAreaInGame deck1 = player1.getGameZone("DeckZone");
		Summon[] summons = cardProvider.getFirstSummonFromZone(deck1, 1);
		Card card1 = summons[0];
		IsAreaInGame summonZone2 = player2.getGameZone("SummonZone");
		IsAreaInGame deck2 = player2.getGameZone("DeckZone");
		summons = cardProvider.getFirstSummonFromZone(deck2, 1);
		Card card2 = summons[0];
		deck1.removeCard(card1);
		deck2.removeCard(card2);
		summonZone1.addCard(card1);
		summonZone2.addCard(card2);
		controller2.addAction("AttackPlayer", card2.getID(), "SummonZone", null);
		controller2.addStackStart();
		Thread gameThread = new Thread(game, "Game");
		Thread ctrlThread1 = new Thread(controller2, "Control2");
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
		if(player1.getHealthPoints() == 1) {
			fail("Player1s HP were not decreased by "+((Summon)card2).getStatus().getAttack());
		}
	}
	
	@Test
	public void testBlockPlayerAttack() {
		/**
		 * Preparation 
		 */
		System.out.println("-=Test BlockPlayerAttack=-");
		ReentrantLock lockGame = new ReentrantLock();
		ReentrantLock lockTest = new ReentrantLock();
		Condition gameCond = lockGame.newCondition();
		Condition testCond = lockTest.newCondition();
		String[] phases = {"Main"};
		TestGame game = new TestGame(player1, player2, getPhases(phases), gameCond, lockGame);
		player1.decreaseHealthPoints(3); //Ensure game ends after first round
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
		IsAreaInGame summonZone1 = player1.getGameZone("SummonZone");
		IsAreaInGame deck1 = player1.getGameZone("DeckZone");
		Summon[] summons = cardProvider.getFirstSummonFromZone(deck1, 1);
		Card card1 = summons[0];
		IsAreaInGame summonZone2 = player2.getGameZone("SummonZone");
		IsAreaInGame deck2 = player2.getGameZone("DeckZone");
		summons = cardProvider.getFirstSummonFromZone(deck2, 1);
		Card card2 = summons[0];
		deck1.removeCard(card1);
		deck2.removeCard(card2);
		summonZone1.addCard(card1);
		summonZone2.addCard(card2);
		GameListener.getInstance().addGameActionListener(new GameActionListener() {
			
			@Override
			public void actionExecuted(GameAction action) {
			}
			
			@Override
			public void actionActivated(GameAction action) {
				if(action.getCode().equals("AttackPlayer")) {
					if(action.getActivator() == player1) {
						MessageInLanguage message = new MessageInLanguage();
						message.id = "#0";
						message.text = "Test case prompts player to activate an action.";
						message.language = "EN";
						controller2.prompt(player2, message);
					}
				}
			}
		});
		//Ensure that card 1 is faster than card 2
		((Summon)card1).getStatus().addStatusChange(new StatusChange(StatusChange.INITIATIVE, UUID.randomUUID(), StatusChange.TYPE_ADDITION, 2));
		controller1.addAction("AttackPlayer", card1.getID(), "SummonZone", null);
		controller1.addStackStart();
		controller2.addExpectedPrompt("#0", ()->{
			((Summon)card2).activateGameAction("BlockPlayerAttack", player2);
			game.processGameStack(player2);
			ReentrantLock lock = game.getActivePhase().getActiveGameStack().getLock();
			Condition cond = game.getActivePhase().getActiveGameStack().getCondition();
			try {
				lock.lock();
				cond.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		});
		 //Round1
		controller1.addExpectedPrompt("#5", ()->{
			game.getActiveBattle().proceed(player1);
		});
		controller1.addExpectedPrompt("#6", ()->{
			game.getActiveBattle().proceed(player1);
		});
		controller2.addExpectedPrompt("#5", ()->{
			game.getActiveBattle().proceed(player2);
		});
		controller2.addExpectedPrompt("#6", ()->{
			game.getActiveBattle().proceed(player2);
		});
		 //Round2
		controller1.addExpectedPrompt("#5", ()->{
			game.getActiveBattle().proceed(player1);
		});
		controller1.addExpectedPrompt("#6", ()->{
			game.getActiveBattle().proceed(player1);
		});
		controller2.addExpectedPrompt("#5", ()->{
			game.getActiveBattle().proceed(player2);
		});
		controller2.addExpectedPrompt("#6", ()->{
			game.getActiveBattle().proceed(player2);
		});
		 //Round3
		controller1.addExpectedPrompt("#5", ()->{
			game.getActiveBattle().proceed(player1);
		});
		controller1.addExpectedPrompt("#6", ()->{
			game.getActiveBattle().proceed(player1);
		});
		controller2.addExpectedPrompt("#5", ()->{
			game.getActiveBattle().proceed(player2);
		});
		controller2.addExpectedPrompt("#6", ()->{
			game.getActiveBattle().proceed(player2);
		});
		 //Round4
		controller1.addExpectedPrompt("#5", ()->{
			game.getActiveBattle().proceed(player1);
		});
		controller1.addPhaseEndAction();
		controller2.addExpectedPrompt("#5", ()->{
			game.getActiveBattle().proceed(player2);
		});
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
		if(player2.getHealthPoints() != 3) {
			fail("Player1s HP were decreased");
		}
		if(((Summon)card2).getStatus().getVitality() != 0) {
			fail("Card2 was not really defeated");
		}
		if(((Summon)card1).getStatus().getVitality() != 2) {
			fail("Card1 didn't got the expected damage");
		}
		if(!((Summon)card1).getActivityStatus().getStatus().equals(ActivityStatus.USED)) {
			fail("Card1's status was not set to used");
		}
		if(((Summon)card1).getSummonHierarchy().getExperience() != 3) {
			fail("Card1 got not 3 points of experience!");
		}
		if(player2.getGameZone("DiscardPile").findCard(card2.getID()) == null) {
			fail("Card2 was not added to discard pile.");
		}
		if(player2.getGameZone("SummonZone").findCard(card2.getID()) != null) {
			fail("Card2 was not removed from Summon zone.");
		}
		if(player1.getGameZone("DiscardPile").findCard(card1.getID()) != null) {
			fail("Card1 was added to discard pile.");
		}
		if(player1.getGameZone("SummonZone").findCard(card1.getID()) == null) {
			fail("Card1 was removed from Summon zone.");
		}
	}
}
