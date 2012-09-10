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
