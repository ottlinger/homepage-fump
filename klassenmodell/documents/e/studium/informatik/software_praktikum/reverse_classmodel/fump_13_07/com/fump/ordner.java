package com.fump;

import javax.mail.*;
import java.util.*;
import javax.swing.tree.*;
import java.io.*;
import java.io.Serializable.*;

/**
 * @author    Markus Hindorf / Philipp Ottlinger
 * @version   $Id: Ordner.java,v 1.28 2001/07/11 18:30:07 ottlinge Exp $
 */

public class Ordner implements Knoten, java.io.Serializable {
/** Attribute
 * Name des aktuellen Ordners
 * private String name;
 *
 * Vorgaenger des aktuellen Ordners
 * private Ordner vorgaenger;
 *
 * beinhaltet die Ordner-Objekte
 * waehrend des Abstiegs im Baum
 * private Vector pfad;
 *
 * Unterelemente, die ein Vektor von Knoten sind
 * private Vector elemente;
 *
 * Pfad und Dateiname für Serialisierung
 * private String speicherDatei;
 *
 * Benutzer, dem dieser Ordner gehört
 * private String benutzer;
 */

public String name;
public Ordner vorgaenger;
private Vector pfad;
public Vector elemente;
//private static String speicherDatei;
private /*static*/ File speicherDatei;
private String benutzer;


   /** Konstruktor1 - erstellt neuen Ordner mit name unter pfad
	* @param: String name, String besitzer,Vector pfad, Ordner vorgaenger
	* @return void
	* @exception
	*/
	public Ordner(String name, String besitzer,Vector pfad, Ordner vorgaenger) {
		this.name=name;
        this.benutzer=besitzer;
		this.elemente=new Vector();
		// einzelne Objekte des Pfades vom Vorgaenger in unseren Pfad uebernehmen
		this.pfad=new Vector();
		Iterator it = pfad.iterator();
		while (it.hasNext()) {
			this.pfad.add(it.next());
		}
		// Pfad um diesen Ordner erweitern
		this.pfad.add(this);
		this.vorgaenger=vorgaenger;
                this.vorgaenger.elemente.trimToSize();
                this.pfad.trimToSize();
	} // end of Ordner

   /** Konstruktor2 - erstellt leeren Wurzelordner mit den 4 standardordnern
        * Das sind in Wurzel:
        * 1.Eingang
        * 2.Ausgang
        * 3.Muell
        * 4.Gesendet
        * 5.Suchergebnisse
	* @param:
	* @return void
	* @exception
	*/
        public Ordner() {
          this.name="Wurzel";
          this.elemente=new Vector();
          Vector temp=new Vector();
          temp.add(this);
          try {
	          // Standardordner anlegen
	          this.unterordnerAnlegen("Eingang",temp);
	          this.unterordnerAnlegen("Ausgang",temp);
	          this.unterordnerAnlegen("Muell",temp);
	          this.unterordnerAnlegen("Gesendet",temp);
	          this.unterordnerAnlegen("Suchergebnisse",temp);
          } // end of try
          catch(Exception e) { /* hier DARF keine Kollision sein, da Tree leer */ }
          // sich selbst dem Pfad hinzufuegen
          this.pfad=new Vector();
          this.pfad.add(this);
          this.vorgaenger=null;
        } // end of Ordner-konstr2

