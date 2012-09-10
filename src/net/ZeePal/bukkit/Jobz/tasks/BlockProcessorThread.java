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
		blockValueListStorage.readLock();
		try {
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
		}finally{
			blockValueListStorage.readUnlock();
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
