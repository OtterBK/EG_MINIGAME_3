package EGServer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.WeatherType;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;

import com.coloredcarrot.api.sidebar.Sidebar;
import com.coloredcarrot.api.sidebar.SidebarString;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.connorlinfoot.titleapi.TitleAPI;
import com.nametagedit.plugin.NametagEdit;

import EGServer.ChatManager.ChatManager;
import EGServer.DataManger.DataManager;
import EGServer.DataManger.PlayerData;
import EGServer.DataManger.MinigameData.MinigameData;
import EGServer.ReportManager.ReportManager;
import EGServer.ServerManager.EGPluginManager;
import EGServer.ServerManager.EGScheduler;
import EGServer.WatchdogManager.WatchdogManager;
import Minigames.Minigame;
import Minigames.AvoidTheAnvil.AvoidTheAnvil;
import Minigames.BuildBattle.BuildBattle;
import Minigames.ColorMatch.ColorMatch;
import Minigames.DeathRun.DeathRun;
import Minigames.FindTheMurder.FindTheMurder;
import Minigames.FirstHit.FirstHit;
import Minigames.HeroesWar.HeroesWar;
import Minigames.HeroesWar.HeroesWar_Ulraf;
import Minigames.KingOfMine.KingOfMine;
import Minigames.ParkourRacing.ParkourRacing;
import Minigames.RandomWeaponWar.RandomWeaponWar;
import Minigames.Spleef.Spleef;
import Minigames.TNTBomber.TNTBomber;
import Minigames.TakeASeet.TakeASeet;
import Minigames.WarOfGod.WarOfGod;
import PacketManager.WrapperPlayServerEntityEffect;
import Utility.MyUtility;
import Utility.ParkourManager;
import Utility.RepairMap;
import Utility.SkullCreator;
import Utility.WorldBorderAPI;
import de.slikey.effectlib.EffectManager;
import me.confuser.barapi.BarAPI;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.mcplayhd.ping.Ping;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_12_R1.MinecraftServer;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldBorder;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldBorder.EnumWorldBorderAction;
import net.minecraft.server.v1_12_R1.WorldBorder;

public class EGServer extends JavaPlugin implements Listener {

	public List<String> noGameName = new ArrayList<String>();
	private PotionEffect speedPt = new PotionEffect(PotionEffectType.REGENERATION, 72000 ,1);
	
	///////////// 각종 매니저
	public EGPluginManager egPM;
	public ChatManager egCM;
	//public DataManager egGM;
	public DataManager egDM;
	public ReportManager egRM;
	public EffectManager egEM;;
	public WatchdogManager egWM;;
	public ParkourManager egParkour;
	public EGServer server;
	public EGServerGUI serverGUI;
	
	////////////// 로케이션
	public Location lobby;
	
	////////////////미니게임들
	public List<Minigame> minigames = new ArrayList<Minigame>();
	public FindTheMurder findTheMurder;
	public FindTheMurder findTheMurder2;
	public HeroesWar heroesWar;
	public HeroesWar heroesWar2;
	public HeroesWar_Ulraf heroesWarUlaf;
	public WarOfGod warOfGod;
	public WarOfGod warOfGod2;
	public WarOfGod warOfGod3;
	public ColorMatch colorMatch;
	public TNTBomber tntBomber;
	public Spleef spleef;
	public BuildBattle buildBattle;
	public DeathRun deathRun;
	public ParkourRacing parkourRacing;
	public TakeASeet takeASeet;
	public AvoidTheAnvil avoidTheAnvil;
	public RandomWeaponWar randomWeaponWar;
	public RandomWeaponWar randomWeaponWar2;
	public KingOfMine kingOfMine1;
	public KingOfMine kingOfMine2;
	public KingOfMine kingOfMine3;
	public KingOfMine kingOfMine4;
	public FirstHit firstHit;
	
	//////////////////
	public String ms_alert = "§7[ §aEG §7] §f";
	public String ms_imp = "§7[ §d알림 §7] §c";
	public HashMap<String, String> playerList = new HashMap<String, String>(); //닉넴, 플레이중인 행동, 기본값 - 로비
	public HashMap<String, String> cmdList = new HashMap<String, String>(30); //명령어, 필요권한
	public List<String> spawnList = new ArrayList<String>(50);
	public List<String> cmdSpawn = new ArrayList<String>(7);
	public HashMap<String, String> waitingPlayer = new HashMap<String, String>(50);
	public HashMap<String, Sidebar> sidebarMap = new HashMap<String, Sidebar>(100);
	public HashMap<String, Minigame> specList = new HashMap<String, Minigame>(20);
	public HashMap<String, Long> helpMeFromBlockCooldownMap = new HashMap<String, Long>(20);
	
	public HashMap<String, Long> block_AutoMouseMap = new HashMap<String, Long>(150);
	
	////명령어 관련
	public HashMap<String, Long> sayCooldown = new HashMap<String, Long>();
	public HashMap<String, Long> requestCooldown = new HashMap<String, Long>();
	private boolean allowEntitySpawn = false;
	private RepairMap testRepairMap;
	private boolean freeze = false;
	
	//메인 스케쥴
	public int mainSchId;
	public int tipTimer = 0;
	public int tipIndex = 0;
	public List<String> tipList = new ArrayList<String>(5);
	
	private List<FallingBlock> testfb = new ArrayList<FallingBlock>();
	private Vehicle testVehicle;
	
	///////무시할 블럭
	List<Material> ignoreBlockList = new ArrayList<Material>();
	
	//abs 초기화용
	private PotionEffect absInit = new PotionEffect(PotionEffectType.ABSORPTION, 10, 0);
	
	public void onEnable() {
		//////////////////////////////////
		this.getLogger().info("plugin start");
		server = this;
		loadConfig();
		
		//////////////////////////////////
		getServer().getPluginManager().registerEvents(new DefaultEventHandler(), this);
		setCmdList();
		///////////////// 매니저들
		egPM = new EGPluginManager(this);
		egCM = new ChatManager(server);
		//egGM = new DataManager(server);
		egDM = new DataManager(server);	
		egRM = new ReportManager(server);
		egEM = new EffectManager(server);
		egWM = new WatchdogManager(server);
		
		egParkour = new ParkourManager(server);
		serverGUI = new EGServerGUI(server);
		
		////// 미니게임
		findTheMurder = new FindTheMurder(server, "FindTheMurder", "§c§l살인자를 찾아라 §f§l1채널§7", "/ftm1");
		minigames.add(findTheMurder);
		findTheMurder2 = new FindTheMurder(server, "FindTheMurder2", "§c§l살인자를 찾아라 §f§l랭크 채널§7", "/ftm2");
		minigames.add(findTheMurder);
		
		heroesWar = new HeroesWar(server, "HeroesWar", "§c§l히어로즈워 §f§l1채널", "/hrw1");
		minigames.add(heroesWar);
		heroesWar2 = new HeroesWar(server, "HeroesWar2", "§c§l히어로즈워 §f§l2채널", "/hrw2");
		minigames.add(heroesWar2);
		heroesWarUlaf = new HeroesWar_Ulraf(server, "HeroesWarUlaf", "§c§l히어로즈워 §f§l원포인트", "/hrw3");
		minigames.add(heroesWarUlaf);
		
		warOfGod = new WarOfGod(server, "WarOfGod1","§c§l미니 신들의전쟁 §f§l1채널","/wog1", false);
		minigames.add(warOfGod);
		warOfGod2 = new WarOfGod(server, "WarOfGod2","§c§l미니 신들의전쟁 §f§l2채널","/wog2", false);	
		minigames.add(warOfGod2);
		warOfGod3 = new WarOfGod(server, "WarOfGod3","§c§l미니 신들의전쟁 §f§l랭크 채널","/wog3", true);
		minigames.add(warOfGod3);
		
		colorMatch = new ColorMatch(server);
		minigames.add(colorMatch);
		
		tntBomber = new TNTBomber(server);
		minigames.add(tntBomber);
		
		spleef = new Spleef(server, "/spf");
		minigames.add(spleef);
		
		buildBattle = new BuildBattle(server, "/bdb");
		minigames.add(buildBattle);
		
		deathRun = new DeathRun(server, "/dtr");
		minigames.add(deathRun);
		
		parkourRacing = new ParkourRacing(server);
		minigames.add(parkourRacing);
		
		takeASeet = new TakeASeet(server);
		minigames.add(takeASeet);
		
		avoidTheAnvil = new AvoidTheAnvil(server);
		minigames.add(avoidTheAnvil);
		
		randomWeaponWar = new RandomWeaponWar(server, "RandomWeaponWar", "§c§l랜덤 무기 전쟁 §f§l1채널§7", "/rww1");
		minigames.add(randomWeaponWar);
		
		randomWeaponWar2 = new RandomWeaponWar(server, "RandomWeaponWar2", "§c§l랜덤 무기 전쟁 §f§l2채널§7", "/rww2");
		minigames.add(randomWeaponWar2);
		
		kingOfMine1 = new KingOfMine(server, "KingOfMine1", "§c§l광물의 왕 §f§l1채널§7", "/kom1");
		minigames.add(kingOfMine1);

		kingOfMine2 = new KingOfMine(server, "KingOfMine2", "§c§l광물의 왕 §f§l2채널§7", "/kom2");
		minigames.add(kingOfMine2);
		
		kingOfMine3 = new KingOfMine(server, "KingOfMine3", "§c§l광물의 왕 §f§l3채널§7", "/kom3");
		minigames.add(kingOfMine3);
		
		kingOfMine4 = new KingOfMine(server, "KingOfMine4", "§c§l광물의 왕 §f§l4채널§7", "/kom4");
		minigames.add(kingOfMine4);
		
		firstHit = new FirstHit(server, "FirstHit", "§c§l선빵게임§7", "/fht");
		minigames.add(firstHit);
		
		////////////////////미니게임 채널설정
		
		///////////////////////
		String path = this.getDataFolder().getAbsolutePath();
		File file = new File(path);
		if (!file.exists()) file.mkdir();
		
		path = this.getDataFolder().getAbsolutePath()+"\\EGServer";
		file = new File(path);
		if (!file.exists()) file.mkdir();
		
		noGameName.add("로비"); noGameName.add("상점");
		cmdSpawn.add("/스폰"); cmdSpawn.add("/spawn"); cmdSpawn.add("/ㅅㅍ"); cmdSpawn.add("lobby");
		cmdSpawn.add("/넴주"); cmdSpawn.add("/SPAWN");
		
		tipList.add(ms_alert+"§f어디서든 스폰으로 이동하고 싶을땐 §c'/스폰' §f명령어를 사용해주세요.");
		tipList.add(ms_alert+"§f채팅 관련 신고는 §c'/신고' §f명령어를 사용해주세요.");
		//tipList.add(ms_alert+"§f블럭에 꼇을땐 §c'/꺼내줘' §f명령어를 사용해보세요. 빠져나올 수도 있습니다.");
		tipList.add(ms_alert+"§f각종 질문 사항은 §c'/문의' §f를 이용해주세요. 서버에 관리자가 접속해있으면 빠르게 답변해 드리겠습니다.");
		tipList.add(ms_alert+"§f실력보다 중요한 건 §c매너§f입니다.");
		tipList.add(ms_alert+"§f카페주소 : §chttps://cafe.naver.com/boli2");
		tipList.add(ms_alert+"§f디스코드 주소 : §chttps://discord.gg/QDzUwGn");
		tipList.add(ms_alert+"§f사용가능한 명령어 목록은 §c'/명령어' §f로 확인이 가능합니다.");
		tipList.add(ms_alert+"§f현재 §c오픈 베타테스트 §f진행중입니다.");
		tipList.add(ms_alert+"§fEG서버에 오신 걸 환영합니다.");
		
		mainScheduler();
		
		testRepairMap = new RepairMap(server, "testRepairMap");
		testRepairMap.loadData(getDataFolder().getPath()+"/Test");
		
		//int loadCnt = egDM.loadPlayerData();
		//egPM.printLog(ms_alert+loadCnt+" 개의 유저데이터 로드");
	}
	
	public void onDisable() {
		for(Minigame minigame : minigames) {
			minigame.endgame(false);
		}
		
		egDM.saveAllData();
		
		this.getLogger().info("plugin stop");
	}
	
