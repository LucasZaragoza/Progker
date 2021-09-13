package org.openjfx.addfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Commenter new Apprenant dans cette classe pour ne pas créer d'historique de niveau
 * 
 * supprimer les splitPane
 * mettre en vert les resultat juste, en rouge les faux 
 * images pour les etapes de la recette 
 * design
 * 800*600 partout
 * etapes
 * 
 ******** SI ON A LE TEMPS ********
 * adapter la section indice, pour nous dire par exemple si la réponse est plus ou moins que ce que l'on cherche 
 * faire cours et indice (perso)
 */

public class App extends Application {

    private static Scene scene;
    private static Stage stage;
    
    
    @Override
    public void start(Stage stage1) throws IOException {
        scene = new Scene(loadFXML("primary"), 800, 600);
        stage = stage1;
        //stage.setMaximized(true);
        //stage.setFullScreen(true);
        stage.setTitle("OP Prog");
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
    
    public static Stage getStage() {
    	return stage;
    }
    
    public static Scene getScene() {
    	return scene;
    }

}