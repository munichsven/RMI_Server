package de.hm.edu.verteilte.client;

import java.util.concurrent.Semaphore;

public class Fork {
	private final int id;
	private final Semaphore semaphore;

	public Fork(final int id){
		this.id = id;
		this.semaphore = new Semaphore(1);
	}
	
	public int getId() {
		return id;
	}
	
	public Semaphore getSemaphore() {
		return semaphore;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Fork other = (Fork) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
