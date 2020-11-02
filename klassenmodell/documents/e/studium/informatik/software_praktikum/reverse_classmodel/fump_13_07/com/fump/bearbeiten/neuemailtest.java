package com.fump.bearbeiten;

import com.fump.bearbeiten.*;

public class NeueMailTest implements NeueMailGeschriebenListener
{
	public void geschriebeneMailAbholen(NeueMailGeschriebenEvent e)
	{
		System.out.println("Neue Mail wurde gesendet");
	}
	
	public static void main(String[] args)
	{
		NeueMail n = new NeueMail();
		n.addNeueMailGeschriebenListener(new NeueMailTest());
		//NeueMail m = new NeueMail();
		//m.addNeueMailGeschriebenListener(new NeueMailTest());
	}
}
