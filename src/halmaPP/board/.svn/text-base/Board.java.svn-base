package halmaPP.board;

import java.util.*;

import halmaPP.game.*;
import halmaPP.player.MoveList;
import halmaPP.preset.*;

/**
 * Diese Klasse implementiert das eigentliche Spielbrett. Es k&ouml;nnen
 * Z&uuml;ge (Objekte der Klasse {@code Move}) entgegengenommen werden und die
 * entsprechenden Z&uuml;ge auf ihre G&uuml;ltigkeit &uuml;berpr&uuml;ft werden.
 * Ausserdem wird in jedem Zug die Spielsituation erkannt (Ist das Spiel zu
 * Ende? Wer hat gewonnen?)
 * 
 * @author Dominick Leppich
 * @version 0.1
 */
public class Board {

	/**
	 * Es wird ein neues Spielbrett erzeugt und mit der Startbelegung
	 * initialisiert (falls {@code newBoard = true}) ,
	 */
	public Board(boolean newBoard) {
		/* Spielfeld wird als zweidimensionales Array von Integern realisiert */
		gfield = new Field[11][15];
		Game.debug("Spielfeld wurde erzeugt");

		/* erzeuge ein Status-Objekt des Spielfeldes */
		state = new Status(Status.OK);
		Game.debug("Spielbrett Status ist OK");

		/* falls HausKoordinatenListe leer, dann fuellen */
		if (Const.BLUE_HOUSE_COORD.isEmpty()) {
			Const.BLUE_HOUSE_COORD.add(new Coord(10, 10));
			Const.BLUE_HOUSE_COORD.add(new Coord(10, 11));
			Const.BLUE_HOUSE_COORD.add(new Coord(10, 12));
			Const.BLUE_HOUSE_COORD.add(new Coord(10, 13));
			Const.BLUE_HOUSE_COORD.add(new Coord(10, 14));
			Const.BLUE_HOUSE_COORD.add(new Coord(9, 11));
			Const.BLUE_HOUSE_COORD.add(new Coord(9, 12));
			Const.BLUE_HOUSE_COORD.add(new Coord(9, 13));
			Const.BLUE_HOUSE_COORD.add(new Coord(9, 14));
			Const.BLUE_HOUSE_COORD.add(new Coord(8, 12));
			Const.BLUE_HOUSE_COORD.add(new Coord(8, 13));
			Const.BLUE_HOUSE_COORD.add(new Coord(8, 14));
			Const.BLUE_HOUSE_COORD.add(new Coord(7, 13));
			Const.BLUE_HOUSE_COORD.add(new Coord(7, 14));
			Const.BLUE_HOUSE_COORD.add(new Coord(6, 14));
		}
		if (Const.RED_HOUSE_COORD.isEmpty()) {
			Const.RED_HOUSE_COORD.add(new Coord(10, 6));
			Const.RED_HOUSE_COORD.add(new Coord(10, 7));
			Const.RED_HOUSE_COORD.add(new Coord(10, 8));
			Const.RED_HOUSE_COORD.add(new Coord(10, 9));
			Const.RED_HOUSE_COORD.add(new Coord(10, 10));
			Const.RED_HOUSE_COORD.add(new Coord(9, 5));
			Const.RED_HOUSE_COORD.add(new Coord(9, 6));
			Const.RED_HOUSE_COORD.add(new Coord(9, 7));
			Const.RED_HOUSE_COORD.add(new Coord(9, 8));
			Const.RED_HOUSE_COORD.add(new Coord(8, 4));
			Const.RED_HOUSE_COORD.add(new Coord(8, 5));
			Const.RED_HOUSE_COORD.add(new Coord(8, 6));
			Const.RED_HOUSE_COORD.add(new Coord(7, 3));
			Const.RED_HOUSE_COORD.add(new Coord(7, 4));
			Const.RED_HOUSE_COORD.add(new Coord(6, 2));
		}

		/*
		 * Falls es ein neues Board ist, muessen Felder erstellt und
		 * initialisiert werden. Die Nachbarreferenzen muessen gesetzt werden.
		 * Es wird unterschieden, damit beim clonen keine doppelte Arbeit
		 * verrichtet werden muss.
		 */
		if (newBoard) {
			createFields();
			setNeighborReferences();
		}
	}

	/**
	 * Konstruktor f&uuml;r ein neues Board, welches standardm&auml;ssig alle
	 * Felder neu erstellt und die Referenzen setzt.
	 */
	public Board() {
		this(true);
	}

