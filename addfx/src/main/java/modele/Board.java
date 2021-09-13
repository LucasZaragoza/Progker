package modele;

import java.util.Vector;

public class Board {
	private Card f1;
	private Card f2;
	private Card f3;
	
	private Card turn;
	
	private Card river;
	
	public Board(Card ff1, Card ff2, Card ff3) {
		f1 = ff1;
		f2 = ff2;
		f3 = ff3;
		turn = null;
		river = null;
	}
	
	public void addTurn (Card c) {
		turn = c;
	}
	
	public void addRiver(Card c) {
		river = c;
	}
	
	public Vector<Card> getCards () {
		Vector<Card> res = new Vector<Card>();
		res.add(f1);
		res.add(f2);
		res.add(f3);
		if (turn != null) {
			res.add(turn);
			if (river != null)
				res.add(river);
		}
		return res;
	}
}
