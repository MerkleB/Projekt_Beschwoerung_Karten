package project.main.Listeners;

import project.main.Card.*;
import project.main.Effect.Effect;

public interface SummonListener {
	public void summonDestroyed(Card destroyer, Summon summon);
	public void summonEnchanted(Effect effect, Summon summon);
	public void summonStatusChanged(StatusChange change, Summon summon);
}
