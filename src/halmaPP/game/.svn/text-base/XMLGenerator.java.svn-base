package halmaPP.game;


import halmaPP.preset.*;
import halmaPP.game.Const;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;
import java.io.File;

/**
 * Klasse, die fuer Game ein XML Objekt vom laufenden Spiel erzeugt und bei Bedarf speichert
 * Struktur des XML files wie folgt:
 * <pre>
 * &lt;game bluePlayerType="1" duration="40462" redPlayerType="1"&gt;
 * 	&lt;move duration="12627"&gt;
 * 		&lt;coord diagonal="9" row="0"/&gt;&lt;coord diagonal="9" row="2"/&gt;
 * 	&lt;/move&gt;
 *	&lt;move duration="6795"&gt;
 *		&lt;coord diagonal="2" row="2"/&gt;&lt;coord diagonal="3" row="3"/&gt;
 * 	&lt;/move&gt;
 * &lt;/game&gt;
 * </pre>
 * @author sebastian horwege
 *
 */
class XMLGenerator{
	/**
	 * Erzeugt ein neues Dokumentobjekt, setzt Timestampvariablen und den root tag
	 */
	public XMLGenerator() {
		this.startTimestamp   = System.currentTimeMillis();
		this.currentTimestamp = this.startTimestamp;
		DocumentBuilder docBuilder;
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		try {
			docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.newDocument();
			root = doc.createElement("game");			//erzeuge neues root element
			doc.appendChild(root);						//und speichere es im document
		}catch (ParserConfigurationException e){
			Game.errorExit("Fehler beim Erzeugen des XML Generator Objekts");
		}
	
	}


	/**
	 * @see XMLGenerator#addMove(Move, long)
	 */
	public void addMove(Move move){
		this.addMove(move, (System.currentTimeMillis() / this.timePrecision) - this.currentTimestamp);	
	}


	/**
	 * F&uuml;gt einen Move dem XML Dokument hinzu!
	 * @param move move, der in xml umgewandelt werden soll
	 * @param duration dauer des moves in millisekunden
	 */
	public void addMove(Move move, long duration){
		
		Element xmlMove = doc.createElement("move");
		xmlMove.setAttribute("duration", String.valueOf(duration));
		this.currentTimestamp = System.currentTimeMillis();				//timestamp neu setzen, fuer neachsten move
		
		
		root.appendChild(xmlMove);
		addCoord(move,xmlMove);
		
		
	}


	/**
	 * Schreibt Koordinaten des Moves rekursiv zwischen den richtigen Move tag!
	 * @param move Position im Move
	 * @param xmlMove xml tag, in den die Kooridnaten des Moves geschrieben werden
	 * @see XMLGenerator#addMove(Move, long)
	 */
	private void addCoord(Move move, Element xmlMove) {
		
		Element coord = doc.createElement("coord");
		xmlMove.appendChild(coord);
		
		coord.setAttribute("diagonal",  String.valueOf(move.getDiagonal()) );	//schreibt coordinaten
		coord.setAttribute("row", 		String.valueOf(move.getRow())      );
		
		if(move.getNext() == null)
			return ;
		else
			addCoord(move.getNext(),xmlMove);				//rekursion
	}


	/**
	 * Versucht XML data in Datei zu speichern!
	 * @param file File in dem das XML document gespeichert werden soll
	 * @throws TransformerConfigurationException 
	 */
	public boolean save(File file) {
		if(Game.getPlayerType(Const.PLAYER_RED) == Const.NET_PLAYER || Game.getPlayerType(Const.PLAYER_BLUE) == Const.NET_PLAYER)
			return false;									//darf nicht speichern, wenn NETZWERKSPIEl
		
															//setzt Attribute des root Elements
		root.setAttribute("duration", String.valueOf((System.currentTimeMillis() / this.timePrecision) - this.startTimestamp));
		root.setAttribute("redPlayerType" , String.valueOf(Game.getPlayerType(Const.PLAYER_RED)));
		root.setAttribute("bluePlayerType" , String.valueOf(Game.getPlayerType(Const.PLAYER_BLUE)));
		
		try {
			
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(file);
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.transform(source, result);
		
			Game.debug(Const.ESC_BG_CYAN + "Spielstand wurde erfolgreich gespeichert");
		}catch (Exception e) {
			Game.debug(Const.ESC_BG_CYAN + "Speichern nicht moeglich in: " + file.toString());
			return false;
		}
		return true;
	}


	/** wird immer aktualisiert, wenn Move zu xml exportiert wurde */
	private long currentTimestamp;
	
	/** xml Objekt auf dem gearbeitet wird */
	Document doc;
	
	/** Wurzel des XML Objekts wird im Konstruktor zum "game" tag */
	Element root;
	
	/** Wird im Konstruktor gesetzt, deshalb muss Objekt auch beim Anfang eines neuen Spiels neu erzeugt werden */
	private long   startTimestamp;
	
	/** Genauigkeit der Zeitmessung pro Zu in Millisekunden */
	private final int timePrecision = 1;
	
	
}
	
	