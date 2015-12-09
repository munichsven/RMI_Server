package de.hm.edu.verteilte.client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class BackUpStorage extends UnicastRemoteObject implements BackUpI{


	private static final long serialVersionUID = 1L;
	private final ClientI client;
	private int[] philIds;
	private int[] eatCnts;
	private boolean[] areHungry;
	private int seatCnt;
	
	protected BackUpStorage(final ClientI clientI) throws RemoteException {
		super();
		this.client = clientI;
	}

	public int[] getPhilIds() {
		return philIds;
	}

	public void setPhilIds(int[] philIds) {
		this.philIds = philIds;
	}

	public int[] getEatCnts() {
		return eatCnts;
	}

	public void setEatCnts(int[] eatCnts) {
		this.eatCnts = eatCnts;
	}

	public boolean[] getAreHungry() {
		return areHungry;
	}

	public void setAreHungry(boolean[] areHungry) {
		this.areHungry = areHungry;
	}

	public int getSeatCnt() {
		return seatCnt;
	}

	public void setSeatCnt(int seatCnt) {
		this.seatCnt = seatCnt;
	}

	public ClientI getClient() {
		return client;
	}

	

}
