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

import project.main.Card.ActivityStatus;
import project.main.Card.Card;
import project.main.Card.CollectsMagicEnergy;
import project.main.Card.Spell;
import project.main.Card.Summon;
import project.main.GameApplication.Application;
import project.main.GameApplication.CollectorZone;
import project.main.GameApplication.DeckZone;
import project.main.GameApplication.Game;
import project.main.GameApplication.GamePhase;
import project.main.GameApplication.HandZone;
import project.main.GameApplication.HostsGame;
import project.main.GameApplication.IsPhaseInGame;
import project.main.GameApplication.Player;
import project.main.GameApplication.SummonZone;
import project.main.build_cards.CardFactory;
import project.main.jsonObjects.ActionDefinitionLibrary;
import project.main.jsonObjects.CardDefinitionLibrary;
import project.main.jsonObjects.MessageInLanguage;
import project.main.util.GameMessageProvider;
import project.test.mok.PhysicalTestPlayer;
import project.test.mok.TestGame;
import project.test.mok.TestPlayer;
import project.test.util.ZoneCardProvider;

public class MagicEnergyStockTest {
	
	private static String[] cardIDs = {"bsc-su-00-0","bsc-su-00-0","bsc-su-00-0","bsc-sp-00","bsc-sp-00","bsc-sp-00"};

	ArrayList<Card> deck;
	CollectsMagicEnergy cut;
	HostsGame app;
	Player player;
	Player player2;
	Game game;
	DeckZone deckZone;
	HandZone hand;
	CollectorZone collectorZone;
	SummonZone summonZone;
	PhysicalTestPlayer controller;
	ZoneCardProvider cardProvider;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Field actionLibrary = ActionDefinitionLibrary.class.getDeclaredField("instance");
		actionLibrary.setAccessible(true);
		actionLibrary.set(null, null);
		Field messageProvider = GameMessageProvider.class.getDeclaredField("instance");
		messageProvider.setAccessible(true);
		messageProvider.set(null, null);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
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

	@Before
	public void setUp() throws Exception {
		deck = new ArrayList<Card>();
		ArrayList<IsPhaseInGame> phases = new ArrayList<IsPhaseInGame>();
		phases.add(new GamePhase("Main"));
		player = new TestPlayer(3, 3, deck);
		player2 = new TestPlayer(3, 3, deck);
		ReentrantLock gl = new ReentrantLock();
		Condition gc = gl.newCondition();
		game = new TestGame(player, player2, phases, gc, gl);
		Field activePhaseField = game.getClass().getDeclaredField("activPhase");
		activePhaseField.setAccessible(true);
		activePhaseField.set(game, phases.get(0));
		Field activePlayerField = game.getClass().getDeclaredField("activPlayer");
		activePlayerField.setAccessible(true);
		activePlayerField.set(game, player);
		for(String id : cardIDs) {
			Card card = CardFactory.getInstance().createCard(id, game);
			deck.add(card);
		}
		app = Application.getInstance();
		app.setLanguage("EN");
		deckZone = ((DeckZone)player.getGameZone("DeckZone"));
		deckZone.setGame(game);
		hand = (HandZone)player.getGameZone("HandZone");
		hand.setGame(game);
		collectorZone = (CollectorZone)player.getGameZone("CollectorZone");
		collectorZone.setGame(game);
		summonZone = (SummonZone)player.getGameZone("SummonZone");
		summonZone.setGame(game);
		controller = new PhysicalTestPlayer(player, game, null, null, null, null);
		((TestPlayer)player).setController(controller);
		cardProvider = new ZoneCardProvider();
		cut = player.getMagicEnergyStock();
	}

