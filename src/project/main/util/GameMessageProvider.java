package project.main.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import project.main.jsonObjects.MessageInLanguage;
import project.main.jsonObjects.MessageTexts;

public class GameMessageProvider implements ProvidesMessageTexts {

	private static ProvidesMessageTexts instance;
	private static String  resourcePath = "project/json/game_settings/messages.json";
	private MessageTexts[] messages;
	private Hashtable<String, Hashtable<String, MessageInLanguage>> messagesByIdAndLanguage;
	
	public static ProvidesMessageTexts getInstance() {
		if(instance == null) {
			instance = new GameMessageProvider();
			InputStream jsonStream = GameMessageProvider.class.getClassLoader().getResourceAsStream(resourcePath);
			JsonReader reader = new JsonReader(new InputStreamReader(jsonStream));
			Gson gson = new Gson();
			instance = gson.fromJson(reader, GameMessageProvider.class); 
		}
		return instance;
	}
	
	private GameMessageProvider() {
	}
	
	@Override
	public MessageInLanguage getMessage(String messageID, String language) {
		if(messagesByIdAndLanguage == null) {
			initializeMessageTable();
		}
		MessageInLanguage message = messagesByIdAndLanguage.get(messageID).get(language);
		return message;
	}
	
	@Override
	public MessageInLanguage getMessage(String messageID, String language, String[] parameters) {
		MessageInLanguage message = getMessage(messageID, language);
		MessageInLanguage newMessage = new MessageInLanguage();
		newMessage.answers = message.answers;
		newMessage.language = message.language;
		newMessage.text = message.text;
		newMessage.id = message.id;
		for(int i=0; i<parameters.length; i++) {
			String param = "&" + i;
			newMessage.text = newMessage.text.replaceAll(param, parameters[i]);
		}
		return newMessage;
	}

	private void initializeMessageTable() {
		messagesByIdAndLanguage = new Hashtable<String, Hashtable<String, MessageInLanguage>>();
		for(MessageTexts messageIds : messages) {
			Hashtable<String, MessageInLanguage> messagesInLanguage = messagesByIdAndLanguage.get(messageIds.id);
			if(messagesInLanguage == null) {
				messagesInLanguage = new Hashtable<String, MessageInLanguage>();
				messagesByIdAndLanguage.put(messageIds.id, messagesInLanguage);
			}
			for(MessageInLanguage text : messageIds.texts) {
				text.id = messageIds.id;
				messagesInLanguage.put(text.language, text);
			}
		}
	}

}
