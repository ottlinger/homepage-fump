package com.fump.bearbeiten;

/**
*  @author Tobias Becker (tbecker), Joerg Dieckmann (dieck)
*  @version $Id: SchreibPanel.java,v 1.7 2001/07/11 16:39:09 tbecker Exp $
*
*
*/

import com.fump.pkonto.*;
import com.fump.*;
import com.fump.bearbeiten.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;


public class SchreibPanel extends JPanel implements ActionListener
{
	private NeueMail mailFenster;
   private KontoContainer kontoContainer;
		
	private JButton abschickenKnopf;
	private JLabel absenderLabel;
	private JComboBox absenderFeld;
	private JTextField anAdressenFeld;
	private JLabel anLabel;
	private JTextField kopieAdressenFeld;
	private JLabel kopieLabel;
	private JTextField blindKopieAdressenFeld;
	private JLabel blindKopieLabel;
	private JButton adressbuchOeffnenAnKnopf;
	private JButton adressbuchOeffnenCCKnopf;
	private JButton adressbuchOeffnenBCCKnopf;
	private JLabel betreffLabel;
	private JTextField betreffFeld;
	private JLabel anhaengselLabel;
	private JComboBox anhaengselFeld;
	private JButton anhaengselKnopf;
	private Vector anhaengselDateien = new Vector();
   private GridBagLayout gbl = new GridBagLayout();


	
	
	public SchreibPanel(NeueMail neueMail)
	{
		// damit man bei dem ActionListener auf das TextFEld zugreifen kann
		mailFenster = neueMail;
		
		setLayout(gbl);
	
		absenderLabel = new JLabel("Absender:");
		addComponent(absenderLabel,0,0,1,1,0.0,0.0);		
		absenderFeld = new JComboBox();
		// hier muss die Methode zum Abrufen der
		// Absendernamen aufgerufen werden; sie soll einen Vector zurückgeben.	
		// ACHTUNG: am Schluss der Methode beim Mailschicken muss auf das selectedItem
		// der Combobox zurückgegriffen werden.
		addComponent(absenderFeld,1,0,0,1,1.0,0.0);
		
		anLabel = new JLabel("An:");
		addComponent(anLabel,0,1,1,1,0.0,0.0);
		anAdressenFeld = new JTextField();
		addComponent(anAdressenFeld,1,1,1,1,1.0,0.0);

		adressbuchOeffnenAnKnopf = new JButton (new ImageIcon(FUMP.class.getResource("bilder/e-adrbook_50x30.GIF")));/* hier muss das AdressbuchIcon hin */
		adressbuchOeffnenAnKnopf.addActionListener(this);
		addComponent(adressbuchOeffnenAnKnopf,2,1,1,1,0.0,0.0);
		
		kopieLabel = new JLabel("Kopie:");
		addComponent(kopieLabel,0,2,1,1,0.0,0.0);
		kopieAdressenFeld = new JTextField();
		addComponent(kopieAdressenFeld,1,2,1,1,1.0,0.0);
		
		adressbuchOeffnenCCKnopf = new JButton (new ImageIcon(FUMP.class.getResource("bilder/e-adrbook_50x30.GIF")));/* hier muss das AdressbuchIcon hin */
		adressbuchOeffnenCCKnopf.addActionListener(this);
		addComponent(adressbuchOeffnenCCKnopf,2,2,1,1,0.0,0.0);
		
		blindKopieLabel = new JLabel("Blindkopie:");
		addComponent(blindKopieLabel,0,3,1,1,0.0,0.0);
		blindKopieAdressenFeld = new JTextField();
		addComponent(blindKopieAdressenFeld,1,3,1,1,1.0,0.0);
		
		adressbuchOeffnenBCCKnopf = new JButton(new ImageIcon(FUMP.class.getResource("bilder/e-adrbook_50x30.GIF")));/*hier muss das AdressbuchIcon hin */
		adressbuchOeffnenBCCKnopf.addActionListener(this);
		addComponent(adressbuchOeffnenBCCKnopf,2,3,1,1,0.0,0.0);
		
		betreffLabel = new JLabel("Betreff");
		addComponent(betreffLabel,0,4,1,1,0.0,0.0);
		betreffFeld = new JTextField();
		addComponent(betreffFeld,1,4,1,1,1.0,0.0);
		
		anhaengselLabel = new JLabel("Anhängsel:");
		addComponent(anhaengselLabel,0,5,1,1,0.0,0.0);
		anhaengselFeld = new JComboBox();
		addComponent(anhaengselFeld,1,5,1,1,1.0,0.0);
		
		anhaengselKnopf = new JButton(new ImageIcon(FUMP.class.getResource("bilder/e-attachment_50x30.GIF")));
		anhaengselKnopf.addActionListener(this);
		addComponent(anhaengselKnopf,2,5,1,1,0.0,0.0);
		
		abschickenKnopf = new JButton("Senden"/*,new ImageIcon("hier kommt der Bildname rein"*/);
		abschickenKnopf.addActionListener(this);
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(5,5,5,5);
			gbc.ipadx = 2; gbc.ipady = 2;
			gbc.fill = gbc.NONE;
			gbc.anchor = gbc.CENTER;
			gbc.gridx = 0; gbc.gridy=6;
			gbc.gridwidth = gbc.REMAINDER; gbc.gridheight = 1;
			gbc.weightx = 0.0; gbc.weighty = 0.0;
			gbl.setConstraints(abschickenKnopf,gbc);
			add(abschickenKnopf);
	}
	
