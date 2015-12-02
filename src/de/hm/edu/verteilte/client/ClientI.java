package de.hm.edu.verteilte.client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

public interface ClientI extends Remote{
	
	public void createSeats(final int anz)throws RemoteException;

	public int getId() throws RemoteException;
	
	public LinkedList<Seat> getSeatList() throws RemoteException;
	
	public void createPhilosophs(final int philosophs) throws RemoteException;
	
	public boolean removePhilosoph(final int id) throws RemoteException;
	
	public void addPhilosoph(final int id) throws RemoteException;
	
	public boolean occupyForkForNeighbour(final int forkId) throws RemoteException; //Funktion um die benötigte gabel vom Nachbarclient zu holen

}
