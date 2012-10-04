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

import java.util.Comparator;

import com.gmail.nossr50.datatypes.SkillType;

public class BlockValue {

	//Comparator for BlockValues (used in sorting the complete lists)
	public final static Comparator<BlockValue> Comparator = new Comparator<BlockValue>() {
		@Override
		public int compare(final BlockValue BlockValue1, final BlockValue BlockValue2) {
			final int IDDiff = BlockValue1.ID - BlockValue2.ID;

			if (IDDiff == 0)
				return BlockValue1.Data - BlockValue2.Data;
			else
				return IDDiff;
		}
	};

	public final short ID;
	public final byte Data;
	public final double Value;
	public final SkillType Skill;
	public final boolean Cap;

	//Storage class for block's, their money value and skill for value modifying
	public BlockValue(final short bId, final byte bData, final double bValue, final SkillType skillType, final boolean bCap) {
		ID = bId;
		Data = bData;
		Value = bValue;
		Skill = skillType;
		Cap = bCap;
	}
}
