package halmaPP.gui;

import java.awt.Dimension;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.*;
import halmaPP.board.*;
import halmaPP.exception.RageException;
import halmaPP.game.Const;
import halmaPP.game.Game;
import halmaPP.preset.*;

/**
 * Diese Klasse ist ein abgeleitetes JFrame und ist das Hauptger&uuml;st
 * f&uuml;r das Spielerfenster.
 * 
 * @author Philip Langer, Dominick Leppich
 * @version 0.1
 */
public class GUI extends JFrame {
	/**
	 * Konstruktor f&uuml;r die GUI.
	 * 
	 * @param playerCode
	 *            Spielerfarbe
	 * @param board
	 *            Board auf dem gespielt wird
	 * @param title
	 *            Titel des Fensters
	 */
	public GUI(boolean playerCode, Board board, String title) {
		super(title);

		/* Eigenreferenz */
		this.self = this;

		drawPanel = new DrawPanel(playerCode, board, this); // erzeuge ein
		// DrawPanel

		setActivePlayer(board.getNextPlayer());
		drawPanel.repaint();

		/* Passe bei Groessenaenderung den scaleFactor an */
		drawPanel.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				if (Math.abs(self.getWidth() - newWidth) > 20)
					newWidth = (int) ((double) self.getHeight() / GUI.quotient);
				drawPanel.setScaleFactor((double) newWidth / oldWidth);
				self.pack();
				drawPanel.calcPos();
				drawPanel.repaint();
			}
		});

		/*
		 * Fuege einen Tastatur-Listener hinzu, fuer die Eingabe der Cheat-Codes
		 * benoetigt
		 */
		drawPanel.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				/* Bei Klick auf Enter wird der Zug uebermittelt */
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					drawPanel.submit();
				/*
				 * Ansonsten wird der eingegebene Text in einem String
				 * gespeichert und ausgewertet, ob eine zu behandelnde Eingabe
				 * gemacht worden ist
				 */
				keyinputs += e.getKeyChar();
				checkCheat();
			}
		});

		/*
		 * Fuegt dem Panel einen Maus-Listener hinzu, sodass auf Klicks auf das
		 * Spielbrett reagiert werden kann. Die Klicks rufen dann die Methode
		 * des Panels auf
		 */
		drawPanel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				drawPanel.mouseClick(e.getX(), e.getY(), e.getClickCount(), e
						.getButton());
			}
		});

		this.add(drawPanel);
		/*
		 * Das Panel ist fokusierbar, erforderlich um auf Tastatureingaben
		 * reagieren zu koennen
		 */
		drawPanel.setFocusable(true);

		/*
		 * Setze optimale Groesse, Groesse nicht veraenderbar und zeige das
		 * Fenster an
		 */
		this.pack();

		/* Rotes Fenster nach rechts, blaues nach links */
		int left;
		if (playerCode == Const.PLAYER_RED)
			left = (ScoreBoard.self.getLocation().x
					+ ScoreBoard.self.getWidth() + 1);
		else
			left = (ScoreBoard.self.getLocation().x - this.getWidth() - 3);

		/* Vergleichsbreite das erste mal setzen */
		newWidth = this.getWidth();
		this.setLocation(left, ScoreBoard.self.getLocation().y);

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new ExitListener());
		this.setVisible(true);
	}

	// ---------------------------------------------------------------
	/**
	 * Darf eine Eingabe gemacht werden?
	 */
	public boolean isInputEnabled() {
		return inputEnabled;
	}
	/**
	 * Gebe Erlaubnis zum Eingabe t&auml;titgen
	 */
	public void setInputEnabled(boolean inputEnabled) {
		this.inputEnabled = inputEnabled;
	}
	/**
	 * Setze das Fenster auf aktiv
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
	/**
	 * Pr&uuml;fe ob das Fenster aktiv ist
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Wecke wartende Methode auf und gib den gemachten Zug zur&uuml;ck
	 */
	synchronized public void moveDone() {
		Game.debug("Zug gemacht");
		isMoveDone = true;
		notify();
	}

	/**
	 * Liefert eine Eingabe von der GUI
	 * 
	 * @return Zug
	 */
	public Move requestMove() {
		try {
			/* Spielbrett neu zeichnen */
			isMoveDone = false;
			drawPanel.reset();

			/* Warte bis der Move gemacht wurde */
			synchronized (this) {
				while (!isMoveDone)
					wait();
			}
		} catch (InterruptedException e) {
			Game.debug("Spiel wurde vorzeitig beendet");
		} catch (Exception e) {

			move = new Move(-1, -1); // Fehlerhafter Zug
		} finally {
			drawPanel.reset();
		}
		return move;
	}

	/**
	 * &Uuml;bergebe gemachten Zug an die GUI
	 * 
	 * @param move
	 */
	public void setMove(Move move) {
		this.move = move;
		Game.debug("GUI-Move: " + move);
	}

	/**
	 * Setze den aktiven Spieler
	 * 
	 * @param playerCode
	 *            Spielerfarbe
	 */
	public void setActivePlayer(boolean playerCode) {
		drawPanel.setActivePlayer(playerCode);
	}

	/**
	 * Spieler {@code playerCode} hat gewonnen
	 * 
	 * @param playerCode
	 *            Spielerfarbe
	 */
	public void setWon(boolean playerCode) {
		drawPanel.setWon(playerCode);
		drawPanel.repaint();
	}

	/**
	 * Pr&uuml;fe ob ein Cheatcode eingegeben wurde. Entsprechend wird darauf
	 * reagiert und die bisherige Eingabe gel&ouml;scht.
	 */
	private void checkCheat() {
		if (keyinputs.length() >= "goldeneye".length()
				&& keyinputs.contains("goldeneye")) {
			keyinputs = "";
			drawPanel.cheatGolden();
		}

		if (keyinputs.length() >= "lumberjack".length()
				&& keyinputs.contains("lumberjack")) {
			keyinputs = "";
			drawPanel.setWood(true);
		}

		if (keyinputs.length() >= "forestburn".length()
				&& keyinputs.contains("forestburn")) {
			keyinputs = "";
			drawPanel.setWood(false);
		}

		if (keyinputs.length() >= "save".length() && keyinputs.contains("save")) {
			keyinputs = "";
			Game.save(new File("save.xml"));
		}

		if (keyinputs.length() >= "greek".length()
				&& keyinputs.contains("greek")) {
			keyinputs = "";
			drawPanel.cheatAntiGold();
		}

		if (keyinputs.length() >= "rage".length() && keyinputs.contains("rage")) {
			keyinputs = "";
			throw new RageException();
		}
		
		if (keyinputs.length() >= "develop".length() && keyinputs.contains("develop")) {
			keyinputs = "";
			new DeveloperWindow().setVisible(true);
		}
	}

	// ---------------------------------------------------------------
	/**
	 * Setze den Skalierungsfaktor f&uumlr das Spielbrett
	 * 
	 * @param image
	 *            Spielbrettgrafik
	 * 
	 * @return double Skalierungsfaktor
	 */
	public static double resGetter(BufferedImage image) {
		if (scaleFactor == 0) {
			double height, width, resHalf, calcScaleFactor, screenHeight, screenWidth;
			height = image.getHeight();
			width = image.getWidth();

			Dimension dm = ScoreBoard.getScreenDimension();
			screenHeight = dm.getHeight();
			screenWidth = dm.getWidth();

			if (screenHeight < screenWidth) {
				resHalf = screenHeight * 0.4;
				calcScaleFactor = resHalf / height;
			} else {
				resHalf = screenWidth * 0.4;
				calcScaleFactor = resHalf / width;
			}
			scaleFactor = calcScaleFactor;
		}
		return scaleFactor;
	}

	// ---------------------------------------------------------------

	private static double scaleFactor = 0;
	public static double quotient;
	public static int oldWidth;
	public static int newWidth;
	private static final long serialVersionUID = 1L;
	public GUI self;

	// ---------------------------------------------------------------

	private String keyinputs = "";
	private DrawPanel drawPanel;
	private Move move = null;
	private boolean isMoveDone = false;
	private boolean active = false;
	private boolean inputEnabled = false;
}
