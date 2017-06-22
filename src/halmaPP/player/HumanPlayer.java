package halmaPP.player;

import java.io.File;
import java.rmi.RemoteException;
import java.util.*;
import halmaPP.board.Board;
import halmaPP.game.*;
import halmaPP.preset.*;

/**
 * Implementiert einen menschlichen Spieler. Erbt von AbstractPlayer und
 * liefert eine Implementation von getMove(), um den Spieler zum Zug aufzufordern.
 * 
 * @author Sebastian Lindner
 */

public class HumanPlayer extends AbstractPlayer {

	/* KONSTRUKTOR */

	/**
	 * Konstruktor.
	 * @param playerCode
	 * 		Spielerfarbe
	 * @param board
	 * 		Spielbrett
	 * @param guiInput
	 * 		mit GUI?
	 */
	public HumanPlayer( boolean playerCode, Board board, boolean guiInput) {
		super( playerCode, board, guiInput);
		Game.debug( "Menschlicher Spieler zugewiesen");
	}
	
	/**
	 * Konstruktor f&uuml;r das Laden/Speichern.
	 * @param playerCode
	 * 		Spielerfarbe
	 * @param board
	 * 		Spielbrett
	 * @param guiInput
	 * 		mit GUI?
	 * @param myTurn
	 * 		gerade am Zug?
	 */
	public HumanPlayer( boolean playerCode, Board board, boolean guiInput,boolean myTurn) {
		super( playerCode, board, guiInput,myTurn);
		Game.debug( "Menschlicher Spieler zugewiesen");
	}
	/* ------------------------------------------------ */

	/* INTERFACE METHODEN */

	/**
	 * Fordert den Spieler auf, den n&auml;chsten Zug zu machen. 
	 * Es werden Koordinaten f&uuml;r den zu setzenden Kegel und f&uuml;r die Zielposition
	 * (und alle Zwischenschritte) erwartet.
	 * 
	 * @return Ein Move-Objekt mit den definierten Koordinaten.
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Move getMove() throws Exception, RemoteException {

		/* mit GUI? */
		if (guiInput) {
			gui.setInputEnabled(true);
			Game.debug("Warte auf GUI Eingabe");
			Move guimove = gui.requestMove();
			Game.debug("GUI Eingabe erfolgt: " + guimove);
			gui.setInputEnabled(false);
			return guimove;
		}
		
		/* Eingabeaufforderung */
		String s = "";
		if (firstTime) {
			s += "Eingabe bitte in folgendem Format, mit Leerzeichen getrennt:\n"
					+ "1. x,y von Startfeld\t2. x,y von Zielfeldern\tmit x = Reihe, y = Diagonale\n"
					+ "WICHTIG: bei Spruengen muessen alle Zwischenschritte nacheinander angegeben werden!\n";
			firstTime = false;
		}
		Game.print(s + "Eingabe: ");
		
		/* Scanner initialisieren */
		int coordinate1 = 0, coordinate2 = 0;
		boolean swapped = false, reachedFirstDigit = false;
		char currentchar;
		Scanner scanner = new Scanner(System.in);
		scanner.reset();
		String input = scanner.next();
		if( input.compareTo("save") == 0){
			Game.save(new File(scanner.next()));
		}
		
		/* ersten zwei Koordinaten lesen */
		for (int i = 0; i < input.length(); i++) {
			currentchar = input.charAt(i);

			/* kein Komma und keine Ziffer -> falsche Eingabe */
			if (currentchar != ',' && !Character.isDigit(currentchar)) {
				Game.print("\nKeine Ziffer! Nochmal...\n");
				return this.request();
			}

			/* ab Komma naechsten Wert bearbeiten */
			if (currentchar == ',')
				swapped = true;

			/* Werte einlesen: */
			if (!swapped && currentchar != ',') {
				coordinate1 *= 10;
				coordinate1 += (int) currentchar - 48;
			} else if (currentchar != ',') {
				coordinate2 *= 10;
				coordinate2 += (int) currentchar - 48;
			}
		}

		/* move initialisieren */
		Move move = new Move(coordinate1, coordinate2);

		/* restliche Zeile einlesen in String */
		input = scanner.nextLine();

		/* ist der restliche String leer -> falsche Eingabe */
		if (!hasMoreSigns(input, 0)) {
			Game.print("\nZu wenig Koordinaten! Nochmal...\n");
			return this.request();
		}

