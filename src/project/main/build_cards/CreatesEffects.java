package project.main.build_cards;

import project.main.Effect.Effect;
import project.main.jsonObjects.EffectDefinition;

public interface CreatesEffects {
	public Effect createEffect(EffectDefinition definition);
}
