package com.thorben.application.layout;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LayoutElements {
	
	//all Labels
	private Label chooseCategory = new Label("Bitte wählen Sie eine Kategorie:");
	private Label chooseRezept = new Label("Bitte wählen Sie ein Rezept:");
	private Label welcome = new Label("Herzlich Wilkommen zu deiner Rezeptverwaltung!");
	private Label rezeptRating = new Label("Bewertung:");
	private Label rezeptTime = new Label("Dauer:");
	private Label rezeptDifficultyLevel = new Label("Schwirigkeitsgrad:");
	private Label rezeptName = new Label("Rezept-Name:");
	private Label moreFunczions = new Label("Weitere Funktionen:");
	
	//All TextFields
	private TextField findCategory = new TextField();
	private TextField findRezept = new TextField();
	
	//All Buttons
	private Button buttonUpload = new Button("Rezept hochladen");
	private Button buttonDownload = new Button("Rezept downloaden");
	private Button btnEvaluate = new Button("Rezept Bewerten");
	private Button btnCreateCategory = new Button("Kategorie erstellen");
	private Button btnFindCategory = new Button("Kategorie suchen!");
	private Button btnFindRezept = new Button("Rezept suchen!");
	
	//All Images
	private Image account = new Image("file:profilePicture.png");
	private Image rezeptImage = new Image("file:rezepte.jpg");
	private Image exit = new Image("file:exit.png");
	
	//Menu and MenuItems
	private Menu menuView = new Menu("View");
	private Menu menuEdit = new Menu("Edit");
	private MenuItem accountInfo = new MenuItem("Account-Informationen anzeigen!");
	private MenuItem closeApplication = new MenuItem("Programm beenden");
	private Menu menuFile = new Menu("File");
	private MenuBar menuBar = new MenuBar();
	
	//Second Stage
	private Stage secondStage;

}