	public void mainScheduler() {
		List<String> murder1Lore = new ArrayList<String>();
		List<String> murder2Lore = new ArrayList<String>();
		List<String> heroesWar1Lore = new ArrayList<String>();
		List<String> heroesWar2Lore = new ArrayList<String>();
		List<String> heroesWar3Lore = new ArrayList<String>();
		List<String> warOfGod1Lore = new ArrayList<String>();
		List<String> warOfGod2Lore = new ArrayList<String>();
		List<String> warOfGod3Lore = new ArrayList<String>();
		List<String> randomWeaponWar1Lore = new ArrayList<String>();
		List<String> randomWeaponWar2Lore = new ArrayList<String>();
		List<String> colorMatchLore = new ArrayList<String>();
		List<String> tntBomberLore = new ArrayList<String>();
		List<String> spleefLore = new ArrayList<String>();
		List<String> buildbattleLore = new ArrayList<String>();
		List<String> deathRunLore = new ArrayList<String>();
		List<String> takeASeetLore = new ArrayList<String>();
		List<String> avoidTheAnvilLore = new ArrayList<String>();
		List<String> kingOfMine1Lore = new ArrayList<String>();
		List<String> kingOfMine2Lore = new ArrayList<String>();
		List<String> kingOfMine3Lore = new ArrayList<String>();
		List<String> kingOfMine4Lore = new ArrayList<String>();
		List<String> firstHitLore = new ArrayList<String>();
		
		mainSchId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				
				murder1Lore.clear();
				murder1Lore.add("");
				murder1Lore.add("§7- 랭크가 기록되지 않는 채널입니다.");
				murder1Lore.add("");
				murder1Lore.add("§7  평균 플레이 시간 : 12분");
				murder1Lore.add("");
				
				murder2Lore.clear();
				murder2Lore.add("");
				murder2Lore.add("§7- 랭크가 기록되는 채널입니다.");
				murder2Lore.add("");
				murder2Lore.add("§7  평균 플레이 시간 : 12분");
				murder2Lore.add("");
				
				heroesWar1Lore.clear();
				heroesWar1Lore.add("");
				heroesWar1Lore.add("§7- 소규모 전장입니다.");
				heroesWar1Lore.add("§7  처음하시는 분들은  이 맵에서 기본적인 것을");
				heroesWar1Lore.add("§7  익히고 가시는걸 추천 드립니다.");
				heroesWar1Lore.add("");
				heroesWar1Lore.add("§7  평균 플레이 시간 : 20분");
				heroesWar1Lore.add("");
				
				heroesWar2Lore.clear();
				heroesWar2Lore.add("");
				heroesWar2Lore.add("§7- 대규모 전장입니다.");
				heroesWar2Lore.add("§7  맵이 넓기에 기본적인 룰을 이해하고");
				heroesWar2Lore.add("§7  플레이하시는 것을 추천 드립니다.");
				heroesWar2Lore.add("");
				heroesWar2Lore.add("§7  평균 플레이 시간 : 25분");
				heroesWar2Lore.add("");
				
				heroesWar3Lore.clear();
				heroesWar3Lore.add("");
				heroesWar3Lore.add("§7- 스킬을 연습해보기 좋은 모드입니다.");
				heroesWar3Lore.add("§7  [점령지 1개], [토템없음], [상점없음]룰을 사용하며");
				heroesWar3Lore.add("§7 궁극기는 쿨타임 방식이 아닌 충전 방식을 사용합니다.");
				heroesWar3Lore.add("§7 [중도 참여 가능] / [재접속시 팀 유지안됨]");
				heroesWar3Lore.add("");
				heroesWar3Lore.add("§7  평균 플레이 시간 : 15분");
				heroesWar3Lore.add("");
				
				warOfGod1Lore.clear();
				warOfGod1Lore.add("");
				warOfGod1Lore.add("§7- 기본 평지 스카이블럭입니다.");
				warOfGod1Lore.add("");
				
				warOfGod2Lore.clear();
				warOfGod2Lore.add("");
				warOfGod2Lore.add("§7- 지옥 컨셉의 스카이블럭입니다.");
				warOfGod2Lore.add("§7  난이도가 더 어렵습니다.");
				warOfGod2Lore.add("");
				
				warOfGod3Lore.clear();
				warOfGod3Lore.add("");
				warOfGod3Lore.add("§7- 그리스 컨셉의 대형맵입니다.");
				warOfGod3Lore.add("§7  게임 결과가 MMR에 반영됩니다.");
				warOfGod3Lore.add("");
				warOfGod2Lore.add("§7  평균 플레이 시간 : 25분");
				warOfGod3Lore.add("");
				
				randomWeaponWar1Lore.clear();
				randomWeaponWar1Lore.add("");
				randomWeaponWar1Lore.add("§7- '베를린' 맵에서 전투합니다.");
				randomWeaponWar1Lore.add("§7- MMR에 반영됩니다.");
				randomWeaponWar1Lore.add("");
				randomWeaponWar1Lore.add("§7  평균 플레이 시간 : 15분");
				randomWeaponWar1Lore.add("");
				
				randomWeaponWar2Lore.clear();
				randomWeaponWar2Lore.add("");
				randomWeaponWar2Lore.add("§7- '네덜란드' 맵에서 전투합니다.");
				randomWeaponWar2Lore.add("§7- MMR에 반영됩니다.");
				randomWeaponWar2Lore.add("");
				randomWeaponWar2Lore.add("§7  평균 플레이 시간 : 10분");
				randomWeaponWar2Lore.add("");
				
				colorMatchLore.clear();
				colorMatchLore.add("");
				colorMatchLore.add("§7- 계속해서 변하는 양털 블럭에서 순발력을 이용해");
				colorMatchLore.add("§7  끝까지 살아남아야하는 개인전 게임입니다.");
				colorMatchLore.add("");
				colorMatchLore.add("§7  평균 플레이 시간 : 5분");
				colorMatchLore.add("");
				colorMatchLore.add("§7  §7좌클릭: §b게임참가 §7| §7우클릭: §b게임설명");
				colorMatchLore.add("");
				
				tntBomberLore.clear();
				tntBomberLore.add("");
				tntBomberLore.add("§7- 폭탄을 가진 술래에게서 도망치거나");
				tntBomberLore.add("§7  폭탄을 다른 플레이어에게 전달하며");
				tntBomberLore.add("§7  마지막까지 살아남으면 승리합니다.");
				tntBomberLore.add("");
				tntBomberLore.add("§7  평균 플레이 시간 : 8분");
				tntBomberLore.add("");
				tntBomberLore.add("§7  §7좌클릭: §b게임참가 §7| §7우클릭: §b게임설명");
				tntBomberLore.add("");
				
				spleefLore.clear();
				spleefLore.add("");
				spleefLore.add("§7- 상대방 아래에 있는 눈 블럭을 파괴하고 떨어뜨리며");
				spleefLore.add("§7  탈락시켜 모든 경쟁자를 탈락시키세요!");
				spleefLore.add("");
				spleefLore.add("§7  평균 플레이 시간 : 5분");
				spleefLore.add("");
				spleefLore.add("§7  §7좌클릭: §b게임참가 §7| §7우클릭: §b게임설명");
				spleefLore.add("");
				
				buildbattleLore.clear();
				buildbattleLore.add("");
				buildbattleLore.add("§7- 주어지는 주제에 맞는 건축물이나 모형을 만들어");
				buildbattleLore.add("§7  누가 가장 표현을 잘 했는지 서로 평가하는 게임입니다.");
				buildbattleLore.add("");
				buildbattleLore.add("§7  평균 플레이 시간 : 7분");
				buildbattleLore.add("");
				buildbattleLore.add("§7  §7좌클릭: §b게임참가 §7| §7우클릭: §b게임설명");
				buildbattleLore.add("");
				
				deathRunLore.clear();
				deathRunLore.add("");
				deathRunLore.add("§7- 점차 사라지는 블럭들 위에서 최대한 오래");
				deathRunLore.add("§7  생존하여 최후의 1인이 되는게 목표입니다.");
				deathRunLore.add("");
				deathRunLore.add("§7  평균 플레이 시간 : 5분");
				deathRunLore.add("");
				deathRunLore.add("§7  §7좌클릭: §b게임참가 §7| §7우클릭: §b게임설명");
				deathRunLore.add("");
				
				takeASeetLore.clear();
				takeASeetLore.add("");
				takeASeetLore.add("§7- 살아남기 위해 다른 플레이어를 방해하며");
				takeASeetLore.add("§7  마인카트에 올라타야하는 게임입니다.");
				takeASeetLore.add("");
				takeASeetLore.add("§7  평균 플레이 시간 : 5분");
				takeASeetLore.add("");
				takeASeetLore.add("§7  §7좌클릭: §b게임참가 §7| §7우클릭: §b게임설명");
				takeASeetLore.add("");
				
				avoidTheAnvilLore.clear();
				avoidTheAnvilLore.add("");
				avoidTheAnvilLore.add("§7- 하늘에서 떨어지는 모루를 피해서");
				avoidTheAnvilLore.add("§7  끝까지 생존하세요!");
				avoidTheAnvilLore.add("");
				avoidTheAnvilLore.add("§7  평균 플레이 시간 : 5분");
				avoidTheAnvilLore.add("");
				avoidTheAnvilLore.add("§7  §7좌클릭: §b게임참가 §7| §7우클릭: §b게임설명");
				avoidTheAnvilLore.add("");
				
				kingOfMine1Lore.clear();
				kingOfMine1Lore.add("");
				kingOfMine1Lore.add("§7- 1채널입니다.");
				kingOfMine1Lore.add("");
				
				kingOfMine2Lore.clear();
				kingOfMine2Lore.add("");
				kingOfMine2Lore.add("§7- 2채널입니다.");
				kingOfMine2Lore.add("");

				kingOfMine3Lore.clear();
				kingOfMine3Lore.add("");
				kingOfMine3Lore.add("§7- 3채널입니다.");
				kingOfMine3Lore.add("");

				kingOfMine4Lore.clear();
				kingOfMine4Lore.add("");
				kingOfMine4Lore.add("§7- 4채널입니다.");
				kingOfMine4Lore.add("");
				
				firstHitLore.clear();
				firstHitLore.add("");
				firstHitLore.add("§7- 먼저 치는 사람이 무조건 이긴다!");
				firstHitLore.add("§7  적을 먼저 때려 처치하고 목표점수를 달성하자!");
				firstHitLore.add("");
				firstHitLore.add("§7  평균 플레이 시간 : 5분");
				firstHitLore.add("");
				firstHitLore.add("§7  §7좌클릭: §b게임참가 §7| §7우클릭: §b게임설명");
				firstHitLore.add("");
				
				ItemStack item;
				ItemMeta meta;
				
				//////////////////살찾채널전체	
				
				item = serverGUI.inven_minigame.getItem(19);
				item.setAmount(
						findTheMurder.ingamePlayer.size()+findTheMurder2.ingamePlayer.size() <= 0 ? 1 : findTheMurder.ingamePlayer.size()
								+findTheMurder2.ingamePlayer.size()); //살찾전체
				
				////////////살찾1채널
				item = serverGUI.inven_ftmSelect.getItem(13);
				
				item.setAmount(findTheMurder.ingamePlayer.size() <= 0 ? 1 : findTheMurder.ingamePlayer.size());
				
				if(findTheMurder.ingame) {
					item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
				} else {
					item.removeEnchantment(Enchantment.DURABILITY);
				}
				
				if(findTheMurder.ingame) {
					murder1Lore.add("§6  상태: §b게임중");
					murder1Lore.add("§6  남은인원: §b"+findTheMurder.ingamePlayer.size());
				}
				else if(findTheMurder.lobbyStart) {
					murder1Lore.add("§6  상태: §b약 "+findTheMurder.startSch.schTime+"§b초 뒤 게임 시작");
					murder1Lore.add("§6  인원: §b"+findTheMurder.ingamePlayer.size() + " / " + findTheMurder.maxPlayer);
				}
				else {
					murder1Lore.add("§6  상태: §b대기중");
					murder1Lore.add("§6  인원: §b"+findTheMurder.ingamePlayer.size() + " / " + findTheMurder.minPlayer);
				}
				
				murder1Lore.add("");	
				
				meta = item.getItemMeta();
				meta.setLore(murder1Lore);
				item.setItemMeta(meta);
				
				////////////살찾2채널
				item = serverGUI.inven_ftmSelect.getItem(31);
				
				item.setAmount(findTheMurder2.ingamePlayer.size() <= 0 ? 1 : findTheMurder2.ingamePlayer.size());
				
				if(findTheMurder2.ingame) {
					item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
				} else {
					item.removeEnchantment(Enchantment.DURABILITY);
				}
				
				if(findTheMurder2.ingame) {
					murder2Lore.add("§6  상태: §b게임중");
					murder2Lore.add("§6  남은인원: §b"+findTheMurder2.ingamePlayer.size());
				}
				else if(findTheMurder2.lobbyStart) {
					murder2Lore.add("§6  상태: §b약 "+findTheMurder2.startSch.schTime+"§b초 뒤 게임 시작");
					murder2Lore.add("§6  인원: §b"+findTheMurder2.ingamePlayer.size() + " / " + findTheMurder2.maxPlayer);
				}
				else {
					murder2Lore.add("§6  상태: §b대기중");
					murder2Lore.add("§6  인원: §b"+findTheMurder2.ingamePlayer.size() + " / " + findTheMurder2.minPlayer);
				}
				
				murder2Lore.add("");	
				
				meta = item.getItemMeta();
				meta.setLore(murder2Lore);
				item.setItemMeta(meta);
					
				/////////////////히어워채널전체
				item = serverGUI.inven_minigame.getItem(20);
				
				item.setAmount(
						heroesWar.ingamePlayer.size()+heroesWar2.ingamePlayer.size()+heroesWarUlaf.ingamePlayer.size() <= 0 
						? 1 : heroesWar.ingamePlayer.size()+heroesWar2.ingamePlayer.size()
						+heroesWarUlaf.ingamePlayer.size()); //신전전체
				
				////////////히어워1채널
					item = serverGUI.inven_hrwSelect.getItem(11);
					
					item.setAmount(heroesWar.ingamePlayer.size() <= 0 ? 1 : heroesWar.ingamePlayer.size());
					
					if(heroesWar.ingame) {
						item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
					} else {
						item.removeEnchantment(Enchantment.DURABILITY);
					}
					
					if(heroesWar.ingame) {
						heroesWar1Lore.add("§6  상태: §b게임중");
						heroesWar1Lore.add("§6  게임중인 인원: §b"+heroesWar.ingamePlayer.size());
						heroesWar1Lore.add("§6  블루팀 남은 영혼 : §b"+heroesWar.blueTeam.leftSoul);
						heroesWar1Lore.add("§6  레드팀 남은 영혼 : §b"+heroesWar.redTeam.leftSoul);
						heroesWar1Lore.add("§6  중도 참여 가능 인원 : §b"+heroesWar.getBreakIntoSize());
					}
					else if(heroesWar.lobbyStart) {
						heroesWar1Lore.add("§6  상태: §b약 "+heroesWar.startSch.schTime+"§b초 뒤 게임 시작");
						heroesWar1Lore.add("§6  인원: §b"+heroesWar.ingamePlayer.size() + " / " + heroesWar.maxPlayer);
					}
					else {
						heroesWar1Lore.add("§6  상태: §b대기중");
						heroesWar1Lore.add("§6  인원: §b"+heroesWar.ingamePlayer.size() + " / " + heroesWar.minPlayer);
					}							
					
					heroesWar1Lore.add("");
					
					meta = item.getItemMeta();
					meta.setLore(heroesWar1Lore);
					item.setItemMeta(meta);
					
					////////////히어워2채널
					item = serverGUI.inven_hrwSelect.getItem(15);
					
					item.setAmount(heroesWar2.ingamePlayer.size() <= 0 ? 1 : heroesWar2.ingamePlayer.size());
					
					if(heroesWar2.ingame) {
						item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
					} else {
						item.removeEnchantment(Enchantment.DURABILITY);
					}
					
					if(heroesWar2.ingame) {
						heroesWar2Lore.add("§6  상태: §b게임중");
						heroesWar2Lore.add("§6  게임중인 인원: §b"+heroesWar2.ingamePlayer.size());
						heroesWar2Lore.add("§6  블루팀 남은 영혼 : §b"+heroesWar2.blueTeam.leftSoul);
						heroesWar2Lore.add("§6  레드팀 남은 영혼 : §b"+heroesWar2.redTeam.leftSoul);
						heroesWar2Lore.add("§6  중도 참여 가능 인원 : §b"+heroesWar.getBreakIntoSize());
					}
					else if(heroesWar2.lobbyStart) {
						heroesWar2Lore.add("§6  상태: §b약 "+heroesWar2.startSch.schTime+"§b초 뒤 게임 시작");
						heroesWar2Lore.add("§6  인원: §b"+heroesWar2.ingamePlayer.size() + " / " + heroesWar2.maxPlayer);
					}
					else {
						heroesWar2Lore.add("§6  상태: §b대기중");
						heroesWar2Lore.add("§6  인원: §b"+heroesWar2.ingamePlayer.size() + " / " + heroesWar2.minPlayer);
					}							
					
					heroesWar2Lore.add("");
					
					meta = item.getItemMeta();
					meta.setLore(heroesWar2Lore);
					item.setItemMeta(meta);
					
					////////////히어워3채널
				item = serverGUI.inven_hrwSelect.getItem(29);

				item.setAmount(heroesWarUlaf.ingamePlayer.size() <= 0 ? 1 : heroesWarUlaf.ingamePlayer.size());

				if (heroesWarUlaf.ingame) {
					item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
				} else {
					item.removeEnchantment(Enchantment.DURABILITY);
				}

				if (heroesWarUlaf.ingame) {
					heroesWar3Lore.add("§6  상태: §b게임중");
					heroesWar3Lore.add("§6  게임중인 인원: §b"+ heroesWarUlaf.ingamePlayer.size() + " / " + heroesWarUlaf.maxPlayer);;
					heroesWar3Lore.add("§6  블루팀 남은 영혼 : §b" + heroesWarUlaf.blueTeam.leftSoul);
					heroesWar3Lore.add("§6  레드팀 남은 영혼 : §b" + heroesWarUlaf.redTeam.leftSoul);
					heroesWar3Lore.add("§c  중도 참여 가능");
				} else if (heroesWarUlaf.lobbyStart) {
					heroesWar3Lore.add("§6  상태: §b약 " + heroesWarUlaf.startSch.schTime + "§b초 뒤 게임 시작");
					heroesWar3Lore.add("§6  인원: §b" + heroesWarUlaf.ingamePlayer.size() + " / " + heroesWarUlaf.maxPlayer);
				} else {
					heroesWar3Lore.add("§6  상태: §b대기중");
					heroesWar3Lore.add("§6  인원: §b" + heroesWarUlaf.ingamePlayer.size() + " / " + heroesWarUlaf.minPlayer);
				}

				heroesWar3Lore.add("");

				meta = item.getItemMeta();
				meta.setLore(heroesWar3Lore);
				item.setItemMeta(meta);
					
				//////////////////신전체널전체	
					item = serverGUI.inven_minigame.getItem(21);
					
					item.setAmount(
							warOfGod.ingamePlayer.size()+warOfGod2.ingamePlayer.size()
							+warOfGod3.ingamePlayer.size()<= 0 
							? 1 : warOfGod.ingamePlayer.size()+warOfGod2.ingamePlayer.size()+warOfGod3.ingamePlayer.size()); //신전전체
					
				
				////////////신전1채널
				item = serverGUI.inven_wogSelect.getItem(11);
				
				item.setAmount(warOfGod.ingamePlayer.size() <= 0 ? 1 : warOfGod.ingamePlayer.size()); //신전1채널		
				
				if(warOfGod.ingame) {
					item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
				} else {
					item.removeEnchantment(Enchantment.DURABILITY);
				}
				
				if(warOfGod.ingame) {
					warOfGod1Lore.add("§6  상태: §b게임중");
					warOfGod1Lore.add("§6  게임중인 인원: §b"+warOfGod.ingamePlayer.size());
					warOfGod1Lore.add("§6  무승부까지 : §b"+warOfGod.leftTime+" 초");
				}
				else if(warOfGod.lobbyStart) {
					warOfGod1Lore.add("§6  상태: §b약 "+warOfGod.startSch.schTime+"§b초 뒤 게임 시작");
					warOfGod1Lore.add("§6  인원: §b"+warOfGod.ingamePlayer.size() + " / " + warOfGod.maxPlayer);
				}
				else {
					warOfGod1Lore.add("§6  상태: §b대기중");
					warOfGod1Lore.add("§6  인원: §b"+warOfGod.ingamePlayer.size() + " / " + warOfGod.minPlayer);
				}
				
				warOfGod1Lore.add("");
				
				meta = item.getItemMeta();
				meta.setLore(warOfGod1Lore);
				item.setItemMeta(meta);
				
				///////////신전2채널
				item = serverGUI.inven_wogSelect.getItem(15);
				
				item.setAmount(warOfGod2.ingamePlayer.size() <= 0 ? 1 : warOfGod2.ingamePlayer.size()); //신전2채널
				
				if(warOfGod2.ingame) {
					item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
				} else {
					item.removeEnchantment(Enchantment.DURABILITY);
				}
				
				if(warOfGod2.ingame) {
					warOfGod2Lore.add("§6  상태: §b게임중");
					warOfGod2Lore.add("§6  게임중인 인원: §b"+warOfGod2.ingamePlayer.size());
					warOfGod2Lore.add("§6  무승부까지 : §b"+warOfGod2.leftTime+" 초");
				}
				else if(warOfGod2.lobbyStart) {
					warOfGod2Lore.add("§6  상태: §b약 "+warOfGod2.startSch.schTime+"§b초 뒤 게임 시작");
					warOfGod2Lore.add("§6  인원: §b"+warOfGod2.ingamePlayer.size() + " / " + warOfGod2.maxPlayer);
				}
				else {
					warOfGod2Lore.add("§6  상태: §b대기중");
					warOfGod2Lore.add("§6  인원: §b"+warOfGod2.ingamePlayer.size() + " / " + warOfGod2.minPlayer);
				}
				
				warOfGod2Lore.add("");
				
				meta = item.getItemMeta();
				meta.setLore(warOfGod2Lore);
				item.setItemMeta(meta);	
				
				/////////// 신전3채널
				item = serverGUI.inven_wogSelect.getItem(31);

				item.setAmount(warOfGod3.ingamePlayer.size() <= 0 ? 1 : warOfGod3.ingamePlayer.size()); // 신전2채널

				if (warOfGod3.ingame) {
					item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
				} else {
					item.removeEnchantment(Enchantment.DURABILITY);
				}

				if (warOfGod3.ingame) {
					warOfGod3Lore.add("§6  상태: §b게임중");
					warOfGod3Lore.add("§6  게임중인 인원: §b" + warOfGod3.ingamePlayer.size());
					warOfGod3Lore.add("§6  무승부까지 : §b" + warOfGod3.leftTime + " 초");
				} else if (warOfGod3.lobbyStart) {
					warOfGod3Lore.add("§6  상태: §b약 " + warOfGod3.startSch.schTime + "§b초 뒤 게임 시작");
					warOfGod3Lore.add("§6  인원: §b" + warOfGod3.ingamePlayer.size() + " / " + warOfGod3.maxPlayer);
				} else {
					warOfGod3Lore.add("§6  상태: §b대기중");
					warOfGod3Lore.add("§6  인원: §b" + warOfGod3.ingamePlayer.size() + " / " + warOfGod3.minPlayer);
				}

				warOfGod3Lore.add("");

				meta = item.getItemMeta();
				meta.setLore(warOfGod3Lore);
				item.setItemMeta(meta);

				//////////////////랜무채널전체	
				
				item = serverGUI.inven_minigame.getItem(29);
				item.setAmount(
						randomWeaponWar.ingamePlayer.size()+randomWeaponWar2.ingamePlayer.size() <= 0 ? 1 : randomWeaponWar.ingamePlayer.size()+randomWeaponWar2.ingamePlayer.size());  //랜무전체
				
				
				////////////랜무1채널
				item = serverGUI.inven_rwwSelect.getItem(13);
				
				item.setAmount(randomWeaponWar.ingamePlayer.size() <= 0 ? 1 : randomWeaponWar.ingamePlayer.size()); //랜무1채널		
				
				if(randomWeaponWar.ingame) {
					item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
				} else {
					item.removeEnchantment(Enchantment.DURABILITY);
				}
				
				if(randomWeaponWar.ingame) {
					randomWeaponWar1Lore.add("§6  상태: §b게임중");
					randomWeaponWar1Lore.add("§6  게임중인 인원: §b"+randomWeaponWar.ingamePlayer.size());
					randomWeaponWar1Lore.add("§6  게임진행 시간: §b"+randomWeaponWar.mainSch.schTime);
					randomWeaponWar1Lore.add("§6  자기장 레벨: §b"+randomWeaponWar.limiterLv);
					randomWeaponWar1Lore.add("§6  자기장 반경: §b"+randomWeaponWar.mainSch.schTime2);
				}
				else if(randomWeaponWar.lobbyStart) {
					randomWeaponWar1Lore.add("§6  상태: §b약 "+randomWeaponWar.startSch.schTime+"§b초 뒤 게임 시작");
					randomWeaponWar1Lore.add("§6  인원: §b"+randomWeaponWar.ingamePlayer.size() + " / " + randomWeaponWar.maxPlayer);
				}
				else {
					randomWeaponWar1Lore.add("§6  상태: §b대기중");
					randomWeaponWar1Lore.add("§6  인원: §b"+randomWeaponWar.ingamePlayer.size() + " / " + randomWeaponWar.minPlayer);
				}
				
				randomWeaponWar1Lore.add("");
				
				meta = item.getItemMeta();
				meta.setLore(randomWeaponWar1Lore);
				item.setItemMeta(meta);	
				
					////////////랜무2채널
				item = serverGUI.inven_rwwSelect.getItem(31);

				item.setAmount(randomWeaponWar2.ingamePlayer.size() <= 0 ? 1 : randomWeaponWar2.ingamePlayer.size()); // 랜무2채널

				if (randomWeaponWar2.ingame) {
					item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
				} else {
					item.removeEnchantment(Enchantment.DURABILITY);
				}

				if (randomWeaponWar2.ingame) {
					randomWeaponWar2Lore.add("§6  상태: §b게임중");
					randomWeaponWar2Lore.add("§6  게임중인 인원: §b" + randomWeaponWar2.ingamePlayer.size());
					randomWeaponWar2Lore.add("§6  게임진행 시간: §b" + randomWeaponWar2.mainSch.schTime);
					randomWeaponWar2Lore.add("§6  자기장 레벨: §b" + randomWeaponWar2.limiterLv);
					randomWeaponWar2Lore.add("§6  자기장 반경: §b" + randomWeaponWar2.mainSch.schTime2);
				} else if (randomWeaponWar2.lobbyStart) {
					randomWeaponWar2Lore.add("§6  상태: §b약 " + randomWeaponWar2.startSch.schTime + "§b초 뒤 게임 시작");
					randomWeaponWar2Lore.add(
							"§6  인원: §b" + randomWeaponWar2.ingamePlayer.size() + " / " + randomWeaponWar2.maxPlayer);
				} else {
					randomWeaponWar2Lore.add("§6  상태: §b대기중");
					randomWeaponWar2Lore.add(
							"§6  인원: §b" + randomWeaponWar2.ingamePlayer.size() + " / " + randomWeaponWar2.minPlayer);
				}

				randomWeaponWar2Lore.add("");

				meta = item.getItemMeta();
				meta.setLore(randomWeaponWar2Lore);
				item.setItemMeta(meta);	
				
//				////////////////광물의 왕 채널전체	
				
				item = serverGUI.inven_minigame.getItem(30);
				item.setAmount(
						kingOfMine1.ingamePlayer.size()+kingOfMine2.ingamePlayer.size()+kingOfMine3.ingamePlayer.size()+kingOfMine4.ingamePlayer.size() <= 0 ? 
								1 : kingOfMine1.ingamePlayer.size()+kingOfMine2.ingamePlayer.size()
									+kingOfMine3.ingamePlayer.size()+kingOfMine4.ingamePlayer.size());  //광물의 왕전체
				
				
				////////////광물1채널
				item = serverGUI.inven_komSelect.getItem(10);
				
				item.setAmount(kingOfMine1.ingamePlayer.size() <= 0 ? 1 : kingOfMine1.ingamePlayer.size()); 
				
				if(kingOfMine1.ingame) {
					item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
				} else {
					item.removeEnchantment(Enchantment.DURABILITY);
				}
				
				if(kingOfMine1.ingame) {
					kingOfMine1Lore.add("§6  상태: §b게임중");
					kingOfMine1Lore.add("§6  게임중인 인원: §b"+kingOfMine1.ingamePlayer.size());
					kingOfMine1Lore.add("§6  현재 점수: §b"+kingOfMine1.score);
					kingOfMine1Lore.add("§6  남은 기회: §b"+kingOfMine1.nowChance);
				}
				else if(kingOfMine1.lobbyStart) {
					kingOfMine1Lore.add("§6  상태: §b게임중");
					kingOfMine1Lore.add("§6  게임중인 인원: §b"+kingOfMine1.ingamePlayer.size());
					kingOfMine1Lore.add("§6  현재 점수: §b"+kingOfMine1.score);
					kingOfMine1Lore.add("§6  남은 기회: §b"+kingOfMine1.nowChance);
				}
				else {
					kingOfMine1Lore.add("§6  상태: §b대기중");
					kingOfMine1Lore.add("§6  인원: §b"+kingOfMine1.ingamePlayer.size() + " / " + kingOfMine1.minPlayer);
				}
				
				kingOfMine1Lore.add("");
				
				meta = item.getItemMeta();
				meta.setLore(kingOfMine1Lore);
				item.setItemMeta(meta);	

				//////////// 광물2채널
				item = serverGUI.inven_komSelect.getItem(12);

				item.setAmount(kingOfMine2.ingamePlayer.size() <= 0 ? 1 : kingOfMine2.ingamePlayer.size());

				if (kingOfMine2.ingame) {
					item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
				} else {
					item.removeEnchantment(Enchantment.DURABILITY);
				}

				if (kingOfMine2.ingame) {
					kingOfMine2Lore.add("§6  상태: §b게임중");
					kingOfMine2Lore.add("§6  게임중인 인원: §b" + kingOfMine2.ingamePlayer.size());
					kingOfMine2Lore.add("§6  현재 점수: §b" + kingOfMine2.score);
					kingOfMine2Lore.add("§6  남은 기회: §b" + kingOfMine2.nowChance);
				} else if (kingOfMine2.lobbyStart) {
					kingOfMine2Lore.add("§6  상태: §b게임중");
					kingOfMine2Lore.add("§6  게임중인 인원: §b" + kingOfMine2.ingamePlayer.size());
					kingOfMine2Lore.add("§6  현재 점수: §b" + kingOfMine2.score);
					kingOfMine2Lore.add("§6  남은 기회: §b" + kingOfMine2.nowChance);
				} else {
					kingOfMine2Lore.add("§6  상태: §b대기중");
					kingOfMine2Lore.add("§6  인원: §b" + kingOfMine2.ingamePlayer.size() + " / " + kingOfMine2.minPlayer);
				}

				kingOfMine2Lore.add("");

				meta = item.getItemMeta();
				meta.setLore(kingOfMine2Lore);
				item.setItemMeta(meta);
				
				//////////// 광물3채널
				item = serverGUI.inven_komSelect.getItem(14);

				item.setAmount(kingOfMine3.ingamePlayer.size() <= 0 ? 1 : kingOfMine3.ingamePlayer.size());

				if (kingOfMine3.ingame) {
					item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
				} else {
					item.removeEnchantment(Enchantment.DURABILITY);
				}

				if (kingOfMine3.ingame) {
					kingOfMine3Lore.add("§6  상태: §b게임중");
					kingOfMine3Lore.add("§6  게임중인 인원: §b" + kingOfMine3.ingamePlayer.size());
					kingOfMine3Lore.add("§6  현재 점수: §b" + kingOfMine3.score);
					kingOfMine3Lore.add("§6  남은 기회: §b" + kingOfMine3.nowChance);
				} else if (kingOfMine3.lobbyStart) {
					kingOfMine3Lore.add("§6  상태: §b게임중");
					kingOfMine3Lore.add("§6  게임중인 인원: §b" + kingOfMine3.ingamePlayer.size());
					kingOfMine3Lore.add("§6  현재 점수: §b" + kingOfMine3.score);
					kingOfMine3Lore.add("§6  남은 기회: §b" + kingOfMine3.nowChance);
				} else {
					kingOfMine3Lore.add("§6  상태: §b대기중");
					kingOfMine3Lore.add("§6  인원: §b" + kingOfMine3.ingamePlayer.size() + " / " + kingOfMine3.minPlayer);
				}

				kingOfMine3Lore.add("");

				meta = item.getItemMeta();
				meta.setLore(kingOfMine3Lore);
				item.setItemMeta(meta);

				//////////// 광물4채널
				item = serverGUI.inven_komSelect.getItem(16);

				item.setAmount(kingOfMine4.ingamePlayer.size() <= 0 ? 1 : kingOfMine4.ingamePlayer.size());

				if (kingOfMine4.ingame) {
					item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
				} else {
					item.removeEnchantment(Enchantment.DURABILITY);
				}

				if (kingOfMine4.ingame) {
					kingOfMine4Lore.add("§6  상태: §b게임중");
					kingOfMine4Lore.add("§6  게임중인 인원: §b" + kingOfMine4.ingamePlayer.size());
					kingOfMine4Lore.add("§6  현재 점수: §b" + kingOfMine4.score);
					kingOfMine4Lore.add("§6  남은 기회: §b" + kingOfMine4.nowChance);
				} else if (kingOfMine4.lobbyStart) {
					kingOfMine4Lore.add("§6  상태: §b게임중");
					kingOfMine4Lore.add("§6  게임중인 인원: §b" + kingOfMine4.ingamePlayer.size());
					kingOfMine4Lore.add("§6  현재 점수: §b" + kingOfMine4.score);
					kingOfMine4Lore.add("§6  남은 기회: §b" + kingOfMine4.nowChance);
				} else {
					kingOfMine4Lore.add("§6  상태: §b대기중");
					kingOfMine4Lore.add("§6  인원: §b" + kingOfMine4.ingamePlayer.size() + " / " + kingOfMine4.minPlayer);
				}

				kingOfMine4Lore.add("");

				meta = item.getItemMeta();
				meta.setLore(kingOfMine4Lore);
				item.setItemMeta(meta);
				
				
				/////////////////컬러매치
				item = serverGUI.inven_minigame.getItem(24);
				
				item.setAmount(colorMatch.ingamePlayer.size() <= 0 ? 1 : colorMatch.ingamePlayer.size());
				
				if(colorMatch.ingame) {
					item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
				} else {
					item.removeEnchantment(Enchantment.DURABILITY);
				}
				
				if(colorMatch.ingame) {
					colorMatchLore.add("§6  상태: §b게임중");
					colorMatchLore.add("§6  게임중인 인원: §b"+colorMatch.ingamePlayer.size());
				}
				else if(colorMatch.lobbyStart) {
					colorMatchLore.add("§6  상태: §b약 "+colorMatch.startSch.schTime+"§b초 뒤 게임 시작");
					colorMatchLore.add("§6  인원: §b"+colorMatch.ingamePlayer.size() + " / " + colorMatch.maxPlayer);
				}
				else {
					colorMatchLore.add("§6  상태: §b대기중");
					colorMatchLore.add("§6  인원: §b"+colorMatch.ingamePlayer.size() + " / " + colorMatch.minPlayer);
				}							
				
				colorMatchLore.add("");
				
				meta = item.getItemMeta();
				meta.setLore(colorMatchLore);
				item.setItemMeta(meta);
				
				/////////////////폭탄 돌리기
				item = serverGUI.inven_minigame.getItem(25);
				
				item.setAmount(tntBomber.ingamePlayer.size() <= 0 ? 1 : tntBomber.ingamePlayer.size());
				
				if(tntBomber.ingame) {
					item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
				} else {
					item.removeEnchantment(Enchantment.DURABILITY);
				}
				
				if(tntBomber.ingame) {
					tntBomberLore.add("§6  상태: §b게임중");
					tntBomberLore.add("§6  게임중인 인원: §b"+tntBomber.ingamePlayer.size());
				}
				else if(tntBomber.lobbyStart) {
					tntBomberLore.add("§6  상태: §b약 "+tntBomber.startSch.schTime+"§b초 뒤 게임 시작");
					tntBomberLore.add("§6  인원: §b"+tntBomber.ingamePlayer.size() + " / " + tntBomber.maxPlayer);
				}
				else {
					tntBomberLore.add("§6  상태: §b대기중");
					tntBomberLore.add("§6  인원: §b"+tntBomber.ingamePlayer.size() + " / " + tntBomber.minPlayer);
				}							
				
				tntBomberLore.add("");
				
				meta = item.getItemMeta();
				meta.setLore(tntBomberLore);
				item.setItemMeta(meta);
				
				/////////////////스플리프
				item = serverGUI.inven_minigame.getItem(34);
				
				item.setAmount(spleef.ingamePlayer.size() <= 0 ? 1 : spleef.ingamePlayer.size());
				
				if(spleef.ingame) {
					item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
				} else {
					item.removeEnchantment(Enchantment.DURABILITY);
				}
				
				if(spleef.ingame) {
					spleefLore.add("§6  상태: §b게임중");
					spleefLore.add("§6  게임중인 인원: §b"+spleef.ingamePlayer.size());
				}
				else if(spleef.lobbyStart) {
					spleefLore.add("§6  상태: §b약 "+spleef.startSch.schTime+"§b초 뒤 게임 시작");
					spleefLore.add("§6  인원: §b"+spleef.ingamePlayer.size() + " / " + spleef.maxPlayer);
				}
				else {
					spleefLore.add("§6  상태: §b대기중");
					spleefLore.add("§6  인원: §b"+spleef.ingamePlayer.size() + " / " + spleef.minPlayer);
				}							
				
				spleefLore.add("");
				
				meta = item.getItemMeta();
				meta.setLore(spleefLore);
				item.setItemMeta(meta);
				
				/////////////////건축 경연
				item = serverGUI.inven_minigame.getItem(32);
				
				item.setAmount(buildBattle.ingamePlayer.size() <= 0 ? 1 : buildBattle.ingamePlayer.size());
				
				if(buildBattle.ingame) {
					item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
				} else {
					item.removeEnchantment(Enchantment.DURABILITY);
				}
				
				if(buildBattle.ingame) {
					buildbattleLore.add("§6  상태: §b게임중");
					buildbattleLore.add("§6  게임중인 인원: §b"+buildBattle.ingamePlayer.size());
					buildbattleLore.add("§6 "+(buildBattle.gameStep>=3 ? "§b심사중" : "심사까지"+": §b"+buildBattle.mainSch.schTime));
				}
				else if(buildBattle.lobbyStart) {
					buildbattleLore.add("§6  상태: §b약 "+buildBattle.startSch.schTime+"§b초 뒤 게임 시작");
					buildbattleLore.add("§6  인원: §b"+buildBattle.ingamePlayer.size() + " / " + buildBattle.maxPlayer);
				}
				else {
					buildbattleLore.add("§6  상태: §b대기중");
					buildbattleLore.add("§6  인원: §b"+buildBattle.ingamePlayer.size() + " / " + buildBattle.minPlayer);
				}							
				buildbattleLore.add("");
				
				meta = item.getItemMeta();
				meta.setLore(buildbattleLore);
				item.setItemMeta(meta);
				
				/////////////////데스런
				item = serverGUI.inven_minigame.getItem(33);
				
				item.setAmount(deathRun.ingamePlayer.size() <= 0 ? 1 : deathRun.ingamePlayer.size());
				
				if(deathRun.ingame) {
					item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
				} else {
					item.removeEnchantment(Enchantment.DURABILITY);
				}
				
				if(deathRun.ingame) {
					deathRunLore.add("§6  상태: §b게임중");
					deathRunLore.add("§6  게임중인 인원: §b"+deathRun.ingamePlayer.size());
				}
				else if(deathRun.lobbyStart) {
					deathRunLore.add("§6  상태: §b약 "+deathRun.startSch.schTime+"§b초 뒤 게임 시작");
					deathRunLore.add("§6  인원: §b"+deathRun.ingamePlayer.size() + " / " + deathRun.maxPlayer);
				}
				else {
					deathRunLore.add("§6  상태: §b대기중");
					deathRunLore.add("§6  인원: §b"+deathRun.ingamePlayer.size() + " / " + deathRun.minPlayer);
				}							
				deathRunLore.add("");
				
				meta = item.getItemMeta();
				meta.setLore(deathRunLore);
				item.setItemMeta(meta);
				
				/////////////////파쿠르 레이싱
				item = serverGUI.inven_minigame.getItem(28);
				
				item.setAmount(parkourRacing.ingamePlayer.size() <= 0 ? 1 : parkourRacing.ingamePlayer.size());
				
				/////////////////자리뺏기
				item = serverGUI.inven_minigame.getItem(23);
				
				item.setAmount(takeASeet.ingamePlayer.size() <= 0 ? 1 : takeASeet.ingamePlayer.size());
				
				if(takeASeet.ingame) {
					item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
				} else {
					item.removeEnchantment(Enchantment.DURABILITY);
				}
				
				if(takeASeet.ingame) {
					takeASeetLore.add("§6  상태: §b게임중");
					takeASeetLore.add("§6  게임중인 인원: §b"+takeASeet.ingamePlayer.size());
				}
				else if(takeASeet.lobbyStart) {
					takeASeetLore.add("§6  상태: §b약 "+takeASeet.startSch.schTime+"§b초 뒤 게임 시작");
					takeASeetLore.add("§6  인원: §b"+takeASeet.ingamePlayer.size() + " / " + takeASeet.maxPlayer);
				}
				else {
					takeASeetLore.add("§6  상태: §b대기중");
					takeASeetLore.add("§6  인원: §b"+takeASeet.ingamePlayer.size() + " / " + takeASeet.minPlayer);
				}							
				takeASeetLore.add("");
				
				meta = item.getItemMeta();
				meta.setLore(takeASeetLore);
				item.setItemMeta(meta);
				
					/////////////////모루 피하기
				item = serverGUI.inven_minigame.getItem(41);
				
				item.setAmount(avoidTheAnvil.ingamePlayer.size() <= 0 ? 1 : avoidTheAnvil.ingamePlayer.size());
				
				if(avoidTheAnvil.ingame) {
					item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
				} else {
					item.removeEnchantment(Enchantment.DURABILITY);
				}
				
				if(avoidTheAnvil.ingame) {
					avoidTheAnvilLore.add("§6  상태: §b게임중");
					avoidTheAnvilLore.add("§6  게임중인 인원: §b"+avoidTheAnvil.ingamePlayer.size());
				}
				else if(avoidTheAnvil.lobbyStart) {
					avoidTheAnvilLore.add("§6  상태: §b약 "+avoidTheAnvil.startSch.schTime+"§b초 뒤 게임 시작");
					avoidTheAnvilLore.add("§6  인원: §b"+avoidTheAnvil.ingamePlayer.size() + " / " + avoidTheAnvil.maxPlayer);
				}
				else {
					avoidTheAnvilLore.add("§6  상태: §b대기중");
					avoidTheAnvilLore.add("§6  인원: §b"+avoidTheAnvil.ingamePlayer.size() + " / " + avoidTheAnvil.minPlayer);
				}							
				avoidTheAnvilLore.add("");
				
				meta = item.getItemMeta();
				meta.setLore(avoidTheAnvilLore);
				item.setItemMeta(meta);
				
				/////////////////선빵 게임
				item = serverGUI.inven_minigame.getItem(42);
				
				item.setAmount(firstHit.ingamePlayer.size() <= 0 ? 1 : firstHit.ingamePlayer.size());
				
				if(firstHit.ingame) {
					item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
				} else {
					item.removeEnchantment(Enchantment.DURABILITY);
				}
				
				if(firstHit.ingame) {
					firstHitLore.add("§6  상태: §b게임중");
					firstHitLore.add("§6  게임중인 인원: §b"+firstHit.ingamePlayer.size());
				}
				else if(firstHit.lobbyStart) {
					firstHitLore.add("§6  상태: §b약 "+firstHit.startSch.schTime+"§b초 뒤 게임 시작");
					firstHitLore.add("§6  인원: §b"+firstHit.ingamePlayer.size() + " / " + firstHit.maxPlayer);
				}
				else {
					firstHitLore.add("§6  상태: §b대기중");
					firstHitLore.add("§6  인원: §b"+firstHit.ingamePlayer.size() + " / " + firstHit.minPlayer);
				}							
				firstHitLore.add("");
				
				meta = item.getItemMeta();
				meta.setLore(firstHitLore);
				item.setItemMeta(meta);
				
				//////////////// 스코어보드
				
				int cnt = 0;
				cnt += findTheMurder.ingamePlayer.size();
				cnt += findTheMurder2.ingamePlayer.size();
				cnt += heroesWar.ingamePlayer.size();
				cnt += heroesWar2.ingamePlayer.size();
				cnt += heroesWarUlaf.ingamePlayer.size();
				cnt += warOfGod.ingamePlayer.size();
				cnt += warOfGod2.ingamePlayer.size();
				cnt += randomWeaponWar.ingamePlayer.size();
				cnt += randomWeaponWar2.ingamePlayer.size();
				cnt += colorMatch.ingamePlayer.size();
				cnt += tntBomber.ingamePlayer.size();
				cnt += spleef.ingamePlayer.size();
				cnt += buildBattle.ingamePlayer.size();
				cnt += deathRun.ingamePlayer.size();
				cnt += parkourRacing.ingamePlayer.size();
				cnt += takeASeet.ingamePlayer.size();
				cnt += avoidTheAnvil.ingamePlayer.size();
				cnt += kingOfMine1.ingamePlayer.size();
				cnt += kingOfMine2.ingamePlayer.size();
				cnt += kingOfMine3.ingamePlayer.size();
				cnt += kingOfMine4.ingamePlayer.size();
				cnt += firstHit.ingamePlayer.size();
								
				for(Player p : Bukkit.getOnlinePlayers()) {
					if(inLobby(p) || waitingPlayer.containsKey(p.getName())) {
									
						PlayerData data = egDM.getPlayerData(p.getName());
						
						String bestRank = "나무";
						if(data != null) {
							bestRank = data.getBestRank();
						}
						
						Sidebar sidebar = sidebarMap.get(p.getName());
						if(sidebar == null) {
							SidebarString tmpLine = new SidebarString("");
							sidebar = new Sidebar("§a§lEG §f§lSERVER", server, 600, tmpLine);
							sidebarMap.put(p.getName(), sidebar);
							sidebar.showTo(p);
							
							Team scoreBoardTeam = sidebar.getTheScoreboard().registerNewTeam("customTeam");
							scoreBoardTeam.setOption(Option.COLLISION_RULE, OptionStatus.NEVER);
							scoreBoardTeam.addEntry(p.getName());
						}else {
							try {
								Team scoreBoardTeam = sidebar.getTheScoreboard().getTeam("customTeam");
								if(!scoreBoardTeam.getEntries().contains(p.getName())){							
										scoreBoardTeam.addEntry(p.getName());
								}
								if(scoreBoardTeam.getOption(Option.COLLISION_RULE) != OptionStatus.NEVER) {
									scoreBoardTeam.setOption(Option.COLLISION_RULE, OptionStatus.NEVER);
								}	
							}catch(Exception e) {
								
							}
						}
						
						List<SidebarString> sidebarList = new ArrayList<SidebarString>();
						
						SidebarString line = new SidebarString("§f──────────────");
						sidebarList.add(line);
						
						line = new SidebarString("§6§l서버 정보");
						sidebarList.add(line);
						
						line = new SidebarString(" §f서버인원: §e"+Bukkit.getOnlinePlayers().size());
						sidebarList.add(line);	
						
						line = new SidebarString(" §f게임중인 인원: §e"+cnt);
						sidebarList.add(line);	
						
						//line = new SidebarString(" §f파티수: §e0개");
						//sidebarList.add(line);	
						
						line = new SidebarString(" ");
						sidebarList.add(line);
						
						line = new SidebarString("§6§l플레이어 정보");
						sidebarList.add(line);
						
						line = new SidebarString(" §f최고랭크: §e"+bestRank);
						sidebarList.add(line);
						
						line = new SidebarString("§f──────────────");
						sidebarList.add(line);						
						
						sidebar.setEntries(sidebarList);
						sidebar.update();
						
						
					} else {
						//egSidebar.hideFrom(p);
					}				
				}
				if(tipTimer++ > 600) {
					tipTimer = 0;
					egCM.broadCast(tipList.get(tipIndex));
					tipIndex++;
					if(tipIndex >= tipList.size()) tipIndex = 0;
					clearDroppedItem();
					Bukkit.getLogger().info("인원 : "+Bukkit.getOnlinePlayers().size());
				}
				
			}
		}, 0l, 10l);
	}
	
	public void clearDroppedItem() {
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				egCM.broadCast(ms_alert+"20초 후 땅에 떨어진 아이템들이 삭제됩니다.");
				Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
					public void run() {
						egCM.broadCast(ms_alert+"땅에 떨어진 아이템들을 삭제하였습니다.");
						for(Entity entity : Bukkit.getWorld("world").getEntities()) {
							if(entity instanceof Item || entity instanceof Arrow) {
								entity.remove();
							}
						}
					}
				}, 400l);
			}
		}, 200l);	
	}
	
	public void spawn(Player p) {
		if(egWM.isSpectating(p)) {
			egWM.stopWatchdog(p, false);
		}
		if (p != null) {
			p.closeInventory();
			p.setPlayerTime(3000, false);
			p.teleport(lobby, TeleportCause.PLUGIN);
			BarAPI.removeBar(p);
			playerList.put(p.getName(), "로비");
			specList.remove(p.getName());
			MyUtility.allClear(p);
			MyUtility.attackDelay(p, true);
			MyUtility.setMaxHealth(p, 20);
			if (!p.isDead())
				MyUtility.healUp(p);
			p.setWalkSpeed(0.2f);
			p.setFlySpeed(0.1f);
			p.setGravity(true);
			p.setLevel(0);
			p.setExp(0);
			p.addPotionEffect(speedPt);
			//p.setFireTicks(20);
			p.setSneaking(false);
			p.addPotionEffect(absInit);
			TitleAPI.sendFullTitle(p, 10, 70, 30, ChatColor.BLUE + "EG Minigame 서버", ChatColor.GOLD + "현재 오픈 베타 중입니다.");
			p.getInventory().setItem(8, serverGUI.serverHelper);
			p.setGameMode(GameMode.SURVIVAL);
			p.setAllowFlight(false);
			for (Player t : Bukkit.getOnlinePlayers()) {
				t.showPlayer(p);
			}
			PlayerInventory pInv = p.getInventory();
			pInv.setHeldItemSlot(8);
			pInv.setItem(80, null);
			pInv.setItem(81, null);
			pInv.setItem(82, null);
			pInv.setItem(83, null);
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
			// Bukkit.dispatchCommand(p.getServer().getConsoleSender(), "scoreboard teams
			// leave "+p.getName());
			if (!spawnList.contains(p.getName()))
				spawnList.add(p.getName());
			Sidebar sidebar = sidebarMap.get(p.getName());
			if (sidebar != null) {
				sidebarMap.get(p.getName()).showTo(p);
			}
			NametagEdit.getApi().setPrefix(p, "&f");
			NametagEdit.getApi().setSuffix(p, "");
			p.setPlayerWeather(WeatherType.CLEAR);

			Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
				public void run() {
					p.teleport(lobby, TeleportCause.PLUGIN);
				}
			}, 2l);
			WorldBorderAPI.removeBorder(p);
			PlayerData pData = egDM.getPlayerData(p.getName());
			if(pData != null) pData.setGame(null);
			// egParkour.join(p.getName());
		}
	}
	
	public void spawn(String pName) {
		Player p = Bukkit.getPlayer(pName);
		spawn(p);
	}
	
	public void setCmdList() {
		cmdList.put("/스폰", "default");
		cmdList.put("/spawn", "default");
		cmdList.put("/SPAWN", "default");
		cmdList.put("/넴주", "default");
		cmdList.put("/ㅅㅍ", "default");
		//cmdList.put("/메뉴", "default");
		//cmdList.put("/menu", "default");
		//cmdList.put("/MENU", "default");
		cmdList.put("/확성기", "default");
		cmdList.put("/tc", "default");
		cmdList.put("/팀챗", "default");
		cmdList.put("/TC", "default");
		cmdList.put("/ftm", "op");
		cmdList.put("/hrw", "op");
		cmdList.put("/hrw2", "op");
		cmdList.put("/wog1", "op");
		cmdList.put("/wog2", "op");
		cmdList.put("/setlobby", "op");
		cmdList.put("/egdebug", "op");
		cmdList.put("/w", "default");
		//cmdList.put("/pl", "default");
		cmdList.put("/문의", "default");
		cmdList.put("/clm", "op");
		cmdList.put("/opchat", "op");
		cmdList.put("/공지", "op");
		cmdList.put("/메모리", "default");
		cmdList.put("/꺼내줘", "default");
		cmdList.put("/골드", "default");
		cmdList.put("/egmute", "op");
		cmdList.put("/prc", "op");
		cmdList.put("/ata", "op");
		cmdList.put("/tas", "op");
		cmdList.put("/rww1", "op");
		cmdList.put("/경고", "op");
		cmdList.put("/신고목록", "op");
		cmdList.put("/신고", "default");
		cmdList.put("/옵션", "default");
		cmdList.put("/내정보", "default");
		cmdList.put("/갱신", "default");
		cmdList.put("/감시", "op");
		cmdList.put("/fht", "op");
		cmdList.put("/차단", "default");
		cmdList.put("/디스코드", "default");
		cmdList.put("/감시로그", "op");
		cmdList.put("/홍보", "default");
	}
	
	
	public String nowPlayer(Player p) {
		try {
			return playerList.containsKey(p.getName()) ? playerList.get(p.getName()) : "튜토리얼";	
		}catch(Exception e) {
			return "";
		}
	}
	
	public String nowPlayer(String pName) {
		try {
			return playerList.containsKey(pName) ? playerList.get(pName) : "튜토리얼";	
		}catch(Exception e) {
		return "";	
		}
	}
	
	public void loadConfig() {
		try {
			lobby = new Location(Bukkit.getWorld(getConfig().getString("Lobby_World")), getConfig().getInt("Lobby_X"),
					getConfig().getInt("Lobby_Y"), getConfig().getInt("Lobby_Z"));
		} catch (IllegalArgumentException e) {
			egPM.printLog("[메인] 로비가 지정되지 않았습니다.");
		}
		if(lobby != null) {
			Minigame.checkSetting = true;
		}
	}
	
	public void setLobby(Location l) {
	    getConfig().set("Lobby_World", l.getWorld().getName());
	    getConfig().set("Lobby_X", l.getBlockX());
	    getConfig().set("Lobby_Y", l.getBlockY() + 1);
	    getConfig().set("Lobby_Z", l.getBlockZ());
	    saveConfig();
	    loadConfig();
	}
	
	private void debugCommand(Player p, String cmdLine) {
		String[] cmd = cmdLine.split(" ");
		if(cmd.length > 1) {
			if(cmd[1].equalsIgnoreCase("printcommand")) {
				printAllCommand(p);
			} else if(cmd[1].equalsIgnoreCase("printtasks")) {
				printTaskList(p);
			} else if(cmd[1].equalsIgnoreCase("iftloop")) {
				p.sendMessage("무한루프로 서버를 멈춰봅니다.");
				while(true) {
					Location l = p.getLocation(); 
				}
			} else if(cmd[1].equalsIgnoreCase("printegids")) {
				for(EGScheduler sch : EGScheduler.debugList) {
					p.sendMessage(sch.schId+"");
				}
			} else if(cmd[1].equalsIgnoreCase("gethead")) {
				for(int i = 0; i < 1000; i++) {
					ItemStack item = SkullCreator.itemFromUuid(p.getUniqueId());
					p.getInventory().addItem(item);
				}	
			} else if(cmd[1].equalsIgnoreCase("setmytime")) {
				p.setPlayerTime(Long.valueOf(cmd[2]), false);
			} else if(cmd[1].equalsIgnoreCase("setmytime2")) {
				p.setPlayerTime(Long.valueOf(cmd[2]), true);
			} else if(cmd[1].equalsIgnoreCase("gethead2")) {
				ItemStack item = getHead(UUID.fromString(cmd[2]));
				p.getInventory().addItem(item);
			}else if(cmd[1].equalsIgnoreCase("allspawn")) {
				for(Player t : Bukkit.getOnlinePlayers()) {
					spawn(t);
					t.sendMessage(ms_alert+"관리자에 의해 스폰에 돌아왔습니다.\n"+ms_alert+(cmd[2] == null ? "" : cmd[2]));
					TitleAPI.sendFullTitle(t, 0, 100, 0, "§c§l스폰에 돌아왔습니다.", "§e§l"+(cmd[2] == null ? "" : cmd[2]));
				}
			}else if(cmd[1].equalsIgnoreCase("cleardrop")) {
				clearDroppedItem();
			}else if(cmd[1].equalsIgnoreCase("gc")) {
				System.gc();
			}else if(cmd[1].equalsIgnoreCase("inlobby")) {
				if(inLobby(p)) {
					p.sendMessage("로비임");
				} else {
					p.sendMessage("로비아님");
				}
			}else if(cmd[1].equalsIgnoreCase("hideme")) {
				for(Player t : Bukkit.getOnlinePlayers()) {
					t.hidePlayer(p);
				}
				p.sendMessage("감추기");
			}else if(cmd[1].equalsIgnoreCase("firework")){
		        Location loc = p.getLocation();
		        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
		        FireworkMeta fwm = fw.getFireworkMeta();
		       
		        fwm.setPower(2);
		        fwm.addEffect(FireworkEffect.builder().withColor(Color.LIME).flicker(true).build());
		       
		        fw.setFireworkMeta(fwm);
		        fw.detonate();
		       
		        for(int i = 0;i<5; i++){
		            Firework fw2 = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
		            fw2.setFireworkMeta(fwm);
		        }
			} else if(cmd[1].equalsIgnoreCase("namgetag")) {
				ItemStack item = getHead(UUID.fromString(cmd[2]));
				p.getInventory().addItem(item);
				NametagEdit.getApi().setPrefix(p, cmd[2]);
			} else if(cmd[1].equalsIgnoreCase("attackdelay")) {
				if(cmd[2].equalsIgnoreCase("true")) {
					MyUtility.attackDelay(p, true);
				}else {
					MyUtility.attackDelay(p, false);
				}
				
				p.sendMessage("did");
			}  else if(cmd[1].equalsIgnoreCase("setprefix")) {
				
				String str = "";
				for(int i = 3; i < cmd.length; i++) {
					str += cmd[i]+" ";
				}
				str = str.substring(0, str.length()-1);
				
				String prefix = egCM.setPrefix(cmd[2], str, true);
				p.sendMessage(cmd[2]+"의 칭호 설정 : §a"+prefix);
			} else if(cmd[1].equalsIgnoreCase("entitySpawn")) {
				if(allowEntitySpawn) allowEntitySpawn = false;
				else allowEntitySpawn = true;
				p.sendMessage("엔티티 스폰 : "+allowEntitySpawn);
			}else if(cmd[1].equalsIgnoreCase("socialspy")) {
				if(egCM.socialSpyList.contains(p.getName())) egCM.socialSpyList.remove(p.getName());
				else egCM.socialSpyList.add(p.getName());
				p.sendMessage("§e모든 챗 듣기 : "+egCM.socialSpyList.contains(p.getName()));
			} else if(cmd[1].equalsIgnoreCase("parkour")) {
				if(egParkour.plist.contains(p.getName())) {
					egParkour.quit(p.getName());
					p.sendMessage(ms_alert+"파쿠르 : OFF");
				}else {
					egParkour.join(p.getName());
					p.sendMessage(ms_alert+"파쿠르 : ON");
				}
			}   else if(cmd[1].equalsIgnoreCase("testbook")) {
				ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
				BookMeta bookMeta = (BookMeta) book.getItemMeta();

				List<BaseComponent> text = new ArrayList<BaseComponent>();
				
				//create a page
				
				TextComponent t = new TextComponent("Click me");
				t.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://cafe.naver.com/boli2/"));
				t.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("카페주소 열기").create()));
				
				text.add(t);

				//add the page to the meta
				bookMeta.spigot().addPage(text.toArray(new BaseComponent[0]));

				//set the title and author of this book
				bookMeta.setTitle("Interactive Book");
				bookMeta.setAuthor("gigosaurus");

				//update the ItemStack with this new meta
				book.setItemMeta(bookMeta);
				
				p.getInventory().addItem(book);
			}else if(cmd[1].equalsIgnoreCase("createRepairMap")) {
				if (cmd[2].equalsIgnoreCase("pos1")) {
					testRepairMap.setPos1(p.getTargetBlock(null, 3).getLocation());
					p.sendMessage(p.getTargetBlock(null, 3).getLocation() + " 설정완료");
					testRepairMap.saveMapBlocks(getDataFolder().getPath() + "/Test");
				} else if (cmd[2].equalsIgnoreCase("pos2")) {
					testRepairMap.setPos2(p.getTargetBlock(null, 3).getLocation());
					p.sendMessage(p.getTargetBlock(null, 3).getLocation() + " 설정완료");
					testRepairMap.saveMapBlocks(getDataFolder().getPath() + "/Test");
				} else {
					p.sendMessage("pos1 또는 pos2");
				}
				testRepairMap.loadData(getDataFolder().getPath()+"/Test");
			}else if(cmd[1].equalsIgnoreCase("testMapRepair")) {
				testRepairMap.repaired = false;
				testRepairMap.Repair(Integer.valueOf(cmd[2]));
			}else if(cmd[1].equalsIgnoreCase("testWater")) {
				p.getLocation().getBlock().setType(Material.STATIONARY_WATER);
				p.sendMessage("dis");
			}else if(cmd[1].equalsIgnoreCase("getTestMapSize")) {
				Runtime runtime = Runtime.getRuntime();
				System.gc();
				long before = ((runtime.totalMemory() - runtime.freeMemory()) / 1048576L);
				long after = ((runtime.totalMemory() - runtime.freeMemory()) / 1048576L);
				p.sendMessage("현재 메모리 : §b"+before+" MB\n");
				testRepairMap.blockData.clear();
				System.gc();
				p.sendMessage("데이터 삭제후 메모리 : §b"+after+" MB\n");
				p.sendMessage("repairTestMap의 크기 : "+(after - before)+" MB\n");
			}
			else if(cmd[1].equalsIgnoreCase("borderTest")) {

			     p.sendMessage("Creating Border");
			     
			     Location l = p.getTargetBlock(null, 20).getLocation();
			     EGScheduler sch = new EGScheduler(null);
			     sch.schTime = 14;
			     sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			    	 public void run() {
			    		 if(sch.schTime-- > 1) {
			    			 WorldBorder wb = new WorldBorder();
			    			 if(p.getLocation().distance(l) >= sch.schTime/2) {
			    				 p.setVelocity(l.toVector().subtract(p.getLocation().toVector()).normalize().multiply(2.0f));
			    			 }
							    wb.setSize(sch.schTime);
							    wb.setCenter(l.getX(), l.getZ());
							    wb.world = ((CraftWorld) p.getWorld()).getHandle();
							    if(wb == null) p.sendMessage("null");
							    if(EnumWorldBorderAction.INITIALIZE == null); p.sendMessage("null2");
							    PacketPlayOutWorldBorder ppowb = new PacketPlayOutWorldBorder(wb, EnumWorldBorderAction.INITIALIZE);   
							     ((CraftPlayer) p).getHandle().playerConnection.sendPacket(ppowb);
							     p.sendMessage(sch.schTime+" / " +p.getLocation().distance(l));							    
			    		 }else {
			    			 sch.cancelTask(true);
			    		 }
			    		
			    	 }
			     }, 10l, 40l);
			}
			else if(cmd[1].equalsIgnoreCase("removeborder")) {
				p.sendMessage("removing Border");
				WorldBorder wb = new WorldBorder();
			    wb.setSize(1000000f);
			    wb.setCenter(p.getLocation().getX(), p.getLocation().getZ());
			    wb.world = ((CraftWorld) p.getWorld()).getHandle();
			    
			    PacketPlayOutWorldBorder ppowb = new PacketPlayOutWorldBorder(wb, EnumWorldBorderAction.INITIALIZE);   
			     ((CraftPlayer) p).getHandle().playerConnection.sendPacket(ppowb);
			     p.sendMessage("Creating Border");
			}
			else if(cmd[1].equalsIgnoreCase("glowingtest")) {
				WrapperPlayServerEntityEffect wp = new WrapperPlayServerEntityEffect();
				wp.setEffectID((byte)PotionEffectType.SPEED.getId());
				wp.setDuration(7200);
				wp.setAmplifier((byte)3);
				//wp.setHideParticles(true);
				wp.setEntityID(Bukkit.getPlayer(cmd[2]).getEntityId());
				wp.sendPacket(p);
			}else if(cmd[1].equalsIgnoreCase("setnametag")) {
				MyUtility.changeName("test", p);
			}else if(cmd[1].equalsIgnoreCase("datamanager")) {
				if(cmd.length <= 2) {
					p.sendMessage("/egdebug datamanager <save/load/specLoad>");
				}else if(cmd[2].equalsIgnoreCase("save")) {
					int cnt = egDM.saveAllData();
					p.sendMessage(cnt+" 개의 데이터 저장");
				}else if(cmd[2].equalsIgnoreCase("load")) {
					int cnt = egDM.loadPlayerData();				
					p.sendMessage(cnt+" 개의 데이터 로드");
				}else if(cmd[2].equalsIgnoreCase("specLoad")) {
					egDM.specifyLoadPlayerData(cmd[3], true);
				}
			}else if(cmd[1].equalsIgnoreCase("setmmr")) {
				PlayerData playerData = server.egDM.getPlayerData(p.getName());
				if(playerData == null) return;
				MinigameData gameData = playerData.getGameData(cmd[2]);
				if(gameData == null) return;
				gameData.setMMR(gameData.getMMR() + Integer.valueOf(cmd[3]));
				gameData.saveData();
			}else if(cmd[1].equalsIgnoreCase("symboltest")) {
				p.sendMessage("");
			}else if(cmd[1].equalsIgnoreCase("repairMinigameInven")) {
				serverGUI.repairMinigameInven();
			}else if(cmd[1].equalsIgnoreCase("citizentest")) {

		        NPCRegistry registry = CitizensAPI.getNPCRegistry();

		        NPC hNpc = registry.createNPC(EntityType.PLAYER, "Test");
		        hNpc.spawn(p.getLocation().add(p.getLocation().getDirection().multiply(20)));
		        //hNpc.getNavigator().setTarget(p, true);
		        Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
		        	public void run() {
		        		hNpc.getNavigator().setTarget(p.getLocation().add(p.getLocation().getDirection().multiply(10)));
		        		//hNpc.getNavigator().
		        	}
		        },20l);
		        
		        
				
			}else if(cmd[1].equalsIgnoreCase("blockgrowing")) {
				Block b = p.getTargetBlock(null, 10);
				MyUtility.sendGlowingBlock(server, p, b.getLocation().add(0.5, 0, 0.5), 300);
			}else if(cmd[1].equalsIgnoreCase("getuuid")) {
				OfflinePlayer offP = Bukkit.getOfflinePlayer(cmd[2]);
				p.sendMessage(offP.getUniqueId().toString());
			}else if(cmd[1].equalsIgnoreCase("getuuidP")) {
				OfflinePlayer getP = Bukkit.getOfflinePlayer(cmd[2]);
				OfflinePlayer offP = Bukkit.getOfflinePlayer(UUID.fromString(getP.toString()));
				p.sendMessage(offP.getName());
			}else if(cmd[1].equalsIgnoreCase("fromBukkit")) {
				String command = "";
				for(int i = 2; i < cmd.length; i++) {
					command += cmd[i] + " ";
				}
				p.sendMessage(command);
				getServer().dispatchCommand(getServer().getConsoleSender(), command);
			}else if(cmd[1].equalsIgnoreCase("uuidTest")) {
				OfflinePlayer offP = Bukkit.getOfflinePlayer(cmd[2]);
				egDM.updatePlayerData(p.getName(), offP.getUniqueId().toString());
			}else if(cmd[1].equalsIgnoreCase("uuidLoop")) {
				for(int i = 0; i < Integer.valueOf(cmd[2]); i++) {
					p.sendMessage(p.getUniqueId().toString());
				}
			}else if(cmd[1].equalsIgnoreCase("freeze")) {
				if(freeze) freeze = false;
				else freeze = true;
				p.sendMessage(ms_alert+"did");
			}
		} else {
			p.sendMessage("§7/egdebug <디버깅 목록>\n"
							+"목록\n"
							+"printcommand - EG미니게임 모든 명령어 출력\n"
							+"printtasks - 스케쥴러 개수 출력\n"
							+"gethead - 자기 머리 얻기\n"
							+"gethead <닉> - 해당 닉 머리 얻기\n"
							+"allspawn - 모두 스폰으로\n"
							+"cleardrop - 땅에 떨어진 아이템 없애기\n"
							+"gc - 가비지 컬렉터 실행\n"
							+"inlobby - 자신이 스폰에 있는걸로 치는지 확인\n"
							+"hideme - 자신의 모습 숨기기(투명버프x)\n"
							+"nametag <태그> - prefix 설정\n"
							+"entityspawn - 엔티티 스폰 여부 설정\n"
							+"socialspy - 모든 챗 듣기\n"
							+"parkour - 파쿠르 기능 사용\n"
							+"testbook - 테스트 북 얻기\n"
							+"createRepair <pos1/pos2> - 테스트 리페어 맵 생성\n"
							+"repairttestMap <시간> - 테스트맵 복구\n"
							+"datamanager <save/load> - 플레이어 데이터로드 또는 세이브\n"
							+"setprefix <플레이어명> <칭호> - 플레이어 채팅 칭호 설정\n"
							+"setmmr <게임영문이름> <점수> - MMR 강제 변경\n"
							+"removeborder - 자신의 월드 보더 강제 삭제\n"
							);	
		}
	}	
	
	/*private void sayCommand(Player p, String cmdLine) {
		String[] cmd = cmdLine.split(" ");
		if(cmd.length == 1) {
			p.sendMessage("§c보낼 메세지를 입력해주세요.");
			return;
		}
		if(!sayCooldown.containsKey(p.getName())) {
			sayCooldown.put(p.getName(), System.currentTimeMillis());
			Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
				public void run() {
					sayCooldown.remove(p.getName());
				}
			}, 1200l);
			if(cmd.length > 1) {
				String msg = "";
				for(int i = 1; i < cmd.length; i++) {
					msg += cmd[i]+" ";
				}
				egCM.sayAll(p ,"               \n§7[ §a확성기 §7] §f"+p.getName()+" : §f"+msg+"\n         ");
			}
		} else {
			long leftTime = sayCooldown.get(p.getName());
			leftTime = (leftTime+60000 - System.currentTimeMillis()) / 1000; 
			ActionBarAPI.sendActionBar(p, ChatColor.RED+"§l"+ (int)leftTime+ "초 후 사용 가능", 80);
			p.playSound(p.getLocation(), Sound.BLOCK_WOOD_BUTTON_CLICK_OFF, 1.0F, 0.2F);
		}
	}*/
	
	private void sayCommand(Player p, String cmdLine) {
		String[] cmd = cmdLine.split(" ");
		
		long leftTime = sayCooldown.containsKey(p.getName()) ? sayCooldown.get(p.getName()) : 0;
		leftTime = (leftTime + 50000 - System.currentTimeMillis()) / 1000;
		
		if(leftTime <= 0) {
			PlayerData pData = egDM.getPlayerData(p.getName());
			if(pData == null) {
				p.sendMessage(ms_alert+"당신의 유저 데이터가 없습니다. 서버에 재접속 해보세요.");
				return;
			}else {
				//egCM.sayAll(p, "               \n§7[ §a확성기 §7] §f" + p.getName() + " : §f" + msg + "\n         ");
				
				Minigame nowPlaying = pData.getGame();
				
				if(nowPlaying == null) {
					p.sendMessage(ms_alert+"현재 미니게임 참여 중이 아니십니다.");
				}else {
					if(nowPlaying == parkourRacing) {
						egCM.sayAll(p, "               \n§7[ §a현황판 §7] §e" + p.getName() + "§f 님이 §e"
								+ ChatColor.stripColor(nowPlaying.disPlayGameName)
										+ "§f에서 §e"+"게임 중"+"§f입니다."
										+ " §f( "+nowPlaying.ingamePlayer.size()+"명 플레이 중"+" )"
												+ "\n         ");
						return;
					}
					String status = "대기 중";
					int maxCnt = 0;
					if(nowPlaying.ingame) {
						status = "게임 중";
						p.sendMessage(ms_alert+"이미 진행중인  게임은 홍보가 불가능합니다.");
						maxCnt = nowPlaying.maxPlayer;
						return;
					} else if(nowPlaying.lobbyStart) {
						status = "시작 대기 중";
						maxCnt = nowPlaying.maxPlayer;
					} else {
						status = "대기 중";
						maxCnt = nowPlaying.minPlayer;
					}
					
					egCM.sayAll(p, "               \n§7[ §a현황판 §7] §e" + p.getName() + "§f 님이 §e"
							+ ChatColor.stripColor(nowPlaying.disPlayGameName)
									+ "§f에서 §e"+status+"§f입니다."
									+ " §f( "+nowPlaying.ingamePlayer.size() +" / "+maxCnt+" )"
											+ "\n         ");
					sayCooldown.put(p.getName(), System.currentTimeMillis());	
				}		
			}		
		} else {
			ActionBarAPI.sendActionBar(p, ChatColor.RED + "§l" + (int) leftTime + "초 후 사용 가능", 80);
			p.playSound(p.getLocation(), Sound.BLOCK_WOOD_BUTTON_CLICK_OFF, 1.0F, 0.2F);
		}
	}
	
	private void muteCommand(Player p, String cmdLine) {
		String[] cmd = cmdLine.split(" ");
		if(cmd.length == 1) {
			p.sendMessage("§c뮤트할 플레이어 이름 입력해주세요.");
			return;
		}
		egCM.mute(cmd[1], Integer.valueOf(cmd[2]));
		p.sendMessage(cmd[1]+" §c뮤트완료.");
	}
	
	private void opChatCommand(Player p) {
		if(!egCM.opChat.contains(p.getName())) {
			p.sendMessage(ms_alert+"이제 모든 메세지를 OP에게만 보냅니다.");
			egCM.opChat.add(p.getName());
		}else {
			p.sendMessage(ms_alert+"이제 모두에게 메세지를 보냅니다.");
			egCM.opChat.remove(p.getName());
		}
	}
	
	private void unMuteCommand(Player p, String cmdLine) {
		String[] cmd = cmdLine.split(" ");
		if(cmd.length == 1) {
			p.sendMessage("§c뮤트를 해제할 플레이어 이름 입력해주세요.");
			return;
		}
		egCM.unMute(cmd[1]);
		p.sendMessage(cmd[1]+" §c뮤트 해제 완료.");
	}
	
	private void warnCommand(Player p, String cmdLine) {
		String[] cmd = cmdLine.split(" ");
		if(cmd.length == 1) {
			p.sendMessage(egRM.ms+"§c경고를 줄 플레이어명을 입력해주세요.\n§c/경고 <닉네임> <경고수>\n§c경고수에 음수 입력시 경고차감 가능");
			return;
		}else if(cmd.length == 2) {
			p.sendMessage(egRM.ms+"§c얼만큼 경고를 줄지 적어주세요.\n§c/경고 <닉네임> <경고수>\n§c경고수에 음수 입력시 경고차감 가능");
			return;
		}else if(cmd.length >= 3) {
			int nowTargetWarn = egRM.addWarn(cmd[1], Integer.valueOf(cmd[2]));
			if(nowTargetWarn == -1) p.sendMessage(egRM.ms+cmd[1]+" 이라는 플레이어는 데이터가 존재하지 않습니다.");
			else p.sendMessage(egRM.ms+cmd[1]+"에게 경고를 "+cmd[2]+"회 줬습니다. "
					+ "\n§c해당 플레이어의 현재 누적 경고수 : "+nowTargetWarn
					+"\n§c"+egRM.getLimiter()+"이상 경고수가 누적되면 영구벤됩니다.");
		}
	}
	
	private void reportCommand(Player p, String cmdLine) {
		String[] cmd = cmdLine.split(" ");
		if(cmd.length == 1) {
			p.sendMessage(egRM.ms+"§c신고할 플레이어명을 입력해주세요.\n§c/신고 <닉네임>");
			return;
		}else if(cmd.length >= 2) {
			if(!egRM.report(p, cmd[1])) {
				p.sendMessage(egRM.ms+cmd[1]+" 님은 서버에 데이터가 존재하지 않습니다.");
			}
		}
	}
	
	private void updateMyDataCommand(Player p) {
		
		UUID uuid = p.getUniqueId();
		if(uuid == null) {
			p.sendMessage("§c당신의 UUID가 널값입니다.");
		}else {
			String uuidStr = uuid.toString();
			String baseName = egDM.updatePlayerData(p.getName(), uuidStr);
			if(baseName == null) {
				p.sendMessage(ms_alert+uuidStr+"과  일치하는 기존 데이터가 없습니다.");
			}else {
				p.sendMessage(ms_alert+"§7기존 데이터를 §c"+baseName+"§7에서 가져왔습니다.");
			}
		}	
	}
	
	private void openReportUICommand(Player p) {
		egRM.openReportUI(p);
	}
	
	private void clearChatCommand(Player p) {
		for(String pName : spawnList) {
			Player t = Bukkit.getPlayer(pName);
			if(t == null) continue;
			if(inLobby(t)) t.sendMessage("\n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n "
					+ "\n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n "
					+ "\n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n \n"
					+ ms_alert+"채팅창 청소");
		}	
	}
	
	private void watchdogCommand(Player p, String cmdLine) {
		if(egWM.isSpectating(p)) {
			egWM.stopWatchdog(p, true);
		}else {
			String[] cmd = cmdLine.split(" ");
			if(cmd.length == 1) {
				p.sendMessage("§c감시할 플레이어 이름을 입력해주세요.");
				return;
			}
			if(cmd.length > 1) {
				Player t = Bukkit.getPlayer(cmd[1]);
				if(t == null) {
					p.sendMessage("§c"+cmd[1] + "은 존재하지 않습니다.");
				}else {
					egWM.watchdog(p, t);		
				}
			}
		}
	}
	
	private void blockChatCommand(Player p, String cmdLine) {
		if(egWM.isSpectating(p)) {
			egWM.stopWatchdog(p, true);
		}else {
			String[] cmd = cmdLine.split(" ");
			if(cmd.length == 1) {
				p.sendMessage(ms_alert+"§c차단할 플레이어 이름을 입력해주세요.");
				return;
			}
			if(cmd.length > 1) {
				//차단 코드
			}
		}
	}
	
	private void discordCommand(Player p, String cmdLine) {
		p.sendMessage("\n§7↓클릭하시면 됩니다.↓\n§7https://discord.gg/QDzUwGn");
	}
	
	private void watchdogLogCommand(Player p, String cmdLine) {
		egWM.toggleLog(p);
	}
	
	
	private void requestCommand(Player p, String cmdLine) {
		String[] cmd = cmdLine.split(" ");
		if(cmd.length == 1) {
			p.sendMessage("§c보낼 메세지를 입력해주세요.");
			return;
		}
		
		long leftTime = requestCooldown.containsKey(p.getName()) ? requestCooldown.get(p.getName()) : 0;
		leftTime = (leftTime + 40000 - System.currentTimeMillis()) / 1000;
		
		if(leftTime <= 0) {
			requestCooldown.put(p.getName(), System.currentTimeMillis());
			if (cmd.length > 1) {
				String msg = "";
				for (int i = 1; i < cmd.length; i++) {
					msg += cmd[i] + " ";
				}
				egCM.opRequest("§7[ §4문의 §7] §f" + p.getName() + " : §f" + msg);
			}
		} else {
			ActionBarAPI.sendActionBar(p, ChatColor.RED + "§l" + (int) leftTime + "초 후 사용 가능", 80);
			p.playSound(p.getLocation(), Sound.BLOCK_WOOD_BUTTON_CLICK_OFF, 1.0F, 0.2F);
		}
	}
	
	private void alertCommand(Player p, String cmdLine) {
		String[] cmd = cmdLine.split(" ");
		if(cmd.length == 1) {
			p.sendMessage("§c보낼 메세지를 입력해주세요.");
			return;
		}
		if(cmd.length > 1) {
			String msg = "";
			for(int i = 1; i < cmd.length; i++) {
				msg += cmd[i]+" ";
			}
			egCM.forceAlert(" \n§7[ §4공지 §7] §b : §c"+msg+"\n ");
		}
	}
	
	private void privateMsg(Player p, String cmdLine) {
		String[] cmd = cmdLine.split(" ");
		if(cmd.length == 1) {
			p.sendMessage("§c보낼 플레이어의 닉네임을 입력해주세요.");
			return;
		}
		if(cmd.length == 2) {
			p.sendMessage("§c보낼 메세지를 입력해주세요.");
			return;
		}
		Player t = Bukkit.getPlayer(cmd[1]);
		if(t == null) {
			p.sendMessage("§c"+cmd[1]+" 님은 서버에 접속중이지 않습니다.");
		} else {
			String msg = "";
			for(int i = 2; i < cmd.length; i++) {
				msg += cmd[i]+" ";
			}
			egCM.sendPrivateMsg(p, t, msg);
		}
	}
	
	private void printServerMemoryInfo(Player p) {
		Runtime runtime = Runtime.getRuntime();
		p.sendMessage(
				"§7[ §a전체 메모리 공간 §7] : "
				+ "§b"+(runtime.totalMemory() / 1048576L) + " MB\n"
				+ "§7[ §a사용중인 메모리 공간 §7] : "
				+ "§b"+((runtime.totalMemory() - runtime.freeMemory()) / 1048576L)+" MB\n"
				+ "§7[ §a사용 가능한 메모리 공간 §7] : "
				+ "§b"+(runtime.freeMemory() / 1048576L) + " MB\n"
				+ "§7[ §aTPS (최근 1분)§7] : §b"+ MinecraftServer.getServer().recentTps[0]+"\n"
				+ "§7[ §a당신의 핑 §7] : §b"+ Ping.getPing(p)+"ms");
	}
	
	private void helpMeFromBlock(Player p) {
		p.sendMessage(ms_alert+"사용 불가능한 명령어입니다.");
		return;
		
		/*if(!helpMeFromBlockCooldownMap.containsKey(p.getName())) {
			p.teleport(p.getLocation().add(0,0.5,0));
			p.sendMessage(ms_alert+"꺼내줘 실행 완료");
			helpMeFromBlockCooldownMap.put(p.getName(), System.currentTimeMillis());
		}else {
			long dur = System.currentTimeMillis() - helpMeFromBlockCooldownMap.get(p.getName());
			if(dur >= 3000) {
				if(p.getLocation().getY() < 0) {
					Location l = p.getLocation().clone();
					l.setY(0);
					Location tmpL = MyUtility.getUpLocation(l);
					if(tmpL != null) p.teleport(tmpL);
					else p.teleport(p.getLocation().add(0,1,0));
				}else {
					p.teleport(p.getLocation().add(0,1,0));
				}
				p.sendMessage(ms_alert+"꺼내줘 실행 완료");
				helpMeFromBlockCooldownMap.put(p.getName(), System.currentTimeMillis());
			}else {
				p.sendMessage(ms_alert+(int)((3000-dur)/1000)+"초후 시도해보세요.");
			}
		}*/
	}
	
	public void printCommandList(Player p) {
		p.sendMessage("§7[§d알림§7] §f"+p.getName()+" 님이 사용 가능한 명령어 목록§7-> §f");
		//if(p.isOp()) {
			p.sendMessage("§7/w(귓) §f>>> §c플레이어에게 귓속말을 보냅니다.");
			p.sendMessage("§7/spawn(스폰) §f>>> §c스폰으로 이동합니다.(게임 퇴장)");
			p.sendMessage("§7/메모리 §f>>> §c서버 TPS와 메모리 현황, 자신의 핑을 확인합니다.");
			p.sendMessage("§7/홍보 §f>>> §c자신이 참여중인 미니게임 현황을 띄웁니다.");
			p.sendMessage("§7/문의 §f>>> §c관리자에게 문의 메세지를 보냅니다.");
			//p.sendMessage("§7/꺼내줘 §f>>> §c블럭에 끼었을 때 사용하세요.");
			p.sendMessage("§7/신고 §f>>> §c규칙 위반 유저를 신고합니다.");
			p.sendMessage("§7/옵션 §f>>> §c개인 옵션을 설정합니다.");
			p.sendMessage("§7/갱신 §f>>> §c닉네임을 변경하셨을 때 사용하세요. 기존 데이터를 가져옵니다.");
		//}
	}
	
	public ItemStack getHead(UUID id) {
		ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
		SkullMeta meta = (SkullMeta) skull.getItemMeta();
		meta.setOwningPlayer(Bukkit.getOfflinePlayer(id));
		//meta.setDisplayName(ChatColor.LIGHT_PURPLE);
		skull.setItemMeta(meta);
		
		return skull;
	}

	
	private void printTaskList(Player p) {
		List<BukkitTask> tasks = Bukkit.getScheduler().getPendingTasks();
		p.sendMessage("list:");
		int egMinigameCnt = 0;
		for(BukkitTask task : tasks) {
			if(task.getOwner().getName().contains("EGMinigame")) {
				p.sendMessage(task.getOwner() + " : "+task.getTaskId());
				egMinigameCnt+=1;
			}
		}
		p.sendMessage("총 활성개수 : "+tasks.size());
		p.sendMessage("EG미니게임 활성수 : "+egMinigameCnt);
	}
	
	private void printAllCommand(Player p) {
		List<String> keySet = new ArrayList<String>(cmdList.keySet());
		for(String cmd : keySet) {
			p.sendMessage(cmd+":"+cmdList.get(cmd));
		}
	}
	
	public void cancelAllSchedule() {
		Bukkit.getScheduler().cancelTasks(this);
	}
	
	public boolean inLobby(Player p) {
		if(p == null) return false;
		/*if(noGameName.contains(nowPlayer(p)) || spawnList.contains(p.getName())
				|| waitingPlayer.containsKey(p.getName())) return true;
		else return false;*/
		if(noGameName.contains(nowPlayer(p)) || spawnList.contains(p.getName())) return true;
		else return false;
	}
	
	public boolean inLobby(String pName) {
		Player p = Bukkit.getPlayer(pName);
		if(noGameName.contains(nowPlayer(p)) || spawnList.contains(p.getName())) return true;
		else return false;
	}


	//////////////////이벤트 클래스
	public class DefaultEventHandler implements Listener{
		
		/////////////////////이벤트
		
		@EventHandler
		public void onMoveVehicle(VehicleMoveEvent e) {
			Vehicle vehicle = e.getVehicle();
			Player p = (Player) vehicle.getPassenger();
			if(vehicle.equals(testVehicle)) {
				Bukkit.broadcastMessage("did");
				for(FallingBlock fb : testfb) {
					//fb.teleport(p.getLocation());
				}
			}
		}
		
		
		@EventHandler(priority = EventPriority.HIGH)
		public void onPlayercommand(PlayerCommandPreprocessEvent e)	{
			Player p = e.getPlayer();
			if(e.getMessage() == null) return;
			String cmdMain = e.getMessage().split(" ")[0];
			if(cmdMain == null) return;
			if(cmdSpawn.contains(cmdMain)) {
				spawn(p);
				e.setCancelled(true);
			} else if(cmdMain.equalsIgnoreCase("/확성기") || cmdMain.equalsIgnoreCase("/홍보")) {
				sayCommand(p, e.getMessage());
				e.setCancelled(true);
			} else if(cmdMain.equalsIgnoreCase("/문의")) {
				requestCommand(p, e.getMessage());
				e.setCancelled(true);
			} else if(cmdMain.equalsIgnoreCase("/공지") && p.isOp()) {
				alertCommand(p, e.getMessage());
				e.setCancelled(true);
			} else if(cmdMain.equalsIgnoreCase("/w")) {
				privateMsg(p, e.getMessage());
				e.setCancelled(true);
			} else if(cmdMain.equalsIgnoreCase("/귓")) {
				privateMsg(p, e.getMessage());
				e.setCancelled(true);
			} else if(cmdMain.equalsIgnoreCase("/opchat") && p.isOp()) {
				opChatCommand(p);
				e.setCancelled(true);
			} else if(cmdMain.equalsIgnoreCase("/명령어")) {
				printCommandList(p);
				e.setCancelled(true);
			} else if(cmdMain.equalsIgnoreCase("/골드")) {
				//p.sendMessage(ms_alert+"§e소지골드 §7: §a"+egGM.checkGold(p.getName()));
				e.setCancelled(true);
			} else if(cmdMain.equalsIgnoreCase("/메모리")) {
				printServerMemoryInfo(p);
				e.setCancelled(true);
			}else if(cmdMain.equalsIgnoreCase("/꺼내줘")) {
				helpMeFromBlock(p);
				e.setCancelled(true);
			}else if(cmdMain.equalsIgnoreCase("/setLobby") && p.isOp()) {
				setLobby(MyUtility.getIntLocation(p));
				p.sendMessage(ms_imp+"로비 설정 완료");
				e.setCancelled(true);
			} else if(cmdMain.equalsIgnoreCase("/egdebug")) {
				debugCommand(p, e.getMessage());
				e.setCancelled(true);
			}else if(cmdMain.equalsIgnoreCase("/egmute") && p.isOp()) {	
				muteCommand(p, e.getMessage());
				e.setCancelled(true);
			}else if(cmdMain.equalsIgnoreCase("/egunmute") && p.isOp()) {	
				unMuteCommand(p, e.getMessage());
				e.setCancelled(true);
			}else if(cmdMain.equalsIgnoreCase("/경고") && p.isOp()) {	
				warnCommand(p, e.getMessage());
				e.setCancelled(true);
			}else if(cmdMain.equalsIgnoreCase("/신고")) {	
				reportCommand(p, e.getMessage());
				e.setCancelled(true);
			}else if(cmdMain.equalsIgnoreCase("/옵션")) {	
				p.openInventory(serverGUI.inven_option);
				e.setCancelled(true);
			}else if(cmdMain.equalsIgnoreCase("/내정보")) {	
				p.openInventory(serverGUI.inven_myInfo);
				e.setCancelled(true);
			}else if(cmdMain.equalsIgnoreCase("/갱신")) {	
				updateMyDataCommand(p);
				e.setCancelled(true);
			}else if(cmdMain.equalsIgnoreCase("/신고목록") && p.isOp()) {	
				openReportUICommand(p);
				e.setCancelled(true);
			}else if(cmdMain.equalsIgnoreCase("/청소") && p.isOp()) {	
				clearChatCommand(p);
				e.setCancelled(true);
			}else if(cmdMain.equalsIgnoreCase("/감시") && p.isOp()) {	
				watchdogCommand(p, e.getMessage());
				e.setCancelled(true);
			}else if(cmdMain.equalsIgnoreCase("/차단")) {	
				blockChatCommand(p, e.getMessage());
				e.setCancelled(true);
			}else if(cmdMain.equalsIgnoreCase("/디스코드")) {	
				discordCommand(p, e.getMessage());
				e.setCancelled(true);
			}else if(cmdMain.equalsIgnoreCase("/감시로그")) {	
				watchdogLogCommand(p, e.getMessage());
				e.setCancelled(true);
			}
			if (!p.isOp() && !e.isCancelled() && !cmdList.containsKey(cmdMain)) {
				p.sendMessage("§c존재하지 않는 명령어입니다..");
				e.setCancelled(true);
			}
		}
		
		/*@EventHandler
		public void onPlayerShotArrow(EntityShootBowEvent e) {
			Arrow a = (Arrow)e.getProjectile();
			a.setBounce(true);
			//a.setSilent(true);
			a.setGravity(false);
			
			
		}*/
		
		@EventHandler
		public void onPlayerTeleport(PlayerTeleportEvent e) {
			if(e.getCause() == TeleportCause.SPECTATE) {
				TitleAPI.sendFullTitle(e.getPlayer(), 0, 60, 0, "", "§e지원하지 않는 기능입니다.");
				e.setCancelled(true);
			}
		}
		
		@EventHandler
		public void onFoodLevelChange(FoodLevelChangeEvent e){
			if(e.getEntity() instanceof Player){
				Player p = (Player) e.getEntity();
				if(inLobby(p)) e.setFoodLevel(20);
			}
		}
		
		@EventHandler
		public void onMouseClick(PlayerInteractEvent e) {
			Player p = e.getPlayer();
			Action action = e.getAction();
			/*long lastClick = block_AutoMouseMap.get(p.getName());
			if(System.currentTimeMillis() - lastClick  < 100) {
				e.setCancelled(true);
				p.sendMessage(ms_alert+"조금만 천천히 클릭하세요.");
			}*/
			if(action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
				if(p.getGameMode() == GameMode.SPECTATOR) {
					TitleAPI.sendFullTitle(e.getPlayer(), 0, 60, 0, "", "§c지원하지 않는 기능입니다.");
					e.setCancelled(true);
				}
			}
			if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;
			Block b = e.getClickedBlock();
			if (e.getClickedBlock() != null) {
				if(b.getType() == Material.ENDER_CHEST || b.getType() == Material.ANVIL) {
					e.setCancelled(true);
				}
			}
			if (!inLobby(p)) return;
			if(p.getInventory().getItemInMainHand().getType() == Material.NETHER_STAR) {
				if(p.getGameMode() == GameMode.SPECTATOR) return;
				p.openInventory(serverGUI.inven_serverHelper);
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 2.0f);
			}
		}
		
		@EventHandler
		public void onPlayerJoin(PlayerJoinEvent e) {
			try {
				Player p = e.getPlayer();
				if(p == null) return;
				e.setJoinMessage(null);
				//block_AutoMouseMap.put(p.getName(), 0l);
				Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
					public void run() {
						spawn(p);
						PlayerData pData = egDM.specifyLoadPlayerData(p.getName(), false);
						for(String pName : spawnList) {
							Player t = Bukkit.getPlayer(pName);
							if(t == null) continue;
							if(inLobby(t)) t.sendMessage(ms_alert+"§e"+p.getName()+"§f님이 서버에 접속하셨습니다. 환영합니다.");
						}		
						pData.setUUID(p.getUniqueId().toString());
						if(p.isOp()) {
							Bukkit.dispatchCommand(p, "v");
						}
					}
				}, 20l);				
			}catch(Exception excep) {
				excep.printStackTrace();
			}
		}
		
		@EventHandler
		public void onPlayerQuit(PlayerQuitEvent e) {
			Player p = e.getPlayer();
			e.setQuitMessage(null);
			playerList.remove(p.getName());
			spawnList.remove(p.getName());
			block_AutoMouseMap.remove(p.getName());
			if(sidebarMap.containsKey(p.getName())) {
				sidebarMap.remove(p.getName());
			}
		}
		
		@EventHandler
		public void onPlayerRespawn(PlayerRespawnEvent e) {
			Player p = e.getPlayer();
			if(inLobby(p)){
				Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
					public void run() {
						spawn(p);
					}
				}, 5l);
			}
		}
		
		@EventHandler
		public void onBlockBreak(BlockBreakEvent e) {
			Player p = e.getPlayer();
			if(inLobby(p) && !p.isOp()) {
				e.setCancelled(true);
			}
		}

		@EventHandler
		public void onBlockPlaced(BlockPlaceEvent e) {
			Player p = e.getPlayer();
			if(inLobby(p) && !p.isOp()) {
				e.setCancelled(true);
			}
		}	
		
		@EventHandler
		public void onPlayerChat(PlayerChatEvent e) {
			Player p = e.getPlayer();
			e.setCancelled(true);
			if(inLobby(p)) {
				if(specList.containsKey(p.getName())) {
					String gameName = specList.get(p.getName()).gameName;
					for(Player t : Bukkit.getOnlinePlayers()) {
						if(!specList.containsKey(t.getName())) continue;
						specList.get(t.getName()).gameName.equalsIgnoreCase(gameName);
						t.sendMessage("§7[ §6관전 §7] "+p.getName()+" : "+e.getMessage());
					}
				} else {
					egCM.sendMessagesToStringList(spawnList, p, e.getMessage(), true);				
				}	
			}
		}	
		
		@EventHandler
		public void onPlayerDeath(PlayerDeathEvent e) {
			Player p = e.getEntity();
			if(inLobby(p)) {
				e.getDrops().clear();
				e.setDroppedExp(0);
			}
		}	
		
		@EventHandler
		public void onDamaged(EntityDamageEvent e) {
			if (e.getEntityType() == EntityType.PLAYER) {
				Player p = (Player) e.getEntity();
				if(inLobby(p)) {
					e.setCancelled(true);
					if(e.getCause() == DamageCause.VOID) {
						if(specList.containsKey(p.getName())) {
							p.teleport(specList.get(p.getName()).loc_Join, TeleportCause.PLUGIN);
						} else {
							p.teleport(lobby, TeleportCause.PLUGIN);
						}
					}
				}
			}
		}
		
		@EventHandler
		public void onItemDrop(PlayerDropItemEvent e) {
			Player p = e.getPlayer();
			if(inLobby(p)){
				Item dropItem = e.getItemDrop();
				ItemStack item = dropItem.getItemStack();
				if(item.getType() == Material.NETHER_STAR) {
					p.sendMessage(ms_alert+ChatColor.GRAY+"이 아이템은 버리실 수 없습니다.");
					e.setCancelled(true);
				}
			}
		}
		
		//갑옷 거치대 소환 방지
		@EventHandler
		public void onEntitySpawn(EntitySpawnEvent e) {
			if(!allowEntitySpawn) {
				if(e.getEntity().getType() == EntityType.ARMOR_STAND ||
						e.getEntity().getType() == EntityType.MINECART_TNT ||
						e.getEntity().getType() == EntityType.MINECART) {
					e.setCancelled(true);
				}
			}		
		}
		
		@EventHandler
		public void onBlockFade(BlockFadeEvent event) {
			if (((event.getBlock().getType() == Material.ICE) || (event.getBlock().getType() == Material.SNOW)
					|| (event.getBlock().getType() == Material.SNOW_BLOCK)))
				event.setCancelled(true);
		}
		
		@EventHandler
		public void onPlayerMove(PlayerMoveEvent event) {
			if(freeze) event.setTo(event.getFrom());
		}

		/*@EventHandler
		public void onBlockForm(BlockFormEvent event) {
			event.setCancelled(true);
		}*/

	}
	
}
	
	

