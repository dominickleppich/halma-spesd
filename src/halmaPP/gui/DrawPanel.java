package halmaPP.gui;

import halmaPP.board.*;
import halmaPP.game.*;
import halmaPP.preset.*;
import halmaPP.player.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import java.io.*;
import java.util.Random;

import javax.imageio.*;
import javax.swing.JPanel;

/**
 * Diese Klasse realisiert das eigentliche Spielgeschehen. Es wird bei jeder
 * Aktualisierung die aktuelle Version des Spielbrett auf das Brett gezeichnet.
 * Das Panel ist skalierbar und passt sich der Gr&ouml;sse entsprechend an. Es
 * k&ouml;nnen eingaben durch klicken gemacht werden, welche dann als
 * Spielereingaben interpretiert werden.
 * 
 * @author Philip Langer, Dominick Leppich
 * 
 */
public class DrawPanel extends JPanel {
	public DrawPanel(boolean playerCode, Board board, GUI gui) {
		posX = new int[11][15];
		posY = new int[11];

		this.playerCode = playerCode;
		this.board = board;
		this.gui = gui;
		this.setDoubleBuffered(true);

		/* Lade alle Grafiken */
		try {
			File file = new File(boardPath);
			picBoardNormal = ImageIO.read(file);
			file = new File(woodGridPath);
			picWoodGrid = ImageIO.read(file);
			file = new File(woodPath);
			picWood = ImageIO.read(file);
			file = new File(boardGoldenPath);
			picBoardGolden = ImageIO.read(file);
			file = new File(redWonPath);
			picRedWon = ImageIO.read(file);
			file = new File(blueWonPath);
			picBlueWon = ImageIO.read(file);
			file = new File(boardActive);
			picBoardActive = ImageIO.read(file);
			file = new File(redPinPath);
			picRedPin = ImageIO.read(file);
			file = new File(bluePinPath);
			picBluePin = ImageIO.read(file);
			file = new File(figureJumpPath);
			picJump = ImageIO.read(file);
			file = new File(figureMovePath);
			picMove = ImageIO.read(file);
			file = new File(redPinSelectedPath);
			picRedSelectedPin = ImageIO.read(file);
			file = new File(bluePinSelectedPath);
			picBlueSelectedPin = ImageIO.read(file);
			file = new File(redPinSelectedTransPath);
			picRedSelectedTransPin = ImageIO.read(file);
			file = new File(bluePinSelectedTransPath);
			picBlueSelectedTransPin = ImageIO.read(file);
			file = new File(redPinGoldenPath);
			picRedGoldenPin = ImageIO.read(file);
			file = new File(bluePinGoldenPath);
			picBlueGoldenPin = ImageIO.read(file);
			file = new File(redPinSelectedGoldenPath);
			picRedSelectedGoldenPin = ImageIO.read(file);
			file = new File(bluePinSelectedGoldenPath);
			picBlueSelectedGoldenPin = ImageIO.read(file);
			file = new File(redPinSelectedGoldenTransPath);
			picRedSelectedGoldenTransPin = ImageIO.read(file);
			file = new File(bluePinSelectedGoldenTransPath);
			picBlueSelectedGoldenTransPin = ImageIO.read(file);

			/* verwendete Bilder setzen */
			picRed = picRedPin;
			picBlue = picBluePin;
			picRedSelected = picRedSelectedPin;
			picBlueSelected = picBlueSelectedPin;
			picRedSelectedTrans = picRedSelectedTransPin;
			picBlueSelectedTrans = picBlueSelectedTransPin;
			picBoard = picBoardNormal;

		} catch (IOException e) {
			Game.errorExit("Fehler bei Grafikeingabe");
		}
		GUI.quotient = (double) picBoard.getHeight() / picBoard.getWidth();
		GUI.oldWidth = picBoard.getWidth();
		scaleFactor = GUI.resGetter(picBoard);
		calcPos();
	}
	
	/**
	 * Gib optimale Gr&ouml;sse zur&uuml;ck
	 * 
	 * @return	Dimension f&uuml;r das skalierte Spielbrett
	 */
	public Dimension getPreferredSize() {
		return new Dimension((int) (scaleFactor * picBoard.getWidth()),
				(int) (scaleFactor * picBoard.getHeight()));
	}

