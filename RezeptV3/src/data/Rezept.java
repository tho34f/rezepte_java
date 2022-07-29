package data;

public class Rezept {
	
	private String title = null;
	private double rating = 0;
	private String difficultyLevel;
	private long time;
	private String dropBoxPath = null;
	
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public String getDropBoxPath() {
		return dropBoxPath;
	}

	public void setDropBoxPath(String dropBoxPath) {
		this.dropBoxPath = dropBoxPath;
	}

	@Override
	public String toString() {
		return this.title;
	}
	
	public String writeTxt() {
		return this.title + ";" + this.difficultyLevel + ";" + this.time + ";" + this.rating;
	}

	public String getDifficultyLevel() {
		return difficultyLevel;
	}

	public void setDifficultyLevel(String difficultyLevel) {
		this.difficultyLevel = difficultyLevel;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
	
	

}
