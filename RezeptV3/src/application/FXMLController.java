package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.dropbox.core.DbxException;

import data.Category;
import data.Rezept;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import service.DropboxWizard;
import service.RezepteService;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;

public class FXMLController implements Initializable {

    @FXML
    private ComboBox<String> action;
    @FXML
    private ListView<Category> categorien;
    @FXML
	private ListView<Rezept> rezepte;
    
    private static RezepteService service = new RezepteService();
    private static DropboxWizard dbw = new DropboxWizard();
    
    private List<Category> categorieList = new ArrayList<>();
	private List<Rezept> rezepteList = new ArrayList<>();
	

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	try {
			categorieList = dbw.listDropboxFolders("");
		} catch (DbxException e) {
			categorien.setItems(null);
			service.createAlert(AlertType.ERROR, service.getERRORDIALOG(), service.getLOOKERROR(),
					"Beim Erstellen der Kategorieliste ist etwas schiefgelaufen! Versuchen Sie es noch einmal!");
		}
    	
    	action.getItems().addAll("Rezept hochladen", "Rezept downloaden", "Rezept bewerten", "Neue Kategorie erstellen");
		action.setValue("Rezept downloaden");
    }
    
    

}
