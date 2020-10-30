package com.fump;

import javax.mail.*;
import javax.mail.Flags;
import javax.mail.search.*;
import javax.mail.internet.InternetAddress;
import java.util.*;
import java.io.*;
import java.util.Date;
import javax.activation.*;
import javax.swing.tree.*;
/**
 * @author Markus Hindorf / Philipp Ottlinger
 * @version $Id: Mail.java,v 1.13 2001/07/11 16:15:03 ottlinge Exp $
 */
public class Mail extends Message implements Knoten {
// Interface - einfach so reingemacht
// Funktionen, die schon sauber implementiert sind,
// finden sich weiter hinten im Code ;-) PO 20010617
  public Enumeration getNonMatchingHeaders(String[] p0) throws MessagingException {return null; }
  public void setFrom() throws MessagingException {}
  public void setFrom(Address p0) throws MessagingException {}
  public void addFrom(Address[] p0) throws MessagingException {}
  public Address[] getRecipients(RecipientType p0) throws MessagingException { return null;}
  public Address[] getAllRecipients() throws MessagingException { return null; }
  public void setRecipients(RecipientType p0, Address[] p1) throws MessagingException { }
  public void setRecipient(RecipientType p0, Address p1) throws MessagingException { }
  public void addRecipients(RecipientType p0, Address[] p1) throws MessagingException {}
  public void addRecipient(RecipientType p0, Address p1) throws MessagingException { }
  public Address[] getReplyTo() throws MessagingException { return null; }
  public void setReplyTo(Address[] p0) throws MessagingException { }
  public void setSubject(String p0) throws MessagingException {}
  public Date getSentDate() throws MessagingException { return null; }
  public void setSentDate(Date p0) throws MessagingException { }
  public Date getReceivedDate() throws MessagingException { return null; }
  public Flags getFlags() throws MessagingException { return null; }
  public boolean isSet(Flags p0) throws MessagingException { return true; }
  public void setFlags(Flags p0, boolean p1) throws MessagingException {}
  public void setFlag(Flags p0, boolean p1) throws MessagingException { }
  public int getMessageNumber() { return -1; }
  protected void setMessageNumber(int p0) { }
  public Folder getFolder() { return null;  }
  public boolean isExpunged() { return false;  }
  protected void setExpunged(Boolean p0) { }
  public Message reply(boolean p0) throws MessagingException { return null; }
  public void saveChanges() throws MessagingException { }
  public boolean match(SearchTerm p0) throws MessagingException {return false;  }
// elemente.capacity(); ?
  public int getSize() throws MessagingException { return -1;  }
// ??
  public int getLineCount() throws MessagingException { return -1;  }
  public String getContentType() throws MessagingException { return null;  }
// isinstanceof MIME ??
  public boolean isMimeType(String p0) throws MessagingException {return false;  }
  public String getDisposition() throws MessagingException { return null;  }
  public void setDisposition(String p0) throws MessagingException { }
  public String getDescription() throws MessagingException { return null;  }
  public void setDescription(String p0) throws MessagingException { }
  public String getFileName() throws MessagingException { return null;  }
  public void setFileName(String p0) throws MessagingException { }
  public InputStream getInputStream() throws IOException, MessagingException { return null;  }
  public DataHandler getDataHandler() throws MessagingException { return null;  }
  public Object getContent() throws IOException, MessagingException { return null; }
  public void setDataHandler(DataHandler p0) throws MessagingException { }
  public void setContent(Object p0, String p1) throws MessagingException { }
  public void setText(String p0) throws MessagingException { }
  public void setContent(Multipart p0) throws MessagingException { }
  public void writeTo(OutputStream p0) throws IOException, MessagingException { }
  public String[] getHeader(String p0) throws MessagingException { return null; }
  public void setHeader(String p0, String p1) throws MessagingException { }
  public void addHeader(String p0, String p1) throws MessagingException { }
  public void removeHeader(String p0) throws MessagingException { }
  public Enumeration getAllHeaders() throws MessagingException {return null; }
  public Enumeration getMatchingHeaders(String[] p0) throws MessagingException {return null; }


/**
 *  Klassenvariablen
 */
  // Mailattribute für unsere interen JTable-Darstellung
  private String name=null;
  private boolean status=false;

  private String betreff=null;
  private Address[] absender=null;
  private Address[] empfaenger=null;
  private Date datum=null;
  private Vector alleHeader=null;
  private String[] derHeader=null;
  private Object inhalt=null;

