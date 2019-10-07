package project.main.Listeners;

import project.main.Action.Effect;
import project.main.Card.*;

public interface SummonListener {
	public void summonDestroyed(Card destroyer, Summon summon);
	public void summonEnchanted(Effect effect, Summon summon);
	public void summonStatusChanged(StatusChange change, Summon summon);
}