	@Test
	public void testEvokeSummon() {
		//Prepare
		Summon summonToEvoke = cardProvider.getFirstSummonFromZone(deckZone, 1)[0];
		cardProvider.moveCardsFromZoneToOtherZone(deckZone, hand);
		for(Card card : hand.getCards()) {
			if(card != summonToEvoke) {
				collectorZone.addCard(card);
			}
		}
		for(Card card : collectorZone.getCards()) {
			hand.removeCard(card);
		}
		//Execute
		int freeEnergyBeforeTest = cut.getFreeEnergy();
		int blockedEnergyBeforeTest = cut.getBlockedEnergy();
		int usedEnergyBeforeTest = cut.getUsedEnergy();
		int depletedEnergyBeforeTest = cut.getDepletedEnergy();
		hand.removeCard(summonToEvoke);
		summonZone.addCard(summonToEvoke);
		int remainingEnergy = cut.blockEnergy(summonToEvoke.getStatus().getMagicPreservationValue());
		int freeEnergyAfterTest = cut.getFreeEnergy();
		int blockedEnergyAfterTest = cut.getBlockedEnergy();
		int usedEnergyAfterTest = cut.getUsedEnergy();
		int depletedEnergyAfterTest = cut.getDepletedEnergy();
		//Test Result
		if(usedEnergyAfterTest != usedEnergyBeforeTest) {
			fail("Used energy must not be affected by the test!");
		}
		if(depletedEnergyAfterTest != depletedEnergyBeforeTest) {
			fail("Depleted energy must not be affected by the test!");
		}
		if(remainingEnergy > 0) {
			fail("Remaining energy is higher than 0.");
		}
		if(freeEnergyBeforeTest - summonToEvoke.getStatus().getMagicPreservationValue() != freeEnergyAfterTest) {
			fail("Free energy was not correctly drecreased");
		}
		if(blockedEnergyBeforeTest > 0) {
			fail("Blocked energy before test is not 0.");
		}
		if(blockedEnergyBeforeTest + summonToEvoke.getStatus().getMagicPreservationValue() != blockedEnergyAfterTest) {
			fail("Blocked energy was not correctly increased.");
		}
	}

	@Test
	public void testCastSpell() {
		//Prepare
		Spell spellToCast = cardProvider.getFirstSpellFromZone(deckZone, 1)[0];
		cardProvider.moveCardsFromZoneToOtherZone(deckZone, hand);
		for(Card card : hand.getCards()) {
			if(card != spellToCast) {
				collectorZone.addCard(card);
			}
		}
		for(Card card : collectorZone.getCards()) {
			hand.removeCard(card);
		}
		//Execute
		int freeEnergyBeforeTest = cut.getFreeEnergy();
		int blockedEnergyBeforeTest = cut.getBlockedEnergy();
		int usedEnergyBeforeTest = cut.getUsedEnergy();
		int depletedEnergyBeforeTest = cut.getDepletedEnergy();
		hand.removeCard(spellToCast);
		int remainingEnergy = cut.useEnergy(spellToCast.getNeededMagicEnergy());
		int freeEnergyAfterTest = cut.getFreeEnergy();
		int blockedEnergyAfterTest = cut.getBlockedEnergy();
		int usedEnergyAfterTest = cut.getUsedEnergy();
		int depletedEnergyAfterTest = cut.getDepletedEnergy();
		//Test Result
		if(usedEnergyAfterTest != usedEnergyBeforeTest + spellToCast.getNeededMagicEnergy()) {
			fail("Used energy must not be affected by the test!");
		}
		if(depletedEnergyAfterTest != depletedEnergyBeforeTest) {
			fail("Depleted energy must not be affected by the test!");
		}
		if(remainingEnergy > 0) {
			fail("Remaining energy is higher than 0.");
		}
		if(freeEnergyBeforeTest - spellToCast.getNeededMagicEnergy() != freeEnergyAfterTest) {
			fail("Free energy was not correctly drecreased");
		}
		if(blockedEnergyBeforeTest != blockedEnergyAfterTest) {
			fail("Blocked energy must not be affected by this test.");
		}
	}

