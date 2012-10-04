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
import java.util.concurrent.TimeUnit;

import net.ZeePal.bukkit.Jobz.Jobz;
import net.ZeePal.bukkit.Jobz.containers.BufferedPayment;

public class BufferedWageThread implements Runnable {

	private static List<BufferedPayment> wages = new ArrayList<BufferedPayment>();
	
	private final long sleepTime;
	
	public BufferedWageThread(final long sleepLength) {
		sleepTime = sleepLength;
	}

	public static synchronized void add(final String player, final double pay) {
		for (final BufferedPayment payment : wages) {
			if (payment.player == player) {
				payment.amount += pay;
				return;
			}
		}
		wages.add(new BufferedPayment(player, pay));
	}

	@Override
	public void run() {
		for (;;) {
			loopWages();
			try {
				TimeUnit.MINUTES.sleep(sleepTime);
			} catch (InterruptedException e) {
				Jobz.logger.severe("Interrupt triggered while sleeping in the wage thread");
			}
		}
	}
	
	private synchronized void loopWages() {
		for (final BufferedPayment payment : wages) {
			if (payment.amount != 0)
				Jobz.scheduler.scheduleSyncDelayedTask(Jobz.plugin, new WageAnnounceTask(payment.player, Jobz.wageMessage.replace("{}", Double.toString(payment.amount))));
		}
		wages.clear();
	}
}
