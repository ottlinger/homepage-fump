package com.fump.filter;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
//import com.fump.*;


/**
 * GUI-Klasse. Definition der Filteraktion 
 * @author	Voss, Stollberg
 * @version 	$Id: FilterAktionPanel.java,v 1.8 2001/07/11 14:53:01 lueck Exp $
 */
 public class FilterAktionPanel extends JPanel implements ItemListener {
	
	private final static int LOESCHEN = 0;
	private final static int VERSCHIEBEN = 1;	
	private int mode;
	//private TreePath zielOrdner;
        private final static String[] modeText = {"gelöscht", "verschoben"};
	private JComboBox modeComboBox;
	private JScrollPane folderTree;
	
	/**
	*  1. Konstruktor
	*/
        public FilterAktionPanel() {
		mode = LOESCHEN;
		//zielOrdner = null;
		setup();
	}
 	
	/**
	*  2. Konstruktor
	* @param FilterAktion
	*/
	public FilterAktionPanel(FilterAktion aktion) throws IllegalAccessException {
		mode = aktion.gibAktion();
		//if (mode == VERSCHIEBEN) zielOrdner = aktion.gibKnoten();
		setup();
	}

	/**
	* Änderunge der Filteraktion
	* @param ItemEvent 
	*/
	public void itemStateChanged(ItemEvent e) {
               	if (e.getSource().equals(modeComboBox) && e.getStateChange() == ItemEvent.SELECTED) {
			/*
			if (modeComboBox.getSelectedIndex() == 0) {
				folderTree.setVisible(false);
			} else {
				folderTree.setVisible(true);
			}
			*/
			mode = modeComboBox.getSelectedIndex();
			repaint();			
			validate();
		}
	 }

        /**
         *  Layoutdefinition mit GridBagLayout
         */                 	                  	
   	public void setup() {
		
		// Panel
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		// Auswahl Aktion
		modeComboBox = new JComboBox(modeText);
                modeComboBox.setPreferredSize(new Dimension(150, 25));	
                modeComboBox.setMaximumSize(new Dimension(150, 25));
                modeComboBox.setMinimumSize(new Dimension(150, 25));
		modeComboBox.setSelectedIndex(mode);
		modeComboBox.addItemListener(this);
		
		

		Dimension ps = new Dimension(300, 50);
		setPreferredSize(ps);
        setMinimumSize(ps);
        setMaximumSize(ps);
		add(new JLabel("E-Mails werden:"));
		add(modeComboBox);

		/*
		folderTree = (JScrollPane) OrdnerOberflaeche.zeigeOrdner(FUMP.benutzer.getName(), FUMP.benutzer.getRootOrdner());
                	folderTree.setPreferredSize(new Dimension(150, 80));	
                                folderTree.setMaximumSize(new Dimension(150, 80));
                	folderTree.setMinimumSize(new Dimension(150, 80));
		if (mode == LOESCHEN) {
			folderTree.setVisible(false);
		} else {
			folderTree.setVisible(true);
			try {
				OrdnerOberflaeche.setzeAktPfad(zielOrdner);
			} catch (Exception e) {}
		 }
		add(folderTree);
		*/
	
        }

        /**
         *  Filteraktion auslesen
	     * @return FilterAktion
		 * @exception NoSuchElementException
         */                 	                  	
	public FilterAktion getFilterAktion() throws NoSuchElementException {
		FilterAktion aktion = new FilterAktion();
		try {
			aktion.setzeAktion(mode);
		} catch (Exception e) {
			throw new NoSuchElementException("Böser interner Fehler 4711!! (FilterAktion kann nicht gesetzt werden)");
		}
		return aktion;
		/*
		if (mode  == VERSCHIEBEN) {
			try {
				return new FilterAktion(OrdnerOberflaeche.gibAktPfad());
			} catch (Exception e) {
				throw new NoSuchElementException("Ordnerpfad kann nicht gelesen werden!");
			}
		} else
			return new FilterAktion();		
		*/
	}

                public static void main(java.lang.String[] args) {
                	try {
                		/* Eigene Darstellungsart festlegen */
                		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                		/* Den Rahmen erstellen */
                		JFrame testFenster = new JFrame("Test");			
                		testFenster.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
                		// Filter_View aFilter_View = new Filter_View();
                		/* Einen windowListener für windowClosedEvent hinzufügen */
                		testFenster.addWindowListener(new java.awt.event.WindowAdapter() {
                			public void windowClosed(java.awt.event.WindowEvent e) {
                				System.exit(0);
                			};
                		});
                		testFenster.setSize(500, 300);
                	        testFenster.getContentPane().add(new FilterAktionPanel());
                		testFenster.setVisible(true);
                	} catch (Throwable exception) {
                		System.err.println("In main() von Filter_View trat eine Ausnahmebedingung auf");
                		exception.printStackTrace(System.out);
                	}
                }



}
