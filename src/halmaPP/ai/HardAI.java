package halmaPP.ai;

import halmaPP.player.*;

import halmaPP.board.*;

import halmaPP.game.*;

import halmaPP.preset.*;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * 
 * Eine schwere KI, die die Spielz&uuml;ge aus einer Liste aller
 * Z&uuml;ge bewertet und und f&uuml;r den besten rekursiv bis zu 
 * einer voreingestellten Tiefe auch gegnerische Z&uuml; generiert.
 * Dabei wird einem Zug eine zus&auml;tzliche Gewichtung hinzugef&uuml;gt. Abh&auml;ngig
 * davon, ob zum Zielhaus oder davon weg gezogen wird, vergr&ouml;ssert sich der Wert,
 * bzw. verkleinert sich dieser.
 * 
 * @author Eduard Dreger, Philip Langer, Dominick Leppich
 * @version 13.37
 */

public class HardAI extends AIPlayer {

	/**
	 * Erzeugt eine schwere KI, die den bestbewerteten Zug generiert.
	 * 
	 * @param playerCode
	 *            Spielerfarbe
	 * @param board
	 *            Board auf dem gespielt werden soll
	 * @param withGUI
	 *            Einstellung, ob &uuml;ber die GUI gespielt wird oder die
	 *            Konsole
	 */
	public HardAI(boolean playerCode, Board board, boolean withGUI) {
		super(playerCode, board, withGUI);
	}

	public HardAI(boolean playerCode, Board board, boolean withGUI,
			boolean myTurn) {
		super(playerCode, board, withGUI, myTurn);
	}

	// ---------------------------------------------------------------

	/**
	 * 
	 * Liefert dem Board den besten Zug zur&uuml;ck.
	 */
	public Move getMove() throws Exception, RemoteException {
		/* Zug zuruecksetzen */
		bestMove = null;
		bestScore = Integer.MIN_VALUE;
		
		/* Teile die MoveList auf vier Listen auf */

		MoveList firstList = new MoveList(board, playerCode);
		ArrayList<MoveAndState> list1 = new ArrayList<MoveAndState>();
		ArrayList<MoveAndState> list2 = new ArrayList<MoveAndState>();
		ArrayList<MoveAndState> list3 = new ArrayList<MoveAndState>();
		ArrayList<MoveAndState> list4 = new ArrayList<MoveAndState>();
		int count = 0;
		for (MoveAndState item : firstList) {
			if (winSituation(item.getState(), playerCode))
				return item.getMove();
			switch (count) {
			case 0:
				list1.add(item);
				break;
			case 1:
				list2.add(item);
				break;
			case 2:
				list3.add(item);
				break;
			case 3:
				list4.add(item);
				break;
			}
			count++;
			count %= 4;
		}

		/* Rechne in 4 Threads parallel*/
		
		thread1 = new AIThread(list1);
		thread2 = new AIThread(list2);
		thread3 = new AIThread(list3);
		thread4 = new AIThread(list4);

		thread1.start();
		thread2.start();
		thread3.start();
		thread4.start();

		thread1.join();
		thread2.join();
		thread3.join();
		thread4.join();

		/* Gebe besten Zug zurueck */
		Game.debug("Zug ist:" + bestMove);
		lastMove = bestMove;
		return bestMove;
	}

	class AIThread extends Thread {
		public AIThread(ArrayList<MoveAndState> list) {
			this.list = list;
		}

		public void run() {
			
			/* Falls der Zug zur vorherigen Position fuehrt, ignoriere ihn */
			
			for (MoveAndState item : list) {
				Move tempMove = item.getMove();
				if (lastMove != null && endMove(tempMove).getRow() == lastMove.getRow()
						&& endMove(tempMove).getDiagonal() == lastMove.getDiagonal())
					continue;

				/* Beginne Rekursion mit eigenem Move und MoveList fuer die gegnerischen Zuege */
				
				Board cloneBoard = board.clone();
				cloneBoard.makeMove(item.getMove());
				enemRecursive(globalAIDepth * 2, cloneBoard, item.getMove(),
						Score(playerCode, board, item.getMove()));
			}
		}

