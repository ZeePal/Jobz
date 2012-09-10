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

import java.util.concurrent.Callable;

import com.gmail.nossr50.api.ExperienceAPI;
import com.gmail.nossr50.datatypes.SkillType;

import org.bukkit.Bukkit;

public class McMMOGetLevelTask implements Callable<Integer> {

	private final String player;
	private final SkillType skill;
	
	public McMMOGetLevelTask(final String player, final SkillType skill) {
		this.player = player;
		this.skill = skill;
	}
	
	public Integer call() {
		return ExperienceAPI.getLevel(Bukkit.getPlayerExact(player), skill);
	}
	
}
