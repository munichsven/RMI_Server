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
	private final int id;

	protected Client() throws RemoteException {
		super();
		id = Constant.createId();
		getRegistryAndregisterToServer();
		register();
		seatList = new LinkedList<Seat>();
		forkList = new LinkedList<Fork>();
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
			String name = "PhilClient" + this.id;
			server.insertIntoRegistry(name, stub);
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
		// TODO FORKS mit Seat
		int i = id * anz;
		anz = (id + 1) * anz;
		int j = i;
		int anz_j = anz;
		while (j < anz_j) {
			forkList.add(new Fork(j));
		}

		while (i < anz) {
			seatList.add(new Seat(this, i));
			i++;
		}
		for (int k = 0; k < seatList.size(); k++) {
			Seat crntSeat = seatList.get(k);
			crntSeat.setLeft(forkList.get(k));
			if (!seatList.get(k).equals(seatList.getLast())) {
				crntSeat.setRight(seatList.get(k + 1).getLeft());
			} else {
				// hier die linke gabel vom "nachbartisch auf dem anderen
				// clietn"
				// darf an aber erst machen, wenn man sicher ist, dass der Platz
				// mit der
				// Gabel auf dem anderen Client schon vorhanden ist
			}
		}
		printSeats();
	}
	
	private void printSeats(){
		for (Seat seat : seatList) {
			System.out.println("Client: "+ seat.getClient()+ " Sitzid: "+seat.getId()+ " Left: "+ seat.getLeft() + " Right: "+ seat.getRight());
		}
	}
}
