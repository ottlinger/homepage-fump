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
import java.awt.event.*;
import java.io.File;
import java.util.*;
import java.awt.*;
/**
 * @author Markus Hindorf / Philipp Ottlinger
 * @version $Id: InhaltOberflaeche.java,v 1.1 2001/06/20 15:25:41 ottlinge Exp $
 */
public class InhaltOberflaeche extends JPanel {
/**
 * Klassenvariablen:
 * static JPopupMenu popup= new JPopupMenu();
 * POPUPMenues, das die Befehle enthaelt
 * static Mail aktNachricht = null;
 * reale Nachricht im Fenster
 */
static JPopupMenu popup= new JPopupMenu();
static Mail aktNachricht = null;
   /** InhaltOberflaeche-main:
    *  erzeugt das Panel mit dem Mailinhalt und zeigt
    *  es zur Demonstration an, wenn Param ungültig ist,
    *  passiert nichts
    * @param
    * @return
    * @exception
    */
  public static void main(String[] args) {
    // Panel erstellen und anzeigen
    JFrame frame = new JFrame("FUMP Inhaltsdarstellung in JPanel- Stand: 2001-06-10");
    frame.getContentPane().add(zeigeInhalt(null));
    frame.setSize(500,200);
    frame.setLocation(120,120);
    frame.setVisible(true);
   } // end of main

  /**
   * zeigeInhalt()
   * @param Mail (extends MailAPIMessage)
   * @return JComponent (eigentlich JScrollpane)
   * @exception
   * statische Methode, die ein JComponent bzw. JScrollpane
   * zurueckgibt; dieses muß dann einfach auf JScrollpane gecastet
   * werden und kann eingesetzt werden
   */
  public static JComponent zeigeInhalt(Mail anzuzeigendeMail) {
  // Fehlerprüfung -> leere Komponenten zurückgeben
  if(anzuzeigendeMail==null) return new JScrollPane();
  aktNachricht=anzuzeigendeMail;

  /**
   * MailOberflaeche.zeigeMails()
   * hier muß noch die Größe des Fensters rein, damit
   * die Scrollbalken an der richtigen Stelle kommen
   * derzeit:
   *   tabelle.setPreferredScrollableViewportSize(new Dimension(500, 200));
   */
//  tabelle.setPreferredScrollableViewportSize(new Dimension(500, 200));
  JScrollPane scrollPane = new JScrollPane();

    // Popup-Menue hinzufuegen
    JMenuItem del= new JMenuItem("loeschen");
    popup.add(del);
    popup.addSeparator();
    JMenuItem oeff=new JMenuItem("in eigenem Fenster öffnen");
    popup.add(oeff);
    popup.addSeparator();
    JMenuItem speich=new JMenuItem("speichern");
    popup.add(speich);

    // Funktionen des Popupmenues herstellen
    del.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(null,"Hier sollte die Mail geloescht werden.","Hinweis", JOptionPane.INFORMATION_MESSAGE);
      } // end of proc
    } // end of class
    );

    oeff.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(null,"Jetzt sollte der Editor in einem neuen Fenster starten.","Hinweis", JOptionPane.INFORMATION_MESSAGE);
      } // end of proc
    } // end of class
    );

    speich.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(null,"Hier sollte die Mail speicherbar sein.","Hinweis", JOptionPane.INFORMATION_MESSAGE);
      } // end of proc
    } // end of class
    );

    // MouseListener erstellen
    MouseListener popupLi = new MouseAdapter() {
	    public void mousePressed(MouseEvent e) {
              showPopup(e);
	    } // end of mousePressed

	    public void mouseReleased(MouseEvent e) {
	      showPopup(e);
	    } // end of mouseReleased

	    private void showPopup(MouseEvent e) {
              // PopupTrigger und JTable-Spalte muss selektiert sein
	      if( e.isPopupTrigger())
	        popup.show(e.getComponent(), e.getX(), e.getY());
		} // end of showPopup
	}; // end of MouseAdapter

    // MenueMausbehandlung hinzufuegen
    scrollPane.addMouseListener(popupLi);

// fertiges Objekt zurueckgeben
return(scrollPane);
  } // end of test
}// end of class

