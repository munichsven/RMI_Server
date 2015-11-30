package de.hm.edu.verteilte.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Semaphore;

import de.hm.edu.verteilte.client.ClientI;
import de.hm.edu.verteilte.controller.Constant;

public class Server extends UnicastRemoteObject implements ServerI{
	
	Semaphore startSemaphore = new Semaphore(0);
	
	protected Server() throws RemoteException {
		super();
		registerRMI();
	}
	
	public void registerRMI()
	{
		try
		{
			Registry registry = LocateRegistry.createRegistry(Constant.PORT);
			registry.rebind("PhilServer", this);
			System.out.println("Server gestartet");
		} catch (RemoteException e) {
			System.out.println("Fehler beim Registrieren");
			e.printStackTrace();
		}
		
//		Alte Version --> funktioniert auch wenn Port already in Use		
//		try {
//			Naming.rebind("PhilServer", this);
//			
//			System.out.println("Server gestartet!");
//		}
//		catch (Exception e) {
//			System.out.println("Server Exception: " + e.getMessage());
//		}
	}
	
	public static void main(String [] args){
		try {
			Server server = new Server();
			server.startSemaphore.acquire(2);
			System.out.println("Semaphor erhalten");
		} catch (RemoteException | InterruptedException e) {}
	}
	
	public int getRegistry(){
		Registry registry;
		int elementsCnt = 0;
		try {
			registry = LocateRegistry.getRegistry();
			String[] elementsInRegistry = registry.list();
			for (String string : elementsInRegistry) {
				System.out.println(string);
			}
			elementsCnt = elementsInRegistry.length;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return elementsCnt;
	}

	@Override
	public boolean insertIntoRegistry(String name, ClientI client) throws RemoteException {
		Registry tmpReg = LocateRegistry.getRegistry(Constant.PORT);
		tmpReg.rebind(name, client);
		System.out.println(name + " in Server-Registry eingetragen!");
		startSemaphore.release();
		System.out.println("Verfügbare Permits: " + startSemaphore.availablePermits());
		return true;
	}


}
