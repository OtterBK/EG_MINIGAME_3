package EGServer.ReportManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import EGServer.EGServer;
import EGServer.DataManger.DataManager;
import EGServer.DataManger.PlayerData;
import EGServer.EGServer.DefaultEventHandler;
import EGServer.ServerManager.EGPlugin;

public class ReportManager extends EGPlugin{

	private List<ReportData> reportList = new ArrayList<ReportData>();
	private HashMap<String, String> reportingMap = new HashMap<String, String>();
	private HashMap<String, ReportData> punishingMap = new HashMap<String, ReportData>();
	private DataManager dataManager;
	private int banLimiter = 5;
	private Inventory inven_selectReason;
	//private List<Inventory> reportUI = new ArrayList<Inventory>();
	private Inventory reportUI;
	private Inventory punishUI;
	public String ms = "§f[ §4신고 관리 §f] §c";
	private String dirPath = server.getDataFolder().getPath() + "/ReportManager/Reports";
	private String rootDirPath = server.getDataFolder().getPath() + "/ReportManager";
	
	public ReportManager(EGServer server) {
		super(server);
		dirSetting("ReportManager");
		
		dataManager = server.egDM;
		
		inven_selectReason = Bukkit.createInventory(null, 27, "§0§l신고 사유 선택");
		
		ItemStack dump = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)15);
		
		for(int i = 0; i <= 9; i++) {
			inven_selectReason.setItem(i, dump);
		}
		
		for(int i = 17; i <= 26; i++) {
			inven_selectReason.setItem(i, dump);
		}
		
		ItemStack item = new ItemStack(Material.WOOL, 1, (byte)14);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§f[ §4욕설 §f]");
		item.setItemMeta(meta);
		inven_selectReason.setItem(10, item);
		
		item = new ItemStack(Material.WOOL, 1, (byte)10);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §4분쟁 유도 §f]");
		item.setItemMeta(meta);
		inven_selectReason.setItem(13, item);
		
		item = new ItemStack(Material.WOOL, 1, (byte)8);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §4기타(채팅 매너 위반) §f]");
		item.setItemMeta(meta);
		inven_selectReason.setItem(16, item);
		
		reportUI = Bukkit.createInventory(null, 54, "§0§l신고 목록");
		
		punishUI = Bukkit.createInventory(null, 27, "§0§l처벌 설정");
		
		item = new ItemStack(Material.WOOL, 1, (byte)11);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §b차감 §7-");
		item.setItemMeta(meta);
		punishUI.setItem(10, item);
		
		item = new ItemStack(Material.WOOL, 1, (byte)1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f §e뮤트('시간' 단위) §f]");
		List<String> loreList = new ArrayList<String>(1);
		loreList.add("§a현재 설정된 시간 : §d0시간");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		punishUI.setItem(11, item);
		
		item = new ItemStack(Material.WOOL, 1, (byte)14);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c증가 §7-");
		item.setItemMeta(meta);
		punishUI.setItem(12, item);
		
		item = new ItemStack(Material.WOOL, 1, (byte)11);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §b차감 §7-");
		item.setItemMeta(meta);
		punishUI.setItem(14, item);
		
		item = new ItemStack(Material.WOOL, 1, (byte)15);
		meta = item.getItemMeta();
		meta.setDisplayName("§f §e벤('일' 단위) §f]");
		loreList = new ArrayList<String>(1);
		loreList.add("§a현재 설정된 일수 : §d0일");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		punishUI.setItem(15, item);
		
		item = new ItemStack(Material.WOOL, 1, (byte)14);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c증가 §7-");
		item.setItemMeta(meta);
		punishUI.setItem(16, item);	
		
		item = new ItemStack(Material.BARRIER);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §4신고 기각 §f]");
		item.setItemMeta(meta);
		punishUI.setItem(21, item);	
		
		item = new ItemStack(Material.IRON_DOOR);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §4처벌 확정 §f]");
		item.setItemMeta(meta);
		punishUI.setItem(23, item);
		
		item = new ItemStack(Material.COAL_BLOCK);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §4영구벤 §f]");
		item.setItemMeta(meta);
		punishUI.setItem(8, item);	

		loadAllData();
		
		server.getServer().getPluginManager().registerEvents(new ReportManagerEvent(), server);
	}
	
	public int addWarn(String pName, int amt) {
		PlayerData pData = dataManager.getPlayerData(pName);
		if(pData == null) return -1;
		int nowWarn = pData.getWarnCnt()+amt;
		if(nowWarn < 0) nowWarn = 0;
		pData.setWarnCnt(nowWarn);
		if(nowWarn >= banLimiter) {
			Bukkit.dispatchCommand(server.getServer().getConsoleSender(), "ban"+" "+pName+" "+"경고 5회 누적으로인한 벤");
		}else {
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p)) {
				if(amt < 0) {
					p.sendMessage(ms+"경고를 "+amt+"회 차감 받았습니다. \n§c당신의 경고 누적 : §e"+nowWarn);	
				}else {
					p.sendMessage(ms+"경고를 "+amt+"회 받았습니다. \n§c당신의 경고 누적 : §e"+nowWarn							
							+"\n§c"+banLimiter+"이상 경고가 누적되면 영구벤됩니다.");
				}
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_XYLOPHONE, 1.0f, 1.0f);
			}
		}
		return nowWarn;
	}
	
	public int getLimiter() {
		return this.banLimiter;
	}
	
	public int getWarn(String pName) {
		PlayerData pData = dataManager.getPlayerData(pName);
		if(pData == null) return -1;
		return pData.getWarnCnt();
	}
	
	public boolean report(Player reporter, String tName) {
		PlayerData tData = dataManager.getPlayerData(tName);
		if(tData == null) return false;
		else {
			reporter.sendMessage(ms+"신고 사유를 선택해주세요.");
			reporter.playSound(reporter.getLocation(), Sound.BLOCK_NOTE_CHIME, 1.0f, 1.0f);
			reportingMap.put(reporter.getName(), tName);
			reporter.openInventory(inven_selectReason);
			return true;
		}
	}
	
	private void createReportData(Player reporter, String target, int reasonCode) {
	
		String reason = getReasonFromCode(reasonCode);
		
		if(reason == null) return;
		
		for(ReportData rData : reportList) {
			if(rData.reporter.equalsIgnoreCase(reporter.getName())) {
				if(rData.target.equalsIgnoreCase(target)) {
					reporter.sendMessage(ms+"이미 §6"+target+" §c님에 대한 §6"+rData.reason+"§c신고를 처리중입니다. 신고ID : §c"+rData.id);
					reporter.closeInventory();
					return;
				}
			}
		}
		
		PlayerData tData = dataManager.getPlayerData(target);
		if(tData == null) {
			reporter.sendMessage(ms+"§6"+target+" §c님은 서버에 데이터가 존재하지 않습니다. 닉네임을 확인하시거나 /문의 를 이용해주세요.");
			return;
		}
		List<String> chatList = tData.getChatList();
		if(chatList == null) {
			reporter.sendMessage(ms+"§6"+target+" §c님은 최근 채팅로그가 존재하지 않습니다. 닉네임을 확인하시거나 /문의 를 이용해주세요.");
			return;
		}
		ItemStack reasonItem = inven_selectReason.getItem(reasonCode);
		byte reasonByte = reasonItem.getData().getData();
		ReportData report = new ReportData(this, reporter.getName(), target, reason, reasonByte,chatList);
		reportList.add(report);
		reporter.sendMessage(ms+"§6"+target+" §c님에 대한 §6"+reason+"§c신고가 완료됐습니다 . 감사합니다.\n"+ms+"§c신고 ID: §6"+report.id);
		reporter.playSound(reporter.getLocation(), Sound.BLOCK_NOTE_GUITAR, 1.0f, 0.5f);
		reporter.closeInventory();
		
		updateReportUI(true);
		
		report.saveData();
	}
	
	private String getReasonFromCode(int code) {
		switch(code) {
		case 10: return "욕설";
		case 12: return "분쟁유도";
		case 14: return "확성기 어그로";
		case 16: return "기타(채팅 매너 위반)";
		default: return null;
		}
	}
	
	private int updateReportUI(boolean alert) {
		int uiCnt = 0;
		
		reportUI.clear();
		
		for(int i = 0; i < reportList.size(); i++) {
			ReportData rData = reportList.get(i);
			byte dataCode = rData.reasonByte;
			ItemStack item = new ItemStack(Material.WOOL, 1, dataCode);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName("§6신고 ID: §4"+rData.id);
			List<String> loreList = new ArrayList<String>(3);
			loreList.add("");
			loreList.add("§6신고자 : §b"+rData.reporter);
			loreList.add("§6처벌 대상자 : §b"+rData.target);
			loreList.add("§6신고 사유 : §b"+rData.reason);
			loreList.add("");
			loreList.add("§6좌클릭 : §b처벌 설정 §7| §6우클릭 : §b채팅로그 확인");
			loreList.add("");
			meta.setLore(loreList);
			item.setItemMeta(meta);
			reportUI.setItem(uiCnt, item);
			uiCnt++;
		}
		
		if(alert) {
			for(Player t : Bukkit.getOnlinePlayers()) {
				if(t.isOp()) {
					t.sendMessage(ms+"신고 목록이 갱신됐습니다. §6'/신고목록' §c으로 확인해보세요.\n"+ms+"미처리 신고수 : §6"+reportList.size()+"개");
				}
			}
		}
			
		return uiCnt;
	}
	
	public void openReportUI(Player p) {
		p.openInventory(reportUI);
		p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BELL, 1.0f, 1.0f);
	}
	
	private void clickReportUI(Player p, int slotNum, boolean isLeftClick) {
		ReportData rData = null;
		try {
			rData = reportList.get(slotNum);
		}catch(Exception e) {
			return;
		}
		
		if(rData == null) {
			p.sendMessage(ms+"오류 발생, 클릭한 슬롯넘버에 대한 플레이어 데이터가 없습니다.");
			return;
		}
		
		if(isLeftClick) {
			punishingMap.put(p.getName(), rData);
			p.sendMessage(ms+"§6"+rData.target+" §c님에 대한 처벌 설정");
			Inventory inven = Bukkit.createInventory(null, 27, "§0§l"+rData.target+"에 대한 처벌 설정");
			inven.setContents(punishUI.getContents());
			p.openInventory(inven);
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BELL, 1.0f, 1.0f);
		}else {
			p.sendMessage(ms+"---------- §6"+rData.target+" 에 대한 채팅로그 40개 시작 §c-----------");
			List<String> chatLog = rData.chatList;
			for(String str : chatLog) {
				p.sendMessage("§f[ §c로그 §f] -> "+str);
			}
			p.sendMessage(ms+"------- §6"+rData.target+" 에 대한 채팅로그 40개 종료 §c-------");
			p.closeInventory();
		}
	}
	
	private void clickPunishUI(Inventory inven, Player p, int slotNum) {
		
		
		ItemStack item = null;
		ItemMeta meta = null;
		ReportData rData = null;
		int amt = 0;
		
		p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
		
		switch(slotNum) {
				
		case 10:
			item = inven.getItem(11);		
			if(item.getAmount() == 1) {
				if(item.getItemMeta().hasEnchant(Enchantment.LURE)) {
					meta = item.getItemMeta();
					meta.removeEnchant(Enchantment.LURE);
					List<String> lore = meta.getLore();
					lore.set(0, "§a현재 설정된 시간 : §d0시간");
					meta.setLore(lore);
					item.setItemMeta(meta);
					item.setAmount(1);
				}
			}else {
				amt = item.getAmount();
				amt -= 1;
				if(amt <= 0) amt = 1;
				item.setAmount(amt);
				meta = item.getItemMeta();
				List<String> lore = meta.getLore();
				lore.set(0, "§a현재 설정된 시간 : §d"+amt+"시간");
				meta.setLore(lore);
				item.setItemMeta(meta);
			}
			break;
			
		case 12: 
			item = inven.getItem(11);
			if(item.getItemMeta().hasEnchant(Enchantment.LURE)) {
				amt = item.getAmount();
				amt += 1;
				if(amt > 64) amt = 64;
				item.setAmount(amt);
				meta = item.getItemMeta();
				List<String> lore = meta.getLore();
				lore.set(0, "§a현재 설정된 시간 : §d"+amt+"시간");
				meta.setLore(lore);
				item.setItemMeta(meta);
			}else {
				item.addUnsafeEnchantment(Enchantment.LURE, 0);
				item.setAmount(1);
				amt = item.getAmount();
				meta = item.getItemMeta();
				List<String> lore = meta.getLore();
				lore.set(0, "§a현재 설정된 시간 : §d"+amt+"시간");
				meta.setLore(lore);
				item.setItemMeta(meta);
			}	
			break;
			
		case 14:
			item = inven.getItem(15);		
			if(item.getAmount() == 1) {
				if(item.getItemMeta().hasEnchant(Enchantment.LURE)) {
					meta = item.getItemMeta();
					meta.removeEnchant(Enchantment.LURE);
					List<String> lore = meta.getLore();
					lore.set(0, "§a현재 설정된 일수 : §d0일");
					meta.setLore(lore);
					item.setItemMeta(meta);
					item.setAmount(1);
				}
			}else {
				amt = item.getAmount();
				amt -= 1;
				if(amt <= 0) amt = 1;
				item.setAmount(amt);
				meta = item.getItemMeta();
				List<String> lore = meta.getLore();
				lore.set(0, "§a현재 설정된 일수 : §d"+amt+"일");
				meta.setLore(lore);
				item.setItemMeta(meta);
			}
			break;
			
		case 16: 
			item = inven.getItem(15);
			if(item.getItemMeta().hasEnchant(Enchantment.LURE)) {
				amt = item.getAmount();
				amt += 1;
				if(amt > 64) amt = 64;
				item.setAmount(amt);
				meta = item.getItemMeta();
				List<String> lore = meta.getLore();
				lore.set(0, "§a현재 설정된 일수 : §d"+amt+"일");
				meta.setLore(lore);
				item.setItemMeta(meta);
			}else {
				item.addUnsafeEnchantment(Enchantment.LURE, 0);
				item.setAmount(1);
				amt = item.getAmount();
				meta = item.getItemMeta();
				List<String> lore = meta.getLore();
				lore.set(0, "§a현재 설정된 일수 : §d"+amt+"일");
				meta.setLore(lore);
				item.setItemMeta(meta);
			}	
			break;
			
		case 21: 
			p.closeInventory();

			rData = punishingMap.get(p.getName());
			if(rData == null) {
				p.sendMessage(ms+"처리 실패 신고 데이터가 null값입니다.");
			}else {
				String result = "§c기각";
				p.sendMessage(ms+"§c신고ID - §6"+rData.id+"§c를 기각했습니다.");
				
				/*String reporterName = rData.reporter; //신고 기각 메세지 처리 전달
				if(reporterName != null) {
					Player reporter = Bukkit.getPlayer(reporterName);
					if(existPlayer(reporter)) {
						reporter.sendMessage(ms+"§6신고ID - "+rData.id+"§c가 처리되었습니다.");
						reporter.sendMessage(ms+"§c신고 대상자 : §6"+rData.target);
						reporter.sendMessage(ms+"§c처리 결과 : §6"+result);
					}
				}*/
				
				for(Player t : Bukkit.getOnlinePlayers()) {
					if(t.isOp()) {
						t.sendMessage(ms+"§6"+p.getName()+" 님이 §6신고ID-"+rData.id+"§c를 §c"+result+"처리 하였습니다.\n"
					+ms+"현재 미처리 신고수 : §6"+reportList.size()+"개");
					}
				}
			}
			
			removeReport(rData);
			updateReportUI(false);
				
			break;
			
		case 8: 
			p.closeInventory();

			rData = punishingMap.get(p.getName());
			if(rData == null) {
				p.sendMessage(ms+"처리 실패 신고 데이터가 null값입니다.");
			}else {
				String result = "§c영구벤";
				p.sendMessage(ms+"§c신고ID - §6"+rData.id+"§c를 영구벤했습니다.");
				
				Bukkit.dispatchCommand(p, "ban"+" "+rData.target+" "+"영구벤");
				Bukkit.dispatchCommand(p, "banip"+" "+rData.target+" "+"영구벤");
				
				String reporterName = rData.reporter;
				if(reporterName != null) {
					Player reporter = Bukkit.getPlayer(reporterName);
					if(existPlayer(reporter)) {
						reporter.sendMessage(ms+"§6신고ID - "+rData.id+"§c가 처리되었습니다.");
						reporter.sendMessage(ms+"§c신고 대상자 : §6"+rData.target);
						reporter.sendMessage(ms+"§c처리 결과 : §6"+result);
					}
				}
				
				for(Player t : Bukkit.getOnlinePlayers()) {
					if(t.isOp()) {
						t.sendMessage(ms+"§6"+p.getName()+" 님이 §6신고ID-"+rData.id+"§c를 §c"+result+"처리 하였습니다.\n"
					+ms+"현재 미처리 신고수 : §6"+reportList.size()+"개");
					}
				}
			}
			
			removeReport(rData);
			updateReportUI(false);
				
			break;
			
		case 23: 
			p.closeInventory();
			
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
			rData = punishingMap.get(p.getName());
			if(rData == null) {
				p.sendMessage(ms+"처리 실패 신고 데이터가 null값입니다.");
			}else {
				int muteHour = 0;
				int banDay = 0;
				if(inven.getItem(11).getItemMeta().hasEnchant(Enchantment.LURE)) {
					muteHour = inven.getItem(11).getAmount();	
				}
				if(inven.getItem(15).getItemMeta().hasEnchant(Enchantment.LURE)) {
					banDay = inven.getItem(15).getAmount();	
				}
				
				if(muteHour <= 0 && banDay <= 0) {
					p.sendMessage(ms+"§c뮤트나 벤등의 처벌강도를 설정해주세요.");
					p.closeInventory();
					return;
				}
				
				if(muteHour > 0) {
					server.egCM.mute(rData.target, muteHour*3600);
				}
				if(banDay > 0) {
					Bukkit.dispatchCommand(p, "tempban"+" "+rData.target+" "+banDay+"d"+" "+rData.reason);
				}
				String result = "§c뮤트 §6"+muteHour+"시간§c, 기간벤: §6"+banDay+"일";
				p.sendMessage(ms+"§6"+rData.target+"§c에게 "+result+"§c의 처벌을 내렸습니다.");
				
				String reporterName = rData.reporter;
				if(reporterName != null) {
					Player reporter = Bukkit.getPlayer(reporterName);
					if(existPlayer(reporter)) {
						reporter.sendMessage(ms+"§6신고ID - "+rData.id+"§c가 처리되었습니다.");
						reporter.sendMessage(ms+"§6신고 대상자 : §6"+rData.target);
						reporter.sendMessage(ms+"처리 결과 : "+result);
					}
				}
				
				for(Player t : Bukkit.getOnlinePlayers()) {
					if(t.isOp()) {
						t.sendMessage(ms+"§6"+p.getName()+" 님이 §6신고ID-"+rData.id+"§c를 §c"+result+"처리 하였습니다.\n"
					+ms+"현재 미처리 신고수 : §6"+reportList.size()+"개");
					}
				}
			}	
			
			removeReport(rData);
			updateReportUI(false);
			
			break;
		}
		
	}
	
	public void removeReport(ReportData rData) {
		reportList.remove(rData);
		File file = new File(this.getDir(), rData.id+".yml");
		if(file.exists()) {
			file.delete();
		}	
	}
	
	public int saveAllData() {	
		int cnt = 0;
		
		for(ReportData rData : reportList) {
			if(rData != null) {
				try {
					rData.saveData();
					cnt++;
				}catch(Exception e){
					e.printStackTrace();
				}	
			}
		}
		server.egPM.printLog(ms+cnt+"개의 신고데이터 저장됨"); 
		return cnt;
	}
	
	public int loadAllData() {		
		int loadCnt = 0;
		File dir = new File(dirPath);
		File fileList[] = dir.listFiles();
		if(fileList == null) return 0;
		for(File file : fileList) {
			if(file.isDirectory() || !file.getAbsolutePath().endsWith(".yml")) continue;
			try {
				FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
				int id = fileConfig.getInt("id");
				ReportData rData = new ReportData(this, id);
				//pData.setChatLog(fileConfig.getList("lastChatLog"));
				reportList.add(rData);
				loadCnt += 1;
			} catch (Exception e) {
				server.egPM.printLog(ms+file.getName()+" 를 읽어오는중 오류 발생");
				e.printStackTrace();
				continue;
			}
		}
		
		File staticFile = new File(getRootDirPath(), "reportStatic" + ".yml");
		try {
			FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(staticFile);
			ReportData.reportId = fileConfig.getInt("staticId");
		} catch (Exception e) {
			server.egPM.printLog(ms+staticFile.getName()+" 를 읽어오는중 오류 발생");
			e.printStackTrace();
		}
		
		updateReportUI(false);
		
		server.egPM.printLog(ms+loadCnt+" 개의 데이터 로드딤, staticId: "+ReportData.reportId);
		return loadCnt;
	}
	
	public String getDir() {
		return this.dirPath;
	}
	
	public String getRootDirPath() {
		return this.rootDirPath;
	}
	
	
	//이벤트
	
	private class ReportManagerEvent implements Listener{
		@EventHandler
		public void onClickInventory(InventoryClickEvent e) {
			if (!(e.getWhoClicked() instanceof Player))
				return;
			Player p = (Player) e.getWhoClicked();
			if(e.getInventory().getTitle().equalsIgnoreCase(inven_selectReason.getTitle())) {
				e.setCancelled(true);
				String target = reportingMap.get(p.getName());
				if(target == null) return;
				else {
					createReportData(p, target, e.getSlot());
				}
			}else if(e.getInventory().getTitle().equalsIgnoreCase(reportUI.getTitle())) {
				e.setCancelled(true);
				clickReportUI(p, e.getSlot(), e.getClick().isLeftClick());
			}else if(e.getInventory().getTitle().contains("처벌 설정")) {
				e.setCancelled(true);
				clickPunishUI(e.getInventory(), p, e.getSlot());
			}
		}
	}	
}
