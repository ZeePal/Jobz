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

	//Storage class for block's, their money value and skill for value modifying
	public BlockValue(final short bId, final byte bData, final double bValue, final SkillType skillType) {
		ID = bId;
		Data = bData;
		Value = bValue;
		Skill = skillType;
	}
}
