package com.thorben.application;
import java.util.List;

import com.thorben.application.data.ApplicationData;
import com.thorben.application.data.Category;
import com.thorben.application.data.Rezept;
import com.thorben.application.layout.LayoutElements;
import com.thorben.application.service.ButtonService;
import com.thorben.application.service.DierkesLooger;
import com.thorben.application.service.DropboxWizard;
import com.thorben.application.service.LayoutService;
import com.thorben.application.service.RezepteService;

import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.scene.Scene;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class RezepteV4 extends Application {

private static final DierkesLooger LOOGER = new DierkesLooger();
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		BorderPane root = new BorderPane();
		ApplicationData mainData = new ApplicationData();
		LayoutElements elements = new LayoutElements();
		DropboxWizard dbw = new DropboxWizard();
		
		Scene scene = new Scene(root,1000,600);
		
		List<Category> categorieList = RezepteService.fillCategory(dbw);
		mainData.setCategorien(categorieList);
		
		MultipleSelectionModel<Category> categoryObserver = mainData.getCategorien().getSelectionModel();
		categoryObserver.getSelectedItems().addListener((ListChangeListener<? super Category>)e -> ButtonService.selectCategory(dbw, mainData, categoryObserver));
		
		MultipleSelectionModel<Rezept> rezepteObserver = mainData.getRezepte().getSelectionModel();
		rezepteObserver.getSelectedItems().addListener((ListChangeListener<? super Rezept>)e -> ButtonService.selectRezept(elements, mainData, rezepteObserver));
		
		elements.getBtnEvaluate().setOnAction(e -> ButtonService.evaluate(mainData, elements));
		elements.getBtnCreateCategory().setOnAction(e -> ButtonService.createCategory(dbw, elements));
		
		elements.getButtonUpload().setOnAction(e -> {
			LOOGER.writeInfoLog("Start Upload Rezept", RezepteV4.class.getName());
			elements.setSecondStage(RezepteService.startCreateUpload(elements.getRezeptImage(), dbw));
		});
		
		elements.getAccountInfo().setOnAction(e -> {
			LOOGER.writeInfoLog("Get Account Information", RezepteV4.class.getName());
			RezepteService.getAccountInformation(dbw);
		});
		
		elements.getButtonDownload().setOnAction(e -> {
			LOOGER.writeInfoLog("Start Download Rezept", RezepteV4.class.getName());
			RezepteService.downloadRezept(mainData.getCategoryChoose().getName(),mainData.getRezept().getTitle(), dbw, mainData.getDownloadFile());
		});
		
		elements.getBtnFindCategory().setOnAction(e -> ButtonService.findCategory(mainData, elements, dbw));
		elements.getBtnFindRezept().setOnAction(e -> ButtonService.findRezept(mainData, elements, dbw));
		
		ButtonService.closeApplication(primaryStage, elements, dbw, mainData);
		LayoutService.createLayout(root, elements, mainData);
		LayoutService.setMainSettingsPrimaryStage(primaryStage, scene, elements);
		
		primaryStage.show();

	}
	
	public static void main(String[] args) {
		launch(args);
	}

}