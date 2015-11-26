package de.hm.edu.verteilte.client;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import de.hm.edu.verteilte.controller.Constant;
import de.hm.edu.verteilte.server.ServerI;

public class Client extends UnicastRemoteObject implements ClientI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ServerI server;
	private Registry registry;
	
	protected Client() throws RemoteException {
		super();
		getRegistryAndregisterToServer();
		register();
	}

	private void getRegistryAndregisterToServer() {
		try {
			
			try {
				registry = LocateRegistry.getRegistry("192.168.56.102",1099);
			} catch (RemoteException e) {}
			
			 server = (ServerI)registry.lookup("PhilServer");
		} catch (Exception e) {
			System.out.println("*** Client Exception: " + e.getMessage());
		}
	}

	private void register() {

		try {
			
			ClientI stub = (ClientI) this;
			server.insertIntoRegistry("PhilServer", stub);
			System.out.println("Client bei Server eingetragen!");
		} catch (Exception e) {
			System.out.println("*** Server Exception: " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		try {
			new Client();
		} catch (RemoteException e) {}
	}
}
