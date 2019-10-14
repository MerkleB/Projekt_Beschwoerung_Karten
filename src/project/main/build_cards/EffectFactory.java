package project.main.build_cards;

import project.main.Effect.Effect;
import project.main.jsonObjects.EffectDefinition;

public class EffectFactory implements CreatesEffects {
	
	public static CreatesEffects getInstance() {
		EffectFactory instance = new EffectFactory();
		return instance;
	}
	
	@Override
	public Effect createEffect(EffectDefinition definition) {
		String className = "project.main.Effect."+definition.effectClass;
		Effect effect = null;
		try {
			Class<?> effectClass = Class.forName(className);
			effect = (Effect) effectClass.newInstance();
			effect.initialize(definition.effectValue);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			throw new RuntimeException("EffectFactory - Class "+className+" could no be found or instantiated!");		}
		return effect;
	}

}
