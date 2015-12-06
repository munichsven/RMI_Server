package de.hm.edu.verteilte.server;

import java.rmi.RemoteException;

import de.hm.edu.verteilte.client.ClientI;
import de.hm.edu.verteilte.controller.Constant;

public class BackUpThread extends Thread {

	ClientI client1;
	ClientI client2;
	int[] philosophs1Ids;
	int[] philosophs2Ids;
	int[] philosophs1EatCnt;
	int[] philosophs2EatCnt;
	boolean[] philosophs1Hungry;
	boolean[] philosophs2Hungry;
	int[] seats1Ids;
	int[] seats2Ids;

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
			}
			try {
				philosophs1Ids = client1.getTableMaster().getPhilIdsBackup();
				philosophs1EatCnt = client1.getTableMaster().getEatCntsBackup();
				philosophs1Hungry = client1.getTableMaster().getAreHungryBackup();
				seats1Ids = client1.getSeatIds();
			} catch (RemoteException e) {
				e.printStackTrace();
				System.out.println("***Erster Client nicht erreichbar!");
				twoClientsRunning = false;
				client1 = null;
			}
			try {
				philosophs2Ids = client2.getTableMaster().getPhilIdsBackup();
				philosophs2EatCnt = client2.getTableMaster().getEatCntsBackup();
				philosophs2Hungry = client2.getTableMaster().getAreHungryBackup();
				seats2Ids = client2.getSeatIds();
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
			runningClient.reinitializeSeats(seats1Ids.length + seats2Ids.length);
			int[] philosophIdsToAdd = null;
			int[] philosophsEatCntToAdd = null;
			boolean[] philosophsHungryToAdd = null;
			if (stoppedClient.getId() < runningClient.getId()) {
				philosophIdsToAdd = philosophs1Ids;
				philosophsEatCntToAdd = philosophs1EatCnt;
				philosophsHungryToAdd = philosophs1Hungry;
			}
			else if (stoppedClient.getId() > runningClient.getId()){
				philosophIdsToAdd = philosophs2Ids;
				philosophsEatCntToAdd = philosophs2EatCnt;
				philosophsHungryToAdd = philosophs2Hungry;
			}
			for(int i = 0; i < philosophIdsToAdd.length; i++){
				runningClient.addPausingPhilosoph(philosophIdsToAdd[i], philosophsEatCntToAdd[i], philosophsHungryToAdd[i]);;
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
