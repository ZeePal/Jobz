/*********************************************************************************
 *  The Jobz plugin for Bukkit allows players to earn in game money for playing the game
 *	Copyright (C) 2012  ZeePal
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
*********************************************************************************/

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
