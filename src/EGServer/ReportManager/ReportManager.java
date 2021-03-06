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
	public String ms = "��f[ ��4�Ű� ���� ��f] ��c";
	private String dirPath = server.getDataFolder().getPath() + "/ReportManager/Reports";
	private String rootDirPath = server.getDataFolder().getPath() + "/ReportManager";
	
	public ReportManager(EGServer server) {
		super(server);
		dirSetting("ReportManager");
		
		dataManager = server.egDM;
		
		inven_selectReason = Bukkit.createInventory(null, 27, "��0��l�Ű� ���� ����");
		
		ItemStack dump = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)15);
		
		for(int i = 0; i <= 9; i++) {
			inven_selectReason.setItem(i, dump);
		}
		
		for(int i = 17; i <= 26; i++) {
			inven_selectReason.setItem(i, dump);
		}
		
		ItemStack item = new ItemStack(Material.WOOL, 1, (byte)14);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��4�弳 ��f]");
		item.setItemMeta(meta);
		inven_selectReason.setItem(10, item);
		
		item = new ItemStack(Material.WOOL, 1, (byte)10);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��4���� ���� ��f]");
		item.setItemMeta(meta);
		inven_selectReason.setItem(13, item);
		
		item = new ItemStack(Material.WOOL, 1, (byte)8);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��4��Ÿ(ä�� �ų� ����) ��f]");
		item.setItemMeta(meta);
		inven_selectReason.setItem(16, item);
		
		reportUI = Bukkit.createInventory(null, 54, "��0��l�Ű� ���");
		
		punishUI = Bukkit.createInventory(null, 27, "��0��ló�� ����");
		
		item = new ItemStack(Material.WOOL, 1, (byte)11);
		meta = item.getItemMeta();
		meta.setDisplayName("��7- ��b���� ��7-");
		item.setItemMeta(meta);
		punishUI.setItem(10, item);
		
		item = new ItemStack(Material.WOOL, 1, (byte)1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f ��e��Ʈ('�ð�' ����) ��f]");
		List<String> loreList = new ArrayList<String>(1);
		loreList.add("��a���� ������ �ð� : ��d0�ð�");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		punishUI.setItem(11, item);
		
		item = new ItemStack(Material.WOOL, 1, (byte)14);
		meta = item.getItemMeta();
		meta.setDisplayName("��7- ��c���� ��7-");
		item.setItemMeta(meta);
		punishUI.setItem(12, item);
		
		item = new ItemStack(Material.WOOL, 1, (byte)11);
		meta = item.getItemMeta();
		meta.setDisplayName("��7- ��b���� ��7-");
		item.setItemMeta(meta);
		punishUI.setItem(14, item);
		
		item = new ItemStack(Material.WOOL, 1, (byte)15);
		meta = item.getItemMeta();
		meta.setDisplayName("��f ��e��('��' ����) ��f]");
		loreList = new ArrayList<String>(1);
		loreList.add("��a���� ������ �ϼ� : ��d0��");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		punishUI.setItem(15, item);
		
		item = new ItemStack(Material.WOOL, 1, (byte)14);
		meta = item.getItemMeta();
		meta.setDisplayName("��7- ��c���� ��7-");
		item.setItemMeta(meta);
		punishUI.setItem(16, item);	
		
		item = new ItemStack(Material.BARRIER);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��4�Ű� �Ⱒ ��f]");
		item.setItemMeta(meta);
		punishUI.setItem(21, item);	
		
		item = new ItemStack(Material.IRON_DOOR);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��4ó�� Ȯ�� ��f]");
		item.setItemMeta(meta);
		punishUI.setItem(23, item);
		
		item = new ItemStack(Material.COAL_BLOCK);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��4������ ��f]");
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
			Bukkit.dispatchCommand(server.getServer().getConsoleSender(), "ban"+" "+pName+" "+"��� 5ȸ ������������ ��");
		}else {
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p)) {
				if(amt < 0) {
					p.sendMessage(ms+"��� "+amt+"ȸ ���� �޾ҽ��ϴ�. \n��c����� ��� ���� : ��e"+nowWarn);	
				}else {
					p.sendMessage(ms+"��� "+amt+"ȸ �޾ҽ��ϴ�. \n��c����� ��� ���� : ��e"+nowWarn							
							+"\n��c"+banLimiter+"�̻� ��� �����Ǹ� �������˴ϴ�.");
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
			reporter.sendMessage(ms+"�Ű� ������ �������ּ���.");
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
					reporter.sendMessage(ms+"�̹� ��6"+target+" ��c�Կ� ���� ��6"+rData.reason+"��c�Ű� ó�����Դϴ�. �Ű�ID : ��c"+rData.id);
					reporter.closeInventory();
					return;
				}
			}
		}
		
		PlayerData tData = dataManager.getPlayerData(target);
		if(tData == null) {
			reporter.sendMessage(ms+"��6"+target+" ��c���� ������ �����Ͱ� �������� �ʽ��ϴ�. �г����� Ȯ���Ͻðų� /���� �� �̿����ּ���.");
			return;
		}
		List<String> chatList = tData.getChatList();
		if(chatList == null) {
			reporter.sendMessage(ms+"��6"+target+" ��c���� �ֱ� ä�÷αװ� �������� �ʽ��ϴ�. �г����� Ȯ���Ͻðų� /���� �� �̿����ּ���.");
			return;
		}
		ItemStack reasonItem = inven_selectReason.getItem(reasonCode);
		byte reasonByte = reasonItem.getData().getData();
		ReportData report = new ReportData(this, reporter.getName(), target, reason, reasonByte,chatList);
		reportList.add(report);
		reporter.sendMessage(ms+"��6"+target+" ��c�Կ� ���� ��6"+reason+"��c�Ű� �Ϸ�ƽ��ϴ� . �����մϴ�.\n"+ms+"��c�Ű� ID: ��6"+report.id);
		reporter.playSound(reporter.getLocation(), Sound.BLOCK_NOTE_GUITAR, 1.0f, 0.5f);
		reporter.closeInventory();
		
		updateReportUI(true);
		
		report.saveData();
	}
	
	private String getReasonFromCode(int code) {
		switch(code) {
		case 10: return "�弳";
		case 12: return "��������";
		case 14: return "Ȯ���� ��׷�";
		case 16: return "��Ÿ(ä�� �ų� ����)";
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
			meta.setDisplayName("��6�Ű� ID: ��4"+rData.id);
			List<String> loreList = new ArrayList<String>(3);
			loreList.add("");
			loreList.add("��6�Ű��� : ��b"+rData.reporter);
			loreList.add("��6ó�� ����� : ��b"+rData.target);
			loreList.add("��6�Ű� ���� : ��b"+rData.reason);
			loreList.add("");
			loreList.add("��6��Ŭ�� : ��bó�� ���� ��7| ��6��Ŭ�� : ��bä�÷α� Ȯ��");
			loreList.add("");
			meta.setLore(loreList);
			item.setItemMeta(meta);
			reportUI.setItem(uiCnt, item);
			uiCnt++;
		}
		
		if(alert) {
			for(Player t : Bukkit.getOnlinePlayers()) {
				if(t.isOp()) {
					t.sendMessage(ms+"�Ű� ����� ���ŵƽ��ϴ�. ��6'/�Ű���' ��c���� Ȯ���غ�����.\n"+ms+"��ó�� �Ű�� : ��6"+reportList.size()+"��");
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
			p.sendMessage(ms+"���� �߻�, Ŭ���� ���Գѹ��� ���� �÷��̾� �����Ͱ� �����ϴ�.");
			return;
		}
		
		if(isLeftClick) {
			punishingMap.put(p.getName(), rData);
			p.sendMessage(ms+"��6"+rData.target+" ��c�Կ� ���� ó�� ����");
			Inventory inven = Bukkit.createInventory(null, 27, "��0��l"+rData.target+"�� ���� ó�� ����");
			inven.setContents(punishUI.getContents());
			p.openInventory(inven);
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BELL, 1.0f, 1.0f);
		}else {
			p.sendMessage(ms+"---------- ��6"+rData.target+" �� ���� ä�÷α� 40�� ���� ��c-----------");
			List<String> chatLog = rData.chatList;
			for(String str : chatLog) {
				p.sendMessage("��f[ ��c�α� ��f] -> "+str);
			}
			p.sendMessage(ms+"------- ��6"+rData.target+" �� ���� ä�÷α� 40�� ���� ��c-------");
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
					lore.set(0, "��a���� ������ �ð� : ��d0�ð�");
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
				lore.set(0, "��a���� ������ �ð� : ��d"+amt+"�ð�");
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
				lore.set(0, "��a���� ������ �ð� : ��d"+amt+"�ð�");
				meta.setLore(lore);
				item.setItemMeta(meta);
			}else {
				item.addUnsafeEnchantment(Enchantment.LURE, 0);
				item.setAmount(1);
				amt = item.getAmount();
				meta = item.getItemMeta();
				List<String> lore = meta.getLore();
				lore.set(0, "��a���� ������ �ð� : ��d"+amt+"�ð�");
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
					lore.set(0, "��a���� ������ �ϼ� : ��d0��");
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
				lore.set(0, "��a���� ������ �ϼ� : ��d"+amt+"��");
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
				lore.set(0, "��a���� ������ �ϼ� : ��d"+amt+"��");
				meta.setLore(lore);
				item.setItemMeta(meta);
			}else {
				item.addUnsafeEnchantment(Enchantment.LURE, 0);
				item.setAmount(1);
				amt = item.getAmount();
				meta = item.getItemMeta();
				List<String> lore = meta.getLore();
				lore.set(0, "��a���� ������ �ϼ� : ��d"+amt+"��");
				meta.setLore(lore);
				item.setItemMeta(meta);
			}	
			break;
			
		case 21: 
			p.closeInventory();

			rData = punishingMap.get(p.getName());
			if(rData == null) {
				p.sendMessage(ms+"ó�� ���� �Ű� �����Ͱ� null���Դϴ�.");
			}else {
				String result = "��c�Ⱒ";
				p.sendMessage(ms+"��c�Ű�ID - ��6"+rData.id+"��c�� �Ⱒ�߽��ϴ�.");
				
				/*String reporterName = rData.reporter; //�Ű� �Ⱒ �޼��� ó�� ����
				if(reporterName != null) {
					Player reporter = Bukkit.getPlayer(reporterName);
					if(existPlayer(reporter)) {
						reporter.sendMessage(ms+"��6�Ű�ID - "+rData.id+"��c�� ó���Ǿ����ϴ�.");
						reporter.sendMessage(ms+"��c�Ű� ����� : ��6"+rData.target);
						reporter.sendMessage(ms+"��có�� ��� : ��6"+result);
					}
				}*/
				
				for(Player t : Bukkit.getOnlinePlayers()) {
					if(t.isOp()) {
						t.sendMessage(ms+"��6"+p.getName()+" ���� ��6�Ű�ID-"+rData.id+"��c�� ��c"+result+"ó�� �Ͽ����ϴ�.\n"
					+ms+"���� ��ó�� �Ű�� : ��6"+reportList.size()+"��");
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
				p.sendMessage(ms+"ó�� ���� �Ű� �����Ͱ� null���Դϴ�.");
			}else {
				String result = "��c������";
				p.sendMessage(ms+"��c�Ű�ID - ��6"+rData.id+"��c�� �������߽��ϴ�.");
				
				Bukkit.dispatchCommand(p, "ban"+" "+rData.target+" "+"������");
				Bukkit.dispatchCommand(p, "banip"+" "+rData.target+" "+"������");
				
				String reporterName = rData.reporter;
				if(reporterName != null) {
					Player reporter = Bukkit.getPlayer(reporterName);
					if(existPlayer(reporter)) {
						reporter.sendMessage(ms+"��6�Ű�ID - "+rData.id+"��c�� ó���Ǿ����ϴ�.");
						reporter.sendMessage(ms+"��c�Ű� ����� : ��6"+rData.target);
						reporter.sendMessage(ms+"��có�� ��� : ��6"+result);
					}
				}
				
				for(Player t : Bukkit.getOnlinePlayers()) {
					if(t.isOp()) {
						t.sendMessage(ms+"��6"+p.getName()+" ���� ��6�Ű�ID-"+rData.id+"��c�� ��c"+result+"ó�� �Ͽ����ϴ�.\n"
					+ms+"���� ��ó�� �Ű�� : ��6"+reportList.size()+"��");
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
				p.sendMessage(ms+"ó�� ���� �Ű� �����Ͱ� null���Դϴ�.");
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
					p.sendMessage(ms+"��c��Ʈ�� ������ ó�������� �������ּ���.");
					p.closeInventory();
					return;
				}
				
				if(muteHour > 0) {
					server.egCM.mute(rData.target, muteHour*3600);
				}
				if(banDay > 0) {
					Bukkit.dispatchCommand(p, "tempban"+" "+rData.target+" "+banDay+"d"+" "+rData.reason);
				}
				String result = "��c��Ʈ ��6"+muteHour+"�ð���c, �Ⱓ��: ��6"+banDay+"��";
				p.sendMessage(ms+"��6"+rData.target+"��c���� "+result+"��c�� ó���� ���Ƚ��ϴ�.");
				
				String reporterName = rData.reporter;
				if(reporterName != null) {
					Player reporter = Bukkit.getPlayer(reporterName);
					if(existPlayer(reporter)) {
						reporter.sendMessage(ms+"��6�Ű�ID - "+rData.id+"��c�� ó���Ǿ����ϴ�.");
						reporter.sendMessage(ms+"��6�Ű� ����� : ��6"+rData.target);
						reporter.sendMessage(ms+"ó�� ��� : "+result);
					}
				}
				
				for(Player t : Bukkit.getOnlinePlayers()) {
					if(t.isOp()) {
						t.sendMessage(ms+"��6"+p.getName()+" ���� ��6�Ű�ID-"+rData.id+"��c�� ��c"+result+"ó�� �Ͽ����ϴ�.\n"
					+ms+"���� ��ó�� �Ű�� : ��6"+reportList.size()+"��");
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
		server.egPM.printLog(ms+cnt+"���� �Ű����� �����"); 
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
				server.egPM.printLog(ms+file.getName()+" �� �о������ ���� �߻�");
				e.printStackTrace();
				continue;
			}
		}
		
		File staticFile = new File(getRootDirPath(), "reportStatic" + ".yml");
		try {
			FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(staticFile);
			ReportData.reportId = fileConfig.getInt("staticId");
		} catch (Exception e) {
			server.egPM.printLog(ms+staticFile.getName()+" �� �о������ ���� �߻�");
			e.printStackTrace();
		}
		
		updateReportUI(false);
		
		server.egPM.printLog(ms+loadCnt+" ���� ������ �ε��, staticId: "+ReportData.reportId);
		return loadCnt;
	}
	
	public String getDir() {
		return this.dirPath;
	}
	
	public String getRootDirPath() {
		return this.rootDirPath;
	}
	
	
	//�̺�Ʈ
	
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
			}else if(e.getInventory().getTitle().contains("ó�� ����")) {
				e.setCancelled(true);
				clickPunishUI(e.getInventory(), p, e.getSlot());
			}
		}
	}	
}