	/**
	 * Reagiere auf einen Klick an Position (x,y) auf dem Spielfeld
	 * 
	 * @param x
	 *            X-Koordinate
	 * @param y
	 *            Y-Koordinate
	 * @param times
	 *            Wie oft wurde geklickt
	 * @param button
	 *            Mit welcher Maustaste
	 */
	public void mouseClick(int x, int y, int times, int button) {
		/* pruefe ob ueberhaupt geklickt werden darf */
		if (!gui.isInputEnabled())
			return;

		/* Soll zurueckgesetzt werden? (Rechte Maustaste) */
		if (button == MouseEvent.BUTTON3) {
			reset();
			Game.debug("Zug zurueckgesetzt");
			return;
		}

		/* ermittle Koordinaten des Klicks (Am Brett ausgerichtet) */
		Coord coord = getPin(x, y);
		/* Falls ausserhalb des Feldes, abbrechen */
		if (coord == null
				|| !board.isField(coord.getRow(), coord.getDiagonal()))
			return;

		/* Pruefe, ob es ein Kegel ist, der bewegt werden kann */
		if (move == null) {
			boolean isPossible = false;
			for (MoveAndState move : moveList)
				if (new Coord(move.getMove().getRow(), move.getMove()
						.getDiagonal()).equals(coord))
					isPossible = true;

			if (!isPossible)
				return;
			else {
				selectedPin = coord;
				move = new Move(coord.getRow(), coord.getDiagonal());
				this.repaint();
				return;
			}
		}

		Move runner = move;
		/* gehe ans Ende des bisherigen Zuges */
		while (runner.getNext() != null)
			runner = runner.getNext();

		/* wenn das Feld nicht schon wieder geklickt wurde */
		if (runner.getRow() != coord.getRow()
				|| runner.getDiagonal() != coord.getDiagonal()) {
			runner.setNext(new Move(coord.getRow(), coord.getDiagonal()));
			/* falls Zug nicht moeglich, setze Schritt zurueck */
			if (!moveList.contains(move)) {
				runner.setNext(null);
				return;
			} else
				/* Fuege das Feld zum Zug hinzu */
				this.repaint();
		}

		/* Bei Doppelklick wird der Zug uebermittelt */
		if (times > 1) {
			submit();
		}
	}
	/**
	 * Sorgt daf&uuml;r welcher Spieler aktiv ist
	 * 
	 * @param playerCode
	 *            Spielerfarbe
	 */
	public void setActivePlayer(boolean playerCode) {
		activePlayer = playerCode;
	}

	/**
	 * Berechne die Feldposition durch die Koordinaten auf dem Bild
	 * 
	 * @param x
	 *            X-Koordinate
	 * @param y
	 *            Y-Koordinate
	 * @return Coord Objekt mit den Koordinaten des Spielfeldes
	 */
	private Coord getPin(int x, int y) {
		for (int row = 0; row < 11; row++) {
			for (int diagonal = 0; diagonal < 15; diagonal++) {
				if (y >= posY[row]
						&& y <= posY[row]
								+ (int) (picRed.getHeight() * scaleFactor)
						&& x >= posX[row][diagonal]
						&& x <= posX[row][diagonal]
								+ (int) (picRed.getWidth() * scaleFactor))
					return new Coord(row, diagonal);
			}
		}
		return null;
	}

	/**
	 * Zeichne das Spielfeld. Zun&auml;chst wird das Brett gezeichnet, dann alle
	 * Kegel der Spieler. Danach falls n&ouml;tig m&ouml;gliche Spr&uuml;nge
	 * oder Schiebefelder.
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		/* Zeichne Spielbrett */
		drawFull(g, picBoard);

		/* Zeichne blaue und rote Kegel */
		drawPins(board.getPins(Const.PLAYER_BLUE), g, Const.PLAYER_BLUE);
		drawPins(board.getPins(Const.PLAYER_RED), g, Const.PLAYER_RED);

		/* Zeige den aktuellen Spieler an */
		BufferedImage image;
		if (activePlayer == Const.PLAYER_RED)
			image = picRed;
		else
			image = picBlue;

		g.drawImage(image, (int) (activePlayerX * scaleFactor),
				(int) (activePlayerY * scaleFactor),
				(int) (activePlayerX * scaleFactor + (image.getWidth()
						* scaleFactor * 1.6)),
				(int) (activePlayerY * scaleFactor + (image.getHeight()
						* scaleFactor * 1.6)), 0, 0, image.getWidth(), image
						.getHeight(), null);

