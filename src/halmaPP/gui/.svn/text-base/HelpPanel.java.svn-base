package halmaPP.gui;

import halmaPP.game.Game;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Diese Klasse ist f&uumlr die Grafik im Tutorial zust&aumlndig.
 * 
 * @author Philip Langer, Dominick Leppich
 * @version 0.1
 */
public class HelpPanel extends JPanel {
	/**
	 * Konstruktor f&uuml;r das Tutorial
	 * 
	 */
	public HelpPanel() {
		slideShow = new BufferedImage[9];

		try {
			for (int i = 0; i < 9; i++) {
				File file = new File("gfx/GridTut" + (i + 1) + "s.jpg");
				slideShow[i] = ImageIO.read(file);
			}
		} catch (IOException e) {
			Game.errorExit("Fehler bei Grafikeingabe");
		}
	}

	public Dimension getPreferredSize() {
		return new Dimension(slideShow[index].getWidth(),
				slideShow[index].getHeight());
	}

	// ---------------------------------------------------------------
	/**
	 * &Ouml;ffne das n&auml;chste Tutorial-Fenster
	 */
	public void next() {
		if (index < 8)
			index++;
		this.repaint();
	}
	/**
	 * &Ouml;ffne das letzte Tutorial-Fenster
	 */
	public void back() {
		if (index > 0)
			index--;
		this.repaint();
	}
	/**
	 * Erstelle Zeichenfl&auml;che
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (index >= 0 && index <= 8)
			drawFull(g, slideShow[index]);
	}
	/**
	 * Zeichne das gew&auml;htle Tutorial-Fenster
	 */
	public void drawFull(Graphics g, BufferedImage image) {
		g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), 0, 0,
				image.getWidth(), image.getHeight(), null);
	}

	// ---------------------------------------------------------------

	private BufferedImage[] slideShow;
	public int index = 0;

	// ---------------------------------------------------------------

	private static final long serialVersionUID = 1L;
}
