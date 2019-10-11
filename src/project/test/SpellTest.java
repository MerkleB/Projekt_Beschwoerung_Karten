package project.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import project.main.Action.GameAction;
import project.main.Card.Spell;
import project.main.Effect.Effect;
import project.main.exception.NoCardException;
import project.test.mok.MokProvider;

public class SpellTest {
	
	Effect[] effectDummies;
	GameAction[] mokActions;
	Spell cut;
	
	@Before
	public void setUp(){
		Effect effectDummy = MokProvider.getEffect();

		effectDummies = new Effect[1];
		effectDummies[0] = effectDummy;
		mokActions = new GameAction[1];
		mokActions[0] = MokProvider.getGameAction("MokAction");
		cut = new Spell("bsc-su-01","TestSpell", "This spell is for testing", effectDummies, 5, 3, 10, MokProvider.getPlayer(), mokActions);
	}

	@Test
	public void testSetNeededMagicEnergy() {
		try {
			int magicEnergyBefore = cut.getNeededMagicEnergy();
			cut.setNeededMagicEnergy(1);
			int magicEnergyAfter = cut.getNeededMagicEnergy();
			if(magicEnergyBefore == magicEnergyAfter) {
				fail("NeededMagicEnergy should have been changed");
			}
			if(magicEnergyAfter != 1) {
				fail("NeededMagicEnergy should be 1");
			}
		} catch (NoCardException e) {
			fail("Unexpected NoCardException was thrown.");
		}
	}

}
