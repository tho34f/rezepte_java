package service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.dropbox.core.DbxException;

import data.Category;
import data.Konstanten;
import data.Rezept;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import layout.LayoutService;
import layout.SecondScreen;
import querries.ReadWriteTextFile;
import querries.RezeptQuerries;

public class RezepteService {
	
	private  String userPath;
	private File file;
	private List<Category> categorieList = new ArrayList<>();
	private List<Rezept> rezepteList = new ArrayList<>();
	private List<Rezept> allRezeptFromDB = new ArrayList<>();
	private static List<Rezept> allRezeptFromFile = new ArrayList<>();
	
	public RezepteService() {
		String userName = System.getProperty("user.name");
        this.userPath = "C:/Users/" + userName + "/Documents/";
	}
	
	public String createLabelText(List<String> list) {
		StringBuilder labelText = new StringBuilder();
		
		for(int i = 0; i < list.size() - 1; i++){
			labelText.append(" " + (i + 1) + ")" + " " + list.get(i));
		}
		
		return labelText.toString();
	}
	
	public List<Category> fillCategory(DropboxWizard dbw) { 
		try {
			this.setCategorieList(dbw.listDropboxFolders(""));
		} catch (DbxException e) {
			LayoutService.createAlert(AlertType.ERROR, Konstanten.ERRORDIALOG, Konstanten.LOOKERROR, Konstanten.CREATE_CATEGORY);
		}
		
		return getCategorieList();
	}
	
	public List<Rezept> fillRezepte(Category categoryChoose, DropboxWizard dbw) { 
		try {
			if(categoryChoose != null) {
				this.setRezepteList(dbw.listDropboxRezepte(categoryChoose.getName()));
				dbw.downloadFromDropbox("", "rezepte", true);
				allRezeptFromFile = ReadWriteTextFile.readFromFile();
				if(this.getAllRezeptFromDb().isEmpty()) {
					this.setAllRezeptFromDb(RezeptQuerries.getAllRezepte());
				}
				this.updateRezepteFromFile(this.getRezepteList());
			}
		} catch (DbxException | IOException e1) {
			LayoutService.createAlert(AlertType.ERROR, Konstanten.ERRORDIALOG, Konstanten.LOOKERROR, Konstanten.CREATE_REZEPT);
		}
		
		return this.getRezepteList();
	}
	
	public void updateRezepteFromFile(List<Rezept> rezepteList) {
		for(Rezept rezept : rezepteList) {
			for(Rezept fromFile : this.getAllRezeptFromDb()) {
				if(rezept.getTitle().equals(fromFile.getTitle())) {
					rezept.setRating(fromFile.getRating());
			    	rezept.setTime(fromFile.getTime());
			    	rezept.setDifficultyLevel(fromFile.getDifficultyLevel());
			    	break;
			    }
		    }
		}
	}
	
	public boolean updateDb() {
		return RezeptQuerries.updateAll(allRezeptFromDB);
	}
	
	public Stage startEvaluation(Image rezeptImage, Rezept rezept){
		
		SecondScreen screen = new SecondScreen("Bitte geben Sie eine Bewertung zwischen 1 und 5 Punkten ein:","Bewertung","Bewertung best�tigen");
		Stage evaluationStage = LayoutService.createStage(rezeptImage, "Rezeptbewertung " + rezept.getTitle(), new Scene(screen.getPane(), 375,120));
		evaluationStage.show();
		
		screen.getConfirm().setOnAction(e-> {
			doEvaluation(evaluationStage, screen.getTextField(), rezept);
			screen.getTextField().clear();
		});
		
		return evaluationStage;
	}
	
	public Stage startCreateCategory(Image rezeptImage, DropboxWizard dbw){
		
		SecondScreen screen = new SecondScreen("Bitte geben Sie einen Namen f�r die neue Kategorie ein:","Kategory-Name","Kategorie erstellen");
		Stage evaluationStage = LayoutService.createStage(rezeptImage, "Neue Kategorie erstellen", new Scene(screen.getPane(), 375,120));
		evaluationStage.show();
		
		screen.getConfirm().setOnAction(e-> {
			try {
				dbw.createFolder(screen.getTextField().getText());
			} catch (DbxException e1) {
				LayoutService.createAlert(AlertType.ERROR, Konstanten.ERRORDIALOG, Konstanten.LOOKERROR, Konstanten.ERRORMESSAGE);
			}
			LayoutService.createAlert(AlertType.INFORMATION, Konstanten.INFORMATIONDIALOG, Konstanten.LOOKINFORMATION,
					"Die Kategorie wurde erfolgreich erstellt.");
			screen.getTextField().clear();
		});
		
		return evaluationStage;
	}
	
