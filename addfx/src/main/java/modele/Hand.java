package modele;

public class Hand {
	
	private Card c1;
	private Card c2;
	
	public Hand (Card one, Card two, JeuCartes jeu) {	//la main fait toujours 4 caracteres
		if (jeu.CardDispo(one.toString())) {
			c1 = jeu.useCard(one.toString());
		}
		if (jeu.CardDispo(two.toString())) {
			c2 = jeu.useCard(two.toString());
		}
	}
	
	public Hand (Card one, Card two) {	//la main fait toujours 4 caracteres
		c1 = one;
		c2 = two;
	}
	
	//constructeur pour joueur
	public Hand (String s, JeuCartes jeu) {
		String sc1 = s.substring(0, 2);
		if (jeu.CardDispo(sc1)) {
			c1 = jeu.useCard(sc1);
		}
		String sc2 = s.substring(2);
		if (jeu.CardDispo(sc2)) {
			c2 = jeu.useCard(sc2);
		}
	}
	
	public Hand () {
		c1 = new Card();
		c2 = new Card();
	}
	
	public Card [] getCards () {
		Card [] res = new Card [2];
		res[0] = c1;
		res[1] = c2;
		return res;
	}
}
