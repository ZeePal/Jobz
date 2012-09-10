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

package net.ZeePal.bukkit.Jobz.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.gmail.nossr50.datatypes.SkillType;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.ZeePal.bukkit.Jobz.Jobz;
import net.ZeePal.bukkit.Jobz.containers.BlockValue;

public class ConfigProcessor {

	private final FileConfiguration config;
	private final File configFile;
	private final String fileName;
	private final SkillType skill;

	private final List<BlockValue> blockBreakValues = new ArrayList<BlockValue>();
	private final List<BlockValue> blockPlaceValues = new ArrayList<BlockValue>();

	public ConfigProcessor(final String fileName, final SkillType skill) {
		this.fileName = fileName;
		this.skill = skill;
		configFile = new File(Jobz.plugin.getDataFolder(), fileName);
		config = YamlConfiguration.loadConfiguration(configFile);
	}

	//Load custom config file and populate Lists of broken and placed block values
	public void loadConfig() {

		final InputStream defaultConfigStream = Jobz.plugin.getResource(fileName);
		if (defaultConfigStream != null) {
			final YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(defaultConfigStream);
			config.setDefaults(defaultConfig);
			config.options().copyDefaults(true);
			try {
				config.save(configFile);
			} catch (final IOException error) {
				Jobz.logger.severe("Unable to save config to " + configFile + "(" + error.toString() + "):");
				error.printStackTrace();
			}
		}

		final Set<String> configBlocks = config.getConfigurationSection("Blocks").getKeys(false);

		for (final String Block : configBlocks) {
			final short blockID = (short) config.getInt("Blocks." + Block + ".ID");
			final byte blockData = (byte) config.getInt("Blocks." + Block + ".Data");

			final double blockBreakValue = config.getDouble("Blocks." + Block + ".Break_Value");
			if (blockBreakValue != 0)
				blockBreakValues.add(new BlockValue(blockID, blockData, blockBreakValue, skill));

			final double blockPlaceValue = config.getDouble("Blocks." + Block + ".Place_Value");
			if (blockPlaceValue != 0)
				blockPlaceValues.add(new BlockValue(blockID, blockData, blockPlaceValue, skill));
		}

	}

	//Return a populated list of BlockValue's for broken blocks
	public List<BlockValue> getBlockBreakList() {
		return blockBreakValues;
	}

	//Return a populated list of BlockValue's for placed blocks
	public List<BlockValue> getBlockPlaceList() {
		return blockPlaceValues;
	}

}
