package de.hm.edu.verteilte.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BackUpI extends Remote{

	/**
	 *  Gibt die Liste der IDs der Philiosophen zurueck falls ein Client ausfaellt.
	 * @return Liste von den IDs der Philiosophen
	 * @throws RemoteException
	 */
	public int[] getPhilIds()throws RemoteException;


	/**
	 *  Um die Philsids regelmaessig zu aktualieseren werden staendig die Philids gesetzt.
	 * @param Liste von den IDs der Philiosophen
	 * @throws RemoteException
	 */
	public void setPhilIds(int[] philIds)throws RemoteException;


	/**
	 *  Gibt die Liste der Eatcounter  der Philiosophen zurueck falls ein Client ausfaellt.
	 * @return Liste der Eatcounter der Philiosophen
	 * @throws RemoteException
	 */
	public int[] getEatCnts()throws RemoteException;

	/**
	 * Um die Eatcounter regelmaessig zu aktualieseren werden staendig die Philids gesetzt.
	 * @param eatCnts - Liste der aktuellen Eatcounts
	 * @throws RemoteException
	 */
	public void setEatCnts(int[] eatCnts) throws RemoteException;

	/**
	 * Gibt eine Liste der Philiosophen zurueck welcher von ihnen hungrig ist, falls ein Client ausfaellt.
	 * @return Liste welcher Philiosoph hungrig ist.
	 * @throws RemoteException
	 */
	public boolean[] getAreHungry()throws RemoteException;

	/**
	 * Um die Eatcounter regelmaessig zu aktualieseren werden staendig die Philids gesetzt.
	 * @param areHungry - Liste von hungrigen Phils
	 * @throws RemoteException
	 */
	public void setAreHungry(boolean[] areHungry)throws RemoteException;

	/**
	 * Gibt die Anzahl der Sitze zurueck fals ein Client ausfaellt.
	 * @return - Anzahl Sitze
	 * @throws RemoteException
	 */
	public int getSeatCnt()throws RemoteException;

	/**
	 * Setzt die aktuelle Anzahl der Sitze.
	 * @param seatCnt - aktuelle Anzahl Sitze
	 * @throws RemoteException
	 */
	public void setSeatCnt(int seatCnt) throws RemoteException;
	
}
