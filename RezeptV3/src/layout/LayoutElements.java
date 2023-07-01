package layout;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class LayoutElements {
	
	//all Labels
	public static Label chooseCategory = new Label("Bitte wählen Sie eine Kategorie:");
	public static Label chooseRezept = new Label("Bitte wählen Sie ein Rezept:");
	public static Label welcome = new Label("Herzlich Wilkommen zu deiner Rezeptverwaltung!");
	public static Label rezeptRating = new Label("Bewertung:");
	public static Label rezeptTime = new Label("Dauer:");
	public static Label rezeptDifficultyLevel = new Label("Schwirigkeitsgrad:");
	public static Label rezeptName = new Label("Rezept-Name:");
	public static Label moreFunczions = new Label("Weitere Funktionen:");
	
	//All TextFields
	public static TextField findCategory = new TextField();
	public static TextField findRezept = new TextField();
	
	//All Buttons
	public static Button buttonUpload = new Button("Rezept hochladen");
	public static Button buttonDownload = new Button("Rezept downloaden");
	public static Button btnEvaluate = new Button("Rezept Bewerten");
	public static Button btnCreateCategory = new Button("Kategorie erstellen");
	public static Button btnFindCategory = new Button("Kategorie suchen!");
	public static Button btnFindRezept = new Button("Rezept suchen!");
	
	//All Images
	public static Image account = new Image("profilePicture.png");
	public static Image rezeptImage = new Image("rezepte.jpg");
	public static Image exit = new Image("exit.png");
	
	//Menu and MenuItems
	public static Menu menuView = new Menu("View");
	public static Menu menuEdit = new Menu("Edit");
	public static MenuItem accountInfo = new MenuItem("Account-Informationen anzeigen!");
	public static MenuItem closeApplication = new MenuItem("Programm beenden");
	public static Menu menuFile = new Menu("File");
	public static MenuBar menuBar = new MenuBar();
	
	//Second Stage
	public static Stage secondStage;
}
