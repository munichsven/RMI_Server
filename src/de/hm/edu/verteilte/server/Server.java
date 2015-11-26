package de.hm.edu.verteilte.server;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class Server extends UnicastRemoteObject implements ServerI{
	
	protected Server() throws RemoteException {
		super();
		registerRMI();
	}
	
	public void registerRMI()
	{
		try
		{
			LocateRegistry.createRegistry(1099);

		} catch (RemoteException e) {}
		
		try {
			Naming.rebind("PhilServer", this);
			
			System.out.println("Server gestartet!");
		}
		catch (Exception e) {
			System.out.println("Server Exception: " + e.getMessage());
		}
	}

	@Override
	public String halloString(final String name) throws RemoteException {
		return name + "Halllllllllllllo";
	}
	
	public static void main(String [] args){
		try {
			new Server();
		} catch (RemoteException e) {}
	}


}
