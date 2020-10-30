package com.fump.bearbeiten;

import javax.mail.internet.*;
import javax.mail.*;
import com.fump.bearbeiten.*;
import javax.swing.*;
import java.awt.*;


public class LesenTest implements WillAntwortMailSchreibenListener
{
	public void antwortMailSchreiben(WillAntwortMailSchreibenEvent e)
	{
		new NeueMail(e);
	}		

	public void single()
	{
		try{
			Session session = Session.getInstance(new java.util.Properties());
			MimeMessage mimeMessage = new MimeMessage(session);
			mimeMessage.setFrom(new InternetAddress("meine.from@email.de"));
			mimeMessage.setRecipient(Message.RecipientType.TO,new InternetAddress("meine.to@email.de"));
			mimeMessage.setRecipient(Message.RecipientType.CC,new InternetAddress("meine.cc@email.de"));
			mimeMessage.setRecipient(Message.RecipientType.BCC,new InternetAddress("meine.bcc@email.de"));
			mimeMessage.setSubject("Mal testen, ne?!");
			mimeMessage.setSentDate(new java.util.Date());

			//Content-Text erstellen
			String content = "Dies ist mein Content!!\n nix für dich!!!!";
			//ab in die Message
			mimeMessage.setContent(content, "text/plain");
			mimeMessage.saveChanges();
			System.out.println(mimeMessage.getContentType());
			NeueMail n = new NeueMail(mimeMessage);
			n.addWillAntwortMailSchreibenListener(this);
		}catch(Exception e) {System.out.println("Hoppla");}
	}
	
	public void html()
	{
		try{
			Session session = Session.getInstance(new java.util.Properties());
			MimeMessage mimeMessage = new MimeMessage(session);
			mimeMessage.setFrom(new InternetAddress("meine.from@email.de"));
			mimeMessage.setRecipient(Message.RecipientType.TO,new InternetAddress("meine.to@email.de"));
			mimeMessage.setRecipient(Message.RecipientType.CC,new InternetAddress("meine.cc@email.de"));
			mimeMessage.setRecipient(Message.RecipientType.BCC,new InternetAddress("meine.bcc@email.de"));
			mimeMessage.setSubject("Mal testen, ne?!");
			mimeMessage.setSentDate(new java.util.Date());

			//Content-Text erstellen
			String content = "<html>\n<head>\n<title>Hallo</title>\n</head>\n<body>Das ist meine Html-Email\n</body>";
			
			//ab in die Message
			mimeMessage.setContent(content, "text/html");
			mimeMessage.saveChanges(); // save changes
			System.out.println(mimeMessage.getContentType());
			NeueMail n = new NeueMail(mimeMessage);
			n.addWillAntwortMailSchreibenListener(this);
		}catch(Exception e) {System.out.println("Hoppla");}
	}
	
	public void htmlvorschau()
	{
		try{
			Session session = Session.getInstance(new java.util.Properties());
			MimeMessage mimeMessage = new MimeMessage(session);
			mimeMessage.setFrom(new InternetAddress("meine.from@email.de"));
			mimeMessage.setRecipient(Message.RecipientType.TO,new InternetAddress("meine.to@email.de"));
			mimeMessage.setRecipient(Message.RecipientType.CC,new InternetAddress("meine.cc@email.de"));
			mimeMessage.setRecipient(Message.RecipientType.BCC,new InternetAddress("meine.bcc@email.de"));
			mimeMessage.setSubject("Mal testen, ne?!");
			mimeMessage.setSentDate(new java.util.Date());

			//Content-Text erstellen
			String content = "<html>\n<head>\n<title>Hallo</title>\n</head>\n<body>Das ist meine Html-Email\n</body></html>";
			
			//ab in die Message
			mimeMessage.setContent(content, "text/html");
			mimeMessage.saveChanges(); // save changes
			System.out.println(mimeMessage.getContentType());
			VorschauPanel v = new VorschauPanel(mimeMessage);
			if (v==null) System.out.println("Scheiß Constructor");
			JFrame j = new JFrame("VorschauPanel");
			if (j==null) System.out.println("Scheiß Constructor");
//			j.setLayout(new GridLayout());
			j.getContentPane().add(v);
			j.setSize(300,300);
			j.show();
		}catch(Exception e) {System.out.println("Hoppla "+e.toString());}
	}
	
	public void multi()
	{
		try{
			Session session = Session.getInstance(new java.util.Properties());
			MimeMessage mimeMessage = new MimeMessage(session);
			mimeMessage.setFrom(new InternetAddress("meine.from@email.de"));
			mimeMessage.setRecipient(Message.RecipientType.TO,new InternetAddress("meine.to@email.de"));
			mimeMessage.setRecipient(Message.RecipientType.CC,new InternetAddress("meine.cc@email.de"));
			mimeMessage.setRecipient(Message.RecipientType.BCC,new InternetAddress("meine.bcc@email.de"));
			mimeMessage.setSubject("Mal testen, ne?!");
			mimeMessage.setSentDate(new java.util.Date());
		
			MimeMultipart mp = new MimeMultipart();
			// create the first bodypart object
			MimeBodyPart b1 = new MimeBodyPart();
			// create textual content
			// and add it to the bodypart object
			b1.setContent("Spaceport Map","text/plain");
			mp.addBodyPart(b1);
			// Multipart messages usually have more than
			// one body part. Create a second body part
			// object, add new text to it, and place it
			// into the multipart message as well. This
			// second object holds postscript data.
			MimeBodyPart b2 = new MimeBodyPart(); 
			b2.setContent("hier muss aber eigentlich ein anderes <object> rein","application/postscript");
			b2.setFileName("meine Datei");
			mp.addBodyPart(b2);
			// Set message attrubutes as in a singlepart
			// message.
			MimeBodyPart b3 = new MimeBodyPart();
			b3.setContent("anderes object","image/jpeg");
			b3.setFileName("bilddatei??");
			mp.addBodyPart(b3);
			mimeMessage.setContent(mp); // add Multipart
			mimeMessage.saveChanges(); // save changes
			System.out.println(mimeMessage.getContentType());
			NeueMail n = new NeueMail(mimeMessage);
			n.addWillAntwortMailSchreibenListener(this);
		}catch(Exception e) {System.out.println("Hoppla");}
	}
	
	public static void main(String args[])
	{
//		LesenTest singlepart = new LesenTest();
//		singlepart.single();
//		LesenTest multipart = new LesenTest();
//		multipart.multi();
//		LesenTest htmlpart1 = new LesenTest();
//		htmlpart1.html();
		LesenTest htmlpart2 = new LesenTest();
		htmlpart2.htmlvorschau();
	}	
}