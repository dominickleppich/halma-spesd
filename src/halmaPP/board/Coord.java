package halmaPP.board;

/**
 * Diese Klasse gibt die M&ouml;glichkeit Koordinaten von einem Feld an andere
 * Klassen und Objekte zu &uuml;bergeben. Dies ist unter anderem n&ouml;tig
 * f&uuml;r die GUI, um das Spielbrett zeichnen zu k&ouml;nnen. Die Koordinate
 * hat ganz einfach eine Reihe und eine Diagonale, welche &uuml;ber Getter- und
 * Setter-Methoden abgefragt und gesetzt werden k&ouml;nnen.
 * 
 * @author Dominick Leppich
 * @version 0.1
 */
public class Coord {
	
	/**
	 * Konstruktor mit Reihe und Diagonale
	 * @param row
	 * 		Reihe
	 * @param diagonal
	 * 		Diagonale
	 */
	public Coord(int row, int diagonal) {
		this.row = row;
		this.diagonal = diagonal;
	}

	// ---------------------------------------------------------------

	/**
	 * Liefert Reihe.
	 * @return Reihe
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Setzt Reihe.
	 * @param row
	 * 		Reihe
	 */
	public void setRow(int row) {
		this.row = row;
	}

	/**
	 * Liefert Diagonale.
	 * @return Diagonale
	 */
	public int getDiagonal() {
		return diagonal;
	}

	/**
	 * Setzt Diagonale.
	 * @param diagonal
	 * 		Diagonale
	 */
	public void setDiagonal(int diagonal) {
		this.diagonal = diagonal;
	}

	// ---------------------------------------------------------------

	/**
	 * Liefert eine String-Repr&auml;sentation der Koordinate
	 * 
	 * @return Koordinate in Textform
	 */
	public String toString() {
		return "(" + row + "|" + diagonal + ")";
	}

	/**
	 * Vergleicht die &uuml;bergebene Koordinate mit dem Objekt auf dem die
	 * Methode aufgerufen wird auf Gleichheit (beide Komponenten stimmen exakt
	 * &uuml;berein.
	 * 
	 * @param obj
	 *            &Uuml;bergebenes Objekt
	 * @return Sind Koordinaten gleich
	 */
	public boolean equals(Coord obj) {
		return (row == obj.getRow() && diagonal == obj.getDiagonal());
	}

	// ---------------------------------------------------------------

	/**
	 * Reihe
	 */
	private int row;
	/**
	 * Diagonale
	 */
	private int diagonal;
}