	// ---------------------------------------------------------------

	/**
	 * Setze Feldeintrag (Reihe, Diagonale) auf &uuml;bergebenes Feld
	 * 
	 * @param row
	 *            Reihe
	 * @param diagonal
	 *            Diagonale
	 * @param field
	 *            Feld, welches an diese Stelle zu schreiben ist
	 */
	private void setField(int row, int diagonal, Field field) {
		if (row < 0 || row > 10 || diagonal < 0 || diagonal > 14)
			return;
		gfield[row][diagonal] = field;
	}

	/**
	 * Liefere zur&uuml;ck, welches Spieler gerade am Zug ist.
	 * 
	 * @return Spielerfarbe
	 */
	public boolean getNextPlayer() {
		return nextPlayer;
	}

	/**
	 * Liefere eine Kopie des aktuellen Status des Spielbrettes zur&uuml;ck.
	 * Dies entspricht dem Status des letzten Zuges.
	 * 
	 * @return Neues Status Objekt mit dem aktuellen Status
	 */
	public Status getStatus() {
		return new Status(state.getValue());
	}

	/**
	 * Liefere ein CoordArray, welches alle Koordinaten der Pins der
	 * gew&uuml;nschten Farbe enth&auml;lt.
	 * 
	 * @param playerCode
	 *            Farbe der Pins
	 * @return CoordArray Ein Array von Koordinaten der blauen Pins.
	 */
	public synchronized CoordList getPins(boolean playerCode) {
		CoordList array = new CoordList();
		int checkFieldValue;
		if (playerCode == Const.PLAYER_BLUE)
			checkFieldValue = Field.BLUE;
		else
			checkFieldValue = Field.RED;
		for (int row = 0; row < 11; row++) {
			for (int diagonal = 0; diagonal < 15; diagonal++) {
				if (gfield[row][diagonal] != null
						&& gfield[row][diagonal].getValue() == checkFieldValue)
					array.add(new Coord(row, diagonal));
			}
		}
		// Game.debug("Gebe Position der blauen Kegel zurueck");
		return array;
	}

	/**
	 * Gib die Feldklasse an Reihe und Diagonale zur&uuml;ck
	 * 
	 * @param row
	 *            Reihe
	 * @param diagonal
	 *            Diagonale
	 * @return Feldklasse
	 */
	public int getFieldClass(int row, int diagonal) {
		if (isField(row, diagonal))
			return gfield[row][diagonal].getFieldclass();
		return -1;
	}

	/**
	 * Liefere Informationen &uuml;ber das Haus an Position (Reihe, Diagonale)
	 * 
	 * @param row
	 *            Reihe
	 * @param diagonal
	 *            Diagonale
	 * @return Haustyp
	 */
	private int getHouse(int row, int diagonal) {
		if (isField(row, diagonal))
			return gfield[row][diagonal].getHouse();
		return -1;
	}

	/**
	 * Gib die Anzahl der Pins an, die sich in dem Haus des Spielers mit der
	 * Farbe {@code playerCode} befinden.
	 * 
	 * @param playerCode
	 *            Spielerfarbe
	 * @return Anzahl der Pins
	 */
	public int getHouseCount(boolean playerCode) {
		int count = 0;
		if (playerCode == Const.PLAYER_RED) {
			for (Coord x : getPins(Const.PLAYER_RED))
				if (getHouse(x.getRow(), x.getDiagonal()) == Field.RED_HOUSE
						|| getHouse(x.getRow(), x.getDiagonal()) == Field.BOTH_HOUSE)
					count++;
			for (Coord x : getPins(Const.PLAYER_BLUE))
				if (getHouse(x.getRow(), x.getDiagonal()) == Field.RED_HOUSE
						|| getHouse(x.getRow(), x.getDiagonal()) == Field.BOTH_HOUSE)
					count++;
		} else {
			for (Coord x : getPins(Const.PLAYER_RED))
				if (getHouse(x.getRow(), x.getDiagonal()) == Field.BLUE_HOUSE
						|| getHouse(x.getRow(), x.getDiagonal()) == Field.BOTH_HOUSE)
					count++;
			for (Coord x : getPins(Const.PLAYER_BLUE))
				if (getHouse(x.getRow(), x.getDiagonal()) == Field.BLUE_HOUSE
						|| getHouse(x.getRow(), x.getDiagonal()) == Field.BOTH_HOUSE)
					count++;
		}
		return count;
	}

