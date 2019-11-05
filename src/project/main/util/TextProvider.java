package project.main.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import project.main.jsonObjects.ActionLanguage;
import project.main.jsonObjects.ActionLanguageNames;
import project.main.jsonObjects.ActionName;
import project.main.jsonObjects.CardDefinitionLibrary;
import project.main.jsonObjects.CardName;
import project.main.jsonObjects.CardTexts;
import project.main.jsonObjects.CardTrivia;
import project.main.jsonObjects.EffectDefinition;
import project.main.jsonObjects.EffectDefinitionList;
import project.main.jsonObjects.EffectDescription;
import project.main.jsonObjects.LanguageName;
import project.main.jsonObjects.LanguageTrivia;
import project.main.jsonObjects.Term;
import project.main.jsonObjects.TermInLanguage;
import project.main.jsonObjects.TermList;

public class TextProvider implements ManagesTextLanguages {
	
	private String resourcePathCards;
	private String resourcePathActions;
	private String resourcePathEffects;
	private String resourcePathTerms;
	private Hashtable<String, Hashtable<String, String>> triviasByLanguageAndCardID;
	private Hashtable<String, Hashtable<String, String>> namesByLanguageAndCardID;
	private Hashtable<String, Hashtable<String, String>> actionsByLanguageAndCode;
	private Hashtable<String, Hashtable<String, EffectDescription>> effectsByClassAndLanguage;
	private Hashtable<String, Hashtable<String, TermInLanguage>> termsByIdAndLanguage;
	
	public static ManagesTextLanguages getInstance() {
		return new TextProvider();
	}
	
	private TextProvider() {
		resourcePathCards = "project/json/card_lists/";
		resourcePathActions = "project/json/game_settings/actionName.json";
		resourcePathEffects = "project/json/game_settings/effects.json";
		resourcePathTerms = "project/json/game_settings/terms.json";
		triviasByLanguageAndCardID = new Hashtable<String, Hashtable<String, String>>();
		namesByLanguageAndCardID = new Hashtable<String, Hashtable<String, String>>();
		actionsByLanguageAndCode = new Hashtable<String, Hashtable<String, String>>();
		effectsByClassAndLanguage = new Hashtable<String, Hashtable<String, EffectDescription>>();
		termsByIdAndLanguage = new Hashtable<String, Hashtable<String, TermInLanguage>>();
	}
	
	@Override
	public String getCardName(String cardID, String language) {
		Hashtable<String, String> namesInLanguage = namesByLanguageAndCardID.get(language);
		if(namesInLanguage == null) {
			loadCardTexts(cardID);
			namesInLanguage = namesByLanguageAndCardID.get(language);
		}
		if(!namesInLanguage.containsKey(cardID)) {
			loadCardTexts(cardID);
		}
		return namesInLanguage.get(cardID);
	}
	
