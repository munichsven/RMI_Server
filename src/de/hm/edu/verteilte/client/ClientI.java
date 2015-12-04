package de.hm.edu.verteilte.client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
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
	void addPhilosoph(final int id, final int eatCnt) throws RemoteException;
	
	
	/**
	 * F�gt ein Seat zur Laufzeit ein mit �bergebener ID.
	 * @return true wenn es funktioniert hat
	 * @throws RemoteException
	 */
	public boolean integrateSeat(final int id) throws RemoteException;
	
	
	/**
	 * L�scht einen Sitz vom Tisch 
	 * @return true wenn es funktioniert hat.
	 * @throws RemoteException
	 */
	public boolean deleteSeat(final int id) throws RemoteException;
	
	public ArrayList<Philosoph> getPhilosophsList() throws RemoteException;
	
	public void pauseEating() throws RemoteException;
	
	public void reactivateEating() throws RemoteException;
	
	public void reinitializeSeats(int anz) throws RemoteException;
	
	public boolean hasNeighborClient() throws RemoteException;
	
	public void setHasNeighborClient(boolean hasNeighborClient) throws RemoteException;
}
