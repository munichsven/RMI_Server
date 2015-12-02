package de.hm.edu.verteilte.client;

import java.util.List;
import java.util.Random;

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
	private Random random;
	private boolean killed;
	
	public Philosoph(final ClientI client, final int id, final boolean hungry, List<Seat> seatList){
		System.out.println("Philosoph erzeugt: " + id);
		this.client = client;
		this.id = id;
		this.hungry = hungry;
		this.seatList = seatList;
		this.random = new Random();
		this.killed = false;
		counter = 0;
		eatCounter = 0;
		if (isHungry())
			eatMax = Constant.EAT_MAX_HUNGRY;
		else
			eatMax = Constant.EAT_MAX_NORMAL;
	}
	
	public void run() {
		while (!killed) {
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
		System.out.println("Thread von Philosoph: " + this.getPhilosophsId() + " wurde beendet!");
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
	
	public void eat() {
		boolean seatFound = false;
		Seat crntSeat = null;
		int seatCount = seatList.size(); //Sitzabzahl ändert sich möglicherweise
		final int startIndex = random.nextInt(seatCount);
		int index = startIndex;
		int tries = 0;
		
		while (!seatFound && tries < 3*seatCount) {
			crntSeat = seatList.get(index);
			seatFound = crntSeat.getSemaphore().tryAcquire();

			if (index == seatCount - 1) {
				index = 0;
			} else {
				index++;
			}
			tries++;
		}
		
		if(!seatFound){
			crntSeat = seatList.get(startIndex);
			try {
				crntSeat.getSemaphore().acquire(); //in warteschlange anstellen
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Philosoph: "+ this.getPhilosophsId() + " hat Sitz gefunden: Nr: " + crntSeat.getId());

		crntSeat.getSemaphore().release();
		/*
		getForks(crntSeat);
		threadBreak(Constants.EAT_LENGTH);
		crntSeat.getLeft().getSemaphore().release();
		crntSeat.getRight().getSemaphore().release();
		crntSeat.getSemaphore().release();
		counter++;
		eatCounter++;
		System.out.println("Philosoph " + getPhilosophsId() + " hat an Platz "
				+ crntSeat.getId() + " zum insg. " + eatCounter
				+ ". mal gegessen.  :" + isHungry());
		*/
	}
	
	public void setKilled(boolean killed) {
		this.killed = killed;
	}
}
