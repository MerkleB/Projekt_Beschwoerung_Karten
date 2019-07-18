package test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.Effect.AccelType;

import main.Effect;
import main.Summon;

public class SummonTest {

	private Effect[] effectDummies;

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
				return null;
			}
		};

		effectDummies = new Effect[1];
		effectDummies[0] = effectDummy;
	}
	
	@Test
	public void testGetTrivia() {
		Summon cut = new Summon("Test Summon", "Test", effectDummies, 2, 1, 5, 1, 4, "Natürliche Bestie", "Junges", "Feuer", 1, 3, 4);
		if(!cut.getTrivia().equals("Test")){
			fail("Expected Trivia was not retrieved");
		}
	}

	@Test
	public void testGetEffects() {
		fail("Not yet implemented");
	}

	@Test
	public void testDecreaseVitality() {
		fail("Not yet implemented");
	}

	@Test
	public void testIncreaseVitality() {
		fail("Not yet implemented");
	}

	@Test
	public void testCheckCollector() {
		fail("Not yet implemented");
	}

	@Test
	public void testCheckCard() {
		fail("Not yet implemented");
	}
	
	

}
