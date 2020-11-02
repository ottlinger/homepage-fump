package com.fump;

import javax.swing.table.AbstractTableModel;
import javax.swing.tree.*;
import javax.swing.*;
import javax.mail.*;
import java.util.*;

/**
 * @author Markus Hindorf / Philipp Ottlinger
 * @version $Id: TabellenModell.java,v 1.9 2001/07/11 19:08:00 ottlinge Exp $
 */
class TabellenModell extends AbstractTableModel {

// tabellenzeilen
public String[] columnNames=
    {"Betreff","Absender","Datum","Gelesen"};
//public Object[][] data;


      /** Hilfsfunktion: gibt Anzahl der Spalten zurück
         *  @param
         *  @return int
         *  @exception
         */
        public int getColumnCount() { return columnNames.length; }

      /** Hilfsfunktion: gibt Anzahl der Reihen zurück
         *  @param
         *  @return int
         *  @exception
         */
        public int getRowCount() {
			try {
	        // aktuellen Pfad rausfinden
	        TreePath pfad=OrdnerOberflaeche.gibAktPfad();
	        // Vector auf Mailobjekte gewinnen
	        Vector mailVec=new Vector();
	        Object temp=OrdnerOberflaeche.gibAktuelleWurzelAlsObjekt().getPfadAlsVector(pfad);
	        if(temp instanceof Ordner){
    	       mailVec= ((Ordner)temp).gibAlleMails();
	           mailVec.trimToSize();
    	     } // if
	        int anzahl=mailVec.size();
			return anzahl;
			} // end of try

			catch(Exception ee) { }
			return 0;		
		} // end of getRowCount

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
	      // aus aktuellem Ordner Sachen lesen ....
	      try {
	        // aktuellen Pfad rausfinden
    	    TreePath pfad=OrdnerOberflaeche.gibAktPfad();
	        // Vector auf Mailobjekte gewinnen
	        Vector mailVec=new Vector();
	        Object temp=OrdnerOberflaeche.gibAktuelleWurzelAlsObjekt().getPfadAlsVector(pfad);
	        if(temp instanceof Ordner){
	           mailVec= ((Ordner)temp).gibAlleMails();
	           mailVec.trimToSize();
	         } // if

    	    int anzahl=mailVec.size();
			switch(col) {
				case 0: return  ((Mail)mailVec.elementAt(row)).getSubject(); 
				case 1: return  ((Mail)mailVec.elementAt(row)).getFrom();
				case 2: return  ((Mail)mailVec.elementAt(row)).getSentDate();
				case 3: return  new Boolean(((Mail)mailVec.elementAt(row)).getStatus());
				} // end of switch
	      } // end of try

      catch(JTreeLaeuftNichtRichtig jlnr) {
      System.out.println("TabellenModell -getValueAt "+jlnr);
      } // end of catch

      catch(KeineMailsDa kmd) {
      System.out.println("TabellenModell -getValueAt "+kmd);
      } // end of catch

      catch(MessagingException mee) {
      System.out.println("TabellenModell -getValueAt "+mee);
      } // end of catch

      catch(UngueltigerPfad up) {
      System.out.println("TabellenModell -getValueAt "+up);
      } // end of catch
		return null;
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
   *  @param aktuell selektierte Zeile aus Tabelle
   *  @return Mail
   *  @exception KeineMarkierung
   */
  public static Mail aktuelleMail(int selektierteZeile) throws KeineMarkierung {
      try {
        // aktuellen Pfad rausfinden
        TreePath pfad=(OrdnerOberflaeche).gibAktPfad();
        // Iterator auf Mailobjekte gewinnen
        Vector mailVec=new Vector();
        Object temp=((OrdnerOberflaeche).gibAktuelleWurzelAlsObjekt()).getPfadAlsVector(pfad);
        if(temp instanceof Ordner){
         mailVec= ((Ordner)temp).gibAlleMails();
         mailVec.trimToSize();
        } // if

        // vom Mailiterator, daß aktuelleSelektion.te auswählen
        return (Mail)(mailVec.elementAt(selektierteZeile));
      } // end of try

    // wenn die Exceptions kamen, war irgendwas faul!
      catch(JTreeLaeuftNichtRichtig jlnr) {
      throw new KeineMarkierung();
      } // end of catch

      catch(KeineMailsDa kmd) {
      throw new KeineMarkierung();
      } // end of catch

      catch(UngueltigerPfad up) {
      throw new KeineMarkierung();
      } // end of catch
  } // end of aktuelleMail
} // end of TabellenModell
