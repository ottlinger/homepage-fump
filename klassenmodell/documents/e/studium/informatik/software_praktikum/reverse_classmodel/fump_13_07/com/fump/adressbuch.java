package com.fump;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.awt.Point;
import java.io.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JOptionPane;
import java.awt.Container;
import java.io.IOException;
import com.fump.hilfe.*;

/**
*@version $Id: Adressbuch.java,v 1.14 2001/07/11 18:59:51 olesen Exp $
*/

public class Adressbuch extends JFrame implements ActionListener{
	public Vector eintraege;
	String pfad;
	String theBenutzer;
	JTable table;
	JScrollPane scrollPane;
	Eintrag neuerEintrag;	
	protected int row;
	JPanel panel, mpanel, unten;
	JButton eintrag_loeschen, person_neu, gruppe_neu, hilfe, ok, aendern, nur_perso, nur_grup, ende;
	int selRow=Integer.MIN_VALUE;
		String ausgewaehlteAdressen = "";
/**
	*@param Benutzer benutzer - die / der BenutzerIn, der  / dem das Adressbuch gehört
	*@param boolean neu - true, wenn Adressbuch das erste Mal angelegt wird
*/

	public Adressbuch(String benutzer, String path, boolean neu) {
		super("Adressbuch");
	// neu true: Adressbuch neu anlegen
			this.theBenutzer = benutzer;
			pfad = path+File.separator+theBenutzer+".adr";
			System.out.println(pfad);
			if(!neu)
			{
			
				System.out.println("kein Neuer user");
				try
				{ 
					eintraege =DatenLeser.laden(pfad);
				}
				catch (IOException e){
					JOptionPane.showMessageDialog(this,"Beim Lesen der Datei ist ein Fehler aufgetreten","Fehler",JOptionPane.ERROR_MESSAGE);
					eintraege=new Vector();
					this.dispose();}
			}			
			else {
				eintraege = new Vector();
				DatenLeser.speichern(eintraege,pfad);
				System.out.println("adr gesp");
			}
			init();
	}

