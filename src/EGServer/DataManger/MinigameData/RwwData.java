package EGServer.DataManger.MinigameData;

import org.bukkit.configuration.ConfigurationSection;

import EGServer.DataManger.PlayerData;
import Minigames.RandomWeaponWar.RandomWeaponWar.RwwPlayer;

public class RwwData extends MinigameData{
	
	public int death; //사망수
	public int kill; //킬수
	public int first; //1등수
	
	public RwwData(PlayerData playerData, String gameName) {
		super(playerData, gameName);
	}

	@Override
	public void customDataSave(ConfigurationSection gameSection) {
		
		gameSection.set("death", death);
		gameSection.set("kill", kill);
		gameSection.set("first", first);
		
	}
	
	@Override
	public void customDataLoad(ConfigurationSection gameSection) {
		
		death = gameSection.getInt("death");
		kill = gameSection.getInt("kill");
		first = gameSection.getInt("first");
		
	}
	
	public void applyNewData(RwwPlayer rwwPlayer) {
		death += rwwPlayer.death; //사망수
		kill += rwwPlayer.kill; //킬수
		first += rwwPlayer.first; //1위수
		
		if(rwwPlayer.first >= 1) {
			this.addWin();
		}else {
			this.addDefeat();
		}
	}
	
}

