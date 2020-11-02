package com.fump;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.JMenuItem.*;
import java.awt.event.*;
import java.io.*;
import java.io.File;
import java.util.*;
import javax.mail.*;

/**
 * @author Markus Hindorf / Philipp Ottlinger
 * @version $Id: OrdnerOberflaeche.java,v 1.31 2001/07/11 19:08:00 ottlinge Exp $
 */
public class OrdnerOberflaeche extends JPanel {
private static JPopupMenu popup; //= new JPopupMenu();
private static JTree tree; // = new JTree();
// wichtig fuer Befehle auf Ordnern
private static int nummer=-1;
private static TreePath aktPfad=null;
private static TreePath zielPfad=null; // wichtig für Kopieren/Verschieben etc.
private static String name=null;
// wurzelordner laden und auf Konsole ausgeben
private static Ordner root; 
private static BaumModell model; // Baummodell, wichtig für De/Serialisierung

// Infos, die ermöglichen, daß man OrdnerOberfläche für
// einen Benutzer aufrufen kann
private static String besitzer;
private static File besitzerSpeicherDatei;

   /** OrdnerOberflaeche-main:
    *  erzeugt ein DUMMY-Panel (User:nobody) mit der Baumdarstellung als JTree
    * @param
    * @return
    * @exception
    */
  public static void main(String[] args) {
    // Panel erstellen und anzeigen
    JFrame frame = new JFrame("FUMP Ordnerdarstellung Test - Stand: 2001-07-10");
    frame.getContentPane().add(zeigeOrdner("nobody"));
    frame.setSize(400,250);
    frame.setLocation(120,120);
    frame.setVisible(true);
   } // end of main

   /** gibAktZielPfad
    *  gibt den aktuellen Pfad zurueck -
    *  dient zur Kommunikation mit KopierenOberflaeche
	* Läuft KopierenOberflaeche nicht oder stimmt das Ergebnis
	* nicht, wird KeineMarkierung geworfen
    * @param
    * @return TreePath
    * @exception KeineMarkierung
    */
  public static TreePath gibAktZielPfad() throws KeineMarkierung {
  if(zielPfad==null) throw new KeineMarkierung();
  return zielPfad;
  } // end of gibAktZielPfad

   /** setzeAktZielPfad
    *  setzt den aktuellen Zielpfad auf pfad
    *  dient zur Kommunikation mit KopierenOberflaeche
    * @param TreePath pfad
    * @return void
    * @exception JTreeLaeuftNichtRichtig
    */
  public static void setzeAktZielPfad(TreePath pfad) throws UngueltigerPfad {
  if(pfad==null) throw new UngueltigerPfad();
  zielPfad=pfad;
  } // end of setzeAktZielPfad

   /** gibAktPfad
    *  gibt den aktuellen Pfad zurueck
    * @param
    * @return TreePath
    * @exception JTreeLaeuftNichtRichtig
    */
  public static TreePath gibAktPfad() throws JTreeLaeuftNichtRichtig {
  if(aktPfad==null) throw new JTreeLaeuftNichtRichtig();
  return aktPfad;
  } // end of gibAktPfad

   /** setzeAktPfad
    *  setzt den aktuellen Pfad auf pfad
    * @param TreePath pfad
    * @return void
    * @exception JTreeLaeuftNichtRichtig
    */
  public static void setzeAktPfad(TreePath pfad) throws JTreeLaeuftNichtRichtig {
  if(aktPfad==null) throw new JTreeLaeuftNichtRichtig();
  aktPfad=pfad;
  tree.setSelectionPath(pfad);
  tree.updateUI();
  } // end of setzeAktPfad

   /** gibAktuelleWurzelAlsObjekt
    *  gibt den aktuellen Pfad zurueck -
    *  dient zur Kommunikation mit MailOberflaeche
    * @param
    * @return Ordner
    * @exception JTreeLaeuftNichtRichtig
    */
  public static Ordner gibAktuelleWurzelAlsObjekt() throws JTreeLaeuftNichtRichtig {
  if(root==null) throw new JTreeLaeuftNichtRichtig();
  return root;
  } // end of gibAktuelleWurzelAlsObjekt


