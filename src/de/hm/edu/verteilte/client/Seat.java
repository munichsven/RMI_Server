package de.hm.edu.verteilte.client;

import java.util.concurrent.Semaphore;

public class Seat {
	private final int id;
	private final Semaphore semaphore;
	private Fork right;
	private Fork left;
	private ClientI client;

	public Seat(final ClientI client, final int id) {
		this.id = id;
		this.client = client;
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

	public void setRight(Fork right) {
		this.right = right;
	}

	public void setLeft(Fork left) {
		this.left = left;
	}

	public void setClient(final ClientI client) {
		this.client = client;
	}

	public ClientI getClient() {
		return client;
	}
}
