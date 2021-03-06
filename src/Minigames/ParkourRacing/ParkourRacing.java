package Minigames.ParkourRacing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.coloredcarrot.api.sidebar.Sidebar;
import com.coloredcarrot.api.sidebar.SidebarString;
import com.connorlinfoot.titleapi.TitleAPI;

import EGServer.EGServer;
import EGServer.DataManger.PlayerData;
import EGServer.ServerManager.EGEventHandler;
import EGServer.ServerManager.EGScheduler;
import Minigames.Minigame;
import Utility.MyUtility;

public class ParkourRacing extends Minigame{
	// �̺�Ʈ��
	public EventHandlerPRC event;
	
	public String ms = "��7[��6��ü��7] ";
	///////////// private
	// ���� �÷��̾� ����Ʈ
	private HashMap<String, PRCPlayer> playerMap = new HashMap<String, PRCPlayer>();
	
	//////// ���� ����
	
	public HashMap<String, List<Location>> mapList = new HashMap<String, List<Location>>(); //���̸�, ��
	public HashMap<String, String> playingMapList = new HashMap<String, String>(); //�÷��̾��, �÷������� ���̸�
	public HashMap<String, Integer> checkPointMap = new HashMap<String, Integer>(); //�÷��̾��, �÷������� ���� üũ����Ʈ
	public HashMap<String, Double> startTimeMap = new HashMap<String, Double>(); //�÷��̾��, ���� �ð�
	
	public HashMap<String, List<String>> mapRankerName = new HashMap<String, List<String>>();
	public HashMap<String, List<Double>> mapRankerTime = new HashMap<String, List<Double>>();
	
	private ItemStack mapItem = new ItemStack(Material.GOLD_NUGGET);
	private ItemStack mapNavi = new ItemStack(Material.COMPASS);
	private ItemStack mapReturnPoint = new ItemStack(Material.PAPER);
	private ItemStack drink = new ItemStack(Material.POTION);
	
	public EGScheduler mainSch = new EGScheduler(this);
	private List<String> noMoveList = new ArrayList<String>();
	
	////// ���� �κ��丮
	public Inventory inven_gameHelper;
	public Inventory inven_mapSelect;
	
	//////// ���̵��
	private HashMap<String, Sidebar> sidebarMap = new HashMap<String, Sidebar>();
	private List<SidebarString> textList = new ArrayList<SidebarString>(5);
	SidebarString tmpLine = new SidebarString("");
	