   /** fuegt ein neues Message-Objekt in die Ordnerstruktur
    *  an die im uebergebenen Pfad angegebene Stelle ein
    *  wird von Mails-Abrufen-Leuten aufgerufen
    *  um die Mail an die richtige Stelle in Ordnerstruktur zu platzieren
    * @param Message neuesMessageObject, TreePath pfad
    * @return
    * @exception UngueltigerPfad, IOExceptionMessagingException
    */
  public void addMail(Message neuesMessageObject, TreePath pfad) throws MessagingException,IOException,UngueltigerPfad{
  	Mail tempMail = new Mail(neuesMessageObject);
	(root.getOrdnerObject(pfad)).addElement(tempMail);
  } // end of addMail

   /** OrdnerOberflaeche-zeigeOrdner
    *  gibt statisch ein ScrollPanel mit dem aktuellen Ordner
    *  zurueck als Jtree mit Menuestruktur 
	* Aus dem übergebenen Benutzernamen user wird versucht,
	* die Ordnerstruktur mit Mails zu deserialisieren
    * @param String user
    * @return JComponent (eigentlich JScrollPane)
    * @exception
    */
  public static JComponent zeigeOrdner(String user) {
		// User nobody dient dazu eine Dummyoberfläche 
		// zu erzeugen, in der nur DefaultOrdner sind ;-))
		tree=new JTree();
		popup= new JPopupMenu();
		// Dummy ??
		if(user.equals("nobody")) {
			try {
	        // root ersetzen und
	        root= new Ordner();
	        // einen neuen JTree in der GUI erzeugen
	        tree.removeAll();
	        model = new BaumModell(root);
	        tree.setModel(model);
	        tree.updateUI();
			} catch(Exception e) {}
		} 
		else {
			// versuch, die übergebene Datei zu deserialisieren
			// wenn Bullshit, dann wird default-Ordner geöffnet und in 
			// Datei serialisiert
			// Daten für Oberflaeche merken
			besitzer= user;
			besitzerSpeicherDatei=new File(BenutzerContainer.getBenutzer().getPath(),user+".dat");
/** Beim Speichern tritt folgendes Phänomen auf:
*Beim Deserialsieren klappt alles und serialisieren auch - 
*beendet man aber FUMP und startet neu, wird
*die vorhanden Datei nicht gefunden ... komisch
*Philipp 2001-07-10 */

			try {
	        FileInputStream quelle= new FileInputStream(besitzerSpeicherDatei);
	        ObjectInputStream ostream= new ObjectInputStream(quelle);
	        // root ersetzen und
	        root= (Ordner) ostream.readObject();
			root.setSicherungsdatei(besitzerSpeicherDatei);
			root.setBesitzer(besitzer);
	        // einen neuen JTree in der GUI erzeugen
	        tree.removeAll();
	        model = new BaumModell(root);
	        tree.setModel(model);
	        tree.updateUI();
			} // end of try

			// Wenn Benutzer nicht da .... oder Fehler -> einfach einen neuen anlegen
			catch(Exception ee) {
//				System.out.println("Oberflaeche:Benutzer "+user+" wird erstellt ...\n("+ee+")");
				System.out.println("Oberflaeche:Benutzer "+user+" wird erstellt ...");
				// leerer Ordner
				root=  new Ordner();
				root.setSicherungsdatei(besitzerSpeicherDatei);
				root.setBesitzer(besitzer);
		        model = new BaumModell(root);
		        tree.setModel(model);
		        tree.updateUI();
				// Grundeinstellung speichern
				try { root.externSpeichern(root); }
				catch(Exception e1) { 
						System.out.println("Die Benutzerdaten von "+user+" konnten nicht\n");
						System.out.println("gespeichert werden -\nFehlermeldung : ("+e1+")");
				} // end of catch					
			} // end of catch
		} // end nobody-if-else

    // Panel erstellen und anzeigen
    final JScrollPane scrollpane = new JScrollPane(tree);
    // Popup-Menue hinzufuegen
    final JMenuItem neu= new JMenuItem("Unterordner anlegen");
    popup.add(neu);
    popup.addSeparator();
    final JMenuItem umbenn= new JMenuItem("Ordner umbenennen");
    popup.add(umbenn);
    popup.addSeparator();
    final JMenuItem loeschen=new JMenuItem("Ordner loeschen");
    popup.add(loeschen);
    final JMenuItem versch=new JMenuItem("Ordner verschieben");
    popup.add(versch);
// noch nicht implementiert
//    final JMenuItem kopier=new JMenuItem("Ordner kopieren");
//    popup.add(kopier);
    popup.addSeparator();
    final JMenuItem speich=new JMenuItem("Ordnerstruktur auf Disk speichern");
    popup.add(speich);
    final JMenuItem lad=new JMenuItem("Ordnerstruktur von Disk laden");
    popup.add(lad);
    popup.addSeparator();
    final JMenuItem hlp=new JMenuItem("Hilfe");
    popup.add(hlp);

    // Funktionen des Popupmenues herstellen
    loeschen.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        int result=JOptionPane.showConfirmDialog(null, "Soll der Ordner \""+name+"\"\ngeloescht werden ?","Loeschbestaetigung", JOptionPane.YES_NO_OPTION);
        // Nur wenn JA geklickt wurde loeschen
        if(result==JOptionPane.YES_OPTION) {
          // aktuellen Pfad als Vektor holen
          try {
          Vector temp= root.getPfadAlsVector(aktPfad);
          Ordner aktOrdner = ((Ordner)(temp.lastElement()));
          // Ordner loeschen
          aktOrdner.loeschen();
		  // Tree zumachen und Element entfernen
		  tree.updateUI();
          } // end of try
          catch(ObjektIstGeschuetzt ue) {
          JOptionPane.showMessageDialog(null, "\""+name+"\" kann nicht geloescht werden.", "Geschuetzter Ordner", JOptionPane.INFORMATION_MESSAGE);
          } // end of catch
	catch(UngueltigerPfad upf) {
        JOptionPane.showMessageDialog(null, "Interner Fehler in Ordnerdatenstruktur !! Schwere Scheisse!", "PROBLEM", JOptionPane.ERROR_MESSAGE);
        } // end of catch
        } // resultif
			  tree.addNotify();
      } // end of proc
    } // end of class
    );

    neu.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
      // neuen Unterordner erstellen
      String inputValue = JOptionPane.showInputDialog(null,"Bitte den Unterordnernamen \nunter \""+name+"\" eingeben ","Unterordner erstellen",JOptionPane.QUESTION_MESSAGE);
      // wenn nichts eingegeben -> CANCEL, also nichts tun
      if(inputValue==null || inputValue.length()==0) return;
      // aktuellen Pfad als Vektor holen
        try {
        Vector temp= root.getPfadAlsVector(aktPfad);
        Ordner aktOrdner = ((Ordner)(temp.lastElement()));
        aktOrdner.unterordnerAnlegen(inputValue,aktOrdner.getPfadAlsVector(aktPfad));
        // Tree aufmachen
        tree.expandPath(aktPfad);
        tree.updateUI();
        } // end of try
        catch(UngueltigerName un) {
      	JOptionPane.showMessageDialog(null, "Der angegebene Name (\""+inputValue+"\")\nenthält ungültige Sonderzeichen.", "Ungueltiger Name", JOptionPane.INFORMATION_MESSAGE);
        } // end of catch un
        catch(DoppelterName dn) {
      	JOptionPane.showMessageDialog(null, "Ein Ordner \""+inputValue+"\" existiert bereits.", "Ordner anlegen", JOptionPane.INFORMATION_MESSAGE);
        } // end of catch dn
        catch(ObjektIstGeschuetzt oig) {
      JOptionPane.showMessageDialog(null, "Ein Ordner \""+inputValue+"\" existiert bereits.", "Ordner anlegen", JOptionPane.INFORMATION_MESSAGE);
        } // end of catch dn
	catch(UngueltigerPfad upf) {
        JOptionPane.showMessageDialog(null, "Interner Fehler in Ordnerdatenstruktur !! Schwere Scheisse!", "PROBLEM", JOptionPane.ERROR_MESSAGE);
        } // end of catch
			  tree.addNotify();
      } // end of proc
    } // end of class
    );

   versch.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
      String inputValue = JOptionPane.showInputDialog(null,"Ordner \""+name+"\" verschieben - \nBitte den Zielpfad fuer den Ordner eingeben ","Ordner verschieben",JOptionPane.WARNING_MESSAGE);
      // wenn nichts eingegeben -> CANCEL, also nichts tun
      if(inputValue==null || inputValue.length()==0) return;
			// KopierenOberflaeche aufrufen .... 
