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
import layout.LayoutService;
import service.RezepteService;

public class RezeptQuerries {
	
	private RezeptQuerries() {
		throw new IllegalStateException("Utility class");
	}
	
	public static List<Rezept> getAllRezepte(){
		List<Rezept> allRezepte = new ArrayList<>();
		String sqlSelect = "SELECT * " +
					" FROM rezept";
		try(Connection con = getConnection()){
			try(Statement stmt = con.createStatement()){
				try(ResultSet rs = stmt.executeQuery(sqlSelect)){
					Rezept rz = null;
					while(rs.next()) {
						rz = new Rezept(rs.getInt("id"), rs.getInt("kategoryid") , rs.getString("name"), 
								rs.getString("schwierigkeitsgrad"),rs.getLong("zeit"), rs.getDouble("bewertung"));
						allRezepte.add(rz);
					}
				}
			}
		} catch(SQLException e) {
			LayoutService.createAlert(AlertType.ERROR, Konstanten.ERRORDIALOG, Konstanten.LOOKERROR,
					Konstanten.DB_ERROR);
		}
		
		return allRezepte;
	}
	
	public static boolean insertRezept(Rezept rz) {
		String sqlInsert = "INSERT INTO rezept " +
				" VALUES (?,?,?,?,?,?)";
		boolean insertIsOk = true;
		try(Connection con = getConnection()){
			try(PreparedStatement pstmt = con.prepareStatement(sqlInsert)){
				int counter = 1;
				pstmt.setInt(counter++, rz.getId());
				pstmt.setString(counter++, rz.getTitle());
				pstmt.setDouble(counter++, rz.getRating());
				pstmt.setString(counter++, rz.getDifficultyLevel());
				pstmt.setLong(counter++, rz.getTime());
				pstmt.setInt(counter++, rz.getCategoryId());
				insertIsOk = pstmt.execute();
			}
		} catch(SQLException e) {
			LayoutService.createAlert(AlertType.ERROR, Konstanten.ERRORDIALOG, Konstanten.LOOKERROR,
					Konstanten.DB_ERROR);
		}
		
		return insertIsOk;
	}
	
	public static boolean updateRezeptRating(Rezept rz) {
		String sqlUpdate = "UPFATE rezept " +
				" SET bewertung = ? " +  
				" WHERE id = ?";
		boolean updateIsOk = false;
		try(Connection con = getConnection()){
			try(PreparedStatement pstmt = con.prepareStatement(sqlUpdate)){
				pstmt.setDouble(1, rz.getRating());
				pstmt.setInt(2, rz.getId());
				updateIsOk = true;
			}
		} catch(SQLException e) {
			LayoutService.createAlert(AlertType.ERROR, Konstanten.ERRORDIALOG, Konstanten.LOOKERROR,
					Konstanten.DB_ERROR);
		}
		
		return updateIsOk;
	}
	
	public static boolean updateRezept(Rezept rz) {
		String sqlUpdate = "UPFATE rezept " +
				" SET kategoryid = ? , " +
					" name = ?, " +
					" schwierigkeitsgrad = ?, " +
					" zeit = ?, " +
					" bewertung = ? " +
				" WHERE id = ?";
		boolean updateIsOk = false;
		try(Connection con = getConnection()){
			try(PreparedStatement pstmt = con.prepareStatement(sqlUpdate)){
				int counter = 1;
				pstmt.setInt(counter++, rz.getCategoryId());
				pstmt.setString(counter++, rz.getTitle());
				pstmt.setString(counter++, rz.getDifficultyLevel());
				pstmt.setLong(counter++, rz.getTime());
				pstmt.setDouble(counter++, rz.getRating());
				pstmt.setInt(counter++, rz.getId());
				updateIsOk = true;
			}
		} catch(SQLException e) {
			LayoutService.createAlert(AlertType.ERROR, Konstanten.ERRORDIALOG, Konstanten.LOOKERROR,
					Konstanten.DB_ERROR);
		}
		
		return updateIsOk;
	}
	
	public static boolean updateAll(List<Rezept> allRezepte) {
		boolean updateIsOk = false;
		
		return updateIsOk;
	}
	
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:sqlite:rezepte.db");
	}

}
