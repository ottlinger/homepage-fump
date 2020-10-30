package com.fump.filter;

import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.mail.search.*;

/**
 * GUI-Klasse. Fenster zum Editieren eines Filters
 * @author	Voss, Stollberg
 * @version 	$Id: FilterView.java,v 1.10 2001/07/04 16:20:08 stollber Exp $
 */
public class FilterView extends JFrame implements ActionListener, WindowListener {
	private JTextField filterName;
	private JButton okButton;
	private JButton abbrechenButton;	
	private Filter filter;
	private ActionListener caller;
	private FilterAktionPanel aktionsPanel;
	private SearchTermPanel searchTermPanel; 
	private String alterFilterName;

	public static int OK = 0;
	public static int CANCEL = 1;

	/**
	*  Eventbehandlung 
	* @param e
	*/
	public void actionPerformed(ActionEvent e) {
               	if (e.getSource().equals(okButton)) {
			// Filterbearbeitung abgeschlossen:
			// 1. Fenster schliessen
			dispose();
			// 2. Caller benachrichtigen
			caller.actionPerformed(new ActionEvent(this, OK, "Filter fertig editiert"));
		} else if (e.getSource().equals(abbrechenButton)) {
			dispose();
			caller.actionPerformed(new ActionEvent(this, CANCEL, "Filter-Editor geschlossen"));
		} 
		
		//else if (e.getSource().equals(windowClosed()))
		//	caller.actionPerformed (new ActionEvent(this, CANCEL, "Filter-Editor geschlossen"));
	 }

	/**
	*  Filterobjekt zusammenbauen
	* @return Filter
	* @exception IllegalStateException
	*/
	public Filter getFilter() throws IllegalStateException {
		//System.out.println("getFilter invoked...");
		try {
			filter.setzeName(filterName.getText());
			//System.out.println("getText ok");
			filter.setzeFilterAktion(aktionsPanel.getFilterAktion());
			//System.out.println("getFilterAktion ok");
			filter.setzeFilterAusdruck(searchTermPanel.getSearchTerm());
			//System.out.println("getSearchTerm ok");
			
					
			
		} catch (Exception e) {
			throw new IllegalStateException ("Filter kann nicht erstellt werden.");
		}
		return filter;
		
	}
	
	/**
	  * Behandlung beim Fensterschliessen mit dem X-Button
	  * aus dem WindowListener Interface
	  * @param e der Event vom x-Button
	  */
	public void windowClosing(WindowEvent e) {
		dispose();
		caller.actionPerformed(new ActionEvent(this, CANCEL, "Filter-Editor geschlossen"));
	}

	/**
	  * Methode aus WindowListener Interface - macht aber nichts
	  */	
	public void windowOpened(WindowEvent e) { }

	/**
	  * Methode aus WindowListener Interface - macht aber nichts
	  */	
	public void windowClosed(WindowEvent e) { }

	/**
	  * Methode aus WindowListener Interface - macht aber nichts
	  */	
	public void windowIconified(WindowEvent e) { }

	/**
	  * Methode aus WindowListener Interface - macht aber nichts
	  */	
	public void windowDeiconified(WindowEvent e) { }
	
	/**
	  * Methode aus WindowListener Interface - macht aber nichts
	  */	
	public void windowActivated(WindowEvent e) { }

	/**
	  * Methode aus WindowListener Interface - macht aber nichts
	  */	
	public void windowDeactivated(WindowEvent e) { }

	/**
	*  Konstruktor
	* @param filter Filterobjekt, dass editiert wird
	* @param e derjenige, der diese Klasse aufruft
	* @exception IllegalAccessException
	*/
	public FilterView(Filter filter, ActionListener caller) throws IllegalAccessException {
		super("Filter Editor");
		this.filter = filter;
		this.caller = caller;
		this.alterFilterName = filter.gibName();
		setup();
		setVisible(true);

		// WindowEventbehandlung
		addWindowListener(this);
		
	}
	
	/**
	*  Konstruktor
	* @param caller derjenige, der diese Klasse aufruft
	* @exception IllegalAccessException
	*/
	public FilterView(ActionListener caller) throws IllegalAccessException {
		super("Filter Editor");
		filter = new Filter(new SubjectTerm(""), "", true, new FilterAktion());
		this.caller = caller;
		this.alterFilterName = null;
		setup();
		setVisible(true);		
/*
		// WindowEventbehandlung
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				dispose();
				caller.actionPerformed(new ActionEvent(this, CANCEL, "Filter-Editor geschlossen"));
			}
		}); */
	}
	
	/**
	  * gibt den alten Filternamen wieder aus - 
	  * zu Behandlung, wenn der Name geändert wurde
	  * @return String der alte Name
	  */	
	public String gibAlterFilterName() {
		return alterFilterName;	
	}

	/**
      *  Layoutdefinition
	  * @exception IllegalAccessException
      */                 	                  	
	public void setup() throws IllegalAccessException {
	   setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
       setSize(550, 300);
       getContentPane().setLayout(new BorderLayout());
		
		// textPanel -- Panel mit Filtername Einagebfeld
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		filterName = new JTextField(filter.gibName());
		filterName.setPreferredSize(new Dimension (200,20));
		filterName.setMinimumSize(new Dimension (200,20));
		filterName.setMaximumSize(new Dimension (200,20));
		textPanel.add(new JLabel("Filtername:"));
		textPanel.add(filterName);
		aktionsPanel = new FilterAktionPanel(filter.gibFilterAktion());
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new GridLayout(2,1));
		northPanel.setPreferredSize(new Dimension (400,80));
		northPanel.setMinimumSize(new Dimension (400,80));
		northPanel.setMaximumSize(new Dimension (400,80));
		northPanel.add(textPanel);
		northPanel.add(aktionsPanel);
		getContentPane().add(BorderLayout.NORTH, northPanel);

		searchTermPanel = new SearchTermPanel(filter.gibFilterAusdruck());
		getContentPane().add(BorderLayout.CENTER, searchTermPanel);

		// buttonPanel -- main buttons
		JPanel buttonPanel = new JPanel(); 
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		okButton = new JButton("OK");
		okButton.addActionListener(this);
		abbrechenButton = new JButton("Abbrechen");
		abbrechenButton.addActionListener(this);
		buttonPanel.add(okButton);
		buttonPanel.add(abbrechenButton);
		getContentPane().add(BorderLayout.SOUTH, buttonPanel);


       }

       public static void main(java.lang.String[] args) {
       		try {
       			FilterView test = new FilterView(new ActionListener() { 
				public void actionPerformed(ActionEvent e) { 
					System.out.println("Event from FilterView: " + e.getActionCommand()); 
					Filter filter = ((FilterView) e.getSource()).getFilter();
					System.out.println("Filtername: " + filter.gibName());
					System.out.println("FilterAktion: " + filter.gibFilterAktion().gibAktion());
					/* System.out.println("\nPassing edited filter to another instance of FilterView...");
					try {
						FilterView test = new FilterView(filter, new ActionListener() { 
							 public void actionPerformed(ActionEvent e) { 
								 System.out.println("Event from FilterView 2: " + e.getActionCommand()); 
								 Filter filter = ((FilterView) e.getSource()).getFilter();
								 System.out.println("Filtername: " + filter.gibName());
								 System.out.println("FilterAktion: " + filter.gibFilterAktion().gibAktion());
								 System.exit(0);
							}
						});
					} catch (Exception e2) {} */
					System.exit(0);
				}	
			});
			System.out.println("waiting for event from FilterView...");				
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}	
       }
}
