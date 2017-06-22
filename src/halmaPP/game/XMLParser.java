package halmaPP.game;

import halmaPP.player.MoveAndState;
import halmaPP.preset.Move;

import java.io.File;

import java.util.ArrayList;

import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


/**
 * &Uuml;bernimmt das Laden von Spielst&auml;nden. Erbt dabei von MoveAndState.
 * Die Moves k&ouml;nnen also einfach ausgelesen werden, indem die Liste abgearbeitet wird!
 * <b>Das Spiel wird beendet, wenn ein falsches File &uuml;bergeben wurde!</b>
 * @author sebastian horwege
 *
 */
public class XMLParser extends ArrayList<MoveAndState>{
	/**
	 * Konstruktor, der XmlFile einliest und auswertet.  
	 * @param fXmlFile
	 */
	public XMLParser(File fXmlFile) {
		Move curMove = null;
		Move tmpMove = null;
		  try {
				//lese xml File und parse es
				 DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				
				
				docBuilder.setErrorHandler(new xmlErrorHandler());
				
				Document doc = docBuilder.parse(fXmlFile);
				doc.getDocumentElement().normalize();
		 		
				//erzeuge eine NodeList mit allen Moves des Dokuments
				NodeList moveList = doc.getElementsByTagName("move");
			
		 		//setze spielertypen
		 		this.bluePlayerType = Integer.valueOf(doc.getDocumentElement().getAttribute("bluePlayerType"));
		 		this.redPlayerType  = Integer.valueOf(doc.getDocumentElement().getAttribute("redPlayerType"));
		 		this.duration 		= Long.valueOf(doc.getDocumentElement().getAttribute("duration"));
		 		
		 		for (int temp = 0; temp < moveList.getLength(); temp++) {
		 			moveCount++;
		 		   Node xmlMove = moveList.item(temp);
		 		   NodeList coordList  = xmlMove.getChildNodes();
		 		  NamedNodeMap attMove = xmlMove.getAttributes(); 
		 		   for(int i=0; i<coordList.getLength();i++) {
		 			   
		 			   Node coordNode = coordList.item(i);
		 			   NamedNodeMap attList = coordNode.getAttributes();
		 			   
		 			   if(i==0) {
		 				  curMove = new Move(
			 					   Integer.parseInt(attList.getNamedItem("row").getNodeValue()),
			 					   Integer.parseInt(attList.getNamedItem("diagonal").getNodeValue()));
		 				 tmpMove = curMove;
		 			   } else {
		 			   curMove.setNext(new Move(
		 					   Integer.parseInt(attList.getNamedItem("row").getNodeValue()),
		 					   Integer.parseInt(attList.getNamedItem("diagonal").getNodeValue())));
		 			   curMove = curMove.getNext();
		 			   }
		 		   }
		 		   this.add(new MoveAndState(tmpMove,Integer.parseInt(attMove.getNamedItem("duration").getNodeValue())));	//hier noch die lang variable von der dauer des Moves einfuegen
				   
				}
		  
			  } catch (SAXException e) {
				Game.debug(Const.ESC_BG_RED + "File: " + fXmlFile.toString() + " enthaelt keinen Spielstand");
				this.success = false;
			  } catch (Exception e) {
				Game.debug(Const.ESC_BG_RED + "File: " + fXmlFile.toString() + " konnte nicht geladen werden");
				this.success = false;
			  } 
			 
	}
	
	/**
	 * Liefert true, wenn Datei geladen werden konnte.
	 * @return Erfolgsboolean
	 */
	public boolean success(){
		return this.success;
	}
	
	
	
	//Getter und Setter 
	/**
	 * Gesamtl&auml;nge des Spiels in Millisekunden
	 * @return Millisekunden seit Spielbeginn!
	 */
	public long getDuration() {
		return duration;
	}
	/**
	 * Liefert Spielertypen des Blauen Spielers
	 * @return Spielertyp siehe Const Klasse
	 */
	public int getBluePlayerType() {
		return bluePlayerType;
	}
	/**
	 * Liefert Spielertypen des Roten Spielers
	 * @return Spielertyp siehe Const Klasse
	 */
	public int getRedPlayerType() {
		return redPlayerType;
	}
	/**
	 * Gesamtmenge der gespeicherten Moves des Spielstandes
	 * @return Zahl der Moves
	 */
	public int getMoveCount() {
		return moveCount;
	}
	
	private static final long serialVersionUID = 1L;
	
	//Klassenvariablen
	
	/**
	 * Gesamtzahl aller Moves im Dokument
	 */
	private int moveCount = 0;
	/**
	 * Spielertyp blau
	 */
	private int bluePlayerType = 0;
	/**
	 * Spielertyp rot
	 */
	private int redPlayerType  = 0;
	/**
	 * Dauer des Spiels
	 */
	private long duration = 0;
	/**
	 * erfolgreich?
	 */
	private boolean success = true;
}



/**
 * Behandelt die Fehler vom Parser
 * @author Sebastian Horwege
 *
 */
class xmlErrorHandler implements ErrorHandler{

	
	public void error(SAXParseException arg) throws SAXException {
		throw new SAXException("Fehler beim Parsen");
	}
	public void fatalError(SAXParseException arg) throws SAXException {
		throw new SAXException("Fataler Parsing Fehler");
	}
	public void warning(SAXParseException arg) throws SAXException {
		throw new SAXException("Warnung Fehler beim Parsen");
	}
}