	// ---------------------------------------------------------------

	/**
	 * Liefert eine String Repr&auml;sentation des Spielfeldes f&uuml;r die
	 * Konsole
	 * 
	 * @return String mit ASCII Darstellung des Spielfeldes
	 */
	@Override
	public String toString() {
		String s = "\n";
		int move = -2;
		for (int row = 10; row >= 0; row--) {
			move++;
			if (row < 10)
				s += " " + row + " ";
			else
				s += row;
			for (int i = 0; i < move; i++)
				s += " ";
			for (int diagonal = 0; diagonal <= 14; diagonal++) {
				if (gfield[row][diagonal] != null) {
					if (gfield[row][diagonal].getValue() == Field.BLUE) {
						if (Game.isColorized())
							s += Const.ESC_BOLD + Const.ESC_BLUE + "@ "
									+ Const.ESC_STD;
						else
							s += "B ";
					} else if (gfield[row][diagonal].getValue() == Field.RED) {
						if (Game.isColorized())
							s += Const.ESC_BOLD + Const.ESC_RED + "@ "
									+ Const.ESC_STD;
						else
							s += "R ";
					} else if (gfield[row][diagonal].getValue() == Field.EMPTY
							&& gfield[row][diagonal].getHouse() == Field.BLUE_HOUSE) {
						if (Game.isColorized())
							s += Const.ESC_BOLD + Const.ESC_BLUE + "# "
									+ Const.ESC_STD;
						else
							s += "- ";
					} else if (gfield[row][diagonal].getValue() == Field.EMPTY
							&& gfield[row][diagonal].getHouse() == Field.RED_HOUSE) {
						if (Game.isColorized())
							s += Const.ESC_BOLD + Const.ESC_RED + "# "
									+ Const.ESC_STD;
						else
							s += "+ ";
					} else if (gfield[row][diagonal].getValue() == Field.EMPTY
							&& gfield[row][diagonal].getHouse() == Field.BOTH_HOUSE) {
						if (Game.isColorized())
							s += Const.ESC_BOLD + Const.ESC_PURPLE + "# "
									+ Const.ESC_STD;
						else
							s += "+ ";
					} else if (gfield[row][diagonal].getValue() == Field.EMPTY) {
						if (Game.isColorized())
							s += Const.ESC_BOLD + Const.ESC_PURPLE + ". "
									+ Const.ESC_STD;
						else
							s += ". ";
					} else
						s += gfield[row][diagonal] + " ";
				} else if (diagonal < 6)
					s += "  ";
			}
			if (row >= 0 && row <= 3)
				s += +(row + 11);
			s += "\n";
		}
		s += "             0 1 2 3 4 5 6 7 8 9 10\n";
		Game.debug("Gebe Spielbrett aus");
		return s;
	}

	/**
	 * Gibt eine echte Kopie des Spielbretts zur&uuml;ck. Alle Felder werden
	 * hierf&uuml;r kopiert und die neuen Referenzen angepasst.
	 * 
	 * @return Referenz auf eine Kopie des Spielbrettes
	 */
	@Override
	public Board clone() {
		Board newBoard = new Board(false);
		for (int row = 0; row < 11; row++) {
			for (int diagonal = 0; diagonal < 15; diagonal++) {
				if (gfield[row][diagonal] != null)
					/* kopiere einzeln alle Feldeintraege */
					newBoard.setField(row, diagonal, gfield[row][diagonal]
							.clone());
			}
		}

		/* setze alle Referenzen zu den angrenzenden Feldern neu */
		newBoard.setNeighborReferences();

		/*
		 * Sorge dafuer, dass der naechste Spieler am Zug auch der richtige ist
		 * (also der naechste Spieler des aktuellen Boards
		 */
		newBoard.nextPlayer = nextPlayer;

		newBoard.state = new Status(state.getValue()); // uebernehme den
		// Boardstatus
		return newBoard; // gebe Feld zurueck
	}

	// ---------------------------------------------------------------