	@Test
	public void testDeplete() {
		//Prepare
		Summon summonToEvoke = cardProvider.getFirstSummonFromZone(deckZone, 1)[0];
		cardProvider.moveCardsFromZoneToOtherZone(deckZone, hand);
		for(Card card : hand.getCards()) {
			if(card != summonToEvoke) {
				collectorZone.addCard(card);
			}
		}
		for(Card card : collectorZone.getCards()) {
			hand.removeCard(card);
		}
		hand.removeCard(summonToEvoke);
		summonZone.addCard(summonToEvoke);
		cut.blockEnergy(summonToEvoke.getStatus().getMagicPreservationValue());
		//Execute
		int freeEnergyBeforeTest = cut.getFreeEnergy();
		int usedEnergyBeforeTest = cut.getUsedEnergy();
		int depletedEnergyBeforeTest = cut.getDepletedEnergy();
		int remainingEnergy = cut.depleteEnergy(freeEnergyBeforeTest-2);
		int freeEnergyAfterTest = cut.getFreeEnergy();
		int blockedEnergyAfterTest = cut.getBlockedEnergy();
		int usedEnergyAfterTest = cut.getUsedEnergy();
		int depletedEnergyAfterTest = cut.getDepletedEnergy();
		//Test Result
		if(usedEnergyAfterTest != usedEnergyBeforeTest) {
			fail("Used energy must not be affected by the test!");
		}
		if(depletedEnergyAfterTest != depletedEnergyBeforeTest + freeEnergyBeforeTest - 2) {
			fail("Depleted energy was not correctly increaesed");
		}
		if(remainingEnergy > 0) {
			fail("Remaining energy is higher than 0.");
		}
		if(freeEnergyBeforeTest - freeEnergyBeforeTest + 2 != freeEnergyAfterTest) {
			fail("Free energy was not correctly drecreased");
		}
		if(summonToEvoke.getStatus().getMagicPreservationValue() != blockedEnergyAfterTest) {
			fail("Blocked energy is not correctly set.");
		}
	}

	@Test
	public void testDepleteWithImmobilize() {
		//Prepare
		ReentrantLock lockTest = new ReentrantLock();
		Condition cond = lockTest.newCondition();
		Summon summonToEvoke = cardProvider.getFirstSummonFromZone(deckZone, 1)[0];
		cardProvider.moveCardsFromZoneToOtherZone(deckZone, hand);
		for(Card card : hand.getCards()) {
			if(card != summonToEvoke) {
				collectorZone.addCard(card);
			}
		}
		for(Card card : collectorZone.getCards()) {
			hand.removeCard(card);
		}
		hand.removeCard(summonToEvoke);
		summonZone.addCard(summonToEvoke);
		cut.blockEnergy(summonToEvoke.getStatus().getMagicPreservationValue());
		//Execute
		int freeEnergyBeforeTest = cut.getFreeEnergy();
		int blockedEnergyBeforeTest = cut.getBlockedEnergy();
		int usedEnergyBeforeTest = cut.getUsedEnergy();
		
		controller.addExpectedPrompt("#7", ()->{
			ReentrantLock stackLock = null;
			try {
				lockTest.lock();
				System.out.println("Execute prompt answer (immobilize one summon)");
				summonToEvoke.activateGameAction("SelectSummon", player);
				game.processGameStack(player2);
				game.processGameStack(player);
				stackLock = game.getActivePhase().getActiveGameStack().getLock();
				Condition stackCondition = game.getActivePhase().getActiveGameStack().getCondition();
				stackLock = game.getActivePhase().getActiveGameStack().getLock();
				stackLock.lock();
				stackCondition.await();
				cond.signal();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}finally {
				stackLock.unlock();
				lockTest.unlock();
			}
			
		});
		int remainingEnergy = cut.depleteEnergy(freeEnergyBeforeTest+blockedEnergyBeforeTest-1);
		try {
			lockTest.lock();
			cond.await();
		} catch (InterruptedException e) {
			fail(e.getMessage());
		}finally {
			lockTest.unlock();
		}
		int freeEnergyAfterTest = cut.getFreeEnergy();
		int blockedEnergyAfterTest = cut.getBlockedEnergy();
		int usedEnergyAfterTest = cut.getUsedEnergy();
		int depletedEnergyAfterTest = cut.getDepletedEnergy();
		//Test Result
		if(usedEnergyAfterTest != usedEnergyBeforeTest) {
			fail("Used energy must not be affected by the test!");
		}
		if(depletedEnergyAfterTest != freeEnergyBeforeTest+blockedEnergyBeforeTest-1) {
			fail("Depleted energy was not correctly increaesed");
		}
		if(remainingEnergy > 0) {
			fail("Remaining energy is higher than 0.");
		}
		if(1 != freeEnergyAfterTest) {
			fail("Free energy was not correctly drecreased");
		}
		if(!summonToEvoke.getActivityStatus().getStatus().equals(ActivityStatus.IMMOBILIZED)) {
			fail("Summon is not in status immobilized.");
		}
		if(blockedEnergyAfterTest != 0) {
			fail("Since all summons are immobilized no energy should be blocked.");
		}
	}
	
