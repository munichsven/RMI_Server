package de.hm.edu.verteilte.client;

import java.util.List;

import de.hm.edu.verteilte.controller.Constant;

public class Philosoph extends Thread{

	private ClientI client;
	private final int id;
	private final boolean hungry;
	private final int eatMax;
	private int counter;
	private int eatCounter;
	private List<Seat> seatList;
	private boolean banned = false;
	
	public Philosoph(final ClientI client, final int id, final boolean hungry, List<Seat> seatList){
		this.client = client;
		this.id = id;
		this.hungry = hungry;
		this.seatList = seatList;
		
		counter = 0;
		eatCounter = 0;
		if (isHungry())
			eatMax = Constant.EAT_MAX_HUNGRY;
		else
			eatMax = Constant.EAT_MAX_NORMAL;
	}
	
	public void run() {
		while (true) {
			if (banned) {
				System.out.println(this.id + " interrupted");
				banFromTable();
				while (banned) {
					banFromTable();
				}
			} else {
				meditate();
				eat();
				if (counter == eatMax) {
					regenerate();
					counter = 0;
				}
			}
		}
	}
	
	/**
	 * Philosoph schl�ft.
	 */
	public void regenerate() {
		threadBreak(Constant.SLEEP_LENGTH);
	}

	/**
	 * Philosoph meditiert.
	 */
	public void meditate() {
		threadBreak(Constant.MEDITATE_LENGTH);
	}
	
	private void threadBreak(final long timeToBreak) {
		try {
			Thread.sleep(timeToBreak);
		} catch (InterruptedException e) {
		}
	}
	
	public boolean isBanned() {
		return banned;
	}

	public void setBanned(boolean banned) {
		this.banned = banned;
	}

	private void banFromTable() {
		try {
			Thread.sleep(Constant.EAT_LENGTH * Constant.BAN_FACTOR);
		} catch (InterruptedException e) {
		}
	}
	
	/**
	 * @return counter - gibt den Aktuellen Essenstand des Philosophen zur�ck.
	 */
	public int getEatCounter() {
		return eatCounter;
	}

	/**
	 * @return id - gibt den Namen bzw. die Id des Philosophen
	 */
	public int getPhilosophsId() {
		return id;
	}

	/**
	 * @return ishungry - gibt an Ob der Philosoph hungrig ist.
	 */
	public boolean isHungry() {
		return hungry;
	}
	
	public void eat(){
		
	}
}
