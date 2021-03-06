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
	// 이벤트용
	public EventHandlerPRC event;
	
	public String ms = "§7[§6전체§7] ";
	///////////// private
	// 게임 플레이어 리스트
	private HashMap<String, PRCPlayer> playerMap = new HashMap<String, PRCPlayer>();
	
	//////// 게임 관련
	
	public HashMap<String, List<Location>> mapList = new HashMap<String, List<Location>>(); //맵이름, 맵
	public HashMap<String, String> playingMapList = new HashMap<String, String>(); //플레이어명, 플레이중인 맵이름
	public HashMap<String, Integer> checkPointMap = new HashMap<String, Integer>(); //플레이어명, 플레이중인 맵의 체크포인트
	public HashMap<String, Double> startTimeMap = new HashMap<String, Double>(); //플레이어명, 시작 시간
	
	public HashMap<String, List<String>> mapRankerName = new HashMap<String, List<String>>();
	public HashMap<String, List<Double>> mapRankerTime = new HashMap<String, List<Double>>();
	
	private ItemStack mapItem = new ItemStack(Material.GOLD_NUGGET);
	private ItemStack mapNavi = new ItemStack(Material.COMPASS);
	private ItemStack mapReturnPoint = new ItemStack(Material.PAPER);
	private ItemStack drink = new ItemStack(Material.POTION);
	
	public EGScheduler mainSch = new EGScheduler(this);
	private List<String> noMoveList = new ArrayList<String>();
	
	////// 각종 인벤토리
	public Inventory inven_gameHelper;
	public Inventory inven_mapSelect;
	
	//////// 사이드바
	private HashMap<String, Sidebar> sidebarMap = new HashMap<String, Sidebar>();
	private List<SidebarString> textList = new ArrayList<SidebarString>(5);
	SidebarString tmpLine = new SidebarString("");
	
	public ParkourRacing(EGServer server) {

		//////////////////// 필수 설정값
		super(server);
		
		ms = "§7[ §e! §7] §f: §c파쿠르 레이싱 §f>> "; // 기본 메세지
		gameName = "ParkourRacing";
		disPlayGameName = ChatColor.RED+"파쿠르 레이싱";
		inventoryGameName = ChatColor.stripColor(disPlayGameName);
		minPlayer = 1;
		maxPlayer = 100;
		startCountTime = 50;
		doneSetting = loadGameData(); // 게임 시작 위치 등등 로드
		/////////////////////// 자동 설정(아이템등등)
		dirSetting("ParkourRacing");
		////////////////
		
		//mainSch = new EGScheduler(this);
		
		/////////////////// 게임 도우미
		inven_gameHelper = Bukkit.createInventory(null, 27, ""+ChatColor.BLACK+ChatColor.BOLD+"도우미");

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
		meta.setDisplayName("§7- §c진행방식 §7-");
		List<String> loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7전통적인 1인 타임어택 레이싱입니다.");
		loreList.add("§7다른 플레이어의 기록을 넘어보세요!.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_gameHelper.setItem(10, item);
		
		inven_mapSelect = Bukkit.createInventory(null, 45, ""+ChatColor.BLACK+ChatColor.BOLD+"맵 선택");

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
		meta.setDisplayName("§7[ §c트레이닝 룸 §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7파쿠르의 기본 동작을 익힐수 있는");
		loreList.add("§7훈련장입니다.");
		loreList.add("");
		loreList.add("§7난이도 : §4☆☆☆☆☆");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_mapSelect.setItem(10, item);
		
		item = new ItemStack(Material.EMPTY_MAP, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §c호텔 §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7훈련장에서 배운 기술을 사용해보기에");
		loreList.add("§7아주 적절한 맵입니다.");
		loreList.add("");
		loreList.add("§7난이도 : §4★☆☆☆☆");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_mapSelect.setItem(11, item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §c랭킹 보기 §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7해당 맵의 레이싱 시간 순위를 확인합니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_mapSelect.setItem(29, item);
		
		item = new ItemStack(Material.EMPTY_MAP, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §c번화가 §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7꽤 어려운 난이도의 맵입니다.");
		loreList.add("§7충분히 숙련된 다음 도전하는 것을 추천합니다.");
		loreList.add("§7도시맵으로서 파쿠르를 제대로  즐길 수 있습니다.");
		loreList.add("");
		loreList.add("§7난이도 : §4★★★★☆");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_mapSelect.setItem(14, item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §c랭킹 보기 §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7해당 맵의 레이싱 시간 순위를 확인합니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_mapSelect.setItem(32, item);
		
		item = new ItemStack(Material.EMPTY_MAP, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §c우주  도시§7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7매우 어려운 난이도의 맵입니다.");
		loreList.add("§7플레이하시다 포기하는 경우가 많습니다.");
		loreList.add("");
		loreList.add("§7난이도 : §4★★★★★");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_mapSelect.setItem(15, item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §c랭킹 보기 §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7해당 맵의 레이싱 시간 순위를 확인합니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_mapSelect.setItem(33, item);
		
		item = new ItemStack(Material.EMPTY_MAP, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §c에펠탑§7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7프랑스 파리의 에펠탑을");
		loreList.add("§7한번 올라가 보시겠습니까?");
		loreList.add("");
		loreList.add("§7난이도 : §4★★★☆☆");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_mapSelect.setItem(16, item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7[ §c랭킹 보기 §7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7해당 맵의 레이싱 시간 순위를 확인합니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_mapSelect.setItem(34, item);
		
		meta = mapItem.getItemMeta();
		meta.setDisplayName("§f[ §c레이싱 시작 §f]");
		mapItem.setItemMeta(meta);
		
		meta = mapNavi.getItemMeta();
		meta.setDisplayName("§8[ §c다음 체크포인트 위치 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7다음 체크포인트의 위치를");
		loreList.add("§7가르킵니다.");
		loreList.add("");
		meta.setLore(loreList);
		mapNavi.setItemMeta(meta);
		
		meta = mapReturnPoint.getItemMeta();
		meta.setDisplayName("§8[ §c체크 포인트로 이동 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7마지막 체크포인트로 이동합니다.");
		loreList.add("");
		meta.setLore(loreList);
		mapReturnPoint.setItemMeta(meta);
		
		meta = drink.getItemMeta();
		meta.setDisplayName("§8[ §c에너지 드링크 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7스테미나를 즉시 회복합니다.");
		loreList.add("");
		meta.setLore(loreList);
		drink.setItemMeta(meta);
		
		event = new EventHandlerPRC(server, this);
		// 이 플러그인에 이벤트 적용
		server.getServer().getPluginManager().registerEvents(event, server);
		
		mainSch();
	}

	/////////////// Minigame에서 override 해야하는 부분
	@Override
	public void gameHelpMsg(Player p) {

	}
	
	@Override
	public void joinGame(Player p) {
		if(server.specList.containsKey(p.getName())) {
			server.spawn(p);
		}
		if(!doneSetting) {
			p.sendMessage(server.ms_alert+"해당 미니게임은 아직 관리자 기본 설정이 완료되지 않았습니다.");
		} else if(joinBlock) {
			p.sendMessage(server.ms_alert+"해당 미니게임은 관리자에 의해 현재 입장 불가 상태입니다.");
		} else if (ingamePlayer.contains(p.getName())) {
				p.sendMessage(server.ms_alert + "이미 이 게임에 참가중이십니다.");
		} else if (!server.noGameName.contains(server.playerList.get(p.getName()))) { //플레이중인 행동이 게임아님 목록에 없을경우
			p.sendMessage(server.ms_alert + "이미 다른 미니게임에 참가중 입니다.");
		} else if (ingamePlayer.size() >= maxPlayer) {
			p.sendMessage(server.ms_alert + "이미 최대인원인 "+maxPlayer +"명의 플레이어가 참가중입니다.");
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
			sendMessage(server.ms_alert + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " 님이 참여했습니다. " + ChatColor.RESET
					+ "[ " + ChatColor.GREEN + ingamePlayer.size() + ChatColor.GRAY +" 명 플레이중"+ChatColor.RESET
					+ " ]");
			TitleAPI.sendTitle(p, 10, 70, 30,ChatColor.RED+""+disPlayGameName);
			sendSound(Sound.BLOCK_NOTE_PLING, 2.0f, 1.0f);
			
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 72000, 1));
			p.setGameMode(GameMode.SURVIVAL);
			server.egParkour.join(p.getName());
			p.getInventory().addItem(mapItem);
			Sidebar sidebar = new Sidebar("§f[ §6게임 현황 §f]", server, 600, tmpLine);
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
							line = new SidebarString("§e현재 체크포인트");
							textList.add(line);
							line = new SidebarString("§a"+checkPointMap.get(p.getName())+" §e번");
							textList.add(line);
							textList.add(blank);
							line = new SidebarString("§e시간 §f: §a"+(int)(getTimeRecord(p.getName()))+"초");
							textList.add(line);
							textList.add(blank);						
						} else {
							SidebarString line = new SidebarString("");
							textList.add(line);
							line = new SidebarString("§b파쿠르 레이싱");
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
		//스케쥴
		schList.clear();
	}

	public boolean loadGameData() {
		boolean ret = true;
		loc_Join = loadLocation(gameName, "JoinPos");
		loc_spectate = loc_Join;
	
		if (loc_Join == null) {
			server.egPM.printLog(ms + "[" + disPlayGameName + "] 게임 시작 대기 지점이 설정되지 않았습니다.");
			ret = false;
		}
		File file = new File(server.getDataFolder().getPath() + "/" + gameName + "/Location", "location.yml");	
		FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
		List<String> mapNameList = fileConfig.getStringList("mapList");
		for(String mapName : mapNameList) {
			ConfigurationSection section = fileConfig.getConfigurationSection(mapName);
			int maxPoint = section.getInt("maxPoint");
			server.egPM.printLog("[" + disPlayGameName + "] "+mapName+" 에서"+maxPoint+"까지 로딩중");
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
			
			server.egPM.printLog("[" + disPlayGameName + "] "+mapName+" 에서 "+checkPointList.size()+"개의 체크포인트 로드됨");
		}
		if(ret == true) {
			server.egPM.printLog("[" + disPlayGameName + "] 설정 이상 없음");
			doneSetting = ret;
		}
		
		return ret;
	}
	
	public void onCommand(Player p, String[] cmd) {
		if (cmd.length <= 1) {
			p.sendMessage(ms + "[" + disPlayGameName + "] " + "/prc join - 게임 참가");
			p.sendMessage(ms + "[" + disPlayGameName + "] " + "/prc quit - 게임 퇴장");
			p.sendMessage(ms + "[" + disPlayGameName + "] " + "/prc set - 게임 설정");
		} else if (cmd[1].equalsIgnoreCase("set")) {
			if (cmd.length <= 2) {
				p.sendMessage(ms + "[" + disPlayGameName + "] " + "/prc set loc- 게임 지점 설정");
			} else if (cmd[2].equalsIgnoreCase("loc")) {
				if (cmd.length <= 3) {
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "/prc set loc join - 게임 시작 대기 지점 설정");
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "/prc set loc checkpoint <맵> <번호> - 쳌포 설정");
				} else if (cmd[3].equalsIgnoreCase("join")) {
					saveLocation(gameName, "JoinPos", p.getLocation());
					loadGameData();
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "게임 시작 대기 지점이 설정되었습니다.");
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
							p.sendMessage(ms+"체크포인트 "+mapName+" 의 "+pointNum+" / "+(maxPoint+1)+" 설정, Location - "+loc);
						} catch (Exception e) {
							server.egPM.printLog("[" + disPlayGameName + "]" + mapName+" "+pointNum + "설정에서 오류 발생");
						}
						loadGameData();
					}else {
						p.sendMessage(ms+"올바르지 않은 입력");
					}					
				}else {
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "/prc set loc join - 게임 시작 대기 지점 설정");
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "/prc set loc checkpoint <맵> <번호> - 쳌포 설정");
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
			server.playerList.put(p.getName(), "로비");
			p.getInventory().clear();
			quitRacing(p);
			sidebarMap.remove(p.getName());
			if (announce) {
				sendMessage(ms + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " 님이 퇴장하셨습니다. " + ChatColor.RESET
						+ "[ " + ChatColor.GREEN + ingamePlayer.size() + ChatColor.GRAY +" 명 플레이중"+ChatColor.RESET
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
								p.sendMessage(ms + "완주 보상으로 150골드를 받으셨습니다.");
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
			server.egCM.broadCast(server.ms_alert + "파쿠르 레이싱 게임이 강제 종료 되었습니다.");
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
			p.sendMessage(ms+"이 레이싱 맵에 대한 새로운 기록이 저장됐습니다. \n+"+ms+"§f기록 : §a§l"+record+"§f초");
			section.set(p.getName()+"_record", record);
			section.set(p.getName()+"_clearCnt", clearCnt+1);
		}else {
			if(baseRecord > record) {
				p.sendMessage(ms+"기존 기록인 §b"+baseRecord+" §f초 보다 더 짧은 기록을 달성하였습니다.");
				p.sendMessage(ms+"새로운 기록인 §b"+record+" §f초 를 저장합니다.");
				section.set(p.getName()+"_record", record);
				section.set(p.getName()+"_clearCnt", clearCnt+1);
			}else {
				p.sendMessage(ms+"기존 기록인 §b"+baseRecord+" §f초가 더 짧아 새로운 기록을 저장하지 않습니다.");
				section.set(p.getName()+"_clearCnt", clearCnt+1);
			}
		}
		p.sendMessage(ms+"이 맵을 §b"+(clearCnt+1)+" §f번 완주하였습니다.");
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
			p.sendMessage(ms+"기존 레이싱을 취소했습니다. \n+"+ms+"도달한 체크포인트 : "+checkPointMap.get(p.getName())+", 걸린 시간  : "+getTimeRecord(p.getName()));
			playingMapList.remove(p.getName());
			checkPointMap.remove(p.getName());
			noMoveList.remove(p.getName());
			startTimeMap.remove(p.getName());		
		}
	}
	
	public String getDisplayMapName(String mapName) {
		String displayMapName = "";
		if(mapName.equalsIgnoreCase("highCity")) {
			displayMapName =  "번화가";
		}else if(mapName.equalsIgnoreCase("construction")) {
			displayMapName = "건설 현장";
		}else if(mapName.equalsIgnoreCase("hotel")) {
			displayMapName = "호텔";
		}else if(mapName.equalsIgnoreCase("space")) {
			displayMapName = "우주 도시";
		}else if(mapName.equalsIgnoreCase("eiffeltop")) {
			displayMapName = "에펠탑";
		}
		return displayMapName;
	}
	
	public void showRanking(Player p, String mapName) {
		List<String> rankNames = mapRankerName.get(mapName);
		List<Double> rankTimes = mapRankerTime.get(mapName);
		
		if(rankNames.size() >= 1) {
			p.sendMessage("§c=========== "+getDisplayMapName(mapName)+" ===========");
			p.sendMessage("§c=========== 랭킹 ===========");
			for(int i = 0; i < rankNames.size(); i++) {
				String pName = rankNames.get(i);
				p.sendMessage("§e"+(i+1)+". "+pName+" §7: §a"+rankTimes.get(i)+"§e초");
			}
			p.sendMessage("§c=========== 당신의 최고 기록 ===========");
			File file = new File(server.getDataFolder().getPath() + "/" + gameName + "/Location", "location.yml");	
			FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
			ConfigurationSection mapSection = fileConfig.getConfigurationSection(mapName);
			ConfigurationSection section = mapSection.getConfigurationSection("record");
			if(section == null) section = mapSection.createSection("record");
			double baseRecord = section.getDouble(p.getName()+"_record");
			int clearCnt = section.getInt(p.getName()+"_clearCnt");
			if(baseRecord == 0) {
				p.sendMessage("§e아직 이 맵에 대한 기록을 가지고 있지 않습니다.");
			} else {
				p.sendMessage("§e완주 시간 : §a"+baseRecord+" §e초");
				p.sendMessage("§e완주 횟수 : §a"+clearCnt+ " §e번");
			}
		}else {
			p.sendMessage(ms+"해당 맵에 대한 레이싱 기록이 아직 없습니다.");
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
				TitleAPI.sendFullTitle(p, 10, 70, 30,ChatColor.RED+"§l5","§8§l레이싱 시작까지");
			}
		}, 20l);
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
				TitleAPI.sendFullTitle(p, 10, 70, 30,ChatColor.RED+"§l4","§8§l레이싱 시작까지");
			}
		}, 40l);
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
				TitleAPI.sendFullTitle(p, 10, 70, 30,ChatColor.RED+"§l3","§8§l레이싱 시작까지");
			}
		}, 60l);
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
				TitleAPI.sendFullTitle(p, 10, 70, 30,ChatColor.RED+"§l2","§8§l레이싱 시작까지");
			}
		}, 80l);
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
				TitleAPI.sendFullTitle(p, 10, 70, 30,ChatColor.RED+"§l1","§8§l레이싱 시작까지");
			}
		}, 100l);
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				if(ingamePlayer.contains(p.getName())) {
					MyUtility.allClear(p);
					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BELL, 1.0f, 1.0f);
					TitleAPI.sendFullTitle(p, 10, 70, 30,ChatColor.RED+"§l시작", "§8§l"+getDisplayMapName(mapName));
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
						p.sendMessage(ms+"이 맵은 에너지 드링크가 지급되는 맵입니다!");
					}else if(mapName.equalsIgnoreCase("hotel")) {
						p.getInventory().addItem(drink);
						p.sendMessage(ms+"이 맵은 에너지 드링크가 지급되는 맵입니다!");
					}
				}			
			}
		}, 120l);
	}

	//////////////// 이벤트
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
						p.sendMessage(ms+"체크포인트 §a"+nextCheckPoint+"§f번 도달, 현재 §b"+getTimeRecord(p.getName())+" §f초");
						if(checkPointList.size() == nextCheckPoint+1) {
							p.sendMessage(ms+"완주 성공!");	
							checkPointMap.put(p.getName(), 0);
							double record = getTimeRecord(p.getName());
							TitleAPI.sendFullTitle(p, 30, 100, 30, "§c§l완주 성공", "§6§l기록 §a§l"+record+" §6§l초");
							p.sendMessage(ms+"기록 - §a§l"+record+"§f초");
							setWinner(p, playingMapList.get(p.getName()));
							saveNewRecord(p, playingMapList.get(p.getName()), record);
						}else {
							TitleAPI.sendFullTitle(p, 20, 60, 20, "§c§l"+nextCheckPoint, "§6§l체크포인트 도달");
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
			if(ingamePlayer.contains(p.getName())) { //게임 참여해야지만 가능한 명령어
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
					if (cause.equals(DamageCause.VOID)) { //대기실 허공 뎀없음, 텔포	
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
			if (e.getInventory().getTitle().equalsIgnoreCase("§0§l도우미")) {				
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}	
				//gameHelper(p, e.getSlot());
			} else if (e.getInventory().getTitle().equalsIgnoreCase("§0§l맵 선택")) {
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
			if (!ingamePlayer.contains(p.getName()) //우클릭만 허용
					|| (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK))
				return;
			if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.CHEST) {
				e.setCancelled(true);			
			}
			if (e.getItem() == null || e.getItem().getType() == Material.AIR) { //아이템을 안들고 우클릭했을때 리턴
				return;
			} else if (getHeldMainItemName(p).equalsIgnoreCase("§f[ §6게임 도우미 §f]")) {
				p.openInventory(inven_gameHelper);
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 2.0f);
			} else if(e.getItem().getType() == Material.GOLD_NUGGET) {
				p.openInventory(inven_mapSelect);
			} else if(e.getItem().getType() == Material.PAPER) {
				if(playingMapList.containsKey(p.getName()) && checkPointMap.containsKey(p.getName())) {
					List<Location> chekcPointList = mapList.get(playingMapList.get(p.getName()));
					int checkPointNum = checkPointMap.get(p.getName());
					p.teleport(chekcPointList.get(checkPointNum).clone().add(0,1,0), TeleportCause.PLUGIN);
					TitleAPI.sendFullTitle(p, 10, 70, 30,"",ChatColor.RED+"§l"+checkPointNum+"§8§l번 체크포인트로 돌아감");
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
				TitleAPI.sendFullTitle(p, 10, 70, 30,"","§8§l에너지 드링크 사용함");
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
				String str = game.ms+p.getName()+" >> §6"+e.getMessage();
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
	
	///////////////////// 파쿠르 레이싱에 참가한 플레이어들 클래스
	private class PRCPlayer {
		
		public PRCPlayer(Player p, String job) {

		}

	}
}