	@Test
	public void testRemoveMagicCollector() {
		//Prepare
		ReentrantLock lockTest = new ReentrantLock();
		Condition cond = lockTest.newCondition();
		Summon summonToEvoke = cardProvider.getFirstSummonFromZone(deckZone, 1)[0];
		cardProvider.moveCardsFromZoneToOtherZone(deckZone, hand);
		Card collector = null;
		boolean oneAdded = false;
		for(Card card : hand.getCards()) {
			if(card != summonToEvoke && !oneAdded) {
				oneAdded = true;
				collectorZone.addCard(card);
				collector = card;
			}
		}
		for(Card card : collectorZone.getCards()) {
			hand.removeCard(card);
		}
		hand.removeCard(summonToEvoke);
		summonZone.addCard(summonToEvoke);
		cut.blockEnergy(summonToEvoke.getStatus().getMagicPreservationValue());
		
		controller.addExpectedPrompt("#7", ()->{
			ReentrantLock stackLock = null;
			try {
				lockTest.lock();
				System.out.println("Execute prompt answer (immobilize one summon)");
				summonToEvoke.activateGameAction("SelectSummon", player);
				game.processGameStack(player2);
				game.processGameStack(player);
				Condition stackCondition = game.getActivePhase().getActiveGameStack().getCondition();
				stackLock = game.getActivePhase().getActiveGameStack().getLock();
				stackLock.lock();
				stackCondition.await();
				cond.signal();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}finally {
				stackLock.unlock();
				lockTest.unlock();
			}
			
		});
		//Execute
		collectorZone.removeCard(collector);
		try {
			lockTest.lock();
			cond.await();
		} catch (InterruptedException e) {
			fail(e.getMessage());
		}finally {
			lockTest.unlock();
		}
		int freeEnergyAfterTest = cut.getFreeEnergy();
		int blockedEnergyAfterTest = cut.getBlockedEnergy();
		int usedEnergyAfterTest = cut.getUsedEnergy();
		int depletedEnergyAfterTest = cut.getDepletedEnergy();
		//Test Result
		if(usedEnergyAfterTest != 0) {
			fail("All energy must be 0 if no collector is available");
		}
		if(depletedEnergyAfterTest != 0) {
			fail("All energy must be 0 if no collector is available");
		}
		if(0 != freeEnergyAfterTest) {
			fail("Free energy must be 0 if no collector is available");
		}
		if(!summonToEvoke.getActivityStatus().getStatus().equals(ActivityStatus.IMMOBILIZED)) {
			fail("Summon is not in status immobilized.");
		}
		if(blockedEnergyAfterTest != 0) {
			fail("Since all summons are immobilized no energy should be blocked.");
		}
	}
	
