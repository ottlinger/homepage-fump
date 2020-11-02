package com.fump;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import java.awt.Point;
import java.io.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JOptionPane;
import java.awt.Container;
import javax.swing.event.TableModelEvent;

public class AbuchTableModel extends AbstractTableModel{

	Adressbuch buch;
	public AbuchTableModel(Adressbuch book){buch = book;}

	public int getColumnCount() { return 3; }

	public String getColumnName(int col){
		switch (col){
			case 0: return "Name";
			case 1: return "Alias";
			case 2: return "Adresse(n)";
		}
		return new String("");
	}
	public int getRowCount() { return buch.eintraege.size();}

	/*public void setValueAt(Object obj, int row, int col){
		switch(col){
			case 0:((Eintrag)buch.eintraege.elementAt(row)).setName((String)obj);break;
			case 1:((Eintrag)buch.eintraege.elementAt(row)).setAlias((String)obj);break;
			case 2:{String adresse = (String)obj;
					if(adresse!=null){
						StringTokenizer adr = new StringTokenizer(adresse);
						while(adr.hasMoreTokens()){((Eintrag)buch.eintraege.elementAt(row)).addAdresse(adr.nextToken());}
					}
					break;
				}
		}//switch
		table.repaint();
	}*/

	public Object getValueAt(int row, int col){
		if (row>buch.eintraege.size()) return new String("?");
		Eintrag e = (Eintrag) buch.eintraege.elementAt(row);
		switch (col) {
			case 0: return e.getName();
			case 1: return e.getAlias();
			case 2: return e.getAdressenString();
		}
		return new String("");
	}
	//public boolean isCellEditable(int x, int y){return true;}

}