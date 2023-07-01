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
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import layout.LayoutElements;
import layout.LayoutService;
import service.DierkesLooger;
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
	
	private static final DierkesLooger LOOGER = new DierkesLooger();
	
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
				LayoutElements.btnEvaluate.setDisable(true);
				LayoutElements.buttonDownload.setDisable(true);
			}
		});
		
		LayoutElements.btnEvaluate.setOnAction(e -> {
			LayoutElements.secondStage = service.startEvaluation(LayoutElements.rezeptImage, getRezept());
			for(Rezept rezeptFromList : service.getRezepteList()) {
				if(rezeptFromList.getTitle().equals(getRezept().getTitle())){
					this.setRezept(rezeptFromList);
					chooseRezept();
				}
			}
		});
		LayoutElements.btnCreateCategory.setOnAction(e -> LayoutElements.secondStage= service.startCreateCategory(LayoutElements.rezeptImage, dbw));
		LayoutElements.buttonUpload.setOnAction(e -> LayoutElements.secondStage= service.startCreateUpload(LayoutElements.rezeptImage, dbw));
		LayoutElements.accountInfo.setOnAction(e -> service.getAccountInformation(dbw));
		LayoutElements.buttonDownload.setOnAction(e -> service.downloadRezept(categoryChoose.getName(), getRezept().getTitle(), dbw));
		LayoutElements.btnFindCategory.setOnAction(e -> {
			String eingabe = LayoutElements.findCategory.getText();
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
			LayoutElements.findCategory.clear();
		});
		LayoutElements.btnFindRezept.setOnAction(e -> {
			String eingabe = LayoutElements.findRezept.getText();
			if(eingabe != null && !eingabe.isEmpty()) {
				for(Rezept re : getRezepteList()) {
					if(eingabe.equals(re.getTitle())) {
						rezepte.getSelectionModel().select(getRezepteList().indexOf(re));
						this.setRezept(re);
						chooseRezept();
					}
				}
			}
			LayoutElements.findRezept.clear();
		});
		
		LayoutElements.closeApplication.setOnAction(e -> {
			service.actionBeforeClose(dbw, LayoutElements.secondStage);
			System.exit(0);
		});
		
		Scene scene = new Scene(root,1000,600);
		
		primaryStage.getIcons().add(LayoutElements.rezeptImage);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Rezeptverwaltung");
		primaryStage.show();
		primaryStage.setOnCloseRequest(e -> service.actionBeforeClose(dbw, LayoutElements.secondStage));

	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void createMenuBar() {
		LayoutElements.menuBar.setPrefWidth(1000);
		LayoutElements.menuBar.getMenus().addAll(LayoutElements.menuFile, LayoutElements.menuEdit, LayoutElements.menuView);
		LayoutElements.accountInfo.setGraphic(LayoutService.createImageView(LayoutElements.account));
		LayoutElements.closeApplication.setGraphic(LayoutService.createImageView(LayoutElements.exit));
		LayoutElements.menuFile.getItems().addAll(LayoutElements.accountInfo, LayoutElements.closeApplication);
	}
	
	public void createLayout(BorderPane root) {
		Insets padding = new Insets(15, 15, 15, 15);
		
		VBox topBorder = LayoutService.createVBOXWithAlignment(10, new Insets(0, 0,15, 0));
		LayoutElements.welcome.setFont(Font.font("cambria", 24));
		topBorder.getChildren().addAll(LayoutElements.menuBar, LayoutElements.welcome);
		
		VBox leftBorder = LayoutService.createVBOX(10, padding);
		categorien.setPrefWidth(200);
		HBox hLeftBorder = LayoutService.createHBOX(10);
		LayoutElements.findCategory.setPromptText("Kategoriename");
		hLeftBorder.getChildren().addAll(LayoutElements.findCategory,LayoutElements.btnFindCategory);
		leftBorder.getChildren().addAll(LayoutElements.chooseCategory,categorien,hLeftBorder);
		
		VBox centerBorder = LayoutService.createVBOX(10, padding);
		HBox hCenterBorder = LayoutService.createHBOX(10);
		LayoutElements.findRezept.setPromptText("Rezeptname");
		hCenterBorder.getChildren().addAll(LayoutElements.findRezept,LayoutElements.btnFindRezept);
		centerBorder.getChildren().addAll(LayoutElements.chooseRezept, rezepte, hCenterBorder);
		
		HBox borderBottom = LayoutService.createHBOX(10, padding);
		borderBottom.getChildren().addAll(LayoutElements.moreFunczions, LayoutElements.btnCreateCategory,LayoutElements.buttonUpload);
		
		VBox rightBorder = LayoutService.createVBOX(10, padding);
		HBox lineButton = LayoutService.createHBOX(10, padding);
		rightBorder.setAlignment(Pos.CENTER);
		LayoutElements.buttonDownload.setDisable(true);
		LayoutElements.btnEvaluate.setDisable(true);
		lineButton.getChildren().addAll(LayoutElements.buttonDownload,LayoutElements.btnEvaluate );
		rightBorder.getChildren().addAll(LayoutElements.rezeptName, LayoutElements.rezeptRating, LayoutElements.rezeptTime, LayoutElements.rezeptDifficultyLevel, lineButton);
		
		root.setLeft(leftBorder);
		root.setCenter(centerBorder);
		root.setRight(rightBorder);
		root.setBottom(borderBottom);
		root.setTop(topBorder);
	}
	
	public void chooseRezept() {
		LayoutElements.rezeptName.setText("Name: " + getRezept().getTitle());
		LayoutElements.rezeptRating.setText("Bewertung: " + getRezept().getRating());
		if(getRezept().getDifficultyLevel() != null) {
			LayoutElements.rezeptDifficultyLevel.setText("Schwirigkeitsgrad: " + getRezept().getDifficultyLevel());
		} else {
			LayoutElements.rezeptDifficultyLevel.setText("Schwirigkeitsgrad: -");
		}
		
		if(getRezept().getTime() != 0) {
			LayoutElements.rezeptTime.setText("Dauer: " + getRezept().getTime() + " Minuten");
		} else {
			LayoutElements.rezeptTime.setText("Dauer: -");
		}
		
		LayoutElements.btnEvaluate.setDisable(false);
		LayoutElements.buttonDownload.setDisable(false);
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