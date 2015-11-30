package de.hm.edu.verteilte.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientI extends Remote{
	
	public void createSeats(final int anz)throws RemoteException;

	public int getId() throws RemoteException;

}
