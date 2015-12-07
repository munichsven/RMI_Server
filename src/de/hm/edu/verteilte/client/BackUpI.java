package de.hm.edu.verteilte.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BackUpI extends Remote{

	public int[] getPhilIds()throws RemoteException;

	public void setPhilIds(int[] philIds)throws RemoteException;

	public int[] getEatCnts()throws RemoteException;

	public void setEatCnts(int[] eatCnts) throws RemoteException;

	public boolean[] getAreHungry()throws RemoteException;

	public void setAreHungry(boolean[] areHungry)throws RemoteException;

	public int getSeatCnt()throws RemoteException;

	public void setSeatCnt(int seatCnt) throws RemoteException;
	
}