   /** Konstruktor3 - gibt zum uebergebenen Benutzername den serialisierten Ordner
	* zurueck
	* @param: String benutzername, String serialisierteDatei
	* @return void
	* @exception IOException, wenn Datei nicht existiert
	*/
        public Ordner(String benutzername,File serialisierteDatei) throws IOException {
        this.setBesitzer(benutzername);
		this.setSicherungsdatei(serialisierteDatei);

        // einfach aufs Geradewohl laden
        try {
        FileInputStream quelle= new FileInputStream(serialisierteDatei);
        ObjectInputStream ostream= new ObjectInputStream(quelle);
        Ordner dummy= (Ordner) ostream.readObject();
        } // end of try

        catch(FileNotFoundException fnfe) {
        System.out.println("Ordner-Konstruktor3 - Param: ("+benutzername+"/"+serialisierteDatei+"\n"+fnfe);
        throw new IOException();
        } // end of catch
        catch(StreamCorruptedException sce) {
        System.out.println("Ordner-Konstruktor3 - Param: ("+benutzername+"/"+serialisierteDatei+"\n"+sce);
        throw new IOException();
        } // end of catch
        catch(IOException ioe) {
        System.out.println("Ordner-Konstruktor3 - Param: ("+benutzername+"/"+serialisierteDatei+"\n"+ioe);
        throw new IOException();
        } // end of catch
        catch(ClassNotFoundException cnfe) {
        System.out.println("Ordner-Konstruktor3 - Param: ("+benutzername+"/"+serialisierteDatei+"\n"+cnfe);
        throw new IOException();
        } // end of catch
        System.out.println("successfully deserialized user \""+this.getBesitzer()+"\"'s data ...");

        } // end of Ordner-konstr3

	/** Name des aktuellen Ordners zurueckgeben
	* @param
	* @return String
	* @exception
	*/
        public String getName() {
          return this.name;
        } // end of getName

	/** Hilfsfunktion, die prüft, ob der angegebene Name
         *  Sonderzeichen enthält, die ungültig sind -
         *  d.h. SPACE und Komma - wegen StringTokenizer,
         *  der nach ", " parst
	* @param String zuPruefenderName
	* @return void
	* @exception UngueltigerName
	*/
        public static void pruefeName(String zuPruefenderName) throws UngueltigerName {
          char temp;
          for(int i=0;i<zuPruefenderName.length();i++) {
            temp=zuPruefenderName.charAt(i);
            if(temp==' ' || temp==',') throw new UngueltigerName();
          } // for
        } // end of pruefeName

	/** aktuellen Ordner in neuerName umbenennen mit Fehlerpruefung
	* @param String neuerName
	* @return void
	* @exception  ObjektIstGeschuetzt, DoppelterName, UngueltigerName
	*/
        public void umbenennen(String neuerName) throws ObjektIstGeschuetzt, DoppelterName,UngueltigerName {
          objektGeschuetztPruefung(this.getName());
          pruefeName(neuerName);
          // pruefen, ob Name schon in gleicher Ebene vorhanden
          Iterator vorgaengerElementeIt = vorgaenger.elemente.iterator();
          while (vorgaengerElementeIt.hasNext()) {
            Object temp = vorgaengerElementeIt.next();
            if (temp instanceof Ordner) {
              if (neuerName.equals(((Ordner)temp).name)) throw new DoppelterName();
            } // if instanceof
          } // while
          // Name aendern
          this.name = neuerName;
	} // end of umbenennen

	/** Prueft, anhand des Ordnernames, ob Ordner uU schreibgeschuetzt ist
        * @param
	* @return void
	* @exception ObjektIstGeschuetzt
	*/
	private void objektGeschuetztPruefung(String nname) throws ObjektIstGeschuetzt {
		// nur die Wurzelebene soll schreibgeschuetzt sein

			if (nname.equals("Wurzel") && vorgaenger==null) throw new ObjektIstGeschuetzt();
			if (vorgaenger!=null) {
				if (nname.equals("Muell") && vorgaenger.vorgaenger==null) throw new ObjektIstGeschuetzt();
				if (nname.equals("Eingang") && vorgaenger.vorgaenger==null) throw new ObjektIstGeschuetzt();
				if (nname.equals("Ausgang") && vorgaenger.vorgaenger==null) throw new ObjektIstGeschuetzt();
				if (nname.equals("Gesendet") && vorgaenger.vorgaenger==null) throw new ObjektIstGeschuetzt();
				if (nname.equals("Suchergebnisse") && vorgaenger.vorgaenger==null) throw new ObjektIstGeschuetzt();
			}
		return;
	} // end of objektGeschuetztPruefung

