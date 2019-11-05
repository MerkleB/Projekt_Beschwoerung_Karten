package project.test;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import project.main.jsonObjects.CardDefinitionLibrary;
import project.main.jsonObjects.HoldsCardDefinitions;
import project.main.util.ManagesTextLanguages;
import project.main.util.TextProvider;
import project.test.mok.MokProvider;

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
	
	@Test
	public void testGetTerm() {
		if(!"Summon".equals(cut.getTerm("Summon", "EN").text)) {
			fail("Expected name of term Summon is Summon in language EN.");
		}
		if(!"Beschwörung".equals(cut.getTerm("Summon", "DE").text)) {
			fail("Expected name of tern Summon is Beschwörung in language DE.");
		}
	}
	
	public void setPathToTestResources() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String testPath = "project/test/testJSON/";
		Field pathField;
		pathField = cut.getClass().getDeclaredField("resourcePathCards");
		pathField.setAccessible(true);
		pathField.set(cut, testPath);
		String testPathAction = "project/test/testJSON/actionName.json";
		Field pathFieldAction;
		pathFieldAction = cut.getClass().getDeclaredField("resourcePathActions");
		pathFieldAction.setAccessible(true);
		pathFieldAction.set(cut, testPathAction);
		String testPathTerm = "project/test/testJSON/terms.json";
		Field pathFieldTerm;
		pathFieldTerm = cut.getClass().getDeclaredField("resourcePathTerms");
		pathFieldTerm.setAccessible(true);
		pathFieldTerm.set(cut, testPathTerm);
	}
	
	@AfterClass
	public static void teardown() throws Exception {
		Field library_instance = CardDefinitionLibrary.class.getDeclaredField("instance");
		library_instance.setAccessible(true);
		library_instance.set(null, null);
	}

}
