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

	// �̺�Ʈ��
	public EventHandlerFTM event;

	//////////// public
	// ���� ���۽� ���� ����
	public Location loc_vote;
	public Location loc_corpse;
	public List<Location> loc_startList = new ArrayList<Location>(20);

	///////////// private
	// ���� �÷��̾� ����Ʈ
	private HashMap<String, FtmPlayer> ftmPlayerList = new HashMap<String, FtmPlayer>();
	private HashMap<String, String> virtualJobMap = new HashMap<String, String>(); //
	private HashMap<String, String> voteMap = new HashMap<String, String>(); // ���� �������� ��ǥ�ߴ���
	private HashMap<String, Integer> voteCnt = new HashMap<String, Integer>(); // ��ǥ �󸶳� �޾Ҵ���
	private HashMap<Location, myCorpse> corpseList = new HashMap<Location, myCorpse>();
	private List<String> murderTeam = new ArrayList<String>(3);
	private List<String> civilTeam = new ArrayList<String>(8);
	private List<ItemStack> chestItem = new ArrayList<ItemStack>(30);
	private List<Location> chestList = new ArrayList<Location>(40);
	private List<Location> doorLockList = new ArrayList<Location>(3);
	private int dayCnt;
	private int dayTime; // 0=��ħ, 1=��, 2=��
	private boolean voteTime = false;
	private boolean isMurderWin;
	private List<String> murderTeam_backUp = new ArrayList<String>(10);
	private List<String> civilTeam_backUp = new ArrayList<String>(10);
	
	//////////// �ɷ� ����
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

	//////// ���� ����
	public String ms;
	public String murderMS;
	public int murderPenalty = 0;
	public int chatDistance = 20;
	private ItemStack rose_sword;
	private ItemStack spy_axe;
	private ItemStack crazy_pickAxe;
	private ItemStack key;
	private EGScheduler updateSch = new EGScheduler(this);
	
	////// ���� �κ��丮
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
	
	//////// ���̵��
	private Sidebar jobSidebar;
	private List<SidebarString> textList = new ArrayList<SidebarString>(5);
	private Team scoreBoardTeam;
	
	public FindTheMurder(EGServer server, String gameName, String displayGameName, String cmdMain) {

		//////////////////// �ʼ� ������
		super(server);
		ms = "��7[ ��e! ��7] ��f: ��c�����ڸ� ã�ƶ� ��f>> "; // �⺻ �޼���
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
		doneSetting = loadGameData(); // ���� ���� ��ġ ��� �ε�
		/////////////////////// �ڵ� ����(�����۵��)
		dirSetting(gameName);
		/////////////////// ���� ���� �κ��丮
		ftmHelper = Bukkit.createInventory(null, 9, ""+ChatColor.BLACK+ChatColor.BOLD+"�����ڸ� ã�ƶ�");

		ItemStack item = new ItemStack(Material.BOOK, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("��7- ��e���� �ɷ�");
		item.setItemMeta(meta);
		ftmHelper.setItem(1, item);
		meta.setDisplayName("��7- ��a�÷��̾� ��Ȳ");
		item.setItemMeta(meta);
		ftmHelper.setItem(3, item);
		meta.setDisplayName("��7- ��c��ǥ�ϱ�");
		item.setItemMeta(meta);
		ftmHelper.setItem(5, item);
		meta.setDisplayName("��7- ��b���� �����");
		item.setItemMeta(meta);
		ftmHelper.setItem(7, item);
		////////////////
		
		/////////////////// ���� �����
		gameHelper = Bukkit.createInventory(null, 27, "��0��l"+inventoryGameName+" �����");

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
		meta.setDisplayName("��7- ��c����");
		List<String> loreList = new ArrayList<String>();
		loreList.add("��7- ���� ������, �¸�����,");
		loreList.add("��7   ���� ��ϵ��� Ȯ���մϴ�.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		gameHelper.setItem(11, item);
		item = new ItemStack(Material.BOOK_AND_QUILL, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7- ��c���Ӹ�ɾ�");
		loreList = new ArrayList<String>();
		loreList.add("��7- �� ���ӿ��� ����Ͻ� �� �ִ�");
		loreList.add("��7  ��ɾ Ȯ���մϴ�.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		gameHelper.setItem(13, item);
		item = new ItemStack(Material.BOOKSHELF, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7- ��c���ӱ�Ģ");
		loreList = new ArrayList<String>();
		loreList.add("��7- ������ �÷����ϸ�");
		loreList.add("��7  ���Ѿ��ϴ� ��Ģ�� Ȯ���մϴ�.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		gameHelper.setItem(15, item);
		////////////////
		
		/////////////////// ���� ����� -> ���� ����
		gameHelperAb = Bukkit.createInventory(null, 27, "��0��l"+inventoryGameName+" ���Ӽ���");

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
		meta.setDisplayName("��7- ��c���Ӽ���");
		loreList = new ArrayList<String>();
		loreList.add("��7- ���� �����İ� �¸������� Ȯ���մϴ�.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		gameHelperAb.setItem(11, item);
		item = new ItemStack(Material.ENCHANTED_BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7- ��c�������");
		loreList = new ArrayList<String>();
		loreList.add("��7- �����ڸ� ã�ƶ���");
		loreList.add("��7  ���� ����� Ȯ���մϴ�.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		gameHelperAb.setItem(15, item);
		////////////////
		
		playerInven = Bukkit.createInventory(null, 36, ""+ChatColor.BLACK+ChatColor.BOLD+"������Ȳ");
		playerInven_ab = Bukkit.createInventory(null, 36, ""+ChatColor.BLACK+ChatColor.BOLD+"�ɷ»��");
		
		/////////////////// ���� ����� -> �������
		abListInven = Bukkit.createInventory(null, 18, "��0��l"+inventoryGameName+" �������");

		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7- ��c������");
		loreList.clear();
		loreList.add("��7- Ŭ���� �ش� ������ ������ Ȯ���մϴ�.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		abListInven.setItem(0, item);
		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7- ��c����");
		loreList.clear();
		loreList.add("��7- Ŭ���� �ش� ������ ������ Ȯ���մϴ�.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		abListInven.setItem(1, item);
		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7- ��c�ǻ�");
		loreList.clear();
		loreList.add("��7- Ŭ���� �ش� ������ ������ Ȯ���մϴ�.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		abListInven.setItem(2, item);
		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7- ��c�����");
		loreList.clear();
		loreList.add("��7- Ŭ���� �ش� ������ ������ Ȯ���մϴ�.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		abListInven.setItem(3, item);
		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7- ��c����");
		loreList.clear();
		loreList.add("��7- Ŭ���� �ش� ������ ������ Ȯ���մϴ�.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		abListInven.setItem(4, item);
		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7- ��c��ġ��");
		loreList.clear();
		loreList.add("��7- Ŭ���� �ش� ������ ������ Ȯ���մϴ�.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		abListInven.setItem(5, item);
		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7- ��c����");
		loreList.clear();
		loreList.add("��7- Ŭ���� �ش� ������ ������ Ȯ���մϴ�.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		abListInven.setItem(6, item);
		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7- ��c������");
		loreList.clear();
		loreList.add("��7- Ŭ���� �ش� ������ ������ Ȯ���մϴ�.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		abListInven.setItem(7, item);
		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7- ��c������");
		loreList.clear();
		loreList.add("��7- Ŭ���� �ش� ������ ������ Ȯ���մϴ�.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		abListInven.setItem(8, item);
		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7- ��c�߸�");
		loreList.clear();
		loreList.add("��7- Ŭ���� �ش� ������ ������ Ȯ���մϴ�.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		abListInven.setItem(9, item);
		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7- ��c��ȣ��");
		loreList.clear();
		loreList.add("��7- Ŭ���� �ش� ������ ������ Ȯ���մϴ�.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		abListInven.setItem(10, item);
		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7- ��cŽ��");
		loreList.clear();
		loreList.add("��7- Ŭ���� �ش� ������ ������ Ȯ���մϴ�.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		abListInven.setItem(11, item);
		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7- ��c���");
		loreList.clear();
		loreList.add("��7- Ŭ���� �ش� ������ ������ Ȯ���մϴ�.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		abListInven.setItem(12, item);
		
		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7- ��c�ù�");
		loreList.clear();
		loreList.add("��7- Ŭ���� �ش� ������ ������ Ȯ���մϴ�.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		abListInven.setItem(13, item);
		
		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7- ��c�����");
		loreList.clear();
		loreList.add("��7- Ŭ���� �ش� ������ ������ Ȯ���մϴ�.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		abListInven.setItem(14, item);
		
		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7- ��c���Ż�");
		loreList.clear();
		loreList.add("��7- Ŭ���� �ش� ������ ������ Ȯ���մϴ�.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		abListInven.setItem(15, item);
		
		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7- ��c����");
		loreList.clear();
		loreList.add("��7- Ŭ���� �ش� ������ ������ Ȯ���մϴ�.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		abListInven.setItem(16, item);
		
		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7- ��c�����");
		loreList.clear();
		loreList.add("��7- Ŭ���� �ش� ������ ������ Ȯ���մϴ�.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		abListInven.setItem(17, item);
		

		////////////////
		
		/////////////////// �ɷ� �����
		abilityInven = Bukkit.createInventory(null, 9, ""+ChatColor.BLACK+ChatColor.BOLD+"�ɷ�");

		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��b���� Ȯ��");
		item.setItemMeta(meta);
		abilityInven.setItem(2, item);

		item = new ItemStack(Material.ENCHANTED_BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��c�ɷ� ���");
		item.setItemMeta(meta);
		abilityInven.setItem(6, item);
		////////////////
		
		////// Ž�� ��ü  ����
		detectorCorpse = Bukkit.createInventory(null, 9, ""+ChatColor.BLACK+ChatColor.BOLD+"��ü ����");
		
		item = new ItemStack(Material.WOOL, 1, (byte)11);
		meta = item.getItemMeta();
		meta.setDisplayName("��b��ü �����ϱ�");
		item.setItemMeta(meta);
		detectorCorpse.setItem(2, item);

		item = new ItemStack(Material.WOOL, 1, (byte)14);
		meta = item.getItemMeta();
		meta.setDisplayName("��c���");
		item.setItemMeta(meta);
		detectorCorpse.setItem(6, item);
		
		////����� ��ü ����
		spyCorpse = Bukkit.createInventory(null, 9, ""+ChatColor.BLACK+ChatColor.BOLD+"��ü ����");
		
		item = new ItemStack(Material.WOOL, 1, (byte)11);
		meta = item.getItemMeta();
		meta.setDisplayName("��b��ü �����ϱ�");
		item.setItemMeta(meta);
		spyCorpse.setItem(2, item);

		item = new ItemStack(Material.WOOL, 1, (byte)14);
		meta = item.getItemMeta();
		meta.setDisplayName("��c���");
		item.setItemMeta(meta);
		spyCorpse.setItem(6, item);
		
		///������ ��ǥ �ź�
		murderAbInven = Bukkit.createInventory(null, 9, ""+ChatColor.BLACK+ChatColor.BOLD+"��ǥ �ź� Ȯ��");
		
		item = new ItemStack(Material.WOOL, 1, (byte)11);
		meta = item.getItemMeta();
		meta.setDisplayName("��b��ǥ ���� �ź�");
		item.setItemMeta(meta);
		murderAbInven.setItem(2, item);

		item = new ItemStack(Material.WOOL, 1, (byte)14);
		meta = item.getItemMeta();
		meta.setDisplayName("��c���");
		item.setItemMeta(meta);
		murderAbInven.setItem(6, item);

		chestItem.add(makeItem(Material.BONE, "��������", 3, 5)); // 0
		chestItem.add(makeItem(Material.STRING, "ä��", 4, 4)); // 1
		chestItem.add(makeItem(Material.QUARTZ, "��������", 2, 3)); // 2
		chestItem.add(makeItem(Material.IRON_SWORD, "��Į", 6, 7)); // 3
		chestItem.add(makeItem(Material.SHEARS, "����", 5, 5)); // 4
		chestItem.add(makeItem(Material.BLAZE_ROD, "�ݼӹ�Ʈ", 4, 5)); // 5
		chestItem.add(new ItemStack(Material.BOW, 1)); // 6
		chestItem.add(makeItem(Material.STONE_SPADE, "������", 2, 2)); // 7
		chestItem.add(new ItemStack(Material.ARROW, 2)); // 8
		chestItem.add(makeGun(Material.IRON_HOE, "M9 ����", 7, 7)); // 9
		chestItem.add(makeItem(Material.MELON_SEEDS, "9mmźȯ", 1, 1)); // 10
		chestItem.add(new ItemStack(Material.POTION, 1, (short) 16389)); // 11
		chestItem.add(new ItemStack(Material.POTION, 1, (short) 16421)); // 12
		chestItem.add(new ItemStack(Material.POTION, 1, (short) 16386)); // 13
		chestItem.add(new ItemStack(Material.POTION, 1, (short) 16388)); // 14
		chestItem.add(new ItemStack(Material.POTION, 1, (short) 16385)); // 15
		chestItem.add(makeItem(Material.WATCH, "���� �ð�", 1, 1)); // 16
		chestItem.add(makeItem(Material.GOLD_AXE, "����", 5, 6)); // 17
		chestItem.add(makeItem(Material.IRON_SPADE, "��", 5, 6)); // 18
		chestItem.add(makeItem(Material.MELON_SEEDS, "9mmźȯ", 1, 1)); // 19
		chestItem.add(new ItemStack(Material.LEATHER_HELMET, 1)); // 20
		chestItem.add(new ItemStack(Material.LEATHER_CHESTPLATE, 1)); // 21
		chestItem.add(makeItem(Material.DIAMOND, "������ ��", 1, 1)); // 22

		murderKnife = makeItem(Material.GOLD_SWORD, "Ȳ�� ��Į", 9, 9); // 0
		rose_sword = makeItem(Material.DIAMOND_SWORD, "���Į", 7, 8);
		spy_axe = makeItem(Material.DIAMOND_AXE, "����� ����", 7, 7);
		key = makeItem(Material.TRIPWIRE_HOOK, "���� ����", 1, 1);
		crazy_pickAxe = makeItem(Material.IRON_PICKAXE, "��ġ�� ���", 6, 6);
		//////////////////////

		jobSidebar = new Sidebar("��c������Ȳ", server, 600, new SidebarString(""));
		
		scoreBoardTeam = jobSidebar.getTheScoreboard().registerNewTeam("noNameTag");
		//team.setOption(Option.NAME_TAG_VISIBILITY, OptionStatus.NEVER);
		scoreBoardTeam.setNameTagVisibility(NameTagVisibility.HIDE_FOR_OTHER_TEAMS);
		scoreBoardTeam.setOption(Option.COLLISION_RULE, OptionStatus.NEVER);
		scoreBoardTeam.setCanSeeFriendlyInvisibles(false);
		
		//////////////////////////
		event = new EventHandlerFTM(server, this);
		// �� �÷����ο� �̺�Ʈ ����
		server.getServer().getPluginManager().registerEvents(event, server);
	}

	/////////////// Minigame���� override �ؾ��ϴ� �κ�
	@Override
	public void gameHelpMsg(Player p) {

	}

	@Override
	public void startGame() {
		if (ingamePlayer.size() < minPlayer) {
			sendMessage(ms + "���� ���࿡ �ʿ��� �ּ��ο�(" + minPlayer + ")�� �������� ���߽��ϴ�.\n" + ms + "������ �����մϴ�.");
			endGame(false);
			return;
			// forceEnd();
		}
		server.egCM.broadCast(server.ms_alert + ChatColor.RED+ChatColor
				.BOLD+ disPlayGameName + ChatColor.GRAY + "�� ���� �Ǿ����ϴ�!");
		initGame();
		CorpseData c = CorpseAPI.spawnCorpse(Bukkit.getPlayer(ingamePlayer.get(MyUtility.getRandom(0, ingamePlayer.size()-1))), loc_corpse);
		myCorpse corpse = new myCorpse("Limes_", "���� 19-17��", "�Կ�������", 0, c, null);
		Location l = loc_corpse.clone();
		l.setY(l.getY()-1);
		corpseList.put(l, corpse);
		ingame = true;
		/////////////// ������
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
				// Ǯ�Ƿ� ����
				MyUtility.healUp(p);
				MyUtility.allClear(p);
				MyUtility.attackDelay(p, false);
				p.setFallDistance(0);     
				TitleAPI.sendFullTitle(p, 10, 80, 30, ChatColor.RED+"���� ����", ChatColor.RED+""+disPlayGameName);
			}
		}
		
		divideTeleport(loc_vote);
		
		waiting = true;
		setRole();
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				sendTitle(ChatColor.RED+"��l��!", "��e��l�κ��丮 ���� å�� ��Ŭ���Ͽ� ���� �����ϼ���!", 80);
				sendMessage(ms+"��� ������ ���� ������ å�� ��Ŭ�� -> ������� ���� Ȯ�ΰ����մϴ�.");
				sendSound(Sound.BLOCK_NOTE_BELL, 1.0f, 2.0f);
			}
		}, 80l);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				sendTitle(ChatColor.RED+"���� ������...", "", 80);
				sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
			}
		}, 160l);
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				for(String s : ingamePlayer) {
					Player p = Bukkit.getPlayer(s);
					if(p!=null) {
						if(getJob(p).equalsIgnoreCase("������")) {
							p.sendMessage(ms+"����� �����ϴ� ������ �־����ϴ�.");
						} else if(getJob(p).equalsIgnoreCase("�����")) {
							p.sendMessage(ms + "����� ��ҿ� ģ�ϰ� ������ ������ ���ÿ� � ����� ������ �޾ҽ��ϴ�.");
						} else if(getJob(p).equalsIgnoreCase("��ġ��")) {
							p.sendMessage(ms+"����� ��� ��ȸ�� �Ҹ��� ǰ�� �ִ� ����̾����ϴ�.");
						}  else if(getJob(p).equalsIgnoreCase("����")) {
							p.sendMessage(ms + "����� �׸� ģ���� �ʴ� ������ ���ÿ� � ����� ������ �޾ҽ��ϴ�.");
						}  else if(getJob(p).equalsIgnoreCase("�����")) {
							p.sendMessage(ms + "����� û�� ���ξ����Դϴ�.");
						} else {
							p.sendMessage(ms + "����� ��ҿ� ģ�ϰ� ������ ������ ���ÿ� � ����� ������ �޾ҽ��ϴ�.");
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
						if(getJob(p).equalsIgnoreCase("������")) {
							p.sendMessage(ms+"����� �� �������� �ڽ��� ���ÿ� ������ ������ �޾ҽ��ϴ�.");
						} else if(getJob(p).equalsIgnoreCase("�����")) {
							p.sendMessage(ms + "����� ������ �������� ���Ͽ���");
						} else if(getJob(p).equalsIgnoreCase("��ġ��")) {
							p.sendMessage(ms+"���������� �ſ� ����� ó���־� �Ϸ��Ϸ縦 ����� ��ư����ϴ�.");
						}  else if(getJob(p).equalsIgnoreCase("����")) {
							p.sendMessage(ms + "����� ������ �������� ���Ͽ���.");
						}  else if(getJob(p).equalsIgnoreCase("�����")) {
							p.sendMessage(ms + "���� û�� �Ƿڸ� ���� ����� ���ϰ� ����� �����Ͽ����ϴ�.");
						} else {
							p.sendMessage(ms + "����� ������ �������� ���Ͽ���");
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
						if(getJob(p).equalsIgnoreCase("������")) {
							p.sendMessage(ms+"������ ���ÿ��� ���� �̿ܿ� �ƹ��� �������ϴ�.");
						} else if(getJob(p).equalsIgnoreCase("�����")) {
							p.sendMessage(ms + "���ÿ� �����Ͽ��� �� ������ ����� ������ �ʾҽ��ϴ�.");
						} else if(getJob(p).equalsIgnoreCase("��ġ��")) {
							p.sendMessage(ms+"�׷��� ����� ������ �˴� ���ο��� �ڽ��� ���ÿ� ������ �ʴ븦 �޾ҽ��ϴ�.");
						}  else if(getJob(p).equalsIgnoreCase("����")) {
							p.sendMessage(ms + "���ÿ� �����Ͽ���  �� ������ ����� ������ �ʾҽ��ϴ�.");
						}  else if(getJob(p).equalsIgnoreCase("�����")) {
							p.sendMessage(ms + "�׷��� �� ����� ����� �������� ���Եȴٴ� ���� �˾ҽ��ϴ�.");
						} else {
							p.sendMessage(ms + "���ÿ� �����Ͽ��� �� ������ ����� ������ �ʾҽ��ϴ�.");
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
						if(getJob(p).equalsIgnoreCase("������")) {
							p.sendMessage(ms+"����� ��ҿ� �� ���ο��� ������ ǰ���־��� �浿������ ������ �����Ͽ����ϴ�.");
						} else if(getJob(p).equalsIgnoreCase("�����")) {
							p.sendMessage(ms + "��� �̿ܿ��� �ʴ븦 ���� ���� ������� �־�����");
						} else if(getJob(p).equalsIgnoreCase("��ġ��")) {
							p.sendMessage(ms+"���ο� ���� �������� ��򰡿��� ���� ������ ���� ��Ҹ��� ����Խ��ϴ�.");
						}  else if(getJob(p).equalsIgnoreCase("����")) {
							p.sendMessage(ms + "������ ã���� ������ �ѷ����� ��");
						}  else if(getJob(p).equalsIgnoreCase("�����")) {
							p.sendMessage(ms + "��ӿ��� ó���� �ϴ� ���� ������ ���̶� ������ �����");
						} else {
							p.sendMessage(ms + "��� �̿ܿ��� �ʴ븦 ���� ���� ������� �־�����");
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
						if(getJob(p).equalsIgnoreCase("������")) {
							p.sendMessage(ms+"����� �ſ� ��Ȳ�ϸ� �� ���ÿ��� ����ġ���� �Ͽ�����");
						} else if(getJob(p).equalsIgnoreCase("�����")) {
							p.sendMessage(ms + "���ÿ� �����Ͽ����� ������ ����� ������ �ʾҽ��ϴ�.");
						} else if(getJob(p).equalsIgnoreCase("��ġ��")) {
							p.sendMessage(ms+"��Ҹ��� �ٿ����� ���� ����� �������� ������ �����ϴ� ����� ����ϰ� �ƽ��ϴ�.");
						}  else if(getJob(p).equalsIgnoreCase("����")) {
							p.sendMessage(ms + "����� ������ ��ü ���� ���ִ� �������� �������ϴ�.");
						}  else if(getJob(p).equalsIgnoreCase("�����")) {
							p.sendMessage(ms + "�ʴ���� ô�ϸ� ����� ���� �������� ���߽��ϴ�.");
						} else {
							p.sendMessage(ms + "�� ������ �Բ� ������ �����Ͽ����ϴ�.");
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
						if(getJob(p).equalsIgnoreCase("������")) {
							p.sendMessage(ms+"â������ ���ο��� �ʴ븦 �������� �ٸ� ������� �����ϴ� ���� �������ϴ�.");
						} else if(getJob(p).equalsIgnoreCase("�����")) {
							p.sendMessage(ms + "����� �������ϸ� ���ÿ� �ִ� ���� ���ǵ��� ���� Ž�̳��� �����߽��ϴ�.");
						} else if(getJob(p).equalsIgnoreCase("��ġ��")) {
							p.sendMessage(ms+"������ ����� ���� ������ �η����̳� ����� �ƴ�");
						} else if(getJob(p).equalsIgnoreCase("����")) {
							p.sendMessage(ms + "����� ������ ��ü�� ���� ��ü ���� ���ִ� ����� ���ظ� �������ٰ� �����߽��ϴ�.");
						} else if(getJob(p).equalsIgnoreCase("�����")) {
							p.sendMessage(ms + "������ �̹� ���ÿ����� ���λ���� �Ͼ�־����ϴ�.");
						} else {
							p.sendMessage(ms + "�׸��� �������� ��ȸ�ǿ��� ������ ��ü�� �߰��Ͽ����ϴ�.");
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
						if(getJob(p).equalsIgnoreCase("������")) {
							p.sendMessage(ms+"����� ������ ��濡 �����־����� �ʴ븦 ���� �ٸ� ������� ������ �����ϴ� ���� ����");
						} else if(getJob(p).equalsIgnoreCase("�����")) {
							p.sendMessage(ms + "�׸��� �������� �������� ��ȸ�ǿ��� ������ ��ü�� �߰��Ͽ����ϴ�.");
						} else if(getJob(p).equalsIgnoreCase("��ġ��")) {
							p.sendMessage(ms+"���ÿ� �ִ� ���ǵ��� ���İ� �� �ְڴٴ� ����̾����ϴ�.");
						}  else if(getJob(p).equalsIgnoreCase("����")) {
							p.sendMessage(ms + "�� ��� ���� ����� �߰��ϰ� ������ �Խ��ϴ�.");
						}  else if(getJob(p).equalsIgnoreCase("�����")) {
							p.sendMessage(ms + "�ٸ� �̰��� ��ſ��� ū ��ȸ�Դϴ�.");
						} else {
							p.sendMessage(ms + "������ �и��� �����������ص� ����־����ϴ�.");
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
						if(getJob(p).equalsIgnoreCase("������")) {
							p.sendMessage(ms+"��� ������ ��ó�� ����� �巯�������ϴ�.");
						} else if(getJob(p).equalsIgnoreCase("�����")) {
							p.sendMessage(ms + "������ ��ü�� �� ����� ������ ������ ��ġ�� ���� ������ ���� �������� �ֽ��ϴ�.");
						} else if(getJob(p).equalsIgnoreCase("��ġ��")) {
							p.sendMessage(ms+"����� ���������� �ڷ��ϰ� ���ÿ��� ��κ��̴� ���ǵ��� ��ġ���Ͽ�����");
						}  else if(getJob(p).equalsIgnoreCase("����")) {
							p.sendMessage(ms + "�״� ��ſ��� �� ����� ���°��� �����ָ� ��ʸ� �ϰڴٸ� �����߽��ϴ�.");
						}  else if(getJob(p).equalsIgnoreCase("�����")) {
							p.sendMessage(ms + "����� ��� ����� ������ �� �����ڿ� �����ϸ�");
						} else {
							p.sendMessage(ms + "���� �� �ڸ��� �ִ� �������� ������ ������ ���� �и��մϴ�.");
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
						if(getJob(p).equalsIgnoreCase("������")) {
							p.sendMessage(ms+"������� ���� ����� ������ ������ �����̶�� ���� ������ ��Ű�� ���� �ð������ε� �մϴ�.");
						} else if(getJob(p).equalsIgnoreCase("�����")) {
							p.sendMessage(ms + "����� ������ �Դ´ٸ� �����ڿ��� �����Ͽ� ������� ��ġ�� ������ ���İ� �� �ֽ��ϴ�.");
						} else if(getJob(p).equalsIgnoreCase("��ġ��")) {
							p.sendMessage(ms+"�ڽ� �̿ܿ��� �ʴ븦 �������� ������� ���ÿ� �����ع��Ƚ��ϴ�.");
						}  else if(getJob(p).equalsIgnoreCase("����")) {
							p.sendMessage(ms + "���ص� ���ΰ� �׸� ģ���� �ʴ� ����� ������ �����߽��ϴ�.");
						}  else if(getJob(p).equalsIgnoreCase("�����")) {
							p.sendMessage(ms + "�� ������ ����� ���� ���� ������ ���Դϴ�.");
						} else {
							p.sendMessage(ms + "����� ������ ������ �����ڸ� ��Ƴ����մϴ�.");
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
						if(getJob(p).equalsIgnoreCase("������")) {
							p.sendMessage(ms+"����� ������ �������ٴ� ���� ��Ű���ʱ� ���Ͽ� ����� ��Ƴ����ϴ� ������� ��� �����Ͻʽÿ�!");
						} else if(getJob(p).equalsIgnoreCase("�����")) {
							p.sendMessage(ms + "����� ��� �Ͻðڽ��ϱ�?");
						} else if(getJob(p).equalsIgnoreCase("��ġ��")) {
							p.sendMessage(ms+"����� �����ڿ��� �����Ͽ� �ٸ� ������� ��� �����ϰ� ���ÿ� �ִ� ���ǵ��� ���� �������ϰ� �ֽ��ϴ�.");
						}  else if(getJob(p).equalsIgnoreCase("����")) {
							p.sendMessage(ms + "�����ڿ� �����Ͽ� ������ ����� �̱� ������ ���߸��ʽÿ�.");
						}  else if(getJob(p).equalsIgnoreCase("�����")) {
							p.sendMessage(ms + "��, ���� �Ƿڸ� �޼��غ����?");
						} else {
							p.sendMessage(ms + "����� ������ �̿��ϰ� ������ �����Ͽ� �����ڸ� ã�Ƴ�����!");
						}
					}
				}
				sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
			}
		}, 470l);
		///////////////// ��¥ ����
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
				sendTitle(ChatColor.GREEN+"������ ���� ����", "��e��lå�� ��Ŭ���Ͽ� Ȯ���ϼ���.", 80);
			}
		}, 600l);
			
	}

	//////////////////
	
	public void setRankMap(String pName) {
		PlayerData data = server.egDM.getPlayerData(pName);
		String rank = "����";
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
		
		//�ɷ�
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
		
		//ȯ��
		murderPenalty = 0;
		dayCnt = 0;
		dayTime = 2;
		
		//������
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
			server.egPM.printLog(ms + "[" + disPlayGameName + "] ���� ���� ������ �������� �ʾҽ��ϴ�.");
			ret = false;
		}
		if (loc_Join == null) {
			server.egPM.printLog(ms + "[" + disPlayGameName + "] ���� ���� ��� ������ �������� �ʾҽ��ϴ�.");
			ret = false;
		}
		if (loc_vote == null) {
			server.egPM.printLog(ms + "[" + disPlayGameName + "] ��ǥ ������ �������� �ʾҽ��ϴ�.");
			ret = false;
		}else {
			loc_corpse = loc_vote.clone().add(2,0,2);
		}
		
		if(ret == true) {
			server.egPM.printLog("[" + disPlayGameName + "] ���� �̻� ����");
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
						loreList.add("��7����: "+virtualJobMap.get(pName));
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
	
	/////////// �κ��丮 Ŭ�� ////////////////
	
	public void gameHelper(Player p, int slot) {
		switch (slot) {
		case 11:
			p.closeInventory();
			p.openInventory(gameHelperAb);
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
			return;
			
		case 13:
			p.sendMessage("\n��f/��ê or tc - ��c�������� �޼����� �����ϴ�. ��7( ���������� ���� )");
			sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
			p.closeInventory();
			return;
			
		case 15:
			p.sendMessage("\n��f���� ���� ��� �Ǵ� ������ �ڿ��� ���ӿ� �����Ͻ� �� �����ϴ�.");
			p.sendMessage("��fȮ���⸦ �̿��� ���� ���� ���� ������ �Ұ����մϴ�.");
			p.sendMessage("��f������ ���� ���Ƿ� ���� ������ ���Ͻ� ��� ������ �� �ֽ��ϴ�.");
			p.sendMessage("��c�� ������ ���� ���θ� ������ �������� �� �� �ֽ��ϴ�.");
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
			else p.sendMessage(ms+"��ǥ �ð��� �ƴմϴ�.");
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
			p.sendMessage("\n��7���� �̸� ��f: ��c�����ڸ� ã�ƶ�");
			p.sendMessage("��f������ ������ Ž���Ͽ� �¸������� �޼��ϴ�");
			p.sendMessage("��f�߸��� �ɸ������Դϴ�.");
			p.sendMessage("��f������ ��������, �ù��� �� ���� ������ ����Ǹ�");
			p.sendMessage("��f��� ������ �ִ� �÷��̾ ���� ó���ϸ� �¸��մϴ�.");
			p.sendMessage("��f�ٸ� �ù����� ���ΰ� ������ �Ʊ����� �⺻�����δ�");
			p.sendMessage("��f���� ���ϸ� �ù����� ���� �÷��̾ ���� �ù����� �Ʊ���");
			p.sendMessage("��f���Ͻ� ���� �÷��̾� ��� ���� �÷��̾ ����ϰ� �˴ϴ�.");
			p.sendMessage("��c�� �� ������ �ִ� ���ڸ� ��Ŭ���� �������� ������ �� �ֽ��ϴ�.");
			p.sendMessage("��c�� ���ΰ��� ä���� �ǹ� ������ 20ĭ�� �÷��̾�Ը� ���޵˴ϴ�.");
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
			if(job.equalsIgnoreCase("������")) {
				if(murderDenied) p.sendMessage(ms+"�̹� �ɷ��� ����Ͻ� �����Դϴ�.");
				else {
					p.openInventory(murderAbInven);
					//p.sendMessage(ms+"������ ��ǥ�� �������� �ʰڽ��ϱ�?");
					ActionBarAPI.sendActionBar(p, ChatColor.RED+ "������ ��ǥ�� �������� �ʰڽ��ϱ�?", 80);
				}
			} else if(job.equalsIgnoreCase("����")) {
				p.openInventory(playerInven_ab); 
				//p.sendMessage(ms+"������ �÷��̾ �������ּ���.");
				ActionBarAPI.sendActionBar(p, ChatColor.RED + "������ �÷��̾ �������ּ���.", 80);
			} else if(job.equalsIgnoreCase("�ǻ�")) {
				p.openInventory(playerInven_ab);
				//p.sendMessage(ms+"������ �غ��� �÷��̾ �������ּ���.");
				ActionBarAPI.sendActionBar(p, ChatColor.RED + "������ �غ��� �÷��̾ �������ּ���.", 80);
			} else if(job.equalsIgnoreCase("�����")) {
				if(civilTeam.contains(p.getName())) {
					p.openInventory(playerInven_ab);
					//p.sendMessage(ms+"������ �˾Ƴ� �÷��̾ �������ּ���.");
					ActionBarAPI.sendActionBar(p, ChatColor.RED + "������ ��Ž�� �÷��̾ �������ּ���.", 80);
				}
				else p.sendMessage(ms+"������ �Ϸ��� ���¿����� �ɷ��� ��ü���ŷ� �ٲ�ϴ�.\n"+ms+"��ü������ ��Ŭ���Ͽ� ���Ű� �����մϴ�.");
			} else if(job.equalsIgnoreCase("����")) {
				p.sendMessage(ms+"����� �ɷ��� Ư�� ����� �����ϴ� �ɷ��� �ƴմϴ�.");
			} else if(job.equalsIgnoreCase("��ġ��")) {
				p.sendMessage(ms+"����� �ɷ��� Ư�� ����� �����ϴ� �ɷ��� �ƴմϴ�.");
			} else if(job.equalsIgnoreCase("����")) {
				p.openInventory(playerInven_ab);
				//p.sendMessage(ms+"��縦 �ۼ��� �÷��̾ �������ּ���.");
				ActionBarAPI.sendActionBar(p, ChatColor.RED + "��縦 �ۼ��� �÷��̾ �������ּ���.", 80);		
			} else if(job.equalsIgnoreCase("������")) {
				p.sendMessage(ms+"����� �ɷ��� Ư�� ����� �����ϴ� �ɷ��� �ƴմϴ�.");
			} else if(job.equalsIgnoreCase("������")) {
				p.openInventory(playerInven_ab);
				//p.sendMessage(ms+"������ ������ �÷��̾ �������ּ���.");
				ActionBarAPI.sendActionBar(p, ChatColor.RED + "������ ������ �÷��̾ �������ּ���.", 80);		
			} else if(job.equalsIgnoreCase("�߸�")) {
				p.sendMessage(ms+"����� �ɷ��� Ư�� ����� �����ϴ� �ɷ��� �ƴմϴ�.");
			} else if(job.equalsIgnoreCase("��ȣ��")) {
				p.openInventory(playerInven_ab);
				//p.sendMessage(ms+"��ȣ�� �÷��̾ �������ּ���.");
				ActionBarAPI.sendActionBar(p, ChatColor.RED + "��ȣ�� �÷��̾ �������ּ���.", 80);
			} else if(job.equalsIgnoreCase("Ž��")) {
				p.sendMessage(ms+"����� �ɷ��� Ư�� ����� �����ϴ� �ɷ��� �ƴմϴ�.");
			} else if(job.equalsIgnoreCase("���")) {
				p.sendMessage(ms+"����� �ɷ��� Ư�� ����� �����ϴ� �ɷ��� �ƴմϴ�.");
			} else if(job.equalsIgnoreCase("�ù�")) {
				p.sendMessage(ms+"����� Ư���� �ɷ��� �����ϴ�.");
			}else if(job.equalsIgnoreCase("�����")) {
				p.openInventory(playerInven_ab);
				ActionBarAPI.sendActionBar(p, ChatColor.RED + "���� ����� �������ּ���.", 80);		
			}else if(job.equalsIgnoreCase("���Ż�")) {
				p.openInventory(playerInven_ab);
				ActionBarAPI.sendActionBar(p, ChatColor.RED + "��ȥ�� ������ ����� �������ּ���.", 80);		
			}else if(job.equalsIgnoreCase("����")) {
				p.openInventory(playerInven_ab);
				ActionBarAPI.sendActionBar(p, ChatColor.RED + "���� ����� �������ּ���.", 80);		
			}else if(job.equalsIgnoreCase("�����")) {
				p.sendMessage(ms+"����� �ɷ��� Ư�� ����� �����ϴ� �ɷ��� �ƴմϴ�.");
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
		if(job.equalsIgnoreCase("����")) {
			if(dayTime == 0) {
				if(policeTarget == null) {
					if(target.equalsIgnoreCase(p.getName())) {
						p.sendMessage(ms+"�ڽ��� ������ �� �����ϴ�.");
					} else {
						policeTarget = target;
						TitleAPI.sendFullTitle(p, 10, 40, 30, ChatColor.GRAY+"���� ����", ChatColor.RESET+target);
						p.sendMessage(ms+target+"���� �����մϴ�. �������� �㿡 �˰Ե˴ϴ�.");
					}
				} else {
					p.sendMessage(ms+"�̹� "+policeTarget+"���� �������Դϴ�.");
				}
			} else {
				p.sendMessage(ms+"��ħ���� �����մϴ�.");
			}
		} else if(job.equalsIgnoreCase("�ǻ�")) {
			if(dayTime == 0) {
				if(doctorTarget == null) {
					if(target.equalsIgnoreCase(p.getName())) {
						p.sendMessage(ms+"�ڽ��� ������ �� �����ϴ�.");
					} else {
						doctorTarget = target;
						p.sendMessage(ms+target+"���� ������ �غ��մϴ�.");
						TitleAPI.sendFullTitle(p, 10, 40, 30, ChatColor.GRAY+"���� �غ�", ChatColor.RESET+target);
					}
				} else {
					p.sendMessage(ms+"�̹� "+doctorTarget+"���� ������ �غ����Դϴ�.");
				}
			} else {
				p.sendMessage(ms+"��ħ���� �����մϴ�.");
			}
		} else if(job.equalsIgnoreCase("�����")) {
			if(dayTime == 2) {
				if(target.equalsIgnoreCase(p.getName())) {
					p.sendMessage(ms+"�ڽ��� ������ �� �����ϴ�.");
				} else {
					if(!spy_use) {
						spy_use = true;
						String tJob = getJob(target);
						p.sendMessage(ms+target+"���� ������ "+tJob+"�Դϴ�.");
						if(tJob.equalsIgnoreCase("����")) {
							Bukkit.getPlayer(target).sendMessage(ms+"����� "+p.getName()+" ���� ����� �����Ͽ����ϴ�.");
						}
						if(tJob.equalsIgnoreCase("������") || tJob.equalsIgnoreCase("����")) {
							p.sendMessage(ms+"������ �����Ͽ����ϴ�. \"��c/tc <�޼���>\" ��f��ɾ�� ������������ ��ȭ�� �����մϴ�.");
							sendTeamChat(murderTeam, ms+"����� "+p.getName()+"���� �����Ͽ����ϴ�. ���� �׵� ����� �����Դϴ�.");
							TitleAPI.sendFullTitle(p, 10, 40, 30, ChatColor.GRAY+"���� ����", ChatColor.RESET+target);
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
						p.sendMessage(ms+"�̹� �ɷ��� ����߽��ϴ�.");
					}			
				}
			} else {
				p.sendMessage(ms+"�㿡�� �����մϴ�.");
			}
		} else if(job.equalsIgnoreCase("����")) {
			if(dayTime == 2) {
				if(!reporter_use) {
					if(reportTarget == null) {
						reportTarget = target;
						reporter_use = true;
						p.sendMessage(ms+target+" �Կ� ���� ��縦 �ۼ��մϴ�. ��ǥ�� ������ ��ħ�Դϴ�.");
					} else {
						p.sendMessage(ms+"�̹� "+target+" �Կ� ���� ��縦 �ۼ����Դϴ�.");
						TitleAPI.sendFullTitle(p, 10, 40, 30, ChatColor.GRAY+"��� �ۼ�", ChatColor.RESET+target);
					}
				} else {
					p.sendMessage(ms+"�̹� "+reportTarget+" �Կ� ���� ��縦 �ۼ��Ͽ����ϴ�.");
				}
			} else {
				p.sendMessage(ms+"�㿡�� �����մϴ�.");
			}
		} else if(job.equalsIgnoreCase("������")) {
			if(dayCnt >= 2) {
				if(dayTime == 2) {
					if(target.equalsIgnoreCase(p.getName())) {
						p.sendMessage(ms+"�ڽ��� ������ �� �����ϴ�.");
					} else {
						String tJob = getJob(target);
						if(!tJob.equalsIgnoreCase("��ġ��")) {
							Player t = Bukkit.getPlayer(target);
							p.sendMessage(ms+target+" ���� ������ ���Ѿҽ��ϴ�.");
							FtmPlayer ftmP = ftmPlayerList.get(p.getName());
							if(ftmP != null) ftmP.magician_take += 1;
							if(!tJob.equalsIgnoreCase("������")) {
								boolean isMurderTeam = murderTeam.contains(t.getName());
								t.playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1.5f, 1.5f);
								p.playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1.5f, 1.5f);
								setFullJob(t, 13);
								t.sendMessage(ms+"�����簡 ����� ������ ���Ѿ� �����ϴ�. ����� ������ �ù����� ����˴ϴ�.");
								setFullJob(p, getJobCode(tJob));
								if(murderTeam.contains(p.getName())) murderTeam.remove(p.getName());
								if(tJob.equalsIgnoreCase("�����") && isMurderTeam) {
									p.sendMessage(ms+target+"���� ������ �Ϸ��� ����ڿ��⿡ �������°� �˴ϴ�.\n"+ms+"\"/tc <�޼���> \"��ɾ�� ������������ ��ȭ�� �����մϴ�.");
									sendTeamChat(murderTeam, ms+"�����翴�� ����� "+p.getName()+"���� �����Ͽ����ϴ�. ���� �׵� ����� �����Դϴ�.");
									sendTeamSound(murderTeam, Sound.BLOCK_NOTE_HARP);
									murderTeam.remove(target);
									if(!murderTeam.contains(p.getName()))murderTeam.add(p.getName());
									civilTeam.remove(p.getName());
									if(police_invensted.contains(p.getName())) {
										for(String tmpName : ingamePlayer) {
											if(getJob(tmpName).equalsIgnoreCase("����")) {
												Player tmp = Bukkit.getPlayer(tmpName);
												if(existPlayer(tmp)) {
													tmp.sendMessage(ms+"��f����� �����ߴ� ��c"+target+" ���� �������� ���ᰡ �� �� �����ϴ�.");
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
								} else if(tJob.equalsIgnoreCase("�����") && isMurderTeam) {
									p.sendMessage(ms+target+"���� �Ƿڸ� �Ϸ��� ����ڿ��⿡ ���θ��� ���ᰡ �˴ϴ�.\n"+ms+"\"/tc <�޼���> \"��ɾ�� ������������ ��ȭ�� �����մϴ�.");
									sendTeamChat(murderTeam, ms+"�����翴�� ����� "+p.getName()+"���� ���Ӱ� ���ᰡ �ƽ��ϴ�. ���� �׵� ����� �����Դϴ�.");
									sendTeamSound(murderTeam, Sound.BLOCK_NOTE_HARP);
									murderTeam.remove(target);
									if(!murderTeam.contains(p.getName()))murderTeam.add(p.getName());
									civilTeam.remove(p.getName());
									if(police_invensted.contains(p.getName())) {
										for(String tmpName : ingamePlayer) {
											if(getJob(tmpName).equalsIgnoreCase("����")) {
												Player tmp = Bukkit.getPlayer(tmpName);
												if(existPlayer(tmp)) {
													tmp.sendMessage(ms+"��f����� �����ߴ� ��c"+target+" ���� �������� ���ᰡ �� �� �����ϴ�.");
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
								} else if(tJob.equalsIgnoreCase("����") && isMurderTeam) {
									murderTeam.remove(target);
									if(!murderTeam.contains(p.getName()))murderTeam.add(p.getName());
									civilTeam.remove(p.getName());
									if(police_invensted.contains(p.getName())) {
										for(String tmpName : ingamePlayer) {
											if(getJob(tmpName).equalsIgnoreCase("����")) {
												Player tmp = Bukkit.getPlayer(tmpName);
												if(existPlayer(tmp)) {
													tmp.sendMessage(ms+"��f����� �����ߴ� ��c"+target+" ���� �������� ���ᰡ �� �� �����ϴ�.");
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
									sendMessage(ms+"������ "+target+"���� �������� �ɷ¿� ���� ������ ���Ѱ� ����Ͽ����ϴ�.\n"+ms+"�������� ��ü�� �ٽ� �̱ü����� �����ϴ�.");
									peopleKnowDeny = false;
								} else {
									sendMessage(ms+"������ "+target+"���� �������� �ɷ¿� ���� ������ ���Ѱ� ����Ͽ����ϴ�.\n"+ms+"���� �����簡 �����ڰ� �Ǿ����ϴ�.");
								}
								if(murderDenied) murderDenied = false;
								setFullJob(t, 13);
								t.sendMessage(ms+"�����簡 ����� ������ ���Ѿ� �����ϴ�. ����� ������ �ù����� ����˴ϴ�.\n"+ms+"�����簡 ����� ó���Ͽ����ϴ�.");
								t.setHealth(0);
								sendTeamChat(murderTeam, ms+"�����翴�� "+p.getName()+"���� �������� ������ ���Ѿҽ��ϴ�. ���� �׵� ����� �����Դϴ�.");
								sendTeamSound(murderTeam, Sound.BLOCK_NOTE_HARP);
								setFullJob(p, 0);
								if(ftmP != null) ftmP.magician_takeMurderTeam += 1;
								civilTeam.remove(p.getName());
								if(police_invensted.contains(p.getName())) {
									for(String tmpName : ingamePlayer) {
										if(getJob(tmpName).equalsIgnoreCase("����")) {
											Player tmp = Bukkit.getPlayer(tmpName);
											if(existPlayer(tmp)) {
												tmp.sendMessage(ms+"��f����� �����ߴ� ��c"+target+" ���� �������� ���ᰡ �� �� �����ϴ�.");
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
							p.sendMessage(ms+"��ġ���� ������ ������ �� �����ϴ�.");
						}
					}
				} else {
					p.sendMessage(ms+"�㿡�� �����մϴ�.");
				}
			} else {
				p.sendMessage(ms+"�������� �ɷ��� 2��°���� ����� �����մϴ�.");
			}
		} else if(job.equalsIgnoreCase("��ȣ��")) {
			if(dayTime == 0 || dayTime == 1) {
				if(guardTarget == null) {
					if(target.equalsIgnoreCase(p.getName())) {
						p.sendMessage(ms+"�ڽ��� ������ �� �����ϴ�.");
					} else {
						guardTarget = target;
						p.sendMessage(ms+target+" ����  ��ȣ�մϴ�.\n"+ms+"�� �÷��̾�� ���� ���� ���� ��ǥ���� ���ڵ��� ������ ����� ������ �޽��ϴ�.");
						TitleAPI.sendFullTitle(p, 10, 40, 30, ChatColor.GRAY+"��ȣ ����", ChatColor.RESET+target);
					}	
				} else {
					p.sendMessage(ms+"�̹� "+target+" �Կ� ��ȣ���Դϴ�.");
				}
			} else {
				p.sendMessage(ms+"��ħ �Ǵ� ������ �����մϴ�.");
			}
		} else if(job.equalsIgnoreCase("�����")) {
			if (contractorTarget == null) {
				if (dayTime == 0) {
					if (target.equalsIgnoreCase(p.getName())) {
						p.sendMessage(ms + "�ڽ��� ������ �� �����ϴ�.");
					} else {
						if(getJob(target).equalsIgnoreCase("��ġ��")) {
							p.sendMessage(ms + "��ġ���� ������ �� �����ϴ�.");
						}else {
							contractorTarget = target;
							p.sendMessage(ms + target + " ���� ���� ���� ����Դϴ�.\n" + ms + "�� �÷��̾ ����� ����� ������ ���̵˴ϴ�.\n" + ms
									+ "����� �� �÷��̾ ���̴� �͵� �����մϴ�.");
							TitleAPI.sendFullTitle(p, 10, 40, 30, ChatColor.GRAY + "�Ƿ� ����", ChatColor.RESET + target);
						}				
					}
				} else {
					p.sendMessage(ms + "��ħ���� �����մϴ�.");
				}
			} else {
				p.sendMessage(ms + "�̹� " + contractorTarget + " ���� ���� ����Դϴ�.");
			}
		} else if(job.equalsIgnoreCase("����")) {
			if (negotiatorTarget == null) {
				if (dayTime == 2) {
					if(dayCnt >= 2) {
						if (target.equalsIgnoreCase(p.getName())) {
							p.sendMessage(ms + "�ڽ��� ������ �� �����ϴ�.");
						} else {
							if(murderTeam.contains(target)) {
								p.sendMessage(ms + "�״� �̹� �����Դϴ�.");
							} else {
								negotiatorTarget = target;
								String tJob = getJob(target);
								sendMessage(ms+"���󰡰� ���������� ������ �õ��߽��ϴ�.");
								if (tJob.equalsIgnoreCase("����") || tJob.equalsIgnoreCase("���") || tJob.equalsIgnoreCase("����")) {
									p.sendMessage(ms + target + " ���� ��b" + tJob + "��f�̾����ϴ�!.\n" + ms + "����� ���󰡶�� ���� ���׽��ϴ�!");
									TitleAPI.sendFullTitle(p, 10, 40, 30, ChatColor.GRAY + "���� ����", ChatColor.RESET + target);
									FtmPlayer ftmP = ftmPlayerList.get(p.getName());
									if(ftmP != null) ftmP.negotiator_fail += 1;
								} else {
									if(!murderTeam.contains(target)) murderTeam.add(target);
									p.sendMessage(ms + "��b" + tJob + "�� " + target + " ���� �����߽��ϴ�.\n");
									TitleAPI.sendFullTitle(p, 10, 40, 30, ChatColor.GRAY + "���� ����", ChatColor.RESET + target);
									sendTeamChat(murderTeam, ms + "���� " + p.getName() + " ���� " + tJob + "�� " + target
											+ " ���� �����Ͽ����ϴ�. ���� �׵� ����� �����Դϴ�.");
									sendTeamSound(murderTeam, Sound.BLOCK_NOTE_HARP);
									civilTeam.remove(target);
									if(police_invensted.contains(target)) {
										for(String tName : ingamePlayer) {
											if(getJob(tName).equalsIgnoreCase("����")) {
												Player t = Bukkit.getPlayer(tName);
												if(existPlayer(t)) {
													t.sendMessage(ms+"��f����� �����ߴ� ��c"+target+" ���� �������� ���ᰡ �� �� �����ϴ�.");
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
						p.sendMessage(ms + "2��° ����� �����մϴ�.");
					}
				} else {
					p.sendMessage(ms + "2��° ����� �����մϴ�.");
				}
			} else {
				p.sendMessage(ms + "�̹� " + negotiatorTarget + " ���� ���� �õ��߽��ϴ�.");
			}
		} else if(job.equalsIgnoreCase("���Ż�")) {
			if(dayTime == 0) {
				if(soul_target == null) {
					if(target.equalsIgnoreCase(p.getName())) {
						p.sendMessage(ms+"�ڽ��� ������ �� �����ϴ�.");
					} else {
						if(getJob(target).equalsIgnoreCase("��ġ��")) {
							p.sendMessage(ms + "��ġ���� ������ �� �����ϴ�.");
						}else {
							soul_target = target;
							p.sendMessage(ms+target+" ���� ��ȥ�� �����մϴ�.\n"+ms+"�� ����� ����� �ش� ������ ����ϴ�.");
							TitleAPI.sendFullTitle(p, 10, 40, 30, ChatColor.GRAY+"��ȥ ���� ����", ChatColor.RESET+target);
						}					
					}	
				} else {
					p.sendMessage(ms+"�̹� "+soul_target+" ���� ��ȥ�� �������Դϴ�.");
				}
			} else {
				p.sendMessage(ms+"��ħ���� �����մϴ�.");
			}
		} 
	}
	
	public void murderAbInvenClick(Player p, int slot) {
		p.closeInventory();
		switch (slot) {
		case 2:
			if(!murderDenied) {
				if(voting) {
					p.sendMessage(ms+"��ǥ�߿��� ����Ͻ� �� ���� �ɷ��Դϴ�.");
				} else {
					p.sendMessage(ms+"����� ��ǥ ������ �ź��߽��ϴ�. ���̵Ǹ� ��� ������� ����� �����ڶ�� ���� ��Ű�Ե˴ϴ�.");
					murderDenied = true;
				}
			} else {
				p.sendMessage(ms+"�̹� �ɷ��� ����Ͻ� �����Դϴ�.");
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
					p.sendMessage(ms+"��ü�� �����Ͽ����ϴ�.");
					corpseList.remove(l);
				}
			}
		}
	}
	
	///////////////////////////////
	
	private int getJobCode(String jobName) {
		switch(jobName) {
			case "������": return 0;
			case "����": return 1;
			case "�ǻ�": return 2;
			case "�����": return 3;
			case "����": return 4;
			case "��ġ��": return 5;
			case "����": return 6;
			case "������": return 7;
			case "������": return 8;
			case "�߸�": return 9;
			case "��ȣ��": return 10;
			case "Ž��": return 11;
			case "���": return 12;
			case "�ù�": return 13;
			case "�����": return 14; 
			case "���Ż�": return 15; //ó�� ���� ����� ������ ������
			case "����": return 16; //�㿡 ������ 1�� �츶������ ������(����,���� ����), �츶�� 11�� �̻��̸� ������ ����
			case "�����": return 17;
			
			default: return -1;
		}
	}

	////////////////// ������
	
	private void giveBasicItem() {
		ItemStack ftmHelper = new ItemStack(Material.BOOK, 1);
		ItemMeta meta = ftmHelper.getItemMeta();
		meta.setDisplayName("��f[ ��c���� �޴� ��f]");
		List<String> loreList = new ArrayList<String>(2);
		loreList.add("��7- ���� �ɷ� ���, ����, ��ǥ ���");
		loreList.add("��7���� ���࿡ �ʿ��� ����� ����մϴ�.");
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
			p.sendMessage(ms + "[" + disPlayGameName + "] " + cmdMain + " join - ���� ����");
			p.sendMessage(ms + "[" + disPlayGameName + "] " + cmdMain + " quit - ���� ����");
			p.sendMessage(ms + "[" + disPlayGameName + "] " + cmdMain + " set - ���� ����");
		} else if (cmd[1].equalsIgnoreCase("set")) {
			if (cmd.length <= 2) {
				p.sendMessage(ms + "[" + disPlayGameName + "] " + cmdMain + " set loc- ���� ���� ����");
			} else if (cmd[2].equalsIgnoreCase("loc")) {
				if (cmd.length <= 3) {
					p.sendMessage(ms + "[" + disPlayGameName + "] " + cmdMain + " set loc join - ���� ���� ��� ���� ����");
					p.sendMessage(ms + "[" + disPlayGameName + "] " + cmdMain + " set loc votepos - ���� ������ ���� ����");
					p.sendMessage(ms + "[" + disPlayGameName + "] " + cmdMain + " set loc startpos - ���� ���� ���� �߰�");
					p.sendMessage(ms + "[" + disPlayGameName + "] " + cmdMain + " set loc removestart <��ȣ> - ���� ���� ���� ����");
				} else if (cmd[3].equalsIgnoreCase("join")) {
					saveLocation(gameName, "JoinPos", p.getLocation());
					loadGameData();
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "���� ���� ��� ������ �����Ǿ����ϴ�.");
				} else if (cmd[3].equalsIgnoreCase("votepos")) {
					saveLocation(gameName, "votePos", p.getLocation());
					loadGameData();
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "��ǥ��Ұ� �����Ǿ����ϴ�.");
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
					p.sendMessage(ms + "[" + disPlayGameName + "] " + startPosCnt + " ��° ���� ���� ������ �߰��Ǿ����ϴ�.");
				} else if (cmd[3].equalsIgnoreCase("removestart")) {
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "������ �������� ��ȣ�� �Է����ּ���.");
				} else {
					p.sendMessage(ms + "[" + disPlayGameName + "] " + cmdMain + " set loc join - ���� ���� ��� ���� ����");
					p.sendMessage(ms + "[" + disPlayGameName + "] " + cmdMain + " set loc votepos - ���� ������ ���� ����");
					p.sendMessage(ms + "[" + disPlayGameName + "] " + cmdMain + " set loc startpos - ���� ���� ���� �߰�");
					p.sendMessage(ms + "[" + disPlayGameName + "] " + cmdMain + " set loc removestart <��ȣ> - ���� ���� ���� ����");
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
			p.sendMessage(ms + "[" + disPlayGameName + "] " + cmdMain + " join - ���� ����");
			p.sendMessage(ms + "[" + disPlayGameName + "] " + cmdMain + " quit - ���� ����");
			p.sendMessage(ms + "[" + disPlayGameName + "] " + cmdMain + " set - ���� ����");
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
			if(inCase == 0) { //��X
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
			}else if(inCase == 1){//��X
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
			setJob(tmpList.get(random), MyUtility.getRandom(0, 1) == 0 ? 3  : 14); //����� �Ǵ� �����
			tmpList.remove(random);
			random = MyUtility.getRandom(0, tmpList.size() - 1);
			setJob(tmpList.get(random), 4);
			tmpList.remove(random);
			jobCodeList.remove(Integer.valueOf(4));
			if (ingamePlayer.size() >= 9) { // 9���̻��̸� 1���� ��ġ��
				random = MyUtility.getRandom(0, tmpList.size() - 1);
				setJob(tmpList.get(random), 5);
				tmpList.remove(random);
			}
			if (ingamePlayer.size() >= 12) { // 12���̻��̸� 1���� ����
				random = MyUtility.getRandom(0, tmpList.size() - 1);
				setJob(tmpList.get(random), 16);
				tmpList.remove(random);
			}
			jobCodeList.remove(Integer.valueOf(5)); // ��ġ�� ����, 9�� �̻��̵� �ƴϵ� ��ġ���� �����ؾ��ϱ� ����(9�� �̸��̸� ��ġ���� �ȳ�������)
			jobCodeList.remove(Integer.valueOf(16)); // ���� ����, 12�� �̻��̵� �ƴϵ� ���󰡴� �����ؾ��ϱ� ����(12�� �̸��̸� ��ġ���� �ȳ�������)
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
			if(inCase == 0) { //��X
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
			}else if(inCase == 1){//��X
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
			setJob(tmpList.get(random), MyUtility.getRandom(0, 1) == 0 ? 3  : 14); //����� �Ǵ� �����
			tmpList.remove(random);
			random = MyUtility.getRandom(0, tmpList.size() - 1);
			setJob(tmpList.get(random), 4);
			tmpList.remove(random);
			jobCodeList.remove(Integer.valueOf(4));
			if (ingamePlayer.size() >= 8) { // 8���̻��̸� 1���� ����
				random = MyUtility.getRandom(0, tmpList.size() - 1);
				setJob(tmpList.get(random), 16);
				tmpList.remove(random);
			}
			jobCodeList.remove(Integer.valueOf(5)); // ��ġ�� ����, 9�� �̻��̵� �ƴϵ� ��ġ���� �����ؾ��ϱ� ����(9�� �̸��̸� ��ġ���� �ȳ�������)
			jobCodeList.remove(Integer.valueOf(16)); // ���� ����, 12�� �̻��̵� �ƴϵ� ���󰡴� �����ؾ��ϱ� ����(12�� �̸��̸� ��ġ���� �ȳ�������)
			while (tmpList.size() != 0 && jobCodeList.size() != 0) {
				int randomJobCode = MyUtility.getRandom(0, jobCodeList.size() - 1);
				random = MyUtility.getRandom(0, tmpList.size() - 1);
				setJob(tmpList.get(random), jobCodeList.get(Integer.valueOf(randomJobCode)));
				tmpList.remove(random);
				jobCodeList.remove(randomJobCode);
			}
		}

		//sendMessage(ms + "������ ���������ϴ�.");
		//sendMessage(ms + "���� �޴�(å)�� ��Ŭ���Ͻø� ���� ���� �� �ɷ� ����� �����մϴ�.");
		MyUtility.mixMap(virtualJobMap);
		// Main.SendMessage(ms + "��������Ʈ�� ���÷��� /ftm list �� �Է����ּ���.");
		// Mixlist();
	}
	
	public void setFullJob(Player p, int jobCode) {
		// jobCode ���, 0=������ ,1=����,2=�ǻ�,3=�����,4=����,5=��ġ��,6=����
		// 7=������,8=������,9=�߸�,10=��ȣ��,11=Ž��,12=���, 
		murderTeam.remove(p.getName());
		civilTeam.remove(p.getName());
		if (jobCode == 0) {
			if(!murderTeam.contains(p.getName())) murderTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "������"));
			virtualJobMap.put(p.getName(), "?");
			p.sendMessage(ms + "��7����� ������ " + ChatColor.GOLD + getJob(p) + "��7 �Դϴ�.");
			// ������ ���� ������
			p.getInventory().addItem(murderKnife);
			for (int i = 0; i < 3; i++) { // ���� �ð� 3�� ����
				p.getInventory().addItem(chestItem.get(16));
			}
			MyUtility.setMaxHealth(p,40);
		} else if (jobCode == 1) {
			if(!civilTeam.contains(p.getName())) civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "����"));
			virtualJobMap.put(p.getName(), "?");
			p.sendMessage(ms + "��7����� ������ " + ChatColor.GOLD + getJob(p) + "��7 �Դϴ�.");
			policeTarget = null;
			/* ���� ���� ������
			ItemStack item = new ItemStack(Material.STICK, 3);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.AQUA + "�����");
			meta.addEnchant(Enchantment.KNOCKBACK, 1, true);
			List<String> lorelist = new ArrayList();
			lorelist.add(ChatColor.GRAY + "�������� �־����� �����");
			lorelist.add(ChatColor.GRAY + "�� ����� ���� �÷��̾�� 3�ʰ�");
			lorelist.add(ChatColor.GRAY + "�������� ���Ѵ�. (������ ��밡��)");
			meta.setLore(lorelist);
			item.setItemMeta(meta);
			p.getInventory().addItem(item);*/
		} else if (jobCode == 2) {
			if(!civilTeam.contains(p.getName()))civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "�ǻ�"));
			virtualJobMap.put(p.getName(), "?");
			p.sendMessage(ms + "��7����� ������ " + ChatColor.GOLD + getJob(p) + "��7 �Դϴ�.");
			doctorTarget = null;
		} else if (jobCode == 3) {
			if(!civilTeam.contains(p.getName()))civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "�����"));
			virtualJobMap.put(p.getName(), "?");
			p.sendMessage(ms + "��7����� ������ " + ChatColor.GOLD + getJob(p) + "��7 �Դϴ�.");
		} else if (jobCode == 4) {
			if(!civilTeam.contains(p.getName())) civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "����"));
			virtualJobMap.put(p.getName(), "?");
			p.sendMessage(ms + "��7����� ������ " + ChatColor.GOLD + getJob(p) + "��7 �Դϴ�.");
			p.getInventory().addItem(chestItem.get(9));
			for(int i = 0; i < 5; i++) p.getInventory().addItem(chestItem.get(19));
			soldier_use = false;
		} else if (jobCode == 5) {
			if(!murderTeam.contains(p.getName())) murderTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "��ġ��"));
			virtualJobMap.put(p.getName(), "��ġ��");
			p.sendMessage(ms + "��7����� ������ " + ChatColor.GOLD + getJob(p) + "��7 �Դϴ�.");
			p.sendMessage(ms + "����� ������ ������Ȳ�� ��ġ���� �÷������ϴ�.");
			sendMessage(ms + "��ġ�� " + p.getName() + " �� �ֽ��ϴ�. �����ϼ���!");
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 72000, 0));
			p.getInventory().addItem(crazy_pickAxe);
			p.getInventory().addItem(chestItem.get(16));
		} else if (jobCode == 6) {
			if(!civilTeam.contains(p.getName()))civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "����"));
			virtualJobMap.put(p.getName(), "?");
			p.sendMessage(ms + "��7����� ������ " + ChatColor.GOLD + getJob(p) + "��7 �Դϴ�.");
			reporter_use = false;
		} else if (jobCode == 7) {
			if(!civilTeam.contains(p.getName()))civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "������"));
			virtualJobMap.put(p.getName(), "?");
			p.sendMessage(ms + "��7����� ������ " + ChatColor.GOLD + getJob(p) + "��7 �Դϴ�.");
		} else if (jobCode == 8) {
			if(!civilTeam.contains(p.getName()))civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "������"));
			virtualJobMap.put(p.getName(), "?");
			p.sendMessage(ms + "��7����� ������ " + ChatColor.GOLD + getJob(p) + "��7 �Դϴ�.");
		} else if (jobCode == 9) {
			if(!civilTeam.contains(p.getName()))civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "�߸�"));
			virtualJobMap.put(p.getName(), "?");
			p.sendMessage(ms + "��7����� ������ " + ChatColor.GOLD + getJob(p) + "��7 �Դϴ�.");
		} else if (jobCode == 10) {
			if(!civilTeam.contains(p.getName()))civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "��ȣ��"));
			virtualJobMap.put(p.getName(), "?");
			p.sendMessage(ms + "��7����� ������ " + ChatColor.GOLD + getJob(p) + "��7 �Դϴ�.");
			guardTarget = null;
		} else if (jobCode == 11) {
			if(!civilTeam.contains(p.getName()))civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "Ž��"));
			virtualJobMap.put(p.getName(), "?");
			p.sendMessage(ms + "��7����� ������ " + ChatColor.GOLD + getJob(p) + "��7 �Դϴ�.");
		} else if (jobCode == 12) {
			if(!civilTeam.contains(p.getName()))civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "���"));
			virtualJobMap.put(p.getName(), "���");
			p.sendMessage(ms + "��7����� ������ " + ChatColor.GOLD + getJob(p) + "��7 �Դϴ�.");
			p.getInventory().addItem(makeItem(Material.GOLD_HOE, "��ⱸ", 5, 5));
		} else if (jobCode == 13) {
			if(!civilTeam.contains(p.getName()))civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "�ù�"));
			virtualJobMap.put(p.getName(), "?");
			p.sendMessage(ms + "��7����� ������ " + ChatColor.GOLD + getJob(p) + "��7 �Դϴ�.");
		}else if (jobCode == 14) {
			if(!civilTeam.contains(p.getName()))civilTeam.add(p.getName());
			contractorTarget = null;
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "�����"));
			virtualJobMap.put(p.getName(), "?");
			p.sendMessage(ms + "��7����� ������ " + ChatColor.GOLD + getJob(p) + "��7 �Դϴ�.");
		}else if (jobCode == 15) {
			if(!civilTeam.contains(p.getName()))civilTeam.add(p.getName());
			soul_target = null;
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "���Ż�"));
			virtualJobMap.put(p.getName(), "?");
			p.sendMessage(ms + "��7����� ������ " + ChatColor.GOLD + getJob(p) + "��7 �Դϴ�.");
		}else if (jobCode == 16) {
			if(!murderTeam.contains(p.getName()))murderTeam.add(p.getName());
			negotiatorTarget = null;
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "����"));
			virtualJobMap.put(p.getName(), "?");
			p.sendMessage(ms + "��7����� ������ " + ChatColor.GOLD + getJob(p) + "��7 �Դϴ�.");
		}else if (jobCode == 17) {
			if(!civilTeam.contains(p.getName()))civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "�����"));
			virtualJobMap.put(p.getName(), "?");
			p.sendMessage(ms + "��7����� ������ " + ChatColor.GOLD + getJob(p) + "��7 �Դϴ�.");
			keySmith_left = 4;
		}
		String tJob = getJob(p);
		if(getJob(p) != null) {
			TitleAPI.sendTitle(p, 10, 80, 30, ChatColor.GRAY+getJob(p));
		}
		sendMessage(ms + "���� �޴�(å)�� ��Ŭ���Ͻø� ���� ���� �� �ɷ� ����� �����մϴ�.");
	}

	public void setJob(Player p, int jobCode) {
		// jobCode ���, 0=������ ,1=����,2=�ǻ�,3=�����,4=����,5=��ġ��,6=����
		// 7=������,8=������,9=�߸�,10=��ȣ��,11=Ž��,12=���, 
		murderTeam.remove(p.getName());
		civilTeam.remove(p.getName());
		if (jobCode == 0) {
			murderTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "������"));
			virtualJobMap.put(p.getName(), "?");
		} else if (jobCode == 1) {
			civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "����"));
			virtualJobMap.put(p.getName(), "?");
			policeTarget = null;
			/* ���� ���� ������
			ItemStack item = new ItemStack(Material.STICK, 3);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.AQUA + "�����");
			meta.addEnchant(Enchantment.KNOCKBACK, 1, true);
			List<String> lorelist = new ArrayList();
			lorelist.add(ChatColor.GRAY + "�������� �־����� �����");
			lorelist.add(ChatColor.GRAY + "�� ����� ���� �÷��̾�� 3�ʰ�");
			lorelist.add(ChatColor.GRAY + "�������� ���Ѵ�. (������ ��밡��)");
			meta.setLore(lorelist);
			item.setItemMeta(meta);
			p.getInventory().addItem(item);*/
		} else if (jobCode == 2) {
			civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "�ǻ�"));
			virtualJobMap.put(p.getName(), "?");
			doctorTarget = null;
		} else if (jobCode == 3) {
			civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "�����"));
			virtualJobMap.put(p.getName(), "?");
		} else if (jobCode == 4) {
			civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "����"));
			virtualJobMap.put(p.getName(), "?");
			soldier_use = false;
		} else if (jobCode == 5) {
			murderTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "��ġ��"));
			virtualJobMap.put(p.getName(), "��ġ��");
		} else if (jobCode == 6) {
			civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "����"));
			virtualJobMap.put(p.getName(), "?");
			reporter_use = false;
		} else if (jobCode == 7) {
			civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "������"));
			virtualJobMap.put(p.getName(), "?");
		} else if (jobCode == 8) {
			civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "������"));
			virtualJobMap.put(p.getName(), "?");
		} else if (jobCode == 9) {
			civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "�߸�"));
			virtualJobMap.put(p.getName(), "?");
		} else if (jobCode == 10) {
			civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "��ȣ��"));
			virtualJobMap.put(p.getName(), "?");
			guardTarget = null;
		} else if (jobCode == 11) {
			civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "Ž��"));
			virtualJobMap.put(p.getName(), "?");
		} else if (jobCode == 12) {
			civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "���"));
			virtualJobMap.put(p.getName(), "���");
		} else if (jobCode == 13) {
			civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "�ù�"));
			virtualJobMap.put(p.getName(), "?");
		} else if (jobCode == 14) {
			civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "�����"));
			virtualJobMap.put(p.getName(), "?");
		} else if (jobCode == 15) {
			civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "���Ż�"));
			virtualJobMap.put(p.getName(), "?");
		} else if (jobCode == 16) {
			murderTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "����"));
			virtualJobMap.put(p.getName(), "?");
		}else if (jobCode == 17) {
			civilTeam.add(p.getName());
			ftmPlayerList.put(p.getName(), new FtmPlayer(p, "�����"));
			virtualJobMap.put(p.getName(), "?");	
			keySmith_left = 4;
		}
	}
	
	public void tellJob(Player p) {
		// jobCode ���, 0=������ ,1=����,2=�ǻ�,3=�����,4=����,5=��ġ��,6=����
		// 7=������,8=������,9=�߸�,10=��ȣ��,11=Ž��,12=���, 
		int jobCode = getJobCode(getJob(p));
		if (jobCode == 0) {
			p.sendMessage(ms + "��7����� ������ " + ChatColor.GOLD + getJob(p) + "��7 �Դϴ�.");
			// ������ ���� ������
			p.getInventory().addItem(murderKnife);
			for (int i = 0; i < 3; i++) { // ���� �ð� 3�� ����
				p.getInventory().addItem(chestItem.get(16));
			}
			MyUtility.setMaxHealth(p,40);
		} else if (jobCode == 1) {
			p.sendMessage(ms + "��7����� ������ " + ChatColor.GOLD + getJob(p) + "��7 �Դϴ�.");
		} else if (jobCode == 2) {
			p.sendMessage(ms + "��7����� ������ " + ChatColor.GOLD + getJob(p) + "��7 �Դϴ�.");
		} else if (jobCode == 3) {
			p.sendMessage(ms + "��7����� ������ " + ChatColor.GOLD + getJob(p) + "��7 �Դϴ�.");
		} else if (jobCode == 4) {
			p.sendMessage(ms + "��7����� ������ " + ChatColor.GOLD + getJob(p) + "��7 �Դϴ�.");
			p.getInventory().addItem(chestItem.get(9));
			for(int i = 0; i < 5; i++) p.getInventory().addItem(chestItem.get(19));
		} else if (jobCode == 5) {
			p.sendMessage(ms + "��7����� ������ " + ChatColor.GOLD + getJob(p) + "��7 �Դϴ�.");
			p.sendMessage(ms + "����� ������ ������Ȳ�� ��ġ���� �÷������ϴ�.");
			sendMessage(ms + "��ġ�� " + p.getName() + " �� �ֽ��ϴ�. �����ϼ���!");
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 72000, 0));
			p.getInventory().addItem(crazy_pickAxe);
			p.getInventory().addItem(chestItem.get(16));
		} else if (jobCode == 6) {
			p.sendMessage(ms + "��7����� ������ " + ChatColor.GOLD + getJob(p) + "��7 �Դϴ�.");
		} else if (jobCode == 7) {
			p.sendMessage(ms + "��7����� ������ " + ChatColor.GOLD + getJob(p) + "��7 �Դϴ�.");
		} else if (jobCode == 8) {
			p.sendMessage(ms + "��7����� ������ " + ChatColor.GOLD + getJob(p) + "��7 �Դϴ�.");
		} else if (jobCode == 9) {
			p.sendMessage(ms + "��7����� ������ " + ChatColor.GOLD + getJob(p) + "��7 �Դϴ�.");
		} else if (jobCode == 10) {
			p.sendMessage(ms + "��7����� ������ " + ChatColor.GOLD + getJob(p) + "��7 �Դϴ�.");
		} else if (jobCode == 11) {
			p.sendMessage(ms + "��7����� ������ " + ChatColor.GOLD + getJob(p) + "��7 �Դϴ�.");
		} else if (jobCode == 12) {
			p.sendMessage(ms + "��7����� ������ " + ChatColor.GOLD + getJob(p) + "��7 �Դϴ�.");
			sendMessage(ms + "������ ��� " + p.getName() + " ���� �ֽ��ϴ�. �׸� �Ͼ��ּ���!");
			p.sendMessage(ms + "����� ������ ������Ȳ�� ��η� �÷������ϴ�.");
			p.getInventory().addItem(makeItem(Material.GOLD_HOE, "��ⱸ", 5, 5));
		} else if (jobCode == 13) {
			p.sendMessage(ms + "��7����� ������ " + ChatColor.GOLD + getJob(p) + "��7 �Դϴ�.");
		} else if (jobCode == 14) {
			p.sendMessage(ms + "��7����� ������ " + ChatColor.GOLD + getJob(p) + "��7 �Դϴ�.");
		} else if (jobCode == 15) {
			p.sendMessage(ms + "��7����� ������ " + ChatColor.GOLD + getJob(p) + "��7 �Դϴ�.");
		} else if (jobCode == 16) {
			p.sendMessage(ms + "��7����� ������ " + ChatColor.GOLD + getJob(p) + "��7 �Դϴ�.");
		}else if (jobCode == 17) {
			p.sendMessage(ms + "��7����� ������ " + ChatColor.GOLD + getJob(p) + "��7 �Դϴ�.");
		}
		String tJob = getJob(p);
		if(tJob != null) {
			TitleAPI.sendTitle(p, 10, 80, 30, ChatColor.GRAY+getJob(p));
		}
		p.sendMessage(ms + "���� �޴�(å)�� ��Ŭ���Ͻø� ���� ���� �� �ɷ� ����� �����մϴ�.");
	}

	public String getJob(Player p) {
		if(!ftmPlayerList.containsKey(p.getName())) return "������";
		FtmPlayer ftmP = ftmPlayerList.get(p.getName());
		return ftmP.job;
	}

	public String getJob(String pName) {
		if(!ftmPlayerList.containsKey(pName)) return "������";
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
		sendMessage(ms + dayCnt + ChatColor.RESET + "��° ���� ��ħ�� �Ǿ����ϴ�. ������ �����ϼ���.");
		sendBossbar(ChatColor.YELLOW+"��l"+dayCnt+"��° ��ħ", 55);
		sendTitle(ChatColor.GOLD+"��ħ", ChatColor.RESET+"��� �߻� "+dayCnt+"��°", 80);
		sendSound(Sound.ENTITY_CHICKEN_AMBIENT, 1.5f, 1.0f);
		for(String pName : ingamePlayer) {
			Player t = Bukkit.getPlayer(pName);
			if(existPlayer(t)) {
				if(existPlayer(t)) t.removePotionEffect(PotionEffectType.BLINDNESS);
				if(reportTarget != null) {
					if(getJob(t).equalsIgnoreCase("����")){
						String tJob = getJob(reportTarget);
						if(!tJob.equalsIgnoreCase("������")) {
							virtualJobMap.put(reportTarget, tJob);
							updatePlayerInven();
							sendTitle(ChatColor.GOLD+"Ư��!", ChatColor.RESET+reportTarget+" ���� ������ "+tJob+"�Դϴ�!", 80);
							sendMessage(ms+"Ư��! ���ڿ� ���� "+reportTarget+" ����  ������ "+tJob+"���� ���������ϴ�!");						
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
				if(!getJob(t).equalsIgnoreCase("��ġ��")) {
					t.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 72000 ,0));	
				}
				t.setSneaking(false);
			}
		}
		if(dayTime == 1) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
				public void run() {
					if(!ingame || ending) return;
					sendTitle(ChatColor.GOLD+"", "��e��l��ħ���� ����� ��ġ�� ǥ�õ˴ϴ�.", 140);
					sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
				}
			}, 400L);
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
				public void run() {
					if(!ingame || ending) return;
					sendTitle(ChatColor.GOLD+"", "��e��l�ٸ� ������ �ɷ��� �ñ��Ͻø� ����̸� ��Ŭ���ϼ���.", 140);
					sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
				}
			}, 600L);
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
				public void run() {
					if(!ingame || ending) return;
					sendTitle(ChatColor.GOLD+"", "��e��l�÷��̾ ä���� ���� ��, 20ĭ���� �����մϴ�.", 140);
					sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
				}
			}, 800L);
		}
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				if(!ingame || ending) return;
				if(peopleKnowDeny) {
					sendMessage(ms + "�� ���� �˴ϴ�. �������� ��ü�� �̹� ���������Ƿ� ��ǥ�� �������� �ʽ��ϴ�.");
				} else {
					sendMessage(ms + "�� ���� �Ǹ� ��ǥ������ �̵��˴ϴ�. ��ǥ�� �غ����ּ���!");
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
		sendTitle(ChatColor.GREEN+"��", ChatColor.RESET+"��� �߻� "+dayCnt+"��°", 80);
		sendBossbar(ChatColor.GOLD+"��l"+dayCnt+"��° ��", 60);
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
					if(getJob(p).equalsIgnoreCase("������")) {
						name = p.getName();
					}
				}
			}
			if(name != null) {
				final String tmpName = name; 
				Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
					public void run() {
						if(!ingame || ending) return;
						sendTitle(ChatColor.GOLD+tmpName,  ChatColor.RESET+reportTarget+"�� �÷��̾ �������Դϴ�!", 80);
						sendMessage(ms+tmpName+"���� ��ǥ�� �������� �����̽��ϴ�.\n"+ms+tmpName+"���� �������Դϴ�!\n"+ms+"���� ���θ��� ������ ����� ��ġ�� ǥ�õ˴ϴ�.");
						for(String pName : ingamePlayer) {
							Player p = Bukkit.getPlayer(pName);
							if(existPlayer(p)) {
								if(!getJob(p).equalsIgnoreCase("������")) 
									p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 72000 ,0));
							}
						}
						virtualJobMap.put(tmpName, "������");
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
					if(getJob(p).equalsIgnoreCase("��ġ��")) {
						p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1400, 1));
						p.sendMessage(ms + "����� ��ġ���̱� ������ ������·� ��ǥ�� �����մϴ�.");
					}
				}
			}
			sendMessage(ms + ChatColor.GRAY + dayCnt + ChatColor.RESET
					+ "��° ���� ���� �Ǿ����ϴ�. ��ǥ������ �̵��մϴ�.\n"
					+ ms+"�� ��ǥ�� ���۵˴ϴ�. ��ǥ�Ͽ� ������ ���  ���� �ǳ��ϼ���.\n"
					+ ms+"���ڵ� �÷��̾�� ������ �����ǰ� ������ ������ ���ݷ��� ������ �ǰ� ��ġ�� ���Դϴ�.");
			sendToLoc(loc_vote);
			Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
				public void run() {
					if(!ingame || ending) return;
					sendMessage(ms + "����� ��ǥ�� ���۵˴ϴ�.");
					sendTitle(ChatColor.GREEN+"���� ���¶�", "��e��l���� ����, ���ݷ� ����, �߱�ȿ��", 140);
					sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
				}
			}, 600L);
			Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
				public void run() {
					if(!ingame || ending) return;
					voteTime = true;
					sendMessage(ms + "������ �÷��̾ ��ǥ���ּ���.");
					sendSound(Sound.BLOCK_NOTE_BELL, 1.0f, 0.5f);
					for(String pName : ingamePlayer) {
						Player p = Bukkit.getPlayer(pName);
						if(existPlayer(p)) {
							if(!getJob(p).equalsIgnoreCase("��ġ��"))
								p.openInventory(playerInven);
						}
					}
				}
			}, 800L);
			Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
				public void run() {
					if(!ingame || ending) return;
					sendMessage(ms + "�� ���� �˴ϴ�. ��ǥ�� ���� ���� �÷��̾�� ���� ��ǥ���ּ���!\n"
							+ ms+"��c����� ��7-> ��c��ǥ���");
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
			sendMessage(ms + dayCnt + ChatColor.RESET + "��° ���� ���� �Ǿ����ϴ�. �������� ��ü�� ������ ���� ��ǥ�� ������ �ʽ��ϴ�.");
			for(String pName : ingamePlayer) {
				Player p = Bukkit.getPlayer(pName);
				if(existPlayer(p)) {
					if(!getJob(p).equalsIgnoreCase("������")) 
						p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 72000 ,0));
				}
			}
			Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
				public void run() {
					if(!ingame || ending) return;
					sendMessage(ms + "�� ���� �˴ϴ�.");
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
					if(getJob(p).equalsIgnoreCase("����")) {
						if(murderTeam.contains(policeTarget)) {
							p.sendMessage(ms+"�������� ���Խ��ϴ�.\n"+ms+policeTarget+" ���� �����ڰų� �������� �����Դϴ�!");
							FtmPlayer ftmP = ftmPlayerList.get(pName);
							if(ftmP != null) ftmP.police_success += 1;
						} else {
							p.sendMessage(ms+"�������� ���Խ��ϴ�.\n"+ms+policeTarget+" ���� ������ �Ǵ� �������� ���ᰡ �ƴմϴ�.");
						}
						police_invensted.add(policeTarget);
						policeTarget = null;
					}
				}
			}
		}
		sendMessage(ms + dayCnt + ChatColor.RESET + "��° ���� ���� �Ǿ����ϴ�. �����ϼ���.");
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
		sendTitle(ChatColor.RED+"��",  ChatColor.RESET+"��� �߻� "+dayCnt+"��°", 80);
		sendBossbar(ChatColor.RED+"��l"+dayCnt+"��° ��", 90);
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
				sendMessage(ms + "�� ��ħ�� �˴ϴ�.");
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
		if (jobName.equalsIgnoreCase("������")) {
			p.sendMessage(ChatColor.GOLD + "����� ���� : " + ChatColor.AQUA + getJob(p) + ChatColor.GRAY
					+ "\n����� ������� ��� �����Ϸ��� �������Դϴ�." 
					+"\n��Ÿ��� ������ ���⸦ ������ �ֽ��ϴ�."
					+ "\n��c����ڡ�f�� �ɷ��� ����Ͽ� ��Ű� ������ ��c����ڡ�f�� ���ᰡ �˴ϴ�."
					+"\n���� ��c��ġ����f�̳� ��c���󰡡�f�� �����Ѵٸ� �����Ͻʽÿ�."
					+"\n����� �ɷ��� ����Ͽ� ��c��ǥ�� ������ �źΡ�f�� �� ������ �ź��ҽ�"
					+ "\n����� �����ڶ�� ���� ��Ű�� ������ ��c��� �÷��̾��� ��ġ�� ǥ�á�f�˴ϴ�."
					+"\n����� �������˰� ������ �� �����ϱ�?");
		} else if (jobName.equalsIgnoreCase("����")) {
			p.sendMessage(ChatColor.GOLD + "����� ���� : " + ChatColor.AQUA + getJob(p) + ChatColor.GRAY
					+ "\n����� �����Դϴ�."
					+ "\n��ħ���� 1���� �����Ͽ� ��c���������� �ƴ��� �����f�� �� �ֽ��ϴ�." 
					+ "\n����� ���� �Ǿ�� ������ ����� ��ſ��Ը� �˷����ϴ�."
					+ "\n�׸��� 1���̶� ������ ����� �������� ���� �Ǿ��� ����"
					+ "\n��ſ��� �˸��� ���ϴ�."
					+ "\n���� ����� ������ �پ�ϴ�. �������� ���ص��� �� ����������� �Ÿ��� ��ǥ�� ����ϴ�."
					+ "\n����Ͻʽÿ�. ����� ������� ���ѳ����մϴ�.");
		} else if (jobName.equalsIgnoreCase("�ǻ�")) {
			p.sendMessage(ChatColor.GOLD + "����� ���� : " + ChatColor.AQUA + getJob(p) + ChatColor.GRAY
					+ "\n����� ������ �츮�� �ǻ��Դϴ�."
					+ "\n����� ��ħ���� 1���� ������ �� �ֽ��ϴ�."
					+ "\n�� ��c������ �÷��̾�� �� ���߿� ����� �ѹ� ��Ȱ��f�մϴ�."
					+ "\n��, �ڱ� �ڽ��� ������ �Ұ����մϴ�."
					+ "\n�ǻ��� ��Ÿ��� ������ ���� �� �ֽ��ϴ�...");
		} else if (jobName.equalsIgnoreCase("�����")) {
			p.sendMessage(ChatColor.GOLD + "����� ���� : " + ChatColor.AQUA + getJob(p) + ChatColor.GRAY
					+ "\n����� Ž���� ������Դϴ�."
					+ "\n�⺻�����δ� �ù������� Ȱ���մϴ�."
					+ "\n�㸶�� ������ �÷��̾� 1���� ������ �� �� �ֽ��ϴ�."
					+ "\n���� ������ �÷��̾ ��c������ �Ǵ� ���󰡡�f��� ����� �������� ���ᰡ �ǰ� �Ǹ�"
					+ ChatColor.GRAY + "\n�����ڿ� ���� �㸶�� �ù��� ���̰�  �ٴ� ��  �ֽ��ϴ�."
					+ "\n��, �� �ɷ��� ���ο��� ���� ������ ����� �����̶�� ���� �˰� �� ���Դϴ�." 
					+ "\n(�����߿��� ����� �Ǵ� ����ڸ� �����մϴ�)." 
					+ "\n�ε� �ɷ��� �����ϰ� ����Ͻʽÿ�. ����� ���� ������� ����� �̴ϱ�?");
		} else if (jobName.equalsIgnoreCase("����")) {
			p.sendMessage(ChatColor.GOLD + "����� ���� : " + ChatColor.AQUA + getJob(p) + ChatColor.GRAY
					+ "\n����� ������ ��ü�� ���� �����Դϴ�. ��c����� �ѹ� ��Ȱ��f�ϰ� �˴ϴ�.(�ڿ�������)" 
					+ "\n��Ȱ�Ŀ��� ������ �ù����� ����˴ϴ�." 
					+ "\n���� ����� �� 1���� �Ѿ� 5���� ������ ���·�  �����մϴ�." 
					+ "\n��Ƴ�������! ����� ������� ����� ����� ��ȣ�� �����Դϴ�.");
		} else if (jobName.equalsIgnoreCase("��ġ��")) {
			p.sendMessage(ChatColor.GOLD + "����� ���� : " + ChatColor.AQUA + getJob(p) + ChatColor.GRAY
					+ "\n����� �������� �����Դϴ�." 
					+ "\n��� �÷��̾�� ����� ��ġ����� ���� �˰� �ֽ��ϴ�."
					+ "\n����� ��c��ħ�� Ȱ���� �ϴ� ���� �Ұ��ɡ�f������ ���� �Ǿ"
					+ "\n��ǥ������ �̵����� ������ ��ħ�̾ ��ġ�� ǥ�õ��� �ʽ��ϴ�."
					+ "\n����� ���� ��Ȱ�� ���� ����� Ÿ�κ��� ��c���� �̵��ӵ���f�� ������ �ֽ��ϴ�."
					+ "\n�߹��� ġ�ʽÿ�. �ƴϸ� �ٽ� ����� ��Ȱ�� ���ư��ðڽ��ϱ�?");
		} else if (jobName.equalsIgnoreCase("����")) {
			p.sendMessage(ChatColor.GOLD + "����� ���� : " + ChatColor.AQUA + getJob(p) + ChatColor.GRAY
					+ "\n����� ������ ������ �����Դϴ�." +  "\n����� �㿡 �÷��̾� 1���� ������ �� �ֽ��ϴ�." + ChatColor.GRAY
					+ "\n��ħ�� �Ǹ� �� ��c�÷��̾��� ������ ��ο��� �����ϴ�.��f"
					+ "\n�ɷ��� �� 1���� ��� �����մϴ�." 
					+ "\n��ŻӸ��� ������ ������ �� �ֽ��ϴ�.");
		} else if (jobName.equalsIgnoreCase("������")) {
			p.sendMessage(ChatColor.GOLD + "����� ���� : " + ChatColor.AQUA + getJob(p) + ChatColor.GRAY
					+ "\n����� ������� ������ �������Դϴ�." 
					+ "\n����� �߾��� ���� �־� ����� ��c��ǥ�� 2ǥ��f�μ� ��޵˴ϴ�."
					+ "\n����� ��c��ǥ�� ���ڵ��� �ʽ��ϴ�.��f" 
					+  "\n����� ������� �̲��� �����ڸ� ��Ƴ��ʽÿ�!");
		} else if (jobName.equalsIgnoreCase("������")) {
			p.sendMessage(ChatColor.GOLD + "����� ���� : " + ChatColor.AQUA + getJob(p) + ChatColor.GRAY
					+ "\n����� ���Ӽ��� ���� �������Դϴ�."
					+ "\n����� 2��°(�Ǵ� �� ����) �㿡 �÷��̾� 1���� ������ �� �ֽ��ϴ�." 
					+ "\n�÷��̾ �����ϴ� ��� �ش� ��c�÷��̾��� ������ ���ҽ��ϴ�.��f"
					+ "\n������ �÷��̾ �������� ��쿡�� �� �÷��̾�� ����ϰ�"
					+ "\n����� �����ڰ� �˴ϴ�."
					+ "\n����� �������� ������ �ֽ��ϴ�. �����Ӱ� �ൿ�Ͻʽÿ�.");
		} else if (jobName.equalsIgnoreCase("�߸�")) {
			p.sendMessage(ChatColor.GOLD + "����� ���� : " + ChatColor.AQUA + getJob(p) + ChatColor.GRAY
					+ "\n����� ������  ���� �ڽ��� �߸��Դϴ�."
					+ "\n����� ���ڸ� Ŭ���Ͽ� �������� ���� ��"
					+ "\n��c50%Ȯ���� �߰� �����ۡ�f�� ����ϴ�."
					+ "\n����鿡�� �������� �����־� �������ֽʽÿ�." 
					+ "\n����� �� ���Դϴ�.");
		} else if (jobName.equalsIgnoreCase("��ȣ��")) {
			p.sendMessage(ChatColor.GOLD + "����� ���� : " + ChatColor.AQUA + getJob(p) + ChatColor.GRAY 
					+ "\n����� Ÿ���� ��Ű�� ��ȣ���Դϴ�."
					+ "\n��ħ �Ǵ� ���� �÷��̾� 1���� ������ ��c�ش� �÷��̾�� �� ����"
					+ "\n��ǥ�� ���ڵ��� �ʽ��ϴ�.��f"
					+ "\n���� ��ȣ����� ����� �������� ���Ͽ� " 
					+ "\n�㿡 Ÿ�ο��� �޴� ��c��� �������� 2���ҡ�f�մϴ�."
					+ "\n�������ڸ� ��ų����� ��Ż��Դϴ�.");
		} else if (jobName.equalsIgnoreCase("Ž��")) {
			p.sendMessage(ChatColor.GOLD + "����� ���� : " + ChatColor.AQUA + getJob(p) + ChatColor.GRAY 
					+ "\n����� ����ɷ��� �پ Ž���Դϴ�."
					+ "\n����� ��c��ü�� �����f�Ͽ�(��ü ������ ��Ŭ��)"
					+ "\n�� �÷��̾��� �̸�, ����, ��� ��¥, ���� ���, ��ó�� �ִ� �÷��̾ �� �� �ֽ��ϴ�."
					+ "\n������ ��� �߸��Ͽ� ����� �ذ��ϼ���!");
		} else if (jobName.equalsIgnoreCase("���")) {
			p.sendMessage(ChatColor.GOLD + "����� ���� : " + ChatColor.AQUA + getJob(p) + ChatColor.GRAY
					+ "\n����� ������ ����Դϴ�."
					+ "\n����� ���۽� ��c���� ������ ��ⱸ��f�� ��� �����մϴ�." 
					+ "\n����� ������ �������� ��� ����鿡�� �ŷڸ� �ް� �ֽ��ϴ�."
					+ "\n����� ������ ��ζ�� ���� ��c��� �÷��̾ �˰� �ֽ��ϴ�.��f"
					+ "\n��� ������� �ϰ��ִ� ����� ���� �����Դϴ�.");
		} else if (jobName.equalsIgnoreCase("�ù�")) {
			p.sendMessage(ChatColor.GOLD + "����� ���� : " + ChatColor.AQUA + getJob(p) + ChatColor.GRAY
					+ "\n����� �ù��Դϴ�."
					+ "\nƯ���� �ɷ��� ������ �ʾ�����." 
					+ "\n�����ڸ� ã�Ƴ��� ���ؼ��� ����� �ʿ��մϴ�.");
		}else if (jobName.equalsIgnoreCase("�����")) {
			p.sendMessage(ChatColor.GOLD + "����� ���� : " + ChatColor.AQUA + getJob(p) + ChatColor.GRAY
					+ "\n����� ���� û�ξ����Դϴ�."
					+ "\n����� ��ħ�� 1���� ������ �� �ֽ��ϴ�." 
					+ "\n������ �÷��̾�� ����� ���� ����̸�"
					+ "\n�� ��c������ �÷��̾ ��� �Ǵ� ������ �� ��� ��������"
					+ "\n�����f���˴ϴ�. (�������� ���ᰡ ���� ��������"
					+ "\n�ù������� ���ֵ����� ���� ����� ����� ���ϼ� �ֽ��ϴ�.)"
					+ "\n(�����߿��� ����� �Ǵ� ����ڸ� �����մϴ�)." 
					+ "\n����� �Ƿڸ� �޼��Ͽ� �����ڿ� �����ϰ� ����� �̱ü����� ���߸�����.");
		} else if (jobName.equalsIgnoreCase("���Ż�")) {
			p.sendMessage(ChatColor.GOLD + "����� ���� : " + ChatColor.AQUA + getJob(p) + ChatColor.GRAY
					+ "\n����� ��ȥ�� �����ϴ� ���Ż��Դϴ�."
					+ "\n����� ��ħ���� 1���� ���Ӱ� ������ �� �ֽ��ϴ�." 
					+ "\n��c������ ����� ��� �Ǵ� ������ �� �� ��ȥ�� ������ ����"
					+ "\n������ ����� ������ �˴ϴ�.��f"
					+ "\n��Ÿ��� �������� �̷��� �ذ��� �� ���� ���Դϴ�."
					+ "\n(�����߿��� ���Ż�� �����簡 ���ÿ� �������� �ʽ��ϴ�.)");
		} else if (jobName.equalsIgnoreCase("����")) {
			p.sendMessage(ChatColor.GOLD + "����� ���� : " + ChatColor.AQUA + getJob(p) + ChatColor.GRAY
					+ "\n����� ȭ���� �پ �����Դϴ�."
					+ "\n����� 2��°�� �ں��� �÷��̾� 1���� ������ �� �ֽ��ϴ�." 
					+ "\n��c������ �÷��̾�� �� ��� �������� ����� �����f�˴ϴ�."
					+ "\n��, ����, ����, ��ο��Դ� ����� �Ұ����ϸ�"
					+ "\n��󿡰� ����� ������ ��ŵ�ϴ�."
					+ "\n���� �ɷ��� ����ҽ� '���󰡰� ������ �õ��ߴٰ� ��ο��� �˸��ϴ�.'."
					+ "\n(�ɷ��� ���ó�� �˴ϴ�)."
					+ "\n(����� ���������̸� ����� �׿��� �������� �ʽ��ϴ�.)"
					+ "\n����� ȭ���� ���Ḧ �ø��� �����ڿ� ������� �¸��� �̲�����.");
		} else if (jobName.equalsIgnoreCase("�����")) {
			p.sendMessage(ChatColor.GOLD + "����� ���� : " + ChatColor.AQUA + getJob(p) + ChatColor.GRAY
					+ "\n����� �����ְ� �پ ������Դϴ�."
					+ "\n����� ��c���ɿ��� 4����f�� ������ �ֽ��ϴ�." 
					+ "\n��c�������� �ݰ� ����Ʈ�� ���� ���¿�����f �� ���� ��Ŭ�� �� ��"
					+ "\n��c��װų� ���� �ֽ��ϴ�.��f"
					+ "\n��� ���� ���� �Ǹ� ����� �����˴ϴ�."
					+ "\n����� ���踦 ����ϸ� ���縮 ������ ���� ���Դϴ�.");
		} 
	}
	
	public void jobHelpMsg(Player p, int jobCode) {
		p.sendMessage("\n");
		if (jobCode == 0) {
			p.sendMessage(ms+
					"\n������� ��� �����Ϸ��� �������Դϴ�." 
					+"\n������ ���⸦ ������ �ֽ��ϴ�."
					+ "\n����ڰ� �ɷ��� ����Ͽ� ������ ����ڴ� ���ᰡ �˴ϴ�."
					+"\n���� ��ġ���̳� ���󰡰� �����Ѵٸ� �����ؾ��մϴ�."
					+"\n�ɷ��� ����Ͽ� ��ǥ�� ������ �ź��� �� ������ �ź��ҽ�"
					+ "\n�����ڶ�� ���� ��Ű�� �˴ϴ�." );
			sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
		} else if (jobCode == 1) {
			p.sendMessage(ms+
					 "\n������ �����Դϴ�."
					+ "\n��ħ���� 1���� �����Ͽ� ���������� �ƴ��� ������ �� �ֽ��ϴ�." 
					+ "\n����� ���� �Ǿ�� ������ ����� ��ſ��Ը� �˷����ϴ�." 
					+ "\n�׸��� 1���̶� ������ ����� �������� ���� �Ǿ��� ����"
					+ "\n�������� �˸��� ���ϴ�."
					+ "\n���� ������ �پ�ϴ�. �������� ���ص��� �� ����������� �Ÿ��� ��ǥ�� ����ϴ�.");
			sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
		} else if (jobCode == 2) {
			p.sendMessage(ms+
					 "\n������ �츮�� �ǻ��Դϴ�."
					+ "\n��ħ���� 1���� ������ �� �ֽ��ϴ�."
					+ "\n�� ������ �÷��̾�� �� ���߿� ����� �ѹ� ��Ȱ�մϴ�."
					+ "\n��, �ڱ� �ڽ��� ������ �Ұ����մϴ�.");
			sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
		} else if (jobCode == 3) {
			p.sendMessage(ms+
					"\nŽ���� ������Դϴ�."
					+ "\n�⺻�����δ� �ù������� Ȱ���մϴ�."
					+ "\n�㸶�� ������ �÷��̾� 1���� ������ �� �� �ֽ��ϴ�."
					+ "\n���� ������ �÷��̾ ������ �Ǵ� ���󰡶�� �������� ���ᰡ �ǰ� �Ǹ�"
					+"\n�����ڿ� ���� �㸶�� �ù��� ���̰�  �ٴ� ��  �ֽ��ϴ�."
					+ "\n��, �� �ɷ��� ���ο��� ���� ������ ����� �����̶�� ���� �˰� �� ���Դϴ�." 
					+ "\n(�����߿��� ����� �Ǵ� ����ڸ� �����մϴ�)." );
			sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
		} else if (jobCode == 4) {
			p.sendMessage(ms+
					 "\n������ ��ü�� ���� �����Դϴ�. ����� �ѹ� ��Ȱ�ϰ� �˴ϴ�.(�ڿ�������)" 
					+ "\n��Ȱ�Ŀ��� ������ �ù����� ����˴ϴ�." 
					+ "\n���� �� 1���� �Ѿ� 5���� ������ ���·�  �����մϴ�.");
			sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
		} else if (jobCode == 5) {
			p.sendMessage(
					 "\n����� �������� �����Դϴ�." 
					+ "\n��� �÷��̾�� ����� ��ġ����� ���� �˰� �ֽ��ϴ�."
					+ "\n����� ��ħ�� Ȱ���� �ϴ� ���� �Ұ��������� ���� �Ǿ"
					+ "\n��ǥ������ �̵����� ������ ��ħ�� ��ġ�� ǥ�õ����� �ʽ��ϴ�."
					+ "\n���� ��Ȱ�� ���� ����� Ÿ�κ��� ���� �̵��ӵ��� ������ �ֽ��ϴ�.");
			sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
		} else if (jobCode == 6) {
			p.sendMessage(
					 "\n������ ������ �����Դϴ�." 
					+  "\n�㿡 �÷��̾� 1���� ������ �� �ֽ��ϴ�." 
					+ "\n��ħ�� �Ǹ� �� �÷��̾��� ������ ��ο��� �����ϴ�."
					+ "\n�ɷ��� �� 1���� ��� �����մϴ�.");
			sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
		} else if (jobCode == 7) {
			p.sendMessage(
					 "\n������� ������ �������Դϴ�." 
					+ "\n�������� �߾��� ���� �־� ����� ��ǥ�� 2ǥ�μ� ��޵˴ϴ�."
					+ "\n�����ڴ� ��ǥ�� ���ڵ��� �ʽ��ϴ�.");
			sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
		} else if (jobCode == 8) {
			p.sendMessage(
					"\n���Ӽ��� ���� �������Դϴ�."
					+ "\n2��°(�Ǵ� �� ����) �㿡 �÷��̾� 1���� ������ �� �ֽ��ϴ�." 
					+ "\n�÷��̾ �����ϴ� ��� �ش� �÷��̾��� ������ ���ҽ��ϴ�."
					+ "\n������ �÷��̾ �������� ��쿡�� �� �÷��̾�� ����ϰ�"
					+ "\n�����簡 �����ڰ� �˴ϴ�.");
			sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
		} else if (jobCode == 9) {
			p.sendMessage(
					"\n������  ���� �ڽ��� �߸��Դϴ�."
					+ "\n���ڸ� Ŭ���Ͽ� �������� ���� ��"
					+ "\n50%Ȯ���� �߰� �������� ����ϴ�.");
			sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
		} else if (jobCode == 10) {
			p.sendMessage("\nŸ���� ��Ű�� ��ȣ���Դϴ�."
					+ "\n��ħ �Ǵ� ���� �÷��̾� 1���� ������ �ش� �÷��̾�� �� ����"
					+ "\n��ǥ�� ���ڵ��� �ʽ��ϴ�. ���� ��ȣ�����"
					+ "\n�㿡 Ÿ�ο��� �޴� ��� �������� 2�����մϴ�.");
			sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
		} else if (jobCode == 11) {
			p.sendMessage("\n����ɷ��� �پ Ž���Դϴ�."
					+ "\n��ü�� �����Ͽ�(��ü ������ ��Ŭ��)"
					+ "\n�� �÷��̾��� �̸�, ����, ��� ��¥, ���� ���, ��ó�� �ִ� �÷��̾ �� �� �ֽ��ϴ�.");
			sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
		} else if (jobCode == 12) {
			p.sendMessage("\n������ ����Դϴ�."
					+ "\n���۽� ���� ������ ��ⱸ�� ��� �����մϴ�." 
					+ "\n������ �������� ��� ����鿡�� �ŷڸ� �ް� �־�"
					+ "\n�� �÷��̾��� ������ ��ζ�� ���� ��� �÷��̾ �˰� �ֽ��ϴ�.");
			sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
		} else if (jobCode == 13) {
			p.sendMessage("\n�ù��Դϴ�."
					+ "\nƯ���� �ɷ��� ������ �ʾҽ��ϴ�." 
					+ "\n�ù��� �������� �ɷ��� ��� �Ǵ� +"
					+"\n������ �ɷ��� ����� �Ŀ��� ������ �����Դϴ�.");
			sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
		} else if (jobCode == 14) {
			p.sendMessage("\n���� û�ξ����Դϴ�."
					+ "\n��ħ�� 1���� ������ �� �ֽ��ϴ�." 
					+ "\n������ �÷��̾�� ����� ���� ����̸�"
					+ "\n�� ������ �÷��̾ ����� �� ��� ��������"
					+ "\n���ᰡ�˴ϴ�. (�������� ���ᰡ ���� ��������"
					+ "\n�ù������� ���ֵ����� ���� ����� ����ڰ� ���ϼ� �ֽ��ϴ�.)"
					+ "\n(�����߿��� ����� �Ǵ� ����ڸ� �����մϴ�).\" ");
			sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
		} else if (jobCode == 15) {
			p.sendMessage("\n��ȥ�� �����ϴ� ���Ż��Դϴ�."
					+ "\n��ħ���� 1���� ���Ӱ� ������ �� �ֽ��ϴ�." 
					+ "\n������ ����� ����� �� �� ��ȥ�� ������ ����"
					+ "\n������ ����� ������ �˴ϴ�."
					+ "\n(�����߿��� ���Ż�� �����簡 ���ÿ� �������� �ʽ��ϴ�.)");
			sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
		} else if (jobCode == 16) {
			p.sendMessage("\nȭ���� �پ �����Դϴ�."
					+ "\n2��°����� �÷��̾� 1���� ������ �� �ֽ��ϴ�." 
					+ "\n������ �÷��̾�� �� ��� �������� ����� ����˴ϴ�."
					+ "\n��, ����, ����, ��ο��Դ� ����� �Ұ����ϸ�."
					+ "\n��󿡰� ����� ������ ��ŵ�ϴ�."
					+ "\n���� �ɷ��� ����ҽ� '���󰡰� ������ �õ��ߴٰ� ��ο��� �˸��ϴ�.'."
					+ "\n(�ɷ��� ���ó�� �˴ϴ�)."
					+ "\n(�����ڴ� ���������̸� ����� �׿��� �������� �ʽ��ϴ�.)");
			sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
		} else if (jobCode == 17) {
			p.sendMessage("\n�����ְ� �پ ������Դϴ�."
					+ "\n���ɿ��� 4���� ������ �ֽ��ϴ�." 
					+ "\n��c�������� �ݰ� ����Ʈ�� ���� ���¿�����f �� ���� ��Ŭ�� �� ��"
					+ "\n��װų� ���� �ֽ��ϴ�."
					+ "\n��� ���� ���� �Ǹ� ����� �����˴ϴ�.");
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
			p.sendMessage(ms + "������ ���۵��� �ʾҽ��ϴ�.");
		} else if (dayTime != 1) {
			p.sendMessage(ms + "��ǥ�� ������ �����մϴ�.");
		} else if(getJob(p).equalsIgnoreCase("��ġ��")) {
			p.sendMessage(ms + "��ſ��Դ� ������ �����ϴ�.");
		} else if (!virtualJobMap.containsKey(target)) {
			p.sendMessage(ms + target + " ���� �������� �ƴմϴ�.");
		} else if (ftmPlayerList.get(target).job.equalsIgnoreCase("��ġ��")) {
			p.sendMessage(ms + target + " ���� ��ġ���Դϴ�. ��ġ�����Դ� ��ǥ�� �Ұ����մϴ�.");
		} else if (voteMap.containsKey(p.getName())) {
			p.sendMessage(ms + voteMap.get(p.getName()) + " �Բ� �̹� ��ǥ �ϼ̽��ϴ�.");
		} else {
			voteMap.put(p.getName(), target);
			if (!voteCnt.containsKey(target)) {
				voteCnt.put(target, 1);
			} else {
				voteCnt.put(target, voteCnt.get(target) + 1);
			}
			//p.sendMessage(ms + target + " ���� ��ǥ�߽��ϴ�.");
			ActionBarAPI.sendActionBar(p, ChatColor.RED+target + " ���� ��ǥ�߽��ϴ�.", 80);
			if (getJob(p.getName()).equalsIgnoreCase("������")) {
				voteCnt.put(target, voteCnt.get(target) + 1);
				p.sendMessage(ms + "����� ������ ���� �ֽ��ϴ�. ����� ��ǥ�� 2ǥ�μ� ��޵˴ϴ�.");
				priestTarget = target;
			}
			sendMessage(ms + "�������� " + ChatColor.AQUA + target + ChatColor.RESET + " �Կ��� ��ǥ�߽��ϴ�.");
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
			if(getJob(maxPName).equalsIgnoreCase("������")) {
				sendMessage(ms + maxPName + " ���� �������� ��ȣ�� ���� ��ǥ�� ���ڵ��� �ʾҽ��ϴ�.");
				virtualJobMap.put(maxPName, "������");
				updatePlayerInven();
				FtmPlayer ftmP = ftmPlayerList.get(maxPName);
				if(ftmP != null) ftmP.priest_noVoted += 1;
			} else if(maxPName.equalsIgnoreCase(guardTarget)) {
				sendMessage(ms + maxPName + " ���� ��ȣ���� ��ȣ�� ���Ͽ� ��ǥ�� ���ڵ��� �ʾҽ��ϴ�.");
			} else {
				if(ingamePlayer.contains(maxPName)) {
					sendMessage(ms + maxPName + " ���� ���� ���� ��ǥ�� �޾� ���ڵǾ����ϴ�.");
					votedPlayer = maxPName;
					FtmPlayer ftmP = ftmPlayerList.get(maxPName);
					if(ftmP != null) ftmP.beVotedPlayer += 1;
					String jobName = getJob(votedPlayer);
					sendMessage(ms+ votedPlayer + " ���� ��c"+jobName+ " ��f�Դϴ�.");
					updatePlayerInven();
					virtualJobMap.put(maxPName, jobName);
					Player votedP = Bukkit.getPlayer(votedPlayer);
					votedP.sendMessage(ms+"����� ���ڵ� �����Դϴ�. ���� �� ������ ���ݷ��� ������ �˴ϴ�.");
					updatePlayerInven();
					if(!votedP.hasPotionEffect(PotionEffectType.GLOWING)) votedP.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 2800, 0));
					if(priestTarget != null) {
						if(priestTarget.equalsIgnoreCase(maxPName)) {
							for(String tName : ingamePlayer) {
								if(tName.equalsIgnoreCase("������")) {
									if(ftmP != null) ftmP.priest_effort += 1;
									break;
								}
							}
						}
					}				
				}else {
					sendMessage(ms + maxPName + " ���� ���� ���� ��ǥ�� �޾����� ���� ���� �������� �ƴմϴ�.");			
				}			
			}
		} else {
			sendMessage(ms + "�ִ� ��ǥ�� ���� �÷��̾ �������� �ʽ��ϴ�. \n"+ms+"���� ��ǥ�� ��ҵǾ����ϴ�.");
		}
		voteMap.clear();
		voteCnt.clear();
	}

	public void gameQuitPlayer(Player p, boolean announce, boolean giveGold) {
		if (ingamePlayer.contains(p.getName())) {
			ingamePlayer.remove(p.getName());
			virtualJobMap.remove(p.getName());
			server.playerList.put(p.getName(), "�κ�");
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
						
						sendMessage(ms + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " ���� ����ϼ̽��ϴ�.");
						sendTitle("", ChatColor.RED+p.getName()+"���� ���صƽ��ϴ�.", 60);
						sendSound(Sound.ENTITY_GHAST_DEATH,1.5f, 1.3f);
					}
					p.sendMessage(ms+"���� �÷��� �������� 10��带 �����̽��ϴ�.");
				} else {
					if(announce) {
						
						FtmPlayer ftmP = ftmPlayerList.get(p.getName());
						if(ftmP != null) ftmP.ignore = true;
						sendMessage(ms + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " ���� �����ϼ̽��ϴ�.");
						sendSound(Sound.BLOCK_NOTE_GUITAR,1.5f, 2.0f);
					}
				}
				
				/////���Ż�, ����� �ɷ»��
				//Bukkit.broadcastMessage("quit: " + p.getName());
				//Bukkit.broadcastMessage("con_target: " + contractorTarget);
				//Bukkit.broadcastMessage("soul_target: " + soul_target);	
				if(contractorTarget != null) {
					if(contractorTarget.equalsIgnoreCase(p.getName())) {
						for(String tName : ingamePlayer) {
							if(getJob(tName).equalsIgnoreCase("�����")) {
								if(murderTeam.contains(tName)) {
									//�̹� �츶���̸� �н�
								}else {
									if(!murderTeam.contains(tName)) murderTeam.add(tName);
									sendTeamChat(murderTeam, ms+"����� "+tName+"���� �Ƿڸ� �޼��Ͽ� ���ᰡ �ƽ��ϴ�. ���� �׵� ����� �����Դϴ�.");
									sendTeamSound(murderTeam, Sound.BLOCK_NOTE_HARP);
									civilTeam.remove(tName);
									FtmPlayer ftmP = ftmPlayerList.get(tName);
									if(ftmP != null) ftmP.contractor_success += 1;
									if(police_invensted.contains(tName)) {
										for(String tmpName : ingamePlayer) {
											if(getJob(tmpName).equalsIgnoreCase("����")) {
												Player tmp = Bukkit.getPlayer(tmpName);
												if(existPlayer(tmp)) {
													tmp.sendMessage(ms+"��f����� �����ߴ� ��c"+tName+" ���� �������� ���ᰡ �� �� �����ϴ�.");
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
							if(getJob(tName).equalsIgnoreCase("���Ż�")) {
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
								
								p.sendMessage(ms+target+" ���� ������ ������ϴ�.");
								soul_player.playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1.5f, 1.5f);
								setFullJob(soul_player, getJobCode(tJob));
								if(tJob.equalsIgnoreCase("�����") && isMurderTeam) {
									soul_player.sendMessage(ms + p.getName() + "���� ������ �Ϸ��� ����ڿ��⿡ �������°� �˴ϴ�.\n" + ms
											+ "\"/tc <�޼���> \"��ɾ�� ������������ ��ȭ�� �����մϴ�.");
									sendTeamChat(murderTeam,
											ms + "���Ż翴�� " + tName + "���� ������ ������� ��ȥ�� ������ϴ�. ���� �׵� ����� �����Դϴ�.");
									sendTeamSound(murderTeam, Sound.BLOCK_NOTE_HARP);
									if(!murderTeam.contains(tName)) murderTeam.add(tName);
									civilTeam.remove(tName);
									if(police_invensted.contains(tName)) {
										for(String tmpName : ingamePlayer) {
											if(getJob(tmpName).equalsIgnoreCase("����")) {
												Player tmp = Bukkit.getPlayer(tmpName);
												if(existPlayer(tmp)) {
													tmp.sendMessage(ms+"��f����� �����ߴ� ��c"+target+" ���� �������� ���ᰡ �� �� �����ϴ�.");
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
								} else if(tJob.equalsIgnoreCase("�����") && isMurderTeam) {
									soul_player.sendMessage(ms+p.getName()+"���� �Ƿڸ� �Ϸ��� ����ڿ��⿡ ���θ��� ���ᰡ �˴ϴ�.\n"+ms+"\"/tc <�޼���> \"��ɾ�� ������������ ��ȭ�� �����մϴ�.");
									sendTeamChat(murderTeam, ms+"���Ż翴��  "+tName+"���� ������� ��ȥ�� ������ϴ�. ���� �׵� ����� �����Դϴ�.");
									sendTeamSound(murderTeam, Sound.BLOCK_NOTE_HARP);
									if(!murderTeam.contains(tName)) murderTeam.add(tName);
									civilTeam.remove(tName);
									if(police_invensted.contains(p.getName())) {
										for(String tmpName : ingamePlayer) {
											if(getJob(tmpName).equalsIgnoreCase("����")) {
												Player tmp = Bukkit.getPlayer(tmpName);
												if(existPlayer(tmp)) {
													tmp.sendMessage(ms+"��f����� �����ߴ� ��c"+target+" ���� �������� ���ᰡ �� �� �����ϴ�.");
													tmp.playSound(tmp.getLocation(), Sound.BLOCK_NOTE_HARP, 1.0f, 1.0f);
												}
												break;
											}
										}
									}
									if(civilTeam.size() <= 0) {
										murderWin();
									}
								} else if(tJob.equalsIgnoreCase("������") && isMurderTeam) {
									soul_player.sendMessage(ms+p.getName()+"���� �����ڿ��� ������ ����� �����ڰ� �˴ϴ�.\n"+ms+"\"/tc <�޼���> \"��ɾ�� ������������ ��ȭ�� �����մϴ�.");
									sendTeamChat(murderTeam, ms+"���Ż翴�� "+tName+"���� �������� ��ȥ�� ������ϴ�. ���� �׵� ����� �����Դϴ�.");
									sendTeamSound(murderTeam, Sound.BLOCK_NOTE_HARP);
									if(!murderTeam.contains(tName)) murderTeam.add(tName);
									civilTeam.remove(tName);
									if(police_invensted.contains(p.getName())) {
										for(String tmpName : ingamePlayer) {
											if(getJob(tmpName).equalsIgnoreCase("����")) {
												Player tmp = Bukkit.getPlayer(tmpName);
												if(existPlayer(tmp)) {
													tmp.sendMessage(ms+"��f����� �����ߴ� ��c"+target+" ���� �������� ���ᰡ �� �� �����ϴ�.");
													tmp.playSound(tmp.getLocation(), Sound.BLOCK_NOTE_HARP, 1.0f, 1.0f);
												}
												break;
											}
										}
									}
									if(civilTeam.size() <= 0) {
										murderWin();
									}
								} else if(tJob.equalsIgnoreCase("����") && isMurderTeam) {
									soul_player.sendMessage(ms+target+"���� �Ƿڸ� �Ϸ��� ����ڿ��⿡ ���θ��� ���ᰡ �˴ϴ�.\n"+ms+"\"/tc <�޼���> \"��ɾ�� ������������ ��ȭ�� �����մϴ�.");
									sendTeamChat(murderTeam, ms+"���Ż翴�� "+tName+"���� ������ ��ȥ�� ������ϴ�. ���� �׵� ����� �����Դϴ�.");
									sendTeamSound(murderTeam, Sound.BLOCK_NOTE_HARP);
									if(!murderTeam.contains(tName)) murderTeam.add(tName);
									civilTeam.remove(tName);
									if(police_invensted.contains(p.getName())) {
										for(String tmpName : ingamePlayer) {
											if(getJob(tmpName).equalsIgnoreCase("����")) {
												Player tmp = Bukkit.getPlayer(tmpName);
												if(existPlayer(tmp)) {
													tmp.sendMessage(ms+"��f����� �����ߴ� ��c"+target+" ���� �������� ���ᰡ �� �� �����ϴ�.");
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
					sendMessage(ms + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " ���� ���� �߽��ϴ�. "
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
		if (pJob.equalsIgnoreCase("������")) {
			sendMessage(ms + p.getName() + " ���� �����ڿ����ϴ�!");
			peopleKnowDeny = true;
		} else if (pJob.equalsIgnoreCase("��ġ��"))
			sendMessage(ms + p.getName() + " ���� ��ġ�������ϴ�!");
		else if (pJob.equalsIgnoreCase("�����") && betrayered) //����� ������϶�
			sendMessage(ms + p.getName() + " ���� �����̿����ϴ�!");
		if(murderTeam.size() <= 0) {
			civilWin();
		} else {
			if(pJob.equalsIgnoreCase("������")) sendMessage(ms+"�����ڴ� ��������� ���� �������� ���ᰡ �����ֽ��ϴ�!");
			else sendMessage(ms+"���� �������� ���ᰡ �����ֽ��ϴ�!");
		}
	}
	
	private void civilDead(Player p) {
		civilTeam.remove(p.getName());
		if(civilTeam.size() <= 0) {
			murderWin();
		}
	}
	
	private void doctorReverse(String doctorName, Player p) {
		sendMessage(ms+"�ǻ簡 ġ����� ���� "+p.getName()+" ����  �����Ͽ� ����½��ϴ�!");
		doctorTarget = null;
		reversePlayer(p);
		FtmPlayer ftmP = ftmPlayerList.get(doctorName);
		if(ftmP != null) ftmP.doctor_revive += 1;
	}
	
	private void soldierReverse(Player p) {
		sendMessage(ms+"���� "+p.getName()+" ���� �������� ������ ���߳��̽��ϴ�!");
		virtualJobMap.put(p.getName(), "����");
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
		
		sendTitle("��� ����", ChatColor.GRAY + "��� ������� ����Ͽ����ϴ�.", 70);
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
					if(getJob(p).equalsIgnoreCase("������")) {
						p.sendMessage(ms+"����� �������� �Ͽ��� ������� ��� ���صǾ����ϴ�.");	
					} else if(getJob(p).equalsIgnoreCase("��ġ��")) {
						p.sendMessage(ms+"����� �����ڿ� �����Ͽ� ������� ��� �����Ͽ����ϴ�.");	
					} else if(getJob(p).equalsIgnoreCase("�����")) {
						p.sendMessage(ms+"����� �����ڿ� �����Ͽ� ������� ��ġ��� �����Ͽ���");
					} else if(getJob(p).equalsIgnoreCase("����")) {
						p.sendMessage(ms+"�����  �����ڿ� �����Ͽ� ������� ��� �����Ͽ����ϴ�.");
					} else if(getJob(p).equalsIgnoreCase("�����")) {
						p.sendMessage(ms+"����� �Ϻ��� �Ƿڸ� �޼��Ͽ����ϴ�.");
					} else { //���� �ɷ����� ���� ����ϰ���
						p.sendMessage(ms+"����� ���󰡿��� ������ �����ڿ� �����Ͽ�");
					}
					
				}
				sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
			}
		}, 100L);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				for (Player p : tmpList) {
					if(getJob(p).equalsIgnoreCase("������")) {
						p.sendMessage(ms+"����� ������� ��ü�� ��� ������ �Ʒ��� ��������");	
					} else if(getJob(p).equalsIgnoreCase("��ġ��")) {
						p.sendMessage(ms+"�����ڴ� ���� ������ ������ �� ����� �����");
					} else if(getJob(p).equalsIgnoreCase("����")) {
						p.sendMessage(ms+"����� �����ڿ��� ���� ���� �ް�");
					}else if(getJob(p).equalsIgnoreCase("�����")) {
						p.sendMessage(ms+"�ٸ� ��� ������� �����Ͽ����ϴ�.");
					}else if(getJob(p).equalsIgnoreCase("�����")) {
						p.sendMessage(ms+"����� �Ƿ� ����� �ƴ� �������� ��������");
					}  else { //���� �ɷ����� ���� ����ϰ���
						p.sendMessage(ms+"������ ������� ���ƽ��ϴ�.");
					}
					
				}
				sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
			}
		}, 160L);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				for (Player p : tmpList) {
					if(getJob(p).equalsIgnoreCase("������")) {
						p.sendMessage(ms+"���� ���˸� ���Ͽ� ����, ������ ���� ���� �����Ͽ����ϴ�.");	
					} else if(getJob(p).equalsIgnoreCase("��ġ��")) {
						p.sendMessage(ms+"����� ���ÿ� �ִ� ���� ������ ���� �޾Ƴ����ϴ�.");
					} else if(getJob(p).equalsIgnoreCase("����")) {
						p.sendMessage(ms+"������ ������ �������ϴ�.");
					}else if(getJob(p).equalsIgnoreCase("�����")) {
						p.sendMessage(ms+"�׸��� ���ÿ� �ִ� ���� ���ǵ��� ��� ���� ������ �������ϴ�.");
					}else if(getJob(p).equalsIgnoreCase("�����")) {
						p.sendMessage(ms+"�ƹ��� ������ ������ �ʴ� �� �����ϴ�..");
					}  else { //���� �ɷ����� ���� ����ϰ���
						p.sendMessage(ms+"����� ������ �밡�μ�");
					}
					
				}
				sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
			}
		}, 220L);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				for (Player p : tmpList) {
					if(getJob(p).equalsIgnoreCase("������")) {
						p.sendMessage(ms+"�������� �� ��ǿ� ���� ���������� �˰� �� �� �ְ�����");	
					} else if(getJob(p).equalsIgnoreCase("��ġ��")) {
						p.sendMessage(ms+"����� �� ������� ��ǿ� ���Ͽ� ������ �˰� �ִ� ���������");
					} else if(getJob(p).equalsIgnoreCase("����")) {
						p.sendMessage(ms+"����� ���� ���� ������� ��������");
					}else if(getJob(p).equalsIgnoreCase("�����")) {
						p.sendMessage(ms+"Ž�忡 ���� �־� ������� ����� ���");
					}else if(getJob(p).equalsIgnoreCase("�����")) {
						p.sendMessage(ms+"�ϱ� ���� û�ξ��ڷμ� ���� ����� ���� �����");
					}  else { //���� �ɷ����� ���� ����ϰ���
						p.sendMessage(ms+"�����ڿ� ���󰡿��� ���� ���� �޾�����");
					}
					
				}
				sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
			}
		}, 280L);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				for (Player p : tmpList) {
					if(getJob(p).equalsIgnoreCase("������")) {
						p.sendMessage(ms+"����� ����� �ֿ��̶�� ���� ������ ���� ���� ���Դϴ�.");	
					} else if(getJob(p).equalsIgnoreCase("��ġ��")) {
						p.sendMessage(ms+"�׷����ѵ� ���� ����� �ְڽ��ϱ�?");
					} else if(getJob(p).equalsIgnoreCase("����")) {
						p.sendMessage(ms+"���� ���� ����� ������� ��ġ �־������?");
					}else if(getJob(p).equalsIgnoreCase("�����")) {
						p.sendMessage(ms+"��å���� ������ �ֱ� �Ѱ���?");
					}else if(getJob(p).equalsIgnoreCase("�����")) {
						p.sendMessage(ms+"�����ͼ� ���ο� ������ �������� ����.");
					} else { //���� �ɷ����� ���� ����ϰ���
						p.sendMessage(ms+"�̰� �ùٸ� ���̶�� �����ó���?");
					}
					sendTitle("��c�¸�", "", 80);
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
							server.ms_alert + ChatColor.GRAY  + "��c����������7�� ��a�¸���7�� ��c"+disPlayGameName+"��7�� ���� �Ǿ����ϴ�.");
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
		
		sendTitle("��� ����", ChatColor.GRAY + "�����ڵ��� ����ҽ��ϴ�.", 70);
		sendSound(Sound.BLOCK_NOTE_BELL, 1.0f, 0.5f);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
					sendMessage(ms+"����� ������ �����Ͽ� ��ħ�� �����ڿ� �� ������� ����ҽ��ϴ�.");
					sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
			}
		}, 100L);
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
					sendMessage(ms+"�����ڿ� �� ������� �����ҿ� �����Ǿ� �а��� ġ��� �� ���Դϴ�.");
					sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
			}
		}, 160L);
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
					sendMessage(ms+"�� �� ��Ű� ������� ������ ��ʽĿ� �����Ͽ� ���� ��������ϴ�.");
					sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
			}
		}, 220L);
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
					sendMessage(ms+"����� ���� �ϻ����� ���ư�����");
					sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
			}
		}, 280L);
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
					sendMessage(ms+"�� ����� �־��� ����� �ϳ��� �������� ���Ե� ���Դϴ�.");
					sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
					sendTitle("��c�¸�", "", 80);
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
							server.ms_alert + ChatColor.GRAY + "��b�ù�����7�� ��a�¸���7�� ��c"+disPlayGameName+"��7�� ���� �Ǿ����ϴ�.");
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
			p.sendMessage("��7���� ������ ���� ������ �ʴ´�.");
			return;
		}
		if (num == chestItem.size() + 1) {
			p.sendMessage("��7�����ۿ� ���� �� ����...");
			return;
		}
		if (num == chestItem.size() + 2) {
			p.sendMessage("��7���ٸ��� �������� �ʴ´�.");
			return;
		}
		if (num == 8 || num == 10 || num == 19) {
			for (int i = 0; i <= MyUtility.getRandom(0, 4); i++)
				p.getInventory().addItem(chestItem.get(num));
		}
		if(getJob(p).equalsIgnoreCase("�߸�") && MyUtility.getRandom(0, 1) == 1) {
			int anum = MyUtility.getRandom(0, chestItem.size() - 1);
			if (anum == 8 || anum == 10 || anum == 19) {
				for (int i = 0; i <= MyUtility.getRandom(0, 4); i++)
					p.getInventory().addItem(chestItem.get(anum));
			}
			p.getInventory().addItem(chestItem.get(anum));
			p.sendMessage("��7��? ���ڿ��� ������ ���� �߰��� �߰��Ͽ���. (�߸� �ɷ�)");
			FtmPlayer ftmP = ftmPlayerList.get(p.getName());
			if(ftmP != null) ftmP.creator_addictionItem += 1;
		}
		p.getInventory().addItem(chestItem.get(num));
		p.updateInventory();
	}

	/*public void setVoteInven() {
		Inventory jobMap = Bukkit.createInventory(null, 45, ""+ChatColor.BLACK+ChatColor.BOLD+"�÷��̾� ���");
		List<String> pList = new ArrayList<String>(virtualJobMap.size());
		pList.addAll(virtualJobMap.keySet());
		int tmpSlot = 1;
		for (int i = 0; i < pList.size(); i++) {
			ItemStack head = new ItemStack(Material.SKULL, 1,(byte)4);
			ItemMeta meta = head.getItemMeta();
			List<String> loreList = new ArrayList<String>(3);
			loreList.add("��7- ����: ��e" + virtualJobMap.get(pList.get(i)));
			meta.setLore(loreList);
			jobMap.setItem(tmpSlot, head);
		}
	} �Ⱦ� */

	public void endGame(boolean force) {
		if(force) {
			server.egCM.broadCast(server.ms_alert + "�����ڸ� ã�ƶ� ������ ���� ���� �Ǿ����ϴ�.");
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
			if(murderTeam.contains(pName)) { //���� ���̾��°� ����
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
				if(gameName.equalsIgnoreCase("FindTheMurder2")) { //Ż�ִ� 50�� ����
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
			
			//MMR å��
			if(gameName.equalsIgnoreCase("FindTheMurder2")) { //2ä�θ� mmr ����
				int mmr = calcMMR(ftmP);
				playerFtmData.setMMR(playerFtmData.getMMR() + mmr);
				Player p = Bukkit.getPlayer(pName);
				if(existPlayer(p)) {
					p.sendMessage(ms+"��f"+disPlayGameName+"��f�� ���� ����� ��a"+mmr+"��f���� �ݿ��ƽ��ϴ�.");
				}
			}				
			playerFtmData.saveData();
		}
	}
	
	public int calcMMR(FtmPlayer ftmP) {
		int mmr = 0;
		
		mmr -= ftmP.death * 5; //����� -5
		
		if(murderTeam_backUp.contains(ftmP.playerName)) { //�츶���̾�����
			//�츶�� �߰� MMR
			mmr += ftmP.civil_kill * 5; //�ù��� ų�� +5
			
			mmr += ftmP.spy_contact * 10; //����ڴ� �������� +10
			
			mmr += ftmP.negotiator_success * 8; //������ ���� ������ +8
			
			mmr -= ftmP.negotiator_fail * 3; //���� ���н� -3
			
			mmr += ftmP.contractor_success * 9; //����� �Ƿ� ������ +9
			
			mmr += ftmP.crazy_kill * 3; //��ġ���� �߰��� ų�� +3
			
			if(isMurderWin) { //�츶���̶��
				mmr += 40; //�¸� MMR
				
			}else { //�츶�ж��
				mmr -= 25;	//�й� MMR
			}
		}else { //�ù����϶�
			mmr += ftmP.police_success * 10; //���� ���߸� 10�� �߰�
			
			mmr += ftmP.doctor_revive * 9; //�ǻ� �츮�� +9
			
			mmr += ftmP.soldier_revive * 5; //���� ��Ȱ +5
			
			mmr += ftmP.reporter_report * 3; //���� �ɷ»�� + 3
			
			mmr += ftmP.reporter_reportSuccess * 12; //���ڰ� �츶�� ���߸� �߰� 12��
			
			mmr -= ftmP.innocent_kill * 25; //������ ��� ���̸� -25
			
			mmr += ftmP.keySmith_Use * 1; //����� ���農�� +1
			
			mmr += ftmP.magician_take * 3; //������ �ɷ»�� +3
			
			mmr += ftmP.magician_takeMurderTeam * 5; //�츶������ ������ �ɷ� ��� +5
			
			mmr += ftmP.creator_addictionItem * 0.5; //�߸� �ɷ� ��� +0.5
			
			mmr += ftmP.priest_effort * 2; //������ ��ǥ ����� ���ֽ� +2
			
			mmr += ftmP.priest_noVoted * 3; //������ ���� ������ +3
			
			mmr += ftmP.shaman_success * 2; //���Ż� �ɷ� ���� +2
			
			mmr += ftmP.shaman_successMurderTeam * 7; //���Ż� �ɷ� �츶������ ���� +7
			
			mmr += ftmP.farmer_kill * 4; //��δ� ų�� +4
			
			if(!isMurderWin) { //�ùν��̶��
				mmr += 35; //�¸� MMR											
			}else {
				mmr -= 30; //�й� MMR
			}
		}
		return mmr;
	}

	//////////////// �̺�Ʈ
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
			if(ingamePlayer.contains(p.getName())) { //���� �����ؾ����� ������ ��ɾ�
				if(cmd[0].equalsIgnoreCase("/tc") || cmd[0].equalsIgnoreCase("/��ê") ) {
					if(murderTeam.contains(p.getName())) {
						if(cmd.length < 2) {
							p.sendMessage(ms+"�޼����� �Է����ּ���.");
							p.sendMessage(ms+"/tc <�޼���>");
						} else {
							String str = "��7[ ��6��������ê��7 ] "+p.getName()+" >> ";
							for(int i = 1; i < cmd.length; i++) {
								str += " "+cmd[i];
							}
							sendTeamChat(murderTeam, str);
						}
					} else {
						p.sendMessage(ms+"�� ä���� ���������� �����մϴ�.");
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
				        if (cause.equals(DamageCause.VOID) && !ingame) { //���� ��� ������, ����		           
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
			if (e.getInventory().getTitle().equalsIgnoreCase("��0��l"+inventoryGameName+" �����")) {				
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}	
				gameHelper(p, e.getSlot());
			} else if (e.getInventory().getTitle().equalsIgnoreCase("��0��l"+inventoryGameName+" �������")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}	
				p.closeInventory();
				gameHelperAbilityClick(p, e.getSlot());	
			} else if (e.getInventory().getTitle().equalsIgnoreCase("��0��l"+inventoryGameName+" ���Ӽ���")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}	
				p.closeInventory();
				gameHelperAbClick(p, e.getSlot());	
			}
			if (!ingamePlayer.contains(p.getName())) return;
			if (e.getInventory().getTitle().equalsIgnoreCase("��0��l��ü ����")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}	
				p.closeInventory();
				detectorInvenClick(p, e.getSlot());	
			} else if (e.getInventory().getTitle().equalsIgnoreCase("��0��l�����ڸ� ã�ƶ�")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}	
				p.closeInventory();
				ftmHelperInvenClick(p, e.getSlot());	
			} else if (e.getInventory().getTitle().equalsIgnoreCase("��0��l�ɷ�")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}	
				p.closeInventory();
				abilityInvenClick(p, e.getSlot());	
			} else if (e.getInventory().getTitle().equalsIgnoreCase("��0��l��ǥ �ź� Ȯ��")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}	
				p.closeInventory();
				murderAbInvenClick(p, e.getSlot());	
			} else if (e.getInventory().getTitle().equalsIgnoreCase("��0��l�ɷ»��")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}	
				p.closeInventory();
				useAbility(p, e.getSlot());	
			} else if (e.getInventory().getTitle().equalsIgnoreCase("��0��l������Ȳ")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}	
				if(voting) { //��ǥ�߿� ������Ȳ Ŭ����
					p.closeInventory();
					votePlayer(p, e.getSlot());	
				}
			} else if (e.getInventory().getTitle().equalsIgnoreCase("��0��l��ü ����")) {
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
				if (!ingamePlayer.contains(p.getName())) { //�����ڰ� �׿� ���������������� ��Ģ ����
					return;
				}
				e.setCancelled(true);
			}
		}

		@EventHandler
		public void onHitPlayer(EntityDamageByEntityEvent e) {
			if(e.getEntity() instanceof Player && ingame) { //���ӽ�����������
				Player player = (Player) e.getEntity();
				Player damager = null;
				
				if (!ingamePlayer.contains(player.getName())) { //�����ڰ� �׿� ���������������� ��Ģ ����
					return;
				}
				
				boolean isDirectAttack = true;
				
				boolean isGun = false;
				if (e.getDamager() instanceof Snowball) { //ȭ��� �Ѿ˿����� ������ ����
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
				
				if (damager == null) //������ ������ ����
					return;
				
				if (!ingamePlayer.contains(damager.getName())) { //�����ڰ� �׿� ���������������� ��Ģ ����
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
					e.setCancelled(true); //��ǥ �Ǵ� ������� ������ �ȹ���
					return;
				}

				// ���� �������� �÷��̾���
				if (!(getItemDamage(damager) == 0) && !isGun) { // ������ ����
					e.setDamage(getItemDamage(damager));
				}		
				
				if(votedPlayer.equalsIgnoreCase(damager.getName())) {
					e.setDamage((int) e.getDamage()/2);
					ActionBarAPI.sendActionBar(damager, "����� ���ڵ� �����̱⿡ ���ݷ��� �����̵˴ϴ�.", 80);
					damager.playSound(damager.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1.0f, 2.0f);
				}
				
				String tJob = getJob(damager);
				if(tJob == null) {
					server.egPM.printLog(ms+"EnitytyDamagerByEntityEvent�� getJob���� Null�߻�");
					return;
				}
				if (tJob.equalsIgnoreCase("��ġ��") && dayTime != 2) { // ��ġ���� ��ħ�� ���� �Ұ���
					//damager.sendMessage(ms + "������ ���� �ƴմϴ�! �� ����� ���̸� ü���� �� �Դϴ�!");
					ActionBarAPI.sendActionBar(damager, ChatColor.RED + player.getName() + "������ ���� �ƴմϴ�! �� ����� ���̸� ������ �� �Դϴ�!", 80);
				}
				/*
				 * if (getJob(damager).equalsIgnoreCase("����")) { //����� ������ if
				 * (getHeldMainItemName(damager).equalsIgnoreCase(ChatColor.AQUA + "�����")) {
				 * takeItem(damager, damager.getInventory().getItemInMainHand(), 1);
				 * player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 10));
				 * player.sendMessage(ChatColor.RED + "������� �¾ҽ��ϴ�!!!");
				 * damager.sendMessage(ChatColor.RED + "������� ��� �Ͽ����ϴ�."); } }
				 */
				if ((murderTeam.contains(player.getName()) && murderTeam.contains(damager.getName()))) { // ������ ��ų �˸�
					//player.sendMessage(ms + "������ " + damager.getName() + "���� ����� �����߽��ϴ�! ");
					//damager.sendMessage(ms + player.getName() + " ���� �����Դϴ�! �������ּ���!");
					player.sendMessage(ChatColor.RED + "������ " + damager.getName() + "���� ����� �����߽��ϴ�! ");
					damager.sendMessage(ChatColor.RED + player.getName() + " ���� �����Դϴ�! �������ּ���!");
					e.setDamage(0.1);
				}

				double damage = e.getDamage();
				if(guardTarget != null) {
					if(guardTarget.equalsIgnoreCase(player.getName()) && dayTime == 2) {
						damage -= 2; 
						if(damage < 1) damage = 1; 
						ActionBarAPI.sendActionBar(player, ChatColor.RED + "��ȣ���� �������� �޴� �������� 2 �����߽��ϴ�.", 80);
					}		
				}
				if ((player.getHealth() - damage <= 0) && (!player.isDead())) { // �����ڰ� ���� ������ �������� �޾Ҵٸ�
					if (getJob(damager).equalsIgnoreCase("��ġ��") && dayTime != 2) {
						e.setCancelled(true); // ������ ��� ����
						gameQuitPlayer(damager, false, true);
						damager.sendMessage(ms + "����� ���� �ƴҶ� ������ ������ ���������ϴ�..");
						sendMessage(ms + "��ġ�� " + damager.getName() + " �Բ��� �볷�� ������ �õ��Ͽ� ���������ϴ�.");
						damager.setHealth(0);
					} else if (civilTeam.contains(player.getName())) { // ������ �ù����̰�
						if (civilTeam.contains(damager.getName())) {
							 // ������ �ù����̸� �����ڸ� ����
							if(getJob(damager.getName()).equalsIgnoreCase("�����") && contractorTarget != null) { //���� ����� ����ڰ� Ÿ���� �������ִٸ�
								if (contractorTarget.equalsIgnoreCase(player.getName())) {// ���� ����� Ÿ���̶��
									// ���̱� ������
									if (getJob(player).equalsIgnoreCase("����") && !soldier_use) { // �����ڰ� �ǻ��� ��ȣ�� ���޾Ҵµ�
										// �����̸� ����
											e.setCancelled(true);
											soldierReverse(player);
									}
								} else { //Ÿ�پƴϸ� ������
									e.setCancelled(true); // ������ ��� ����
									damager.sendMessage(ms + "����� ������ �÷��̾ �׿����� ���������ϴ�.");
									sendMessage(ms + damager.getName() + " �Բ��� ������ �÷��̾ �����һ��Ͽ� ���������ϴ�.");
									if(damagerFtmP != null) {
										if(!peopleKnowDeny)damagerFtmP.innocent_kill += 1;
									}
									damager.setHealth(0);
								}
							}else {
								e.setCancelled(true); // ������ ��� ����
								damager.sendMessage(ms + "����� ������ �÷��̾ �׿����� ���������ϴ�.");
								sendMessage(ms + damager.getName() + " �Բ��� ������ �÷��̾ �����һ��Ͽ� ���������ϴ�.");
								if(damagerFtmP != null) damagerFtmP.innocent_kill += 1;
								damager.setHealth(0);
							}														
						} else { // ������ �츶���̸�
							if (doctorTarget != null && player.getName().equalsIgnoreCase(doctorTarget)) { // �����ڰ� �ǻ���
																											// ��ȣ�� ������
																										// ����
								boolean suc = false;
								for(String tName : ingamePlayer) {
									if(getJob(tName).equalsIgnoreCase("�ǻ�")) { //�ǻ簡 �����Ѵٸ�
										if(dayTime == 2) { //�㿡��
											e.setCancelled(true);
											doctorReverse(tName, player);	
											
											suc = true;
										}
										break;
									}
								}
								
								if(!suc) {
									if (getJob(player).equalsIgnoreCase("����") && !soldier_use) { // �����ڰ� �ǻ��� ��ȣ�� ���޾Ҵµ�
										// �����̸� ����
										e.setCancelled(true);
										soldierReverse(player);
									}
								}

							} else if (getJob(player).equalsIgnoreCase("����") && !soldier_use) { // �����ڰ� �ǻ��� ��ȣ�� ���޾Ҵµ�
																								// �����̸� ����
								e.setCancelled(true);
								soldierReverse(player);
							} else { //��Ȱx��
								if(damagerFtmP != null) {
									damagerFtmP.murder_kill += 1;
									if(getJob(damager).equalsIgnoreCase("��ġ��")) {
										damagerFtmP.crazy_kill += 1;	
									}
								}
							}
						} 
					} else { //������ �츶���̸�
						if (doctorTarget != null && player.getName().equalsIgnoreCase(doctorTarget)) {
							for(String tName : ingamePlayer) {
								if(getJob(tName).equalsIgnoreCase("�ǻ�")) { //�ǻ簡 �����Ѵٸ�
									if(dayTime == 2) { //�㿡��
										e.setCancelled(true);
										doctorReverse(tName, player);	
									}
									break;
								}
							}
						}					
						if(damagerFtmP != null) damagerFtmP.civil_kill += 1; 
						if(getJob(damager).equalsIgnoreCase("���")) {
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
								/*if(dayCnt != 2 && getJob(player.getKiller().getName()).equalsIgnoreCase("��ġ��")) {
									killer.sendMessage(ms + "���� ������ ������ ü�� �Ǿ����ϴ�.");
									killer.setHealth(0);
								}*/
							}
						}
						gameQuitPlayer(player, true, true);
						Location pL = MyUtility.getIntLocation(player);
						CorpseData c = CorpseAPI.spawnCorpse(player, pL);
						List<String> nears = new ArrayList<String>(4); //�ֺ��� ���� ��Ǥ���
						HashMap<String, Double> locMap = new HashMap<String, Double>(ingamePlayer.size()); //�÷��̾ ��ü���� �Ÿ�
						for(String tName : ingamePlayer) {
							Player t = Bukkit.getPlayer(tName);
							if(existPlayer(t)) {
								locMap.put(tName, pL.distance(t.getLocation())); //�� �־�
							}
						}
						List<String> tmpList = MyUtility.sortByValue(locMap, true); //���������� ����
						for(int i = 0; i < (tmpList.size() < 4 ? tmpList.size() : 4); i++) {
							nears.add(tmpList.get(i));
						}
						MyUtility.mixList(nears);
						myCorpse corpse = new myCorpse(player.getName(), getJob(player), killer != null ? getHeldMainItemName(killer) : "Ư������ ����", dayCnt, c, nears);
						corpseList.put(pL, corpse);	
						for(String pName : ingamePlayer) {							
							Player t = Bukkit.getPlayer(pName);
							if(existPlayer(t)) {
								if(getJob(t).equalsIgnoreCase("����")) {
									t.sendMessage(ms+"��� ���� ��ǥ - ��eX: "+pL.getBlockX()+", Y: "+pL.getBlockY()+", Z: "+pL.getBlockZ()+"\n"
											+ ms+"��� ������� �Ÿ�: ��e"+(int)t.getLocation().distance(pL)+"m");
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
				if(getJob(p).equalsIgnoreCase("�����") && p.isSneaking()) {
					Location l = e.getClickedBlock().getLocation();
					if (l.getBlock().getType() == Material.WOODEN_DOOR) {
						if(keySmith_left >= 1) {
							keySmith_left -= 1;
							if (doorLockList.contains(l)) {
								doorLockList.remove(l.clone().add(0, -1, 0));
								doorLockList.remove(l);
								doorLockList.remove(l.clone().add(0, 1, 0));
								p.sendMessage(ms + "���� �������ϴ�. "+keySmith_left+" �� ���ҽ��ϴ�.");
								p.getWorld().playSound(p.getLocation(), Sound.BLOCK_IRON_DOOR_OPEN, 1.0f, 1.0f);
							} else {
								doorLockList.add(l.clone().add(0, -1, 0));
								doorLockList.add(l);
								doorLockList.add(l.clone().add(0, 1, 0));
								p.sendMessage(ms + "���� �ᰬ���ϴ�."+keySmith_left+" �� ���ҽ��ϴ�.");
								p.getWorld().playSound(p.getLocation(), Sound.BLOCK_IRON_DOOR_CLOSE, 1.0f, 1.0f);
							}
							FtmPlayer ftmP = ftmPlayerList.get(p.getName());
							if (ftmP != null) ftmP.keySmith_Use += 1;
						} else {
							p.sendMessage(ms + "���ɿ��踦 ��� ����߽��ϴ�.");
						}
					}	
				}
			}
			if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
				String job = getJob(p);
				if (job != null && (job.equalsIgnoreCase("Ž��") || job.equalsIgnoreCase("�����"))) {
					List<Location> keyList = new ArrayList<Location>(corpseList.size());
					keyList.addAll(corpseList.keySet());
					for(Location l : keyList) {
						if(l.distance(p.getLocation()) < 1) {
							p.openInventory(job.equalsIgnoreCase("Ž��") ? detectorCorpse : spyCorpse);
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
									case 0: p.sendMessage(ms+"��7�̹� ������ �о ������ �ִ�..."); break;
									case 1: p.sendMessage(ms+"��7�ֱٿ� ���ڸ� �� ������ �ִ� ���߿� �ٽ� �����"); break;
									case 2: p.sendMessage(ms+"��7�� ���ڴ� ��� �и� �� ����."); break;
								}
							}	
						}	
					}else if(e.getClickedBlock().getType() == Material.WOODEN_DOOR){
						Location l = e.getClickedBlock().getLocation();
						for(Location tl : doorLockList) {
							if(MyUtility.compareLocation(tl, l)) {
								e.setCancelled(true);
								p.sendMessage(ms+"�� ���� ��� �ֽ��ϴ�.");							
							}
						}
					}
							
				}
				if (e.getItem() == null || e.getItem().getType() == Material.AIR) { //�������� �ȵ�� ��Ŭ�������� ����
					return;
				} else if (getHeldMainItemName(p).equalsIgnoreCase("��f[ ��6���� ����� ��f]")) {
					p.openInventory(gameHelper);
					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 2.0f);
				} else if (getHeldMainItemName(p).equalsIgnoreCase("��f[ ��c���� �޴� ��f]")) {
					p.openInventory(ftmHelper);
					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 2.0f);
				} else if (e.getItem().getType() == Material.IRON_HOE) {
					if (!takeItem(p, Material.MELON_SEEDS, 1)) {
						p.sendMessage("��c9mmźȯ�� �����մϴ�.");
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
					if (getJob(p).equalsIgnoreCase("�߸�")) {
						if (takeItem(p, Material.IRON_SWORD, 3)) {
							p.sendMessage(ms + "���Į�� �����!(�߸��� ������ �ɷ�)");
							p.getInventory().addItem(rose_sword);
							p.playSound(p.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1.5f, 1.5f);
						}
					}
				} else if (e.getItem().getType() == Material.WATCH) {
					if(!p.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
						p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 300, 1));
						//p.removePotionEffect(PotionEffectType.);
						removeItem(p, Material.WATCH, 1);
						p.sendMessage(ms + "���� �ð踦 ��� �߽��ϴ�. 15�ʰ� ����� ����ϴ�.");
					}else {
						p.sendMessage(ms + "�̹� ���� �����Դϴ�.");
					}				
				} else if (e.getItem().getType() == Material.DIAMOND) {
					p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 1));
					removeItem(p, Material.DIAMOND, 1);
					p.sendMessage(ms + "������ ���� ��� �߽��ϴ�. 5�ʰ� ����2�� �ްԵ˴ϴ�.");
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
					server.egCM.sendDistanceMsgToStringList(ingamePlayer, p, "��f[ ��e"+rankMap.get(p.getName())+ " ��f] ��7"+p.getName()+" >> " +e.getMessage(), chatDistance, 6 ,false);
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
				p.sendMessage(ms+"�� �������� ������ �� �����ϴ�.");
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
						ActionBarAPI.sendActionBar(p, ChatColor.RED+ "��l�㿡�� �̸�ǥ�� �������ϴ�.", 80);
					}
				}
			}
		}*/
		
		/*@EventHandler
		public void onPlayerMove(PlayerMoveEvent e){
			Player p = (Player) e.getPlayer();
			if(ingamePlayer.contains(p.getName())) {
				///��ġ�̵� �ƴϸ� ĵ��
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
	
	///////////////////// ��ã�� ������ �÷��̾�� Ŭ����
	public class FtmPlayer {
		public String job;
		public String playerName;
		public boolean ignore = false;
		
		//������ �����
		public int death; //�����
		public int innocent_kill; //������ �÷��̾ ���μ�
		public int murder_kill; //�츶�� �ù��� ������ ��
		public int civil_kill; //�ù������� ���θ��� ų�� ��
		public int doctor_revive; //�ǻ�� �츰��
		public int police_success; //������ ���θ��� ã����
		public int spy_contact; //�����̷� ������ ��
		public int soldier_revive; //�������� ��Ȱ�� ��
		public int crazy_kill; //��ġ���̷� ����� ���� ��
		public int reporter_report; //���ڷ� ���� ��
		public int reporter_reportSuccess; //���ڷ� ���θ��� ���� ��
		public int priest_effort; //�������� ǥ�� ������� ������ ��
		public int priest_noVoted; //�����ڷ� ���� ������
		public int magician_take; //������� �ɷ��� ������
		public int magician_takeMurderTeam; //������� ���θ��� �ɷ��� ������
		public int creator_addictionItem; //�߸��� �߰� �������� ���� ��
		public int farmer_kill; //��η� ų�Ѽ�
		public int contractor_success; //����ڷ� �Ƿڸ� �޼��� ��
		public int shaman_success; //���Ż�� �ɷ��� ���� ��
		public int shaman_successMurderTeam; //���Ż�� ���θ��� �ɷ��� ���� ��
		public int negotiator_success; //���󰡷� ������ ��
		public int negotiator_fail; //���󰡷� ���� ������ ��
		public int keySmith_Use; //������� ���踦 �����  ��
		public int beVotedPlayer; //���ڵ� ��
		
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
			return "��7��ü����\n�̸�: "+corpseName+
					"\n����: "+corpseJob+
					"\n���: "+killItem+
					"��7\n���ص� ��¥: "+killDay+
					"��7\n������ �ִ� �÷��̾� 4��: "
					+ "\n��7"+(nearList == null ? "" : nearList.toString());
		}
		
		public void deleteCorpse() {
			CorpseAPI.removeCorpse(corpse);
		}

	}
}
