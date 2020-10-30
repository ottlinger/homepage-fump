package com.fump.pkonto;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.io.*;

public class PassDialog extends JFrame{
	JLabel info;
	JButton but;
	JPasswordField pass;

	public PassDialog(String text,final Konto k){
		super("Passwortabfrage");
		setResizable(false);
		info = new JLabel(text);
		pass = new JPasswordField();
		but = new JButton("OK");
		but.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				k.uPass=pass.getText();
				dispose();
				k.mailAbrufen2();
			}
		}); 			
		
		getContentPane().setLayout(new GridLayout(3,1));		
		getContentPane().add(info);
		getContentPane().add(pass);
		getContentPane().add(but);
		
		Dimension size=Toolkit.getDefaultToolkit().getScreenSize();
		move(
			(int)(size.width*0.5)-(int) (getSize().width*0.5),
			(int)(size.height*0.5)-(int) (getSize().height*0.5)
		);
		pack();
		show();
	}
}