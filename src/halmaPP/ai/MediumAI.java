package halmaPP.ai;

import halmaPP.player.*;
import halmaPP.board.*;
import halmaPP.game.*;
import halmaPP.preset.*;

import java.rmi.RemoteException;

/**
 * Die mittlere KI erzeugt sich eine Liste aus m&ouml;glichen Z&uuml;gen.
 * Alle Z&uuml;ge werden gegen die daraus f&uuml;r den Gegner reslutierenden
 * Z&uuml;ge gewichtet und der beste ausgew&auml;hlt.
 * 
 * @author Eduard Dreger
 * @version 1.0
 */

public class MediumAI extends AIPlayer {

	/**
	 * Erzeugt eine mittlere KI, die den bestbewerteten Zug generiert.
	 * 
	 * @param playerCode
	 *            Spielerfarbe
	 * @param board
	 *            Board auf dem gespielt werden soll
	 * @param withGUI
	 *            Einstellung, ob &uuml;ber die GUI gespielt wird oder die
	 *            Konsole
	 */
	public MediumAI(boolean playerCode, Board board, boolean withGUI) {
		super(playerCode, board, withGUI);
	}
	
	public MediumAI(boolean playerCode, Board board, boolean withGUI,boolean myTurn) {
		super(playerCode, board, withGUI,myTurn);
	}
	
	// ---------------------------------------------------------------
	
	/**
	 * Liefert dem Board den besten Zug zur&uuml;ck.
	 */
	public Move getMove() throws Exception, RemoteException {
		Move move = scoredMove();
		return move;
	}

	/**
	 * Die Methode liefert den Zug mit der besten Bewertung zur&uuml;ck.
	 * Es wird eine MoveList erzeugt. Falls ein Zug zum Sieg f&uuml;hrt, wird
	 * dieser gew&auml;hlt.<br>
	 * Zus&auml;tzlich werden alle Z&uuml;ge, die zu einem Unentschieden f&uuml;hren
	 * gez&auml;hlt. Falls es genau so viele Z&uuml;ge gibt, wie Elemente in der
	 * Liste, wird der erste Zug der Liste gew&auml;hlt. Ansonsten existieren
	 * andere Z&uuml;ge, die gew&auml;hlt werden k&ouml;nnen. Es werden alle
	 * Z&uuml;ge, die zum Unentschieden f&uuml;hren entfernt und die restlichen
	 * vorhandenen Z&uuml;ge bewertet. Der beste Zug wird gew&auml;hlt.
	 */

