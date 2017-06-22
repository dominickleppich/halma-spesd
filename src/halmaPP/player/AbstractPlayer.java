package halmaPP.player;

import java.rmi.RemoteException;

import halmaPP.board.*;
import halmaPP.preset.*;
import halmaPP.game.*;
import halmaPP.gui.*;

/**
 * Abstrakte Spielerklasse.
 * Implementiert die geforderten Methoden und liefert Konstruktoren. 
 * 
 * @author Sebastian Lindner
 */

public abstract class AbstractPlayer implements halmaPP.preset.Player {

	/* KONSTRUKTOR */

	/**
	 * Konstruktor
	 * @param playerCode
	 * 		Spielerfarbe
	 * @param board
	 * 		Spielbrett
	 * @param guiInput
	 * 		Mit GUI?
	 */
	public AbstractPlayer( boolean playerCode, Board board, boolean guiInput) {
			this( playerCode, board, guiInput, true);
			if ( playerCode == Const.PLAYER_BLUE)
				this.expectConfirm = false;
	}
	
	/**
	 * Konstruktor f&uuml;r das Laden/Speichern.
	 * @param playerCode
	 * 		Spielerfarbe
	 * @param board
	 * 		Spielbrett
	 * @param guiInput
	 * 		Mit GUI?
	 * @param myTurn
	 * 		gerade am Zug?
	 */
	public AbstractPlayer( boolean playerCode, Board board, boolean guiInput, boolean myTurn) {
		if ( myTurn)
			this.expectConfirm = true;
		else 
			this.expectConfirm = false;
		this.playerCode = playerCode;
		this.board = board;
		this.guiInput = guiInput;
		String title;
		if (playerCode == Const.PLAYER_RED)
			title = "Spieler: ROT";
		else
			title = "Spieler: BLAU";
		gui = new GUI(playerCode, board, title);
		Game.debug("Spieler wurde erschaffen");
	}

	/* ------------------------------------------------ */

	/* INTERFACE - METHODEN */
	
	/**
	 * Der best&auml;tigte Zug wird auf dem eigenen Spielbrett ausgef&uuml;hrt
	 * und Debug-Messages werden ausgegeben. Liefert dem Spieler im Parameter
	 * boardStatus Informationen &uuml;ber den letzten mit request gelieferten Zug.
	 * @param boardStatus
	 * 		Status
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void confirm( Status boardStatus) throws RemoteException, Exception { 
		/* darf confirm gerade ausgefuehrt werden? */
		if ( !this.expectConfirm)
			throw new Exception( "Falsche Methodenaufruf-Reihenfolge.");

		switch (boardStatus.getValue()) {
		/* Alles in Ordnung */
		case Status.OK:
			Game.debug( "Zug Confirmation: OK!");
			this.board.makeMove( this.requestedMove);
			guiUpdate();
			break;

		/* Rot gewinnt */
		case Status.REDWIN:
			Game.debug( "Zug Confirmation: Rot gewinnt!");
			this.board.makeMove( this.requestedMove);
			guiUpdate();
			break;

		/* Blau gewinnt */
		case Status.BLUEWIN:
			Game.debug( "Zug Confirmation: Blau gewinnt!");
			this.board.makeMove( this.requestedMove);
			guiUpdate();
			break;

		/* Gleichstand */
		case Status.DRAW:
			Game.debug( "Zug Confirmation: Gleichstand!");
			this.board.makeMove( this.requestedMove);
			guiUpdate();
			break;

		/* Illegaler Zug */
		case Status.ILLEGAL:
			Game.debug( "Zug Confirmation: Illegal!");
			break;

		/* ERROR */
		case Status.ERROR:
			throw new IllegalArgumentException( "STATUS = FEHLER");

		/* default */
		default:
			throw new IllegalArgumentException( "UNBEKANNTER WERT");
		}

		/* confirm wurde ausgefuehrt, wird also nicht als naechstes erwartet */
		this.expectConfirm = false;
	}

	/**
	 * Abstrakte Methode f&uuml;r Implementationen, request greift hierauf zur&uuml;ck.
	 * So k&ouml;nnen Computer- und menschliche Spieler auf unterschiedliche Weise Move-Objekte
	 * generieren und dennoch f&uuml;r beide die request Methode verwendet werden.
	 * @return Zug
	 * @throws Exception
	 * @throws RemoteException
	 */
	protected abstract Move getMove() throws Exception, RemoteException;
	
	/**
	 * Die vom Player-Interface verlangte request Methode.
	 * Es wird daf&uuml;r gesorgt, dass alle n&ouml;tigen Werte korrekt gesetzt werden
	 * und das Spielfeld aktualisiert.
	 * @return Move-Objekt
	 * @throws RemoteException
	 * @throws Exception
	 */
	public Move request() throws RemoteException, Exception {
		gui.setActivePlayer( playerCode);
		gui.setActive( true);
		guiUpdate();
		Move move = getMove();
		requestedMove = move;
		gui.setActive( false);
		gui.setActivePlayer( !playerCode);
		return move;
	}
	
	/**
	 * Updatet das Spielfeld eines Spielers, nachdem der andere einen Zug
	 * gemacht hat.
	 * @param opponentMove
	 * 		der Move, der vom Gegner gemacht wurde
	 * @param boardStatus
	 * 		der Spielstatus des Gegners
	 */
	public void update( Move opponentMove, Status boardStatus) throws Exception, RemoteException {
		if ( this.expectConfirm)
			throw new Exception( "Falsche Methodenaufruf-Reihenfolge.");
		Game.debug( "Game-Brett-Status: " + boardStatus);
		
		/* setze den gegnerischen Zug auf eigenem Spielbrett */
		this.board.makeMove( opponentMove);
		guiUpdate();
		Game.debug( "Spieler-Brett-Status: " + this.board.getStatus());
		
		/* ueberpruefe, ob neuer Status gleich dem gegnerischen Status ist */
		if (this.board.getStatus().getValue() != boardStatus.getValue())
			throw new RuntimeException( "STATUS STIMMT NICHT UEBEREIN");
		Game.debug( "Zug Update: Erfolg!");

		/* als naechstes wird ein Aufrug der expect Methode erwartet */
		this.expectConfirm = true;
	}

	/* ------------------------------------------------ */
	
	/* EIGENE METHODEN */
	
	/**
	 * Beendet die GUI dieses Spielers.
	 */
	public void delete() {
		gui.setVisible( false);
	}
	
	/**
	 * Aktualisiert die GUI Anzeige.
	 */
	public void guiUpdate() {
		if ( gui != null)
			gui.repaint();
	}

	/**
	 * Gewinn-Banner wird angezeigt.
	 * 
	 * @param playerCode
	 * 		Spielerfarbe
	 */
	public void setWon( boolean playerCode) {
		gui.setWon( playerCode);
	}

	/* ------------------------------------------------ */

	/* KLASSENVARIABLEN */

	/** letzter Zug, der durch request geliefert wurde */
	protected Move requestedMove;
	/** Spielbrett des Spielers */
	protected Board board;
	/** GUI des Spielers */
	protected GUI gui;
	/** Farbe des Spielers */
	protected boolean playerCode;
	/** mit GUI? */
	protected boolean guiInput;
	/** Sicherheits-boolean, um richtige Methodenaufruf-Reihenfolge zu &uuml;berpruefen */
	protected boolean expectConfirm;
	
}