	@Test
	public void testAddMagicCollector() {
		//Prepare
		ReentrantLock lockTest = new ReentrantLock();
		Condition cond = lockTest.newCondition();
		Summon summonToEvoke = cardProvider.getFirstSummonFromZone(deckZone, 1)[0];
		cardProvider.moveCardsFromZoneToOtherZone(deckZone, hand);
		Card collector = null;
		boolean oneAdded = false;
		for(Card card : hand.getCards()) {
			if(card != summonToEvoke && !oneAdded) {
				oneAdded = true;
				collectorZone.addCard(card);
				collector = card;
			}
		}
		for(Card card : collectorZone.getCards()) {
			hand.removeCard(card);
		}
		hand.removeCard(summonToEvoke);
		summonZone.addCard(summonToEvoke);
		cut.blockEnergy(summonToEvoke.getStatus().getMagicPreservationValue());
		controller.addExpectedPrompt("#7", ()->{
			ReentrantLock stackLock = null;
			try {
				lockTest.lock();
				System.out.println("Execute prompt answer (immobilize one summon)");
				summonToEvoke.activateGameAction("SelectSummon", player);
				game.processGameStack(player2);
				game.processGameStack(player);
				Condition stackCondition = game.getActivePhase().getActiveGameStack().getCondition();
				stackLock = game.getActivePhase().getActiveGameStack().getLock();
				stackLock.lock();
				stackCondition.await();
				cond.signal();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}finally {
				stackLock.unlock();
				lockTest.unlock();
			}
		});
		controller.addExpectedPrompt("#0", ()->{
			ReentrantLock stackLock = null;
			try {
				lockTest.lock();
				System.out.println("Execute action UnimmobilizeSummon");
				summonToEvoke.activateGameAction("UnimmobilizeSummon", player);
				game.processGameStack(player2);
				game.processGameStack(player);
				Condition stackCondition = game.getActivePhase().getActiveGameStack().getCondition();
				stackLock = game.getActivePhase().getActiveGameStack().getLock();
				stackLock.lock();
				stackCondition.await();
				cond.signal();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}finally {
				stackLock.unlock();
				lockTest.unlock();
			}
		});
		MessageInLanguage message = new MessageInLanguage();
		message.id = "#0";
		message.text = "Test case prompts player to activate an action.";
		message.language = "EN";
		collectorZone.removeCard(collector);
		try {
			lockTest.lock();
			cond.await();
		} catch (InterruptedException e) {
			fail(e.getMessage());
		}finally {
			lockTest.unlock();
		}
		//Execute
		collectorZone.addCard(collector);
		controller.prompt(player, message);
		try {
			lockTest.lock();
			cond.await();
		} catch (InterruptedException e) {
			fail(e.getMessage());
		}finally {
			lockTest.unlock();
		}
		int freeEnergyAfterTest = cut.getFreeEnergy();
		int blockedEnergyAfterTest = cut.getBlockedEnergy();
		int usedEnergyAfterTest = cut.getUsedEnergy();
		int depletedEnergyAfterTest = cut.getDepletedEnergy();
		//Test Result
		if(usedEnergyAfterTest != 0) {
			fail("Used energy must be 0 because nothing was used");
		}
		if(depletedEnergyAfterTest != 0) {
			fail("Depleted energy must be 0 because nothing was depleted");
		}
		if(3 != freeEnergyAfterTest) {
			fail("Free energy must be 3 because one summon blocks energy (2) and the collector gives 5.");
		}
		if(summonToEvoke.getActivityStatus().getStatus().equals(ActivityStatus.IMMOBILIZED)) {
			fail("Summon is still in status immobilized.");
		}
		if(blockedEnergyAfterTest != 2) {
			fail("The reactivated summon should block some energy.");
		}
		
	}	

}
