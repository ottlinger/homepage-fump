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
 * @version $Id: LasseKlasse.java,v 1.2 2001/07/11 14:39:55 ottlinge Exp $
 */
public class LasseKlasse extends JPanel {
/**
 * Klassenvariablen:
 * static JPopupMenu popup= new JPopupMenu();
 * POPUPMenues, das die Befehle enthaelt
 * static JTable tabelle= new JTable();
 * eigentliche Tabelle, die die Mails enthaelt
 */
public static JTable tabelle= new JTable();

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
	LasseKlasse lk1= new LasseKlasse();
    // Panel erstellen und anzeigen
    JFrame frame = new JFrame("LASSEKlasse in JTable- Stand: 2001-07-10");
    frame.getContentPane().add(lk1);
    frame.setSize(500,200);
    frame.setLocation(120,120);
    frame.setVisible(true);
   } // end of main

  /**
   * Konstruktor
   * @param Ordner
   * @return JScrollpane
   * @exception
   * statische Methode, die ein JComponent bzw. JScrollpane
   * zurueckgibt; dieses muﬂ dann einfach auf JScrollpane gecastet
   * werden und kann eingesetzt werden
   */
  public LasseKlasse() {
  // JTable erzeugen mit entsprechendem Modell
  FilterTabellenModell tabmodel = new FilterTabellenModell(new Ordner());

  tabelle = new JTable(tabmodel);
  /**
   * hier muﬂ noch die Grˆﬂe des Fensters rein, damit
   * die Scrollbalken an der richtigen Stelle kommen
   * derzeit:
   *   tabelle.setPreferredScrollableViewportSize(new Dimension(500, 200));
   */
  tabelle.setPreferredScrollableViewportSize(new Dimension(500, 200));
  JScrollPane scrollPane = new JScrollPane(tabelle);
  this.add(scrollPane);
  
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
	    } // end of mousePressed

	    public void mouseReleased(MouseEvent e) {
//	      showPopup(e);
	    } // end of mouseReleased

	    private void showPopup(MouseEvent e) {
		} // end of showPopup
	}; // end of MouseAdapter

    // MenueMausbehandlung hinzufuegen
    tabelle.addMouseListener(popupLi);
} // end of LasseKlasse-Konstruktor
} // end of class
