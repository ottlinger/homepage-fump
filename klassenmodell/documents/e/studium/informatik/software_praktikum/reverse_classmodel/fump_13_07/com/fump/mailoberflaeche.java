package com.fump;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.JScrollPane;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import javax.swing.JMenuItem.*;
import javax.swing.tree.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;
import java.awt.*;
import javax.mail.*;
import javax.mail.Message;
import com.fump.bearbeiten.*; // Mail-Anzeige-Panel

/**
 * @author Markus Hindorf / Philipp Ottlinger
 * @version $Id: MailOberflaeche.java,v 1.10 2001/07/11 18:40:55 ottlinge Exp $
 */
public class MailOberflaeche extends JPanel {
/**
 * Klassenvariablen:
 * static JPopupMenu popup= new JPopupMenu();
 * POPUPMenues, das die Befehle enthaelt
 * static JTable tabelle= new JTable();
 * eigentliche Tabelle, die die Mails enthaelt
 */
static JPopupMenu popup= new JPopupMenu();
static JTable tabelle= new JTable();

/**
 * wichtig fuer Befehle auf JTable:
 * static int nummer=-1;
 * beinhaltet die Zeilennummer
 * static String name=null;
 * Name beinhaltet derzeit nur die Betreffzeile der Nachricht
 */
private static int nummer=-1;
private static String name=null;
private static Mail aktMail=null;

   /** MailOberflaeche-main:
    *  erzeugt das Panel mit der Mailtabelle und zeigt
    *  es zur Demonstration an
    * @param
    * @return
    * @exception
    */
  public static void main(String[] args) {
    // Panel erstellen und anzeigen
    JFrame frame = new JFrame("FUMP Maildarstellung in JTable- Stand: 2001-07-10");
    frame.getContentPane().add(zeigeMails());
    frame.setSize(500,200);
    frame.setLocation(120,120);
    frame.setVisible(true);
   } // end of main

  /**
   * zeigeMails()
   * @param
   * @return JComponent (eigentlich JScrollpane)
   * @exception
   * statische Methode, die ein JComponent bzw. JScrollpane
   * zurueckgibt; dieses muß dann einfach auf JScrollpane gecastet
   * werden und kann eingesetzt werden
   */
  public static JComponent zeigeMails() {
  // JTable erzeugen mit entsprechendem Modell
  TabellenModell tabmodel = new TabellenModell();

  tabelle = new JTable(tabmodel);
  /**
   * MailOberflaeche.zeigeMails()
   * hier muß noch die Größe des Fensters rein, damit
   * die Scrollbalken an der richtigen Stelle kommen
   * derzeit:
   *   tabelle.setPreferredScrollableViewportSize(new Dimension(500, 200));
   */
  tabelle.setPreferredScrollableViewportSize(new Dimension(500, 200));
  JScrollPane scrollPane = new JScrollPane(tabelle);

    // Popup-Menue hinzufuegen
    JMenuItem antw= new JMenuItem("Mail beantworten");
    popup.add(antw);
    JMenuItem showmail= new JMenuItem("Mail in einem neuen Fenster oeffnen");
    popup.add(showmail);
    popup.addSeparator();
    JMenuItem loeschen=new JMenuItem("Mail loeschen");
    popup.add(loeschen);
    popup.addSeparator();
    JMenuItem versch=new JMenuItem("Mail verschieben");
    popup.add(versch);
    JMenuItem kopi=new JMenuItem("Mail kopieren");
    popup.add(kopi);
    JMenuItem speich=new JMenuItem("Mail speichern");
    popup.add(speich);

    // Funktionen des Popupmenues herstellen
    showmail.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
      // Mailstatus verändern
      JOptionPane.showMessageDialog(null,"Hier sollte jetzt der Editor starten.","Hinweis", JOptionPane.INFORMATION_MESSAGE);
      // Status der aktuellen Nachricht auf gelesen setzen
      try {
        ((TabellenModell).aktuelleMail(tabelle.getSelectedRow())).setStatus(true);
      } // end of try
      catch(Exception e1) {
        System.out.println("Fehler beim Editieren der Nachricht "+e1);
        System.out.println("Der Status konnte nicht auf gelesen gesetzt werden");
      } // end of catch
      } // end of proc
    } // end of class
    );

    antw.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
	try {
	 	new NeueMail((TabellenModell.aktuelleMail(tabelle.getSelectedRow())));
	} // end of try
	catch(Exception e) {
	} // end of catch
      } // end of proc
    } // end of class
    );

    loeschen.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JOptionPane.showConfirmDialog(null, "Soll die Mail \""+name+"\"\ngeloescht werden ?","Loeschbestaetigung", JOptionPane.YES_NO_OPTION);
      } // end of proc
    } // end of class
    );

   versch.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String inputValue = JOptionPane.showInputDialog(null,"Mail \""+name+"\" verschieben - \nBitte den Zielpfad fuer den Ordner eingeben ","Verschieben",JOptionPane.QUESTION_MESSAGE);
      if(inputValue.equals(null) || inputValue.length()==0) return;
      } // end of proc
    } // end of class
    );

   kopi.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String inputValue = JOptionPane.showInputDialog(null,"Mail \""+name+"\" kopieren - \nBitte den Zielpfad fuer den Ordner eingeben ","Kopieren",JOptionPane.QUESTION_MESSAGE);
      if(inputValue==null || inputValue.length()==0) return;
      } // end of proc
    } // end of class
    );

   speich.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JOptionPane.showConfirmDialog(null, "Soll die Mail \""+name+"\"gespeichert werden ?","Mail speichern", JOptionPane.YES_NO_OPTION);
      } // end of proc
    } // end of class
    );

    // MouseListener erstellen
    MouseListener popupLi = new MouseAdapter() {
	    public void mousePressed(MouseEvent e) {
              // Attribute speichern
              nummer = tabelle.getSelectedRow();
              /**
              * wenn keine Selektierung in Tabelle ist,
              * wird name = "" !!
              */
              if(nummer==-1) { name=new String(""); return; }
              /**
              * Anzeige des Namens :=
              * Name wird derzeit die Betreffzeile
              * in der TestTabelle - muss noch angepasst werden
              * in Abstimmung mit Methoden der Mail-Klasse
              */
              name= (String) tabelle.getValueAt(tabelle.getSelectedRow(),0);

			try {
			// VorschauPanel starten mit aktuell selektierter Mail ..
			// kommt es dabei zu einem Fehler wird die Operation einfach ignoriert
			new VorschauPanel((Message) aktuelleMail(tabelle.getSelectedRow())); 
			} // end of try
			catch(Exception aeee) {}
			  showPopup(e);
	    } // end of mousePressed

	    public void mouseReleased(MouseEvent e) {
	      showPopup(e);
	    } // end of mouseReleased

	    private void showPopup(MouseEvent e) {
              // PopupTrigger und JTable-Spalte muss selektiert sein
	      if( e.isPopupTrigger() && !(name.equals("")) )
	        popup.show(e.getComponent(), e.getX(), e.getY());
		} // end of showPopup
	}; // end of MouseAdapter

    // MenueMausbehandlung hinzufuegen
    tabelle.addMouseListener(popupLi);

// fertiges Objekt zurueckgeben
return(scrollPane);
} // end of zeigeMails

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
} // end of class