      /** im aktuellen Ordner wird ein Unterordner mit dem uebergebenen
	* Namen angelegt
    * @param String name, Vector pfad
	* @return void
	* @exception  ObjektIstGeschuetzt, DoppelterName, UngueltigerName
	*/
	public void unterordnerAnlegen(String name, Vector pfad) throws DoppelterName,ObjektIstGeschuetzt,UngueltigerName {
          pruefeName(name);
          // namenskollisionen auf gleicher Ebene prüfen
          // pruefen, ob Name schon in gleicher Ebene vorhanden
          Iterator vorgaengerElementeIt = getElemente().iterator();
          while (vorgaengerElementeIt.hasNext()) {
            Object temp = vorgaengerElementeIt.next();
            if (temp instanceof Ordner) {
              if (name.equals(((Ordner)temp).name)) throw new DoppelterName();
            } // if instanceof
          } // while

              // Erfolg - Ordner anlegen
		this.elemente.add(new Ordner(name,this.getBesitzer(),pfad,this));
        this.elemente.trimToSize();
	} // end of unterordnerAnlegen


      /** aktuellen Ordner loeschen
        * @param
	* @return void
	* @exception ObjektIstGeschuetzt
	*/
	public void loeschen() throws ObjektIstGeschuetzt {
		this.objektGeschuetztPruefung(this.getName());
		// aus Ueberordner aktuellen Ordner loeschen
		this.vorgaenger.elemente.removeElement(this);
	} // end of loeschen

      /** Verschiebt den aktuellen Ordner in den uebergebenen Pfad
	* @param Vector neuerPfad
	* @return void
	* @exception  UngueltigerPfad, DoppelterName
	*/
	public void verschieben(Vector neuerPfad) throws UngueltigerPfad, DoppelterName {
		// SUCHEN des Zielordners
		Ordner temp=gibPassendenOrdner(neuerPfad);
                // pruefen auf doppelten Namen
                Iterator tempIt = temp.elemente.iterator();
                while (tempIt.hasNext()) {
                  Object temp2 = tempIt.next();
                  if (temp2 instanceof Ordner) {
                    if (this.name.equals(((Ordner)temp2).name)) throw new DoppelterName();
                  } // if
                } // while
        	// VERSCHIEBEVORGANG
		// uns selbst beim Vorgaenger entfernen
		vorgaenger.elemente.remove(this);
        // neuen Vorgänger zuweisen und einhaengen
		this.vorgaenger=temp;
		this.vorgaenger.elemente.add(this);
		// neuen Pfad setzen
		this.pfad=neuerPfad;
		this.pfad.add(this);
	} // end of verschieben

   /** Hilfsfunktion: entfernt zuLoeschendeMail aus diesem Ordner
    * TODOLIST: eventuell nicht public, da sonst jeder Mail loeschen kann ???
    * @param Mail zuLoeschendeMail
    * @return
    * @exception
    */
    public void loescheMail(Mail zuLoeschendeMail) {
      this.elemente.removeElement(zuLoeschendeMail);
    } // end of loescheMail

   /** Hilfsfunktion: entfernt alle Mails aus diesem Ordner
    * @param 
    * @return
    * @exception 
    */
    public void loescheAlleMails()  {
	    Iterator MailIt=this.elemente.iterator();
		while(MailIt.hasNext()) {
			Object temp=MailIt.next();
			if(temp instanceof Mail) this.loescheMail((Mail)temp);
		} // while 		
    } // end of loescheAlleMails

   /** Hilfsfunktion: fügt dem aktuellen Ordner
    *  das übergebene Mailobjekt ohne Fehlerprüfung hinzu
    * @param Mail neuesElement
    * @return
    * @exception
    */
    public void addElement(Mail neuesElement) {
      this.elemente.add(neuesElement);
    } // end of addElement

   /** Hilfsfunktion: gibt aktuellen Inhalt als Vector zurück
    * @param
    * @return Vector
    * @exception
    */
    public Vector getElemente() {
      return this.elemente;
    } // end of getElemente

   /** Hilfsfunktion: gibt aktuellen Pfad als Vector zurück
    * @param
    * @return Vector
    * @exception
    */
    public Vector getPfad() {
    return this.pfad;
    } // end of getPfad

