package ranges;

import java.util.Vector;

import modele.Card;
import modele.JeuCartes;

public class JeuPot {

	private JeuCartes jeu;
	public int nb2;
	public int nb3;
	public int nb4;
	public int nb5;
	public int nb6;
	public int nb7;
	public int nb8;
	public int nb9;
	public int nbT;
	public int nbJ;
	public int nbQ;
	public int nbK;
	public int nbA;
	public int nbH;
	public int nbS;
	public int nbD;
	public int nbC;
	
	
	public JeuPot(JeuCartes jeuc) {
		jeu = jeuc;
		nb2 = 0; nb3 = 0; nb4 = 0; nb5 = 0; nb6 = 0; nb7 = 0; nb8 = 0; nb9 = 0; 
		nbT = 0; nbJ = 0; nbQ = 0; nbK = 0; nbA = 0; nbH = 0; nbS = 0; nbC = 0; nbD = 0; 
		calculRestes();
	}
	
	/**
	 * calcule les occurences de chaque valeur/couleur
	 */
	private void calculRestes() {
		for (int i = 0; i < jeu.getJeu().size(); i++) {
			Card c = jeu.getJeu().get(i);
			switch (c.getCouleur()) {
			case "h": nbH++; break;
			case "s": nbS++; break;
			case "d": nbD++; break;
			case "c": nbC++; break;
			}
			switch (c.getValue()) {
			case 2: nb2++; break;
			case 3: nb3++; break;
			case 4: nb4++; break;
			case 5: nb5++; break;
			case 6: nb6++; break;
			case 7: nb7++; break;
			case 8: nb8++; break;
			case 9: nb9++; break;
			case 10: nbT++; break;
			case 11: nbJ++; break;
			case 12: nbQ++; break;
			case 13: nbK++; break;
			case 14: nbA++; break;
			}
		}
	}

	
	public Vector<Card> getJeu() {
		return jeu.getJeu();
	}
	
}
