package com.fump.filter;

import java.util.*;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.JScrollPane;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import java.awt.*;
import java.awt.event.*;

/** 
  * Abstraktes Tabellenmodell fuer FilterKontainerView
  * @author: Stollberg, Krause
  * @version: $Id: FilterKontainerTableModel.java,v 1.3 2001/07/11 14:53:02 lueck Exp $
  **/


public class FilterKontainerTableModel extends AbstractTableModel {
        
protected Object[][] filterData;
final static String[] columnNames = {"Status","Filtername"};


/**
  * Konstruktor
  * @param FilterKontainer
  **/
public FilterKontainerTableModel(FilterKontainer kontainer) {
	erfrische(kontainer);
}	


/**
  * @param FilterKontainer
  *
  **/
public void erfrische(FilterKontainer kontainer) {
	
	int i = kontainer.gibFilterAnzahl();
	filterData = new Object[i][2];
	
	Iterator filterIterator = kontainer.gibAlleFilter();
	int y = 0;
	Filter filter;
	while (filterIterator.hasNext()) {
		filter = (Filter) filterIterator.next();
		setValueAt( new Boolean(filter.istAktiv()), y, 0);
		setValueAt( filter.gibName(), y, 1);
		y++;
	}
	fireTableStructureChanged();
}
	
        public int getColumnCount() {
        	return columnNames.length;
        }
        
        public int getRowCount() {
		return filterData.length;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
		return filterData[row][col];
        }

        public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
        }

        public boolean isCellEditable(int row, int col) {
	        return (col == 0);
        }

        public void setValueAt(Object value, int row, int col) {
                filterData[row][col] = value;
                fireTableCellUpdated(row, col);
        }

}
