package project.main.util;

import project.main.jsonObjects.EffectDescription;

public interface ManagesTextLanguages {
	public String getCardName(String cardID, String language);
	public String getCardTrivia(String cardID, String language);
	public String getActionName(String actionCode, String language);
	public EffectDescription getEffectDescription(String effectClass, String language);
}
