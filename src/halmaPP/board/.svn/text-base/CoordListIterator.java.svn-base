package halmaPP.board;

import java.util.Iterator;

/**
 * Ein Iterator, welcher die CoordList durchl&auml;uft.
 * 
 * @author Dominick Leppich
 * 
 */
public class CoordListIterator implements Iterator<Coord> {
	/**
	 * Konstruktor f&uuml;r einen Iterator der CoordList
	 * 
	 * @param array
	 *            Coord Array das durchlaufen werden soll
	 * @param itemcount
	 *            Anzahl der gespeicherten Elemente
	 */
	public CoordListIterator(Coord[] array, int itemcount) {
		this.array = array;
		this.itemcount = itemcount;
	}

	/**
	 * noch ein Element vorhanden?
	 */
	public boolean hasNext() {
		return (index < itemcount);
	}

	/**
	 * Liefert n&auml;chste Koordinate.
	 */
	public Coord next() {
		return array[index++];
	}

	/**
	 * L&ouml;scht aktuelles Element.
	 */
	public void remove() {
		array[index++] = null;
	}

	/**
	 * Index
	 */
	private int index;
	/**
	 * Anzahl
	 */
	private int itemcount;
	/**
	 * Array
	 */
	private Coord[] array;
}