	private void loadCardTexts(String card_id) {
		FileLoader loader = new FileLoader();
		String cardSet = CardDefinitionLibrary.getInstance().getCardSetName(card_id);
		InputStream jsonStream = loader.getFileAsStream(resourcePathCards+cardSet+"_texts.json");
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new InputStreamReader(jsonStream));
		CardTexts texts = gson.fromJson(reader, CardTexts.class);
		convertToHashtable(texts);
	}
	
	private void loadActionNames() {
		FileLoader loader = new FileLoader();
		InputStream jsonStream = loader.getFileAsStream(resourcePathActions);
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new InputStreamReader(jsonStream));
		ActionLanguageNames actions = gson.fromJson(reader, ActionLanguageNames.class);
		convertToHashtable(actions);
	}
	
	private void loadEffects() {
		FileLoader loader = new FileLoader();
		InputStream jsonStream = loader.getFileAsStream(resourcePathEffects);
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new InputStreamReader(jsonStream));
		EffectDefinitionList effects = gson.fromJson(reader, EffectDefinitionList.class);
		convertToHashtable(effects);
	}
	
	private void loadTerms() {
		FileLoader loader = new FileLoader();
		InputStream jsonStream = loader.getFileAsStream(resourcePathTerms);
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new InputStreamReader(jsonStream));
		TermList termList = gson.fromJson(reader, TermList.class);
		convertToHashtable(termList);
	}
	
	private void convertToHashtable(CardTexts texts) {
		for(LanguageTrivia languageTrivia : texts.card_trivias) {
			Hashtable<String, String> card_trivia = triviasByLanguageAndCardID.get(languageTrivia.language);
			if(card_trivia == null) {
				card_trivia = new Hashtable<String, String>();
				triviasByLanguageAndCardID.put(languageTrivia.language, card_trivia);
			}
			for(CardTrivia trivia : languageTrivia.trivias) {
				card_trivia.put(trivia.card_id, trivia.trivia);
			}
		}
		
		for(LanguageName languageName : texts.card_names) {
			Hashtable<String, String> card_name = namesByLanguageAndCardID.get(languageName.language);
			if(card_name == null) {
				card_name = new Hashtable<String, String>();
				namesByLanguageAndCardID.put(languageName.language, card_name);
			}
			for(CardName name : languageName.names) {
				card_name.put(name.card_id, name.name);
			}
		}
	}
	
	private void convertToHashtable(ActionLanguageNames actionLanguages) {
		for(ActionLanguage language : actionLanguages.languages) {
			Hashtable<String, String> action_language = actionsByLanguageAndCode.get(language.language);
			if(action_language == null) {
				action_language = new Hashtable<String, String>();
				actionsByLanguageAndCode.put(language.language, action_language);
			}
			for(ActionName name : language.actions) {
				action_language.put(name.code, name.name);
			}
		}
	}
	
	private void convertToHashtable(EffectDefinitionList effects) {
		for(EffectDefinition effect : effects.effects) {
			Hashtable<String, EffectDescription> effectDefinitions = effectsByClassAndLanguage.get(effect.effectClass);
			if(effectDefinitions == null) {
				effectDefinitions = new Hashtable<String, EffectDescription>();
				effectsByClassAndLanguage.put(effect.effectClass, effectDefinitions);
			}
			for(EffectDescription description : effect.descriptions) {
				effectDefinitions.put(description.language, description);
			}
		}
	}
	
	private void convertToHashtable(TermList termList) {
		for(Term term : termList.terms) {
			Hashtable<String, TermInLanguage> termLanguages = termsByIdAndLanguage.get(term.id);
			if(termLanguages == null) {
				termLanguages = new Hashtable<String, TermInLanguage>();
				termsByIdAndLanguage.put(term.id, termLanguages);
			}
			for(TermInLanguage text : term.texts) {
				termLanguages.put(text.language, text);
			}
		}
	}

	@Override
	public String getCardTrivia(String cardID, String language) {
		Hashtable<String, String> triviasInLanguage = triviasByLanguageAndCardID.get(language);
		if(triviasInLanguage == null) {
			loadCardTexts(cardID);
			triviasInLanguage = triviasByLanguageAndCardID.get(language);
		}
		if(!triviasInLanguage.containsKey(cardID)) {
			loadCardTexts(cardID);
		}
		return triviasInLanguage.get(cardID);
	}

	@Override
	public String getActionName(String actionCode, String language) {
		Hashtable<String, String> namesInLanguage = actionsByLanguageAndCode.get(language);
		if(namesInLanguage == null) {
			loadActionNames();
			namesInLanguage = actionsByLanguageAndCode.get(language);
		}
		return namesInLanguage.get(actionCode);
	}

	@Override
	public EffectDescription getEffectDescription(String effectClass, String language) {
		Hashtable<String, EffectDescription> definitions = effectsByClassAndLanguage.get(effectClass);
		if(definitions == null) {
			loadEffects();
			definitions = effectsByClassAndLanguage.get(effectClass);
		}
		return definitions.get(language);
	}

	@Override
	public TermInLanguage getTerm(String termId, String language) {
		Hashtable<String, TermInLanguage> termTexts = termsByIdAndLanguage.get(termId);
		if(termTexts == null) {
			loadTerms();
			termTexts = termsByIdAndLanguage.get(termId);
		}
		return termTexts.get(language);
	}

}
