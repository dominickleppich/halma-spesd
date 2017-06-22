package halmaPP.game;

import halmaPP.player.*;
import halmaPP.preset.*;
import java.io.*;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.JOptionPane;

/**
 * Methoden zum Aufsetzen einer Mehrspielerpartie
 * @author Sebastian Lindner
 *
 */

public class MultiplayerSetup {
	
	/* METHODEN ZUM AUFSETZEN EINES MULTIPLAYER - SPIELS */
	
	/**
	 * Biete &uuml;bergebenen Spieler &uuml;ber die RMI Registry an.
	 * @param player
	 * 		Spielerobjekt, das angeboten werden soll
	 */
	public static void offer(Player player) {
		try {
			assignAsHost( getHostAddress(), getHostName(), new NetPlayer( player));
			printLocalHostInfo();
		} catch ( RemoteException e) {
			Game.debug( e.toString());
		}
	}
	
	/**
	 * Finde ein RemoteObject mit abgefragter Hostadresse.
	 * @return Spielerobjekt, das an ein RemoteObject gebunden wurde
	 */
	public static Player find() {
		Player player;
		String hostAddress;
		/* wenn ohne GUI, dann Eingabe ueber Konsole */
		if ( Game.commandLine.isSet( Const.CMD_NO_GUI_INPUT)) {
			Game.print( "Bitte Hostadresse eingeben: ");
			hostAddress = userInput();
		/* sonst ueber Eingabefenster */
		} else 
			hostAddress = JOptionPane.showInputDialog("Bitte geben Sie die IP-Adresse Ihres Gegenspielers ein: "); 
		/* Spieler an RemoteObject binden */
		player = findHost( getRmiURL( hostAddress));
		return player;
	}
	
	/* ------------------------------------------------ */
	
	/* NETZWERK METHODEN */
	
	/**
	 * Setzt diesen Spieler als Host ein.
	 * Der Spieler wird in die RMI-Registry eingetragen.
	 * @param host
	 * 		eigene Host-Adresse
	 * @param name
	 * 		eigener Netzwerkname
	 */
	public static void assignAsHost( String host, String name, Player player) {
		try {
			LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
			RmiURL = "rmi://" + host + "/" + name;
			Naming.rebind( RmiURL, player);
			Game.print( "Host (" + name + ") bereit" );
			
		} catch ( MalformedURLException e) {
			Game.debug( e.getMessage());
			Game.errorExit( "Falsche URL");
		} catch ( RemoteException e) {
			Game.debug( e.getMessage());
			Game.errorExit( "Fehler. Laeuft rmiregistry? Beende.");
		} 
	}
	
	/**
	 * Findet einen fremden Netzwerkspieler mit &uuml;bergebener Adresse und Netzwerknamen.
	 * @param RmiURL
	 * 		RMI-URL des Gesuchten
	 * @return gebundenes Spielerobjekt
	 */
	public static Player findHost( String RmiURL) {
		Player player = null;
		try {
			player = (Player) Naming.lookup( RmiURL);
			Game.print( "Spieler (" + RmiURL.substring( RmiURL.lastIndexOf( "/") + 1, RmiURL.length()) + ") gefunden");
		} catch( Exception e) {
			Game.debug( e.toString());
			Game.errorExit( "Spieler oder Host nicht gefunden. Beende.");
		}
		return player;
	}
	
	/**
	 * Objektbindung aufheben
	 */
	public static void killRmiRegistry() {
		if (RmiURL.equals(""))
			return;
		try {
			Naming.unbind( RmiURL);
		} catch (RemoteException e) {
			Game.debug( e.getMessage());
		} catch (MalformedURLException e) {
			Game.debug( e.getMessage());
		} catch (NotBoundException e) {
			Game.debug( e.getMessage());
		}
	}
	
	/* ------------------------------------------------ */
	
	/* HILFSMETHODEN */
	
	/**
	 * Gibt Informationen zur eigenen IP-Adresse und zum Netzwerknamen aus.
	 */
	private static void printLocalHostInfo() {
		Game.print( "Host-Adresse: " + getHostAddress() +
				"\nHost-Name: " + getHostName());
	}
	
	/**
	 * Liefert eigene IP-Adresse.
	 * @return eigene IP-Adresse
	 */
	private static String getHostAddress() {
		try {
			InetAddress localHost = InetAddress.getLocalHost();
			return localHost.getHostAddress();
		} catch ( Exception e) {
			Game.debug( "Kann eigene Host-Adresse nicht identifizieren");
			return "";
		}
	}
	
	/**
	 * Liefert eigenen Netzwerknamen.
	 * @return eigenen Netzwerknamen
	 */
	private static String getHostName() {
		try {
			InetAddress localHost = InetAddress.getLocalHost();
			return localHost.getHostName();
		} catch ( Exception e) {
			Game.debug( "Kann eigenen Host-Namen nicht identifizieren");
			return "";
		}
	}
	
	/**
	 * Eingabeaufforderung &uuml;ber die Konsole.
	 * @return Eingegebener Text
	 */
	private static String userInput() {
		BufferedReader reader = new BufferedReader( new InputStreamReader( System.in));
		try {
			return reader.readLine();
		} catch (Exception e) {
			Game.debug( e.toString());
			return "";
		}
	}
	
	/**
	 * Liefert komplette RMI-URL f&uuml;r &uuml;bergebene Hostadresse.
	 * @param hostaddress
	 * 		Hostadresse
	 * @return RMI-URL
	 */
	protected static String getRmiURL( String hostaddress) {
	try {
		String[] address = Naming.list( "rmi://" + hostaddress);
		return ( "rmi:" + address[0]);
		} catch ( Exception e) {
			Game.debug( e.toString());
			Game.print( "Kann kein RemoteObject unter angegebener Host-Adresse finden");
			return "";
		}
	}
	
	/* ------------------------------------------------ */
	
	/* KLASSENVARIABLEN */
	
	/** RMI-URL, mit der ein Objekt in der Registry gebunden wurde */
	private static String RmiURL = "";
}
