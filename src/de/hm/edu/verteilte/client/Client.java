package de.hm.edu.verteilte.client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;

import de.hm.edu.verteilte.controller.Constant;
import de.hm.edu.verteilte.server.ServerI;

public class Client extends UnicastRemoteObject implements ClientI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ServerI server;
	private Registry registry;
	private int seats; 
	private LinkedList<Seat> seatList;
	private LinkedList<Fork> forkList;

	protected Client() throws RemoteException {
		super();
		getRegistryAndregisterToServer();
		register();
		seats = Constant.SEATS / Constant.CLIENTS;
		seatList = new LinkedList<Seat>();
		forkList = new LinkedList<Fork>();
		createSeats(seats);
	}

	private void getRegistryAndregisterToServer() {
		try {

			try {
				registry = LocateRegistry.getRegistry(Constant.IP_SERVER, Constant.PORT);
			} catch (RemoteException e) {
			}

			server = (ServerI) registry.lookup("PhilServer");
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
		} catch (RemoteException e) {
		}
	}

	@Override
	public void createSeats(int anz) throws RemoteException {
		//TODO FORKS mit Seat 
		for(int i = 0; i < anz; i++){
			seatList.add(new Seat(i, null , null));
		}
	}
}
