package project.main.util;

import project.main.jsonObjects.EffectDescription;
import project.main.jsonObjects.TermInLanguage;

public interface ManagesTextLanguages {
	public String getCardName(String cardID, String language);
	public String getCardTrivia(String cardID, String language);
	public String getActionName(String actionCode, String language);
	public EffectDescription getEffectDescription(String effectClass, String language);
	public TermInLanguage getTerm(String termId, String language);
}
