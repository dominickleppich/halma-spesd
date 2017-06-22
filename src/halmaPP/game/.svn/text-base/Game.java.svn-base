package halmaPP.game;

import halmaPP.ai.*;
import halmaPP.board.Board;
import halmaPP.gui.*;
import halmaPP.player.*;
import halmaPP.preset.*;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Iterator;

import javax.swing.JOptionPane;

/**
 * Hauptklasse des Spiels, hier werden Spieler erstellt, die abwechselnd
 * aufgerufen werden.
 * 
 * @author Sebastian Horwege
 */
public class Game {

	/**
	 * Wird beim Programmstart geladen! Liest Kommandozeileneingabe ein und
	 * erstellt ggf dann schonmal ein Spiel mit Konfiguration aus Kommandozeile
	 * 
	 * @param args
	 */
	public static void main(String args[]) {

		commandLine = new ArgsParser(args); // initialisierung des
		// Kommandozeilenverwalters
		if (commandLine.isSet(Const.CMD_HELP)) { // helptext
			Game.print(Const.TXT_HELP);
			Game.exit();
		}
		// erzeuge das Menu
		scoreBoard = new ScoreBoard();

		Game.newGame(); // initialisiere neues Spiel

		if (commandLine.isSet(Const.CMD_LOAD)) // Spielstand von Kommandozeile
			// Laden
			Game.load(new File(commandLine.getValueOf(Const.CMD_LOAD)));

		// versuche Spieler von Kommandozeile zu erzeugen
		createPlayer(Const.PLAYER_RED, Game.getPlayerTypeFromInput(commandLine
				.getValueOf(Const.CMD_PLAYER_RED)));
		createPlayer(Const.PLAYER_BLUE, Game.getPlayerTypeFromInput(commandLine
				.getValueOf(Const.CMD_PLAYER_BLUE)));

	}

	/**
	 * Liefert den PlayerType eines Spielers, f&uuml;r PlayerTypeKonstanten
	 * 
	 * @see Const
	 * @param playerCode
	 * @return Zahlenwert korrespondierend mit Const Interface Konstanten
	 */
	public static int getPlayerType(boolean playerCode) {
		if (playerCode == Const.PLAYER_BLUE) {
			Game.debug(Const.ESC_BG_BLUE + "PLAYER BLAU:"
					+ String.valueOf(redPlayerSelection));
			return bluePlayerSelection;
		} else {
			Game.debug(Const.ESC_BG_BLUE + "PLAYER RED:"
					+ String.valueOf(redPlayerSelection));
			return redPlayerSelection;
		}
	}

	/**
	 * Gib dieser Funktion einen userinput und sie gibt dir die PlayerType Const
	 * Repr&auml;sentierung
	 * 
	 * @param type
	 * @return Spielerart als int
	 */
	public static int getPlayerTypeFromInput(String type) {
		if (type.compareTo("human") == 0)
			return Const.HUMAN_PLAYER;

		if (type.compareTo("easyai") == 0)
			return Const.EASYAI_PLAYER;

		if (type.compareTo("mediumai") == 0)
			return Const.MEDIUMAI_PLAYER;

		if (type.compareTo("hardai") == 0)
			return Const.HARDAI_PLAYER;

		return Const.NO_PLAYER;
	}

	/**
	 * Gibt Auskunft &uuml;ber den Debug, ob er koloriert ist oder nicht
	 * 
	 * @return true bei gesetzem {@code -d color} flag
	 */
	public static boolean isColorized() {
		return commandLine.isSet(Const.CMD_COLORIZED);
	}

	/**
	 * Ist das Spiel pausiert?
	 * 
	 * @return liefert true, wenn Spiel pausiert ist?
	 */
	public static boolean isPaused() {
		return Game.pause;
	}

	/**
	 * gibt das Spiel wieder frei
	 */
	public static void unpause() {
		Game.pause = false;
	}

	/**
	 * Pausiert das Spiel
	 */
	public static void pause() {
		Game.pause = true;

	}

	/**
	 * Speichert aktuellen Spielstand im xml Format
	 * 
	 * @param url
	 *            File, in das geschrieben werden soll
	 */
	public static void save(File url) {
		xmlSave.save(url);
	}

	/**
	 * @see Game#load(File,int)
	 */
	public static void load(File url) {
		Game.load(url, -1);
	}

	/**
	 * L&auml;dt einen Spielstand ins Game. Mit Angabe von Anzahl der Moves, die
	 * durchgef&uuml;hrt werden!
	 * 
	 * @param url
	 *            File zum Spielstand
	 * @param moveCount
	 *            Anzahl der zu ladenen Moves!
	 */
	public static void load(File url, int moveCount) {

		LoadThread load = new LoadThread(url, moveCount);
		load.start();

	}

