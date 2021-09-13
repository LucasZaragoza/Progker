package org.openjfx.addfx;


import java.util.Vector;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import modele.Fonctions;
import modele.Card;
import modele.Dealer;
import modele.Hand;
import modele.JeuCartes;
import modele.Player;
import modele.TourTable;

public class SixJoueursController {
	
	@FXML AnchorPane mainPane;
	
	@FXML ImageView flopc1; @FXML ImageView flopc2;	@FXML ImageView flopc3;
	@FXML ImageView turnc1;	@FXML ImageView riverc1;
	
	@FXML AnchorPane playerPane; Player player;
	@FXML AnchorPane villain1; Player v1;
	@FXML AnchorPane villain2; Player v2;
	@FXML AnchorPane villain3; Player v3;
	@FXML AnchorPane villain4; Player v4;
	@FXML AnchorPane villain5; Player v5;
	
	@FXML Button boutonDealer; @FXML ImageView jetonDealer; Dealer dealer;
	@FXML Button startBut;
	
	Vector<Player> joueurs;
	Vector<Player> nonjoueurs;
	
	JeuCartes jeuCartes;
	
	/**
	 * ini classe
	 */
	@FXML
	protected void initialize() {		
		iniJoueurs(); 	//initialise tous les joueurs
		
		boutonsAdd();	//on ajoute tous les boutons add
		
		resetCartes();	//creation jeu de cartes
		
		spawn(0);	//fait apparaitre le joueur
		jetonDealer.setImage(Fonctions.getImage("dealer.png")); dealer = new Dealer(player); moveDeal(); //creation dealer sur joueur
		
	}
	
	/**
	 * initialise les objets joueurs
	 */
	private void iniJoueurs() {
		joueurs = new Vector<Player>(); //crée le vecteur qui contient les joueurs du début de la main
		nonjoueurs =  new Vector<Player>();
		player = new Player(100, true, 0, playerPane);
		v1 = new Player(100, false, 1, villain1); nonjoueurs.add(v1); 
		v2 = new Player(100, false, 2, villain2); nonjoueurs.add(v2); 
		v3 = new Player(100, false, 3, villain3); nonjoueurs.add(v3); 
		v4 = new Player(100, false, 4, villain4); nonjoueurs.add(v4); 
		v5 = new Player(100, false, 5, villain5); nonjoueurs.add(v5); 
	}
	
	/**
	 * crée boutons d ajouts de joueurs
	 */
	private void boutonsAdd() {
		villain1.getChildren().add(addPlayerBut(villain1));
		villain2.getChildren().add(addPlayerBut(villain2));
		villain3.getChildren().add(addPlayerBut(villain3));
		villain4.getChildren().add(addPlayerBut(villain4));
		villain5.getChildren().add(addPlayerBut(villain5));
	}
	
	//DEROULEMENT TABLE DE JEU
	
	/**
	 * debut d'une main
	 */
	@FXML
	public void debutTour() {
		
		if (joueurs.size() >= 2 && player.getHand().getCards()[0].getValue() != 0) {
			//bouton de gestions "globaux"
			disparait(boutonDealer);
			disparait(startBut);
			
			//on ne modifie plus participants(nb ou stacks) au cours de la main
			debutAddRemove();
			
			//lance le tour
			new TourTable(joueurs, dealer.getDealer(), mainPane, this, jeuCartes);
		} else {
			if (joueurs.size() < 2)
				System.out.println("Au moins 2 joueurs pour jouer, merci.");
			if (player.getHand().getCards()[0].getValue() != 0)
				System.out.println("Le joueur doit avoir des cartes.");
		}
	}
	
	/**
	 * fait disparaitre les add remove de chaque joueur ainsi que modif de stack
	 */
	public void debutAddRemove() {
		//modif stack et rem des joueurs jouant
		for (int i = 0; i < joueurs.size(); i++) {
			ObservableList<Node> enfants = joueurs.get(i).getAnchorPane().getChildren();
			for(int j = 0; j < enfants.size(); j++) {
				Node child = enfants.get(j);
				if (child.getId() != null) {
					if (child.getId().equals("remBut")
							|| child.getId().equals("incBut") || child.getId().equals("decBut")
							|| child.getId().equals("cardsText")) {
						disparait((Region)child);
					}
					if (child.getId().equals("stackText")) {
						child.setDisable(true);
					}
				}
			}
		}
		
		//butt add des joueurs non sur la table
		for (int i = 0; i < nonjoueurs.size(); i++) {
			ObservableList<Node> enfants = nonjoueurs.get(i).getAnchorPane().getChildren();
			for (int j = 0; j < enfants.size(); j++)
				if (enfants.get(j).getId() != null 
					&& enfants.get(j).getId() == "addBut") {
					disparait((Region)enfants.get(j));
				}
		}
	}
		
	/**
	 * fin d'une main
	 */
	public void finTour() {
		resetCartes();
		apparait(boutonDealer);
		apparait(startBut);
		moveDeal();
		removeFlop();

	}
	