		private ArrayList<MoveAndState> list;
	}

	/**
	 * Beim Aufruf dieser Methode werden alle laufenden Threads der HardAI
	 * unterbrochen.
	 */
	
	public void foundBest() {
		thread1.interrupt();
		thread2.interrupt();
		thread3.interrupt();
		thread4.interrupt();
	}

	// ---------------------------------------------------------------
	
	/**
	 * Diese Methode gewichtet den eigenen Zug und ruft rekursiv die Methode 
	 * enemRecursive(int depth, Board board, Move madeMove, int score) auf.
	 * 
	 * @param depth
	 *            Suchtiefe
	 * @param board
	 *            Board von dem ausgehend die Bewertungen gemacht werden und die Z&uuml; ausgef&uuml;hrt werden.
	 * @param madeMove
	 * 			  Der aktuell bewertete Zug
	 * @param score
	 * 			 Die derzeitige Bewertung f&uuml; den Move
	 */

	public void myRecursive(int depth, Board board, Move madeMove, int score) {
		MoveList myList = new MoveList(board, playerCode);
		
		/* Gehe alle eigenen Zuege durch */
		for (MoveAndState item : myList) {

			Board cloneBoard = board.clone();
			cloneBoard.makeMove(item.getMove());

			int calcScore = Score(playerCode, board, item.getMove());
			enemRecursive(depth - 1, cloneBoard, madeMove, score + calcScore);
		}
	}

	/**
	 * Diese Methode gewichtet den gegnerischen Zug und ruft rekursiv die Methode 
	 * myRecursive(int depth, Board board, Move madeMove, int score) auf.
	 * 
	 * @param depth
	 *            Suchtiefe
	 * @param board
	 *            Board von dem ausgehend die Bewertungen gemacht werden und die Z&uuml; ausgef&uuml;hrt werden.
	 * @param madeMove
	 * 			  Der aktuell bewertete Zug
	 * @param score
	 * 			 Die derzeitige Bewertung f&uuml; den Move
	 */
	public void enemRecursive(int depth, Board board, Move madeMove, int score) {
		int enemScore = 0;
		int differenceScore = score - enemScore;

		/* Hier wird aufgehoert */
		if (depth == 0) {
			if (differenceScore > bestScore) {
				bestScore = score - enemScore;
				bestMove = madeMove;
				Game.debug("Besseren Zug gefunden");
			}
			return;
		}

		MoveList enemList = new MoveList(board, !playerCode);
		for (MoveAndState item : enemList) {

			/* Wenn Gegner mit dem Zug gewinnen kann, ignoriere den Zug */
			if (winSituation(item.getState(), !playerCode))
				return;

			Board cloneBoard = board.clone();
			cloneBoard.makeMove(item.getMove());

			/* Nimm nur den besten darauf folgenden Zug des Gegners */
			int calcScore = Score(!playerCode, board, item.getMove());
			myRecursive(depth - 1, cloneBoard, madeMove, score - calcScore);
		}
	}

	/**
	 * 
	 * Diese Methode ermittelt das letzte Element eines Move-Objekts. Es wird
	 * next auf ein vorhandenes Folge-Move-Objekt &uuml;berpr&uuml;ft. Falls
	 * kein Folge-Objekt existiert, ist es das letzte Element und wird
	 * zur&uuml;ckgegeben.
	 * 
	 * @param move
	 *            Das Move-Objekt, das durchsucht werden soll
	 */
	public Move endMove(Move move) {
		while (move.getNext() != null)
			move = move.getNext();
		return move;
	}

