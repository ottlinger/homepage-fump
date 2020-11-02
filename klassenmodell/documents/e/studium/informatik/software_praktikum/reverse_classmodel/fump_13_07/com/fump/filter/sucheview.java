package com.fump.filter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.mail.search.*;
import javax.swing.event.*;
import com.fump.*;
import java.util.*;
import javax.mail.*;

/**
 * GUI-Klasse. Fenster zum Suchen.
 * @author	Voss, Stollberg
 * @version 	$Id: SucheView.java,v 1.8 2001/07/11 16:58:41 stollber Exp $
 */
public class SucheView extends JFrame implements ActionListener, ChangeListener {
	private JButton okButton;
	private JButton abbrechenButton;	
	private EinzelSearchTermPanel searchTermPanel; 
	private JProgressBar progressBar;
	private FilterTabellenModell mailTable;
	private boolean searching = false;
	
	/**
	*  Eventbehandlung 
	*  @param e
	*/
	public void actionPerformed(ActionEvent e) {
       	if (e.getSource().equals(okButton) && !searching) {
			// Suchen
			try { 
				suchen();
			} catch (SearchException se) {
				JOptionPane.showMessageDialog(null, "Während der Suche trat ein Fehler auf!", "What the f*ck?!",
				JOptionPane.ERROR_MESSAGE);
			}			
		} else if (e.getSource().equals(abbrechenButton)) {
			// Abbrechen, Fenster schliessen
			dispose();
		} 
	 }


	/**
	 * Behandelt Suchfortschritts-Ereignisse und updated die ProgressBar.
	 */
	public void stateChanged(ChangeEvent e) {
		if (((Suche) e.getSource()).isDone()) {
			// search done, get mails and notify user
			Vector mails = ((Suche) e.getSource()).getMails();
			JOptionPane.showMessageDialog(null, "Suche abgeschlossen, " + mails.size() + " Mails gefunden.", "Suche", JOptionPane.INFORMATION_MESSAGE);
			
			// add mails to mail table
			Iterator mailIterator = mails.iterator();
			while (mailIterator.hasNext()) {
				mailTable.addMail((Mail) mailIterator.next());
			}
						
			// enable new searches
			searching = false;			
		} else {
			// still searching
			int progress = ((Suche) e.getSource()).getProgress();
			progressBar.setValue(progress);
			progressBar.repaint();
		}
	}
	
	/**
	*  Suche durchführen.
	*  @exception SearchException Fehler beim suchen
	*/
	private void suchen() throws SearchException {
		Ordner root;
		try {
			root = OrdnerOberflaeche.gibAktuelleWurzelAlsObjekt();
		} catch (JTreeLaeuftNichtRichtig e) {
			throw new SearchException("JTree fehlerhaft!");
		}
		SearchTerm suchAusdruck = searchTermPanel.getSearchTerm();
		progressBar.setValue(0);
		searching = true;
		Suche suche = new Suche(root, suchAusdruck, true);
		suche.addChangeListener(this);
		Thread sucheThread = new Thread(suche);
		sucheThread.start();		
	}
	
	/*
	private void suchen() throws SearchException {
		Ordner root = new Ordner();
		SearchTerm suchAusdruck = searchTermPanel.getSearchTerm();
		progressBar.setValue(0);
		searching = true;
		Suche suche = new Suche(root, suchAusdruck, true);
		suche.addChangeListener(this);
		Thread sucheThread = new Thread(suche);
		sucheThread.start();		
	}
	*/
	
	/**
	*  Konstruktor
	*/
	public SucheView()  {
		super("Suchen");
		setup();
		setVisible(true);		
	}

        /**
         *  Layoutdefinition
         */                 	                  	
	private void setup() { 
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
       	setSize(550, 350);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		searchTermPanel = new EinzelSearchTermPanel();
		
		okButton = new JButton("Suchen");
		okButton.addActionListener(this);
		abbrechenButton = new JButton("Schliessen");
		abbrechenButton.addActionListener(this);
		JPanel buttonPanel = new JPanel(); 
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(okButton);
		buttonPanel.add(Box.createRigidArea(new Dimension (5,0)));
		buttonPanel.add(abbrechenButton);
		
		progressBar = new JProgressBar(0, 100);
		progressBar.setMinimumSize(new Dimension(300,20));
		progressBar.setMaximumSize(new Dimension(300,20));
		progressBar.setPreferredSize(new Dimension(300,20));		
		JPanel progressPanel = new JPanel();
		progressPanel.add(new JLabel("Suchfortschritt: "));
		progressPanel.add(progressBar);				
		
		LasseKlasse mailView = new LasseKlasse();
		mailTable = (FilterTabellenModell) mailView.tabelle.getModel();
		
		getContentPane().add(searchTermPanel);
		getContentPane().add(Box.createRigidArea(new Dimension (0,5)));
		getContentPane().add(buttonPanel);		
		getContentPane().add(Box.createRigidArea(new Dimension (0,5)));
		getContentPane().add(progressPanel);		
		getContentPane().add(Box.createRigidArea(new Dimension (0,5)));
		getContentPane().add(mailView);
       }


	/**
	 * Nur zum testen.
	 */
	public static void main(java.lang.String[] args) {
		SucheView suche = new SucheView();
       }
}
