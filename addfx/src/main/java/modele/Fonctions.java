package modele;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Vector;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;

public class Fonctions {

	/**
	 * renvoie le joueur suivant p
	 */
	public static Player getPreviousPlayer(Vector<Player> joueurs, Player p) {
		//tri de joueurs dans l'ordre de la table
		joueurs.sort(null);
		
		//on trouve la pos actuelle de p
		int posP = 10000; 
		for(int i = 0; i < joueurs.size(); i++) {
			if (joueurs.get(i) == p) {
				posP = i;
				break;
			}
		}
		
		//on change get le suivant
		posP = (posP - 1) % joueurs.size();

		return joueurs.get(posP);
	}
	
	/**
	 * renvoie le joueur suivant p
	 */
	public static Player getNextPlayer(Vector<Player> joueurs, Player p) {
		//tri de joueurs dans l'ordre de la table
		joueurs.sort(null);
		
		//on trouve la pos actuelle de p
		int posP = 10000; 
		for(int i = 0; i < joueurs.size(); i++) {
			if (joueurs.get(i) == p) {
				posP = i;
				break;
			}
		}
		
		//on change get le suivant
		posP = (posP + 1) % joueurs.size();

		return joueurs.get(posP);
	}
	
	/**
	 * renvoie une image selon le path
	 */
	public static Image getImage(String path) {
		String nomFichier = "src/main/resources/images/" + path;
		Image image = null;
		try {
			image = new Image(new FileInputStream(nomFichier));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return image;
	}
	
	/***
	 * affiche un vector de joueurs
	 */
	public static void printVect(Vector<Player> v) {
		System.out.print("(");
		for (int i = 0; i < v.size(); i++) {
			System.out.print(v.get(i).getNumJoueur() + ",");
		}
		System.out.println(")");
	}

	/** 
	 * return un Node by id
	 */
	public static Node getFromPaneById(AnchorPane ap, String id) {
		for(int i = 0; i < ap.getChildren().size(); i++) {
			if (ap.getChildren().get(i).getId() != null 
					&& ap.getChildren().get(i).getId().equals(id)) {
				return ap.getChildren().get(i);
			}
		}
		return null;
	}
	
	/**
	 * enleve un objet d un node selon un id
	 */
	public static void removeFromPaneById(AnchorPane ap, String id) {
		for(int i = 0; i < ap.getChildren().size(); i++) {
			if (ap.getChildren().get(i).getId() != null 
					&& ap.getChildren().get(i).getId().equals(id)) {
				ap.getChildren().remove(i);
				break;
			}
		}
	}

	/**
	 * return le joueur qui doit ouvrir postflop
	 */
	public static Player getFirst(Vector<Player> joueursDebut, Vector<Player> joueurs, Player dealer) {
		Player commence = dealer;
		for (int i = 0; i < joueursDebut.size(); i++) {
			commence = getNextPlayer(joueursDebut, commence);
			if (commence != null && joueurs.contains(commence)) {
				return commence;
			}
		}
		return null;
	}
	
	/**
	 * retourne le joueur avec la meilleure combi
	 */
	public static Vector<Player> getWinners(Vector<Player> joueurs, Board b) {
		Vector<Player> winners = new Vector<>();
		System.out.print("Dans get winner : "); printVect(joueurs);
		winners.add(joueurs.get(0));
		for (int i = 1; i < joueurs.size(); i++) {
			int comparaison = winners.get(0).getCombinaison().compareTo(joueurs.get(i).getCombinaison());
			if (comparaison == 0) {	//egalite
				winners.add(joueurs.get(i));
			}
			if (comparaison == -1) {	//winner a perdu, lui et ses confreres n'en sont plus
				winners = new Vector<Player>();
				winners.add(joueurs.get(i));
			}
		}
		return winners;
	}

	
	/**
	 * return les winners qui ont le stack le plus petit
	 */
	public static Vector<Player> getWinnersFinis(Vector<Player> winners) {
		
		//calcule gain max
		double gainMax = winners.get(0).getMiseMain();
		for (int i = 0; i < winners.size(); i++) {
			gainMax = Math.min(gainMax, winners.get(i).getMiseMain());
		}
		
		//add ceux avec le max a winnersfinis
		Vector<Player> winnersFinis = new Vector<Player>();
		for (int i = 0; i < winners.size(); i++) {
			if (winners.get(i).getMiseMain() == gainMax) {
				winnersFinis.add(winners.get(i));
			}
		}
		return winnersFinis;
	}
	
}
