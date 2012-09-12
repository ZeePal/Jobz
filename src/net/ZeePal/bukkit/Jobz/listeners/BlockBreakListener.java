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

package net.ZeePal.bukkit.Jobz.listeners;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import net.ZeePal.bukkit.Jobz.Jobz;
import net.ZeePal.bukkit.Jobz.tasks.BlockProcessorThread;

public class BlockBreakListener implements Listener {

	//Run block search in separate thread to leave the main thread to do more important stuff
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockBreakEvent(final BlockBreakEvent event){
		final Block block = event.getBlock();
		Jobz.scheduler.scheduleAsyncDelayedTask(Jobz.plugin, new BlockProcessorThread(event.getPlayer().getName(), (short) block.getTypeId(), block.getData(), Jobz.blockBreakValueListStorage));
	}

}
