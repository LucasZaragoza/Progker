package modele;

import java.util.Vector;

public class Combinaison implements Comparable<Combinaison>{
	
	public Card[] main;
	public Comb force;
		
	public Combinaison(Hand hand, Board board) {
		force = getForce(hand, board);
	}
	
	public Comb getForce(Hand hand, Board board) {
		
		//on cree le vecteur contenant le board et la main
		Vector<Card> comb = new Vector<Card>();
		Card [] h = hand.getCards();
		comb.add(h[0]);
		comb.add(h[1]);
		
		Vector<Card> b = board.getCards();
		for (int i = 0; i < b.size(); i++) {
			comb.add(b.get(i));
		}
		
		//tri decroissant
		comb.sort(null);
		
		
		//teste les combinaisons dans l ordre decroissant
		//pour chaque test (sauf hc), on renvoie 5 cartes, sauf si pas de combi trouvée
		
		//test sf
		main = sf(comb);
		if (main != null) 
			return Comb.SF;
		
		//test fok 
		main = fok(comb);
		if (main != null) 
			return Comb.FOK;
		
		//test fh 
		main = fh(comb);
		if (main != null)
			return Comb.FH;
		
		//test flush
		main = flush(comb);
		if (main != null) 
			return Comb.FLUSH;
		
		//test straight 
		printVect(comb);
		main = straight(comb);
		if (main != null)
			return Comb.STRAIGHT;
		
		//test tok 
		main = tok(comb);
		if (main != null)
			return Comb.TOK;
		
		//test dp 
		main = dp(comb);
		if (main != null)
			return Comb.DP;
		
		//test p 
		main = p(comb);
		if (main != null)
			return Comb.P;
		
		//test hc
		main = hc(comb);
		return Comb.HC;
	}
	
	private Card [] sf(Vector<Card> comb) {
		Vector<Card> vc = duplique(comb);
		Card [] res = new Card [5];
		
		//test flush dabord
		int cpts = 0; int cpth = 0; int cptd = 0; int cptc = 0;
		for (int i = 0; i < vc.size(); i++) {
			switch (vc.get(i).getCouleur()) {
			case "s": cpts++; break;
			case "c": cptc++; break;
			case "h": cpth++; break;
			case "d": cptd++; break;
			}
		}
		String coul = "";
		if (cpts >= 5)
			coul = "s";
		if (cptc >= 5)
			coul = "c";
		if (cpth >= 5)
			coul = "h";
		if (cptd >= 5)
			coul = "d";

		if (!coul.equals("")) {
			for (int i = vc.size() - 1; i >= 0; i--) {
				if (vc.get(i).getCouleur() != coul)
					vc.remove(i);
			}
			
			//plus que la meme couleur si 5 cartes ou plus de la meme couleur, mtn test suite
			for (int i = 0; i < vc.size() - 4; i++) {
				if(vc.get(i).getValue() == vc.get(i+1).getValue() + 1
					&& vc.get(i).getValue()	== vc.get(i+2).getValue() + 2
					&& vc.get(i).getValue()	== vc.get(i+3).getValue() + 3
					&& vc.get(i).getValue()	== vc.get(i+4).getValue() + 4) {
					for(int j = 0; j < 5; j++) {
						res[j] = vc.get(i + j);
					}
					return res;
				}
			}
			
			int ind = vc.size()-4;
			if (vc.get(ind).getValue() == 5) {	//cas particulier 5-4-3-2-A possible
				if(vc.get(ind).getValue() == vc.get(ind+1).getValue() + 1
						&& vc.get(ind).getValue()	== vc.get(ind+2).getValue() + 2
						&& vc.get(ind).getValue()	== vc.get(ind+3).getValue() + 3
						&& vc.get(0).getValue() == 14) {
						for(int j = 0; j < 4; j++) {
							res[j] = vc.get(ind + j);
						}
						res[4] = vc.get(0);
						return res;
				}
			}
			
		}
		return null;
	}
	
	private Card [] fok(Vector<Card> comb) {
		//creation du tableau de retour
		Card [] res = new Card[5];
		
		for (int i = 0; i < comb.size() - 3; i++) {
			if (comb.get(i).getValue() == comb.get(i+1).getValue() 
					&& comb.get(i).getValue() == comb.get(i+2).getValue() 
					&& comb.get(i).getValue() == comb.get(i+3).getValue()) {
				res[0] = comb.get(i);
				res[1] = comb.get(i+1);
				res[2] = comb.get(i+2);
				res[3] = comb.get(i+3);
				comb.remove(res[0]);
				comb.remove(res[1]);
				comb.remove(res[2]);
				comb.remove(res[3]);
				res[4] = comb.get(0);
				return res;
			}
		}
		return null;
	}
	
