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
	 * Gibt die Id des Clients zurï¿½ck
	 * @return ID
	 * @throws RemoteException
	 */
	public int getId() throws RemoteException;
	
	/**
	 * Gibt die Liste von Sitzen zurï¿½ck.
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
	 * Lï¿½scht einen bestimmten Philosoph vom Client. Wartet jedoch bis dieser
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
	 * Gibt den Namen des Clients zurï¿½ck.
	 * @return Name
	 * @throws RemoteException
	 */
	public String getClientName() throws RemoteException;
	
	/**
	 * Gibt die Gabel vom Nachbarclient zurï¿½ck.
	 * @return true - wenn erfolgreich
	 * @throws RemoteException
	 */
	public boolean releaseForkByNeighbor() throws RemoteException;

	/**
	 * Fï¿½gt einen einzeln Philosoph hinzu nach der Initialisierungsphase 
	 * @param id - id Philosoph
	 * @throws RemoteException
	 */
	void addPhilosoph(final int id, final int eatCnt) throws RemoteException;
	
	/**
	 * Fügt einen einzelnen neuen Philosophen hinzu nach Absturz eines Clients.
	 * @param id
	 * @param eatCnt
	 * @param isHungry
	 * @throws RemoteException
	 */
	void addPausingPhilosoph(final int id, final int eatCnt, final boolean isHungry) throws RemoteException;
	
	
	/**
	 * Fï¿½gt ein Seat zur Laufzeit ein mit ï¿½bergebener ID.
	 * @return true wenn es funktioniert hat
	 * @throws RemoteException
	 */
	public boolean integrateSeat(final int id) throws RemoteException;
	
	
	/**
	 * Lï¿½scht einen Sitz vom Tisch 
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
	
	public TableMaster getTableMaster() throws RemoteException;
	
	public int[] getSeatIds() throws RemoteException;
}
