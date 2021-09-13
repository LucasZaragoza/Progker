package modele;

import java.util.Vector;

import org.openjfx.addfx.SixJoueursController;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class TourTable {
	
	private Vector<Player> joueursDebut;
	private Vector<Player> joueurs;
	private Player dealer;
	private Board board;
	private double pot;
	private AnchorPane mainPane;
	private int nbTour;
	private SixJoueursController controller;
	private JeuCartes jeu;
	//pots principal, secondaire.. selon joueurs
	
	public TourTable(Vector<Player> players, Player p, AnchorPane ap, SixJoueursController c, JeuCartes jeuCartes) {
		joueursDebut = new Vector<>(players);
		joueurs = new Vector<>(players);
		dealer = p;
		mainPane = ap;
		preflop();
		controller = c;
		jeu = jeuCartes;
	}
	
	private void preflop() {
		for (int i = 0; i < joueurs.size(); i++) {	//on sauvegarde les stacks
			joueursDebut.get(i).debutTour();
		}
		blindes();
		int numDernierMiseur = Fonctions.getNextPlayer(joueurs, getBB()).getNumJoueur();
		
		//appel avec derniere mise a 1
		new EtatTable(joueurs, 1, numDernierMiseur, this);
	}
	
	/**
	 * met les blindes des joueurs
	 */
	private void blindes() {
		getSB().mise(0.5);
		getBB().mise(1);
	}
	
	/**
	 * affiche le texte de saisie du flop
	 */
	private void flop() {
		TextField flopText = new TextField();
		flopText.setLayoutX(365); flopText.setLayoutY(210);
		flopText.setPrefWidth(70);
		flopText.setFont(new Font(12));
		flopText.setId("flopText");
		flopText.setOnAction(l -> { 
			//verif format
			if (flopText.getText().length() == 6) {
				String c1 = flopText.getText().substring(0, 2);
				String c2 = flopText.getText().substring(2, 4);
				String c3 = flopText.getText().substring(4);
				if (Card.formatCarteValide(c1) && Card.formatCarteValide(c2) && Card.formatCarteValide(c3)
						&& jeu.CardDispo(c1) && jeu.CardDispo(c2) && jeu.CardDispo(c3)
						&& !c1.equals(c2) && !c2.equals(c3)) {
					//affiche le flop
					Card ct1 = jeu.useCard(c1); Card ct2 = jeu.useCard(c2); Card ct3 = jeu.useCard(c3);
					board = new Board(ct1, ct2, ct3);
					((ImageView)Fonctions.getFromPaneById(mainPane, "flopc1")).setImage(ct1.getImage());
					((ImageView)Fonctions.getFromPaneById(mainPane, "flopc2")).setImage(ct2.getImage());
					((ImageView)Fonctions.getFromPaneById(mainPane, "flopc3")).setImage(ct3.getImage());
					
					///fait disparaitre floptext
					Fonctions.removeFromPaneById(mainPane, "flopText");
					
					//joueur suivant 
					if (allAllIn()) {
						turn();
					} else {
						Player commence = Fonctions.getFirst(joueursDebut, joueurs, dealer);
						new EtatTable(joueurs, this, commence);
					}
				}
			}
		});	
		mainPane.getChildren().add(flopText);
	}
	
	private void turn() {
		TextField turnText = new TextField();
		turnText.setLayoutX(380); turnText.setLayoutY(215);
		turnText.setPrefWidth(40);
		turnText.setFont(new Font(12));
		turnText.setId("turnText");
		turnText.setOnAction(l -> { 
			//verif format
			if (turnText.getText().length() == 2) {
				String c1 = turnText.getText();
				if (Card.formatCarteValide(c1) && jeu.CardDispo(c1)) {
					
					Card ct1 = jeu.useCard(c1);
					board.addTurn(ct1);
					
					//affiche le flop
					((ImageView)Fonctions.getFromPaneById(mainPane, "turnc1")).setImage(new Card(c1).getImage());
					
					///fait disparaitre floptext
					Fonctions.removeFromPaneById(mainPane, "turnText");
					
					//joueur suivant 
					if (allAllIn()) {
						river();
					} else {
						Player commence = Fonctions.getFirst(joueursDebut, joueurs, dealer);
						new EtatTable(joueurs, this, commence);
					}
				}
			}
		});	
		mainPane.getChildren().add(turnText);	
	}
	
	private void river() {
		TextField riverText = new TextField();
		riverText.setLayoutX(380); riverText.setLayoutY(210);
		riverText.setPrefWidth(40);
		riverText.setFont(new Font(12));
		riverText.setId("riverText");
		riverText.setOnAction(l -> { 
			//verif format
			if (riverText.getText().length() == 2) {
				String c1 = riverText.getText();
				if (Card.formatCarteValide(c1) && jeu.CardDispo(c1)) {
					
					Card ct1 = jeu.useCard(c1); 
					board.addRiver(ct1);
					
					//affiche le flop
					((ImageView)Fonctions.getFromPaneById(mainPane, "riverc1")).setImage(new Card(c1).getImage());
					
					///fait disparaitre floptext
					Fonctions.removeFromPaneById(mainPane, "riverText");
					
					//joueur suivant 
					if (allAllIn()) {
						abattage();
					} else {
						Player commence = Fonctions.getFirst(joueursDebut, joueurs, dealer);
						new EtatTable(joueurs, this, commence);
					}
				}
			}
		});	
		mainPane.getChildren().add(riverText);	
	}
	
	/**
	 * test si main est finie selon nbjoueurs restants 
	 */
	private boolean isFinMain() {
		if (joueurs.size() == 1)
			return true;
		return false;
	}
	
	/**
	 * test si plus d'un joueur encore de quoi miser
	 */
	private boolean allAllIn() {
		int cptJoueursRestants = 0;
		for (int i = 0; i < joueurs.size(); i++) {
			if (!joueurs.get(i).isAllIn()) {
				cptJoueursRestants++;
			}
		}
		return cptJoueursRestants < 2;
	}
	
	/**
	 * calcul le winner de la main au showdown
	 */
	private void abattage() {	
		//texte choix cartes + bouton hide pour coucher pour tous joueurs sauf 0
		for (int i = 0; i < joueurs.size(); i++) {
			if (joueurs.get(i).getNumJoueur() != 0) {
				joueurs.get(i).abattage(this);
			}
		}	
	}
	
	public void hideHand(Player p) {
		joueurs.remove(p);
		testAbattage();
	}
	
	/**
	 * test si l'abattage a lieu ou s'il manque des cartes de joueurs
	 */
	public void testAbattage() {
		boolean abattage = true;
		for (int i = 0; i < joueurs.size(); i++) {
			if (joueurs.get(i).getHand().getCards()[0].getValue() == 0 
					|| joueurs.get(i).getHand().getCards()[0].getValue() == 0) {
				abattage = false;
				break;
			}
		}
		if (abattage) {
			finMain();
		}
	}
	
	public void attributionGainsPots() {
		//calcul des pots séparés
		//calcul du gagnant de chaque pot
		//update stack de chaque gagnant
		//si un joueur arrive a 0, recave a 100
		
		//stackDebutTour - stack = argent misé depuis debut tour
		
		for(int i = 0; i < joueurs.size(); i++) {
			joueurs.get(i).setCombinaison(board);
		}
		
		double lastGain = 0;
		while(pot > 0) {
			System.out.print("il reste " + pot + " dans le pot");
			Vector<Player> winners = Fonctions.getWinners(joueurs, board);
			Vector<Player> winnersFinis = Fonctions.getWinnersFinis(winners);

			System.out.print("\n\n Debut winners : ");
			Fonctions.printVect(winners);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
			//calcul du gain max des winners
			double gainsMax = winnersFinis.get(0).getMiseMain() - lastGain;
			
			//winners se partagent ce pot
			double sommePotTemp = 0;
			for(int i = 0; i < joueursDebut.size(); i++) {
				double aDispo = joueursDebut.get(i).getMiseMain() - lastGain;
				if (aDispo < 0) {
					aDispo = 0;
				}
				sommePotTemp += Math.min(aDispo, gainsMax);
				System.out.println("joueur " + joueursDebut.get(i).getNumJoueur() + " a misé "
				+ joueursDebut.get(i).getMiseMain() + " et reste de la mise " + (joueursDebut.get(i).getMiseMain() - lastGain));
			}
			
			System.out.println("SommePotTemp = " + sommePotTemp);			
			//partage et attribution des gains du potTemp
			for(int i = 0; i < winners.size(); i++) {
				double gain = Math.floor(sommePotTemp / winners.size() * 100) / 100;
				System.out.println("On donne " + gain + " au joueur " + winners.get(i));
				winners.get(i).changeStack(gain);
				winners.get(i).updateStackMise();
			}
			
			//remove le/les winner avec stack min
			for (int i = 0; i < winnersFinis.size(); i++) {
				winners.remove(winnersFinis.get(i));
			}
			
			//memoire du derniergain
			lastGain += gainsMax;
			
			//remove tous les joueurs ayant leur mise dépassée par le gains deja attribués
			for (int i = joueurs.size() - 1; i >= 0; i--) {
				if (joueurs.get(i).getMiseMain() <= lastGain) {
					joueurs.remove(i);
				}
			}
			joueurs.sort(null);
			
			pot -= sommePotTemp;
		}
	}

	public void tourSuivant(Double potTour, Vector<Player> j) {
		joueurs = j;
		pot += potTour;
		if (Fonctions.getFromPaneById(mainPane, "potText") != null) {
			majPot();
		} else {
			addPot();
		}
		
		if (isFinMain()) {
			finMain();
		} else {
			nbTour++;
			switch (nbTour) {
			case 1: flop(); break;
			case 2: turn(); break;
			case 3: river(); break;
			case 4: abattage(); break;
			}
		}
	}

	/**
	 * enleve affichage pot et cartes
	 * reaffiche les champs necessaires a chaque joueur 
	 * reaffiche start, enleve end
	 */
	private void finMain() {
		for (int i = 0; i < joueursDebut.size(); i++) {
			joueursDebut.get(i).finTourAffichage();
			joueursDebut.get(i).setAbattage();
		}
		attributionGainsPots();
		
		remPot();
		controller.finAddRemove();
		controller.finTour();
	}
	
	/**
	 * ajoute l affichage du pot au mainpane
	 */
	private void addPot() {		
		ImageView potImageView = new ImageView();
		potImageView.setLayoutX(370); potImageView.setLayoutY(320);
		potImageView.setFitWidth(60); potImageView.setFitHeight(60);
		potImageView.setId("potImage");
		potImageView.setImage(Fonctions.getImage("pot.jpg"));
		mainPane.getChildren().add(potImageView);
		
		Text potText = new Text();
		potText.setId("potText");
		potText.setFill(Color.YELLOW);
		potText.setStroke(Color.BLANCHEDALMOND);
		potText.setLayoutX(390); potText.setLayoutY(350);
		potText.setText(Double.toString(pot));
		potText.setFont(new Font(16));
		mainPane.getChildren().add(potText);
	}
	
	/**
	 * update la taille du pot
	 */
	private void majPot() {
		Node n = Fonctions.getFromPaneById(mainPane, "potText");
		((Text)n).setText(Double.toString(pot));
	}
	
	/**
	 * enleve image et texte du pot
	 */
	private void remPot() {
		Fonctions.removeFromPaneById(mainPane, "potText");
		Fonctions.removeFromPaneById(mainPane, "potImage");
	}
	
	public boolean isPreflop() {
		return nbTour == 0;
	}
	
	/**
	 * renvoie le joueur de petite blinde
	 */
	public Player getSB() {
		return Fonctions.getNextPlayer(joueurs, dealer);
	}
	
	/**
	 * renvoie le joueur de grosse blinde
	 */
	public Player getBB() {
		return Fonctions.getNextPlayer(joueurs, getSB());
	}

	public JeuCartes getJeuCartes() {
		return jeu;
	}
	
}
