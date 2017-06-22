package halmaPP.gui;

import halmaPP.game.Const;
import halmaPP.game.Game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
/**
 * Diese Klasse ist ein f&uuml;r die Darstellung des Scoreboards zust&auml;ndig.
 * 
 * @author Philip Langer, Dominick Leppich
 * @version 0.1
 */
public class ScorePanel extends JPanel {
	/**
	 * Konstruktor
	 */
	public ScorePanel() {
		super();

		this.setDoubleBuffered(true);

		try {
			File file = new File(scoreBoardPath);
			picScoreBoard = ImageIO.read(file);
		} catch (IOException e) {
			Game.errorExit("Fehler bei Grafikeingabe");
		}
	}
	/**
	 * Setzt die Scoreboardh&ouml;he nach Dateidimensionen
	 */
	public Dimension getPreferredSize() {
		return new Dimension(picScoreBoard.getWidth(),
				picScoreBoard.getHeight());
	}

	// ---------------------------------------------------------------

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
		if (playerCode == Const.PLAYER_RED)
			redHouseCount = count;
		else
			blueHouseCount = count;
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
		if (playerCode == Const.PLAYER_RED)
			redPlayerType = playerType;
		else
			bluePlayerType = playerType;
	}
	/**
	 * &Uuml;bernimmt die gew&auml;hlte Runde
	 */
	public void setRound(long round) {
		this.round = round;
	}

	// ---------------------------------------------------------------

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		/* Zeichne das ScoreBoard */
		g.drawImage(picScoreBoard, 0, 0, null);

		/* Zeichne die Hausbelegungen */
		Font font = g.getFont().deriveFont((float) (35f));
		g.setFont(font);

		int x1 = 0;
		int x2 = 0;
		if (redHouseCount < 10)
			x1 = 12;
		if (blueHouseCount < 10)
			x2 = 12;
		g.setColor(Color.RED);
		g.drawString(Integer.toString(redHouseCount), 50 + x1, 391);
		g.setColor(Color.BLUE);
		g.drawString(Integer.toString(blueHouseCount), 130 + x2, 391);

		/* Zeichne Rundenzahl */
		g.setColor(Color.BLACK);
		int x3 = 0;
		long tmpRound = round;
		while (tmpRound > 9) {
			x3 -= 11;
			tmpRound /= 10;
		}
		g.drawString(Long.toString(round), 104+x3, 490);

		/* Zeichne die Spielertypen */
		font = g.getFont().deriveFont((float) (25f));
		;
		g.setFont(font);

		g.setColor(Color.RED);
		if (redPlayerType > 0)
			g.drawString(Const.PLAYER_NAME_SHORT[redPlayerType - 1], 40, 90);
		g.setColor(Color.BLUE);
		if (bluePlayerType > 0)
			g.drawString(Const.PLAYER_NAME_SHORT[bluePlayerType - 1], 40, 175);
	}

	// ----------------------------------------------------a-----------

	private BufferedImage picScoreBoard;

	private int redHouseCount, blueHouseCount;
	private int redPlayerType, bluePlayerType;
	private long round;

	private static final String scoreBoardPath = "gfx/ScoreBoard.png";

	private static final long serialVersionUID = 1L;
}
