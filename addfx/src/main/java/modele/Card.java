package modele;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.image.Image;

public class Card implements Comparable<Card>{
	
	private int value;
	private String couleur; //heart, diamond, club, spade
	private Image image;
	
	public Card (int valeur, String coul) {
		value = valeur;
		couleur = coul;
		setImage();
	}
	
	public Card (String s) {
		if (s.length() > 2) {
			throw new Error("Format carte invalide");
		} else {
			value = 0;
			couleur = s.substring(1);
			if (s.substring(0,1).equals("A") || s.substring(0,1).equals("a"))
				value = 14;
			if (s.substring(0,1).equals("K") || s.substring(0,1).equals("k"))
				value = 13;
			if (s.substring(0,1).equals("Q") || s.substring(0,1).equals("q"))
				value = 12;
			if (s.substring(0,1).equals("J") || s.substring(0,1).equals("j"))
				value = 11;
			if (s.substring(0,1).equals("T") || s.substring(0,1).equals("t"))
				value = 10;
			if (value == 0) {
				value = Integer.parseInt(s.substring(0,1));
			}
			setImage();
		}
	}
	
	public Card () {
		setImageNone();
	}
	
	private void setImage() {
		String nomFichier = "src/main/resources/images/cards/" + toString() + ".png";
		try {
			this.image = new Image(new FileInputStream(nomFichier));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void setImageNone() {
		String nomFichier = "src/main/resources/images/cards/none.png";
		try {
			this.image = new Image(new FileInputStream(nomFichier));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public int getValue() {
		return value;
	}
	
	public String getCouleur() {
		return couleur;
	}
	
	public Image getImage() {
		return image;
	}
	
	@Override
	public String toString() {
		String aRet = "";
		if (value > 9) {
			switch(value) {
			case 10 : aRet = "T"; break;
			case 11 : aRet = "J"; break;
			case 12 : aRet = "Q"; break;
			case 13 : aRet = "K"; break;
			case 14 : aRet = "A"; break;
			}
		} else {
			aRet = Integer.toString(value);
		}
		
		aRet += couleur;
		return aRet;
	}
	
	public static boolean formatCarteValide(String s) {
		return valeurCarteValide(s.substring(0,1)) && couleurCarteValide(s.substring(1));
	}
	
	private static boolean valeurCarteValide(String s) {
		return (s.equals("2") || s.equals("3") || s.equals("4") || s.equals("5")
				 || s.equals("6") || s.equals("7") || s.equals("8") || s.equals("9") 
				 || s.equals("T") || s.equals("J") || s.equals("Q") || s.equals("K") || s.equals("A")
				 || s.equals("t") || s.equals("j") || s.equals("q") || s.equals("k") || s.equals("a"));
	}
	
	private static boolean couleurCarteValide(String s) {
		return (s.equals("h") || s.equals("s") || s.equals("c") || s.equals("d"));
	}
	
	public int compareTo(Card otherCard) {
		return otherCard.value - value;	
	}
	
}
