package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import main.Card;
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

			@Override
			public void getName() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void activate() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean activatable() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public Card getOwningCard() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void setOwningCard(Card owningCard) {
				// TODO Auto-generated method stub
				
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
			cut.useEnergy(3);
			cut.depleteEnergyFromUsed(2);
			if(!(cut.getUsedEnergy() == 1 && cut.getDepletedEnergy() == 2)) {
				fail("Test failed - Expected: UsedEnergy=1, DepletedEnergy=2");
			}
			
			int remainingEnergyToDeplete = cut.depleteEnergyFromUsed(2);
			if(!(cut.getUsedEnergy() == 0 && cut.getDepletedEnergy() == 3 && remainingEnergyToDeplete == 1)) {
				fail("Test failed - Expected: UsedEnergy=1, DepletedEnergy=2, remainingEnergyToDeplete=1");
			}
		} catch (NoCollectorException e) {
			fail("Unexpected NoCollectorEception");
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
