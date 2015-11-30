package de.hm.edu.verteilte.client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

public interface ClientI extends Remote{
	
	public void createSeats(final int anz)throws RemoteException;

	public int getId() throws RemoteException;
	
	public LinkedList<Seat> getSeatList() throws RemoteException;
	
	public void createPhilosophs(final int philosophs);
	
	public void removePhilosoph(final int id);

}
