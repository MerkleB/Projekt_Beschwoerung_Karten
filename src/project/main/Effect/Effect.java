package project.main.Effect;

import project.main.Action.Stackable;
import project.main.Card.Card;

public interface Effect extends Stackable {
	public String getDescription();
	public boolean isExecutable();
}
