package querries;

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

import data.Konstanten;
import data.Rezept;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import service.LayoutService;
import service.RezepteService;

public class ReadWriteTextFile {
	
	private ReadWriteTextFile() {
		throw new IllegalStateException("Utility class");
	}

	public static void writeToFile() {
		try(BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(createOutputStream(), StandardCharsets.UTF_8.name()))){
			bfw.write(Konstanten.HEADER + "\n");
			for(Rezept rzt : RezepteService.getAllRezeptFromFile()) {
				bfw.write(rzt.writeTxt() + "\n");
			}
		}catch(IOException e) {
			LayoutService.createAlert(AlertType.ERROR, Konstanten.ERRORDIALOG, Konstanten.LOOKERROR,
					Konstanten.ERRORMESSAGE);
		}
	}
	
	public static List<Rezept> readFromFile() {
		String[] rezeptMetaDataSplitted = null;
		List<Rezept> allRezepte = new ArrayList<>();
		
		try(BufferedReader bfr = new BufferedReader(new InputStreamReader(createInputStream(),StandardCharsets.UTF_8.name()))){
			String rezeptMetaData = "";
			while ((rezeptMetaData = bfr.readLine()) != null) {
				rezeptMetaDataSplitted = rezeptMetaData.split(";");
		    	if(!rezeptMetaDataSplitted[0].equals("name")) {
		    		Rezept rezept = new Rezept(rezeptMetaDataSplitted[0], rezeptMetaDataSplitted[1], Long.parseLong(rezeptMetaDataSplitted[2]), 
		    				Double.parseDouble(rezeptMetaDataSplitted[3]));
		    		allRezepte.add(rezept);
		    	}
		    }
		}catch(IOException e) {
			LayoutService.createAlert(AlertType.ERROR, Konstanten.ERRORDIALOG, Konstanten.LOOKERROR,
					Konstanten.ERRORMESSAGE);
		}
		
		return allRezepte;
	}
	
	public static File createFile(Stage stage) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("W�hle eine Datei");
		fileChooser.getExtensionFilters().addAll(
			    new FileChooser.ExtensionFilter("All Text", "*.*"),
			    new FileChooser.ExtensionFilter("PDF", "*.pdf"),
			    new FileChooser.ExtensionFilter("TXT", "*.txt")
		);
		
		return fileChooser.showOpenDialog(stage);
	}
	
	public static FileInputStream createInputStream() throws FileNotFoundException {
		String path = getUserPath() + Konstanten.TXT;
		return new FileInputStream(new File(path));
	}
	
	public static FileOutputStream createOutputStream() throws FileNotFoundException {
		String path = getUserPath() + Konstanten.TXT;
		return new FileOutputStream(new File(path));
	}
	public static String getUserPath() {
		String userName = System.getProperty("user.name");
	    return "C:/Users/" + userName + "/Documents/";
	}
}