		/* Falls ein Kegel markiert wurde */
		if (selectedPin != null) {
			/* Zeige alle moeglichen Zuege an */
			for (MoveAndState x : moveList) {
				/*
				 * Gehe hierzu nur alle Zuege der Liste durch, wo die Eintraege
				 * mit dem ausgewaehlten Pin beginnen
				 */
				if (x.getMove().getRow() == selectedPin.getRow()
						&& x.getMove().getDiagonal() == selectedPin
								.getDiagonal()) {
					Move runner1 = move;
					Move runner2 = x.getMove();
					/*
					 * Gehe die Listeneintraege bis zu dem bisher gemachten Zug
					 * durch
					 */
					boolean goOn = true;
					while (runner1.getNext() != null && goOn) {
						if (runner2.getNext() != null
								&& runner1.getRow() == runner2.getRow()
								&& runner1.getDiagonal() == runner2
										.getDiagonal()) {
							runner1 = runner1.getNext();
							runner2 = runner2.getNext();
						} else
							// bei Abweichung brich ab, dann ist es kein Zug der
							// gesucht ist
							goOn = false;
					}
					/*
					 * falls letzte Koordinate uebereinstimmt, dann zeichne den
					 * naechsten Zug aus der Liste als moeglichen Sprung oder
					 * Schiebebewegung auf das Brett
					 */
					if (runner1.getRow() == runner2.getRow()
							&& runner1.getDiagonal() == runner2.getDiagonal()
							&& runner2.getNext() != null) {
						/* Sprung oder Schieben */
						if (x.isJump())
							drawAt(g, picJump, runner2.getNext().getRow(),
									runner2.getNext().getDiagonal());
						else
							drawAt(g, picMove, runner2.getNext().getRow(),
									runner2.getNext().getDiagonal());
					}
				}
			}
		}

		/* Zeige bisher gemachte Zwischenschritte an */
		if (move != null) {
			Move runner = move;
			/* Zeichne bisher gemachte Zwischenschritte transparent */
			while (runner.getNext() != null) {
				if (activePlayer == Const.PLAYER_BLUE)
					drawAt(g, picBlueSelectedTrans, runner.getRow(), runner
							.getDiagonal());
				else
					drawAt(g, picRedSelectedTrans, runner.getRow(), runner
							.getDiagonal());
				runner = runner.getNext();
			}
			/*
			 * Die letzte Position wo sich der Kegel gerade befinden wuerde,
			 * wird nicht transparent gezeichnet
			 */
			if (activePlayer == Const.PLAYER_BLUE)
				drawAt(g, picBlueSelected, runner.getRow(), runner
						.getDiagonal());
			else
				drawAt(g, picRedSelected, runner.getRow(), runner.getDiagonal());
		}

		/* Holz-Cheat? */
		if (wood) {
			drawFull(g, picWoodGrid);
			for (int i = 0; i < 50; i++)
				drawAtXY(g, picWood, woodPositions[i][0], woodPositions[i][2]);
		}

		/*
		 * Falls GUI inaktiv -> verdunkeln (dies wird erreicht, indem ein
		 * schwarzes zu 50% transparentes schwarzes Bild darueber gelegt wird
		 */
		if (gui.isActive())
			drawFull(g, picBoardActive);

