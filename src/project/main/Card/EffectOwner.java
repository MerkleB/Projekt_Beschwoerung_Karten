package project.main.Card;

import project.main.Effect.Effect;
import project.main.exception.NoCardException;

public interface EffectOwner {
	public void activateEffect(int effectNumber) throws NoCardException;
	public Effect[] getEffects() throws NoCardException;
	public Effect getEffect(int index) throws NoCardException;
}