	/**
	 * @see Game#createPlayer(boolean, int, boolean)
	 */
	public static void createPlayer(boolean color, int selection) {
		if (color == Const.PLAYER_RED)
			Game.createPlayer(color, selection, true);
		else if (color == Const.PLAYER_BLUE)
			Game.createPlayer(color, selection, false);

	}

	/**
	 * Erstelle einen Spieler mit bestimmter Farbe und einem SpielerTypen
	 * 
	 * @param color
	 *            Spielerfarbe
	 * @param selection
	 *            Typ des Spielers
	 */
	public static void createPlayer(boolean color, int selection, boolean myTurn) {

		if (color == Const.PLAYER_BLUE) {
			bluePlayer = Game.makePlayer(color, selection, myTurn);
			bluePlayerSelection = selection;
		} else {
			redPlayer = Game.makePlayer(color, selection, myTurn);
			redPlayerSelection = selection;
		}

		if (selection != Const.NO_PLAYER) {
			// Entferne Auswahlmoeglichkeit fuer diese Farbe auf dem Scoreboard
			scoreBoard.setPlayerEnabled(color, false);
			scoreBoard.setPlayerType(color, selection);
		}

		// Spieler hostet netzwerkspiel

		if (isMultiplayer) {
			MultiplayerSetup.offer(redPlayer);
			scoreBoard.setVisible(false);
			JOptionPane.showMessageDialog(null,
					"Spiel aufgesetzt. Warte auf Client...",
					"Netzwerkspiel gestartet", JOptionPane.PLAIN_MESSAGE);
			Menu.switchLAN();
			return;
		}

		// Spieler tritt Netzwerkspiel bei
		if (isJoining) {
			redPlayer = MultiplayerSetup.find();
			scoreBoard.setPlayerType(Const.PLAYER_RED, Const.NET_PLAYER);
			Menu.switchLAN();
		}

		if (redPlayer != null && bluePlayer != null) // wurden beide Spieler
			// gesetzt wird Spiel
			// gestartet
			startGame();
	}

	/**
	 * Setzt roten Spieler als Startspieler bei normalem gamestart
	 * 
	 * @see Game#makePlayer(boolean, int, boolean)
	 */
	static Player makePlayer(boolean color, int selection) {
		if (color == Const.PLAYER_BLUE)
			return makePlayer(color, selection, false);
		return makePlayer(color, selection, true);
	}

	/**
	 * Erzeugt Player basierend auf playerSelection.
	 * 
	 * @param color
	 * @param selection
	 * @return Player-Objekt
	 */
	private static Player makePlayer(boolean color, int selection,
			boolean myTurn) {
		switch (selection) {
		case Const.HUMAN_PLAYER: // erzeugt neuen HumanPlayer
			return new HumanPlayer(color, board.clone(), !commandLine
					.isSet(Const.CMD_NO_GUI_INPUT), myTurn);
		case Const.EASYAI_PLAYER: // erzeugt neue EASY AI
			return new EasyAI(color, board.clone(), !commandLine
					.isSet(Const.CMD_NO_GUI_INPUT), myTurn);

		case Const.MEDIUMAI_PLAYER: // erzeugt neue Medium AI
			return new MediumAI(color, board.clone(), !commandLine
					.isSet(Const.CMD_NO_GUI_INPUT), myTurn);

		case Const.HARDAI_PLAYER: // erzeugt neue HARDAI
			return new HardAI(color, board.clone(), !commandLine
					.isSet(Const.CMD_NO_GUI_INPUT), myTurn);

		default:
			return null;
		}
	}

	public static void setMultiplayer(boolean Multiplayer) {
		isMultiplayer = Multiplayer;
	}

	public static void setJoining(boolean Joining) {
		isJoining = Joining;
	}

	public static boolean getMultiplayer() {
		return isMultiplayer;
	}

	public static boolean getJoining() {
		return isJoining;
	}

	/**
	 * Debugunabh&auml;ngige Ausgabe (bitte {@code System.out.println(String); }
	 * 
	 * @param printtext
	 *            Formatierter Text der Ausgabe Formatierungsstrings stehen in
	 *            Const im gleichen package
	 */
	public static synchronized void print(String printtext) {
		if (printtext != null) {

			printtext += Const.ESC_STD;
			if (!commandLine.isSet(Const.CMD_COLORIZED))
				printtext = printtext.replaceAll(Const.ESC_REG_EXP, "");

			System.out.println(printtext);
		}
	}

