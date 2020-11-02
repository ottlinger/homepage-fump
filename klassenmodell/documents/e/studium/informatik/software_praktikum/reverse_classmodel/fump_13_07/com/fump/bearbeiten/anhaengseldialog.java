package com.fump.bearbeiten;

/**
*  @author Tobias Becker (tbecker), Joerg Dieckmann (dieck)
*  @version $Id: 
*
*
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import com.fump.*;
import com.fump.bearbeiten.*;
import java.io.*;
import javax.activation.*;

public class AnhaengselDialog
{
	private DataHandler dataHandler;
	private OutputStream outputStream;
	private NeueMail neueMail;
//	private JFrame neueMail;
	
	public AnhaengselDialog(NeueMail neueMail,DataHandler dataHandler,String dateiName)
	{
		this.dataHandler = dataHandler;
		try
		{
			outputStream = dataHandler.getOutputStream();
		}catch(Exception x) {System.out.println("lsmf");}
		this.neueMail = neueMail;
		String[] option = {"Ja","Nein"};
		int ergebnis1 = JOptionPane.showOptionDialog(neueMail,"Moechten Sie das Attachment \""+dateiName+"\" abspeichern?","Eine kurze Frage haette ich:",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,option,option[0]);
		System.out.println(ergebnis1 +" "+ JOptionPane.YES_OPTION);
		if (ergebnis1 == JOptionPane.YES_OPTION)
		{
			JFileChooser speichernDialog = new JFileChooser();
			speichernDialog.setSelectedFile(new File(dateiName));
			int ergebnis2 = speichernDialog.showSaveDialog(neueMail);
			System.out.println(ergebnis2 +" "+ JFileChooser.APPROVE_OPTION);
			if (ergebnis2 == JFileChooser.APPROVE_OPTION)
			{
				File datei = speichernDialog.getSelectedFile();
				//jetzt muﬂ gespeichert werden
			}
		}
	}
}
				
