package de.hm.edu.verteilte.client;

import de.hm.edu.verteilte.controller.Constant;

public class Philosoph extends Thread{

	private ClientI client;
	private final int id;
	private final boolean hungry;
	private final int eatMax;
	private int counter;
	private int eatCounter;
	
	public Philosoph(final ClientI client, final int id, final boolean hungry){
		this.client = client;
		this.id = id;
		this.hungry = hungry;
		
		counter = 0;
		eatCounter = 0;
		if (isHungry())
			eatMax = Constant.EAT_MAX_HUNGRY;
		else
			eatMax = Constant.EAT_MAX_NORMAL;
	}
	
	/**
	 * @return counter - gibt den Aktuellen Essenstand des Philosophen zurück.
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
}