	/**
	 * Gibt auf der Kommandozeile die in debugtext &uuml;bergebene Debug Message
	 * aus. Der debugtext kann mit den Escapesequenzen aus Const formatiert
	 * werden, eine formatierte Ausgabe sieht dann so aus:
	 * 
	 * <pre>
	 * {@code Game.debug(Const.ESC_RED + "HI" + Const.ESC_STD); }
	 * </pre>
	 * 
	 * @param debugtext
	 *            Debugmessagetext
	 */
	public static void debug(String debugtext) {
		// ist die farbige Ausgabe im Debug deaktiviert,
		// so muessen die farbelemente entfernt werden.
		if (commandLine.isSet(Const.CMD_DEBUG))
			Game.print(debugtext);
	}

	/**
	 * Beendet das Programm mit einer Fehlermeldung
	 * 
	 * @param errortext
	 */
	public static synchronized void errorExit(String errortext) {
		Game.print(errortext);
		System.exit(1);
	}

	/**
	 * Beendet das Spiel planm&auml;ssig.
	 */
	public static void exit() {
		Game.debug("Beende.");
		MultiplayerSetup.killRmiRegistry();
		System.exit(0);
	}

	/**
	 * Pr&uuml;fe ob das Spiel bereit ist gestartet zu werden
	 */
	public static void startGame() {

		if (redPlayer != null && bluePlayer != null) {

			if (gameThread != null && gameThread.isAlive())
				gameThread.interrupt();

			gameThread = new GameThread();
			gameThread.start();
		}
	}

	/**
	 * Initialisiere das neue Spiel
	 */
	public static void newGame() {

		Game.pause();
		xmlSave = new XMLGenerator(); // Objekt, dass XML Spielstaende erzeugt

		if (gameThread != null && gameThread.isAlive())
			gameThread.interrupt();

		if (redPlayer instanceof AbstractPlayer) {
			AbstractPlayer tempPlayer = (AbstractPlayer) redPlayer;
			if (tempPlayer != null)
				tempPlayer.delete();
		}
		if (bluePlayer instanceof AbstractPlayer) {
			AbstractPlayer tempPlayer = (AbstractPlayer) bluePlayer;
			if (tempPlayer != null)
				tempPlayer.delete();
		}

		board = new Board(); // erstelle ein neues Spielbrett!

		nextPlayer = Const.PLAYER_RED; // roter Spieler beginnt das Spiel

		Game.debug(Const.ESC_BG_BLUE + Const.ESC_RED
				+ "Willkommen bei halmaPP!" + Const.ESC_STD);

		// spieler zuruecksetzen
		redPlayer = null;
		bluePlayer = null;

		round = 0L;

		scoreBoard.reset();

		Game.unpause();
	}

	/** boolean, ob ein Netzwerkspiel gehostet werden soll */
	private static boolean isMultiplayer = false;
	/** boolean, ob ein Netzwerkspiel beigetreten werden soll */
	private static boolean isJoining = false;
	/**
	 * Objekt, in das Z&uuml;ge gespeichert werden, um bei Bedarf nach XML zu
	 * exportieren
	 */
	static XMLGenerator xmlSave;
	/** blaue Spielfigure */
	static Player bluePlayer;

	/** Auswahl f&uuml;r den blauen Spieler */
	static int bluePlayerSelection = Const.NO_PLAYER;

	/** Das Board des Spiels */
	static Board board;

	/** Werkzeug zum Auswerten der Kommandozeilenparameter */
	static ArgsParser commandLine;

	/** Thread zum Verwalten des Spielablaufs */
	private static Thread gameThread;

	/** Das Spiel bekommt eine Gui, auf der die Spielst&auml;nde geladen werden! */
	static GUI gui;

	/**
	 * wird gesetzt und gelesen, damit klar ist, welcher Spieler an der Reihe
	 * ist
	 */
	static boolean nextPlayer;

	/** ist das Spiel pausiert? */
	private static boolean pause = false;

	/** rote Spielfigur */
	static Player redPlayer;

	/** Auswahl f&uuml;r den roten Spieler */
	static int redPlayerSelection = Const.NO_PLAYER;

	/** rundenanzeige */
	static long round;

	/** Anzeige von Menu und Spielstand */
	protected static ScoreBoard scoreBoard;

}

