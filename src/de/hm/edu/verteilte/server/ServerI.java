package de.hm.edu.verteilte.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import de.hm.edu.verteilte.client.ClientI;
import de.hm.edu.verteilte.client.BackUpI;

/**
 * Interface für den Server.
 * @author benutzer
 *
 */
public interface ServerI extends Remote{
	
	/**
	 * Schreibt den Client in die Registry 
	 * @throws RemoteException
	 */
	boolean insertIntoRegistry(String name, ClientI client) throws RemoteException;
	
	/**
	 * Schreibt den Backupclient in die Registry
	 * @throws RemoteException
	 */
	boolean insertIntoRegistry(String name, BackUpI master) throws RemoteException;

	/**
	 * Gibt die Registry des Servers zurueck
	 * @throws RemoteException
	 */
	int getRegistry() throws RemoteException;
}
