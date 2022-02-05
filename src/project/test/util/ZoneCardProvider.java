package project.test.util;

import java.util.ArrayList;

import project.main.Card.Card;
import project.main.Card.Spell;
import project.main.Card.Summon;
import project.main.GameApplication.IsAreaInGame;
import project.main.build_cards.CardTypes;

public class ZoneCardProvider {
	
	public Summon[] getFirstSummonFromZone(IsAreaInGame zone, int number) {
		Summon[] summons = new Summon[number];
		int selected = 0;
		ArrayList<Card> cards = zone.getCards();
		for(Card card : cards) {
			if(card.getType().equals(CardTypes.Summon)) {
				selected++;
				summons[selected-1] = (Summon)card;
			}
			if(selected == number) break;
		}
		return summons;
	}
	
	public Spell[] getFirstSpellFromZone(IsAreaInGame zone, int number) {
		Spell[] spells = new Spell[number];
		int selected = 0;
		ArrayList<Card> cards = zone.getCards();
		for(Card card : cards) {
			if(card.getType().equals(CardTypes.Spell)) {
				selected++;
				spells[selected-1] = (Spell)card;
			}
			if(selected == number) break;
		}
		return spells;
	}
	
	public void moveCardsFromZoneToOtherZone(IsAreaInGame zone, IsAreaInGame otherZone) {
		ArrayList<Card> cards = zone.getCards();
		for(int i=0; i<cards.size(); i++) {
			otherZone.addCard(cards.get(i));
		}
		cards = otherZone.getCards();
		for(int i=0; i<cards.size(); i++) {
			zone.removeCard(cards.get(i));
		}
	}

}