	/**
	 * Die Referenzen jedes Nachbarfeldes (Nord-Ost, Ost, Suedost, ...) werden
	 * von jedem Spielfeld gesetzt, sofern sie existieren.
	 */
	private void setNeighborReferences() {
		for (int row = 0; row < 11; row++) {
			for (int diagonal = 0; diagonal < 15; diagonal++) {
				if (gfield[row][diagonal] != null) {
					if (row < 10) {
						if (gfield[row + 1][diagonal] != null)
							gfield[row][diagonal]
									.setNorthWest(gfield[row + 1][diagonal]);
						if (diagonal < 14
								&& gfield[row + 1][diagonal + 1] != null)
							gfield[row][diagonal]
									.setNorthEast(gfield[row + 1][diagonal + 1]);
					}
					if (diagonal > 0 && gfield[row][diagonal - 1] != null)
						gfield[row][diagonal]
								.setWest(gfield[row][diagonal - 1]);
					if (diagonal < 14 && gfield[row][diagonal + 1] != null)
						gfield[row][diagonal]
								.setEast(gfield[row][diagonal + 1]);
					if (row > 0) {
						if (diagonal > 0
								&& gfield[row - 1][diagonal - 1] != null)
							gfield[row][diagonal]
									.setSouthWest(gfield[row - 1][diagonal - 1]);
						if (gfield[row - 1][diagonal] != null)
							gfield[row][diagonal]
									.setSouthEast(gfield[row - 1][diagonal]);
					}
				}
			}
		}
		Game.debug("Nachbarreferenzen gesetzt");
	}

	/**
	 * Einzelne Felder des grossen Spielfeldes werden erzeugt (jeweils
	 * Reihenweise nur bis zu den Spielfeldgrenzen) und in das Array an
	 * passender Stelle gespeichert. Es werden weiterhin alle Spielsteine der
	 * jeweiligen Farben in ihre Starth&auml;user gesetzt. Die Feldklassen jedes
	 * Feldes werden berechnet und dem Feld hinzugef&uuml;gt, die H&auml;user
	 * werden als solche markiert.
	 */
	private void createFields() {
		int lb[] = { 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6 };
		int ub[] = { 10, 11, 12, 13, 14, 14, 14, 14, 14, 14, 14 };
		for (int row = 0; row < 11; row++) {
			for (int diagonal = lb[row]; diagonal <= ub[row]; diagonal++) {
				Field nfield = new Field(row, diagonal);
				gfield[row][diagonal] = nfield;
				if (row % 2 == 0)
					nfield.setFieldclass((diagonal % 2) + 1);
				else
					nfield.setFieldclass(((diagonal + 1) % 2) + 3);
			}
		}
		Game.debug("Spielbrett mit Feldern initialisiert");

		/* Hier werden die blauen Spielsteine ins Starthaus gesetzt */
		for (int h = 4, x = 0; x <= 4; x++, h--) {
			for (int y = 0; y <= h; y++)
				gfield[y][x].setValue(Field.BLUE);
		}
		Game.debug("Blaue Kegel wurden auf Startposition gesetzt");

		/* Initialisiere das blaue Haus */
		for (Coord x : Const.BLUE_HOUSE_COORD)
			gfield[x.getRow()][x.getDiagonal()].setHouse(Field.BLUE_HOUSE);

		/* Initialisiere das rote Haus */
		for (Coord x : Const.RED_HOUSE_COORD)
			gfield[x.getRow()][x.getDiagonal()].setHouse(Field.RED_HOUSE);

		gfield[10][10].setHouse(Field.BOTH_HOUSE); // Position beider Haueser
		Game.debug("Haeuser sind gesetzt");

		/*
		 * Hier entsprechend die roten, wobei hier mehrere Fallunterscheidungen
		 * zu machen sind, aufgrund der Symmetrie des Spielfeldes.
		 */
		for (int h = 0, x = 6; x <= 9; x += 2, h++) {
			for (int y = 0; y <= h; y++)
				gfield[y][x].setValue(Field.RED);
			for (int y = 0; y <= h; y++)
				gfield[y][x + 1].setValue(Field.RED);
		}
		for (int y = 0; y <= 2; y++)
			gfield[y][10].setValue(Field.RED);
		gfield[1][11].setValue(Field.RED);
		gfield[2][11].setValue(Field.RED);
		gfield[2][12].setValue(Field.RED);
		gfield[3][12].setValue(Field.RED);
		gfield[3][13].setValue(Field.RED);
		gfield[4][14].setValue(Field.RED);
		Game.debug("Rote Kegel wurden auf Startposition gesetzt");
	}
	
	/**
	 * Liefere letzte Position eines Zuges zur&uuml;ck
	 * 
	 * @param move
	 *            Zug
	 * @return Letzte Position
	 */
	private Move getLastMove(Move move) {
		while (move.getNext() != null)
			move = move.getNext();
		return move;
	}
	
	// ---------------------------------------------------------------

