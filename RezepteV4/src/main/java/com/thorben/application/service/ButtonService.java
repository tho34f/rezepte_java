package com.thorben.application.service;

import java.util.List;

import com.thorben.application.data.ApplicationData;
import com.thorben.application.data.Category;
import com.thorben.application.data.Rezept;
import com.thorben.application.layout.LayoutElements;

import javafx.scene.control.MultipleSelectionModel;
import javafx.stage.Stage;

public class ButtonService {
	
	private static final DierkesLooger LOGGER = new DierkesLooger();
	
	private ButtonService() {
		throw new IllegalStateException("Utility class");
	}

	public static void closeApplication(Stage primaryStage, LayoutElements elements, DropboxWizard dbw, ApplicationData mainData) {
		elements.getCloseApplication().setOnAction(e -> {
			LOGGER.writeInfoLog("Start close Application", ButtonService.class.getName());
			RezepteService.actionBeforeClose(dbw, elements.getSecondStage(), mainData.getDownloadFile());
			System.exit(0);
		});
		
		primaryStage.setOnCloseRequest(e -> { 
			LOGGER.writeInfoLog("Start Close Request", ButtonService.class.getName());
			RezepteService.actionBeforeClose(dbw, elements.getSecondStage(), mainData.getDownloadFile());
		});
	}
	
	public static void chooseRezept(LayoutElements elements, ApplicationData mainData) {
		LOGGER.writeInfoLog("Start chooseRezept()", ButtonService.class.getName());
		Rezept rezept = mainData.getRezept();
		
		elements.getRezeptName().setText("Name: " + rezept.getTitle());
		elements.getRezeptRating().setText("Bewertung: " + rezept.getRating());
		
		String level = rezept.getDifficultyLevel();
		if(level != null) {
			elements.getRezeptDifficultyLevel().setText("Schwirigkeitsgrad: " + level);
		} else {
			elements.getRezeptDifficultyLevel().setText("Schwirigkeitsgrad: -");
		}
		
		long time = rezept.getTime();
		if(time != 0) {
			elements.getRezeptTime().setText("Dauer: " + time + " Minuten");
		} else {
			elements.getRezeptTime().setText("Dauer: -");
		}
		
		elements.getBtnEvaluate().setDisable(false);
		elements.getButtonDownload().setDisable(false);
	}
	public static void selectRezept(LayoutElements elements, ApplicationData mainData, MultipleSelectionModel<Rezept> rezepteObserver) {
		LOGGER.writeInfoLog("Start select Rezept", ButtonService.class.getName());
		Rezept rezept = rezepteObserver.getSelectedItem();
		if(rezept != null) {
			mainData.setRezept(rezept);
			chooseRezept(elements, mainData);
		} else {
			elements.getBtnEvaluate().setDisable(true);
			elements.getButtonDownload().setDisable(true);
		}
	}
	
	public static void createCategory(DropboxWizard dbw, LayoutElements elements) {
		LOGGER.writeInfoLog("Start create Category", ButtonService.class.getName());
		elements.setSecondStage(RezepteService.startCreateCategory(elements.getRezeptImage(), dbw));
	}
	
	public static void selectCategory(DropboxWizard dbw, ApplicationData mainData, MultipleSelectionModel<Category> categoryObserver) {
		LOGGER.writeInfoLog("Start select Category", ButtonService.class.getName());
		mainData.setCategoryChoose(categoryObserver.getSelectedItem());
		mainData.setRezepteList(RezepteService.fillRezepte(mainData.getCategoryChoose(), dbw));
		mainData.setRezepte(mainData.getRezepteList());
	}
	
	public static void evaluate(ApplicationData mainData, LayoutElements elements) {
		LOGGER.writeInfoLog("Start Evaluation", ButtonService.class.getName());
		elements.setSecondStage(RezepteService.startEvaluation(elements.getRezeptImage(), mainData.getRezept()));
		for(Rezept rezeptFromList : RezepteService.getRezepteList()) {
			if(rezeptFromList.getTitle().equals(mainData.getRezept().getTitle())){
				mainData.setRezept(rezeptFromList);
				chooseRezept(elements, mainData);
			}
		}
	}
	
	public static void findCategory(ApplicationData mainData, LayoutElements elements, DropboxWizard dbw) {
		LOGGER.writeInfoLog("Start find Category", ButtonService.class.getName());
		String eingabe = elements.getFindCategory().getText();
		if(eingabe != null && !eingabe.isEmpty()) {
			for(Category category : RezepteService.getCategorieList()) {
				if(eingabe.equals(category.getName())) {
					mainData.getCategorien().getSelectionModel().select(RezepteService.getCategorieList().indexOf(category));
					mainData.setCategoryChoose(category);
					List<Rezept> rezepte = RezepteService.fillRezepte(category, dbw);
					mainData.setRezepteList(rezepte);
					mainData.setRezepte(rezepte);
				}
			}
		}
		elements.getFindCategory().clear();
	}
	
	public static void findRezept(ApplicationData mainData, LayoutElements elements, DropboxWizard dbw) {
		LOGGER.writeInfoLog("Start find Rezept", ButtonService.class.getName());
		String eingabe = elements.getFindRezept().getText();
		if(eingabe != null && !eingabe.isEmpty()) {
			List<Rezept> rezeptList = mainData.getRezepteList();
			for(Rezept re : rezeptList) {
				if(eingabe.equals(re.getTitle())) {
					mainData.getRezepte().getSelectionModel().select(rezeptList.indexOf(re));
					mainData.setRezept(re);
					chooseRezept(elements, mainData);
				}
			}
		}
		elements.getFindRezept().clear();
	}

}
