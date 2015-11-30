package de.hm.edu.verteilte.server;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import de.hm.edu.verteilte.client.ClientI;
import de.hm.edu.verteilte.controller.Constant;

public class Server extends UnicastRemoteObject implements ServerI{
	
	protected Server() throws RemoteException {
		super();
		registerRMI();
	}
	
	public void registerRMI()
	{
		try
		{
			LocateRegistry.createRegistry(Constant.PORT);

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
	
	public void printRegistry(){
		Registry registry;
		try {
			registry = LocateRegistry.getRegistry();
			String[] elementsInRegistry = registry.list();
			for (String string : elementsInRegistry) {
				System.out.println(string);
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	@Override
	public boolean insertIntoRegistry(String name, ClientI client) throws RemoteException {
		Registry tmpReg = LocateRegistry.getRegistry(Constant.PORT);
		tmpReg.rebind(name, client);
		System.out.println(name + " in Server-Registry eingetragen!");
		return true;
	}


}
