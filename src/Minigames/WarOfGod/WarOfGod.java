package Minigames.WarOfGod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.connorlinfoot.titleapi.TitleAPI;
import com.nametagedit.plugin.NametagEdit;

import EGServer.EGServer;
import EGServer.DataManger.PlayerData;
import EGServer.DataManger.MinigameData.MinigameData;
import EGServer.DataManger.MinigameData.WogData;
import EGServer.ServerManager.EGEventHandler;
import EGServer.ServerManager.EGScheduler;
import Minigames.Minigame;
import Minigames.WarOfGod.Abilities.Ability;
import Minigames.WarOfGod.Abilities.Agnes;
import Minigames.WarOfGod.Abilities.Apolon;
import Minigames.WarOfGod.Abilities.Ares;
import Minigames.WarOfGod.Abilities.Artemis;
import Minigames.WarOfGod.Abilities.Asclypius;
import Minigames.WarOfGod.Abilities.Atene;
import Minigames.WarOfGod.Abilities.Cekmet;
import Minigames.WarOfGod.Abilities.Demeter;
import Minigames.WarOfGod.Abilities.Dionisoce;
import Minigames.WarOfGod.Abilities.Gaia;
import Minigames.WarOfGod.Abilities.Hades;
import Minigames.WarOfGod.Abilities.Hepaistos;
import Minigames.WarOfGod.Abilities.Hermes;
import Minigames.WarOfGod.Abilities.Nemesis;
import Minigames.WarOfGod.Abilities.Nicks;
import Minigames.WarOfGod.Abilities.Nike;
import Minigames.WarOfGod.Abilities.Odin;
import Minigames.WarOfGod.Abilities.Oneiroi;
import Minigames.WarOfGod.Abilities.Poseidon;
import Minigames.WarOfGod.Abilities.Preir;
import Minigames.WarOfGod.Abilities.Pulutos;
import Minigames.WarOfGod.Abilities.Skadi;
import Minigames.WarOfGod.Abilities.Tote;
import Minigames.WarOfGod.Abilities.Uros;
import Minigames.WarOfGod.Abilities.Zeus;
import Utility.MyUtility;
import Utility.RepairMap;
import me.confuser.barapi.BarAPI;

public class WarOfGod extends Minigame{

	// 이벤트용
	public EventHandlerWOG event;

	// 로케이션
	public Location loc_tempSpawn;
	public Location loc_midChest;
	
	///////////// private
	// 게임 플레이어 리스트
	public HashMap<String, WOGPlayer> playerMap = new HashMap<String, WOGPlayer>(12);
	public HashMap<String, WOGPlayer> reConnectMap;
	public HashMap<String, String> teamMap = new HashMap<String, String>(); //스폰 버그가 있는듯... 스폰시에는 이 맵을 사용할 것
	private List<String> teamChatList = new ArrayList<String>(12);

	//////// 게임 관련
	public String ms;
	public String allMS = "§7[ §6전체 §7] ";
	public int gameStep = 0; //0 시작대기  1 팀설정 2 능력설정 3게임진행 4 게임종료 5게임 마무리중
	private String locPath;
	public Material coreBlock = Material.DIAMOND_BLOCK;
	private int baseDis = 20;
	
	/////// 각종 아이템
	public ItemStack item_wogHelper;
	
	////// 각종 인벤토리
	public Inventory inven_gameHelper;
	public Inventory inven_gameHelper2;
	public Inventory inven_abilityList;
	public Inventory inven_wogHelper;
	
	public Inventory inven_midChest;
	
	//팀
	public Team blueTeam;
	public Team redTeam;
	
	public Team winTeam;
	
	//맵
	public RepairMap map;
	
	///스케쥴
	public int leftTime = rankGame ? 2400 : 1800;
	public int goBaseTime = leftTime - 300;
	public int mainTime = -1;
	public int mainSchId = -1;
	
	public boolean baseAccess = false;
	
	public EGScheduler updateSch = new EGScheduler(this);
	
	//능력
	public List<String> abilityList = new ArrayList<String>(20);
	public HashMap<String, String> abTmpMap = new HashMap<String, String>(ingamePlayer.size());
	