	private void init(){
		AbuchTableModel mod = new AbuchTableModel(this);	
		table = new JTable(mod);
		mod.addTableModelListener(table);
		table.getTableHeader().addMouseListener(new java.awt.event.MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				header_clicked(e);
			}
		});
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ListSelectionModel rows = table.getSelectionModel();
		rows.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e){
				if(e.getValueIsAdjusting())return;
				ListSelectionModel lsm = (ListSelectionModel)e.getSource();
				selRow = lsm.getMinSelectionIndex();
			}
		});
		
		this.getContentPane().setLayout(new BorderLayout());
		scrollPane = new JScrollPane(table);
		mpanel = new JPanel(new BorderLayout());		// mpanel zeigt JTable mit Einträgen an
		mpanel.add(scrollPane,BorderLayout.NORTH);
		this.getContentPane().add(mpanel, BorderLayout.CENTER);

		eintrag_loeschen = new JButton("<html><font size=-2>l&ouml;schen</font></html>");
		eintrag_loeschen.setToolTipText("den gewählten Eintrag löschen");
		eintrag_loeschen.addActionListener(this);
		person_neu = new JButton("<html><font size=-2>neue Person</font></html>");
		person_neu.setToolTipText("einen neuen Personeneintrag anlegen");
		person_neu.addActionListener(this);
		gruppe_neu = new JButton("<html><font size=-2>neue Gruppe</font></html>");
		gruppe_neu.setToolTipText("Einen neuen Gruppeneintrag anlegen");
		gruppe_neu.addActionListener(this);
		hilfe = new JButton((Icon)new ImageIcon(FUMP.class.getResource("bilder/e-help_klein.gif")));
		hilfe.setToolTipText("hilfe");
		hilfe.addActionListener(this);
		ok = new JButton("<html><font size=-2>ok</font></html>");
		ok.setToolTipText("Adresse übernehmen");
		ok.addActionListener(this);
		aendern = new JButton ("<html><font size=-2>&auml;ndern</font></html>");
		aendern.setToolTipText("den markierten Eintrag ändern");
		aendern.addActionListener(this);
		ende = new JButton("<html><font size=-2>ende</font></html>");
		ende.setToolTipText("Adressbuch schliessen");
		ende.addActionListener(this);


		panel = new JPanel(new FlowLayout());		// panel: Buttonleiste
		panel.add(eintrag_loeschen);
		panel.add(person_neu);
		panel.add(gruppe_neu);
		panel.add(hilfe);
		panel.add(ok);
		panel.add(aendern);
		panel.add(ende);
		this.getContentPane().add(panel, BorderLayout.NORTH);
		
		unten = new JPanel();
		nur_perso = new JButton("<html><font size=-2>nur Personen<br>anzeigen</html>");
		nur_perso.setToolTipText("ein neues Fenster öffnen, in dem nur die Personeneinträge angezeigt werden");
		nur_perso.addActionListener(this);
		nur_grup = new JButton("<html><font size=-2>nur Gruppen<br>anzeigen</html>");
		nur_grup.setToolTipText("ein neues Fenster öffnen, in dem nur die Gruppeneinträge angezeigt werden");
		nur_grup.addActionListener(this);
		unten.add(nur_perso);
		unten.add(nur_grup);
		this.getContentPane().add(unten, BorderLayout.SOUTH);
		
	} //init()

	public void actionPerformed(ActionEvent e){

		if(e.getSource() instanceof JButton){
			if(e.getSource()==aendern){
				if(selRow==Integer.MIN_VALUE){
					JOptionPane.showMessageDialog(this, "Bitte waehlen Sie zuerst einen Eintag aus!","Nachricht",JOptionPane.INFORMATION_MESSAGE);
				}
				else{
					AdresseAendern m = new AdresseAendern(this, "", true, this,selRow);
					/*for (int i = 0; i<this.eintraege.size(); i++){
						System.out.println(((Eintrag)eintraege.elementAt(i)).toString());
					}*/
				}
				DatenLeser.speichern(this.eintraege,this.pfad);

			}
				
			if(e.getSource()==hilfe){FUMP.hilfe.kontext_hilfe("Adressbuch");}
			if(e.getSource()==eintrag_loeschen){
				if(selRow==Integer.MIN_VALUE){
					JOptionPane.showMessageDialog(this,"Bitte wählen Sie zuerst einen Eintrag aus!","Nachricht",
					JOptionPane.INFORMATION_MESSAGE);
				}
				else{
					int y = JOptionPane.showConfirmDialog(this,"den gewählten Eintrag wirklich löschen?","Warnung",JOptionPane.YES_NO_OPTION);
					// gewählten Eintrag löschen:
					if(y==JOptionPane.YES_OPTION){
						this.eintraege.removeElementAt(selRow);
						table.repaint();
						table.addNotify();
					}
				}
				DatenLeser.speichern(this.eintraege,this.pfad);

			}
			if(e.getSource()==person_neu){
				NeuenEintragAnlegen neu = new NeuenEintragAnlegen(this, true, this, false,"Neue Person anlegen");
				DatenLeser.speichern(this.eintraege,this.pfad);
				table.addNotify();
			}
			if(e.getSource()==gruppe_neu){
				NeuenEintragAnlegen neu = new NeuenEintragAnlegen(this, true, this,true,"Neue Gruppe anlegen");
				DatenLeser.speichern(this.eintraege,this.pfad);
				table.addNotify();
			}
			if(e.getSource()==ok){
				if(selRow==Integer.MIN_VALUE){
					JOptionPane.showMessageDialog(this, "Bitte waehlen Sie zuerst einen Eintrag aus!", "Nachricht", JOptionPane.INFORMATION_MESSAGE);
				}
				else{
					getAusgewaehlteAdressen(selRow);
					int x = JOptionPane.showConfirmDialog(this,"Adressbuch wirklich schliessen?","Warnung",JOptionPane.YES_NO_OPTION);
					if(x==JOptionPane.YES_OPTION){
						//System.out.println("Adressbuch-ok speichert");
						DatenLeser.speichern(this.eintraege,this.pfad);
						this.dispose();
					}
				}
			}
			if(e.getSource()==ende){
				int x = JOptionPane.showConfirmDialog(this,"Adressbuch schliessen?","Warnung",JOptionPane.YES_NO_OPTION);
				if(x==JOptionPane.YES_OPTION){
					DatenLeser.speichern(this.eintraege, this.pfad);
					//System.out.println("Adressbuch-ende speichert");
					this.dispose();
				}
			}
			if(e.getSource()==nur_grup){NurGruppenOderPersonenAnzeigen gruppe = new NurGruppenOderPersonenAnzeigen("Nur Gruppen anzeigen",this,true);}
			if(e.getSource()==nur_perso){NurGruppenOderPersonenAnzeigen person = new NurGruppenOderPersonenAnzeigen("Nur Personen anzeigen",this,false);}

		}//if(e.getSource() instanceof JButton)
		
	}//actionPerformed

	/**
	*@param Eintrag e - der Eintrag, der eingefügt werden soll
	*/

	public void addNeuenEintrag(Eintrag e){
		eintraege.add(e);
		DatenLeser.speichern(this.eintraege,this.pfad);
		table.addNotify();
	}
	
	/**
	*@param Eintrag e - Eintrag, der gelöscht werden soll
	*/

	private void loeschenEintrag(Eintrag e){
		eintraege.remove(e);
		DatenLeser.speichern(this.eintraege,this.pfad);
		table.addNotify();
	}

	/**
	*@param String name - Name des zu ändernden Eintrags
	*@param Eintrag e - neuer Eintrag
	*/

	public void eintragAendern(String name, Eintrag e){
		for(int i = 0; i<eintraege.size(); i++){
			if(((Eintrag)eintraege.elementAt(i)).getName().equals(name)){
				eintraege.setElementAt(e,i);
				break;	// falls der Eintrag vorhanden ist: aendern, for-Schleife beenden
			} // ende if
		}	// ende for
		/*for(int t = 0; t<eintraege.size();t++){
			System.out.println(((Eintrag)eintraege.elementAt(t)).getEintragString());
		}*/
		DatenLeser.speichern(this.eintraege,this.pfad);
		table.addNotify();
	}

	/**
		*@param int row - ausgewählter Eintrag
		*@return String ausgewählteAdressen 
	*/
	
	public String getAusgewaehlteAdressen(int x){
		Eintrag tmp = (Eintrag)eintraege.elementAt(x);

		for(int i = 0; i<tmp.adressen.size(); i++){
			ausgewaehlteAdressen += ((String)tmp.adressen.elementAt(i)+", ");
		}
		if(ausgewaehlteAdressen.endsWith(", "))ausgewaehlteAdressen = ausgewaehlteAdressen.substring(0,ausgewaehlteAdressen.length()-2);
		return ausgewaehlteAdressen;
	}
	
	public String getAusgewaehlteAdressen(){
		return ausgewaehlteAdressen;
	}

	/*public void eintragNeuSetzen(Eintrag e, int x){
		for(int i = 0; i<eintraege.size(); i++){
			Eintrag d = (Eintrag)eintraege.elementAt(i);
			System.out.println(d.getName()+" "+d.getAlias()+" "+d.getAdressenString());
		}
		this.eintraege.setElementAt(e,x);
		for(int i = 0; i<eintraege.size(); i++){
			Eintrag d = (Eintrag)eintraege.elementAt(i);
			System.out.println(d.getName()+" "+d.getAlias()+" "+d.getAdressenString());
		}
		DatenLeser.speichern(this.eintraege,this.pfad);
	}*/

	public void header_clicked(MouseEvent e){
		int column = table.columnAtPoint(new Point(e.getX(), e.getY()));
		int indexDaten = table.convertColumnIndexToModel (column);
		sortiere(indexDaten);
		table.addNotify();
	}

	public void sortiere(int indexDaten){
		switch (indexDaten){
			case 0: Collections.sort(eintraege, new NameComp()); break;
			case 1: Collections.sort(eintraege, new AliasComp()); break;
			case 2: Collections.sort(eintraege, new AdressComp()); break;
		}
	}
	
	/**
	*@return dieses Adressbuch
	*/
	
	public Adressbuch getAdressbuch(){
		return this;
	}

	/*public static void main(String[] args) {
		Adressbuch test = new Adressbuch("NeuTest","/home/elfe/eschner/", false);
		Eintrag t = new Eintrag(true);
		t.setName("Alex b.");
		t.addAdresse("q");
		Eintrag w = new Eintrag(false);
		w.setName("Be");
		w.setAlias("c");
		w.addAdresse("www");
		//test.addNeuenEintrag(t);
		//test.addNeuenEintrag(w);
		//test.repaint();
		//try{
		//DatenLeser.speichern(test.eintraege,test.pfad);
		//}
		//catch(IOException e){System.out.("Fehler!");}
		//test.setSize(test.getPreferredSize());
		test.pack();
		test.setVisible(true);
	}*/
}

class NameComp implements Comparator{
	public NameComp(){}
	public int compare(Object o1, Object o2){
		Eintrag e1 = (Eintrag) o1;
		Eintrag e2 = (Eintrag) o2;
		return e1.getName().compareTo(e2.getName());
	}
}

class AliasComp implements Comparator{
	public AliasComp(){}
	public int compare(Object o1, Object o2){
		Eintrag e1 = (Eintrag) o1;
		Eintrag e2 = (Eintrag) o2;
		return e1.getAlias().compareTo(e2.getAlias());
	}
}

class AdressComp implements Comparator{
	public AdressComp(){}
	public int compare(Object o1, Object o2){
		Eintrag e1 = (Eintrag) o1;
		Eintrag e2 = (Eintrag) o2;
		return e1.getAdressenString().compareTo(e2.getAdressenString());
	}
}
