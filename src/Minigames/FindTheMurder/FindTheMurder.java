package Minigames.FindTheMurder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.core.config.Order;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.WeatherType;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;
import org.bukkit.util.Vector;
import org.golde.bukkit.corpsereborn.CorpseAPI.CorpseAPI;
import org.golde.bukkit.corpsereborn.nms.Corpses.CorpseData;

import com.coloredcarrot.api.sidebar.Sidebar;
import com.coloredcarrot.api.sidebar.SidebarString;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.connorlinfoot.titleapi.TitleAPI;

import EGServer.EGServer;
import EGServer.DataManger.PlayerData;
import EGServer.DataManger.MinigameData.FtmData;
import EGServer.DataManger.MinigameData.MinigameData;
import EGServer.ServerManager.EGEventHandler;
import EGServer.ServerManager.EGScheduler;
import Minigames.Minigame;
import Utility.MyUtility;

public class FindTheMurder extends Minigame {

	// 이벤트용
	public EventHandlerFTM event;

	//////////// public
	// 게임 시작시 랜덤 스폰
	public Location loc_vote;
	public Location loc_corpse;
	public List<Location> loc_startList = new ArrayList<Location>(20);

	///////////// private
	// 게임 플레이어 리스트
	private HashMap<String, FtmPlayer> ftmPlayerList = new HashMap<String, FtmPlayer>();
	private HashMap<String, String> virtualJobMap = new HashMap<String, String>(); //
	private HashMap<String, String> voteMap = new HashMap<String, String>(); // 누가 누구에게 투표했는지
	private HashMap<String, Integer> voteCnt = new HashMap<String, Integer>(); // 투표 얼마나 받았는지
	private HashMap<Location, myCorpse> corpseList = new HashMap<Location, myCorpse>();
	private List<String> murderTeam = new ArrayList<String>(3);
	private List<String> civilTeam = new ArrayList<String>(8);
	private List<ItemStack> chestItem = new ArrayList<ItemStack>(30);
	private List<Location> chestList = new ArrayList<Location>(40);
	private List<Location> doorLockList = new ArrayList<Location>(3);
	private int dayCnt;
	private int dayTime; // 0=아침, 1=낮, 2=밤
	private boolean voteTime = false;
	private boolean isMurderWin;
	private List<String> murderTeam_backUp = new ArrayList<String>(10);
	private List<String> civilTeam_backUp = new ArrayList<String>(10);
	
	//////////// 능력 관련
	private boolean soldier_use;
	private boolean reporter_use;
	private boolean police_use;
	private boolean spy_use;
	private boolean betrayered;
	private boolean voting;
	private boolean murderDenied;
	private boolean peopleKnowDeny;
	private boolean waiting;
	private boolean alreadyRole;
	private boolean alreadyKnow;
	private String doctorTarget;
	private String policeTarget;
	private String reportTarget;
	private String guardTarget;
	private String contractorTarget;
	private String negotiatorTarget;
	private String soul_target;
	private String priestTarget;
	public int keySmith_left = 4;
	private List<String> police_invensted = new ArrayList<String>();
	
	public String votedPlayer = "";
	
	private ItemStack murderKnife;

	//////// 게임 관련
	public String ms;
	public String murderMS;
	public int murderPenalty = 0;
	public int chatDistance = 20;
	private ItemStack rose_sword;
	private ItemStack spy_axe;
	private ItemStack crazy_pickAxe;
	private ItemStack key;
	private EGScheduler updateSch = new EGScheduler(this);
	
	////// 각종 인벤토리
	private Inventory detectorCorpse;
	private Inventory spyCorpse;
	public Inventory ftmHelper;
	public Inventory gameHelper;
	public Inventory gameHelperAb;
	public Inventory abListInven;
	public Inventory abilityInven;
	public Inventory murderAbInven;
	public Inventory playerInven;
	public Inventory playerInven_ab;
	
	//////// 사이드바
	private Sidebar jobSidebar;
	private List<SidebarString> textList = new ArrayList<SidebarString>(5);
	private Team scoreBoardTeam;
	
