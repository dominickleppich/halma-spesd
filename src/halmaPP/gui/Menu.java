package halmaPP.gui;

import halmaPP.game.*;

import javax.swing.*;

import java.awt.event.*;
import java.awt.TextField;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.event.*;

/**
 * Diese Klasse dient der Erstellung eines Men&uuml;s f&uuml;r das GUI Dabei wird
 * der besseren Anbringbarkeit von einem JPanel geerbt.
 * 
 * @author Philip Langer, Dominick Leppich
 * 
 */
public class Menu extends JPanel {
	/**
	 * Konstruktor
	 */
	public Menu() {
		super();

		/*
		 * Der Knopf "Speichern" speichert per Filedialog alle Spielzuege als
		 * XML im user.dir
		 */
		save = new JMenuItem("Speichern");
		save.setMnemonic(KeyEvent.VK_S);
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Game.pause();
				String wd = System.getProperty("user.dir");
				JFileChooser fc = new JFileChooser(wd);
				int rc = fc.showDialog(null, "Save");
				if (rc == JFileChooser.APPROVE_OPTION) {
					Game.save(fc.getSelectedFile());
				}
				Game.unpause();
			}
		});

		/* Der Knopf "Laden" */
		load = new JMenuItem("Laden");
		load.setMnemonic(KeyEvent.VK_L);
		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				/*
				 * An dieser Stelle wird beim ersten druecken von "Laden" ein
				 * Filedialog aufgerufen, der das gewuenschte Savegame
				 * erstmal nur einliest
				 */
				String wd = System.getProperty("user.dir");
				final JFileChooser fc = new JFileChooser(wd);
				int rc = fc.showDialog(null, "Load");
				int a = 0;
				if (rc == JFileChooser.APPROVE_OPTION) {

					/*
					 * Hier wird aus dem geladenen XML - Savegame die Anzahl
					 * aller Zuege ausgelesen
					 */
					XMLParser xmlFile = new XMLParser(fc.getSelectedFile());
					a = xmlFile.getMoveCount();
					if(!xmlFile.success())
						return;
				}
				/*
				 * Nach dem Einlesen oeffnet sich ein Fenster mit Regler, bei
				 * dem der zu ladende Spielstand auswaehlbar ist. Dabei kann per
				 * Slider oder auch per Textfield der gewueschte Spielstand
				 * eingegeben werden.
				 */
				final JFrame loadSubF = new JFrame("Spielstand wählen");
				Box loadBox = Box.createVerticalBox();
				JPanel loadSub = new JPanel();
				final JSlider slider2 = new JSlider(0, a, 0);
				final TextField loadNum = new TextField("0", String.valueOf(a)
						.length());
				loadNum.addKeyListener(new KeyAdapter() {

					/*
					 * Hier erfolgt die Echtzeitkontrolle ob Slider / Textfeld
					 * uebereinstimmen
					 */
					public void keyPressed(KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_ENTER) {
							slider2.setValue(Integer
									.parseInt(loadNum.getText()));
						}
					}
				});
				slider2.setBorder(BorderFactory
						.createTitledBorder("Zu ladenden Spielstand(Runde):"));
				slider2.setMajorTickSpacing((int) a / 5);
				slider2.setMinorTickSpacing((int) a / 2);
				slider2.setPaintTicks(true);
				slider2.setPaintLabels(true);
				slider2.addMouseListener(new MouseAdapter() {
					public void mouseReleased(MouseEvent arg0) {
						loadNum.setText(String.valueOf(slider2.getValue()));
					}
				});
				slider2.addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent arg0) {
						loadNum.setText(String.valueOf(slider2.getValue()));
					}
				});

				/* Hier der Button, der das eigentliche Spiel initialisiert */
				JButton confirm = new JButton("Laden");
				confirm.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						loadSubF.setVisible(false);
						Game.newGame();

						Game.load(fc.getSelectedFile(), slider2.getValue());

					}
				});
				loadSub.add(slider2);
				loadSub.add(loadNum);
				loadBox.add(loadSub);
				loadBox.add(confirm);
				loadSubF.add(loadBox);
				loadSubF.pack();
				loadSubF.setVisible(true);
			}
		});

		/* Der Knopf "Beenden" beendet das Spiel */
		exit = new JMenuItem("Beenden");
		exit.setMnemonic(KeyEvent.VK_E);
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Game.exit();
			}
		});

		/*
		 * Das Menue fuer die einzelnen Spielersorten (Human, KI,
		 * Netzwerk)
		 */
		playerList = new JMenu[Const.PLAYER_TYPE.length];
		red = new JMenuItem[Const.PLAYER_TYPE.length];
		blue = new JMenuItem[Const.PLAYER_TYPE.length];
		for (int i = 0; i < Const.PLAYER_TYPE.length; i++) {
			playerList[i] = new JMenu("Neuer " + Const.PLAYER_NAME[i]);
			red[i] = new JMenuItem("Rot");
			blue[i] = new JMenuItem("Blau");

			red[i].addActionListener(new MenuListener(Const.PLAYER_RED,
					Const.PLAYER_TYPE[i], Const.PLAYER_NAME[i]));
			playerList[i].add(red[i]);

			blue[i].addActionListener(new MenuListener(Const.PLAYER_BLUE,
					Const.PLAYER_TYPE[i], Const.PLAYER_NAME[i]));
			playerList[i].add(blue[i]);

			playerList[i].setMnemonic(KeyEvent.VK_P);

			player.add(playerList[i]);
		}
		
		/* Menueeintrag fuer Hilfe */
		helpm = new JMenuItem("Tutorial");
		helpm.setEnabled(false);
		helpm.setMnemonic(KeyEvent.VK_H);
		helpm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (helpWindow != null)
					helpWindow.setVisible(true);
			}
		});

		/* Hilfefenster erzeugen in neuem Frame */
		new Thread(new Thread() {
			public void run() {
				JFrame tempWindow = new JFrame();
				final HelpPanel helpPanel = new HelpPanel();
				Box mainBox = Box.createVerticalBox();
				mainBox.add(helpPanel);

				/* Buttons erstellen */
				Box buttonBox = Box.createHorizontalBox();
				JButton back = new JButton("Zurück");
				back.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent arg0) {
						helpPanel.back();
					}
				});
				JButton next = new JButton("Weiter");
				next.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent arg0) {
						helpPanel.next();
					}
				});
				buttonBox.add(back);
				buttonBox.add(Box.createHorizontalGlue());
				buttonBox.add(next);
				mainBox.add(buttonBox);

				tempWindow.add(mainBox);
				tempWindow.setResizable(false);
				tempWindow.pack();
				tempWindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

				helpWindow = tempWindow;
				helpm.setEnabled(true);
			}
		}).start();

		man = new JMenuItem("Handbuch");
		man.setMnemonic(KeyEvent.VK_B);
		man.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Runtime run = Runtime.getRuntime();	
				try {
					run.exec("gnome-open Handbuch.htm &");
				} catch (IOException e1) {
					
				}
			}
		});

		lanHost = new JMenuItem("Netzwerkspiel hosten");
		lanHost.setMnemonic(KeyEvent.VK_H);
		lanHost.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Game.setMultiplayer(!Game.getMultiplayer());
				if (Game.getMultiplayer()) {
					Game.setJoining(false);
					lanJoin.setText("Netzwerkspiel beitreten");
					lanHost.setText("zu Einzelspieler wechseln");
					setPlayerEnabled(Const.PLAYER_BLUE, false);
					Game.print("Waehlen Sie jetzt ueber \"Spieler\" Ihren Spieler aus!");
					JOptionPane.showMessageDialog(null, "Waehlen Sie jetzt ueber \"Spieler\" Ihren Spieler aus!", "Netzwerkspiel hosten...", JOptionPane.PLAIN_MESSAGE);
				} else {
					lanHost.setText("Netzwerkspiel hosten");
					setPlayerEnabled(Const.PLAYER_BLUE, true);
					Game.print("gewechselt zu Einzelspieler");
				}
			}
		});
		
		/* Der Knopf "Netzwerkspiel" erstellt ein neues Game */
		lanJoin = new JMenuItem("Netzwerkspiel beitreten");
		lanJoin.setMnemonic(KeyEvent.VK_H);
		lanJoin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Game.setJoining(!Game.getJoining());
				if (Game.getJoining()) {
					/* Laden und Speichern ausgrauen */
					load.setEnabled(false);
					save.setEnabled(false);
					/* "ich hoste" - boolean auf false setzen */
					Game.setMultiplayer(false);
					lanHost.setText("Netzwerkspiel hosten");
					lanJoin.setText("zu Einzelspieler wechseln");
					/* Roten Spieler ausgrauen, denn Clients duerfen nur blau sein */
					setPlayerEnabled(Const.PLAYER_RED, false);
					Game.print("Waehlen Sie jetzt ueber \"Spieler\" Ihren Spieler aus!");
					JOptionPane.showMessageDialog(null, "Waehlen Sie jetzt ueber \"Spieler\" Ihren Spieler aus!", "Netzwerkspiel beitreten...", JOptionPane.PLAIN_MESSAGE); 
				} else {
					load.setEnabled(true);
					save.setEnabled(true);
					lanJoin.setText("Netzwerkspiel beitreten");
					setPlayerEnabled(Const.PLAYER_RED, true);
					Game.print("gewechselt zu Einzelspieler");
				}
			}
		});

		/* Der Knopf "Neu" erstellt ein neues Game */
		newGame = new JMenuItem("Neu");
		newGame.setMnemonic(KeyEvent.VK_N);
		newGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lan.setEnabled( true);
				lanEnabled = true;
				Game.newGame();
				setPlayerEnabled(Const.PLAYER_RED, true);
				setPlayerEnabled(Const.PLAYER_BLUE, true);
			}
		});

		/* Zusammensetzen der Menueunterpunkte */
		file.add(newGame);
		file.addSeparator();
		file.add(save);
		file.add(load);
		file.addSeparator();
		file.add(exit);

		help.add(man);
		help.add(helpm);

		lan.add(lanHost);
		lan.add(lanJoin);

		mainmenu.add(file);
		mainmenu.add(player);
		mainmenu.add(help);
		mainmenu.add(lan);

		this.add(mainmenu);
	}

	// ---------------------------------------------------------------

	/**
	 * Erm&oumlglicht das Laden von Spielst&auml;nden
	 */
	public void setLoadEnabled(boolean enabled) {
		load.setEnabled(enabled);
	}

	/**
	 * Setze den Men&uuml;eintrag f&uuml;r den jeweiligen Spieler auf
	 * aktiv/inaktiv.
	 * 
	 * @param playerCode
	 *            Spielerfarbe
	 * @param enabled
	 *            Aktiv
	 */
	public void setPlayerEnabled(boolean playerCode, boolean enabled) {
		if (playerCode == Const.PLAYER_RED)
			for (int i = 0; i < Const.PLAYER_TYPE.length; i++)
				red[i].setEnabled(enabled);
		else
			for (int i = 0; i < Const.PLAYER_TYPE.length; i++)
				blue[i].setEnabled(enabled);
	}
	
	/**
	 * LAN Schalter-Ausgrauung umschalten
	 */
	public static void switchLAN() {
		lanEnabled = !lanEnabled;
		lan.setEnabled( lanEnabled);
	}

	/**
	 * Graut Men&uuml; Datei aus w&auml;hrend des Ladens
	 */
	public static void switchFile() {
		fileEnabled = !fileEnabled;
		file.setEnabled( fileEnabled);
	}
	// ---------------------------------------------------------------

	private static boolean lanEnabled = true;
	private static boolean fileEnabled =true;
	public JFrame helpWindow;

	private JMenu playerList[];

	private JMenuItem blue[];
	private JMenuItem red[];
	private JMenuItem exit;
	private JMenuItem man;
	private JMenuItem newGame;
	private JMenuItem lanHost;
	private JMenuItem lanJoin;
	private final JMenuItem helpm;
	private JMenuItem load;
	private JMenuItem save;

	private JMenuBar mainmenu = new JMenuBar();

	private static JMenu file = new JMenu("Datei");
	private JMenu player = new JMenu("Spieler");
	private JMenu help = new JMenu("Hilfe");
	private static JMenu lan = new JMenu("LAN");

	// ---------------------------------------------------------------

	private static final long serialVersionUID = 1L;
}
