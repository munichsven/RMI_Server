package de.hm.edu.verteilte.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerI extends Remote{
	public String halloString(String name) throws RemoteException;
}
