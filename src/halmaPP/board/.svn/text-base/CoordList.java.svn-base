package halmaPP.board;

import java.util.Iterator;

/**
 * Implementiert eine Liste von Koordinaten, welche verwendet wird um Spieler
 * &uuml;ber die Positionen der Kegel zu informieren.
 * 
 * @author Dominick Leppich
 * 
 */
public class CoordList implements Iterable<Coord> {

	/**
	 * Konstruktor.
	 */
	public CoordList() {
		array = new Coord[itemcount];
		index = 0;
	}

	// ---------------------------------------------------------------

	/**
	 * Liefere einen Iterator f&uuml;r die Liste, mit dem sie von Anfang bis
	 * Ende durchlaufen werden kann.
	 * 
	 * @return Iterator
	 */
	public Iterator<Coord> iterator() {
		return new CoordListIterator(array, index);
	}

	/**
	 * Liefert String-Repr&auml;sentation der Liste: <br />
	 * Alle Elemente werden durch Kommas getrennt.
	 */
	public String toString() {
		String s = "";
		for (Coord x : array)
			s += x;
		return s;
	}

	// ---------------------------------------------------------------

	/**
	 * Pr&uuml;fe ob die Liste leer ist.
	 * 
	 * @return Liste leer?
	 */
	public boolean isEmpty() {
		return (index == 0);
	}

	/**
	 * F&uuml;ge neues Coord-Objekt (Koordinate) in die Liste ein. Wirft eine
	 * Runtime-Exception wenn die Liste voll ist (kann nur 15 aufnehmen).
	 * 
	 * @param obj
	 *            Element das einzuf&uuml;gen ist
	 */
	public void add(Coord obj) {
		if (index > itemcount)
			throw new RuntimeException("Liste ist voll");
		array[index++] = obj;
	}

	// ---------------------------------------------------------------

	/**
	 * Array
	 */
	private Coord[] array;
	/**
	 * Index
	 */
	private int index;

	// ---------------------------------------------------------------

	/**
	 * Maximale Gr&ouml;sse
	 */
	private final int itemcount = 15;

}