	public ParkourRacing(EGServer server) {

		//////////////////// �ʼ� ������
		super(server);
		
		ms = "��7[ ��e! ��7] ��f: ��c���� ���̽� ��f>> "; // �⺻ �޼���
		gameName = "ParkourRacing";
		disPlayGameName = ChatColor.RED+"���� ���̽�";
		inventoryGameName = ChatColor.stripColor(disPlayGameName);
		minPlayer = 1;
		maxPlayer = 100;
		startCountTime = 50;
		doneSetting = loadGameData(); // ���� ���� ��ġ ��� �ε�
		/////////////////////// �ڵ� ����(�����۵��)
		dirSetting("ParkourRacing");
		////////////////
		
		//mainSch = new EGScheduler(this);
		
		/////////////////// ���� �����
		inven_gameHelper = Bukkit.createInventory(null, 27, ""+ChatColor.BLACK+ChatColor.BOLD+"�����");

		ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(" ");
		item.setItemMeta(meta);
		for (int i = 0; i <= 9; i++) {
			inven_gameHelper.setItem(i, item);
		}
		for (int i = 17; i < 27; i++) {
			inven_gameHelper.setItem(i, item);
		}
		
		item = new ItemStack(Material.ENCHANTED_BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7- ��c������ ��7-");
		List<String> loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7�������� 1�� Ÿ�Ӿ��� ���̽��Դϴ�.");
		loreList.add("��7�ٸ� �÷��̾��� ����� �Ѿ����!.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_gameHelper.setItem(10, item);
		
		inven_mapSelect = Bukkit.createInventory(null, 45, ""+ChatColor.BLACK+ChatColor.BOLD+"�� ����");

		item = new ItemStack(Material.STAINED_GLASS_PANE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName(" ");
		item.setItemMeta(meta);
		for (int i = 0; i <= 9; i++) {
			inven_mapSelect.setItem(i, item);
		}
		for (int i = 35; i < 45; i++) {
			inven_mapSelect.setItem(i, item);
		}
		inven_mapSelect.setItem(17, item);
		inven_mapSelect.setItem(26, item);
		inven_mapSelect.setItem(27, item);
		inven_mapSelect.setItem(18, item);
		
		item = new ItemStack(Material.BOOK_AND_QUILL, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��cƮ���̴� �� ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7������ �⺻ ������ ������ �ִ�");
		loreList.add("��7�Ʒ����Դϴ�.");
		loreList.add("");
		loreList.add("��7���̵� : ��4�١١١١�");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_mapSelect.setItem(10, item);
		
		item = new ItemStack(Material.EMPTY_MAP, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��cȣ�� ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7�Ʒ��忡�� ��� ����� ����غ��⿡");
		loreList.add("��7���� ������ ���Դϴ�.");
		loreList.add("");
		loreList.add("��7���̵� : ��4�ڡ١١١�");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_mapSelect.setItem(11, item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��c��ŷ ���� ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7�ش� ���� ���̽� �ð� ������ Ȯ���մϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_mapSelect.setItem(29, item);
		
		item = new ItemStack(Material.EMPTY_MAP, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��c��ȭ�� ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7�� ����� ���̵��� ���Դϴ�.");
		loreList.add("��7����� ���õ� ���� �����ϴ� ���� ��õ�մϴ�.");
		loreList.add("��7���ø����μ� ������ �����  ��� �� �ֽ��ϴ�.");
		loreList.add("");
		loreList.add("��7���̵� : ��4�ڡڡڡڡ�");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_mapSelect.setItem(14, item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��c��ŷ ���� ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7�ش� ���� ���̽� �ð� ������ Ȯ���մϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_mapSelect.setItem(32, item);
		
		item = new ItemStack(Material.EMPTY_MAP, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��c����  ���á�7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7�ſ� ����� ���̵��� ���Դϴ�.");
		loreList.add("��7�÷����Ͻô� �����ϴ� ��찡 �����ϴ�.");
		loreList.add("");
		loreList.add("��7���̵� : ��4�ڡڡڡڡ�");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_mapSelect.setItem(15, item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��c��ŷ ���� ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7�ش� ���� ���̽� �ð� ������ Ȯ���մϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_mapSelect.setItem(33, item);
		
		item = new ItemStack(Material.EMPTY_MAP, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��c����ž��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7������ �ĸ��� ����ž��");
		loreList.add("��7�ѹ� �ö� ���ðڽ��ϱ�?");
		loreList.add("");
		loreList.add("��7���̵� : ��4�ڡڡڡ١�");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_mapSelect.setItem(16, item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��c��ŷ ���� ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7�ش� ���� ���̽� �ð� ������ Ȯ���մϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_mapSelect.setItem(34, item);
		
		meta = mapItem.getItemMeta();
		meta.setDisplayName("��f[ ��c���̽� ���� ��f]");
		mapItem.setItemMeta(meta);
		
		meta = mapNavi.getItemMeta();
		meta.setDisplayName("��8[ ��c���� üũ����Ʈ ��ġ ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7���� üũ����Ʈ�� ��ġ��");
		loreList.add("��7����ŵ�ϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		mapNavi.setItemMeta(meta);
		
		meta = mapReturnPoint.getItemMeta();
		meta.setDisplayName("��8[ ��cüũ ����Ʈ�� �̵� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7������ üũ����Ʈ�� �̵��մϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		mapReturnPoint.setItemMeta(meta);
		
		meta = drink.getItemMeta();
		meta.setDisplayName("��8[ ��c������ �帵ũ ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7���׹̳��� ��� ȸ���մϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		drink.setItemMeta(meta);
		
		event = new EventHandlerPRC(server, this);
		// �� �÷����ο� �̺�Ʈ ����
		server.getServer().getPluginManager().registerEvents(event, server);
		
		mainSch();
	}

	/////////////// Minigame���� override �ؾ��ϴ� �κ�
	@Override
	public void gameHelpMsg(Player p) {

	}
	
	@Override
	public void joinGame(Player p) {
		if(server.specList.containsKey(p.getName())) {
			server.spawn(p);
		}
		if(!doneSetting) {
			p.sendMessage(server.ms_alert+"�ش� �̴ϰ����� ���� ������ �⺻ ������ �Ϸ���� �ʾҽ��ϴ�.");
		} else if(joinBlock) {
			p.sendMessage(server.ms_alert+"�ش� �̴ϰ����� �����ڿ� ���� ���� ���� �Ұ� �����Դϴ�.");
		} else if (ingamePlayer.contains(p.getName())) {
				p.sendMessage(server.ms_alert + "�̹� �� ���ӿ� �������̽ʴϴ�.");
		} else if (!server.noGameName.contains(server.playerList.get(p.getName()))) { //�÷������� �ൿ�� ���Ӿƴ� ��Ͽ� �������
			p.sendMessage(server.ms_alert + "�̹� �ٸ� �̴ϰ��ӿ� ������ �Դϴ�.");
		} else if (ingamePlayer.size() >= maxPlayer) {
			p.sendMessage(server.ms_alert + "�̹� �ִ��ο��� "+maxPlayer +"���� �÷��̾ �������Դϴ�.");
		} else {
			PlayerData pData = server.egDM.getPlayerData(p.getName());
			if(pData != null) pData.setGame(this);
			server.waitingPlayer.put(p.getName(),disPlayGameName);
			MyUtility.allClear(p);
			p.setFoodLevel(20);
			p.closeInventory();
			ingamePlayer.add(p.getName());
			server.playerList.put(p.getName(), disPlayGameName);
			server.spawnList.remove(p.getName());
			server.waitingPlayer.remove(p.getName());		
			p.getInventory().setItem(8, helpItem);
			p.getInventory().setHeldItemSlot(8);
			p.teleport(loc_Join, TeleportCause.PLUGIN);
			sendMessage(server.ms_alert + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " ���� �����߽��ϴ�. " + ChatColor.RESET
					+ "[ " + ChatColor.GREEN + ingamePlayer.size() + ChatColor.GRAY +" �� �÷�����"+ChatColor.RESET
					+ " ]");
			TitleAPI.sendTitle(p, 10, 70, 30,ChatColor.RED+""+disPlayGameName);
			sendSound(Sound.BLOCK_NOTE_PLING, 2.0f, 1.0f);
			
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 72000, 1));
			p.setGameMode(GameMode.SURVIVAL);
			server.egParkour.join(p.getName());
			p.getInventory().addItem(mapItem);
			Sidebar sidebar = new Sidebar("��f[ ��6���� ��Ȳ ��f]", server, 600, tmpLine);
			sidebarMap.put(p.getName(), sidebar);
			sidebar.showTo(p);
		}
	}

	//////////////////
	
	public void mainSch() {
		
		mainSch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				for(String pName : sidebarMap.keySet()) {
					Player p = Bukkit.getPlayer(pName);
					Sidebar sidebar = sidebarMap.get(pName);	
					if(existPlayer(p)) {
						textList.clear();
						if(playingMapList.containsKey(p.getName()) && checkPointMap.containsKey(p.getName())) {								
							SidebarString line = new SidebarString("");
							SidebarString blank = new SidebarString("");
							textList.add(blank);
							line = new SidebarString("��e���� üũ����Ʈ");
							textList.add(line);
							line = new SidebarString("��a"+checkPointMap.get(p.getName())+" ��e��");
							textList.add(line);
							textList.add(blank);
							line = new SidebarString("��e�ð� ��f: ��a"+(int)(getTimeRecord(p.getName()))+"��");
							textList.add(line);
							textList.add(blank);						
						} else {
							SidebarString line = new SidebarString("");
							textList.add(line);
							line = new SidebarString("��b���� ���̽�");
							textList.add(line);
						}	
						sidebar.setEntries(textList);
						sidebar.update();
					}
				}
			}
		}, 0l, 20l);
		
	}

	public void initGame() {
		lobbyStart = false;
		ending = false;
		//������
		schList.clear();
	}

	public boolean loadGameData() {
		boolean ret = true;
		loc_Join = loadLocation(gameName, "JoinPos");
		loc_spectate = loc_Join;
	
		if (loc_Join == null) {
			server.egPM.printLog(ms + "[" + disPlayGameName + "] ���� ���� ��� ������ �������� �ʾҽ��ϴ�.");
			ret = false;
		}
		File file = new File(server.getDataFolder().getPath() + "/" + gameName + "/Location", "location.yml");	
		FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
		List<String> mapNameList = fileConfig.getStringList("mapList");
		for(String mapName : mapNameList) {
			ConfigurationSection section = fileConfig.getConfigurationSection(mapName);
			int maxPoint = section.getInt("maxPoint");
			server.egPM.printLog("[" + disPlayGameName + "] "+mapName+" ����"+maxPoint+"���� �ε���");
			List<Location> checkPointList = new ArrayList<Location>();
			for(int i = 0; i <= maxPoint; i++) {
				String posW = section.getString(i + "_w");
				int posX = section.getInt(i + "_x");
				int posY = section.getInt(i + "_y");
				int posZ = section.getInt(i + "_z");
				Location loc = new Location(Bukkit.getWorld(posW), posX, posY, posZ);
				checkPointList.add(loc);
			}
			mapList.put(mapName, checkPointList);	
			List<String> rankNames = new ArrayList<String>();
			List<Double> rankTimes = new ArrayList<Double>();
			ConfigurationSection rankSection = section.getConfigurationSection("ranking");
			if(rankSection == null) rankSection = section.createSection("ranking");
			for(int i = 1; i < 11; i++){
				String rank_name = rankSection.getString(("rankerName_"+i));
				double rank_time = rankSection.getDouble(("rankerTime_"+i));
				if(rank_name == null) {
					break;
				}
				rankNames.add(rank_name);
				rankTimes.add(rank_time);
			}
			mapRankerName.put(mapName, rankNames);
			mapRankerTime.put(mapName, rankTimes);
			
			server.egPM.printLog("[" + disPlayGameName + "] "+mapName+" ���� "+checkPointList.size()+"���� üũ����Ʈ �ε��");
		}
		if(ret == true) {
			server.egPM.printLog("[" + disPlayGameName + "] ���� �̻� ����");
			doneSetting = ret;
		}
		
		return ret;
	}
	
	public void onCommand(Player p, String[] cmd) {
		if (cmd.length <= 1) {
			p.sendMessage(ms + "[" + disPlayGameName + "] " + "/prc join - ���� ����");
			p.sendMessage(ms + "[" + disPlayGameName + "] " + "/prc quit - ���� ����");
			p.sendMessage(ms + "[" + disPlayGameName + "] " + "/prc set - ���� ����");
		} else if (cmd[1].equalsIgnoreCase("set")) {
			if (cmd.length <= 2) {
				p.sendMessage(ms + "[" + disPlayGameName + "] " + "/prc set loc- ���� ���� ����");
			} else if (cmd[2].equalsIgnoreCase("loc")) {
				if (cmd.length <= 3) {
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "/prc set loc join - ���� ���� ��� ���� ����");
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "/prc set loc checkpoint <��> <��ȣ> - �n�� ����");
				} else if (cmd[3].equalsIgnoreCase("join")) {
					saveLocation(gameName, "JoinPos", p.getLocation());
					loadGameData();
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "���� ���� ��� ������ �����Ǿ����ϴ�.");
				}else if (cmd[3].equalsIgnoreCase("checkPoint")) {
					if(cmd.length >= 6) {
						String mapName = cmd[4];
						int pointNum = Integer.valueOf(cmd[5]);
						Location loc = p.getLocation().subtract(0,1,0);
						int maxPoint = -1;
						if(mapList.containsKey(mapName)) {
							maxPoint = mapList.get(mapName).size()-1;
						}				
						try {
							File file = new File(server.getDataFolder().getPath() + "/" + gameName + "/Location", "location.yml");
							FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
							ConfigurationSection section = fileConfig.getConfigurationSection(mapName);
							if(section == null) section = fileConfig.createSection(mapName);
							section.set(pointNum + "_w", loc.getWorld().getName());
							section.set(pointNum + "_x", Integer.valueOf(loc.getBlockX()));
							section.set(pointNum + "_y", Integer.valueOf(loc.getBlockY()));
							section.set(pointNum + "_z", Integer.valueOf(loc.getBlockZ()));
							section.set("maxPoint", maxPoint+1);
							List<String> mapNameList = new ArrayList<String>(mapList.keySet());
							if(!mapNameList.contains(mapName)) mapNameList.add(mapName);
							fileConfig.set("mapList", mapNameList);
							fileConfig.save(file);
							p.sendMessage(ms+"üũ����Ʈ "+mapName+" �� "+pointNum+" / "+(maxPoint+1)+" ����, Location - "+loc);
						} catch (Exception e) {
							server.egPM.printLog("[" + disPlayGameName + "]" + mapName+" "+pointNum + "�������� ���� �߻�");
						}
						loadGameData();
					}else {
						p.sendMessage(ms+"�ùٸ��� ���� �Է�");
					}					
				}else {
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "/prc set loc join - ���� ���� ��� ���� ����");
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "/prc set loc checkpoint <��> <��ȣ> - �n�� ����");
				}
			}
		} else if (cmd[1].equalsIgnoreCase("join")) {
			if(cmd.length > 2) joinGame(Bukkit.getPlayer(cmd[2]));
			else joinGame(p);
		} else if (cmd[1].equalsIgnoreCase("forceend")) {
			endGame(true);
		}else if (cmd[1].equalsIgnoreCase("quit")) {
			gameQuitPlayer(p, true, false);
		}  else if (cmd[1].equalsIgnoreCase("block")) {
			if(joinBlock) joinBlock = false;
			else joinBlock = true;
		} else if (cmd[1].equalsIgnoreCase("start")) {
			startCount();
		} else if(cmd[1].equalsIgnoreCase("debug0")) {
			checkPointMap.put(p.getName(), mapList.get(playingMapList.get(p.getName())).size());
		} else if(cmd[1].equalsIgnoreCase("debug1")) {
			startRacing(p, cmd[2]);
		} 
	}

	public void gameQuitPlayer(Player p, boolean announce, boolean giveGold) {
		if (ingamePlayer.contains(p.getName())) {
			server.egParkour.quit(p.getName());
			ingamePlayer.remove(p.getName());
			server.playerList.put(p.getName(), "�κ�");
			p.getInventory().clear();
			quitRacing(p);
			sidebarMap.remove(p.getName());
			if (announce) {
				sendMessage(ms + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " ���� �����ϼ̽��ϴ�. " + ChatColor.RESET
						+ "[ " + ChatColor.GREEN + ingamePlayer.size() + ChatColor.GRAY +" �� �÷�����"+ChatColor.RESET
						+ " ]");
				sendSound(Sound.BLOCK_CLOTH_BREAK, 1.5f, 1.5f);
			}
		}
	}
	
	public void performence(Player p) {
		EGScheduler sch = new EGScheduler(this);
		sch.schTime = 6;
		sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				if(sch.schTime> 0) {
					sch.schTime--;
					if(existPlayer(p)) {
						for(int i = 0; i < 5; i++) {
							Location tmpL = p.getLocation().clone();
							tmpL.add(MyUtility.getRandom(-3, 3), MyUtility.getRandom(-3, 3), MyUtility.getRandom(-3, 3));
							Firework fw = (Firework) tmpL.getWorld().spawnEntity(tmpL, EntityType.FIREWORK);
							FireworkMeta fwm = fw.getFireworkMeta();

							fwm.setPower(1);
							fwm.addEffect(FireworkEffect.builder().withColor(MyUtility.getRandomColor()).flicker(true).build());

							fw.setFireworkMeta(fwm);
							fw.detonate();
							
					        Firework fw2 = (Firework) tmpL.getWorld().spawnEntity(tmpL, EntityType.FIREWORK);
					        fw2.setFireworkMeta(fwm);
						}
					}			
				}else {
					sch.cancelTask(true);
				}
			}
		}, 0l, 20l);
	}

	private void setWinner(Player p, String mapName) {
			MyUtility.allClear(p);
		
			performence(p);			
			
			try {
				Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
					public void run() {
							if(existPlayer(p)) {
								//server.egGM.giveGold(p.getName(), 40);
								p.sendMessage(ms + "���� �������� 150��带 �����̽��ϴ�.");
								sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
								p.teleport(loc_Join, TeleportCause.PLUGIN);
								p.getInventory().clear();
								p.getInventory().addItem(mapItem);
								playingMapList.remove(p.getName());
								checkPointMap.remove(p.getName());
								noMoveList.remove(p.getName());
								startTimeMap.remove(p.getName());
								showRanking(p, mapName);
							}
					}
				}, 120L);
			} catch (Exception e) {
				
			}
	
	}

