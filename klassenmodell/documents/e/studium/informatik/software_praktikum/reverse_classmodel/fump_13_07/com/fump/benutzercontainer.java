package com.fump;
import java.util.*;
import java.io.*;
import java.io.File;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.*;

/**
*@author J.K.Olesen, B. Hentrich
*@Version $id$
*/
/**
*Die Klasse BenutzerContainer stellt Methoden zur Benutzerverwaltung von FUMP zur
*Verfügung.
*/
public class BenutzerContainer
{
	private static boolean start = false;
	private static Vector allUsers;
	private static Vector allUsersNames;
	public static Benutzer benutzer;
	static JFrame frame = new JFrame();
	private static DefaultComboBoxModel allUsersModel;

/**
*Constructor
*Ruft init() auf
@see #init
*/	
	public BenutzerContainer(JFrame frame)
	{
		this.frame = frame;
		allUsers = new Vector();
		allUsersNames = new Vector();
		allUsersModel = new DefaultComboBoxModel(allUsersNames);
		init();
	}
/**
*Gibt den aktuellen Benutzer von FUMP zurück.
*@return Benutzer, der aktuellen Benutzer.
*/	
	public static Benutzer getBenutzer()
	{
		return benutzer;
	}

/**
*Gibt an, ob FUMP gerade gestartet wurde.
*@return boolean, true = gerade gestartet.
*/
	public static boolean isStart()
	{
		return start;
	}

/**
*Gibt alle bekannten Benutzernamen zurück.
*@return DefaultComboBoxModel, enthält alle Benutzernamen. 
*/
	public static DefaultComboBoxModel getallUsersModel()
	{
		return allUsersModel;
	}

/**
*Initialisiert den BenutzerContainer.
*Ließt die Datei users.conf ein, bei Fehlern erscheint eine ERROR_MESSAGE, dann erfolgt System.exit(0).
*/
	public void init()
	{
		try
		{
			read();
		}
		catch(IOException io)
		{
			JOptionPane.showMessageDialog(BenutzerContainer.frame,	"Fehler beim Lesen der Datei users.conf!!!",
		  										      	"Fehler",
                   	            						JOptionPane.ERROR_MESSAGE);	
            System.exit(0);
        }	
        catch(NoSuchElementException nse)
		{
			JOptionPane.showMessageDialog(BenutzerContainer.frame,	"Syntaxfehler in der Datei users.conf!!!",
												      	"Fehler",
                   	            						JOptionPane.ERROR_MESSAGE);	
           	System.exit(0);
       	}
 //      	frame.dispose();
       	if (benutzer==null) start=true;
	BenutzerGui sbg = new BenutzerGui(frame);
	}
	
