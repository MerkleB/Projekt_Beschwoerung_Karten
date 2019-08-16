package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import main.Action.Effect;
import main.Action.GameAction;
import main.Card.MagicCollector;
import main.Card.Spell;
import main.exception.NoCollectorException;
import test.mok.MokProvider;

public class MagicCollectorTest {
	
	Effect[] effectDummies;
	MagicCollector cut;
	
	@Before
	public void setUp(){
		Effect effectDummy = MokProvider.getEffect();

		effectDummies = new Effect[1];
		effectDummies[0] = effectDummy;
		GameAction[] mokActions = new GameAction[1];
		mokActions[0] = MokProvider.getGameAction("MokAction");
		Spell testSpell = new Spell("TestSpell", "This spell is for testing", effectDummies, 5, 5, 10, MokProvider.getPlayer(), mokActions);
		cut = new MagicCollector(testSpell, 5, 10);
	}

	@Test
	public void testIncreaseFreeEnergyFromUsed() {
		cut.useEnergy(3);
		if(!(cut.getUsedEnergy() == 3 && cut.getFreeEnergy() == 2)){
			fail("Used Energy was not set properly");
		}
		
		int remainingEnergieForUse = cut.useEnergy(3);
		if(!(remainingEnergieForUse == 1 && cut.getUsedEnergy() == 5 && cut.getFreeEnergy() == 0)){
			fail("When more energy should be used than available the remaining energy should've been retrieved");
		}
	}

	@Test
	public void testIncreaseFreeEnergyFromDepleted() {
		cut.depleteEnergyFromFree(2);
		cut.increaseFreeEnergyFromDepleted(1);
		if(!(cut.getFreeEnergy() == 4 && cut.getDepletedEnergy() == 1)) {
			fail("Test failed - Expected: FreeEnergy=4, DepletedEnergy=1");
		}
	}

	@Test
	public void testDepleteEnergyFromFree() {
		cut.depleteEnergyFromFree(2);
		if(!(cut.getFreeEnergy() == 3 && cut.getDepletedEnergy() == 2)) {
			fail("Test failed - Expected: FreeEnergy=3, DepletedEnergy=2");
		}
		
		int remainingEnergyToDeplete = cut.depleteEnergyFromFree(4);
		if(!(remainingEnergyToDeplete == 1 && cut.getFreeEnergy() == 0 && cut.getDepletedEnergy() == 5)) {
			fail("Test failed - Expected: FreeEnergy=0, DepletedEnergy=5, RemainingEnergyToDeplete=1");
		}
	}

	@Test
	public void testDepleteEnergyFromUsed() {
		cut.useEnergy(3);
		cut.depleteEnergyFromUsed(2);
		if(!(cut.getUsedEnergy() == 1 && cut.getDepletedEnergy() == 2)) {
			fail("Test failed - Expected: UsedEnergy=1, DepletedEnergy=2");
		}
		
		int remainingEnergyToDeplete = cut.depleteEnergyFromUsed(2);
		if(!(cut.getUsedEnergy() == 0 && cut.getDepletedEnergy() == 3 && remainingEnergyToDeplete == 1)) {
			fail("Test failed - Expected: UsedEnergy=1, DepletedEnergy=2, remainingEnergyToDeplete=1");
		}
	}

	@Test
	public void testDecreaseCurrentHealth() {
		try {
			cut.decreaseCurrentHealth(3);
			if(!(cut.getCurrentHealth() == 7)) {
				fail("Test failed - Expected: CurrentHealth=7");
			}
			
			cut.decreaseCurrentHealth(8);
			if(!(cut.getCurrentHealth() == 0)) {
				fail("Test failed - Expected: CurrentHealth=0");
			}
		} catch (NoCollectorException e) {
			fail("Unexpected NoCollectorEception");
		}
	}

	@Test
	public void testIncreaseCurrentHealth() {
		try {
			cut.increaseCurrentHealth(3);
			if(!(cut.getCurrentHealth() == 10)) {
				fail("Test failed - Expected: CurrentHealth=10");
			}
			
			cut.decreaseCurrentHealth(3);
			cut.increaseCurrentHealth(2);
			if(!(cut.getCurrentHealth() == 9)) {
				fail("Test failed - Expected: CurrentHealth=9");
			}
		} catch (NoCollectorException e) {
			fail("Unexpected NoCollectorEception");
		}
	}

	@Test
	public void testIsCompletelyDepleted() {
		try {
			cut.depleteEnergyFromFree(2);
			if(cut.isCompletelyDepleted()) {
				fail("MagicCollector shouldn't be completely depleted.");
			}
			
			cut.depleteEnergyFromFree(6);
			if(!cut.isCompletelyDepleted()) {
				fail("MagicCollector should be completely depleted.");
			}
			
		} catch (NoCollectorException e) {
			fail("Unexpected NoCollectorEception");
		}
	}
}
