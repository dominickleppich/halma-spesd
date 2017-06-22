package halmaPP.ai;

import halmaPP.board.Board;
import halmaPP.game.Const;
import halmaPP.game.Game;
import halmaPP.preset.*;
import halmaPP.player.*;

import java.util.*;
import java.rmi.RemoteException;

/**
 * Die leichte KI w&auml;hlt einen beliebigen Zug aus einer
 * Liste von m&ouml;glichen generierten Z&uuml;gen aus. Z&uuml;ge,
 * die zum Sieg f&uuml;hren werden favorisiert.
 * 
 * @author Eduard Dreger
 * @version 1.0
 */

public class EasyAI extends AIPlayer {

	/**
	 * Erzeugt eine einfache KI, die einen zuf&auml;lligen zug generiert.
	 * 
	 * @param playerCode
	 *            Spielerfarbe.
	 * @param board
	 *            Brett auf dem der Spieler spielen soll.
	 * @param withGUI
	 *            Einstellung, ob &uuml;ber die GUI gespielt wird oder die
	 *            Konsole.
	 */

	public EasyAI(boolean playerCode, Board board, boolean withGUI) {
		super(playerCode, board, withGUI);
	}
	public EasyAI(boolean playerCode, Board board, boolean withGUI,boolean myTurn) {
		super(playerCode, board, withGUI,myTurn);
	}

	/**
	 * Gibt einen Zug an das Board zur&uuml;ck.
	 */

	public Move getMove() throws Exception, RemoteException {
		Move random = randomMove();

		try {
			Thread.sleep(Const.AI_WAIT_TIME);
		} catch (InterruptedException e) {}
		
		return random;
	}

	/**
	 * Diese Methode gibt einen zuf&auml;lligen Zug zur&uuml;ck.
	 * Es wird eine MoveList erzeugt. Falls es einen Zug gibt, der zum Sieg
	 * f&uuml;hrt, wird dieser zur&uuml;ckgegeben. Ansonsten wird ein
	 * zuf&auml;lliger Zug ermittelt und zur&uuml;ckgegeben.
	 */
	public Move randomMove() {

		MoveList myList = new MoveList(board, playerCode);

		for (MoveAndState item : myList) {
			if (winSituation(item.getState()))
				return item.getMove();
		}

		Random random = new Random();
		Move returnMove = null;
		try {
			int doMove = random.nextInt(myList.size());
			returnMove = myList.get(doMove).getMove();
		} catch (IllegalArgumentException e) {
			Game.debug("Fehler bei Berechnung der Zufallszahl");
		}
		return returnMove;
	}

	/**
	 * Diese Methode &uuml;berpr&uuml;ft, ob dieser Status eine Gewinnsituation
	 * f&uuml;r diesen Spieler hervorruft.
	 * 
	 * @param item
	 *           Der Status eines Zuges.
	 */
	public boolean winSituation(Status item) {
		if (item.isBLUEWIN() && this.playerCode == Const.PLAYER_BLUE)
			return true;
		if (item.isREDWIN() && this.playerCode == Const.PLAYER_RED)
			return true;
		return false;
	}
}
