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

	private static final long serialVersionUID = 1L;

	/**
	 * Startet den Client
	 */
	public static void main(String[] args) {
		try {
			new Client();
		} catch (RemoteException e) {
			e.printStackTrace();
			System.out.println("***Fehler beim Erstellen des Clients");
		}
	}

	private ServerI server;
	private Registry registry;
	private LinkedList<Seat> seatList;
	private LinkedList<Fork> forkList;
	private ArrayList<Philosoph> philosophList;
	private int clientId = 0;
	private Random random;
	private int hungryPeopleCnt = 0;
	private String clientName;
	private String neighborName;

	private boolean hasNeighborClient;
	private final TableMaster master;

	protected Client() throws RemoteException {
		super();
		getRegistryAndregisterToServer();
		register();
		seatList = new LinkedList<Seat>();
		forkList = new LinkedList<Fork>();
		philosophList = new ArrayList<Philosoph>();
		master = new TableMaster(this);
		master.start();
		random = new Random();
	}

	@Override
	public void addPhilosoph(final int id, final int eatCnt) throws RemoteException {
		Philosoph phil = new Philosoph(this, id, randomHungry(), seatList, eatCnt);
		philosophList.add(phil);
		phil.start();
	}
	
	@Override
	public void addPausingPhilosoph(final int id, final int eatCnt, final boolean isHungry) throws RemoteException {
		Philosoph phil = new Philosoph(this, id, isHungry, seatList, eatCnt);
		phil.setPaused(true);
		philosophList.add(phil);
		phil.start();
	}

	@Override
	public void createPhilosophs(int philosophs) {
		int i = clientId * philosophs;
		philosophs = (clientId + 1) * philosophs;

		while (i < philosophs) {
			Philosoph phil = new Philosoph(this, i, randomHungry(), seatList, 0);
			philosophList.add(phil);
			phil.start();
			i++;
		}
	}

	@Override
	public void createSeats(int anz) throws RemoteException {
		// Berechnet die passende ID f�r die jeweiligen Sitze auf den
		// verschieden Clients
		int i = clientId * anz;
		anz = (clientId + 1) * anz;
		int j = i;
		int anz_j = anz;
		// Erstellt die linken Gabel des jeweiligen Sitzes
		while (j < anz_j) {
			forkList.add(new Fork(j));
			j++;
		}

		// Erstellt die Sitze
		while (i < anz) {
			seatList.add(new Seat(this, i));
			i++;
		}
		// Legt die Rechte Gabel f�r den Sitz fest.
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

	@Override
	public boolean deleteSeat(int id) throws RemoteException {
		return false;
	}

	@Override
	public String getClientName() throws RemoteException {
		return this.clientName;
	}

	@Override
	public int getId() throws RemoteException {
		return this.clientId;
	}

	@Override
	public ArrayList<Philosoph> getPhilosophsList() throws RemoteException {
		return philosophList;
	}

	@Override
	public boolean hasNeighborClient() {
		return hasNeighborClient;
	}

	@Override
	public boolean integrateSeat(int id) throws RemoteException {
		return false;
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
		if(successful){
			System.out.println("Eigene Gabel wurde f�r den Nachbarn blockiert!");
		}
		return successful;
	}

	/**
	 * Setzt alle Philosophen dieses Clients auf Pause.
	 */
	@Override
	public void pauseEating() throws RemoteException {
		for (Philosoph philosoph : philosophList) {
			philosoph.setPaused(true);
		}
		System.out.println("Alle Philosophen auf pasuiert gesetzt!");
	}

	@Override
	public boolean releaseForkByNeighbor() throws RemoteException {
		this.forkList.getFirst().getSemaphore().release();
		System.out.println("Eigene Gabel wurde durch den Nachbarn freigegeben.");
		return true;
	}

	@Override
	public boolean removePhilosoph(int id) {
		boolean philDeleted = false;
		int i = 0;
		while (!philDeleted && i < philosophList.size()) {
			Philosoph philosoph = philosophList.get(i);
			// Wenn Philosoph gefunden aus List l�schen und killed auf true
			// setzen
			if (philosoph.getPhilosophsId() == id) {
				philosoph.setKilled(true);
				System.out.println(
						"Philsoph" + philosoph.getPhilosophsId() + " wurde aus Client:" + this.clientId + " entfernt");
				philosophList.remove(i);
				philDeleted = true;
			}
			i++;
		}
		return philDeleted;
	}
	
	/**
	 * Erzeugt anhand einer �bergebenen Anzahl anz, alle Plaetze, inkl. Gabeln neu.
	 */
	@Override
	public void reinitializeSeats(int anz) throws RemoteException {
		this.seatList.removeAll(seatList);
		this.forkList.removeAll(forkList);
		System.out.println("Seatlist sollte 0 sein ---> " + seatList.size());

		// Erstellt alle Gabeln
		for (int i = 0; i < anz; i++) {
			forkList.add(new Fork(i));
		}

		// Erstellt alle Sitze
		for (int i = 0; i < anz; i++) {
			seatList.add(new Seat(this, i));
		}

		// den Sitzen ihre Gabeln zuweisen
		for (int i = 0; i < anz - 1; i++) {
			seatList.get(i).setLeft(forkList.get(i));
			seatList.get(i).setRight(forkList.get(i + 1));
		}
		seatList.getLast().setLeft(forkList.getLast());
		seatList.getLast().setRight(forkList.getFirst());

		this.printSeats(); // Testausgabe
	}
	
	@Override
	public void reactivateEating() throws RemoteException {
		for (Philosoph philosoph : philosophList) {
			philosoph.setPaused(false);
		}
		System.out.println("Alle Philosophen von Pause befreit.");
	}
	
	@Override
	public int[] getSeatIds(){
		int[] seatIds = new int[this.seatList.size()];
		for(int i = 0; i < seatList.size(); i++){
			seatIds[i] = seatList.get(i).getId();
		}
		return seatIds;
	}

	public boolean callNeighborToBlockFork() {

		boolean gotFork = false;
		try {
			if (hasNeighborClient) {
				ClientI neighborClient = (ClientI) this.registry.lookup(neighborName);
				gotFork = neighborClient.occupyForkForNeighbour();
			}
		} catch (RemoteException | NotBoundException e) {
			this.handleClientFailure(e);
		}
		return gotFork;
	}

	public void callNeighborToReleaseFork() {
		ClientI neighborClient;
		try {
			neighborClient = (ClientI) this.registry.lookup(neighborName);
			neighborClient.releaseForkByNeighbor();
		} catch (RemoteException | NotBoundException e1) {
			this.handleClientFailure(e1);
		}
	}
	
	public LinkedList<Seat> getSeatList() {
		return seatList;
	}

	/**
	 * Registiert sich am Server bzw. in der Registry des Servers
	 */
	private void getRegistryAndregisterToServer() {
		try {

			try {
				registry = LocateRegistry.getRegistry(Constant.IP_SERVER, Constant.PORT);
			} catch (RemoteException e) {
				System.out.println("***Registrierungsfehler");
			}

			server = (ServerI) registry.lookup("PhilServer");
		} catch (Exception e) {
			System.out.println("***Client Exception: " + e.getMessage());
		}
	}

	private void handleClientFailure(Exception e) {
		this.setHasNeighborClient(false);
		System.out.println("***Client: " + neighborName + " ist ausgefallen!");
		e.printStackTrace();
		try {
			this.pauseEating();
		} catch (RemoteException e1) {
			System.out.println("Sollte nicht passieren!");
		}
	}

	/**
	 * Test Methode um die Sitze Ausgeben zu k�nnen.
	 */
	private void printSeats() throws RemoteException {
		for (Seat seat : seatList) {
			System.out.println("Client: " + seat.getClient().getId() + " Sitzid: " + seat.getId() + " Left: "
					+ seat.getLeft() + " Right: " + seat.getRight());
		}
	}

	/**
	 * �berpr�ft ob die Maximale Anzahl von hungrigen Philosophen erreicht ist,
	 * wenn nicht wird ein Random boolean zur�ck gegeben.
	 * 
	 * @return hungry - gibt zur�ck ob der Philosoph hungrig ist oder nicht.
	 */
	private boolean randomHungry() {
		final boolean hungry;
		if (hungryPeopleCnt < Constant.HUNGRY_PHILOSOPHS / Constant.CLIENTS) {
			hungry = random.nextBoolean();

			if (hungry) {
				hungryPeopleCnt++;
			}

			return hungry;
		} else
			return false;
	}

	

	private void register() {
		ClientI stub = (ClientI) this;
		clientName = "PhilClient" + clientId;
		try {
			registry.lookup(clientName); // pr�fen ob id schon verwendet wird!
			clientId++;
			neighborName = clientName;
			clientName = "PhilClient" + clientId;
		} catch (AccessException e) {
			System.out.println("***RegistryFehler");
		} catch (RemoteException e) {
			System.out.println("***RegistryFehler");
		} catch (NotBoundException e) {
			// erster Namensvorschlag wird genommen
			int neighborsClientNumber = clientId + 1;
			if (neighborsClientNumber == Constant.CLIENTS) {
				neighborsClientNumber = 0;
			}
			neighborName = "PhilClient" + neighborsClientNumber;
		}
		try {
			server.insertIntoRegistry(this.clientName, stub);
			System.out.println("Client bei Server eingetragen!");
		} catch (RemoteException e) {
			System.out.println("***RegistryFehler");
		}
	}

	public void setHasNeighborClient(boolean hasNeighborClient) {
		this.hasNeighborClient = hasNeighborClient;
	}

	public TableMaster getTableMaster() {
		return master;
	}
}
