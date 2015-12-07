package de.hm.edu.verteilte.server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Semaphore;

import de.hm.edu.verteilte.client.ClientI;
import de.hm.edu.verteilte.controller.Constant;

public class Server extends UnicastRemoteObject implements ServerI{
	
	private Semaphore startSemaphore = new Semaphore(0);
	private Registry registry;
	
	protected Server() throws RemoteException {
		super();
		registerRMI();
	}
	
	public void registerRMI()
	{
		try
		{
			registry = LocateRegistry.createRegistry(Constant.PORT);
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
			for (String string : server.registry.list()) {
				System.out.println(string);
			}
			String client1Name = server.registry.list()[1];
			String client2Name = server.registry.list()[2];
			ClientI client1 = (ClientI) server.registry.lookup(client1Name);
			ClientI client2 = (ClientI) server.registry.lookup(client2Name);
			client1.setHasNeighborClient(true);
			client2.setHasNeighborClient(true);
					
			//initialisierungsphase, deswegen nicht maximal parallel
			client1.createSeats(Constant.SEATS/Constant.CLIENTS);
			client2.createSeats(Constant.SEATS/Constant.CLIENTS);
			
			client1.createPhilosophs(Constant.PHILOSOPHS/Constant.CLIENTS);
			client2.createPhilosophs(Constant.PHILOSOPHS/Constant.CLIENTS);
			
			//BackUpThread backUpThread = new BackUpThread(client1,client2);
			//backUpThread.start();
			
			Thread.sleep(Constant.TIME_UNTIL_NEW_PHILS_ARE_ADDED);
			client2.addPhilosoph(Constant.createId(),0);
			client1.addPhilosoph(Constant.createId(),0);
			client2.addPhilosoph(Constant.createId(), 0);
			client1.addPhilosoph(Constant.createId(), 0);
			client1.removePhilosoph(0);
			client1.removePhilosoph(1);
			client1.removePhilosoph(2);
			client1.integrateSeat(Constant.createSeatId());
			client1.integrateSeat(Constant.createSeatId());
			client2.deleteSeat();
			client2.deleteSeat();
			
			
			//Pl�tze n�ssen auch noch dynamisch hnzugef�gt werden k�nnen
									
		} catch (RemoteException | InterruptedException | NotBoundException e) {
			e.printStackTrace();
		}
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
		try {
			Naming.rebind(name, client);
		} catch (MalformedURLException e) {
			System.out.println("Bind funzt nicht");
			e.printStackTrace();
		}
		System.out.println(name + " in Server-Registry eingetragen!");
		startSemaphore.release();
		System.out.println("Verfügbare Permits: " + startSemaphore.availablePermits());
		return true;
	}


}
