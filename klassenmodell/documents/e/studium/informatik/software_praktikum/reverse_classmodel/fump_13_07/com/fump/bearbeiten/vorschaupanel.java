package com.fump.bearbeiten;

/**
*  @author Tobias Becker (tbecker), Joerg Dieckmann (dieck)
*  @version $Id: VorschauPanel.java,v 1.4 2001/06/27 14:37:55 tbecker Exp $
*
*
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class VorschauPanel extends JPanel implements ItemListener
{

	BorderLayout bl;
	JComboBox attachmentsBox;
	JTextPane textPane;
	Vector attachmentVektor;
	Vector fileNameVektor;
	String adressen="";
	String htmladressen="";
	
	public VorschauPanel(Message nachricht)
	{
		textPane = new JTextPane();
		nachrichtVerarbeiten(nachricht);
		bl = new BorderLayout();
		setLayout(bl);
		if (fileNameVektor!=null)
			for (int i=0;i<fileNameVektor.size();i++)
			{
					attachmentsBox.addItem((String) fileNameVektor.elementAt(i));
			}
		try{
			if (nachricht.getContent() instanceof MimeMultipart) add(attachmentsBox,bl.NORTH);
		}catch(Exception x) {System.out.println("casten von MimeMultipart für Attachments nicht geklappt");}
		
		JScrollPane scr = new JScrollPane(textPane);
		add(scr,bl.CENTER);
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
						adressen += "\n\n\n"+((String) o);
						textPane.setText(adressen);
						schonGezeigt = true;
		    	  }
			  }
			  else if ((multi.getBodyPart(partNummer).isMimeType("text/html")) && (!schonGezeigt))
			  {
//				  System.out.println("MultipartContentType: Text/Html");
				  Object o = multi.getBodyPart(partNummer).getContent();
				  if (o instanceof String)
				  {
						textPane.setContentType("text/html");
						String mailText = (String) o;
						int stelle = (mailText.toUpperCase()).indexOf("<BODY");
						stelle+=5;
						stelle = mailText.indexOf('>',stelle);
						String temp = mailText.substring(0,stelle);
						temp+=htmladressen+"<HR><br>";
						temp+=mailText.substring(stelle+1);
						textPane.setText(temp);
						schonGezeigt = true;
//						System.out.println(mailFenster.textPane.getText()+(String) o);
				  }
			  }
			  else if ((multi.getBodyPart(partNummer).isMimeType("text/*")))
			  {
					String name = multi.getBodyPart(partNummer).getFileName();
					if (name == null) name = "Standard";
								
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
					adressen+="Von: "+wandelAdresse(nachricht.getFrom());
					htmladressen+="Von: "+wandelAdresse(nachricht.getFrom())+"<Br>";
				}catch(Exception e){System.out.println("VorschauPanel Z. 115:"+e.toString());}
				try{
					adressen+="\nKopie(n): "+wandelAdresse(nachricht.getRecipients(Message.RecipientType.CC));
					htmladressen+="Kopie(n): "+wandelAdresse(nachricht.getRecipients(Message.RecipientType.CC))+"<BR>";
				}catch(Exception e){System.out.println("VorschauPanel Z. 118:"+e.toString());}
				try{
					adressen+="\nBetreff: "+nachricht.getSubject();
					htmladressen+="Betreff: "+nachricht.getSubject();
				}catch(Exception e){System.out.println("VorschauPanel Z. 121:"+e.toString());}

			if (nachricht.isMimeType("text/plain"))
			{
				System.out.println("Contenttype: Text/Plain");	
				Object o = nachricht.getContent();
				if (o instanceof String)
				{
					adressen += "\n\n\n"+((String) o);
					textPane.setText(adressen);
		    	}
			}
			else if(nachricht.isMimeType("text/html"))
			{
				System.out.println("ContentType: Text/Html");
				Object o = nachricht.getContent();
				if (o instanceof String)
				{
					try{
						String mailText = (String) o;
						int stelle = (mailText.toUpperCase()).indexOf("<BODY");
//						System.out.println("Erstes Vorkommen von <body> bei" + stelle);
						stelle+=5;
						stelle = mailText.indexOf('>',stelle);
//						System.out.println("<body> zuende bei" + stelle);
						String temp = mailText.substring(0,stelle+1);
//						System.out.println("Bis Body: " +temp);
						temp+=htmladressen+"<HR> <br>";
//						System.out.println("+ adressen und <br>'s: " +temp);
//						System.out.println("Rest: "+mailText.substring(stelle+1));
						temp+=mailText.substring(stelle+1);
//						System.out.println("Alles: " +temp);
						if (textPane == null) System.out.println("blödes Teil");
						textPane.setContentType("text/html");
						textPane.setText(temp);
					}
					catch(Exception f){System.out.println("VorschauPanel Z. 150:"+f.toString());}
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
	
	public void itemStateChanged(ItemEvent e)
	{
		//mit Object getItem() arbeiten 
	}
	
}
