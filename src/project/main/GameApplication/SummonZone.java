package project.main.GameApplication;

import java.util.ArrayList;

import project.main.Card.Card;
import project.main.Card.Summon;

public class SummonZone extends GameZone {
	
	ArrayList<ArrayList<SummoningCircle>> greatCircles;
	
	public SummonZone(Player owner) {
		super(owner);
		greatCircles = new ArrayList<ArrayList<SummoningCircle>>();
		for(int i=0; i<3; i++) {
			ArrayList<SummoningCircle> greatCircle = new ArrayList<SummoningCircle>();
			greatCircles.add(greatCircle);
			for(int j=0; j<3; j++) {
				SummoningCircle circle = new SummoningCircle(owner,i,j);
				greatCircle.add(circle);
			}
		}
	}
	
	@Override
	public String getName() {
		return "SummonZone";
	}

	@Override
	public void setGame(Game game) {
		super.setGame(game);
		for(int i=0; i<greatCircles.size(); i++) {
			for(int j=0; j<greatCircles.get(i).size(); j++) {
				greatCircles.get(i).get(j).setGame(game);
			}
		}
	}

	@Override
	public void removeCard(Card card) {
		super.removeCard(card);
		removeCardFromCircle(card.getID());
	}

	@Override
	public void removeCard(String id) {
		super.removeCard(id);
		removeCardFromCircle(id);
	}
	
	private void removeCardFromCircle(String id) {
		for(int i=0; i<greatCircles.size(); i++) {
			for(int j=0; j<greatCircles.get(i).size(); j++) {
				if(greatCircles.get(i).get(j).findCard(id) != null) {
					greatCircles.get(i).get(j).removeCard(id);
					break;
				}
			}
		}
	}
	
	public void addCardToCircle(Summon summon, SummoningCircle circle) {
		if(circle.isFree()) {
			addCard(summon);
			circle.addCard(summon);
		}
		
	}
	
	public ArrayList<SummoningCircle> getCircles(){
		ArrayList<SummoningCircle> circles = new ArrayList<SummoningCircle>();
		for(int i=0; i<greatCircles.size(); i++) {
			for(int j=0; j<greatCircles.get(i).size(); j++) {
				circles.add(greatCircles.get(i).get(j));
			}
		}
		return circles;
	}

}
