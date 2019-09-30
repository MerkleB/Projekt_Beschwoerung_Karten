package test;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import main.jsonObjects.CardDefinitionLibrary;
import main.jsonObjects.HoldsCardDefinitions;
import main.util.ManagesTextLanguages;
import main.util.TextProvider;
import test.mok.MokProvider;

public class TextProviderTest {
	
	private ManagesTextLanguages cut;
	private static boolean libraryAlreadyMokked;
	
	@Before
	public void setUp() throws Exception {
		if(!libraryAlreadyMokked) {
			libraryAlreadyMokked = true;
			HoldsCardDefinitions library = CardDefinitionLibrary.getInstance();
			Field library_instance = library.getClass().getDeclaredField("instance");
			library_instance.setAccessible(true);
			library_instance.set(null, MokProvider.getCardDefinitions());
		}
		cut = TextProvider.getInstance();
		setPathToTestResources();
	}

	@Test
	public void testGetCardName() {
		if(!"Aries".equals(cut.getCardName("bsc-su-00-0", "DE"))) {
			fail("Expected name for bsc-su-00-0 in language DE was not retrieved");
		}
		
		if(!"Aries".equals(cut.getCardName("bsc-su-00-0", "EN"))) {
			fail("Expected name for bsc-su-00-0 in language EN was not retrieved");
		}
	}

	@Test
	public void testGetCardTrivia() {
		if(!"Ein sagenhafter Widder der einst Phrixos und seine Schwester Helle vor ihrer Stiefmutter Ino rettete.".equals(cut.getCardTrivia("bsc-su-00-0", "DE"))) {
			fail("Expected Trivia for bsc-su-00-0 in language DE was not retrieved.");
		}
		if(!"A marvelous ram who save Phrixos and his sister Helle from their stepmother Ino.".equals(cut.getCardTrivia("bsc-su-00-0", "EN"))) {
			fail("Expected Trivia for bsc-su-00-0 in language EN was not retrieved.");
		}
	}
	
	@Test
	public void testGetActionName() {
		if(!"Promote Summon".equals(cut.getActionName("PromoteSummon", "EN"))) {
			fail("Expected Action name was not retrieved for language EN");
		}
		if(!"Aufsteigen".equals(cut.getActionName("PromoteSummon", "DE"))) {
			fail("Expected Action name was not retrieved for language DE");
		}
	}
	
	public void setPathToTestResources() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String testPath = "test/testJSON/";
		Field pathField;
		pathField = cut.getClass().getDeclaredField("resourcePathCards");
		pathField.setAccessible(true);
		pathField.set(cut, testPath);
		String testPathAction = "test/testJSON/actionName.json";
		Field pathFieldAction;
		pathFieldAction = cut.getClass().getDeclaredField("resourcePathActions");
		pathFieldAction.setAccessible(true);
		pathFieldAction.set(cut, testPathAction);
	}
	
	@AfterClass
	public static void teardown() throws Exception {
		Field library_instance = CardDefinitionLibrary.class.getDeclaredField("instance");
		library_instance.setAccessible(true);
		library_instance.set(null, null);
	}

}
