package de.hm.edu.verteilte.client;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;

import de.hm.edu.verteilte.controller.Constant;

public class TableMaster extends Thread {
	private ArrayList<Philosoph> philList = new ArrayList<>();
	private final Client client;
	private int minCount;
	private int[] philIdsBackup;
	private int[] eatCntsBackup;
	private boolean[] areHungryBackup; // Info für Backupthread

	public TableMaster(final Client client) {
		this.client = client;
		minCount = 0;
	}

	/**
	 * Geht die kompletten Philosophen durch und überprüft ob eine zu große
	 * Differenz zwischen ihnen ist. Falls ja wird der Philosoph verbannt vom
	 * Tisch für eine gewisse Zeit.
	 */
	public void run() {
		boolean clientIsRunning = true;
		while (clientIsRunning) {

			while (this.philList.size() <= 0) {
				try {
					philList = this.client.getPhilosophsList();
				} catch (RemoteException e1) {
					System.out.println("Client des TableMasters wurde beendet!");
					clientIsRunning = false;
				}
				try {
					Thread.sleep(Constant.GET_PHILLIST_RYTHM_INITIAL);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			int[] crntCounts = new int[philList.size()];
			philIdsBackup = new int[philList.size()];
			areHungryBackup = new boolean[philList.size()];
			int i = 0;
			// Holt sich alle Counter und sotiert diese nach dem kleinesten
			for (Philosoph crntPhil : philList) {
				crntCounts[i] = crntPhil.getEatCounter();
				philIdsBackup[i] = crntPhil.getPhilosophsId();
				areHungryBackup[i] = crntPhil.isHungry();
				i++;
			}
			eatCntsBackup = crntCounts;
			Arrays.sort(crntCounts);
			minCount = crntCounts[0];
			// Überprüft die jeweiligen Philosophen und berechnet die Differenz
			// zwischen dem wo am
			// meisten gegessen hat und am wenigstens und liegt den Philosoph
			// gegebenfalls schlafen
			for (Philosoph crntPhil : philList) {
				if (crntPhil.getEatCounter() >= minCount + Constant.DIFFERENZ && !crntPhil.isBanned()) {

					crntPhil.setBanned(true);
					System.out.println("Phil " + crntPhil.getPhilosophsId() + " wird demnächst gebannt! Essvorgänge "
							+ crntPhil.getEatCounter() + " / " + minCount);
				} else if (crntPhil.getEatCounter() < minCount + Constant.DIFFERENZ && crntPhil.isBanned()) {
					crntPhil.setBanned(false);
					System.out.println("Phil " + crntPhil.getPhilosophsId() + " wurde entbannt!");
				}
			}
			try {
				Thread.sleep(Constant.EAT_LENGTH);
			} catch (InterruptedException e) {
				System.out.println("Tablemaster unterbrochen!");
				e.printStackTrace();
			}
			try {
				this.philList = client.getPhilosophsList();
			} catch (RemoteException e) {
				System.out.println("Client des TableMasters wurde beendet!");
				clientIsRunning = false;
			}
		}
	}

	public int[] getPhilIdsBackup() {
		return philIdsBackup;
	}

	public int[] getEatCntsBackup() {
		return eatCntsBackup;
	}

	public boolean[] getAreHungryBackup() {
		return areHungryBackup;
	}
}