	public WarOfGod(EGServer server, String gameName, String disPlayGameName, String cmdMain, boolean isRank) {
		super(server);
		rankGame = isRank;
		ShapedRecipe recipe = new ShapedRecipe(new ItemStack(Material.BLAZE_ROD));
		recipe.shape(new String[]{"|","|","|"}).setIngredient('|', Material.STICK);
		server.getServer().addRecipe(recipe);
		
		ItemStack item;
		ItemMeta meta;
		List<String> loreList;
		//mainSch = new EGScheduler();
		//////////////////// 필수 설정값
	
		ms = "§7[ §e! §7] §f: §c미니 신들의전쟁 §f>> "; // 기본 메세지
		this.gameName = gameName;
		this.disPlayGameName = disPlayGameName;
		inventoryGameName = ChatColor.stripColor(disPlayGameName);
		minPlayer = rankGame ? 8 : 6; //랭겜은 8명이상 시작
		maxPlayer = rankGame ? 14 : 12;
		startCountTime = rankGame ? 150 : 120;
		this.cmdMain = cmdMain;
		
		baseDis = rankGame ? 30 : 20;
		
		blueTeam = new Team("BLUE");
		redTeam = new Team("RED");
		
		blueTeam.ms = "§7[ §b블루팀 §7] ";
		redTeam.ms = "§7[ §c레드팀 §7] ";
		
		dirSetting(gameName);
		locPath = server.getDataFolder().getPath() + "/" + gameName + "/Location";
		
		map = new RepairMap(server, "wog1c");
		map.loadData(locPath);
		
		doneSetting = loadGameData(); // 게임 시작 위치 등등 로드				
		
		/////////////////////// 자동 설정(아이템등등)
		
		item_wogHelper = new ItemStack(Material.ENCHANTED_BOOK, 1);
		meta = item_wogHelper.getItemMeta();
		meta.setDisplayName("§f[ §b게임 메뉴 §f]");
		loreList = new ArrayList<String>(1);
		loreList.add("§7- 우클릭시 미니 신들의 전쟁의 게임 메뉴를 엽니다.");
		meta.setLore(loreList);
		item_wogHelper.setItemMeta(meta);
		
		/////////////////// 게임 도움말 인벤토리
		inven_wogHelper = Bukkit.createInventory(null, 9, "§0§l"+inventoryGameName+" 메뉴");

		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §e능력 확인");
		item.setItemMeta(meta);
		inven_wogHelper.setItem(1, item);
		meta.setDisplayName("§7- §a아이템 보급");
		item.setItemMeta(meta);
		inven_wogHelper.setItem(3, item);
		meta.setDisplayName("§7- §c팀원 목록");
		item.setItemMeta(meta);
		inven_wogHelper.setItem(5, item);
		meta.setDisplayName("§7- §b게임 도우미");
		item.setItemMeta(meta);
		inven_wogHelper.setItem(7, item);
		////////////////
		
		/////////////////// 게임 도우미
		inven_gameHelper = Bukkit.createInventory(null, 27, "§0§l"+inventoryGameName+" 도우미");

		item = new ItemStack(Material.STAINED_GLASS_PANE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName(" ");
		item.setItemMeta(meta);
		for (int i = 0; i <= 9; i++) {
			inven_gameHelper.setItem(i, item);
		}
		for (int i = 17; i < 27; i++) {
			inven_gameHelper.setItem(i, item);
		}
		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c게임설명");
		loreList = new ArrayList<String>();
		loreList.add("§7- 게임 진행방식, 승리조건 등을 확인합니다.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_gameHelper.setItem(11, item);
		item = new ItemStack(Material.BOOK_AND_QUILL, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c게임명령어");
		loreList = new ArrayList<String>();
		loreList.add("§7- 이 게임에서 사용하실 수 있는");
		loreList.add("§7  명령어를 확인합니다.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_gameHelper.setItem(13, item);
		item = new ItemStack(Material.BOOKSHELF, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c게임규칙");
		loreList = new ArrayList<String>();
		loreList.add("§7- 게임을 플레이하며");
		loreList.add("§7  지켜야하는 규칙을 확인합니다.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_gameHelper.setItem(15, item);
		
		/////////////////// 게임 도우미2
		inven_gameHelper2 = Bukkit.createInventory(null, 27, "§0§l"+inventoryGameName+" 게임설명");

		item = new ItemStack(Material.STAINED_GLASS_PANE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName(" ");
		item.setItemMeta(meta);
		for (int i = 0; i <= 9; i++) {
			inven_gameHelper2.setItem(i, item);
		}
		for (int i = 17; i < 27; i++) {
			inven_gameHelper2.setItem(i, item);
		}
		item = new ItemStack(Material.KNOWLEDGE_BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c진행방식");
		loreList = new ArrayList<String>();
		loreList.add("§7- 게임 진행방식과 승리조건을 확인합니다.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_gameHelper2.setItem(11, item);
		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c신 목록");
		loreList = new ArrayList<String>();
		loreList.add("§7- 신 목록을 확인합니다.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_gameHelper2.setItem(13, item);
		item = new ItemStack(Material.ENCHANTED_BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c능력 사용법");
		loreList = new ArrayList<String>();
		loreList.add("§7- 능력을 어떻게 사용하는지 확인합니다.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_gameHelper2.setItem(15, item);
			
		//////////////////////////능력목록
		inven_abilityList = Bukkit.createInventory(null, 27, ""+ChatColor.BLACK+ChatColor.BOLD+"신 목록");
			
		abilityList.add("§e가이아");
		abilityList.add("§e니케");
		abilityList.add("§e데메테르");
		abilityList.add("§e디오니소스");
		abilityList.add("§e아레스");
		abilityList.add("§e아르테미스");
		abilityList.add("§e아폴론");
		abilityList.add("§e제우스");
		abilityList.add("§e포세이돈");
		abilityList.add("§e하데스");
		abilityList.add("§e토트");
		abilityList.add("§e헤파이토스");
		abilityList.add("§e아스클리피어스");
		abilityList.add("§e헤르메스");
		abilityList.add("§e아테나");
		abilityList.add("§e오딘");
		abilityList.add("§e우로스");
		abilityList.add("§e닉스");
		abilityList.add("§e세크메스");
		abilityList.add("§e오네이로이");
		abilityList.add("§e아그네스");
		abilityList.add("§e스카디");
		abilityList.add("§e프레이르");
		abilityList.add("§e네메시스");
		abilityList.add("§e플루토스");
		
		ItemStack abItem = new ItemStack(Material.PAPER, 1);
		ItemMeta abMeta = abItem.getItemMeta();
		
		for(int i = 0; i < abilityList.size(); i++) {
			abMeta.setDisplayName(abilityList.get(i));
			abItem.setItemMeta(abMeta);
			inven_abilityList.setItem(i, abItem);
		}
		
		
		///////////////////중앙 상자
		inven_midChest = Bukkit.createInventory(null, 27, ""+ChatColor.BLACK+ChatColor.BOLD+"중앙 상자");

		item = new ItemStack(Material.IRON_INGOT, 1);
		inven_midChest.setItem(0, item);
		inven_midChest.setItem(4, item);
		inven_midChest.setItem(8, item);
		inven_midChest.setItem(18, item);
		inven_midChest.setItem(22, item);
		inven_midChest.setItem(26, item);
		inven_midChest.setItem(18, item);
		inven_midChest.setItem(12, item);
		inven_midChest.setItem(14, item);
		item = new ItemStack(Material.DIAMOND, 1);
		inven_midChest.setItem(9, item);
		inven_midChest.setItem(13, item);
		inven_midChest.setItem(17, item);
		
		////////////////
		
		/*abilityList.add("이리스");
		 팀원 선택하여 위치교체
		 
		 
		 
		 */
		
		
		/////갑옷

		
		//////////////////////////
		event = new EventHandlerWOG(server, this);
		// 이 플러그인에 이벤트 적용
		server.getServer().getPluginManager().registerEvents(event, server);
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
		} else if (ingamePlayer.contains(p.getName())) {
				p.sendMessage(server.ms_alert + "이미 이 게임에 참가중이십니다.");
		} else if (!server.noGameName.contains(server.playerList.get(p.getName()))) { //플레이중인 행동이 게임아님 목록에 없을경우
			p.sendMessage(server.ms_alert + "이미 다른 미니게임에 참가중 입니다.");
		} else if (ingame) {
			if(reConnectMap != null) {
				if (reConnectMap.containsKey(p.getName())) {
					p.setFoodLevel(20);
					p.closeInventory();
					MyUtility.allClear(p);
					ingamePlayer.add(p.getName());
					WOGPlayer wogp = reConnectMap.get(p.getName());
					playerMap.put(p.getName(), wogp);
					sendMessage(server.ms_alert + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " 님이 재접속 하였습니다. ");
					server.playerList.put(p.getName(), disPlayGameName);
					server.spawnList.remove(p.getName());
					server.waitingPlayer.remove(p.getName());
					MyUtility.attackDelay(p, false);
					if (wogp.team == redTeam)
						setRedTeam(p);
					else
						setBlueTeam(p);
					spawn(p);
				}else {
					MyUtility.allClear(p);
					p.sendMessage(server.ms_alert + "이미 게임이 시작되었습니다. 관전장소로 이동되며 관전 전용 채팅을 사용합니다.");
					p.teleport(loc_spectate, TeleportCause.PLUGIN);
					server.specList.put(p.getName(), this);
					p.setGameMode(GameMode.SPECTATOR);
					p.setFlying(true);
				}
			}	
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
			p.getInventory().setItem(8, helpItem);
			p.getInventory().setHeldItemSlot(8);
			sendMessage(server.ms_alert + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " 님이 참여했습니다. " + ChatColor.RESET
					+ "[ " + ChatColor.GREEN + ingamePlayer.size() + " / "+ minPlayer + ChatColor.RESET + " ]");
			TitleAPI.sendFullTitle(p, 10, 70, 30, "", ChatColor.RED+""+disPlayGameName);
			sendSound(Sound.BLOCK_NOTE_PLING, 2.0f, 1.0f);
			p.teleport(loc_Join, TeleportCause.PLUGIN);
			if(lobbyStart) {
				if(startSch.schTime > 1)
					BarAPI.setMessage(p, ChatColor.GREEN+"게임 시작까지", startSch.schTime);
			} else {
				if ((ingamePlayer.size() >= minPlayer)) {
					startCount();
				} 
			}
		}
	}
	
	@Override
	public void startCount() {
		if(ingame) return;
		lobbyStart = true;
		startSch.schTime = startCountTime;
		map.Repair(startCountTime-10);
		server.egCM.broadCast(server.ms_alert + ChatColor.RED+ChatColor.BOLD + disPlayGameName + ChatColor.GRAY + " 게임이 " + ChatColor.GRAY + "곧 시작됩니다.");
		sendBossbar(ChatColor.GREEN+"게임 시작까지", startSch.schTime);
		startSch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				if (startSch.schTime > 0) {
					if (startSch.schTime == 30 || startSch.schTime == 10) {
						sendMessage(server.ms_alert + ChatColor.GRAY + "게임이 "
								+ ChatColor.AQUA + startSch.schTime + ChatColor.GRAY + "초 뒤 시작됩니다.");
						sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
					}
					startSch.schTime -= 1;
				} else {
					ingame = true;
					lobbyStart = false;
					Bukkit.getScheduler().cancelTask(startSch.schId);
					playerSet();
					startGame();
				}
			}
		}, 0L, 20L);
	}
	
	@Override
	public void startGame() {
		server.egCM.broadCast(server.ms_alert + disPlayGameName + ChatColor.GRAY + "이 시작 되었습니다!");
		initGame();
		ingame = true;
		gameStep = 1; //팀 설정중	
		baseAccess = false;
		for (String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p)){
				p.setGameMode(GameMode.SURVIVAL);
				MyUtility.setMaxHealth(p,20);
				// 풀피로 만듬
				MyUtility.healUp(p);
				MyUtility.attackDelay(p, false);
				p.setFallDistance(0);          
				TitleAPI.sendFullTitle(p, 10, 80, 30, ChatColor.RED+"게임 시작", ChatColor.RED+""+disPlayGameName);
			}
		}
		//sendMessage(ms+"게임이 시작 됐습니다.");
		if(!ingame) return;
		sendTitle(ChatColor.RED+"게임 시작",  ChatColor.BLUE+""+disPlayGameName, 80);
		sendSound(Sound.BLOCK_NOTE_CHIME, 1.0f, 0.75f);
		
		EGScheduler sch = new EGScheduler(this);
		sch.schTime = 0;
		sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				if(sch.schTime < ingamePlayer.size()) {
					String pName = ingamePlayer.get(sch.schTime);
					Player p = Bukkit.getPlayer(pName);
					if(existPlayer(p)) {
						p.teleport(loc_tempSpawn, TeleportCause.PLUGIN);
					}
					sch.schTime++;
				}else {
					sch.cancelTask(true);
				}
			}
		}, 10, 5l);
		for(Entity entity : Bukkit.getWorld("world").getEntities()) {
			if(entity instanceof Item || entity instanceof Arrow) {
				entity.remove();
			}
		}
		//// 팀설정 ////		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				sendMessage(ms + "팀 설정중입니다.");
				sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
				setTeam();
				setMidChestItem();
				gameStep = 2;
			}
		}, 80l);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				setAbility();
				sendMessage(ms + "능력을 설정중입니다.");
				sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
				for(String pName : ingamePlayer){
					Player p = Bukkit.getPlayer(pName);
					if(p == null || !p.isOnline()) continue;
				}
			}
		}, 200l);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				sendSound(Sound.BLOCK_NOTE_PLING, 1.0f, 2.0f);
				for(String pName : ingamePlayer){
					Player p = Bukkit.getPlayer(pName);
					if(p == null || !p.isOnline()) continue;
					WOGPlayer wog = playerMap.get(pName);
					if(wog != null) {
						p.teleport(wog.team.loc_spawn, TeleportCause.PLUGIN);
					} else {
						p.sendMessage(server.ms_alert+"버그 발생 (코드1) 개발자에게 문의해주세요.");
						spawn(p);
					}
					p.setGameMode(GameMode.SPECTATOR);
					TitleAPI.sendFullTitle(p, 10, 80, 30, ChatColor.RED+"5", ChatColor.RESET+"게임 시작까지");
				}
			}
		}, 560l);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				sendSound(Sound.BLOCK_NOTE_PLING, 1.0f, 2.0f);
				for(String pName : ingamePlayer){
					Player p = Bukkit.getPlayer(pName);
					if(!existPlayer(p)) continue;
					TitleAPI.sendFullTitle(p, 10, 80, 30, ChatColor.RED+"4", ChatColor.RESET+"게임 시작까지");
				}
				reConnectMap = new HashMap<String, WOGPlayer>(playerMap);
			}
		}, 580l);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				sendSound(Sound.BLOCK_NOTE_PLING, 1.0f, 2.0f);
				for(String pName : ingamePlayer){
					Player p = Bukkit.getPlayer(pName);
					if(!existPlayer(p)) continue;
					TitleAPI.sendFullTitle(p, 10, 80, 30, ChatColor.RED+"3", ChatColor.RESET+"게임 시작까지");
				}
				blueTeam.core.getBlock().setType(Material.DIAMOND_BLOCK);
				redTeam.core.getBlock().setType(Material.DIAMOND_BLOCK);
			}
		}, 600l);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				sendSound(Sound.BLOCK_NOTE_PLING, 1.0f, 2.0f);
				blueTeam.updateTeamList();
				redTeam.updateTeamList();
				for(String pName : ingamePlayer){
					Player p = Bukkit.getPlayer(pName);
					if(!existPlayer(p)) continue;
					TitleAPI.sendFullTitle(p, 10, 80, 30, ChatColor.RED+"2", ChatColor.RESET+"게임 시작까지");
				}
			}
		}, 620l);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				sendSound(Sound.BLOCK_NOTE_PLING, 1.0f, 2.0f);
				for(String pName : ingamePlayer){
					Player p = Bukkit.getPlayer(pName);
					if(!existPlayer(p)) continue;
					TitleAPI.sendFullTitle(p, 10, 80, 30, ChatColor.RED+"1", ChatColor.RESET+"게임 시작까지");
				}
			}
		}, 640l);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				leftTime = rankGame ? 2400 : 1800;
				for(String pName : ingamePlayer) {
					Player p = Bukkit.getPlayer(pName);
					if(existPlayer(p)) {
						try {
							playerMap.get(pName).ability.itemSupply(p);
							p.getInventory().setItem(8, item_wogHelper);
							p.setGameMode(GameMode.SURVIVAL);
						}catch(Exception e) {
							
						}				
					}
				}
				sendMessage(ms + "전투를 시작합니다! 코블스톤을 모아 능력을 사용하여\n"
						+ms+"적팀의 코어를 파괴하십시오!");
				sendSound(Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.5f);
				sendTitle(ChatColor.RED+"게임 시작", ChatColor.RESET+"적팀의 코어를 파괴하세요!", 60);
				sendBossbar(ChatColor.GRAY+"무승부까지", 1800);
				gameStep = 3;
				mainScheduler();
			}
		}, 660l);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				sendTitle(ChatColor.RED+"§l능력 확인방법", ChatColor.RESET+"§l책 우클릭 -> 능력확인", 100);
			}
		}, 760l);
	}
	
	@Override
	public void gameQuitPlayer(Player p, boolean annouce, boolean isDead) {
		if (ingamePlayer.contains(p.getName())) {
			ingamePlayer.remove(p.getName());
			blueTeam.teamList.remove(p.getName());
			redTeam.teamList.remove(p.getName());
			server.playerList.put(p.getName(), "로비");
			if (gameStep == 2) {
				if(blueTeam.teamList.size() <= 0) {									
				
					redTeamWin("적팀이 모두 탈주하였습니다.");
								
				} else if(redTeam.teamList.size() <= 0) {
					
					blueTeamWin("적팀이 모두 탈주하였습니다.");
					
				}
			} else {
				if(annouce) {
					sendMessage(ms + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " 님이 퇴장 하셨습니다. "
							+ ChatColor.RESET + "[ " + ChatColor.GREEN + ingamePlayer.size() + " / "+ minPlayer + ChatColor.RESET + " ]");
					sendSound(Sound.BLOCK_CLOTH_BREAK,1.5f, 1.5f);
				}
				if(ingamePlayer.size() <= 0) {
					if(lobbyStart) {
						lobbyStart = false;
						Bukkit.getScheduler().cancelTask(startSch.schId);
					}				
				}
			}
		}
	}
	
	//////////////////

	public void initGame() {
		for(WOGPlayer wogP : playerMap.values()) {
			try {
				wogP.ability.sidebar.getTheScoreboard().getEntryTeam("customTeam").unregister();
				wogP = null;
			}catch(Exception e) {
				
			}
		}
		playerMap.clear();
		if(reConnectMap != null)
		reConnectMap.clear();
		teamChatList.clear();
		gameStep = 0;
		blueTeam.killCnt = 0;
		blueTeam.inven_TeamChest.clear();
		blueTeam.inven_TeamList.clear();
		blueTeam.teamList.clear();
		redTeam.killCnt = 0;
		redTeam.inven_TeamChest.clear();
		redTeam.inven_TeamList.clear();
		redTeam.teamList.clear();
		leftTime = rankGame ? 2400 : 1800;
		blueTeam.teamList.clear();	
		redTeam.teamList.clear();
		teamMap.clear();
		map.repaired = false;
		ending = false;
		winTeam = null;
		//blueTeam = new Team("BLUE");
		//redTeam = new Team("RED");
		
		clearClickMap();
	}

	public boolean loadGameData() {
		boolean ret = true;
		loc_Join = loadLocation(gameName, "JoinPos");
		blueTeam.loc_spawn = loadLocation(gameName, "blueSpawn");
		redTeam.loc_spawn = loadLocation(gameName, "redSpawn");
		blueTeam.core = loadLocation(gameName, "blueCore");
		redTeam.core = loadLocation(gameName, "redCore");
		blueTeam.loc_chest = loadLocation(gameName, "blueChest");
		redTeam.loc_chest = loadLocation(gameName, "redChest");
		loc_midChest = loadLocation(gameName, "midChest");
		loc_tempSpawn = loadLocation(gameName, "tmpSpawn");
		loc_Join = loc_tempSpawn;
		if (loc_Join == null) {
			server.egPM.printLog("[" + disPlayGameName + "] 게임 시작 대기 지점이 설정되지 않았습니다.");
			ret = false;
		}
		if (blueTeam.loc_spawn == null) {
			server.egPM.printLog("[" + disPlayGameName + "] 블루팀 스폰지점이 설정되지 않았습니다.");
			ret = false;
		} 
		if (redTeam.loc_spawn == null) {
			server.egPM.printLog("[" + disPlayGameName + "] 레드팀 스폰지점이 설정되지 않았습니다.");
			ret = false;
		}
		if (blueTeam.core == null) {
			server.egPM.printLog("[" + disPlayGameName + "] 블루팀 코어지점이 설정되지 않았습니다.");
			ret = false;
		}
		if (redTeam.core == null) {
			server.egPM.printLog("[" + disPlayGameName + "] 레드팀 코어지점이 설정되지 않았습니다.");
			ret = false;
		}
		if (blueTeam.loc_chest == null) {
			server.egPM.printLog("[" + disPlayGameName + "] 블루팀 공용상자지점이 설정되지 않았습니다.");
			ret = false;
		}
		if (redTeam.loc_chest == null) {
			server.egPM.printLog("[" + disPlayGameName + "] 레드팀 공용상자지점이 설정되지 않았습니다.");
			ret = false;
		}
		if (loc_midChest == null) {
			server.egPM.printLog("[" + disPlayGameName + "] 중앙상자지점이 설정되지 않았습니다.");
			ret = false;
		}else {
			loc_spectate = loc_midChest.clone().add(0,2,0);
		}
		if (loc_tempSpawn == null) {
			server.egPM.printLog("[" + disPlayGameName + "] 임시 스폰 지점이 설정되지 않았습니다.");
			ret = false;
		}
		if (!map.loadData(locPath)) {
			server.egPM.printLog("[" + disPlayGameName + "] 맵이 설정되지 않았습니다.");
			ret = false;
		}
		
		if(ret == true) {
			server.egPM.printLog("[" + disPlayGameName + "] 설정 이상 없음");
			doneSetting = ret;
			
		}
		
		return ret;
	}
	
	/////////// 인벤토리 클릭 ////////////////
	
	public void gameHelper(Player p, int slot) {
		switch (slot) {
		case 11:
			p.openInventory(inven_gameHelper2);
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
			return;
			
		case 13:
			p.sendMessage("§f/팀챗 ( 또는 tc ) - §c팀 채팅으로 전환합니다.");
			p.closeInventory();
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
			return;
			
		case 15:
			p.sendMessage("\n§f아군의 코어주위 2칸까지 블럭설치가 되지않습니다.");
			p.sendMessage("§c고의적 §f트롤(아이템을 가지고 일부러 자살)은 신고대상입니다.");
			p.sendMessage("§f용암은 기지에서 20칸 밖으로는 가져가지지 않습니다."); //밖에서 용암주웠을떄도 체크해야함
			p.sendMessage("§f용암 및 능력을 이용한 §c고의적 §f팀킬은 신고대상입니다.");
			p.sendMessage("§f다이아몬드 블럭은 돌, 철, 금, 다이아 곡괭이로는 캘 수 없습니다.");		
			p.sendMessage("§f게임이 시작된 뒤 5분까지는 적팀의 기지(스폰기준10칸)에 접근이 불가능합니다.");	
			p.closeInventory();
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
			return;

		default:
			return;
		}
	}	
	
	public void gameHelper2(Player p, int slot) {
		switch (slot) {
		case 11:
			p.sendMessage("\n§7게임 이름 §f: §c미니 신들의 전쟁");
			p.sendMessage("§f스카이블럭에서 신의 능력을 사용하여");
			p.sendMessage("§f상대의 코어(다이아블럭)를 파괴해야하는 팀 협동 게임입니다.");
			p.sendMessage("§f게임은 블루팀, 레드팀 두 진영 나뉘어 진행되며");
			p.sendMessage("§f상대 진영에 있는 코어를 파괴하면 승리합니다.");
			p.sendMessage("§f각 플레이어는 고유한 능력을 가지고있으며");
			p.sendMessage("§f대부분의 능력을 코블스톤을 필요로합니다.");
			p.sendMessage("§e사망하여도 자신의 팀 기지에서 부활합니다.");
			p.sendMessage("§e해당 게임은 배고픔이 줄어듭니다.");
			p.sendMessage("§e옵시디언 블럭을 빈 양동이로 우클릭시 용암양동이로 전환됩니다.");
			p.sendMessage("§e해당 게임은 공격딜레이를 사용하지 않습니다. 이에 따라 모든 도끼의");
			p.sendMessage("§e피해량은 3만큼 감소되어 적용됩니다.");
			p.closeInventory();
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
			return;
			
		case 13:
			p.openInventory(inven_abilityList);
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
			return;
			
		case 15:
			p.sendMessage("\n§f능력은 패시브와 액티브로 나뉘어집니다.");
			p.sendMessage("§f패시브는 상시 발동이기에 특정한 무언가를 할 필요가 없습니다.");
			p.sendMessage("§f반대로 액티브는 §c블레이즈 막대§f를 좌클릭 또는 우클릭시 발동됩니다."); //밖에서 용암주웠을떄도 체크해야함
			p.sendMessage("§f블레이즈 막대는 아래와 같이 조합하면 제작할 수 있습니다.");
			p.sendMessage("§e□ 나무막대 □");
			p.sendMessage("§e□ 나무막대 □");
			p.sendMessage("§e□ 나무막대 □");
			p.closeInventory();
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
			return;

		default:
			return;
		}
	}
	
	public void wogHelper(Player p, int slot) {
		switch (slot) {
		case 1:
			if(playerMap.containsKey(p.getName())) {
				p.sendMessage(ms+"당신의 능력");
				playerMap.get(p.getName()).ability.helpMsg(p);
			} else {
				p.sendMessage(ms+"능력이 없습니다.");
			}
			p.closeInventory();
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
			return;
			
		case 3:
			if(playerMap.containsKey(p.getName())) {
				playerMap.get(p.getName()).ability.itemSupply(p);
			} else {
				p.sendMessage(ms+"능력이 없습니다.");
			}
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
			return;
			
		case 5:
			p.closeInventory();
			if(playerMap.containsKey(p.getName())) {
				p.openInventory(playerMap.get(p.getName()).team.inven_TeamList);
			} else {
				p.sendMessage(ms+"팀이 없습니다.");
			}
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
			return;
			
			
		case 7:		
			p.openInventory(inven_gameHelper);	
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
			return;

		default:
			return;
		}
	}

	/////////////////////////////////
	
	
	public void onCommand(Player p, String[] cmd) {
		if (cmd.length > 1) {
			if (cmd[1].equalsIgnoreCase("set")) {
				if (cmd.length > 2) {
					if (cmd[2].equalsIgnoreCase("loc")) {
						if (cmd.length > 3) {
							if (cmd[3].equalsIgnoreCase("join")) {
								saveLocation(gameName, "JoinPos", p.getLocation());
								loadGameData();
								p.sendMessage("[" + disPlayGameName + "] " + "게임 시작 대기 지점이 설정되었습니다.");
							} else if (cmd[3].equalsIgnoreCase("blueSpawn")) {
								saveLocation(gameName, "blueSpawn", p.getLocation().add(0,0,0));
								p.sendMessage("[" + disPlayGameName + "] " + "블루팀 스폰지점 설정완료");
							} else if (cmd[3].equalsIgnoreCase("redSpawn")) {
								saveLocation(gameName, "redSpawn", p.getLocation().add(0,0,0));
								p.sendMessage("[" + disPlayGameName + "] " + "레드팀 스폰지점 설정완료");
							} else if (cmd[3].equalsIgnoreCase("bluecore")) {
								saveLocation(gameName, "blueCore", p.getLocation().add(0,-1,0));
								p.sendMessage("[" + disPlayGameName + "] " + "블루팀 코어 설정완료");
							} else if (cmd[3].equalsIgnoreCase("redcore")) {
								saveLocation(gameName, "redCore", p.getLocation().add(0,-1,0));
								p.sendMessage("[" + disPlayGameName + "] " + "레드팀 코어 설정완료");
							} else if (cmd[3].equalsIgnoreCase("midChest")) {
								saveLocation(gameName, "midChest", p.getLocation().add(0,-1,0));
								p.sendMessage("[" + disPlayGameName + "] " + "가운데 상자 설정완료");
							} else if (cmd[3].equalsIgnoreCase("blueChest")) {
								saveLocation(gameName, "blueChest", p.getLocation().add(0,-1,0));
								p.sendMessage("[" + disPlayGameName + "] " + "블루팀 상자 설정완료");
							} else if (cmd[3].equalsIgnoreCase("redChest")) {
								saveLocation(gameName, "redChest", p.getLocation().add(0,-1,0));
								p.sendMessage("[" + disPlayGameName + "] " + "레드팀 상자 설정완료");
							}  else if (cmd[3].equalsIgnoreCase("tmpspawn")) {
								saveLocation(gameName, "tmpSpawn", p.getLocation().add(0,0,0));
								p.sendMessage("[" + disPlayGameName + "] " + "임시스폰 설정완료");
							}  else if (cmd[3].equalsIgnoreCase("map1")) {
								map.setPos1(p.getTargetBlock(null, 3).getLocation());
								p.sendMessage("[" + disPlayGameName + "] " + p.getTargetBlock(null, 3).getLocation()+" 설정완료");
								map.saveMapBlocks(locPath);
							}  else if (cmd[3].equalsIgnoreCase("map2")) {
								map.setPos2(p.getTargetBlock(null, 3).getLocation());
								p.sendMessage("[" + disPlayGameName + "] " + p.getTargetBlock(null, 3).getLocation()+" 설정완료");
								map.saveMapBlocks(locPath);
							} else {
								p.sendMessage("[" + disPlayGameName + "] " + cmd[3]+"은 올바르지 않은 인수");
							}
							loadGameData();
						} else {
							p.sendMessage(ms+"인수를 입력해주세요.");
						}
					} else {
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" set loc join - 게임 시작 대기 지점 설정");
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" set loc bluespawn - 블루팀 스폰");
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" set loc redspawn - 레드팀 스폰");
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" set loc bluecore - 레드팀 코어");
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" set loc redcore - 레드팀 코어");
					}
				} else {
					p.sendMessage("[" + disPlayGameName + "] " + "/wog set loc- 게임 지점 설정");
				}
			} else if (cmd[1].equalsIgnoreCase("join")) {
				if(cmd.length > 2) joinGame(Bukkit.getPlayer(cmd[2]));
				else joinGame(p);
			} else if (cmd[1].equalsIgnoreCase("forceend")) {
				endGame(true);
			} else if (cmd[1].equalsIgnoreCase("quit")) {
				gameQuitPlayer(p, true, false);
			} else if (cmd[1].equalsIgnoreCase("block")) {
				if(joinBlock) joinBlock = false;
				else joinBlock = true;
			} else if (cmd[1].equalsIgnoreCase("start")) {
				startCount();
			} else if (cmd[1].equalsIgnoreCase("debug1")) {
				ingamePlayer.add(p.getName());
				setAbility();
			}else if(cmd[1].equalsIgnoreCase("debug2")) {
				map.saveMapBlocks(locPath);
			}else if(cmd[1].equalsIgnoreCase("debug3")) {
				setAb(p, Integer.valueOf(cmd[2]));
			}else if(cmd[1].equalsIgnoreCase("debug4")) {
				map.repaired = false;
				p.sendMessage(""+Integer.valueOf(cmd[2]));
				map.Repair(Integer.valueOf(cmd[2]));
			}else if(cmd[1].equalsIgnoreCase("debug5")) {
				coreBreakEffect(p.getLocation());
			}else if(cmd[1].equalsIgnoreCase("debug6")) {
				removeSidebar(p);
			}else if(cmd[1].equalsIgnoreCase("debug7")) {
				p.sendMessage(baseAccess+"");
			}else if(cmd[1].equalsIgnoreCase("debug8")) {
				Bukkit.broadcastMessage(redTeam.loc_spawn.distance(p.getLocation())+" ||| "+redTeam.loc_spawn);
				if(redTeam.loc_spawn.distance(p.getLocation()) < 15) {
					p.sendMessage("yes");
				}else {
					p.sendMessage("no");
				}
			}else if(cmd[1].equalsIgnoreCase("debug9")) {
				ItemStack pHead = new ItemStack(Material.SKULL_ITEM, 1,(byte)3);
				ItemMeta meta = pHead.getItemMeta();
				meta.setDisplayName(p.getName());
				List<String> loreList = new ArrayList<String>(1);
				loreList.add("");
				//loreList.add("§7"+playerMap.get(p.getName()).ability.abilityName);
				loreList.add("");
				meta.setLore(loreList);
				pHead.setItemMeta(meta);
				p.getInventory().addItem(pHead);
				p.sendMessage("did");
			}
		} else {
			p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" join - 게임 참가");
			p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" quit - 게임 퇴장");
			p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" set - 게임 설정");
		}
	}
	
	public List<String> getEnemyList(String team) {
		if(team.equalsIgnoreCase("BLUE")) {
			return redTeam.teamList;
		} else if(team.equalsIgnoreCase("RED")) {
			return blueTeam.teamList;
		}
		return ingamePlayer;
	}
	
	public void mainScheduler() {
		mainSchId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				if(!ingame || gameStep != 3) Bukkit.getScheduler().cancelTask(mainSchId);
				
				if(mainTime > 0) {
					mainTime = 0;
					List<String> keySet = new ArrayList<String>(playerMap.keySet());
					
					if(--leftTime >= 0) {
						for(String pName : ingamePlayer) {
							Player p = Bukkit.getPlayer(pName);
							if(existPlayer(p)) {
								playerMap.get(pName).ability.setUI();
							}
						}
						if(leftTime == goBaseTime && !baseAccess) {
							baseAccess = true;
							sendMessage(ms+"이제 적팀의 기지에 접근이 가능합니다.");
							sendSound(Sound.BLOCK_NOTE_CHIME);
						}
						if(leftTime == 60) {
							sendTitle("", "§c무승부까지 60초", 60);
						}
						if(leftTime < 60) {
							sendSound(Sound.BLOCK_NOTE_HAT);
						}
					} else {
						if(gameStep == 3) {
							gameDraw();
						}
					}
				} else {
					mainTime++;
					if(leftTime < 30) {
						sendSound(Sound.BLOCK_NOTE_HAT);
					}
				}

			}
		}, 0l, 10l);
	}

	public void setTeam() {
		List<String> tmpList = new ArrayList<String>(ingamePlayer);
		MyUtility.mixList(tmpList);
		for(int i = 0; i < tmpList.size(); i++){
			Player p = Bukkit.getPlayer(ingamePlayer.get(i));
			if(existPlayer(p)) {
				MyUtility.allClear(p);
				if(blueTeam.teamList.size() <= redTeam.teamList.size()){
					setBlueTeam(p);
				} else {
					setRedTeam(p);
				}
			}
		}
	}
	
	public void setMidChestItem() {
		if(loc_midChest.getBlock().getType() == Material.CHEST) {
			Chest chest = (Chest) loc_midChest.getBlock().getState();
			chest.update();
			chest.getBlockInventory().setContents(inven_midChest.getContents());
		} 
	}
	
	public void setAbility() {
		List<Integer> abCodes = new ArrayList<Integer>();
		for(int i = 0; i <= 24; i++) {
			abCodes.add(i);
		}
		
		MyUtility.mixList(abCodes);
		for(int i = 0; i < ingamePlayer.size(); i++) {
			Player p = Bukkit.getPlayer(ingamePlayer.get(i));
			setAb(p, abCodes.get(i));
			
			
			if(p != null && p.isOnline()) {
				EGScheduler sch = new EGScheduler(this);
				sch.schTime = 1;
				sch.schTime2 = 0;
				String abName = playerMap.get(p.getName()).ability.abilityName;
				abTmpMap.put(p.getName(), abilityList.get(MyUtility.getRandom(0, abilityList.size()-1)));
				sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
					public void run() {
						if(!ingamePlayer.contains(p.getName())) Bukkit.getScheduler().cancelTask(sch.schId);
						if(sch.schTime2 < 40) {
							sch.schTime += 5;
							if(sch.schTime > sch.schTime2){
								sch.schTime = 0;
								sch.schTime2 += 1;
								String tmpS = getTempString(p.getName());
								TitleAPI.sendFullTitle(p, 0, 60, 0, abTmpMap.get(p.getName()), tmpS);
								abTmpMap.put(p.getName(), tmpS);
								p.playSound(p.getLocation(), Sound.BLOCK_NOTE_HARP, 1.0F, 2.5F);
							}							
						} else {
							sch.cancelTask(true);
							Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
								public void run() {
									TitleAPI.sendFullTitle(p, 0, 60, 0, abTmpMap.get(p.getName()), ChatColor.YELLOW+abName);
									p.playSound(p.getLocation(), Sound.BLOCK_NOTE_HARP, 1.0F, 2.5F);
								}
							}, 12l);
							Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
								public void run() {
									TitleAPI.sendFullTitle(p, 0, 60, 0, ChatColor.YELLOW+abName, getTempString(p.getName()));
									p.playSound(p.getLocation(), Sound.BLOCK_NOTE_HARP, 1.0F, 2.5F);
								}
							}, 25l);
							Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
								public void run() {
									TitleAPI.sendFullTitle(p, 0, 60, 0, ChatColor.RED+abName, "능력이 설정되었습니다.");
									p.playSound(p.getLocation(), Sound.BLOCK_NOTE_XYLOPHONE, 1.0F, 1.0F);
								}
							}, 70l);
						}
					}
				}, 20l, 1l);
			}
		}
		
	}
	
	public String getTempString(String pName) {
		String s = null;
		if(abTmpMap.containsKey(pName)) {
			do {
				s = abilityList.get(MyUtility.getRandom(0, abilityList.size()-1));
			}while(abTmpMap.get(pName).equalsIgnoreCase(s));
		}
		return s;
	}
	
	public void setAb(Player p, int abCode) {
		Ability ab = null;
		if(abCode == 0) {
			ab = new Gaia(this, p);
		} else if(abCode == 1) {
			ab = new Nike(this, p);
		} else if(abCode == 2) {
			ab = new Demeter(this, p);
		}else if(abCode == 3) {
			ab = new Dionisoce(this, p);
		}else if(abCode == 4) {
			ab = new Ares(this, p);
		}else if(abCode == 5) {
			ab = new Artemis(this, p);
		}else if(abCode == 6) {
			ab = new Apolon(this, p);
		}else if(abCode == 7) {
			ab = new Zeus(this, p);
		}else if(abCode == 8) {
			ab = new Poseidon(this, p);
		}else if(abCode == 9) {
			ab = new Hades(this, p);
		}else if(abCode == 10) {
			ab = new Tote(this, p);
		}else if(abCode == 11) {
			ab = new Hepaistos(this, p);
		}else if(abCode == 12) {
			ab = new Asclypius(this, p);
		}else if(abCode == 13) {
			ab = new Hermes(this, p);
		}else if(abCode == 14) {
			ab = new Atene(this, p);
		}else if(abCode == 15) {
			ab = new Odin(this, p);
		}else if(abCode == 16) {
			ab = new Uros(this, p);
		}else if(abCode == 17) {
			ab = new Nicks(this, p);
		}else if(abCode == 18) {
			ab = new Cekmet(this, p);
		}else if(abCode == 19) {
			ab = new Oneiroi(this, p);
		}else if(abCode == 20) {
			ab = new Agnes(this, p);
		}else if(abCode == 21) {
			ab = new Skadi(this, p);
		}else if(abCode == 22) {
			ab = new Preir(this, p);
		}else if(abCode == 23) {
			ab = new Nemesis(this, p);
		}else if(abCode == 24) {
			ab = new Pulutos(this, p);
		}
		
		playerMap.put(p.getName(), new WOGPlayer(p, ab, (getTeam(p.getName()).equalsIgnoreCase("BLUE") ? blueTeam : redTeam)));
	}
	
	public void setBlueTeam(Player p) {
		blueTeam.add(p);
		NametagEdit.getApi().setPrefix(p, "&9");
		teamMap.put(p.getName(), "BLUE");
		//backuptlist.put(p.getName(), "블루");
	}
	
	public void setRedTeam(Player p) {
		redTeam.add(p);
		NametagEdit.getApi().setPrefix(p, "&c");
		teamMap.put(p.getName(), "RED");
		//backuptlist.put(p.getName(), "블루");
	}

	public String getTeam(Player p) {
		if(teamMap.containsKey(p.getName())) return teamMap.get(p.getName());
		else return "없음";
	}
	
	public String getTeam(String pName) {
		/*if(blueTeam.teamList.contains(pName)) return "BLUE";
		else if(redTeam.teamList.contains(pName)) return "RED";
		return "없음";*/
		if(teamMap.containsKey(pName)) return teamMap.get(pName);
		else return "없음";
	}
	
	public short carryLavaToChest(Player p) {
		int lavaCnt = countItem(p.getInventory(), Material.LAVA_BUCKET);
		removeItem(p, Material.LAVA_BUCKET, lavaCnt);
		Team team = playerMap.get(p.getName()).team;
		if(team == null) return 0;
		if(team.teamName.equalsIgnoreCase("Blue")) {
			int chestLavaCnt = countItem(blueTeam.inven_TeamChest, Material.LAVA_BUCKET);
			blueTeam.inven_TeamChest.addItem(new ItemStack(Material.LAVA_BUCKET, chestLavaCnt+lavaCnt));
		} else {
			int chestLavaCnt = countItem(redTeam.inven_TeamChest, Material.LAVA_BUCKET);
			blueTeam.inven_TeamChest.addItem(new ItemStack(Material.LAVA_BUCKET, chestLavaCnt+lavaCnt));
		}
		TitleAPI.sendFullTitle(p, 0, 80, 0, "", ChatColor.RED+"용암이 팀 공용상자로 이동되었습니다.");
		return (short) lavaCnt;
	}
	
	public void spawn(Player p) {
		p.setGameMode(GameMode.SURVIVAL);
		p.getInventory().setItem(8, item_wogHelper);
		if(getTeam(p).equalsIgnoreCase("BLUE")) {
			p.teleport(blueTeam.loc_spawn, TeleportCause.PLUGIN);
		} else if(getTeam(p).equalsIgnoreCase("RED")) {
			p.teleport(redTeam.loc_spawn, TeleportCause.PLUGIN);
		} else {
			gameQuitPlayer(p, true, false);
		}
	}	
	
	public void coreBreakEffect(Location l) {
		for(int i = 0; i < 3; i++) {
			Location tmpL = l.clone();
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

	public void blueTeamWin(String reason) {
		
		if(gameStep == 3) {
			gameStep = 4;
			ending = true;
			winTeam = blueTeam;
			sendMessage(ms + ChatColor.GRAY + reason);
			for(String pName : ingamePlayer) {
				Player p = Bukkit.getPlayer(pName);
				if(existPlayer(p)) {
					playerMap.get(pName).ability.hideUI();
					MyUtility.allClear(p);
				}
			}
			try {
				Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
					public void run() {
						for (String pName : ingamePlayer) {
							Player p = Bukkit.getPlayer(pName);
							if(existPlayer(p)) {
								if(getTeam(p).equalsIgnoreCase("BLUE")) {
									//server.egGM.giveGold(p.getName(), 130);
									//p.sendMessage(ms + "승리 보상으로 130골드를 받으셨습니다.");
								} else {
									//server.egGM.giveGold(p.getName(), 800);
									//p.sendMessage(ms + "게임 참가 보상으로 보상으로 80골드를 받으셨습니다.");
								}
							}
							p.playSound(p.getLocation(), Sound.BLOCK_STONE_BREAK, 1.0f, 1.3f);
						}
					}
				}, 100L);
			} catch (Exception e) {
				
			}
			
			try {
				Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
					@Override
					public void run() {
						divideSpawn();
						endGame(false);
						server.egCM.broadCast(server.ms_alert + ChatColor.GRAY + "블루팀의 승리로 "+disPlayGameName+" §7이 종료 되었습니다.");
					}
					
				}, 200L);
			} catch (Exception e) {
				endGame(true);
			}	
		} else if(gameStep == 4) {
			
		} else {
			for (String pName : ingamePlayer) {
				Player p = Bukkit.getPlayer(pName);
				if(existPlayer(p)) {
					try {
						server.spawn(p);
						p.sendMessage(ms+"게임 시작전에 상대팀이 모두 나갔습니다.");
						endGame(false);
					} catch (Exception e) {
						endGame(true);
					}
				}
			}
		}		
	}
	
	public void redTeamWin(String reason) {
		if(gameStep == 3) {
			ending = true;
			gameStep = 4;
			winTeam = redTeam;
			sendMessage(ms + ChatColor.GRAY + reason);
			for(String pName : ingamePlayer) {
				Player p = Bukkit.getPlayer(pName);
				if(existPlayer(p)) {
					playerMap.get(pName).ability.hideUI();
					MyUtility.allClear(p);
				}
			}
			try {
				Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
					public void run() {
						for (String pName : ingamePlayer) {
							Player p = Bukkit.getPlayer(pName);
							if(existPlayer(p)) {
								if(getTeam(p).equalsIgnoreCase("RED")) {
									//server.egGM.giveGold(p.getName(), 130);
									//p.sendMessage(ms + "승리 보상으로 130골드를 받으셨습니다.");
								} else {
									//server.egGM.giveGold(p.getName(), 150);
									//p.sendMessage(ms + "게임 참가 보상으로 보상으로 80골드를 받으셨습니다.");
								}
							}
							p.playSound(p.getLocation(), Sound.BLOCK_STONE_BREAK, 1.0f, 1.3f);
						}
					}
				}, 100L);
			} catch (Exception e) {
				
			}
			try {
				Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
					public void run() {
						divideSpawn();
						endGame(false);
						server.egCM.broadCast(server.ms_alert + ChatColor.GRAY + "레드팀의 승리로 "+disPlayGameName+" 이 종료 되었습니다.");
					}
				}, 200L);
			} catch (Exception e) {
				endGame(true);
			}
		} else if(gameStep == 4){
			
		} else {
			for (String pName : ingamePlayer) {
				Player p = Bukkit.getPlayer(pName);
				if(existPlayer(p)) {
					try {
						server.spawn(p);
						p.sendMessage(ms+"게임 시작전에 상대팀이 모두 나갔습니다.");
						endGame(false);
					} catch (Exception e) {
						// player.teleport(Main.Lobby);
						endGame(true);
						return;
					}
				}
			}
			endGame(false);
		}
	}
	
	public void gameDraw() {
		if(gameStep == 3) {
			gameStep = 4;
			sendMessage(ms + ChatColor.GRAY + "\n제한시간을 초과하여 무승부되었습니다.\n");
			sendSound(Sound.BLOCK_NOTE_BELL);
			for(String pName : ingamePlayer) {
				Player p = Bukkit.getPlayer(pName);
				if(existPlayer(p)) {
					playerMap.get(pName).ability.hideUI();
					MyUtility.allClear(p);
				}
			}
			try {
				Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
					public void run() {
						for (String pName : ingamePlayer) {
							Player p = Bukkit.getPlayer(pName);
							if(existPlayer(p)) {
									//p.sendMessage(ms + "게임 플레이 보상으로 100골드를 받으셨습니다.");
									p.playSound(p.getLocation(), Sound.BLOCK_STONE_BREAK, 1.0f, 1.3f);
									//server.egGM.giveGold(p.getName(), 100);
							}
						}
					}
				}, 100L);
			} catch (Exception e) {
				
			}
			try {
				Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
					public void run() {
						divideSpawn();
						endGame(false);
						server.egCM.broadCast(server.ms_alert + ChatColor.GRAY + "무승부로 "+disPlayGameName+" 이 종료 되었습니다.");
					}
				}, 200L);
			} catch (Exception e) {
				endGame(true);
			}
		}
	}

	public void endGame(boolean force) {
		if(force) {
			server.egCM.broadCast(server.ms_alert + disPlayGameName+ " 게임이 강제 종료 되었습니다.");
		}
		divideSpawn();
		try {
			for(EGScheduler sch : schList) {
				sch.cancelTask(false);
			}
		}catch(Exception e) {
			
		}
		
		schList.clear();
		ingame = false;
		ending = false;
		initGame();
		ingamePlayer.clear();
	}

	//////////////// 이벤트
	public class EventHandlerWOG extends EGEventHandler {

		private WarOfGod game;

		public EventHandlerWOG(EGServer server, WarOfGod warOfGod) {
			super(server);
			this.game = warOfGod;
		}

		@EventHandler
		public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
			Player p = e.getPlayer();
			String[] cmd = e.getMessage().split(" ");
			if (cmd[0].equalsIgnoreCase(cmdMain)) {
				e.setCancelled(true);
				if (p.isOp()) {
					game.onCommand(p, cmd);
				}
			} 
			if(ingamePlayer.contains(p.getName())) { //게임 참여해야지만 가능한 명령어
				
				if(playerMap.get(p.getName()) != null)
					playerMap.get(p.getName()).ability.onPlayerCommand(e);
				
				if(cmd[0].equalsIgnoreCase("/tc")) {
					String team = getTeam(p);
					if(team.equalsIgnoreCase("BLUE") || team.equalsIgnoreCase("RED")) {
						if(teamChatList.contains(p.getName())) {
							teamChatList.remove(p.getName());
							p.sendMessage(ms+"전체 채팅으로 전환합니다.");
							e.setCancelled(true);
						} else {
							teamChatList.add(p.getName());
							p.sendMessage(ms+"팀 채팅으로 전환합니다.");
							e.setCancelled(true);
						}
					}
				} else if(server.cmdSpawn.contains(cmd[0])) {
					gameQuitPlayer(p, true, false);
				}
			}
		}

		@EventHandler
		public void onEntityDamaged(EntityDamageEvent e) {
			if (e.getEntityType() == EntityType.PLAYER) {
				Player p = (Player) e.getEntity();
				DamageCause cause = e.getCause();
				if (ingamePlayer.contains(p.getName())) {
					if(!ingame) {
				        if (cause.equals(DamageCause.VOID)) { //대기실 허공 뎀없음, 텔포		           
				            p.teleport(loc_Join, TeleportCause.PLUGIN);
				        }
				        e.setCancelled(true);
					}  else if(gameStep == 3) {
						if(cause.equals(DamageCause.VOID)) {
							p.setHealth(0);
						} else if(playerMap.get(p.getName()) != null){
							playerMap.get(p.getName()).ability.onEntityDamaged(e);
						}
					} else {
				        if (cause.equals(DamageCause.VOID)) { //대기실 허공 뎀없음, 텔포		           
				            p.teleport(loc_Join, TeleportCause.PLUGIN);
				        }
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
			
			String title = e.getInventory().getTitle();
			
			if (title.equalsIgnoreCase("§0§l"+inventoryGameName+" 도우미")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}	
				p.closeInventory();
				gameHelper(p, e.getSlot());
			}  else if(title.equalsIgnoreCase("§0§l"+inventoryGameName+" 게임설명")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}	
				p.closeInventory();
				gameHelper2(p, e.getSlot());
			} else if(title.equalsIgnoreCase("§0§l신 목록")) {
				e.setCancelled(true);
				/*if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}	
				p.closeInventory();
				//abilityListHelper(p, e.getSlot());*/
			}
			
			if (!ingamePlayer.contains(p.getName())) return;
			if(playerMap.get(p.getName()) != null)
				playerMap.get(p.getName()).ability.onClickInventory(e);
			
			if(e.getCurrentItem() == null || e.getCurrentItem().equals(item_wogHelper)) {
				e.setCancelled(true);
			}	
			
			if(title.equalsIgnoreCase("§0§l"+inventoryGameName+" 메뉴")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}	
				p.closeInventory();
				wogHelper(p, e.getSlot());
			}else if(title.contains("목록")) {
				e.setCancelled(true);
			}
		}

		@EventHandler
		public void onHitPlayer(EntityDamageByEntityEvent e) {
			if(e.getEntity() instanceof Player && ingame) { //게임시작했을때만
				Player player = (Player) e.getEntity();
				Player damager = null;
				
				if (!ingamePlayer.contains(player.getName())) return; //피해자가 겜에 참가중이지않으면 규칙 무시
				
				boolean isDirectAttack = true;
				
				if (e.getDamager() instanceof Snowball) { //화살과 총알에대한 공격자 설정
					Snowball snowball = (Snowball) e.getDamager();
					if (snowball.getShooter() instanceof Player) {
						damager = (Player) snowball.getShooter();
						e.setDamage(7);
						isDirectAttack = false;
					}
				}
				if (e.getDamager() instanceof Arrow) {
					Arrow arrow = (Arrow) e.getDamager();
					if (arrow.getShooter() instanceof Player) {
						damager = (Player) arrow.getShooter();
						isDirectAttack = false;
					}
				}
				if (e.getDamager() instanceof Player)
					damager = (Player) e.getDamager();
				
				if (damager == null) //공격자 없으면 리턴
					return;
				
				if (!ingamePlayer.contains(damager.getName())) { //공격자가 겜에 참가중이지않으면 규칙 무시
					return;
				}
				
				if(isDirectAttack && !checkVictimDelay(damager.getName(), player)) {
					e.setCancelled(true);
					return;
				}
				
				ItemStack mainHand = damager.getInventory().getItemInMainHand();
				ItemStack offHand = damager.getInventory().getItemInOffHand();
				if(mainHand != null) {
					if(mainHand.getType() == Material.WOOD_AXE
							|| mainHand.getType() == Material.STONE_AXE
							|| mainHand.getType() == Material.GOLD_AXE
							||	mainHand.getType() == Material.IRON_AXE
							|| mainHand.getType() == Material.DIAMOND_AXE) {
						e.setDamage(e.getDamage()-3);
					}
				}
				if(offHand != null) {
					if(offHand.getType() == Material.WOOD_AXE
							|| offHand.getType() == Material.STONE_AXE
							|| offHand.getType() == Material.GOLD_AXE
							||	offHand.getType() == Material.IRON_AXE
							|| offHand.getType() == Material.DIAMOND_AXE) {
						e.setDamage(e.getDamage()-3);
					}
				}
				
				WOGPlayer dWogP = playerMap.get(damager.getName());
				WOGPlayer pWogP = playerMap.get(player.getName());
				if(dWogP != null) {
					if(dWogP.team.teamName.equalsIgnoreCase(pWogP.team.teamName)) {
						e.setCancelled(true);
					} else {
						dWogP.ability.onHitPlayer(e);
						pWogP.ability.onHitted(e);
					}
				}
					
				
				ItemStack weapon = damager.getInventory().getItemInMainHand();
				

			}
		}
		
		@EventHandler
		public void onRegainHealth(EntityRegainHealthEvent e) {
			if(!(e.getEntity() instanceof Player)) return;
			Player p = (Player) e.getEntity();
			
			if(!ingamePlayer.contains(p.getName())) return;
			
			if(playerMap.get(p.getName()) != null)
				playerMap.get(p.getName()).ability.onRegainHealth(e);
		}
		
		@EventHandler
		public void onPlayerShotBow(EntityShootBowEvent e) {
			if (((e.getEntity() instanceof Player)) && ingame) {
				Player p = (Player) e.getEntity();
				
				if(!ingamePlayer.contains(p.getName())) return;
				
				if(playerMap.get(p.getName()) != null)
					playerMap.get(p.getName()).ability.onPlayerShotBow(e);

			}
		}
		
		@EventHandler
		public void onBlockBreak(BlockBreakEvent e) {
			Player p = e.getPlayer();
			if(!ingamePlayer.contains(p.getName())) return;
			
			if(gameStep != 3) {
				e.setCancelled(true); 
				return;
			}
			
			if(playerMap.get(p.getName()) != null)
				playerMap.get(p.getName()).ability.onBlockBreak(e);		
			
			Block b = e.getBlock();
			Location l = b.getLocation();
			if(l.getBlockX() > map.pos1.getBlockX() || l.getBlockX() < map.pos2.getBlockX()
					|| l.getBlockY() > map.pos1.getBlockY() || l.getBlockY() < map.pos2.getBlockY()
					|| l.getBlockZ() > map.pos1.getBlockZ() || l.getBlockZ() < map.pos2.getBlockZ()) {
				e.setCancelled(true);
			}
			
			if((l.getBlockX() == redTeam.loc_chest.getBlockX()
					&& l.getBlockY() == redTeam.loc_chest.getBlockY()
					&& l.getBlockZ() == redTeam.loc_chest.getBlockZ())
					|| (l.getBlockX() == blueTeam.loc_chest.getBlockX()
							&& l.getBlockY() == blueTeam.loc_chest.getBlockY()
							&& l.getBlockZ() == blueTeam.loc_chest.getBlockZ())) {
				p.sendMessage(ms+"각 팀의 공용상자는 파괴할 수 없습니다.");
				e.setCancelled(true);
			}
				
			if(b.getType() == coreBlock) {
				Material itemMeta = p.getInventory().getItemInMainHand().getType();
				if(itemMeta == Material.IRON_PICKAXE || itemMeta == Material.DIAMOND_PICKAXE 
						|| itemMeta == Material.GOLD_PICKAXE|| itemMeta == Material.STONE_PICKAXE){
					e.setCancelled(true);
					p.sendMessage(ms+"돌, 철, 금, 다이아 곡괭이로는 캐실 수 없습니다!");
					return;
				}
				/*p.sendMessage("e: "+l.getBlockX()+","+l.getBlockY()+","+l.getBlockZ());
				p.sendMessage("b: "+blueTeam.core.getBlockX()+","+blueTeam.core.getBlockY()+","+blueTeam.core.getBlockZ());
				p.sendMessage("r: "+blueTeam.core.getBlockX()+","+blueTeam.core.getBlockY()+","+blueTeam.core.getBlockZ());*/
				
				if(l.getBlockX() == blueTeam.core.getBlockX()
						&& l.getBlockY() == blueTeam.core.getBlockY()
						&& l.getBlockZ() == blueTeam.core.getBlockZ()) {
					if(getTeam(p).equalsIgnoreCase("BLUE")) {
						e.setCancelled(true);
						p.sendMessage(ms+"아군의 코어가 아닌 적의 코어를 부수세요!");
					} else {
						coreBreakEffect(blueTeam.loc_spawn);
						coreBreakEffect(redTeam.loc_spawn);
						WOGPlayer wogP = playerMap.get(p.getName());
						if(wogP != null) wogP.breakCore += 1;
						Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
							public void run() {
								redTeamWin(p.getName()+" 님께서 적팀의 코어를 파괴하였습니다.");
							}
						}, 80l);
					}
				} else if(l.getBlockX() == redTeam.core.getBlockX()
						&& l.getBlockY() == redTeam.core.getBlockY()
						&& l.getBlockZ() == redTeam.core.getBlockZ()) {
					if(getTeam(p).equalsIgnoreCase("RED")) {
						e.setCancelled(true);
						p.sendMessage(ms+"아군의 코어가 아닌 적의 코어를 부수세요!");
					} else {
						coreBreakEffect(blueTeam.loc_spawn);
						coreBreakEffect(redTeam.loc_spawn);
						WOGPlayer wogP = playerMap.get(p.getName());
						if(wogP != null) wogP.breakCore += 1;
						Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
							public void run() {
								blueTeamWin(p.getName()+" 님께서 적팀의 코어를 파괴하였습니다.");
							}
						}, 80l);
					}
				}
			} else if(b.getType() == Material.COBBLESTONE) {
				WOGPlayer wogP = playerMap.get(p.getName());
				if(wogP != null) wogP.breakCobbleStone += 1;
			}
		}
		
		@EventHandler
		public void onUseBucket(PlayerBucketEmptyEvent e) {
			Player p = e.getPlayer();
			Material bucket = e.getBucket();
			if (!ingamePlayer.contains(p.getName()))
				return;

			if (gameStep != 3) {
				e.setCancelled(true);
				return;
			}
			
			String team = getTeam(p);
			
			if(bucket.toString().contains("WATER") || bucket.toString().contains("LAVA")) {
				if(team.equalsIgnoreCase("BLUE")) {
					if(blueTeam.loc_spawn.distance(e.getBlockClicked().getLocation()) > 20) {
						TitleAPI.sendFullTitle(p, 0, 80, 0, "", ChatColor.RED+"기지 20칸 밖에서는 양동이를 사용하실 수 없습니다");
						e.setCancelled(true);
					}
				} else if(team.equalsIgnoreCase("RED")) {
					if(redTeam.loc_spawn.distance(e.getBlockClicked().getLocation()) > 20) {
						TitleAPI.sendFullTitle(p, 0, 80, 0, "", ChatColor.RED+"기지 20칸 밖에서는 양동이를 사용하실 수 없습니다");
						e.setCancelled(true);
					}
				}
			}	
			
			Block b = e.getBlockClicked();
			
			if((Math.abs(b.getLocation().getBlockX() - playerMap.get(p.getName()).team.core.getBlockX())) <= 3
					&& (Math.abs(b.getLocation().getBlockY() - playerMap.get(p.getName()).team.core.getBlockY())) <= 3
					&& (Math.abs(b.getLocation().getBlockZ() - playerMap.get(p.getName()).team.core.getBlockZ()) <= 3)) {
				TitleAPI.sendFullTitle(p, 0, 80, 0, "", ChatColor.RED+"코어 주변 3블럭이내에는 설치가 불가능합니다.");
				e.setCancelled(true);
			}else {
				Location l = b.getLocation();
				if(redTeam.loc_spawn.distance(l) <= 2 || blueTeam.loc_spawn.distance(l) <= 2) {
					e.setCancelled(true);
					TitleAPI.sendFullTitle(p, 0, 80, 0, "", ChatColor.RED+"스폰 블럭 2칸내에는 설치가 불가능합니다.");
				}
			}
		}
		
		@EventHandler
		public void onBlockPlaced(BlockPlaceEvent e) {
			Player p = e.getPlayer();
			if(!ingamePlayer.contains(p.getName())) return;
			
			if(gameStep != 3) {
				e.setCancelled(true); 
				return;
			}
			
			if(playerMap.get(p.getName()) != null)
				playerMap.get(p.getName()).ability.onBlockPlaced(e);
			
			Block b = e.getBlock();
			
			if(b != null) {
				if((Math.abs(b.getLocation().getBlockX() - playerMap.get(p.getName()).team.core.getBlockX())) <= 2
						&& (Math.abs(b.getLocation().getBlockY() - playerMap.get(p.getName()).team.core.getBlockY())) <= 2
						&& (Math.abs(b.getLocation().getBlockZ() - playerMap.get(p.getName()).team.core.getBlockZ()) <= 2)) {
					TitleAPI.sendFullTitle(p, 0, 80, 0, "", ChatColor.RED+"코어 주변 2블럭이내에는 설치가 불가능합니다.");
					e.setCancelled(true);
				}else {
					Location l = b.getLocation().subtract(0,1,0);
					if(MyUtility.compareLocation(l, redTeam.loc_spawn) || MyUtility.compareLocation(l, blueTeam.loc_spawn)) {
						e.setCancelled(true);
						TitleAPI.sendFullTitle(p, 0, 80, 0, "", ChatColor.RED+"스폰 블럭 위에는 설치가 불가능합니다.");
					}
				}
			}
		}
		
		@EventHandler
		public void onItemDrop(PlayerDropItemEvent e) {
			Player p = e.getPlayer();
			if(!ingamePlayer.contains(p.getName())) return;
			
			if(playerMap.get(p.getName()) != null)
				playerMap.get(p.getName()).ability.onItemDrop(e);
			
			if(e.getItemDrop().getItemStack().getItemMeta().hasDisplayName()) {
				ActionBarAPI.sendActionBar(p, "이 아이템은 버리실 수 없습니다.", 20);
				e.setCancelled(true);
			}

		}
		
		
		@EventHandler
		public void onVehicleSpawn(VehicleCreateEvent e) {
			Vehicle vehicle = e.getVehicle();
			List<Entity> nearby = vehicle.getNearbyEntities(8, 8, 8);
			Entity near = null;
			double minDis = 10;
			for(Entity entity : nearby) {
				if(entity instanceof Player) {
					double dis = entity.getLocation().distance(vehicle.getLocation());
					if(dis < minDis) {
						near = entity;
						minDis = dis;
					}
				}
			}
			
			if(near instanceof Player) {
				Player p = (Player) near;
				if(ingamePlayer.contains(p.getName())) {
					e.setCancelled(true);
				}
			}
		}

		@EventHandler
		public void onPlayerDeath(PlayerDeathEvent e) {
			if (((e.getEntity() instanceof Player)) && ingame) {
				Player p = e.getEntity();
				Player k = p.getKiller();
				if(!ingamePlayer.contains(p.getName())) return;
				
				for(ItemStack item : e.getDrops()) {
					if(item.getType() == Material.ENCHANTED_BOOK) {
						e.getDrops().remove(item);
						break;
					}							
				}
				
				if(playerMap.get(p.getName()) != null)
					playerMap.get(p.getName()).ability.onPlayerDeath(e);
				if(k != null && ingamePlayer.contains(k.getName())) {
					if(playerMap.get(k.getName()) != null)
						playerMap.get(k.getName()).ability.onKillPlayer(p);
				}

			}
		}
		
		@EventHandler
		public void onPlayerRespawn(PlayerRespawnEvent e) {
			Player p = e.getPlayer();
			if(!ingamePlayer.contains(p.getName())) return;
			
			if(ingame) {
				e.setRespawnLocation(loc_tempSpawn);
				p.setGameMode(GameMode.SPECTATOR);
				p.sendMessage(ms+"5초 후 부활합니다.");
				//p.playSound(team.equalsIgnoreCase("BLUE") ? loc_blue_spawn : loc_red_spawn, Sound.BLOCK_NOTE_CHIME, 1.0f, 1.0f);
				TitleAPI.sendFullTitle(p, 10, 70, 30, "", ChatColor.RED+"5초 후 부활합니다.");
				//timerLevelBar(p, 10, true);
				int respawnId = Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
					public void run() {
						if(ingamePlayer.contains(p.getName())) {
							if(ending) {
								TitleAPI.sendFullTitle(p, 10, 70, 30,  ChatColor.RED+"게임 종료","게임이 종료되어 부활하지 않습니다.");
							} else {
								spawn(p);
								TitleAPI.sendFullTitle(p, 10, 70, 30,  ChatColor.RED+"부활하였습니다.","5초간 회복버프를 얻습니다.");
								MyUtility.attackDelay(p, false);
								p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BELL, 1.0f, 1.0f);
								p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 4));
								if(playerMap.get(p.getName()) != null)
									playerMap.get(p.getName()).ability.onPlayerRespawn(e);
							}							
						}					
					}
				}, 100l);
			}
		}

		@EventHandler
		public void onPlayerQuit(PlayerQuitEvent e) {
			Player p = e.getPlayer();
			if (!ingamePlayer.contains(p.getName())) return;
			if(playerMap.get(p.getName()) != null)
				playerMap.get(p.getName()).ability.onPlayerQuit(e);
			gameQuitPlayer(p, true, false);
		}

		@EventHandler
		public void onMouseClick(PlayerInteractEvent e) {
			Player p = e.getPlayer();
			Action action = e.getAction();

			if (!ingamePlayer.contains(p.getName())) return; 
			
			if(playerMap.get(p.getName()) != null)
				playerMap.get(p.getName()).ability.onMouseClick(e);
			
			if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;
			
			Block b = e.getClickedBlock();
			
			if(b != null && b.getType() == Material.OBSIDIAN) {
				if(p.getInventory().getItemInMainHand().getType() == Material.BUCKET){
					b.setType(Material.AIR);
					p.getInventory().getItemInMainHand().setType(Material.LAVA_BUCKET);
				}
			}
			
			if(b != null && b.getType() == Material.TRAPPED_CHEST) {
				if(getTeam(p).equalsIgnoreCase("BLUE") ) {
					if(b.getLocation().distance(blueTeam.loc_chest) == 0) {
						p.openInventory(blueTeam.inven_TeamChest);
						p.getWorld().playSound(p.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0F, 1.0F);
						e.setCancelled(true);
					} else if(b.getLocation().distance(redTeam.loc_chest) == 0){
						p.sendMessage(ms+"레드팀의 공용상자입니다.");
						e.setCancelled(true);
					}
				} else if(getTeam(p).equalsIgnoreCase("RED")) {
					if(b.getLocation().distance(redTeam.loc_chest) == 0) {
						p.getWorld().playSound(p.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0F, 1.0F);
						p.openInventory(redTeam.inven_TeamChest);
						e.setCancelled(true);
					} else if(b.getLocation().distance(blueTeam.loc_chest) == 0){
						p.sendMessage(ms+"블루팀의 공용상자입니다.");
						e.setCancelled(true);
					}
				}
			}
			
			if (e.getItem() == null || e.getItem().getType() == Material.AIR) { //아이템을 안들고 우클릭했을때 리턴
				return;
			} else if (getHeldMainItemName(p).equalsIgnoreCase("§f[ §6게임 도우미 §f]")) {
				p.openInventory(inven_gameHelper);
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 2.0f);
			} else if (getHeldMainItemName(p).equalsIgnoreCase("§f[ §b게임 메뉴 §f]")) {
				p.openInventory(inven_wogHelper);
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 2.0f);
			}
		}
		
		@EventHandler
		public void onFoodLevelChange(FoodLevelChangeEvent e){
			if(e.getEntity() instanceof Player){
				Player p = (Player) e.getEntity();
				if(!ingamePlayer.contains(p.getName())) return;
				
				if(!ingame) e.setCancelled(true);
				
				if(playerMap.get(p.getName()) != null)
					playerMap.get(p.getName()).ability.onFoodLevelChange(e);
			}
		}
		
		@EventHandler
		public void onInventoryClose(InventoryCloseEvent e){
			Player p = (Player) e.getPlayer();
			if(ingamePlayer.contains(p.getName())) {
				
				if(playerMap.get(p.getName()) != null)
					playerMap.get(p.getName()).ability.onInventoryClose(e);
	
			}
		}
		
		@EventHandler
		public void onPlayerMove(PlayerMoveEvent e){
			Player p = (Player) e.getPlayer();
			if(ingamePlayer.contains(p.getName()) && ingame) {
				///위치이동 아니면 캔슬
				if(e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockY() == e.getTo().getBlockY()
						&& e.getFrom().getBlockZ() == e.getTo().getBlockZ()) return;
				
				if(gameStep == 2) egCancelMovement(e);
				
				if(playerMap.get(p.getName()) != null)
					playerMap.get(p.getName()).ability.onPlayerMove(e);
				
				Location gotoLoc = new Location(e.getTo().getWorld(), e.getTo().getBlockX(), e.getTo().getBlockY(), e.getTo().getBlockZ());
				String team = getTeam(p);
				if (team.equalsIgnoreCase("BLUE")) {
					if (blueTeam.loc_spawn.distance(gotoLoc) > 20) {
						if (hasItem(p, Material.LAVA_BUCKET, 1))
							carryLavaToChest(p);
					}
					if (redTeam.loc_spawn.distance(gotoLoc) < 20) {
						if (!baseAccess) {
							p.teleport(blueTeam.loc_spawn, TeleportCause.PLUGIN);
							p.sendMessage(ms + "게임 시작 후 5분까지는 적팀의 기지에 접근이 불가능합니다.");
						}
					}
				} else {
					if (redTeam.loc_spawn.distance(gotoLoc) > baseDis) {
						if (hasItem(p, Material.LAVA_BUCKET, 1))
							carryLavaToChest(p);
					}
					if (blueTeam.loc_spawn.distance(gotoLoc) < baseDis) {
						if (!baseAccess) {
							p.teleport(redTeam.loc_spawn, TeleportCause.PLUGIN);
							p.sendMessage(ms + "게임 시작 후 5분까지는 적팀의 기지에 접근이 불가능합니다.");
						}
					}
				}
					
				/*if(gotoLoc.getBlock().getType() == Material.GOLD_BLOCK){
					if(Cooldown.Checkcooldown(p, "치유의샘")){
						Cooldown.Setcooldown(p, "치유의샘", 75, false);
						p.getWorld().playSound(p.getLocation(), Sound.LEVEL_UP, 1.5f, 1.0f);
						p.sendMessage(Main.MS+"치유의  샘을 사용했습니다.");
						MobSystem.Addhp(p, 14);
					}
				}*/
			}
		}
			
		@EventHandler
		public void onPlayerChat(PlayerChatEvent e){
			Player p = (Player) e.getPlayer();
			if(ingamePlayer.contains(p.getName())) {
				if(playerMap.get(p.getName()) != null)
					playerMap.get(p.getName()).ability.onChat(e);
				String msg = e.getMessage();
				if(teamChatList.contains(p.getName()) && gameStep == 3) {
					String team = getTeam(p);
					String str = (team.equalsIgnoreCase("BLUE") ? blueTeam.ms : redTeam.ms)+p.getName()+" >> §b"+msg;
					server.egCM.sendMessagesToStringList((playerMap.get(p.getName()).team.teamName.equals("BLUE") ? blueTeam.teamList : redTeam.teamList), p, str, false);
					sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 1.5f);
				} else {
					String str = allMS+p.getName()+" >> §6"+msg;
					server.egCM.sendMessagesToStringList(ingamePlayer, p, str, false);
					sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 1.5f);
				}
				e.setCancelled(true);
			}
		}
		
		
		@EventHandler
		public void onPlayerItemPickUp(PlayerPickupItemEvent e) {
			Player p = (Player) e.getPlayer();
			if(ingamePlayer.contains(p.getName())) {
				if(e.getItem().getItemStack().getType() == Material.LAVA_BUCKET) {
					String team = getTeam(p);
					if(team.equalsIgnoreCase("BLUE")) {
						if(blueTeam.loc_spawn.distance(p.getLocation()) > 7) {
							if(hasItem(p, Material.LAVA_BUCKET, 1))
								carryLavaToChest(p);
						} 
						if(redTeam.loc_spawn.distance(p.getLocation()) > 7) {
							
						}
					}
				}
				
				if(playerMap.get(p.getName()) != null)
					playerMap.get(p.getName()).ability.onPlayerItemPickUp(e);
			}
		}
		
		@EventHandler
		public void onSneak(PlayerToggleSneakEvent e){
			Player p = (Player) e.getPlayer();
			if(ingamePlayer.contains(p.getName())) {
				if(playerMap.get(p.getName()) != null)
					playerMap.get(p.getName()).ability.onSneak(e);
			}
		}
	}
	
	public void applyData(String pName) {
		WOGPlayer wogP = playerMap.get(pName);
		if(wogP == null)return;
		PlayerData playerData = server.egDM.getPlayerData(pName);
		if(playerData == null) return;
		MinigameData gameData = playerData.getGameData("WarOfGod");
		if(gameData == null) return;
		if(!(gameData instanceof WogData)) return;
		WogData playerWogData = (WogData) gameData;
		playerWogData.applyNewData(wogP);
		playerWogData.addPlaycount();
		
		//MMR 책정
		int mmr = calcMMR(wogP);
		playerWogData.setMMR(playerWogData.getMMR() + mmr);
		Player p = Bukkit.getPlayer(pName);
		if (existPlayer(p)) {
			p.sendMessage("");
			p.sendMessage(ms + "§f" + disPlayGameName + "§f의 게임 결과로 §a" + mmr + "§f점이 반영됐습니다.");
			p.sendMessage("");
		}
		playerWogData.saveData();
	}
	
	public int calcMMR(WOGPlayer wogP) {
		int MMR = 0;
		
		MMR += wogP.breakCore * 10;
		MMR += ((int)wogP.breakCobbleStone * 0.25);
	
		if(winTeam != null) {
			if(winTeam.equals(wogP.team)) {
				MMR += 70;
			}else {
				MMR -= 40;	
			}
		}
		
		return MMR;
	}
	
	public class WOGPlayer{
		
		public String playerName;
		public Ability ability;
		public int breakCobbleStone;
		public int breakCore;
		public Team team;
		
		public WOGPlayer(Player p, Ability ab, Team team) {
			playerName = p.getName();
			ability = ab;
			this.team = team;
		}
		
	}
	
	public class Team{
		
		public List<String> teamList = new ArrayList<String>(6);
		public Location loc_spawn;
		public String ms;
		public Inventory inven_TeamList;
		public Inventory inven_TeamChest;
		public String teamName;
		public short killCnt;
		public Location core;
		public Location loc_chest;
		
		public Team(String teamName) {
			
			this.teamName = teamName;
			inven_TeamChest = Bukkit.createInventory(null, 27, "§0§l"+teamName+"팀 공용상자");
			inven_TeamList = Bukkit.createInventory(null, 27, "§0§l"+teamName+"팀 플레이어 목록");
		}
		
		public void add(Player p) {
			
			teamList.add(p.getName());
			//Bukkit.dispatchCommand(p.getServer().getConsoleSender(), "scoreboard teams join "+teamName+" "+p.getName());
			
		}
		
		public void updateTeamList() {
			inven_TeamList.clear();
			updateSch.schTime = teamList.size();
			updateSch.schTime2 = 1;
			updateSch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
				public void run() {
					if(updateSch.schTime > 0) {
						updateSch.schTime--;
						String pName = teamList.get(updateSch.schTime-1);
						Player p = Bukkit.getPlayer(pName);
						if(existPlayer(p)) {
							ItemStack pHead = getHead(p);
							ItemMeta meta = pHead.getItemMeta();
							meta.setDisplayName(p.getName());
							List<String> loreList = new ArrayList<String>(1);
							loreList.add("");
							String abilityName = playerMap.get(p.getName()).ability.abilityName;
							loreList.add("§7"+ abilityName == null ? "" : abilityName);
							loreList.add("");
							meta.setLore(loreList);
							pHead.setItemMeta(meta);
							inven_TeamList.setItem(updateSch.schTime2, pHead);	
							if(updateSch.schTime2 == 7 || updateSch.schTime2 == 16 || updateSch.schTime2 == 25) updateSch.schTime2 += 3;
							else updateSch.schTime2 += 2;
						}
					} else {
						updateSch.cancelTask(false);
					}
				}
			}, 0l, 3l);
		}
		
		public void remove(String pName) {
			teamList.remove(pName);
		}
		
	}
}
