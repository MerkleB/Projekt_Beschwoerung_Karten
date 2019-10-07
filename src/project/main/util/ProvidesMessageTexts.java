package project.main.util;

public interface ProvidesMessageTexts {
	public MessageInLanguage getMessage(String messageID, String language);
	public MessageInLanguage getMessage(String messageID, String language, String[] parameters);
}
