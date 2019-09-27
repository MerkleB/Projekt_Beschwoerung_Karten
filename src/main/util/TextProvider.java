package main.util;

import java.util.Hashtable;

import main.jsonObjects.CardDefinitionLibrary;

public class TextProvider implements ManagesTextLanguages {
	
	private String resourcePath;
	private LanguageTrivia[] card_trivias;
	private LanguagName[] card_names;
	private Hashtable<String, Hashtable<String, String>> triviasByLanguageAndCardID;
	private Hashtable<String, Hashtable<String, String>> namesByLanguageAndCardID;
	
	public static ManagesTextLanguages getInstance() {
		return new TextProvider();
	}
	
	private TextProvider() {
		resourcePath = "/main/json/card_lists/";
	}
	
	@Override
	public String getCardName(String cardID, String language) {
		return null;
	}

	@Override
	public String getCardTrivia(String cardID, String language) {
		return null;
	}

}
