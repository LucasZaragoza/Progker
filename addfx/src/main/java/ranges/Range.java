package ranges;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import modele.Board;
import modele.Hand;
import modele.Card;
import modele.JeuCartes;

public class Range {
	
	public static void getRangePreflop(Hand h, JeuCartes j) {
	}
	
	public static void getRangePostFlop(Hand h, Board b, JeuCartes j) {
		
	}
	
	/**
	 * calcule toutes les combi possible pour chaque main possible au preflop
	 */
	public Vector<Hand> genereCombisPreflop(JeuPot jeu){
		Vector<Hand> hands = genereMains(jeu);
		Vector<Hand> mComb = new Vector<Hand>();
		return null;
	}
	
	/**
	 * calcule toutes la meilleure combi possible pour chaque main possible au postflop
	 */
	public Map<Hand, CombiPotentielle> genereCombisPostflop(Board b, JeuPot jeu){
		Vector<Hand> hands = genereMains(jeu);
		Map<Hand, CombiPotentielle> mComb = new HashMap<Hand, CombiPotentielle>();
		for (int i = 0; i < hands.size(); i++) {
			Hand h = hands.get(i);
			mComb.put(h, new CombiPotentielle(h,b,jeu));
		}
		return mComb;
	}

	/**
	 * genere toutes les mains possibles depuis un jeu de cartes 
	 */
	private Vector<Hand> genereMains(JeuPot jeu) {
		Vector<Hand> hands = new Vector<Hand>();
		Vector<Card> cards = jeu.getJeu();
		for (int i = 0; i < jeu.getJeu().size() - 1; i++) {
			for (int j = i; j < jeu.getJeu().size(); j++) {
				hands.add(new Hand(cards.get(i), cards.get(j)));
			}
		}
		return hands;
	}
	
	
}
