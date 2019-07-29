package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import main.Card;
import main.Effect;
import main.Spell;
import main.exception.NoCardException;
import main.exception.NoCollectorException;

public class SpellTest {
	
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
		cut = new Spell("TestSpell", "This spell is for testing", effectDummies, 5, 3, 10);
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

	@Test
	public void testGetFreeEnergy() {
		boolean errorWasNotThrown = true;
		
		try {
			cut.getFreeEnergy();
		} catch (NoCollectorException e) {
			errorWasNotThrown = false;
		}
		
		if(errorWasNotThrown) {
			fail("NoCollectorException was expected.");
		}
	}

}
