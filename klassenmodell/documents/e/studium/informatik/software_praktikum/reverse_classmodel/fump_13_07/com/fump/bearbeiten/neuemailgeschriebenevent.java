package com.fump.bearbeiten;

/**
*  @author Tobias Becker (tbecker), Joerg Dieckmann (dieck)
*  @version $Id: NeueMailGeschriebenEvent.java,v 1.4 2001/06/13 16:29:00 tbecker Exp $
*
*
*/

import com.fump.bearbeiten.*;
import com.fump.pkonto.*;
import javax.swing.event.*;
import java.util.*;
import java.io.*;

public class NeueMailGeschriebenEvent extends EventObject
{
	private String absender;
	private String empfaengerTo;
	private String empfaengerCC;
	private String empfaengerBCC;
	private String betreff;
	private String mailText;
	private File[] anhaengsel;
   private KontoContainer kontoContainer;
			
	public NeueMailGeschriebenEvent(Object source, String absender, String empfaengerTo,
												String empfaengerCC, String empfaengerBCC, String mailText,
												File[] anhaengsel)
	{
		super(source);
		this.absender=absender;
		this.empfaengerTo=empfaengerTo;
		this.empfaengerCC=empfaengerCC;
		this.empfaengerBCC=empfaengerBCC;
		this.mailText=mailText;
		this.anhaengsel=anhaengsel;
	}

	public String getAbsender()
	{
		return absender;
	}
	
	public String getEmpfaengerTo()
	{
		return empfaengerTo;
	}
	
	public String getEmpfaengerCC()
	{
		return empfaengerCC;
	}
	
	public String getEmpaengerBCC()
	{
		return empfaengerBCC;
	}

	public String getBetreff()
	{
		return betreff;
	}
	
	public String getMailText()
	{
		return mailText;
	}
	
	public File[] getAnhaengsel()
	{
		return anhaengsel;
	}
	
	public String toString()
	{
		return "Es wurde eine neue Mail fertiggestellt";
	}
}
