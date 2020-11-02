package com.fump.filter;

import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.mail.*;
import javax.mail.search.*;

/**
 * GUI-Klasse. Definition der Filteraktion 
 * @author	Voss, Stollberg
 * @version 	$Id: SearchTermPanel.java,v 1.7 2001/07/11 14:53:02 lueck Exp $
 */
public class SearchTermPanel extends JPanel implements ActionListener {
	
	private Vector allTermPanels;
	private final static int AND = 0;
	private final static int OR = 1;	
	private int mode;
	private JButton more;
	private JButton less;
	private final static String[] modeText = {"alle Bedingungen (AND)", "mind. eine Bedingung (OR)"};
	private JComboBox modeComboBox;
	
	/**
	*  1. Konstruktor
	*/
        public SearchTermPanel() {
		mode = AND;
		allTermPanels = new Vector();
		allTermPanels.add(new EinzelSearchTermPanel());		
		setup();
	}
                  	
	
	/**
	*  Eventbehandlung
	* @param ActionEvent
	*/
	public void actionPerformed(ActionEvent e) {
                if (e.getSource().equals(more))
			addSearchTerm();
		else if (e.getSource().equals(less))
			removeSearchTerm();
	 }

	/**
	*  neues EinzelSearchTermPanel hinzufügen 
	* @see EinzelSearchTermPanel
	*/
	private void addSearchTerm() {
		EinzelSearchTermPanel ausdruck = new EinzelSearchTermPanel();
		allTermPanels.add(ausdruck);
		GridBagConstraints constraintsSearchTermPanel = new GridBagConstraints();
		constraintsSearchTermPanel.gridx = 0; constraintsSearchTermPanel.gridy = allTermPanels.size()+1;
		constraintsSearchTermPanel.fill = GridBagConstraints.BOTH;
		constraintsSearchTermPanel.weightx = 1.0;
		constraintsSearchTermPanel.weighty = 1.0;
		constraintsSearchTermPanel.insets = new Insets(4, 4, 4, 4);
		add((JPanel) ausdruck, constraintsSearchTermPanel);
                repaint();
		validate();
	}

	/**
	*  EinzelSearchTermPanel entfernen
	* @see EinzelSearchTermPanel
	*/
	private void removeSearchTerm() {
		if (allTermPanels.size() > 1) {
			remove(allTermPanels.size());
			allTermPanels.remove(allTermPanels.size() -1);
                        repaint();
		}
	}

	/**
	*  Layoutdefinition mit GridBagLayout
	*/
	private void setup() {
                /*setLayout(new java.awt.GridBagLayout());

		GridBagConstraints constraintsSearchTermPanel = new GridBagConstraints();
		constraintsSearchTermPanel.gridx = 0; constraintsSearchTermPanel.gridy = 0;
		constraintsSearchTermPanel.fill = GridBagConstraints.BOTH;
		constraintsSearchTermPanel.weightx = 1.0;
		constraintsSearchTermPanel.weighty = 1.0;
		constraintsSearchTermPanel.insets = new Insets(4, 4, 4, 4); */

		// modeComboBox -- Verknüpfungsmodus
		modeComboBox = new JComboBox(modeText);
       	modeComboBox.setPreferredSize(new Dimension(250, 25));	
        modeComboBox.setMaximumSize(new Dimension(250, 25));
        modeComboBox.setMinimumSize(new Dimension(250, 25));
		modeComboBox.setSelectedIndex(mode);

		// more, less -- Buttons
		more = new JButton("Meer!");
		more.addActionListener(this);
		less = new JButton("Weniger");
		less.addActionListener(this);		

		// modePanel -- Modus + Buttons
        JPanel modePanel = new JPanel();
		setPreferredSize(new Dimension(250,30));
        setMinimumSize(new Dimension(250,30));
        setMaximumSize(new Dimension(250,30));
		modePanel.add(modeComboBox);
		modePanel.add(more);
		modePanel.add(less);

		//add(modePanel, constraintsSearchTermPanel);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(modePanel);

		Iterator i = allTermPanels.iterator();	
		int y = 1;
		
		while (i.hasNext() ) {
			//constraintsSearchTermPanel.gridy = y++;
			//add((JPanel) i.next(), constraintsSearchTermPanel);
			add((JPanel) i.next());
		}

		setPreferredSize(new Dimension(450, y * 30));
        setMinimumSize(new Dimension(450, y * 30));
        setMaximumSize(new Dimension(450, y * 30));
        }

	/**
	*  SearchTerm zusammenbauen 
	* @param ausdruck 
	*/
	public SearchTermPanel(SearchTerm ausdruck) {
     		
		allTermPanels = new Vector();
		
		if (ausdruck.getClass().getName().equals("javax.mail.search.AndTerm")) {
                        mode = AND;
			//System.out.println("SearchTermPanel: AND");
			Iterator allPanels = (new Vector(Arrays.asList(((AndTerm) ausdruck).getTerms())).iterator());
			while (allPanels.hasNext()) {
			 	allTermPanels.add(new EinzelSearchTermPanel((SearchTerm) allPanels.next()));
			}	
		} else if (ausdruck.getClass().getName().equals("javax.mail.search.OrTerm")) {
			mode = OR;
			//System.out.println("SearchTermPanel: OR");			
			Iterator allPanels = (new Vector(Arrays.asList(((OrTerm) ausdruck).getTerms())).iterator());
			while (allPanels.hasNext()) {
			 	allTermPanels.add(new EinzelSearchTermPanel((SearchTerm) allPanels.next()));
			}	

		} else {
			mode = AND;
			//System.out.println("SearchTermPanel: Unknown");
			allTermPanels.add(new EinzelSearchTermPanel(ausdruck));
		}
	
		setup();
	}
	
	/**
	*  SearchTerm ausgeben
	* @param ausdruck 
	* @exception SearchException
	* @returns SearchTerm
	*/
        public SearchTerm getSearchTerm() throws SearchException {
		
		Vector searchTerms = new Vector();
		Iterator pans = allTermPanels.iterator();
		mode = modeComboBox.getSelectedIndex();

		while (pans.hasNext()) {
			searchTerms.add(((EinzelSearchTermPanel) pans.next()).getSearchTerm());			
		}
		
		//System.out.println("getSearchTerm: compiling vector of searchterms ok");
				
		if (allTermPanels.size() == 1) {
			//System.out.println("single searchterm");
			return (SearchTerm) searchTerms.get(0);
		} else {
			SearchTerm[] terms = new SearchTerm[allTermPanels.size()];
			if (mode == AND) {
				//System.out.println("AND searchterms");
				return new AndTerm((SearchTerm[]) searchTerms.toArray(terms));
			} else if (mode == OR) {
				//System.out.println("OR searchterms");		
				return new OrTerm((SearchTerm[]) searchTerms.toArray(terms));
			} else throw new SearchException("interner Fehler 08/15:kein Suchausdruck vorhanden." );		
		}
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
                      //testFenster.setContentPane(new EinzelSearchTermPanel());
                      testFenster.setSize(800, 300);
                      testFenster.getContentPane().add(new SearchTermPanel(new AndTerm(new BodyTerm("TestTextHallo"), new SubjectTerm("John Doe"))));
                      testFenster.setVisible(true);
              } catch (Throwable exception) {
                      System.err.println("In main() von Filter_View trat eine Ausnahmebedingung auf");
                      exception.printStackTrace(System.out);
              }
      }



}
