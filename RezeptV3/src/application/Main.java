package application;
	
import java.util.ArrayList;
import java.util.List;

import data.Category;
import data.Rezept;
import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import service.DropboxWizard;
import service.RezepteService;


public class Main extends Application {
	
	private List<Rezept> rezepteList = new ArrayList<>();
	private static DropboxWizard dbw = new DropboxWizard();
	
	private static RezepteService service = new RezepteService();
	
	private ListView<Category> categorien = new ListView<>();
	private ListView<Rezept> rezepte = new ListView<>();
	private Rezept rezept = null;
	private Category categoryChoose = null;
	
	private Label chooseCategory = new Label("Bitte wählen Sie eine Kategorie:");
	private Label chooseRezept = new Label("Bitte wählen Sie ein Rezept:");
	private Label welcome = new Label("Herzlich Wilkommen zu deiner Rezeptverwaltung!");
	private static Label rezeptRating = new Label("Bewertung:");
	private static Label rezeptTime = new Label("Dauer:");
	private static Label rezeptDifficultyLevel = new Label("Schwirigkeitsgrad:");
	private static Label rezeptName = new Label("Rezept-Name:");
	private Label moreFunczions = new Label("Weitere Funktionen:");
	private TextField findCategory = new TextField();
	private TextField findRezept = new TextField();
	
	private Button buttonUpload = new Button("Rezept hochladen");
	private static Button buttonDownload = new Button("Rezept downloaden");
	private static Button btnEvaluate = new Button("Rezept Bewerten");
	private Button btnCreateCategory = new Button("Kategorie erstellen");
	private Button btnFindCategory = new Button("Kategorie suchen!");
	private Button btnFindRezept = new Button("Rezept suchen!");
	
	private Image account = new Image("profilePicture.png");
	private Image rezeptImage = new Image("rezepte.jpg");
	private Image exit = new Image("exit.png");
	
	private Menu menuView = new Menu("View");
	private Menu menuEdit = new Menu("Edit");
	private MenuItem accountInfo = new MenuItem("Account-Informationen anzeigen!");
	private MenuItem closeApplication = new MenuItem("Programm beenden");
	private Menu menuFile = new Menu("File");
	private MenuBar menuBar = new MenuBar();
	private Stage secondStage;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		BorderPane root = new BorderPane();
		createMenuBar();
		createLayout(root);
		
		List<Category> categorieList = service.fillCategory(dbw);
		categorien.getItems().addAll(categorieList);
		
		categorien.getSelectionModel().getSelectedItems().addListener((ListChangeListener<? super Category>)e ->{
			categoryChoose = categorien.getSelectionModel().getSelectedItem();
			setRezepteList(service.fillRezepte(categoryChoose, dbw));
			rezepte.getItems().setAll(getRezepteList());
		});
		
		rezepte.getSelectionModel().getSelectedItems().addListener((ListChangeListener<? super Rezept>)e -> {
			this.setRezept(rezepte.getSelectionModel().getSelectedItem());
			if(getRezept() != null) {
				chooseRezept();
			} else {
				btnEvaluate.setDisable(true);
				buttonDownload.setDisable(true);
			}
		});
		
		btnEvaluate.setOnAction(e -> {
			secondStage = service.startEvaluation(rezeptImage, getRezept());
			for(Rezept rezeptFromList : service.getRezepteList()) {
				if(rezeptFromList.getTitle().equals(getRezept().getTitle())){
					this.setRezept(rezeptFromList);
					chooseRezept();
				}
			};
		});
		btnCreateCategory.setOnAction(e -> secondStage= service.startCreateCategory(rezeptImage, dbw));
		buttonUpload.setOnAction(e -> secondStage= service.startCreateUpload(rezeptImage, dbw));
		accountInfo.setOnAction(e -> service.getAccountInformation(dbw));
		buttonDownload.setOnAction(e -> service.downloadRezept(categoryChoose.getName(), getRezept().getTitle(), dbw));
		btnFindCategory.setOnAction(e -> {
			String eingabe = findCategory.getText();
			if(eingabe != null && !eingabe.isEmpty()) {
				for(Category category : service.getCategorieList()) {
					if(eingabe.equals(category.getName())) {
						categorien.getSelectionModel().select(service.getCategorieList().indexOf(category));
						categoryChoose = category;
						setRezepteList(service.fillRezepte(category, dbw));
						rezepte.getItems().setAll(getRezepteList());
					}
				}
			}
			findCategory.clear();
		});
		btnFindRezept.setOnAction(e -> {
			String eingabe = findRezept.getText();
			if(eingabe != null && !eingabe.isEmpty()) {
				for(Rezept re : getRezepteList()) {
					if(eingabe.equals(re.getTitle())) {
						rezepte.getSelectionModel().select(getRezepteList().indexOf(re));
						this.setRezept(re);
						chooseRezept();
					}
				}
			}
			findRezept.clear();
		});
		
