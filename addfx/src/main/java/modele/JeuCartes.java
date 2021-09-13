package modele;

import java.util.Vector;

public class JeuCartes {
	public static Vector<Card> jeu;
	
	public JeuCartes() {		//genere 52 cartes
		jeu = new Vector<Card>();
		String couleur = "";
		for(int i = 0; i < 4; i++) {
			switch (i) {
			case 0 : couleur = "s"; break;
			case 1 : couleur = "d"; break;
			case 2 : couleur = "h"; break;
			case 3 : couleur = "c"; break;
			}
			for (int j = 2; j < 10; j++) {
				jeu.add(new Card(j, couleur));
			}
			jeu.add(new Card("T" + couleur));
			jeu.add(new Card("J" + couleur));
			jeu.add(new Card("Q" + couleur));
			jeu.add(new Card("K" + couleur));
			jeu.add(new Card("A" + couleur));
		}
		System.out.println("size jeu = " + jeu.size());
	}
	
	/**
	 * renvoie la carte pour qu'elle soit utilisée 
	 */
	public Card useCard(String card) {
		Card c = getCard(card);
		jeu.remove(c);
		System.out.println("size jeu restant = " + jeu.size());
		return c;
	}
	
	private Card getCard(String card) {
		for(int i = 0; i < jeu.size(); i++) {
			if (card.equalsIgnoreCase(jeu.get(i).toString())) {
				return jeu.get(i);
			}
		}
		return null;
	}
	
	public boolean CardDispo(String s) {
		if (getCard(s) != null) 
			return true;
		return false;
	}
	
	public Vector<Card> getJeu(){
		return jeu;
	}
}
