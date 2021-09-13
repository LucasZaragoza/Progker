package modele;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Player implements Comparable<Player>{
	
	private AnchorPane ap;
	private int numJoueur;
	private double stack;
	private double stackDebutTour;
	private double miseAbattage;
	private Hand hand;
	private boolean conscient;
	private boolean allIn;
	private double mise;
	private Combinaison combinaison;
	
	public Player(double s, boolean b, int num, AnchorPane ancp) {
		hand = new Hand();
		stack = s;
		conscient = b;
		numJoueur = num;
		ap = ancp;
		allIn = false;
		mise = 0;
	}
	
	public Hand getHand () {
		return hand;
	}
	
	public void setHand (Hand h) {
		hand = h;
	}
	
	/**
	 * met le stack a x
	 */
	public void setStack(double x) {
		stack = x;
	}
	
	public void resetStack() {
		stack = 100;
	}
	
	/**
	 * add x au stack
	 */
	public void changeStack (double x) {
		stack += x;
	}

	public boolean estJoueur() {
		return conscient;
	}
	
	public Image getC1() {
		return hand.getCards()[0].getImage();
	}
	
	public Image getC2() {
		return hand.getCards()[1].getImage();
	}
	
	public String getStackS() {
		return Double.toString(stack);
	}

	public double getStack() {
		return stack;
	}
	
	public double getMise() {
		return mise;
	}
	
	public int getNumJoueur() {
		return numJoueur;
	}

	public AnchorPane getAnchorPane() {
		return ap;
	}

	public int compareTo(Player otherPlayer) {
		return numJoueur - otherPlayer.getNumJoueur();	
	}

	/**
	 * mise mise a jour en fonction de derniere mise du joueur
	 */
	public void mise(double d) {
		
		//decrementer stack
		if (d >= stack) {
			mise += stack;
			stack = 0;
			allIn = true;
		} else {
			double diff = d - mise;
			stack -= diff;
			mise += diff;
		}
		updateStackMise();
	}
	
	public void misePreflopSB(double d) {
		//ajouter 0.5
		//decrementer stack
		if (d >= stack) {
			mise += stack;
			stack = 0;
			allIn = true;
		} else {
			double diff = d - mise;
			stack -= diff;
			mise = d;
		}
		updateStackMise();
	}
	
	/**
	 * mise a jour affichage de la mise
	 */
	public void updateStackMise() {
		
		boolean bool = true;
		
		for (int i = 0; i < ap.getChildren().size(); i++) {
			if (ap.getChildren().get(i).getId().equals("miseText")) {
				((Text)ap.getChildren().get(i)).setText(Double.toString(mise));
				bool = false;
			}
			if (ap.getChildren().get(i).getId().equals("stackText")) {
				((TextField)ap.getChildren().get(i)).setText(Double.toString(stack));
			}
		}
		
		if (bool) {
			addJeton();
			addMise();
		}
		
	}
	
	/**
	 * ajoute image jeton a ap
	 */
	private void addJeton() {
		ImageView imv = new ImageView();
		imv.setId("miseImage");
		imv.setLayoutX(75); imv.setLayoutY(150);
		imv.setImage(Fonctions.getImage("jeton.png"));
		ap.getChildren().add(imv);
	}

	/**
	 * ajoute le montant de mise dans un text a l ap
	 */
	private void addMise() {
		Text miseX = new Text();
		miseX.setLayoutX(90); miseX.setLayoutY(178);
		miseX.setText(Double.toString(mise));
		miseX.setId("miseText");
		miseX.setFont(new Font(12));
		ap.getChildren().add(miseX);
	}

	public boolean isAllIn() {
		return allIn;
	}

	public void joue(EtatTable e) {
		addOptions(e);
	}
	
	public void addOptions(EtatTable e) {
		if (e.getLastBetter() == numJoueur 
				&& ( !(e.isPreflop() && mise == 0.5 && this == e.getSB())
						&& !(e.isPreflop() && mise == 0 && this != e.getSB())
						&& !(!e.isPreflop() && mise == 0 && this == e.getFirstBetter()))) {	//fin tour de table
			e.tourSuivant();
		} else {	//sinon, on affiche les options
			if (allIn) {
				e.playerChecks(this);
			} else {
				if (e.getMise() > mise) { //affiche call - raise - fold
					callRaiseFold(e);
				} else { //check - bet
					checkBet(e);
				}
			}
		}
	}
	
	/**
	 * ajout bouton check,bet, textfield pour bet
	 */
	private void checkBet(EtatTable e) {
		Button check = new Button();
		check.setLayoutX(35);
		check.setId("checkBut");
		check.setText("Check");
		check.setOnAction( l -> {
			e.playerChecks(this);
		});
		ap.getChildren().add(check);
		
		TextField amount = new TextField();
		amount.setLayoutX(90);
		amount.setPrefWidth(50);
		amount.setFont(new Font(12));
		amount.setId("amountText");
		if (e.isPreflop()) {
			amount.setText("Raise");
		} else {
			amount.setText("Bet");
		}
		amount.setOnMouseClicked( l -> {
			amount.setText("");
		});
		amount.setOnAction(l -> {
			e.playerBets(this, amount.getText());
		});
		ap.getChildren().add(amount);
	}

	/**
	 * ajout bouton call,raise,fold, textfield pour raise
	 */
	private void callRaiseFold(EtatTable e) {
		Button call = new Button();
		call.setLayoutX(35);
		call.setText("Call");
		call.setId("callBut");
		call.setOnAction( l -> {
			e.playerCalls(this);
		});
		ap.getChildren().add(call);
		
		TextField amount = new TextField();
		amount.setLayoutX(75);
		amount.setPrefWidth(50);
		amount.setFont(new Font(12));
		amount.setId("amountText");
		amount.setText("Raise");
		amount.setOnMouseClicked( l -> {
			amount.setText("");
		});
		amount.setOnAction(l -> {
			e.playerRaises(this, amount.getText());
		});
		ap.getChildren().add(amount);
		
		Button fold = new Button();
		fold.setLayoutX(130);
		fold.setText("Fold");
		fold.setId("foldBut");
		fold.setOnAction( l -> {
			e.playerFolds(this);
		});
		ap.getChildren().add(fold);
	}
	
	/**
	 * remove les boutons call/fold..
	 */
	public void aJoue() {
		for (int i = ap.getChildren().size() - 1; i >= 0; i--) {
			String id = ap.getChildren().get(i).getId();
			if (id != null) {
				if (id.equals("amountText") || id.equals("callBut") || id.equals("checkBut")
						 || id.equals("callBut") || id.equals("foldBut") || id.equals("callBut")) {
					ap.getChildren().remove(i);
				}
			}
		}

	}
	
	/**
	 * reset les attributs du joueur pour nouveau tour
	 */
	public void debutTour() {
		mise = 0;
		allIn = false;
		stackDebutTour = stack;
	}
	
	public double getStackDebutTour() {
		return stackDebutTour;
	}
	
	/**
	 * s'occupe de la fin du tour (fin preflop, fin flop..)
	 */
	public void finTour() {
		mise = 0;
		finTourAffichage();
	}
	
	/**
	 * supprime miseText et stackText de l ap
	 */
	public void finTourAffichage() {
		for (int i = ap.getChildren().size() - 1; i >= 0; i--) {
			String x = ap.getChildren().get(i).getId();
			if (x.equals("miseText")) {
				ap.getChildren().remove(i);
			}
			if (x.equals("miseImage")) {
				ap.getChildren().remove(i);			
			}
		}
	}
		
	/**
	 * enleve l'affichage cartes du joueur
	 */
	public void hideCards() {
		Fonctions.getFromPaneById(ap, "C1").setVisible(false);
		Fonctions.getFromPaneById(ap, "C2").setVisible(false);	
	}

	/**
	 * reinitialise les cartes et leur affichage
	 */
	public void resetCards() {
		hand = new Hand();
		((ImageView)Fonctions.getFromPaneById(ap, "C1")).setImage(getC1());
		Fonctions.getFromPaneById(ap, "C1").setVisible(true);	
		((ImageView)Fonctions.getFromPaneById(ap, "C2")).setImage(getC2());
		Fonctions.getFromPaneById(ap, "C2").setVisible(true);
	}

	/**
	 * affiche texte de saisie de main et bouton hide
	 */
	public void abattage(TourTable tourTable) {
		System.out.println();
		ap.getChildren().add(setCards(tourTable));
		ap.getChildren().add(hideCards(tourTable));
		
	}
	
	public Node setCards(TourTable t) {
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
					String c1 = cardsText.getText().substring(0,2);
					String c2 = cardsText.getText().substring(2);
					if (Card.formatCarteValide(c1) && Card.formatCarteValide(c2) && t.getJeuCartes().CardDispo(c1) && t.getJeuCartes().CardDispo(c2)) {
						cardsText.setText("");
						
						Hand hand = new Hand(s, t.getJeuCartes());
						setHand(hand);
						updateImageCards();
						
						finAbattage();
						t.testAbattage();
					}
				}
			} catch (Error e) {} });
		return cardsText;
	}
	
	public Node hideCards(TourTable t) {
		Button hideBut = new Button();
		hideBut.setLayoutX(128); hideBut.setLayoutY(0);
		hideBut.setId("hideBut");
		hideBut.setFont(new Font(12));
		hideBut.setPrefHeight(20);
		hideBut.setPrefWidth(40);
		hideBut.setText("Hide");
		hideBut.setOnAction( l -> {
			try {
				finAbattage();
				hideCards();
				t.hideHand(this);
			} catch (Error e) {} });
		return hideBut;
	}
	
	public void setCombinaison(Board b) {
		combinaison = new Combinaison(hand, b);
	}
	
	public Combinaison getCombinaison() {
		return combinaison;
	}
	
 	private void finAbattage() {
		Fonctions.removeFromPaneById(ap, "cardsText");
		Fonctions.removeFromPaneById(ap, "hideBut");
	}

	/**
	 * met a jour les cartes d'un joueur
	 */
	public void updateImageCards() {
		int taille = ap.getChildren().size();
		int cptCards = 0;
		for (int i = 0; i < taille; i++) {
			if (ap.getChildren().get(i).getId() != null) {
				if (ap.getChildren().get(i).getId().equals("C1")) {
					((ImageView)ap.getChildren().get(i)).setImage(getC1());
					cptCards++;
				}
				if (ap.getChildren().get(i).getId().equals("C2")) {
					((ImageView)ap.getChildren().get(i)).setImage(getC2());
					cptCards++;
				}
				if(cptCards == 2) {
					break;
				}
			}
		}
	}

	public void setAbattage() {
		miseAbattage = stackDebutTour - stack;
	}
	
	/**
	 * argent mise par le joueur depuis le debut de la main
	 */
	public double getMiseMain() {
		return miseAbattage;
	}	

}