	/**
	 * F&uuml;hrt den Zug aus, der &uuml;bergeben wird. Falls es zu einem
	 * Problem kommt, wird der Status des Bretts entsprechend gesetzt und der
	 * Zug nicht ausgef&uuml;hrt.
	 * 
	 * @param move
	 *            Auszuf&uuml;hrender Zug
	 */
	public synchronized void makeMove(Move move) {
		/* Zug nur moeglich, wenn noch niemand gewonnen hat */
		if (state.isREDWIN() || state.isBLUEWIN() || state.isDRAW()
				|| state.isERROR()) {
			Game.debug("Spiel schon vorbei");
			return;
		}

		/*
		 * es wird geprueft ob der Zug gueltig ist und der Wert des Status
		 * gespeichert
		 */
		Status newState = testMove(move, false);

		/* falls er nicht gueltig war, breche ab und passe den Status an */
		if (newState.isILLEGAL()) {
			state.setValue(newState.getValue());
			Game.debug("Zug wird nicht ausgefuehrt");
			return;
		}

		/* ist das Spiel zu Ende */
		if (newState.isREDWIN() || newState.isBLUEWIN())
			return;

		nextPlayer(); // setze naechsten als aktiven Spieler

		Game.debug("Zug ausgefuehrt. Naechster Spieler ist dran");

		/* Teste ob eine unentschieden Situatsion erreicht ist */
		MoveList moveList = new MoveList(this, nextPlayer);
		if (moveList.isEmpty())
			state.setValue(Status.DRAW);
	}

	/**
	 * Diese Methode wird nach aussen weitergegeben. Sie erm&ouml;glicht das
	 * testen eines Zuges auf Richtigkeit.
	 * 
	 * @param move
	 *            Zu pr&uuml;fender Zug
	 * @return Ergebnis dieses Zuges
	 */
	public Status testMove(Move move) {
		return testMove(move, true);
	}

	/**
	 * Pr&uuml;fe ob der Zug nach Spielregeln g&uuml;ltig ist und wie sich die
	 * Spielsituation &auml;ndern wird. Falls {@code justtest} auf {@code true}
	 * gesetzt wird, werden keine &Auml;nderungen am Spielfeld vorgenommen.
	 * Andernfalls wird der neue Boardstatus gesetzt.
	 * 
	 * @param move
	 *            Zu pr&uuml;fender Zug
	 * @param justTest
	 *            Soll nur getestet werden
	 * @return Ergebnis dieses Zuges
	 */
	private synchronized Status testMove(Move move, boolean justTest) {
		if (!isField(move.getRow(), move.getDiagonal())) {
			if (!justTest)
				Game.debug("Spielfeld wurde verlassen");
			return new Status(Status.ILLEGAL);
		}
		if (!isAPin(move.getRow(), move.getDiagonal())) {
			if (!justTest)
				Game.debug("Kein Pin vorhanden");
			return new Status(Status.ILLEGAL);
		}
		if (!checkCorrectPlayer(move)) {
			if (!justTest)
				Game.debug("Falscher Spielkegel (nicht der richtige Spieler)");
			return new Status(Status.ILLEGAL);
		}
		if (!checkMove(move, justTest)) {
			if (!justTest)
				Game.debug("Zug ist nicht gueltig");
			return new Status(Status.ILLEGAL);
		}

		/*
		 * Versetze den Kegel. Speichere hierzu den Wert des Ursprungsfeldes und
		 * ersetze das Zielfeld durch diesen Wert. Das Zielfeld muss leer sein,
		 * was zuvor auch geprueft wird
		 */
		int oldPosValue = gfield[move.getRow()][move.getDiagonal()].getValue();
		gfield[move.getRow()][move.getDiagonal()].setValue(Field.EMPTY);
		gfield[getLastMove(move).getRow()][getLastMove(move).getDiagonal()]
				.setValue(oldPosValue);

		/*
		 * Berechne Gewinnstatus nach dem Zug (Rot gewinnt, Blau gewinnt oder
		 * Zug war einfach nur OK)
		 */
		Status newState = isWon();

		/*
		 * Falls nur getestet werden soll, mach den Zug rueckgaengig, d.h. Kegel
		 * zuruecksetzen
		 */
		if (justTest) {
			gfield[move.getRow()][move.getDiagonal()].setValue(oldPosValue);
			gfield[getLastMove(move).getRow()][getLastMove(move).getDiagonal()]
					.setValue(Field.EMPTY);
		} else { // sonst neuen Status setzen
			state.setValue(newState.getValue());
			Game.debug("Neuer Status: " + state);
		}

		return newState;
	}

