package com.fump.bearbeiten;

/**
*  @author Tobias Becker (tbecker), Joerg Dieckmann (dieck)
*  @version $Id: LesePanel.java,v 1.7 2001/07/04 15:58:54 dieck Exp $
*
*
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import com.fump.bearbeiten.*;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

class LesePanel extends JPanel implements ActionListener
{
	private NeueMail mailFenster;

	private JLabel absenderLabel;
	private JTextField absenderFeld;
	private JLabel kopieLabel;
	private JTextField kopieFeld;
	private JLabel betreffLabel;
	private JTextField betreffFeld;
	private JLabel anhaengselLabel;
	private JComboBox anhaengselFeld;
	private JButton antwortenKnopf;
	private boolean anhaengsel=false;
	private JButton weiterschickenKnopf;
	
	private Vector attachmentVektor = new Vector();
	private Vector fileNameVektor = new Vector();

	private GridBagLayout gbl = new GridBagLayout();
	
	/**
	 * Konstruktor um eine Mail zu lesen
	 */
	public LesePanel(NeueMail neueMail,Message nachricht)
	{
		mailFenster = neueMail;
		setLayout(gbl);
		
		//Componenten initialisieren
		absenderLabel = new JLabel("Absender:");
		absenderFeld = new JTextField();
		absenderFeld.setEditable(false);
		
		kopieLabel = new JLabel("Kopien an:");
		kopieFeld = new JTextField();
		kopieFeld.setEditable(false);
		
		betreffLabel = new JLabel("Betreff:");
		betreffFeld = new JTextField();
		betreffFeld.setEditable(false);
		
		anhaengselLabel = new JLabel("Anhang:");

		//die Felder füllen
		nachrichtVerarbeiten(nachricht);
		anhaengselFeld = new JComboBox();
		anhaengselFeld.addActionListener(this);
		
		for (int i=0;i<fileNameVektor.size();i++)
		{
//			if (fileNameVektor.elementAt(i)!=null)
			{
				System.out.println("Vektor :"+(String) fileNameVektor.elementAt(i)+Integer.toString(i));
				anhaengselFeld.addItem((String) fileNameVektor.elementAt(i));
			}
		}

		
		//Componenten setzen
		addComponent(absenderLabel,0,0,1,1,0.0,0.0);
		addComponent(absenderFeld,1,0,1,1,1.0,0.0);
		addComponent(kopieLabel,0,1,1,1,0.0,0.0);
		addComponent(kopieFeld,1,1,1,1,1.0,0.0);
		addComponent(betreffLabel,0,2,1,1,0.0,0.0);
		addComponent(betreffFeld,1,2,1,1,1.0,0.0);
		System.out.println("komponenten gesetzt");

		try
		{
			// if abfrage für attachments
			if (nachricht.getContent() instanceof MimeMultipart)
			{
				addComponent(anhaengselLabel,0,3,1,1,0.0,0.0);
				addComponent(anhaengselFeld,1,3,1,1,1.0,0.0);
			}
		}catch(Exception e){System.out.println("Hoppla klappt" + e.toString());}
		
		antwortenKnopf = new JButton("Antworten"/*,new ImageIcon("hier kommt der Bildname rein"*/);
		antwortenKnopf.addActionListener(this);
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(5,5,5,5);
			gbc.ipadx = 2; gbc.ipady = 2;
			gbc.fill = gbc.NONE;
			gbc.anchor = gbc.CENTER;
			gbc.gridx = 0; gbc.gridy=6;
			gbc.gridwidth = gbc.REMAINDER; gbc.gridheight = 1;
			gbc.weightx = 0.0; gbc.weighty = 0.0;
			gbl.setConstraints(antwortenKnopf,gbc);
			add(antwortenKnopf);
			
	}
	
	private void partsVerarbeiten(MimeMultipart multi,int tiefe) throws Exception
	{
		  for (int partNummer=0; partNummer<multi.getCount();partNummer++)
		  {
			  if ((multi.getBodyPart(partNummer)).isMimeType("multipart/*"))
			  {	
				  partsVerarbeiten(((MimeMultipart) multi.getBodyPart(partNummer).getContent()),tiefe+1);
			  }
			  else if ((multi.getBodyPart(partNummer).isMimeType("text/plain")) && (!schonGezeigt))
			  {
//				  System.out.println("MultipartContenttype: Text/Plain");	
				  Object o = multi.getBodyPart(partNummer).getContent();
				  if (o instanceof String)
				  {
//						System.out.println("bin ich schon drin???"+ (String) o);
						mailFenster.textPane.setText((String) o);
						schonGezeigt = true;
		    	  }
			  }
			  else if ((multi.getBodyPart(partNummer).isMimeType("text/html")) && (!schonGezeigt))
			  {
//				  System.out.println("MultipartContentType: Text/Html");
				  Object o = multi.getBodyPart(partNummer).getContent();
				  if (o instanceof String)
				  {
						mailFenster.textPane.setContentType("text/html");
						mailFenster.textPane.setText((String) o);
						schonGezeigt = true;
//						System.out.println(mailFenster.textPane.getText()+(String) o);
				  }
			  }
			  else if ((multi.getBodyPart(partNummer).isMimeType("text/*")))
			  {
					String name = multi.getBodyPart(partNummer).getFileName();
					if (name == null) name = "Standard"+partNummer;
								
					System.out.println("filename : " + name);
			  		
					attachmentVektor.add(multi.getBodyPart(partNummer));
					fileNameVektor.add(name);
			  }
			  else 
			  {
					
					System.out.println("filename : "+multi.getBodyPart(partNummer).getFileName());
			  		
					attachmentVektor.add(multi.getBodyPart(partNummer));
					fileNameVektor.add(multi.getBodyPart(partNummer).getFileName());
				}
		  }	
		//}catch(Exception e){System.out.println("Exception in partsVerarbeiten: " +e.toString());
	}

	public boolean schonGezeigt = false;
	
	private void nachrichtVerarbeiten(Message nachricht)
	{
		try{
				try{
					absenderFeld.setText(wandelAdresse(nachricht.getFrom()));
				}catch(Exception e){;}
				try{
					kopieFeld.setText(wandelAdresse(nachricht.getRecipients(Message.RecipientType.CC)));
				}catch(Exception e){;}
				try{
					betreffFeld.setText(nachricht.getSubject());
				}catch(Exception e){;}

			if (nachricht.isMimeType("text/plain"))
			{
				System.out.println("Contenttype: Text/Plain");	
				Object o = nachricht.getContent();
				if (o instanceof String)
				{
					mailFenster.textPane.setText((String) o);
		    	}
			}
			else if(nachricht.isMimeType("text/html"))
			{
				System.out.println("ContentType: Text/Html");
				Object o = nachricht.getContent();
				if (o instanceof String)
				{
					mailFenster.textPane.setContentType("text/html");
					mailFenster.textPane.setText((String) o);
				}
			}
			else if(nachricht.isMimeType("multipart/*"))
			{
				System.out.println("ATTAAAAAAACHMENT");
				try{
					partsVerarbeiten((MimeMultipart) nachricht.getContent(),0);
				} catch (Exception e){System.out.println("Fehler in partsVerarbeiten: "+e.toString());}
			}
		}catch(Exception e) {System.out.println("erreur"+e.toString());}
		System.out.println("fertig");
	}
	
	private String wandelAdresse(Address[] adr)
	{
		String str = adr[0].toString();
		for(int i=1;i<adr.length;i++)
		{
			str+= ", " + adr[i].toString();
		}
		return str;
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
		if (e.getSource() == antwortenKnopf)
		{
			mailFenster.fireWillAntwortMailSchreibenEvent(absenderFeld.getText(),kopieFeld.getText());
		}
		else if (e.getSource() == anhaengselFeld)
		{
			Object anhaengsel = anhaengselFeld.getSelectedItem();
			int index = fileNameVektor.indexOf(anhaengsel);
			try
			{
				AnhaengselDialog a = new AnhaengselDialog(mailFenster,((MimeBodyPart) attachmentVektor.elementAt(index)).getDataHandler(),(String) anhaengsel);
			}catch(Exception x) {System.out.println("Scheiß casten");}
		}
			
	}

	
	/**	

		ContentTypes:
			text
			multipart
			application
			message
			image
			audio
			video
		
			
	
*/
	
}
