package com.fump;

import java.util.*;
import javax.swing.tree.*;

/**
 * @author Markus Hindorf / Philipp Ottlinger
 * @version $Id: Knoten.java,v 1.8 2001/06/20 13:43:12 ottlinge Exp $
 */
public interface Knoten {
   /** Hilfsfunktion: gibt uebergebenen TreePath als Vector der
	* richtigen Objektreferenzen zurueck, damit man
	* damit arbeiten kann und Zugriff auf die richtigen Ordner/Mailinhalte
	* hat und nicht nur den Namen als String
    * @param TreePath aktPfad
    * @return Vector (von Ordner- Objekten)
    * @exception   UngueltigerPfad
    */
    public Vector getPfadAlsVector(TreePath aktPfad) throws UngueltigerPfad;

/**
 * @return void
 * @exception
    */
	public void verschieben(Vector neuerPfad) throws UngueltigerPfad, DoppelterName;

   /**
    * @return void
    * @exception
    */
	public void kopieren(Vector neuerPfad) throws UngueltigerPfad, DoppelterName;

   /**
    * @return void
    * @exception
    */
	public void exportieren();

   /**
    * @return void
    * @exception
    */
	public void loeschen() throws ObjektIstGeschuetzt;

} // end of interface

