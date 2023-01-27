package data;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDropBoxPath() {
		return dropBoxPath;
	}

	public void setDropBoxPath(String dropBoxPath) {
		this.dropBoxPath = dropBoxPath;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isNewOne() {
		return newOne;
	}

	public void setNewOne(boolean newOne) {
		this.newOne = newOne;
	}

}
