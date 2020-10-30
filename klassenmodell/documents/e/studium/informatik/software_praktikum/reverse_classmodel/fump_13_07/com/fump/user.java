package com.fump;

/**
*@author J.K.Olesen, B. Hentrich
*@version $id
*/
/**Zweck der Klasse User ist es, nicht für jeden Benutzer von Fump
*ein vollständiges Benutzer-Objekt im Speicher zu halten. User hält nur den
*Namen, das Passwort und den Speicherpfad des Benutzers bereit.
*/
public class User 
{
	private String name;
	private String password;
	private String path;
	
/**
*@param String der Benutzername
*@param String das Passwort des Benutzers
*@param String der Specherpfad zu den Benutzerdateien
*/
	public User (String name, String password, String path)
	{
		this.name = name;
		this.password = password;
		this.path=path;
	}
	
	public User(){}
	
	public String getName() 
	{
		return name;
	}

	public String getPassword() 
	{
		return password;
	}
	
	public String getPath() 
	{
		return path;
	}

/**
*setUser ändert die Variablen name, password, path des Users
*@param String der Benutzername
*@param String das Passwort des Benutzers
*@param String der Specherpfad zu den Benutzerdateien
*/
	public void setUser(String name, String password, String path)
	{
		this.name = name;
		this.password = password;
		this.path = path;
	}
}	
	
	
	
	
