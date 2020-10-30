package com.fump;

import javax.swing.table.AbstractTableModel;
import javax.swing.tree.*;
import javax.swing.*;
import javax.mail.*;
import java.util.*;

/**
 * @author Markus Hindorf / Philipp Ottlinger
 * @version $Id: FilterTabellenModell.java,v 1.5 2001/07/11 14:39:55 ottlinge Exp $
 * <h1><b>"exclusively designed for Lasse the guy with the Fortschrittsbalken"</h1></b>
 * FilterTabellenModel - derzeit zum testen beinhaltet
 * das nur drei Dummymails - diese muessen noch
 * mit Methoden der Mailklasse in die Tabelle geladen
 * werden
 */
public class FilterTabellenModell extends AbstractTableModel {
// tabellenzeilen
public String[] columnNames=
    {"Betreff","Absender","Datum","Gelesen"};
public static Object[][] data;
private static Ordner lassesOrdner;

      // Konstruktor zum Laden der aktuellen Maildaten
      public FilterTabellenModell(Ordner lassesOrdner) {
      super();
		// Mails in Tabelle anzeigen
		try { 	
		        // Vector auf Mailobjekte gewinnen
		        Vector mailVec;
		        mailVec= lassesOrdner.gibAlleMails();
				showInhalt(mailVec); 
		} // end of try
		catch(Exception e)  { /* kann ignoriert werden - just don't do a thing */ }
      } // end of Konstruktor

      /** Hilfsfunktion: gibt Anzahl der Spalten zurück
         *  @param
         *  @return int
         *  @exception
         */
        public int getColumnCount() { 
			return columnNames.length; 
			}

      /** Hilfsfunktion: gibt Anzahl der Reihen zurück
         *  @param
         *  @return int
         *  @exception
         */
        public int getRowCount() {
			if(data==null) return 0; 
			else
			return data.length; 
		}


      /** Hilfsfunktion: gibt aktuellen Spaltennamen zurück
         *  @param int spalte
         *  @return String
         *  @exception
         */
        public String getColumnName(int col) {
        // wenn Index ausserhalb der Grenzen ist ->
        // nichts tun/ ignorieren bzw. leerer String
          if ((col<0) || (col>this.getColumnCount())) return new String("");
          return columnNames[col];
        } // end of getColumnName

      /** Hilfsfunktion: gibt String an den aktuellen Koordinaten zurück
         *  @param int reihe, int spalte
         *  @return Object
         *  @exception
         */
        public Object getValueAt(int row, int col) {
          if ((col<0) || (col>this.getColumnCount())) return new String("");
          if ((row<0) || (row>this.getRowCount())) return new String("");
          return data[row][col];
        } // end of getValueAt

      /** Hilfsfunktion: zur Anzeige der letzten Spalte mit Checkbox
         *  @param int position
         *  @return Class
         *  @exception
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

  /** Hilfsfunktion: gibt das aktuelle markierte Mailobjekt zurück,
   *  sonst KeineMarkierungException
   *  @param Mail
   *  @return 
   *  @exception 
   */
	public static void addMail(Mail abc) {
		try {
		lassesOrdner.addElement(abc);	
		showInhalt(lassesOrdner.gibAlleMails());
		} // end of try
		catch(Exception e) {
			System.out.println("Schwerer Fehler - ask Lasse for relief ;-)\n"+e);
		} // end of catch
	} // end of addMail
	

  /** Hilfsfunktion: aktualisiert die Tabelle und Ihre Mails darin
   *  @param Vector der Mailobjekte des aktuellen Ordners
   *  @return 
   *  @exception
   */
	public static void showInhalt(Vector mailVec) {
      // aus aktuellem Ordner Sachen lesen ....
			  		
      try {
        int anzahl=mailVec.size();

        // in Tabelle Platz schaffen fuer alle Mails
        // data = 4 Zeilenköpfe x #Mails
        data=new Object[anzahl][4];

        // Iterator passt hier nicht, da wir die nummern brauchen
        for(int i=0;i<anzahl;i++) {
          // aktuelle Nachricht parsen und
          // Teile in Tabelle laden
            Mail aktMail=(Mail)mailVec.elementAt(i);
            data[i][0]=aktMail.getSubject();  // Betreff
            data[i][1]=aktMail.getFrom();     // Addressen
            data[i][2]=aktMail.getSentDate(); // Sendedatum
// traditionell: checkbox-darstellung
          data[i][3]=new Boolean(aktMail.getStatus());   // Status => gelesen
            // Imagedarstellung
//            if(aktMail.getStatus())   data[i][3]=new ImageIcon("read.gif");
//            else                      data[i][3]=new ImageIcon("unread.gif");
        } // end of for
      } // end of try
      catch(MessagingException mee) {
//      System.out.println("FilterTabellenModell "+mee);
      } // end of catch

      catch(Exception ee) {
//      System.out.println("FilterTabellenModell "+ee);
      } // end of catch
	} // end of showInhalt
} // end of FilterTabellenModell
