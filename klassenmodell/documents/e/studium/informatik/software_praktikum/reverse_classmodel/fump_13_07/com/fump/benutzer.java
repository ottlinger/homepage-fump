package com.fump;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.fump.bearbeiten.*;
import com.fump.pkonto.*;
import com.fump.hilfe.*;
import com.fump.filter.*;

/**
 * @author B. Hentrich, J. K. Olesen
 * @version $Id: Benutzer.java,v 1.16 2001/07/11 18:59:51 olesen Exp $
 */
/**
*Stellt Funktionen zur Kommunikation zwischen den einzelnen Komponenten von FUMP her.
*/
public class Benutzer implements NeueMailGeschriebenListener 
{
	private String name;
 	private String path;
	private Adressbuch theAdressbuch;
//	private OrdnerOberflaeche rootOrdner;
	private FilterKontainer theFilterContainer;
	private KontoContainer theKontoContainer;
   
/**
*Constructor
*ruft Constructoren von Adressbuch, KontoContainer, FilterContainer, Ordneroberflaeche auf.
*@param name der Benutzername.
*@param path Speicherpfad für Benutzerdateien.
*@param existiert true=Benutzer hat bereits Benutzerdateien, false sonst.
*/
	public Benutzer(String name, String path, boolean existiert)
 	{
		this.name = name;
		this.path = path;
//		rootOrdner = new OrdnerOberflaeche(name, path, existiert);
		theKontoContainer = new KontoContainer(name, path, existiert);
		theAdressbuch = new Adressbuch(name, path, !existiert);
		theFilterContainer = new FilterKontainer(name, path,existiert);
	}
/**
*Gibt den Benutzernamen zurück.
*@return String den Benutzernamen.
*/
   	public String getName()
   	{
   		return name;
   	}

/**
*Gibt den Speicherpfad des Benutzers zurück.
*@return String der Speicherpfad.
*/   	
   	public String getPath()
   	{
   		return path;
   	}

/**
*Gibt den KontoContainer des Benutzers zurück.
*@return KontoContainer der KontoContainer.
*/   	   	
   	public KontoContainer gibKontoKontainer()
   	{
		return theKontoContainer;
   	}

/**
*Gibt das Adressbuch des Benutzers zurück.
*@return Adressbuch das Adressbuch.
*/   	   	
   	public Adressbuch gibAdressbuch()
   	{
		return theAdressbuch;
   	}

	public void setzeAdressbuch(Adressbuch adr)
	{
		theAdressbuch = adr;
	}

/**
*Gibt den rootOrdner des Benutzers zurück.
*@return OrdnerOberflaeche der RootOrdner.
*/   	   	   	
//   	public OrdnerOberflaeche getRootOrdner()
//	{
//  		return rootOrdner;
//	}

/**
*Gibt den FilterKontainer des Benutzers zurück.
*@return FilterKontainer der FilterKontainer.
*/   	   	
	public FilterKontainer gibFilterKontainer()
  	{
    		return theFilterContainer;
   	}

//????	   
//  	private void ändernDanke()
//   	{
//   	}

/**
*Senden aller geschriebenen Mails.
*/   	   	  
	public void alleKontenSenden()
   	{
   	}

/**
*Mails von allen Konten abrufen.
*/   	   	  
	private void alleKontonAbrufen()
   	{
   	}
   

//Sachen der neueMail geschrieben Leute

  	public void neueMail()
  	{
		NeueMail n = new NeueMail(this);
		n.addNeueMailGeschriebenListener(this);
   	}

   	public void geschriebeneMailAbholen(NeueMailGeschriebenEvent e)
  	{
		String empfaenger= e.getEmpfaengerTo();
		String subject= e.getBetreff();
	 	String mailText= e.getMailText();


		((Konto)theKontoContainer.konten.elementAt(0)).mailSenden(empfaenger,subject,mailText);

	}

////	noch nicht ausklammern, kann sein dass das wieder wegmuß
//	public void antwortMailSchreiben(WillAntwortMailSchreibenEvent e)
//	{
//		NeueMail(e.getAbsender(),e.getEmpfaengerTo(),e.getEmpfaengerCC(),getMailText());
//	}
} 
