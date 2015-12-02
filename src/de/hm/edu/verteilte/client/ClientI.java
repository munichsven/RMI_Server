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
	 * Gibt die Id des Clients zurück
	 * @return ID
	 * @throws RemoteException
	 */
	public int getId() throws RemoteException;
	
	/**
	 * Gibt die Liste von Sitzen zurück.
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
	 * Löscht einen bestimmten Philosoph vom Client. Wartet jedoch bis dieser
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
	public boolean occupyForkForNeighbour() throws RemoteException; //Funktion um die benÃ¶tigte gabel vom Nachbarclient zu holen

	/**
	 * Gibt den Namen des Clients zurück.
	 * @return Name
	 * @throws RemoteException
	 */
	public String getClientName() throws RemoteException;
	
	/**
	 * Gibt die Gabel vom Nachbarclient zurück.
	 * @return true - wenn erfolgreich
	 * @throws RemoteException
	 */
	public boolean releaseForkByNeighbor() throws RemoteException;

	/**
	 * Fügt einen einzeln Philosoph hinzu nach der Initialisierungsphase 
	 * @param id - id Philosoph
	 * @throws RemoteException
	 */
	void addPhilosoph(int id) throws RemoteException;
}
