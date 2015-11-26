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
			} catch (RemoteException e) {
			}
			
			 server = (ServerI)registry.lookup("PhilServer");
			 String test = server.halloString("dsjafjkdsfjl");
			 System.out.println(test);
			 //id = server.register(this);
		} catch (Exception e) {
			System.out.println("*** Client Exception: " + e.getMessage());
		}
	}

	private void register() {

		try {
			//registry.bind("PhilClient", this);
//			ServerIntf stub = (ServerIntf) UnicastRemoteObject.exportObject(helloServer, 0);
//			Registry registry = LocateRegistry.createRegistry(1088);
//			registry.rebind(nameOfHelloServer, stub);
			
			
			ClientI stub = (ClientI) this;
			System.out.println("***1");
			server.insertIntoRegistry("PhilServer", stub);
			System.out.println("***2");
			System.out.println("Client bei Server eingetragen!");
		} catch (Exception e) {
			System.out.println("*** Server Exception: " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		try {
			new Client();
		} catch (RemoteException e) {
		}
	}
}
