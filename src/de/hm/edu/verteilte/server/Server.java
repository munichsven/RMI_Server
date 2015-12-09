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
import de.hm.edu.verteilte.client.BackUpI;
import de.hm.edu.verteilte.controller.Constant;

public class Server extends UnicastRemoteObject implements ServerI {

	private static final long serialVersionUID = 1L;
	private Semaphore startSemaphore = new Semaphore(0);
	private Registry registry;

	protected Server() throws RemoteException {
		super();
		registerRMI();
	}

	public void registerRMI() {
		try {
			registry = LocateRegistry.createRegistry(Constant.PORT);
			registry.rebind("PhilServer", this);
			System.out.println("Server gestartet");
		} catch (RemoteException e) {
			System.out.println("Fehler beim Registrieren");
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		try {
			Server server = new Server();
			server.startSemaphore.acquire(2);
			for (String string : server.registry.list()) {
				System.out.println(string);
			}
			String client1Name = server.registry.list()[1];
			String client2Name = server.registry.list()[2];
			ClientI client1 = (ClientI) server.registry.lookup(client1Name);
			ClientI client2 = (ClientI) server.registry.lookup(client2Name);
			client1.setHasNeighborClient(true);
			client2.setHasNeighborClient(true);

			// initialisierungsphase, deswegen nicht maximal parallel
			client1.createSeats(Constant.SEATS / Constant.CLIENTS);
			client2.createSeats(Constant.SEATS / Constant.CLIENTS);

			client1.createPhilosophs(Constant.PHILOSOPHS / Constant.CLIENTS);
			client2.createPhilosophs(Constant.PHILOSOPHS / Constant.CLIENTS);

			String backUpStorage1Name = server.registry.list()[3];
			String backUpStorage2Name = server.registry.list()[4];
			BackUpI backUp1I = (BackUpI) server.registry.lookup(backUpStorage1Name);
			BackUpI backUp2I = (BackUpI) server.registry.lookup(backUpStorage2Name);
			BackUpThread backUpThread = new BackUpThread(backUp1I, backUp2I, client1, client2);
			backUpThread.start();

			Thread.sleep(Constant.TIME_UNTIL_NEW_PHILS_ARE_ADDED);
			client2.addPhilosoph(Constant.createId(), 0);
			client1.addPhilosoph(Constant.createId(), 0);
			
			client1.removePhilosoph(0);
			client1.removePhilosoph(1);
			
			client1.integrateSeat(Constant.createSeatId());
			
			client2.deleteSeat();
			
		} catch (RemoteException | InterruptedException | NotBoundException e) {
			e.printStackTrace();
		}
	}

	public int getRegistry() {
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
			e.printStackTrace();
		}
		return elementsCnt;
	}

	@Override
	public boolean insertIntoRegistry(String name, ClientI client) throws RemoteException {
		try {
			Naming.rebind(name, client);
		} catch (MalformedURLException e) {
			System.out.println("Bind funktioniert nicht!");
			e.printStackTrace();
		}
		System.out.println(name + " in Server-Registry eingetragen!");
		startSemaphore.release();
		return true;
	}

	@Override
	public boolean insertIntoRegistry(String name, BackUpI master) throws RemoteException {
		try {
			Naming.rebind(name, master);
		} catch (MalformedURLException e) {
			System.out.println("Bind funktioniert nicht");
			e.printStackTrace();
		}
		System.out.println(name + " in Server-Registry eingetragen!");
		return true;
	}

}
