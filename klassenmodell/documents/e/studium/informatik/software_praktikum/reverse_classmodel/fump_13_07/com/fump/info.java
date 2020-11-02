package com.fump;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;




public class Info extends JDialog   {
   public static boolean flag=false;
   JPanel content;
   private int  n=1500;
   private int  z=0;
   private static String[] liste= { "","","","","","","","","","","","","","Autoren","",
                                     "Tobias Lenz","Philipp Ottlinger","Peter Menze",
                                     "George Kharitonashvili","Jörg Dieckmann","Tobias Becker",
                                     "Marc Scherfenberg","Florian Brötzmann", "Lasse Voß",
                                     "Michael Stollberg", "Florian Krause", "Sebastian Lück",
                                     "Markus Hindorf", "Philipp Ottlinger", "Benjamin Hentrich",
                                     "Janne K.Olesen",  "Lisa Eschner",
                                     "","Copyright 2001","","","","","","","","","","","",""} ;

   private int[] split= {6,7,5,6,4,6,4,7,5,7,7,9,6,7,8,5,4};
   FontMetrics fm ;


	public Info(JFrame parent , boolean modal ) {
		super(parent, "Info", modal);
                content=(JPanel)getContentPane();
                content.setBackground(Color.black);
                setSize(400, 450);
                setLocation((Toolkit.getDefaultToolkit().getScreenSize().width-400)/2,(Toolkit.getDefaultToolkit().getScreenSize().height-450)/3);
               	setVisible(false);

         }
        public void paint(Graphics g) {
        	  if(!flag) return;
        	  flag=false;
      
                  g.setColor(Color.black);
                  g.fillRect(0,0,400,450);
                  g.setFont(new Font("Serif",Font.PLAIN,20));
                  g.setColor(Color.white);
                  g.drawString("Gruppe C",150,50);
                  fm=g.getFontMetrics();
          try {
                Thread.sleep(2500);

                while (z <(liste.length-13)) {
                          g.setColor(Color.black);
                          g.fillRect(0,0,400,450);
                          g.setColor(Color.white);
                     for(int j=0;j<14;j++){
                          g.drawString(liste[z+j],(400-fm.stringWidth(liste[z+j]))/2,50+j*27 );
                     }
                     for(int i=0;i<20;i++){
                             if(z<15||(z >=liste.length-14)) {Thread.sleep(1000); break;} // Nur namen teilen
                             g.setColor(Color.black);
                             g.fillRect(0,0,400,55);
                             g.setColor(Color.white);
                             String s=liste[z];
                             int punkt1=(400-fm.stringWidth(s))/2 ;
                             int punkt2= punkt1+fm.stringWidth(s.substring(0,split[z-15]));
                             int leer=fm.stringWidth(" ");
                             g.drawString(s.substring(0,split[z-15]),punkt1-i*10,50);
                             g.drawString(s.substring((split[z-15]+1),s.length()),punkt2+leer+i*10,50);
                             Thread.sleep(50);
                     }
                      z++;
                 }
 

           g.setColor(Color.black);
           g.fillRect(0,0,400,450);
           Thread.sleep(1000);
           g.setColor(Color.white);
           g.setFont(new Font("Serif",Font.BOLD,80));
           fm=g.getFontMetrics();
           g.drawString("FUMP",80,200);

           Thread.sleep(300);
           int[] breite=new int[4];
             for(int i=0;i<4;i++) breite[i]=fm.charWidth("FUMP".charAt(i));
             int anfang=80;

             for(int i=0;i<4;i++) {
                     g.setColor(Color.yellow);
                     g.drawString("FUMP".substring(i,i+1),anfang,200);
                     g.setColor(Color.white);
                     Thread.sleep(130);
                     g.drawString("FUMP".substring(i,i+1),anfang,200);
                     anfang +=breite[i];
             }
             int length=fm.stringWidth("FUMP");
             g.setFont(new Font("Serif",Font.BOLD,15));
             String st="FREIE UNI MAIL PROGRAMM";
             fm=g.getFontMetrics();
             int hight=fm.getHeight();
             int aktlength=fm.stringWidth(st);
             for (int i=0;i<st.length();i++) {
                g.drawString(st.substring(0,i+1),80+(length-aktlength)/2,200+hight+5);
                Thread.sleep(60);
             }



          } catch (Exception e) {}
         z=0;

        }
}