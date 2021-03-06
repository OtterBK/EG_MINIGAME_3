package Minigames.RandomWeaponWar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.EntityShootBowEvent;
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
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

import com.coloredcarrot.api.sidebar.Sidebar;
import com.coloredcarrot.api.sidebar.SidebarString;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.connorlinfoot.titleapi.TitleAPI;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.handler.PickupHandler;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;

import EGServer.EGServer;
import EGServer.DataManger.PlayerData;
import EGServer.DataManger.MinigameData.MinigameData;
import EGServer.DataManger.MinigameData.RwwData;
import EGServer.ServerManager.EGEventHandler;
import EGServer.ServerManager.EGScheduler;
import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.AiasShield;
import Minigames.RandomWeaponWar.Weapons.AirCannon;
import Minigames.RandomWeaponWar.Weapons.AvoidAxe;
import Minigames.RandomWeaponWar.Weapons.BigHammer;
import Minigames.RandomWeaponWar.Weapons.Blinder;
import Minigames.RandomWeaponWar.Weapons.Booster;
import Minigames.RandomWeaponWar.Weapons.BorrowHealth;
import Minigames.RandomWeaponWar.Weapons.BurnOut;
import Minigames.RandomWeaponWar.Weapons.C4Bomber;
import Minigames.RandomWeaponWar.Weapons.CreeperWeapon;
import Minigames.RandomWeaponWar.Weapons.DNAChanger;
import Minigames.RandomWeaponWar.Weapons.DeadDefense;
import Minigames.RandomWeaponWar.Weapons.DeadRevive;
import Minigames.RandomWeaponWar.Weapons.DeathNote;
import Minigames.RandomWeaponWar.Weapons.DiscoBangBang;
import Minigames.RandomWeaponWar.Weapons.DragonRelic;
import Minigames.RandomWeaponWar.Weapons.DragonSword;
import Minigames.RandomWeaponWar.Weapons.DrainStone;
import Minigames.RandomWeaponWar.Weapons.EscapeWeapon;
import Minigames.RandomWeaponWar.Weapons.FastBow;
import Minigames.RandomWeaponWar.Weapons.FlashBang;
import Minigames.RandomWeaponWar.Weapons.FlickerBook;
import Minigames.RandomWeaponWar.Weapons.ForceTaker;
import Minigames.RandomWeaponWar.Weapons.GlowingDust;
import Minigames.RandomWeaponWar.Weapons.GodokSword;
import Minigames.RandomWeaponWar.Weapons.GravityController;
import Minigames.RandomWeaponWar.Weapons.Grenade;
import Minigames.RandomWeaponWar.Weapons.HappyBowl;
import Minigames.RandomWeaponWar.Weapons.IcePrison;
import Minigames.RandomWeaponWar.Weapons.Invincibler;
import Minigames.RandomWeaponWar.Weapons.IronFeather;
import Minigames.RandomWeaponWar.Weapons.IronHook;
import Minigames.RandomWeaponWar.Weapons.Levitator;
import Minigames.RandomWeaponWar.Weapons.LocationHolder;
import Minigames.RandomWeaponWar.Weapons.LocationSaver;
import Minigames.RandomWeaponWar.Weapons.LocationSwitcher;
import Minigames.RandomWeaponWar.Weapons.Machingun;
import Minigames.RandomWeaponWar.Weapons.MagnetWeapon;
import Minigames.RandomWeaponWar.Weapons.Mirroring;
import Minigames.RandomWeaponWar.Weapons.MoveToPlayerWeapon;
import Minigames.RandomWeaponWar.Weapons.MurderSword;
import Minigames.RandomWeaponWar.Weapons.NoDebuff;
import Minigames.RandomWeaponWar.Weapons.NoGravityBow;
import Minigames.RandomWeaponWar.Weapons.Nomeither;
import Minigames.RandomWeaponWar.Weapons.OdinRod;
import Minigames.RandomWeaponWar.Weapons.OnePunch;
import Minigames.RandomWeaponWar.Weapons.Parachute;
import Minigames.RandomWeaponWar.Weapons.ParkourBook;
import Minigames.RandomWeaponWar.Weapons.PathFinder;
import Minigames.RandomWeaponWar.Weapons.PoisonKnife;
import Minigames.RandomWeaponWar.Weapons.RandomAxe;
import Minigames.RandomWeaponWar.Weapons.RandomBuffer;
import Minigames.RandomWeaponWar.Weapons.RandomMedic;
import Minigames.RandomWeaponWar.Weapons.RandomSword;
import Minigames.RandomWeaponWar.Weapons.ReflectorMirror;
import Minigames.RandomWeaponWar.Weapons.RollerSkate;
import Minigames.RandomWeaponWar.Weapons.RoseKnife;
import Minigames.RandomWeaponWar.Weapons.ShootFireball;
import Minigames.RandomWeaponWar.Weapons.SlimeJumper;
import Minigames.RandomWeaponWar.Weapons.SnipeRifle;
import Minigames.RandomWeaponWar.Weapons.SpecialWeapon;
import Minigames.RandomWeaponWar.Weapons.Spoon;
import Minigames.RandomWeaponWar.Weapons.Stimulant;
import Minigames.RandomWeaponWar.Weapons.StopWatch;
import Minigames.RandomWeaponWar.Weapons.Telephone512K;
import Minigames.RandomWeaponWar.Weapons.TeleportStick;
import Minigames.RandomWeaponWar.Weapons.ThrowBomb;
import Minigames.RandomWeaponWar.Weapons.ThunderAxe;
import Minigames.RandomWeaponWar.Weapons.WindShooter;
import Minigames.RandomWeaponWar.Weapons.WitherKnife;
import Minigames.RandomWeaponWar.Weapons.WizardPowder;
import Minigames.RandomWeaponWar.Weapons.WizardRod;
import Minigames.RandomWeaponWar.Weapons.WizardUltimate;
import Utility.MyUtility;
import Utility.WorldBorderAPI;
import me.confuser.barapi.BarAPI;
import net.minecraft.server.v1_12_R1.EntityShulker;

public class RandomWeaponWar extends Minigame{
	// 이벤트용
	public EventHandlerRWW event;
	
	public String ms = "§7[§6전체§7] ";

	///////////// private
	// 게임 플레이어 리스트
	//private HashMap<String, SpfPlayer> playerMap = new HashMap<String, SpfPlayer>();
	private String cmdMain;
	//////// 게임 관련
	public int gameStep = 0;
	public EGScheduler mainSch;
	
	public List<Location> loc_items = new ArrayList<Location>();
	public List<Location> loc_helidoors = new ArrayList<Location>(20);
	public Location loc_Helicopter;
	
	public Location tmpItemPos1;
	public Location tmpItemPos2;
	
	public List<String> noSoundList = new ArrayList<String>();
	
	public LinkedHashMap<String, Long> testMap = new LinkedHashMap<String, Long>();
	
	//무기들
	public LinkedHashMap<String, SpecialWeapon> allWeapon = new LinkedHashMap<String, SpecialWeapon>();
	public LinkedHashMap<String, SpecialWeapon> specialWeaponMap = new LinkedHashMap<String, SpecialWeapon>();
	public List<String> tmpWeaponList = new ArrayList<String>(specialWeaponMap.keySet());
	
	public SpecialWeapon item_parachute = new Parachute(this);
	public SpecialWeapon item_grenade = new Grenade(this);
	public SpecialWeapon item_flashbang = new FlashBang(this);
	public ItemStack item_finder;
	public ItemStack item_noSound;
	public ItemStack item_getRandomWeapon;
	
	//파밍템
	public LinkedHashMap<ItemStack, Integer> farmingItemsMap = new LinkedHashMap<ItemStack, Integer>();
	
	public List<Hologram> itemHolograms = new ArrayList<Hologram>();
	
	//자기장
	public int limiterLv = 0;
	public Location limiterCenter;
	public int limiterRadius = 600;
	public boolean applyBorder = false;
	public boolean doDamage = false;
	
	//힐 관련
	public HashMap<String, EGScheduler> healItemSchMap = new HashMap<String, EGScheduler>();
	
	//보급관련
	public HashMap<Location, Material> supplyLocMap = new HashMap<Location, Material>();
	public HashMap<String, List<EntityShulker>> supplyShulkerMap = new HashMap<String, List<EntityShulker>>();
	public List<ItemStack> supplyItems = new ArrayList<ItemStack>();
	
	////// 각종 인벤토리
	public Inventory inven_gameHelper;
	
	//////// 사이드바
	private Sidebar rwwSidebar;
	private List<SidebarString> textList = new ArrayList<SidebarString>(5);
	
	//자기 자신
	public RandomWeaponWar rww = this;
	
	//랭크 저장용
	public HashMap<String, RwwPlayer> rwwPlayerMap = new HashMap<String, RwwPlayer>();
	
