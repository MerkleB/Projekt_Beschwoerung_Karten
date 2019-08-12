package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import main.Card;
import main.Effect;
import main.GameAction;
import main.Spell;
import main.exception.NoCardException;
import main.exception.NoCollectorException;
import test.mok.MokProvider;

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
		mokActions[0] = MokProvider.getGameAction();
		cut = new Spell("TestSpell", "This spell is for testing", effectDummies, 5, 3, 10, MokProvider.getPlayer(), mokActions);
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
