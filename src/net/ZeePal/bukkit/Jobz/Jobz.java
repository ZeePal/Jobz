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

package net.ZeePal.bukkit.Jobz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
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
	
	public static CopyOnWriteArrayList<String> excludedPlayerNames = new CopyOnWriteArrayList<String>();
	
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
		
		final List<BlockValue> blockBreakValues = new ArrayList<BlockValue>();
		
		blockBreakValues.addAll(miningConfig.getBlockBreakList());
		blockBreakValues.addAll(woodcuttingConfig.getBlockBreakList());
		blockBreakValues.addAll(excavationConfig.getBlockBreakList());
		blockBreakValues.addAll(herbalismConfig.getBlockBreakList());
		Collections.sort(blockBreakValues, BlockValue.Comparator);
		
		final List<BlockValue> blockPlaceValues = new ArrayList<BlockValue>();
		
		blockPlaceValues.addAll(miningConfig.getBlockPlaceList());
		blockPlaceValues.addAll(woodcuttingConfig.getBlockPlaceList());
		blockPlaceValues.addAll(excavationConfig.getBlockPlaceList());
		blockPlaceValues.addAll(herbalismConfig.getBlockPlaceList());
		Collections.sort(blockPlaceValues, BlockValue.Comparator);
		
		blockBreakValueListStorage.blockValues.clear();
		blockBreakValueListStorage.blockValues.addAll(blockBreakValues);
		
		blockPlaceValueListStorage.blockValues.clear();
		blockPlaceValueListStorage.blockValues.addAll(blockPlaceValues);
		
		excludedPlayerNames.clear();
		excludedPlayerNames.addAll(config.getStringList("ExcludedPlayers"));

	}

}