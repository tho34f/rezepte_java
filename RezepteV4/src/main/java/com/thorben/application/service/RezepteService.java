package com.thorben.application.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.dropbox.core.DbxException;
import com.thorben.application.data.Category;
import com.thorben.application.data.Konstanten;
import com.thorben.application.data.Rezept;
import com.thorben.application.layout.SecondScreen;
import com.thorben.application.queries.ReadWriteTextFile;

import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class RezepteService {
	
	private static final DierkesLooger LOGGER = new DierkesLooger();
	
	private static File file;
	private static List<Category> categorieList = new ArrayList<>();
	private static List<Rezept> rezepteList = new ArrayList<>();
	private static List<Rezept> allRezeptFromDB = new ArrayList<>();
	private static List<Rezept> allRezeptFromFile = new ArrayList<>();
	
	public String createLabelText(List<String> list) {
		StringBuilder labelText = new StringBuilder();
		
		for(int i = 0; i < list.size() - 1; i++){
			labelText.append(" " + (i + 1) + ")" + " " + list.get(i));
		}
		
		return labelText.toString();
	}
	
	public static List<Category> fillCategory(DropboxWizard dbw) {
		LOGGER.writeInfoLog("Start fillCategory()", RezepteService.class.getName());
		List<Category> categorieList = new ArrayList<>();
		try {
			categorieList = dbw.listDropboxFolders("");
		} catch (DbxException e) {
			LOGGER.writeExceptionLog(e, "Error while execute fillCategory()", RezepteService.class.getName());
			LayoutService.createAlert(AlertType.ERROR, Konstanten.ERRORDIALOG, Konstanten.LOOKERROR, Konstanten.CREATE_CATEGORY);
		}
		
		return categorieList;
	}
	
	public static List<Rezept> fillRezepte(Category categoryChoose, DropboxWizard dbw) { 
		LOGGER.writeInfoLog("Start fillRezepte()", RezepteService.class.getName());
		List<Rezept> rezepteList = new ArrayList<>();
		try {
			if(categoryChoose != null) {
				rezepteList = dbw.listDropboxRezepte(categoryChoose.getName());
				dbw.downloadFromDropbox("", "rezepte", true);
				allRezeptFromFile = ReadWriteTextFile.readFromFile();
				updateRezepteFromFile(rezepteList);
			}
		} catch (DbxException | IOException e1) {
			LOGGER.writeExceptionLog(e1, "Error while execute fillRezepte()", RezepteService.class.getName());
			LayoutService.createAlert(AlertType.ERROR, Konstanten.ERRORDIALOG, Konstanten.LOOKERROR, Konstanten.CREATE_REZEPT);
		}
		
		return rezepteList;
	}
	
	public static void updateRezepteFromFile(List<Rezept> rezepteList) {
		for(Rezept rezept : rezepteList) {
			for(Rezept fromFile : getAllRezeptFromDb()) {
				if(rezept.getTitle().equals(fromFile.getTitle())) {
					rezept.setRating(fromFile.getRating());
			    	rezept.setTime(fromFile.getTime());
			    	rezept.setDifficultyLevel(fromFile.getDifficultyLevel());
			    	break;
			    }
		    }
		}
	}
	
	public static Stage startEvaluation(Image rezeptImage, Rezept rezept){
		
		SecondScreen screen = new SecondScreen("Bitte geben Sie eine Bewertung zwischen 1 und 5 Punkten ein:","Bewertung","Bewertung best�tigen");
		Stage evaluationStage = LayoutService.createStage(rezeptImage, "Rezeptbewertung " + rezept.getTitle(), new Scene(screen.getPane(), 375,120));
		evaluationStage.show();
		
		screen.getConfirm().setOnAction(e-> {
			doEvaluation(evaluationStage, screen.getTextField(), rezept);
			screen.getTextField().clear();
		});
		
		return evaluationStage;
	}
	
	public static Stage startCreateCategory(Image rezeptImage, DropboxWizard dbw){
		
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
	
	public static Stage startCreateUpload(Image rezeptImage, DropboxWizard dbw){
		
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
	
	public static void actionBeforeClose(DropboxWizard dbw, Stage evalStage, String userPath) {
		boolean success = false;
		try {
			File metadataPath = new File(userPath + Konstanten.TXT);
			if(metadataPath.exists()) {
				ReadWriteTextFile.writeToFile();
				success = dbw.uploadToDropbox(Konstanten.TXT);
				success = success && metadataPath.delete();
			}
			if(evalStage != null && success) {
				evalStage.close();
			}
		} catch (IOException | DbxException e) {
			LayoutService.createAlert(AlertType.ERROR, Konstanten.ERRORDIALOG, Konstanten.LOOKERROR, Konstanten.ERRORMESSAGE);
		}
		if(!success) {
			LayoutService.createAlert(AlertType.ERROR, Konstanten.ERRORDIALOG, Konstanten.LOOKERROR, Konstanten.ERRORMESSAGE);
		}
	}
	
	public static void doEvaluation(Stage evaluationStage, TextField evalField, Rezept rezept){
		double bewertung = Double.parseDouble(evalField.getText());
		
		for(Rezept rzt : getAllRezeptFromDb()) {
			if(rzt.getTitle().equals(rezept.getTitle())) {
				rzt.setRating((rezept.getRating() + bewertung)/2);
				getRezepteList().remove(rezept);
				getRezepteList().add(rzt);
			}
		}
		
		evaluationStage.close();
	}
	
	public static void downloadRezept(String category, String rezept, DropboxWizard dbw, String userPath) {
		try {
			dbw.downloadFromDropbox(category, rezept, false);
			LayoutService.createAlert(AlertType.INFORMATION, Konstanten.INFORMATIONDIALOG, Konstanten.LOOKINFORMATION,
						"Das Rezept wurde erfolgreich in den Ordner " + userPath + "heruntergeladen!");
		} catch (DbxException | IOException e1) {
			LayoutService.createAlert(AlertType.ERROR, Konstanten.ERRORDIALOG, Konstanten.LOOKERROR, Konstanten.ERRORMESSAGE);
		}
	}
	
	public static void getAccountInformation(DropboxWizard dbw) {
		try {
			LayoutService.createAlert(AlertType.INFORMATION, Konstanten.INFORMATIONDIALOG, Konstanten.LOOKINFORMATION,
					dbw.getAccountInformation());
		} catch (DbxException e1) {
			LayoutService.createAlert(AlertType.ERROR, Konstanten.ERRORDIALOG, Konstanten.LOOKERROR, Konstanten.ERRORMESSAGE);
		}
	}

	public static List<Category> getCategorieList() {
		return categorieList;
	}

	public static void setCategorieList(List<Category> categories) {
		categorieList = categories;
	}

	public static List<Rezept> getRezepteList() {
		return rezepteList;
	}

	public static void setRezepteList(List<Rezept> rezepte) {
		rezepteList = rezepte;
	}

	public static List<Rezept> getAllRezeptFromDb() {
		return allRezeptFromDB;
	}

	public static void setAllRezeptFromDb(List<Rezept> rezeptFromDb) {
		allRezeptFromDB = rezeptFromDb;
	}
	
	public static  List<Rezept> getAllRezeptFromFile() {
		return allRezeptFromFile;
	}

	public static void setAllRezeptFromFile(List<Rezept> rezeptFromFile) {
		allRezeptFromFile = rezeptFromFile;
	}
}