	private Card [] fh(Vector<Card> combIn) {
		//creation de dupliquat combIn
		Vector<Card> comb = duplique(combIn);
				
		//creation du tableau de retour
		Card [] res = new Card[5];
		boolean isBrelan = false;
		
		//on choppe le brelan
		for (int i = 0; i < comb.size() - 2; i++) {
			if (comb.get(i).getValue() == comb.get(i+1).getValue() 
					&& comb.get(i+1).getValue() == comb.get(i+2).getValue()) {
				res[0] = comb.get(i);
				res[1] = comb.get(i+1);
				res[2] = comb.get(i+2);
				comb.remove(res[0]);
				comb.remove(res[1]);
				comb.remove(res[2]);
				isBrelan = true;
				break;
			}
		}
		if (isBrelan) {
			//on choppe la paire
			for (int i = 0; i < comb.size() - 1; i++) {
				if (comb.get(i).getValue() == comb.get(i+1).getValue()) {
					res[3] = comb.get(i);
					res[4] = comb.get(i+1);
					return res;
				}
			}
		}
		
		return null;
	}
	
	private Card [] flush(Vector<Card> comb) {

		int cpts = 0; int cpth = 0; int cptd = 0; int cptc = 0;
		for (int i = 0; i < comb.size(); i++) {
			switch (comb.get(i).getCouleur()) {
			case "s": cpts++; break;
			case "c": cptc++; break;
			case "h": cpth++; break;
			case "d": cptd++; break;
			}
		}
		String coul = "";
		if (cpts >= 5)
			coul = "s";
		if (cptc >= 5)
			coul = "c";
		if (cpth >= 5)
			coul = "h";
		if (cptd >= 5)
			coul = "d";

		if (!coul.equals("")) {
			Card [] res = new Card[5];
			int cptHand = 0;
			for (int i = 0; i < comb.size(); i++) {
				if (comb.get(i).getCouleur().equals(coul)) {
					res[cptHand] = comb.get(i);
					cptHand++;
					if (cptHand == 5)
						return res;
				}
			}
		}
		return null;
	}
	
	private Card [] straight(Vector<Card> comb) { 
		
		//vector de cartes sans doubles
		Vector<Card> comb2 = new Vector<Card>();
		comb2.add(comb.get(0));
		int memoire = 0;
		for (int i = 1; i < comb.size(); i++) {
			if (comb.get(i).getValue() != comb2.get(memoire).getValue()) {
				comb2.add(comb.get(i));
				memoire++;
			}
		}
		
		//A-T , 5-A
		if (comb2.size() >= 5) {	//si 5 cartes differentes ou plus
			
			//creation du tableau de retour
			Card [] res = new Card[5];
			
			for (int i = 0; i < comb2.size() - 4; i++) {
				if(comb2.get(i).getValue() == comb2.get(i+1).getValue() + 1
					&& comb2.get(i).getValue()	== comb2.get(i+2).getValue() + 2
					&& comb2.get(i).getValue()	== comb2.get(i+3).getValue() + 3
					&& comb2.get(i).getValue()	== comb2.get(i+4).getValue() + 4) {
					for(int j = 0; j < 5; j++) {
						res[j] = comb2.get(i + j);
					}
					return res;
				}
			}
			
			int ind = comb2.size()-4;
			if (comb2.get(ind).getValue() == 5) {	//cas particulier 5-4-3-2-A possible
				if(comb2.get(ind).getValue() == comb2.get(ind+1).getValue() + 1
						&& comb2.get(ind).getValue()	== comb2.get(ind+2).getValue() + 2
						&& comb2.get(ind).getValue()	== comb2.get(ind+3).getValue() + 3
						&& comb2.get(0).getValue() == 14) {
						for(int j = 0; j < 4; j++) {
							res[j] = comb2.get(ind + j);
						}
						res[4] = comb2.get(0);
						return res;
				}
			}
		}
		return null;
	}
	
