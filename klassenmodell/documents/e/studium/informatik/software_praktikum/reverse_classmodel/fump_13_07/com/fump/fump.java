package com.fump;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.net.*;
import java.util.*;
import com.fump.*;
import com.fump.hilfe.*;
import com.fump.bearbeiten.*;
import com.fump.filter.*;
import com.fump.pkonto.*;


public class FUMP extends JFrame implements ActionListener
{
	public static Image icon;

	static Dimension size;

	
	//VorschauPanel vp;
	BenutzerContainer benutzercontainer;
	static Benutzer benutzer;
	
	JSplitPane mainSplit;
	JSplitPane msgSplit;
	
	public static Hilfe hilfe;
	NeueMail neue_Mail;
        KontoContainer kontocontainer;
	
	Adressbuch adressbuch;

	Info info = new Info(this,true);

	JButton button_Hilfe = new JButton();
        JButton button_NeueMail = new JButton();
        JButton button_Mailholen = new JButton();
        JButton button_Weiter = new JButton();
        JButton button_Antwort = new JButton();
        JButton button_Loeschen = new JButton();
        JButton button_Adressbuch = new JButton();
        JButton button_Senden = new JButton();
	
	JScrollPane leftPane;
	
        JLabel benutzerName;
	FilterKontainer fk;
	
