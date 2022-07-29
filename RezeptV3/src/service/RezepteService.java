package service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.dropbox.core.DbxException;

import application.SecondScreen;
import data.Category;
import data.Rezept;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class RezepteService {
	
	private static final String ERRORDIALOG = "Error Dialog";
	private static final String INFORMATIONDIALOG = "Information Dialog";
	private static final String LOOKERROR = "Look, an Error Dialog";
	private static final String LOOKINFORMATION = "Look, an Information";
	private static final String ERRORMESSAGE = "Es ist ein Fehler aufgetreten. Bitte versuchen Sie es erneut!";
	private static final String TXT = "rezepte.txt";
	private static final String HEADER = "name;schwierigkeitsgrad;zeit;durchschnittsbewertung";
	private  String userPath;
	private File file;
	private List<Category> categorieList = new ArrayList<>();
	private List<Rezept> rezepteList = new ArrayList<>();
	private List<Rezept> allRezeptFromFile = new ArrayList<>();
	
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
			this.createAlert(AlertType.ERROR, this.getERRORDIALOG(), this.getLOOKERROR(),
					"Beim Erstellen der Kategorieliste ist etwas schiefgelaufen! Versuchen Sie es noch einmal!");
		}
		
		return getCategorieList();
	}
	
	public List<Rezept> fillRezepte(Category categoryChoose, DropboxWizard dbw) { 
		try {
			if(categoryChoose != null) {
				this.setRezepteList(dbw.listDropboxRezepte(categoryChoose.getName()));
				dbw.downloadFromDropbox("", "rezepte", true);
				this.readFromFile();
				this.updateRezepteFromFile(this.getRezepteList());
			}
		} catch (DbxException | IOException e1) {
			this.createAlert(AlertType.ERROR, this.getERRORDIALOG(), this.getLOOKERROR(),
					"Beim Erstellen der Rezeptliste ist etwas schiefgelaufen! Versuchen Sie es noch einmal!");
		}
		
		return this.getRezepteList();
	}
	
	public File createFile(Stage stage) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Wähle eine Datei");
		fileChooser.getExtensionFilters().addAll(
			    new FileChooser.ExtensionFilter("All Text", "*.*"),
			    new FileChooser.ExtensionFilter("PDF", "*.pdf"),
			    new FileChooser.ExtensionFilter("TXT", "*.txt")
		);
		
		return fileChooser.showOpenDialog(stage);
	}
	
	public void createAlert(AlertType type,String title, String headerText, String contentText) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
		alert.showAndWait();
	}
	
	public FileInputStream createInputStream() throws FileNotFoundException {
		String path = this.userPath + this.getTxt();
		return new FileInputStream(new File(path));
	}
	
	public FileOutputStream createOutputStream() throws FileNotFoundException {
		String path = this.userPath + this.getTxt();
		return new FileOutputStream(new File(path));
	}
	
	public void updateRezepteFromFile(List<Rezept> rezepteList) {
		for(Rezept rezept : rezepteList) {
			for(Rezept fromFile : this.getAllRezeptFromFile()) {
				if(rezept.getTitle().equals(fromFile.getTitle())) {
					rezept.setRating(fromFile.getRating());
			    	rezept.setTime(fromFile.getTime());
			    	rezept.setDifficultyLevel(fromFile.getDifficultyLevel());
			    	break;
			    }
		    }
		}
	}
	
	public void writeToFile() {
		try(BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(createOutputStream(), StandardCharsets.UTF_8.name()))){
			bfw.write(getHeader() + "\n");
			for(Rezept rzt : this.getAllRezeptFromFile()) {
				bfw.write(rzt.writeTxt() + "\n");
			}
		}catch(IOException e) {
			this.createAlert(AlertType.ERROR, this.getERRORDIALOG(), this.getLOOKERROR(),
					this.getErrormessage());
		}
	}
	
	public void readFromFile() {
		String[] rezeptMetaDataSplitted = null;
		
		try(BufferedReader bfr = new BufferedReader(new InputStreamReader(createInputStream(),StandardCharsets.UTF_8.name()))){
			String rezeptMetaData = "";
			while ((rezeptMetaData = bfr.readLine()) != null) {
				rezeptMetaDataSplitted = rezeptMetaData.split(";");
		    	if(!rezeptMetaDataSplitted[0].equals("name")) {
		    		Rezept rezept = new Rezept(rezeptMetaDataSplitted[0], rezeptMetaDataSplitted[1], Long.parseLong(rezeptMetaDataSplitted[2]), 
		    				Double.parseDouble(rezeptMetaDataSplitted[3]));
		    		this.getAllRezeptFromFile().add(rezept);
		    	}
		    }
		}catch(IOException e) {
			this.createAlert(AlertType.ERROR, this.getERRORDIALOG(), this.getLOOKERROR(),
					this.getErrormessage());
		}
	}
	
	public Stage startEvaluation(Image rezeptImage, Rezept rezept){
		
		SecondScreen screen = new SecondScreen("Bitte geben Sie eine Bewertung zwischen 1 und 5 Punkten ein:","Bewertung","Bewertung bestätigen");
		Stage evaluationStage = createStage(rezeptImage, "Rezeptbewertung " + rezept.getTitle(), new Scene(screen.getPane(), 375,120));
		evaluationStage.show();
		
		screen.getConfirm().setOnAction(e-> {
			doEvaluation(evaluationStage, screen.getTextField(), rezept);
			screen.getTextField().clear();
		});
		
		return evaluationStage;
	}
	
	public Stage startCreateCategory(Image rezeptImage, DropboxWizard dbw){
		
		SecondScreen screen = new SecondScreen("Bitte geben Sie einen Namen für die neue Kategorie ein:","Kategory-Name","Kategorie erstellen");
		Stage evaluationStage = createStage(rezeptImage, "Neue Kategorie erstellen", new Scene(screen.getPane(), 375,120));
		evaluationStage.show();
		
		screen.getConfirm().setOnAction(e-> {
			try {
				dbw.createFolder(screen.getTextField().getText());
			} catch (DbxException e1) {
				this.createAlert(AlertType.ERROR, this.getERRORDIALOG(), this.getLOOKERROR(),
						this.getErrormessage());
			}
			this.createAlert(AlertType.INFORMATION, this.getInformationdialog(), this.getLookinformation(),
					"Die Kategorie wurde erfolgreich erstellt.");
			screen.getTextField().clear();
		});
		
		return evaluationStage;
	}
	
	public Stage startCreateUpload(Image rezeptImage, DropboxWizard dbw){
		
		List<Category> categorieListForScreen = fillCategory(dbw);
		
		SecondScreen screen = new SecondScreen("Bitte wählen Sie eine Rezept-Datei aus:",
				"Bitte wählen Sie eine Kategorie aus:","Rezept-Auswahl","Ein Rezeptdatei suchen", "Rezept-Datei hochladen", categorieListForScreen);
		Stage evaluationStage = createStage(rezeptImage, "Hochladen einer Rezeptdatei", new Scene(screen.getPane(), 375,175));
		evaluationStage.show();
		
		screen.getChooseBtn().setOnAction(e->{
			file = createFile(evaluationStage);
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
				createAlert(AlertType.ERROR, getERRORDIALOG(), getLOOKERROR(),
						"Beim Upload des Rezeptes ist etwas schiefgelaufen! Versuchen Sie es noch einmal!");
			}
		});
		
		return evaluationStage;
	}
	
	public void actionBeforeClose(DropboxWizard dbw, Stage evalStage) {
		try {
			File metadataPath = new File(this.userPath + this.getTxt());
			if(metadataPath.exists()) {
				this.writeToFile();
				dbw.uploadToDropbox(this.getTxt());
				metadataPath.delete();
			}
			if(evalStage != null) {
				evalStage.close();
			}
		} catch (IOException | DbxException e) {
			this.createAlert(AlertType.ERROR, this.getERRORDIALOG(), this.getLOOKERROR(),
					this.getErrormessage());
		}
	}
	
	public void doEvaluation(Stage evaluationStage, TextField evalField, Rezept rezept){
		double bewertung = Double.parseDouble(evalField.getText());
		
		for(Rezept rzt : this.getAllRezeptFromFile()) {
			if(rzt.getTitle().equals(rezept.getTitle())) {
				rzt.setRating((rezept.getRating() + bewertung)/2);
			}
		}
		
		evaluationStage.close();
	}
	
	public ImageView createImageView(Image im) {
		ImageView iv = new ImageView(im);
		iv.setFitHeight(20);
		iv.setFitWidth(20);
		return iv;
	}
	
	public void downloadRezept(String category, String rezept, DropboxWizard dbw) {
		try {
			dbw.downloadFromDropbox(category, rezept, false);
			this.createAlert(AlertType.INFORMATION, this.getInformationdialog(), this.getLookinformation(),
						"Das Rezept wurde erfolgreich in den Ordner " + this.userPath + "heruntergeladen!");
		} catch (DbxException | IOException e1) {
			this.createAlert(AlertType.ERROR, this.getERRORDIALOG(), this.getLOOKERROR(),
					this.getErrormessage());
		}
	}
	
	public void getAccountInformation(DropboxWizard dbw) {
		try {
			this.createAlert(AlertType.INFORMATION, this.getInformationdialog(), this.getLookinformation(),
					dbw.getAccountInformation());
		} catch (DbxException e1) {
			this.createAlert(AlertType.ERROR, this.getERRORDIALOG(), this.getLOOKERROR(),
					this.getErrormessage());
		}
	}
	
	public VBox createVBOX(double spacing, Insets padding) {
		VBox vbox = new VBox();
		vbox.setSpacing(spacing);
		vbox.setPadding(padding);
		return vbox;
	}
	
	public static VBox createVBOXWithAlignment(double spacing, Insets padding) {
		VBox vbox = new VBox();
		vbox.setSpacing(spacing);
		vbox.setPadding(padding);
		vbox.setAlignment(Pos.CENTER);
		return vbox;
	}
	
	public static HBox createHBOX(double spacing) {
		HBox hbox = new HBox();
		hbox.setSpacing(spacing);
		return hbox;
	}
	
	public HBox createHBOX(double spacing, Insets padding) {
		HBox hbox = new HBox();
		hbox.setSpacing(spacing);
		hbox.setPadding(padding);
		return hbox;
	}
	
	public static Stage createStage(Image rezeptImage, String title, Scene scene) {
		Stage evaluationStage = new Stage();
		evaluationStage.setTitle(title);
		evaluationStage.getIcons().add(rezeptImage);
		evaluationStage.setScene(scene);
		
		return evaluationStage;
	}

	public String getERRORDIALOG() {
		return ERRORDIALOG;
	}

	public String getLOOKERROR() {
		return LOOKERROR;
	}

	public String getInformationdialog() {
		return INFORMATIONDIALOG;
	}

	public String getLookinformation() {
		return LOOKINFORMATION;
	}

	public String getTxt() {
		return TXT;
	}

	public String getErrormessage() {
		return ERRORMESSAGE;
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

	public static String getHeader() {
		return HEADER;
	}

	public List<Rezept> getAllRezeptFromFile() {
		return allRezeptFromFile;
	}

	public void setAllRezeptFromFile(List<Rezept> allRezeptFromFile) {
		this.allRezeptFromFile = allRezeptFromFile;
	}
}