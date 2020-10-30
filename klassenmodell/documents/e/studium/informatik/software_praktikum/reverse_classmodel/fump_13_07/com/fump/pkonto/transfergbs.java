//gfhfg
package com.fump.pkonto;


/**
 * @author Marc Scherfenberg, Florian Brötzmann
 * @version $Id:
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.io.*;

public class TransferGBS extends JFrame{
	JLabel info,konto,ma;
	JProgressBar prog;

	public TransferGBS(){
		super("Transfer");
		setResizable(false);
		info = new JLabel("Ihre Mails werden abgerufen:");
		konto = new JLabel("Konto:");
		ma = new JLabel();

    prog = new JProgressBar(0, 1);
    prog.setValue(0);
    prog.setStringPainted(true);

		
		getContentPane().setLayout(new GridLayout(4,1));		
		getContentPane().add(info);
		getContentPane().add(konto);
		getContentPane().add(ma);
		getContentPane().add(prog);
		
		Dimension size=Toolkit.getDefaultToolkit().getScreenSize();
		move(
			(int)(size.width*0.5)-(int) (getSize().width*0.5),
			(int)(size.height*0.5)-(int) (getSize().height*0.5)
		);
		pack();
		show();
	}
}