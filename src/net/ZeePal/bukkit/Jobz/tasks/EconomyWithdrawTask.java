package net.ZeePal.bukkit.Jobz.tasks;

import net.ZeePal.bukkit.Jobz.Jobz;

public class EconomyWithdrawTask implements Runnable {

	final String whichPlayer;
	final double toWithdraw;

	public EconomyWithdrawTask(final String player, final double withdraw) {
		whichPlayer = player;
		toWithdraw = withdraw;
	}

	//Deposit payments to player in main thread
	@Override
	public void run() {
		Jobz.econ.withdrawPlayer(whichPlayer, toWithdraw);
	}

}
