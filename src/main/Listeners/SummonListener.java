package main.Listeners;

import main.Action.Effect;
import main.Card.*;

public interface SummonListener {
	public void summonDestroyed(Card destroyer, Summon summon);
	public void summonEnchanted(Effect effect, Summon summon);
	public void summonStatusChanged(StatusChange change, Summon summon);
}
