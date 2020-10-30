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



public class AdresseAendern extends JDialog implements ActionListener{
	JTextField namef, aliasf, adressef;
	JLabel namel, aliasl, adressel;
	int x;
	JPanel inhalt, buttons;
	JButton aendern, abbrechen;
	Adressbuch buch;
		
	public AdresseAendern(JFrame owner, String title, boolean modal, Adressbuch buch, int x){
		super(owner, "Eintrag ändern", modal);
		this.buch = buch;
		
		this.getContentPane().setLayout(new BorderLayout());
		
		this.x = x;
		namel = new JLabel("Name:");
		aliasl = new JLabel("Alias:");
		adressel = new JLabel("Adresse(n):");
		
		namef = new JTextField(((Eintrag)buch.eintraege.elementAt(x)).getName());
		aliasf = new JTextField(((Eintrag)buch.eintraege.elementAt(x)).getAlias());
		adressef = new JTextField(((Eintrag)buch.eintraege.elementAt(x)).getAString());
		
		inhalt = new JPanel(new GridLayout(3,2));
		inhalt.add(namel);
		inhalt.add(namef);
		inhalt.add(aliasl);
		inhalt.add(aliasf);
		inhalt.add(adressel);
		inhalt.add(adressef);
		this.getContentPane().add(inhalt, BorderLayout.CENTER);
		
		aendern = new JButton("ändern");
		aendern.addActionListener(this);
		abbrechen = new JButton ("abbrechen");
		abbrechen.addActionListener(this);
		
		buttons = new JPanel();
		buttons.add(aendern);
		buttons.add(abbrechen);
		
		this.getContentPane().add(buttons, BorderLayout.SOUTH);
		this.pack();
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e){
		Eintrag tmp = (Eintrag)buch.eintraege.elementAt(x);
		if(e.getSource()==aendern){
			tmp.loescheAdressen();
			tmp.setName(namef.getText());
			tmp.setAlias(aliasf.getText());
			String adresse = adressef.getText();
			StringTokenizer adr = new StringTokenizer(adresse);
			while(adr.hasMoreTokens()){
				String temp = adr.nextToken();
				if(temp!=";")tmp.addAdresse(temp);
			}
			DatenLeser.speichern(buch.eintraege,buch.pfad);
			/*for(int i = 0; i<buch.eintraege.size(); i++){
				System.out.println(((Eintrag)buch.eintraege.elementAt(i)).getEintragString());
			}
			System.out.println("AdresseAendern hat gespeichert");
			for(int i = 0; i<buch.eintraege.size(); i++){
				System.out.println(((Eintrag)buch.eintraege.elementAt(i)).getEintragString());
			}*/
			buch.table.addNotify();
			this.dispose();
		}//if
		if(e.getSource()==abbrechen){dispose();}//nix machen
	}
}