	/**
	 * remove le flop
	 */
	private void removeFlop() {
		((ImageView)Fonctions.getFromPaneById(mainPane, "flopc1")).setImage(null);
		((ImageView)Fonctions.getFromPaneById(mainPane, "flopc2")).setImage(null);
		((ImageView)Fonctions.getFromPaneById(mainPane, "flopc3")).setImage(null);
		((ImageView)Fonctions.getFromPaneById(mainPane, "turnc1")).setImage(null);
		((ImageView)Fonctions.getFromPaneById(mainPane, "riverc1")).setImage(null);
	}
	
	/**
	 * fait disparaitre les add remove de chaque joueur ainsi que modif de stack
	 */
	public void finAddRemove() {
		//modif stack et rem des joueurs jouant
		for (int i = 0; i < joueurs.size(); i++) {
			joueurs.get(i).resetCards();
			ObservableList<Node> enfants = joueurs.get(i).getAnchorPane().getChildren();
			for(int j = 0; j < enfants.size(); j++) {
				Node child = enfants.get(j);
				if (child.getId() != null) {
					if (child.getId().equals("remBut")
							|| child.getId().equals("incBut") || child.getId().equals("decBut") 
							|| child.getId().equals("stackText")
							|| child.getId().equals("cardsText")) {
						apparait((Region)child);
					}
				}
			}
		}
		
		//butt add des joueurs non sur la table
		for (int i = 0; i < nonjoueurs.size(); i++) {
			ObservableList<Node> enfants = nonjoueurs.get(i).getAnchorPane().getChildren();
			for (int j = 0; j < enfants.size(); j++)
				if (enfants.get(j).getId() != null 
					&& enfants.get(j).getId() == "addBut") {
					apparait((Region)enfants.get(j));
				}
		}
	}
	
	/**
	 * le jeton dealer se deplace
	 */
	@FXML private void moveDeal() {
		Player nouveauDealer = Fonctions.getNextPlayer(joueurs, dealer.getDealer());
		dealer.setDealer(nouveauDealer);
		switch (nouveauDealer.getNumJoueur()) {
		case 0: jetonDealer.setLayoutX(385); jetonDealer.setLayoutY(370); break;
		case 1: jetonDealer.setLayoutX(215); jetonDealer.setLayoutY(370); break;
		case 2: jetonDealer.setLayoutX(215); jetonDealer.setLayoutY(210); break;
		case 3: jetonDealer.setLayoutX(385); jetonDealer.setLayoutY(210); break;
		case 4: jetonDealer.setLayoutX(550); jetonDealer.setLayoutY(210); break;
		case 5: jetonDealer.setLayoutX(550); jetonDealer.setLayoutY(370); break;
		}
	}

	
	/**
	 * fonction precedent spawn(ap,p)
	 */
	private void spawn(int x) {
		switch (x) {
		case 0: spawn(player); break;
		case 1: spawn(v1); break;
		case 2: spawn(v2); break;
		case 3: spawn(v3); break;
		case 4: spawn(v4); break;
		case 5: spawn(v5); break;
		}
	}
	
	/**
	 * crée un joueur, remplit son pane et l'ajoute à la liste courante des joueurs
	 */
	private void spawn(Player p) {
		AnchorPane ap = p.getAnchorPane();
		setBorder(p);
		p.resetStack();
		joueurs.add(p); 
		nonjoueurs.remove(p);
		ap.getChildren().add(carte1(p.getC1()));
		ap.getChildren().add(carte2(p.getC2()));
		ap.getChildren().add(decBut(ap, p));
		ap.getChildren().add(incBut(ap, p));
		ap.getChildren().add(stackT(p));
		if (p.getNumJoueur() == 0)
			ap.getChildren().add(setCards(p));
		if(ap != playerPane) {
			ap.getChildren().add(remPlayerBut(ap, p));
		}
	}
	
