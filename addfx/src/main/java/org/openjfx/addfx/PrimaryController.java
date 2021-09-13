package org.openjfx.addfx;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import modele.Combinaison;

public class PrimaryController  {
	    
    @FXML
    private TextField niveauStp;
    
    @FXML
    private void switchTo6Joueurs() throws IOException {
        App.setRoot("sixjoueurs");
    } 
    
    @FXML
    private void test() {
    	Combinaison.test();
    }
    /**
     * TODO
     * nbjoueurs, dispo table
     * stack joueurs
     * profil joueur
     * tour des actions preflop flop
     * 
     * remplissage des cartes du joueur
     * 
     * table updatable
     * 
     * ranges..
     */

}
