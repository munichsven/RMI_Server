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
	 * Gibt die Id des Clients zurueck
	 * @return ID
	 * @throws RemoteException
	 */
	public int getId() throws RemoteException;
	
	/**
	 * Gibt die Liste von Sitzen zurueck.
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
	 * Loescht einen bestimmten Philosoph vom Client. Wartet jedoch bis dieser
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
	 * Gibt den Namen des Clients zurueck.
	 * @return Name
	 * @throws RemoteException
	 */
	public String getClientName() throws RemoteException;
	
	/**
	 * Gibt die Gabel vom Nachbarclient zurueck.
	 * @return true - wenn erfolgreich
	 * @throws RemoteException
	 */
	public boolean releaseForkByNeighbor() throws RemoteException;

	/**
	 * Fuegt einen einzeln Philosoph hinzu nach der Initialisierungsphase 
	 * @param id - id Philosoph
	 * @throws RemoteException
	 */
	void addPhilosoph(final int id, final int eatCnt) throws RemoteException;
	
	/**
	 * Fuegt einen einzelnen neuen Philosophen hinzu nach Absturz eines Clients oder waehrend Betrieb.
	 * @param id
	 * @param eatCnt
	 * @param isHungry
	 * @throws RemoteException
	 */
	void addPausingPhilosoph(final int id, final int eatCnt, final boolean isHungry) throws RemoteException;
	
	
	/**
	 * Fuegt ein Seat zur Laufzeit ein mit uebergebener ID.
	 * @return true wenn es funktioniert hat
	 * @throws RemoteException
	 */
	public boolean integrateSeat(final int id) throws RemoteException;
	
	
	/**
	 * Loescht einen Sitz vom Tisch 
	 * @return true wenn es funktioniert hat.
	 * @throws RemoteException
	 */
	public boolean deleteSeat() throws RemoteException;
	
	public ArrayList<Philosoph> getPhilosophsList() throws RemoteException;
	
	/**
	 * Pausiert alle Philosophen um spaeter einen Sitz waehrend des Betriebs einzufuegen 
	 * @throws RemoteException
	 */
	public void pauseEating() throws RemoteException;
	
	/**
	 * Hebt die Pause fuer alle Philosophen wieder auf.
	 * @throws RemoteException
	 */
	public void reactivateEating() throws RemoteException;
	
	
	public void reinitializeSeats(int anz) throws RemoteException;
	
	/**
	 * Gibt zurueck ob noch ein Client verfuegbar ist.
	 * @throws RemoteException
	 */
	public boolean hasNeighborClient() throws RemoteException;
	
	/**
	 * Setzt auf false wenn eine Exception geworfen wird.
	 * @throws RemoteException
	 */
	public void setHasNeighborClient(boolean hasNeighborClient) throws RemoteException;
	
	
	public int[] getSeatIds() throws RemoteException;
}
