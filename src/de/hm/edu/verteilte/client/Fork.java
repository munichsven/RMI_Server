package de.hm.edu.verteilte.client;

import java.util.concurrent.Semaphore;

public class Fork {
	private final int id;
	private final Semaphore semaphore;

	public Fork(final int id){
		this.id = id;
		this.semaphore = new Semaphore(1);
	}
	
	/**
	 * @return Gibt Id zurück.
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Fork other = (Fork) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
