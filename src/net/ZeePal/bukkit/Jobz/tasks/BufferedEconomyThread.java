package net.ZeePal.bukkit.Jobz.tasks;

import java.util.ArrayList;
import java.util.List;

import net.ZeePal.bukkit.Jobz.Jobz;
import net.ZeePal.bukkit.Jobz.containers.BufferedPayment;

public class BufferedEconomyThread implements Runnable {

	private static List<BufferedPayment> payments = new ArrayList<BufferedPayment>();

	//Add payments to buffer to be sent to the main thread in the near future
	public static synchronized void add(final String player, final double pay) {
		for (final BufferedPayment payment : payments) {
			if (payment.player == player) {
				payment.amount += pay;
				return;
			}
		}
		payments.add(new BufferedPayment(player, pay));
	}

	//Send payments to main thread for processing per person
	@Override
	public synchronized void run() {
		for (final BufferedPayment payment : payments) {
			if (payment.amount > 0) {
				Jobz.scheduler.scheduleSyncDelayedTask(Jobz.plugin, new EconomyDepositTask(payment.player, payment.amount));
			}else if (payment.amount < 0) {
				Jobz.scheduler.scheduleSyncDelayedTask(Jobz.plugin, new EconomyWithdrawTask(payment.player, -payment.amount));
			}
		}
		payments.clear();
	}
}
