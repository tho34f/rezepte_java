module RezeptV3 {
	requires javafx.controls;
	requires javafx.graphics;
	requires dropbox.core.sdk;
	requires javafx.fxml;
	requires java.sql;
	
	opens application to javafx.graphics, javafx.fxml;
}
