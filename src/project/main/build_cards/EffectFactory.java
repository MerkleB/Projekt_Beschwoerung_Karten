package project.main.build_cards;

import project.main.Effect.Effect;

public class EffectFactory implements CreatesEffects {
	
	public static CreatesEffects getInstance() {
		EffectFactory instance = new EffectFactory();
		return instance;
	}
	
	@Override
	public Effect createEffect(String effectName) {
		String className = "project.main.Effect."+effectName;
		Effect effect = null;
		try {
			Class<?> effectClass = Class.forName(className);
			effect = (Effect) effectClass.newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			throw new RuntimeException("EffectFactory - Class "+className+" could no be found or instantiated!");		}
		return effect;
	}

}