	private void setBorder(Player p) {
		AnchorPane ap = p.getAnchorPane();
		if (p == player) {
			ap.setBorder(new Border(new BorderStroke(Color.BLACK, 
	        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		} else {
			ap.setBorder(new Border(new BorderStroke(Color.RED, 
			        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		}
	}

	/**
	 * renvoie un bouton qui ajoutera le joueur sur l'ap ou il se trouve
	 */
	private Node addPlayerBut(AnchorPane ap) {
		Button addPlayer = new Button();
		addPlayer.setId("addBut");
		addPlayer.setLayoutX(30);
		addPlayer.setLayoutY(175);
		addPlayer.setOnAction(l -> {
			addPlayer(ap);
		});
		addPlayer.setText("Add");
		return addPlayer;
	}
	
	/**
	
	 * ajoute le joueur de l'ap
	 */
	private void addPlayer(AnchorPane ap) {
		int numPlayer = Integer.parseInt(ap.getId().substring(ap.getId().length()-1));
		ap.getChildren().clear();
		spawn(numPlayer);		
	}
	
	/**
	 * renvoie un bouton qui supprimera le joueur sur l'ap ou il se trouve
	 */
	private Node remPlayerBut(AnchorPane ap, Player p) {
		Button remPlayer = new Button();
		remPlayer.setId("remBut");
		remPlayer.setLayoutX(30);
		remPlayer.setLayoutY(175);
		remPlayer.setOnAction(l -> {
			removePlayer(ap, p);
		});
		remPlayer.setText("Del");
		return remPlayer;
	}

	/**
	 * supprime le joueur de l'ap et de la liste courante des joueurs
	 */
	private void removePlayer(AnchorPane ap, Player p) {
		ap.getChildren().clear();
		ap.getChildren().add(addPlayerBut(ap));	
		if (dealer.getDealer() == p) {	//si ce joueur est dealer, le jeton bouge
			moveDeal();
		}
		joueurs.remove(p); nonjoueurs.add(p);
	}
	
	/**
	 * image de la carte 1 du joueur de l'ap
	 */
	private Node carte1(Image im) {
		ImageView imv = new ImageView();
		imv.setId("C1");
		imv.setFitHeight(110); imv.setFitWidth(60);
		imv.setLayoutX(110); imv.setLayoutY(30);
		imv.setPreserveRatio(true);
		imv.setPickOnBounds(true);
		imv.setImage(im);
		return imv;
	}
	
	/**
	 * image de la carte 1 du joueur de l'ap
	 */
	private Node carte2(Image im) {
		ImageView imv = new ImageView();
		imv.setId("C2");
		imv.setFitHeight(110); imv.setFitWidth(60);
		imv.setLayoutX(32); imv.setLayoutY(30);
		imv.setPreserveRatio(true);
		imv.setPickOnBounds(true);
		imv.setImage(im);
		return imv;
	}
	
	/**
	 * renvoie bouton qui decremente le stack du joueur p
	 */
	private Button decBut(AnchorPane ap, Player p) {
		Button dec = new Button();
		dec.setId("decBut");
		dec.setLayoutX(32);
		dec.setLayoutY(124);
		dec.setMnemonicParsing(false);
		dec.setOnAction(l -> changeStack(ap,p,-10));
		dec.setText("-");
		return dec;
	}
	
	/**
	 * renvoie bouton qui incremente le stack du joueur p
	 */
	private Button incBut(AnchorPane ap, Player p) {
		Button inc = new Button();
		inc.setId("incBut");
		inc.setLayoutX(137);
		inc.setLayoutY(124);
		inc.setMnemonicParsing(false);
		inc.setOnAction(l -> changeStack(ap,p,10));
		inc.setText("+");
		return inc;
	} 
	
	/**
	 * renvoie le texte qui affiche le stack du joueur p
	 */
	private TextField stackT(Player p) {
		TextField stackX = new TextField();
		stackX.setLayoutX(75); stackX.setLayoutY(125);
		stackX.setText(p.getStackS());
		stackX.setId("stackText");
		stackX.setFont(new Font(12));
		stackX.setPrefHeight(25);
		stackX.setPrefWidth(50);
		stackX.setOnAction( l -> {
			try {
				Double d = Double.parseDouble(stackX.getText());
				p.setStack(d);
			} catch (Error e) {
				
			}
		});
		return stackX;
	}

	/**
	 * champs texte pour update les cartes d un joueur
	 */
	public Node setCards(Player p) {
		TextField cardsText = new TextField();
		cardsText.setLayoutX(78); cardsText.setLayoutY(0);
		cardsText.setId("cardsText");
		cardsText.setFont(new Font(12));
		cardsText.setPrefHeight(20);
		cardsText.setPrefWidth(45);
		cardsText.setOnAction( l -> {
			try {
				String s = cardsText.getText();
				if (s.length() == 4) {
					String c1 = s.substring(0,2);
					String c2 = s.substring(2);
					if (Card.formatCarteValide(c1) && Card.formatCarteValide(c2) && !c1.equals(c2)) {
						cardsText.setText("");
						
						Hand hand = new Hand(s, jeuCartes);
						p.setHand(hand);
						p.updateImageCards();
					}			
				}
			} catch (Error e) {} });
		return cardsText;
	}
	
	/**
	 * change le stack du joueur p d'un montant de x
	 */
	private void changeStack(AnchorPane ap, Player p, int x) {
		p.changeStack(x);
		for (int i = 0; i < ap.getChildren().size(); i++) {
			if (ap.getChildren().get(i).getId() != null 
					&& ap.getChildren().get(i).getId().equals("stackText")) {
				((TextField)ap.getChildren().get(i)).setText(p.getStackS());
			}
		}
	}
	
	/**
	 * fait apparaitre un objet et le rend utilisable
	 */
	public static void apparait(Region reg) {
		reg.setVisible(true);
		reg.setDisable(false);
	}
	
	/**
	 * fait disparaitre un objet et le rend inutilisable
	 */
	public static void disparait(Region reg) {
		reg.setVisible(false);
		reg.setDisable(true);
	}
	
	public void resetCartes() {
		jeuCartes = new JeuCartes();
	}
	
}

	