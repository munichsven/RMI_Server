package de.hm.edu.verteilte.client;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import de.hm.edu.verteilte.controller.Constant;

public class Philosoph extends Thread {

	private Client client;
	private final int id;
	// Ob Philosoph hungrig ist.
	private final boolean hungry;
	// Wie oft der Philosoph essen darf bevor er schlafen gelegt wird.
	private final int eatMax;
	// Wie oft der Philosoph gegessen hat
	private int counter;
	// Wie oft der Philosoph schon gegessen hat bis er schlaf gelegt wird
	private int eatCounter;
	private LinkedList<Seat> seatList;
	// Ob der Philosoph vom Tisch verbannd wurde da er zuviel gegessen hat
	private boolean banned = false;
	private boolean paused = false;
	private Random random;
	private boolean killed;

	public Philosoph(final Client client, final int id, final boolean hungry, LinkedList<Seat> seatList,
			final int eatCnt) {
		System.out.println("Philosoph erzeugt: " + id);
		this.client = client;
		this.id = id;
		this.hungry = hungry;
		this.seatList = seatList;
		this.random = new Random();
		this.eatCounter = eatCnt;
		this.killed = false;
		counter = 0;
		if (isHungry())
			eatMax = Constant.EAT_MAX_HUNGRY;
		else
			eatMax = Constant.EAT_MAX_NORMAL;
	}

	public void run() {
		while (!killed) {
			// Wenn Philosoph gebannt ist wird extra schlafen gelegt.
			if (banned || paused) {
				System.out.println("***Philosoph " + this.getPhilosophsId() + " pausiert oder ist gebannt.");
				banFromTable();
				while (banned || paused) {
					System.out.println("***Philosoph " + this.getPhilosophsId() + " pausiert oder ist gebannt.");
					banFromTable();
				}
				// Andernfalls geht der Philosoph meditieren und essen.
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

	/**
	 * Gibt zur�ck ob der Philosoph gebannt ist oder nicht
	 * 
	 * @return
	 */
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
			System.out.println("***Bann-Fehler!");
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
	
	public void setSeatList(LinkedList<Seat> seatList){
		this.seatList = seatList;
	}

	public void eat() {
		boolean seatFound = false;
		Seat crntSeat = null;
		int seatCount = seatList.size();
		// Setzt den Start Index wo der Philosoph anf�ngt einen Platz zu suchen
		int startIndex = random.nextInt(seatCount);
		int index = startIndex;
		int tries = 0;
		// Geht die verschieden Sitze durch um einen Sitzplatz zu bekommen
		while (!seatFound && tries < (3 * seatCount)) {
			crntSeat = seatList.get(index);
			seatFound = crntSeat.getSemaphore().tryAcquire();

			if (index == seatCount - 1) {
				index = 0;
			} else {
				index++;
			}
			tries++;
		}

		if (!seatFound) {
			crntSeat = seatList.get(startIndex);
			try {
				System.out.println("Philosoph " + this.getPhilosophsId() + " wartet an Platz " + crntSeat.getId());
				crntSeat.getSemaphore().acquire(); // in warteschlange anstellen
				seatFound = true;
				System.out.println("Philosoph " + this.getPhilosophsId() + " wartete erfolgreich an Platz " + crntSeat.getId());
			} catch (InterruptedException e) {
				System.out.println("***Warteproblem!");
				e.printStackTrace();
			}
		}
		//if (seatFound) { //wir wissen dass wir auf jeden Fall einen gefunden hat !!!!!oben in der !seatFound-if setzten wir seatFound nie auf true
			System.out.println("Philosoph " + this.getPhilosophsId() + " hat Sitz gefunden: Nr: " + crntSeat.getId());
			boolean forksFound = false;
			while (!forksFound) {
				forksFound = getForks(crntSeat);
			}

			threadBreak(Constant.EAT_LENGTH);
			// Gibt die Semaphoren zur�ck damit sich jemand anders wieder
			// hinsetzen kann

			releaseForks(crntSeat);
			crntSeat.getSemaphore().release();
			counter++;
			eatCounter++;
			System.out.println("Philosoph " + getPhilosophsId() + " hat an Platz " + crntSeat.getId() + " zum insg. "
					+ eatCounter + ". mal gegessen.  :" + isHungry());
		//}
	}

	private void releaseForks(Seat crntSeat) {
		Fork left = crntSeat.getLeft();
		left.getSemaphore().release();
		if (crntSeat.equals(seatList.getLast()) && this.client.hasNeighborClient()) {
			System.out.println("Philosoph " + this.getPhilosophsId() + " gibt Nachbargabel zur�ck!");
			this.client.callNeighborToReleaseFork();
		} else {
			crntSeat.getRight().getSemaphore().release();
		}
	}

	private boolean getForks(final Seat seat) {
		Fork left = seat.getLeft();
		boolean hasRight = false;
		boolean hasBoth = false;

		while (!hasBoth) {
			int tries = 0;
			try {
				left.getSemaphore().acquire();
			} catch (InterruptedException e1) {
				System.out.println("This shouldnt happen.");
				e1.printStackTrace();
			}

			while (!hasRight && tries <= Constant.TRIES_TO_GET_FORK) {
				// Unterscheidung ob rechte Gabel lokal oder remote liegt
				if (seat.equals(seatList.getLast()) && this.client.hasNeighborClient()) {
					hasRight = this.client.callNeighborToBlockFork();
				} else {
					try {
						hasRight = seat.getRight().getSemaphore().tryAcquire(Constant.TIME_UNTIL_NEW_FORKTRY,
								TimeUnit.MILLISECONDS);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				tries++;
			}
			hasBoth = hasRight;
			if (!hasBoth) {
				left.getSemaphore().release();
				try {
					Thread.sleep(Constant.TIME_UNTIL_NEW_FORKTRY);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return hasBoth;
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public void setKilled(boolean killed) {
		this.killed = killed;
	}
}
