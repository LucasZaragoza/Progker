package modele;

import java.util.Vector;

public class EtatTable {

	private Vector<Player> joueursDebutTour;
	private Vector<Player> joueurs;
	private double betAmount;
	private double miseEnCours;
	private double pot;
	private int numDernierMiseur;
	private Player firstBetter;
	private TourTable tourTable;

	/**
	 * constructeur preflop, blinde = 1BB
	 */
	public EtatTable(Vector<Player> j, double blinde, int ndm, TourTable t) {
		joueursDebutTour = new Vector<>(j);
		joueurs = new Vector<>(j);
		miseEnCours = blinde;
		betAmount = blinde;
		numDernierMiseur = ndm;
		tourTable = t;
		beginPreflop();
	}
	
	/**
	 * constructeur postflop
	 */	
	public EtatTable(Vector<Player> j, TourTable t, Player commence) {
		joueursDebutTour = new Vector<Player>(j);
		joueurs = new Vector<>(j);
		miseEnCours = 0;
		betAmount = 0;
		tourTable = t;
		firstBetter = commence;
		numDernierMiseur = commence.getNumJoueur();
		commence.joue(this);
	}
	
	/**
	 * demarre le tour de jeu preflop
	 */
	public void beginPreflop() {
		Player bb = tourTable.getBB();
		joueurSuivant(bb);	
	}
	
	/**
	 * fait jouer le joueur suivant x
	 */
	public void joueurSuivant(Player x) {
		Fonctions.getNextPlayer(joueurs, x).joue(this);
	}
	
	public Vector<Player> getJoueurs(){
		return joueurs;
	}
	
	public Player getSB() {
		return tourTable.getSB();
	}
	
 	public double getPot() {
		return pot;
	}
	
	public double getMise() {
		return miseEnCours;
	}
	
	public int getLastBetter() {
		return numDernierMiseur;
	}

	public Player getFirstBetter() {
		return firstBetter;
	}
	
	/**
	 * action quand un joueur se couche
	 */
	public void playerFolds(Player p) {
		
		if (tourTable.isPreflop() && numDernierMiseur == p.getNumJoueur()) {
			numDernierMiseur = Fonctions.getNextPlayer(joueurs, p).getNumJoueur();
		}
		
		p.hideCards();
		p.aJoue();
		Player temp = Fonctions.getNextPlayer(joueurs, p);
		joueurs.remove(p);
		temp.joue(this);
		
	}
	
	/**
	 * action quand un joueur check
	 */
	public void playerChecks(Player p) {
		firstBetter = null;
		p.aJoue();
		joueurSuivant(p);
	}
	
	/**
	 * action quand un joueur suit
	 */
	public void playerCalls(Player p) {
		
		p.mise(miseEnCours);
		p.aJoue();
		p.updateStackMise();
		
		joueurSuivant(p);
	}
	
	/**
	 * action quand un joueur mise
	 */
	public void playerBets(Player p, String am) {
		try {
			double amount = Double.parseDouble(am);
			amount = Math.min(amount, p.getStack());
			if (amount > 1) {
				p.mise(amount);
				p.aJoue();
				p.updateStackMise();
				
				pot += amount;
				miseEnCours = amount;
				betAmount = amount;
				numDernierMiseur = p.getNumJoueur();
				
				joueurSuivant(p);
			}
		} catch (Exception e) {}
	}
	
	/**
	 * action quand un joueur relance
	 */
	public void playerRaises(Player p, String am) {
		try {
			double amount = Double.parseDouble(am);
			amount = Math.min(amount, p.getStack());
			if (amount >= miseEnCours + betAmount || p.getStack() == amount) {
				p.mise(amount);
				p.aJoue();
				p.updateStackMise();
				
				pot += amount;
				if (amount > miseEnCours) {
					betAmount = amount - miseEnCours;
					numDernierMiseur = p.getNumJoueur();
					miseEnCours = amount;	
				}			
				joueurSuivant(p);
			}
		} catch (Exception e) {}
	}
	
	public void tourSuivant() {
		pot = 0;
		for (int i = 0; i < joueursDebutTour.size(); i++) {
			pot += joueursDebutTour.get(i).getMise();
			joueursDebutTour.get(i).finTour();
		}
		tourTable.tourSuivant(pot, joueurs);
	}
	
	public boolean isPreflop() {
		return tourTable.isPreflop();
	}
	
}