/**
 * Die GameThreadklasse erzeugt einen neuen Thread, in dem das Spiel gespielt
 * wird. Hier werden also die Schnittstellen des Roten und Blauen Spielers
 * abwechselnd angesprochen! Der Grund f&uumlr; die Implementation als Thread
 * ist, dass der Thread interrupted werden kann und dadurch ein laufendes Spiel
 * einfach beendbar ist. Dies wird zum Beispiel von der Gui verlangt, sollte
 * Datei>>neu gew&aumlhlt werden!
 * 
 * @author sebastian
 * 
 */
class GameThread extends Thread {

	/**
	 * Konstruktor, damit Klasse weiss, dass sie Objekt ist!
	 */
	public GameThread() {

	}

	/**
	 * Hier l&auml;ft das eigentliche Spiel. Es werden abwechselnd
	 * Spielz&uuml;ge angefordert, dann vom Board best&auml;tigt und an den
	 * Gegener zum updaten gesendet. Bei Status nicht OK wird das Spiel
	 * unterbrochen und entsprechend darauf reagiert. Wird der Thread
	 * interrupted reagiert das Spiel darauf und beendet den abwechselnden
	 * Playeraufruf!
	 * 
	 * @return Status des Spiels nach Ende
	 */
	private Status operate() {

		while (Game.board.getStatus().isOK() && !this.isInterrupted()) {

			if (Game.isPaused())
				continue;

			Move lastMove;

			try {
				Player currentPlayer;

				do { // waehle den aktuellen Spieler! und frage
					// nach neuem Move, solange der Move nicht auf dem Board
					// ausgefuehrt werden kann!
					if (Game.nextPlayer == Const.PLAYER_RED) {
						Game.print(Const.ESC_RED + "Roter Spieler am Zug:");
						currentPlayer = Game.redPlayer;
					} else {
						Game.print(Const.ESC_BLUE + "Blauer Spieler am Zug:");
						currentPlayer = Game.bluePlayer;
					}

					lastMove = currentPlayer.request(); // Zug anfordern

					if (lastMove == null && currentPlayer != null) { // Aufgabe
						// eines
						// Spielers

						if (Game.nextPlayer == Const.PLAYER_RED)
							return new Status(Status.BLUEWIN);
						else
							return new Status(Status.REDWIN);
					}

					Game.board.makeMove(lastMove); // versuche Move aufs Board
					// anzuwenden!

				} while (Game.board.getStatus().isILLEGAL());

				Game.xmlSave.addMove(lastMove); // Zug wird in XML Objekt
				// gespeichert!

				if (Game.nextPlayer == Const.PLAYER_RED) { // fordert vom
					// aktuellen spieler
					// den Zug an

					Game.redPlayer.confirm(Game.board.getStatus());
					Game.bluePlayer.update(lastMove, Game.board.getStatus());

				} else if (Game.nextPlayer == Const.PLAYER_BLUE) {

					Game.bluePlayer.confirm(Game.board.getStatus());
					Game.redPlayer.update(lastMove, Game.board.getStatus());

				}

				Game.debug(Game.board.toString()); // voraussetzung: board nur
				// anziegen, wenn debug ist

				// Viewer aktualisieren
				Game.scoreBoard.setPinsInHouse(Game.nextPlayer, Game.board
						.getHouseCount(Game.nextPlayer));
				Game.scoreBoard.setRound(++Game.round);

				Game.nextPlayer = !Game.nextPlayer; // Spieler wechseln

			} catch (NullPointerException e) {
				Game.print(Const.ESC_BG_CYAN + "Spieler wurde geloescht");
				return new Status(Status.DRAW);
			} catch (RemoteException e) {
				Game.print(Const.ESC_BG_CYAN + "Verbindung verloren!");
				return new Status(Status.ERROR);
			} catch (InterruptedException e) {
				// Falls HardAI beendet wird
			} catch (Exception e) {
//				Game.debug(e.getMessage());
//				Game.errorExit(Const.ESC_BG_CYAN
//						+ "Spiel durch Fehler beendet.");
			}

		}

		Game.debug(Const.ESC_BG_YELLOW + "Spielstatus: "
				+ Game.board.getStatus());
		return Game.board.getStatus(); // wird das spiel unterbrochen, so
		// liefere boardstatus
	}

