package main.GameApplication;

public class HandZone extends GameZone {
	
	public HandZone(Player owner) {
		super(owner);
	}

	@Override
	public String getName() {
		return "HandZone";
	}

}
