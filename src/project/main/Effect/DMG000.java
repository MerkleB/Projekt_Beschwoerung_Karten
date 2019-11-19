package project.main.Effect;

import project.main.GameApplication.Application;
import project.main.GameApplication.IsPhaseInGame;
import project.main.GameApplication.Player;
import project.main.Listeners.GameListener;
import project.main.Listeners.PhaseListener;
import project.main.exception.NotActivableException;
import project.main.util.TextProvider;

public class DMG000 extends CardEffect implements PhaseListener{

	private boolean mainPhaseEntered;
	private boolean mainPhaseLeft;
	private Player target;
	private int value;
	
	@Override
	public String getCode() {
		return "DMG000";
	}

	@Override
	public void activate(Player activator) throws NotActivableException {
		super.activate(activator);
		setTarget();
		metaData.put("TargetID", target.getID().toString());
		game.getActivePhase().getActiveGameStack().addEntry(this);
		GameListener.getInstance().effectActivated(this);
	}
	
	private void setTarget() {
		Player[] players = game.getPlayers();
		for(Player player : players) {
			if(player != owningCard.getOwningPlayer()) {
				target = player;
			}
		}
	}

	@Override
	public boolean activateable(Player activator){
		if(!isActiv) {
			return false;
		}
		if(!mainPhaseEntered) {
			return false;
		}
		return true;
	}

	@Override
	public void execute() {
		if(isActiv && !withdrawn) {
			super.execute();
			target.decreaseHealthPoints(value);
			GameListener.getInstance().effectExecuted(this);
		}
	}
	
	@Override
	public String getDescription() {
		String description = TextProvider.getInstance().getEffectDescription(getCode(), Application.getInstance().getLanguage()).description;
		description = description.replace("&0", value+"");
		return description;
	}

	@Override
	public void initialize(String value) {
		super.initialize(value);
		metaData.put("TargetType", "Player");
		metaData.put("EffectKind","Damage");
		metaData.put("EffectValue",value);
		this.value = Integer.parseInt(value);
		GameListener.getInstance().addPhaseListener(this);
		mainPhaseEntered = false;
	}

	@Override
	public void setInactiv() {
		if(mainPhaseLeft) {
			super.setInactiv();
			mainPhaseEntered = false;
			mainPhaseLeft = false;
		}
	}

	@Override
	public void phaseStarted(IsPhaseInGame phase) {
		if(phase.getName().equals("Main") && game.getActivePlayer() == owningCard.getOwningPlayer()) {
			setActiv(owningCard.getOwningPlayer());
			mainPhaseEntered = true;
		}
	}

	@Override
	public void phaseEnded(IsPhaseInGame phase) {
		if(phase.getName().equals("Main")) {
			if(mainPhaseEntered) {
				mainPhaseLeft = true;
			}
			setInactiv();
		}
	}

}
