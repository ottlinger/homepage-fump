//JBuilderRest ;-) package kopierenoberflaeche;
package com.fump;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.JMenuItem.*;
import java.awt.event.*;
import java.io.*;
import java.io.File;
import java.util.*;
import java.awt.*;

/**
 * Title:        kopierenOberflaeche
 * Description:  eine separate Oberfläche aufbauen, die ein TreePath ändert und sich dann zerstört
 * oder <code>KeineMarkierung</code> wirft, wenn nichts markiert wurde oder abbrechen betätigt wurde
 * Copyright:    Copyright (c) 2001
 * Company:      student's workout
 * @version $Id: KopierenOberflaeche.java,v 1.3 2001/07/11 19:08:00 ottlinge Exp $
 * @author PO und MH, 2001-07-11
 * @version 1.0
 */

public class KopierenOberflaeche extends JFrame {
// JButtons
private static JButton okay=new JButton("Okay");
private static JButton cancel=new JButton("Abbruch");
private static JPanel ordnerfenster=new JPanel();
private static JTree ordnerbaum;

// wichtig fuer Befehle auf Ordnern
private static int nummer=-1;
private static TreePath aktPfad=null;
private static String name=null;

// sonstiges
private static Dimension size; // BS-Größe

   /** kopierenOberflaeche-Konstruktor
    *  erzeugt das Panel mit der Baumdarstellung als JTree
    * @param JTree anzuzeigenderOrdnerbaum, String operation
    * @return
    * @exception
    */
  public KopierenOberflaeche(JTree anzuzeigenderOrdnerbaum,String operation) {
    // Komponenten hinzufügen
    super();
    // BS-Größe holen
    size=Toolkit.getDefaultToolkit().getScreenSize();
     /**
     * kopierenOberflaeche()
     * hier muß noch die Größe des Fensters rein, damit
     * die Scrollbalken an der richtigen Stelle kommen
     * derzeit:
     *   ordnerfenster.setSize(new Dimension(200, 200));
     */
    ordnerfenster.setSize(new Dimension(200, 200));
    this.getContentPane().setLayout(new GridLayout(1,1));

    // JTree einhängen
    ordnerbaum=anzuzeigenderOrdnerbaum;
    ordnerbaum.setPreferredSize(new Dimension(200,200));
    ordnerfenster.add(ordnerbaum);
    // Mausreaktionen einfügen
    MouseListener popupLi = new MouseAdapter() {
	    public void mousePressed(MouseEvent e) {
              // Attribute speichern
              nummer = ordnerbaum.getRowForLocation(e.getX(), e.getY());
              // wenn man daneben klickt, passiert nichts
              if(nummer==-1) return;
              aktPfad = ordnerbaum.getPathForLocation(e.getX(), e.getY());
              name=aktPfad.getLastPathComponent().toString();
	      ordnerbaum.setSelectionPath(aktPfad);
	    } // end of mousePressed
	}; // end of MouseAdapter
    ordnerbaum.addMouseListener(popupLi);

    // Tastenkürzel
    okay.setMnemonic('O');
    okay.addActionListener(new ActionListener () {
      public void actionPerformed(ActionEvent e) {
        if(aktPfad==null) {
          // Frage ob Beenden oder wieder markieren
          int result=JOptionPane.showConfirmDialog(null, "Sie haben nichts markiert.\nWollen sie die Operation abbrechen ?","Bestaetigung", JOptionPane.YES_NO_OPTION);
          // Nur wenn JA geklickt wurde loeschen
          if(result==JOptionPane.YES_OPTION) {
            dispose();
          } // resultif
        }
        else {
          // Kontrollfrage
          int result=JOptionPane.showConfirmDialog(null, "Sie haben den Pfad\n\""+aktPfad+"\"\n markiert. \nWollen sie die Operation dort ausführen ?","Bestaetigung", JOptionPane.YES_NO_OPTION);
          // Nur wenn JA geklickt wurde loeschen
          if(result==JOptionPane.YES_OPTION) {
 			/** Setzen des Zielpfades:
			* wurde etwas markiert, so wird das in der OrdnerOberflaeche vermerkt (Variable: <code>ZielPfad</code>)
			* dann wird das Fenster geschlossen und die OrdnerOberflaeche kann weitermachen
			* Philipp 2001-07-10
			*/
            try { OrdnerOberflaeche.setzeAktZielPfad(aktPfad); }
			// Exception wird ignoriert - dann tut er  halt nichts
			catch(Exception ee) {}
			dispose(); // fertig ;-)
          } // resultif
        }
      } // end of actionPerformed
      } // end of class
    );

    cancel.setMnemonic('A');
    cancel.setMnemonic('O');
    cancel.addActionListener(new ActionListener () {
      public void actionPerformed(ActionEvent e) {
      // Fenster schließen
      dispose();
      } // end of actionPerformed
      } // end of class
    );

     // Einfuegen ist etwas sibirisch, aber so geht das Layout wenigstens
    getContentPane().add(ordnerfenster);
    ordnerfenster.add(okay);
    ordnerfenster.add(cancel);
    ordnerfenster.setVisible(true);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    // Statusinfos
    this.setTitle("Bitte "+operation+"-Zielpfad auswählen");

    // Grösse in Abhängigkeit von BS-Größe wählen
    setSize((int)(size.getWidth()*0.95),(int)(size.getHeight()*0.85));
    setLocation((int)(size.getWidth()*0.35),(int)(size.getHeight()*0.15));

    this.pack();
    this.setVisible(true);
  } // end of Konstruktor

	/** Steuerung des Zusammenspiels zwischen Ordner und dieser Hilfsoberfläche ;-)
	* NOCH NICHT AUSGEREIFT - Philipp 2001-07-10
	* @param 
	* @return
	* @exception
	*/
	public boolean istFensterNochDa() {
	return true;
	} // end of istFensterNochDa


   /** main-
    *  Anzeige eines Dummyfensters zur Auswahl des Pfades eines normalen JTree's
    * @param
    * @return
    * @exception
    */
  public static void main(String[] args) {
    // Dummyobjekt anzeigen
    KopierenOberflaeche kopierenOberflaeche1 = new KopierenOberflaeche(new JTree(),"Verschieben");
  } // end of main
} // end of class
