package com.fump.filter;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import com.fump.hilfe.*;
import com.fump.*;

/** GUI-Klasse. Verwalten der Filter eines Benutzers
  * @author Voss, Stollberg, Krause, Lueck
  * @version $Id: FilterKontainerView.java,v 1.5 2001/07/11 15:56:56 stollber Exp $
  */
public class FilterKontainerView extends JFrame implements ActionListener, TableModelListener {

private JLabel filterLabel;
private JPanel mainPanel;
private JPanel mainButtonPanel;
private JTable filterTable;
private JPanel mainButtonSubPanel;
private JButton neuButton;
private JButton bearbeitenButton;
private JButton loeschenButton;
private JButton hilfeButton;
private JButton okButton;
private JButton abbrechenButton;

private FilterView editor;
private FilterKontainer kontainer;
private FilterKontainerTableModel tabelle;
	

/**
  * Konstruktor ohne Parameter
  */
public FilterKontainerView(FilterKontainer kontainer) {
	super("FUMP Filter");
	this.kontainer = kontainer;
	setup();
}

/**
 * Tabelle wurde bearbeitet - Filter aktivieren/deaktivieren.
 */
public void tableChanged(TableModelEvent e) {
	if (e.getSource().equals(tabelle) && (e.getType() == TableModelEvent.UPDATE) && (e.getFirstRow() != -1) && (e.getColumn() == 0)) {
		// Filter-Aktiv Checkbox wurde geändert
		try {
			Filter filter = kontainer.gibFilter((String) tabelle.getValueAt(e.getFirstRow(), 1));
			boolean aktiv = ((Boolean) tabelle.getValueAt(e.getFirstRow(), 0)).booleanValue();
			if (aktiv) {
				filter.aktivieren();
			} else {
				filter.deaktivieren();
			}
		} catch (Exception a) {
			// kein "echter" Fehler -> tritt auf, wenn JTable beim Filter einfuegen synchronisiert wird			
		}
	}
}

/**
  * Eventbehandlung
  * @param e der Event eben
  */
public void actionPerformed (ActionEvent e) {
	if (e.getSource().equals(neuButton) && (editor == null)) {
		// neuen Filter anlegen
		try {
			editor = new FilterView(this);
		} catch (Exception a) {
			JOptionPane.showMessageDialog(null, "Interner Fehler - kann Filter Editor nicht instantiieren!", "Fehler",
			JOptionPane.ERROR_MESSAGE);
		}

	} else if (e.getSource().equals(bearbeitenButton) && (filterTable.getSelectedRow() != -1) && (editor == null)) {
		// existierenden Filter bearbeiten
		try {
			String filterName = (String) tabelle.getValueAt(filterTable.getSelectedRow(), 1);
			editor = new FilterView(kontainer.gibFilter(filterName), this);
		} catch (Exception a) {
			JOptionPane.showMessageDialog(null, "Ansicht nicht aktuell","FEHLER",
			JOptionPane.ERROR_MESSAGE);			
		}

	} else if (e.getSource().equals(loeschenButton) && (filterTable.getSelectedRow() != -1)) {
		// existierenden Filter loeschen
		
		if (JOptionPane.showConfirmDialog(this, "Filter ganz in echt wirklich total im Ernst löschen?", "Löschen",
		JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			try {
				String filterName = (String) tabelle.getValueAt(filterTable.getSelectedRow(), 1);
				kontainer.loeschen(filterName);
				tabelle.erfrische(kontainer);
				filterTable.repaint();						
			} catch (Exception a) {
				JOptionPane.showMessageDialog(null, "Ansicht nicht aktuell","FEHLER",
				JOptionPane.ERROR_MESSAGE);			
			}
		}

	} else if (e.getSource().equals(abbrechenButton)) {
		// Dialog schliessen
		dispose();
	} else if (e.getSource().equals(hilfeButton)) {
		// Hilfe anzeigen
		FUMP.hilfe.kontext_hilfe("Filter");
	} else if (e.getSource().equals(okButton)) {
		// Filter speichern und Dialog schliessen
		kontainer.speichern();
		dispose();
	} else if (e.getSource().equals(editor) && e.getID() == FilterView.OK) {
		// bearbeiteten Filter aktualisieren
		filterAktuell();

	} else if (e.getSource().equals(editor) && e.getID() == FilterView.CANCEL) {
		// Filter-Editor Fenster wurde geschlossen
		editor = null;
	}
} 	


/**
 * Verarbeite Filter aus Filter-Editor nach abgeschlossener Bearbeitung.
 */
private void filterAktuell() {
	try {
		if (editor.gibAlterFilterName() != null) {
			// ein Filter wurde bearbeitet
			if (editor.gibAlterFilterName().equals(editor.getFilter().gibName())) {
				// Filtername ist gleich geblieben - Objekt ersetzen
				Filter filter = kontainer.gibFilter(editor.getFilter().gibName());
				filter = editor.getFilter();
			} else {
				// Filtername wurde geaendert
				kontainer.ersetze(editor.gibAlterFilterName(), editor.getFilter());
			}
		} else {
			// ein neuer Filter wurde angelegt
			kontainer.hinzufuegen(editor.getFilter());
		}
		editor = null;
		tabelle.erfrische(kontainer);
		filterTable.repaint();
	} catch (IllegalArgumentException e)	{
		// Filtername bereits vorhanden!
		String filterName = editor.getFilter().gibName();

		// erfrage neuen Filternamen
		while (kontainer.existiert(filterName)) {
			filterName = JOptionPane.showInputDialog(this, "Filtername schon vorhanden - bitte anderen Namen angeben:", "Fehler",
		JOptionPane.WARNING_MESSAGE);
		} 
		
		// modifizierten Filter im Kontainer speichern
		Filter filter = editor.getFilter();
		filter.setzeName(filterName);
		kontainer.hinzufuegen(filter);

		// Editor deaktivieren, JTable updaten
		editor = null;
		tabelle.erfrische(kontainer);
		filterTable.repaint();					
	}
}

/**
 * Öffne Filter zum editieren in einem neuen Fenster.
 */
private void editiereFilter(Filter filter) {
	try {
		editor = new FilterView(filter, this);
	} catch (Exception x) {
		JOptionPane.showMessageDialog(null, "interner Fehler","FEHLER",
		JOptionPane.ERROR_MESSAGE);			
	}				
}

/**
  * Layoutdefinition
  */
public void setup() {
	setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	setSize(500, 270);
	
	getContentPane().setLayout(new GridBagLayout());

	// Filterueberschrift	
	GridBagConstraints filterNameContraints = new GridBagConstraints();
	filterNameContraints.gridx = 0;
	filterNameContraints.gridy = 0;
	filterNameContraints.anchor = filterNameContraints.NORTHEAST;
	filterNameContraints.insets = new Insets(4,4,4,4);
	filterLabel = new JLabel("Filter:");
	getContentPane().add(filterLabel, filterNameContraints);

	// mainPanel Inhalt
	mainPanel = new JPanel();
	mainPanel.setSize(500,270);
	mainPanel.setLayout(new GridLayout(1,2));
	mainPanel.add(zeigeFilterListe());
	
	mainButtonPanel = new JPanel();
	mainButtonPanel.setSize(200,125);
	mainButtonSubPanel = new JPanel();
	mainButtonSubPanel.setLayout(new GridLayout(4,1));
	neuButton = new JButton("Neu");
	neuButton.addActionListener(this);
	bearbeitenButton = new JButton("Bearbeiten");
	bearbeitenButton.addActionListener(this);
	loeschenButton = new JButton("Löschen");
	loeschenButton.addActionListener(this);
	hilfeButton = new JButton("Hilfe");
	hilfeButton.addActionListener(this);
	mainButtonSubPanel.add(neuButton);
	mainButtonSubPanel.add(bearbeitenButton);
	mainButtonSubPanel.add(loeschenButton);
	mainButtonSubPanel.add(hilfeButton);
	
	mainButtonPanel.add(mainButtonSubPanel);
	
	mainPanel.add(mainButtonPanel);	


	// mainPanel
	GridBagConstraints mainPanelContraints = new GridBagConstraints();
	mainPanelContraints.gridx = 0;
	mainPanelContraints.gridy = 1;
	mainPanelContraints.gridheight = 4;
	mainPanelContraints.gridwidth = 5;
	mainPanelContraints.insets = new Insets(4,4,4,4);
	getContentPane().add(mainPanel, mainPanelContraints);



	// OK Button
	GridBagConstraints OKButtonContraints = new GridBagConstraints();
	OKButtonContraints.gridx = 4;
	OKButtonContraints.gridy = 5;
	OKButtonContraints.anchor = OKButtonContraints.SOUTHEAST;
	OKButtonContraints.insets = new Insets(4,4,4,4);
	
	JPanel untereButtonPanel = new JPanel();
	untereButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
	okButton = new JButton("OK");
	okButton.addActionListener(this);
	abbrechenButton = new JButton("Abbrechen");
	abbrechenButton.addActionListener(this);
	untereButtonPanel.add(okButton);
	untereButtonPanel.add(abbrechenButton);
	getContentPane().add(untereButtonPanel, OKButtonContraints);
/*	
	// Abbrechen Button
	GridBagConstraints AbbrechenButtonContraints = new GridBagConstraints();
	AbbrechenButtonContraints.gridx = 4;
	AbbrechenButtonContraints.gridy = 5;
	AbbrechenButtonContraints.anchor = OKButtonContraints.SOUTHWEST;
	AbbrechenButtonContraints.insets = new Insets(4,4,4,4);
	abbrechenButton = new JButton("Abbrechen");
	abbrechenButton.addActionListener(this);
	getContentPane().add(abbrechenButton, AbbrechenButtonContraints);
*/	
	setVisible(true);
}

/** 
  * Anzeigen der Filter in Tabelle
  */
public JScrollPane zeigeFilterListe() {
	tabelle = new FilterKontainerTableModel(kontainer);
	tabelle.addTableModelListener(this);
	filterTable = new JTable(tabelle);
	JScrollPane filterScrollPane = new JScrollPane(filterTable);
	
		filterScrollPane.setMinimumSize(new Dimension(220,100)); 
		filterScrollPane.setMaximumSize(new Dimension(220,100)); 
		filterScrollPane.setPreferredSize(new Dimension(220,100)); 	
	
	return filterScrollPane;
} 


public static void main(java.lang.String[] args) {
	try {
		FilterKontainerView test = new FilterKontainerView(new FilterKontainer("filters.data"));
	} catch (Exception e) {
		System.out.println(e.getMessage());
	}
}

}
