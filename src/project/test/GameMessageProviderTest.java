package project.test;

import static org.junit.Assert.*;
import java.lang.reflect.Field;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import project.main.util.GameMessageProvider;
import project.main.util.ProvidesMessageTexts;

public class GameMessageProviderTest {
	
	private ProvidesMessageTexts cut;
	private String originalPath;
	
	@Before
	public void setUp() throws Exception {
		Field pathField = GameMessageProvider.class.getDeclaredField("resourcePath");
		pathField.setAccessible(true);
		originalPath = (String)pathField.get(null);
		pathField.set(null, "project/test/testJSON/messages.json");
		cut = GameMessageProvider.getInstance();
	}
	
	@After
	public void teardown() throws Exception {
		Field pathField = GameMessageProvider.class.getDeclaredField("resourcePath");
		pathField.setAccessible(true);
		pathField.set(null, originalPath);
		cut = GameMessageProvider.getInstance();
	}

	@Test
	public void testGetMessage() {
		if(!"You have no cards to draw.\r\nYou either can loose 1 HP or pay 5 HP to draw a card from Discard pile.\r\nWhat do you want to do?".equals(cut.getMessage("#1", "EN").text)) {
			fail("Expected text was not retrieved for language EN!");
		}
		if(!"Du hast keine Karten mehr im Deck.\r\nZahle entweder 5 Lebenspunkte um eine Karte vom Ablagestapel zu ziehen oder erleide einen Schaden.\r\nWas wirst du tun?".equals(cut.getMessage("#1", "DE").text)) {
			fail("Expected text was not retrieved for language DE!");
		}
	}
	
	@Test
	public void testGetMessageWithParameters() {
		String[] parameters = {"Summon", "MagicCollector", "heal"};
		if(!"Please select the Summon or MagicCollector to heal.".equals(cut.getMessage("#2", "EN", parameters).text)) {
			fail("Expected text was not retrieved for language EN!");
		}
		if(!"Wähle die Summon oder den MagicCollector welchen du heal möchtest.".equals(cut.getMessage("#2", "DE", parameters).text)) {
			fail("Expected text was not retrieved for language DE!");
		}
	}

}
