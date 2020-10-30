package com.fump.filter;

import com.fump.*;
import javax.mail.search.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.mail.*;

/**
 * Threaded search utility.
 * <p><code>
 * Suche suche = new Suche(myFolder, mySearchTerm, true);
 * Thread sucheThread = new Thread(suche);
 * sucheThread.run();
 * </code><p>
 * Use addChangeListener() to receive notifications on search progress.
 * 
 * @author Voss
 * @version $Id: Suche.java,v 1.5 2001/07/11 16:58:41 stollber Exp $
 */
public class Suche implements Runnable {
	private boolean searching = false;
	private boolean done = false;
	private int folderCount;
	private int foldersDone;
	private ChangeListener listener;
	private Ordner folder;
	private SearchTerm searchTerm;
	private boolean searchSubFolders;
	private Vector mails = new Vector();

	/**
	 *  Setup thread.
	 */
	public Suche(Ordner folder, SearchTerm searchTerm, boolean searchSubFolders) {
		this.folder = folder;
		this.searchTerm = searchTerm;
		this.searchSubFolders = searchSubFolders;
	}	

	/**
	 * Returns all matching mails.
	 * @return matching mails (Vector of com.fump.Mail)
	 */
	public Vector getMails() {
		return mails;
	}
	
	/**
	 * Returns if search is complete.
	 * @return search done
	 */
	public boolean isDone() {
		return done;
	}
	
	/**
	 * Search progress in percent. Actually this is the percentage of folders searched, the effort
	 * to build the initial folder list is not included.
	 * @return percentage
	 */
	public int getProgress() {
		if (searching) { 
			Double progress = new Double((double) foldersDone / (double) folderCount * 100.0);
			return progress.intValue();
		} else {
			return 0;
		}
	}
	
	private void fireChangeEvent() {
		if (listener != null) listener.stateChanged(new ChangeEvent(this));
		Thread.currentThread().yield();			
	}
	
	/**
	 * Adds a change listener. Since only one listener is supported, this overwrites any
	 * previously defined listeners.
	 */
	public void addChangeListener(ChangeListener _listener) {
		listener = _listener;
	}
	

	
	public void run() {
		folderCount = 100;
		foldersDone = 0;
		searching = true;

		for (foldersDone = 0; foldersDone <= folderCount; foldersDone++) {
			try {
				Thread.currentThread().sleep(20);
			} catch (InterruptedException e) {}
			fireChangeEvent();
		}
		
		searching = false;		
		done = true;
		fireChangeEvent();
	}
	

	/**
	 *  Performs actual search.
	 */
	 /*
	public void run() {
		Vector folders = new Vector();
		folders.add(folder);
		if (searchSubFolders) folders.addAll(getChildFolders(folder));
		
		System.out.println("Suche: Durchsuche " + folders.size() + " Ordner...");
		
		folderCount = folders.size();
		foldersDone = 0;
		searching = true;
		mails = search(folders, searchTerm);
		searching = false;		
		done = true;
		fireChangeEvent();
	}
	*/
	
	/**
	 *  Searches the collection of folders contained in <code>ordner</code>.
	 *  @return matching mails, may be empty
	 */
	private Vector search(Vector ordner, SearchTerm searchTerm) {
		Vector mails = new Vector();
		Iterator ordnerIterator = ordner.iterator();
		while (ordnerIterator.hasNext()) {
			Vector matching = searchSingleFolder((Ordner) ordnerIterator.next(), searchTerm);
			if (matching != null) mails.addAll(matching);			
			foldersDone++;
			fireChangeEvent();
		}
		
		return mails;	
	}
	
	
	/**
	 * Returns all child folders (recursively).
	 * @return folders, may be empty if no child folders where found
	 */
	private Vector getChildFolders(Ordner ordner) {
		Vector folders = new Vector();
		
		// fetch this folder's child folders
		try {
			folders.addAll(ordner.gibAlleUnterordner());
		} catch (KeineUnterordnerDa e) {
			return folders;
		}
		
		// traverse child folders
		Iterator folderIterator = folders.iterator();
		while (folderIterator.hasNext()) {
			Ordner child = (Ordner) folderIterator.next();
			// add child folder's child folders to Vector
			Vector childs = getChildFolders(child);
			if (child != null) folders.addAll(childs);
		}
	
		return folders;
	}
	
	
	/**
	 * Returns mails from folder <code>ordner</code> that match <code>searchTerm</code>.
	 * @return matching mails, may be null if no mails match
	 */
	private Vector searchSingleFolder(Ordner ordner, SearchTerm searchTerm) {
		Iterator mails;
		
		// fetch all mails from folder
		try {
			mails = ordner.gibAlleMails().iterator();
		} catch (KeineMailsDa e) {
			return null;
		}
		
		// match all mails against SearchTerm, returning matching ones
		Vector foundMails = new Vector();
		while (mails.hasNext()) {
			Mail mail = (Mail) mails.next();
			if (searchTerm.match(mail)) {
				foundMails.add(mail);
			}
		}
		
		return foundMails;
	}


}
