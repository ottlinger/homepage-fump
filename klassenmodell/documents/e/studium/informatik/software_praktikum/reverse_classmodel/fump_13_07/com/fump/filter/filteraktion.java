package com.fump.filter;

import java.io.*;
import javax.swing.tree.*;

/**
 * Definition der Aktion, die auf gefilterte Mails angewendet wird. Das Objekt wird einer neuen Mail als Parameter übergeben. 
 * @author	Krause, Stollberg, Voss
 * @version 	$Id: FilterAktion.java,v 1.4 2001/06/27 16:21:58 stollber Exp $
 */
public class FilterAktion implements Serializable
{
	/**
	*  Flag: gefilterte Mails werden geloescht
	*/
	public static final int LOESCHEN = 0;
	/**
	*  Flag: gefilterte Mails werden verschoben
	*/
	public static final int VERSCHIEBEN = 1;	

	private int aktion;
	
	//private TreePath zielKnoten;

	/**
	*  1. Konstruktor: Aktion loeschen
	*/
	public FilterAktion() {
		aktion = LOESCHEN;
	}

	/**
	*  2. Konstruktor: Aktion verschieben
	* @param TreePath Pfad des Zeilknotens zur Ablage einer gefilterten Mail
	*/
	public FilterAktion(TreePath knoten) {
		aktion = VERSCHIEBEN;
		//zielKnoten = knoten;
	}
	
	/**
	*  Flag ausgeben
	* @return int des Flag
	*/
	public int gibAktion() {
		return aktion;	
	}

	/**
	*  Zielknoten zur Ablage ausgeben
	* @return TreePath Pfad des Zeilknotens zur Ablage einer gefilterten Mail
	* @exception IllegalAccessException 
	*/
	public TreePath gibKnoten() throws IllegalAccessException {
		if (aktion == LOESCHEN)
			throw new IllegalAccessException("Aktion ist LOESCHEN -> Zielknoten nicht definiert!");
		else
			//return zielKnoten;
			return null;
	}

	/**
	* FilterAktion setzen
	* @param aktion die Aktion eben
	* @return void
	* @exception IllegalArgumentException
	*/
	public void setzeAktion(int aktion) throws IllegalArgumentException {
		if (aktion == LOESCHEN || aktion == VERSCHIEBEN)
			this.aktion = aktion;
		else
			throw new IllegalArgumentException("Aktion nicht unterstützt.");			
	}

	/**
	* Zielknoten zum Verschieben setzen
	* @param knoten Zielpfad zum Verschieben
	* @return void
	* @exception IllegalArgumentException
	*/
	public void setzeKnoten(TreePath knoten) throws IllegalArgumentException {
		if (aktion == VERSCHIEBEN)
			//zielKnoten = knoten;
			// NOP ;)
			aktion = aktion;
		else
			throw new IllegalArgumentException("Aktion ist nicht VERSCHIEBEN -> kein Zielknoten definierbar!");
	}
}