	/**
	 * 
	 * Pr&uuml;fe ob der Zug g&uuml;ltig ist. Hierzu werden alle
	 * Zwischenschritte auf G&uuml;ltigkeit &uuml;berpr&uuml;ft.
	 * 
	 * @param move
	 *            zu machender Zug (Move Objekt)
	 * @return Ist der Zug g&uuml;ltig
	 */
	private boolean checkMove(Move move, boolean justTest) {
		if (move.getNext() == null) // falls kein naechstes Element existiert,
			// ist es kein Zug (da nur eine Koordinate)
			return false;
	
		int nextRow = move.getNext().getRow(); // speichere naechste Zeile
		int nextDiagonal = move.getNext().getDiagonal(); // speichere naechste
		// Diagonale
	
		/*
		 * Pruefe ob der Kegel geschoben wird, d.h. die Zielposition ist ein
		 * angrenzendes Nachbarfeld des Ausgangsfeldes
		 */
		Field now = gfield[move.getRow()][move.getDiagonal()];
		Field neighbor;
		for (int i = 0; i < 6; i++) {
			switch (i) {
			case 0:
				neighbor = now.getNorthEast();
				break;
			case 1:
				neighbor = now.getEast();
				break;
			case 2:
				neighbor = now.getSouthEast();
				break;
			case 3:
				neighbor = now.getNorthWest();
				break;
			case 4:
				neighbor = now.getWest();
				break;
			case 5:
			default:
				neighbor = now.getSouthWest();
				break;
			}
			/*
			 * existiert ein Nachbar des aktuellen Feldes und stimmt ein Nachbar
			 * mit dem naechsten Feld ueberein, dann kann dahin geschoben
			 * werden, wenn dieses Feld leer ist
			 */
			if (neighbor != null && nextRow == neighbor.row
					&& nextDiagonal == neighbor.diagonal
					&& neighbor.getValue() == Field.EMPTY)
				return true;
		}
		if (!justTest)
			Game.debug("Kegel wird nicht geschoben");
	
		/* Ab hier wird geprueft, ob ein korrekter Sprung vorliegt */
	
		List<Coord> passedCoords = new LinkedList<Coord>();
		/* pruefe dazu so lange, bis kein naechster Zug mehr folgt */
		while (move != null) {
	
			/*
			 * war das Feld schonmal besucht, d.h. gehe alle gemachten
			 * Zwischenschritte durch und kontrolliere, ob die neue Positionen
			 * mit keiner vorherigen uebereinstimmt
			 */
			if (!passedCoords.isEmpty()) {
				Coord mypos = new Coord(move.getRow(), move.getDiagonal());
				for (Iterator<Coord> itr = passedCoords.iterator(); itr
						.hasNext();) {
					if (itr.next().equals(mypos)) {
						if (!justTest)
							Game.debug("Feld doppelt besucht");
						/*
						 * Zug ist jetzt schon nicht mehr gueltig und muss nicht
						 * weiter geprueft werden
						 */
						return false;
					}
				}
			}
	
			/* Am Ende angekommen, dann war der Zug wohl richtig */
			if (move.getNext() == null)
				return true;
	
			/* existiert das naechste Feld und stimmen die Feldklassen ueberein */
			if (!isJump(move, move.getNext(), justTest)) {
				if (!justTest)
					Game.debug("Kein gueltiger Sprung");
				return false;
			}
			/* Fuege die Position zur Liste hinzu */
			passedCoords.add(new Coord(move.getRow(), move.getDiagonal()));
			move = move.getNext();
		}
		return true;
	}

	/**
	 * &Uuml;berpr&uuml;ft ob der Spieler auch einen Spieler seiner Farbe setzen
	 * will.
	 * 
	 * @param move
	 *            Zug der zu machen ist
	 * @return Ist der Zug vom richtigen Spieler
	 */
	private boolean checkCorrectPlayer(Move move) {
		/* pruefe ob der Pin zum aktuellen Spieler gehoert */
		if (nextPlayer == Const.PLAYER_RED
				&& gfield[move.getRow()][move.getDiagonal()].getValue() == Field.RED)
			return true;
		if (nextPlayer == Const.PLAYER_BLUE
				&& gfield[move.getRow()][move.getDiagonal()].getValue() == Field.BLUE)
			return true;
		return false;
	}