	public FUMP()
	{
		
		setSize((int)(size.width*0.95),(int)(size.height*0.85));
		hilfe = new Hilfe(this);

		
		hilfe.setLocation((int)(size.width*0.29),(int)(size.height*0.1));

		icon = Toolkit.getDefaultToolkit().createImage(FUMP.class.getResource("bilder/e-logo_21x21.GIF"));
				 
		//enu
		JMenuBar menuBar = new JMenuBar();
		
		JMenu file = new JMenu("Datei");
		file.setFont(new Font("SansSerif",Font.PLAIN,14));
		JMenu edit = new JMenu("Bearbeiten");
		edit.setFont(new Font("SansSerif",Font.PLAIN,14));
		JMenu config = new JMenu("Einstellungen");
		config.setFont(new Font("SansSerif",Font.PLAIN,14));
		JMenu help = new JMenu("Hilfe");
		help.setFont(new Font("SansSerif",Font.PLAIN,14));
		
		//file menu's items	
		JMenuItem newMail = new JMenuItem("Neue Nachricht");
                           newMail.setFont(new Font("SansSerif",Font.PLAIN,14));
		setCtrlAccelerator(newMail, 'N');
		newMail.addActionListener(this);
		JMenuItem newFolder = new JMenuItem("Neuer Ordner");
		newFolder.setFont(new Font("SansSerif",Font.PLAIN,14));
		setCtrlAccelerator(newFolder, 'O');
		newFolder.addActionListener(this);
		JMenuItem sendMsg = new JMenuItem("Nachricht(en) senden");
		sendMsg.setFont(new Font("SansSerif",Font.PLAIN,14));		
		setCtrlAccelerator(sendMsg, 'S');
		sendMsg.addActionListener(this);
		JMenuItem getMsg = new JMenuItem("Nachrichten holen");
		getMsg.setFont(new Font("SansSerif",Font.PLAIN,14));
		JMenuItem exit = new JMenuItem("Beenden");
		exit.setFont(new Font("SansSerif",Font.PLAIN,14));
		exit.addActionListener(this);
		
		//edit menu's items
		JMenuItem cut = new JMenuItem("Ausschneiden");
		cut.setFont(new Font("SansSerif",Font.PLAIN,14));
		JMenuItem copy = new JMenuItem("Kopieren");
		copy.setFont(new Font("SansSerif",Font.PLAIN,14));
		JMenuItem delete = new JMenuItem("Löschen");
		delete.setFont(new Font("SansSerif",Font.PLAIN,14));
		JMenuItem search = new JMenuItem("Suchen");
		search.setFont(new Font("SansSerif",Font.PLAIN,14));
		search.addActionListener(this);
			
		//config's items
		JMenuItem accounts = new JMenuItem("Kontos");
		accounts.setFont(new Font("SansSerif",Font.PLAIN,14));
		setCtrlAccelerator(accounts, 'K');
                accounts.addActionListener(this);
		JMenuItem filters = new JMenuItem("Filter");
		filters.setFont(new Font("SansSerif",Font.PLAIN,14));
		setCtrlAccelerator(filters, 'F');
		JMenuItem adress = new JMenuItem("Adressbuch");
		adress.setFont(new Font("SansSerif",Font.PLAIN,14));
		setCtrlAccelerator(adress, 'A');
  		filters.addActionListener(this);
		JMenuItem users = new JMenuItem("Benutzerverwaltung");
		users.setFont(new Font("SansSerif",Font.PLAIN,14));
		users.addActionListener(this);
		
		//help's items
		JMenuItem helpItem = new JMenuItem("Hilfe");
		helpItem.setFont(new Font("SansSerif",Font.PLAIN,14));
		setCtrlAccelerator(helpItem, 'H');
		helpItem.addActionListener(this);
		JMenuItem about = new JMenuItem("Info");
		about.setFont(new Font("SansSerif",Font.PLAIN,14));
		about.addActionListener(this);
		
		
		
		//making file menu
		file.add(newMail);
		file.add(newFolder);
		file.addSeparator();
		file.add(sendMsg);
		file.add(getMsg);
		file.addSeparator();
		file.add(exit); 
		
		//making edit menu
		edit.add(cut);
		edit.add(copy);
		edit.add(delete);
		edit.add(search);
		
		//making config menu
		config.add(accounts);
		config.add(filters);
		config.add(adress);
		config.add(users);

		//making help menu
		help.add(helpItem);
		help.add(about);

		//making menubar
		menuBar.add(file);
		menuBar.add(edit);
		menuBar.add(config);
		menuBar.add(help);

		//ToolBar
		JToolBar toolBar = new JToolBar();
		toolBar.setLayout(new GridLayout(1,1));

		    		
          	ImageIcon image_Hilfe = new ImageIcon(FUMP.class.getResource("bilder/e-help_50x30.GIF"));
    		ImageIcon image_NeueMail = new ImageIcon(FUMP.class.getResource("bilder/e-write_eMail_50x30.GIF"));
    		ImageIcon image_Mailholen = new ImageIcon(FUMP.class.getResource("bilder/e-get_new_eMail_50x30.GIF"));
    		ImageIcon image_Weiter = new ImageIcon(FUMP.class.getResource("bilder/e-forward_eMail_50x30.GIF"));
    		ImageIcon image_Antwort = new ImageIcon(FUMP.class.getResource("bilder/e-reply_to_50x30.GIF"));
    		ImageIcon image_Loeschen = new ImageIcon(FUMP.class.getResource("bilder/e-delete_eMail_50x30.GIF"));
		ImageIcon image_Adressen = new ImageIcon(FUMP.class.getResource("bilder/e-adrbook_50x30.GIF"));
		ImageIcon image_Senden = new   ImageIcon(FUMP.class.getResource("bilder/e-send_eMail_50x30.GIF"));
		
		button_Hilfe.setIcon(image_Hilfe);
		button_Hilfe.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
                button_Hilfe.addMouseListener(new MyMouseListener());
		button_Hilfe.addActionListener(this);
		button_Hilfe.setToolTipText("Hilfe");
        	button_NeueMail.setIcon(image_NeueMail);
 		button_NeueMail.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
		button_NeueMail.addActionListener(this);
                            button_NeueMail.addMouseListener(new  MyMouseListener());
		button_NeueMail.setToolTipText("Neue Nachricht schreiben");
        	button_Mailholen.setIcon(image_Mailholen);
		button_Mailholen.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
		button_Mailholen.addActionListener(this);
                            button_Mailholen.addMouseListener(new MyMouseListener());
		button_Mailholen.setToolTipText("Nachrichten holen");
        	button_Weiter.setIcon(image_Weiter);
                           button_Weiter.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
		button_Weiter.addActionListener(this);
                            button_Weiter.addMouseListener(new MyMouseListener());
		button_Weiter.setToolTipText("Weiterleiten");
        	button_Antwort.setIcon(image_Antwort);
                            button_Antwort.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
		button_Antwort.addActionListener(this);
                            button_Antwort.addMouseListener(new MyMouseListener());
		button_Antwort.setToolTipText("Antworten");
        	button_Loeschen.setIcon(image_Loeschen);
             		button_Loeschen.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
		button_Loeschen.addActionListener(this);	
                            button_Loeschen.addMouseListener(new MyMouseListener());
		button_Loeschen.setToolTipText("Mail löschen");
               button_Adressbuch.setIcon(image_Adressen);
             		button_Adressbuch.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
		button_Adressbuch.addActionListener(this);	
                            button_Adressbuch.addMouseListener(new MyMouseListener());
		button_Adressbuch.setToolTipText("Adressbuch");
	 button_Senden.setIcon(image_Senden);
             		button_Senden.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
		button_Senden.addActionListener(this);	
                            button_Senden.addMouseListener(new MyMouseListener());
		button_Senden.setToolTipText("Nachrichten senden");

		JPanel links = new JPanel(new GridLayout(1,7));
		JPanel rechts = new JPanel(new GridLayout(1,5));
		JPanel container = new JPanel(new GridLayout(1,2));

              	 rechts.add(new Label());
                	rechts.add(new Label());
               	rechts.add(new Label());
                	rechts.add(new Label());
                	rechts.add(button_Hilfe);
		container.add(links);
		container.add(rechts);	

		toolBar.setLayout(new GridLayout(1,2));
		toolBar.setBorder(BorderFactory.createEtchedBorder());
                links.add(button_NeueMail);
		links.add(button_Mailholen);
		links.add(button_Senden);
		links.add(button_Antwort);
		links.add(button_Weiter);
		links.add(button_Loeschen);
		links.add(button_Adressbuch);
		
		rechts.add( button_Hilfe);

		toolBar.add(container);
		
		toolBar.setFloatable(false);
		
		//Assembling menu and toolbar in a JPanel
		JPanel menu_tools = new JPanel(new BorderLayout());
		menu_tools.add(BorderLayout.NORTH, menuBar);
		menu_tools.add(BorderLayout.SOUTH, toolBar);
		
		
		getContentPane().add(BorderLayout.NORTH, menu_tools);
		
/*
		//contents of the 3 areas...
		
		JTextArea upText = new JTextArea();
		*/
	// Tobias und Philipp	
	
		  
		JScrollPane upPane = (JScrollPane)MailOberflaeche.zeigeMails();

		JScrollPane downPane = new JScrollPane();
		
		//the 2 main split's creation + configuration 
		msgSplit = new JSplitPane();
		msgSplit.setOrientation(JSplitPane.VERTICAL_SPLIT);
		msgSplit.setDividerLocation(250);
		msgSplit.setLeftComponent(upPane);
		msgSplit.setRightComponent(downPane);
		msgSplit.setOneTouchExpandable(true);

		mainSplit = new JSplitPane();
		mainSplit.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		mainSplit.setDividerLocation(200);
		
		mainSplit.setRightComponent(msgSplit);
		mainSplit.setOneTouchExpandable(true);

		
		//showing status
		
	      //	Font f = new Font("Status", Font.BOLD, 2);
		
		JPanel status = new JPanel(new BorderLayout()) ;
		benutzerName= new JLabel("  Benutzer Name  ");
                benutzerName.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
                status.add(BorderLayout.WEST,benutzerName);
		
		getContentPane().add(BorderLayout.CENTER, mainSplit);
		getContentPane().add(BorderLayout.SOUTH, status);

		
		

		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				dispose();
				System.exit(0);
			}
		});

	}
	
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
                	if(command.equals("Hilfe")) {
			hilfe.kontext_hilfe("Main");
			hilfe.setVisible(true);
			return;
		}

		if(command.equals("Neue Nachricht")) {
			//benutzer.neueMail();
			neue_Mail = new NeueMail(benutzer);
			neue_Mail.setLocation((int)(size.width*0.25),(int)(size.height*0.1));
			return;
		}
	
		if(command.equals("Beenden")) {
			
			
			System.exit(0);
		}

		if(command.equals("Filter")) {
			FilterKontainerView filter = new FilterKontainerView(fk);
			filter.setLocation((int)(size.width*0.25),(int)(size.height*0.3));
			
			return;
		}

	             if(command.equals("Kontos")){
		     			//kontocontainer = benutzer.gibKontoKontainer();
                        		if(kontocontainer==null) {kontocontainer=new
					KontoContainer(benutzer.getName(),benutzer.getPath(),true);         
                                           			       kontocontainer.kontoGBS();
                                                 			}
                        		else                     	kontocontainer.kontoGBS();
                        	return;
                	}

 		if(command.equals("Info")) {
			Info.flag=true;
			info.setVisible(true);
			info.repaint();
			return;
		}
	
		if(command.equals("Adressbuch")) {
			
			
			try {
			
			/*adressbuch = benutzer.gibAdressbuch();
			if(adressbuch==null) benutzer.setzeAdressbuch(new Adressbuch(benutzer.getName(),benutzer.getPath(),true));
			adressbuch.pack();
			adressbuch.show();
			*/
			}   catch(Exception ex) { System.out.println("IO-Fehler beim Anlegen des Adressbuches"); }
		}
		if(command.equals("Benutzerverwaltung")) {
			System.out.println(command);
			benutzercontainer.benutzerVerwaltung(this);
			setBenutzerText();
			return;
		}

		
		if(command.equals("Suchen")) {
			SucheView sw = new SucheView();
		}
		

		if(e.getSource().equals(button_Hilfe)) {
                           		hilfe.kontext_hilfe("Main");
			//hilfe.setVisible(true);		
                  		return;
                	}

		if(e.getSource().equals(button_NeueMail)) {
			//benutzer.neueMail();
			neue_Mail = new NeueMail(benutzer);
			neue_Mail.setLocation((int)(size.width*0.25),(int)(size.height*0.1));
			return;
	  	}

		
		if(e.getSource().equals(button_Mailholen)) {
			kontocontainer.mailsAbrufen();
			return;
	  	}

		if(e.getSource().equals(button_Adressbuch)) {

			
			//nur zum Test der KontextHilfe:
			//hilfe.kontext_hilfe("Adressbuch"); return;

			try {
			//adressbuch = benutzer.gibAdressbuch();
			//if(adressbuch==null) adressbuch.setzeAdressbuch(new Adressbuch(benutzer.getName(),benutzer.getPath(),true));
			//adressbuch.pack();
			//adressbuch.show();
			
			}catch (Exception ex) { System.out.println("IO-Fehler beim Anlegen des Adressbuches");}
		}
		
		if(e.getSource().equals(button_Loeschen)) {

			//Nur zum Test der KontextHilfe
			hilfe.kontext_hilfe("Ordner"); return;
		}
		
	}
	
	
	
	//Benutzer-anmeldung
	
	public boolean createBenutzer() {

			benutzercontainer = new BenutzerContainer((JFrame)this);
			while(benutzer==null) { try { Thread.sleep(100); }
						catch (Exception e) {}  }
			leftPane = (JScrollPane) OrdnerOberflaeche.zeigeOrdner(benutzer.getName());
			
			kontocontainer = benutzer.gibKontoKontainer();
			fk = benutzer.gibFilterKontainer();
			benutzerName.setText("    Aktueller Benutzer ist : "+benutzer.getName()+"    ");
			return true;
			
	}
	
	public boolean setBenutzerText() {
			benutzerName.setText("    Aktueller Benutzer ist : "+benutzer.getName()+"    ");
			return true;
	}


	public void setLeftComponent() {
			if(leftPane!=null) mainSplit.setLeftComponent(leftPane);
			else System.out.println("leftpane=null");
	}
	
	public static void main(String args[])
	{

		size=Toolkit.getDefaultToolkit().getScreenSize();

		Logo logo = new Logo();
		logo.window.setLocation((int)(size.width*0.35),(int)(size.height*0.35));
		logo.setVisible();
	
		System.out.println("Starting Main...");
		FUMP mainFrame = new FUMP();

	
		mainFrame.setLocation((int)(size.width*0.025), (int)(size.height*0.05));
		mainFrame.setTitle("FUMP");
		mainFrame.setIconImage(icon);
		logo.destroyLogo();
		mainFrame.createBenutzer();
		mainFrame.setLeftComponent();
		mainFrame.setVisible(true);
		
		
	}
	private void setCtrlAccelerator(JMenuItem mi, char acc) {
		KeyStroke ks = KeyStroke.getKeyStroke(acc,Event.SHIFT_MASK);
		mi.setAccelerator(ks);
	}
}