		/* currentmove zur Bearbeitung der next-Werte, coordinates zuruecksetzen */
		Move currentmove = move;
		coordinate1 = 0;
		coordinate2 = 0;
		swapped = false;

		/* weitere Eingabe durchlaufen */
		for (int i = 0; i < input.length(); i++) {

			currentchar = input.charAt(i);
			boolean hasMoreSigns = hasMoreSigns(input, i);

			/* Eingabe eine Ziffer? */
			if (currentchar != ' ' && currentchar != ','
					&& !Character.isDigit(currentchar)) {
				Game.print("\nKeine Ziffer! Nochmal...\n");
				return this.request();
			}

			/* hier kein Leerzeichen und nicht getauscht -> coordinate1 */
			if (currentchar != ' ' && currentchar != ',' && !swapped) {
				reachedFirstDigit = true;
				coordinate1 *= 10;
				coordinate1 += (int) currentchar - 48;

			}

			/* hier kein Leerzeichen und getauscht -> coordinate2 */
			if (currentchar != ' ' && currentchar != ',' && swapped) {
				coordinate2 *= 10;
				coordinate2 += (int) currentchar - 48;
			}

			/* nach Komma coordinate2 bearbeiten: */
			if (currentchar == ',')
				swapped = true;

			/* bei Leerzeichen gehe so vor: */
			outerLoop: if (currentchar == ' ') {

				/* fuehrende Leerzeichen ignorieren */
				if (!reachedFirstDigit)
					break outerLoop;

				/* next-Objekt setzen, coordinates zuruecksetzen, swapped zuruecksetzen */
				if (hasMoreSigns) {
					currentmove.setNext(new Move(coordinate1, coordinate2));
					currentmove = currentmove.getNext();
					coordinate1 = 0;
					coordinate2 = 0;
					swapped = false;
				}
			}

			/* bei mehreren Leerzeichen Fehler, sofern noch Zeichen folgen */
			if (reachedFirstDigit && currentchar == ' '
					&& input.charAt(i - 1) == ' ') {
				if (hasMoreSigns) {
					Game.print("\nAufeinanderfolgende Leerzeichen! Nochmal...\n");
					return this.request();
				}
			}
		}

		if (hasInvalidLastCoord(input)) {
			Game.print("\nFehler bei Eingabe! Nochmal...\n");
			return this.request();
		}

		/* letztes Next-Objekt setzen */
		currentmove.setNext(new Move(coordinate1, coordinate2));
		return move;
	}

	/* ------------------------------------------------ */

	/* EIGENE METHODEN */

	/**
	 * Testet letzte Koordinaten auf richtige Formatierung.
	 * @param string
	 * 		Koordinaten als String
	 * @return boolean
	 */
	private static boolean hasInvalidLastCoord(String string) {
		int metDigitAt = 1;
		boolean metDigit = false;

		if (string.length() > 0) {
			for (int i = string.length() - 1; i != 0; i--) {

				/* wo habe ich die letzte Zahl gefunden? */
				if (Character.isDigit(string.charAt(i))) {
					metDigitAt = i;
					metDigit = true;
				}

				/* habe ich schon eine Zahl gefunden und das Zeichen davor ist ein Leerzeichen? z.B. 1,2 3,4 5 = falsche Eingabe */
				if (metDigit && string.charAt(metDigitAt - 1) == ' ')
					return true;

				/* habe ich noch keine Zahl gefunden und finde ein Komma? z.B. 1,2 3,4 5, = falsche Eingabe */
				if (!metDigit && string.charAt(i) == ',')
					return true;

				/* habe ich schon eine Zahl gefunden und finde dann ein Komma? Richtige Eingabe! */
				if (metDigit && string.charAt(metDigitAt - 1) == ',')
					return false;

			}
		}
		return false;
	}

	/**
	 * Testet, ob String ab einer bestimmten Position noch Zeichen ausser Leerzeichen enth&auml;lt.
	 * @param string
	 * 		String
	 * @param pos
	 * 		Position in String
	 * @return boolean
	 */
	private static boolean hasMoreSigns(String string, int pos) {
		for (; pos < string.length(); pos++)
			if (string.charAt(pos) != ' ')
				return true;
		return false;
	}

	/* ------------------------------------------------ */

	/* KLASSENVARIABLEN */
	
	/** damit bei der ersten Eingabeaufforderung &uuml;ber die Konsole eine Anleitung ausgegeben wird */
	private boolean firstTime = true;
}
