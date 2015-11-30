package de.hm.edu.verteilte.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import de.hm.edu.verteilte.client.ClientI;


public interface ServerI extends Remote{
	
	boolean insertIntoRegistry(String name, ClientI client) throws RemoteException;

	int getRegistry() throws RemoteException;
}