	public SchreibPanel(NeueMail neueMail,KontoContainer kontoContainer,String adresse,String adressecc)
	{
		this(neueMail);
		this.kontoContainer=kontoContainer;
		anAdressenFeld.setText(adresse);
		kopieAdressenFeld.setText(adressecc);
		Vector adr = kontoContainer.gibAbsenderBitte();
		if (adr==null) System.out.println("leider null");
		else 
		{
			for (int i= 0;i<adr.size();i++)
			{
				absenderFeld.addItem((String) adr.elementAt(i));
			}
		}
	}
	
	public SchreibPanel(NeueMail neueMail,KontoContainer kontoContainer)
	{
		this(neueMail);
		this.kontoContainer=kontoContainer;
		Vector adr = kontoContainer.gibAbsenderBitte();
		if (adr==null) System.out.println("leider null");
		else 
		{
			for (int i= 0;i<adr.size();i++)
			{
				absenderFeld.addItem(adr.elementAt(i));
			}
		}
	}
	
	private void addComponent(Component c,int x,int y,int width,int height,double weightx,double weighty)
	{
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5,5,5,5);
		gbc.ipadx = 2; gbc.ipady = 2;
		gbc.fill = gbc.BOTH;
		gbc.gridx = x; gbc.gridy=y;
		gbc.gridwidth = width; gbc.gridheight = height;
		gbc.weightx = weightx; gbc.weighty = weighty;
		gbl.setConstraints(c,gbc);
		add(c);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == adressbuchOeffnenAnKnopf)
		{
			if (mailFenster.benutzer==null) System.out.println("benutzer: dev/null");
			Adressbuch adressbuch = mailFenster.benutzer.gibAdressbuch();
			if (adressbuch == null) System.out.println("nichts mit Adressbuch");
			adressbuch.show();
			if (anAdressenFeld.getText().equals("")) anAdressenFeld.setText(adressbuch.getAusgewaehlteAdressen());
			else anAdressenFeld.setText(anAdressenFeld.getText()+", "+adressbuch.getAusgewaehlteAdressen());
		}
		else if (e.getSource() == adressbuchOeffnenCCKnopf)
		{
			if (mailFenster.benutzer==null) System.out.println("benutzer: dev/null");
			Adressbuch adressbuch = mailFenster.benutzer.gibAdressbuch();
			if (adressbuch == null) System.out.println("nichts mit Adressbuch");
			adressbuch.show();
			if (kopieAdressenFeld.getText().equals("")) kopieAdressenFeld.setText(adressbuch.getAusgewaehlteAdressen());
			else kopieAdressenFeld.setText(kopieAdressenFeld.getText()+", "+adressbuch.getAusgewaehlteAdressen());
		}
		else if (e.getSource() == adressbuchOeffnenBCCKnopf)
		{
			if (mailFenster.benutzer==null) System.out.println("benutzer: dev/null");
			Adressbuch adressbuch = mailFenster.benutzer.gibAdressbuch();
			if (adressbuch == null) System.out.println("nichts mit Adressbuch");
			adressbuch.show();
			if (blindKopieAdressenFeld.getText().equals("")) blindKopieAdressenFeld.setText(adressbuch.getAusgewaehlteAdressen());
			else blindKopieAdressenFeld.setText(blindKopieAdressenFeld.getText()+", "+adressbuch.getAusgewaehlteAdressen());
		}
		else if (e.getSource() == abschickenKnopf)
		{
			if (!(anAdressenFeld.getText().equals("")))
			{
				mailFenster.fireNeueMailGeschriebenEvent((String)absenderFeld.getSelectedItem(),anAdressenFeld.getText(),kopieAdressenFeld.getText(),blindKopieAdressenFeld.getText(),(File[]) anhaengselDateien.toArray());
			}
			mailFenster.dispose();
		}
		else if (e.getSource() == anhaengselKnopf)
		{
			JFileChooser anhaengselDialog = new JFileChooser();
			anhaengselDialog.setMultiSelectionEnabled(true);
			int ergebnis = anhaengselDialog.showOpenDialog(mailFenster);
			if (ergebnis == JFileChooser.APPROVE_OPTION) 
			{
				File[] neueDateien = anhaengselDialog.getSelectedFiles();
				for (int i=0;i<neueDateien.length;i++)
				{
					anhaengselDateien.add(neueDateien[i]);
					anhaengselFeld.addItem(neueDateien[i].getName());
				}
			}
		}
	}	
}


