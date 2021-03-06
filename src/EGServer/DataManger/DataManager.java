package EGServer.DataManger;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import EGServer.EGServer;
import EGServer.ServerManager.EGPlugin;

public class DataManager extends EGPlugin{

	public HashMap<String, PlayerData> infoMap = new HashMap<String, PlayerData>(1000);
	public String ms = "[데이터 매니저] ";
	private String dirPath = server.getDataFolder().getPath() + "/InfoManger/PlayerInfo";
	
	public DataManager(EGServer server) {
		super(server);
		dirSetting("InfoManger");	
	}
	
	public int loadPlayerData() {
		infoMap.clear();
		server.egPM.printLog(ms+"플레이어 데이터 로드중...");
		int loadCnt = 0;
		File dir = new File(dirPath);
		File fileList[] = dir.listFiles();
		if(fileList == null) return 0;
		for(File file : fileList) {
			if(file.isDirectory() || !file.getAbsolutePath().endsWith(".yml")) continue;
			try {
				FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
				String pName = fileConfig.getString("playerName");
				//server.egPM.printLog(ms+pName+" 데이터 로드시도");
				PlayerData pData = new PlayerData(this, pName);
				//pData.setChatLog(fileConfig.getList("lastChatLog"));
				pData.loadData();
				infoMap.put(pName, pData);
				loadCnt += 1;
				if(loadCnt % 100 == 0) server.egPM.printLog(ms+"현재 "+loadCnt+"개의 데이터 로딩됨");
			} catch (Exception e) {
				server.egPM.printLog(ms+file.getName()+" 를 읽어오는중 오류 발생");
				e.printStackTrace();
				continue;
			}
		}
		return loadCnt;
	}
	
	public boolean existPlayerData(String pName) {
		PlayerData pData = getPlayerData(pName);
		if(pData == null) {
			OfflinePlayer offP = Bukkit.getOfflinePlayer(pName);
			
			pData = createPlayerData(pName, offP.getUniqueId().toString());
			pData.setBestRank();
			infoMap.put(pName, pData);
			return false;
		}
		return true;
	}
	
	public String updatePlayerData(String pName, String uuid) {
		for(PlayerData pData : infoMap.values()) {
			if(pData.getUUID().equalsIgnoreCase(uuid)) {
				if(pData.getPlayerName().equalsIgnoreCase(pName)) continue;
				String baseName = pData.getPlayerName();
				if(baseName.equalsIgnoreCase(pName)) {
					return baseName;
				}
				pData.delete();
				pData.changeOwner(pName);
				infoMap.remove(baseName);
				infoMap.put(pName, pData);
				return baseName;
			}
		}
		return null;
	}
	
	/*public boolean giveGold(String pName, int amt) {
		if(infoMap.containsKey(pName)) {
			infoMap.put(pName, infoMap.get(pName)+amt);
		} else {
			infoMap.put(pName, amt);
		}
		return saveFileData(pName);
	}
	
	public boolean takeGold(String pName, int amt) {
		amt = amt * -1;
		if(infoMap.containsKey(pName)) {
			infoMap.put(pName, infoMap.get(pName)+amt);
		} else {
			infoMap.put(pName, amt);
		}
		return saveFileData(pName);
	}
	
	public int checkGold(String pName) {
		return infoMap.containsKey(pName) ? infoMap.get(pName) : 0;
	}*/
	
	public boolean giveGold(String pName, int amt) {
		return true;
	}

	public int checkGold(String pName) {
		return 0;
	}
	
	public String getDir() {
		return this.dirPath;
	}
	
	public boolean savePlayerData(String pName) {
		PlayerData data = infoMap.get(pName);
		if(data != null) {
			return data.saveData();
		}
		return false;
	}
	
	public boolean savePlayerData(PlayerData data) {
		if(data != null) {
			return data.saveData();
		}
		return false;
	}
	
	public PlayerData createPlayerData(String pName, String uuid) {
		
		if(!infoMap.containsKey(pName)) {
			PlayerData data = new PlayerData(this, pName, uuid);
			
			infoMap.put(pName, data);
			savePlayerData(data);
			
			server.egPM.printLog(pName+"의 유저데이터 생성됨");
			return data;
		} 
		return infoMap.get(pName);
	}
	
	public PlayerData specifyLoadPlayerData(String pName, boolean forceReload) {

		if(!forceReload && infoMap.containsKey(pName)) return infoMap.get(pName);
		
		PlayerData data = new PlayerData(this, pName);
		
		if(!data.loadData()) savePlayerData(data);

		infoMap.put(pName, data);
		
		server.egPM.printLog(pName+"의 유저데이터 로드됨");
			
		return data;
	}
	
	public int saveAllData() {
		
		int cnt = 0;
		
		for(PlayerData data : infoMap.values()) {
			if(data != null) {
				try {
					data.saveData();
					cnt++;
				}catch(Exception e){
					e.printStackTrace();
				}	
			}
		}
		return cnt;
	}
	
	public PlayerData getPlayerData(String pName) {
		return infoMap.get(pName);
	}
	
}
