package main.GameApplication;

import main.Card.Card;

public class CollectorZone extends GameZone {

	public CollectorZone(Player owner) {
		super(owner);
	}

	@Override
	public String getName() {
		return "CollectorZone";
	}

	@Override
	public void addCard(Card card) {
		super.addCard(card.getCollector());
	}

	@Override
	public void removeCard(Card card) {
		super.removeCard(card.getCollector());
	}

}
