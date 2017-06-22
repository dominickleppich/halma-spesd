package halmaPP.game;

import halmaPP.board.*;

/**
 * In diesem Interface werden diverse Konstanten abgelegt, die alle benutzen
 * k&ouml;nnen! Eigene Konstanten k&ouml;nnen gerne hinzugef&uuml;gt werden!
 * 
 * 
 * @author Sebastian Horwege
 */
public interface Const {
	/* Escapesequenzen fuer debugtextformatierung */

	/** Formatierung zuruecksetzen */
	public static final String ESC_STD = "\u001B[0m";
	/** Text fett */
	public static final String ESC_BOLD = "\u001B[1m";
	/** Text unterstrichen */
	public static final String ESC_UNDERSCORE = "\u001B[4m";

	/* Escapesequenzen fuer debugtextfarbe */
	public static final String ESC_BLACK = "\u001B[30m";
	public static final String ESC_RED = "\u001B[31m";
	public static final String ESC_GREEN = "\u001B[32m";
	public static final String ESC_YELLOW = "\u001B[33m";
	public static final String ESC_BLUE = "\u001B[34m";
	public static final String ESC_PURPLE = "\u001B[35m";
	public static final String ESC_CYAN = "\u001B[36m";
	public static final String ESC_WHITE = "\u001B[37m";
	/* Escapesequenzen fuer debugtexthintergrund */
	public static final String ESC_BG_BLACK = "\u001B[40m";
	public static final String ESC_BG_RED = "\u001B[41m";
	public static final String ESC_BG_GREEN = "\u001B[42m";
	public static final String ESC_BG_YELLOW = "\u001B[43m";
	public static final String ESC_BG_BLUE = "\u001B[44m";
	public static final String ESC_BG_PURPLE = "\u001B[45m";
	public static final String ESC_BG_CYAN = "\u001B[46m";
	public static final String ESC_BG_WHITE = "\u001B[47m";
	/** Regulaerer Ausdruck, um Escapesequenzen zu erkennen! */
	public static final String ESC_REG_EXP = "\\u001B\\[[0-9]*m";

	public static final boolean PLAYER_RED = true;
	public static final boolean PLAYER_BLUE = false;

	public static final CoordList BLUE_HOUSE_COORD = new CoordList();
	public static final CoordList RED_HOUSE_COORD = new CoordList();

	public static final int NO_PLAYER = 0;
	public static final int HUMAN_PLAYER = 1;
	public static final int EASYAI_PLAYER = 2;
	public static final int MEDIUMAI_PLAYER = 3;
	public static final int HARDAI_PLAYER = 4;
	public static final int NET_PLAYER = 5;

	public static final String[] PLAYER_NAME = { "Menschlicher Spieler",
			"Einfacher Computerspieler", "Mittlerer Computerspieler",
			"Schwerer Computerspieler" };
	public static final String[] PLAYER_NAME_SHORT = { "Mensch", "Comp. Einf.",
			"Comp. Mittl.", "Comp. Schw.", "Netzwerk" };
	public static final int[] PLAYER_TYPE = { 1, 2, 3, 4};

	/* Konstanten für Kommandozeilenparameter */
	public static final String[] CMD_DEBUG = { "--debug", "-d" };
	public static final String[] CMD_COLORIZED = { "--colorized", "-c" };
	public static final String[] CMD_LOAD = { "--load", "-l" };
	public static final String[] CMD_HELP = { "--help", "-h" };
	public static final String[] CMD_NO_GUI_INPUT = { "--noguiinput", "-ngi" };
	public static final String[] CMD_PLAYER_RED  = { "--red", "-r" };
	public static final String[] CMD_PLAYER_BLUE = { "--blue", "-b" };
	
	/** HELP Text der bei gesetztem flag gezegit werden soll */
	public static final String TXT_HELP = "" +
	"*********************************************\n" +
	"*                                           *\n" +
	"*              HALMAPP-SPESD                *\n" +
	"*                                           *\n" +
	"*********************************************\n" +
	"Willkommen bei HALMAPP-SPESD, dem Halmaspiel \n" +
	"fuer nur 2 Personen.\n" +
	"\n" +
	"Optionen:\n" +
	"-h \t --help \t\t zeigt diesen Helptext\n" +
	"-d \t --debug \t\t setzt Debugschalter\n" +
	"-c \t --colorized \t\t farbige Kommandozeilenausgabe\n" +
	"-l \t --load \t<file>\t gespeicherten Spielstand laden\n" +
	"-r \t --red \t<playertype\t Erzeugt neuen roten Spieler\n" +
	"-b \t --blue \t<playertype\t Erzeugt neuen blauen Spieler\n" +
	"-ngi \t --noguiinput \t\t Spiel wird ueber Kommandozeile gesteuert\n" +
	"" +
	"\n\nPlayerTypes:\n" +
	"human\n" +
	"easyai\n" +
	"mediumai\n" +
	"hardai\n" +
	"" +
	"\n\nAutoren:\n" +
	"Domminik \t Eduard \t Sebastian \t Philip \t Sebastian\n";
	

	public static final int AI_WAIT_TIME = 500;

}
