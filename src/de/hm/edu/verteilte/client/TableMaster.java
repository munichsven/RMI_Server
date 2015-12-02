package de.hm.edu.verteilte.client;

import java.util.ArrayList;
import java.util.Arrays;

import de.hm.edu.verteilte.controller.Constant;

public class TableMaster extends Thread{
	private ArrayList<Philosoph> philList;
	private int minCount;

	public TableMaster(final ArrayList<Philosoph> philList) {
		this.philList = philList;
		minCount = 0;
	}

	public void run() {
		while (true) {
			int[] crntCounts = new int[philList.size()];
			int i = 0;
			for (Philosoph crntPhil : philList) {
				crntCounts[i] = crntPhil.getEatCounter();
				i++;
			}
			Arrays.sort(crntCounts);
			minCount = crntCounts[0];
			for (Philosoph crntPhil : philList) {
				if (crntPhil.getEatCounter() >= minCount + Constant.DIFFERENZ && !crntPhil.isBanned()) {
					
					crntPhil.setBanned(true);
					System.out.println("Phil " + crntPhil.getPhilosophsId() + " wird demnächst gebannt! Essvorgänge " + crntPhil.getEatCounter() + " / " + minCount);
				}
				else if(crntPhil.getEatCounter() < minCount + Constant.DIFFERENZ && crntPhil.isBanned()){
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
		}
	}


}
