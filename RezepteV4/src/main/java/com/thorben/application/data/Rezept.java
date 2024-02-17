package com.thorben.application.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Rezept {
	
	private String title = null;
	private double rating = 0;
	private String difficultyLevel;
	private long time = 0;
	private String dropBoxPath = null;
	private int id = 0;
	private int categoryId = 0;
	private boolean newOne = false;
	
	public Rezept(String title, String dropBoxPath) {
		this.title = title;
		this.dropBoxPath = dropBoxPath;
	}
	
	public Rezept(String title, String difficultyLevel, long time, double rating) {
		this.title = title;
		this.difficultyLevel = difficultyLevel;
		this.time = time;
		this.rating = rating;
	}
	
	public Rezept(int id, int categoryId, String title, String difficultyLevel, long time, double rating) {
		this.id = id;
		this.categoryId = categoryId;
		this.title = title;
		this.difficultyLevel = difficultyLevel;
		this.time = time;
		this.rating = rating;
	}

	@Override
	public String toString() {
		return this.title;
	}
	
	public String writeTxt() {
		return this.title + ";" + this.difficultyLevel + ";" + this.time + ";" + this.rating;
	}
	
}
