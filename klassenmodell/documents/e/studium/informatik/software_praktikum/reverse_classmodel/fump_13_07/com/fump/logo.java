package com.fump;

import java.awt.*;
import javax.swing.*;

public class Logo extends Canvas{

	Image logo;
	JWindow window; 

	public Logo() {

		logo =
Toolkit.getDefaultToolkit().createImage(FUMP.class.getResource("bilder/fump-logo3.jpg"));
		prepareImage(logo, this);

		window = new JWindow();
		window.getContentPane().add(this);
		window.setSize(250,140);
	}
	
	public void paint(Graphics g) {
		g.drawImage(logo,0,0,this);
	}

	public void destroyLogo() {
		window.setVisible(false);
		logo.flush();
		window.dispose();
	}
	public void setVisible() {
		window.setVisible(true);
	}
}