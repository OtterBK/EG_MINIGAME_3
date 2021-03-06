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
	private String gameName; //미니게임 이름
	private PlayerData playerData;//데이터 매니저
	
	public MinigameData(PlayerData playerData, String gameName) {
		this.playerData = playerData;
		this.gameName = gameName;
	}
	
	//승리 관련
	public void setWin(int wincount) {
		this.win = wincount;
	}
	
	public void addWin() {
		this.win += 1;
	}
	
	public int getWin() {
		return win;
	}
	
	//패배 관련
	public void setDefeat(int defeatcount) {
		this.defeat = defeatcount;
	}
	
	public void addDefeat() {
		this.defeat += 1;
	}
	
	public int getDefeat() {
		return defeat;
	}	
	
	//총 플레이 수 관련
	public void setPlaycount(int count) {
		this.playcount = count;
	}
	
	public void addPlaycount() {
		this.playcount += 1;
	}
	
	public int getPlayCount() {
		return playcount;
	}	
	
	//MMR 관련
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
	
	//이 게임의 랭크 이름 얻기
	public String getRankName() {
		if(mmr >= 3500) {
			return "배드락";
		} else if(mmr >= 3000) {
			return "흑요석";
		} else if(mmr >= 2500) {
			return "에메랄드";
		} else if(mmr >= 2000) {
			return "다이아";
		} else if(mmr >= 1500) {
			return "금";
		} else if(mmr >= 1000) {
			return "철";
		} else if(mmr >= 500) {
			return "돌";
		} else {
			return "나무";
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
			playerData.getPluginManager().printLog(playerData.getPlayerName()+" 의 "+gameName+" 데이터를 저장하는중 오류 발생");
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
			playerData.getPluginManager().printLog(playerData.getPlayerName()+" 의 "+gameName+" 데이터를 불러오는중 오류 발생");
			return false;
		}
	}
	
	public abstract void customDataSave(ConfigurationSection gameSection);
	
	public abstract void customDataLoad(ConfigurationSection gameSection);
	
}