/**
* verschieben geht noch nicht !!!!
* philipp 2001-07-10
*/
			new KopierenOberflaeche(tree,"Verschieben");

			  tree.addNotify();
      } // end of proc
    } // end of class
    );

   umbenn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
      String inputValue = JOptionPane.showInputDialog(null,"Ordner \""+name+"\" umbennen - \nBitte den neuen Namen fuer den Ordner angeben ","Ordner umbenennen",JOptionPane.WARNING_MESSAGE);
      // wenn nichts eingegeben -> CANCEL, also nichts tun
      if(inputValue==null || inputValue.length()==0) return;
      try {
        Vector temp= root.getPfadAlsVector(aktPfad);
        Ordner aktOrdner = ((Ordner)(temp.lastElement()));
        aktOrdner.umbenennen(inputValue);
	    tree.updateUI();
      } // end of try
      catch(UngueltigerName un) {
      JOptionPane.showMessageDialog(null, "Der angegebene Name (\""+inputValue+"\")\nenthält ungültige Sonderzeichen.", "Ungueltiger Name", JOptionPane.INFORMATION_MESSAGE);
      } // end of catch un
      catch(ObjektIstGeschuetzt ue) {
      JOptionPane.showMessageDialog(null, "\""+name+"\" kann nicht umbenannt werden.", "Geschuetzter Ordner", JOptionPane.INFORMATION_MESSAGE);
      } // end of catch
      catch(DoppelterName dpn) {
      JOptionPane.showMessageDialog(null, "\""+name+"\" kann nicht umbenannt werden.", "Geschuetzter Ordner", JOptionPane.INFORMATION_MESSAGE);
      } // end of catch
      catch(UngueltigerPfad upf) {
      JOptionPane.showMessageDialog(null, "Interner Fehler in Ordnerdatenstruktur !! Schwere Scheisse!", "PROBLEM", JOptionPane.ERROR_MESSAGE);
      } // end of catch
			  tree.addNotify();
      } // end of proc
    } // end of class
    );

