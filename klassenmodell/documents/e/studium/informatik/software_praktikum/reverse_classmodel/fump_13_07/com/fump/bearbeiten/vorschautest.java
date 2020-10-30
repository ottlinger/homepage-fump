package com.fump.bearbeiten;

/**
*  @author Tobias Becker (tbecker), Joerg Dieckmann (dieck)
*  @version $Id: VorschauTest.java,v 1.2 2001/06/13 16:29:00 tbecker Exp $
*
*
*/

import java.awt.*;
import java.util.*;
import javax.swing.*;

public class VorschauTest extends JFrame
{
	public VorschauTest(String args[])
	{
		if (args.length <= 2)
		{
			getContentPane().add(new VorschauPanel("to:hallo\n und so weiter","das war wohl mal wieder nix!!!!!!!",null));
		}
		else
		{
			Vector att = new Vector();
			for (int i=2;i<args.length;i++)
			{
				att.add(args[i]);
				System.out.println("scheiss attachments");
			}
			getContentPane().add(new VorschauPanel(args[0],args[1],att));
		}
		setSize(200,200);
		show();
	}
	
	
	public static void main(String args[])
	{
		new VorschauTest(args);
	}
}