   /** Hilfsfunktion: gibt Pfad und Namen der Quelldatei zur Serialisierung zurueck
    * @param
    * @return File
    * @exception
    */
    public File getSicherungsdatei() {
    return this.speicherDatei;
    } // end of getSicherungsdatei

   /** Hilfsfunktion: setzt Name und Pfad der Quelldatei zur Serialisierung zurueck
    * @param String
    * @return 
    * @exception
    */
    public void setSicherungsdatei(File ns) {
    this.speicherDatei=ns;
    } // end of setSicherungsdatei

   /** Hilfsfunktion: gibt Besitzer/Username dieses Objekts zurueck
    * @param
    * @return String
    * @exception
    */
    public String getBesitzer() {
    return this.benutzer;
    } // end of getPfad

   /** Hilfsfunktion: setzt Besitzer/Username dieses Objekts
    * @param String nuser
    * @return 
    * @exception
    */
    public void setBesitzer(String nuser) {
    this.benutzer=nuser;
    } // end of setBesitzer

   /** Hilfsfunktion: serialisert aktuelle Ordnerstruktur -
    *  damit alles gespeichert wird, sollte diese
    *  Prozedur auf dem WURZEL-Ordner aufgerufen werden
    *  -> zZt zu speichernder Ordner als Parameter
    * @param Ordner ord
    * @return
    * @exception IOException
    */
    public static void externSpeichern(Ordner ord) throws IOException {
	    FileOutputStream ausgabe= new FileOutputStream(ord.getSicherungsdatei());
	    ObjectOutputStream ostream= new ObjectOutputStream(ausgabe);
	    ostream.writeObject(ord);
	    ostream.flush();
	    ostream.close();
    } // end of externSpeichern

   /** Hilfsfunktion: Vergleiche Zielpfad mit aktuellem Pfad und pruefe auf Fehler
    * @param Vector neuerPfad
    * @return Ordner - Zielpfadordner
    * @exception UngueltigerPfad
    */
	private Ordner gibPassendenOrdner(Vector neuerPfad) throws UngueltigerPfad {
		Ordner temp = (Ordner) this.pfad.firstElement(); // beim "Ursprungsordner" beginnen
		boolean treffer;
		Knoten aktKnoten;
		Iterator itPfad = this.pfad.iterator();

		while(itPfad.hasNext()) {
			treffer=false;
            aktKnoten = (Knoten)itPfad.next();
			Iterator itElemente = temp.elemente.iterator();
			while (itElemente.hasNext()) {
				Object pottSau = itElemente.next();
                                if (pottSau instanceof Ordner) {
                                  temp = (Ordner)pottSau;
				  if (aktKnoten.equals(temp.name)) {
                                    treffer=true;
                                    break;
                                  }
				}
			}
			if (!treffer) throw new UngueltigerPfad();
		}

		return temp;
	} // end of gibPassendenOrdner

   /** wichtig fuer GUI - gibt alle Mails des aktuellen Ordners
    * @param
    * @return Vector (von Message-Objekten)
    * @exception KeineMailsDa - aktueller Ordner enthaelt keine Mails
    */
	public Vector gibAlleMails() throws KeineMailsDa {
		if(this.elemente.capacity()==1) throw new KeineMailsDa();
		Vector temp = null;
		Object tempkn;

		// elemente traversieren mit Iterator
		Iterator it1= this.elemente.iterator();

		while(it1.hasNext()) {
			tempkn = it1.next();
			if(tempkn instanceof Mail) temp.add((Mail)tempkn);
		}
		return temp;
	} // end of gibAlleMails

   /** wichtig fuer GUI - gibt alle Unterordner des aktuellen Ordners
    * @param
    * @return Vector (von Ordner-Objekten)
    * @exception KeineUnterordnerDa
    */
	public Vector gibAlleUnterordner() throws KeineUnterordnerDa {
        this.elemente.trimToSize();
	if(this.elemente.size()==0) throw new KeineUnterordnerDa();

	// elemente traversieren mit Iterator
        Vector temp = new Vector();
        Object tempkn=null;

        Iterator printIt=this.elemente.iterator();
        while(printIt.hasNext()) {
          tempkn=printIt.next();
          if(tempkn instanceof Ordner) temp.add(((Ordner)tempkn));
        } // while

        // trimmen und zurueckgeben
        temp.trimToSize();
        return temp;
	} // end of gibAlleUnterordner

