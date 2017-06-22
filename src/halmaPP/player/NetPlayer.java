package halmaPP.player;

import halmaPP.game.*;
import halmaPP.preset.*;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

/**
 * Implementation des Netzwerkspielers.
 * @author Sebastian Lindner
 *
 */

public class NetPlayer extends UnicastRemoteObject implements Player {
	
	/* KONSTRUKTOR */
	
	/**
	 * Konstruktor
	 * @param player
	 * 		Spielerobjekt, aus dem der NetPlayer gemacht wird
	 */
	public NetPlayer( Player player) throws RemoteException {
		this.player = player;
		Game.debug( "Netzwerkspieler zugewiesen");
	}
	
	
	/* ------------------------------------------------ */

	/* INTERFACE METHODEN */
	
	/**
	 * Greift auf die request Methode des eigentlichen Spielerobjekts zur&uuml;ck.
	 */
	public Move request() throws Exception, RemoteException {
		return player.request();
	}

	/**
	 * Greift auf die confirm Methode des eigentlichen Spielerobjekts zur&uuml;ck.
	 */
	public void confirm( Status boardStatus) throws Exception, RemoteException {
		player.confirm( boardStatus);
	}

	/**
	 * Greift auf die update Methode des eigentlichen Spielerobjekts zur&uuml;ck.
	 */
	public void update( Move opponentMove, Status boardStatus) throws Exception, RemoteException {
		player.update( opponentMove, boardStatus);
	}

	
	/* ------------------------------------------------ */

	/* KLASSENVARIABLEN */
	
	/** Spielerobjekt f&uuml;r den NetPlayer */
	private Player player;
	/** Checksum zum Sicherstellen, dass eine &uuml;ber das Netzwerk geschickte Klasse korrekt ankommt */
	private static final long serialVersionUID = 1L;
}