	public FindTheMurder(EGServer server, String gameName, String displayGameName, String cmdMain) {

		//////////////////// 필수 설정값
		super(server);
		ms = "§7[ §e! §7] §f: §c살인자를 찾아라 §f>> "; // 기본 메세지
		this.gameName = gameName;
		this.disPlayGameName = displayGameName;
		inventoryGameName = ChatColor.stripColor(displayGameName);
		minPlayer = 5;
		maxPlayer = 13;
		if(gameName.equalsIgnoreCase("FindTheMurder2")) {
			minPlayer = 7;
			maxPlayer = 11;
		}
		startCountTime = 60;
		this.cmdMain = cmdMain;
		canSpectate = false;
		doneSetting = loadGameData(); // 게임 시작 위치 등등 로드
		/////////////////////// 자동 설정(아이템등등)
		dirSetting(gameName);
		/////////////////// 게임 도움말 인벤토리
		ftmHelper = Bukkit.createInventory(null, 9, ""+ChatColor.BLACK+ChatColor.BOLD+"살인자를 찾아라");

		ItemStack item = new ItemStack(Material.BOOK, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§7- §e직업 능력");
		item.setItemMeta(meta);
		ftmHelper.setItem(1, item);
		meta.setDisplayName("§7- §a플레이어 현황");
		item.setItemMeta(meta);
		ftmHelper.setItem(3, item);
		meta.setDisplayName("§7- §c투표하기");
		item.setItemMeta(meta);
		ftmHelper.setItem(5, item);
		meta.setDisplayName("§7- §b게임 도우미");
		item.setItemMeta(meta);
		ftmHelper.setItem(7, item);
		////////////////
		
		/////////////////// 게임 도우미
		gameHelper = Bukkit.createInventory(null, 27, "§0§l"+inventoryGameName+" 도우미");

		item = new ItemStack(Material.STAINED_GLASS_PANE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName(" ");
		item.setItemMeta(meta);
		for (int i = 0; i <= 9; i++) {
			gameHelper.setItem(i, item);
		}
		for (int i = 17; i < 27; i++) {
			gameHelper.setItem(i, item);
		}
		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c게임");
		List<String> loreList = new ArrayList<String>();
		loreList.add("§7- 게임 진행방식, 승리조건,");
		loreList.add("§7   직업 목록등을 확인합니다.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		gameHelper.setItem(11, item);
		item = new ItemStack(Material.BOOK_AND_QUILL, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c게임명령어");
		loreList = new ArrayList<String>();
		loreList.add("§7- 이 게임에서 사용하실 수 있는");
		loreList.add("§7  명령어를 확인합니다.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		gameHelper.setItem(13, item);
		item = new ItemStack(Material.BOOKSHELF, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c게임규칙");
		loreList = new ArrayList<String>();
		loreList.add("§7- 게임을 플레이하며");
		loreList.add("§7  지켜야하는 규칙을 확인합니다.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		gameHelper.setItem(15, item);
		////////////////
		
		/////////////////// 게임 도우미 -> 게임 설명
		gameHelperAb = Bukkit.createInventory(null, 27, "§0§l"+inventoryGameName+" 게임설명");

		item = new ItemStack(Material.STAINED_GLASS_PANE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName(" ");
		item.setItemMeta(meta);
		for (int i = 0; i <= 9; i++) {
			gameHelperAb.setItem(i, item);
		}
		for (int i = 17; i < 27; i++) {
			gameHelperAb.setItem(i, item);
		}
		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c게임설명");
		loreList = new ArrayList<String>();
		loreList.add("§7- 게임 진행방식과 승리조건을 확인합니다.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		gameHelperAb.setItem(11, item);
		item = new ItemStack(Material.ENCHANTED_BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c직업목록");
		loreList = new ArrayList<String>();
		loreList.add("§7- 살인자를 찾아라의");
		loreList.add("§7  직업 목록을 확인합니다.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		gameHelperAb.setItem(15, item);
		////////////////
		
		playerInven = Bukkit.createInventory(null, 36, ""+ChatColor.BLACK+ChatColor.BOLD+"조사현황");
		playerInven_ab = Bukkit.createInventory(null, 36, ""+ChatColor.BLACK+ChatColor.BOLD+"능력사용");
		
		/////////////////// 게임 도우미 -> 직업목록
		abListInven = Bukkit.createInventory(null, 18, "§0§l"+inventoryGameName+" 직업목록");

		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c살인자");
		loreList.clear();
		loreList.add("§7- 클릭시 해당 직업의 설명을 확인합니다.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		abListInven.setItem(0, item);
		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c경찰");
		loreList.clear();
		loreList.add("§7- 클릭시 해당 직업의 설명을 확인합니다.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		abListInven.setItem(1, item);
		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c의사");
		loreList.clear();
		loreList.add("§7- 클릭시 해당 직업의 설명을 확인합니다.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		abListInven.setItem(2, item);
		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c배신자");
		loreList.clear();
		loreList.add("§7- 클릭시 해당 직업의 설명을 확인합니다.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		abListInven.setItem(3, item);
		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c군인");
		loreList.clear();
		loreList.add("§7- 클릭시 해당 직업의 설명을 확인합니다.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		abListInven.setItem(4, item);
		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c미치광");
		loreList.clear();
		loreList.add("§7- 클릭시 해당 직업의 설명을 확인합니다.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		abListInven.setItem(5, item);
		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c기자");
		loreList.clear();
		loreList.add("§7- 클릭시 해당 직업의 설명을 확인합니다.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		abListInven.setItem(6, item);
		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c성직자");
		loreList.clear();
		loreList.add("§7- 클릭시 해당 직업의 설명을 확인합니다.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		abListInven.setItem(7, item);
		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c마술사");
		loreList.clear();
		loreList.add("§7- 클릭시 해당 직업의 설명을 확인합니다.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		abListInven.setItem(8, item);
		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c발명가");
		loreList.clear();
		loreList.add("§7- 클릭시 해당 직업의 설명을 확인합니다.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		abListInven.setItem(9, item);
		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c경호원");
		loreList.clear();
		loreList.add("§7- 클릭시 해당 직업의 설명을 확인합니다.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		abListInven.setItem(10, item);
		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c탐정");
		loreList.clear();
		loreList.add("§7- 클릭시 해당 직업의 설명을 확인합니다.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		abListInven.setItem(11, item);
		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c농부");
		loreList.clear();
		loreList.add("§7- 클릭시 해당 직업의 설명을 확인합니다.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		abListInven.setItem(12, item);
		
		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c시민");
		loreList.clear();
		loreList.add("§7- 클릭시 해당 직업의 설명을 확인합니다.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		abListInven.setItem(13, item);
		
		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c계약자");
		loreList.clear();
		loreList.add("§7- 클릭시 해당 직업의 설명을 확인합니다.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		abListInven.setItem(14, item);
		
		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c영매사");
		loreList.clear();
		loreList.add("§7- 클릭시 해당 직업의 설명을 확인합니다.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		abListInven.setItem(15, item);
		
		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c협상가");
		loreList.clear();
		loreList.add("§7- 클릭시 해당 직업의 설명을 확인합니다.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		abListInven.setItem(16, item);
		
		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c열쇠공");
		loreList.clear();
		loreList.add("§7- 클릭시 해당 직업의 설명을 확인합니다.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		abListInven.setItem(17, item);
		

		////////////////
		
		/////////////////// 능력 도우미
		abilityInven = Bukkit.createInventory(null, 9, ""+ChatColor.BLACK+ChatColor.BOLD+"능력");

		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§b직업 확인");
		item.setItemMeta(meta);
		abilityInven.setItem(2, item);

		item = new ItemStack(Material.ENCHANTED_BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§c능력 사용");
		item.setItemMeta(meta);
		abilityInven.setItem(6, item);
		////////////////
		
		////// 탐정 시체  조사
		detectorCorpse = Bukkit.createInventory(null, 9, ""+ChatColor.BLACK+ChatColor.BOLD+"시체 조사");
		
		item = new ItemStack(Material.WOOL, 1, (byte)11);
		meta = item.getItemMeta();
		meta.setDisplayName("§b시체 조사하기");
		item.setItemMeta(meta);
		detectorCorpse.setItem(2, item);

		item = new ItemStack(Material.WOOL, 1, (byte)14);
		meta = item.getItemMeta();
		meta.setDisplayName("§c취소");
		item.setItemMeta(meta);
		detectorCorpse.setItem(6, item);
		
		////배신자 시체 제거
		spyCorpse = Bukkit.createInventory(null, 9, ""+ChatColor.BLACK+ChatColor.BOLD+"시체 제거");
		
		item = new ItemStack(Material.WOOL, 1, (byte)11);
		meta = item.getItemMeta();
		meta.setDisplayName("§b시체 제거하기");
		item.setItemMeta(meta);
		spyCorpse.setItem(2, item);

		item = new ItemStack(Material.WOOL, 1, (byte)14);
		meta = item.getItemMeta();
		meta.setDisplayName("§c취소");
		item.setItemMeta(meta);
		spyCorpse.setItem(6, item);
		
		///살인자 투표 거부
		murderAbInven = Bukkit.createInventory(null, 9, ""+ChatColor.BLACK+ChatColor.BOLD+"투표 거부 확인");
		
		item = new ItemStack(Material.WOOL, 1, (byte)11);
		meta = item.getItemMeta();
		meta.setDisplayName("§b투표 참여 거부");
		item.setItemMeta(meta);
		murderAbInven.setItem(2, item);

		item = new ItemStack(Material.WOOL, 1, (byte)14);
		meta = item.getItemMeta();
		meta.setDisplayName("§c취소");
		item.setItemMeta(meta);
		murderAbInven.setItem(6, item);

		chestItem.add(makeItem(Material.BONE, "쇠파이프", 3, 5)); // 0
		chestItem.add(makeItem(Material.STRING, "채찍", 4, 4)); // 1
		chestItem.add(makeItem(Material.QUARTZ, "접시조각", 2, 3)); // 2
		chestItem.add(makeItem(Material.IRON_SWORD, "식칼", 6, 7)); // 3
		chestItem.add(makeItem(Material.SHEARS, "가위", 5, 5)); // 4
		chestItem.add(makeItem(Material.BLAZE_ROD, "금속배트", 4, 5)); // 5
		chestItem.add(new ItemStack(Material.BOW, 1)); // 6
		chestItem.add(makeItem(Material.STONE_SPADE, "숟가락", 2, 2)); // 7
		chestItem.add(new ItemStack(Material.ARROW, 2)); // 8
		chestItem.add(makeGun(Material.IRON_HOE, "M9 권총", 7, 7)); // 9
		chestItem.add(makeItem(Material.MELON_SEEDS, "9mm탄환", 1, 1)); // 10
		chestItem.add(new ItemStack(Material.POTION, 1, (short) 16389)); // 11
		chestItem.add(new ItemStack(Material.POTION, 1, (short) 16421)); // 12
		chestItem.add(new ItemStack(Material.POTION, 1, (short) 16386)); // 13
		chestItem.add(new ItemStack(Material.POTION, 1, (short) 16388)); // 14
		chestItem.add(new ItemStack(Material.POTION, 1, (short) 16385)); // 15
		chestItem.add(makeItem(Material.WATCH, "투명 시계", 1, 1)); // 16
		chestItem.add(makeItem(Material.GOLD_AXE, "도끼", 5, 6)); // 17
		chestItem.add(makeItem(Material.IRON_SPADE, "삽", 5, 6)); // 18
		chestItem.add(makeItem(Material.MELON_SEEDS, "9mm탄환", 1, 1)); // 19
		chestItem.add(new ItemStack(Material.LEATHER_HELMET, 1)); // 20
		chestItem.add(new ItemStack(Material.LEATHER_CHESTPLATE, 1)); // 21
		chestItem.add(makeItem(Material.DIAMOND, "저항의 돌", 1, 1)); // 22

		murderKnife = makeItem(Material.GOLD_SWORD, "황금 식칼", 9, 9); // 0
		rose_sword = makeItem(Material.DIAMOND_SWORD, "장미칼", 7, 8);
		spy_axe = makeItem(Material.DIAMOND_AXE, "배신자 도끼", 7, 7);
		key = makeItem(Material.TRIPWIRE_HOOK, "만능 열쇠", 1, 1);
		crazy_pickAxe = makeItem(Material.IRON_PICKAXE, "미치광 곡괭이", 6, 6);
		//////////////////////

		jobSidebar = new Sidebar("§c조사현황", server, 600, new SidebarString(""));
		
		scoreBoardTeam = jobSidebar.getTheScoreboard().registerNewTeam("noNameTag");
		//team.setOption(Option.NAME_TAG_VISIBILITY, OptionStatus.NEVER);
		scoreBoardTeam.setNameTagVisibility(NameTagVisibility.HIDE_FOR_OTHER_TEAMS);
		scoreBoardTeam.setOption(Option.COLLISION_RULE, OptionStatus.NEVER);
		scoreBoardTeam.setCanSeeFriendlyInvisibles(false);
		
		//////////////////////////
		event = new EventHandlerFTM(server, this);
		// 이 플러그인에 이벤트 적용
		server.getServer().getPluginManager().registerEvents(event, server);
	}

	/////////////// Minigame에서 override 해야하는 부분
	@Override
	public void gameHelpMsg(Player p) {

	}

	@Override
	public void startGame() {
		if (ingamePlayer.size() < minPlayer) {
			sendMessage(ms + "게임 진행에 필요한 최소인원(" + minPlayer + ")을 충족하지 못했습니다.\n" + ms + "게임을 종료합니다.");
			endGame(false);
			return;
			// forceEnd();
		}
		server.egCM.broadCast(server.ms_alert + ChatColor.RED+ChatColor
				.BOLD+ disPlayGameName + ChatColor.GRAY + "이 시작 되었습니다!");
		initGame();
		CorpseData c = CorpseAPI.spawnCorpse(Bukkit.getPlayer(ingamePlayer.get(MyUtility.getRandom(0, ingamePlayer.size()-1))), loc_corpse);
		myCorpse corpse = new myCorpse("Limes_", "육군 19-17기", "입영통지서", 0, c, null);
		Location l = loc_corpse.clone();
		l.setY(l.getY()-1);
		corpseList.put(l, corpse);
		ingame = true;
		/////////////// 오프닝
		for(String tName : scoreBoardTeam.getEntries()) {
			scoreBoardTeam.removeEntry(tName);	
		}
		
		for (String pName : ingamePlayer) {
			scoreBoardTeam.addEntry(pName);
		}
		textList.clear();
		jobSidebar.setEntries(textList);
		
		jobSidebar.update();
		for(String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p)) 
				jobSidebar.showTo(p);
		}
		
		for (String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p)){
				MyUtility.setMaxHealth(p,35);
				// 풀피로 만듬
				MyUtility.healUp(p);
				MyUtility.allClear(p);
				MyUtility.attackDelay(p, false);
				p.setFallDistance(0);     
				TitleAPI.sendFullTitle(p, 10, 80, 30, ChatColor.RED+"게임 시작", ChatColor.RED+""+disPlayGameName);
			}
		}
		
		divideTeleport(loc_vote);
		
		waiting = true;
		setRole();
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				sendTitle(ChatColor.RED+"§l꼭!", "§e§l인벤토리 끝의 책을 우클릭하여 룰을 이해하세요!", 80);
				sendMessage(ms+"모든 직업에 대한 설명은 책을 우클릭 -> 직업목록 에서 확인가능합니다.");
				sendSound(Sound.BLOCK_NOTE_BELL, 1.0f, 2.0f);
			}
		}, 80l);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				sendTitle(ChatColor.RED+"직업 설정중...", "", 80);
				sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
			}
		}, 160l);
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				for(String s : ingamePlayer) {
					Player p = Bukkit.getPlayer(s);
					if(p!=null) {
						if(getJob(p).equalsIgnoreCase("살인자")) {
							p.sendMessage(ms+"당신은 원망하는 지인이 있었습니다.");
						} else if(getJob(p).equalsIgnoreCase("배신자")) {
							p.sendMessage(ms + "당신은 평소에 친하게 지내는 지인의 저택에 놀러 오라는 연락을 받았습니다.");
						} else if(getJob(p).equalsIgnoreCase("미치광")) {
							p.sendMessage(ms+"당신은 평소 사회에 불만을 품고 있는 사람이었습니다.");
						}  else if(getJob(p).equalsIgnoreCase("협상가")) {
							p.sendMessage(ms + "당신은 그리 친하지 않던 지인의 저택에 놀러 오라는 연락을 받았습니다.");
						}  else if(getJob(p).equalsIgnoreCase("계약자")) {
							p.sendMessage(ms + "당신은 청부 살인업자입니다.");
						} else {
							p.sendMessage(ms + "당신은 평소에 친하게 지내는 지인의 저택에 놀러 오라는 연락을 받았습니다.");
						}
					}
					p.setPlayerWeather(WeatherType.DOWNFALL);
				}
				sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
			}
		}, 200l);
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				for(String s : ingamePlayer) {
					Player p = Bukkit.getPlayer(s);
					if(p!=null) {
						if(getJob(p).equalsIgnoreCase("살인자")) {
							p.sendMessage(ms+"어느날 그 지인한테 자신의 저택에 놀러오라는 연락을 받았습니다.");
						} else if(getJob(p).equalsIgnoreCase("배신자")) {
							p.sendMessage(ms + "당신은 지인의 저택으로 향하였고");
						} else if(getJob(p).equalsIgnoreCase("미치광")) {
							p.sendMessage(ms+"경제적으로 매우 어려움에 처해있어 하루하루를 힘겹게 살아갔습니다.");
						}  else if(getJob(p).equalsIgnoreCase("협상가")) {
							p.sendMessage(ms + "당신은 지인의 저택으로 향하였고.");
						}  else if(getJob(p).equalsIgnoreCase("계약자")) {
							p.sendMessage(ms + "살인 청부 의뢰를 받은 당신은 몇일간 대상을 미행하였습니다.");
						} else {
							p.sendMessage(ms + "당신은 지인의 저택으로 향하였고");
						}
					}
				}
				sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
			}
		}, 230l);
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				for(String s : ingamePlayer) {
					Player p = Bukkit.getPlayer(s);
					if(p!=null) {
						if(getJob(p).equalsIgnoreCase("살인자")) {
							p.sendMessage(ms+"지인의 저택에는 지인 이외에 아무도 없었습니다.");
						} else if(getJob(p).equalsIgnoreCase("배신자")) {
							p.sendMessage(ms + "저택에 도착하였을 때 지인의 모습은 보이지 않았습니다.");
						} else if(getJob(p).equalsIgnoreCase("미치광")) {
							p.sendMessage(ms+"그러던 어느날 예전에 알던 지인에게 자신의 저택에 놀러오라는 초대를 받았습니다.");
						}  else if(getJob(p).equalsIgnoreCase("협상가")) {
							p.sendMessage(ms + "저택에 도착하였을  때 지인의 모습은 보이지 않았습니다.");
						}  else if(getJob(p).equalsIgnoreCase("계약자")) {
							p.sendMessage(ms + "그러던 중 대상이 산속의 저택으로 가게된다는 것을 알았습니다.");
						} else {
							p.sendMessage(ms + "저택에 도착하였을 때 지인의 모습은 보이지 않았습니다.");
						}
					}
				}
				sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
			}
		}, 260l);
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				for(String s : ingamePlayer) {
					Player p = Bukkit.getPlayer(s);
					if(p!=null) {
						if(getJob(p).equalsIgnoreCase("살인자")) {
							p.sendMessage(ms+"당신은 평소에 그 지인에게 원한을 품고있었고 충동적으로 지인을 살해하였습니다.");
						} else if(getJob(p).equalsIgnoreCase("배신자")) {
							p.sendMessage(ms + "당신 이외에도 초대를 받은 듯한 사람들이 있었으며");
						} else if(getJob(p).equalsIgnoreCase("미치광")) {
							p.sendMessage(ms+"지인에 집에 도착하자 어딘가에서 서로 다투는 듯한 목소리가 들려왔습니다.");
						}  else if(getJob(p).equalsIgnoreCase("협상가")) {
							p.sendMessage(ms + "지인을 찾으려 저택을 둘러보던 중");
						}  else if(getJob(p).equalsIgnoreCase("계약자")) {
							p.sendMessage(ms + "산속에서 처리를 하는 것이 간편할 것이라 생각한 당신은");
						} else {
							p.sendMessage(ms + "당신 이외에도 초대를 받은 듯한 사람들이 있었으며");
						}
					}
				} 
				sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
			}
		}, 290l);
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				for(String s : ingamePlayer) {
					Player p = Bukkit.getPlayer(s);
					if(p!=null) {
						if(getJob(p).equalsIgnoreCase("살인자")) {
							p.sendMessage(ms+"당신은 매우 당황하며 그 저택에서 도망치려고 하였지만");
						} else if(getJob(p).equalsIgnoreCase("배신자")) {
							p.sendMessage(ms + "저택에 도착하였지만 지인의 모습은 보이지 않았습니다.");
						} else if(getJob(p).equalsIgnoreCase("미치광")) {
							p.sendMessage(ms+"목소리의 근원지를 따라간 당신은 누군가가 지인을 살해하는 장면을 목격하게 됐습니다.");
						}  else if(getJob(p).equalsIgnoreCase("협상가")) {
							p.sendMessage(ms + "당신은 지인의 시체 옆에 서있는 누군가를 만났습니다.");
						}  else if(getJob(p).equalsIgnoreCase("계약자")) {
							p.sendMessage(ms + "초대받은 척하며 대상을 따라 저택으로 향했습니다.");
						} else {
							p.sendMessage(ms + "그 사람들과 함께 저택을 수색하였습니다.");
						}
					}
				}
				sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
			}
		}, 320l);
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				for(String s : ingamePlayer) {
					Player p = Bukkit.getPlayer(s);
					if(p!=null) {
						if(getJob(p).equalsIgnoreCase("살인자")) {
							p.sendMessage(ms+"창문으로 지인에게 초대를 받은듯한 다른 사람들이 도착하는 것이 보였습니다.");
						} else if(getJob(p).equalsIgnoreCase("배신자")) {
							p.sendMessage(ms + "당신은 수색을하며 저택에 있는 고가의 물건들을 보고 탐이나기 시작했습니다.");
						} else if(getJob(p).equalsIgnoreCase("미치광")) {
							p.sendMessage(ms+"하지만 당신이 느낀 감정은 두려움이나 놀람이 아닌");
						} else if(getJob(p).equalsIgnoreCase("협상가")) {
							p.sendMessage(ms + "당신은 지인의 시체를 보고 시체 옆에 서있는 사람이 살해를 저질렀다고 직감했습니다.");
						} else if(getJob(p).equalsIgnoreCase("계약자")) {
							p.sendMessage(ms + "하지만 이미 저택에서는 살인사건이 일어나있었습니다.");
						} else {
							p.sendMessage(ms + "그리고 수색도중 연회실에서 지인의 시체를 발견하였습니다.");
						}
					}
				}
				sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
			}
		}, 350l);
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				for(String s : ingamePlayer) {
					Player p = Bukkit.getPlayer(s);
					if(p!=null) {
						if(getJob(p).equalsIgnoreCase("살인자")) {
							p.sendMessage(ms+"당신은 저택의 빈방에 숨어있었지만 초대를 받은 다른 사람들이 저택을 수색하는 것을 보고");
						} else if(getJob(p).equalsIgnoreCase("배신자")) {
							p.sendMessage(ms + "그리고 수색도중 누군가가 연회실에서 지인의 시체를 발견하였습니다.");
						} else if(getJob(p).equalsIgnoreCase("미치광")) {
							p.sendMessage(ms+"저택에 있는 물건들을 훔쳐갈 수 있겠다는 기쁨이었습니다.");
						}  else if(getJob(p).equalsIgnoreCase("협상가")) {
							p.sendMessage(ms + "그 사람 또한 당신을 발견하고 가까이 왔습니다.");
						}  else if(getJob(p).equalsIgnoreCase("계약자")) {
							p.sendMessage(ms + "다만 이것은 당신에게 큰 기회입니다.");
						} else {
							p.sendMessage(ms + "지인은 분명히 어제까지만해도 살아있었습니다.");
						}
					}
				}
				sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
			}
		}, 380l);
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				for(String s : ingamePlayer) {
					Player p = Bukkit.getPlayer(s);
					if(p!=null) {
						if(getJob(p).equalsIgnoreCase("살인자")) {
							p.sendMessage(ms+"방금 도착한 것처럼 모습을 드러내었습니다.");
						} else if(getJob(p).equalsIgnoreCase("배신자")) {
							p.sendMessage(ms + "지인의 시체를 본 당신은 저택의 물건을 훔치고 싶은 마음이 점점 강해지고 있습니다.");
						} else if(getJob(p).equalsIgnoreCase("미치광")) {
							p.sendMessage(ms+"당신은 살해현장을 뒤로하고 저택에서 비싸보이는 물건들을 훔치려하였으나");
						}  else if(getJob(p).equalsIgnoreCase("협상가")) {
							p.sendMessage(ms + "그는 당신에게 이 사건을 덮는것을 도와주면 사례를 하겠다며 제안했습니다.");
						}  else if(getJob(p).equalsIgnoreCase("계약자")) {
							p.sendMessage(ms + "당신은 계약 대상을 살해한 후 살인자와 협력하면");
						} else {
							p.sendMessage(ms + "지금 이 자리에 있는 누군가가 지인을 살해한 것이 분명합니다.");
						}
					}
				}
				sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
			}
		}, 410l);
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				for(String s : ingamePlayer) {
					Player p = Bukkit.getPlayer(s);
					if(p!=null) {
						if(getJob(p).equalsIgnoreCase("살인자")) {
							p.sendMessage(ms+"사람들은 아직 당신이 살인을 저지른 범인이라는 것을 모르지만 들키는 것은 시간문제인듯 합니다.");
						} else if(getJob(p).equalsIgnoreCase("배신자")) {
							p.sendMessage(ms + "당신이 마음만 먹는다면 살인자에게 협력하여 사람들을 해치고 물건을 훔쳐갈 수 있습니다.");
						} else if(getJob(p).equalsIgnoreCase("미치광")) {
							p.sendMessage(ms+"자신 이외에도 초대를 받은듯한 사람들이 저택에 도착해버렸습니다.");
						}  else if(getJob(p).equalsIgnoreCase("협상가")) {
							p.sendMessage(ms + "살해된 지인과 그리 친하지 않던 당신은 흔쾌히 수락했습니다.");
						}  else if(getJob(p).equalsIgnoreCase("계약자")) {
							p.sendMessage(ms + "이 저택의 사건을 덮는 것이 가능할 것입니다.");
						} else {
							p.sendMessage(ms + "당신이 지인을 살해한 살인자를 잡아내야합니다.");
						}
					}
				}
				sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
			}
		}, 440l);
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				for(String s : ingamePlayer) {
					Player p = Bukkit.getPlayer(s);
					if(p!=null) {
						if(getJob(p).equalsIgnoreCase("살인자")) {
							p.sendMessage(ms+"당신이 살인을 저질렀다는 것을 들키지않기 위하여 당신을 잡아내려하는 사람들을 모두 살해하십시오!");
						} else if(getJob(p).equalsIgnoreCase("배신자")) {
							p.sendMessage(ms + "당신은 어떻게 하시겠습니까?");
						} else if(getJob(p).equalsIgnoreCase("미치광")) {
							p.sendMessage(ms+"당신은 살인자에게 협조하여 다른 사람들을 모두 살해하고 저택에 있는 물건들을 훔쳐 가려고하고 있습니다.");
						}  else if(getJob(p).equalsIgnoreCase("협상가")) {
							p.sendMessage(ms + "살인자와 협력하여 저택의 사건을 미궁 속으로 빠뜨리십시오.");
						}  else if(getJob(p).equalsIgnoreCase("계약자")) {
							p.sendMessage(ms + "자, 이제 의뢰를 달성해볼까요?");
						} else {
							p.sendMessage(ms + "당신의 직업을 이용하고 사람들과 소통하여 살인자를 찾아내세요!");
						}
					}
				}
				sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
			}
		}, 470l);
		///////////////// 진짜 시작
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				randomTp(ingamePlayer);
				for(String pName : ingamePlayer) {
					Player p = Bukkit.getPlayer(pName);
					if(p != null) {
						p.setGameMode(GameMode.SURVIVAL);
						tellJob(p);
					}
				}
				sendSound(Sound.BLOCK_SLIME_BREAK, 1.0f, 1.3f);
				waiting = false;
				giveBasicItem();
				updatePlayerInven();
				Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
					public void run() {
						dayCycle();
					}
				}, 100l);
			}
		}, 550l);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				sendTitle(ChatColor.GREEN+"직업에 대한 정보", "§e§l책을 우클릭하여 확인하세요.", 80);
			}
		}, 600l);
			
	}

	//////////////////
	
	public void setRankMap(String pName) {
		PlayerData data = server.egDM.getPlayerData(pName);
		String rank = "나무";
		if(data != null) {
			MinigameData gameData = data.getGameData("FindTheMurder");
			if(gameData != null) {
				rank = gameData.getRankName();	
			}
		}
		rankMap.put(pName, rank);
	}

	public void initGame() {
		lobbyStart = false;
		isMurderWin = false;
		alreadyRole = false;
		alreadyKnow = false;
		voteTime = false;
		ftmPlayerList.clear();
		virtualJobMap.clear();
		doorLockList.clear();
		voteMap.clear();
		voteCnt.clear();
		List<Location> keyList = new ArrayList<Location>(corpseList.keySet());
		for(Location l : keyList){
			corpseList.get(l).deleteCorpse();;
		}
		corpseList.clear();
		murderTeam.clear();
		civilTeam.clear();
		chestList.clear();
		police_invensted.clear();

		murderTeam_backUp.clear();
		civilTeam_backUp.clear();
		
		//능력
		soldier_use = false;
		reporter_use = false;
		police_use = false;
		spy_use = false;
		betrayered = false;
		voting = false;
		murderDenied = false;
		peopleKnowDeny = false;
		ending = false;
		doctorTarget = null;
		policeTarget = null;
		reportTarget = null;
		guardTarget = null;
		soul_target = null;
		contractorTarget = null;
		negotiatorTarget = null;
		votedPlayer = "";
		
		//환경
		murderPenalty = 0;
		dayCnt = 0;
		dayTime = 2;
		
		//스케쥴
		schList.clear();
		
		clearClickMap();
	}

	public boolean loadGameData() {
		boolean ret = true;
		loc_Join = loadLocation(gameName, "JoinPos");
		loc_vote = loadLocation(gameName, "votePos");
		int startPosCnt = 0;
		try {
			File file = new File(server.getDataFolder().getPath() + "/" + gameName + "/Location", "location.yml");
			FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
			startPosCnt = fileConfig.getInt("startPosCnt");
		} catch (Exception e) {

		}
		loc_startList.clear();
		for (int num = 1; num <= startPosCnt; num++) {
			loc_startList.add(loadLocation(gameName, "StartPos" + num));
		}
		if (loc_startList.size() == 0) {
			server.egPM.printLog(ms + "[" + disPlayGameName + "] 게임 시작 지점이 설정되지 않았습니다.");
			ret = false;
		}
		if (loc_Join == null) {
			server.egPM.printLog(ms + "[" + disPlayGameName + "] 게임 시작 대기 지점이 설정되지 않았습니다.");
			ret = false;
		}
		if (loc_vote == null) {
			server.egPM.printLog(ms + "[" + disPlayGameName + "] 투표 지점이 설정되지 않았습니다.");
			ret = false;
		}else {
			loc_corpse = loc_vote.clone().add(2,0,2);
		}
		
		if(ret == true) {
			server.egPM.printLog("[" + disPlayGameName + "] 설정 이상 없음");
			doneSetting = ret;
		}
		
		return ret;
	}

	
	private void updatePlayerInven() {
		
		List<String> keyList = new ArrayList<String>(virtualJobMap.keySet());	
		
		textList.clear();
		
		for(int i = 0; i < keyList.size(); i++) {
			String name = keyList.get(i);
			if(name.length() > 5) name = name.substring(0, 6);
			
			SidebarString line = new SidebarString(ChatColor.GOLD+name+" : "+ChatColor.GOLD+virtualJobMap.get(keyList.get(i)));
			textList.add(line);
		}
		
		jobSidebar.setEntries(textList);
		
		jobSidebar.update();
		for(String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p)) 
				jobSidebar.showTo(p);
		}
		
		///////////////////
		playerInven.clear();
		playerInven_ab.clear();
		updateSch.schTime = ingamePlayer.size();
		updateSch.schTime2 = 1;
		updateSch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				if(updateSch.schTime > 0) {
					updateSch.schTime--;
					String pName = ingamePlayer.get(updateSch.schTime);
					Player p = Bukkit.getPlayer(pName);
					if(existPlayer(p)) {
						ItemStack pHead = getHead(p);
						ItemMeta meta = pHead.getItemMeta();
						meta.setDisplayName(pName);
						List<String> loreList = new ArrayList<String>(1);
						loreList.add("§7직업: "+virtualJobMap.get(pName));
						meta.setLore(loreList);
						pHead.setItemMeta(meta);
						playerInven.setItem(updateSch.schTime2, pHead);	
						playerInven_ab.setItem(updateSch.schTime2, pHead);
						if(updateSch.schTime2 == 7 || updateSch.schTime2 == 16 || updateSch.schTime2 == 25) updateSch.schTime2 += 3;
						else updateSch.schTime2 += 2;
					}
				} else {
					updateSch.cancelTask(false);
				}
			}
		}, 0l, 3l);
		/////////////////////
		
		///////////////////
	}
	
	private void randomTp(List<String> ingamePlayer) {
		for (String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p))
				p.teleport(loc_startList.get(MyUtility.getRandom(0, loc_startList.size() - 1)), TeleportCause.PLUGIN);
		}
	}
	
	/////////// 인벤토리 클릭 ////////////////
	
	public void gameHelper(Player p, int slot) {
		switch (slot) {
		case 11:
			p.closeInventory();
			p.openInventory(gameHelperAb);
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
			return;
			
		case 13:
			p.sendMessage("\n§f/팀챗 or tc - §c팀원에게 메세지를 보냅니다. §7( 살인자팀만 가능 )");
			sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
			p.closeInventory();
			return;
			
		case 15:
			p.sendMessage("\n§f게임 도중 사망 또는 퇴장한 뒤에는 게임에 관여하실 수 없습니다.");
			p.sendMessage("§f확성기를 이용한 직업 공개 등의 행위는 불가능합니다.");
			p.sendMessage("§f살인자 팀은 고의로 같은 팀원을 죽일시 경고를 받으실 수 있습니다.");
			p.sendMessage("§c※ 살인자 팀은 서로를 때릴시 팀원임을 알 수 있습니다.");
			sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
			p.closeInventory();
			return;

		default:
			return;
		}
	}
	
	public void ftmHelperInvenClick(Player p, int slot) {
		p.closeInventory();
		switch (slot) {
		case 1:
			p.openInventory(abilityInven);
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
			return;

		case 3:
			p.openInventory(playerInven);
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
			return;
			
		case 5:
			if(voteTime) {
				p.openInventory(playerInven);
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
			}
			else p.sendMessage(ms+"투표 시간이 아닙니다.");
			return;
			
		case 7:
			p.openInventory(gameHelper);
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
			return;

		default:
			return;
		}
	}
	
	public void divideTeleport(Location l) {	
		EGScheduler sch = new EGScheduler(this);
		sch.schTime = 0;
		sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				if(sch.schTime < ingamePlayer.size()) {
					String pName = ingamePlayer.get(sch.schTime);
					Player p = Bukkit.getPlayer(pName);
					if(existPlayer(p)) {
						p.teleport(l);
					}
					sch.schTime++;
				}else {
					sch.cancelTask(true);
				}
			}
		}, 0l, 2l);
	}
	
	public void gameHelperAbilityClick(Player p, int slot) {
		p.closeInventory();
		p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
		jobHelpMsg(p, slot);
	}
	
	public void gameHelperAbClick(Player p, int slot) {
		p.closeInventory();
		switch (slot) {
		case 11:
			p.sendMessage("\n§7게임 이름 §f: §c살인자를 찾아라");
			p.sendMessage("§f서로의 직업을 탐색하여 승리조건을 달성하는");
			p.sendMessage("§f추리와 심리게임입니다.");
			p.sendMessage("§f게임은 살인자팀, 시민팀 두 진영 나뉘어 진행되며");
			p.sendMessage("§f상대 진영에 있는 플레이어를 전부 처리하면 승리합니다.");
			p.sendMessage("§f다만 시민팀은 서로가 적인지 아군인지 기본적으로는");
			p.sendMessage("§f알지 못하며 시민팀에 속한 플레이어가 같은 시민팀인 아군을");
			p.sendMessage("§f죽일시 죽은 플레이어 대신 죽인 플레이어가 사망하게 됩니다.");
			p.sendMessage("§c※ 맵 곳곳에 있는 상자를 우클릭시 아이템을 얻으실 수 있습니다.");
			p.sendMessage("§c※ 서로간의 채팅은 건물 같은층 20칸내 플레이어에게만 전달됩니다.");
			sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
			return;
			
		case 15:
			p.openInventory(abListInven);
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
			return;
			
		default:
			return;
		}
	}
	
	public void abilityInvenClick(Player p, int slot) {
		p.closeInventory();
		switch (slot) {
		case 2:
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
			jobHelpMsg(p);
			return;
		case 6:
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
			String job = getJob(p);
			if(job.equalsIgnoreCase("살인자")) {
				if(murderDenied) p.sendMessage(ms+"이미 능력을 사용하신 상태입니다.");
				else {
					p.openInventory(murderAbInven);
					//p.sendMessage(ms+"정말로 투표에 참여하지 않겠습니까?");
					ActionBarAPI.sendActionBar(p, ChatColor.RED+ "정말로 투표에 참여하지 않겠습니까?", 80);
				}
			} else if(job.equalsIgnoreCase("경찰")) {
				p.openInventory(playerInven_ab); 
				//p.sendMessage(ms+"조사할 플레이어를 선택해주세요.");
				ActionBarAPI.sendActionBar(p, ChatColor.RED + "조사할 플레이어를 선택해주세요.", 80);
			} else if(job.equalsIgnoreCase("의사")) {
				p.openInventory(playerInven_ab);
				//p.sendMessage(ms+"수술을 준비할 플레이어를 선택해주세요.");
				ActionBarAPI.sendActionBar(p, ChatColor.RED + "수술을 준비할 플레이어를 선택해주세요.", 80);
			} else if(job.equalsIgnoreCase("배신자")) {
				if(civilTeam.contains(p.getName())) {
					p.openInventory(playerInven_ab);
					//p.sendMessage(ms+"직업을 알아낼 플레이어를 선택해주세요.");
					ActionBarAPI.sendActionBar(p, ChatColor.RED + "직업을 염탐할 플레이어를 선택해주세요.", 80);
				}
				else p.sendMessage(ms+"접선을 완료한 상태에서는 능력이 시체제거로 바뀝니다.\n"+ms+"시체위에서 우클릭하여 제거가 가능합니다.");
			} else if(job.equalsIgnoreCase("군인")) {
				p.sendMessage(ms+"당신의 능력은 특정 대상을 지목하는 능력이 아닙니다.");
			} else if(job.equalsIgnoreCase("미치광")) {
				p.sendMessage(ms+"당신의 능력은 특정 대상을 지목하는 능력이 아닙니다.");
			} else if(job.equalsIgnoreCase("기자")) {
				p.openInventory(playerInven_ab);
				//p.sendMessage(ms+"기사를 작성할 플레이어를 선택해주세요.");
				ActionBarAPI.sendActionBar(p, ChatColor.RED + "기사를 작성할 플레이어를 선택해주세요.", 80);		
			} else if(job.equalsIgnoreCase("성직자")) {
				p.sendMessage(ms+"당신의 능력은 특정 대상을 지목하는 능력이 아닙니다.");
			} else if(job.equalsIgnoreCase("마술사")) {
				p.openInventory(playerInven_ab);
				//p.sendMessage(ms+"직업을 빼앗을 플레이어를 선택해주세요.");
				ActionBarAPI.sendActionBar(p, ChatColor.RED + "직업을 빼앗을 플레이어를 선택해주세요.", 80);		
			} else if(job.equalsIgnoreCase("발명가")) {
				p.sendMessage(ms+"당신의 능력은 특정 대상을 지목하는 능력이 아닙니다.");
			} else if(job.equalsIgnoreCase("경호원")) {
				p.openInventory(playerInven_ab);
				//p.sendMessage(ms+"보호할 플레이어를 선택해주세요.");
				ActionBarAPI.sendActionBar(p, ChatColor.RED + "보호할 플레이어를 선택해주세요.", 80);
			} else if(job.equalsIgnoreCase("탐정")) {
				p.sendMessage(ms+"당신의 능력은 특정 대상을 지목하는 능력이 아닙니다.");
			} else if(job.equalsIgnoreCase("농부")) {
				p.sendMessage(ms+"당신의 능력은 특정 대상을 지목하는 능력이 아닙니다.");
			} else if(job.equalsIgnoreCase("시민")) {
				p.sendMessage(ms+"당신은 특별한 능력이 없습니다.");
			}else if(job.equalsIgnoreCase("계약자")) {
				p.openInventory(playerInven_ab);
				ActionBarAPI.sendActionBar(p, ChatColor.RED + "살해 대상을 선택해주세요.", 80);		
			}else if(job.equalsIgnoreCase("영매사")) {
				p.openInventory(playerInven_ab);
				ActionBarAPI.sendActionBar(p, ChatColor.RED + "영혼에 접근할 대상을 선택해주세요.", 80);		
			}else if(job.equalsIgnoreCase("협상가")) {
				p.openInventory(playerInven_ab);
				ActionBarAPI.sendActionBar(p, ChatColor.RED + "포섭 대상을 선택해주세요.", 80);		
			}else if(job.equalsIgnoreCase("열쇠공")) {
				p.sendMessage(ms+"당신의 능력은 특정 대상을 지목하는 능력이 아닙니다.");
			} 
			return;
			
		default:
			return;
		}
	}
	
	public void useAbility(Player p, int slot) {
		p.closeInventory();
		if(dayCnt <= 0) return;
		
		String job = getJob(p);
		String target = null;
		ItemStack item = playerInven.getItem(slot);
		if(item == null) return;
		if(item.hasItemMeta()) {
			ItemMeta meta = playerInven.getItem(slot).getItemMeta();
			if(meta.hasDisplayName()) {
				target = meta.getDisplayName();
			}
		}
		if(target == null) return;
		if(job.equalsIgnoreCase("경찰")) {
			if(dayTime == 0) {
				if(policeTarget == null) {
					if(target.equalsIgnoreCase(p.getName())) {
						p.sendMessage(ms+"자신을 지정할 수 없습니다.");
					} else {
						policeTarget = target;
						TitleAPI.sendFullTitle(p, 10, 40, 30, ChatColor.GRAY+"조사 시작", ChatColor.RESET+target);
						p.sendMessage(ms+target+"님을 조사합니다. 조사결과는 밤에 알게됩니다.");
					}
				} else {
					p.sendMessage(ms+"이미 "+policeTarget+"님을 조사중입니다.");
				}
			} else {
				p.sendMessage(ms+"아침에만 가능합니다.");
			}
		} else if(job.equalsIgnoreCase("의사")) {
			if(dayTime == 0) {
				if(doctorTarget == null) {
					if(target.equalsIgnoreCase(p.getName())) {
						p.sendMessage(ms+"자신을 지정할 수 없습니다.");
					} else {
						doctorTarget = target;
						p.sendMessage(ms+target+"님의 수술을 준비합니다.");
						TitleAPI.sendFullTitle(p, 10, 40, 30, ChatColor.GRAY+"수술 준비", ChatColor.RESET+target);
					}
				} else {
					p.sendMessage(ms+"이미 "+doctorTarget+"님의 수술을 준비중입니다.");
				}
			} else {
				p.sendMessage(ms+"아침에만 가능합니다.");
			}
		} else if(job.equalsIgnoreCase("배신자")) {
			if(dayTime == 2) {
				if(target.equalsIgnoreCase(p.getName())) {
					p.sendMessage(ms+"자신을 지정할 수 없습니다.");
				} else {
					if(!spy_use) {
						spy_use = true;
						String tJob = getJob(target);
						p.sendMessage(ms+target+"님의 직업은 "+tJob+"입니다.");
						if(tJob.equalsIgnoreCase("군인")) {
							Bukkit.getPlayer(target).sendMessage(ms+"배신자 "+p.getName()+" 님이 당신을 조사하였습니다.");
						}
						if(tJob.equalsIgnoreCase("살인자") || tJob.equalsIgnoreCase("협상가")) {
							p.sendMessage(ms+"접선에 성공하였습니다. \"§c/tc <메세지>\" §f명령어로 살인자팀만의 대화가 가능합니다.");
							sendTeamChat(murderTeam, ms+"배신자 "+p.getName()+"님이 접선하였습니다. 이제 그도 당신의 동료입니다.");
							TitleAPI.sendFullTitle(p, 10, 40, 30, ChatColor.GRAY+"접선 성공", ChatColor.RESET+target);
							sendTeamSound(murderTeam, Sound.BLOCK_NOTE_HARP);
							if(!murderTeam.contains(p.getName()))murderTeam.add(p.getName());	
							civilTeam.remove(p.getName());
							if(civilTeam.size() <= 0) {
								murderWin();
							}
							p.getInventory().addItem(spy_axe);
							FtmPlayer ftmP = ftmPlayerList.get(p.getName());
							if(ftmP != null) ftmP.spy_contact += 1;
						}
					}else {
						p.sendMessage(ms+"이미 능력을 사용했습니다.");
					}			
				}
			} else {
				p.sendMessage(ms+"밤에만 가능합니다.");
			}
		} else if(job.equalsIgnoreCase("기자")) {
			if(dayTime == 2) {
				if(!reporter_use) {
					if(reportTarget == null) {
						reportTarget = target;
						reporter_use = true;
						p.sendMessage(ms+target+" 님에 대해 기사를 작성합니다. 발표는 다음날 아침입니다.");
					} else {
						p.sendMessage(ms+"이미 "+target+" 님에 대한 기사를 작성중입니다.");
						TitleAPI.sendFullTitle(p, 10, 40, 30, ChatColor.GRAY+"기사 작성", ChatColor.RESET+target);
					}
				} else {
					p.sendMessage(ms+"이미 "+reportTarget+" 님에 대해 기사를 작성하였습니다.");
				}
			} else {
				p.sendMessage(ms+"밤에만 가능합니다.");
			}
		} else if(job.equalsIgnoreCase("마술사")) {
			if(dayCnt >= 2) {
				if(dayTime == 2) {
					if(target.equalsIgnoreCase(p.getName())) {
						p.sendMessage(ms+"자신을 지정할 수 없습니다.");
					} else {
						String tJob = getJob(target);
						if(!tJob.equalsIgnoreCase("미치광")) {
							Player t = Bukkit.getPlayer(target);
							p.sendMessage(ms+target+" 님의 직업을 빼앗았습니다.");
							FtmPlayer ftmP = ftmPlayerList.get(p.getName());
							if(ftmP != null) ftmP.magician_take += 1;
							if(!tJob.equalsIgnoreCase("살인자")) {
								boolean isMurderTeam = murderTeam.contains(t.getName());
								t.playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1.5f, 1.5f);
								p.playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1.5f, 1.5f);
								setFullJob(t, 13);
								t.sendMessage(ms+"마술사가 당신의 직업을 빼앗아 갔습니다. 당신의 직업은 시민으로 변경됩니다.");
								setFullJob(p, getJobCode(tJob));
								if(murderTeam.contains(p.getName())) murderTeam.remove(p.getName());
								if(tJob.equalsIgnoreCase("배신자") && isMurderTeam) {
									p.sendMessage(ms+target+"님이 접선을 완료한 배신자였기에 접선상태가 됩니다.\n"+ms+"\"/tc <메세지> \"명령어로 살인자팀만의 대화가 가능합니다.");
									sendTeamChat(murderTeam, ms+"마술사였던 배신자 "+p.getName()+"님이 접선하였습니다. 이제 그도 당신의 동료입니다.");
									sendTeamSound(murderTeam, Sound.BLOCK_NOTE_HARP);
									murderTeam.remove(target);
									if(!murderTeam.contains(p.getName()))murderTeam.add(p.getName());
									civilTeam.remove(p.getName());
									if(police_invensted.contains(p.getName())) {
										for(String tmpName : ingamePlayer) {
											if(getJob(tmpName).equalsIgnoreCase("경찰")) {
												Player tmp = Bukkit.getPlayer(tmpName);
												if(existPlayer(tmp)) {
													tmp.sendMessage(ms+"§f당신이 조사했던 §c"+target+" 님이 살인자의 동료가 된 것 같습니다.");
													tmp.playSound(tmp.getLocation(), Sound.BLOCK_NOTE_HARP, 1.0f, 1.0f);
												}
												break;
											}
										}
									}
									if(ftmP != null) ftmP.magician_takeMurderTeam += 1;
									if(civilTeam.size() <= 0) {
										murderWin();
									}
									p.getInventory().addItem(spy_axe);
								} else if(tJob.equalsIgnoreCase("계약자") && isMurderTeam) {
									p.sendMessage(ms+target+"님이 의뢰를 완료한 계약자였기에 살인마의 동료가 됩니다.\n"+ms+"\"/tc <메세지> \"명령어로 살인자팀만의 대화가 가능합니다.");
									sendTeamChat(murderTeam, ms+"마술사였던 계약자 "+p.getName()+"님이 새롭게 동료가 됐습니다. 이제 그도 당신의 동료입니다.");
									sendTeamSound(murderTeam, Sound.BLOCK_NOTE_HARP);
									murderTeam.remove(target);
									if(!murderTeam.contains(p.getName()))murderTeam.add(p.getName());
									civilTeam.remove(p.getName());
									if(police_invensted.contains(p.getName())) {
										for(String tmpName : ingamePlayer) {
											if(getJob(tmpName).equalsIgnoreCase("경찰")) {
												Player tmp = Bukkit.getPlayer(tmpName);
												if(existPlayer(tmp)) {
													tmp.sendMessage(ms+"§f당신이 조사했던 §c"+target+" 님이 살인자의 동료가 된 것 같습니다.");
													tmp.playSound(tmp.getLocation(), Sound.BLOCK_NOTE_HARP, 1.0f, 1.0f);
												}
												break;
											}
										}
									}
									if(ftmP != null) ftmP.magician_takeMurderTeam += 1;
									if(civilTeam.size() <= 0) {
										murderWin();
									}
									if(ftmP != null) ftmP.magician_takeMurderTeam += 1;
								} else if(tJob.equalsIgnoreCase("협상가") && isMurderTeam) {
									murderTeam.remove(target);
									if(!murderTeam.contains(p.getName()))murderTeam.add(p.getName());
									civilTeam.remove(p.getName());
									if(police_invensted.contains(p.getName())) {
										for(String tmpName : ingamePlayer) {
											if(getJob(tmpName).equalsIgnoreCase("경찰")) {
												Player tmp = Bukkit.getPlayer(tmpName);
												if(existPlayer(tmp)) {
													tmp.sendMessage(ms+"§f당신이 조사했던 §c"+target+" 님이 살인자의 동료가 된 것 같습니다.");
													tmp.playSound(tmp.getLocation(), Sound.BLOCK_NOTE_HARP, 1.0f, 1.0f);
												}
												break;
											}
										}
									}
									if(ftmP != null) ftmP.magician_takeMurderTeam += 1;
									if(civilTeam.size() <= 0) {
										murderWin();
									}
								}
							} else {
								t.playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1.5f, 1.5f);
								p.playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1.5f, 1.5f);
								if(peopleKnowDeny) {
									sendMessage(ms+"살인자 "+target+"님은 마술사의 능력에 의해 직업을 빼앗겨 사망하였습니다.\n"+ms+"살인자의 정체는 다시 미궁속으로 들어갔습니다.");
									peopleKnowDeny = false;
								} else {
									sendMessage(ms+"살인자 "+target+"님은 마술사의 능력에 의해 직업을 빼앗겨 사망하였습니다.\n"+ms+"이제 마술사가 살인자가 되었습니다.");
								}
								if(murderDenied) murderDenied = false;
								setFullJob(t, 13);
								t.sendMessage(ms+"마술사가 당신의 직업을 빼앗아 갔습니다. 당신의 직업은 시민으로 변경됩니다.\n"+ms+"마술사가 당신을 처형하였습니다.");
								t.setHealth(0);
								sendTeamChat(murderTeam, ms+"마술사였던 "+p.getName()+"님이 살인자의 직업을 빼앗았습니다. 이제 그도 당신의 동료입니다.");
								sendTeamSound(murderTeam, Sound.BLOCK_NOTE_HARP);
								setFullJob(p, 0);
								if(ftmP != null) ftmP.magician_takeMurderTeam += 1;
								civilTeam.remove(p.getName());
								if(police_invensted.contains(p.getName())) {
									for(String tmpName : ingamePlayer) {
										if(getJob(tmpName).equalsIgnoreCase("경찰")) {
											Player tmp = Bukkit.getPlayer(tmpName);
											if(existPlayer(tmp)) {
												tmp.sendMessage(ms+"§f당신이 조사했던 §c"+target+" 님이 살인자의 동료가 된 것 같습니다.");
												tmp.playSound(tmp.getLocation(), Sound.BLOCK_NOTE_HARP, 1.0f, 1.0f);
											}
											break;
										}
									}
								}
								if(civilTeam.size() <= 0) {
									murderWin();
								}
							}
						} else {
							p.sendMessage(ms+"미치광의 직업은 빼앗을 수 없습니다.");
						}
					}
				} else {
					p.sendMessage(ms+"밤에만 가능합니다.");
				}
			} else {
				p.sendMessage(ms+"마술사의 능력은 2일째부터 사용이 가능합니다.");
			}
		} else if(job.equalsIgnoreCase("경호원")) {
			if(dayTime == 0 || dayTime == 1) {
				if(guardTarget == null) {
					if(target.equalsIgnoreCase(p.getName())) {
						p.sendMessage(ms+"자신을 지정할 수 없습니다.");
					} else {
						guardTarget = target;
						p.sendMessage(ms+target+" 님을  보호합니다.\n"+ms+"이 플레이어는 낮에 있을 포박 투표에서 포박되지 않으며 당신의 도움을 받습니다.");
						TitleAPI.sendFullTitle(p, 10, 40, 30, ChatColor.GRAY+"보호 시작", ChatColor.RESET+target);
					}	
				} else {
					p.sendMessage(ms+"이미 "+target+" 님에 보호중입니다.");
				}
			} else {
				p.sendMessage(ms+"아침 또는 낮에만 가능합니다.");
			}
		} else if(job.equalsIgnoreCase("계약자")) {
			if (contractorTarget == null) {
				if (dayTime == 0) {
					if (target.equalsIgnoreCase(p.getName())) {
						p.sendMessage(ms + "자신을 지정할 수 없습니다.");
					} else {
						if(getJob(target).equalsIgnoreCase("미치광")) {
							p.sendMessage(ms + "미치광은 지정할 수 없습니다.");
						}else {
							contractorTarget = target;
							p.sendMessage(ms + target + " 님이 이제 살해 대상입니다.\n" + ms + "이 플레이어가 사망시 당신은 살인자 팀이됩니다.\n" + ms
									+ "당신이 이 플레이어를 죽이는 것도 가능합니다.");
							TitleAPI.sendFullTitle(p, 10, 40, 30, ChatColor.GRAY + "의뢰 시작", ChatColor.RESET + target);
						}				
					}
				} else {
					p.sendMessage(ms + "아침에만 가능합니다.");
				}
			} else {
				p.sendMessage(ms + "이미 " + contractorTarget + " 님이 살해 대상입니다.");
			}
		} else if(job.equalsIgnoreCase("협상가")) {
			if (negotiatorTarget == null) {
				if (dayTime == 2) {
					if(dayCnt >= 2) {
						if (target.equalsIgnoreCase(p.getName())) {
							p.sendMessage(ms + "자신을 지정할 수 없습니다.");
						} else {
							if(murderTeam.contains(target)) {
								p.sendMessage(ms + "그는 이미 동료입니다.");
							} else {
								negotiatorTarget = target;
								String tJob = getJob(target);
								sendMessage(ms+"협상가가 누군가에게 포섭을 시도했습니다.");
								if (tJob.equalsIgnoreCase("경찰") || tJob.equalsIgnoreCase("농부") || tJob.equalsIgnoreCase("군인")) {
									p.sendMessage(ms + target + " 님은 §b" + tJob + "§f이었습니다!.\n" + ms + "당신이 협상가라는 것을 들켰습니다!");
									TitleAPI.sendFullTitle(p, 10, 40, 30, ChatColor.GRAY + "포섭 실패", ChatColor.RESET + target);
									FtmPlayer ftmP = ftmPlayerList.get(p.getName());
									if(ftmP != null) ftmP.negotiator_fail += 1;
								} else {
									if(!murderTeam.contains(target)) murderTeam.add(target);
									p.sendMessage(ms + "§b" + tJob + "인 " + target + " 님을 포섭했습니다.\n");
									TitleAPI.sendFullTitle(p, 10, 40, 30, ChatColor.GRAY + "포섭 성공", ChatColor.RESET + target);
									sendTeamChat(murderTeam, ms + "협상가 " + p.getName() + " 님이 " + tJob + "인 " + target
											+ " 님을 포섭하였습니다. 이제 그도 당신의 동료입니다.");
									sendTeamSound(murderTeam, Sound.BLOCK_NOTE_HARP);
									civilTeam.remove(target);
									if(police_invensted.contains(target)) {
										for(String tName : ingamePlayer) {
											if(getJob(tName).equalsIgnoreCase("경찰")) {
												Player t = Bukkit.getPlayer(tName);
												if(existPlayer(t)) {
													t.sendMessage(ms+"§f당신이 조사했던 §c"+target+" 님이 살인자의 동료가 된 것 같습니다.");
													t.playSound(t.getLocation(), Sound.BLOCK_NOTE_HARP, 1.0f, 1.0f);
												}
												break;
											}
										}
									}
									FtmPlayer ftmP = ftmPlayerList.get(p.getName());
									if(ftmP != null) ftmP.negotiator_success += 1;
									if (civilTeam.size() <= 0) {
										murderWin();
									}
								}
							}					
						} 				
					}else {
						p.sendMessage(ms + "2번째 밤부터 가능합니다.");
					}
				} else {
					p.sendMessage(ms + "2번째 밤부터 가능합니다.");
				}
			} else {
				p.sendMessage(ms + "이미 " + negotiatorTarget + " 님을 포섭 시도했습니다.");
			}
		} else if(job.equalsIgnoreCase("영매사")) {
			if(dayTime == 0) {
				if(soul_target == null) {
					if(target.equalsIgnoreCase(p.getName())) {
						p.sendMessage(ms+"자신을 지정할 수 없습니다.");
					} else {
						if(getJob(target).equalsIgnoreCase("미치광")) {
							p.sendMessage(ms + "미치광은 지정할 수 없습니다.");
						}else {
							soul_target = target;
							p.sendMessage(ms+target+" 님의 영혼에 접근합니다.\n"+ms+"이 대상이 사망시 해당 직업을 얻습니다.");
							TitleAPI.sendFullTitle(p, 10, 40, 30, ChatColor.GRAY+"영혼 접근 시작", ChatColor.RESET+target);
						}					
					}	
				} else {
					p.sendMessage(ms+"이미 "+soul_target+" 님의 영혼에 접근중입니다.");
				}
			} else {
				p.sendMessage(ms+"아침에만 가능합니다.");
			}
		} 
	}
	
	public void murderAbInvenClick(Player p, int slot) {
		p.closeInventory();
		switch (slot) {
		case 2:
			if(!murderDenied) {
				if(voting) {
					p.sendMessage(ms+"투표중에는 사용하실 수 없는 능력입니다.");
				} else {
					p.sendMessage(ms+"당신은 투표 참여를 거부했습니다. 낮이되면 모든 사람에게 당신이 살인자라는 것을 들키게됩니다.");
					murderDenied = true;
				}
			} else {
				p.sendMessage(ms+"이미 능력을 사용하신 상태입니다.");
			}
			return;
		case 6:
			return;

		default:
			return;
		}
	}

	private void detectorInvenClick(Player p, int slot) {
		if(slot == 2) {
			List<Location> keyList = new ArrayList<Location>(corpseList.size());
			keyList.addAll(corpseList.keySet());
			for(Location l : keyList) {
				if(l.distance(p.getLocation()) < 1) {
					p.sendMessage(corpseList.get(l).getDataToMsg());
					break;
				}
			}
		}
	}
	
	private void spyInvenClick(Player p, int slot) {
		if(slot == 2) {
			List<Location> keyList = new ArrayList<Location>(corpseList.size());
			keyList.addAll(corpseList.keySet());
			for(Location l : keyList) {
				if(l.distance(p.getLocation()) < 1) {
					corpseList.get(l).deleteCorpse();
					p.sendMessage(ms+"시체를 제거하였습니다.");
					corpseList.remove(l);
				}
			}
		}
	}
	
	///////////////////////////////
	
	private int getJobCode(String jobName) {
		switch(jobName) {
			case "살인자": return 0;
			case "경찰": return 1;
			case "의사": return 2;
			case "배신자": return 3;
			case "군인": return 4;
			case "미치광": return 5;
			case "기자": return 6;
			case "성직자": return 7;
			case "마술사": return 8;
			case "발명가": return 9;
			case "경호원": return 10;
			case "탐정": return 11;
			case "농부": return 12;
			case "시민": return 13;
			case "계약자": return 14; 
			case "영매사": return 15; //처음 죽은 사람의 직업을 가져옴
			case "협상가": return 16; //밤에 지정한 1명씩 살마팀으로 데려옴(경찰,군인 제외), 살마팀 11명 이상이면 무조건 나옴
			case "열쇠공": return 17;
			
			default: return -1;
		}
	}

	////////////////// 아이템
	
	private void giveBasicItem() {
		ItemStack ftmHelper = new ItemStack(Material.BOOK, 1);
		ItemMeta meta = ftmHelper.getItemMeta();
		meta.setDisplayName("§f[ §c게임 메뉴 §f]");
		List<String> loreList = new ArrayList<String>(2);
		loreList.add("§7- 직업 능력 사용, 도움말, 투표 등등");
		loreList.add("§7게임 진행에 필요한 기능을 사용합니다.");
		meta.setLore(loreList);
		ftmHelper.setItemMeta(meta);
		for (String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p)) {
				p.getInventory().setItem(8, ftmHelper);
				p.getInventory().setHeldItemSlot(8);
			}		
		}
	}

	/////////////////////////////////

	public void onCommand(Player p, String[] cmd) {
		if (cmd.length <= 1) {
			p.sendMessage(ms + "[" + disPlayGameName + "] " + cmdMain + " join - 게임 참가");
			p.sendMessage(ms + "[" + disPlayGameName + "] " + cmdMain + " quit - 게임 퇴장");
			p.sendMessage(ms + "[" + disPlayGameName + "] " + cmdMain + " set - 게임 설정");
		} else if (cmd[1].equalsIgnoreCase("set")) {
			if (cmd.length <= 2) {
				p.sendMessage(ms + "[" + disPlayGameName + "] " + cmdMain + " set loc- 게임 지점 설정");
			} else if (cmd[2].equalsIgnoreCase("loc")) {
				if (cmd.length <= 3) {
					p.sendMessage(ms + "[" + disPlayGameName + "] " + cmdMain + " set loc join - 게임 시작 대기 지점 설정");
					p.sendMessage(ms + "[" + disPlayGameName + "] " + cmdMain + " set loc votepos - 게임 오프닝 지점 설정");
					p.sendMessage(ms + "[" + disPlayGameName + "] " + cmdMain + " set loc startpos - 게임 시작 지점 추가");
					p.sendMessage(ms + "[" + disPlayGameName + "] " + cmdMain + " set loc removestart <번호> - 게임 시작 지점 삭제");
				} else if (cmd[3].equalsIgnoreCase("join")) {
					saveLocation(gameName, "JoinPos", p.getLocation());
					loadGameData();
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "게임 시작 대기 지점이 설정되었습니다.");
				} else if (cmd[3].equalsIgnoreCase("votepos")) {
					saveLocation(gameName, "votePos", p.getLocation());
					loadGameData();
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "투표장소가 설정되었습니다.");
				} else if (cmd[3].equalsIgnoreCase("startPos")) {
					int startPosCnt = loc_startList.size()+1;
					saveLocation(gameName, "StartPos" + startPosCnt, p.getLocation());
					File file = new File(server.getDataFolder().getPath() + "/" + gameName + "/Location", "location.yml");
					FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
					fileConfig.set("startPosCnt", startPosCnt);
					try {
						fileConfig.save(file);
					} catch (IOException e) {
					}
					loadGameData();
					p.sendMessage(ms + "[" + disPlayGameName + "] " + startPosCnt + " 번째 게임 시작 지점이 추가되었습니다.");
				} else if (cmd[3].equalsIgnoreCase("removestart")) {
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "삭제할 시작지점 번호를 입력해주세요.");
				} else {
					p.sendMessage(ms + "[" + disPlayGameName + "] " + cmdMain + " set loc join - 게임 시작 대기 지점 설정");
					p.sendMessage(ms + "[" + disPlayGameName + "] " + cmdMain + " set loc votepos - 게임 오프닝 지점 설정");
					p.sendMessage(ms + "[" + disPlayGameName + "] " + cmdMain + " set loc startpos - 게임 시작 지점 추가");
					p.sendMessage(ms + "[" + disPlayGameName + "] " + cmdMain + " set loc removestart <번호> - 게임 시작 지점 삭제");
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
		} else if (cmd[1].equalsIgnoreCase("debug")) {
			if(cmd[2].equalsIgnoreCase("job")) {
				setFullJob(p, Integer.valueOf(cmd[3]));
			}
		}  else if (cmd[1].equalsIgnoreCase("debug0")) {
			dayTime = 2;
			ingame = true;
			if(!murderTeam.contains(p.getName()))murderTeam.add(p.getName());
		}else if(cmd[1].equalsIgnoreCase("debug2")){
			sendSound(Sound.MUSIC_CREDITS, 1.0f, 1.5f);
		}else if(cmd[1].equalsIgnoreCase("debug3")){
			for(String pName : ingamePlayer) {
				p.sendMessage(pName+" : "+ftmPlayerList.get(pName).job);
			}
		}else if(cmd[1].equalsIgnoreCase("debug4")){	
				ftmPlayerList.clear();
				List<Player> tmpList = new ArrayList<Player>(Bukkit.getOnlinePlayers().size());
				for (Player t : Bukkit.getOnlinePlayers()) {
					if(existPlayer(t))
						tmpList.add(t);
				}
				
				List<Integer> jobCodeList = new ArrayList<Integer>(12);
				for (int i = 0; i < 3; i++)
					jobCodeList.add(i);

				while (tmpList.size() != 0 && jobCodeList.size() != 0) {
					int randomJobCode = MyUtility.getRandom(0, jobCodeList.size() - 1);
					int random = MyUtility.getRandom(0, tmpList.size() - 1);
					p.sendMessage("randomCode: "+randomJobCode+",  random: "+random);
					setJob(tmpList.get(random), jobCodeList.get(Integer.valueOf(randomJobCode)));
					tmpList.remove(random);
					jobCodeList.remove(randomJobCode);
					for(Player name : tmpList) {
						p.sendMessage("name: "+name.getName());
					}
					for(int code : jobCodeList) {
						p.sendMessage("code: "+code);
					}
				}
				p.sendMessage("-------------------------");
				for(Player t : Bukkit.getOnlinePlayers()) {
					String pName = t.getName();
					p.sendMessage(pName+" : "+ftmPlayerList.get(pName).job);
				}
		}else if(cmd[1].equalsIgnoreCase("debug5")){
			ingamePlayer.clear();
			ftmPlayerList.clear();
			alreadyRole = false;	
			for(Player t : Bukkit.getOnlinePlayers()) {
				ingamePlayer.add(t.getName());
			}
			setRole();
			for(String pName : ingamePlayer) {
				Bukkit.broadcastMessage(pName+" : "+ftmPlayerList.get(pName).job);
			}
		}else {
			p.sendMessage(ms + "[" + disPlayGameName + "] " + cmdMain + " join - 게임 참가");
			p.sendMessage(ms + "[" + disPlayGameName + "] " + cmdMain + " quit - 게임 퇴장");
			p.sendMessage(ms + "[" + disPlayGameName + "] " + cmdMain + " set - 게임 설정");
		}
	}

	public void setRole() {
		if(alreadyRole) return;
		alreadyRole = true;		
		List<Player> tmpList = new ArrayList<Player>(ingamePlayer.size());
		virtualJobMap.clear();
		for (String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p))
				tmpList.add(p);
		}

		if(!gameName.equalsIgnoreCase("FindTheMurder2")) {
			List<Integer> jobCodeList = new ArrayList<Integer>(12);
			int inCase = MyUtility.getRandom(0, 1);
			if(inCase == 0) { //마X
				jobCodeList.add(0);
				jobCodeList.add(1);
				jobCodeList.add(2);
				jobCodeList.add(4);
				jobCodeList.add(5);
				jobCodeList.add(6);
				jobCodeList.add(7);
				//jobCodeList.add(8);
				jobCodeList.add(9);
				jobCodeList.add(10);
				jobCodeList.add(11);
				jobCodeList.add(12);
				jobCodeList.add(15);
				jobCodeList.add(16);
				jobCodeList.add(17);
			}else if(inCase == 1){//영X
				jobCodeList.add(0);
				jobCodeList.add(1);
				jobCodeList.add(2);
				jobCodeList.add(4);
				jobCodeList.add(5);
				jobCodeList.add(6);
				jobCodeList.add(7);
				jobCodeList.add(8);
				jobCodeList.add(9);
				jobCodeList.add(10);
				jobCodeList.add(11);
				jobCodeList.add(12);
				//jobCodeList.add(15); 
				jobCodeList.add(16);
				jobCodeList.add(17);
			}
				

			int random = MyUtility.getRandom(0, tmpList.size() - 1);
			setJob(tmpList.get(random), 0);
			tmpList.remove(random);
			jobCodeList.remove(Integer.valueOf(0));
			random = MyUtility.getRandom(0, tmpList.size() - 1);
			setJob(tmpList.get(random), 1);
			tmpList.remove(random);
			jobCodeList.remove(Integer.valueOf(1));
			random = MyUtility.getRandom(0, tmpList.size() - 1);
			setJob(tmpList.get(random), 2);
			tmpList.remove(random);
			jobCodeList.remove(Integer.valueOf(2));
			random = MyUtility.getRandom(0, tmpList.size() - 1);
			setJob(tmpList.get(random), MyUtility.getRandom(0, 1) == 0 ? 3  : 14); //배신자 또는 계약자
			tmpList.remove(random);
			random = MyUtility.getRandom(0, tmpList.size() - 1);
			setJob(tmpList.get(random), 4);
			tmpList.remove(random);
			jobCodeList.remove(Integer.valueOf(4));
			if (ingamePlayer.size() >= 9) { // 9명이상이면 1명에게 미치광
				random = MyUtility.getRandom(0, tmpList.size() - 1);
				setJob(tmpList.get(random), 5);
				tmpList.remove(random);
			}
			if (ingamePlayer.size() >= 12) { // 12명이상이면 1명에게 협상가
				random = MyUtility.getRandom(0, tmpList.size() - 1);
				setJob(tmpList.get(random), 16);
				tmpList.remove(random);
			}
			jobCodeList.remove(Integer.valueOf(5)); // 미치광 삭제, 9명 이상이든 아니든 미치광는 삭제해야하기 때문(9명 미만이면 미치광가 안나오도록)
			jobCodeList.remove(Integer.valueOf(16)); // 협상가 삭제, 12명 이상이든 아니든 협상가는 삭제해야하기 때문(12명 미만이면 미치광가 안나오도록)
			while (tmpList.size() != 0 && jobCodeList.size() != 0) {
				int randomJobCode = MyUtility.getRandom(0, jobCodeList.size() - 1);
				random = MyUtility.getRandom(0, tmpList.size() - 1);
				setJob(tmpList.get(random), jobCodeList.get(Integer.valueOf(randomJobCode)));
				tmpList.remove(random);
				jobCodeList.remove(randomJobCode);
			}
		} else {
			List<Integer> jobCodeList = new ArrayList<Integer>(12);
			int inCase = MyUtility.getRandom(0, 1);
			if(inCase == 0) { //마X
				jobCodeList.add(0);
				jobCodeList.add(1);
				jobCodeList.add(2);
				jobCodeList.add(4);
				jobCodeList.add(6);
				jobCodeList.add(7);
				//jobCodeList.add(8);
				jobCodeList.add(9);
				jobCodeList.add(10);
				jobCodeList.add(11);
				jobCodeList.add(12);
				jobCodeList.add(15);
				jobCodeList.add(16);
				jobCodeList.add(17);
			}else if(inCase == 1){//영X
				jobCodeList.add(0);
				jobCodeList.add(1);
				jobCodeList.add(2);
				jobCodeList.add(4);
				jobCodeList.add(6);
				jobCodeList.add(7);
				jobCodeList.add(8);
				jobCodeList.add(9);
				jobCodeList.add(10);
				jobCodeList.add(11);
				jobCodeList.add(12);
				//jobCodeList.add(15); 
				jobCodeList.add(16);
				jobCodeList.add(17);
			}
				

			int random = MyUtility.getRandom(0, tmpList.size() - 1);
			setJob(tmpList.get(random), 0);
			tmpList.remove(random);
			jobCodeList.remove(Integer.valueOf(0));
			random = MyUtility.getRandom(0, tmpList.size() - 1);
			setJob(tmpList.get(random), 1);
			tmpList.remove(random);
			jobCodeList.remove(Integer.valueOf(1));
			random = MyUtility.getRandom(0, tmpList.size() - 1);
			setJob(tmpList.get(random), 2);
			tmpList.remove(random);
			jobCodeList.remove(Integer.valueOf(2));
			random = MyUtility.getRandom(0, tmpList.size() - 1);
			setJob(tmpList.get(random), MyUtility.getRandom(0, 1) == 0 ? 3  : 14); //배신자 또는 계약자
			tmpList.remove(random);
			random = MyUtility.getRandom(0, tmpList.size() - 1);
			setJob(tmpList.get(random), 4);
			tmpList.remove(random);
			jobCodeList.remove(Integer.valueOf(4));
			if (ingamePlayer.size() >= 8) { // 8명이상이면 1명에게 협상가
				random = MyUtility.getRandom(0, tmpList.size() - 1);
				setJob(tmpList.get(random), 16);
				tmpList.remove(random);
			}
			jobCodeList.remove(Integer.valueOf(5)); // 미치광 삭제, 9명 이상이든 아니든 미치광는 삭제해야하기 때문(9명 미만이면 미치광가 안나오도록)
			jobCodeList.remove(Integer.valueOf(16)); // 협상가 삭제, 12명 이상이든 아니든 협상가는 삭제해야하기 때문(12명 미만이면 미치광가 안나오도록)
			while (tmpList.size() != 0 && jobCodeList.size() != 0) {
				int randomJobCode = MyUtility.getRandom(0, jobCodeList.size() - 1);
				random = MyUtility.getRandom(0, tmpList.size() - 1);
				setJob(tmpList.get(random), jobCodeList.get(Integer.valueOf(randomJobCode)));
				tmpList.remove(random);
				jobCodeList.remove(randomJobCode);
			}
		}

		//sendMessage(ms + "직업이 정해졌습니다.");
		//sendMessage(ms + "게임 메뉴(책)을 우클릭하시면 관련 도움말 및 능력 사용이 가능합니다.");
		MyUtility.mixMap(virtualJobMap);
		// Main.SendMessage(ms + "직업리스트를 보시려면 /ftm list 를 입력해주세요.");
		// Mixlist();
	}
	
	public void setFullJob(Player p, int jobCode) {
		// jobCode 목록, 0=살인자 ,1=경찰,2=의사,3=배신자,4=군인,5=미치광,6=기자
		// 7=성직자,8=마술사,9=발명가,10=경호원,11=탐정,12=농부, 
		murderTeam.remove(p.getName());
		civilTeam.remove(p.getName());
		if (jobCode == 0) {
			if(!murderTeam.contains(p.getName())) murderTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "살인자"));
			virtualJobMap.put(p.getName(), "?");
			p.sendMessage(ms + "§7당신의 직업은 " + ChatColor.GOLD + getJob(p) + "§7 입니다.");
			// 살인자 전용 아이템
			p.getInventory().addItem(murderKnife);
			for (int i = 0; i < 3; i++) { // 투명 시계 3개 지급
				p.getInventory().addItem(chestItem.get(16));
			}
			MyUtility.setMaxHealth(p,40);
		} else if (jobCode == 1) {
			if(!civilTeam.contains(p.getName())) civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "경찰"));
			virtualJobMap.put(p.getName(), "?");
			p.sendMessage(ms + "§7당신의 직업은 " + ChatColor.GOLD + getJob(p) + "§7 입니다.");
			policeTarget = null;
			/* 경찰 전용 아이템
			ItemStack item = new ItemStack(Material.STICK, 3);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.AQUA + "경관봉");
			meta.addEnchant(Enchantment.KNOCKBACK, 1, true);
			List<String> lorelist = new ArrayList();
			lorelist.add(ChatColor.GRAY + "경찰에게 주어지는 무기로");
			lorelist.add(ChatColor.GRAY + "이 무기로 때린 플레이어는 3초간");
			lorelist.add(ChatColor.GRAY + "움직이지 못한다. (경찰만 사용가능)");
			meta.setLore(lorelist);
			item.setItemMeta(meta);
			p.getInventory().addItem(item);*/
		} else if (jobCode == 2) {
			if(!civilTeam.contains(p.getName()))civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "의사"));
			virtualJobMap.put(p.getName(), "?");
			p.sendMessage(ms + "§7당신의 직업은 " + ChatColor.GOLD + getJob(p) + "§7 입니다.");
			doctorTarget = null;
		} else if (jobCode == 3) {
			if(!civilTeam.contains(p.getName()))civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "배신자"));
			virtualJobMap.put(p.getName(), "?");
			p.sendMessage(ms + "§7당신의 직업은 " + ChatColor.GOLD + getJob(p) + "§7 입니다.");
		} else if (jobCode == 4) {
			if(!civilTeam.contains(p.getName())) civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "군인"));
			virtualJobMap.put(p.getName(), "?");
			p.sendMessage(ms + "§7당신의 직업은 " + ChatColor.GOLD + getJob(p) + "§7 입니다.");
			p.getInventory().addItem(chestItem.get(9));
			for(int i = 0; i < 5; i++) p.getInventory().addItem(chestItem.get(19));
			soldier_use = false;
		} else if (jobCode == 5) {
			if(!murderTeam.contains(p.getName())) murderTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "미치광"));
			virtualJobMap.put(p.getName(), "미치광");
			p.sendMessage(ms + "§7당신의 직업은 " + ChatColor.GOLD + getJob(p) + "§7 입니다.");
			p.sendMessage(ms + "당신의 직업이 조사현황에 미치광로 올려졌습니다.");
			sendMessage(ms + "미치광 " + p.getName() + " 가 있습니다. 조심하세요!");
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 72000, 0));
			p.getInventory().addItem(crazy_pickAxe);
			p.getInventory().addItem(chestItem.get(16));
		} else if (jobCode == 6) {
			if(!civilTeam.contains(p.getName()))civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "기자"));
			virtualJobMap.put(p.getName(), "?");
			p.sendMessage(ms + "§7당신의 직업은 " + ChatColor.GOLD + getJob(p) + "§7 입니다.");
			reporter_use = false;
		} else if (jobCode == 7) {
			if(!civilTeam.contains(p.getName()))civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "성직자"));
			virtualJobMap.put(p.getName(), "?");
			p.sendMessage(ms + "§7당신의 직업은 " + ChatColor.GOLD + getJob(p) + "§7 입니다.");
		} else if (jobCode == 8) {
			if(!civilTeam.contains(p.getName()))civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "마술사"));
			virtualJobMap.put(p.getName(), "?");
			p.sendMessage(ms + "§7당신의 직업은 " + ChatColor.GOLD + getJob(p) + "§7 입니다.");
		} else if (jobCode == 9) {
			if(!civilTeam.contains(p.getName()))civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "발명가"));
			virtualJobMap.put(p.getName(), "?");
			p.sendMessage(ms + "§7당신의 직업은 " + ChatColor.GOLD + getJob(p) + "§7 입니다.");
		} else if (jobCode == 10) {
			if(!civilTeam.contains(p.getName()))civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "경호원"));
			virtualJobMap.put(p.getName(), "?");
			p.sendMessage(ms + "§7당신의 직업은 " + ChatColor.GOLD + getJob(p) + "§7 입니다.");
			guardTarget = null;
		} else if (jobCode == 11) {
			if(!civilTeam.contains(p.getName()))civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "탐정"));
			virtualJobMap.put(p.getName(), "?");
			p.sendMessage(ms + "§7당신의 직업은 " + ChatColor.GOLD + getJob(p) + "§7 입니다.");
		} else if (jobCode == 12) {
			if(!civilTeam.contains(p.getName()))civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "농부"));
			virtualJobMap.put(p.getName(), "농부");
			p.sendMessage(ms + "§7당신의 직업은 " + ChatColor.GOLD + getJob(p) + "§7 입니다.");
			p.getInventory().addItem(makeItem(Material.GOLD_HOE, "농기구", 5, 5));
		} else if (jobCode == 13) {
			if(!civilTeam.contains(p.getName()))civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "시민"));
			virtualJobMap.put(p.getName(), "?");
			p.sendMessage(ms + "§7당신의 직업은 " + ChatColor.GOLD + getJob(p) + "§7 입니다.");
		}else if (jobCode == 14) {
			if(!civilTeam.contains(p.getName()))civilTeam.add(p.getName());
			contractorTarget = null;
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "계약자"));
			virtualJobMap.put(p.getName(), "?");
			p.sendMessage(ms + "§7당신의 직업은 " + ChatColor.GOLD + getJob(p) + "§7 입니다.");
		}else if (jobCode == 15) {
			if(!civilTeam.contains(p.getName()))civilTeam.add(p.getName());
			soul_target = null;
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "영매사"));
			virtualJobMap.put(p.getName(), "?");
			p.sendMessage(ms + "§7당신의 직업은 " + ChatColor.GOLD + getJob(p) + "§7 입니다.");
		}else if (jobCode == 16) {
			if(!murderTeam.contains(p.getName()))murderTeam.add(p.getName());
			negotiatorTarget = null;
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "협상가"));
			virtualJobMap.put(p.getName(), "?");
			p.sendMessage(ms + "§7당신의 직업은 " + ChatColor.GOLD + getJob(p) + "§7 입니다.");
		}else if (jobCode == 17) {
			if(!civilTeam.contains(p.getName()))civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "열쇠공"));
			virtualJobMap.put(p.getName(), "?");
			p.sendMessage(ms + "§7당신의 직업은 " + ChatColor.GOLD + getJob(p) + "§7 입니다.");
			keySmith_left = 4;
		}
		String tJob = getJob(p);
		if(getJob(p) != null) {
			TitleAPI.sendTitle(p, 10, 80, 30, ChatColor.GRAY+getJob(p));
		}
		sendMessage(ms + "게임 메뉴(책)을 우클릭하시면 관련 도움말 및 능력 사용이 가능합니다.");
	}

	public void setJob(Player p, int jobCode) {
		// jobCode 목록, 0=살인자 ,1=경찰,2=의사,3=배신자,4=군인,5=미치광,6=기자
		// 7=성직자,8=마술사,9=발명가,10=경호원,11=탐정,12=농부, 
		murderTeam.remove(p.getName());
		civilTeam.remove(p.getName());
		if (jobCode == 0) {
			murderTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "살인자"));
			virtualJobMap.put(p.getName(), "?");
		} else if (jobCode == 1) {
			civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "경찰"));
			virtualJobMap.put(p.getName(), "?");
			policeTarget = null;
			/* 경찰 전용 아이템
			ItemStack item = new ItemStack(Material.STICK, 3);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.AQUA + "경관봉");
			meta.addEnchant(Enchantment.KNOCKBACK, 1, true);
			List<String> lorelist = new ArrayList();
			lorelist.add(ChatColor.GRAY + "경찰에게 주어지는 무기로");
			lorelist.add(ChatColor.GRAY + "이 무기로 때린 플레이어는 3초간");
			lorelist.add(ChatColor.GRAY + "움직이지 못한다. (경찰만 사용가능)");
			meta.setLore(lorelist);
			item.setItemMeta(meta);
			p.getInventory().addItem(item);*/
		} else if (jobCode == 2) {
			civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "의사"));
			virtualJobMap.put(p.getName(), "?");
			doctorTarget = null;
		} else if (jobCode == 3) {
			civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "배신자"));
			virtualJobMap.put(p.getName(), "?");
		} else if (jobCode == 4) {
			civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "군인"));
			virtualJobMap.put(p.getName(), "?");
			soldier_use = false;
		} else if (jobCode == 5) {
			murderTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "미치광"));
			virtualJobMap.put(p.getName(), "미치광");
		} else if (jobCode == 6) {
			civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "기자"));
			virtualJobMap.put(p.getName(), "?");
			reporter_use = false;
		} else if (jobCode == 7) {
			civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "성직자"));
			virtualJobMap.put(p.getName(), "?");
		} else if (jobCode == 8) {
			civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "마술사"));
			virtualJobMap.put(p.getName(), "?");
		} else if (jobCode == 9) {
			civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "발명가"));
			virtualJobMap.put(p.getName(), "?");
		} else if (jobCode == 10) {
			civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "경호원"));
			virtualJobMap.put(p.getName(), "?");
			guardTarget = null;
		} else if (jobCode == 11) {
			civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "탐정"));
			virtualJobMap.put(p.getName(), "?");
		} else if (jobCode == 12) {
			civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "농부"));
			virtualJobMap.put(p.getName(), "농부");
		} else if (jobCode == 13) {
			civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "시민"));
			virtualJobMap.put(p.getName(), "?");
		} else if (jobCode == 14) {
			civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "계약자"));
			virtualJobMap.put(p.getName(), "?");
		} else if (jobCode == 15) {
			civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "영매사"));
			virtualJobMap.put(p.getName(), "?");
		} else if (jobCode == 16) {
			murderTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "협상가"));
			virtualJobMap.put(p.getName(), "?");
		}else if (jobCode == 17) {
			civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "열쇠공"));
			virtualJobMap.put(p.getName(), "?");	
			keySmith_left = 4;
		}
	}
	
	public void tellJob(Player p) {
		// jobCode 목록, 0=살인자 ,1=경찰,2=의사,3=배신자,4=군인,5=미치광,6=기자
		// 7=성직자,8=마술사,9=발명가,10=경호원,11=탐정,12=농부, 
		int jobCode = getJobCode(getJob(p));
		if (jobCode == 0) {
			p.sendMessage(ms + "§7당신의 직업은 " + ChatColor.GOLD + getJob(p) + "§7 입니다.");
			// 살인자 전용 아이템
			p.getInventory().addItem(murderKnife);
			for (int i = 0; i < 3; i++) { // 투명 시계 3개 지급
				p.getInventory().addItem(chestItem.get(16));
			}
			MyUtility.setMaxHealth(p,40);
		} else if (jobCode == 1) {
			p.sendMessage(ms + "§7당신의 직업은 " + ChatColor.GOLD + getJob(p) + "§7 입니다.");
		} else if (jobCode == 2) {
			p.sendMessage(ms + "§7당신의 직업은 " + ChatColor.GOLD + getJob(p) + "§7 입니다.");
		} else if (jobCode == 3) {
			p.sendMessage(ms + "§7당신의 직업은 " + ChatColor.GOLD + getJob(p) + "§7 입니다.");
		} else if (jobCode == 4) {
			p.sendMessage(ms + "§7당신의 직업은 " + ChatColor.GOLD + getJob(p) + "§7 입니다.");
			p.getInventory().addItem(chestItem.get(9));
			for(int i = 0; i < 5; i++) p.getInventory().addItem(chestItem.get(19));
		} else if (jobCode == 5) {
			p.sendMessage(ms + "§7당신의 직업은 " + ChatColor.GOLD + getJob(p) + "§7 입니다.");
			p.sendMessage(ms + "당신의 직업이 조사현황에 미치광로 올려졌습니다.");
			sendMessage(ms + "미치광 " + p.getName() + " 가 있습니다. 조심하세요!");
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 72000, 0));
			p.getInventory().addItem(crazy_pickAxe);
			p.getInventory().addItem(chestItem.get(16));
		} else if (jobCode == 6) {
			p.sendMessage(ms + "§7당신의 직업은 " + ChatColor.GOLD + getJob(p) + "§7 입니다.");
		} else if (jobCode == 7) {
			p.sendMessage(ms + "§7당신의 직업은 " + ChatColor.GOLD + getJob(p) + "§7 입니다.");
		} else if (jobCode == 8) {
			p.sendMessage(ms + "§7당신의 직업은 " + ChatColor.GOLD + getJob(p) + "§7 입니다.");
		} else if (jobCode == 9) {
			p.sendMessage(ms + "§7당신의 직업은 " + ChatColor.GOLD + getJob(p) + "§7 입니다.");
		} else if (jobCode == 10) {
			p.sendMessage(ms + "§7당신의 직업은 " + ChatColor.GOLD + getJob(p) + "§7 입니다.");
		} else if (jobCode == 11) {
			p.sendMessage(ms + "§7당신의 직업은 " + ChatColor.GOLD + getJob(p) + "§7 입니다.");
		} else if (jobCode == 12) {
			p.sendMessage(ms + "§7당신의 직업은 " + ChatColor.GOLD + getJob(p) + "§7 입니다.");
			sendMessage(ms + "성실한 농부 " + p.getName() + " 님이 있습니다. 그를 믿어주세요!");
			p.sendMessage(ms + "당신의 직업이 조사현황에 농부로 올려졌습니다.");
			p.getInventory().addItem(makeItem(Material.GOLD_HOE, "농기구", 5, 5));
		} else if (jobCode == 13) {
			p.sendMessage(ms + "§7당신의 직업은 " + ChatColor.GOLD + getJob(p) + "§7 입니다.");
		} else if (jobCode == 14) {
			p.sendMessage(ms + "§7당신의 직업은 " + ChatColor.GOLD + getJob(p) + "§7 입니다.");
		} else if (jobCode == 15) {
			p.sendMessage(ms + "§7당신의 직업은 " + ChatColor.GOLD + getJob(p) + "§7 입니다.");
		} else if (jobCode == 16) {
			p.sendMessage(ms + "§7당신의 직업은 " + ChatColor.GOLD + getJob(p) + "§7 입니다.");
		}else if (jobCode == 17) {
			p.sendMessage(ms + "§7당신의 직업은 " + ChatColor.GOLD + getJob(p) + "§7 입니다.");
		}
		String tJob = getJob(p);
		if(tJob != null) {
			TitleAPI.sendTitle(p, 10, 80, 30, ChatColor.GRAY+getJob(p));
		}
		p.sendMessage(ms + "게임 메뉴(책)을 우클릭하시면 관련 도움말 및 능력 사용이 가능합니다.");
	}

	public String getJob(Player p) {
		if(!ftmPlayerList.containsKey(p.getName())) return "미지정";
		FtmPlayer ftmP = ftmPlayerList.get(p.getName());
		return ftmP.job;
	}

	public String getJob(String pName) {
		if(!ftmPlayerList.containsKey(pName)) return "미지정";
		FtmPlayer ftmP = ftmPlayerList.get(pName);
		return ftmP.job;
	}

	public void dayCycle() {
		if (ingame || !ending) {
			if (dayTime == 0) {
				setNoon();
			} else if (dayTime == 1) {
				setNight();
			} else if (dayTime == 2) {
				setMorning();
			}
		}
	}

	public void setMorning() {
		if(!ingame || ending) return;
		dayTime = 0;
		police_use = false;
		policeTarget = null;
		doctorTarget = null;
		guardTarget = null;
		soul_target = null;
		murderPenalty = 0;
		dayCnt += 1;
		scoreBoardTeam.setNameTagVisibility(NameTagVisibility.HIDE_FOR_OTHER_TEAMS);
		sendMessage(ms + dayCnt + ChatColor.RESET + "일째 날의 아침이 되었습니다. 정보를 수집하세요.");
		sendBossbar(ChatColor.YELLOW+"§l"+dayCnt+"일째 아침", 55);
		sendTitle(ChatColor.GOLD+"아침", ChatColor.RESET+"사건 발생 "+dayCnt+"일째", 80);
		sendSound(Sound.ENTITY_CHICKEN_AMBIENT, 1.5f, 1.0f);
		for(String pName : ingamePlayer) {
			Player t = Bukkit.getPlayer(pName);
			if(existPlayer(t)) {
				if(existPlayer(t)) t.removePotionEffect(PotionEffectType.BLINDNESS);
				if(reportTarget != null) {
					if(getJob(t).equalsIgnoreCase("기자")){
						String tJob = getJob(reportTarget);
						if(!tJob.equalsIgnoreCase("미지정")) {
							virtualJobMap.put(reportTarget, tJob);
							updatePlayerInven();
							sendTitle(ChatColor.GOLD+"특종!", ChatColor.RESET+reportTarget+" 님의 직업은 "+tJob+"입니다!", 80);
							sendMessage(ms+"특종! 기자에 의해 "+reportTarget+" 님의  직업이 "+tJob+"으로 밝혀졌습니다!");						
							FtmPlayer ftmP = ftmPlayerList.get(pName);
							if(ftmP != null) {
								ftmP.reporter_report += 1;
								if(murderTeam.contains(reportTarget)) {
									ftmP.reporter_reportSuccess += 1;
								}
							}
							reportTarget = null;
						}
					}
				}			
				if(!getJob(t).equalsIgnoreCase("미치광")) {
					t.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 72000 ,0));	
				}
				t.setSneaking(false);
			}
		}
		if(dayTime == 1) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
				public void run() {
					if(!ingame || ending) return;
					sendTitle(ChatColor.GOLD+"", "§e§l아침에는 모두의 위치가 표시됩니다.", 140);
					sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
				}
			}, 400L);
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
				public void run() {
					if(!ingame || ending) return;
					sendTitle(ChatColor.GOLD+"", "§e§l다른 직업의 능력이 궁금하시면 도우미를 우클릭하세요.", 140);
					sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
				}
			}, 600L);
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
				public void run() {
					if(!ingame || ending) return;
					sendTitle(ChatColor.GOLD+"", "§e§l플레이어간 채팅은 같은 층, 20칸내만 가능합니다.", 140);
					sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
				}
			}, 800L);
		}
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				if(!ingame || ending) return;
				if(peopleKnowDeny) {
					sendMessage(ms + "곧 낮이 됩니다. 살인자의 정체는 이미 공개됐으므로 투표를 진행하지 않습니다.");
				} else {
					sendMessage(ms + "곧 낮이 되며 투표장으로 이동됩니다. 투표를 준비해주세요!");
				}
				sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
			}
		}, 900L);
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				if(!ingame || ending) return;
				dayCycle();
			}
		}, 1100L);
	}

	public void setNoon() {
		if(!ingame || ending) return;
		dayTime = 1;
		sendSound(Sound.ENTITY_COW_AMBIENT, 1.0f, 1.5f);
		sendTitle(ChatColor.GREEN+"낮", ChatColor.RESET+"사건 발생 "+dayCnt+"일째", 80);
		sendBossbar(ChatColor.GOLD+"§l"+dayCnt+"일째 낮", 60);
		votedPlayer = "";
		priestTarget = null;
		doorLockList.clear();
		if(murderDenied && !peopleKnowDeny) {
			peopleKnowDeny = true;
			alreadyKnow = true;
			String name = null;
			for(String pName : murderTeam) {
				Player p = Bukkit.getPlayer(pName);
				if(existPlayer(p)) {
					if(getJob(p).equalsIgnoreCase("살인자")) {
						name = p.getName();
					}
				}
			}
			if(name != null) {
				final String tmpName = name; 
				Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
					public void run() {
						if(!ingame || ending) return;
						sendTitle(ChatColor.GOLD+tmpName,  ChatColor.RESET+reportTarget+"이 플레이어가 살인자입니다!", 80);
						sendMessage(ms+tmpName+"님이 투표에 참석하지 않으셨습니다.\n"+ms+tmpName+"님이 살인자입니다!\n"+ms+"이제 살인마를 제외한 모두의 위치가 표시됩니다.");
						for(String pName : ingamePlayer) {
							Player p = Bukkit.getPlayer(pName);
							if(existPlayer(p)) {
								if(!getJob(p).equalsIgnoreCase("살인자")) 
									p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 72000 ,0));
							}
						}
						virtualJobMap.put(tmpName, "살인자");
						updatePlayerInven();
					}
				}, 100L);
			}
		}
		if(!peopleKnowDeny) {
			voting = true;
			for(String pName : murderTeam) {
				Player p = Bukkit.getPlayer(pName);
				if(existPlayer(p)) {
					if(getJob(p).equalsIgnoreCase("미치광")) {
						p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1400, 1));
						p.sendMessage(ms + "당신은 미치광이기 때문에 투명상태로 투표를 관전합니다.");
					}
				}
			}
			sendMessage(ms + ChatColor.GRAY + dayCnt + ChatColor.RESET
					+ "일째 날의 낮이 되었습니다. 투표장으로 이동합니다.\n"
					+ ms+"곧 투표가 시작됩니다. 투표하여 포박할 대상에  대해 의논하세요.\n"
					+ ms+"포박된 플레이어는 직업이 공개되고 다음날 낮까지 공격력이 절반이 되고 위치가 보입니다.");
			sendToLoc(loc_vote);
			Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
				public void run() {
					if(!ingame || ending) return;
					sendMessage(ms + "잠시후 투표가 시작됩니다.");
					sendTitle(ChatColor.GREEN+"포박 상태란", "§e§l직업 공개, 공격력 절반, 발광효과", 140);
					sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
				}
			}, 600L);
			Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
				public void run() {
					if(!ingame || ending) return;
					voteTime = true;
					sendMessage(ms + "포박할 플레이어를 투표해주세요.");
					sendSound(Sound.BLOCK_NOTE_BELL, 1.0f, 0.5f);
					for(String pName : ingamePlayer) {
						Player p = Bukkit.getPlayer(pName);
						if(existPlayer(p)) {
							if(!getJob(p).equalsIgnoreCase("미치광"))
								p.openInventory(playerInven);
						}
					}
				}
			}, 800L);
			Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
				public void run() {
					if(!ingame || ending) return;
					sendMessage(ms + "곧 밤이 됩니다. 투표를 하지 않은 플레이어는 빨리 투표해주세요!\n"
							+ ms+"§c도우미 §7-> §c투표기능");
					sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
				}
			}, 1000L);
			Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
				public void run() {
					if(!ingame || ending) return;
					dayCycle();
				}
			}, 1200L);
		} else {
			sendMessage(ms + dayCnt + ChatColor.RESET + "일째 날의 낮이 되었습니다. 살인자의 정체가 밝혀져 포박 투표는 열리지 않습니다.");
			for(String pName : ingamePlayer) {
				Player p = Bukkit.getPlayer(pName);
				if(existPlayer(p)) {
					if(!getJob(p).equalsIgnoreCase("살인자")) 
						p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 72000 ,0));
				}
			}
			Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
				public void run() {
					if(!ingame || ending) return;
					sendMessage(ms + "곧 밤이 됩니다.");
					sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
				}
			}, 1000L);
			Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
				public void run() {
					if(!ingame || ending) return;
					dayCycle();
				}
			}, 1200L);
		}
	}

	public void setNight() {
		if(!ingame || ending) return;
		spy_use = false;
		voting = false;
		voteTime = false;
		dayTime = 2;
		for(String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p))
				p.closeInventory();
		}
		
		if(!alreadyKnow) randomTp(ingamePlayer);
		if(peopleKnowDeny) alreadyKnow = true;
		
		disposalVote();
		sendSound(Sound.ENTITY_GHAST_SCREAM, 1.3f, 1.3f);
		scoreBoardTeam.setNameTagVisibility(NameTagVisibility.HIDE_FOR_OWN_TEAM);
		if(policeTarget != null) {
			for(String pName : civilTeam) {
				Player p = Bukkit.getPlayer(pName);
				if(existPlayer(p)) {
					if(getJob(p).equalsIgnoreCase("경찰")) {
						if(murderTeam.contains(policeTarget)) {
							p.sendMessage(ms+"조사결과가 나왔습니다.\n"+ms+policeTarget+" 님은 살인자거나 살인자의 동료입니다!");
							FtmPlayer ftmP = ftmPlayerList.get(pName);
							if(ftmP != null) ftmP.police_success += 1;
						} else {
							p.sendMessage(ms+"조사결과가 나왔습니다.\n"+ms+policeTarget+" 님은 살인자 또는 살인자의 동료가 아닙니다.");
						}
						police_invensted.add(policeTarget);
						policeTarget = null;
					}
				}
			}
		}
		sendMessage(ms + dayCnt + ChatColor.RESET + "일째 날의 밤이 되었습니다. 조심하세요.");
		//PotionEffect nightVision = new PotionEffect(PotionEffectType.NIGHT_VISION, 1500, 0);
		PotionEffect blindness = new PotionEffect(PotionEffectType.BLINDNESS, 1800, 0);
		for(String tName : ingamePlayer) {
			Player t = Bukkit.getPlayer(tName);
			if(existPlayer(t)) {
				//if(!murderTeam.contains(tName)) {
					t.addPotionEffect(blindness);		
					//t.setSneaking(true);
				//} else {
					//t.addPotionEffect(nightVision); 
				//}
			}
		}
		sendTitle(ChatColor.RED+"밤",  ChatColor.RESET+"사건 발생 "+dayCnt+"일째", 80);
		sendBossbar(ChatColor.RED+"§l"+dayCnt+"일째 밤", 90);
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				if(!ingame || ending) return;
				sendSound(Sound.ENTITY_LIGHTNING_THUNDER, 0.6f, 0.5f);
			}
		}, 150L);
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				if(!ingame || ending) return;
				sendSound(Sound.AMBIENT_CAVE, 0.6f, 1.5f);
			}
		}, 300L);
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				if(!ingame || ending) return;
				sendSound(Sound.ENTITY_CAT_AMBIENT, 0.6f, 0.1f);
			}
		}, 700L);
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				if(!ingame || ending) return;
				sendSound(Sound.BLOCK_IRON_TRAPDOOR_CLOSE, 0.6f, 0.25f);
			}
		}, 900L);
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				if(!ingame || ending) return;
				sendSound(Sound.AMBIENT_CAVE, 0.6f, 1.5f);
			}
		}, 1100L);
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				if(!ingame || ending) return;
				sendSound(Sound.ENTITY_BAT_AMBIENT, 0.6f, 0.25f);
			}
		}, 1450L);
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				if(!ingame || ending) return;
				sendMessage(ms + "곧 아침이 됩니다.");
				sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
			}
		}, 1600L);
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				if(!ingame || ending) return;
				dayCycle();
			}
		}, 1800L);
	}

	public void jobHelpMsg(Player p) {
		String jobName = getJob(p);
		if (jobName.equalsIgnoreCase("살인자")) {
			p.sendMessage(ChatColor.GOLD + "당신의 직업 : " + ChatColor.AQUA + getJob(p) + ChatColor.GRAY
					+ "\n당신은 사람들을 모두 살해하려는 살인자입니다." 
					+"\n당신만의 강력한 무기를 가지고 있습니다."
					+ "\n§c배신자§f가 능력을 사용하여 당신과 접선시 §c배신자§f는 동료가 됩니다."
					+"\n만약 §c미치광§f이나 §c협상가§f가 존재한다면 협력하십시오."
					+"\n당신은 능력을 사용하여 §c투표장 참가를 거부§f할 수 있으며 거부할시"
					+ "\n당신이 살인자라는 것을 들키게 되지만 §c모든 플레이어의 위치가 표시§f됩니다."
					+"\n당신은 완전범죄가 가능할 것 같습니까?");
		} else if (jobName.equalsIgnoreCase("경찰")) {
			p.sendMessage(ChatColor.GOLD + "당신의 직업 : " + ChatColor.AQUA + getJob(p) + ChatColor.GRAY
					+ "\n당신은 경찰입니다."
					+ "\n아침마다 1명을 지정하여 §c살인자인지 아닌지 조사§f할 수 있습니다." 
					+ "\n조사는 밤이 되어야 끝나며 결과는 당신에게만 알려집니다."
					+ "\n그리고 1번이라도 조사한 대상이 살인자의 팀이 되었을 때도"
					+ "\n당신에게 알림이 갑니다."
					+ "\n또한 당신의 직감은 뛰어납니다. 누군가가 살해됐을 때 살해현장과의 거리와 좌표를 얻습니다."
					+ "\n명심하십시오. 당신이 사람들을 지켜내야합니다.");
		} else if (jobName.equalsIgnoreCase("의사")) {
			p.sendMessage(ChatColor.GOLD + "당신의 직업 : " + ChatColor.AQUA + getJob(p) + ChatColor.GRAY
					+ "\n당신은 생명을 살리는 의사입니다."
					+ "\n당신은 아침마다 1명을 지정할 수 있습니다."
					+ "\n이 §c지정한 플레이어는 그 밤중에 사망시 한번 부활§f합니다."
					+ "\n단, 자기 자신은 지정이 불가능합니다."
					+ "\n의사인 당신만이 생명을 구할 수 있습니다...");
		} else if (jobName.equalsIgnoreCase("배신자")) {
			p.sendMessage(ChatColor.GOLD + "당신의 직업 : " + ChatColor.AQUA + getJob(p) + ChatColor.GRAY
					+ "\n당신은 탐욕한 배신자입니다."
					+ "\n기본적으로는 시민팀으로 활동합니다."
					+ "\n밤마다 지정한 플레이어 1명의 직업을 알 수 있습니다."
					+ "\n만약 지정한 플레이어가 §c살인자 또는 협상가§f라면 당신은 살인자의 동료가 되게 되며"
					+ ChatColor.GRAY + "\n살인자와 같이 밤마다 시민을 죽이고  다닐 수  있습니다."
					+ "\n단, 이 능력을 군인에게 사용시 군인은 당신이 스파이라는 것을 알게 될 것입니다." 
					+ "\n(게임중에는 배신자 또는 계약자만 등장합니다)." 
					+ "\n부디 능력을 신중하게 사용하십시오. 당신은 정말 사람들을 배신할 겁니까?");
		} else if (jobName.equalsIgnoreCase("군인")) {
			p.sendMessage(ChatColor.GOLD + "당신의 직업 : " + ChatColor.AQUA + getJob(p) + ChatColor.GRAY
					+ "\n당신은 강인한 신체를 가진 군인입니다. §c사망시 한번 부활§f하게 됩니다.(자연사제외)" 
					+ "\n부활후에는 직업이 시민으로 변경됩니다." 
					+ "\n또한 당신은 총 1정과 총알 5발을 소지한 상태로  시작합니다." 
					+ "\n살아남으세요! 당신은 사람들의 생명과 재산을 보호할 군인입니다.");
		} else if (jobName.equalsIgnoreCase("미치광")) {
			p.sendMessage(ChatColor.GOLD + "당신의 직업 : " + ChatColor.AQUA + getJob(p) + ChatColor.GRAY
					+ "\n당신은 살인자의 동료입니다." 
					+ "\n모든 플레이어는 당신이 미치광라는 것을 알고 있습니다."
					+ "\n당신은 §c아침에 활동을 하는 것이 불가능§f하지만 낮이 되어도"
					+ "\n투표장으로 이동되지 않으며 아침이어도 위치가 표시되지 않습니다."
					+ "\n당신은 범죄 생활에 얻은 기술로 타인보다 §c빠른 이동속도§f를 가지고 있습니다."
					+ "\n발버둥 치십시오. 아니면 다시 빈곤한 생활로 돌아가시겠습니까?");
		} else if (jobName.equalsIgnoreCase("기자")) {
			p.sendMessage(ChatColor.GOLD + "당신의 직업 : " + ChatColor.AQUA + getJob(p) + ChatColor.GRAY
					+ "\n당신은 진실을 밝히는 기자입니다." +  "\n당신은 밤에 플레이어 1명을 지목할 수 있습니다." + ChatColor.GRAY
					+ "\n아침이 되면 그 §c플레이어의 직업을 모두에게 밝힙니다.§f"
					+ "\n능력은 단 1번만 사용 가능합니다." 
					+ "\n당신뿐만이 진실을 밝혀낼 수 있습니다.");
		} else if (jobName.equalsIgnoreCase("성직자")) {
			p.sendMessage(ChatColor.GOLD + "당신의 직업 : " + ChatColor.AQUA + getJob(p) + ChatColor.GRAY
					+ "\n당신은 사람들이 따르는 성직자입니다." 
					+ "\n당신의 발언은 힘이 있어 당신의 §c투표는 2표§f로서 취급됩니다."
					+ "\n당신은 §c투표로 포박되지 않습니다.§f" 
					+  "\n당신이 사람들을 이끌어 살인자를 잡아내십시오!");
		} else if (jobName.equalsIgnoreCase("마술사")) {
			p.sendMessage(ChatColor.GOLD + "당신의 직업 : " + ChatColor.AQUA + getJob(p) + ChatColor.GRAY
					+ "\n당신은 속임수의 달인 마술사입니다."
					+ "\n당신은 2일째(또는 그 이후) 밤에 플레이어 1명을 지정할 수 있습니다." 
					+ "\n플레이어를 지정하는 즉시 해당 §c플레이어의 직업을 빼았습니다.§f"
					+ "\n지정한 플레이어가 살인자인 경우에는 그 플레이어는 사망하고"
					+ "\n당신이 살인자가 됩니다."
					+ "\n당신은 여러얼굴을 가질수 있습니다. 자유롭게 행동하십시오.");
		} else if (jobName.equalsIgnoreCase("발명가")) {
			p.sendMessage(ChatColor.GOLD + "당신의 직업 : " + ChatColor.AQUA + getJob(p) + ChatColor.GRAY
					+ "\n당신은 도구에  대해 박식한 발명가입니다."
					+ "\n당신은 상자를 클릭하여 아이템을 얻을 시"
					+ "\n§c50%확률로 추가 아이템§f을 얻습니다."
					+ "\n사람들에게 아이템을 나눠주어 지원해주십시오." 
					+ "\n재산은 곧 힘입니다.");
		} else if (jobName.equalsIgnoreCase("경호원")) {
			p.sendMessage(ChatColor.GOLD + "당신의 직업 : " + ChatColor.AQUA + getJob(p) + ChatColor.GRAY 
					+ "\n당신은 타인을 지키는 경호원입니다."
					+ "\n아침 또는 낮에 플레이어 1명을 지정시 §c해당 플레이어는 그 낮에"
					+ "\n투표로 포박되지 않습니다.§f"
					+ "\n또한 보호대상은 당신의 도움으로 인하여 " 
					+ "\n밤에 타인에게 받는 §c모든 데미지가 2감소§f합니다."
					+ "\n무고한자를 지킬사람은 당신뿐입니다.");
		} else if (jobName.equalsIgnoreCase("탐정")) {
			p.sendMessage(ChatColor.GOLD + "당신의 직업 : " + ChatColor.AQUA + getJob(p) + ChatColor.GRAY 
					+ "\n당신은 조사능력이 뛰어난 탐정입니다."
					+ "\n당신은 §c시체를 조사§f하여(시체 위에서 우클릭)"
					+ "\n그 플레이어의 이름, 직업, 사망 날짜, 살해 흉기, 근처에 있던 플레이어를 알 수 있습니다."
					+ "\n정보를 모아 추리하여 사건을 해결하세요!");
		} else if (jobName.equalsIgnoreCase("농부")) {
			p.sendMessage(ChatColor.GOLD + "당신의 직업 : " + ChatColor.AQUA + getJob(p) + ChatColor.GRAY
					+ "\n당신은 성실한 농부입니다."
					+ "\n당신은 시작시 §c고유 무기인 농기구§f를 들고 시작합니다." 
					+ "\n당신은 성실한 성격으로 모든 사람들에게 신뢰를 받고 있습니다."
					+ "\n당신의 직업이 농부라는 것은 §c모든 플레이어가 알고 있습니다.§f"
					+ "\n모든 사람들이 믿고있는 당신이 나설 차례입니다.");
		} else if (jobName.equalsIgnoreCase("시민")) {
			p.sendMessage(ChatColor.GOLD + "당신의 직업 : " + ChatColor.AQUA + getJob(p) + ChatColor.GRAY
					+ "\n당신은 시민입니다."
					+ "\n특별한 능력을 가지지 않았지만." 
					+ "\n살인자를 찾아내기 위해서는 당신이 필요합니다.");
		}else if (jobName.equalsIgnoreCase("계약자")) {
			p.sendMessage(ChatColor.GOLD + "당신의 직업 : " + ChatColor.AQUA + getJob(p) + ChatColor.GRAY
					+ "\n당신은 살인 청부업자입니다."
					+ "\n당신은 아침에 1명을 지정할 수 있습니다." 
					+ "\n지정한 플레이어는 당신의 살해 대상이며"
					+ "\n이 §c지정한 플레이어가 사망 또는 퇴장할 시 즉시 살인자의"
					+ "\n동료§f가됩니다. (살인자의 동료가 되지 전까지는"
					+ "\n시민팀으로 간주되지만 살해 대상은 당신이 죽일수 있습니다.)"
					+ "\n(게임중에는 배신자 또는 계약자만 등장합니다)." 
					+ "\n당신의 의뢰를 달성하여 살인자와 협력하고 사건을 미궁속으로 빠뜨리세요.");
		} else if (jobName.equalsIgnoreCase("영매사")) {
			p.sendMessage(ChatColor.GOLD + "당신의 직업 : " + ChatColor.AQUA + getJob(p) + ChatColor.GRAY
					+ "\n당신은 영혼과 소통하는 영매사입니다."
					+ "\n당신은 아침마다 1명을 새롭게 지정할 수 있습니다." 
					+ "\n§c지정한 대상이 사망 또는 퇴장할 시 그 영혼의 정보를 가져"
					+ "\n지정한 대상의 직업이 됩니다.§f"
					+ "\n당신만이 죽은자의 미련을 해결할 수 있을 것입니다."
					+ "\n(게임중에는 영매사와 마술사가 동시에 존재하지 않습니다.)");
		} else if (jobName.equalsIgnoreCase("협상가")) {
			p.sendMessage(ChatColor.GOLD + "당신의 직업 : " + ChatColor.AQUA + getJob(p) + ChatColor.GRAY
					+ "\n당신은 화술이 뛰어난 협상가입니다."
					+ "\n당신은 2일째밤 뒤부터 플레이어 1명을 지정할 수 있습니다." 
					+ "\n§c지정한 플레이어는 그 즉시 살인자의 동료로 변경§f됩니다."
					+ "\n단, 경찰, 군인, 농부에게는 사용이 불가능하며"
					+ "\n대상에게 당신의 직업이 들킵니다."
					+ "\n또한 능력을 사용할시 '협상가가 협상을 시도했다고 모두에게 알립니다.'."
					+ "\n(능력은 사용처리 됩니다)."
					+ "\n(당신은 살인자팀이며 사람을 죽여도 붙잡히지 않습니다.)"
					+ "\n당신의 화술로 동료를 늘리고 살인자와 동료들을 승리로 이끄세요.");
		} else if (jobName.equalsIgnoreCase("열쇠공")) {
			p.sendMessage(ChatColor.GOLD + "당신의 직업 : " + ChatColor.AQUA + getJob(p) + ChatColor.GRAY
					+ "\n당신은 손재주가 뛰어난 열쇠공입니다."
					+ "\n당신은 §c만능열쇠 4개§f를 가지고 있습니다." 
					+ "\n§c나무문을 닫고 쉬프트를 누른 상태에서§f 그 문을 좌클릭 할 시"
					+ "\n§c잠그거나 열수 있습니다.§f"
					+ "\n모든 문은 낮이 되면 잠금이 해제됩니다."
					+ "\n당신은 열쇠를 사용하면 쉽사리 당하지 않을 것입니다.");
		} 
	}
	
	public void jobHelpMsg(Player p, int jobCode) {
		p.sendMessage("\n");
		if (jobCode == 0) {
			p.sendMessage(ms+
					"\n사람들을 모두 살해하려는 살인자입니다." 
					+"\n강력한 무기를 가지고 있습니다."
					+ "\n배신자가 능력을 사용하여 접선시 배신자는 동료가 됩니다."
					+"\n만약 미치광이나 협상가가 존재한다면 협력해야합니다."
					+"\n능력을 사용하여 투표장 참가를 거부할 수 있지만 거부할시"
					+ "\n살인자라는 것을 들키게 됩니다." );
			sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
		} else if (jobCode == 1) {
			p.sendMessage(ms+
					 "\n유일한 경찰입니다."
					+ "\n아침마다 1명을 지정하여 살인자인지 아닌지 조사할 수 있습니다." 
					+ "\n조사는 밤이 되어야 끝나며 결과는 당신에게만 알려집니다." 
					+ "\n그리고 1번이라도 조사한 대상이 살인자의 팀이 되었을 때도"
					+ "\n경찰에게 알림이 갑니다."
					+ "\n또한 직감이 뛰어납니다. 누군가가 살해됐을 때 살해현장과의 거리와 좌표를 얻습니다.");
			sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
		} else if (jobCode == 2) {
			p.sendMessage(ms+
					 "\n생명을 살리는 의사입니다."
					+ "\n아침마다 1명을 지정할 수 있습니다."
					+ "\n이 지정한 플레이어는 그 밤중에 사망시 한번 부활합니다."
					+ "\n단, 자기 자신은 지정이 불가능합니다.");
			sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
		} else if (jobCode == 3) {
			p.sendMessage(ms+
					"\n탐욕한 배신자입니다."
					+ "\n기본적으로는 시민팀으로 활동합니다."
					+ "\n밤마다 지정한 플레이어 1명의 직업을 알 수 있습니다."
					+ "\n만약 지정한 플레이어가 살인자 또는 협상가라면 살인자의 동료가 되게 되며"
					+"\n살인자와 같이 밤마다 시민을 죽이고  다닐 수  있습니다."
					+ "\n단, 이 능력을 군인에게 사용시 군인은 당신이 스파이라는 것을 알게 될 것입니다." 
					+ "\n(게임중에는 배신자 또는 계약자만 등장합니다)." );
			sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
		} else if (jobCode == 4) {
			p.sendMessage(ms+
					 "\n강인한 신체를 가진 군인입니다. 사망시 한번 부활하게 됩니다.(자연사제외)" 
					+ "\n부활후에는 직업이 시민으로 변경됩니다." 
					+ "\n또한 총 1정과 총알 5발을 소지한 상태로  시작합니다.");
			sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
		} else if (jobCode == 5) {
			p.sendMessage(
					 "\n당신은 살인자의 동료입니다." 
					+ "\n모든 플레이어는 당신이 미치광라는 것을 알고 있습니다."
					+ "\n당신은 아침에 활동을 하는 것이 불가능하지만 낮이 되어도"
					+ "\n투표장으로 이동되지 않으며 아침에 위치가 표시되지도 않습니다."
					+ "\n범죄 생활에 얻은 기술로 타인보다 빠른 이동속도를 가지고 있습니다.");
			sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
		} else if (jobCode == 6) {
			p.sendMessage(
					 "\n진실을 밝히는 기자입니다." 
					+  "\n밤에 플레이어 1명을 지목할 수 있습니다." 
					+ "\n아침이 되면 그 플레이어의 직업을 모두에게 밝힙니다."
					+ "\n능력은 단 1번만 사용 가능합니다.");
			sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
		} else if (jobCode == 7) {
			p.sendMessage(
					 "\n사람들이 따르는 성직자입니다." 
					+ "\n성직자의 발언은 힘이 있어 당신의 투표는 2표로서 취급됩니다."
					+ "\n성직자는 투표로 포박되지 않습니다.");
			sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
		} else if (jobCode == 8) {
			p.sendMessage(
					"\n속임수의 달인 마술사입니다."
					+ "\n2일째(또는 그 이후) 밤에 플레이어 1명을 지정할 수 있습니다." 
					+ "\n플레이어를 지정하는 즉시 해당 플레이어의 직업을 빼았습니다."
					+ "\n지정한 플레이어가 살인자인 경우에는 그 플레이어는 사망하고"
					+ "\n마술사가 살인자가 됩니다.");
			sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
		} else if (jobCode == 9) {
			p.sendMessage(
					"\n도구에  대해 박식한 발명가입니다."
					+ "\n상자를 클릭하여 아이템을 얻을 시"
					+ "\n50%확률로 추가 아이템을 얻습니다.");
			sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
		} else if (jobCode == 10) {
			p.sendMessage("\n타인을 지키는 경호원입니다."
					+ "\n아침 또는 낮에 플레이어 1명을 지정시 해당 플레이어는 그 낮에"
					+ "\n투표로 포박되지 않습니다. 또한 보호대상은"
					+ "\n밤에 타인에게 받는 모든 데미지가 2감소합니다.");
			sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
		} else if (jobCode == 11) {
			p.sendMessage("\n조사능력이 뛰어난 탐정입니다."
					+ "\n시체를 조사하여(시체 위에서 우클릭)"
					+ "\n그 플레이어의 이름, 직업, 사망 날짜, 살해 흉기, 근처에 있던 플레이어를 알 수 있습니다.");
			sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
		} else if (jobCode == 12) {
			p.sendMessage("\n성실한 농부입니다."
					+ "\n시작시 고유 무기인 농기구를 들고 시작합니다." 
					+ "\n성실한 성격으로 모든 사람들에게 신뢰를 받고 있어"
					+ "\n이 플레이어의 직업이 농부라는 것은 모든 플레이어가 알고 있습니다.");
			sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
		} else if (jobCode == 13) {
			p.sendMessage("\n시민입니다."
					+ "\n특별한 능력을 가지지 않았습니다." 
					+ "\n시민은 마술사의 능력의 대상 또는 +"
					+"\n군인의 능력을 사용한 후에만 가지는 직업입니다.");
			sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
		} else if (jobCode == 14) {
			p.sendMessage("\n살인 청부업자입니다."
					+ "\n아침에 1명을 지정할 수 있습니다." 
					+ "\n지정한 플레이어는 당신의 살해 대상이며"
					+ "\n이 지정한 플레이어가 사망할 시 즉시 살인자의"
					+ "\n동료가됩니다. (살인자의 동료가 되지 전까지는"
					+ "\n시민팀으로 간주되지만 살해 대상은 계약자가 죽일수 있습니다.)"
					+ "\n(게임중에는 배신자 또는 계약자만 등장합니다).\" ");
			sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
		} else if (jobCode == 15) {
			p.sendMessage("\n영혼과 소통하는 영매사입니다."
					+ "\n아침마다 1명을 새롭게 지정할 수 있습니다." 
					+ "\n지정한 대상이 사망할 시 그 영혼의 정보를 가져"
					+ "\n지정한 대상의 직업이 됩니다."
					+ "\n(게임중에는 영매사와 마술사가 동시에 존재하지 않습니다.)");
			sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
		} else if (jobCode == 16) {
			p.sendMessage("\n화술이 뛰어난 협상가입니다."
					+ "\n2번째밤부터 플레이어 1명을 지정할 수 있습니다." 
					+ "\n지정한 플레이어는 그 즉시 살인자의 동료로 변경됩니다."
					+ "\n단, 경찰, 군인, 농부에게는 사용이 불가능하며."
					+ "\n대상에게 당신의 직업이 들킵니다."
					+ "\n또한 능력을 사용할시 '협상가가 협상을 시도했다고 모두에게 알립니다.'."
					+ "\n(능력은 사용처리 됩니다)."
					+ "\n(협장자는 살인자팀이며 사람을 죽여도 붙잡히지 않습니다.)");
			sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
		} else if (jobCode == 17) {
			p.sendMessage("\n손재주가 뛰어난 열쇠공입니다."
					+ "\n만능열쇠 4개를 가지고 있습니다." 
					+ "\n§c나무문을 닫고 쉬프트를 누른 상태에서§f 그 문을 좌클릭 할 시"
					+ "\n잠그거나 열수 있습니다."
					+ "\n모든 문은 낮이 되면 잠금이 해제됩니다.");
			sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
		}
	}

	public void voteInit() {
		voteMap.clear();
		voteCnt.clear();
	}

	public void votePlayer(Player p, int slot) {
		String target = null;
		if(playerInven.getItem(slot) != null) {
			if(playerInven.getItem(slot).hasItemMeta()) {
				ItemMeta meta = playerInven.getItem(slot).getItemMeta();
				if(meta.hasDisplayName()) {
					target = meta.getDisplayName();
				}
			}
		}	
		if(target == null) return;
		if (!ingame) {
			p.sendMessage(ms + "게임이 시작되지 않았습니다.");
		} else if (dayTime != 1) {
			p.sendMessage(ms + "투표는 낮에만 가능합니다.");
		} else if(getJob(p).equalsIgnoreCase("미치광")) {
			p.sendMessage(ms + "당신에게는 권한이 없습니다.");
		} else if (!virtualJobMap.containsKey(target)) {
			p.sendMessage(ms + target + " 님은 생존중이 아닙니다.");
		} else if (ftmPlayerList.get(target).job.equalsIgnoreCase("미치광")) {
			p.sendMessage(ms + target + " 님은 미치광입니다. 미치광에게는 투표가 불가능합니다.");
		} else if (voteMap.containsKey(p.getName())) {
			p.sendMessage(ms + voteMap.get(p.getName()) + " 님께 이미 투표 하셨습니다.");
		} else {
			voteMap.put(p.getName(), target);
			if (!voteCnt.containsKey(target)) {
				voteCnt.put(target, 1);
			} else {
				voteCnt.put(target, voteCnt.get(target) + 1);
			}
			//p.sendMessage(ms + target + " 님을 투표했습니다.");
			ActionBarAPI.sendActionBar(p, ChatColor.RED+target + " 님을 투표했습니다.", 80);
			if (getJob(p.getName()).equalsIgnoreCase("성직자")) {
				voteCnt.put(target, voteCnt.get(target) + 1);
				p.sendMessage(ms + "당신의 말에는 힘이 있습니다. 당신의 투표는 2표로서 취급됩니다.");
				priestTarget = target;
			}
			sendMessage(ms + "누군가가 " + ChatColor.AQUA + target + ChatColor.RESET + " 님에게 투표했습니다.");
			sendSound(Sound.BLOCK_NOTE_PLING, 1.5f, 1.2f);
		}
	}

	public void disposalVote() {
		List<String> keyList = new ArrayList<String>();
		keyList.addAll(voteCnt.keySet());
		List<Integer> valueList = new ArrayList<Integer>();
		valueList.addAll(voteCnt.values());

		int maxCnt = 0;
		int maxPlayerCnt = 0;
		String maxPName = null;
		for (int i = 0; i < valueList.size(); i++) {
			if (valueList.get(i) > maxCnt)
				maxCnt = valueList.get(i);
		}

		for (int i = 0; i < keyList.size(); i++) {
			String nowKey = keyList.get(i);
			if (voteCnt.get(nowKey) == maxCnt) {
				maxPlayerCnt += 1;
				maxPName = nowKey;
			}
		}
		
		for(String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p)) p.removePotionEffect(PotionEffectType.GLOWING);
		}

		if (maxPlayerCnt == 1) {
			if(getJob(maxPName).equalsIgnoreCase("성직자")) {
				sendMessage(ms + maxPName + " 님은 성스러운 가호로 인해 투표로 포박되지 않았습니다.");
				virtualJobMap.put(maxPName, "성직자");
				updatePlayerInven();
				FtmPlayer ftmP = ftmPlayerList.get(maxPName);
				if(ftmP != null) ftmP.priest_noVoted += 1;
			} else if(maxPName.equalsIgnoreCase(guardTarget)) {
				sendMessage(ms + maxPName + " 님은 경호원의 보호로 인하여 투표로 포박되지 않았습니다.");
			} else {
				if(ingamePlayer.contains(maxPName)) {
					sendMessage(ms + maxPName + " 님이 가장 많은 투표를 받아 포박되었습니다.");
					votedPlayer = maxPName;
					FtmPlayer ftmP = ftmPlayerList.get(maxPName);
					if(ftmP != null) ftmP.beVotedPlayer += 1;
					String jobName = getJob(votedPlayer);
					sendMessage(ms+ votedPlayer + " 님은 §c"+jobName+ " §f입니다.");
					updatePlayerInven();
					virtualJobMap.put(maxPName, jobName);
					Player votedP = Bukkit.getPlayer(votedPlayer);
					votedP.sendMessage(ms+"당신은 포박된 상태입니다. 낮이 될 때까지 공격력이 절반이 됩니다.");
					updatePlayerInven();
					if(!votedP.hasPotionEffect(PotionEffectType.GLOWING)) votedP.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 2800, 0));
					if(priestTarget != null) {
						if(priestTarget.equalsIgnoreCase(maxPName)) {
							for(String tName : ingamePlayer) {
								if(tName.equalsIgnoreCase("성직자")) {
									if(ftmP != null) ftmP.priest_effort += 1;
									break;
								}
							}
						}
					}				
				}else {
					sendMessage(ms + maxPName + " 님이 가장 많은 투표를 받았지만 현재 게임 참가중이 아닙니다.");			
				}			
			}
		} else {
			sendMessage(ms + "최다 득표를 받은 플레이어가 존재하지 않습니다. \n"+ms+"포박 투표는 취소되었습니다.");
		}
		voteMap.clear();
		voteCnt.clear();
	}

	public void gameQuitPlayer(Player p, boolean announce, boolean giveGold) {
		if (ingamePlayer.contains(p.getName())) {
			ingamePlayer.remove(p.getName());
			virtualJobMap.remove(p.getName());
			server.playerList.put(p.getName(), "로비");
			p.getInventory().clear();
			if (ingame) {
				if(giveGold) {
					if(announce) {
						FtmPlayer ftmP = ftmPlayerList.get(p.getName());
						if(ftmP != null) ftmP.death += 1;
						
						if(murderTeam.contains(p.getName())) {
							murderTeam_backUp.add(p.getName());
						}else {
							civilTeam_backUp.add(p.getName());
						}
						
						sendMessage(ms + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " 님이 사망하셨습니다.");
						sendTitle("", ChatColor.RED+p.getName()+"님이 살해됐습니다.", 60);
						sendSound(Sound.ENTITY_GHAST_DEATH,1.5f, 1.3f);
					}
					p.sendMessage(ms+"게임 플레이 보상으로 10골드를 받으셨습니다.");
				} else {
					if(announce) {
						
						FtmPlayer ftmP = ftmPlayerList.get(p.getName());
						if(ftmP != null) ftmP.ignore = true;
						sendMessage(ms + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " 님이 퇴장하셨습니다.");
						sendSound(Sound.BLOCK_NOTE_GUITAR,1.5f, 2.0f);
					}
				}
				
				/////영매사, 계약자 능력사용
				//Bukkit.broadcastMessage("quit: " + p.getName());
				//Bukkit.broadcastMessage("con_target: " + contractorTarget);
				//Bukkit.broadcastMessage("soul_target: " + soul_target);	
				if(contractorTarget != null) {
					if(contractorTarget.equalsIgnoreCase(p.getName())) {
						for(String tName : ingamePlayer) {
							if(getJob(tName).equalsIgnoreCase("계약자")) {
								if(murderTeam.contains(tName)) {
									//이미 살마팀이면 패스
								}else {
									if(!murderTeam.contains(tName)) murderTeam.add(tName);
									sendTeamChat(murderTeam, ms+"계약자 "+tName+"님이 의뢰를 달성하여 동료가 됐습니다. 이제 그도 당신의 동료입니다.");
									sendTeamSound(murderTeam, Sound.BLOCK_NOTE_HARP);
									civilTeam.remove(tName);
									FtmPlayer ftmP = ftmPlayerList.get(tName);
									if(ftmP != null) ftmP.contractor_success += 1;
									if(police_invensted.contains(tName)) {
										for(String tmpName : ingamePlayer) {
											if(getJob(tmpName).equalsIgnoreCase("경찰")) {
												Player tmp = Bukkit.getPlayer(tmpName);
												if(existPlayer(tmp)) {
													tmp.sendMessage(ms+"§f당신이 조사했던 §c"+tName+" 님이 살인자의 동료가 된 것 같습니다.");
													tmp.playSound(tmp.getLocation(), Sound.BLOCK_NOTE_HARP, 1.0f, 1.0f);
												}
												break;
											}
										}
									}
									if (civilTeam.size() <= 0) {
										murderWin();
									}
									break;
								}							
							}
						}
					}
				}
				if(soul_target != null) {
					if(soul_target.equalsIgnoreCase(p.getName())) {
						for(String tName : ingamePlayer) {
							if(getJob(tName).equalsIgnoreCase("영매사")) {
								//Bukkit.broadcastMessage("tJob: " + getJob(tName));
								String target = p.getName();
								String tJob = getJob(target);
								Player soul_player = Bukkit.getPlayer(tName);	
								boolean isMurderTeam = murderTeam.contains(target);
								
								FtmPlayer ftmP = ftmPlayerList.get(tName);
								if(ftmP != null) {
									ftmP.shaman_success += 1;
									if(isMurderTeam)ftmP.shaman_successMurderTeam += 1;
								}
								
								p.sendMessage(ms+target+" 님의 직업을 얻었습니다.");
								soul_player.playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1.5f, 1.5f);
								setFullJob(soul_player, getJobCode(tJob));
								if(tJob.equalsIgnoreCase("배신자") && isMurderTeam) {
									soul_player.sendMessage(ms + p.getName() + "님이 접선을 완료한 배신자였기에 접선상태가 됩니다.\n" + ms
											+ "\"/tc <메세지> \"명령어로 살인자팀만의 대화가 가능합니다.");
									sendTeamChat(murderTeam,
											ms + "영매사였던 " + tName + "님이 접선한 배신자의 영혼을 얻었습니다. 이제 그도 당신의 동료입니다.");
									sendTeamSound(murderTeam, Sound.BLOCK_NOTE_HARP);
									if(!murderTeam.contains(tName)) murderTeam.add(tName);
									civilTeam.remove(tName);
									if(police_invensted.contains(tName)) {
										for(String tmpName : ingamePlayer) {
											if(getJob(tmpName).equalsIgnoreCase("경찰")) {
												Player tmp = Bukkit.getPlayer(tmpName);
												if(existPlayer(tmp)) {
													tmp.sendMessage(ms+"§f당신이 조사했던 §c"+target+" 님이 살인자의 동료가 된 것 같습니다.");
													tmp.playSound(tmp.getLocation(), Sound.BLOCK_NOTE_HARP, 1.0f, 1.0f);
												}
												break;
											}
										}
									}
									if (civilTeam.size() <= 0) {
										murderWin();
									}
									soul_player.getInventory().addItem(spy_axe);
								} else if(tJob.equalsIgnoreCase("계약자") && isMurderTeam) {
									soul_player.sendMessage(ms+p.getName()+"님이 의뢰를 완료한 계약자였기에 살인마의 동료가 됩니다.\n"+ms+"\"/tc <메세지> \"명령어로 살인자팀만의 대화가 가능합니다.");
									sendTeamChat(murderTeam, ms+"영매사였던  "+tName+"님이 계약자의 영혼을 얻었습니다. 이제 그도 당신의 동료입니다.");
									sendTeamSound(murderTeam, Sound.BLOCK_NOTE_HARP);
									if(!murderTeam.contains(tName)) murderTeam.add(tName);
									civilTeam.remove(tName);
									if(police_invensted.contains(p.getName())) {
										for(String tmpName : ingamePlayer) {
											if(getJob(tmpName).equalsIgnoreCase("경찰")) {
												Player tmp = Bukkit.getPlayer(tmpName);
												if(existPlayer(tmp)) {
													tmp.sendMessage(ms+"§f당신이 조사했던 §c"+target+" 님이 살인자의 동료가 된 것 같습니다.");
													tmp.playSound(tmp.getLocation(), Sound.BLOCK_NOTE_HARP, 1.0f, 1.0f);
												}
												break;
											}
										}
									}
									if(civilTeam.size() <= 0) {
										murderWin();
									}
								} else if(tJob.equalsIgnoreCase("살인자") && isMurderTeam) {
									soul_player.sendMessage(ms+p.getName()+"님이 살인자였기 때문에 당신이 살인자가 됩니다.\n"+ms+"\"/tc <메세지> \"명령어로 살인자팀만의 대화가 가능합니다.");
									sendTeamChat(murderTeam, ms+"영매사였던 "+tName+"님이 살인자의 영혼을 얻었습니다. 이제 그도 당신의 동료입니다.");
									sendTeamSound(murderTeam, Sound.BLOCK_NOTE_HARP);
									if(!murderTeam.contains(tName)) murderTeam.add(tName);
									civilTeam.remove(tName);
									if(police_invensted.contains(p.getName())) {
										for(String tmpName : ingamePlayer) {
											if(getJob(tmpName).equalsIgnoreCase("경찰")) {
												Player tmp = Bukkit.getPlayer(tmpName);
												if(existPlayer(tmp)) {
													tmp.sendMessage(ms+"§f당신이 조사했던 §c"+target+" 님이 살인자의 동료가 된 것 같습니다.");
													tmp.playSound(tmp.getLocation(), Sound.BLOCK_NOTE_HARP, 1.0f, 1.0f);
												}
												break;
											}
										}
									}
									if(civilTeam.size() <= 0) {
										murderWin();
									}
								} else if(tJob.equalsIgnoreCase("협상가") && isMurderTeam) {
									soul_player.sendMessage(ms+target+"님이 의뢰를 완료한 계약자였기에 살인마의 동료가 됩니다.\n"+ms+"\"/tc <메세지> \"명령어로 살인자팀만의 대화가 가능합니다.");
									sendTeamChat(murderTeam, ms+"영매사였던 "+tName+"님이 협상가의 영혼을 얻었습니다. 이제 그도 당신의 동료입니다.");
									sendTeamSound(murderTeam, Sound.BLOCK_NOTE_HARP);
									if(!murderTeam.contains(tName)) murderTeam.add(tName);
									civilTeam.remove(tName);
									if(police_invensted.contains(p.getName())) {
										for(String tmpName : ingamePlayer) {
											if(getJob(tmpName).equalsIgnoreCase("경찰")) {
												Player tmp = Bukkit.getPlayer(tmpName);
												if(existPlayer(tmp)) {
													tmp.sendMessage(ms+"§f당신이 조사했던 §c"+target+" 님이 살인자의 동료가 된 것 같습니다.");
													tmp.playSound(tmp.getLocation(), Sound.BLOCK_NOTE_HARP, 1.0f, 1.0f);
												}
												break;
											}
										}
									}
									if(civilTeam.size() <= 0) {
										murderWin();
									}
								}
								break;
							}
						}
					}
				}
				
				if (murderTeam.contains(p.getName())) {
					murderDead(p);
				} else {
					civilDead(p);
				}
				if(jobSidebar != null) jobSidebar.hideFrom(p);
				
				updatePlayerInven();
			} else {
				if(announce) {
					sendMessage(ms + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " 님이 퇴장 했습니다. "
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
	
	private void murderDead(Player p) {
		String pJob = getJob(p);
		murderTeam.remove(p.getName());
		if (pJob.equalsIgnoreCase("살인자")) {
			sendMessage(ms + p.getName() + " 님은 살인자였습니다!");
			peopleKnowDeny = true;
		} else if (pJob.equalsIgnoreCase("미치광"))
			sendMessage(ms + p.getName() + " 님은 미치광였습니다!");
		else if (pJob.equalsIgnoreCase("배신자") && betrayered) //배신한 배신자일때
			sendMessage(ms + p.getName() + " 님은 스파이였습니다!");
		if(murderTeam.size() <= 0) {
			civilWin();
		} else {
			if(pJob.equalsIgnoreCase("살인자")) sendMessage(ms+"살인자는 사라졌지만 아직 살인자의 동료가 남아있습니다!");
			else sendMessage(ms+"아직 살인자의 동료가 남아있습니다!");
		}
	}
	
	private void civilDead(Player p) {
		civilTeam.remove(p.getName());
		if(civilTeam.size() <= 0) {
			murderWin();
		}
	}
	
	private void doctorReverse(String doctorName, Player p) {
		sendMessage(ms+"의사가 치명상을 입은 "+p.getName()+" 님을  수술하여 살려냈습니다!");
		doctorTarget = null;
		reversePlayer(p);
		FtmPlayer ftmP = ftmPlayerList.get(doctorName);
		if(ftmP != null) ftmP.doctor_revive += 1;
	}
	
	private void soldierReverse(Player p) {
		sendMessage(ms+"군인 "+p.getName()+" 님이 살인자의 공격을 버텨내셨습니다!");
		virtualJobMap.put(p.getName(), "군인");
		soldier_use = true;
		reversePlayer(p);
		updatePlayerInven();
		FtmPlayer ftmP = ftmPlayerList.get(p.getName());
		if(ftmP != null) ftmP.soldier_revive += 1;
	}
	
	private void reversePlayer(Player p) {
		p.teleport(loc_startList.get(MyUtility.getRandom(0, loc_startList.size() - 1)), TeleportCause.PLUGIN);
		MyUtility.healUp(p);
	}

	private void murderWin() {
		if(ending) return;
		ending = true;
		
		isMurderWin = true;
		
		sendTitle("사건 종결", ChatColor.GRAY + "모든 사람들이 사망하였습니다.", 70);
		sendSound(Sound.BLOCK_NOTE_BELL, 1.0f, 0.5f);
		
		List<Player> tmpList = new ArrayList<Player>(ingamePlayer.size()) ;
		for (String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p)) {
				tmpList.add(p);
			}
		}
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				for (Player p : tmpList) {
					if(getJob(p).equalsIgnoreCase("살인자")) {
						p.sendMessage(ms+"당신을 잡으려고 하였던 사람들은 모두 살해되었습니다.");	
					} else if(getJob(p).equalsIgnoreCase("미치광")) {
						p.sendMessage(ms+"당신은 살인자와 협력하여 사람들을 모두 살해하였습니다.");	
					} else if(getJob(p).equalsIgnoreCase("배신자")) {
						p.sendMessage(ms+"당신은 살인자와 협력하여 사람들을 해치기로 결정하였고");
					} else if(getJob(p).equalsIgnoreCase("협상가")) {
						p.sendMessage(ms+"당신은  살인자와 협력하여 사람들을 모두 살해하였습니다.");
					} else if(getJob(p).equalsIgnoreCase("계약자")) {
						p.sendMessage(ms+"당신은 완벽히 의뢰를 달성하였습니다.");
					} else { //협상가 능력으로 팀된 경우일거임
						p.sendMessage(ms+"당신은 협상가에게 포섭돼 살인자와 협력하여");
					}
					
				}
				sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
			}
		}, 100L);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				for (Player p : tmpList) {
					if(getJob(p).equalsIgnoreCase("살인자")) {
						p.sendMessage(ms+"당신은 사람들의 시체를 모두 저택의 아래에 묻었으며");	
					} else if(getJob(p).equalsIgnoreCase("미치광")) {
						p.sendMessage(ms+"살인자는 범죄 현장을 정리한 후 모습을 감췄고");
					} else if(getJob(p).equalsIgnoreCase("협상가")) {
						p.sendMessage(ms+"당신은 살인자에게 많은 돈을 받고");
					}else if(getJob(p).equalsIgnoreCase("배신자")) {
						p.sendMessage(ms+"다른 모든 사람들을 살해하였습니다.");
					}else if(getJob(p).equalsIgnoreCase("계약자")) {
						p.sendMessage(ms+"당신은 의뢰 대상이 아닌 사람들까지 해쳤지만");
					}  else { //협상가 능력으로 팀된 경우일거임
						p.sendMessage(ms+"무고한 사람들을 해쳤습니다.");
					}
					
				}
				sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
			}
		}, 160L);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				for (Player p : tmpList) {
					if(getJob(p).equalsIgnoreCase("살인자")) {
						p.sendMessage(ms+"완전 범죄를 위하여 혈흔, 흉기등의 증거 또한 정리하였습니다.");	
					} else if(getJob(p).equalsIgnoreCase("미치광")) {
						p.sendMessage(ms+"당신은 저택에 있는 고가의 물건을 훔쳐 달아났습니다.");
					} else if(getJob(p).equalsIgnoreCase("협상가")) {
						p.sendMessage(ms+"유유히 저택을 떠났습니다.");
					}else if(getJob(p).equalsIgnoreCase("배신자")) {
						p.sendMessage(ms+"그리곤 저택에 있는 고가의 물건들을 모두 훔쳐 저택을 떠났습니다.");
					}else if(getJob(p).equalsIgnoreCase("계약자")) {
						p.sendMessage(ms+"아무런 감정을 느끼지 않는 것 같습니다..");
					}  else { //협상가 능력으로 팀된 경우일거임
						p.sendMessage(ms+"당신은 협상의 대가로서");
					}
					
				}
				sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
			}
		}, 220L);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				for (Player p : tmpList) {
					if(getJob(p).equalsIgnoreCase("살인자")) {
						p.sendMessage(ms+"언젠가는 이 사건에 대해 세상사람들이 알게 될 수 있겠지만");	
					} else if(getJob(p).equalsIgnoreCase("미치광")) {
						p.sendMessage(ms+"당신은 이 충격적인 사건에 대하여 진실을 알고 있는 사람이지만");
					} else if(getJob(p).equalsIgnoreCase("협상가")) {
						p.sendMessage(ms+"당신은 돈을 위해 사람들을 해졌지만");
					}else if(getJob(p).equalsIgnoreCase("배신자")) {
						p.sendMessage(ms+"탐욕에 눈이 멀어 사람들을 배신한 당신");
					}else if(getJob(p).equalsIgnoreCase("계약자")) {
						p.sendMessage(ms+"하긴 살인 청부업자로서 많은 사람들 죽인 당신이");
					}  else { //협상가 능력으로 팀된 경우일거임
						p.sendMessage(ms+"살인자와 협상가에게 많은 돈을 받았지만");
					}
					
				}
				sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
			}
		}, 280L);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				for (Player p : tmpList) {
					if(getJob(p).equalsIgnoreCase("살인자")) {
						p.sendMessage(ms+"당신이 사건의 주역이라는 것은 누구도 알지 못할 것입니다.");	
					} else if(getJob(p).equalsIgnoreCase("미치광")) {
						p.sendMessage(ms+"그렇다한들 무슨 상관이 있겠습니까?");
					} else if(getJob(p).equalsIgnoreCase("협상가")) {
						p.sendMessage(ms+"정말 돈이 사람의 목숨보다 가치 있었을까요?");
					}else if(getJob(p).equalsIgnoreCase("배신자")) {
						p.sendMessage(ms+"죄책감을 느끼고 있긴 한가요?");
					}else if(getJob(p).equalsIgnoreCase("계약자")) {
						p.sendMessage(ms+"이제와서 살인에 감정을 느낄리가 없죠.");
					} else { //협상가 능력으로 팀된 경우일거임
						p.sendMessage(ms+"이게 올바른 일이라고 생각시나요?");
					}
					sendTitle("§c승리", "", 80);
				}
				sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
			}
		}, 340L);
		
		try {
			Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
				public void run() {
					for (String pName : ingamePlayer) {
						Player p = Bukkit.getPlayer(pName);
						if(existPlayer(p)) {
							sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
						}
					}
				}
			}, 420L);
		} catch (Exception e) {
		
		}
		try {
			Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
				public void run() {
					divideSpawn();
					endGame(false);
					server.egCM.broadCast(
							server.ms_alert + ChatColor.GRAY  + "§c살인자팀§7의 §a승리§7로 §c"+disPlayGameName+"§7이 종료 되었습니다.");
				}
			}, 500L);
		} catch(Exception e) {
			endGame(true);
		}
	}

	private void civilWin() {
		if(ending) return;
		ending = true;
		
		isMurderWin = false;
		
		for(String pName : ingamePlayer) {
			if(murderTeam.contains(pName)) {
				murderTeam_backUp.add(pName);
			}else {
				civilTeam_backUp.add(pName);
			}
		}
		
		sendTitle("사건 종결", ChatColor.GRAY + "살인자들을 붙잡았습니다.", 70);
		sendSound(Sound.BLOCK_NOTE_BELL, 1.0f, 0.5f);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
					sendMessage(ms+"당신은 사람들과 협력하여 마침내 살인자와 그 동료들을 붙잡았습니다.");
					sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
			}
		}, 100L);
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
					sendMessage(ms+"살인자와 그 동료들은 교도소에 수감되어 죗값을 치루게 될 것입니다.");
					sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
			}
		}, 160L);
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
					sendMessage(ms+"그 후 당신과 동료들은 지인의 장례식에 참가하여 명복을 빌어줬습니다.");
					sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
			}
		}, 220L);
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
					sendMessage(ms+"당신은 이제 일상으로 돌아가지만");
					sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
			}
		}, 280L);
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
					sendMessage(ms+"이 사건은 최악의 사건중 하나로 오랫동안 남게될 것입니다.");
					sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
					sendTitle("§c승리", "", 80);
			}
		}, 340L);
		try {
			Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
				public void run() {
					for (String pName : ingamePlayer) {
						Player p = Bukkit.getPlayer(pName);
						if(existPlayer(p)) {
							sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
						}
					}
				}
			}, 420L);
		} catch (Exception e) {
			
		}
		try {
			Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
				public void run() {
					divideSpawn();
					endGame(false);
					server.egCM.broadCast(
							server.ms_alert + ChatColor.GRAY + "§b시민팀§7의 §a승리§7로 §c"+disPlayGameName+"§7이 종료 되었습니다.");
				}
			}, 500L);
		} catch (Exception e) {
			endGame(true);
		}
	}

	public void giveChestitem(Player p) {
		int num = MyUtility.getRandom(0, chestItem.size() + 2);
		p.getWorld().playSound(p.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0f, 1.0f);
		if (num == chestItem.size()) {
			p.sendMessage("§7딱히 쓸만한 것이 보이지 않는다.");
			return;
		}
		if (num == chestItem.size() + 1) {
			p.sendMessage("§7먼지밖에 없는 것 같다...");
			return;
		}
		if (num == chestItem.size() + 2) {
			p.sendMessage("§7별다른게 보이지는 않는다.");
			return;
		}
		if (num == 8 || num == 10 || num == 19) {
			for (int i = 0; i <= MyUtility.getRandom(0, 4); i++)
				p.getInventory().addItem(chestItem.get(num));
		}
		if(getJob(p).equalsIgnoreCase("발명가") && MyUtility.getRandom(0, 1) == 1) {
			int anum = MyUtility.getRandom(0, chestItem.size() - 1);
			if (anum == 8 || anum == 10 || anum == 19) {
				for (int i = 0; i <= MyUtility.getRandom(0, 4); i++)
					p.getInventory().addItem(chestItem.get(anum));
			}
			p.getInventory().addItem(chestItem.get(anum));
			p.sendMessage("§7엇? 상자에서 쓸만한 것을 추가로 발견하였다. (발명가 능력)");
			FtmPlayer ftmP = ftmPlayerList.get(p.getName());
			if(ftmP != null) ftmP.creator_addictionItem += 1;
		}
		p.getInventory().addItem(chestItem.get(num));
		p.updateInventory();
	}

	/*public void setVoteInven() {
		Inventory jobMap = Bukkit.createInventory(null, 45, ""+ChatColor.BLACK+ChatColor.BOLD+"플레이어 목록");
		List<String> pList = new ArrayList<String>(virtualJobMap.size());
		pList.addAll(virtualJobMap.keySet());
		int tmpSlot = 1;
		for (int i = 0; i < pList.size(); i++) {
			ItemStack head = new ItemStack(Material.SKULL, 1,(byte)4);
			ItemMeta meta = head.getItemMeta();
			List<String> loreList = new ArrayList<String>(3);
			loreList.add("§7- 직업: §e" + virtualJobMap.get(pList.get(i)));
			meta.setLore(loreList);
			jobMap.setItem(tmpSlot, head);
		}
	} 안씀 */

	public void endGame(boolean force) {
		if(force) {
			server.egCM.broadCast(server.ms_alert + "살인자를 찾아라 게임이 강제 종료 되었습니다.");
		}
		
		saveData();
		
		divideSpawn();
		ingame = false;
		ending = false;
		ingamePlayer.clear();
		initGame();
		rankMap.clear();
		
		for(Entity entity : Bukkit.getWorld("world").getEntities()) {
			if(entity instanceof Item || entity instanceof Arrow) {
				if(entity.getLocation().distance(loc_Join) < 100) entity.remove();
			}
		}
	}
	
	@Override
	public void saveData() {
		for(String pName : ftmPlayerList.keySet()) {
			if(murderTeam.contains(pName)) { //무슨 팀이었는가 저장
				murderTeam_backUp.add(pName);
			}else {
				civilTeam_backUp.add(pName);
			}
			
			FtmPlayer ftmP = ftmPlayerList.get(pName);
			if(ftmP == null)continue;
			PlayerData playerData = server.egDM.getPlayerData(pName);
			if(playerData == null) continue;
			MinigameData gameData = playerData.getGameData("FindTheMurder");
			if(gameData == null) continue;
			if(!(gameData instanceof FtmData)) continue;
			FtmData playerFtmData = (FtmData) gameData;
			if(ftmP.ignore) {
				if(gameName.equalsIgnoreCase("FindTheMurder2")) { //탈주는 50점 감점
					playerFtmData.setMMR(playerFtmData.getMMR() - 50);
				}
				continue;
			}
			playerFtmData.applyNewData(ftmP);
			playerFtmData.addPlaycount();
			if(murderTeam_backUp.contains(pName)) {
				if(isMurderWin) {
					playerFtmData.addWin();	
				}else {
					playerFtmData.addDefeat();
				}
			}else {
				if(isMurderWin) {
					playerFtmData.addDefeat();
				}else {
					playerFtmData.addWin();	
				}
			}
			
			//MMR 책정
			if(gameName.equalsIgnoreCase("FindTheMurder2")) { //2채널만 mmr 설정
				int mmr = calcMMR(ftmP);
				playerFtmData.setMMR(playerFtmData.getMMR() + mmr);
				Player p = Bukkit.getPlayer(pName);
				if(existPlayer(p)) {
					p.sendMessage(ms+"§f"+disPlayGameName+"§f의 게임 결과로 §a"+mmr+"§f점이 반영됐습니다.");
				}
			}				
			playerFtmData.saveData();
		}
	}
	
	public int calcMMR(FtmPlayer ftmP) {
		int mmr = 0;
		
		mmr -= ftmP.death * 5; //사망시 -5
		
		if(murderTeam_backUp.contains(ftmP.playerName)) { //살마팀이었을때
			//살마팀 추가 MMR
			mmr += ftmP.civil_kill * 5; //시민팀 킬당 +5
			
			mmr += ftmP.spy_contact * 10; //배신자는 접선점수 +10
			
			mmr += ftmP.negotiator_success * 8; //협상자 포섭 성공시 +8
			
			mmr -= ftmP.negotiator_fail * 3; //포섭 실패시 -3
			
			mmr += ftmP.contractor_success * 9; //계약자 의뢰 성공시 +9
			
			mmr += ftmP.crazy_kill * 3; //미치광은 추가로 킬당 +3
			
			if(isMurderWin) { //살마승이라면
				mmr += 40; //승리 MMR
				
			}else { //살마패라면
				mmr -= 25;	//패배 MMR
			}
		}else { //시민팀일때
			mmr += ftmP.police_success * 10; //경찰 맞추면 10점 추가
			
			mmr += ftmP.doctor_revive * 9; //의사 살리면 +9
			
			mmr += ftmP.soldier_revive * 5; //군인 부활 +5
			
			mmr += ftmP.reporter_report * 3; //기자 능력사용 + 3
			
			mmr += ftmP.reporter_reportSuccess * 12; //기자가 살마팀 맞추면 추가 12점
			
			mmr -= ftmP.innocent_kill * 25; //무고한 사람 죽이면 -25
			
			mmr += ftmP.keySmith_Use * 1; //열쇠공 열쇠쓰면 +1
			
			mmr += ftmP.magician_take * 3; //마술사 능력사용 +3
			
			mmr += ftmP.magician_takeMurderTeam * 5; //살마팀에게 마술사 능력 사용 +5
			
			mmr += ftmP.creator_addictionItem * 0.5; //발명가 능력 사용 +0.5
			
			mmr += ftmP.priest_effort * 2; //성직자 투표 영향력 발휘시 +2
			
			mmr += ftmP.priest_noVoted * 3; //성직자 포박 방지시 +3
			
			mmr += ftmP.shaman_success * 2; //영매사 능력 사용시 +2
			
			mmr += ftmP.shaman_successMurderTeam * 7; //영매사 능력 살마팀에게 사용시 +7
			
			mmr += ftmP.farmer_kill * 4; //농부는 킬당 +4
			
			if(!isMurderWin) { //시민승이라면
				mmr += 35; //승리 MMR											
			}else {
				mmr -= 30; //패배 MMR
			}
		}
		return mmr;
	}

	//////////////// 이벤트
	public class EventHandlerFTM extends EGEventHandler {

		private FindTheMurder game;

		public EventHandlerFTM(EGServer server, FindTheMurder ftm) {
			super(server);
			this.game = ftm;
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
				if(cmd[0].equalsIgnoreCase("/tc") || cmd[0].equalsIgnoreCase("/팀챗") ) {
					if(murderTeam.contains(p.getName())) {
						if(cmd.length < 2) {
							p.sendMessage(ms+"메세지를 입력해주세요.");
							p.sendMessage(ms+"/tc <메세지>");
						} else {
							String str = "§7[ §6살인자팀챗§7 ] "+p.getName()+" >> ";
							for(int i = 1; i < cmd.length; i++) {
								str += " "+cmd[i];
							}
							sendTeamChat(murderTeam, str);
						}
					} else {
						p.sendMessage(ms+"팀 채팅은 살인자팀만 가능합니다.");
					}
					e.setCancelled(true);
				} else if(server.cmdSpawn.contains(cmd[0])) {
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
					if(!ingame) {
				        if (cause.equals(DamageCause.VOID) && !ingame) { //대기실 허공 뎀없음, 텔포		           
				            p.teleport(loc_Join, TeleportCause.PLUGIN);
				        }
				        e.setCancelled(true);
					} else {
						if(waiting || ending || voting) {
							e.setCancelled(true);
						}
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
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}	
				gameHelper(p, e.getSlot());
			} else if (e.getInventory().getTitle().equalsIgnoreCase("§0§l"+inventoryGameName+" 직업목록")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}	
				p.closeInventory();
				gameHelperAbilityClick(p, e.getSlot());	
			} else if (e.getInventory().getTitle().equalsIgnoreCase("§0§l"+inventoryGameName+" 게임설명")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}	
				p.closeInventory();
				gameHelperAbClick(p, e.getSlot());	
			}
			if (!ingamePlayer.contains(p.getName())) return;
			if (e.getInventory().getTitle().equalsIgnoreCase("§0§l시체 조사")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}	
				p.closeInventory();
				detectorInvenClick(p, e.getSlot());	
			} else if (e.getInventory().getTitle().equalsIgnoreCase("§0§l살인자를 찾아라")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}	
				p.closeInventory();
				ftmHelperInvenClick(p, e.getSlot());	
			} else if (e.getInventory().getTitle().equalsIgnoreCase("§0§l능력")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}	
				p.closeInventory();
				abilityInvenClick(p, e.getSlot());	
			} else if (e.getInventory().getTitle().equalsIgnoreCase("§0§l투표 거부 확인")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}	
				p.closeInventory();
				murderAbInvenClick(p, e.getSlot());	
			} else if (e.getInventory().getTitle().equalsIgnoreCase("§0§l능력사용")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}	
				p.closeInventory();
				useAbility(p, e.getSlot());	
			} else if (e.getInventory().getTitle().equalsIgnoreCase("§0§l조사현황")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}	
				if(voting) { //투표중에 조사현황 클릭시
					p.closeInventory();
					votePlayer(p, e.getSlot());	
				}
			} else if (e.getInventory().getTitle().equalsIgnoreCase("§0§l시체 제거")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}	
				p.closeInventory();
				spyInvenClick(p, e.getSlot());
			} 
		}
		
		@EventHandler
		public void onPlayerCraft(CraftItemEvent e) {
			if(e.getWhoClicked() instanceof Player) {
				Player p = (Player) e.getWhoClicked();	
				if (!ingamePlayer.contains(p.getName())) { //피해자가 겜에 참가중이지않으면 규칙 무시
					return;
				}
				e.setCancelled(true);
			}
		}

		@EventHandler
		public void onHitPlayer(EntityDamageByEntityEvent e) {
			if(e.getEntity() instanceof Player && ingame) { //게임시작했을때만
				Player player = (Player) e.getEntity();
				Player damager = null;
				
				if (!ingamePlayer.contains(player.getName())) { //피해자가 겜에 참가중이지않으면 규칙 무시
					return;
				}
				
				boolean isDirectAttack = true;
				
				boolean isGun = false;
				if (e.getDamager() instanceof Snowball) { //화살과 총알에대한 공격자 설정
					Snowball snowball = (Snowball) e.getDamager();
					if (snowball.getShooter() instanceof Player) {
						damager = (Player) snowball.getShooter();
						e.setDamage(7);
						isGun = true;
					}
					isDirectAttack = false;
				}
				if (e.getDamager() instanceof Arrow) {
					Arrow arrow = (Arrow) e.getDamager();
					if (arrow.getShooter() instanceof Player) {
						damager = (Player) arrow.getShooter();
					}
					isDirectAttack = false;
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
				
				FtmPlayer victimFtmP = ftmPlayerList.get(player.getName());
				FtmPlayer damagerFtmP = ftmPlayerList.get(damager.getName());
				
				ItemStack weapon = damager.getInventory().getItemInMainHand();
				if(weapon != null && (
						weapon.getType().toString().contains("AXE") || 
						weapon.getType().toString().contains("HOE") ||
						weapon.getType().toString().contains("SWORD") ||
						weapon.getType().toString().contains("SPADE") ||
						weapon.getType().toString().contains("BOW"))){
					weapon.setDurability((short) (weapon.getType().getMaxDurability() - weapon.getType().getMaxDurability()));
				}
				
				if(voting || waiting || ending) {
					e.setCancelled(true); //투표 또는 대기중은 데미지 안받음
					return;
				}

				// 게임 참가중인 플레이어라면
				if (!(getItemDamage(damager) == 0) && !isGun) { // 데미지 설정
					e.setDamage(getItemDamage(damager));
				}		
				
				if(votedPlayer.equalsIgnoreCase(damager.getName())) {
					e.setDamage((int) e.getDamage()/2);
					ActionBarAPI.sendActionBar(damager, "당신은 포박된 상태이기에 공격력이 절반이됩니다.", 80);
					damager.playSound(damager.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1.0f, 2.0f);
				}
				
				String tJob = getJob(damager);
				if(tJob == null) {
					server.egPM.printLog(ms+"EnitytyDamagerByEntityEvent의 getJob에서 Null발생");
					return;
				}
				if (tJob.equalsIgnoreCase("미치광") && dayTime != 2) { // 미치광는 아침에 살해 불가능
					//damager.sendMessage(ms + "지금은 밤이 아닙니다! 이 사람을 죽이면 체포될 것 입니다!");
					ActionBarAPI.sendActionBar(damager, ChatColor.RED + player.getName() + "지금은 밤이 아닙니다! 이 사람을 죽이면 붙잡힐 것 입니다!", 80);
				}
				/*
				 * if (getJob(damager).equalsIgnoreCase("경찰")) { //경관봉 삭제됨 if
				 * (getHeldMainItemName(damager).equalsIgnoreCase(ChatColor.AQUA + "경관봉")) {
				 * takeItem(damager, damager.getInventory().getItemInMainHand(), 1);
				 * player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 10));
				 * player.sendMessage(ChatColor.RED + "경관봉에 맞았습니다!!!");
				 * damager.sendMessage(ChatColor.RED + "경관봉을 사용 하였습니다."); } }
				 */
				if ((murderTeam.contains(player.getName()) && murderTeam.contains(damager.getName()))) { // 살인자 팀킬 알림
					//player.sendMessage(ms + "동료인 " + damager.getName() + "님이 당신을 공격했습니다! ");
					//damager.sendMessage(ms + player.getName() + " 님은 동료입니다! 조심해주세요!");
					player.sendMessage(ChatColor.RED + "동료인 " + damager.getName() + "님이 당신을 공격했습니다! ");
					damager.sendMessage(ChatColor.RED + player.getName() + " 님은 동료입니다! 조심해주세요!");
					e.setDamage(0.1);
				}

				double damage = e.getDamage();
				if(guardTarget != null) {
					if(guardTarget.equalsIgnoreCase(player.getName()) && dayTime == 2) {
						damage -= 2; 
						if(damage < 1) damage = 1; 
						ActionBarAPI.sendActionBar(player, ChatColor.RED + "경호원의 도움으로 받는 데미지가 2 감소했습니다.", 80);
					}		
				}
				if ((player.getHealth() - damage <= 0) && (!player.isDead())) { // 피해자가 죽을 정도의 데미지를 받았다면
					if (getJob(damager).equalsIgnoreCase("미치광") && dayTime != 2) {
						e.setCancelled(true); // 피해자 사망 방지
						gameQuitPlayer(damager, false, true);
						damager.sendMessage(ms + "당신은 밤이 아닐때 살인을 저질러 붙잡혔습니다..");
						sendMessage(ms + "미치광 " + damager.getName() + " 님께서 대낮에 살인을 시도하여 붙잡혔습니다.");
						damager.setHealth(0);
					} else if (civilTeam.contains(player.getName())) { // 피해자 시민팀이고
						if (civilTeam.contains(damager.getName())) {
							 // 공격자 시민팀이면 공격자를 죽임
							if(getJob(damager.getName()).equalsIgnoreCase("계약자") && contractorTarget != null) { //죽인 사람이 계약자고 타겟이 정해져있다면
								if (contractorTarget.equalsIgnoreCase(player.getName())) {// 죽은 사람이 타겟이라면
									// 죽이기 가능함
									if (getJob(player).equalsIgnoreCase("군인") && !soldier_use) { // 피해자가 의사의 가호를 못받았는데
										// 군인이면 살음
											e.setCancelled(true);
											soldierReverse(player);
									}
								} else { //타겟아니면 못죽임
									e.setCancelled(true); // 피해자 사망 방지
									damager.sendMessage(ms + "당신은 무고한 플레이어를 죽여버려 붙잡혔습니다.");
									sendMessage(ms + damager.getName() + " 님께서 무고한 플레이어를 살해할뻔하여 붙잡혔습니다.");
									if(damagerFtmP != null) {
										if(!peopleKnowDeny)damagerFtmP.innocent_kill += 1;
									}
									damager.setHealth(0);
								}
							}else {
								e.setCancelled(true); // 피해자 사망 방지
								damager.sendMessage(ms + "당신은 무고한 플레이어를 죽여버려 붙잡혔습니다.");
								sendMessage(ms + damager.getName() + " 님께서 무고한 플레이어를 살해할뻔하여 붙잡혔습니다.");
								if(damagerFtmP != null) damagerFtmP.innocent_kill += 1;
								damager.setHealth(0);
							}														
						} else { // 공격자 살마팀이면
							if (doctorTarget != null && player.getName().equalsIgnoreCase(doctorTarget)) { // 피해자가 의사의
																											// 가호를 받으면
																										// 살음
								boolean suc = false;
								for(String tName : ingamePlayer) {
									if(getJob(tName).equalsIgnoreCase("의사")) { //의사가 존재한다면
										if(dayTime == 2) { //밤에만
											e.setCancelled(true);
											doctorReverse(tName, player);	
											
											suc = true;
										}
										break;
									}
								}
								
								if(!suc) {
									if (getJob(player).equalsIgnoreCase("군인") && !soldier_use) { // 피해자가 의사의 가호를 못받았는데
										// 군인이면 살음
										e.setCancelled(true);
										soldierReverse(player);
									}
								}

							} else if (getJob(player).equalsIgnoreCase("군인") && !soldier_use) { // 피해자가 의사의 가호를 못받았는데
																								// 군인이면 살음
								e.setCancelled(true);
								soldierReverse(player);
							} else { //부활x면
								if(damagerFtmP != null) {
									damagerFtmP.murder_kill += 1;
									if(getJob(damager).equalsIgnoreCase("미치광")) {
										damagerFtmP.crazy_kill += 1;	
									}
								}
							}
						} 
					} else { //피해자 살마팀이면
						if (doctorTarget != null && player.getName().equalsIgnoreCase(doctorTarget)) {
							for(String tName : ingamePlayer) {
								if(getJob(tName).equalsIgnoreCase("의사")) { //의사가 존재한다면
									if(dayTime == 2) { //밤에만
										e.setCancelled(true);
										doctorReverse(tName, player);	
									}
									break;
								}
							}
						}					
						if(damagerFtmP != null) damagerFtmP.civil_kill += 1; 
						if(getJob(damager).equalsIgnoreCase("농부")) {
							damagerFtmP.farmer_kill += 1;
						}
					}
				}
			}
		}

		@EventHandler
		public void onPlayerDeath(PlayerDeathEvent e) {
			if (((e.getEntity() instanceof Player))) {
				Player player = e.getEntity();
				Player killer = player.getKiller();
				if(ingamePlayer.contains(player.getName())) {
					if(ingame) {
						e.getDrops().clear();
						e.setDroppedExp(0);
						if(killer != null) {
							if(ingamePlayer.contains(killer.getName())) {
								/*if(dayCnt != 2 && getJob(player.getKiller().getName()).equalsIgnoreCase("미치광")) {
									killer.sendMessage(ms + "낮에 살인을 저질러 체포 되었습니다.");
									killer.setHealth(0);
								}*/
							}
						}
						gameQuitPlayer(player, true, true);
						Location pL = MyUtility.getIntLocation(player);
						CorpseData c = CorpseAPI.spawnCorpse(player, pL);
						List<String> nears = new ArrayList<String>(4); //주변인 담을 고ㅗㅇ간
						HashMap<String, Double> locMap = new HashMap<String, Double>(ingamePlayer.size()); //플레이어별 시체와의 거리
						for(String tName : ingamePlayer) {
							Player t = Bukkit.getPlayer(tName);
							if(existPlayer(t)) {
								locMap.put(tName, pL.distance(t.getLocation())); //다 넣어
							}
						}
						List<String> tmpList = MyUtility.sortByValue(locMap, true); //낮은값으로 나열
						for(int i = 0; i < (tmpList.size() < 4 ? tmpList.size() : 4); i++) {
							nears.add(tmpList.get(i));
						}
						MyUtility.mixList(nears);
						myCorpse corpse = new myCorpse(player.getName(), getJob(player), killer != null ? getHeldMainItemName(killer) : "특정되지 않음", dayCnt, c, nears);
						corpseList.put(pL, corpse);	
						for(String pName : ingamePlayer) {							
							Player t = Bukkit.getPlayer(pName);
							if(existPlayer(t)) {
								if(getJob(t).equalsIgnoreCase("경찰")) {
									t.sendMessage(ms+"사건 현장 좌표 - §eX: "+pL.getBlockX()+", Y: "+pL.getBlockY()+", Z: "+pL.getBlockZ()+"\n"
											+ ms+"사건 현장과의 거리: §e"+(int)t.getLocation().distance(pL)+"m");
								}
								
							}
						}
					} else {
						gameQuitPlayer(player, true, false);
					}
				}
			}
		}

		@EventHandler
		public void onPlayerQuit(PlayerQuitEvent e) {
			Player p = e.getPlayer();
			if (!ingamePlayer.contains(p.getName())) return;
			gameQuitPlayer(p, true, false);
		}

		@EventHandler
		public void onMouseClick(PlayerInteractEvent e) {
			Player p = e.getPlayer();
			Action action = e.getAction();
			if (!ingamePlayer.contains(p.getName())) return;
			if(action == Action.LEFT_CLICK_BLOCK) {
				if(getJob(p).equalsIgnoreCase("열쇠공") && p.isSneaking()) {
					Location l = e.getClickedBlock().getLocation();
					if (l.getBlock().getType() == Material.WOODEN_DOOR) {
						if(keySmith_left >= 1) {
							keySmith_left -= 1;
							if (doorLockList.contains(l)) {
								doorLockList.remove(l.clone().add(0, -1, 0));
								doorLockList.remove(l);
								doorLockList.remove(l.clone().add(0, 1, 0));
								p.sendMessage(ms + "문을 열었습니다. "+keySmith_left+" 개 남았습니다.");
								p.getWorld().playSound(p.getLocation(), Sound.BLOCK_IRON_DOOR_OPEN, 1.0f, 1.0f);
							} else {
								doorLockList.add(l.clone().add(0, -1, 0));
								doorLockList.add(l);
								doorLockList.add(l.clone().add(0, 1, 0));
								p.sendMessage(ms + "문을 잠갔습니다."+keySmith_left+" 개 남았습니다.");
								p.getWorld().playSound(p.getLocation(), Sound.BLOCK_IRON_DOOR_CLOSE, 1.0f, 1.0f);
							}
							FtmPlayer ftmP = ftmPlayerList.get(p.getName());
							if (ftmP != null) ftmP.keySmith_Use += 1;
						} else {
							p.sendMessage(ms + "만능열쇠를 모두 사용했습니다.");
						}
					}	
				}
			}
			if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
				String job = getJob(p);
				if (job != null && (job.equalsIgnoreCase("탐정") || job.equalsIgnoreCase("배신자"))) {
					List<Location> keyList = new ArrayList<Location>(corpseList.size());
					keyList.addAll(corpseList.keySet());
					for(Location l : keyList) {
						if(l.distance(p.getLocation()) < 1) {
							p.openInventory(job.equalsIgnoreCase("탐정") ? detectorCorpse : spyCorpse);
							break;
						}
					}
				} 
				if (e.getClickedBlock() != null) {
					if(e.getClickedBlock().getType() == Material.CHEST) {
						e.setCancelled(true);
						if(ingame) {
							final Location bl = e.getClickedBlock().getLocation();
							if (!chestList.contains(bl)) {
								chestList.add(bl);
								giveChestitem(p);
								Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
									public void run() {
										chestList.remove(bl);
									}
								}, 800L);
							} else {
								switch (MyUtility.getRandom(0, 2)) {
									case 0: p.sendMessage(ms+"§7이미 누군가 털어간 흔적이 있다..."); break;
									case 1: p.sendMessage(ms+"§7최근에 상자를 연 흔적이 있다 나중에 다시 열어보자"); break;
									case 2: p.sendMessage(ms+"§7이 상자는 방금 털린 것 같다."); break;
								}
							}	
						}	
					}else if(e.getClickedBlock().getType() == Material.WOODEN_DOOR){
						Location l = e.getClickedBlock().getLocation();
						for(Location tl : doorLockList) {
							if(MyUtility.compareLocation(tl, l)) {
								e.setCancelled(true);
								p.sendMessage(ms+"이 문은 잠겨 있습니다.");							
							}
						}
					}
							
				}
				if (e.getItem() == null || e.getItem().getType() == Material.AIR) { //아이템을 안들고 우클릭했을때 리턴
					return;
				} else if (getHeldMainItemName(p).equalsIgnoreCase("§f[ §6게임 도우미 §f]")) {
					p.openInventory(gameHelper);
					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 2.0f);
				} else if (getHeldMainItemName(p).equalsIgnoreCase("§f[ §c게임 메뉴 §f]")) {
					p.openInventory(ftmHelper);
					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 2.0f);
				} else if (e.getItem().getType() == Material.IRON_HOE) {
					if (!takeItem(p, Material.MELON_SEEDS, 1)) {
						p.sendMessage("§c9mm탄환이 부족합니다.");
						return;
					}
					Snowball snowball = p.launchProjectile(Snowball.class); // here we launch the snowball
					Block target = p.getTargetBlock(null, 50);
					Vector velocity = (target.getLocation().toVector().subtract(snowball.getLocation().toVector())
							.normalize()).multiply(6);
					snowball.setVelocity(velocity);
					snowball.setShooter(p);
					p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 4.0f, 0.75f);
					p.getWorld().playEffect(e.getPlayer().getLocation(), Effect.SMOKE, 20); // We will play a smoke effect
																							// at the shooter's location
				} else if (e.getItem().getType() == Material.IRON_SWORD) {
					if (getJob(p).equalsIgnoreCase("발명가")) {
						if (takeItem(p, Material.IRON_SWORD, 3)) {
							p.sendMessage(ms + "장미칼을 얻었다!(발명가의 숨겨진 능력)");
							p.getInventory().addItem(rose_sword);
							p.playSound(p.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1.5f, 1.5f);
						}
					}
				} else if (e.getItem().getType() == Material.WATCH) {
					if(!p.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
						p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 300, 1));
						//p.removePotionEffect(PotionEffectType.);
						removeItem(p, Material.WATCH, 1);
						p.sendMessage(ms + "투명 시계를 사용 했습니다. 15초간 모습을 감춥니다.");
					}else {
						p.sendMessage(ms + "이미 투명 상태입니다.");
					}				
				} else if (e.getItem().getType() == Material.DIAMOND) {
					p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 1));
					removeItem(p, Material.DIAMOND, 1);
					p.sendMessage(ms + "저항의 돌을 사용 했습니다. 5초간 저항2를 받게됩니다.");
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
		public void onPlayerChat(PlayerChatEvent e){
			Player p = (Player) e.getPlayer();
			if(ingamePlayer.contains(p.getName())) {
				e.setCancelled(true);
				if(!waiting){
					server.egCM.sendDistanceMsgToStringList(ingamePlayer, p, "§f[ §e"+rankMap.get(p.getName())+ " §f] §7"+p.getName()+" >> " +e.getMessage(), chatDistance, 6 ,false);
				}
			
			}
		}
		
		@EventHandler
		public void onItemDrop(PlayerDropItemEvent e) {
			Player p = e.getPlayer();
			if(!ingamePlayer.contains(p.getName())) return;
			Item dropItem = e.getItemDrop();
			ItemStack item = dropItem.getItemStack();
			if(item.getType() == Material.BOOK || item.getType() == Material.GOLD_SWORD) {
				p.sendMessage(ms+"이 아이템은 버리실 수 없습니다.");
				e.setCancelled(true);
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
		
		/*@EventHandler
		public void onToggleSneak(PlayerToggleSneakEvent e) {
			Player p = e.getPlayer();
			if(ingamePlayer.contains(p.getName())) {
				if(ingame) {
					if(dayTime == 2) {
						e.setCancelled(true);
						ActionBarAPI.sendActionBar(p, ChatColor.RED+ "§l밤에는 이름표가 감춰집니다.", 80);
					}
				}
			}
		}*/
		
		/*@EventHandler
		public void onPlayerMove(PlayerMoveEvent e){
			Player p = (Player) e.getPlayer();
			if(ingamePlayer.contains(p.getName())) {
				///위치이동 아니면 캔슬
				if(e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockY() == e.getTo().getBlockY()
						&& e.getFrom().getBlockZ() == e.getTo().getBlockZ()) return;
				
				if(p.isSprinting()) {
					p.getWorld().playSound(p.getLocation(), Sound.BLOCK_CLOTH_STEP, 0.05f, 1.5f);
				} else if(!p.isSneaking()) {
					p.getWorld().playSound(p.getLocation(), Sound.BLOCK_CLOTH_STEP, 0.01f, 1.5f);
				}
			}
		}*/
	}
	
	///////////////////// 살찾에 참가한 플레이어들 클래스
	public class FtmPlayer {
		public String job;
		public String playerName;
		public boolean ignore = false;
		
		//데이터 저장용
		public int death; //사망수
		public int innocent_kill; //무고한 플레이어를 죽인수
		public int murder_kill; //살마로 시민팀 살인한 수
		public int civil_kill; //시민팀으로 살인마팀 킬한 수
		public int doctor_revive; //의사로 살린수
		public int police_success; //경찰로 살인마팀 찾은수
		public int spy_contact; //스파이로 접선한 수
		public int soldier_revive; //군인으로 부활한 수
		public int crazy_kill; //미치광이로 사람을 죽인 수
		public int reporter_report; //기자로 밝힌 수
		public int reporter_reportSuccess; //기자로 살인마팀 밝힌 수
		public int priest_effort; //성직자의 표로 영향력을 발휘한 수
		public int priest_noVoted; //성직자로 포박 저지수
		public int magician_take; //마술사로 능력을 뺏은수
		public int magician_takeMurderTeam; //마술사로 살인마팀 능력을 뺏은수
		public int creator_addictionItem; //발명가로 추가 아이템을 얻은 수
		public int farmer_kill; //농부로 킬한수
		public int contractor_success; //계약자로 의뢰를 달성한 수
		public int shaman_success; //영매사로 능력을 얻은 수
		public int shaman_successMurderTeam; //영매사로 살인마팀 능력을 얻은 수
		public int negotiator_success; //협상가로 포섭한 수
		public int negotiator_fail; //협상가로 포섭 실패한 수
		public int keySmith_Use; //열쇠공이 열쇠를 사용한  수
		public int beVotedPlayer; //포박된 수
		
		public FtmPlayer(Player p, String job) {
			this.job = job;
			this.playerName = p.getName();
		}

	}
	
	private class myCorpse {
		private String corpseName;
		private String corpseJob;
		private String killItem;
		private CorpseData corpse;
		private int killDay;
		private List<String> nearList = new ArrayList<String>(4);

		public myCorpse(String corpseName, String corpseJob, String killItem, int killDay, CorpseData corpse, List<String> nears) {
			this.corpseName = corpseName;
			this.corpseJob = corpseJob;
			this.killItem = killItem;
			this.killDay = killDay;
			this.corpse = corpse;
			this.nearList = nears;
		}
		
		public String getDataToMsg() {
			return "§7시체정보\n이름: "+corpseName+
					"\n직업: "+corpseJob+
					"\n흉기: "+killItem+
					"§7\n살해된 날짜: "+killDay+
					"§7\n가까이 있던 플레이어 4명: "
					+ "\n§7"+(nearList == null ? "" : nearList.toString());
		}
		
		public void deleteCorpse() {
			CorpseAPI.removeCorpse(corpse);
		}

	}
}
