package main.util;

public interface ManagesTextLanguages {
	public String getCardName(String cardID, String language);
	public String getCardTrivia(String cardID, String language);
	public String getActionName(String actionCode, String language);
}