   /** Ausgabe des Ordnernamen -
    *  wichtig fuer JTree
    * @param
    * @return String
    * @exception
    */
    public String toString() {
      return this.getName();
    } // end of toString

   /** Ausgabe der Baumstruktur auf Konsole fuer Testzwecke geeignet
    *  gibt nur eine "flache Ausgabe des Wurzelordners" - keine Komplettansicht
    * @param
    * @return void
    * @exception void
    */
    public void print() {
      Object temp=null;
      System.out.println("Ordnername="+this.getName());
      Iterator printIt=this.elemente.iterator();

      while(printIt.hasNext()) {
        temp=printIt.next();
        if(temp instanceof Ordner) System.out.println("-- "+((Ordner)temp).getName());
      } // while
    } // end of print


   /** Tiefe Kopie des aktuellen Ordners im Zielpfad erstellen -
   	* nicht implementiert nur drin wegen Interface
    * @param
    * @return void
    * @exception UngueltigerPfad, DoppelterName
    */
	public void kopieren(Vector neuerPfad) throws UngueltigerPfad, DoppelterName {
/*
		// SUCHEN des Zielordners
		Ordner zielOrdner=gibPassendenOrdner(neuerPfad);
                // pruefen auf doppelten Namen
                Iterator tempIt = zielOrdner.elemente.iterator();
                while (tempIt.hasNext()) {
                  Object temp2 = tempIt.next();
                  if (temp2 instanceof Ordner) {
                    if (this.name.equals(((Ordner)temp2).name)) throw new DoppelterName();
                  } // if
                } // while
        	
/*RICHTIG SO???
//WIE TIEFE KOPIE ERZEUGEN???
	      	// KOPIERVORGANG
		// tiefe Kopie vom Objekt erzeugen
		neuesObjekt = //.....

                // neuen Vorgänger zuweisen und einhaengen
		neuesObjekt.vorgaenger=zielOrdner;
		neuesObjekt.vorgaenger.elemente.add(neuesObjekt);
		// neuen Pfad setzen
		neuesObjekt.pfad=neuerPfad;
		neuesObjekt.pfad.add(neuesObjekt);
*/
	} // end of kopieren
	

   /**
    * @param Exportieren des Ordners an einer Stelle
	* noch nicht implementiert, eigentlich sollte hier nur
	* serialisert werden <code>externSpeichern</code> also -
	* als Parameter könnte eine Datei als Ziel übergeben werden
    * @return void
    * @exception
    */
	public void exportieren() { 
	} // end of exportieren


   /** Hilfsfunktion: gibt uebergebenen TreePath als Vector der
  * richtigen Objektreferenzen zurueck, damit man
  * damit arbeiten kann und Zugriff auf die richtigen Ordner/Mailinhalte
  * hat und nicht nur den Namen als String
    * @param TreePath aktPfad
    * @return Vector (von Ordner- Objekten)
    * @exception   UngueltigerPfad
    */
    public Vector getPfadAlsVector(TreePath aktPfad) throws UngueltigerPfad {
	// Fehlerpruefung - Null ?
	if(aktPfad==null) throw new UngueltigerPfad();

	// Hilfsvars
	String token=null;
	Ordner tempOrdner=(Ordner) this.pfad.firstElement();
	Object aktElem=null;
        // aus dem aktPfad-String die Klammern am Anfang und Ende entfernen
        String aktPfadTemp = aktPfad.toString();
        aktPfadTemp = aktPfadTemp.substring(1,aktPfadTemp.length()-1);
	// zerlegen des Pfades mittel StringTokenizer
	// Trennzeichen ist ", "
	StringTokenizer s = new StringTokenizer(aktPfadTemp,", ");
        token=s.nextToken();
        if(!((tempOrdner.getName()).equals(token))) throw new UngueltigerPfad();
	while(s.hasMoreTokens()) {
		token=s.nextToken();
		Iterator elemIt= tempOrdner.elemente.iterator();
			while(elemIt.hasNext()) {
				aktElem= (Ordner) elemIt.next();
				if(aktElem instanceof Ordner) {
					if( ((Ordner)aktElem).getName().equals(token)) {
						tempOrdner=(Ordner) aktElem;
						break;
					}
				} // if
			} // while
                // wurde nichts gefunden, ist der Pfad ungueltig
	if(!(tempOrdner.equals(aktElem))) throw new UngueltigerPfad();
	} // while
	// der letzte Ordner beinhaltet das Element als Pfad, was
	// den gesuchten Objektreferenzen entspricht
	return tempOrdner.getPfad();
    } // end of getPfadAlsVector


