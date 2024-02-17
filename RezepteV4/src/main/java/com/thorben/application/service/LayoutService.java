package com.thorben.application.service;

import com.thorben.application.data.ApplicationData;
import com.thorben.application.layout.LayoutElements;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class LayoutService {
	
	private static final DierkesLooger LOGGER = new DierkesLooger();
	
	private LayoutService() {
		throw new IllegalStateException("Utility class");
	}
	
	public static VBox createVBOX(double spacing, Insets padding) {
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
	
	public static HBox createHBOX(double spacing, Insets padding) {
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
	
	public static void createAlert(AlertType type,String title, String headerText, String contentText) {
		Alert alert = new Alert(type);
		alert.setGraphic(null);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
		alert.showAndWait();
	}
	
	public static ImageView createImageView(Image im) {
		ImageView iv = new ImageView(im);
		iv.setFitHeight(20);
		iv.setFitWidth(20);
		return iv;
	}

	public static void createLayout(BorderPane root, LayoutElements elements, ApplicationData mainData) {
		LOGGER.writeInfoLog("LayoutService: start createLayout()", LayoutService.class.getName());
		createMenuBar(elements);
		createLayout(root, mainData, elements);
	}

	public static void setMainSettingsPrimaryStage(Stage primaryStage, Scene scene, LayoutElements elements) {
		LOGGER.writeInfoLog("LayoutService: start setMainSettingsPrimaryStage()", LayoutService.class.getName());
		primaryStage.getIcons().add(elements.getRezeptImage());
		primaryStage.setScene(scene);
		primaryStage.setTitle("Rezeptverwaltung");
	}
	
	private static void createMenuBar(LayoutElements elements) {
		elements.getMenuBar().setPrefWidth(1000);
		elements.getMenuBar().getMenus().addAll(elements.getMenuFile(), elements.getMenuEdit(), elements.getMenuView());
		elements.getAccountInfo().setGraphic(LayoutService.createImageView(elements.getAccount()));
		elements.getCloseApplication().setGraphic(LayoutService.createImageView(elements.getExit()));
		elements.getMenuFile().getItems().addAll(elements.getAccountInfo(), elements.getCloseApplication());
	}
	
	private static void createLayout(BorderPane root, ApplicationData mainData, LayoutElements elements) {
		Insets padding = new Insets(15, 15, 15, 15);
		
		VBox topBorder = LayoutService.createVBOXWithAlignment(10, new Insets(0, 0,15, 0));
		elements.getWelcome().setFont(Font.font("cambria", 24));
		topBorder.getChildren().addAll(elements.getMenuBar(), elements.getWelcome());
		
		VBox leftBorder = LayoutService.createVBOX(10, padding);
		mainData.getCategorien().setPrefWidth(200);
		HBox hLeftBorder = LayoutService.createHBOX(10);
		elements.getFindCategory().setPromptText("Kategoriename");
		hLeftBorder.getChildren().addAll(elements.getFindCategory(),elements.getBtnFindCategory());
		leftBorder.getChildren().addAll(elements.getChooseCategory(),mainData.getCategorien(),hLeftBorder);
		
		VBox centerBorder = LayoutService.createVBOX(10, padding);
		HBox hCenterBorder = LayoutService.createHBOX(10);
		elements.getFindRezept().setPromptText("Rezeptname");
		hCenterBorder.getChildren().addAll(elements.getFindRezept(),elements.getBtnFindRezept());
		centerBorder.getChildren().addAll(elements.getChooseRezept(), mainData.getRezepte(), hCenterBorder);
		
		HBox borderBottom = LayoutService.createHBOX(10, padding);
		borderBottom.getChildren().addAll(elements.getMoreFunczions(), elements.getBtnCreateCategory(),elements.getButtonUpload());
		
		VBox rightBorder = LayoutService.createVBOX(10, padding);
		HBox lineButton = LayoutService.createHBOX(10, padding);
		rightBorder.setAlignment(Pos.CENTER);
		elements.getButtonDownload().setDisable(true);
		elements.getBtnEvaluate().setDisable(true);
		lineButton.getChildren().addAll(elements.getButtonDownload(),elements.getBtnEvaluate());
		rightBorder.getChildren().addAll(elements.getRezeptName(), elements.getRezeptRating(), elements.getRezeptTime(), 
				elements.getRezeptDifficultyLevel(), lineButton);
		
		root.setLeft(leftBorder);
		root.setCenter(centerBorder);
		root.setRight(rightBorder);
		root.setBottom(borderBottom);
		root.setTop(topBorder);
	}

}
