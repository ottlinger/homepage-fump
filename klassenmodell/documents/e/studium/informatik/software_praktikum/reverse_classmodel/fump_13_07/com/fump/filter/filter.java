package com.fump.filter;

import javax.mail.*;
import javax.mail.search.*;
import java.io.*;

/**
 * Eine einzelne Filterregel.
 * @author
 * @version $Id: Filter.java,v 1.3 2001/06/20 17:17:25 stollber Exp $
 */
public class Filter implements Serializable
{
	private SearchTerm filterAusdruck;
	private String name;
	private boolean aktiv;
	private FilterAktion aktion;

	/**
	*  Protected Konstruktor ohne Parameter (wird fuer Serialisierung benoetigt).
	*/
	protected Filter() {
	}
	
	/**
	*  Konstruktor mit 2 Parametern
	* @param ausdruck der SearchTerm des Filters
	* @param name Filtername
	*/
	public Filter(SearchTerm ausdruck, String name)
	{
		filterAusdruck = ausdruck;	
		this.name = name;
		aktiv  = true;
		aktion = new FilterAktion();
	   }

	/**
	*  Konstruktor mit 4 Parametern
	* @param ausdruck der SearchTerm des Filters
	* @param name Filtername
	* @param status Aktivierungsstatus des Filters
	* @param aktion definierte Filterauktion 
	*/
	public Filter(SearchTerm ausdruck, String name, boolean status, FilterAktion aktion)
	{
		filterAusdruck = ausdruck;	
		this.name = name;
		aktiv  = status;
		this.aktion = aktion;
	   }

	   /**
	    * @return void
	  */
	public void aktivieren()
	  {
		aktiv = true;
	   }

	   /**
	    * @return void
	    */
	   public void deaktivieren()
	   {
		aktiv = false;
	   }

	   /**
	    * @return boolean Status
	    */
	 public boolean istAktiv()
	   {
		return aktiv;
	   }

	   /**
	    * Filterausführung - überschreibt die macht-Methode von SearchTerm
	    * @return boolean - 1, wenn Filter zuschnappt
	    */
	 public boolean pruefeBitte(Message msg)   {
		  return filterAusdruck.match(msg);
	   }
	
	/**
	* @return Filtername ausgeben
	*/
	public String gibName()
	{
		return name;
	}

	/**
	* @return void Filternamen setzen
	*/
	public void setzeName(String in)
	{
		name = in;
	}

	/**
	* @return SearchTerm SearchTerm des Filters
	*/
	public SearchTerm gibFilterAusdruck()
	{
		return filterAusdruck;
	}

	/**
	* @return void den SearchTerm setzen
	*/
	public  void setzeFilterAusdruck(SearchTerm in)
	{
		 filterAusdruck = in;
	}
		
	/**
	* @return FilterAktion definierte Filteraktion
	*/
	public FilterAktion gibFilterAktion()
	{
		return aktion;
	}

	/**
	* @return Filteraktion setzen
	*/
	public  void setzeFilterAktion(FilterAktion aktion)
	{
		 this.aktion = aktion;
	}

}
