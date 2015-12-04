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
	}

	@Override
	public void run() {
		boolean twoClientsRunning = true;
		while (twoClientsRunning) {
			try {
				Thread.sleep(Constant.BACKUP_RYTHM);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			} // Aktualisierung alle 1000 millis
			try {
				philosophs1 = client1.getPhilosophsList();
				seats1 = client1.getSeatList();
			} catch (RemoteException e) {
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
		// ein Client ausgefallen => auf dem anderen die Philosophen zum
		// pausieren auffordern
		if (client1 != null) {
			try {
				client1.pauseEating();
				client1.setHasNeighborClient(false);
				this.reloadSeatsAndPhilosophs(client1, client2);
			} catch (RemoteException | InterruptedException e) {
				System.out.println("Sollte nicht passieren!");
				e.printStackTrace();
			}
		} else if (client2 != null) {
			try {
				client2.pauseEating();
				client2.setHasNeighborClient(false);
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
		Thread.sleep(5000);
		try {
			runningClient.reinitializeSeats(seats1.size() + seats2.size());
			ArrayList<Philosoph> philosophsToAdd = null;
			if (stoppedClient.getId() < runningClient.getId()) {
				//einser ist gestoppt
				philosophsToAdd = philosophs1;
			}
			else if (stoppedClient.getId() > runningClient.getId()){
				//zweier ist gestoppt
				philosophsToAdd = philosophs2;
			}
			//Philosophen dem anderen Client hinzufügen
			for(int i = 0; i < philosophsToAdd.size(); i++){
				Philosoph crntPhil = philosophsToAdd.get(i);
				runningClient.getPhilosophsList().add(crntPhil);
			}
			runningClient.reactivateEating();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
