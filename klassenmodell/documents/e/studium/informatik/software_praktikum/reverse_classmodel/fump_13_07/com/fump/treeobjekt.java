package com.fump;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class TreeObjekt {
// Attribute
private Ordner derOrdner;
private String name;
private Icon icon;

// Konstruktoren
  TreeObjekt(String n) {
  this.name = n;
  this.icon = new ImageIcon();
  } // end of Konstruktor 1

  TreeObjekt(String n, String bild) {
  this.name = n;
System.out.println("n="+n+"\tBild="+bild);

  this.icon = new ImageIcon(getClass().getResource(bild));
  } // end of Konstruktor 2

  public void setWurzelOrdner(Ordner wurzel) {
    this.derOrdner=wurzel;
  } // end of setWurzelOrdner

  public Ordner getOrdner() {
    return this.derOrdner;
  } // end of getOrdner

  public Icon getIcon() {
    return this.icon;
  } // end of getIcon

  public String getName() {
    return this.toString();
  } // end of getName

  public String toString() {
    return ((Ordner)derOrdner).getName();
   } // end of toString
} // end of class