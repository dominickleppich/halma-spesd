package halmaPP.player;

import halmaPP.board.*;
import halmaPP.game.Const;
import halmaPP.preset.*;
import java.util.ArrayList;

/**
 * Klasse zur Berechnungen aller m&ouml;glichen Zugkombinationen durch
 * rekursiven Abstieg
 * 
 * @author Sebastian Horwege
 */
public class MoveList extends ArrayList<MoveAndState> {
	/**
	 * Erzeugt Liste mit Eintraegen aller moeglichen Moves, Playerunspezifisch
	 * 
	 * @param board
	 *            Auf diesem Board sollen Z&uuml;ge gesucht werden!
	 * 
	 */
	public MoveList(Board board) {
		this.createList(board, Const.PLAYER_BLUE);
		this.createList(board, Const.PLAYER_RED);
	}

	
	/**
	 * Erzeugt eine Liste mit allen m&ouml;glichen Z&uuml;gen die f&uuml;r
	 * diesen Spieler von der eigenen Position gemacht werden k&ouml;nnen.
	 * 
	 * @param board
	 *            Arbeitsbrett
	 * @param playerCode
	 *            Spielerfarbe angegeben als {@code Const.BLUE_PLAYER} oder
	 *            {@code Const.RED_PLAYER}
	 */
	public MoveList(Board board, boolean playerCode) {
		this.createList(board, playerCode);
	}

	
	/**
	 * Erzeugt ein Objekt deren Liste nur Moves des &uuml;bergebenen Startwerts
	 * enth&auml;t
	 * 
	 * @param board
	 * @param move
	 *            Startmove von dem aus gesucht werden soll!
	 */
	public MoveList(Board board, Coord move) {
		this.board = board;
		Move startMove = new Move(move.getRow(), move.getDiagonal());
		addPushMove(startMove);
		addJumpMove(startMove);
	}

	
	/**
	 * F&uuml;gt alle m&ouml;glichen Moves der Liste hinzu, die mit Figuren der
	 * Farbe <code>playerCode</code> m&ouml;glich sind! Diese Funktion wird nur
	 * von Konstrutoren dieser Klasse verwendet
	 * 
	 * @see MoveList#MoveList(Board)
	 * @param board
	 * @param playerCode
	 */
	private void createList(Board board, boolean playerCode) {
		if (board == null)
			return;
		
		this.board = board;
		
		CoordList figures = board.getPins(playerCode);
	
		for (Coord x : figures) { // fuer jeden spielstein des Players
			Move startMove = new Move(x.getRow(), x.getDiagonal());
			this.addPushMove(startMove); // ermittle alle moeglichen Schiebe
											// Zuege
			this.addJumpMove(startMove); // rekursiv alle moeglichen Spruenge
		}
	
	}


	/**
	 * Alle moeglichen jumps von figure werden ermittelt und der liste
	 * hinzugefuegt Methode arbeitet rekursiv
	 * 
	 * @param figure
	 */
	private void addJumpMove(Move figure) {

			
		Move end_node = copyMove(figure);										//arbeite auf Copy vom Move
		Move start_node = end_node;
		while (end_node.getNext() != null)
			end_node = end_node.getNext();

		Status state;

		for (int i = 0; i < 6; i++) {											//durchlaufe alle sechs richtungen in die gesprungen werden kann
			end_node.setNext(new Move(end_node.getRow() + 2 * row_arr[i], end_node.getDiagonal() + 2 * diag_arr[i]));
			state = board.testMove(start_node);
			
			if (!state.isILLEGAL()) {											//entscheide, ob move in die liste kommt
				this.add(new MoveAndState(copyMove(start_node), state, true));
				addJumpMove(copyMove(start_node));
			}
			end_node.setNext(null);						
		}
	}

	
	/**
	 * F&uuml;gt Elemente in die Liste ein, die die von <code>Figure</code> aus
	 * geschoben werden können!
	 * 
	 * @param figure
	 *            Startmove
	 */
	private void addPushMove(Move figure) {

																		//es gibt sechs himmelsrichtungen in die ein Stein geschoben werden koennte
		 
		for (int i = 0; i < 6; i++) {
			Status state = null;

			Move move = new Move(figure);

			move.setNext(new Move(move.getRow() + row_arr[i], move
					.getDiagonal()
					+ diag_arr[i]));
			state = board.testMove(move);

																		// wenn Zug gueltig, fuege hinzu 
			if (!state.isILLEGAL())
				this.add(new MoveAndState(move, state, false));
		}
	}

	/**
	 * Durchl&auml;uft uebergebenes Move Object und erzeugt dabei ein neues Move Objekt mit gleicher Listenstruktur
	 * Rekursiv!
	 * @param startMove
	 *            Move bei dem angefangen werden soll
	 * @return Kopie des Zuges
	 */
	public static Move copyMove(Move startMove) {
		if(startMove == null)
			return null;
		return new Move(startMove.getRow(),startMove.getDiagonal(),copyMove(startMove.getNext()));
	}

	/**
	 * Pr&uuml;fe ob dieser Zug in der Liste vorhanden ist
	 * 
	 * @param move
	 *            Zug
	 * @return Ist vorhanden
	 */
	public boolean contains(Move move) {
		if (this.isEmpty())
			return false;
		for (MoveAndState x : this) {
			if( equalMoves(move, x.getMove()))
				return true;
		}
		return false;
	}
	
	/**
	 * Pr&uuml;ft, ob 2 Moves gleich sind. Wurde auch schon getestet!
	 * Rekursiv
	 * @param move1 Move zum vergleichen mit move2
	 * @param move2 Move zum vergleichen mit move1
	 * @return true, wenn beide Moves &uuml;bereinstimmen
	 */
	public static boolean equalMoves(Move move1, Move move2) {
		if(move1 == null && move2 == null)
			return true;
		if(move1 == null || move2 == null)
			return false;
		if(move1.getRow() == move2.getRow() && move1.getDiagonal() == move2.getDiagonal()) {
			if((move1.getNext() == null && move2.getNext() == null) || equalMoves(move1.getNext(),move2.getNext()))
				return true;
			else
			return false;
		}else
			return false;
				
	}
	
	/**
	 * Erzeugt einen String, der alle Moves der Liste enth&auml;t (durch NewLine
	 * getrennt)
	 */
	public String toString() {
		String s = "";
		for (MoveAndState x : this)
			s += x.getMove() + "\n";
		return s;
	}

	// ---------------------------------------------------------------
	
	/** Board, auf dem gearbeitet wird, muss vom Konstruktor gesetzt werden! */
	private Board board;

	/** Array mit Diagonal Koordinaten ubgebener Felder   */
	private final int[] diag_arr = { 1, 1, 0, -1, -1, 0 };

	/** Array mit Row Koordinaten ubgebener Felder   */
	private final int[] row_arr = { 1, 0, -1, -1, 0, 1 };
	
	// ---------------------------------------------------------------
	
	private static final long serialVersionUID = 1L;
}
