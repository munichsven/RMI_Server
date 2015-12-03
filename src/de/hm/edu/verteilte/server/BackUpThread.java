package de.hm.edu.verteilte.server;

import java.rmi.RemoteException;
import java.util.ArrayList;

import de.hm.edu.verteilte.client.ClientI;
import de.hm.edu.verteilte.client.Philosoph;
import de.hm.edu.verteilte.controller.Constant;

public class BackUpThread extends Thread {

	ClientI client1;
	ClientI client2;
	ArrayList<Philosoph> philosophs1;
	ArrayList<Philosoph> philosophs2;

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
			} //Aktualisierung alle 1000 millis
			try {
				philosophs1 = client1.getBackUpPhilosophs();
			} catch (RemoteException e) {
				System.out.println("***Erster Client nicht erreichbar!");
				twoClientsRunning = false;
				client1 = null;
			}
			try {
				philosophs2 = client2.getBackUpPhilosophs();
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
			} catch (RemoteException e) {
				System.out.println("Sollte nicht passieren!");
				e.printStackTrace();
			}
		} else if (client2 != null) {
			try {
				client2.pauseEating();
			} catch (RemoteException e) {
				System.out.println("Sollte nicht passieren!");
				e.printStackTrace();
			}
		}
	}
}