	public static void benutzerVerwaltung(JFrame parent)
	{
		start=false;
		BenutzerGui sbg = new BenutzerGui(parent);
									
	}			
/**
*Überprüft, ob Benutzer existiert.
*@param name der zu prüfende Benutzername.
*@return boolean, true = Benutzer gibt es, false sonst.
*/	
	public static boolean existBenutzer(String name)
	{
		User b = new User();
		for (Enumeration e= allUsers.elements(); e.hasMoreElements();)
		{
			b = (User) e.nextElement();
			if(b.getName().equals(name)) return true;
		}
		return false;
	}

/**
*Überprüft Passwort/Benutzer Zuordnung.
*@param name der zu prüfende Benutzername.
*@param password das dazugehörige Password.
*@return boolean, true = Password stimmt, false sonst.
*/	
	static boolean accessBenutzer(String name, String password)
	{
		User b = new User();
		for (Enumeration e= allUsers.elements(); e.hasMoreElements();)
		{
			b = (User) e.nextElement();
			if(b.getName().equals(name)) 
			{
				if (b.getPassword().equals(password)) return true;
				return false;
			}
		}
		return false;
	}

/**
*Gibt den Pfad zu einem Benutzernamen zurück.
*@param name der Benutzername.
*@return String der Speicherpfad.
*/		
	static String getPath(String name)
	{
		User b = new User();
		for (Enumeration e= allUsers.elements(); e.hasMoreElements();)
		{
			b = (User) e.nextElement();
			if(b.getName().equals(name)) 
			{
				return b.getPath();
			}
		}
		return "";
	}
/**
*Gibt das Password zu einem Benutzernamen zurück.
*@param name der Benutzername.
*@return String der Speicherpfad.
*/			
	static String getPassword(String name)
	{
		User b = new User();
		for (Enumeration e= allUsers.elements(); e.hasMoreElements();)
		{
			b = (User) e.nextElement();
			if(b.getName().equals(name)) 
			{
				return b.getPassword();
			}
		}
		return "";
	}
/**
*Legt einen neuen Benutzer mit den Verzeichnissen/Dateien an.
*Wurde der Standardpfad mitgegeben, wird in diesem Verzeichnis ein Ordner
*mit dem Benutzernamen (Leerzeichen durch _ ersetzt), angelegt. Ansonsten
*werden alle Dateien in dem ausgewählten Verzeichnis gespeichert. Dabei werden
*fehlende Parent-Verzeichnisse ohne Nachfrage erstellt.
*Die Namen werden alphabetisch absteigend sortiert.
*@param name der Benutzername.
*@param password das Passwort.
*@param password1 das Passwort zur Überprüfung.
*@param path der Speicherpfad.
*@exception InputException wenn der Benutzer bereits existiert, die Passwörter
*nicht übereinstimmen, kein Name oder kein Passwort angegeben wurde.
*@exception IOException Bei Schreibfehlern in users.conf.
*/				
	protected static void addBenutzer(String name, String password, String password1, String path) throws InputException,IOException
	{
		if (existBenutzer(name)) throw new InputException(1);
		if (name.trim().equals(""))throw new InputException(2);
		if (!(password.equals(password1))) throw new InputException(5);
		if (password.trim().equals("")) throw new InputException(6);
		if (path.trim().equals("")) path=System.getProperty("user.dir");
		File dir = new File(path);
		if (path.equalsIgnoreCase(System.getProperty("user.dir")))dir = new File(path,name.replace(' ','_'));
		dir.mkdirs();
		path=(dir.getAbsolutePath());
		Collator col = Collator.getInstance();
		User b = new User();
		int pos=0;
		for (Enumeration e= allUsers.elements(); e.hasMoreElements();)
		{
			b = (User) e.nextElement();
			if(col.compare(b.getName(),(name))>=0)
			{
				allUsersModel.insertElementAt(name,pos);
				allUsers.add(pos,new User(name,password,path));	
				break;
			}	
			pos++;
		}
		if (pos==allUsersModel.getSize())
		{
			allUsersModel.insertElementAt(name,pos);
			allUsers.add(pos,new User(name,password,path));	
		}
		save();
		Benutzer dummy = new Benutzer(name,path,false);
		if (start) setBenutzer(name,password);
	}

/**
*Legt ein neues Benutzer-Objekt als aktuellen Benutzer an.
*@param name der Benutzername.
*@param password das Passwort.
*@exception InputException wenn das Passwort nicht stimmt.
*/
	protected static void setBenutzer(String name, String password) throws InputException
	{
		User b = new User();
		for (Enumeration e= allUsers.elements(); e.hasMoreElements();)
		{
			b = (User) e.nextElement();
			if(b.getName().equals(name))
			{
				if (!(b.getPassword().equals(password))) throw new InputException(7);
				benutzer = new Benutzer(b.getName(),b.getPath(),true);
				System.out.println("Benutzer-Objekt fuer: "+benutzer.getName()+" erzeugt.");
				start=false;
				break;
			}	
		}
		FUMP.benutzer = benutzer;
	}

/**
*Ändert Benutzerdaten wie Password und Speicherpfad.
*Alle Dateien in dem Benutzerverzeichnis werden in das neue
*Benutzerverzeichnis verschoben (keine Unterordner!).
*@param name der Benutzername
*@param password das Passwort
*@param password1 das Passwort zur Überprüfung
*@param path der Speicherpfad
*@exception InputException wenn die Passwörter
*nicht übereinstimmen oder kein Passwort angegeben wurde.
*@exception IOException Bei Schreibfehlern in users.conf.
*/	
	protected static void changeBenutzer(String name, String password, String password1, String path) throws InputException,IOException
	{
		if (!(password.equals(password1))) throw new InputException(5);
		if (password.trim().equals("")) throw new InputException(6);
		User b = new User();
		String oldpath="";
		for (Enumeration e= allUsers.elements(); e.hasMoreElements();)
		{
			b = (User) e.nextElement();
			if(b.getName().equals(name))
			{
				oldpath=b.getPath();
				b.setUser(name,password,path);
			}	
		}
		File dir = new File(path);
		dir.mkdirs();
		File dirold = new File(oldpath);
		File [] subdir = dirold.listFiles();
		for (int i=0; i<subdir.length;i++)
		{
			File tmp = new File(dir,subdir[i].getName());
			FileInputStream fin = new FileInputStream(subdir[i]);
			FileOutputStream fout = new FileOutputStream(tmp);
			int c;
			while ((c=fin.read())!=-1)
			{
				fout.write(c);
			}
			subdir[i].delete();
		}
		dirold.delete();
		save();
	}

/**
*Löscht den Benutzer mit allen zugehörigen Dateien.
*@param name der Benutzername.
*@param password das Passwort.
*@exception InputException wenn das Password falsch ist.
*@exception IOException Bei Schreibfehlern in users.conf.
*/					
	protected static void delBenutzer(String name, String password) throws IOException, InputException
	{
		if (!start)
		{
		 	if (benutzer.getName().equals(name)) throw new InputException(10); 
		}
		User b = new User();
		for (Enumeration e= allUsers.elements(); e.hasMoreElements();)
		{
			b = (User) e.nextElement();
			if(b.getName().equals(name))
			{
				if (!(b.getPassword().equals(password))) throw new InputException(7);
//				System.out.println(b.getpath);
				File dir = new File(b.getPath());
				File [] subdir = dir.listFiles();
				System.out.println(subdir);
				for (int i=0; i<subdir.length;i++)
				{
					subdir[i].delete();
				}
				dir.delete();
				allUsers.remove(b);
				allUsersModel.removeElement(name);
				allUsersNames.remove(name);
				save();
				break;
			}	
		}
	}

/**
*Wandelt ein char[] in einen String, Hilfsfunktion zur Umwandlung der Passwörter.
*@param char[]c ein Character Array.
*@return String das Passwort als String.
*/							
	public static String ToString(char[] c)
	{
		String s ="";
		for (int i = 0; i < c.length; i++)
		{
			s = s + c[i];
		}
		return s;
	}	
	
/** 
*Speichert vorhandene Benutzer in di Datei users.conf.
*@exception IOException bei Schreibfehlern in der users.conf.
*/	
	private static void save() throws IOException
	{
		User b = new User();
		FileWriter fw = new FileWriter("users.conf");
		for (Enumeration e = allUsers.elements(); e.hasMoreElements();)
		{
			b = (User) e.nextElement();
			fw.write(b.getName()+";"+b.getPassword()+";"+b.getPath()+";\n");
		}
		fw.close();
	}	
	
/** 
*Ließt die Datei users.conf ein.
*@exception IOEXception bei Lesefehlern der users.conf.
*@exception NoSuchElementException als Folge von Syntaxfehlern in der users.conf.
*/	
	public static void read() throws IOException, NoSuchElementException
	{
		allUsers.clear();
		allUsersNames.clear();
		FileReader fr = new FileReader("users.conf");
    	int c;
    	String input="";
	   	while ((c=fr.read())!=-1)
    	{
    		if (((char) c)=='\n') 
    		{
    			StringTokenizer tokenizer = new StringTokenizer(input,";");
				String name = tokenizer.nextToken();
				String password = tokenizer.nextToken();
				String path = tokenizer.nextToken();
				allUsers.add(new User(name,password,path));
				allUsersNames.add(name);
				input="";
			}
			else input = input + ((char) c);
    	}
    }
}

/** 
*@author B. Hentrich, J.K.Olesen
*@version $id$
*/	
class InputException extends Exception
{
	int fault;
/** 
*Constructor
*@param int der auslösende Fehlercode.
*/			
	public InputException(int fault)
	{
		this.fault=fault;
	}

/** 
*Gibt den ausgelösten Fehlercode zurück.
*@return int Fehlercode.
*/			
	public int getFault()
	{
		return fault;
	}

/** 
*Gibt den ausgelösten Fehler als String zurück.
*@return String Fehlermeldung.
*/		
	public String getFehlerMeldung()
	{
		switch(fault)
		{
			case 1:
				return("Den Benutzer gibt es schon!");
			case 2:
				return("Sie haben keinen Benutzernamen angegeben!");
			case 5:
				return("Die Passwörter stimmen nicht überein!");
			case 6:
				return("Sie haben kein Passwort eingegeben!");
			case 7:
				return("Das eingegebene Password ist falsch!");
			case 10:
				return("Der aktuelle Benutzer läßt sich nicht löschen!");
			default:
				return("Es ist ein Fehler aufgetreten!");
		}
	}
}	
	
