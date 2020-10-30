
// aslödkfjalöskdjfölaksdjflöaksjdfoäö
package com.fump.pkonto;

import java.util.*;
import java.io.*;
import javax.mail.*;
import javax.mail.event.*;

/**
 * @author Marc Scherfenberg, Florian Brötzmann
 * @version $Id: KontoContainer.java,v 1.15 2001/07/11 19:02:04 olesen Exp $
 */
public class KontoContainer implements Serializable { 
  public Vector konten,kontenDaten;
  protected KontoGBS kGBS;
  TransferGBS tF;
  String path;

  public KontoContainer(String Benutzername,String path,boolean ex){
  	konten = new Vector(); 	
  	this.path=path+File.separator+Benutzername+".dat";
	 	if (ex) {
	  	try {
	  		laden();
	  	} catch (Exception s) {System.out.println("LF");}	
	  }
  }

	public void speichern() {
		// Daten übertragen
		Konto k;
		kontenDaten=new Vector();
		for (int i=0; i<konten.size();i++){
			k = ((Konto) konten.elementAt(i));
  		kontenDaten.add(new KontoDaten(k.kName, k.sName, k.sender, k.replyTo,k.pop,k.smtp,k.uName,k.uPass,k.savePass,k.del));
  	}
  	k=null;
		
		try {               		
			FileOutputStream ostream = new FileOutputStream(path);
      ObjectOutputStream p = new ObjectOutputStream(ostream);
      p.writeObject(kontenDaten);
      p.flush();
      ostream.close();
		} catch (Exception e) {
			System.out.println("SF");	
		}	
		kontenDaten=null;
	}


	public void laden() throws LadeFehler  {
		try {
	  	FileInputStream istream = new FileInputStream(path);  	
      ObjectInputStream ip = new ObjectInputStream(istream);
      //System.out.println("nach iprea-1");
      kontenDaten = (Vector) ip.readObject();
      //System.out.println("nach ipread");
	    istream.close();
		} 
		catch (FileNotFoundException e) {
			//System.out.println("FileNotFoundException");
			throw new LadeFehler();
		}
		catch (SecurityException e) {
			//System.out.println("SecurityException");
			throw new LadeFehler();
		}
		catch (StreamCorruptedException e) {
			//System.out.println("StreamCorruptedException");
			throw new LadeFehler();
		}
		catch (IOException e) {
			//System.out.println("IOException ");		
			throw new LadeFehler();
		}		 
		catch (Exception e) {
			//System.out.println("Exception ");
			throw new LadeFehler();
		}		 
		
		// hier Konten anlegen
		KontoDaten temp;
		for (int i=0; i<kontenDaten.size();i++){
			temp = ((KontoDaten) kontenDaten.elementAt(i));
			try{
  			neuesKonto(temp.kName, temp.sName, temp.sender, temp.replyTo,temp.pop,temp.smtp,temp.uName,temp.uPass,temp.savePass,temp.del);
  		} catch (KontoExistiertSchonFehler e) {}
  	}
  	temp=null;
  	kontenDaten=null;
	}


	public void kontoGBS() {
		if (kGBS==null || !kGBS.isShowing()) kGBS=new KontoGBS(this);
	}
	
   /**
    * @return void
    * @exception
    */
  void neuesKonto(String kName, String sName, String sender, String replyTo,String  pop,String smtp,String uName,String  uPass, boolean savePass, boolean del) throws KontoExistiertSchonFehler {
   	for (int i=0; i<konten.size();i++){
  		if (((Konto) konten.elementAt(i)).kName.equals(kName))
  			throw new KontoExistiertSchonFehler();
  	}
   	konten.add(new Konto(kName, sName, sender, replyTo, pop, smtp, uName, uPass, savePass, del));
  }
  
  public Konto gibKonto(String kName) throws KeinSolchesKontoFehler{
  	for (int i=0; i<konten.size();i++){
  		if (((Konto) konten.elementAt(i)).kName.equals(kName))
  			return (Konto) konten.elementAt(i);
  	}
  	throw new KeinSolchesKontoFehler();
  }

  /**
   * @return void
   * @exception KeinSolchesKontoFehler
   */
  void KontoLoeschen(String kName) throws KeinSolchesKontoFehler{
  	for (int i=0; i<konten.size();i++){
  		if (((Konto) konten.elementAt(i)).kName == kName){
  			konten.removeElementAt(i);
  			return;
  		}
  	}
  	throw new KeinSolchesKontoFehler();
	}

  /**
   * @return Vector
   * @exception 
   */
  public Vector gibAbsenderBitte(){
		Vector ergebnis = new Vector();
		for (int i=0;i<konten.size();i++) {
			ergebnis.add(((Konto) konten.elementAt(i)).kName);
		}
		return ergebnis;
	}	
	
	public void mailsAbrufen() {
		tF = new TransferGBS();
		for (int i=0;i<konten.size();i++) {
			((Konto) konten.elementAt(i)).kC=this;
			tF.konto.setText("Konto: " + ((Konto) konten.elementAt(i)).kName);
  		((Konto) konten.elementAt(i)).mailAbrufen();
  	}
  	tF.dispose();
	}
}	