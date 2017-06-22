package halmaPP.board;

/**
 * Diese Klasse realisiert die Elemente oder einzelnen Punkte des Spielbretts.
 * Es werden &uuml;ber die Belegung des Feldes (Rot, Blau oder leer), die Art
 * des Feldes (normales Spielbrett, rotes oder blaues Haus) und die Klasse
 * (1,2,3 oder 4) gespeichert. Letztere wird zur sp&auml;teren einfachen
 * &Uuml;berpr&uuml;fung der G&uuml;ltigkeit eines Zuges verwendet.
 * 
 * @author Dominick Leppich
 * @version 0.1
 */
public class Field {
	/**
	 * Es wird ein neues Feld-Element erzeugt mit entsprechenden Attributen
	 * erzeugt.
	 * 
	 * @param value
	 *            Figur die auf diese Feld steht
	 * @param house
	 *            Zu welchem Haus geh&ouml;rt dieses Feld
	 * @param fclass
	 *            Welche Feldklasse hat das Feld
	 * @param northeast
	 *            Referenz auf nord-&ouml;stliches Feld
	 * @param east
	 *            Referenz auf &ouml;stliches Feld
	 * @param southeast
	 *            Referenz auf s&uuml;d-&ouml;stliches Feld
	 * @param northwest
	 *            Referenz auf nord-westliches Feld
	 * @param west
	 *            Referenz auf westliches Feld
	 * @param southwest
	 *            Referenz auf s&uuml;d-westliches Feld
	 */
	public Field(int row, int diagonal, int value, int house, int fclass,
			Field northeast, Field east, Field southeast, Field northwest,
			Field west, Field southwest) {
		this.row = row;
		this.diagonal = diagonal;
		this.value = value;
		this.house = house;
		this.fieldClass = fclass;
		this.northEast = northeast;
		this.east = east;
		this.southEast = southeast;
		this.northWest = northwest;
		this.west = west;
		this.southWest = southwest;
	}

	/**
	 * Es wird eine "leeres" Feld an Position (Reihe, Diagonale) erzeugt.
	 * 
	 * @param row
	 *            Reihe
	 * @param diagonal
	 *            Diagonale
	 */
	public Field(int row, int diagonal) {
		this.row = row;
		this.diagonal = diagonal;
		house = Field.NO_HOUSE;
	}

	// ---------------------------------------------------------------
	/*
	 * Getter-Methoden fuer alle Nachbarreferenzen und fuer die einzelnen Werte
	 * und Attribute des Feldes
	 */

	/**
	 * Liefert Feld: Nord-Ost
	 * @return Feld: Nord-Ost
	 */
	public Field getNorthEast() {
		return northEast;
	}

	/**
	 * Setzt Feld: Nord-Ost
	 * @param northEast
	 * 		Feld
	 */
	public void setNorthEast(Field northEast) {
		this.northEast = northEast;
	}

	/**
	 * Liefert Feld: Osten
	 * @return Feld: Osten
	 */
	public Field getEast() {
		return east;
	}

	/**
	 * Setzt Feld: Osten
	 * @param east
	 * 		Feld
	 */
	public void setEast(Field east) {
		this.east = east;
	}

	/**
	 * Liefert Feld: S&uuml;d-Osten
	 * @return Feld: S&uuml;d-Osten
	 */
	public Field getSouthEast() {
		return southEast;
	}

	/**
	 * Setzt Feld: S&uuml;d-Osten
	 * @param southEast
	 * 		Feld
	 */
	public void setSouthEast(Field southEast) {
		this.southEast = southEast;
	}

	/**
	 * Liefert Feld: Nord-Westen
	 * @return Feld: Nord-Westen
	 */
	public Field getNorthWest() {
		return northWest;
	}

	/**
	 * Setzt Feld: Nord-Westen
	 * @param northWest
	 * 		Feld
	 */
	public void setNorthWest(Field northWest) {
		this.northWest = northWest;
	}

	/**
	 * Liefert Feld: Westen
	 * @return Feld: Westen
	 */
	public Field getWest() {
		return west;
	}

	/**
	 * Setzt Feld: Westen
	 * @param west
	 * 		Feld
	 */
	public void setWest(Field west) {
		this.west = west;
	}

	/**
	 * Liefert Feld: S&uuml;d-Westen
	 * @return Feld: S&uml;d-Westen
	 */
	public Field getSouthWest() {
		return southWest;
	}

	/**
	 * Setzt Feld: S&uuml;d-Westen
	 * @param southWest
	 * 		Feld
	 */
	public void setSouthWest(Field southWest) {
		this.southWest = southWest;
	}

	/**
	 * Liefert Haus
	 * @return Haus
	 */
	public int getHouse() {
		return house;
	}

	/**
	 * Setzt Haus
	 * @param house
	 * 		Haus
	 */
	public void setHouse(int house) {
		this.house = house;
	}

	/** 
	 * Liefert Wert
	 * @return Wert
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Setzt Wert
	 * @param value
	 * 		Wert
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * Liefert fieldClass
	 * @return fieldClass
	 */
	public int getFieldclass() {
		return fieldClass;
	}

	/**
	 * Setzt fieldClass
	 * @param fclass
	 * 		fieldClass
	 */
	public void setFieldclass(int fclass) {
		this.fieldClass = fclass;
	}

	// ---------------------------------------------------------------

	/**
	 * Liefere eine Kopie des Feld-Elementes zur&uuml;ck
	 * 
	 * @return Referenz auf eine Kopie dieses Feldes
	 */
	public Field clone() {
		return new Field(row, diagonal, value, house, fieldClass, northEast,
				east, southEast, northWest, west, southWest);
	}

	/**
	 * Liefert eine String Repr&auml;sentation des Feldes f&uuml;r die Konsole
	 * 
	 * @return String mit Buchstabe f&uuml;r Darstellung des Feldes (B,R, " ")
	 */
	public String toString() {
		if (value == Field.BLUE)
			return "B";
		if (value == Field.RED)
			return "R";
		if (value == Field.EMPTY) {
			if (house == Field.BLUE_HOUSE)
				return "-";
			if (house == Field.RED_HOUSE)
				return "+";
			if (house == Field.BOTH_HOUSE)
				return "*";
			return ".";
		}
		return "";
	}

	// ---------------------------------------------------------------

	public static final int NO_HOUSE = 3;
	public static final int BLUE_HOUSE = 4;
	public static final int RED_HOUSE = 5;
	public static final int BOTH_HOUSE = 6;

	public static final int BLUE = 1;
	public static final int RED = 2;
	public static final int EMPTY = 0;

	// ---------------------------------------------------------------

	/* Referenzen auf umliegende Felder */
	private Field northEast;
	private Field east;
	private Field southEast;
	private Field northWest;
	private Field west;
	private Field southWest;

	private int house; // zu welchem Haus gehoert dieses Feld
	private int value; // was befindet sich auf diesem Feld

	/* Koordinaten des Feldes */
	public final int row;
	public final int diagonal;

	private int fieldClass; // Klasse des Feldes (1-4)
}