		/*
		 * Falls Spiel gewonnen -> Anzeigen und fertig
		 */
		if (playerWon != 0) {
			if (playerWon == Status.REDWIN)
				drawFull(g, picRedWon);
			else
				drawFull(g, picBlueWon);
		}
	}

	/**
	 * Setze den Skalierungsfaktor auf den &uuml;bergebenen Faktor.
	 * 
	 * @param scaleFactor
	 *            Skalierungsfaktor
	 */
	public void setScaleFactor(double scaleFactor) {
		this.scaleFactor = scaleFactor;
	}

	/**
	 * Setze Holz (CheatCode)
	 * 
	 * @param enabled
	 */
	public void setWood(boolean enabled) {
		if (enabled) {
			wood = true;
			final int windowHeight = this.getHeight();
			final int windowWidth = this.getWidth();
			new Thread(new Thread() {

				public void setWoodPosition(int i) {
					Random random = new Random();
					if (i % 2 == 0) {
						woodPositions[i][0] = random.nextInt(50)
								- (int) (picWood.getHeight() * scaleFactor);
						woodPositions[i][1] = random.nextInt(10) + 1;
					} else {
						woodPositions[i][0] = windowWidth + random.nextInt(50);
						woodPositions[i][1] = (random.nextInt(10) + 1) * (-1);
					}
					woodPositions[i][2] = windowHeight
							- random.nextInt(windowHeight / 2);
					woodPositions[i][3] = (random.nextInt(10) + 20) * (-1);
					woodPositions[i][4] = random.nextInt(3) + 1;
				}

				public void run() {
					woodPositions = new int[50][5];
					int count = 300;
					for (int i = 0; i < 50; i++)
						setWoodPosition(i);
					boolean goOn = true;
					while (goOn) {
						goOn = false;
						for (int i = 0; i < 50; i++) {
							if (woodPositions[i][2] < windowHeight) {
								woodPositions[i][0] += woodPositions[i][1];
								woodPositions[i][2] += woodPositions[i][3];
								woodPositions[i][3] += woodPositions[i][4];
								repaint();
								try {
									Thread.sleep(1);
								} catch (Exception e) {
								}
								goOn = true;
							} else if (count > 0) {
								setWoodPosition(i);
								count--;
							}
						}
					}
					/* Zum Schluss Holz aus sichtbarem Bereich entfernen */
					for (int i = 0; i < 50; i++)
						woodPositions[i][2] = -200;
				}
			}).start();
		} else
			wood = false;
		repaint();
	}

	/**
	 * Setze das Spiel als gewonnen (hierdurch wird dieser Status in dem Panel
	 * ausgegeben.
	 * 
	 * @param playerCode
	 *            Spielerfarbe
	 */
	public void setWon(boolean playerCode) {
		if (playerCode == Const.PLAYER_RED)
			playerWon = Status.REDWIN;
		else
			playerWon = Status.BLUEWIN;
	}

	/**
	 * Zeichne Bild &uuml;ber das gesamte Panel
	 * 
	 * @param g
	 *            Graphics Objekt
	 * @param image
	 *            Bild
	 */
	public void drawFull(Graphics g, BufferedImage image) {
		g.drawImage(image, 0, 0, (int) (image.getWidth() * scaleFactor),
				(int) (image.getHeight() * scaleFactor), 0, 0,
				image.getWidth(), image.getHeight(), null);
	}

	/**
	 * Hier werden die Koordinaten f&uuml;r das Gitter des Spielfeldes in den
	 * Array abgelegt, um die Kegel sp&auml;ter anhand dieser Arrays schnell
	 * zeichnen zu k&ouml;nnen
	 */
	public void calcPos() {
		for (int y = 10; y >= 0; y--)
			posY[y] = (int) (scaleFactor * ((10 - y) * vSpace + yBegin));
		for (int y = 0; y < 11; y++) {
			for (int x = 0; x < 15; x++) {
				posX[y][x] = (int) (scaleFactor * (x * hSpace + xBegin - y
						* (hSpace / 2)));
			}
		}
	}

	/**
	 * Zeichne die Kegel
	 * 
	 * @param pins
	 *            Liste von Koordinaten (von den Kegeln)
	 * @param g
	 *            Graphics Objekt
	 * @param color
	 *            Spielerfarbe
	 */
	private void drawPins(CoordList pins, Graphics g, boolean color) {
		for (int row = 0; row < 11; row++) {
			for (int diagonal = 0; diagonal < 15; diagonal++) {
				for (Coord item : pins) {
					if (row == item.getRow() && diagonal == item.getDiagonal()) {
						if (!(selectedPin != null && item.equals(selectedPin))) {
							BufferedImage image;
							if (color == Const.PLAYER_RED)
								image = picRed;
							else
								image = picBlue;
							drawAt(g, image, row, diagonal);
						}
					}
				}
			}
		}
	}

	/**
	 * Zeichne ein Bild an die Stelle im Bild (x/y)
	 * 
	 * @param g
	 *            Graphics Objekt
	 * @param image
	 *            Bild
	 * @param x
	 *            X-Koordinate
	 * @param y
	 *            Y-Koordinate
	 */
	private void drawAtXY(Graphics g, BufferedImage image, int x, int y) {
		g.drawImage(image, x, y, x + (int) (image.getWidth() * scaleFactor), y
				+ (int) (image.getHeight() * scaleFactor), 0, 0, image
				.getWidth(), image.getHeight(), null);
	}

	/**
	 * Zeichne ein Bild an die Stelle im Gitter (Reihe/Diagonale)
	 * 
	 * @param g
	 *            Graphics Objekt
	 * @param image
	 *            Bild
	 * @param row
	 *            Reihe
	 * @param diagonal
	 *            Diagonale
	 */
	private void drawAt(Graphics g, BufferedImage image, int row, int diagonal) {
		g.drawImage(image, posX[row][diagonal], posY[row], posX[row][diagonal]
				+ (int) (image.getWidth() * scaleFactor), posY[row]
				+ (int) (image.getHeight() * scaleFactor), 0, 0, image
				.getWidth(), image.getHeight(), null);
	}

	// ---------------------------------------------------------------

	/**
	 * &Uuml;bermittle den gemachten Zug
	 */
	public void submit() {
		gui.setMove(move);
		gui.moveDone();
		this.reset();
	}

	/**
	 * Setze alle gemachten Eingaben zur&uuml;ck
	 */
	public void reset() {
		move = null;
		selectedPin = null;
		moveList = new MoveList(board, playerCode);
		this.repaint();
	}

	/* Eine Sammlung von Cheat-Codes */
	/**
	 * F&auml;rbt das Spielfeld und die Kegel in gold
	 */
	public void cheatGolden() {
		picRed = picRedGoldenPin;
		picBlue = picBlueGoldenPin;
		picBoard = picBoardGolden;
		picRedSelected = picRedSelectedGoldenPin;
		picBlueSelected = picBlueSelectedGoldenPin;
		picRedSelectedTrans = picRedSelectedGoldenTransPin;
		picBlueSelectedTrans = picBlueSelectedGoldenTransPin;
		this.repaint();
	}

	/**
	 * F&auml;rbt das Spielfeld normal(entfernt Goldcheat)
	 */
	public void cheatAntiGold() {
		picRed = picRedPin;
		picBlue = picBluePin;
		picBoard = picBoardNormal;
		picRedSelected = picRedSelectedPin;
		picBlueSelected = picBlueSelectedPin;
		picRedSelectedTrans = picRedSelectedTransPin;
		picBlueSelectedTrans = picBlueSelectedTransPin;
		this.repaint();
	}

	// ---------------------------------------------------------------
	/* Variablen zum Zeichnen */
	private BufferedImage picRed, picBlue, picRedSelected, picBlueSelected,
			picRedSelectedTrans, picBlueSelectedTrans, picBoard, picJump,
			picMove, picRedWon, picBlueWon;
	private BufferedImage picRedPin, picBluePin, picRedSelectedPin,
			picBlueSelectedPin, picRedSelectedTransPin,
			picBlueSelectedTransPin, picRedGoldenPin, picBlueGoldenPin,
			picRedSelectedGoldenPin, picBlueSelectedGoldenPin,
			picRedSelectedGoldenTransPin, picBlueSelectedGoldenTransPin;
	private BufferedImage picBoardNormal, picBoardGolden, picBoardActive;
	public BufferedImage picWoodGrid, picWood;

	private int playerWon = 0;

	private boolean activePlayer;
	private boolean playerCode;
	private Board board;

	private boolean wood = false;
	public int woodPositions[][];

	private double scaleFactor;
	private int posX[][];
	private int posY[];

	/* Variablen zum Erstellen eines Zuges */
	private Coord selectedPin;
	private Move move = null;
	private GUI gui;
	private MoveList moveList;

	// ---------------------------------------------------------------

	private static final String redWonPath = "gfx/RedWins.png";
	private static final String blueWonPath = "gfx/BlueWins.png";

	private static final String redPinPath = "gfx/FigurRed.png";
	private static final String bluePinPath = "gfx/FigurBlue.png";
	private static final String figureJumpPath = "gfx/FigurJump.png";
	private static final String figureMovePath = "gfx/FigurMove.png";
	private static final String boardPath = "gfx/GridSingle.png";
	private static final String boardActive = "gfx/BoardActive.png";
	private static final String redPinSelectedPath = "gfx/FigurRedSelected.png";
	private static final String bluePinSelectedPath = "gfx/FigurBlueSelected.png";
	private static final String redPinSelectedTransPath = "gfx/FigurRedSelectedTrans.png";
	private static final String bluePinSelectedTransPath = "gfx/FigurBlueSelectedTrans.png";
	private static final String redPinGoldenPath = "gfx/FigurRedGold.png";
	private static final String bluePinGoldenPath = "gfx/FigurBlueGold.png";
	private static final String redPinSelectedGoldenPath = "gfx/FigurRedSelectGold.png";
	private static final String bluePinSelectedGoldenPath = "gfx/FigurBlueSelectGold.png";
	private static final String redPinSelectedGoldenTransPath = "gfx/FigurRedSelectGoldTrans.png";
	private static final String bluePinSelectedGoldenTransPath = "gfx/FigurBlueSelectGoldTrans.png";
	private static final String boardGoldenPath = "gfx/GridSingleGold.png";

	private static final String woodGridPath = "gfx/GridWood.png";
	private static final String woodPath = "gfx/Wood.png";

	private static final int hSpace = 140;
	private static final int vSpace = 121;
	private static final int xBegin = 332;
	private static final int yBegin = 54;

	private static final int activePlayerX = 150;
	private static final int activePlayerY = 140;

	private static final long serialVersionUID = 1L;
}
