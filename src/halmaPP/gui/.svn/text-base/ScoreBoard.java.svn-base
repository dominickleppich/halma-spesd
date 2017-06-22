package halmaPP.gui;

import halmaPP.game.Const;

import java.awt.*;

import javax.swing.*;

/**
 * Diese Klasse ist ein abgeleitetes JFrame und ist das Hauptger&uuml;st
 * f&uuml;r das Scoreboard mit Men&uuml;.
 * 
 * @author Philip Langer, Dominick Leppich
 * @version 0.1
 */
public class ScoreBoard extends JFrame {
	/**
	 * Konstruktor
	 */
	public ScoreBoard() {
		super("halmaPP"); // erzeuge neues Fenster mit Titel halmaPP

		self = this;
		
		Box mainBox = Box.createVerticalBox();

		mainMenu = new Menu();
		mainBox.add(mainMenu);

		scorePanel = new ScorePanel();
		mainBox.add(scorePanel);

		this.add(mainBox);

		this.pack();

		// Zentrierung des Fensters
		Dimension screenSize = getScreenDimension();
		int left = (screenSize.width - this.getWidth()) / 2;
		int top = (screenSize.height - this.getHeight()) / 2;
		this.setLocation(left, top);

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new ExitListener());
		this.setResizable(false);
		this.setVisible(true);
	}
	/**
	 * Besorgt sich die aktuelle Desktopaufl&ouml;sung
	 */
	public static Dimension getScreenDimension() {
		if (screenDimension == null) {
			GraphicsEnvironment env = GraphicsEnvironment
					.getLocalGraphicsEnvironment();
			GraphicsDevice gd = env.getDefaultScreenDevice();
			DisplayMode dm = gd.getDisplayMode();
			screenDimension = new Dimension(dm.getWidth(), dm.getHeight());
		}
		return screenDimension;
	}

	// ---------------------------------------------------------------
	/**
	 * Setze ScoreBoard zur&uuml;ck
	 */
	public void reset() {
		this.setPlayerType(Const.PLAYER_RED, 0);
		this.setPlayerType(Const.PLAYER_BLUE, 0);
		this.setPinsInHouse(Const.PLAYER_RED, 0);
		this.setPinsInHouse(Const.PLAYER_BLUE, 0);
		this.setRound(0);
	}
	/**
	 * Schalte Auswahlfl&auml;chen f&uuml;r die entsprechende Spielerfarbe
	 * an/aus
	 * 
	 * @param playerCode
	 *            Spielerfarbe
	 * @param enabled
	 *            An / Aus
	 */
	public void setPlayerEnabled(boolean playerCode, boolean enabled) {
		mainMenu.setPlayerEnabled(playerCode, enabled);
	}

	/**
	 * Setze die Anzahl der Kegel im Haus von Spielerfarbe auf die Anzahl
	 * {@code count}.
	 * 
	 * @param playerCode
	 *            Spielerfarbe
	 * @param count
	 *            Anzahl
	 */
	public void setPinsInHouse(boolean playerCode, int count) {
		scorePanel.setPinsInHouse(playerCode, count);
		scorePanel.repaint();
	}

	/**
	 * Setze den Spielertyp von Rot oder Blau.
	 * 
	 * @param playerCode
	 *            Spielerfarbe
	 * @param playerType
	 *            Spielertyp
	 */
	public void setPlayerType(boolean playerCode, int playerType) {
		scorePanel.setPlayerType(playerCode, playerType);
		scorePanel.repaint();
	}
	/**
	 * Setzt die aktuelle Spielrunde(nur f&uuml;r Laden)
	 */
	public void setRound(long round) {
		if (round > 0)
			mainMenu.setLoadEnabled(false);
		else
			mainMenu.setLoadEnabled(true);
		scorePanel.setRound(round);
		scorePanel.repaint();
	}

	// ---------------------------------------------------------------

	private Menu mainMenu;
	private ScorePanel scorePanel;
	
	public static JFrame self;

	private static Dimension screenDimension;
	
	private static final long serialVersionUID = 1L;
}