	public Stage startCreateUpload(Image rezeptImage, DropboxWizard dbw){
		
		List<Category> categorieListForScreen = fillCategory(dbw);
		
		SecondScreen screen = new SecondScreen("Bitte wählen Sie eine Rezept-Datei aus:",
				"Bitte wählen Sie eine Kategorie aus:","Rezept-Auswahl","Ein Rezeptdatei suchen", "Rezept-Datei hochladen", categorieListForScreen);
		Stage evaluationStage = LayoutService.createStage(rezeptImage, "Hochladen einer Rezeptdatei", new Scene(screen.getPane(), 375,175));
		evaluationStage.show();
		
		screen.getChooseBtn().setOnAction(e->{
			file = ReadWriteTextFile.createFile(evaluationStage);
			if(file != null) {
				screen.getConfirm().setDisable(false);
				screen.getTextField().setText(file.getPath());
			}
		});
		
		screen.getConfirm().setOnAction(e ->{
			try {
				if(file != null) {
					dbw.uploadToDropbox(file.getPath());
					screen.getConfirm().setDisable(true);
					screen.getTextField().clear();
					file = null;
				}
				
			} catch (DbxException | IOException e1) {
				LayoutService.createAlert(AlertType.ERROR, Konstanten.ERRORDIALOG, Konstanten.LOOKERROR, Konstanten.ERROR_REZEPT_UPLOAD);
			}
		});
		
		return evaluationStage;
	}
	
	public void actionBeforeClose(DropboxWizard dbw, Stage evalStage) {
		try {
			File metadataPath = new File(this.userPath + Konstanten.TXT);
			if(metadataPath.exists()) {
				this.updateDb();
				ReadWriteTextFile.writeToFile();
				dbw.uploadToDropbox(Konstanten.TXT);
				metadataPath.delete();
			}
			if(evalStage != null) {
				evalStage.close();
			}
		} catch (IOException | DbxException e) {
			LayoutService.createAlert(AlertType.ERROR, Konstanten.ERRORDIALOG, Konstanten.LOOKERROR, Konstanten.ERRORMESSAGE);
		}
	}
	
	public void doEvaluation(Stage evaluationStage, TextField evalField, Rezept rezept){
		double bewertung = Double.parseDouble(evalField.getText());
		
		for(Rezept rzt : this.getAllRezeptFromDb()) {
			if(rzt.getTitle().equals(rezept.getTitle())) {
				rzt.setRating((rezept.getRating() + bewertung)/2);
				RezeptQuerries.updateRezeptRating(rzt);
				getRezepteList().remove(rezept);
				getRezepteList().add(rzt);
			}
		}
		
		evaluationStage.close();
	}
	
	public void downloadRezept(String category, String rezept, DropboxWizard dbw) {
		try {
			dbw.downloadFromDropbox(category, rezept, false);
			LayoutService.createAlert(AlertType.INFORMATION, Konstanten.INFORMATIONDIALOG, Konstanten.LOOKINFORMATION,
						"Das Rezept wurde erfolgreich in den Ordner " + this.userPath + "heruntergeladen!");
		} catch (DbxException | IOException e1) {
			LayoutService.createAlert(AlertType.ERROR, Konstanten.ERRORDIALOG, Konstanten.LOOKERROR, Konstanten.ERRORMESSAGE);
		}
	}
	
	public void getAccountInformation(DropboxWizard dbw) {
		try {
			LayoutService.createAlert(AlertType.INFORMATION, Konstanten.INFORMATIONDIALOG, Konstanten.LOOKINFORMATION,
					dbw.getAccountInformation());
		} catch (DbxException e1) {
			LayoutService.createAlert(AlertType.ERROR, Konstanten.ERRORDIALOG, Konstanten.LOOKERROR, Konstanten.ERRORMESSAGE);
		}
	}

	public List<Category> getCategorieList() {
		return categorieList;
	}

	public void setCategorieList(List<Category> categorieList) {
		this.categorieList = categorieList;
	}

	public List<Rezept> getRezepteList() {
		return rezepteList;
	}

	public void setRezepteList(List<Rezept> rezepteList) {
		this.rezepteList = rezepteList;
	}

	public List<Rezept> getAllRezeptFromDb() {
		return allRezeptFromDB;
	}

	public void setAllRezeptFromDb(List<Rezept> allRezeptFromDb) {
		this.allRezeptFromDB = allRezeptFromDb;
	}
	
	public static  List<Rezept> getAllRezeptFromFile() {
		return allRezeptFromFile;
	}

	public void setAllRezeptFromFile(List<Rezept> allRezeptFromFile) {
		this.allRezeptFromFile = allRezeptFromFile;
	}
}