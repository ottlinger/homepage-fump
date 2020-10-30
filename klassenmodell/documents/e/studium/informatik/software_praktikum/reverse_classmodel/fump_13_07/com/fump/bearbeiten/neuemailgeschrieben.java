package com.fump.bearbeiten;

import com.fump.bearbeiten.*;
import java.io.*;

class NeueMailGeschrieben extends Exception
{
	private String absender;
	private String empfaengerTo;
	private String empfaengerCC;
	private String empfaengerBCC;
	private String mailText;
	private File[] anhaengsel;
		
	public NeueMailGeschrieben(String absender, String empfaengerTo,
				String empfaengerCC,String empfaengerBCC,String mailText,File[] anhaengsel)
	{
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
	
	public String getMailText()
	{
		return mailText;
	}
	
	public File[] getAnhaengsel()
	{
		return anhaengsel;
	}
}
