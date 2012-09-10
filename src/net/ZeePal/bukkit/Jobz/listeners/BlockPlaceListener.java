package net.ZeePal.bukkit.Jobz.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import net.ZeePal.bukkit.Jobz.Jobz;
import net.ZeePal.bukkit.Jobz.tasks.BlockProcessorThread;

public class BlockPlaceListener implements Listener {

	//Run block search in separate thread to leave the main thread to do more important stuff
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockPlaceEvent(final BlockPlaceEvent event){
		Jobz.scheduler.scheduleAsyncDelayedTask(Jobz.plugin, new BlockProcessorThread(event.getPlayer().getName(), (short) event.getBlock().getTypeId(), event.getBlock().getData(), Jobz.blockPlaceValueListStorage));
	}

}
