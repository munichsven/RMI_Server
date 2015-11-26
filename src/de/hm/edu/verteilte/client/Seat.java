package de.hm.edu.verteilte.client;

import java.util.concurrent.Semaphore;

public class Seat {
	private final int id;
	private final Semaphore semaphore;
	private final Fork right;
	private final Fork left;

	public Seat(final int id, final Fork right, final Fork left) {
		this.id = id;
		this.right = right;
		this.left = left;
		this.semaphore = new Semaphore(1);
	}

	public int getId() {
		return id;
	}

	public Semaphore getSemaphore() {
		return semaphore;
	}

	public Fork getRight() {
		return right;
	}

	public Fork getLeft() {
		return left;
	}
}