	/**
	 * Diese Methode &uuml;berpr&uuml;ft, ob die Liste einen Zug enth&auml;lt,
	 * der zum Sieg f&uuml;rt.
	 */
	public boolean winSituation(Status state, boolean playerCode) {
		if (state.isBLUEWIN() && playerCode == Const.PLAYER_BLUE)
			return true;
		if (state.isREDWIN() && playerCode == Const.PLAYER_RED)
			return true;
		return false;
	}

	/**
	 * Diese Methode bewertet einen Zug anhand seiner Position auf dem Spielfeld
	 * und seiner Klasse. Sowie den Richtungen in die der Zug geht.
	 * 
	 * @param playerCode
	 * 				Farbe des Spielers f&uuml;r den der Score erstellt werden soll
	 * @param board
	 *            Board von dem die Positionen der Kegel geholt werden soll
	 * @param move 
	 * 				Der aktuelle Move, der gemacht werden soll
	 */
	public int Score(boolean playerCode, Board board, Move move) {
		
		/* Erstelle eine Liste mit allen Pins fuer den Spieler (playerCode) */
		
		CoordList pins = board.getPins(playerCode);
		int position = 0;

		if (playerCode == Const.PLAYER_RED) {
			/* Nimm die Standardbewertung*/
			for (Coord item : pins)
				position += 2 * item.getRow() - item.getDiagonal() + 10;
			
			/* Addiere zu jedem Move abhaengig von seiner Richtung eine Gewichtung */ 
			while (move.getNext() != null) {
				/* Falls bereits im Haus und der Sprung aus dem Haus herausgeht */
				if (Score(playerCode, move) > 19
						&& Score(playerCode, move.getNext()) < 20)
					position -= inHouse * 3;
				
				/* Falls bereits im Haus und der Sprung im Haus bleibt */
				else if (Score(playerCode, move) > 19
						&& Score(playerCode, move.getNext()) > 19)
					position -= 5;
				
				/* Falls Du durch den Zug ins Haus gelangst */
				else if (Score(playerCode, move.getNext()) > 19)
					position += inHouse;
				/* Sprung nach Nordwest */
				else if (move.getDiagonal() == move.getNext().getDiagonal()
						&& move.getRow() < move.getNext().getRow()) {
					if (move.getRow() + 2 == move.getNext().getRow())
						position += redNorthWest;
					position += redNorthWest;
				}
				/* Sprung nach Westen */
				else if (move.getDiagonal() > move.getNext().getDiagonal()
						&& move.getRow() == move.getNext().getRow()) {
					if (move.getDiagonal() - 2 == move.getNext().getDiagonal())
						position += redWest;
					position += redWest;
				}
				/* Sprung nach Suedwesten */
				else if (move.getDiagonal() > move.getNext().getDiagonal()
						&& move.getRow() > move.getNext().getRow()) {
					if (move.getDiagonal() - 2 == move.getNext().getDiagonal())
						position += redSouthWest;
					position += redSouthWest;
				}
				/* Sprung nach Suedosten */
				else if (move.getDiagonal() == move.getNext().getDiagonal()
						&& move.getRow() > move.getNext().getRow()) {
					if (move.getRow() - 2 == move.getNext().getRow())
						position += redSouthEast;
					position += redSouthEast;
				}
				/* Sprung nach Osten */
				else if (move.getDiagonal() < move.getNext().getDiagonal()
						&& move.getRow() == move.getNext().getRow()) {
					if (move.getDiagonal() + 2 == move.getNext().getDiagonal())
						position += redEast;
					position += redEast;
				}
				/* Sprung nach Nordosten */
				else if (move.getDiagonal() < move.getNext().getDiagonal()
						&& move.getRow() < move.getNext().getRow()) {
					if (move.getDiagonal() + 2 == move.getNext().getDiagonal())
						position += redNorthEast;
					position += redNorthEast;
				} else
					return -10;
				move = move.getNext();
			}
		} else {
			for (Coord item : pins)

				position += item.getRow() + item.getDiagonal();

			while (move.getNext() != null) {
				/* Falls bereits im Haus und der Sprung aus dem Haus herausgeht */
				if (Score(playerCode, move) > 19
						&& Score(playerCode, move.getNext()) < 20)
					position -= inHouse * 3;
				
				/* Falls bereits im Haus und der Sprung im Haus bleibt */
				else if (Score(playerCode, move) > 19
						&& Score(playerCode, move.getNext()) > 19)
					position -= 5;
				
				/* Falls Du durch den Zug ins Haus gelangst */
				else if (Score(playerCode, move.getNext()) > 19)
					position += inHouse;
				
				/* Sprung nach Nordwesten */
				else if (move.getDiagonal() == move.getNext().getDiagonal()
						&& move.getRow() < move.getNext().getRow()) {
					if (move.getRow() + 2 == move.getNext().getRow())
						position += blueNorthWest;
					position += blueNorthWest;
				}
				/* Sprung nach Westen */
				else if (move.getDiagonal() > move.getNext().getDiagonal()
						&& move.getRow() == move.getNext().getRow()) {
					if (move.getDiagonal() - 2 == move.getNext().getDiagonal())
						position += blueWest;
					position += blueWest;
				}
				/* Sprung nach Suedwesten */
				else if (move.getDiagonal() > move.getNext().getDiagonal()
						&& move.getRow() > move.getNext().getRow()) {
					if (move.getDiagonal() - 2 == move.getNext().getDiagonal())
						position += blueSouthWest;
					position += blueSouthWest;
				}
				/* Sprung nach Suedost */
				else if (move.getDiagonal() == move.getNext().getDiagonal()
						&& move.getRow() > move.getNext().getRow()) {
					if (move.getRow() - 2 == move.getNext().getRow())
						position += blueSouthEast;
					position += blueSouthEast;
				}
				/* Sprung nach Osten */
				else if (move.getDiagonal() < move.getNext().getDiagonal()
						&& move.getRow() == move.getNext().getRow()) {
					if (move.getDiagonal() + 2 == move.getNext().getDiagonal())
						position += blueEast;
					position += blueEast;
				}
				/* Sprung nach Nordosten */
				else if (move.getDiagonal() < move.getNext().getDiagonal()
						&& move.getRow() < move.getNext().getRow()) {
					if (move.getDiagonal() + 2 == move.getNext().getDiagonal())
						position += blueNorthEast;
					position += blueNorthEast;
				} else
					return -10;
				move = move.getNext();
			}
		}
		return position;

	}
	/**
	 * Diese Methode bewertet die Stellung des Kegels auf dem Spielfeld, relativ zum Ausgangshaus.
	 * 
	 * @param color 
	 * 			Die Spielerfarbe, der der Zug geh&ouml;rt
	 * @param move
	 * 			Der Zug, der ausgef&uuml;hrt werden soll
	 */

	public int Score(boolean color, Move move) {
		if (color == Const.PLAYER_RED)
			return (2 * move.getRow() - move.getDiagonal() + 10);
		else
			return (move.getRow() + move.getDiagonal());
	}

	// ---------------------------------------------------------------

	private int globalAIDepth = 2;
	private int bestScore = Integer.MIN_VALUE;
	private Move bestMove;
	AIThread thread1, thread2, thread3, thread4;
	private Move lastMove = null;

	// ---------------------------------------------------------------

	private final int redNorthWest = 10;
	private final int redNorthEast = 5;
	private final int redSouthEast = -11;
	private final int redSouthWest = -5;
	private final int redWest = 5;
	private final int redEast = -6;
	private final int inHouse = 25;
	private final int blueNorthWest = 5;
	private final int blueNorthEast = 10;
	private final int blueSouthEast = -5;
	private final int blueSouthWest = -11;
	private final int blueWest = -6;
	private final int blueEast = 5;

}
