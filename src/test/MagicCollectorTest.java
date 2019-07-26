package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import main.Effect;
import main.Spell;
import main.exception.NoCardException;
import main.exception.NoCollectorException;

public class MagicCollectorTest {
	
	Effect[] effectDummies;
	Spell cut;
	
	@Before
	public void setUp(){
		Effect effectDummy = new Effect(){
		
			@Override
			public void execute() {
				
			}
		
			@Override
			public boolean isExecutable() {
				return false;
			}
		
			@Override
			public String getDescription() {
				return "This is an effect.";
			}
		};

		effectDummies = new Effect[1];
		effectDummies[0] = effectDummy;
		cut = new Spell("TestSpell", "This spell is for testing", effectDummies, 5, 5, 10);
		cut.setIsCollector(true);
	}

	@Test
	public void testIncreaseFreeEnergyFromUsed() {
		try {
			cut.useEnergy(3);
			if(!(cut.getUsedEnergy() == 3 && cut.getFreeEnergy() == 2)){
				fail("Used Energy was not set properly");
			}
			
			int remainingEnergieForUse = cut.useEnergy(3);
			if(!(remainingEnergieForUse == 1 && cut.getUsedEnergy() == 5 && cut.getFreeEnergy() == 0)){
				fail("When more energy should be used than available the remaining energy should've been retrieved");
			}
		} catch (NoCollectorException e) {
			fail("Unexpected NoCollectorEception");
		}
	}

	@Test
	public void testIncreaseFreeEnergyFromDepleted() {
		try {
			cut.depleteEnergyFromFree(2);
			cut.increaseFreeEnergyFromDepleted(1);
			if(!(cut.getFreeEnergy() == 4 && cut.getDepletedEnergy() == 1)) {
				fail("Test failed - Expected: FreeEnergy=4, DepletedEnergy=1");
			}
		} catch (NoCollectorException e) {
			fail("Unexpected NoCollectorEception");
		}
	}

	@Test
	public void testDepleteEnergyFromFree() {
		try {
			cut.depleteEnergyFromFree(2);
			if(!(cut.getFreeEnergy() == 3 && cut.getDepletedEnergy() == 2)) {
				fail("Test failed - Expected: FreeEnergy=3, DepletedEnergy=2");
			}
			
			int remainingEnergyToDeplete = cut.depleteEnergyFromFree(4);
			if(!(remainingEnergyToDeplete == 1 && cut.getFreeEnergy() == 0 && cut.getDepletedEnergy() == 5)) {
				fail("Test failed - Expected: FreeEnergy=0, DepletedEnergy=5, RemainingEnergyToDeplete=1");
			}
		} catch (NoCollectorException e) {
			fail("Unexpected NoCollectorEception");
		}
	}

	@Test
	public void testDepleteEnergyFromUsed() {
		try {
			cut.depleteEnergyFromFree(2);
		} catch (NoCollectorException e) {
			fail("Unexpected NoCollectorEception");
		}
	}

	@Test
	public void testDecreaseCurrentHealth() {
		try {
			cut.depleteEnergyFromFree(2);
		} catch (NoCollectorException e) {
			fail("Unexpected NoCollectorEception");
		}
	}

	@Test
	public void testIncreaseCurrentHealth() {
		try {
			cut.depleteEnergyFromFree(2);
		} catch (NoCollectorException e) {
			fail("Unexpected NoCollectorEception");
		}
	}

	@Test
	public void testIsCompletelyDepleted() {
		try {
			cut.depleteEnergyFromFree(2);
		} catch (NoCollectorException e) {
			fail("Unexpected NoCollectorEception");
		}
	}
	
	@Test
	public void testSetNeededMagicEnergy() {
		boolean exceptionWasNotThrown = true;
		try {
			cut.setNeededMagicEnergy(1);
		} catch (NoCardException e) {
			exceptionWasNotThrown = false;
		}
		
		if(exceptionWasNotThrown) {
			fail("NoCardException was expected but not thrown.");
		}
	}

}
