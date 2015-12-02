package de.hm.edu.verteilte.client;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

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
	private ArrayList<Philosoph> philosophList;
	private int id = 0;
	private Random random;
	private int hungryPeople = 0;
	private String clientName;
	private String neighborName;

	protected Client() throws RemoteException {
		super();
		getRegistryAndregisterToServer();
		register();
		seatList = new LinkedList<Seat>();
		forkList = new LinkedList<Fork>();
		philosophList = new ArrayList<Philosoph>();
		random = new Random();
	}

	private void getRegistryAndregisterToServer() {
		try {

			try {
				registry = LocateRegistry.getRegistry(Constant.IP_SERVER,
						Constant.PORT);
			} catch (RemoteException e) {
			}

			server = (ServerI) registry.lookup("PhilServer");
		} catch (Exception e) {
			System.out.println("*** Client Exception: " + e.getMessage());
		}
	}

	public LinkedList<Seat> getSeatList() {
		return seatList;
	}

	// inkl. Benennung des Clients und des rechten Nachbarclients
	private void register() {

		ClientI stub = (ClientI) this;
		clientName = "PhilClient" + id;

		try {
			registry.lookup(clientName); // prüfen ob id schon verwendet wird!
			id++;
			neighborName = clientName;
			clientName = "PhilClient" + id;
		} catch (AccessException e) {
			System.out.println("***RegistryFehler");
		} catch (RemoteException e) {
			System.out.println("***RegistryFehler");
		} catch (NotBoundException e) {
			// erster Namensvorschlag wird genommen
			System.out.println(id);
			int neighborsClientNumber = id + 1;
			if (neighborsClientNumber == Constant.CLIENTS) {
				neighborsClientNumber = 0;
			}
			neighborName = "PhilClient" + neighborsClientNumber;
		}
		System.out.println("Eigener Name: " + this.clientName);
		System.out.println("Nachbar's Name: " + this.neighborName);

		try {
			server.insertIntoRegistry(this.clientName, stub);
			System.out.println("Client bei Server eingetragen!");
		} catch (RemoteException e) {
			System.out.println("***RegistryFehler");
		}
	}

	public static void main(String[] args) {
		try {
			new Client();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void createSeats(int anz) throws RemoteException {
		int i = id * anz;
		anz = (id + 1) * anz;
		int j = i;
		int anz_j = anz;
		while (j < anz_j) {
			forkList.add(new Fork(j));
			j++;
		}

		while (i < anz) {
			seatList.add(new Seat(this, i));
			i++;
		}
		for (int k = 0; k < seatList.size(); k++) {
			Seat crntSeat = seatList.get(k);
			crntSeat.setLeft(forkList.get(k));
			if (!seatList.get(k).equals(seatList.getFirst())) {
				seatList.get(k - 1).setRight(crntSeat.getLeft());
			} else {
				// hier die linke gabel vom "nachbartisch auf dem anderen
				// clietn"
				// darf an aber erst machen, wenn man sicher ist, dass der Platz
				// mit der
				// Gabel auf dem anderen Client schon vorhanden ist
			}
		}
	}

	private void printSeats() throws RemoteException {
		for (Seat seat : seatList) {
			System.out.println("Client: " + seat.getClient().getId()
					+ " Sitzid: " + seat.getId() + " Left: " + seat.getLeft()
					+ " Right: " + seat.getRight());
		}
	}

	@Override
	public int getId() throws RemoteException {
		return this.id;
	}

	@Override
	public void createPhilosophs(int philosophs) {
		int i = id * philosophs;
		philosophs = (id + 1) * philosophs;

		while (i < philosophs) {
			Philosoph phil = new Philosoph(this, i, randomHungry(), seatList);
			philosophList.add(phil);
			phil.start();
			i++;
		}
	}

	/**
	 * �berpr�ft ob die Maximale Anzahl von hungrigen Philosophen erreicht
	 * ist, wenn nicht wird ein Random boolean zur�ck gegeben.
	 * 
	 * @return hungry - gibt zur�ck ob der Philosoph hungrig ist oder nicht.
	 */
	private boolean randomHungry() {
		final boolean hungry;
		if (hungryPeople < Constant.HUNGRY_PHILOSOPHS / Constant.CLIENTS) {
			hungry = random.nextBoolean();

			if (hungry) {
				hungryPeople++;
			}

			return hungry;
		} else
			return false;
	}

	@Override
	public boolean removePhilosoph(int id) {
		// 1. Prüfen -> Hab ich den Philosophen überhaupt
		boolean philDeleted = false;
		int i = 0;
		while (i < philosophList.size() && !philDeleted) {
			Philosoph philosoph = philosophList.get(i);
			if (philosoph.getPhilosophsId() == id) {
				philosoph.setKilled(true);
				// philosophList.remove(i); //ist das in Ordung, oder brauchen
				// wir ihn noch?
				philDeleted = true; // gibt an, das er in nächster Zukunft
									// gelöscht wird
				System.out.println("Philsoph" + philosoph.getPhilosophsId()
						+ " wurde aus Client:" + this.id + " entfernt");
			}
		}
		return philDeleted;
	}

	@Override
	public boolean occupyForkForNeighbour() throws RemoteException {
		Fork sharedFork = forkList.getFirst();
		boolean successful = false;
		try {
			successful = sharedFork.getSemaphore().tryAcquire(Constant.TIME_TO_GET_RIGHT_FORK, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Obacht! Hier k�nnte noch ein Problem mit dem Semaphor und der boolean-Var. vorliegen!");
		}
		return successful;
	}

	public boolean callNeighborToBlockFork(){
		boolean gotFork = false;
		try {
			ClientI neighborClient = (ClientI) this.registry.lookup(neighborName);
			gotFork = neighborClient.occupyForkForNeighbour();
			System.out.println("Gabel: " + " vom Nachbarn bekommen: " + neighborClient.getClientName() + "  wirklich?: " + gotFork);
		} catch (RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return gotFork;
	}

	@Override
	public String getClientName() throws RemoteException {
		return this.clientName;
	}
	
	public void callNeighborToReleaseFork(){
		ClientI neighborClient;
		try {
			neighborClient = (ClientI) this.registry.lookup(neighborName);
			neighborClient.releaseForkByNeighbor();
		} catch (RemoteException | NotBoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public boolean releaseForkByNeighbor() throws RemoteException {
		this.seatList.getLast().getRight().getSemaphore().release();
		return true;
	}

	
}