	private Card [] tok(Vector<Card> comb) {
		//creation du tableau de retour
		Card [] res = new Card[5];
		
		for (int i = 0; i < comb.size() - 2; i++) {
			if (comb.get(i).getValue() == comb.get(i+1).getValue() 
					&& comb.get(i+1).getValue() == comb.get(i+2).getValue()) {
				res[0] = comb.get(i);
				res[1] = comb.get(i+1);
				res[2] = comb.get(i+2);
				comb.remove(res[0]);
				comb.remove(res[1]);
				comb.remove(res[2]);
				res[3] = comb.get(0);
				res[4] = comb.get(1);
				return res;
			}
		}
		return null;
	}
	
	private Card [] dp(Vector<Card> comb) {
		
		//creation du tableau de retour
		Card [] res = new Card[5];
				
		int nbPaires = 0;
		for (int i = 0; i < comb.size() - 1; i++) {
			if (comb.get(i).getValue() == comb.get(i+1).getValue()) {
				res[nbPaires*2] = comb.get(i);
				res[nbPaires*2+1] = comb.get(i+1);
				nbPaires++;
				i++;
			}
			if(nbPaires == 2) {
				comb.remove(res[0]);
				comb.remove(res[1]);
				comb.remove(res[2]);
				comb.remove(res[3]);
				res[4] = comb.get(0);
				return res;
			}
		}

		return null;
	}
	
	private Card [] p(Vector<Card> comb) {
		
		//verifie s'il y a une paire
		int memoire = 0;
		boolean isPair = false;
		for (int i = 1; i < comb.size(); i++) {
			memoire = i - 1;
			if(comb.get(i).getValue() == comb.get(memoire).getValue()) {
				isPair = true;
				break;
			}
		}
		
		//creation du tableau de retour
		Card [] res = new Card[5];
		
		if (isPair) {
			//add la paire
			res[0] = comb.get(memoire);
			res[1] = comb.get(memoire + 1);
			
			//supprime la paire du vector
			comb.remove(res[0]);
			comb.remove(res[1]);
			
			// add les 3 HC à la paire
			int remplissage = 2;
			for(int i = 0; i < 3; i++)
					res[remplissage + i] = comb.get(i);
			return res;
		} else {
			return null;
		}
	}
	
	private Card [] hc(Vector<Card> comb) {
		Card [] res = new Card[5];
		for (int i = 0; i < 5; i++)
			res[i] = comb.get(i);
		return res;
	}
	
	public String toString() {
		String aRet = "";
		aRet += "[";
		for (int i = 0; i < main.length; i++) {
			aRet += " " + main[i] + " ";
		}
		aRet += "]";
		aRet += "  ;  " + force;
		return aRet;
	}

	private void printVect(Vector<Card> v) {
		System.out.print("[");
		for (int i = 0; i < v.size(); i++) {
			System.out.print(v.get(i) + ",");
		}
		System.out.println("]");
	}
	
	private Vector<Card> duplique(Vector<Card> vect){
		Vector<Card> comb = new Vector<Card>();
		for (int i = 0; i < vect.size(); i++) {
			comb.add(vect.get(i));
		}
		return comb;
	}
	
	public static void test() {
		//h, s, d, c
		Card f1 = new Card(4, "h");
		Card f2 = new Card(3, "h");
		Card f3 = new Card(5, "h");
		Card t1 = new Card(8, "h");
		Board b = new Board(f1,f2,f3);
		b.addTurn(t1);
		Card h1 = new Card(7, "c");
		Card h2 = new Card(14, "h");
		Hand h = new Hand(h1, h2, new JeuCartes());
		Combinaison comb = new Combinaison(h, b);
		System.out.println(comb + " " + comb.force.getforce());
		System.out.println(Comb.SF + "  " + Comb.SF.getforce() + " .. " + Comb.TOK + " " + Comb.TOK.getforce());
		System.out.println("f" + new Card(10, "d") + "e");
	}

	@Override
	public int compareTo(Combinaison c) {
		int forceThis = force.getforce();
		int forceC = c.force.getforce();
		if (forceThis > forceC) {
			return 1;
		}
		if (forceC > forceThis) {
			return -1;
		}
		int compareCartes = 0;
		for (int i = 0; i < 5; i++) {
			compareCartes = main[i].compareTo(c.main[i]);
			if (compareCartes != 0) {
				break;
			}
		}
		return compareCartes;
	}
	
}
