package EGServer.GoldManager;

import java.io.File;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import EGServer.EGServer;
import EGServer.ServerManager.EGPlugin;

public class GoldManager extends EGPlugin{

	public HashMap<String, Integer> goldList = new HashMap<String, Integer>(1000);
	public String dirPath = server.getDataFolder().getPath() + "/GoldManager/PlayerGold";
	public String ms = "[골드 매니저] ";
	
	public GoldManager(EGServer server) {
		super(server);
		dirSetting("GoldManager");
		loadGoldData();	
	}
	
	public int loadGoldData() {
		int loadCnt = 0;
		File dir = new File(dirPath);
		File fileList[] = dir.listFiles();
		if(fileList == null) return 0;
		for(File file : fileList) {
			if(file.isDirectory() || file.getAbsolutePath().endsWith(".goldData")) continue;
			try {
				FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
				String pName = fileConfig.getString("Name");
				int pGold = fileConfig.getInt("Gold");
				goldList.put(pName, pGold);
				loadCnt += 1;
			} catch (Exception e) {
				server.egPM.printLog(ms+file.getName()+" 를 읽어오는중 오류 발생");
			}
		}
		return loadCnt;
	}
	
	public boolean giveGold(String pName, int amt) {
		if(goldList.containsKey(pName)) {
			goldList.put(pName, goldList.get(pName)+amt);
		} else {
			goldList.put(pName, amt);
		}
		return saveFileData(pName);
	}
	
	public boolean takeGold(String pName, int amt) {
		amt = amt * -1;
		if(goldList.containsKey(pName)) {
			goldList.put(pName, goldList.get(pName)+amt);
		} else {
			goldList.put(pName, amt);
		}
		return saveFileData(pName);
	}
	
	private boolean saveFileData(String pName) {
		File file = new File(dirPath, pName+".yml");
		try {
			FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
			fileConfig.set("Name",pName);
			fileConfig.set("Gold", goldList.get(pName));
			fileConfig.save(file);
		} catch (Exception e) {
			server.egPM.printLog(ms+file.getName()+" 를 저장하는중 오류 발생");
			return false;
		}
		return true;
	}	
	
}
