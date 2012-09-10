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
