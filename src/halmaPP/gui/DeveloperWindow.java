package halmaPP.gui;

import halmaPP.game.Game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Dieses Fenster zeigt ein Bild von den Programmierern.
 * 
 * @author Philip Langer
 * 
 */
public class DeveloperWindow extends JFrame {
	/**
	 * Konstruktor
	 */
	public DeveloperWindow() {
		super("Die Programmierer");
		try {
			File file = new File(developPath);
			final BufferedImage picture = ImageIO.read(file);
			
			/* Fuege Panel hinzu, auf dem das Bild angezeigt wird */
			this.add(new JPanel() {
				private static final long serialVersionUID = 1L;

				public Dimension getPreferredSize() {
					return new Dimension(picture.getWidth(), picture.getHeight());
				}

				protected void paintComponent(Graphics g) {
					g.drawImage(picture, 0, 0, null);
				}
			});
			
		} catch (IOException e) {
			Game.errorExit("Fehler bei Grafikeingabe");
		}
		
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		this.pack();
		this.repaint();
	}
	
	// ---------------------------------------------------------------

	private static final long serialVersionUID = 1L;

	private static final String developPath = "gfx/TeamSpesd.jpg";
}
