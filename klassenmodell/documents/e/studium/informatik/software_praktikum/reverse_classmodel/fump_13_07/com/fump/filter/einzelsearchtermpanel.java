package com.fump.filter;

import javax.swing.*;
import java.util.*;
import java.awt.*;
import javax.mail.*;
import javax.mail.search.*;

/**
 *  GUI-Klasse fuer einzelne Search Terms
 *  Ein JPanel mit den 3 Editionsfeldern 
 * @author	Voss, Stollberg
 * @version 	$Id: EinzelSearchTermPanel.java,v 1.6 2001/07/11 14:53:01 lueck Exp $
 */
public class EinzelSearchTermPanel extends JPanel {
      private JComboBox searchTermComboBox;
      private JComboBox operatorComboBox;
      private JTextField filterTextField;
      private final static String[] searchTerms = {"Betreff", "Absender", "Empfänger", "Nachrichtentext"};
      private final static String[] searchOperators = {"enthält"};


      /**
        *  1. Konstruktor ohne Parameter
        */                 	
      public EinzelSearchTermPanel() {
      	setup();
      }


      /**
        *  Layoutdefinition mit GridBagLayout
        */                 	                  	
      private void setup() {
              //setLayout(new java.awt.GridBagLayout());
			  setLayout(new FlowLayout());
			  setSize(450, 30);
//      	      setPreferredSize(new Dimension(450, 25));
//              setMinimumSize(new Dimension(450, 25));
//              setMaximumSize(new Dimension(450, 25));
				
              searchTermComboBox = new javax.swing.JComboBox(searchTerms);
              searchTermComboBox.setPreferredSize(new Dimension(150, 25));	
              searchTermComboBox.setMaximumSize(new Dimension(150, 25));
              searchTermComboBox.setMinimumSize(new Dimension(150, 25));
              add(searchTermComboBox);	

              operatorComboBox = new javax.swing.JComboBox(searchOperators);
              operatorComboBox.setPreferredSize(new Dimension(120, 25));	
              operatorComboBox.setMaximumSize(new Dimension(120, 25));
              operatorComboBox.setMinimumSize(new Dimension(120, 25));
              add(operatorComboBox);

              filterTextField = new javax.swing.JTextField();
              filterTextField.setPreferredSize(new Dimension(120, 25));
              filterTextField.setMaximumSize(new Dimension(120, 25));
              filterTextField.setMinimumSize(new Dimension(120, 25));
              add(filterTextField);	
      }



      /**
      *  2. Konstruktor ohne Parameter
      * @param ausdruck ein SearchTerm
      */                 	
      public EinzelSearchTermPanel(SearchTerm ausdruck) {
	      setup();
	      setSearchTerm(ausdruck);
      }

      /**
      *  Auslesen des konkreten SearchTerms / Unterklasse der Instanz
      * @param ausdruck der SearchTerm 
      * @exception IllegalArgumentException
      */

      public void setSearchTerm(SearchTerm ausdruck)  throws IllegalArgumentException {
	      filterTextField.setText(((StringTerm) ausdruck).getPattern());
		
		// System.out.println("EinzelSearchTerm - type: " + ausdruck.getClass().getName());

	      if (ausdruck.getClass().getName().equals("javax.mail.search.SubjectTerm")) {
		      searchTermComboBox.setSelectedIndex(0);
	      } else if (ausdruck.getClass().getName().equals("javax.mail.search.FromStringTerm")) {
		      searchTermComboBox.setSelectedIndex(1);
	      } else if (ausdruck.getClass().getName().equals("javax.mail.search.RecipientStringTerm")) {
		      searchTermComboBox.setSelectedIndex(2);
	      } else if (ausdruck.getClass().getName().equals("javax.mail.search.BodyTerm")) {
		      searchTermComboBox.setSelectedIndex(3);
	      } else throw new IllegalArgumentException("ungueltiger Filtertyp: "+ ausdruck.getClass().getName());
	      
	      // System.out.println("EinzelSearchTermPanel: setSearchTerm done");
      }

      /**
      *  Auslesen des konkreten SearchTerms / Unterklasse der Instanz
      * @param ausdruck der SearchTerm 
      * @exception IllegalArgumentException
	  * @returns SearchTerm
      */
      public SearchTerm getSearchTerm() throws SearchException {
	       String searchTerm = (String) searchTermComboBox.getSelectedItem();
	       String searchOperator = (String) operatorComboBox.getSelectedItem();
	       String filterText = filterTextField.getText();

	       if (searchTerm.equals("Betreff"))
		       return new SubjectTerm(filterText);
	       else if (searchTerm.equals("Absender"))
		       return new FromStringTerm(filterText);
	       else if (searchTerm.equals("Empfänger"))			
		       return new RecipientStringTerm(Message.RecipientType.TO, filterText);
	       else if (searchTerm.equals("Nachrichtentext"))
		       return new BodyTerm(filterText);
	       else
		       throw new SearchException("ungueltige Auswahl");
      }

      /**
      * main-Methode zum Testen
      */	
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
                      testFenster.setSize(450, 30);
                	      testFenster.getContentPane().add(new EinzelSearchTermPanel(new BodyTerm("TestTextHallo")));
                      testFenster.setVisible(true);
              } catch (Throwable exception) {
                      System.err.println("In main() von Filter_View trat eine Ausnahmebedingung auf");
                      exception.printStackTrace(System.out);
              }
      }

}