	public void endGame(boolean force) {
		if(force) {
			server.egCM.broadCast(server.ms_alert + "���� ���̽� ������ ���� ���� �Ǿ����ϴ�.");
		}
		divideSpawn();
		//Bukkit.getLogger().info("mainid: "+mainSch.schId);
		try {
			for(EGScheduler sch : schList) {
				sch.cancelTask(false);
			}
			
		}catch(Exception e) {
			
		}
		
		schList.clear();
		ingame = false;
		ending = false;
		ingamePlayer.clear();
		initGame();	
	}
	
	public void mapSelectClick(Player p, int slot) {
		switch (slot) {
		case 10:
			quitRacing(p);
			p.closeInventory();
			p.teleport(loc_Join, TeleportCause.PLUGIN);
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
			return;	
		case 11:
			quitRacing(p);
			p.closeInventory();
			startRacing(p, "hotel");
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
			return;
		case 14:
			quitRacing(p);
			p.closeInventory();
			startRacing(p, "highCity");
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
			return;
		case 16:
			quitRacing(p);
			p.closeInventory();
			startRacing(p, "eiffeltop");
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
			return;
		case 29:
			p.closeInventory();
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
			showRanking(p, "hotel");
			return;	
		case 32:
			p.closeInventory();
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
			showRanking(p, "highCity");
			return;
		case 34:
			p.closeInventory();
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
			showRanking(p, "eiffeltop");
			return;

		default:
			return;
		}
	}
	
