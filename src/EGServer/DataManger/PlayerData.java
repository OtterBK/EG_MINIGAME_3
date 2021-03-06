package EGServer.DataManger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import EGServer.DataManger.MinigameData.FtmData;
import EGServer.DataManger.MinigameData.MinigameData;
import EGServer.DataManger.MinigameData.RwwData;
import EGServer.DataManger.MinigameData.WogData;
import EGServer.ServerManager.EGPluginManager;
import Minigames.Minigame;

public class PlayerData {

	private String playerName;
	private Minigame nowPlaying;
	private String uuid;
	private int warnCnt = 0;
	private String prefix;
	private String bestRank = "나무"; //나무(<500), 돌(<1000), 철(<1500), 금(<2000), 다이아(<2500), 에메랄드(<3000)
	private List<String> chatLog = new ArrayList<String>(40);
	private DataManager dataManager;
	private long muteUntilTime = 0;
	
	//게임 데이터
	private List<MinigameData> gameDataList = new ArrayList<MinigameData>();
	
	public PlayerData(DataManager dataManager, String playerName) {
		if(playerName == null) return;
		this.dataManager = dataManager;
		this.playerName = playerName;
		
		gameDataList.add(new FtmData(this, "FindTheMurder"));
		gameDataList.add(new RwwData(this, "RandomWeaponWar"));
		gameDataList.add(new WogData(this, "WarOfGod"));
	}
	
	public PlayerData(DataManager dataManager, String playerName, String uuid) {
		if(playerName == null) return;
		this.dataManager = dataManager;
		this.playerName = playerName;
		this.uuid = uuid;
		
		gameDataList.add(new FtmData(this, "FindTheMurder"));
		gameDataList.add(new RwwData(this, "RandomWeaponWar"));
		gameDataList.add(new WogData(this, "WarOfGod"));
	}
	
	public void appendChat(String chat) {
		if(chatLog.size() < 40) {
			chatLog.add(chat);
		}else {
			chatLog.remove(0);
			chatLog.add(chat);
		}
	}
	
	public List<String> getChatList(){
		return this.chatLog;
	}
	
	public void setGame(Minigame game) {
		this.nowPlaying = game;
	}
	
	public Minigame getGame() {
		return this.nowPlaying;
	}
	
	public DataManager getDataManager() {
		return this.dataManager;
	}
	
	public String setBestRank() {
		int maxMMR = 0;
		MinigameData maxGameData = null;
		
		for(MinigameData data : gameDataList) {
			if(maxMMR <= data.getMMR()) {
				maxGameData = data;
				maxMMR = data.getMMR();
			}
		}
		
		bestRank = (maxGameData == null ? "나무" : maxGameData.getRankName());
		return bestRank;
 	}
	
	public String getBestRank() {
		return this.bestRank;
	}
	
	public String getPlayerName() {
		return this.playerName;
	}
	
	public MinigameData getGameData(String gameName) {
		for(MinigameData data : gameDataList) {
			if(data.getGameName().contains(gameName)) {
				return data;
			}
		}
		return null;
	}
	
	
	public String getPrefix() {
		if(prefix == null) {
			return "";
		} else {
			return prefix;
		}
	}
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public int setWarnCnt(int cnt) {
		this.warnCnt = cnt;
		return warnCnt;
	}
	
	public int getWarnCnt() {
		return this.warnCnt;
	}
	
	public EGPluginManager getPluginManager() {
		return dataManager.server.egPM;
	}
	
	public File getDataFile() {	
		try {
			File file = new File(dataManager.getDir(), playerName+".yml");
			return file;	
		} catch (Exception e) {
			getPluginManager().printLog(dataManager.ms+playerName+" 의 데이터를 불러오는중 오류 발생");
			e.printStackTrace();
			return null;
		}
	}
	
	public long setMuteUntilTime(long time) {
		this.muteUntilTime = time;
		return this.muteUntilTime;
	}
	
	public long getMuteUntilTime() {
		return this.muteUntilTime;
	}
	
	public int getleftMuteTime() {
		if(isMuted()) {
			long leftLong = getMuteUntilTime() - System.currentTimeMillis();
			return (int)leftLong/1000;
		}else {
			return 0;
		}
	}
	
	public void setUUID(String uuid) {
		this.uuid = uuid;
	}
	
	public String getUUID() {
		if(this.uuid == null) return "";
		return this.uuid;
	}
	
	public boolean isMuted() {
		return muteUntilTime > System.currentTimeMillis() ? true : false;
	}
	
	public boolean saveData() {
		for(MinigameData data : gameDataList) {
			data.saveData();
		}
		File file = new File(dataManager.getDir(), playerName + ".yml");
		try {
			FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
			
			fileConfig.set("playerName", playerName);
			fileConfig.set("prefix", prefix);
			fileConfig.set("warnCnt", warnCnt);
			fileConfig.set("chatLog", chatLog);
			fileConfig.set("muteUntilTime", muteUntilTime);
			fileConfig.set("uuid", uuid);
			
			fileConfig.save(file);
		} catch (Exception e) {
			getPluginManager().printLog(dataManager.ms + file.getName() + " 를 저장하는중 오류 발생");
			return false;
		}
		return true;	
	}
	
	public boolean loadData() {
		
		File file = getDataFile();
		if(file == null) return false;
		FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file); 
		
		playerName = fileConfig.getString("playerName");
		prefix = fileConfig.getString("prefix");
		warnCnt = fileConfig.getInt("warnCnt");
		chatLog = fileConfig.getStringList("chatLog");
		muteUntilTime = fileConfig.getLong("muteUntilTime");
		uuid = fileConfig.getString("uuid"); //uuid 긁어옴
		if(uuid == null || uuid.equalsIgnoreCase("")) { //저장된 uuid가 없으면
			if(playerName == null) return false;
			OfflinePlayer offP = Bukkit.getOfflinePlayer(playerName); //닉넴에서 긁어옴
			uuid = offP.getUniqueId().toString();
			if(uuid == null) uuid = "";
		}
		if(chatLog == null) {
			chatLog = new ArrayList<String>(30);
		}
		if(prefix != null && prefix != "") {
			dataManager.server.egCM.setPrefix(playerName, prefix, false);
		}
		
		for(MinigameData data : gameDataList) {
			data.loadData();
		}
		
		setBestRank();
		
		return true;
	}
	
	public void delete() {
		File file = getDataFile();
		file.delete();
	}
	
	public void changeOwner(String pName) {
		this.playerName = pName;
		saveData();
	}
	
}
