package com.fump;
import java.util.Vector;
import java.util.StringTokenizer;

/**
 * @version $Id: Eintrag.java,v 1.6 2001/07/11 16:50:28 eschner Exp $
 */

public class Eintrag{
	private String name;
	public Vector adressen;
	private String alias;
	private boolean isGroup;

	/**
	*@param String name		Name des Eintrags
	*@param boolean istGruppe		ob der Eintrag eine Gruppe oder Person ist
	*/
	public Eintrag(String name, boolean istGruppe){
		this(name,"", istGruppe);
	}
	
	/**
	*@param boolean istGruppe		ob der Eintrag eine Gruppe oder Person ist
	*/
	public Eintrag(boolean istGruppe){
		this("","",istGruppe);
	}

	/**
	*@param String name		Name des Eintrags
	*@param String alias	Alias des Eintrags
	*@param boolean istGruppe		ob der Eintrag eine Gruppe oder Person ist
	*/
	public Eintrag (String name,String alias, boolean istGruppe){
		adressen = new Vector();
		setIsGroup(istGruppe);
		setName(name);
		setAlias(alias);
	}

	/**
	*@param Eintrag neu		die neue Version des Eintrags
	*/

	public void aendern(Eintrag neu){
		setName(neu.getName());
		setAlias(neu.getAlias());
		addAdresse(neu.getAdresse());
	}

	/**
	*@return String-Repräsentation des Eintrags
	*/
	
	public String toString(){
		String adr = "";
		for (int i=0;i<adressen.size();i++) adr+= " "+adressen.elementAt(i);
		return (name+" "+alias+adr);
	}
  
  /**
  *@param boolean isGroup		ob der Eintrag eine Gruppe oder Person ist
  */
  public void setIsGroup(boolean isGroup){
		this.isGroup = isGroup;
	}
	
	/**
	*@return ob der Eintrag eine Gruppe oder Person ist
	*/
	
	public boolean getIsGroup(){
		return this.isGroup;
	}

	/**
	*@param String name		Name des Eintrags
	*/

	public void setName(String name){
		this.name = name;
	}
	
	/**
	*@return String		Name des Eintrags
	*/

	public String getName(){
		return this.name;
	}
	
	/**
	*@param String alias		Alias des Eintrags
	*/

	public void setAlias(String alias){
		this.alias = alias;
	}

	/**
	*@return String		Alias des Eintrags
	*/

	public String getAlias(){
		return this.alias;
	}

	/**
	*@return String[]			String[0] = Name des Eintrags, String[1..length-1]=Adressen
	*/

	public String[] getAdressenArray(){
		String[] rueckgabe = new String[this.adressen.size()+1];
		rueckgabe[0] = this.getName();
		for(int i = 0; i<adressen.size(); i++){
			rueckgabe[i+1] = (String)adressen.elementAt(i);
		}
		return rueckgabe;
	}

	/**
	*@return String		alle email-Adressen in einem String, getrennt durch ; 
	*/

	public String getAdressenString(){
		String s = "";
		for (int i = 0; i<adressen.size(); i++){
			s += (adressen.elementAt(i)+"; ");
		}
		return s;
	}

	/**
	*@return String - alle email-Adressen des Eintrag in einem String, getrennt durch " "
	*/
	public String getAString(){
		String s = "";
		for(int i = 0; i<adressen.size(); i++){
			s += (adressen.elementAt(i)+" ");
		}
		return s;
	}
	/**
	*@param String adresse		fügt dem Eintrag adresse hinzu
	*/

	public void addAdresse(String adresse){
		adressen.add(adresse);
	}

	public void addAdressenString(String astring){
		StringTokenizer tmp = new StringTokenizer(astring);
		while(tmp.hasMoreTokens()){
			this.addAdresse(tmp.nextToken());
		}
	}

	/**
	*@param Vector liste		fügt dem Eintrag die im Vector stehenden Adressen hinzu
	*/

	public void addAdresse(Vector liste){
		for (int i=0;i<liste.size();i++){
			adressen.add(liste.elementAt(i));
		}
	}

	/**
	*@return		gibt den Adressen-Vektor des Eintragobjektes zurück
	*/

	public Vector getAdresse(){
		return adressen;
	}

	/**
	*@param Vector liste		ersetzt den aktuellen Adressenvektor durch liste
	*/

	public void setAdresse(Vector liste){
		adressen=liste;
	}

	/**
	*@param String adresse		zu entfernende Adresse
	*@throws Exception
	*/
	
	public void removeAdresse(String adresse) throws Exception{
		for (int i=0;i<adressen.size();i++){
			if (adresse==adressen.elementAt(i)) {
				adressen.removeElementAt(i);
				return;
			}
		}	
		throw new Exception();
	}
	
	public void loescheAdressen(){
		this.adressen.removeAllElements();
	}
	
	public String getEintragString(){
		return(this.getName()+" "+this.getAlias()+" "+this.getAdressenString());
	}

	/**
	*@param String adresseAlt	alte Adresse
	*@param String adresseNeu	neue Adresse
	*@throws Exception
	*/

	public void aendernAdresse(String adresseAlt, String adresseNeu)throws Exception{
		for (int i=0;i<adressen.size();i++){
			if (adresseAlt==adressen.elementAt(i)){
				adressen.insertElementAt(adresseNeu,i);
				return;
			}	
		}
		throw new Exception();
	}
}
