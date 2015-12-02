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

	/**
	 * Gibt die ID des Sitzes zurück
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gibt den Semaphoren zurück
	 * @return
	 */
	public Semaphore getSemaphore() {
		return semaphore;
	}

	/**
	 * gibt die Rechte Gabel zurück
	 * @return
	 */
	public Fork getRight() {
		return right;
	}

	/**
	 * Gibt die linke Gabel zurück
	 * @return
	 */
	public Fork getLeft() {
		return left;
	}

	/**
	 * Setzt die Rechte Gabel.
	 * @param right
	 */
	public void setRight(Fork right) {
		this.right = right;
	}

	/**
	 * Setzt die Linke Gabel
	 * @param left
	 */
	public void setLeft(Fork left) {
		this.left = left;
	}

	/**
	 * Setzt den Client
	 * @param client
	 */
	public void setClient(final ClientI client) {
		this.client = client;
	}

	/**
	 * Gibt den Client zurück.
	 * @return
	 */
	public ClientI getClient() {
		return client;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((left == null) ? 0 : left.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Seat other = (Seat) obj;
		if (id != other.id)
			return false;
		if (left == null) {
			if (other.left != null)
				return false;
		} else if (!left.equals(other.left))
			return false;
		return true;
	}
	
}