	/**
	 * Setze den n&auml;chsten Spieler. Das wird verwendet um sicherzustellen,
	 * dass jeder Spieler stets seine eigenen Figuren verwendet.
	 */
	private void nextPlayer() {
		/* setze aktiven Spieler auf den entsprechend anderen */
		nextPlayer = !nextPlayer;
	}

	/**
	 * &Uuml;berpr&uuml;ft ob das aktuelle Feld im Spielfeld liegt.
	 * 
	 * @param row
	 *            Reihe des Feldes
	 * @param diagonal
	 *            Diagonale des Feldes
	 * @return Ist dieses Feld im Spielfeld
	 */
	public boolean isField(int row, int diagonal) {
		/*
		 * ueberpruefe, ob das Feld im Array liegt und im naechsten Schritt, ob
		 * es im gueltigen Spielbereich liegt, also eine Referenz auf ein Feld
		 * existiert
		 */
		if (row >= 0 && row <= 10 && diagonal >= 0 && diagonal <= 14)
			if (gfield[row][diagonal] != null)
				return true;
		return false;
	}

	/**
	 * 
	 * Pr&uuml;fe ob ein Teilsprung &uuml;berhaupt g&uuml;ltig ist. D.h. ob das
	 * direkt anliegende Feld mit einem Spieler besetzt und das darauf in der
	 * gleichen Richtung folgende Feld leer ist.
	 * 
	 * @param now
	 *            Aktuelle Position
	 * @param next
	 *            N&auml;chste Position
	 * @param justTest
	 *            Soll der Zug nur getestet werden
	 * @return Ist der Teilsprung g&uuml;ltig
	 */
	private boolean isJump(Move now, Move next, boolean justTest) {
		if (!isField(next.getRow(), next.getDiagonal())) {
			if (!justTest)
				Game.debug("Sprung verlaesst den Spielbereich");
			return false;
		}
		if (gfield[now.getRow()][now.getDiagonal()].getFieldclass() != gfield[next
				.getRow()][next.getDiagonal()].getFieldclass()) {
			if (!justTest)
				Game.debug("Feldklassen stimmen nicht ueberein");
			return false;
		}
		/*
		 * Kontrolliere alle moeglichen Sprungrichtungen, indem geprueft wird,
		 * ob die Zielposition das Feld in zweifach nord-oestlicher, oestlicher,
		 * suedoestlicher, nord-westlicher, ... Richtung ist UND das Zielfeld
		 * leer ist und das Feld dazwischen von einem Pin besetzt ist
		 */
		Field fNow = gfield[now.getRow()][now.getDiagonal()];
		if (next.getRow() > now.getRow()) { // liegt darueber
			if (next.getDiagonal() == now.getDiagonal()) { // links daneben
				if (fNow.getNorthWest() != null
						&& fNow.getNorthWest().getValue() != Field.EMPTY
						&& fNow.getNorthWest().getNorthWest() != null
						&& fNow.getNorthWest().getNorthWest().getValue() == Field.EMPTY
						&& fNow.getNorthWest().getNorthWest().row == next
								.getRow()
						&& fNow.getNorthWest().getNorthWest().diagonal == next
								.getDiagonal())
					return true;
				return false;
			} else if (next.getDiagonal() > now.getDiagonal()) { // rechts
				// daneben
				if (fNow.getNorthEast() != null
						&& fNow.getNorthEast().getValue() != Field.EMPTY
						&& fNow.getNorthEast().getNorthEast() != null
						&& fNow.getNorthEast().getNorthEast().getValue() == Field.EMPTY
						&& fNow.getNorthEast().getNorthEast().row == next
								.getRow()
						&& fNow.getNorthEast().getNorthEast().diagonal == next
								.getDiagonal())
					return true;
				return false;
			}
		} else if (next.getRow() == now.getRow()) { // liegt in der gleichen
			// Zeile
			if (next.getDiagonal() < now.getDiagonal()) { // links daneben
				if (fNow.getWest() != null
						&& fNow.getWest().getValue() != Field.EMPTY
						&& fNow.getWest().getWest() != null
						&& fNow.getWest().getWest().getValue() == Field.EMPTY
						&& fNow.getWest().getWest().row == next.getRow()
						&& fNow.getWest().getWest().diagonal == next
								.getDiagonal())
					return true;
				return false;
			} else if (next.getDiagonal() > now.getDiagonal()) { // rechts
				// daneben
				if (fNow.getEast() != null
						&& fNow.getEast().getValue() != Field.EMPTY
						&& fNow.getEast().getEast() != null
						&& fNow.getEast().getEast().getValue() == Field.EMPTY
						&& fNow.getEast().getEast().row == next.getRow()
						&& fNow.getEast().getEast().diagonal == next
								.getDiagonal())
					return true;
				return false;
			}
		} else { // liegt darunter
			if (next.getDiagonal() < now.getDiagonal()) { // links daneben
				if (fNow.getSouthWest() != null
						&& fNow.getSouthWest().getValue() != Field.EMPTY
						&& fNow.getSouthWest().getSouthWest() != null
						&& fNow.getSouthWest().getSouthWest().getValue() == Field.EMPTY
						&& fNow.getSouthWest().getSouthWest().row == next
								.getRow()
						&& fNow.getSouthWest().getSouthWest().diagonal == next
								.getDiagonal())
					return true;
				return false;
			} else if (next.getDiagonal() == now.getDiagonal()) { // rechts
				// daneben
				if (fNow.getSouthEast() != null
						&& fNow.getSouthEast().getValue() != Field.EMPTY
						&& fNow.getSouthEast().getSouthEast() != null
						&& fNow.getSouthEast().getSouthEast().getValue() == Field.EMPTY
						&& fNow.getSouthEast().getSouthEast().row == next
								.getRow()
						&& fNow.getSouthEast().getSouthEast().diagonal == next
								.getDiagonal())
					return true;
				return false;
			}
		}
		if (!justTest)
			Game.debug("Kein Fall beim Springen eingetreten");
		return false;
	}

