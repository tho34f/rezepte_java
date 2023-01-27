package service;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LayoutService {
	
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

}
