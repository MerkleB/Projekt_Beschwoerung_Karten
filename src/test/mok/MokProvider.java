package test.mok;

import java.util.Hashtable;

import main.Card;
import main.Effect;
import main.GameZone;
import main.Player;
import main.Spell;
import main.Summon;

public class MokProvider {
	public static Effect getEffect() {
		return new Effect(){
		
			@Override
			public void execute() {
				
			}
		
			@Override
			public boolean isExecutable() {
				return false;
			}
		
			@Override
			public String getDescription() {
				return "This is an effect.";
			}

			@Override
			public void getName() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void activate() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean activatable() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public Card getOwningCard() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void setOwningCard(Card owningCard) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void withdraw() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setActiv() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setInactiv() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public Card getCard() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void setCard(Card card) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public Hashtable<String, String> getMetaData() {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}
	
	public static Player getPlayer() {
		return new Player() {
			
			@Override
			public Summon findSummon(String id) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Spell findSpell(String id) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Card findCard(String id) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void deavtivateAll() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void activate(String[] actions, Player player) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public GameZone getGameZone(String zoneName) {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}
}