  // Vector von Ordner-Objekten
  private Vector pfad=null;
  // Überordner
  private Ordner vorgaenger=null;
/**
 * Konstruktor
 * erzeugt anhand des übergebenen Message-Objekts ein
 * FUMP-Mail-Objekt
 *
 * @param Message originalNachricht
 *
 * evtl. in Zukunft noch irgendwas mit dem Pfad und vorgaenger
 * und so - ist aber noch nicht komplett durchdacht ;-)
 *
 */
  public Mail(Message originalNachricht) throws MessagingException,IOException {

    this.name = this.toString();
    this.betreff = originalNachricht.getSubject();
    this.absender = originalNachricht.getFrom();
    this.empfaenger = originalNachricht.getAllRecipients();
    // wenn kein ReceivedDate vorhanden, SentDate nehmen
    if (originalNachricht.getReceivedDate()!=null)
      this.datum = originalNachricht.getReceivedDate();
    else
      this.datum = originalNachricht.getSentDate();
    // naja, auf mich wirkts irgendwie doof ;-)
    this.alleHeader = (Vector) originalNachricht.getAllHeaders();
    //** macht so noch keinen Sinn!!! Haben die Doku nicht kapiert! */
    this.derHeader = originalNachricht.getHeader(null);
    this.inhalt = originalNachricht.getContent();

  } // end of Mail-Konstruktor

/** Hilfsfunktion, die anzeigt, ob Nachricht gelesen ist
 * getStatus() - gelesen (j/n)
 * @param
 * @return boolean
 * @exception
 */
  public boolean getStatus()  { return this.status; }

/** Hilfsfunktion, die festsetzt, ob Nachricht gelesen wurde
 * setStatus() - gelesen (j/n)
 * @param boolean
 * @return
 * @exception
 */
  public void setStatus(boolean neuerWert)  { this.status=neuerWert; }

  /**
   * aktuelle Mail loeschen
   * @param
   * @return void
   * @exception ObjectIstGeschuetzt
   * (im Prinzip ungenutzt, da keine geschuetzten Mails existieren)
   */
  public void loeschen() throws ObjektIstGeschuetzt {
  this.vorgaenger.loescheMail(this);
  }// end of loeschen


 /** Verschiebt den aktuellen Ordner in den uebergebenen Pfad
  * @param Vector neuerPfad
  * @return void
  * @exception  UngueltigerPfad, DoppelterName
  * (DoppelterName wird in der Realitaet nie geworfen, da der Name eindeutig ist)
  */
  public void verschieben(Vector neuerPfad) throws UngueltigerPfad, DoppelterName {
    // SUCHEN des Zielordners
    Ordner temp=gibPassendenOrdner(neuerPfad);
    // VERSCHIEBEVORGANG
    // uns selbst beim Vorgaenger entfernen
    vorgaenger.loescheMail(this);
    // neuen Vorgänger zuweisen und einhaengen
    this.vorgaenger=temp;
    this.vorgaenger.addElement(this);
    // neuen Pfad setzen
    this.pfad=neuerPfad;
    this.pfad.add(this);
  }// end of verschieben

   /** aktuelles Subjekt zurückgeben
    * @param
    * @return String
    * @exception MessagingException, wenn null
    */
  public String getSubject() throws MessagingException {
  if(this.betreff==null) throw new MessagingException();
  return this.betreff;
  } // end of getSubject

   /** aktuellen Absender zurückgeben
    * @param
    * @return String
    * @exception MessagingException, wenn null
    */
  public Address[] getFrom() throws MessagingException {
  if(this.absender==null) throw new MessagingException();
  return this.absender;
  } // end of getFrom

   /** Hilfsfunktion: Vergleiche Zielpfad mit aktuellem Pfad und pruefe auf Fehler
    * @param Vector neuer Pfad
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

			Iterator itElemente = temp.getElemente().iterator();

			while (itElemente.hasNext()) {
				Object pottSau = itElemente.next();
                                if (pottSau instanceof Ordner) {
                                  temp = (Ordner)pottSau;
				  if (aktKnoten.equals(temp.getName())) {
                                    treffer=true;
                                    break;
                                  } // equals-if
				} // pottsau-if
			} // while it
			if (!treffer) throw new UngueltigerPfad();
		} // itPfad-while
		return temp;
	} // end of gibPassendenOrdner

 /** kopieren einer Mail in einen uebergebenen Pfad
  * @param Vector neuerPfad
  * @return void
  * @exception UngueltigerPfad, DoppelterName
  */
  public void kopieren(Vector neuerPfad) throws UngueltigerPfad, DoppelterName {
	// SUCHEN des Zielordners
	Ordner zielOrdner=gibPassendenOrdner(neuerPfad);

      	// KOPIERVORGANG
	// tiefe Kopie vom Objekt erzeugen
	try{
		Mail neuesObjekt = (Mail)this.clone();
		// neuen Vorgänger zuweisen und einhaengen
		neuesObjekt.vorgaenger=zielOrdner;
		neuesObjekt.vorgaenger.elemente.add(neuesObjekt);
		// neuen Pfad setzen
		neuesObjekt.pfad=neuerPfad;
		neuesObjekt.pfad.add(neuesObjekt);
	}
	catch (CloneNotSupportedException hfgv){}
  } // end of kopieren


   /**
    * @param
    * @return void
    * @exception
    */
	public void exportieren() {
	}

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
        Ordner tempOrdner = (Ordner) this.pfad.firstElement(); // beim "Ursprungsordner" beginnen
	Object aktElem=null;
	// zerlegen des Pfades mittel StringTokenizer
	// Trennzeichen ist Leerzeichen per Default
	StringTokenizer s = new StringTokenizer(aktPfad.toString());
	while(s.hasMoreTokens()) {
		token=s.nextToken();
		Iterator elemIt= tempOrdner.getElemente().iterator();
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
		if(!tempOrdner.equals(aktElem)) throw new UngueltigerPfad();
	} // while
	// der letzte Ordner beinhaltet das Element als Pfad, was
	// den gesuchten Objektreferenzen entspricht
    return tempOrdner.getPfad();
    } // end of getPfadAlsVector
} // end of class