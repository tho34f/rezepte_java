package com.thorben.application.data;

public class Konstanten {
	
	private Konstanten() {
	    throw new IllegalStateException("Utility class");
	  }
	
	public static final String DB_ERROR = "Beim lesen aus der Datenbank ist etwas schiefgelaufen!";
	public static final String ERRORDIALOG = "Error Dialog";
	public static final String INFORMATIONDIALOG = "Information Dialog";
	public static final String LOOKERROR = "Look, an Error Dialog";
	public static final String LOOKINFORMATION = "Look, an Information";
	public static final String ERRORMESSAGE = "Es ist ein Fehler aufgetreten. Bitte versuchen Sie es erneut!";
	public static final String TXT = "rezepte.txt";
	public static final String HEADER = "name;schwierigkeitsgrad;zeit;durchschnittsbewertung";
	public static final String CREATE_CATEGORY = "Beim Erstellen der Kategorieliste ist etwas schiefgelaufen! Versuchen Sie es noch einmal!";
	public static final String CREATE_REZEPT = "Beim Erstellen der Rezeptliste ist etwas schiefgelaufen! Versuchen Sie es noch einmal!";
	public static final String ERROR_REZEPT_UPLOAD = "Beim Upload des Rezeptes ist etwas schiefgelaufen! Versuchen Sie es noch einmal!";

}
