module RezeptV3 {
	requires javafx.controls;
	requires javafx.graphics;
	requires dropbox.core.sdk;
	requires javafx.fxml;
	
	opens application to javafx.graphics, javafx.fxml;
}
