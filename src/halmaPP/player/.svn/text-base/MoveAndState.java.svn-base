package halmaPP.player;

import halmaPP.preset.Move;
import halmaPP.preset.Status;
/**
 * Klasse, die ein Move-Objekt mit dem zugeh&öuml;rigen Status versieht und gegebenfalls einem Hinweis, ob es sich um einen Sprung handelt.
 *   
 * @see MoveList
 * @author Sebastian Horwege
 */
public class MoveAndState {
	
	/**
	 * Konstruktor.
	 * @param move
	 * 		Move-Objekt
	 * @param state
	 * 		zugeh&ouml;riger Status
	 * @param isJump
	 * 		handelt es sich um einen Sprung?
	 */
	public MoveAndState(Move move, Status state, boolean isJump) {
		this.isJump = isJump;
		this.move = move;
		this.state = state;
	}
	
	/**
	 * Konstruktor mit "leerem" Status f&uuml;r sp&auml;tere Bearbeitung.
	 * @param move
	 * 		Move-Objekt
	 */
	public MoveAndState(Move move) {
		this.move = move;
		this.state = null;
	
		
	}
	/**
	 * Konstruktor mit Move und "leerem" Status und einem Parameter, der von XMLParse genutzt wird.
	 * Damit kann die Zugdauer, die im XML Dokument gespeichert ist, verarbeitet werden.
	 * @param move
	 * 		Move-Objekt
	 * @param duration
	 * 		Zugdauer
	 */
	public MoveAndState(Move move, long duration) {
		this.move = move;
		this.state = null;
		this.duration = duration;
	}
	
	// ---------------------------------------------------------------
	
	/**
	 * Liefert Move-Objekt eines MoveAndStates-Objekts.
	 */
	public Move getMove() {
		return move;
	}

	/**
	 * Liefert Status eines MoveAndStates-Objekts.
	 * @return Status
	 */
	public Status getState() {
		return state;
	}

	/**
	 * Liefert Hinweis, ob es sich um einen Sprung handelt eines MoveAndStates-Objekts.
	 * @return Sprung?
	 */
	public boolean isJump() {
		return isJump;
	}
	
	/**
	 * Liefert Zugdauer eines MoveAndStates-Objekts.
	 * @return Zugdauer
	 */
	public long getDuration() {
		return duration;
	}

	// ---------------------------------------------------------------
	
	/**
	 * Move-Objekt
	 */
	private Move move;
	/**
	 * Status
	 */
	private Status state;
	/**
	 * handelt es sich um einen Sprung?
	 */
	private boolean isJump;
	/**
	 * Zugdauer
	 */
	private long duration = -1;
	
}
