package de.hm.edu.verteilte.server;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import de.hm.edu.verteilte.client.ClientI;

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
	
	public static void main(String [] args){
		try {
			new Server();
		} catch (RemoteException e) {}
	}

	@Override
	public boolean insertIntoRegistry(String name, ClientI client) throws RemoteException {
		Registry tmpReg = LocateRegistry.getRegistry(1099);
		tmpReg.rebind(name, client);
		System.out.println(name + " in Server-Registry eingetragen!");
		return true;
	}


}
