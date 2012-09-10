package net.ZeePal.bukkit.Jobz.tasks;

import net.ZeePal.bukkit.Jobz.Jobz;

public class EconomyDepositTask implements Runnable {

	final String whichPlayer;
	final double toDeposit;

	public EconomyDepositTask(final String player, final double deposit) {
		whichPlayer = player;
		toDeposit = deposit;
	}

	//Deposit payments to player in main thread
	@Override
	public void run() {
		Jobz.econ.depositPlayer(whichPlayer, toDeposit);
	}

}
