package com.fump.filter;

import java.util.*;
import javax.mail.*;
import javax.mail.search.*;
import java.io.*;
import com.fump.*;

/**
 *  DIese Klasse verwaltet die einzelnen definierten Filter.
 *  Als Verwaltungskonstrukt wurde eine Hashtable gewählt.
 * @author	Krause, Stollberg, Voss
 * @version 	$Id: FilterKontainer.java,v 1.15 2001/07/12 12:06:04 ottlinge Exp $
 */
 
 
public class FilterKontainer implements Serializable
{
	private Hashtable filterListe;
	private static final String DEFAULT_DATEINAME = "filter.dat";
	private String dateiName;

	/**
	  *  Instantiiert einen neuen FilterKontainer
	  */
	public FilterKontainer() {
        	filterListe = new Hashtable();
	}

	public FilterKontainer(String fileName) {
        if (!laden(fileName)) filterListe = new Hashtable();
	} 		
/**
  *  Mit User  Unterstützung
  */

	public FilterKontainer(String benutzerName, String pfad, boolean existiert) {
        if (!laden(pfad + File.separator + DEFAULT_DATEINAME)) filterListe = new Hashtable();
	} 		

/**
  * @param filterName der Filtername
  * @exception IllegalArgumentException
  * @return  Filter  - der entsprechende Filter eben
 */     
	
	    public Filter gibFilter(String filterName) throws IllegalArgumentException
           {
                if (filterListe.containsValue(filterName))
                	throw new IllegalArgumentException("Filter nicht vorhanden");
                 else
                	return (Filter) filterListe.get(filterName);
         }


	/**
	 * Überprüft Existenz eines Filters.
	 * @param filterName
	 * @return Boolean
	 */
	public boolean existiert(String filterName) {
		return (filterListe.containsKey(filterName));	
	}
	
	
           /**
            *  Erstetzt existenten Filter 
            * @param filter der neue Filter
            * @param filterName der existente Filtername
            * @exception IllegalArgumentException
            */
        public void ersetze(String filterName, Filter filter) throws IllegalArgumentException
           {
                if (! filterListe.containsKey(filterName) || (filterListe.containsKey(filter.gibName()) && filterName != filter.gibName())) 
			throw new IllegalArgumentException("Filtername kann nicht ersetzt werden");
		loeschen(filterName);
		hinzufuegen(filter);
         }


           /**
            *  Erstellung eines neuen Filters
            * @param filter der neue Filter
           * @exception IllegalArgumentException
            */
        public void hinzufuegen(Filter filter) throws IllegalArgumentException
           {
                if (filterListe.containsKey(filter.gibName() ))
                	throw new IllegalArgumentException("Filtername schon vorhanden");
                 else
                  filterListe.put(filter.gibName(), filter);
         }
           /**
            *  Löscht einen Filter
            * @param filterName der zu löschende Filter
            */
        public void loeschen(String filterName)
        {
                filterListe.remove(filterName);
        }


             /**
             *  Sammelt alle Filter in einem Iterator.
            * @return Iterator  der Iterator mit allen Filterchens
            */
        public Iterator gibAlleFilter()
           {
                return filterListe.values().iterator();	
           }

       /**
       *  Führt alle aktiven Filter über eine Message aus
       * @param msg zu evaluierende Message (zu deutsch: elektronische Nachricht)
       * @return FilterAktion wenn NULL, dann paßt kein Filter ; sonst wird entsprechende FilterAktion zurückgegeben.
       */
       public FilterAktion pruefeBitte(Message msg)
       {
                Iterator alleFilter = gibAlleFilter();
                while (alleFilter.hasNext() )
                {
                	Filter filter = (Filter)alleFilter.next();
                	if (filter.istAktiv() && filter.pruefeBitte(msg)) return filter.gibFilterAktion();
                }
                return null;
        }


	/**
 	 * Wrapper fuer <code>pruefeBitte</code>, fuehrt ermittelte <code>FilterAktion</code> jedoch selbst aus.
 	 * @param msg auszuwertende Message
	 * @throws JTreeLaeuftNichtRichtig
	 */
	public void ausfuehren(Message msg) throws JTreeLaeuftNichtRichtig {
System.out.println("**** Mail da ****");
		FilterAktion aktion = pruefeBitte(msg);

		try {
			if (aktion != null) {
				if (aktion.gibAktion() == FilterAktion.VERSCHIEBEN) {
					// in filterspezifischen Zielordner speichern
					// momentan ist dies ein Default-Ordner
System.out.println("Mail da in SUCH");					
					OrdnerOberflaeche.gibAktuelleWurzelAlsObjekt().addInSuchergebnisse(new Mail((Message)msg));
				}
				// ansonsten wird die E-Mail verworfen, d.h. in den Trash-Folder gespeichert
System.out.println("Mail da in MUELL");					
				OrdnerOberflaeche.gibAktuelleWurzelAlsObjekt().addInMuell(new Mail((Message)msg));			
			} else {
				// E-Mail normal in Eingang speichern
System.out.println("Mail da in EINGANG");					
				OrdnerOberflaeche.gibAktuelleWurzelAlsObjekt().addInEingang(new Mail((Message)msg));
			}
		} catch (Exception e) {

System.out.println("Beim Abrufen kam folgende Exception:\n"+e);


			throw new JTreeLaeuftNichtRichtig();
		}
	}
	
	
        /**
        *  Gibt Anzahl der im Kontainer enthaltenen Filter zurueck.
        * @return int Anzahl der Filter
        */
	public int gibFilterAnzahl() {
                 	return filterListe.size();
	}

        /**
         *  Speichert den kompletten Kontainer in eine Datei.
	     * @param fileName Dateiname
         * @return boolean <code>true</code> bei erfolgreicher Speicherung
         */
	public boolean speichern(String fileName) {
		try {               		
			FileOutputStream ostream = new FileOutputStream(fileName);
               		ObjectOutputStream p = new ObjectOutputStream(ostream);
      			p.writeObject(filterListe);
       			p.flush();
               		ostream.close();
		 } catch (Exception e) {
			return false;
		}
		return true;
	}
  /**
         *  Speichert den kompletten Kontainer in die default Datei.
	     *  @return boolean <code>true</code> bei erfolgreicher Speicherung
         */
	public boolean speichern() {
		if (dateiName != null)
			return speichern(dateiName);
		else
			return false;                                		
	}

        /**
         *  Laedt den kompletten Kontainer aus einer Datei.
	     * @param fileName Dateiname
         * @return boolean <code>true</code> beim erfolgreichen Laden
         */
	public boolean laden(String fileName) {
		dateiName = fileName;
		try {
	       		FileInputStream istream = new FileInputStream(fileName);
               		ObjectInputStream ip = new ObjectInputStream(istream);
       			filterListe = (Hashtable) ip.readObject();
	       		istream.close();
		} catch (Exception e) {
			return false;	
		}
		return true;
	}
	
}
