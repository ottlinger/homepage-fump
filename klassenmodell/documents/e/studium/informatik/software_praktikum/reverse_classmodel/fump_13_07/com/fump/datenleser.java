package com.fump;
import java.util.Vector;
import java.io.*;
import java.util.StringTokenizer;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
*@version $Id: DatenLeser.java,v 1.6 2001/07/11 18:59:52 olesen Exp $
*/

public class DatenLeser{

	/**
	*@param String dateiname		liest Datei dateiname ein
	*@return Vector mit in dateiname stehenden Eintraegen
	*/
	public static Vector laden(String dateiname)throws IOException{
		Vector ergebnis = new Vector (0);
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(dateiname)));
			while(in.ready()){
				String s = in.readLine();
				if(s==null) break;
				StringTokenizer tok = new StringTokenizer(s,";",false);
				String next = tok.nextToken();
				Eintrag tmp = new Eintrag((new Boolean(next)).booleanValue());

				next = (String) tok.nextToken();
				if(!next.equals("#")) tmp.setName(next);
				else tmp.setName("");

				next = (String) tok.nextToken();
				if(!next.equals("#"))tmp.setAlias(next);
				else tmp.setAlias("");

				next = "";
				while (tok.hasMoreTokens()){
					next = next+tok.nextToken()+" ";
				}
				tmp.addAdressenString(next);
				ergebnis.add(tmp);
			}
		return ergebnis;
	}
	
	/**
	*@param Vector liste		Vektor der zum Adressbuch gehoerenden Eintraege
	*@param String dateiname	Dateiname
	* speichert das Adressbuch (bzw. dessen Eintraege in Datei dateiname.adr
	*/

	public static void speichern(Vector liste, String dateiname) /*throws IOException*/{
		try{
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dateiname)));
		for (int i = 0; i<liste.size(); i++){
			Eintrag g = null;
			if (((Eintrag)liste.elementAt(i)).getIsGroup()) g = (Eintrag) liste.elementAt(i);
			else g = (Eintrag) liste.elementAt(i);
			String s = new String("");
			for (int k = 0; k<((Vector)g.getAdresse()).size(); k++){
				if(((Vector)g.getAdresse()).elementAt(k)!=null)s+=((Vector)g.getAdresse()).elementAt(k)+";";
			}
				String eingabe = new String((new Boolean(g.getIsGroup())).toString()+";"+
					(((g.getName()==null)||(g.getName()==""))?"#":g.getName())+";"+
					(((g.getAlias()==null)||(g.getAlias()==""))?"#":g.getAlias())+";"
					+s);
				out.write(eingabe);	// ende out.write()
				out.newLine();
		} //ende for
		if (liste.size()==0) out.write("");
		out.close();
		}
			catch(IOException e){JOptionPane.showMessageDialog(null,"Fehler beim Speichern","Fehler",JOptionPane.WARNING_MESSAGE);}
	}	// ende speichern
}
