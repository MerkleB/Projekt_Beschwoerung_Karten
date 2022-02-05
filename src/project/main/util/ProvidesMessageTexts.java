package project.main.util;

import project.main.jsonObjects.MessageInLanguage;

public interface ProvidesMessageTexts {
	public MessageInLanguage getMessage(String messageID, String language);
	public MessageInLanguage getMessage(String messageID, String language, String[] parameters);
}
