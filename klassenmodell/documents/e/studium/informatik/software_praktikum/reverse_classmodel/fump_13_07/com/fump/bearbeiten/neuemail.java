package com.fump.bearbeiten;

/**
*  @author Tobias Becker (tbecker), Joerg Dieckmann (dieck)
*  @version $Id: NeueMail.java,v 1.9 2001/07/11 16:39:45 tbecker Exp $
*
*
*/

import com.fump.bearbeiten.*;
import com.fump.pkonto.*;
import com.fump.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.io.*;
import javax.mail.*;
import javax.mail.internet.*;

public class NeueMail extends JFrame //implements WindowListener
{
	/**
	 * Ein neuer MailGeschriebenListener soll implementiert werden
	 */
	private EventListenerList listenerList = new EventListenerList();
	private NeueMailGeschriebenEvent neueMailEvent = null;
	private WillAntwortMailSchreibenEvent antwortMailEvent = null;
	
	/**
	 * Die neuen Listener anmelden
	 */
	public void addNeueMailGeschriebenListener(NeueMailGeschriebenListener l)
	{
		listenerList.add(NeueMailGeschriebenListener.class,l);
	}
	
	public void addWillAntwortMailSchreibenListener(WillAntwortMailSchreibenListener l)
	{
		listenerList.add(WillAntwortMailSchreibenListener.class,l);
	}
	
	/**
	 * Die Listener wieder entfernen
	 */
	public void removeNeueMailGeschriebenListener(NeueMailGeschriebenListener l)
	{
		listenerList.remove(NeueMailGeschriebenListener.class,l);
	}
	
	public void removeWillAntwortMailSchreibenListener(NeueMailGeschriebenListener l)
	{
		listenerList.remove(WillAntwortMailSchreibenListener.class,l);
	}

	/**
	 * Abschicken eines NeueMailGeschriebenEvents
	 */
	protected void fireNeueMailGeschriebenEvent(String absender,String anAdressen, 
							String kopieAdressen,String blindKopieAdressen, File[] anhaengsel)
	{
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length-2;i>=0;i-=2)
		{
			if (listeners[i] == NeueMailGeschriebenListener.class)
			{
				if (neueMailEvent == null)
				{
					neueMailEvent = new NeueMailGeschriebenEvent(this,absender,anAdressen,kopieAdressen,blindKopieAdressen,textPane.getText(),anhaengsel);
				}
				((NeueMailGeschriebenListener)listeners[i+1]).geschriebeneMailAbholen(neueMailEvent);
			}
		}
	}
	
	/**
	 * Abschicken eines WillAntwortMailSchreibenEvents
	 */
	protected void fireWillAntwortMailSchreibenEvent(String anAdressen,String kopieAdressen)
	{
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length-2;i>=0;i-=2)
		{
			if (listeners[i] == WillAntwortMailSchreibenListener.class)
			{
				if (antwortMailEvent == null)
				{
					antwortMailEvent = new WillAntwortMailSchreibenEvent(this,anAdressen,kopieAdressen,textPane.getText());
				}
				((WillAntwortMailSchreibenListener)listeners[i+1]).antwortMailSchreiben(antwortMailEvent);
			}
		}
	}
	
		
	private BorderLayout bl = new BorderLayout();
	public JEditorPane textPane = new JEditorPane();
	public Benutzer benutzer;

	/**
	 *Konstruktor um eine Mail zu schreiben 
	 */
	public NeueMail(Benutzer benutzer/*KontoContainer kontoContainer*/)
	{
		super("Neue Mail schreiben");
		this.benutzer=benutzer;
		
		// Layout setzen
		getContentPane().setLayout(bl);
		
		// SchreibPanel hinzufügen
		SchreibPanel schreibPanel = new SchreibPanel(this,benutzer.gibKontoKontainer()/*,kontoContainer*/);
		getContentPane().add(schreibPanel,BorderLayout.NORTH);

		//Die Textfläche initialisieren und hinzufügen
		getContentPane().add(new JScrollPane(textPane),BorderLayout.CENTER);
		
		//Fenster sichtbar machen
		setSize(500,500);
		show();
	}
		
	public NeueMail(Benutzer benutzer,/*KontoContainer kontoContainer,*/WillAntwortMailSchreibenEvent e)
	{
		super("Sie wollen doch wohl nicht etwa eine Antwort schreiben???");
		
		// Layout setzen
		this.benutzer=benutzer;
		getContentPane().setLayout(bl);
		
		// SchreibPanel hinzufügen
		SchreibPanel schreibPanel = new SchreibPanel(this,benutzer.gibKontoKontainer(),/*kontoContainer,*/e.getEmpfaengerTo(), e.getEmpfaengerCC());
		getContentPane().add(schreibPanel,BorderLayout.NORTH);

		//Die Textfläche initialisieren und hinzufügen
//		System.out.println(mailText);
//		System.out.println(antwortTextEinruecken(mailText));
		textPane.setText(antwortTextEinruecken(e.getMailText()));
		getContentPane().add(new JScrollPane(textPane),BorderLayout.CENTER);
		
		//Fenster sichtbar machen
		setSize(500,500);
		show();
	}

	public NeueMail(Message nachricht)
	{
		super("Mail lesen");
		this.benutzer=benutzer;
		
		// Layout setzen
		getContentPane().setLayout(bl);
		
		// LesePanel hinzufügen
		LesePanel lesePanel = new LesePanel(this, nachricht);
		getContentPane().add(lesePanel,BorderLayout.NORTH);
		System.out.println("lesepanel dazu");

		//Die Textfläche initialisieren und hinzufügen
		textPane.setEditable(false);
		getContentPane().add(new JScrollPane(textPane),BorderLayout.CENTER);
		
		//Fenster sichtbar machen
		setSize(500,500);
		show();
	}
	
	private String antwortTextEinruecken(String text)
	{
		int vorn=0;
		int hinten=0;
		String retur="\n";
		while((vorn=text.indexOf('\n',hinten))>=0)
		{
			retur+="> ";
			retur+=(text.substring(hinten,vorn-hinten));
			hinten = vorn+1;
		}
		return retur;
	}


	// implements WindowListener
	public void windowActivated(WindowEvent e)
	{;}
	
	// implements WindowListener
	public void windowClosed(WindowEvent e)
	{
		dispose();
	}

	// implements WindowListener
	public void windowClosing(WindowEvent e)
	{;}
	
	// implements WindowListener
	public void windowDeactivated(WindowEvent e)
	{;}
	
	// implements WindowListener
	public void windowDeiconified(WindowEvent e)
	{;}
	
	// implements WindowListener
	public void windowIconified(WindowEvent e)
	{;}
	
	// implements WindowListener
	public void windowOpened(WindowEvent e)
	{;}
}

//wenn hilfe erforderlich
//FUMP.hilfe.kontext_hilfe("NeueMail");
