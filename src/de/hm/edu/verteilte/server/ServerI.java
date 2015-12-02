package de.hm.edu.verteilte.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import de.hm.edu.verteilte.client.ClientI;


public interface ServerI extends Remote{
	
	/**
	 * Schreibt den Client in die Registry 
	 * @throws RemoteException
	 */
	boolean insertIntoRegistry(String name, ClientI client) throws RemoteException;

	/**
	 * Gibt die Registry des Servers zurück
	 * @throws RemoteException
	 */
	int getRegistry() throws RemoteException;
}
