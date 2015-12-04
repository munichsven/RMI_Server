package de.hm.edu.verteilte.server;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedList;

import de.hm.edu.verteilte.client.ClientI;
import de.hm.edu.verteilte.client.Philosoph;
import de.hm.edu.verteilte.client.Seat;
import de.hm.edu.verteilte.controller.Constant;

public class BackUpThread extends Thread {

	ClientI client1;
	ClientI client2;
	ArrayList<Philosoph> philosophs1;
	ArrayList<Philosoph> philosophs2;
	LinkedList<Seat> seats1;
	LinkedList<Seat> seats2;

	public BackUpThread(final ClientI client1, final ClientI client2) {
		super();
		this.client1 = client1;
		this.client2 = client2;
		System.out.println("BBBBBBBBBBBBBBBBBBBBBB: " + client1);
		System.out.println("BBBBBBBBBBBBBBBBBBBBBB: " + client2);
	}

	@Override
	public void run() {
		boolean twoClientsRunning = true;
		while (twoClientsRunning) {
			try {
				Thread.sleep(Constant.BACKUP_RYTHM);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			try {
				philosophs1 = client1.getPhilosophsList();
				seats1 = client1.getSeatList();
			} catch (RemoteException e) {
				e.printStackTrace();
				System.out.println("***Erster Client nicht erreichbar!");
				twoClientsRunning = false;
				client1 = null;
			}
			try {
				philosophs2 = client2.getPhilosophsList();
				seats2 = client2.getSeatList();
			} catch (RemoteException e) {
				System.out.println("***Zweiter Client nicht erreichbar!");
				twoClientsRunning = false;
				client2 = null;
			}
		}
		
		System.out.println("***BackUpRoutine wird gestartet!");
		System.out.println(client1);
		System.out.println(client2);
		
		if (client1 != null) {
			System.out.println("***BackUpRoutine wird gestartet! *** 1");
			try {
				client1.pauseEating();
				client1.setHasNeighborClient(false);
				this.reloadSeatsAndPhilosophs(client1, client2);
			} catch (RemoteException | InterruptedException e) {
				System.out.println("Sollte nicht passieren!");
				e.printStackTrace();
			}
		} else if (client2 != null) {
			System.out.println("***BackUpRoutine wird gestartet! *** 2");
			try {
				client2.pauseEating();
				client2.setHasNeighborClient(false);
				System.out.println("***BackUpRoutine wird gestartet! *** 3");
				this.reloadSeatsAndPhilosophs(client2, client1);
			} catch (RemoteException | InterruptedException e) {
				System.out.println("Sollte nicht passieren!");
				e.printStackTrace();
			}
		}
		// wenn der noch laufende Thread dann tatsächlich pasuiert (ggf. noch
		// abfrage einbauen)
		// müssen plätze und philosophen auf dem pausierenden client neu erzeugt
		// / initialisiert werden
	}

	private void reloadSeatsAndPhilosophs(ClientI stoppedClient, ClientI runningClient) throws InterruptedException {
		// Wartezeit eibauen, bis wirklich der komplette Tisch leer ist und die
		// neuen Sitze erzeugt werden können
		System.out.println("***BackUpRoutine wird gestartet! *** 4");
		Thread.sleep(5000);
		System.out.println("***BackUpRoutine wird gestartet! *** 5");
		try {
			runningClient.reinitializeSeats(seats1.size() + seats2.size());
			ArrayList<Philosoph> philosophsToAdd = null;
			if (stoppedClient.getId() < runningClient.getId()) {
				philosophsToAdd = philosophs1;
			}
			else if (stoppedClient.getId() > runningClient.getId()){
				philosophsToAdd = philosophs2;
			}
			for(int i = 0; i < philosophsToAdd.size(); i++){
				Philosoph crntPhil = philosophsToAdd.get(i);
				runningClient.getPhilosophsList().add(crntPhil);
			}
			System.out.println("***BackUpRoutine wird gestartet! *** 6");
			runningClient.reactivateEating();
			System.out.println("***BackUpRoutine erfolgreich beendet!");
		} catch (RemoteException e) {
			e.printStackTrace();
			System.out.println("***BackUp-Fehler!!!");
		}
	}
}
