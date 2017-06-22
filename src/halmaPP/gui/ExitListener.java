package halmaPP.gui;

import halmaPP.game.Game;

import java.awt.event.*;

/**
 * Listener, der sich um das Beenden des Programms k&uuml;mmert.
 * 
 * @author Dominick Leppich
 */
public class ExitListener extends WindowAdapter {
	/**
	 * Beim Klick auf das Kreuz am Fenster wird die Exit-Methode der Game-Klasse
	 * aufgerufen, um alle n&ouml;tigen Schritte zur sauberen Schliessung des
	 * Programmes auszuf&uuml;hren.
	 */
	public void windowClosing(WindowEvent e) {
		Game.exit();
	}
}
