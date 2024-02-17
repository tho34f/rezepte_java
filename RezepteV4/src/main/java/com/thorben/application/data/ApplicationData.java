package com.thorben.application.data;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javafx.scene.control.ListView;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationData {
	
	private String userName;
	private String directoryName;
	private Properties propRezepte;
	private ListView<Category> categorien = new ListView<>();
	private ListView<Rezept> rezepte = new ListView<>();
	private Rezept rezept = null;
	private Category categoryChoose = null;
	private List<Rezept> rezepteList = new ArrayList<>();
	
	public ApplicationData() {
		this.userName = System.getProperty("user.name");
		this.directoryName = System.getProperty("user.dir");
        this.propRezepte = loadInitData(this.directoryName + "/rezepte.ini");
	}
	
	public void setCategorien(List<Category> categorien) {
		this.categorien.getItems().setAll(categorien);
	}
	
	public void setRezepte(List<Rezept> rezepte) {
		this.rezepte.getItems().setAll(rezepte);
	}
	
	public String getDownloadFile() {
		return this.propRezepte.getProperty("downloadFile");
	}
	
	private Properties loadInitData(String path) {
		Properties prop = new Properties();
		try(FileInputStream fis = new FileInputStream(path)) {
			prop.load(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}

}
