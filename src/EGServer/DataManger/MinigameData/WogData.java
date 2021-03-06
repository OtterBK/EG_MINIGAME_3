package EGServer.DataManger.MinigameData;

import java.io.File;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import EGServer.DataManger.PlayerData;
import Minigames.FindTheMurder.FindTheMurder.FtmPlayer;
import Minigames.WarOfGod.WarOfGod.WOGPlayer;

public class WogData extends MinigameData{
	
	public int breakCore; //코어파괴수
	public int breakCobbleStone; //돌캔수
	
	public WogData(PlayerData playerData, String gameName) {
		super(playerData, gameName);
	}

	@Override
	public void customDataSave(ConfigurationSection gameSection) {
		gameSection.set("breakCore", breakCore);
		gameSection.set("breakCobbleStone", breakCobbleStone);
	}
	
	@Override
	public void customDataLoad(ConfigurationSection gameSection) {	
		breakCore = gameSection.getInt("breakCore");
		breakCobbleStone = gameSection.getInt("breakCobbleStone");
	}
	
	public void applyNewData(WOGPlayer wogPlayer) {
		breakCore += wogPlayer.breakCore; //코어파괴수
		breakCobbleStone += wogPlayer.breakCobbleStone; //무고한 플레이어를 죽인수
	}
	
}

