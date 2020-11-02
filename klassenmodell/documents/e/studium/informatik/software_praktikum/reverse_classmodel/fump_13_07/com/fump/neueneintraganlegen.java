package com.fump;
import java.util.*;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import java.awt.Point;
import java.io.*;

public class NeuenEintragAnlegen extends JDialog implements ActionListener{
	Adressbuch buch;
	JLabel namel, aliasl, adressel;
	JTextField namef , aliasf, adressef;
	boolean gruppe;
	JPanel eingabe, buttonleiste;
	JButton ok, abbrechen;
	
	public NeuenEintragAnlegen(JFrame owner,boolean modal, Adressbuch book, boolean group, String title){
		super(owner,title, modal);
		buch = book;
		gruppe = group;
		init();
	}
	
	public void init(){
		if(gruppe){
			namel = new JLabel ("Bitte geben Sie hier den Namen der Gruppe ein:");
			aliasl = new JLabel ("Bitte geben Sie hier das Alias der Gruppe ein:");
			adressel = new JLabel ("Bitte geben Sie hier die Adressen der Gruppe ein:");
		}
		else{
			namel = new JLabel ("Bitte geben Sie hier den Namen ein:");
			aliasl = new JLabel ("Bitte geben Sie hier das Alias ein:");
			adressel = new JLabel ("Bitte geben Sie hier die Adresse(n) ein:");
		}
		
		namef = new JTextField();
		aliasf = new JTextField();
		adressef= new JTextField();
		eingabe = new JPanel(new GridLayout(3,2));
		eingabe.add(namel);
		eingabe.add(namef);
		eingabe.add(aliasl);
		eingabe.add(aliasf);
		eingabe.add(adressel);
		eingabe.add(adressef);
		
		this.getContentPane().add(eingabe, BorderLayout.CENTER);
		ok = new JButton ("ok");
		ok.addActionListener(this);
		abbrechen = new JButton ("abbrechen");
		abbrechen.addActionListener(this);
		buttonleiste = new JPanel();
		buttonleiste.add(ok);
		buttonleiste.add(abbrechen);
		this.getContentPane().add(buttonleiste, BorderLayout.SOUTH);
		this.pack();
		this.setVisible(true);
		this.repaint();
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==ok){
			Eintrag neu = new Eintrag(this.gruppe);
			neu.setName(namef.getText());
			neu.setAlias(aliasf.getText());
			String adresse = adressef.getText();
			StringTokenizer adr = new StringTokenizer(adresse);
			while(adr.hasMoreTokens()){neu.addAdresse(adr.nextToken());}
			buch.addNeuenEintrag(neu);
			DatenLeser.speichern(buch.eintraege,buch.pfad);
			buch.table.addNotify();
			/*System.out.println("NeuenEintragAnlegen speichert");
			for(int i = 0; i<buch.eintraege.size(); i++){
				System.out.println(((Eintrag)buch.eintraege.elementAt(i)).getEintragString());
			}*/
			dispose();
		}
		if(e.getSource()==abbrechen){
			dispose();
		}
	}
}
