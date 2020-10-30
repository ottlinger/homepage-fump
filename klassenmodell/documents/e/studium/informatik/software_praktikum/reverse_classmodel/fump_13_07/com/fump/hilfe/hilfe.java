  package com.fump.hilfe;
  
  import javax.swing.*;
  import java.awt.*;
  import java.net.*;
  import javax.swing.text.html.*;
  import javax.swing.event.*;
  import java.awt.event.*;
import com.fump.FUMP;

  public class Hilfe extends JFrame      {

	public void kontext_hilfe(String s) {
		 URL url2;
		try {

		if(s.equals("Filter"))  {
			url2 =   new URL("file:"+System.getProperty("user.dir") +"/com/fump/hilfe/main_filter.html");
			editorPane.setPage(url2); setVisible(true); return; }
	
		if(s.equals("Ordner"))  {
			url2 =   new URL("file:"+System.getProperty("user.dir") +"/com/fump/hilfe/main_ordner.html");
			editorPane.setPage(url2);  setVisible(true);  return; }

		
		if(s.equals("Main")) {
         url2 =   new URL("file:"+System.getProperty("user.dir") +"/com/fump/hilfe/main_anzeige.html");
			editorPane.setPage(url2);  setVisible(true); return; }
		if(s.equals("NeueMail")) {
         url2 =   new URL("file:"+System.getProperty("user.dir") +"/com/fump/hilfe/main_neuemail.html");
			editorPane.setPage(url2);  setVisible(true); return; }
		if(s.equals("Konto")) {
         url2 =   new URL("file:"+System.getProperty("user.dir") +"/com/fump/hilfe/main_konto.html");
			editorPane.setPage(url2);  setVisible(true); return; }
		if(s.equals("Adressbuch")) {
         url2 =   new URL("file:"+System.getProperty("user.dir") +"/com/fump/hilfe/main_adressbuch.html");
			editorPane.setPage(url2);  setVisible(true); return; }
		if(s.equals("Benutzer")) {
			 url2 =   new URL("file:"+System.getProperty("user.dir") +"/com/fump/hilfe/main_benutzer.html");
			editorPane.setPage(url2);  setVisible(true); return; }
		
		else System.out.println("Falsches Kontext-Hilfe-Argument : "+s);

		} catch(Exception e) { e.printStackTrace(); }
		
	}
	public  static int  count=0;
	//static JEditorPane pane = new JEditorPane();
   	    Container   contentPane;
   	    JScrollPane sqrollPane;
   	 static  JEditorPane editorPane;
                  FUMP fump;
   	    URL   url;                                 //AnfangsSeite
  	      public  Hilfe(FUMP fump) {                           //Konstruktor
  	            super("HTML - Hilfe");
                         this.fump=fump;
  	            try {
     url=new URL("file:"+System.getProperty("user.dir") +"/com/fump/hilfe/main_anzeige.html");
    	             editorPane=new JEditorPane(url); 
    	            } catch (Exception e) { System.out.println("URL-Fehler");}
    	            count ++;
                    editorPane.setEditable(false);
                    editorPane.addHyperlinkListener(new Hyperactive());
                    sqrollPane=new JScrollPane(editorPane);
                    contentPane =getContentPane(); 
                    contentPane.add(sqrollPane);
                    setBackground(Color.white);
                    setSize((int)(fump.getSize().width*0.6), (int)(fump.getSize().height*0.8) );             //Bearbeiten
                    setLocation(150,0);
         //         setResizable(false);
  	            setVisible(false);
  	            this.addWindowListener(new WindowAdapter(){
  	            	  public void windowClosing(WindowEvent e){
  	            	  	count--;
  	            	  	setVisible(false);
  	            	  }});      
                }                                   //ende des Konstuktors  
      class Hyperactive implements HyperlinkListener { 
 	    public void hyperlinkUpdate(HyperlinkEvent e) { 
	      if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
		JEditorPane pane = (JEditorPane) e.getSource();
		
	         if (e instanceof HTMLFrameHyperlinkEvent) {
	  	       HTMLFrameHyperlinkEvent  evt = (HTMLFrameHyperlinkEvent)e;
	  	       HTMLDocument doc = (HTMLDocument)pane.getDocument();
	  	       doc.processHTMLFrameHyperlinkEvent(evt);
	  	      } else { 
	          try {
	     		      pane.setPage(e.getURL());
		          } catch (Throwable t) {
			      t.printStackTrace();
		          }
		      }
	          }
	    }
      }             
         
 }                                                // ende Class Hilfe