	public double getTimeRecord(String pName) {
		if(startTimeMap.containsKey(pName)) {
			double startTime = startTimeMap.get(pName);
			double record = System.currentTimeMillis() - startTime;
//			server.egPM.printLog("[" + disPlayGameName + "] record "+record);
			record = (int)(record/10);
//			server.egPM.printLog("[" + disPlayGameName + "] record "+(double)record/100);
			return (double)record/100;
		} else {
			return 0;
		}
	}
	
	public void saveNewRecord(Player p, String mapName, double record) {
		File file = new File(server.getDataFolder().getPath() + "/" + gameName + "/Location", "location.yml");	
		FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
		ConfigurationSection mapSection = fileConfig.getConfigurationSection(mapName);
		ConfigurationSection section = mapSection.getConfigurationSection("record");
		if(section == null) section = mapSection.createSection("record");
		double baseRecord = section.getDouble(p.getName()+"_record");
		int clearCnt = section.getInt(p.getName()+"_clearCnt");
		if(baseRecord == 0) {
			p.sendMessage(ms+"�� ���̽� �ʿ� ���� ���ο� ����� ����ƽ��ϴ�. \n+"+ms+"��f��� : ��a��l"+record+"��f��");
			section.set(p.getName()+"_record", record);
			section.set(p.getName()+"_clearCnt", clearCnt+1);
		}else {
			if(baseRecord > record) {
				p.sendMessage(ms+"���� ����� ��b"+baseRecord+" ��f�� ���� �� ª�� ����� �޼��Ͽ����ϴ�.");
				p.sendMessage(ms+"���ο� ����� ��b"+record+" ��f�� �� �����մϴ�.");
				section.set(p.getName()+"_record", record);
				section.set(p.getName()+"_clearCnt", clearCnt+1);
			}else {
				p.sendMessage(ms+"���� ����� ��b"+baseRecord+" ��f�ʰ� �� ª�� ���ο� ����� �������� �ʽ��ϴ�.");
				section.set(p.getName()+"_clearCnt", clearCnt+1);
			}
		}
		p.sendMessage(ms+"�� ���� ��b"+(clearCnt+1)+" ��f�� �����Ͽ����ϴ�.");
		List<String> rankNames = mapRankerName.get(mapName);
		List<Double> rankTimes = mapRankerTime.get(mapName);
		
		ConfigurationSection rankSection = mapSection.getConfigurationSection("ranking");
		if(rankSection == null) rankSection = mapSection.createSection("ranking");
		if(rankTimes.size() <= 0) {
			rankSection.set("rankerName_"+1, p.getName());
			rankSection.set("rankerTime_"+1, record);
			for(int k = 2; k < 11; k++) {
				rankSection.set("rankerName_"+k, "ParkourMaster");
				rankSection.set("rankerTime_"+k, 3600);
			}	
		} else {
			for(int i = 0; i < rankTimes.size(); i++){
				if(rankTimes.get(i) > record){
					if(rankNames.contains(p.getName())){
						if(rankTimes.get(rankNames.indexOf(p.getName())) <= record) return;
						else{
							for(int j = rankNames.indexOf(p.getName()); j > i; j--){
								rankNames.set(j, rankNames.get(j-1));
								rankTimes.set(j, rankTimes.get(j-1));
							} 
							rankNames.set(i, p.getName());
							rankTimes.set(i, record);
						}
					}else{
						for(int j = 9; j > i; j--){
							rankNames.set(j, rankNames.get(j-1));
							rankTimes.set(j, rankTimes.get(j-1));
						} 
						rankNames.set(i, p.getName());
						rankTimes.set(i, record);
					}
					for(int k = 0; k < 10; k++){
						rankSection.set("rankerName_"+(k+1), rankNames.get(k));
						rankSection.set("rankerTime_"+(k+1), rankTimes.get(k));
					}
					break;
				}
			}
		}		
		try {
			fileConfig.save(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		loadGameData();
	}
	
	public void quitRacing(Player p) {
		if(playingMapList.containsKey(p.getName())) {
			p.sendMessage(ms+"���� ���̽��� ����߽��ϴ�. \n+"+ms+"������ üũ����Ʈ : "+checkPointMap.get(p.getName())+", �ɸ� �ð�  : "+getTimeRecord(p.getName()));
			playingMapList.remove(p.getName());
			checkPointMap.remove(p.getName());
			noMoveList.remove(p.getName());
			startTimeMap.remove(p.getName());		
		}
	}
	
	public String getDisplayMapName(String mapName) {
		String displayMapName = "";
		if(mapName.equalsIgnoreCase("highCity")) {
			displayMapName =  "��ȭ��";
		}else if(mapName.equalsIgnoreCase("construction")) {
			displayMapName = "�Ǽ� ����";
		}else if(mapName.equalsIgnoreCase("hotel")) {
			displayMapName = "ȣ��";
		}else if(mapName.equalsIgnoreCase("space")) {
			displayMapName = "���� ����";
		}else if(mapName.equalsIgnoreCase("eiffeltop")) {
			displayMapName = "����ž";
		}
		return displayMapName;
	}
	
	public void showRanking(Player p, String mapName) {
		List<String> rankNames = mapRankerName.get(mapName);
		List<Double> rankTimes = mapRankerTime.get(mapName);
		
		if(rankNames.size() >= 1) {
			p.sendMessage("��c=========== "+getDisplayMapName(mapName)+" ===========");
			p.sendMessage("��c=========== ��ŷ ===========");
			for(int i = 0; i < rankNames.size(); i++) {
				String pName = rankNames.get(i);
				p.sendMessage("��e"+(i+1)+". "+pName+" ��7: ��a"+rankTimes.get(i)+"��e��");
			}
			p.sendMessage("��c=========== ����� �ְ� ��� ===========");
			File file = new File(server.getDataFolder().getPath() + "/" + gameName + "/Location", "location.yml");	
			FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
			ConfigurationSection mapSection = fileConfig.getConfigurationSection(mapName);
			ConfigurationSection section = mapSection.getConfigurationSection("record");
			if(section == null) section = mapSection.createSection("record");
			double baseRecord = section.getDouble(p.getName()+"_record");
			int clearCnt = section.getInt(p.getName()+"_clearCnt");
			if(baseRecord == 0) {
				p.sendMessage("��e���� �� �ʿ� ���� ����� ������ ���� �ʽ��ϴ�.");
			} else {
				p.sendMessage("��e���� �ð� : ��a"+baseRecord+" ��e��");
				p.sendMessage("��e���� Ƚ�� : ��a"+clearCnt+ " ��e��");
			}
		}else {
			p.sendMessage(ms+"�ش� �ʿ� ���� ���̽� ����� ���� �����ϴ�.");
		}
	}
	
	public void startRacing(Player p, String mapName) {
		MyUtility.allClear(p);
		List<Location> checkPointList = mapList.get(mapName);
		playingMapList.put(p.getName(), mapName);
		checkPointMap.put(p.getName(), 0);
		startTimeMap.remove(p.getName());
		noMoveList.add(p.getName());
		p.setFallDistance(0);
		showRanking(p, mapName);
		p.teleport(checkPointList.get(0), TeleportCause.PLUGIN);
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
				TitleAPI.sendFullTitle(p, 10, 70, 30,ChatColor.RED+"��l5","��8��l���̽� ���۱���");
			}
		}, 20l);
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
				TitleAPI.sendFullTitle(p, 10, 70, 30,ChatColor.RED+"��l4","��8��l���̽� ���۱���");
			}
		}, 40l);
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
				TitleAPI.sendFullTitle(p, 10, 70, 30,ChatColor.RED+"��l3","��8��l���̽� ���۱���");
			}
		}, 60l);
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
				TitleAPI.sendFullTitle(p, 10, 70, 30,ChatColor.RED+"��l2","��8��l���̽� ���۱���");
			}
		}, 80l);
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
				TitleAPI.sendFullTitle(p, 10, 70, 30,ChatColor.RED+"��l1","��8��l���̽� ���۱���");
			}
		}, 100l);
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				if(ingamePlayer.contains(p.getName())) {
					MyUtility.allClear(p);
					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BELL, 1.0f, 1.0f);
					TitleAPI.sendFullTitle(p, 10, 70, 30,ChatColor.RED+"��l����", "��8��l"+getDisplayMapName(mapName));
					noMoveList.remove(p.getName());
					startTimeMap.put(p.getName(), (double)System.currentTimeMillis());			
					p.getInventory().setItem(0, mapNavi);
					p.getInventory().setItem(1, mapReturnPoint);
					p.getInventory().setItem(8, mapItem);
					p.setCompassTarget(checkPointList.get(1));
					p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 72000, 1));
					p.setExp(1f);
					if(mapName.equalsIgnoreCase("eiffeltop")) {
						p.getInventory().addItem(drink);	
						p.getInventory().addItem(drink);	
						p.sendMessage(ms+"�� ���� ������ �帵ũ�� ���޵Ǵ� ���Դϴ�!");
					}else if(mapName.equalsIgnoreCase("hotel")) {
						p.getInventory().addItem(drink);
						p.sendMessage(ms+"�� ���� ������ �帵ũ�� ���޵Ǵ� ���Դϴ�!");
					}
				}			
			}
		}, 120l);
	}

	//////////////// �̺�Ʈ
	public class EventHandlerPRC extends EGEventHandler {

		private ParkourRacing game;

		public EventHandlerPRC(EGServer server, ParkourRacing prc) {
			super(server);
			this.game = prc;
		}

		@EventHandler
		public void onPlayerMove(PlayerMoveEvent e) {
			Player p = e.getPlayer();
			if(ingamePlayer.contains(p.getName())) {
				if(e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockY() == e.getTo().getBlockY()
						&& e.getFrom().getBlockZ() == e.getTo().getBlockZ()) return;
				if(noMoveList.contains(p.getName())) {
					egCancelMovement(e);
				}
				Location gotoLoc = new Location(e.getTo().getWorld(), e.getTo().getBlockX(), e.getTo().getBlockY(), e.getTo().getBlockZ());
				gotoLoc.setY(gotoLoc.getBlockY()-1);
				if(gotoLoc.getBlock().getType() == Material.BEACON && playingMapList.containsKey(p.getName())) {
					List<Location> checkPointList = mapList.get(playingMapList.get(p.getName()));
					int nextCheckPoint = checkPointMap.get(p.getName())+1;
					Location nextCheckPointLocation = checkPointList.get(nextCheckPoint);
					if(MyUtility.compareLocation(gotoLoc.getBlock().getLocation(), nextCheckPointLocation)) {
						p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0F, 1.5F);
						p.sendMessage(ms+"üũ����Ʈ ��a"+nextCheckPoint+"��f�� ����, ���� ��b"+getTimeRecord(p.getName())+" ��f��");
						if(checkPointList.size() == nextCheckPoint+1) {
							p.sendMessage(ms+"���� ����!");	
							checkPointMap.put(p.getName(), 0);
							double record = getTimeRecord(p.getName());
							TitleAPI.sendFullTitle(p, 30, 100, 30, "��c��l���� ����", "��6��l��� ��a��l"+record+" ��6��l��");
							p.sendMessage(ms+"��� - ��a��l"+record+"��f��");
							setWinner(p, playingMapList.get(p.getName()));
							saveNewRecord(p, playingMapList.get(p.getName()), record);
						}else {
							TitleAPI.sendFullTitle(p, 20, 60, 20, "��c��l"+nextCheckPoint, "��6��lüũ����Ʈ ����");
							checkPointMap.put(p.getName(), nextCheckPoint);
							p.setCompassTarget(checkPointList.get(nextCheckPoint+1));
							p.setLevel(nextCheckPoint);
						}
					}		
				}
				if(p.getLocation().getY() < 25) {
					p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 72000,2));
				}else {
					p.removePotionEffect(PotionEffectType.SLOW);
				}
			}
		}
		
		@EventHandler
		public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
			Player p = e.getPlayer();
			String[] cmd = e.getMessage().split(" ");
			if (cmd[0].equalsIgnoreCase("/prc")) {
				e.setCancelled(true);
				if (p.isOp()) {
					game.onCommand(p, cmd);
				}
			} 
			if(ingamePlayer.contains(p.getName())) { //���� �����ؾ����� ������ ��ɾ�
				if(server.cmdSpawn.contains(cmd[0])) {
					gameQuitPlayer(p, true, false);
				}
			}
		}

		@EventHandler
		public void onFall(EntityDamageEvent e) {
			if (e.getEntityType() == EntityType.PLAYER) {
				Player p = (Player) e.getEntity();
				DamageCause cause = e.getCause();
				if (ingamePlayer.contains(p.getName())) {
					if (cause.equals(DamageCause.VOID)) { //���� ��� ������, ����	
			            gameQuitPlayer(p, true, true);
			            server.spawn(p);
			        } else if(cause.equals(DamageCause.FALL)) {
			        	
			        } else {
			        	e.setCancelled(true);
			        }
				}
			}
		}

		@EventHandler
		public void onClickInventory(InventoryClickEvent e) {
			if (!(e.getWhoClicked() instanceof Player))
				return;
			Player p = (Player) e.getWhoClicked();
			if (!ingamePlayer.contains(p.getName()))
				return;
			if (e.getInventory().getTitle().equalsIgnoreCase("��0��l�����")) {				
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}	
				//gameHelper(p, e.getSlot());
			} else if (e.getInventory().getTitle().equalsIgnoreCase("��0��l�� ����")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}	
				p.closeInventory();
				mapSelectClick(p, e.getSlot());
			} 
		}
		
		@EventHandler
		public void onPlayerQuit(PlayerQuitEvent e) {
			Player p = e.getPlayer();
			if (!ingamePlayer.contains(p.getName())) return;
			gameQuitPlayer(p, true, false);
		}

		@EventHandler
		public void onRightClick(PlayerInteractEvent e) {
			Player p = e.getPlayer();
			Action action = e.getAction();
			if (!ingamePlayer.contains(p.getName()) //��Ŭ���� ���
					|| (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK))
				return;
			if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.CHEST) {
				e.setCancelled(true);			
			}
			if (e.getItem() == null || e.getItem().getType() == Material.AIR) { //�������� �ȵ�� ��Ŭ�������� ����
				return;
			} else if (getHeldMainItemName(p).equalsIgnoreCase("��f[ ��6���� ����� ��f]")) {
				p.openInventory(inven_gameHelper);
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 2.0f);
			} else if(e.getItem().getType() == Material.GOLD_NUGGET) {
				p.openInventory(inven_mapSelect);
			} else if(e.getItem().getType() == Material.PAPER) {
				if(playingMapList.containsKey(p.getName()) && checkPointMap.containsKey(p.getName())) {
					List<Location> chekcPointList = mapList.get(playingMapList.get(p.getName()));
					int checkPointNum = checkPointMap.get(p.getName());
					p.teleport(chekcPointList.get(checkPointNum).clone().add(0,1,0), TeleportCause.PLUGIN);
					TitleAPI.sendFullTitle(p, 10, 70, 30,"",ChatColor.RED+"��l"+checkPointNum+"��8��l�� üũ����Ʈ�� ���ư�");
					p.playSound(p.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1.0f, 2.0f);	
				}
				
			}
		}
		
		@EventHandler
		public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent e) {
			Player p = e.getPlayer();
			if(!ingamePlayer.contains(p.getName())) return;
			ItemStack item = e.getItem();
			if(item.getType() == Material.POTION) {
				p.setExp(1f);
				TitleAPI.sendFullTitle(p, 10, 70, 30,"","��8��l������ �帵ũ �����");
				p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_DRINK, 1.0f, 2.0f);
			}
			item.setType(Material.AIR);
			e.setItem(item);
		}
		
		@EventHandler
		public void onFoodLevelChange(FoodLevelChangeEvent e){
			if(e.getEntity() instanceof Player){
				Player p = (Player) e.getEntity();
				if(ingamePlayer.contains(p.getName())) e.setFoodLevel(20);
			}
		}
		
		@EventHandler
		public void onPlayerDeath(PlayerDeathEvent e){
			if(e.getEntity() instanceof Player){
				Player p = (Player) e.getEntity();
				if(ingamePlayer.contains(p.getName())) {
					e.getDrops().clear();
					gameQuitPlayer(p, true, true);
				}				
			}
		}
		
		@EventHandler
		public void onPlayerChat(PlayerChatEvent e){
			Player p = (Player) e.getPlayer();
			if(ingamePlayer.contains(p.getName())) {
				e.setCancelled(true);
				String str = game.ms+p.getName()+" >> ��6"+e.getMessage();
				server.egCM.sendMessagesToStringList(ingamePlayer, p, str, false);
				sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 1.5f);		
			}
		}
		
		@EventHandler
		public void onItemDrop(PlayerDropItemEvent e) {
			Player p = e.getPlayer();
			if(!ingamePlayer.contains(p.getName())) return;
			e.setCancelled(true);
		}
		
		@EventHandler
		public void onInventoryClick(InventoryClickEvent e) {
			if(!(e.getWhoClicked() instanceof Player)) return;
			Player p = (Player)e.getWhoClicked();
			if(!ingamePlayer.contains(p.getName())) return;
			if(e.getCurrentItem() == null) return;
			if(e.getCurrentItem().getType() == Material.TNT || e.getCurrentItem().getType() == Material.DIAMOND_SWORD)
				e.setCancelled(true);
		}
		
		@EventHandler
		public void onBlockBreak(BlockBreakEvent e) {
			Player p = e.getPlayer();
			if(ingamePlayer.contains(p.getName())) {
				e.setCancelled(true);
			}
		}

		@EventHandler
		public void onBlockPlaced(BlockPlaceEvent e) {
			Player p = e.getPlayer();
			if(ingamePlayer.contains(p.getName())) {
				e.setCancelled(true);
			}
		}
	}
	
	///////////////////// ���� ���̽̿� ������ �÷��̾�� Ŭ����
	private class PRCPlayer {
		
		public PRCPlayer(Player p, String job) {

		}

	}
}
