package EGServer.DataManger.MinigameData;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import EGServer.DataManger.PlayerData;

public abstract class MinigameData {

	private int win;
	private int defeat;
	private int playcount;
	private int mmr;
	private String gameName; //�̴ϰ��� �̸�
	private PlayerData playerData;//������ �Ŵ���
	
	public MinigameData(PlayerData playerData, String gameName) {
		this.playerData = playerData;
		this.gameName = gameName;
	}
	
	//�¸� ����
	public void setWin(int wincount) {
		this.win = wincount;
	}
	
	public void addWin() {
		this.win += 1;
	}
	
	public int getWin() {
		return win;
	}
	
	//�й� ����
	public void setDefeat(int defeatcount) {
		this.defeat = defeatcount;
	}
	
	public void addDefeat() {
		this.defeat += 1;
	}
	
	public int getDefeat() {
		return defeat;
	}	
	
	//�� �÷��� �� ����
	public void setPlaycount(int count) {
		this.playcount = count;
	}
	
	public void addPlaycount() {
		this.playcount += 1;
	}
	
	public int getPlayCount() {
		return playcount;
	}	
	
	//MMR ����
	public int setMMR(int mmr) {
		this.mmr = mmr;
		if(this.mmr < 0) {
			this.mmr = 0;
		}
		playerData.setBestRank();
		return this.mmr;
	}
	
	public int getMMR() {
		return mmr;
	}
	
	public String getGameName() {
		return this.gameName;
	}
	
	//�� ������ ��ũ �̸� ���
	public String getRankName() {
		if(mmr >= 3500) {
			return "����";
		} else if(mmr >= 3000) {
			return "��伮";
		} else if(mmr >= 2500) {
			return "���޶���";
		} else if(mmr >= 2000) {
			return "���̾�";
		} else if(mmr >= 1500) {
			return "��";
		} else if(mmr >= 1000) {
			return "ö";
		} else if(mmr >= 500) {
			return "��";
		} else {
			return "����";
		}
	}
	
	public boolean saveData() {
		try {
			File file = playerData.getDataFile();
			FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file); 
			ConfigurationSection gameSection = fileConfig.getConfigurationSection(gameName);
			if(gameSection == null) gameSection = fileConfig.createSection(gameName);
			gameSection.set("Win", win);
			gameSection.set("Defeat", defeat);
			gameSection.set("PlayCount", playcount);
			gameSection.set("MMR", mmr);		
			
			customDataSave(gameSection);
					
			fileConfig.save(file);
			return true;
		} catch (Exception e) {
			playerData.getPluginManager().printLog(playerData.getPlayerName()+" �� "+gameName+" �����͸� �����ϴ��� ���� �߻�");
			return false;
		}
	}
	
	
	public boolean loadData() {
		try {
			File file = playerData.getDataFile();
			FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file); 
			ConfigurationSection gameSection = fileConfig.getConfigurationSection(gameName);
			if(gameSection == null) gameSection = fileConfig.createSection(gameName);
			
			win = gameSection.getInt("Win");
			defeat = gameSection.getInt("Defeat");
			playcount = gameSection.getInt("PlayCount");
			mmr = gameSection.getInt("MMR");		
			
			customDataLoad(gameSection);
			
			return true;
		} catch (Exception e) {
			playerData.getPluginManager().printLog(playerData.getPlayerName()+" �� "+gameName+" �����͸� �ҷ������� ���� �߻�");
			return false;
		}
	}
	
	public abstract void customDataSave(ConfigurationSection gameSection);
	
	public abstract void customDataLoad(ConfigurationSection gameSection);
	
}
