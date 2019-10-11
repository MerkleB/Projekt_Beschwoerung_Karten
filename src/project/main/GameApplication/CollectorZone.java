package project.main.GameApplication;

import project.main.Card.Card;
import project.main.Listeners.GameListener;
import project.main.exception.NoCardException;

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
		if(card == null) return;
		if(!cardHash.containsKey(card.getID())) {
			try {
				card.setOwningPlayer(owner);
				cardHash.put(card.getID(), card.getCollector());
				cardList.add(card.getCollector());
			} catch (NoCardException e) {
				System.out.println("Abort adding of card to zone "+getName()+". Reason: "+e.getMessage());
			}
			GameListener.getInstance().cardAdded(this, card);
		}
	}

	@Override
	public void removeCard(Card card) {
		super.removeCard(card.getCollector());
	}

}
