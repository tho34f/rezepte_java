package querries;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import data.Konstanten;
import data.Rezept;
import javafx.scene.control.Alert.AlertType;
import service.RezepteService;

public class CategoryQuerries {
	
	private CategoryQuerries() {
		throw new IllegalStateException("Utility class");
	}
	
	
	
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:sqllite:rezepte.db");
	}

}
