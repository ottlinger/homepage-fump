//fhdfhfd
package com.fump.pkonto;

import java.util.*;
import java.io.*;
import javax.mail.*;
import javax.mail.event.*;
import javax.activation.*;
import javax.mail.internet.*;

/**
 * @author Marc Scherfenberg, Florian Brötzmann
 * @version $Id: Konto.java,v 1.16 2001/07/11 18:35:36 ottlinge Exp $
 */
public class Konto {
	String kName, sName, sender, replyTo, pop, smtp, uName, uPass;
	boolean savePass, del;
  Properties props;
	KontoContainer kC;
	
  public Konto(String kName,String sName,String sender,String replyTo,String pop,String smtp,String uName,String uPass, boolean savePass,boolean del) {
  	
  	props = new Properties();
  	props.put("mail.smtp.host", smtp);
  	
  	this.kName=kName;
  	this.sName =sName ;
  	this.sender = sender;
  	this.replyTo= replyTo;
  	this.pop = pop;
  	this.smtp = smtp;
  	this.uName = uName;
  	this.uPass = uPass;
  	this.savePass = savePass;
	 	this.del = del;
  }

 	/**
  * @return void
  * @exception
  */
  
  public void mailSenden(String to, String subject, String text)
  {
		TransferGBS tF = new TransferGBS();
		tF.info.setText("Ihre Mails werden gesendet:");
		tF.konto.setText("Konto: " + kName);		
		Session session = Session.getDefaultInstance(props, null);
		//tF.ma.setText("Mail " + (i+1) + " von " + msgs.length);
		Message msg = new MimeMessage(session);
		try
		{
			msg.setFrom (new InternetAddress(sender));
			InternetAddress[] toAddress = InternetAddress.parse(to);
			msg.setRecipients(Message.RecipientType.TO, toAddress);
			msg.setSubject (subject);
			msg.setSentDate (new Date());
			msg.setContent (text, "text/plain");
			
			Transport.send(msg);
		}
		catch (MessagingException mex)
		{
			mex.printStackTrace();
			Exception ex;
			if ((ex=mex.getNextException()) != null) {
				ex.printStackTrace();
			}
		}
		tF.dispose();
	}

   /**
    * @return void
    * @exception
    */
	void mailAbrufen2(){
		try{
			Session session = Session.getDefaultInstance(props, null);
			session.setDebug(true);
		  Store store = session.getStore("pop3");
		  store.connect(pop, uName, uPass);
		  Folder folder = store.getDefaultFolder();
		  folder = folder.getFolder("INBOX");

		  // try to open read/write and if that fails try read-only
		  try {
		  	folder.open(Folder.READ_WRITE);
		  } catch (MessagingException ex) {
		  	folder.open(Folder.READ_ONLY);
		  }

		  int totalMessages = folder.getMessageCount();
		  if (totalMessages > 0) {
			  // int newMessages = folder.getNewMessageCount();
			  /*System.out.println("Total messages = " + totalMessages);
			  System.out.println("New messages = " + newMessages);
			  System.out.println("-------------------------------");
				*/
	
			  // Attributes & Flags for all messages ..
			  Message[] msgs = folder.getMessages();
			  
			  // Use a suitable FetchProfile
			  FetchProfile fp = new FetchProfile();
			  fp.add(FetchProfile.Item.ENVELOPE);
			  fp.add(FetchProfile.Item.CONTENT_INFO);
			  fp.add(FetchProfile.Item.FLAGS);
			  fp.add("X-Mailer");
			  folder.fetch(msgs, fp);
			  
		  	kC.tF.prog.setMaximum(msgs.length);	
			  for (int i = 0; i < msgs.length; i++) {
					kC.tF.ma.setText("Mail " + (i+1) + " von " + msgs.length);

				  //	System.out.println("--------------------------");
				  //	System.out.println("MESSAGE #" + (i + 1) + ":");
					//  dumpEnvelope(msgs[i]);
					//	new NeueMail(msgs[i]);
			  	msgs[i].setFlag(Flags.Flag.DELETED, del); 
			  	
			  	try {
			  		com.fump.BenutzerContainer.getBenutzer().gibFilterKontainer().ausfuehren(msgs[i]); //folder.getMessage(i)
			  	} catch (com.fump.JTreeLaeuftNichtRichtig e) {
			  		System.out.println("JTreeLaeuftNichtRichtig");
			  	}
			  	kC.tF.prog.setValue(i+1);	
			  }
			}
		  folder.close(del);
		  store.close();
		}	catch(Exception e){}
	}		
	
	public void mailAbrufen() {
		if (!savePass) {
			PassDialog d=new PassDialog("Bitte geben Sie das Passwort für das Konto ein:",this);
		} else 
			mailAbrufen2();
	}
}



