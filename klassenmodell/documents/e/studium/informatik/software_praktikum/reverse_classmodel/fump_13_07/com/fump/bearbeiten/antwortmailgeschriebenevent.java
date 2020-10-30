package com.fump.bearbeiten;

/**
*  @author Tobias Becker (tbecker), Joerg Dieckmann (dieck)
*  @version $Id: AntwortMailGeschriebenEvent.java,v 1.4 2001/06/13 16:28:59 tbecker Exp $
*
*
*/

import com.fump.bearbeiten.*;
import javax.swing.event.*;
import java.util.*;
import java.io.*;

public class AntwortMailGeschriebenEvent extends EventObject
{
	private String absender;
	private String empfaengerCC;
	private String mailText;
		
	public AntwortMailGeschriebenEvent(Object source, String absender, String empfaengerCC, String mailText)
	{
		super(source);
		this.absender=absender;
		this.empfaengerCC=empfaengerCC;
		this.mailText=mailText;
	}

	public String getAbsender()
	{
		return absender;
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
		return "Es soll eine Antwortmail geschrieben werden";
	}
}