	/**
	 * Spielt das Spiel und reagiert dann auf Situationen, mit denen das Spiel
	 * beendet wurde!
	 */
	public void run() {
		try {
			Status gameState = operate(); // starte das Spiel
			Player tempRedPlayer = Game.redPlayer;
			Player tempBluePlayer = Game.bluePlayer;
			if (gameState.isREDWIN()) {
				Game.print(Const.ESC_RED + "ROT gewinnt das Spiel!");
				if (tempRedPlayer instanceof AbstractPlayer)
					((AbstractPlayer) tempRedPlayer).setWon(Const.PLAYER_RED);

				if (tempBluePlayer instanceof AbstractPlayer)
					((AbstractPlayer) tempBluePlayer).setWon(Const.PLAYER_RED);

			} else if (gameState.isBLUEWIN()) {
				Game.print(Const.ESC_BLUE + "BLAU gewinnt das Spiel!");
				if (tempRedPlayer instanceof AbstractPlayer)
					((AbstractPlayer) tempRedPlayer).setWon(Const.PLAYER_BLUE);

				if (tempBluePlayer instanceof AbstractPlayer)
					((AbstractPlayer) tempBluePlayer).setWon(Const.PLAYER_BLUE);
			} else if (gameState.isDRAW()) {
				Game.print(Const.ESC_BOLD + "Spiel beendet mit Unentschieden!");
				Game.newGame();
			} else if (gameState.isERROR()) {
				Game.print(Const.ESC_BOLD
						+ "Spiel beendet weil Netzwerkfehler aufgetreten ist!");
				Game.newGame();
			}

		} catch (NullPointerException e) {
			Game.debug(Const.ESC_BG_WHITE + "Spieler wurde geloescht!"); // Spieler
			// koennte
			// geloeschtgeworden
			// sein!
		}

	}

}

/**
 * Ein Thread wird erzeugt, der einen Spielstand laden kann!
 * 
 * @author sebastian.horwege
 * 
 */
class LoadThread extends Thread {

	/**
	 * L&auml;dt alle Spielst&auml;nde!
	 * 
	 * @see LoadThread#LoadThread(File, int)
	 */
	public LoadThread(File url) {
		this(url, -1);
	}

	/**
	 * Erzeugt ein Objekt, dass einen gespeicherten Spielstand l&auml;dt und den
	 * Spielverlauf simuliert!
	 * 
	 * @param url
	 *            File zum Spielstand
	 * @param moveCount
	 *            Anzahl der Moves
	 */
	public LoadThread(File url, int moveCount) {
		this.url = url;
		this.moveCount = moveCount;

	}

	/**
	 * L&auml;dt den Spielstand ins Game
	 */
	public void run() {
		int currentMove = 0;
		XMLParser xmlLoad;

		Menu.switchFile();
		Game.debug("Spielstand laden aus Datei: " + Const.ESC_BOLD + url);
		Game.newGame();

		Game.gui = new GUI(true, Game.board, "Spielstand laden");

		xmlLoad = new XMLParser(url);

		if (!xmlLoad.success())
			return;

		Iterator<MoveAndState> it = xmlLoad.iterator();

		if (moveCount < 0) // gesamten Spielstand laden
			moveCount = xmlLoad.getMoveCount();

		while (it.hasNext() && currentMove < moveCount) { // lese moves so
			// lange, bis
			// gesamtlaenge
			// erreicht

			MoveAndState loadMove = it.next();
			Game.debug(Const.ESC_GREEN + "lade move:" + loadMove.getMove());

			Game.xmlSave.addMove(loadMove.getMove(), loadMove.getDuration());
			Game.board.makeMove(loadMove.getMove());

			Game.print(Game.board.toString());

			Game.gui.repaint();

			// Verzögertes Laden der einzelnen Züge zum Nachvollziehen des
			// Spielverlaufs auf der Kommandozeile
			try {
				Thread.sleep(500L);
			} catch (InterruptedException e) {
				Game.errorExit("Wurde beim Laden unterbrochen!");
			}

			Game.scoreBoard.setRound(++currentMove);

		}

		// gui sollte hier dann wieder geschlossen werden!
		Game.gui.setVisible(false);
		Game.round = currentMove;
		Game.scoreBoard.setRound(Game.round);

		// Erzeugt die Spieler mit den gespeicherten Spielertypen
		// es wird uebergeben, ob ein Spieler am Zug ist oder nicht

		if (currentMove % 2 == 0)
			Game.nextPlayer = Const.PLAYER_RED;
		else
			Game.nextPlayer = Const.PLAYER_BLUE;
		Game.createPlayer(Const.PLAYER_RED, xmlLoad.getRedPlayerType(),
				Game.nextPlayer == Const.PLAYER_RED);
		Game.createPlayer(Const.PLAYER_BLUE, xmlLoad.getBluePlayerType(),
				Game.nextPlayer == Const.PLAYER_BLUE);
		Menu.switchFile();
		Game.startGame();

	}

	/** Datei mit Spielstand */
	File url;
	/** Anzahl der auszuf&uuml;hrenden Moves des Spielstandes */
	int moveCount;

}