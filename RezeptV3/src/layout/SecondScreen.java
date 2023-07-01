package layout;

import java.util.List;

import data.Category;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import service.LayoutService;
import service.RezepteService;

public class SecondScreen {
	
	private BorderPane pane;
	private Label label;
	private Label secondLabel;
	private TextField textField;
	private Button chooseBtn;
	private Button confirm;
	private ComboBox<Category> categorien;
	private VBox borderCenter;
	private HBox btnButton;
	
	public SecondScreen(String labelText, String promptText, String buttonText) {
		this.pane = new BorderPane();
		this.label = new Label(labelText);
		this.textField = new TextField();
		this.textField.setPromptText(promptText);
		this.confirm = new Button(buttonText);
		this.borderCenter = LayoutService.createVBOXWithAlignment(10, new Insets(15, 15, 15, 15));
		this.borderCenter.getChildren().addAll(this.label, this.textField, this.confirm);
		this.pane.setCenter(borderCenter);
	}
	
	public SecondScreen(String labelText, String secondLabelText, String promptText, String buttonTextOne, String buttonTextTwo, List<Category> categorieList) {
		this.pane = new BorderPane();
		this.label = new Label(labelText);
		this.secondLabel = new Label(secondLabelText);
		this.textField = new TextField();
		this.textField.setPromptText(promptText);
		this.confirm = new Button(buttonTextTwo);
		this.confirm.setDisable(true);
		this.categorien = new ComboBox<>();
		this.categorien.getItems().addAll(categorieList);
		this.chooseBtn = new Button(buttonTextOne);
		this.borderCenter = LayoutService.createVBOXWithAlignment(10, new Insets(15, 15, 15, 15));
		this.btnButton = LayoutService.createHBOX(10);
		this.btnButton.getChildren().addAll(chooseBtn, confirm);
		this.borderCenter.getChildren().addAll(this.secondLabel, this.getCategorien(), this.label, this.textField, this.btnButton);
		this.pane.setCenter(borderCenter);
	}

	public BorderPane getPane() {
		return pane;
	}

	public void setPane(BorderPane pane) {
		this.pane = pane;
	}

	public Label getLabel() {
		return label;
	}

	public void setLabel(Label label) {
		this.label = label;
	}

	public TextField getTextField() {
		return textField;
	}

	public void setTextField(TextField textField) {
		this.textField = textField;
	}

	public Button getConfirm() {
		return confirm;
	}

	public void setConfirm(Button confirm) {
		this.confirm = confirm;
	}

	public VBox getBorderCenter() {
		return borderCenter;
	}

	public void setBorderCenter(VBox borderCenter) {
		this.borderCenter = borderCenter;
	}

	public Button getChooseBtn() {
		return chooseBtn;
	}

	public void setChooseBtn(Button chooseBtn) {
		this.chooseBtn = chooseBtn;
	}

	public HBox getBtnButton() {
		return btnButton;
	}

	public void setBtnButton(HBox btnButton) {
		this.btnButton = btnButton;
	}

	public ComboBox<Category> getCategorien() {
		return categorien;
	}

	public void setCategorien(ComboBox<Category> categorien) {
		this.categorien = categorien;
	}

	public Label getSecondLabel() {
		return secondLabel;
	}

	public void setSecondLabel(Label secondLabel) {
		this.secondLabel = secondLabel;
	}

}