/** Kopieren noch nicht implementiert!!
* Philipp 2001-07-10
*/
/*
   kopier.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
      String inputValue = JOptionPane.showInputDialog(null,"Ordner \""+name+"\" kopieren - \nBitte den neuen Namen fuer den Ordner angeben ","Ordner kopieren",JOptionPane.PLAIN_MESSAGE);
      // wenn nichts eingegeben -> CANCEL, also nichts tun
      if(inputValue==null || inputValue.length()==0) return;
			  tree.addNotify();
      } // end of proc
    } // end of class
    );
*/
   speich.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        int result=JOptionPane.showConfirmDialog(null, "Soll die aktuelle Ordnerstruktur\ngespeichert werden ?","Ordnerstruktur speichern", JOptionPane.YES_NO_OPTION);
        // Nur wenn JA geklickt wurde loeschen
        if(result==JOptionPane.YES_OPTION) {
            // ab Wurzel alles speichern
            try { root.externSpeichern(root);
            lad.enable();
            }
            catch(IOException ioe) {
              JOptionPane.showMessageDialog(null, "Die aktuelle Ordnerstrukur konnte \nnicht serialisiert werden", "Fehler bei Serialisierung", JOptionPane.ERROR_MESSAGE);
              lad.disable();
              return;
              } // end of catch
            JOptionPane.showMessageDialog(null, "Die aktuelle Ordnerstrukur\nwurde serialisiert", "Serialisierung", JOptionPane.INFORMATION_MESSAGE);
        } // result-if
			  tree.addNotify();
      } // end of proc
    } // end of class
    );

   lad.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        int result=JOptionPane.showConfirmDialog(null, "Soll die aktuelle Ordnerstruktur\ndurch die gespeicherte ersetzt werden ?","Ordnerstruktur überschreiben", JOptionPane.YES_NO_OPTION);
        // Nur wenn JA geklickt wurde loeschen
     if(result==JOptionPane.YES_OPTION) {
        // Wurzel einlesen aus Datei
        try {
        FileInputStream quelle= new FileInputStream(root.getSicherungsdatei());
        ObjectInputStream ostream= new ObjectInputStream(quelle);
        // root ersetzen und
        root= (Ordner) ostream.readObject();
        // einen neuen JTree in der GUI erzeugen
        tree.removeAll();
        BaumModell model = new BaumModell(root);
        tree.setModel(model);
        tree.updateUI();
        } // end of try
        catch(FileNotFoundException fnfe) {
        JOptionPane.showMessageDialog(null, "Die aktuelle Ordnerstrukur konnte \nnicht serialisiert werden\n"+fnfe, "Fehler bei Serialisierung", JOptionPane.INFORMATION_MESSAGE);
        return;
        } // end of catch
        catch(StreamCorruptedException sce) {
        JOptionPane.showMessageDialog(null, "Die aktuelle Ordnerstrukur konnte \nnicht serialisiert werden\n"+sce, "Fehler bei Serialisierung", JOptionPane.INFORMATION_MESSAGE);
        return;
        } // end of catch
        catch(IOException ioe) {
        JOptionPane.showMessageDialog(null, "Die aktuelle Ordnerstrukur konnte \nnicht serialisiert werden\n"+ioe, "Fehler bei Serialisierung", JOptionPane.INFORMATION_MESSAGE);
        return;
        } // end of catch
        catch(ClassNotFoundException cnfe) {
        JOptionPane.showMessageDialog(null, "Die aktuelle Ordnerstrukur konnte \nnicht serialisiert werden\n"+cnfe, "Fehler bei Serialisierung", JOptionPane.INFORMATION_MESSAGE);
        return;
        } // end of catch
        JOptionPane.showMessageDialog(null, "Die Ordnerstrukur\nwurde erfolgreich geladen.", "Serialisierung-Erfolg", JOptionPane.INFORMATION_MESSAGE);
      } // result-if
			  tree.addNotify();
      } // end of proc
    } // end of class
    );

   hlp.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	  // FUMP-Hilfe aufrufen
		FUMP.hilfe.kontext_hilfe("Ordner");
	} // end of proc
	}); // end of class und add

    MouseListener popupLi = new MouseAdapter() {
	    public void mousePressed(MouseEvent e) {
              // Attribute speichern
              nummer = tree.getRowForLocation(e.getX(), e.getY());
              // wenn man daneben klickt, passiert nichts
              if(nummer==-1) return;
              aktPfad = tree.getPathForLocation(e.getX(), e.getY());
              name=aktPfad.getLastPathComponent().toString();
		      tree.setSelectionPath(aktPfad);
              showPopup(e);
			  tree.addNotify();
	    } // end of mousePressed

	    public void mouseReleased(MouseEvent e) {
              showPopup(e);
	    } // end of mouseReleased

	    private void showPopup(MouseEvent e) {
              // man muss am Baum sein, damit das Menue aufgeht
	      if( e.isPopupTrigger() && !(nummer==-1))
	        popup.show(e.getComponent(), e.getX(), e.getY());
              } // end of showPopup
	}; // end of MouseAdapter

	// MenueMausbehandlung hinzufuegen
    tree.addMouseListener(popupLi);

	// fertiges Objekt zurueckgeben
	return(scrollpane);
  } // end of zeigeOrdner


/** statisches Speichern des root-Ordners ermöglichen,
* damit man beim Programmbeenden die Sachen speichert :
* Stimmen Benutzername/Pfad nicht, oder ist kein <code>root</code>
* verfügbar, wird die Exception <code>JTreeLaeuftNichtRichtig</code> geworfen
* @param 
* @return
* @exception JTreeLaeuftNichtRichtig
*/
public static void speichernBeimBeenden() throws JTreeLaeuftNichtRichtig {
            // ab Wurzel alles speichern
            try { 
				root.externSpeichern(root); 
				} // end of try
        		catch(Exception ere) {
					System.out.println("Oberflaeche-speichernBeimBeenden: Die Daten konnten nicht gespeichert werden\n"+ere);
				} // end of catch
} // end of speichernBeimBeenden
}// end of class
