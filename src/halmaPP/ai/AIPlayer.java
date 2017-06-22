package halmaPP.ai;

import halmaPP.board.Board;
import halmaPP.player.AbstractPlayer;

public abstract class AIPlayer extends AbstractPlayer {

	public AIPlayer(boolean playerCode, Board board, boolean withGUI) {
		super(playerCode, board, withGUI);
	}
	
	public AIPlayer(boolean playerCode, Board board, boolean withGUI, boolean myTurn) {
		super(playerCode, board, withGUI, myTurn);
	}
}
