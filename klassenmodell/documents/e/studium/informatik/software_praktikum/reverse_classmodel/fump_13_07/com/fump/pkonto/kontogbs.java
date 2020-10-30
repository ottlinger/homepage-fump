package com.fump.pkonto;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.io.*;

public class KontoGBS extends JFrame{
	KontoContainer kC;
	boolean geaendert;
	JComboBox kontoAuswahl;
	JTextField kontoName,sName,sender,replyTo,pop,smtp,user;
	JCheckBox pswSpeichern,del;
	JPasswordField pass;
	JDialog KontoLoeschenBest,KontoEx;

	public KontoGBS(KontoContainer kC){
		super("Konto-Daten");
		setResizable(false);
		this.kC=kC;
		geaendert=false;

		// Layout setzen
		kontoAuswahl = new JComboBox(kC.gibAbsenderBitte());
		kontoAuswahl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				felderSetzen();
			}
		}); 		
		JButton KontoLoeschen = new JButton("Konto löschen");
		KontoLoeschen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				kontoLoeschenPerformed();
			}
		}); 			
		JButton Schliessen = new JButton("Zurück");
		Schliessen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		}); 			
		JButton neuesKonto = new JButton("Konto hinzufügen");
		neuesKonto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				neuesKontoPerformed();
			}
		}); 
		JButton KontoAendern = new JButton("Konto ändern");
		KontoAendern.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent g) {
				kontoAendernPerformed();
			}
		}); 
		JButton FelderLoeschen = new JButton("Felder Löschen");
		FelderLoeschen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent g) {
				felderLoeschen();
			}
		}); 		

		getContentPane().setLayout(new BorderLayout());		
		
		JPanel oben = new JPanel(new FlowLayout(FlowLayout.LEFT));
		oben.add(new JLabel("Konto:"));
		oben.add(kontoAuswahl);
		oben.add(KontoAendern);
		oben.add(neuesKonto);	
		
		JPanel unten = new JPanel(new GridLayout(1,3));
		unten.add(FelderLoeschen);
		unten.add(KontoLoeschen);
		unten.add(Schliessen);

		getContentPane().add(oben, BorderLayout.NORTH );
		getContentPane().add(unten, BorderLayout.SOUTH );

 		// Parameter-Bereich
 		JPanel param = new JPanel(new GridLayout(10,2));
 		param.add(new JLabel("Konto-Name:"));
 		kontoName=new JTextField();
 		param.add(kontoName);
 		param.add(new JLabel("Absendername"));
 		sName=new JTextField();
 		param.add(sName);
 		param.add(new JLabel("Absenderadresse"));
 		sender=new JTextField();
 		param.add(sender);
 		param.add(new JLabel("Antwortadresse"));
 		replyTo=new JTextField();
 		param.add(replyTo);
 		param.add(new JLabel("pop-Server"));
 		pop=new JTextField();
 		param.add(pop);
 		param.add(new JLabel("smtp-Server"));
 		smtp=new JTextField();
 		param.add(smtp);
 		param.add(new JLabel("user-name"));
 		user=new JTextField();
 		param.add(user);
 		param.add(new JLabel("Passwort speichern"));
		pswSpeichern = new JCheckBox();
		pswSpeichern.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (pswSpeichern.isSelected()) 
					pass.show();
				else  {
					pass.setText("");
					pass.hide();
					repaint();
				}	
			}
		});
 		param.add(pswSpeichern);
 		param.add(new JLabel("Passwort"));
 		pass = new JPasswordField( 15 );
		pass.setEchoChar('*');
		param.add(pass);
		param.add(new JLabel("e-mails vom Server löschen"));
		del = new JCheckBox();
		param.add(del);
		getContentPane().add(param, BorderLayout.CENTER);
		getContentPane().add(new JLabel("     "), BorderLayout.WEST);

		felderSetzen();		
		pack();
		Dimension size=Toolkit.getDefaultToolkit().getScreenSize();
		
		move(
			(int)(size.width*0.5)-(int) (getSize().width*0.5),
			
			(int)(size.height*0.5)-(int) (getSize().height*0.5)
		);
		show();
	}
	
	void felderSetzen() {
		try {
 			Konto konto=kC.gibKonto((String) kontoAuswahl.getSelectedItem());
 			kontoName.setText((String) kontoAuswahl.getSelectedItem());
 			sName.setText(konto.sName);
 			sender.setText(konto.sender);
 			replyTo.setText(konto.replyTo);	
 			pop.setText(konto.pop);
 			smtp.setText(konto.smtp);
 			user.setText(konto.uName);
 			pass.setText(konto.uPass); 	
 			pswSpeichern.setSelected(konto.savePass);
 			if (pswSpeichern.isSelected()) 
						pass.show();
					else  {
						pass.setText("");
						pass.hide();
						repaint();
					}
			del.setSelected(konto.del);
		} catch (KeinSolchesKontoFehler f) {
			felderLoeschen();
		};
	}
	
	void felderLoeschen() {
		kontoName.setText("");
		sName.setText("");
		sender.setText("");
		replyTo.setText("");
		pop.setText("");			
		smtp.setText("");			
		user.setText("");			
		pass.setText("");			
		pswSpeichern.setSelected(false);
		pass.hide();
		del.setSelected(false);
		repaint();
	}
	
	void kontoLoeschenPerformed() {
		KontoLoeschenBest = new JDialog(this,"Konto löschen",true);
		KontoLoeschenBest.getContentPane().add(new JLabel(" Wollen Sie das Konto '"+kontoAuswahl.getSelectedItem()+"' wirklich löschen? "), BorderLayout.NORTH);
		JPanel buttons=new JPanel(new FlowLayout());
		JButton Loeschen=new JButton("Konto löschen");
		Loeschen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					kC.KontoLoeschen((String) kontoAuswahl.getSelectedItem());
				} catch (KeinSolchesKontoFehler f) {}
				kontoAuswahl.removeItem(kontoAuswahl.getSelectedItem());
				KontoLoeschenBest.hide();
				felderSetzen();
				pack();
				repaint();
				kC.speichern();
			}
		}); 
		JButton Abbrechen=new JButton("Abbrechen");
		Abbrechen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				KontoLoeschenBest.dispose();
			}
		}); 
		buttons.add(Loeschen);
		buttons.add(Abbrechen);
		KontoLoeschenBest.getContentPane().add(buttons, BorderLayout.CENTER);
		KontoLoeschenBest.pack();
		KontoLoeschenBest.setResizable(false);

		Dimension size=Toolkit.getDefaultToolkit().getScreenSize();		
		KontoLoeschenBest.move(
			(int)(size.width*0.5)-(int) (KontoLoeschenBest.getSize().width*0.5),
			(int)(size.height*0.5)-(int) (KontoLoeschenBest.getSize().height*0.5)
		);
		KontoLoeschenBest.show();
	}
	
	void kontoAendernPerformed() {
marke:
		try {
			Konto k =	kC.gibKonto((String) kontoAuswahl.getSelectedItem());
			k.sName = sName.getText();
			k.sender = sender.getText();
			k.replyTo = replyTo.getText();
			k.pop = pop.getText();
			k.smtp = smtp.getText();
			k.uName = user.getText();
			k.uPass = pass.getText();
			k.savePass = pswSpeichern.isSelected();
			k.del = del.isSelected();
			if (!k.kName.equals(kontoName.getText())) {
				for (int i=0; i<kC.konten.size();i++){
		  		if (((Konto) kC.konten.elementAt(i)).kName.equals(kontoName.getText())) {						
						kontoExistiertDialog();			  								
		  			kontoName.setText(k.kName);
		  			break marke;
		  		}
		  	}
				String temp = kontoName.getText();
				kontoAuswahl.removeItem(k.kName);
				k.kName = temp;
				kontoAuswahl.addItem(temp);
				kontoAuswahl.setSelectedItem(temp);
				pack();
			}
		} catch (KeinSolchesKontoFehler f) {};
		kC.speichern();
	}

	void neuesKontoPerformed() {
		String nkName=new String(kontoName.getText());
		try {
			kC.neuesKonto(nkName,sName.getText(),sender.getText(),replyTo.getText(),pop.getText(),smtp.getText(),user.getText(),pass.getText(),pswSpeichern.isSelected(),del.isSelected());
			kontoAuswahl.addItem(nkName);
			kontoAuswahl.setSelectedItem(nkName);
			pack();
			kC.speichern();
		} catch (KontoExistiertSchonFehler f) {
			kontoExistiertDialog();
		}
	}
	
	void kontoExistiertDialog() {
		KontoEx = new JDialog(this,"Konto existiert bereits",true);
		KontoEx.getContentPane().add(new JLabel(" Das Konto '"+kontoName.getText()+"' existiert bereits! "), BorderLayout.NORTH);
		JPanel buttonpanel=new JPanel(new FlowLayout());
		JButton zurueck=new JButton("Zurück");
		zurueck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				KontoEx.dispose();
			}
		});
		buttonpanel.add(zurueck);
		KontoEx.getContentPane().add(buttonpanel, BorderLayout.CENTER);
		KontoEx.setResizable(false);
		KontoEx.pack();
		
		Dimension size=Toolkit.getDefaultToolkit().getScreenSize();		
		KontoEx.move(
			(int)(size.width*0.5)-(int) (KontoEx.getSize().width*0.5),
			(int)(size.height*0.5)-(int) (KontoEx.getSize().height*0.5)
		);
		KontoEx.show(); 				
	}
}