package main.GameApplication;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.UUID;

import main.Card.Card;

public class HandZone implements GameZone {
	
	private ArrayList<Card> cardList;
	private Hashtable<UUID, Card> cardHash;
	
	public HandZone() {
		cardList = new ArrayList<Card>();
		cardHash = new Hashtable<UUID, Card>();
	}
	
	@Override
	public ArrayList<Card> getCards() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Card findCard(UUID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addCard(Card card) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeCard(Card card) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeCard(UUID id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void activate(Player player, IsPhaseInGame gamePhase) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deavtivateAll() {
		// TODO Auto-generated method stub

	}

}