   /**	Hilfsmethode, die ein Objekt zurueckgibt, auf das der uebergebene
    *	TreePath zeigt
    * @param TreePath 
    * @return Ordner-Objekt
    * @exception   UngueltigerPfad
    */
	public Ordner getOrdnerObject(TreePath pfad) throws UngueltigerPfad {
		return ((Ordner)(getPfadAlsVector(pfad).lastElement()));
	} // end of getOrdnerObject


   /** <code>Mail neueMail</code> wird in EINGANG geschmissen -
    *  wenn Ordner nicht root -> Exception
    * @param  Mail neueMail
    * @return void
    * @exception UngueltigerPfad 
    */
    public void addInEingang (Mail neueMail) throws UngueltigerPfad {
	addInFolderGenerisch(neueMail,"Eingang",true);
    } // end of addInEingang

   /** <code>Mail neueMail</code> wird in EINGANG geschmissen -
    *  wenn Ordner nicht root -> Exception
    * @param  Mail neueMail
    * @return void
    * @exception UngueltigerPfad 
    */
    public void addInAusgang (Mail neueMail) throws UngueltigerPfad {
	addInFolderGenerisch(neueMail,"Ausgang",true);
    } // end of addInAusgang

   /** <code>Mail neueMail</code> wird in MUELL geschmissen -
    *  wenn Ordner nicht root -> Exception
    * @param  Mail neueMail
    * @return void
    * @exception UngueltigerPfad 
    */
    public void addInMuell (Mail neueMail) throws UngueltigerPfad {
	addInFolderGenerisch(neueMail,"Muell",true);
    } // end of addInMuell

   /** <code>Mail neueMail</code> wird in SUCHERGEBNISSE geschmissen -
    *  wenn Ordner nicht root -> Exception
    * @param  Mail neueMail
    * @return void
    * @exception UngueltigerPfad 
    */
    public void addInSuchergebnisse (Mail neueMail) throws UngueltigerPfad {
	addInFolderGenerisch(neueMail,"Suchergebnisse",true);
    } // end of addInSuchergebnisse
	
   /** <code>Mail neueMail</code> wird in <code>String zielname</code> geschmissen -
    * wenn Name unbekannt, dann Ungueltiger Pfad /
	* wenn <code>istWurzel</code> gesetzt ist, wird geprüft, ob es die Wurzel ist
    * @param  Mail neueMail, String zielname, boolean istWurzel
    * @return void
    * @exception UngueltigerPfad 
    */
    private void addInFolderGenerisch (Mail neueMail,String zielname,boolean istWurzel) throws UngueltigerPfad {
	// Fehler, wenn nicht auf root-Ordner aufgerufen wird
	if ((istWurzel) && (this.vorgaenger!=null)) throw new UngueltigerPfad();
	try {
		// alle Unterordner durchgehen und den Namen vergleichen
		Iterator elemIt = this.gibAlleUnterordner().iterator();
		while (elemIt.hasNext()) {
			Ordner aktOrd = (Ordner) elemIt.next();
			if (aktOrd.getName().equals(zielname)) {
				aktOrd.addElement(neueMail);
				return;
			} // if
		} // while
	} // try
	catch (KeineUnterordnerDa kud){throw new UngueltigerPfad();}
	// damit auch alle anderen Sachen abgefangen werden
	catch (Exception ee) { throw new UngueltigerPfad(); }
	return;
    } // end of addInFolderGenerisch
} // end of class Ordner