	public RandomWeaponWar(EGServer server, String gameName, String displayGameName, String cmdMain) {

		//////////////////// 필수 설정값
		super(server);
		
		this.cmdMain = cmdMain;
		
		ms = "§7[ §e! §7] §f: §c랜덤 무기 전쟁 §f>> "; // 기본 메세지
		this.gameName = gameName;
		this.disPlayGameName = displayGameName;
		inventoryGameName = ChatColor.stripColor(disPlayGameName);
		minPlayer = 4;
		maxPlayer = 24;
		startCountTime = 60;

		doneSetting = loadGameData(); // 게임 시작 위치 등등 로드	
		
		/////////////////////// 자동 설정(아이템등등)
		dirSetting(this.gameName);
		////////////////
		
		//map.loadData(locPath);
		
		mainSch = new EGScheduler(this);
		
		SidebarString tmpLine = new SidebarString("");
		rwwSidebar = new Sidebar("§f[ §6게임 현황 §f]", server, 600, tmpLine);
		Objective tmpO = rwwSidebar.getTheScoreboard().getObjective("health");
		if(tmpO != null) {
			tmpO.unregister();
		}
		Objective o = rwwSidebar.getTheScoreboard().registerNewObjective("health", "health");
		o.setDisplayName("§cHP");
		o.setDisplaySlot(DisplaySlot.BELOW_NAME);
		
		/////////////////// 게임 도우미
		inven_gameHelper = Bukkit.createInventory(null, 27, "§0§l"+inventoryGameName+" 도우미");

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
		meta.setDisplayName("§7- §c승리조건 §7-");
		List<String> loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7최대한 오래 살아남으세요.");
		loreList.add("§7순위가 높을수록 더 많은 점수를 받습니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_gameHelper.setItem(10, item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c진행방식 §7-");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7게임이 시작되면 비행기로 이동됩니다.");
		loreList.add("§71분안에 비행기에서 낙하해야하며 1분이 지나면");
		loreList.add("§7자동으로 낙하 하게됩니다.");
		loreList.add("§7게임시작 1분이 지날시에 무기를 뽑을 수 있는 뽑기권을 받습니다.");
		loreList.add("§7이 무기를 이용하여 특별한 무기를 뽑아 다른 플레이어를");
		loreList.add("§7처치하고 1위를 노려보세요!");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_gameHelper.setItem(11, item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c파밍 §7-");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7맵 곳곳에 아이템이 떨어져있습니다.");
		loreList.add("§7이 아이템들을 모아 자신을 더욱 강화하세요!");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_gameHelper.setItem(12, item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c자기장 §7-");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7플레이 시간이 지날수록 맵 구역을 제한하기 위한");
		loreList.add("§7자기장이 활성화됩니다.");
		loreList.add("§7제한된 자기장 구역 밖에서는 데미지를 입습니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_gameHelper.setItem(13, item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c보급 §7-");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7일정시간마다 보급품이 생성됩니다.");
		loreList.add("§7보급품은 발광되어 표시됩니다.");
		loreList.add("§7보급품에서는 무기상자와 전용 아이템들을 얻을 수 있습니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_gameHelper.setItem(14, item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c주의사항 §7-");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7서로 팀을 맺는 것은 불가능합니다.");
		loreList.add("§7아이템 교환, 살려주기 등등 가능하지만");
		loreList.add("§72명이서 1명을 지속적으로 집중 공격하는 것은");
		loreList.add("§7규칙에 위반됩니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_gameHelper.setItem(16, item);

		event = new EventHandlerRWW(server, this);
		// 이 플러그인에 이벤트 적용
		server.getServer().getPluginManager().registerEvents(event, server);
		
		
		
		//////무기 생성
		makeWeapons();
		
		makefarmingItems();
		
		makeSupplyItems();
		
		allWeapon.putAll(specialWeaponMap);
		
		allWeapon.put(item_parachute.getDisplayName(), item_parachute);
		allWeapon.put(item_grenade.getDisplayName(), item_grenade);
		allWeapon.put(item_flashbang.getDisplayName(), item_flashbang);
		
		item_getRandomWeapon = new ItemStack(Material.CHEST, 1);
		meta = item_getRandomWeapon.getItemMeta();
		meta.setDisplayName("§f[ §b무기 상자 §f]");
		//loreList = new ArrayList<String>();
		item_getRandomWeapon.setItemMeta(meta);
		
		item_finder = new ItemStack(Material.COMPASS, 1);
		meta = item_finder.getItemMeta();
		meta.setDisplayName("§f[ §b자기장 탐색기 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§e이 나침반은 항상");
		loreList.add("§e자기장의 중심지를 가르킨다.");
		loreList.add("");
		meta.setLore(loreList);
		item_finder.setItemMeta(meta);
		
		item_noSound = new ItemStack(Material.GOLDEN_CARROT, 1);
		meta = item_noSound.getItemMeta();
		meta.setDisplayName("§f[ §b귀마개 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§e비행기 소리를 듣지 않습니다.");
		loreList.add("");
		meta.setLore(loreList);
		item_noSound.setItemMeta(meta);
		
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
		} else if (ingame) {
			if(canSpectate && loc_spectate != null) {
				if(!existPlayer(p)) return;
				MyUtility.allClear(p);
				p.sendMessage(server.ms_alert + "이미 게임이 시작되었습니다. 관전장소로 이동되며 관전 전용 채팅을 사용합니다.");
				try {
					String tName = ingamePlayer.get(MyUtility.getRandom(0, ingamePlayer.size()-1));
					Player t = Bukkit.getPlayer(tName);
					if(existPlayer(t)) {
						p.teleport(t.getLocation(), TeleportCause.PLUGIN);	
					}
				}catch(Exception e) {
					p.teleport(loc_spectate, TeleportCause.PLUGIN);	
				}
				server.specList.put(p.getName(), this);
				p.setGameMode(GameMode.SPECTATOR);
				p.setFlying(true);
			} else {
				p.sendMessage(server.ms_alert + "이미 게임이 시작되었습니다."); 
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
			p.teleport(loc_Join, TeleportCause.PLUGIN);
			sendMessage(server.ms_alert + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " 님이 참여했습니다. " + ChatColor.RESET
					+ "[ " + ChatColor.GREEN + ingamePlayer.size() + " / "+ minPlayer + ChatColor.RESET + " ]");
			TitleAPI.sendTitle(p, 10, 70, 30,ChatColor.RED+""+disPlayGameName);
			sendSound(Sound.BLOCK_NOTE_PLING, 2.0f, 1.0f);
			if(lobbyStart) {
				if(startSch.schTime > 1)
					BarAPI.setMessage(p, ChatColor.GREEN+"게임 시작까지", startSch.schTime);
			} else {
				if ((ingamePlayer.size() >= minPlayer)) {
					startCount();
				} 
			}
			setRankMap(p.getName());
		}
	}
	
	@Override
	public void setRankMap(String pName) {
		PlayerData data = server.egDM.getPlayerData(pName);
		String rank = "나무";
		if(data != null) {
			MinigameData gameData = data.getGameData("RandomWeaponWar");
			if(gameData != null) {
				rank = gameData.getRankName();	
			}
		}
		rankMap.put(pName, rank);
	}

	@Override
	public void startGame() {
		if(ingamePlayer.size() < minPlayer) {
			for(String pName : ingamePlayer) {
				Player t = Bukkit.getPlayer(pName);
				server.spawn(t);
				t.sendMessage(ms+"최소 시작인원이 부족하여 시작이 취소됐습니다.");
			}
			endGame(false);
			return;
		}
		server.egCM.broadCast(server.ms_alert + ChatColor.RED+ChatColor
				.BOLD+ disPlayGameName + ChatColor.GRAY + "이 시작 되었습니다!");
		initGame();
		ingame = true;
		gameStep = 1;
		/////////////// 오프닝
		
		sendSound(Sound.BLOCK_NOTE_BELL, 1.0f, 1.0f);
		updateSidebar();
		
		for(Location l : loc_helidoors) {
			Block b = l.getBlock();
			b.setType(Material.STAINED_GLASS);
			b.setData((byte)15);
		}
		
		EGScheduler sch = new EGScheduler(this);
		sch.schTime = 0;
		sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				if(sch.schTime < ingamePlayer.size()) {
					String pName = ingamePlayer.get(sch.schTime);
					Player p = Bukkit.getPlayer(pName);
					if(existPlayer(p)) {
						p.teleport(loc_Helicopter, TeleportCause.PLUGIN);
						p.closeInventory();
						MyUtility.allClear(p); 
						rwwPlayerMap.put(p.getName(), new RwwPlayer(p));
						WorldBorderAPI.removeBorder(p);
						p.getInventory().addItem(item_noSound);
						p.getInventory().setItem(8, helpItem);
						supplyShulkerMap.put(pName, new ArrayList<EntityShulker>());
					}
					sch.schTime++;
				}else {
					sch.cancelTask(true);
				}
			}
		}, 60l, 4l);
		
		EGScheduler fallCount = new EGScheduler(this);
		fallCount.schTime = 15;
		fallCount.schTime2 = 0;
		fallCount.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				if(fallCount.schTime == 15) {
					gameStep = 2;
					floatItems();
					for (Entity entity : Bukkit.getWorld("world").getEntities()) {
						if (entity instanceof Item || entity instanceof Arrow) {
							if (entity.getLocation().distance(loc_Join) < 700)
								entity.remove();
						}
					}
				}
				if(fallCount.schTime > 0) {
					sendTitle("§6§l"+fallCount.schTime,"§e낙하 시작까지", 30);
					sendSound(Sound.BLOCK_NOTE_PLING);
					fallCount.schTime--;
				}else {
					fallCount.cancelTask(true);
					gameStep = 3;
					for(Location l : loc_helidoors) {
						l.getBlock().setType(Material.AIR);
					}
					sendTitle("§6§l문 열림!","§e비행기 밖으로 낙하하세요!!!", 80);
					sendSound(Sound.BLOCK_IRON_DOOR_OPEN);
					for (String pName : ingamePlayer) {
						Player p = Bukkit.getPlayer(pName);
						if(existPlayer(p)){
							p.setGameMode(GameMode.ADVENTURE);
							MyUtility.setMaxHealth(p, 100);
							MyUtility.healUp(p);
							MyUtility.allClear(p); 
							MyUtility.attackDelay(p, false);
							healItemSchMap.put(p.getName(), new EGScheduler(rww));
						}
					}
					giveBaseItem();
				}
				
			}
		}, 150l, 20l);
		
		EGScheduler heliSound = new EGScheduler(this);
		heliSound.schTime = 240;
		heliSound.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				if(heliSound.schTime-- > 0) {
					int rn = MyUtility.getRandom(0, 1);
					if(rn == 0) {
						for(String tName : ingamePlayer) {
							if(noSoundList.contains(tName)) continue;
							Player t = Bukkit.getPlayer(tName);
							if(existPlayer(t)) {
								if(t.getLocation().distance(loc_Helicopter) < 25)
								t.playSound(t.getLocation(), Sound.ENTITY_BAT_LOOP, 1.0f ,0.1f);
							}
						}
					}else if(rn == 1){
						for(String tName : ingamePlayer) {
							if(noSoundList.contains(tName)) continue;
							Player t = Bukkit.getPlayer(tName);
							if(existPlayer(t)) {
								if(t.getLocation().distance(loc_Helicopter) < 25) {								
									t.playSound(t.getLocation(), Sound.ENTITY_ENDERDRAGON_FLAP, 1.0f ,0.1f);
								}
							}
						}
					}
					for(String tName : ingamePlayer) {
						Player t = Bukkit.getPlayer(tName);
						if(existPlayer(t)) {
							if(t.getLocation().distance(loc_Helicopter) < 25) {
								if(!noSoundList.contains(tName))
								t.playSound(t.getLocation(), Sound.ENTITY_MINECART_RIDING, 1.0f ,0.1f);	
							}else{
								t.setFlying(false);
								t.setAllowFlight(false);
							}						
						}
					}
				}else {
					heliSound.cancelTask(true);
					for(String tName : ingamePlayer) {
						Player t = Bukkit.getPlayer(tName);
						if(existPlayer(t)) {
							if(t.getLocation().getY() > 210) {
								TitleAPI.sendFullTitle(t, 10, 70, 10, "§c§l강제 낙하", "§e§l너무 안뛰어내리셔서 강제로 낙하됩니다.");
								t.teleport(t.getLocation().add(0, -10, 0), TeleportCause.PLUGIN);
							}
							t.sendMessage(ms+"곧 무기 상자가 지급됩니다.");
							sendTitle("","§e동맹하여 같이 싸우는 플레이는 절대 금지입니다!", 120);
						}
					}
					if(gameName.equalsIgnoreCase("randomWeaponWar")) {
						berlinScheduler();
					}else if(gameName.equalsIgnoreCase("randomWeaponWar2")){
						NederlandsScheduler();
					}
				}
			}
		}, 0l, 5l);
		
	}
	
	//////////////////
	
	public void floatItems() {
		List<Location> tmpItemLoc = new ArrayList<Location>(loc_items);
		List<ItemStack> farmItems = new ArrayList<ItemStack>(farmingItemsMap.keySet());
		
		if(farmingItemsMap.size() == 0) return;
		EGScheduler sch = new EGScheduler(this);
		sch.schTime = 0;
		sch.schTime2 = 0;
		
		double multiple = loc_items.size()/100;
		
		sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				if(sch.schTime == farmItems.size()) {
					sch.cancelTask(true);
					Bukkit.getLogger().info(sch.schTime2+"개의 파밍템 생성");
					return;
				}
				ItemStack item = farmItems.get(sch.schTime);
				int dropPercent = farmingItemsMap.get(item);
				int loopCnt = (int)(dropPercent * multiple);
				for(int i = 0; i < loopCnt; i++) {
					if(tmpItemLoc.size() <= 0) return;
					Location loc = tmpItemLoc.get(MyUtility.getRandom(0, tmpItemLoc.size()-1));
					tmpItemLoc.remove(loc);
					
					final Hologram hologram = HologramsAPI.createHologram(server, loc);
					ItemLine itemLine = hologram.appendItemLine(item);

					itemLine.setPickupHandler(new PickupHandler() {	

						@Override
						public void onPickup(Player player) {
										
							// Play a sound
							player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1F, 1F);
										
							player.getInventory().addItem(itemLine.getItemStack());									
										
							// Delete the hologram
							hologram.delete();
										
						}
					});
					
					itemHolograms.add(hologram);
					
					sch.schTime2 += 1;
				}	
				sch.schTime++;
			}
		}, 0l, 2l);
	}
	
	public void updateSidebar() {
		if(!ingame) return;
		textList.clear();
		
		SidebarString blank = new SidebarString("");
		textList.add(blank);
		SidebarString line = new SidebarString("§e남은인원 §f: §a"+ingamePlayer.size()+"명");
		textList.add(line);
		textList.add(blank);
		line = new SidebarString("§e현재 자기장 레벨 §f: §a"+limiterLv);
		textList.add(line);
		textList.add(blank);
		line = new SidebarString("§e현재 자기장 반경 §f: §a"+mainSch.schTime2+"m");
		textList.add(line);
		textList.add(blank);
		line = new SidebarString("§e게임 진행 시간 §f: "+(mainSch.schTime)+"초");
		textList.add(line);
		textList.add(blank);
		
		rwwSidebar.setEntries(textList);
		rwwSidebar.update();
		
		/*for(String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p)) 
				rwwSidebar.showTo(p);
		}*/
	}
	
	public void berlinScheduler() {
		
		schList.add(mainSch);
		mainSch.schTime = 0;
		mainSch.schTime2 = 300;
		limiterRadius = 300;	
		
		mainSch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				if(!ingame) mainSch.cancelTask(false);
				mainSch.schTime++;
				
				if(applyBorder) {
					if(mainSch.schTime2 > limiterRadius) {
						mainSch.schTime2--;
						if(limiterLv == 1) mainSch.schTime2--;
						sendPulsingBorder(mainSch.schTime2*2);
					}else if(mainSch.schTime2 <= limiterRadius){
						sendBorder(limiterRadius*2);
						applyBorder = false;
					}
				}
				
				if(doDamage) {
					for (String pName : ingamePlayer) {
						try {
							Player p = Bukkit.getPlayer(pName);
							if(existPlayer(p)){
								Location pl = p.getLocation();
								if(Math.abs(limiterCenter.getX() - pl.getX()) > mainSch.schTime2 
										|| Math.abs(limiterCenter.getZ() - pl.getZ()) > mainSch.schTime2) {
									p.damage(limiterLv*2);
									ActionBarAPI.sendActionBar(p, "§c§l제한구역에 있기에 피해를 받습니다.", 30);
								}
								//Bukkit.getLogger().info(Math.abs(limiterCenter.getX() - pl.getX())+", "+Math.abs(limiterCenter.getZ() - pl.getZ())+" | "+mainSch.schTime2);
							}
						}catch(Exception e) {
							
						}						
					}				
				}
					
				if(mainSch.schTime == 1) {
					gameStep = 4;
					for (String pName : ingamePlayer) {
						Player p = Bukkit.getPlayer(pName);
						if(existPlayer(p)){
							p.getInventory().addItem(item_getRandomWeapon);
							p.playSound(p.getLocation(), Sound.ENTITY_EGG_THROW, 1.0F, 0.1F);
							TitleAPI.sendFullTitle(p, 10, 70, 10, "§a§l무기뽑기", "§e§l상자를 우클릭하여 무기를 뽑으세요!.");
							p.sendMessage(ms+"무적 시간이 끝났습니다. 낙하산이 해제됩니다.");
							Block b = p.getLocation().getBlock().getRelative(0, -1, 0);
							if(b != null && b.getType() == Material.AIR) {
								b.setType(Material.WEB);
								Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
									public void run() {
										b.setType(Material.AIR);
										p.setFlying(false);
										p.setAllowFlight(false);
									}
								}, 10l);
							}
							p.setFlying(false);
							p.setAllowFlight(false);
							p.getInventory().remove(item_parachute.getType());
							
							rwwSidebar.showTo(p);
						}
					}
				}
				
				if(mainSch.schTime == 90) {
					limiterCenter = getRandomLocation(loc_Join, 75);
					
					for(String tName : ingamePlayer) {
						Player t = Bukkit.getPlayer(tName);
						if(existPlayer(t)) {
							t.sendMessage("\n"+ms+"이번 게임의 자기장 위치가 설정됐습니다.\n"
									+ ms+"중심지 좌표 : §a"+limiterCenter.getBlockX()+", "+limiterCenter.getBlockZ()+"§6 | 제한 반경 : §a"+limiterRadius+"m\n"
											+ ms+"1분후 맵 구역이 제한됩니다.");
							t.playSound(t.getLocation(), Sound.BLOCK_METAL_BREAK, 1.0f, 1.0f);							
							t.setCompassTarget(limiterCenter);
						}
					}
					
					
				}
							
				if(mainSch.schTime == 120) {
					createSupplyChest(90);
				}
				
				if(mainSch.schTime == 150) {
					limiterLv = 1;
					mainSch.schTime2 = 300;
					limiterRadius = 100;
					applyBorder = true;
					doDamage = true;
					sendMessage("\n"+ms+"맵 구역이 제한되고 있습니다!\n");
					sendSound(Sound.BLOCK_METAL_BREAK);
				}
				
				//보급
				if(mainSch.schTime == 200) {
					createSupplyChest(70);
				}
				
				if(mainSch.schTime == 270) {					
					sendMessage("\n"+ms+"1분후 맵 구역이 제한됩니다.\n"
							+ ms+"중심지 좌표 : §a"+limiterCenter.getBlockX()+", "+limiterCenter.getBlockZ()+"§6 | 제한 반경 : §a"+limiterRadius+"m\n");
					sendSound(Sound.BLOCK_METAL_BREAK);
				}
				if(mainSch.schTime == 330) {
					limiterLv = 2;
					limiterRadius = 75;
					applyBorder = true;
					sendMessage("\n"+ms+"맵 구역이 제한되고 있습니다!\n");
					sendSound(Sound.BLOCK_METAL_BREAK);
				}
				
				//보급
				if(mainSch.schTime == 350) {
					createSupplyChest(50);
				}
				
				if(mainSch.schTime == 410) {					
					sendMessage("\n"+ms+"1분후 맵 구역이 제한됩니다.\n"
							+ ms+"중심지 좌표 : §a"+limiterCenter.getBlockX()+", "+limiterCenter.getBlockZ()+"§6 | 제한 반경 : §a"+limiterRadius+"m\n");
					sendSound(Sound.BLOCK_METAL_BREAK);
				}
				if(mainSch.schTime == 470) {
					limiterLv = 3;
					limiterRadius = 50;
					applyBorder = true;
					sendMessage("\n"+ms+"맵 구역이 제한되고 있습니다!\n");
					sendSound(Sound.BLOCK_METAL_BREAK);
				}
				
				//보급
				if(mainSch.schTime == 530) {
					createSupplyChest(35);
				}
				
				if(mainSch.schTime == 600) {
					sendMessage("\n"+ms+"1분후 맵 구역이 제한됩니다.\n"
							+ ms+"중심지 좌표 : §a"+limiterCenter.getBlockX()+", "+limiterCenter.getBlockZ()+"§6 | 제한 반경 : §a"+limiterRadius+"m\n");
					sendSound(Sound.BLOCK_METAL_BREAK);
				}
				if(mainSch.schTime == 660) {
					limiterLv = 4;
					limiterRadius = 25;
					applyBorder = true;
					sendMessage("\n"+ms+"맵 구역이 제한되고 있습니다!\n");
					sendSound(Sound.BLOCK_METAL_BREAK);
				}
				
				//보급
				if(mainSch.schTime == 700) {
					createSupplyChest(17);
				}
				
				if(mainSch.schTime == 760) {					
					sendMessage("\n"+ms+"1분후 맵 구역이 제한됩니다.\n"
							+ ms+"중심지 좌표 : §a"+limiterCenter.getBlockX()+", "+limiterCenter.getBlockZ()+"§6 | 제한 반경 : §a"+limiterRadius+"m\n");
					sendSound(Sound.BLOCK_METAL_BREAK);
				}
				if(mainSch.schTime == 820) {
					limiterLv = 5;
					limiterRadius = 10;
					applyBorder = true;
					sendMessage("\n"+ms+"맵 구역이 제한되고 있습니다!\n");
					sendSound(Sound.BLOCK_METAL_BREAK);
				}
				if(mainSch.schTime == 900) {					
					sendMessage("\n"+ms+"1분후 맵 구역이 제한됩니다.\n"
							+ ms+"중심지 좌표 : §a"+limiterCenter.getBlockX()+", "+limiterCenter.getBlockZ()+"§6 | 제한 반경 : §a"+limiterRadius+"m\n");
					sendSound(Sound.BLOCK_METAL_BREAK);
				}
				if(mainSch.schTime == 960) {
					limiterLv = 6;
					limiterRadius = 0;
					applyBorder = true;
					sendMessage("\n"+ms+"맵 구역이 제한되고 있습니다!\n");
					sendSound(Sound.BLOCK_METAL_BREAK);
				}
				
				updateSidebar();
				
			}
		}, 200l, 20l);
		
	}
	
	public void NederlandsScheduler() {
		
		schList.add(mainSch);
		mainSch.schTime = 0;
		mainSch.schTime2 = 150;
		limiterRadius = 150;	
		
		mainSch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				mainSch.schTime++;
				
				if(applyBorder) {
					if(mainSch.schTime2 > limiterRadius) {
						mainSch.schTime2--;
						if(limiterLv == 1) mainSch.schTime2 -= 2;
						sendPulsingBorder(mainSch.schTime2*2);
					}else if(mainSch.schTime2 <= limiterRadius){
						sendBorder(limiterRadius*2);
						applyBorder = false;
					}
				}
				
				if(doDamage) {
					for (String pName : ingamePlayer) {
						try {
							Player p = Bukkit.getPlayer(pName);
							if(existPlayer(p)){
								Location pl = p.getLocation();
								if(Math.abs(limiterCenter.getX() - pl.getX()) > mainSch.schTime2 
										|| Math.abs(limiterCenter.getZ() - pl.getZ()) > mainSch.schTime2) {
									p.damage(limiterLv*2);
									ActionBarAPI.sendActionBar(p, "§c§l제한구역에 있기에 피해를 받습니다.", 30);
								}
								//Bukkit.getLogger().info(Math.abs(limiterCenter.getX() - pl.getX())+", "+Math.abs(limiterCenter.getZ() - pl.getZ())+" | "+mainSch.schTime2);
							}
						}catch(Exception e) {
							
						}						
					}
					
				}
					
				if(mainSch.schTime == 1) {
					gameStep = 4;
					for (String pName : ingamePlayer) {
						Player p = Bukkit.getPlayer(pName);
						if(existPlayer(p)){
							p.getInventory().addItem(item_getRandomWeapon);
							p.playSound(p.getLocation(), Sound.ENTITY_EGG_THROW, 1.0F, 0.1F);
							TitleAPI.sendFullTitle(p, 10, 70, 10, "§a§l무기뽑기", "§e§l상자를 우클릭하여 무기를 뽑으세요!.");
							p.sendMessage(ms+"무적 시간이 끝났습니다. 낙하산이 해제됩니다.");
							Block b = p.getLocation().getBlock().getRelative(0, -1, 0);
							if(b != null && b.getType() == Material.AIR) {
								b.setType(Material.WEB);
								Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
									public void run() {
										b.setType(Material.AIR);
										p.setFlying(false);
										p.setAllowFlight(false);
									}
								}, 10l);
							}
							p.setFlying(false);
							p.setAllowFlight(false);
							p.getInventory().remove(item_parachute.getType());
							
							rwwSidebar.showTo(p);
						}
					}
				}
				
				if(mainSch.schTime == 50) {
					limiterCenter = getRandomLocation(loc_Join, 75);
					
					sendMessage("\n"+ms+"이번 게임의 자기장 위치가 설정됐습니다.\n"
							+ ms+"중심지 좌표 : §a"+limiterCenter.getBlockX()+", "+limiterCenter.getBlockZ()+"§6 | 제한 반경 : §a"+limiterRadius+"m\n"
									+ ms+"1분후 맵 구역이 제한됩니다.");
					sendSound(Sound.BLOCK_METAL_BREAK);
				}
							
				if(mainSch.schTime == 90) {
					createSupplyChest(100);
				}
				
				if(mainSch.schTime == 110) {
					limiterLv = 1;
					mainSch.schTime2 = 200;
					limiterRadius = 120;
					applyBorder = true;
					doDamage = true;
					sendMessage("\n"+ms+"맵 구역이 제한되고 있습니다!\n");
					sendSound(Sound.BLOCK_METAL_BREAK);
				}
				
				//보급
				if(mainSch.schTime == 160) {
					createSupplyChest(70);
				}
				
				if(mainSch.schTime == 200) {					
					sendMessage("\n"+ms+"1분후 맵 구역이 제한됩니다.\n"
							+ ms+"중심지 좌표 : §a"+limiterCenter.getBlockX()+", "+limiterCenter.getBlockZ()+"§6 | 제한 반경 : §a"+limiterRadius+"m\n");
					sendSound(Sound.BLOCK_METAL_BREAK);
				}
				if(mainSch.schTime == 260) {
					limiterLv = 2;
					limiterRadius = 80;
					applyBorder = true;
					sendMessage("\n"+ms+"맵 구역이 제한되고 있습니다!\n");
					sendSound(Sound.BLOCK_METAL_BREAK);
				}
				
				//보급
				if(mainSch.schTime == 300) {
					createSupplyChest(40);
				}
				
				if(mainSch.schTime == 360) {					
					sendMessage("\n"+ms+"1분후 맵 구역이 제한됩니다.\n"
							+ ms+"중심지 좌표 : §a"+limiterCenter.getBlockX()+", "+limiterCenter.getBlockZ()+"§6 | 제한 반경 : §a"+limiterRadius+"m\n");
					sendSound(Sound.BLOCK_METAL_BREAK);
				}
				if(mainSch.schTime == 420) {
					limiterLv = 3;
					limiterRadius = 40;
					applyBorder = true;
					sendMessage("\n"+ms+"맵 구역이 제한되고 있습니다!\n");
					sendSound(Sound.BLOCK_METAL_BREAK);
				}
				
				//보급
				if(mainSch.schTime == 450) {
					createSupplyChest(20);
				}
				
				if(mainSch.schTime == 500) {
					sendMessage("\n"+ms+"1분후 맵 구역이 제한됩니다.\n"
							+ ms+"중심지 좌표 : §a"+limiterCenter.getBlockX()+", "+limiterCenter.getBlockZ()+"§6 | 제한 반경 : §a"+limiterRadius+"m\n");
					sendSound(Sound.BLOCK_METAL_BREAK);
				}
				if(mainSch.schTime == 560) {
					limiterLv = 4;
					limiterRadius = 20;
					applyBorder = true;
					sendMessage("\n"+ms+"맵 구역이 제한되고 있습니다!\n");
					sendSound(Sound.BLOCK_METAL_BREAK);
				}
				
				//보급
				if(mainSch.schTime == 580) {
					createSupplyChest(10);
				}
				
				if(mainSch.schTime == 600) {					
					sendMessage("\n"+ms+"1분후 맵 구역이 제한됩니다.\n"
							+ ms+"중심지 좌표 : §a"+limiterCenter.getBlockX()+", "+limiterCenter.getBlockZ()+"§6 | 제한 반경 : §a"+limiterRadius+"m\n");
					sendSound(Sound.BLOCK_METAL_BREAK);
				}
				if(mainSch.schTime == 660) {
					limiterLv = 5;
					limiterRadius = 10;
					applyBorder = true;
					sendMessage("\n"+ms+"맵 구역이 제한되고 있습니다!\n");
					sendSound(Sound.BLOCK_METAL_BREAK);
				}
				if(mainSch.schTime == 700) {					
					sendMessage("\n"+ms+"1분후 맵 구역이 제한됩니다.\n"
							+ ms+"중심지 좌표 : §a"+limiterCenter.getBlockX()+", "+limiterCenter.getBlockZ()+"§6 | 제한 반경 : §a"+limiterRadius+"m\n");
					sendSound(Sound.BLOCK_METAL_BREAK);
				}
				if(mainSch.schTime == 760) {
					limiterLv = 6;
					limiterRadius = 0;
					applyBorder = true;
					sendMessage("\n"+ms+"맵 구역이 제한되고 있습니다!\n");
					sendSound(Sound.BLOCK_METAL_BREAK);
				}
				
				updateSidebar();
				
			}
		}, 200l, 20l);
		
	}
	
	public void sendPulsingBorder(int size) {
		for(String tName : ingamePlayer) {
			Player t = Bukkit.getPlayer(tName);
			if(existPlayer(t)) {
				WorldBorderAPI.sendPulsingBorder(server, t, false, false, limiterCenter.getBlockX(), limiterCenter.getBlockZ(), size, limiterLv*2);
				t.playSound(t.getLocation(), Sound.BLOCK_NOTE_HAT, 0.5f, 1.0f);
			}
		}
	}
	
	public void sendBorder(int size) {
		for(String tName : ingamePlayer) {
			Player t = Bukkit.getPlayer(tName);
			if(existPlayer(t)) {
				WorldBorderAPI.sendBorder(t, false, false, limiterCenter.getBlockX(), limiterCenter.getBlockZ(), size, limiterLv*2);
			}
		}
	}
	
	public Location createSupplyChest(int radius) {
		
		Location resultLoc = loc_Join;
		
		for(int loopCnt = 0; loopCnt < 50; loopCnt++) {
			int rToX = MyUtility.getRandom(0, radius);
			if(MyUtility.getRandom(0, 1) == 0) {
				rToX *= -1;
			}
			int rToZ = MyUtility.getRandom(0, radius);
			if(MyUtility.getRandom(0, 1) == 0) {
				rToZ *= -1;
			}
			
			Location tl = null;
			
			if(limiterCenter != null) {
				tl = limiterCenter.clone().add(rToX, -1, rToZ);
			}else {
				tl = loc_Join.clone().add(rToX, -1, rToZ);
			}
			
			Location tl_backup = tl.clone();
			
			Location tmpL = MyUtility.getTopLocation(tl, 150);
			if(tmpL == null || tmpL.getY() < 3) tmpL = tl_backup;
			tmpL.add(0,1,0);
			if(tmpL.getY() < loc_Join.getY()-2) {
				continue;
			}else {
				loopCnt = 100;
			}
			Material before = tmpL.getBlock().getType();
			tmpL.getBlock().setType(Material.CHEST);
			Location gl = tmpL.clone().add(0.5, -1, 0.5);
			for(String tName : ingamePlayer) {
				Player t = Bukkit.getPlayer(tName);
				if(existPlayer(t)) {
					t.sendMessage("\n"+ms+"보급상자가 생성되었습니다!\n"
							+ms+"§6좌표 : X: §a"+tmpL.getBlockX()+" §6Y: §a"+tmpL.getBlockY()+" §6Z: §a"+tmpL.getBlockZ()
							+"\n§e당신과 보급상자와의 거리  : §a"+((int)tmpL.distance(t.getLocation()))+"m");
				}
				EntityShulker shulker = MyUtility.sendGlowingBlock(server, t, gl);
				List<EntityShulker> shulkerList = supplyShulkerMap.get(tName);
				if(shulkerList != null) shulkerList.add(shulker);
			}
			sendSound(Sound.BLOCK_ENDERCHEST_OPEN);
			if(tmpL.getBlock().getType() == Material.CHEST) {
				Chest chest = (Chest) tmpL.getBlock().getState();
				chest.update();
				chest.getBlockInventory().addItem(item_getRandomWeapon);
				chest.getBlockInventory().addItem(supplyItems.get(MyUtility.getRandom(0, supplyItems.size()-1)));
				chest.getBlockInventory().addItem(supplyItems.get(4));
			} 
			supplyLocMap.put(tmpL, before);
			Block upB = tmpL.getBlock().getRelative(0, 1, 0);
			if(upB.getType() != Material.AIR) {
				Material type = upB.getType();
				supplyLocMap.put(upB.getLocation(), type);
				upB.setType(Material.AIR);
			}
			
			Block downB = tmpL.getBlock().getRelative(0, -1, 0);
			Material type = downB.getType();
			supplyLocMap.put(downB.getLocation(), type);
			downB.setType(Material.DIAMOND_BLOCK);
			
			resultLoc = tmpL;
			break;
		}
		return resultLoc;
	}
	
	public Location createSupplyChest(Location l) {
		
		Material before = l.getBlock().getType();
		l.getBlock().setType(Material.CHEST);
		Location gl = l.clone().add(0.5, -1, 0.5);
		for (String tName : ingamePlayer) {
			Player t = Bukkit.getPlayer(tName);
			if (existPlayer(t)) {
				t.sendMessage("\n" + ms + "보급상자가 생성되었습니다!\n" + ms + "§6좌표 : X: §a" + l.getBlockX() + " §6Y: §a"
						+ l.getBlockY() + " §6Z: §a" + l.getBlockZ() + "\n§e당신과 보급상자와의 거리  : §a"
						+ ((int) l.distance(t.getLocation())) + "m");
			}
			EntityShulker shulker = MyUtility.sendGlowingBlock(server, t, gl);
			List<EntityShulker> shulkerList = supplyShulkerMap.get(tName);
			if(shulkerList != null) shulkerList.add(shulker);
		}
		sendSound(Sound.BLOCK_ENDERCHEST_OPEN);
		if (l.getBlock().getType() == Material.CHEST) {
			Chest chest = (Chest) l.getBlock().getState();
			chest.update();
			chest.getBlockInventory().addItem(item_getRandomWeapon);
			chest.getBlockInventory().addItem(supplyItems.get(MyUtility.getRandom(0, supplyItems.size() - 1)));
			chest.getBlockInventory().addItem(supplyItems.get(4));
		}
		supplyLocMap.put(l, before);
		Block upB = l.getBlock().getRelative(0, 1, 0);
		if (upB.getType() != Material.AIR) {
			Material type = upB.getType();
			supplyLocMap.put(upB.getLocation(), type);
			upB.setType(Material.AIR);
		}
		
		Block downB = l.getBlock().getRelative(0, -1, 0);
		Material type = downB.getType();
		supplyLocMap.put(downB.getLocation(), type);
		downB.setType(Material.DIAMOND_BLOCK);

		return l;
	}
	
	public void callSupply(Player p, Location l) {
		EGScheduler sch = new EGScheduler(this);
		sch.schTime = 10;
		sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				if(sch.schTime > 0) {
					sch.schTime--;
					l.getWorld().playSound(l, Sound.ENTITY_MINECART_RIDING, 1.0f, 0.5f);
					l.getWorld().playSound(l, Sound.ENTITY_ENDERDRAGON_FLAP, 1.0f, 0.5f);		
				}else {
					sch.cancelTask(true);
					createSupplyChest(l);
				}
			}
		}, 15l, 10l);
	}
	
	
	public Location getRandomLocation(Location baseLoc, int radius) {
		int rToX = MyUtility.getRandom(0, radius);
		if(MyUtility.getRandom(0, 1) == 0) {
			rToX *= -1;
		}
		int rToZ = MyUtility.getRandom(0, radius);
		if(MyUtility.getRandom(0, 1) == 0) {
			rToZ *= -1;
		}
		
		Block b = baseLoc.getBlock().getRelative(rToX, 0, rToZ);
		Location bl = b.getLocation();
		bl.setY(0);
		return 	bl;
	}
	
	public void getRandomWeapon(Player p) {
		String weaponName = tmpWeaponList.get(MyUtility.getRandom(0, tmpWeaponList.size()-1));
		tmpWeaponList.remove(weaponName);
		SpecialWeapon weapon = specialWeaponMap.get(weaponName);
		p.getInventory().addItem(weapon);
		p.sendMessage(ms+weaponName+" 무기를 뽑았습니다.");
		p.getWorld().playSound(p.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0f, 1.0f);
	}

	public void initGame() {
		lobbyStart = false;
		ending = false;
		gameStep = 0;
		//스케쥴
		schList.clear();
		tmpWeaponList.clear();
		tmpWeaponList.addAll(specialWeaponMap.keySet());
		
		for(Location l : supplyLocMap.keySet()) {
			Material type = supplyLocMap.get(l);
			l.getBlock().setType(type);
		}
		
		int cnt = 0;
		for(Hologram holo : itemHolograms) {
			holo.delete();
			cnt++;
		}
		Bukkit.getLogger().info(cnt+"개의 파밍템 삭제");
		
		itemHolograms.clear();	
		supplyLocMap.clear();
		limiterLv = 0;
		limiterCenter = null;
		limiterRadius = 600;
		applyBorder = false;
		doDamage = false;	
		healItemSchMap.clear();
		mainSch.schTime = 0;
		rwwPlayerMap.clear();
		noSoundList.clear();
		
		clearClickMap();
	}

	public boolean loadGameData() {
		boolean ret = true;
		loc_Join = loadLocation(gameName, "JoinPos");
		loc_Helicopter = loadLocation(gameName, "Helicopter");
		
		int doorPosCnt = 0;
		try {
			File file = new File(server.getDataFolder().getPath() + "/" + gameName + "/Location", "location.yml");
			FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
			doorPosCnt = fileConfig.getInt("doorPosCnt");
		} catch (Exception e) {

		}
		loc_helidoors.clear();
		for (int num = 1; num <= doorPosCnt; num++) {
			loc_helidoors.add(loadLocation(gameName, "heliDoorPos" + num));
		}
		if (loc_helidoors.size() == 0) {
			server.egPM.printLog(ms + "[" + disPlayGameName + "] 비행기 문 지점이 설정되지 않았습니다.");
			ret = false;
		}
		if (loc_Join == null) {
			server.egPM.printLog(ms + "[" + disPlayGameName + "] 게임 시작 대기 지점이 설정되지 않았습니다.");
			ret = false;
		}else {
			loc_spectate = loc_Join.clone();
		}
		if (loc_Helicopter == null) {
			server.egPM.printLog(ms + "[" + disPlayGameName + "] 낙하 대기 지점이 설정되지 않았습니다.");
			ret = false;
		}
		int itemPosCnt = 0;
		try {
			File file = new File(server.getDataFolder().getPath() + "/" + gameName + "/Location", "location.yml");
			FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
			itemPosCnt = fileConfig.getInt("itemPosCnt");
		} catch (Exception e) {

		}
		loc_items.clear();
		for (int num = 1; num <= itemPosCnt; num++) {
			loc_items.add(loadLocation(gameName, "itemPos" + num));
		}
		if (loc_items.size() == 0) {
			server.egPM.printLog(ms + "[" + disPlayGameName + "] 아이템 지점이 설정되지 않았습니다.");
			ret = false;
		}

		
		if(ret == true) {
			server.egPM.printLog("[" + disPlayGameName + "] 설정 이상 없음");
			doneSetting = ret;
		}
		
		return ret;
	}
	
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
							}  else if (cmd[3].equalsIgnoreCase("Helicopter")) {
								saveLocation(gameName, "Helicopter", p.getLocation());
								p.sendMessage("[" + disPlayGameName + "] " + p.getLocation()+" 설정완료");
							}  else if (cmd[3].equalsIgnoreCase("helidoor")) {
								Block b = p.getTargetBlock(null, 3);
								if(b == null) return;
								int doorPosCnt = loc_helidoors.size()+1;
								saveLocation(gameName, "heliDoorPos" + doorPosCnt, b.getLocation().add(0,-1,0));
								File file = new File(server.getDataFolder().getPath() + "/" + gameName + "/Location", "location.yml");
								FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
								fileConfig.set("doorPosCnt", doorPosCnt);
								try {
									fileConfig.save(file);
								} catch (IOException e) {
								}
								loadGameData();
								p.sendMessage(ms + "[" + disPlayGameName + "] " + doorPosCnt + " 번째 비행기 문 지점이 추가되었습니다.");
							}else if (cmd[3].equalsIgnoreCase("setItemLoc1")) {
								tmpItemPos1 = p.getLocation();
								p.sendMessage(ms+"/rww set lco setitemloc2 입력해서 저장 ㄱㄱ");
							} else if (cmd[3].equalsIgnoreCase("setItemLoc2")) {
								if (tmpItemPos1 == null)
									p.sendMessage(ms + "1번부터 지정 ㄱㄱ");
								else {
									tmpItemPos2 = p.getLocation();

									int cnt = 0;

									if (tmpItemPos1 == null || tmpItemPos2 == null)
										return;
									Location l1 = new Location(p.getWorld(), 0, 0, 0);
									Location l2 = new Location(p.getWorld(), 0, 0, 0);

									if (tmpItemPos1.getX() > tmpItemPos2.getX()) {
										l1.setX(tmpItemPos1.getX());
										l2.setX(tmpItemPos2.getX());
									} else {
										l1.setX(tmpItemPos2.getX());
										l2.setX(tmpItemPos1.getX());
									}

									if (tmpItemPos1.getY() > tmpItemPos2.getY()) {
										l1.setY(tmpItemPos1.getY());
										l2.setY(tmpItemPos2.getY());
									} else {
										l1.setY(tmpItemPos2.getY());
										l2.setY(tmpItemPos1.getY());
									}

									if (tmpItemPos1.getZ() > tmpItemPos2.getZ()) {
										l1.setZ(tmpItemPos1.getZ());
										l2.setZ(tmpItemPos2.getZ());
									} else {
										l1.setZ(tmpItemPos2.getZ());
										l2.setZ(tmpItemPos1.getZ());
									}

									tmpItemPos1 = l1;
									tmpItemPos2 = l2;

									if (tmpItemPos1 == null || tmpItemPos2 == null) {
										return;
									}

									loc_items.clear();

									int loc1_x = tmpItemPos1.getBlockX();
									int loc1_y = tmpItemPos1.getBlockY();
									int loc1_z = tmpItemPos1.getBlockZ();

									int loc2_x = tmpItemPos2.getBlockX();
									int loc2_y = tmpItemPos2.getBlockY();
									int loc2_z = tmpItemPos2.getBlockZ();

									for (int i1 = loc1_y; i1 >= loc2_y; i1--) {
										for (int i2 = loc1_z; i2 >= loc2_z; i2--) {
											for (int i3 = loc1_x; i3 >= loc2_x; i3--) {
												Location l = new Location(tmpItemPos1.getWorld(), i3, i1, i2);
												if (l.getBlock().getType() == Material.TRAPPED_CHEST) {
													cnt = cnt + 1;
													saveLocation(gameName, "itemPos" + cnt, l);
													File file = new File(server.getDataFolder().getPath() + "/"
															+ gameName + "/Location", "location.yml");
													FileConfiguration fileConfig = YamlConfiguration
															.loadConfiguration(file);
													fileConfig.set("itemPosCnt", cnt);
													try {
														fileConfig.save(file);
													} catch (IOException e) {
													}
												}
											}
										}
									}
									loadGameData();

									p.sendMessage(ms + cnt + "개의 아이템 지정 설정완료");
								}								
							} else {
								p.sendMessage("[" + disPlayGameName + "] " + cmd[3]+"은 올바르지 않은 인수");
							}  
							loadGameData();
						} else {
							p.sendMessage(ms+"인수를 입력해주세요.");
						}
					} else {
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" set loc join - 게임 시작 대기 지점 설정");
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" set loc helicopter - 낙하 대기 지점 설정");
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" set loc helidoor - 비행기 문 지점 설정");
					}
				} else {
					p.sendMessage("[" + disPlayGameName + "] " + cmdMain + " set loc- 게임 지점 설정");
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
			}  else if (cmd[1].equalsIgnoreCase("debug0")) {
				ingamePlayer.remove(p.getName());
				ingame = true;
				gameStep = 4;
				ingamePlayer.add(p.getName());
				server.playerList.put(p.getName(), "테스트");
				server.spawnList.remove(p.getName());
				healItemSchMap.put(p.getName(), new EGScheduler(this));
				MyUtility.setMaxHealth(p, 120);
				MyUtility.healUp(p);
				MyUtility.attackDelay(p, false);
				Collection<SpecialWeapon> collection = specialWeaponMap.values();
				
				int index = Integer.valueOf(cmd[2]);
				int i = 0;
				
				for(SpecialWeapon weapon : collection) {
					
					p.sendMessage(weapon.getDisplayName());
					if(i == index) {
						p.getInventory().addItem(weapon);
						break;
					}
					i++;
				}
			}else if(cmd[1].equalsIgnoreCase("debug1")) {
				p.getInventory().addItem(item_parachute);
			}else if(cmd[1].equalsIgnoreCase("debug2")) {
				p.teleport(createSupplyChest(Integer.valueOf(cmd[2])), TeleportCause.PLUGIN);
			}else if(cmd[1].equalsIgnoreCase("debug3")) {
				Location loc = p.getLocation().add(0,1,0);
				
				List<ItemStack> items = new ArrayList<ItemStack>(farmingItemsMap.keySet());
				
				final Hologram hologram = HologramsAPI.createHologram(server, loc);
				ItemLine itemLine = hologram.appendItemLine(items.get(Integer.valueOf(cmd[2])));

				itemLine.setPickupHandler(new PickupHandler() {	

					@Override
					public void onPickup(Player player) {
									
						// Play a sound
						player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1F, 1F);
									
						player.getInventory().addItem(itemLine.getItemStack());									
									
						// Delete the hologram
						hologram.delete();
									
					}
				});
			}else if(cmd[1].equalsIgnoreCase("debug4")) {
				floatItems();
			}else if(cmd[1].equalsIgnoreCase("debug5")) {
				int cnt = 0;
				for(int i = 0; i < Integer.valueOf(cmd[2]); i++) {
					limiterCenter = getRandomLocation(loc_Join, 100);
					Location l = createSupplyChest(Integer.valueOf(cmd[3]));
					if(l.getBlockY() < 30) {
						cnt++;
					}
				}
				p.sendMessage(ms+cnt+"개");
			}
		} else {
			p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" join - 게임 참가");
			p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" quit - 게임 퇴장");
			p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" set - 게임 설정");
		}
	}

	public void gameQuitPlayer(Player p, boolean announce, boolean giveGold) {
		if (ingamePlayer.contains(p.getName())) {
			ingamePlayer.remove(p.getName());
			server.playerList.put(p.getName(), "로비");
			p.getInventory().clear();
			updateSidebar();
			server.egParkour.quit(p.getName());
			if(existPlayer(p)) {
				List<EntityShulker> shulkerList = supplyShulkerMap.get(p.getName());
				if(shulkerList != null) {
					for(EntityShulker shulker : shulkerList) {
						MyUtility.removeGlowingBlock(p, shulker);
					}
					supplyShulkerMap.remove(p.getName());
				}
			}				
			RwwPlayer rwwP = rwwPlayerMap.get(p.getName());
			if(rwwP != null) {
				rwwP.death += 1;
				rwwP.ranking = ingamePlayer.size()+1;
			}
			if (ingame) {
				if(giveGold) {
					if(announce) {
						sendMessage(ms + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " 님이 탈락하셨습니다.");
						sendTitle("", ChatColor.YELLOW+p.getName()+"님 탈락", 60);
						sendSound(Sound.BLOCK_NOTE_CHIME,1.5f, 2.0f);
					}
				} else {
					if(announce) {
						sendMessage(ms + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " 님이 퇴장하셨습니다.");
						sendSound(Sound.BLOCK_NOTE_GUITAR,1.5f, 2.0f);
					}
				}
				if(rwwSidebar != null) rwwSidebar.hideFrom(p);
				applyData(p.getName());
				if(ingamePlayer.size() <= 1) {
					setWinner();
				}
			} else {
				if(announce) {
					sendMessage(ms + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " 님이 퇴장하셨습니다. "
							+ ChatColor.RESET + "[ " + ChatColor.GREEN + ingamePlayer.size() + " / "+ minPlayer + ChatColor.RESET + " ]");
					sendSound(Sound.BLOCK_CLOTH_BREAK,1.5f, 1.5f);			
				}
				if(ingamePlayer.size() <= 0) {
					if(lobbyStart) {
						lobbyStart = false;
						startSch.cancelTask(true);
					}				
				}
			}
		}
	}
	
	public void applyData(String pName) {
		RwwPlayer rwwP = rwwPlayerMap.get(pName);
		if(rwwP == null)return;
		PlayerData playerData = server.egDM.getPlayerData(pName);
		if(playerData == null) return;
		MinigameData gameData = playerData.getGameData("RandomWeaponWar");
		if(gameData == null) return;
		if(!(gameData instanceof RwwData)) return;
		RwwData playerRwwData = (RwwData) gameData;
		playerRwwData.applyNewData(rwwP);
		playerRwwData.addPlaycount();
		
		//MMR 책정
			int mmr = calcMMR(rwwP);
			playerRwwData.setMMR(playerRwwData.getMMR() + mmr);
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p)) {
				p.sendMessage("");
				p.sendMessage(ms+"§f"+disPlayGameName+"§f의 게임 결과로 §a"+mmr+"§f점이 반영됐습니다.");
				p.sendMessage(ms+"§f킬수 : §a"+rwwP.kill+"킬");
				p.sendMessage(ms+"§f순위 : §a"+rwwP.ranking+"등");
				p.sendMessage("");
			}			
		playerRwwData.saveData();
	}
	
	
	public int calcMMR(RwwPlayer rwwP) {
		int rankMMR = 0;
		if(rwwP.ranking > rwwPlayerMap.size()/2) {
			rankMMR = (rwwP.ranking - (rwwPlayerMap.size()/2)) * (((int) rwwPlayerMap.size()/4))*-1;
			if(rankMMR > 0) {
				rankMMR = 0;
			}
			rankMMR -= 3;
		}else {
			rankMMR = (rwwPlayerMap.size()/2 - rwwP.ranking + 1) *(((int) rwwPlayerMap.size()/4)+1);	
			if(rankMMR < 0) {
				rankMMR = 0;
			}
			rankMMR += 3;
		}
		int firstMMR = 0;
		if(rwwP.first >= 1) {
			firstMMR += 20;
		}
		int killMMR = rwwP.kill * 4;
		
		int allMMR = rankMMR+firstMMR+killMMR;
		
		return allMMR;
	}
	
	public void giveBaseItem() {
		for(String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p)) {
				p.getInventory().addItem(item_parachute);
				p.getInventory().setItem(7, item_finder);
				p.getInventory().setItem(8, helpItem);
			}
		}
	}
	
	public void giveSupplyItem(Player p) {
		if(existPlayer(p)) {
			p.getInventory().addItem(item_getRandomWeapon);
			p.getInventory().addItem(supplyItems.get(MyUtility.getRandom(0, supplyItems.size()-1)));
			p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ENDERCHEST_OPEN, 3.0f, 0.25f);
		}
	}
	
	public void performence() {
		if(ingamePlayer.size() <= 0) return;
		EGScheduler sch = new EGScheduler(this);
		sch.schTime = 6;
		String pName = ingamePlayer.get(0);
		Player p = Bukkit.getPlayer(pName);
		if(!existPlayer(p)) return;
		Location l = p.getLocation();
		sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				if(sch.schTime> 0) {
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
				}else {
					sch.cancelTask(true);
				}
			}
		}, 0l, 20l);
	}

	private void setWinner() {
		if(ending) return;
		ending = true;
		gameStep = 5;
		mainSch.cancelTask(true);
		if(ingamePlayer.size() >= 1) {
			String winner = ingamePlayer.get(0);
			sendTitle("§6§l승리", ChatColor.GRAY + "1위 달성! 마지막까지 생존했습니다!", 70);
			sendSound(Sound.BLOCK_NOTE_BELL, 1.0f, 0.5f);
			
			RwwPlayer rwwP = rwwPlayerMap.get(winner);
			if(rwwP != null) {
				rwwP.first += 1;
				rwwP.ranking = 1;
			}
			
			for (Entity entity : Bukkit.getWorld("world").getEntities()) {
				if (entity instanceof Item || entity instanceof Arrow) {
					if (entity.getLocation().distance(loc_Join) < 700)
						entity.remove();
				}
			}
			
			performence();			
			
			try {
				Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
					public void run() {
						for (String pName : ingamePlayer) {
							Player p = Bukkit.getPlayer(pName);
							if(existPlayer(p)) {
								//server.egGM.giveGold(p.getName(), 40);
								sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
								server.egParkour.quit(p.getName());
								applyData(pName);
							}
						}
					}
				}, 120L);
			} catch (Exception e) {
				
			}
			try {
				Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
					public void run() {
						divideSpawn();
						endGame(false);
						server.egCM.broadCast(
							server.ms_alert + ChatColor.GRAY  + "§c"+winner+"§7님의 §a승리§7로 "+disPlayGameName+"이 종료 되었습니다.");
					}
				}, 200L);
			} catch(Exception e) {
					endGame(true);
			}
		} else {
			try {
				Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
					public void run() {
						divideSpawn();
						endGame(false);
						server.egCM.broadCast(
							server.ms_alert + ChatColor.GRAY  + "무승부로 §c랜덤 무기 전쟁§7가 종료 되었습니다.");
					}
				}, 200L);
			} catch(Exception e) {
					endGame(true);
			}
		}
		
	
	}

	public void endGame(boolean force) {
		if(force) {
			server.egCM.broadCast(server.ms_alert + "랜덤 무기 전쟁 게임이 강제 종료 되었습니다.");
		}
		divideSpawn();
		//Bukkit.getLogger().info("mainid: "+mainSch.schId);
		try {
			for(EGScheduler sch : schList) {
				sch.cancelTask(false);
			}
			
		}catch(Exception e) {
			
		}
		
		for(String tName : supplyShulkerMap.keySet()) {
			Player t = Bukkit.getPlayer(tName);
			if(!existPlayer(t)) continue;
			List<EntityShulker> shulkerList = supplyShulkerMap.get(tName);
			if(shulkerList == null) continue;
			for(EntityShulker shulker : shulkerList) {
				if(shulker == null) continue;
				MyUtility.removeGlowingBlock(t, shulker);
			}
		}
		
		supplyShulkerMap.clear();
		
		for(SpecialWeapon weapon : specialWeaponMap.values()) {
			weapon.onInit();
		}
		
		schList.clear();
		ending = false;
		ingamePlayer.clear();
		rankMap.clear();
		initGame();	
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {			
				for (Entity entity : Bukkit.getWorld("world").getEntities()) {
					if (entity instanceof Item || entity instanceof Arrow) {
						if (entity.getLocation().distance(loc_Join) < 700)
							entity.remove();
					}
				}
			}
		}, 40l);
		
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {			
				ingame = false;
			}
		}, 60l);
		
	}
	
	public void speedUp(Player p, float amt) {
		p.setWalkSpeed(p.getWalkSpeed()+amt);
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
		p.sendMessage(ms+"당신의 현재 이동 속도 -> "+MyUtility.getTwoRound(p.getWalkSpeed()*5));
	}
	
	public void healthUp(Player p, int amt) {
		MyUtility.setMaxHealth(p, p.getMaxHealth()+amt);
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
		p.sendMessage(ms+"당신의 현재 최대 체력 -> "+p.getMaxHealth());
	}
	
	public void makeSupplyItems() {
		
		//supplyItems.add(new ItemStack(Material.DIAMOND_SWORD, 1));
		supplyItems.add(new ItemStack(Material.DIAMOND_HELMET, 1));
		supplyItems.add(new ItemStack(Material.DIAMOND_CHESTPLATE, 1));
		supplyItems.add(new ItemStack(Material.DIAMOND_LEGGINGS, 1));
		supplyItems.add(new ItemStack(Material.DIAMOND_BOOTS, 1));
		
		ItemStack item = new ItemStack(Material.POTION, 1); 
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§f[ §c빨간 포션 §f]");
		List<String> loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§e사용시 모든 체력을 즉시 회복한다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		
		supplyItems.add(item);
		
		item = new ItemStack(Material.BEETROOT_SEEDS, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §c방벽(대) §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§e사용시 5초간 방어구 48을 얻는다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		
		supplyItems.add(item);
		
	}
	
	public void makefarmingItems() {
		farmingItemsMap.put(new ItemStack(Material.LEATHER_HELMET, 1), 3); //가죽셋 12%
		farmingItemsMap.put(new ItemStack(Material.LEATHER_CHESTPLATE, 1), 3);
		farmingItemsMap.put(new ItemStack(Material.LEATHER_LEGGINGS, 1), 3);
		farmingItemsMap.put(new ItemStack(Material.LEATHER_BOOTS, 1), 3);
		
		farmingItemsMap.put(new ItemStack(Material.IRON_HELMET, 1), 2); //철셋 8%
		farmingItemsMap.put(new ItemStack(Material.IRON_CHESTPLATE, 1), 2);
		farmingItemsMap.put(new ItemStack(Material.IRON_LEGGINGS, 1), 2);
		farmingItemsMap.put(new ItemStack(Material.IRON_BOOTS, 1), 2);
		
		farmingItemsMap.put(new ItemStack(Material.GOLD_HELMET, 1), 3);//금셋 12%
		farmingItemsMap.put(new ItemStack(Material.GOLD_CHESTPLATE, 1), 3);
		farmingItemsMap.put(new ItemStack(Material.GOLD_LEGGINGS, 1), 3);
		farmingItemsMap.put(new ItemStack(Material.GOLD_BOOTS, 1), 3);
		
		farmingItemsMap.put(new ItemStack(Material.CHAINMAIL_HELMET, 1), 2); //사슬셋 8%
		farmingItemsMap.put(new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1), 2);
		farmingItemsMap.put(new ItemStack(Material.CHAINMAIL_LEGGINGS, 1), 2);
		farmingItemsMap.put(new ItemStack(Material.CHAINMAIL_BOOTS, 1), 2);
		
		farmingItemsMap.put(new ItemStack(Material.POTION, 1, (short)16389), 4); //투척 포션 8%
		farmingItemsMap.put(new ItemStack(Material.POTION, 1, (short)16421), 4);
		
		farmingItemsMap.put(new ItemStack(Material.GOLD_SWORD, 1), 3);//검류
		farmingItemsMap.put(new ItemStack(Material.IRON_SWORD, 1), 3);
		
		farmingItemsMap.put(item_grenade, 4);//수류탄
		farmingItemsMap.put(item_flashbang, 4);//섬광탄
		
		ItemStack item = new ItemStack(Material.POTION, 1); //드링크류 9%
		PotionMeta ptMeta = (PotionMeta) item.getItemMeta();
		ptMeta.setDisplayName("§f[ §c비타민 §f]");
		List<String> loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§e사용시 체력 15를 서서히 회복한다.");
		loreList.add("§c적혀있는 포션 효과는 적용이 안됩니다.");
		loreList.add("");
		ptMeta.setBasePotionData(new PotionData(PotionType.JUMP));
		ptMeta.setLore(loreList);
		item.setItemMeta(ptMeta);
		
		farmingItemsMap.put(item, 4);
		
		item = new ItemStack(Material.POTION, 1);
		ptMeta = (PotionMeta) item.getItemMeta();
		ptMeta.setDisplayName("§f[ §c진통제 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§e사용시 체력 30을 서서히 회복한다.");
		loreList.add("§c적혀있는 포션 효과는 적용이 안됩니다.");
		loreList.add("");
		ptMeta.setBasePotionData(new PotionData(PotionType.NIGHT_VISION));
		ptMeta.setLore(loreList);
		item.setItemMeta(ptMeta);
		
		farmingItemsMap.put(item, 3);
		
		item = new ItemStack(Material.POTION, 1);
		ptMeta = (PotionMeta) item.getItemMeta();
		ptMeta.setDisplayName("§f[ §c아드레날린 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§e사용시 체력 70을 서서히 회복한다.");
		loreList.add("§c적혀있는 포션 효과는 적용이 안됩니다.");
		loreList.add("");
		ptMeta.setBasePotionData(new PotionData(PotionType.FIRE_RESISTANCE));
		ptMeta.setLore(loreList);
		item.setItemMeta(ptMeta);
		
		farmingItemsMap.put(item, 2);
		
		//////////////////////////////////////////////// 즉회
		item = new ItemStack(Material.CARPET, 1, (byte)4);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§f[ §c반창고 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§e사용시 체력 10를 즉시 회복한다.");
		loreList.add("§e단, 최대 체력의 75% 이상은 회복이 불가능하다.");
		loreList.add("");
		loreList.add("§b사용에 필요한 시간 : §a2초");
		loreList.add("§b쉬프트 + 좌클릭 - §a아이템 사용");
		loreList.add("§b쉬프트 떼기 - §a아이템 사용 취소");
		loreList.add("§c사용시 쉬프트를 누르고 있어야 사용됩니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		//item.addUnsafeEnchantment(Enchantment.LUCK, 0);
		
		farmingItemsMap.put(item, 5);
		
		item = new ItemStack(Material.LONG_GRASS, 1, (byte)1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §c붕대 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§e사용시 체력 20을 즉시 회복한다.");
		loreList.add("§e단, 최대 체력의 75% 이상은 회복이 불가능하다.");
		loreList.add("");
		loreList.add("§b사용에 필요한 시간 : §a3초");
		loreList.add("§b쉬프트 + 좌클릭 - §a아이템 사용");
		loreList.add("§b쉬프트 떼기 - §a아이템 사용 취소");
		loreList.add("§c사용시 쉬프트를 누르고 있어야 사용됩니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		//item.addUnsafeEnchantment(Enchantment.LUCK, 0);
		
		farmingItemsMap.put(item, 4);
		
		item = new ItemStack(Material.REDSTONE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §c수혈팩 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§e사용시 체력 40을 즉시 회복한다.");
		loreList.add("§e단, 최대 체력의 75% 이상은 회복이 불가능하다.");
		loreList.add("");
		loreList.add("§b사용에 필요한 시간 : §a3초");
		loreList.add("§b쉬프트 + 좌클릭 - §a아이템 사용");
		loreList.add("§b쉬프트 떼기 - §a아이템 사용 취소");
		loreList.add("§c사용시 쉬프트를 누르고 있어야 사용됩니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		//item.addUnsafeEnchantment(Enchantment.LUCK, 0);
		
		farmingItemsMap.put(item, 3);
		
		item = new ItemStack(Material.ENCHANTMENT_TABLE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §c메디킷 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§e사용시 체력 75를 즉시 회복한다.");
		loreList.add("§e단, 최대 체력의 75% 이상은 회복이 불가능하다.");
		loreList.add("");
		loreList.add("§b사용에 필요한 시간 : §a4초");
		loreList.add("§b쉬프트 + 좌클릭 - §a아이템 사용");
		loreList.add("§b쉬프트 떼기 - §a아이템 사용 취소");
		loreList.add("§c사용시 쉬프트를 누르고 있어야 사용됩니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		//item.addUnsafeEnchantment(Enchantment.LUCK, 0);
		
		farmingItemsMap.put(item, 3);
		
		//////////주문서
		
		item = new ItemStack(Material.RABBIT_FOOT, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §b이동 속도 주문서 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§e사용시 이동속도를");
		loreList.add("§e5%만큼 영구히 증가시킨다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		
		farmingItemsMap.put(item, 4);
		
		item = new ItemStack(Material.BEETROOT, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §b체력 주문서 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§e사용시 체력을 1만큼 영구히 증가시킨다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		
		farmingItemsMap.put(item, 4);
		
		/////방벽류
		item = new ItemStack(Material.PUMPKIN_SEEDS, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §c방벽(소) §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§e사용시 5초간 방어구 8을 얻는다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		
		farmingItemsMap.put(item, 3);
		
		item = new ItemStack(Material.MELON_SEEDS, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §c방벽(중) §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§e사용시 5초간 방어구 12를 얻는다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		
		farmingItemsMap.put(item, 2);
		
		item = new ItemStack(Material.RABBIT_HIDE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §c보급 요청 코드 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§e사용시 자신의 위치에 보급상자를 요청한다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		
		farmingItemsMap.put(item, 1);
		
	}
	
	public void makeWeapons() {

		SpecialWeapon weapon = new PathFinder(this); //1. 위치 탐색기
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new RandomSword(this); //2.천운 검
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new IronFeather(this); //3.그리폰의 깃털
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new Booster(this); //4.부스터 엔진
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new Levitator(this); //5.무중력 장치
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new HappyBowl(this); //6.축복 받은 그릇
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new LocationSaver(this); //7.위치 기록기
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new WizardRod(this); //8.마도사의 지팡이
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new WizardPowder(this); //9.마도사의 파우치
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new PoisonKnife(this); //10.독성 수리검
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new IcePrison(this); //11. 절대영도
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new TeleportStick(this); //12. 순간이동 막대기
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new ReflectorMirror(this); //13. 반사 거울
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new Invincibler(this); //14. 금강  성배
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new Blinder(this); //15. 말소성 덩어리
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new DeadRevive(this); //16. 죽은자의 소생
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new DNAChanger(this); //17. 세포 조작기
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new NoDebuff(this); //18. 천년 빗자루
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new RandomBuffer(this); //19. 이십사면체 주사위
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new StopWatch(this); //20. 스탑워치
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new WindShooter(this); //21. 장풍 부채
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new ForceTaker(this); //22. 강탈 장미
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new GodokSword(this); //23. 고독의 검
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new SnipeRifle(this); //24. 저격총
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new Machingun(this); //25. 속사기
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new NoGravityBow(this); //26. 매직 보우
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new GlowingDust(this); //27. 빛의 가루
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new SlimeJumper(this); //28. 말랑말랑한 구체
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new ParkourBook(this); //29. 파쿠르 교본서
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new DeadDefense(this); //30. 불사의 황금궤
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new IronHook(this); //31. 철제 갈고리
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new AirCannon(this); //32. 공기포
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new GravityController(this); //33. 반중력 조절기
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new DragonRelic(this); //34. 드래곤 랠릭
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new C4Bomber(this); //35. C4
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new FlickerBook(this); //36. 점멸 주문서
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new MurderSword(this); //37. 살인자의 칼
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new Spoon(this); //38. 숟가락
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new RoseKnife(this); //39. 장미칼
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new DragonSword(this); //40. 용광검
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new AiasShield(this); //41. 아이아스의 방패
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new Telephone512K(this); //42. 512k
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new FastBow(this); //43. 컴파인드 보우
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new MagnetWeapon(this); //44. 자석
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new WitherKnife(this); //45. 위더 수리검
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new OnePunch(this); //46. 핵펀치 글러브
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new LocationSwitcher(this); //47. 위치 변환기
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new RollerSkate(this); //48. 롤러 스케이트
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new ThunderAxe(this); //49. 라이트닝 엑스
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new WizardUltimate(this); //50. 불의 돌
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new AvoidAxe(this); //51. 회피 도끼
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new ThrowBomb(this); //52. 폭약탄
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new BigHammer(this); //53. 대망치
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new LocationHolder(this); //54. 차원 고정 장치
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new MoveToPlayerWeapon(this); //55. 무빙머신
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new EscapeWeapon(this); //56. 긴급 탈출 장치
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new DeathNote(this); //57. 데스노트
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new Stimulant(this); //58. 각성제
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new DiscoBangBang(this); //59. 디스코 팡팡
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new CreeperWeapon(this); //60. 자폭 
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new Nomeither(this); //61. 노미더
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new RandomMedic(this); //62. 구급 상자 
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new BurnOut(this); //63. 번아웃
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new Mirroring(this); //64. 미러링	
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new DrainStone(this); //65. 변환의 돌
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new BorrowHealth(this); //66. 미래조정기 
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new ShootFireball(this); //67. 화염구
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new OdinRod(this); //68. 오딘의 지팡이
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new RandomAxe(this); //69. 도박도끼
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
	}
	
	//방벽 관련
	public void giveAbsorp(Player p, int lv) {
		p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 2.0f);
		p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 100 ,lv));
	}
	
	
	//힐 관련
	public void slowHeal(Player p, int healAmt) {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EAT, 1.0f, 0.1f);
		EGScheduler sch = new EGScheduler(this);
		sch.schTime = healAmt;
		sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				if(!ingame || !existPlayer(p)) sch.cancelTask(true);
				if (sch.schTime-- > 0) {
					double amt = p.getHealth() + 1;
					if (amt > p.getMaxHealth()) {
						amt = p.getMaxHealth();
						sch.cancelTask(true);
					}
					if (!p.isDead())
						p.setHealth(amt);
				} else {
					sch.cancelTask(true);
				}
			}
		}, 0l, 10l);
	}
	
	
	public void useHealItem(Player p, ItemStack item,int time, int healAmt) {
		EGScheduler sch = healItemSchMap.get(p.getName());
		if(sch == null) return;
		sch.cancelTask(false);
		sch.schTime = time;
		sch.schTime2 = 0;
		sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				if(!p.isSneaking() || !p.getInventory().getItemInMainHand().equals(item)) {
					sch.cancelTask(false);
					p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_SMALL_FALL, 1.0f, 2.0f);
					ActionBarAPI.sendActionBar(p, "");
					return;
				}
				if(sch.schTime > 0) {
					if(sch.schTime2 == 0) {
						sch.schTime2 = 1;
					} else {
						sch.schTime--;
						sch.schTime2 = 0;
					}
					p.getWorld().playSound(p.getLocation(), Sound.BLOCK_SNOW_BREAK, 1.0F, 0.5F);
					String symbol = "■";
					String symbolBlank = "□";
					String barStr = "§b§l사용중: ";
					int amtPerSec = 24/time;
					
					for(int i = 0; i < time - sch.schTime; i++) {
						for(int j = 0; j < amtPerSec; j++) {
							barStr += symbol;
						}
					}
					
					for(int i = 0; i < sch.schTime; i++) {
						for(int j = 0; j < amtPerSec; j++) {
							barStr += symbolBlank;
						}
					}
					
					ActionBarAPI.sendActionBar(p, barStr);
				}else {
					sch.cancelTask(false);
					ActionBarAPI.sendActionBar(p, "");
					if(fastHeal(p, healAmt)) {
						removeItem(p, item.getType(), 1);
					}else {
						p.sendMessage(ms+"§c이미 최대 체력의 75% 이상입니다.");
					}
				}
			}
		}, 0l, 10l);
		
	}
	
	public boolean fastHeal(Player p, int healAmt) {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EAT, 1.0f, 0.1f);
		double max = (p.getMaxHealth()/4)*3;
		double newHealth = p.getHealth()+healAmt;
		if(newHealth > max) newHealth = max;
		if(!p.isDead() && p.getHealth() < max) {
			p.setHealth(newHealth);
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_BURP, 1.0f, 0.1f);
			return true;
		}else {
			return false;
		}
	}

	//////////////// 이벤트
	public class EventHandlerRWW extends EGEventHandler {

		private RandomWeaponWar game;

		public EventHandlerRWW(EGServer server, RandomWeaponWar game) {
			super(server);
			this.game = game;
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
				if(server.cmdSpawn.contains(cmd[0])) {
					gameQuitPlayer(p, true, false);
				}
			}
		}

		@EventHandler(priority = EventPriority.HIGH)
		public void onEntityDamage(EntityDamageEvent e) {
			if (e.getEntityType() == EntityType.PLAYER) {
				Player p = (Player) e.getEntity();
				DamageCause cause = e.getCause();			
				
				/*if(testMap.containsKey(p.getName())) {
					long lastDamageTime = testMap.get(p.getName());					
					testMap.put(p.getName(), System.currentTimeMillis());
					Bukkit.broadcastMessage(System.currentTimeMillis() - lastDamageTime+"ms, hp: "+(int)p.getHealth());
				}else {
					testMap.put(p.getName(), System.currentTimeMillis());
				}*/
				
				if (ingamePlayer.contains(p.getName())) {
					if(!ingame) {
						e.setCancelled(true);
				        if (cause.equals(DamageCause.VOID)) { //대기실 뎀없음, 텔포		           
				            p.teleport(loc_Join, TeleportCause.PLUGIN);
				        }
					} else {
						if(gameStep != 4) {
							if(gameStep == 3) {
								if(cause.equals(DamageCause.FALL)){
									//if(p.getFallDistance() > 50) {					
										//e.setDamage(10);
										//p.sendMessage("§6낙하산을 펼치지 않아 낙사 피해를 받았습니다.");
									//}else {
										e.setCancelled(true);
									//}
								}else {
									e.setCancelled(true);
								}
							}else {
								e.setCancelled(true);
								if (cause.equals(DamageCause.VOID)) { //허공뎀 = 탈락
						            p.teleport(loc_Join, TeleportCause.PLUGIN);
								}
							}	
				        }else {
				        	if (cause.equals(DamageCause.VOID)) { //허공뎀 = 탈락
					            gameQuitPlayer(p, true, true);
					            server.spawn(p);
							}else {
								String itemName = getHeldMainItemName(p);
								if(itemName != null && !itemName.equalsIgnoreCase("meta없음")) {
									SpecialWeapon weapon = allWeapon.get(itemName);
									if(weapon != null) {
										weapon.onEntityDamaged(e);
									}
								}	
								String offItemName = getHeldOffItemName(p);
								if(offItemName != null && !offItemName.equalsIgnoreCase("meta없음")) {
									SpecialWeapon weapon = allWeapon.get(offItemName);
									if(weapon != null) {
										weapon.onEntityDamaged(e);
									}
								}	
							}				        	
				        }
					}
				}
			}
		}
		
		@EventHandler(priority = EventPriority.LOW)
		public void onEntityDamagedByEntity(EntityDamageByEntityEvent e) {
			if(e.getEntity() instanceof Player && ingame) { //게임시작했을때만
				
				Player player = (Player) e.getEntity();
				Player damager = null;

				if (!ingamePlayer.contains(player.getName())) return; //피해자가 겜에 참가중이지않으면 규칙 무시
				
				/*if(!checkClickDelay(player.getName())) {
					e.setCancelled(true);
					return;
				}*/
				
				
				boolean isProjectile = false;
				
				Arrow arrow = null;

				if (e.getDamager() instanceof Snowball) { //화살과 총알에대한 공격자 설정
					Snowball snowball = (Snowball) e.getDamager();
					if (snowball.getShooter() instanceof Player) {
						damager = (Player) snowball.getShooter();
					}
					isProjectile = true;
				}
				
				if (e.getDamager() instanceof Arrow) {
					arrow = (Arrow) e.getDamager();
					if (arrow.getShooter() instanceof Player) {
						damager = (Player) arrow.getShooter();
					}
					e.getDamager().remove();	
					isProjectile = true;
				}
				
				if (e.getDamager() instanceof Fireball) {
					Fireball fireball = (Fireball) e.getDamager();
					if (fireball.getShooter() instanceof Player) {
						damager = (Player) fireball.getShooter();
					}
					e.getDamager().remove();	
					isProjectile = true;
				}
				
				if (e.getDamager() instanceof Player)
					damager = (Player) e.getDamager();
				
				if (damager == null) //공격자 없으면 리턴
					return;
				
				if (!ingamePlayer.contains(damager.getName())) { //공격자가 겜에 참가중이지않으면 규칙 무시
					return;
				}
				
				if(!isProjectile && !checkVictimDelay(damager.getName(), player)) {
					e.setCancelled(true);
					//Bukkit.broadcastMessage("갠됨");
					return;
				}
				
				String damager_itemName = getHeldMainItemName(damager);
				if(damager_itemName != null && !damager_itemName.equalsIgnoreCase("meta없음")) {
					SpecialWeapon weapon = allWeapon.get(damager_itemName);
					if(weapon != null) {
						if(!isProjectile)e.setDamage(weapon.getCalcDamage());
						weapon.onHitPlayer(e, damager, player);
					}
				}
				
				String victim_itemName = getHeldMainItemName(player);
				if(victim_itemName != null && !victim_itemName.equalsIgnoreCase("meta없음")) {
					SpecialWeapon weapon = allWeapon.get(victim_itemName);
					if(weapon != null) {
						weapon.onHitted(e, damager, player);
					}
				}
				
				String victim_offItemName = getHeldOffItemName(player);
				if(victim_offItemName != null && !victim_offItemName.equalsIgnoreCase("meta없음")) {
					SpecialWeapon weapon = allWeapon.get(victim_offItemName);
					if(weapon != null) {
						weapon.onHitted(e, damager, player);
					}
				}
				
			}
		}

		@EventHandler
		public void onClickInventory(InventoryClickEvent e) {
			if (!(e.getWhoClicked() instanceof Player))
				return;
			Player p = (Player) e.getWhoClicked();
			if (e.getInventory().getTitle().equalsIgnoreCase("§0§l"+inventoryGameName+" 도우미")) {				
				e.setCancelled(true);
			if (!ingamePlayer.contains(p.getName()))
				return;
				/*if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}	
				gameHelper(p, e.getSlot());
			} else if (e.getInventory().getTitle().equalsIgnoreCase("§0§l게임설명")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}	
				p.closeInventory();
				gameHelperAbClick(p, e.getSlot());	*/
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
			//Action action = e.getAction();
			if (!ingamePlayer.contains(p.getName()))return;
		
			if (e.getClickedBlock() != null) {
				Material blockType = e.getClickedBlock().getType();
				if(blockType == Material.CHEST
					|| blockType == Material.TRAPPED_CHEST
					|| blockType == Material.FURNACE
					|| blockType == Material.DISPENSER
					|| blockType == Material.HOPPER
					|| blockType == Material.DROPPER
					|| blockType == Material.ENDER_CHEST
					|| blockType == Material.ITEM_FRAME
					|| blockType == Material.BREWING_STAND					
					) {
					e.setCancelled(true);
					if(blockType == Material.CHEST) {
						Block b = e.getClickedBlock();
						b.setType(Material.AIR);
						if(b.getRelative(0, -1, 0).getType() == Material.DIAMOND_BLOCK) {
							b.setType(Material.AIR);
						}
					}
				}
			}
			
			if (e.getItem() == null || e.getItem().getType() == Material.AIR) { //아이템을 안들고 우클릭했을때 리턴
				return;
			} else if (getHeldMainItemName(p).equalsIgnoreCase("§f[ §6게임 도우미 §f]")) {
				p.openInventory(inven_gameHelper);
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 2.0f);
			} else if (getHeldMainItemName(p).equalsIgnoreCase("§f[ §b무기 상자 §f]")) {
				e.setCancelled(true);
				getRandomWeapon(p);
				removeItem(p, e.getItem().getType(), 1);
			} else if (getHeldMainItemName(p).equalsIgnoreCase("§f[ §b이동 속도 주문서 §f]")) {
				speedUp(p, 0.01f);
				removeItem(p, e.getItem().getType(), 1);
			}else if (getHeldMainItemName(p).equalsIgnoreCase("§f[ §b체력 주문서 §f]")) {
				healthUp(p, 2);
				removeItem(p, e.getItem().getType(), 1);
			}else if (getHeldMainItemName(p).equalsIgnoreCase("§f[ §c방벽(소) §f]")) {
				giveAbsorp(p, 1);
				removeItem(p, e.getItem().getType(), 1);
			}else if (getHeldMainItemName(p).equalsIgnoreCase("§f[ §c방벽(중) §f]")) {
				giveAbsorp(p, 2);
				removeItem(p, e.getItem().getType(), 1);
			}else if (getHeldMainItemName(p).equalsIgnoreCase("§f[ §c방벽(대) §f]")) {
				giveAbsorp(p, 11);
				removeItem(p, e.getItem().getType(), 1);
			}else if (getHeldMainItemName(p).equalsIgnoreCase("§f[ §c반창고 §f]")) {
				useHealItem(p, e.getItem(), 1, 15);
			}else if (getHeldMainItemName(p).equalsIgnoreCase("§f[ §c붕대 §f]")) {
				useHealItem(p, e.getItem(), 2, 30);
			}else if (getHeldMainItemName(p).equalsIgnoreCase("§f[ §c수혈팩 §f]")) {
				useHealItem(p, e.getItem(), 2, 50);
			}else if (getHeldMainItemName(p).equalsIgnoreCase("§f[ §c메디킷 §f]")) {
				useHealItem(p, e.getItem(), 3, 90);
			}else if (getHeldMainItemName(p).equalsIgnoreCase("§f[ §c보급 요청 코드 §f]")) {
				callSupply(p, p.getLocation().getBlock().getLocation());
				removeItem(p, e.getItem().getType(), 1);
			}else if (getHeldMainItemName(p).equalsIgnoreCase("§f[ §b귀마개 §f]")) {
				noSoundList.add(p.getName());
				removeItem(p, e.getItem().getType(), 1);
			}else {
				String itemName = getHeldMainItemName(p);
				if(itemName != null && !itemName.equalsIgnoreCase("meta없음")) {
					SpecialWeapon weapon = allWeapon.get(itemName);
					if(weapon != null) {
						weapon.onInteract(e);
					}
				}
			}
		}
		
		@EventHandler
		public void onFoodLevelChange(FoodLevelChangeEvent e){
			if(e.getEntity() instanceof Player){
				Player p = (Player) e.getEntity();
				if(ingamePlayer.contains(p.getName())) e.setFoodLevel(20);
			}
		}
		
		@EventHandler
		public void onPlayerDeath(PlayerDeathEvent e) {
			if (e.getEntity() instanceof Player) {
				Player p = (Player) e.getEntity();
				if (ingamePlayer.contains(p.getName())) {
					LivingEntity k = p.getKiller();
					if (k instanceof Player) {
						Player killer = (Player) k;
						RwwPlayer rwwP = rwwPlayerMap.get(killer.getName());
						if (rwwP != null)
							rwwP.kill += 1;
					}
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
				server.egCM.sendMessagesToStringList(ingamePlayer, p, "§f[ §e"+rankMap.get(p.getName())+ " §f] §7"+p.getName()+" >> " +e.getMessage(),false);
				sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 1.5f);		
			}
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
		
		@EventHandler
		public void onPlayerMove(PlayerMoveEvent e) {
			Player p = (Player) e.getPlayer();
			if(ingamePlayer.contains(p.getName()) && ingame) {
				///위치이동 아니면 캔슬
				String itemName = getHeldMainItemName(p);
				if(itemName != null && !itemName.equalsIgnoreCase("meta없음")) {
					SpecialWeapon weapon = allWeapon.get(itemName);
					if(weapon != null) {
						weapon.onPlayerMove(e);
					}
				}
				String offItemName = getHeldOffItemName(p);
				if(offItemName != null && !offItemName.equalsIgnoreCase("meta없음")) {
					SpecialWeapon weapon = allWeapon.get(offItemName);
					if(weapon != null) {
						weapon.onPlayerMove(e);
					}
				}
			}
		}
		
		@EventHandler
		public void onRegainHealth(EntityRegainHealthEvent e) {
			if(!(e.getEntity() instanceof Player)) return;
			Player p = (Player) e.getEntity();
			
			if(!ingamePlayer.contains(p.getName())) return;
			if (e.getRegainReason() == RegainReason.SATIATED || e.getRegainReason() == RegainReason.REGEN)
				e.setCancelled(true);
			
			String itemName = getHeldMainItemName(p);
			if(itemName != null && !itemName.equalsIgnoreCase("meta없음")) {
				SpecialWeapon weapon = allWeapon.get(itemName);
				if(weapon != null) {
					weapon.onRegainHealth(e);
				}
			}
			String offItemName = getHeldOffItemName(p);
			if(offItemName != null && !offItemName.equalsIgnoreCase("meta없음")) {
				SpecialWeapon weapon = allWeapon.get(offItemName);
				if(weapon != null) {
					weapon.onRegainHealth(e);
				}
			}
		}
		
		@EventHandler
		public void onPlayerShotBow(EntityShootBowEvent e) {
			if (((e.getEntity() instanceof Player)) && ingame) {
				Player p = (Player) e.getEntity();
				
				if(!ingamePlayer.contains(p.getName())) return;
				
				String itemName = getHeldMainItemName(p);
				if(itemName != null && !itemName.equalsIgnoreCase("meta없음")) {
					SpecialWeapon weapon = allWeapon.get(itemName);
					if(weapon != null) {
						weapon.onPlayerShotBow(e);
					}
				}
			}
		}
		
		@EventHandler
		public void onPlayerItemConsume(PlayerItemConsumeEvent e) {
			ItemStack item = e.getItem();
			Player p = e.getPlayer();
			
			if(!ingamePlayer.contains(p.getName())) return;
			//p.getInventory().getItemInMainHand().setType(Material.AIR);
			boolean remove = true;
			if(item.getType() == Material.POTION) {
				String s = item.getItemMeta().getDisplayName();
				if(s.equalsIgnoreCase("§f[ §c비타민 §f]")) {
					slowHeal(p, 15);
				} else if(s.equalsIgnoreCase("§f[ §c진통제 §f]")) {
					slowHeal(p, 30);
				} else if(s.equalsIgnoreCase("§f[ §c아드레날린 §f]")) { //보급템
					slowHeal(p, 70);
				} else if(s.equalsIgnoreCase("§f[ §c빨간 포션 §f]")) { //보급템
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EAT, 1.0f, 0.1f);
					p.setHealth(p.getMaxHealth());
				} 
			}
			if(remove) {
				item.setType(Material.AIR);
				e.setItem(item);
			}else {
				e.setCancelled(true);
				p.sendMessage(ms+"이미 체력이 75% 이상입니다.");
			}
			
		}
		
		@EventHandler
		public void onPlayerDropItem(PlayerDropItemEvent e) {
			
			Player p = e.getPlayer();
			
			if(!ingamePlayer.contains(p.getName())) return;
			
			if(gameStep != 4) e.setCancelled(true);
			if(e.getItemDrop().getItemStack().getType() == Material.WEB) {
				p.sendMessage(ms+"낙하산은 버릴 수 없습니다.");
				e.setCancelled(true);
			}
		}
		
		/*@EventHandler
		public void onHeldItem(PlayerItemHeldEvent e) {
			Player p = (Player) e.getPlayer();
			if(ingamePlayer.contains(p.getName()) && ingame) {
				int prevSlot = e.getPreviousSlot();
				int newSlot = e.getNewSlot();
				
				ItemStack prevItem = p.getInventory().getItem(prevSlot);
				ItemStack newItem = p.getInventory().getItem(newSlot);
				
				String prevItemName = null;
				String newItemName = null;
				
				if(prevItem != null) {
					if(prevItem.hasItemMeta()) {
						if(prevItem.getItemMeta().hasDisplayName()) {
							prevItemName = prevItem.getItemMeta().getDisplayName();
						}
					}
				}
				
				if(newItem != null) {
					if(newItem.hasItemMeta()) {
						if(newItem.getItemMeta().hasDisplayName()) {
							newItemName = newItem.getItemMeta().getDisplayName();
						}
					}
				}
				
				if(prevItemName != null && !prevItemName.equalsIgnoreCase("meta없음")) {
					SpecialWeapon weapon = allWeapon.get(prevItemName);
					if(weapon != null) {
						weapon.onRelease(p);
					}
				}
				
				if(newItem != null && !newItem.equalsIgnoreCase("meta없음")) {
					SpecialWeapon weapon = allWeapon.get(newItem);
					if(weapon != null) {
						weapon.onHeld(p);
					}
				}
			}
			
		}*/
	}
	
	///////////////////// 랜덤 무기 전쟁에 참가한 플레이어들 클래스
	public class RwwPlayer {
		
		public String playerName;
		public int death = 0;
		public int kill = 0;
		public int first = 0;
		public int ranking = maxPlayer;
		
		public RwwPlayer(Player p) {
			this.playerName = p.getName();
		}

	}
}
	
