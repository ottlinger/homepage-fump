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

public class NurGruppenOderPersonenAnzeigen extends JFrame implements ActionListener{
	
	JPanel anzeige;
	Adressbuch buch;
	boolean gruppe;
	Vector selEintraege;
	JTable table;
	JScrollPane scrollPane;
	JButton ok;
	int selRow;

	public NurGruppenOderPersonenAnzeigen(String title, Adressbuch book, boolean gruppe){
		super(title);
		buch = book;
		this.gruppe = gruppe;
		this.getContentPane().setLayout(new BorderLayout());
		
		anzeige = new JPanel();
		
		selEintraege = new Vector();
			
		if(gruppe){// falls nur Gruppen angezeigt werden sollen
			for(int i = 0; i < buch.eintraege.size();i++){
				Eintrag tmp = (Eintrag)buch.eintraege.elementAt(i);
				if(tmp.getIsGroup())selEintraege.addElement(tmp);
			}
		}
		else{	// nur Personen anzeigen
			for(int i = 0; i < buch.eintraege.size(); i++){
				Eintrag tmp = (Eintrag)buch.eintraege.elementAt(i);
				if(!tmp.getIsGroup())selEintraege.addElement(tmp);
			}
		}
		
		TableModel mod = new AbstractTableModel(){
			public int getColumnCount() { return 3; }
			public String getColumnName(int col){
				switch (col){
					case 0: return "Name";
					case 1: return "Alias";
					case 2: return "Adresse(n)";
				}
				return new String("");
			}
			public int getRowCount() { return selEintraege.size();}
			public Object getValueAt(int row, int col){
				if (row>selEintraege.size()) return new String("?");
				Eintrag e = (Eintrag) selEintraege.elementAt(row);
				switch (col) {
					case 0: return e.getName();
					case 1: return e.getAlias();
					case 2: return e.getAdressenString();
				}
				return new String("");
			}
		};
		
		table = new JTable(mod);
					
		scrollPane = new JScrollPane(table);
		this.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		ok = new JButton("ok");
		ok.addActionListener(this);
		this.getContentPane().add(ok, BorderLayout.SOUTH);
		this.pack();
		this.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==ok)dispose();	
	}
}