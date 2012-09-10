package net.ZeePal.bukkit.Jobz.containers;

public class BufferedPayment {

	public final String player;
	public double amount;

	//Storage class for payments in the near future
	public BufferedPayment(final String person, final double pay) {
		player = person;
		amount = pay;
	}
}
