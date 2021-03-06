package EGServer.ReportManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ReportData {

	public static int reportId = 0;
	
	public String reporter;
	public String target;
	public String reason;
	public int id;
	public byte reasonByte;
	private ReportManager rm;
	
	public List<String> chatList = new ArrayList<String>(40);
	
	public ReportData(ReportManager rm, String reporter, String tName, String reason, byte reasonCode,List<String> chatList) {
		
		this.reporter = reporter;
		this.target = tName;
		this.reason = reason;
		this.chatList.addAll(chatList);
		this.reasonByte = reasonCode;
		this.rm = rm;
		
		ReportData.reportId += 1;
		
		this.id = ReportData.reportId;
		
	}
	
	public ReportData(ReportManager rm, int id) {
		
		this.rm = rm;
		this.id = id;
		
		loadData();
	}
	
	
	public byte getTypeData() {
		return this.reasonByte;
	}
	
	public boolean saveData() {
		File file = new File(rm.getDir(), id + ".yml");
		try {
			FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
			
			fileConfig.set("id", id);
			fileConfig.set("reporter", reporter);
			fileConfig.set("target", target);
			fileConfig.set("reason", reason);
			fileConfig.set("reasonByte", reasonByte);
			fileConfig.set("chatList", chatList);
			
			fileConfig.save(file);
		} catch (Exception e) {
			rm.server.egPM.printLog(rm.ms + file.getName() + " 를 저장하는중 오류 발생");
			return false;
		}
		
		File staticFile = new File(rm.getRootDirPath(), "reportStatic" + ".yml");
		try {
			FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(staticFile);
			
			fileConfig.set("staticId", ReportData.reportId);
			
			fileConfig.save(staticFile);
		} catch (Exception e) {
			rm.server.egPM.printLog(rm.ms + staticFile.getName() + " 를 저장하는중 오류 발생");
			return false;
		}
		
		return true;	
	}
	
	public boolean loadData() {
		
		File file = new File(rm.getDir(), id+".yml");
		if(!file.exists()) return false;
		FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file); 
		
		id = fileConfig.getInt("id");
		reporter = fileConfig.getString("reporter");
		target = fileConfig.getString("target");
		reason = fileConfig.getString("reason");
		chatList = fileConfig.getStringList("chatList");
		reasonByte =(byte) fileConfig.getInt("reasonByte");
		if(chatList == null) {
			chatList = new ArrayList<String>(5);
		}
		return true;
	}
}
