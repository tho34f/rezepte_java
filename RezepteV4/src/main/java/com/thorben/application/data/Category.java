package com.thorben.application.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Category {
	
	private String name;
	private String dropBoxPath;
	private int id;
	private boolean newOne;

	public Category(String name, String dropBoxPath) {
		this.name = name;
		this.dropBoxPath = dropBoxPath;
	}
	
	public Category(int id, String name, String dropBoxPath) {
		this.name = name;
		this.dropBoxPath = dropBoxPath;
		this.id = id;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