		closeApplication.setOnAction(e -> {
			service.actionBeforeClose(dbw, secondStage);
			System.exit(0);
		});
		
		//Parent root = FXMLLoader.load(getClass().getResource("Rezepte.fxml"));
		Scene scene = new Scene(root,1000,600);
		
		primaryStage.getIcons().add(rezeptImage);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Rezeptverwaltung");
		primaryStage.show();
		primaryStage.setOnCloseRequest(e -> service.actionBeforeClose(dbw, secondStage));

	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void createMenuBar() {
		menuBar.setPrefWidth(1000);
		menuBar.getMenus().addAll(menuFile, menuEdit, menuView);
		accountInfo.setGraphic(service.createImageView(account));
		closeApplication.setGraphic(service.createImageView(exit));
		menuFile.getItems().addAll(accountInfo, closeApplication);
	}
	
	public void createLayout(BorderPane root) {
		Insets padding = new Insets(15, 15, 15, 15);
		
		VBox topBorder = RezepteService.createVBOXWithAlignment(10, new Insets(0, 0,15, 0));
		welcome.setFont(Font.font("cambria", 24));
		topBorder.getChildren().addAll(menuBar, welcome);
		
		VBox leftBorder = service.createVBOX(10, padding);
		categorien.setPrefWidth(200);
		HBox hLeftBorder = RezepteService.createHBOX(10);
		findCategory.setPromptText("Kategoriename");
		hLeftBorder.getChildren().addAll(findCategory,btnFindCategory);
		leftBorder.getChildren().addAll(chooseCategory,categorien,hLeftBorder);
		
		VBox centerBorder = service.createVBOX(10, padding);
		HBox hCenterBorder = RezepteService.createHBOX(10);
		findRezept.setPromptText("Rezeptname");
		hCenterBorder.getChildren().addAll(findRezept,btnFindRezept);
		centerBorder.getChildren().addAll(chooseRezept, rezepte, hCenterBorder);
		
		HBox borderBottom = service.createHBOX(10, padding);
		borderBottom.getChildren().addAll(moreFunczions, btnCreateCategory,buttonUpload);
		
		VBox rightBorder = service.createVBOX(10, padding);
		HBox lineButton = service.createHBOX(10, padding);
		rightBorder.setAlignment(Pos.CENTER);
		buttonDownload.setDisable(true);
		btnEvaluate.setDisable(true);
		lineButton.getChildren().addAll(buttonDownload,btnEvaluate );
		rightBorder.getChildren().addAll(rezeptName, rezeptRating, rezeptTime, rezeptDifficultyLevel, lineButton);
		
		root.setLeft(leftBorder);
		root.setCenter(centerBorder);
		root.setRight(rightBorder);
		root.setBottom(borderBottom);
		root.setTop(topBorder);
	}
	
	public void chooseRezept() {
		rezeptName.setText("Name: " + getRezept().getTitle());
		rezeptRating.setText("Bewertung: " + getRezept().getRating());
		if(getRezept().getDifficultyLevel() != null) {
			rezeptDifficultyLevel.setText("Schwirigkeitsgrad: " + getRezept().getDifficultyLevel());
		} else {
			rezeptDifficultyLevel.setText("Schwirigkeitsgrad: -");
		}
		
		if(getRezept().getTime() != 0) {
			rezeptTime.setText("Dauer: " + getRezept().getTime() + " Minuten");
		} else {
			rezeptTime.setText("Dauer: -");
		}
		
		btnEvaluate.setDisable(false);
		buttonDownload.setDisable(false);
	}

	public List<Rezept> getRezepteList() {
		return rezepteList;
	}

	public void setRezepteList(List<Rezept> rezepteList) {
		this.rezepteList = rezepteList;
	}

	public Rezept getRezept() {
		return rezept;
	}
	
	public void setRezept(Rezept rezept) {
		this.rezept = rezept;
	}
}