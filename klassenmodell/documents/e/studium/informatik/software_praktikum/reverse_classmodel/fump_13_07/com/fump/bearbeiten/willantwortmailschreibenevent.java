package com.fump.bearbeiten;

/**
*  @author Tobias Becker (tbecker), Joerg Dieckmann (dieck)
*  @version $Id: WillAntwortMailSchreibenEvent.java,v 1.2 2001/06/27 16:28:06 tbecker Exp $
*
*
*/

import com.fump.bearbeiten.*;
import javax.swing.event.*;
import java.util.*;
import java.io.*;

public class WillAntwortMailSchreibenEvent extends EventObject
{
	private String empfaengerTo;
	private String empfaengerCC;
	private String mailText;
		
	public WillAntwortMailSchreibenEvent(Object source, String empfaengerTo,
	String empfaengerCC, String mailText)
	{
		super(source);
		this.empfaengerTo=empfaengerTo;
		this.empfaengerCC=empfaengerCC;
		this.mailText=mailText;
	}

	public String getEmpfaengerTo()
	{
		return empfaengerTo;
	}
	
	public String getEmpfaengerCC()
	{
		return empfaengerCC;
	}
	
	public String getMailText()
	{
		return mailText;
	}
	
	public String toString()
	{
		return "Es soll eine neue AntwortMail geschreiben werden";
	}
}
