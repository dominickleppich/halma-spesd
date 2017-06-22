package halmaPP.preset;
/**
 * Implementiert Zug-Objekte
 * @author Doktor Brosenne
 *
 */
public class Move implements java.io.Serializable {
	
	/**
	 * Konstruktor mit Reihe und Diagonale und "leerem" n&auml;chsten Zug.
	 * @param row
	 * 		Reihe
	 * @param diagonal
	 * 		Diagonale
	 */
	public Move(int row, int diagonal) {
	this.row = row;
	this.diagonal = diagonal;
	next = null;
    }

	/**
	 * Konstruktor mit Reihe und Diagonale und definiertem n&auml;chsten Zug.
	 * @param row
	 * 		Reihe
	 * @param diagonal
	 * 		Diagonale
	 * @param next
	 * 		n&auml;chster Move
	 */
    public Move(int row, int diagonal, Move next) {
	this.row = row;
	this.diagonal = diagonal;
	this.next = next;
    }

    /**
     * Konstruktor &uuml;ber definiertes Move-Objekt.
     * @param mov
     * 		Move-Objekt
     */
    public Move(Move mov) {
	row = mov.getRow();
	diagonal = mov.getDiagonal();
	next = mov.getNext();
    }

    //---------------------------------------------------------------
    
    /**
     * Liefert Reihe eines Move-Objekts.
     * @return Reihe
     */
    public int getRow() {
	return row;
    }

    /**
     * Liefert Diagonale eines Move-Objekts.
     * @return Diagonale
     */
    public int getDiagonal() {
	return diagonal;
    }

    /**
     * Liefert n&auml;chsten Move eines Move-Objekts.
     * @return n&auml;chster Move
     */
    public Move getNext() {
	return next;
    }

    /**
     * setzt n&auml;chsten Move.
     * @param next
     * 		n&auml;chstes Move-Objekt
     */
    public void setNext(Move next) {
	this.next = next;
    }

    //---------------------------------------------------------------
    
    /**
     * Stringformat eines Move-Objekts.
     */
    public String toString() {
	String s = "(" + row + "/" + diagonal + ")";
	if (next == null)
	    return s;

	return s + "," + next;
    }

    // private ------------------------------------------------------
    
    /**
     * n&auml;chster Move
     */
    private Move next = null;
    /**
     * Reihe
     */
    private int row;
    /**
     * Diagonale
     */
    private int diagonal;
    /**
     * Checksum zum Sicherstellen, dass eine &uuml;ber das Netzwerk geschickte Klasse korrekt ankommt 
     */
    private static final long serialVersionUID = 1L;
}
