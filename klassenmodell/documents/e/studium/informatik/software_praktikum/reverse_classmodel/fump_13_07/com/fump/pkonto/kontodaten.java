package com.fump.pkonto;

import java.io.*;

class KontoDaten implements Serializable {
	String kName, sName, sender, replyTo, pop, smtp, uName, uPass;
	boolean savePass, del;
	
	KontoDaten(String kName,String sName,String sender,String replyTo,String pop,String smtp,String uName,String uPass, boolean savePass,boolean del) {
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
}