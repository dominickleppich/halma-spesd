package halmaPP.game;

/**
 * Argmumentarray der Kommandozeile kann nach Flags durchsucht werden und zudem auch deren Inhalt zurückgeben.
 * @author sebastian Horwege
 *
 */
public class ArgsParser {
	/**
	 * Konstruktor
	 * @param args Argumentarray
	 */
	public ArgsParser(String[] args) {
		this.argArr = args;
	}
	/**
	 * Sucht nach einem Argument und liefert dessen Position im Argumentarray zurück. Liefert -1, wenn Argument nicht gefunden wurde
	 * @param argument gesuchtes Argument
	 * @return position des Arguments im ArgumentsArray
	 */
	public int getArgumentPos(String argument) {
		for(int i = 0; i< this.argArr.length; i++)
			if(this.argArr[i].compareTo(argument) == 0)
				return i;
		return -1;
	}
	/**
	 * Sucht nach Argument und liefert true zurück, wenn es gefunden wurde!
	 * @param argument Argument, nach dem gsucht werden soll
	 * @return true, wenn argument gefunden wurde
	 */
	public boolean isSet(String argument) {
		if(this.getArgumentPos(argument) != -1)
			return true;
		return false;
	}
	
	/**
	 * Durchsucht Kommandozeilenparameter nach Argumenten aus dem übergebenen Array 
	 * @param argument Array aus Strings
	 * @return liefert true, wenn mindestens ein Argument in Kommandozeile stand 
	 */
	public boolean isSet(String[] argument) {
		for(int i=0; i<argument.length;i++)
			if(this.getArgumentPos(argument[i]) != -1)
				return true;
		return false;
	}
	/**
	 * Wenn argument exisitert und ihm ein Wert zugewiesen wurde, dann wird dieser zurück gegben:
	 *  Kommandozeilen eingabeformat :{@code $ --argument value}
	 * @param argument
	 * @return "", wenn argument keine Value hat, String mit Valueinhalt, wenn doch!
	 */
	public String getValueOf(String argument) {
		String[] array = {argument};
		return this.getValueOf(array);
		
	}
	/**
	 * Gleiches, wie String getValueOf(String) jedoch mit array als argument
	 * @param argument array mit suchstrings
	 * @return Inhalt des flags
	 */
	public String getValueOf(String[] argument) {
		int i;
		for(int j=0;j<argument.length;j++) {
		 i = this.getArgumentPos(argument[j]);
		if(i!=-1 && i < this.argArr.length-1) {
			return new String(this.argArr[i+1]);
		}
		}
		return "";
	}
	/**
	 * lokale Referenz des Kommandozeilenparameterarrays
	 */
	private String[] argArr;
	
}
