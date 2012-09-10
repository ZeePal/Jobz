package net.ZeePal.bukkit.Jobz;

import java.util.Collections;
import java.util.logging.Logger;

import com.gmail.nossr50.datatypes.SkillType;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import net.ZeePal.bukkit.Jobz.config.ConfigProcessor;
import net.ZeePal.bukkit.Jobz.containers.BlockValue;
import net.ZeePal.bukkit.Jobz.containers.BlockValueListStorage;
import net.ZeePal.bukkit.Jobz.listeners.BlockBreakListener;
import net.ZeePal.bukkit.Jobz.listeners.BlockPlaceListener;
import net.ZeePal.bukkit.Jobz.tasks.BufferedEconomyThread;

public class Jobz extends JavaPlugin {

	public static Jobz plugin = null;
	public static Logger logger = null;
	public static BukkitScheduler scheduler = null;
	public static Economy econ = null;
	
	public static BlockValueListStorage blockBreakValueListStorage = null;
	public static BlockValueListStorage blockPlaceValueListStorage = null;
	
	private FileConfiguration config = null;

	//Enable plugin
	@Override
	public void onEnable() {
		getLogger().info("onEnabled has been invoked!");

		plugin = this;
		logger = getLogger();
		config = getConfig();
		scheduler = getServer().getScheduler();

		if (!setupEconomy()) {
			getLogger().info("Disabled! Unable to setup Economy.");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		loadConfigurations();

		scheduler.scheduleAsyncRepeatingTask(this, new BufferedEconomyThread(),	config.getLong("EconomyThread.Init_Wait_Time"),	config.getLong("EconomyThread.Repeat_Every"));

		getServer().getPluginManager().registerEvents(new BlockBreakListener(),	this);
		getServer().getPluginManager().registerEvents(new BlockPlaceListener(),	this);
	}

	//Disable plugin
	@Override
	public void onDisable() {
		getLogger().info("onDisable has been invoked!");

		HandlerList.unregisterAll(this);
		getServer().getScheduler().cancelTasks(this);

		econ = null;
		scheduler = null;
		config = null;
		logger = null;
		plugin = null;
		blockPlaceValueListStorage = null;
		blockBreakValueListStorage = null;
	}

	//Find and Load Economy plugin (Vault)
	private boolean setupEconomy() {
		final RegisteredServiceProvider<Economy> econProvider = getServer().getServicesManager().getRegistration(Economy.class);
		if (econProvider == null) {
			return false;
		}
		econ = econProvider.getProvider();
		return econ != null;
	}

	//Load all config's including making default config files if missing
	private void loadConfigurations() {

		config.options().copyDefaults(true);
		saveConfig();

		blockBreakValueListStorage = new BlockValueListStorage(
				(short) config.getInt("mcMMO.BlockBreak.Mining.Level_Bonus_Before_Divide"),
				(short) config.getInt("mcMMO.BlockBreak.Mining.Divid_Level_by"),
				(short) config.getInt("mcMMO.BlockBreak.Woodcutting.Level_Bonus_Before_Divide"),
				(short) config.getInt("mcMMO.BlockBreak.Woodcutting.Divid_Level_by"),
				(short) config.getInt("mcMMO.BlockBreak.Excavation.Level_Bonus_Before_Divide"),
				(short) config.getInt("mcMMO.BlockBreak.Excavation.Divid_Level_by"),
				(short) config.getInt("mcMMO.BlockBreak.Herbalism.Level_Bonus_Before_Divide"),
				(short) config.getInt("mcMMO.BlockBreak.Herbalism.Divid_Level_by"));
		
		blockPlaceValueListStorage = new BlockValueListStorage(
				(short) config.getInt("mcMMO.BlockPlace.Mining.Level_Bonus_Before_Divide"),
				(short) config.getInt("mcMMO.BlockPlace.Mining.Divid_Level_by"),
				(short) config.getInt("mcMMO.BlockPlace.Woodcutting.Level_Bonus_Before_Divide"),
				(short) config.getInt("mcMMO.BlockPlace.Woodcutting.Divid_Level_by"),
				(short) config.getInt("mcMMO.BlockPlace.Excavation.Level_Bonus_Before_Divide"),
				(short) config.getInt("mcMMO.BlockPlace.Excavation.Divid_Level_by"),
				(short) config.getInt("mcMMO.BlockPlace.Herbalism.Level_Bonus_Before_Divide"),
				(short) config.getInt("mcMMO.BlockPlace.Herbalism.Divid_Level_by"));

		final ConfigProcessor miningConfig = new ConfigProcessor("Mining.yml", SkillType.MINING);
		final ConfigProcessor woodcuttingConfig = new ConfigProcessor("Woodcutting.yml", SkillType.WOODCUTTING);
		final ConfigProcessor excavationConfig = new ConfigProcessor("Excavation.yml", SkillType.EXCAVATION);
		final ConfigProcessor herbalismConfig = new ConfigProcessor("Herbalism.yml", SkillType.HERBALISM);

		miningConfig.loadConfig();
		woodcuttingConfig.loadConfig();
		excavationConfig.loadConfig();
		herbalismConfig.loadConfig();

		blockBreakValueListStorage.writeLock();
		try {
			blockBreakValueListStorage.blockValues.addAll(miningConfig.getBlockBreakList());
			blockBreakValueListStorage.blockValues.addAll(woodcuttingConfig.getBlockBreakList());
			blockBreakValueListStorage.blockValues.addAll(excavationConfig.getBlockBreakList());
			blockBreakValueListStorage.blockValues.addAll(herbalismConfig.getBlockBreakList());
			Collections.sort(blockBreakValueListStorage.blockValues, BlockValue.Comparator);
		} finally {
			blockBreakValueListStorage.writeUnlock();
		}

		blockPlaceValueListStorage.writeLock();
		try {
			blockPlaceValueListStorage.blockValues.addAll(miningConfig.getBlockPlaceList());
			blockPlaceValueListStorage.blockValues.addAll(woodcuttingConfig.getBlockPlaceList());
			blockPlaceValueListStorage.blockValues.addAll(excavationConfig.getBlockPlaceList());
			blockPlaceValueListStorage.blockValues.addAll(herbalismConfig.getBlockPlaceList());
			Collections.sort(blockPlaceValueListStorage.blockValues, BlockValue.Comparator);
		} finally {
			blockPlaceValueListStorage.writeUnlock();
		}

	}

}