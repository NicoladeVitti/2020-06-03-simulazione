/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.Model;
import it.polito.tdp.PremierLeague.model.Opposite;
import it.polito.tdp.PremierLeague.model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnTopPlayer"
    private Button btnTopPlayer; // Value injected by FXMLLoader

    @FXML // fx:id="btnDreamTeam"
    private Button btnDreamTeam; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="txtGoals"
    private TextField txtGoals; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {

    	txtResult.clear();
    	
    	String gol = this.txtGoals.getText();
    	Double golFatti;
    	
    	if(gol==""){
    		this.txtResult.setText("ERRORE: non hai inserito il numero minimo di gol");
    		return;
    	}
    	
    	try {
    		golFatti = Double.parseDouble(gol);
    	}
    	catch(NumberFormatException e) {
    		this.txtResult.setText("ERRORE: formato del numero minimo di gol non corretto");
    		return;
    	}
    	
    	model.creaGrafo(golFatti);
    	txtResult.appendText("#ARCHI: "+model.getNumArchi()+"\n");
    	txtResult.appendText("#VERTICI: "+model.getNumVertici()+"\n\n");
    	
    }

    @FXML
    void doDreamTeam(ActionEvent event) {
    	
    	String gio = this.txtK.getText();
    	Integer giocatori;
    	
    	if(gio==""){
    		this.txtResult.setText("ERRORE: non hai inserito il numero di giocatori");
    		return;
    	}
    	
    	try {
    		giocatori = Integer.parseInt(gio);
    	}
    	catch(NumberFormatException e) {
    		this.txtResult.setText("ERRORE: formato del numero di giocatori non corretto");
    		return;
    	}
    	
    	List<Player> result = model.dreamTeam(giocatori);
    	txtResult.appendText("DREAM TEAM:\n");
    	
    	for(Player p : result) {
    		this.txtResult.appendText(p.toString()+"\n");
    	}
    	
    	
    }

    @FXML
    void doTopPlayer(ActionEvent event) {
    	
    	txtResult.appendText("TOP PLAYER\n");
    	txtResult.appendText(model.topPlayer().toString()+"\n\n");
    	
    	List<Opposite> l = model.giocatoriBattuti();
    	for(Opposite o : l) {
    		txtResult.appendText(o.toString() +"\n");
    	}
    	txtResult.appendText("\n\n");
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnTopPlayer != null : "fx:id=\"btnTopPlayer\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnDreamTeam != null : "fx:id=\"btnDreamTeam\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtGoals != null : "fx:id=\"txtGoals\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
