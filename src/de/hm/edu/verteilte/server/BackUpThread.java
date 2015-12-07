package de.hm.edu.verteilte.server;

import java.rmi.RemoteException;

import de.hm.edu.verteilte.client.BackUpI;
import de.hm.edu.verteilte.client.ClientI;
import de.hm.edu.verteilte.controller.Constant;

public class BackUpThread extends Thread {

	BackUpI backUp1;
	BackUpI backUp2;
	ClientI client1;
	ClientI client2;
	int[] philosophs1Ids;
	int[] philosophs2Ids;
	int[] philosophs1EatCnt;
	int[] philosophs2EatCnt;
	boolean[] philosophs1Hungry;
	boolean[] philosophs2Hungry;
	int seats1Cnt;
	int seats2Cnt;

	public BackUpThread(final BackUpI backUp1I, final BackUpI backUp2I, ClientI client1, ClientI client2) {
		//super();
		if(backUp1I == null){
			System.out.println("***BackUpThread Error! ************************");
		}
		this.backUp1 = backUp1I;
		this.backUp2 = backUp2I;
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
				philosophs1Ids = backUp1.getPhilIds();
				philosophs1EatCnt = backUp1.getEatCnts();
				philosophs1Hungry = backUp1.getAreHungry();
				seats1Cnt = backUp1.getSeatCnt();
			} catch (RemoteException e) {
				System.out.println("***Erster Client nicht erreichbar!");
				twoClientsRunning = false;
				client1 = null;
			}
			try {
				philosophs2Ids = backUp2.getPhilIds();
				philosophs2EatCnt = backUp2.getEatCnts();
				philosophs2Hungry = backUp2.getAreHungry();
				seats2Cnt = backUp2.getSeatCnt();
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
		// wenn der noch laufende Thread dann tats�chlich pasuiert (ggf. noch
		// abfrage einbauen)
		// m�ssen pl�tze und philosophen auf dem pausierenden client neu erzeugt
		// / initialisiert werden
	}

	private void reloadSeatsAndPhilosophs(ClientI stoppedClient, ClientI runningClient) throws InterruptedException {
		// Wartezeit eibauen, bis wirklich der komplette Tisch leer ist und die
		// neuen Sitze erzeugt werden k�nnen
		System.out.println("***BackUpRoutine wird gestartet! *** 4");
		try {
			runningClient.reinitializeSeats(this.seats1Cnt + this.seats2Cnt);
			int[] philosophIdsToAdd = null;
			int[] philosophsEatCntToAdd = null;
			boolean[] philosophsHungryToAdd = null;
			if (client1 == null) {
				philosophIdsToAdd = philosophs1Ids;
				philosophsEatCntToAdd = philosophs1EatCnt;
				philosophsHungryToAdd = philosophs1Hungry;
			}
			else if (client2 == null){
				philosophIdsToAdd = philosophs2Ids;
				philosophsEatCntToAdd = philosophs2EatCnt;
				philosophsHungryToAdd = philosophs2Hungry;
			}
			for(int i = 0; i < philosophIdsToAdd.length; i++){
				runningClient.addPausingPhilosoph(philosophIdsToAdd[i], philosophsEatCntToAdd[i], philosophsHungryToAdd[i]);;
			}
			System.out.println("***BackUpRoutine wird gestartet! *** 5");
			runningClient.reactivateEating();
			System.out.println("***BackUpRoutine erfolgreich beendet!");
		} catch (RemoteException e) {
			e.printStackTrace();
			System.out.println("***BackUp-Fehler!!!");
		}
	}
}
