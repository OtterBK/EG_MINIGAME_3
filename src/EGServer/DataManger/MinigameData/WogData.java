package EGServer.DataManger.MinigameData;

import java.io.File;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import EGServer.DataManger.PlayerData;
import Minigames.FindTheMurder.FindTheMurder.FtmPlayer;
import Minigames.WarOfGod.WarOfGod.WOGPlayer;

public class WogData extends MinigameData{
	
	public int breakCore; //�ھ��ı���
	public int breakCobbleStone; //��ĵ��
	
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
		breakCore += wogPlayer.breakCore; //�ھ��ı���
		breakCobbleStone += wogPlayer.breakCobbleStone; //������ �÷��̾ ���μ�
	}
	
}