	/**
	 * Kontrolliert ob das Haus des entsprechenden Spielers mit 15 Kegeln
	 * gef&uuml;llt ist, also voll ist.
	 * 
	 * @param playerCode
	 *            Spielerfarbe
	 * @return Ist voll
	 */
	private boolean isHouseFull(boolean playerCode) {
		if (playerCode == Const.PLAYER_RED) {
			for (Coord x : Const.RED_HOUSE_COORD)
				if (gfield[x.getRow()][x.getDiagonal()].getValue() == Field.EMPTY)
					return false;
			return true;
		} else {
			for (Coord x : Const.BLUE_HOUSE_COORD)
				if (gfield[x.getRow()][x.getDiagonal()].getValue() == Field.EMPTY)
					return false;
			return true;
		}
	}

	/**
	 * Erkenne Spielsituation und liefere zur&uuml;ck, ob das Spiel zu Ende ist.
	 * 
	 * @return Ist das Spiel zu Ende
	 */
	private Status isWon() {
		/*
		 * sind beide Haeuser voll, hat der Spieler gewonnen, der den Zug
		 * ausgefuehrt hat
		 */
		if (isHouseFull(Const.PLAYER_RED) && isHouseFull(Const.PLAYER_BLUE)) {
			if (nextPlayer == Const.PLAYER_RED) {
				Game.debug("Rot gewinnt!");
				return new Status(Status.REDWIN);
			} else {
				Game.debug("Blau gewinnt!");
				return new Status(Status.BLUEWIN);
			}
		} else if (isHouseFull(Const.PLAYER_RED)) { // ist nur das rote Haus
			// voll, hat rot gewonnen
			Game.debug("Rot gewinnt!");
			return new Status(Status.REDWIN);
		} else if (isHouseFull(Const.PLAYER_BLUE)) { // ist nur das blaue Haus
			Game.debug("Blau gewinnt!");
			return new Status(Status.BLUEWIN);
		}
		return new Status(Status.OK);
	}

	/**
	 * &Uuml;berpr&uuml;ft ob auf dem Feld ein Kegel vorhanden ist.
	 * 
	 * @param row
	 *            Reihe des Feldes
	 * @param diagonal
	 *            Diagonale des Feldes
	 * @return Ist hier ein Kegel der bewegt werden kann
	 */
	private boolean isAPin(int row, int diagonal) {
		/* ist das gesuchte Feld nicht leer */
		if (gfield[row][diagonal].getValue() != Field.EMPTY)
			return true;
		return false;
	}
	
	// ---------------------------------------------------------------
	
	/**
	 * Klassenvariable f&uuml;r das Feld
	 */
	private Field[][] gfield; 
	/**
	 * Status des Boards
	 */
	private Status state;

	// ---------------------------------------------------------------
	
	/**
	 * Farbe des n&auml;chsten Spielers
	 */
	private boolean nextPlayer = Const.PLAYER_RED;
}