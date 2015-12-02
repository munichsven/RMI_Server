package de.hm.edu.verteilte.client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

public interface ClientI extends Remote{
	
	/**
	 * Erstellt eine bestimmte Anzahl von Sitzen
	 * @param anz 
	 * @throws RemoteException
	 */
	public void createSeats(final int anz)throws RemoteException;

	/**
	 * Gibt die Id des Clients zur�ck
	 * @return ID
	 * @throws RemoteException
	 */
	public int getId() throws RemoteException;
	
	/**
	 * Gibt die Liste von Sitzen zur�ck.
	 * @return
	 * @throws RemoteException
	 */
	public LinkedList<Seat> getSeatList() throws RemoteException;
	
	/**
	 * Erstellt eine bestimmte Anzahl von Philosophen.
	 * @param philosophs
	 * @throws RemoteException
	 */
	public void createPhilosophs(final int philosophs) throws RemoteException;
	
	/**
	 * L�scht einen bestimmten Philosoph vom Client. Wartet jedoch bis dieser
	 * sein Essvorgang beendet hat.
	 * @param id
	 * @return
	 * @throws RemoteException
	 */
	public boolean removePhilosoph(final int id) throws RemoteException;
	
	/**
	 * Holt sich die Gabel vom Nachbarclient.
	 * @return true -  wenn erfolgreich.
	 * @throws RemoteException
	 */
	public boolean occupyForkForNeighbour() throws RemoteException; //Funktion um die benötigte gabel vom Nachbarclient zu holen

	/**
	 * Gibt den Namen des Clients zur�ck.
	 * @return Name
	 * @throws RemoteException
	 */
	public String getClientName() throws RemoteException;
	
	/**
	 * Gibt die Gabel vom Nachbarclient zur�ck.
	 * @return true - wenn erfolgreich
	 * @throws RemoteException
	 */
	public boolean releaseForkByNeighbor() throws RemoteException;

	/**
	 * F�gt einen einzeln Philosoph hinzu nach der Initialisierungsphase 
	 * @param id - id Philosoph
	 * @throws RemoteException
	 */
	void addPhilosoph(int id) throws RemoteException;
}
