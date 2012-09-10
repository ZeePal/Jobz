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
