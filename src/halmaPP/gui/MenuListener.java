package halmaPP.gui;

import halmaPP.game.*;

import java.awt.event.*;

/**
 * Diese Klasse ist f&uuml;r die ActionListener der Spielereingabe im Men&uuml; zu tun
 *
 * @author Philip Langer, Dominick Leppich
 * 
 */
public class MenuListener implements ActionListener {
	public MenuListener(boolean playerCode, int playerType,
			String playerTypeName) {
		this.playerCode = playerCode;
		this.playerType = playerType;
		this.playerTypeName = playerTypeName;
	}

	/**
	 * Reagiere auf Buttoneingabe und erstelle Spieler
	 * 
	 * @param e
	 *            Event vom Player-Button
	 */
	public void actionPerformed(ActionEvent e) {
		if (playerCode == Const.PLAYER_RED)
			Game.debug("Roter " + playerTypeName);
		else
			Game.debug("Blauer " + playerTypeName);
		Game.createPlayer(playerCode, playerType);
	}

	// ---------------------------------------------------------------
	
	boolean playerCode;
	int playerType;
	String playerTypeName;
}
