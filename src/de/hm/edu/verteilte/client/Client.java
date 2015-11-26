package de.hm.edu.verteilte.client;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import de.hm.edu.verteilte.controller.Constant;
import de.hm.edu.verteilte.server.ServerI;

public class Client extends UnicastRemoteObject implements ClientI{
	
	private ServerI server;

	protected Client() throws RemoteException {
		super();
		registerRMI();
		registerToServer();
	}
	
	private void registerToServer()
	{
		try 
		{
			server = (ServerI)Naming.lookup("//" + Constant.IP + "/PhilServer");
			String test = server.halloString("dsjafjkdsfjl");
			System.out.println(test);
			//id = server.register(this);
		}
		catch (Exception e) {
			System.out.println("Client Exception: " + e.getMessage());
		}
	}
	
	private void registerRMI()
	{
		try{
			LocateRegistry.createRegistry(1099);} 
		catch (RemoteException e) {}
		
		try {
			Naming.rebind("PhilClient", this);
			System.out.println("Client gestartet!");}
		catch (Exception e) {
			System.out.println("Server Exception: " + e.getMessage());}
	}
	
	public static void main(String[]args){
		try {
			new Client();
		} catch (RemoteException e) {}
	}
}
