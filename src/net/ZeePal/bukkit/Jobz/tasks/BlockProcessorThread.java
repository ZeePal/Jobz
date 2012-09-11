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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.gmail.nossr50.datatypes.SkillType;

import net.ZeePal.bukkit.Jobz.Jobz;
import net.ZeePal.bukkit.Jobz.containers.BlockValue;
import net.ZeePal.bukkit.Jobz.containers.BlockValueListStorage;

public class BlockProcessorThread implements Runnable {

	private final String player;
	private final short blockID;
	private final byte blockData;
	private final BlockValueListStorage blockValueListStorage;

	public BlockProcessorThread(final String p, final short bID, final byte bData, final BlockValueListStorage blockValueListStorage) {
		player = p;
		blockID = bID;
		blockData = bData;
		this.blockValueListStorage = blockValueListStorage;
	}

	//Process block change data for money
	@Override
	public void run() {
		BlockValue foundBlock = null;
		for (final BlockValue Block : blockValueListStorage.blockValues) {
			if (blockID == Block.ID) {
				if (Block.Data == -1 || blockData == Block.Data) {
					foundBlock = Block;
					break;
				}
			}
		}
		if (foundBlock != null) {
			BufferedEconomyThread.add(player, (getValueModifier(foundBlock.Skill) * foundBlock.Value));
		}
	}

	private double getValueModifier(final SkillType skill) {
		switch (skill) {
		case MINING:
			return (getLevel(SkillType.MINING) + blockValueListStorage.mcMMOMiningLevelBonusAmount) / (double) blockValueListStorage.mcMMOMiningLevelDivideAmount;
		case WOODCUTTING:
			return (getLevel(SkillType.WOODCUTTING) + blockValueListStorage.mcMMOWoodcuttingLevelBonusAmount) / (double) blockValueListStorage.mcMMOWoodcuttingLevelDivideAmount;
		case EXCAVATION:
			return (getLevel(SkillType.EXCAVATION) + blockValueListStorage.mcMMOExcavationLevelBonusAmount) / (double) blockValueListStorage.mcMMOExcavationLevelDivideAmount;
		case HERBALISM:
			return (getLevel(SkillType.HERBALISM) + blockValueListStorage.mcMMOHerbalismLevelBonusAmount) / (double) blockValueListStorage.mcMMOHerbalismLevelDivideAmount;
		default:
			return 0.0;
		}
	}
	
	private int getLevel(SkillType skill) {
		Future<Integer> future = Jobz.scheduler.callSyncMethod(Jobz.plugin, new McMMOGetLevelTask(player, skill));
		try {
		    return future.get();
		} catch (InterruptedException error) {
		    Jobz.logger.severe("Interrupt triggered while getting mcMMO Skill Level");
		} catch (ExecutionException error) {
			Jobz.logger.severe("Callable task for mcMMO Skill Level threw an exception");
		    error.getCause().printStackTrace();
		}
		return 0;
	}

}
