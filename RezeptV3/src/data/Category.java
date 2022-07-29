package data;

public class Category {
	
	private String name;
	private String dropBoxPath;

	public Category(String name, String dropBoxPath) {
		this.name = name;
		this.dropBoxPath = dropBoxPath;
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

}
