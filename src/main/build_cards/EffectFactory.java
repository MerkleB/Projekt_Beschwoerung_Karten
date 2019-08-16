package main.build_cards;

import main.Action.Effect;

public class EffectFactory implements CreatesEffects {
	
	public static CreatesEffects getInstance() {
		EffectFactory instance = new EffectFactory();
		return instance;
	}
	
	@Override
	public Effect createEffect(String effectName) {
		// TODO Auto-generated method stub
		return null;
	}

}