	private Move scoredMove() {

		MoveList myList = new MoveList(board, playerCode);										//Eine MoveList fuer den eigenen Spieler wird erstellt

		for (MoveAndState item : myList)														//Alle Moves werden auf einen moeglichen Sieg getestet
			if (winSituation(item.getState()))
				return item.getMove();
				
		for (MoveAndState item : myList) {														//Alle Draws werden entfernt
			if (item.getState().isDRAW() && !myList.isEmpty() && myList.size()>1)
				myList.remove(item);
		}

		Move myTempMove;																		//Tempoeraere Moves fuer eigenen/gegnerischen Spieler
		Move enemTempMove;
		Move move = null;																		//Der Move, der uebergeben wird

		int score, myScore, enemScore, oldWeight, newWeight, oldEnemWeight, newEnemWeight;
		score = Integer.MIN_VALUE;

		for (MoveAndState myItem : myList) {													//Alle Moves aus der eigenne Liste werden durchgelaufen

			Board cloneBoard = board.clone();													//Das urspruengliche Board wird geclont
			oldWeight = weightClass(board, playerCode);											//Alle Kegel gewichten (Klasse), urspruengliche Gewichtung
			
			myTempMove = myItem.getMove();														//Vom Item den Move geholt
			cloneBoard.makeMove(myTempMove);													//Der Move wird auch dem geclonten Board ausgefuehrt

			newWeight = weightClass(cloneBoard, playerCode);									//Alle Kegel gewichten (Klasse), neue Gewichtung

			myScore = Score(playerCode, cloneBoard,												//Eigener Zug wird bewertet
					oldWeight, newWeight);

			MoveList enemList = new MoveList(cloneBoard, !playerCode);							//Fuer den Gegner wird eine MoveList erzeugt, mit dem geclonten Board und dem ausgefuehrten Zug
			boolean enemWins = false;															

			for (MoveAndState enemItem : enemList) {											//Abfrage, ob Gegner gewinnen koennte
				if (winSituation(enemItem.getState())) {
					enemWins = true;															
					break;
				}
			}

			if (enemWins)																		//Falls der Gegner gewinnen koennte, fuehre einen anderen Zug aus
				continue;

			for (MoveAndState enemItem : enemList) {											//Gehe alle Moves vom Gegner durch

				Board enemCloneBoard = cloneBoard.clone();										//Erzeuge fuer den Gegner ein Board vom geclonten Board
				oldEnemWeight = weightClass(cloneBoard, !playerCode);							//Alle Kegel gewichten (Klasse), urspruengliche Gewichtung

				enemTempMove = enemItem.getMove();												//Vom Item den Move geholt
				enemCloneBoard.makeMove(enemTempMove);											//Der Move wird auch dem geclonten Board ausgefuehrt

				newEnemWeight = weightClass(enemCloneBoard, !playerCode);						//Alle Kegel gewichten (Klasse), neue Gewichtung

				enemScore = Score(!playerCode, enemCloneBoard,									//Bewerte den Zug vom Gegner
						oldEnemWeight, newEnemWeight);

				if (myScore - enemScore > score) {												//Wenn die Bewertung von mir, abzueglich der des 
						score = myScore - enemScore;											//Gegners echt groeßer ist, als die gespeicherte Bewertung,
						move = myTempMove;														//setze das aktuell ausgewaehlte Move-Element aus der Liste auf move
				} else
					continue;																	//Ansonsten suche weiter.
			}
		}

		return move;																			//Gib den move zurueck
	}

	/**
	 * Diese Methode &uuml;berpr&uuml;ft, ob die Liste einen Zug enth&auml;lt,
	 * der zum Sieg f&uuml;rt.
	 */

	private boolean winSituation(Status item) {
		if (item.isBLUEWIN() && this.playerCode == Const.PLAYER_BLUE)
			return true;
		if (item.isREDWIN() && this.playerCode == Const.PLAYER_RED)
			return true;
		return false;
	}
	
	/**
	 * Diese Methode ermittelt f&uuml;r die Kegel eines Spielers die Summe
	 * ihrer Klassen auf dem Board und gibt diese Summe zur&uuml;ck.
	 * 
	 * @param board
	 * 		Das Board, mit den entsprechenden Positionen der Kegel
	 * @param color
	 * 		Die Spielerfarbe, f&uuml;r die summiert werden soll
	 */

	private int weightClass(Board board, boolean color) {

		int weightClass = 0;
		CoordList pins = board.getPins(color);

		for (Coord item : pins)
			weightClass += board.getFieldClass(item.getRow(),
					item.getDiagonal());
		return weightClass;

	}

	/**
	 * Diese Methode bewertet einen Zug anhand seiner Position auf dem Spielfeld
	 * und seiner Klasse.
	 * 
	 * @param playerCode
	 * 			Spielerfarbe, f&uuml;r die die Bewertung gemacht werden soll
	 * @param board
	 * 			Das Spielbrett, f&uuml;r das die Kegel bewertet werden sollen
	 * @param oldWeightClass
	 * 			Ist die Summe der Klassen aller Kegel f&uuml;r den entsprechenden Spieler, vor einem Zug
	 * @param newWeightClass
	 * 			Ist die Summe der Klassen aller Kegel f&uuml;r den entsprechenden Spieler, nach einem Zug
	 */
	private int Score(boolean playerCode, Board board,
			int oldWeightClass, int newWeightClass) {

		int score;
		int position = 0;
		int weight = Math.abs(oldWeightClass - newWeightClass);

		CoordList pins = board.getPins(playerCode);

		if (playerCode == Const.PLAYER_RED){
			for (Coord item : pins)
				position += 2 * item.getRow() - item.getDiagonal() + 10;
		}
		else{
			for (Coord item : pins)
				position += item.getRow() + item.getDiagonal();
		}

		score = position- (5 * weight);

		return score;
	}
}