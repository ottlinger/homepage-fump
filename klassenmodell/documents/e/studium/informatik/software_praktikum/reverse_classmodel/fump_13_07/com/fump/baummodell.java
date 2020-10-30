package com.fump;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.io.File;
import java.util.*;
/**
 * @author Markus Hindorf / Philipp Ottlinger
 * @version $Id: BaumModell.java,v 1.5 2001/06/20 13:43:12 ottlinge Exp $
 */
public class BaumModell implements TreeModel {
  // Wurzel
  protected Ordner root;

   /** Konstruktor
    * @param root-Ordner
    * @return
    * @exception
    */
  public BaumModell(Ordner root) {
    this.root = root;
  } // end of Konstruktor

   /** Wurzel zurueckgeben
    * @param
    * @return Object
    * @exception
    */
  public Object getRoot() { return this.root; }

   /** Pruefen, ob node ein Blatt ist -
    *  ist wichtig fuer Symboldarstellung
    * @param node
    * @return boolean
    * @exception
    */
  public boolean isLeaf(Object node) {
    /** Frage:
     *  sollte doch immer false sein, damit
     *  die Ordner gleiche Symbole haben ??
     *  nein - nur zum Teil -
     *  vielleicht kann man ein defaultsymbol laden
     *  derzeit erscheint
     */
	try { return (root.getElemente().size()==0); 	}
	catch(Exception e) {}
	return false;
  } // end of isLeaf

   /** liefert die anzahl der nachfolger fuer den parent-Knoten
    * @param  Object parent
    * @return int
    * @exception
    */
  public int getChildCount (Object parent) {
    Vector kinder=new Vector();
    // sollte eigentlich stimmen .... aber doppelt haelt besser
    if(parent instanceof Ordner) {
      try {
        kinder=((Ordner)parent).gibAlleUnterordner();
        return kinder.size();
      } // try
    catch(KeineUnterordnerDa e) { return 0; }  // catch
    } // instanceof-if
    return 0;
    } // end of getChildCount

   /** liefert den index-ten Nachfolger des angegebenen parent-Knotens
    * @param Object param, int index
    * @return Object
    * @exception
    */
  public Object getChild(Object parent, int index) {
    // alle Kinder aufsammeln
    Vector children=new Vector();
    try { children = ((Ordner) parent).gibAlleUnterordner(); }
    catch(KeineUnterordnerDa e) { return null; }

    // Parametercheck: ausserhalb der gueltigen arraygrenzen
    if(index>=children.size()) return null;
    // gewuenschten Wert aus Vector zurueckgeben
    return children.elementAt(index);
  } // end of getChild

   /** liefert den Index des Nachfolgers (child) im aktuellen Knoten (parent)
    * @param Object parent, Object child
    * @return int
    * @exception
    */
  public int getIndexOfChild (Object parent, Object child) {
    Vector children=new Vector();
    try { children = ((Ordner) parent).gibAlleUnterordner(); }
    catch(KeineUnterordnerDa e) { return -1; }
    // indexOf liefert -1 bei nicht-auffindbarem Element
    return children.indexOf((Ordner)child);
  } // end of getIndexOfChild

   /** haengt neues Objekt ein (bei path) und loest treeNodeChanged-Event aus
    * @param TreePath parent, Object newValue
    * @return
    * @exception
    */
  public void valueForPathChanged(TreePath path, Object newValue){}

   /** addTreeModelListener
    * @param TreeModelListener
    * @return
    * @exception
    */
  public void addTreeModelListener(TreeModelListener l){}

   /** removeTreeModelListener
    * @param TreeModelListener
    * @return
    * @exception
    */
  public void removeTreeModelListener(TreeModelListener l){}
} // end of class
