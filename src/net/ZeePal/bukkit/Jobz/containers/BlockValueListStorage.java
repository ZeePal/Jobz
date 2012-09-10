package net.ZeePal.bukkit.Jobz.containers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class BlockValueListStorage {

	private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private final Lock read  = readWriteLock.readLock();
	private final Lock write = readWriteLock.writeLock();

	public final List<BlockValue> blockValues = new ArrayList<BlockValue>();

	public final short mcMMOMiningLevelBonusAmount;
	public final short mcMMOMiningLevelDivideAmount;

	public final short mcMMOWoodcuttingLevelBonusAmount;
	public final short mcMMOWoodcuttingLevelDivideAmount;

	public final short mcMMOExcavationLevelBonusAmount;
	public final short mcMMOExcavationLevelDivideAmount;

	public final short mcMMOHerbalismLevelBonusAmount;
	public final short mcMMOHerbalismLevelDivideAmount;

	public BlockValueListStorage(final short MiningBonus, final short MiningDivide, final short WoodBonus, final short WoodDivide, final short ExcavBonus, final short ExcavDivide, final short HerbBonus, final short HerbDivide) {
		mcMMOMiningLevelBonusAmount = MiningBonus;
		mcMMOMiningLevelDivideAmount = MiningDivide;
		mcMMOWoodcuttingLevelBonusAmount = WoodBonus;
		mcMMOWoodcuttingLevelDivideAmount = WoodDivide;
		mcMMOExcavationLevelBonusAmount = ExcavBonus;
		mcMMOExcavationLevelDivideAmount = ExcavDivide;
		mcMMOHerbalismLevelBonusAmount = HerbBonus;
		mcMMOHerbalismLevelDivideAmount = HerbDivide;
	}

	public void writeLock() {
		write.lock();
	}

	public void readLock() {
		read.lock();
	}

	public void writeUnlock() {
		write.unlock();
	}

	public void readUnlock() {
		read.unlock();
	}

}
