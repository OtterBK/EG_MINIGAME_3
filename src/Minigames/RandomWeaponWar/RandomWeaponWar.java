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
	// �̺�Ʈ��
	public EventHandlerRWW event;
	
	public String ms = "��7[��6��ü��7] ";

	///////////// private
	// ���� �÷��̾� ����Ʈ
	//private HashMap<String, SpfPlayer> playerMap = new HashMap<String, SpfPlayer>();
	private String cmdMain;
	//////// ���� ����
	public int gameStep = 0;
	public EGScheduler mainSch;
	
	public List<Location> loc_items = new ArrayList<Location>();
	public List<Location> loc_helidoors = new ArrayList<Location>(20);
	public Location loc_Helicopter;
	
	public Location tmpItemPos1;
	public Location tmpItemPos2;
	
	public List<String> noSoundList = new ArrayList<String>();
	
	public LinkedHashMap<String, Long> testMap = new LinkedHashMap<String, Long>();
	
	//�����
	public LinkedHashMap<String, SpecialWeapon> allWeapon = new LinkedHashMap<String, SpecialWeapon>();
	public LinkedHashMap<String, SpecialWeapon> specialWeaponMap = new LinkedHashMap<String, SpecialWeapon>();
	public List<String> tmpWeaponList = new ArrayList<String>(specialWeaponMap.keySet());
	
	public SpecialWeapon item_parachute = new Parachute(this);
	public SpecialWeapon item_grenade = new Grenade(this);
	public SpecialWeapon item_flashbang = new FlashBang(this);
	public ItemStack item_finder;
	public ItemStack item_noSound;
	public ItemStack item_getRandomWeapon;
	
	//�Ĺ���
	public LinkedHashMap<ItemStack, Integer> farmingItemsMap = new LinkedHashMap<ItemStack, Integer>();
	
	public List<Hologram> itemHolograms = new ArrayList<Hologram>();
	
	//�ڱ���
	public int limiterLv = 0;
	public Location limiterCenter;
	public int limiterRadius = 600;
	public boolean applyBorder = false;
	public boolean doDamage = false;
	
	//�� ����
	public HashMap<String, EGScheduler> healItemSchMap = new HashMap<String, EGScheduler>();
	
	//���ް���
	public HashMap<Location, Material> supplyLocMap = new HashMap<Location, Material>();
	public HashMap<String, List<EntityShulker>> supplyShulkerMap = new HashMap<String, List<EntityShulker>>();
	public List<ItemStack> supplyItems = new ArrayList<ItemStack>();
	
	////// ���� �κ��丮
	public Inventory inven_gameHelper;
	
	//////// ���̵��
	private Sidebar rwwSidebar;
	private List<SidebarString> textList = new ArrayList<SidebarString>(5);
	
	//�ڱ� �ڽ�
	public RandomWeaponWar rww = this;
	
	//��ũ �����
	public HashMap<String, RwwPlayer> rwwPlayerMap = new HashMap<String, RwwPlayer>();
	
	public RandomWeaponWar(EGServer server, String gameName, String displayGameName, String cmdMain) {

		//////////////////// �ʼ� ������
		super(server);
		
		this.cmdMain = cmdMain;
		
		ms = "��7[ ��e! ��7] ��f: ��c���� ���� ���� ��f>> "; // �⺻ �޼���
		this.gameName = gameName;
		this.disPlayGameName = displayGameName;
		inventoryGameName = ChatColor.stripColor(disPlayGameName);
		minPlayer = 4;
		maxPlayer = 24;
		startCountTime = 60;

		doneSetting = loadGameData(); // ���� ���� ��ġ ��� �ε�	
		
		/////////////////////// �ڵ� ����(�����۵��)
		dirSetting(this.gameName);
		////////////////
		
		//map.loadData(locPath);
		
		mainSch = new EGScheduler(this);
		
		SidebarString tmpLine = new SidebarString("");
		rwwSidebar = new Sidebar("��f[ ��6���� ��Ȳ ��f]", server, 600, tmpLine);
		Objective tmpO = rwwSidebar.getTheScoreboard().getObjective("health");
		if(tmpO != null) {
			tmpO.unregister();
		}
		Objective o = rwwSidebar.getTheScoreboard().registerNewObjective("health", "health");
		o.setDisplayName("��cHP");
		o.setDisplaySlot(DisplaySlot.BELOW_NAME);
		
		/////////////////// ���� �����
		inven_gameHelper = Bukkit.createInventory(null, 27, "��0��l"+inventoryGameName+" �����");

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
		meta.setDisplayName("��7- ��c�¸����� ��7-");
		List<String> loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7�ִ��� ���� ��Ƴ�������.");
		loreList.add("��7������ �������� �� ���� ������ �޽��ϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_gameHelper.setItem(10, item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7- ��c������ ��7-");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7������ ���۵Ǹ� ������ �̵��˴ϴ�.");
		loreList.add("��71�оȿ� ����⿡�� �����ؾ��ϸ� 1���� ������");
		loreList.add("��7�ڵ����� ���� �ϰԵ˴ϴ�.");
		loreList.add("��7���ӽ��� 1���� �����ÿ� ���⸦ ���� �� �ִ� �̱���� �޽��ϴ�.");
		loreList.add("��7�� ���⸦ �̿��Ͽ� Ư���� ���⸦ �̾� �ٸ� �÷��̾");
		loreList.add("��7óġ�ϰ� 1���� ���������!");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_gameHelper.setItem(11, item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7- ��c�Ĺ� ��7-");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7�� ������ �������� �������ֽ��ϴ�.");
		loreList.add("��7�� �����۵��� ��� �ڽ��� ���� ��ȭ�ϼ���!");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_gameHelper.setItem(12, item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7- ��c�ڱ��� ��7-");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7�÷��� �ð��� �������� �� ������ �����ϱ� ����");
		loreList.add("��7�ڱ����� Ȱ��ȭ�˴ϴ�.");
		loreList.add("��7���ѵ� �ڱ��� ���� �ۿ����� �������� �Խ��ϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_gameHelper.setItem(13, item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7- ��c���� ��7-");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7�����ð����� ����ǰ�� �����˴ϴ�.");
		loreList.add("��7����ǰ�� �߱��Ǿ� ǥ�õ˴ϴ�.");
		loreList.add("��7����ǰ������ ������ڿ� ���� �����۵��� ���� �� �ֽ��ϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_gameHelper.setItem(14, item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7- ��c���ǻ��� ��7-");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7���� ���� �δ� ���� �Ұ����մϴ�.");
		loreList.add("��7������ ��ȯ, ����ֱ� ��� ����������");
		loreList.add("��72���̼� 1���� ���������� ���� �����ϴ� ����");
		loreList.add("��7��Ģ�� ���ݵ˴ϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_gameHelper.setItem(16, item);

		event = new EventHandlerRWW(server, this);
		// �� �÷����ο� �̺�Ʈ ����
		server.getServer().getPluginManager().registerEvents(event, server);
		
		
		
		//////���� ����
		makeWeapons();
		
		makefarmingItems();
		
		makeSupplyItems();
		
		allWeapon.putAll(specialWeaponMap);
		
		allWeapon.put(item_parachute.getDisplayName(), item_parachute);
		allWeapon.put(item_grenade.getDisplayName(), item_grenade);
		allWeapon.put(item_flashbang.getDisplayName(), item_flashbang);
		
		item_getRandomWeapon = new ItemStack(Material.CHEST, 1);
		meta = item_getRandomWeapon.getItemMeta();
		meta.setDisplayName("��f[ ��b���� ���� ��f]");
		//loreList = new ArrayList<String>();
		item_getRandomWeapon.setItemMeta(meta);
		
		item_finder = new ItemStack(Material.COMPASS, 1);
		meta = item_finder.getItemMeta();
		meta.setDisplayName("��f[ ��b�ڱ��� Ž���� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��e�� ��ħ���� �׻�");
		loreList.add("��e�ڱ����� �߽����� ����Ų��.");
		loreList.add("");
		meta.setLore(loreList);
		item_finder.setItemMeta(meta);
		
		item_noSound = new ItemStack(Material.GOLDEN_CARROT, 1);
		meta = item_noSound.getItemMeta();
		meta.setDisplayName("��f[ ��b�͸��� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��e����� �Ҹ��� ���� �ʽ��ϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item_noSound.setItemMeta(meta);
		
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
		} else if (ingame) {
			if(canSpectate && loc_spectate != null) {
				if(!existPlayer(p)) return;
				MyUtility.allClear(p);
				p.sendMessage(server.ms_alert + "�̹� ������ ���۵Ǿ����ϴ�. ������ҷ� �̵��Ǹ� ���� ���� ä���� ����մϴ�.");
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
				p.sendMessage(server.ms_alert + "�̹� ������ ���۵Ǿ����ϴ�."); 
			}		
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
			p.getInventory().setItem(8, helpItem);
			p.getInventory().setHeldItemSlot(8);
			p.teleport(loc_Join, TeleportCause.PLUGIN);
			sendMessage(server.ms_alert + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " ���� �����߽��ϴ�. " + ChatColor.RESET
					+ "[ " + ChatColor.GREEN + ingamePlayer.size() + " / "+ minPlayer + ChatColor.RESET + " ]");
			TitleAPI.sendTitle(p, 10, 70, 30,ChatColor.RED+""+disPlayGameName);
			sendSound(Sound.BLOCK_NOTE_PLING, 2.0f, 1.0f);
			if(lobbyStart) {
				if(startSch.schTime > 1)
					BarAPI.setMessage(p, ChatColor.GREEN+"���� ���۱���", startSch.schTime);
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
		String rank = "����";
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
				t.sendMessage(ms+"�ּ� �����ο��� �����Ͽ� ������ ��ҵƽ��ϴ�.");
			}
			endGame(false);
			return;
		}
		server.egCM.broadCast(server.ms_alert + ChatColor.RED+ChatColor
				.BOLD+ disPlayGameName + ChatColor.GRAY + "�� ���� �Ǿ����ϴ�!");
		initGame();
		ingame = true;
		gameStep = 1;
		/////////////// ������
		
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
					sendTitle("��6��l"+fallCount.schTime,"��e���� ���۱���", 30);
					sendSound(Sound.BLOCK_NOTE_PLING);
					fallCount.schTime--;
				}else {
					fallCount.cancelTask(true);
					gameStep = 3;
					for(Location l : loc_helidoors) {
						l.getBlock().setType(Material.AIR);
					}
					sendTitle("��6��l�� ����!","��e����� ������ �����ϼ���!!!", 80);
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
								TitleAPI.sendFullTitle(t, 10, 70, 10, "��c��l���� ����", "��e��l�ʹ� �ȶپ���ż� ������ ���ϵ˴ϴ�.");
								t.teleport(t.getLocation().add(0, -10, 0), TeleportCause.PLUGIN);
							}
							t.sendMessage(ms+"�� ���� ���ڰ� ���޵˴ϴ�.");
							sendTitle("","��e�����Ͽ� ���� �ο�� �÷��̴� ���� �����Դϴ�!", 120);
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
					Bukkit.getLogger().info(sch.schTime2+"���� �Ĺ��� ����");
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
		SidebarString line = new SidebarString("��e�����ο� ��f: ��a"+ingamePlayer.size()+"��");
		textList.add(line);
		textList.add(blank);
		line = new SidebarString("��e���� �ڱ��� ���� ��f: ��a"+limiterLv);
		textList.add(line);
		textList.add(blank);
		line = new SidebarString("��e���� �ڱ��� �ݰ� ��f: ��a"+mainSch.schTime2+"m");
		textList.add(line);
		textList.add(blank);
		line = new SidebarString("��e���� ���� �ð� ��f: "+(mainSch.schTime)+"��");
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
									ActionBarAPI.sendActionBar(p, "��c��l���ѱ����� �ֱ⿡ ���ظ� �޽��ϴ�.", 30);
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
							TitleAPI.sendFullTitle(p, 10, 70, 10, "��a��l����̱�", "��e��l���ڸ� ��Ŭ���Ͽ� ���⸦ ��������!.");
							p.sendMessage(ms+"���� �ð��� �������ϴ�. ���ϻ��� �����˴ϴ�.");
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
							t.sendMessage("\n"+ms+"�̹� ������ �ڱ��� ��ġ�� �����ƽ��ϴ�.\n"
									+ ms+"�߽��� ��ǥ : ��a"+limiterCenter.getBlockX()+", "+limiterCenter.getBlockZ()+"��6 | ���� �ݰ� : ��a"+limiterRadius+"m\n"
											+ ms+"1���� �� ������ ���ѵ˴ϴ�.");
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
					sendMessage("\n"+ms+"�� ������ ���ѵǰ� �ֽ��ϴ�!\n");
					sendSound(Sound.BLOCK_METAL_BREAK);
				}
				
				//����
				if(mainSch.schTime == 200) {
					createSupplyChest(70);
				}
				
				if(mainSch.schTime == 270) {					
					sendMessage("\n"+ms+"1���� �� ������ ���ѵ˴ϴ�.\n"
							+ ms+"�߽��� ��ǥ : ��a"+limiterCenter.getBlockX()+", "+limiterCenter.getBlockZ()+"��6 | ���� �ݰ� : ��a"+limiterRadius+"m\n");
					sendSound(Sound.BLOCK_METAL_BREAK);
				}
				if(mainSch.schTime == 330) {
					limiterLv = 2;
					limiterRadius = 75;
					applyBorder = true;
					sendMessage("\n"+ms+"�� ������ ���ѵǰ� �ֽ��ϴ�!\n");
					sendSound(Sound.BLOCK_METAL_BREAK);
				}
				
				//����
				if(mainSch.schTime == 350) {
					createSupplyChest(50);
				}
				
				if(mainSch.schTime == 410) {					
					sendMessage("\n"+ms+"1���� �� ������ ���ѵ˴ϴ�.\n"
							+ ms+"�߽��� ��ǥ : ��a"+limiterCenter.getBlockX()+", "+limiterCenter.getBlockZ()+"��6 | ���� �ݰ� : ��a"+limiterRadius+"m\n");
					sendSound(Sound.BLOCK_METAL_BREAK);
				}
				if(mainSch.schTime == 470) {
					limiterLv = 3;
					limiterRadius = 50;
					applyBorder = true;
					sendMessage("\n"+ms+"�� ������ ���ѵǰ� �ֽ��ϴ�!\n");
					sendSound(Sound.BLOCK_METAL_BREAK);
				}
				
				//����
				if(mainSch.schTime == 530) {
					createSupplyChest(35);
				}
				
				if(mainSch.schTime == 600) {
					sendMessage("\n"+ms+"1���� �� ������ ���ѵ˴ϴ�.\n"
							+ ms+"�߽��� ��ǥ : ��a"+limiterCenter.getBlockX()+", "+limiterCenter.getBlockZ()+"��6 | ���� �ݰ� : ��a"+limiterRadius+"m\n");
					sendSound(Sound.BLOCK_METAL_BREAK);
				}
				if(mainSch.schTime == 660) {
					limiterLv = 4;
					limiterRadius = 25;
					applyBorder = true;
					sendMessage("\n"+ms+"�� ������ ���ѵǰ� �ֽ��ϴ�!\n");
					sendSound(Sound.BLOCK_METAL_BREAK);
				}
				
				//����
				if(mainSch.schTime == 700) {
					createSupplyChest(17);
				}
				
				if(mainSch.schTime == 760) {					
					sendMessage("\n"+ms+"1���� �� ������ ���ѵ˴ϴ�.\n"
							+ ms+"�߽��� ��ǥ : ��a"+limiterCenter.getBlockX()+", "+limiterCenter.getBlockZ()+"��6 | ���� �ݰ� : ��a"+limiterRadius+"m\n");
					sendSound(Sound.BLOCK_METAL_BREAK);
				}
				if(mainSch.schTime == 820) {
					limiterLv = 5;
					limiterRadius = 10;
					applyBorder = true;
					sendMessage("\n"+ms+"�� ������ ���ѵǰ� �ֽ��ϴ�!\n");
					sendSound(Sound.BLOCK_METAL_BREAK);
				}
				if(mainSch.schTime == 900) {					
					sendMessage("\n"+ms+"1���� �� ������ ���ѵ˴ϴ�.\n"
							+ ms+"�߽��� ��ǥ : ��a"+limiterCenter.getBlockX()+", "+limiterCenter.getBlockZ()+"��6 | ���� �ݰ� : ��a"+limiterRadius+"m\n");
					sendSound(Sound.BLOCK_METAL_BREAK);
				}
				if(mainSch.schTime == 960) {
					limiterLv = 6;
					limiterRadius = 0;
					applyBorder = true;
					sendMessage("\n"+ms+"�� ������ ���ѵǰ� �ֽ��ϴ�!\n");
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
									ActionBarAPI.sendActionBar(p, "��c��l���ѱ����� �ֱ⿡ ���ظ� �޽��ϴ�.", 30);
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
							TitleAPI.sendFullTitle(p, 10, 70, 10, "��a��l����̱�", "��e��l���ڸ� ��Ŭ���Ͽ� ���⸦ ��������!.");
							p.sendMessage(ms+"���� �ð��� �������ϴ�. ���ϻ��� �����˴ϴ�.");
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
					
					sendMessage("\n"+ms+"�̹� ������ �ڱ��� ��ġ�� �����ƽ��ϴ�.\n"
							+ ms+"�߽��� ��ǥ : ��a"+limiterCenter.getBlockX()+", "+limiterCenter.getBlockZ()+"��6 | ���� �ݰ� : ��a"+limiterRadius+"m\n"
									+ ms+"1���� �� ������ ���ѵ˴ϴ�.");
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
					sendMessage("\n"+ms+"�� ������ ���ѵǰ� �ֽ��ϴ�!\n");
					sendSound(Sound.BLOCK_METAL_BREAK);
				}
				
				//����
				if(mainSch.schTime == 160) {
					createSupplyChest(70);
				}
				
				if(mainSch.schTime == 200) {					
					sendMessage("\n"+ms+"1���� �� ������ ���ѵ˴ϴ�.\n"
							+ ms+"�߽��� ��ǥ : ��a"+limiterCenter.getBlockX()+", "+limiterCenter.getBlockZ()+"��6 | ���� �ݰ� : ��a"+limiterRadius+"m\n");
					sendSound(Sound.BLOCK_METAL_BREAK);
				}
				if(mainSch.schTime == 260) {
					limiterLv = 2;
					limiterRadius = 80;
					applyBorder = true;
					sendMessage("\n"+ms+"�� ������ ���ѵǰ� �ֽ��ϴ�!\n");
					sendSound(Sound.BLOCK_METAL_BREAK);
				}
				
				//����
				if(mainSch.schTime == 300) {
					createSupplyChest(40);
				}
				
				if(mainSch.schTime == 360) {					
					sendMessage("\n"+ms+"1���� �� ������ ���ѵ˴ϴ�.\n"
							+ ms+"�߽��� ��ǥ : ��a"+limiterCenter.getBlockX()+", "+limiterCenter.getBlockZ()+"��6 | ���� �ݰ� : ��a"+limiterRadius+"m\n");
					sendSound(Sound.BLOCK_METAL_BREAK);
				}
				if(mainSch.schTime == 420) {
					limiterLv = 3;
					limiterRadius = 40;
					applyBorder = true;
					sendMessage("\n"+ms+"�� ������ ���ѵǰ� �ֽ��ϴ�!\n");
					sendSound(Sound.BLOCK_METAL_BREAK);
				}
				
				//����
				if(mainSch.schTime == 450) {
					createSupplyChest(20);
				}
				
				if(mainSch.schTime == 500) {
					sendMessage("\n"+ms+"1���� �� ������ ���ѵ˴ϴ�.\n"
							+ ms+"�߽��� ��ǥ : ��a"+limiterCenter.getBlockX()+", "+limiterCenter.getBlockZ()+"��6 | ���� �ݰ� : ��a"+limiterRadius+"m\n");
					sendSound(Sound.BLOCK_METAL_BREAK);
				}
				if(mainSch.schTime == 560) {
					limiterLv = 4;
					limiterRadius = 20;
					applyBorder = true;
					sendMessage("\n"+ms+"�� ������ ���ѵǰ� �ֽ��ϴ�!\n");
					sendSound(Sound.BLOCK_METAL_BREAK);
				}
				
				//����
				if(mainSch.schTime == 580) {
					createSupplyChest(10);
				}
				
				if(mainSch.schTime == 600) {					
					sendMessage("\n"+ms+"1���� �� ������ ���ѵ˴ϴ�.\n"
							+ ms+"�߽��� ��ǥ : ��a"+limiterCenter.getBlockX()+", "+limiterCenter.getBlockZ()+"��6 | ���� �ݰ� : ��a"+limiterRadius+"m\n");
					sendSound(Sound.BLOCK_METAL_BREAK);
				}
				if(mainSch.schTime == 660) {
					limiterLv = 5;
					limiterRadius = 10;
					applyBorder = true;
					sendMessage("\n"+ms+"�� ������ ���ѵǰ� �ֽ��ϴ�!\n");
					sendSound(Sound.BLOCK_METAL_BREAK);
				}
				if(mainSch.schTime == 700) {					
					sendMessage("\n"+ms+"1���� �� ������ ���ѵ˴ϴ�.\n"
							+ ms+"�߽��� ��ǥ : ��a"+limiterCenter.getBlockX()+", "+limiterCenter.getBlockZ()+"��6 | ���� �ݰ� : ��a"+limiterRadius+"m\n");
					sendSound(Sound.BLOCK_METAL_BREAK);
				}
				if(mainSch.schTime == 760) {
					limiterLv = 6;
					limiterRadius = 0;
					applyBorder = true;
					sendMessage("\n"+ms+"�� ������ ���ѵǰ� �ֽ��ϴ�!\n");
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
					t.sendMessage("\n"+ms+"���޻��ڰ� �����Ǿ����ϴ�!\n"
							+ms+"��6��ǥ : X: ��a"+tmpL.getBlockX()+" ��6Y: ��a"+tmpL.getBlockY()+" ��6Z: ��a"+tmpL.getBlockZ()
							+"\n��e��Ű� ���޻��ڿ��� �Ÿ�  : ��a"+((int)tmpL.distance(t.getLocation()))+"m");
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
				t.sendMessage("\n" + ms + "���޻��ڰ� �����Ǿ����ϴ�!\n" + ms + "��6��ǥ : X: ��a" + l.getBlockX() + " ��6Y: ��a"
						+ l.getBlockY() + " ��6Z: ��a" + l.getBlockZ() + "\n��e��Ű� ���޻��ڿ��� �Ÿ�  : ��a"
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
		p.sendMessage(ms+weaponName+" ���⸦ �̾ҽ��ϴ�.");
		p.getWorld().playSound(p.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0f, 1.0f);
	}

	public void initGame() {
		lobbyStart = false;
		ending = false;
		gameStep = 0;
		//������
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
		Bukkit.getLogger().info(cnt+"���� �Ĺ��� ����");
		
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
			server.egPM.printLog(ms + "[" + disPlayGameName + "] ����� �� ������ �������� �ʾҽ��ϴ�.");
			ret = false;
		}
		if (loc_Join == null) {
			server.egPM.printLog(ms + "[" + disPlayGameName + "] ���� ���� ��� ������ �������� �ʾҽ��ϴ�.");
			ret = false;
		}else {
			loc_spectate = loc_Join.clone();
		}
		if (loc_Helicopter == null) {
			server.egPM.printLog(ms + "[" + disPlayGameName + "] ���� ��� ������ �������� �ʾҽ��ϴ�.");
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
			server.egPM.printLog(ms + "[" + disPlayGameName + "] ������ ������ �������� �ʾҽ��ϴ�.");
			ret = false;
		}

		
		if(ret == true) {
			server.egPM.printLog("[" + disPlayGameName + "] ���� �̻� ����");
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
								p.sendMessage("[" + disPlayGameName + "] " + "���� ���� ��� ������ �����Ǿ����ϴ�.");
							}  else if (cmd[3].equalsIgnoreCase("Helicopter")) {
								saveLocation(gameName, "Helicopter", p.getLocation());
								p.sendMessage("[" + disPlayGameName + "] " + p.getLocation()+" �����Ϸ�");
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
								p.sendMessage(ms + "[" + disPlayGameName + "] " + doorPosCnt + " ��° ����� �� ������ �߰��Ǿ����ϴ�.");
							}else if (cmd[3].equalsIgnoreCase("setItemLoc1")) {
								tmpItemPos1 = p.getLocation();
								p.sendMessage(ms+"/rww set lco setitemloc2 �Է��ؼ� ���� ����");
							} else if (cmd[3].equalsIgnoreCase("setItemLoc2")) {
								if (tmpItemPos1 == null)
									p.sendMessage(ms + "1������ ���� ����");
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

									p.sendMessage(ms + cnt + "���� ������ ���� �����Ϸ�");
								}								
							} else {
								p.sendMessage("[" + disPlayGameName + "] " + cmd[3]+"�� �ùٸ��� ���� �μ�");
							}  
							loadGameData();
						} else {
							p.sendMessage(ms+"�μ��� �Է����ּ���.");
						}
					} else {
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" set loc join - ���� ���� ��� ���� ����");
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" set loc helicopter - ���� ��� ���� ����");
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" set loc helidoor - ����� �� ���� ����");
					}
				} else {
					p.sendMessage("[" + disPlayGameName + "] " + cmdMain + " set loc- ���� ���� ����");
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
				server.playerList.put(p.getName(), "�׽�Ʈ");
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
				p.sendMessage(ms+cnt+"��");
			}
		} else {
			p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" join - ���� ����");
			p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" quit - ���� ����");
			p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" set - ���� ����");
		}
	}

	public void gameQuitPlayer(Player p, boolean announce, boolean giveGold) {
		if (ingamePlayer.contains(p.getName())) {
			ingamePlayer.remove(p.getName());
			server.playerList.put(p.getName(), "�κ�");
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
						sendMessage(ms + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " ���� Ż���ϼ̽��ϴ�.");
						sendTitle("", ChatColor.YELLOW+p.getName()+"�� Ż��", 60);
						sendSound(Sound.BLOCK_NOTE_CHIME,1.5f, 2.0f);
					}
				} else {
					if(announce) {
						sendMessage(ms + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " ���� �����ϼ̽��ϴ�.");
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
					sendMessage(ms + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " ���� �����ϼ̽��ϴ�. "
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
		
		//MMR å��
			int mmr = calcMMR(rwwP);
			playerRwwData.setMMR(playerRwwData.getMMR() + mmr);
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p)) {
				p.sendMessage("");
				p.sendMessage(ms+"��f"+disPlayGameName+"��f�� ���� ����� ��a"+mmr+"��f���� �ݿ��ƽ��ϴ�.");
				p.sendMessage(ms+"��fų�� : ��a"+rwwP.kill+"ų");
				p.sendMessage(ms+"��f���� : ��a"+rwwP.ranking+"��");
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
			sendTitle("��6��l�¸�", ChatColor.GRAY + "1�� �޼�! ���������� �����߽��ϴ�!", 70);
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
							server.ms_alert + ChatColor.GRAY  + "��c"+winner+"��7���� ��a�¸���7�� "+disPlayGameName+"�� ���� �Ǿ����ϴ�.");
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
							server.ms_alert + ChatColor.GRAY  + "���ºη� ��c���� ���� �����7�� ���� �Ǿ����ϴ�.");
					}
				}, 200L);
			} catch(Exception e) {
					endGame(true);
			}
		}
		
	
	}

	public void endGame(boolean force) {
		if(force) {
			server.egCM.broadCast(server.ms_alert + "���� ���� ���� ������ ���� ���� �Ǿ����ϴ�.");
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
		p.sendMessage(ms+"����� ���� �̵� �ӵ� -> "+MyUtility.getTwoRound(p.getWalkSpeed()*5));
	}
	
	public void healthUp(Player p, int amt) {
		MyUtility.setMaxHealth(p, p.getMaxHealth()+amt);
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
		p.sendMessage(ms+"����� ���� �ִ� ü�� -> "+p.getMaxHealth());
	}
	
	public void makeSupplyItems() {
		
		//supplyItems.add(new ItemStack(Material.DIAMOND_SWORD, 1));
		supplyItems.add(new ItemStack(Material.DIAMOND_HELMET, 1));
		supplyItems.add(new ItemStack(Material.DIAMOND_CHESTPLATE, 1));
		supplyItems.add(new ItemStack(Material.DIAMOND_LEGGINGS, 1));
		supplyItems.add(new ItemStack(Material.DIAMOND_BOOTS, 1));
		
		ItemStack item = new ItemStack(Material.POTION, 1); 
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��c���� ���� ��f]");
		List<String> loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��e���� ��� ü���� ��� ȸ���Ѵ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		
		supplyItems.add(item);
		
		item = new ItemStack(Material.BEETROOT_SEEDS, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��c�溮(��) ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��e���� 5�ʰ� �� 48�� ��´�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		
		supplyItems.add(item);
		
	}
	
	public void makefarmingItems() {
		farmingItemsMap.put(new ItemStack(Material.LEATHER_HELMET, 1), 3); //���׼� 12%
		farmingItemsMap.put(new ItemStack(Material.LEATHER_CHESTPLATE, 1), 3);
		farmingItemsMap.put(new ItemStack(Material.LEATHER_LEGGINGS, 1), 3);
		farmingItemsMap.put(new ItemStack(Material.LEATHER_BOOTS, 1), 3);
		
		farmingItemsMap.put(new ItemStack(Material.IRON_HELMET, 1), 2); //ö�� 8%
		farmingItemsMap.put(new ItemStack(Material.IRON_CHESTPLATE, 1), 2);
		farmingItemsMap.put(new ItemStack(Material.IRON_LEGGINGS, 1), 2);
		farmingItemsMap.put(new ItemStack(Material.IRON_BOOTS, 1), 2);
		
		farmingItemsMap.put(new ItemStack(Material.GOLD_HELMET, 1), 3);//�ݼ� 12%
		farmingItemsMap.put(new ItemStack(Material.GOLD_CHESTPLATE, 1), 3);
		farmingItemsMap.put(new ItemStack(Material.GOLD_LEGGINGS, 1), 3);
		farmingItemsMap.put(new ItemStack(Material.GOLD_BOOTS, 1), 3);
		
		farmingItemsMap.put(new ItemStack(Material.CHAINMAIL_HELMET, 1), 2); //�罽�� 8%
		farmingItemsMap.put(new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1), 2);
		farmingItemsMap.put(new ItemStack(Material.CHAINMAIL_LEGGINGS, 1), 2);
		farmingItemsMap.put(new ItemStack(Material.CHAINMAIL_BOOTS, 1), 2);
		
		farmingItemsMap.put(new ItemStack(Material.POTION, 1, (short)16389), 4); //��ô ���� 8%
		farmingItemsMap.put(new ItemStack(Material.POTION, 1, (short)16421), 4);
		
		farmingItemsMap.put(new ItemStack(Material.GOLD_SWORD, 1), 3);//�˷�
		farmingItemsMap.put(new ItemStack(Material.IRON_SWORD, 1), 3);
		
		farmingItemsMap.put(item_grenade, 4);//����ź
		farmingItemsMap.put(item_flashbang, 4);//����ź
		
		ItemStack item = new ItemStack(Material.POTION, 1); //�帵ũ�� 9%
		PotionMeta ptMeta = (PotionMeta) item.getItemMeta();
		ptMeta.setDisplayName("��f[ ��c��Ÿ�� ��f]");
		List<String> loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��e���� ü�� 15�� ������ ȸ���Ѵ�.");
		loreList.add("��c�����ִ� ���� ȿ���� ������ �ȵ˴ϴ�.");
		loreList.add("");
		ptMeta.setBasePotionData(new PotionData(PotionType.JUMP));
		ptMeta.setLore(loreList);
		item.setItemMeta(ptMeta);
		
		farmingItemsMap.put(item, 4);
		
		item = new ItemStack(Material.POTION, 1);
		ptMeta = (PotionMeta) item.getItemMeta();
		ptMeta.setDisplayName("��f[ ��c������ ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��e���� ü�� 30�� ������ ȸ���Ѵ�.");
		loreList.add("��c�����ִ� ���� ȿ���� ������ �ȵ˴ϴ�.");
		loreList.add("");
		ptMeta.setBasePotionData(new PotionData(PotionType.NIGHT_VISION));
		ptMeta.setLore(loreList);
		item.setItemMeta(ptMeta);
		
		farmingItemsMap.put(item, 3);
		
		item = new ItemStack(Material.POTION, 1);
		ptMeta = (PotionMeta) item.getItemMeta();
		ptMeta.setDisplayName("��f[ ��c�Ƶ巹���� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��e���� ü�� 70�� ������ ȸ���Ѵ�.");
		loreList.add("��c�����ִ� ���� ȿ���� ������ �ȵ˴ϴ�.");
		loreList.add("");
		ptMeta.setBasePotionData(new PotionData(PotionType.FIRE_RESISTANCE));
		ptMeta.setLore(loreList);
		item.setItemMeta(ptMeta);
		
		farmingItemsMap.put(item, 2);
		
		//////////////////////////////////////////////// ��ȸ
		item = new ItemStack(Material.CARPET, 1, (byte)4);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��c��â�� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��e���� ü�� 10�� ��� ȸ���Ѵ�.");
		loreList.add("��e��, �ִ� ü���� 75% �̻��� ȸ���� �Ұ����ϴ�.");
		loreList.add("");
		loreList.add("��b��뿡 �ʿ��� �ð� : ��a2��");
		loreList.add("��b����Ʈ + ��Ŭ�� - ��a������ ���");
		loreList.add("��b����Ʈ ���� - ��a������ ��� ���");
		loreList.add("��c���� ����Ʈ�� ������ �־�� ���˴ϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		//item.addUnsafeEnchantment(Enchantment.LUCK, 0);
		
		farmingItemsMap.put(item, 5);
		
		item = new ItemStack(Material.LONG_GRASS, 1, (byte)1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��c�ش� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��e���� ü�� 20�� ��� ȸ���Ѵ�.");
		loreList.add("��e��, �ִ� ü���� 75% �̻��� ȸ���� �Ұ����ϴ�.");
		loreList.add("");
		loreList.add("��b��뿡 �ʿ��� �ð� : ��a3��");
		loreList.add("��b����Ʈ + ��Ŭ�� - ��a������ ���");
		loreList.add("��b����Ʈ ���� - ��a������ ��� ���");
		loreList.add("��c���� ����Ʈ�� ������ �־�� ���˴ϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		//item.addUnsafeEnchantment(Enchantment.LUCK, 0);
		
		farmingItemsMap.put(item, 4);
		
		item = new ItemStack(Material.REDSTONE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��c������ ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��e���� ü�� 40�� ��� ȸ���Ѵ�.");
		loreList.add("��e��, �ִ� ü���� 75% �̻��� ȸ���� �Ұ����ϴ�.");
		loreList.add("");
		loreList.add("��b��뿡 �ʿ��� �ð� : ��a3��");
		loreList.add("��b����Ʈ + ��Ŭ�� - ��a������ ���");
		loreList.add("��b����Ʈ ���� - ��a������ ��� ���");
		loreList.add("��c���� ����Ʈ�� ������ �־�� ���˴ϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		//item.addUnsafeEnchantment(Enchantment.LUCK, 0);
		
		farmingItemsMap.put(item, 3);
		
		item = new ItemStack(Material.ENCHANTMENT_TABLE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��c�޵�Ŷ ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��e���� ü�� 75�� ��� ȸ���Ѵ�.");
		loreList.add("��e��, �ִ� ü���� 75% �̻��� ȸ���� �Ұ����ϴ�.");
		loreList.add("");
		loreList.add("��b��뿡 �ʿ��� �ð� : ��a4��");
		loreList.add("��b����Ʈ + ��Ŭ�� - ��a������ ���");
		loreList.add("��b����Ʈ ���� - ��a������ ��� ���");
		loreList.add("��c���� ����Ʈ�� ������ �־�� ���˴ϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		//item.addUnsafeEnchantment(Enchantment.LUCK, 0);
		
		farmingItemsMap.put(item, 3);
		
		//////////�ֹ���
		
		item = new ItemStack(Material.RABBIT_FOOT, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��b�̵� �ӵ� �ֹ��� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��e���� �̵��ӵ���");
		loreList.add("��e5%��ŭ ������ ������Ų��.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		
		farmingItemsMap.put(item, 4);
		
		item = new ItemStack(Material.BEETROOT, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��bü�� �ֹ��� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��e���� ü���� 1��ŭ ������ ������Ų��.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		
		farmingItemsMap.put(item, 4);
		
		/////�溮��
		item = new ItemStack(Material.PUMPKIN_SEEDS, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��c�溮(��) ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��e���� 5�ʰ� �� 8�� ��´�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		
		farmingItemsMap.put(item, 3);
		
		item = new ItemStack(Material.MELON_SEEDS, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��c�溮(��) ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��e���� 5�ʰ� �� 12�� ��´�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		
		farmingItemsMap.put(item, 2);
		
		item = new ItemStack(Material.RABBIT_HIDE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��c���� ��û �ڵ� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��e���� �ڽ��� ��ġ�� ���޻��ڸ� ��û�Ѵ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		
		farmingItemsMap.put(item, 1);
		
	}
	
	public void makeWeapons() {

		SpecialWeapon weapon = new PathFinder(this); //1. ��ġ Ž����
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new RandomSword(this); //2.õ�� ��
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new IronFeather(this); //3.�׸����� ����
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new Booster(this); //4.�ν��� ����
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new Levitator(this); //5.���߷� ��ġ
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new HappyBowl(this); //6.�ູ ���� �׸�
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new LocationSaver(this); //7.��ġ ��ϱ�
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new WizardRod(this); //8.�������� ������
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new WizardPowder(this); //9.�������� �Ŀ�ġ
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new PoisonKnife(this); //10.���� ������
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new IcePrison(this); //11. ���뿵��
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new TeleportStick(this); //12. �����̵� �����
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new ReflectorMirror(this); //13. �ݻ� �ſ�
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new Invincibler(this); //14. �ݰ�  ����
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new Blinder(this); //15. ���Ҽ� ���
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new DeadRevive(this); //16. �������� �һ�
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new DNAChanger(this); //17. ���� ���۱�
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new NoDebuff(this); //18. õ�� ���ڷ�
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new RandomBuffer(this); //19. �̽ʻ��ü �ֻ���
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new StopWatch(this); //20. ��ž��ġ
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new WindShooter(this); //21. ��ǳ ��ä
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new ForceTaker(this); //22. ��Ż ���
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new GodokSword(this); //23. ���� ��
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new SnipeRifle(this); //24. ������
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new Machingun(this); //25. �ӻ��
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new NoGravityBow(this); //26. ���� ����
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new GlowingDust(this); //27. ���� ����
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new SlimeJumper(this); //28. ���������� ��ü
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new ParkourBook(this); //29. ���� ������
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new DeadDefense(this); //30. �һ��� Ȳ�ݱ�
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new IronHook(this); //31. ö�� ����
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new AirCannon(this); //32. ������
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new GravityController(this); //33. ���߷� ������
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new DragonRelic(this); //34. �巡�� ����
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new C4Bomber(this); //35. C4
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new FlickerBook(this); //36. ���� �ֹ���
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new MurderSword(this); //37. �������� Į
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new Spoon(this); //38. ������
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new RoseKnife(this); //39. ���Į
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new DragonSword(this); //40. �뱤��
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new AiasShield(this); //41. ���̾ƽ��� ����
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new Telephone512K(this); //42. 512k
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new FastBow(this); //43. �����ε� ����
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new MagnetWeapon(this); //44. �ڼ�
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new WitherKnife(this); //45. ���� ������
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new OnePunch(this); //46. ����ġ �۷���
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new LocationSwitcher(this); //47. ��ġ ��ȯ��
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new RollerSkate(this); //48. �ѷ� ������Ʈ
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new ThunderAxe(this); //49. ����Ʈ�� ����
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new WizardUltimate(this); //50. ���� ��
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new AvoidAxe(this); //51. ȸ�� ����
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new ThrowBomb(this); //52. ����ź
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new BigHammer(this); //53. ���ġ
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new LocationHolder(this); //54. ���� ���� ��ġ
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new MoveToPlayerWeapon(this); //55. �����ӽ�
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new EscapeWeapon(this); //56. ��� Ż�� ��ġ
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new DeathNote(this); //57. ������Ʈ
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new Stimulant(this); //58. ������
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new DiscoBangBang(this); //59. ���� ����
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new CreeperWeapon(this); //60. ���� 
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new Nomeither(this); //61. ��̴�
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new RandomMedic(this); //62. ���� ���� 
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new BurnOut(this); //63. ���ƿ�
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new Mirroring(this); //64. �̷���	
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new DrainStone(this); //65. ��ȯ�� ��
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new BorrowHealth(this); //66. �̷������� 
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new ShootFireball(this); //67. ȭ����
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new OdinRod(this); //68. ������ ������
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
		
		weapon = new RandomAxe(this); //69. ���ڵ���
		specialWeaponMap.put(weapon.getDisplayName(), weapon);
	}
	
	//�溮 ����
	public void giveAbsorp(Player p, int lv) {
		p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 2.0f);
		p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 100 ,lv));
	}
	
	
	//�� ����
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
					String symbol = "��";
					String symbolBlank = "��";
					String barStr = "��b��l�����: ";
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
						p.sendMessage(ms+"��c�̹� �ִ� ü���� 75% �̻��Դϴ�.");
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

	//////////////// �̺�Ʈ
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
			if(ingamePlayer.contains(p.getName())) { //���� �����ؾ����� ������ ��ɾ�
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
				        if (cause.equals(DamageCause.VOID)) { //���� ������, ����		           
				            p.teleport(loc_Join, TeleportCause.PLUGIN);
				        }
					} else {
						if(gameStep != 4) {
							if(gameStep == 3) {
								if(cause.equals(DamageCause.FALL)){
									//if(p.getFallDistance() > 50) {					
										//e.setDamage(10);
										//p.sendMessage("��6���ϻ��� ��ġ�� �ʾ� ���� ���ظ� �޾ҽ��ϴ�.");
									//}else {
										e.setCancelled(true);
									//}
								}else {
									e.setCancelled(true);
								}
							}else {
								e.setCancelled(true);
								if (cause.equals(DamageCause.VOID)) { //����� = Ż��
						            p.teleport(loc_Join, TeleportCause.PLUGIN);
								}
							}	
				        }else {
				        	if (cause.equals(DamageCause.VOID)) { //����� = Ż��
					            gameQuitPlayer(p, true, true);
					            server.spawn(p);
							}else {
								String itemName = getHeldMainItemName(p);
								if(itemName != null && !itemName.equalsIgnoreCase("meta����")) {
									SpecialWeapon weapon = allWeapon.get(itemName);
									if(weapon != null) {
										weapon.onEntityDamaged(e);
									}
								}	
								String offItemName = getHeldOffItemName(p);
								if(offItemName != null && !offItemName.equalsIgnoreCase("meta����")) {
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
			if(e.getEntity() instanceof Player && ingame) { //���ӽ�����������
				
				Player player = (Player) e.getEntity();
				Player damager = null;

				if (!ingamePlayer.contains(player.getName())) return; //�����ڰ� �׿� ���������������� ��Ģ ����
				
				/*if(!checkClickDelay(player.getName())) {
					e.setCancelled(true);
					return;
				}*/
				
				
				boolean isProjectile = false;
				
				Arrow arrow = null;

				if (e.getDamager() instanceof Snowball) { //ȭ��� �Ѿ˿����� ������ ����
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
				
				if (damager == null) //������ ������ ����
					return;
				
				if (!ingamePlayer.contains(damager.getName())) { //�����ڰ� �׿� ���������������� ��Ģ ����
					return;
				}
				
				if(!isProjectile && !checkVictimDelay(damager.getName(), player)) {
					e.setCancelled(true);
					//Bukkit.broadcastMessage("����");
					return;
				}
				
				String damager_itemName = getHeldMainItemName(damager);
				if(damager_itemName != null && !damager_itemName.equalsIgnoreCase("meta����")) {
					SpecialWeapon weapon = allWeapon.get(damager_itemName);
					if(weapon != null) {
						if(!isProjectile)e.setDamage(weapon.getCalcDamage());
						weapon.onHitPlayer(e, damager, player);
					}
				}
				
				String victim_itemName = getHeldMainItemName(player);
				if(victim_itemName != null && !victim_itemName.equalsIgnoreCase("meta����")) {
					SpecialWeapon weapon = allWeapon.get(victim_itemName);
					if(weapon != null) {
						weapon.onHitted(e, damager, player);
					}
				}
				
				String victim_offItemName = getHeldOffItemName(player);
				if(victim_offItemName != null && !victim_offItemName.equalsIgnoreCase("meta����")) {
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
			if (e.getInventory().getTitle().equalsIgnoreCase("��0��l"+inventoryGameName+" �����")) {				
				e.setCancelled(true);
			if (!ingamePlayer.contains(p.getName()))
				return;
				/*if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
					p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0F, 3.0F);
					return;
				}	
				gameHelper(p, e.getSlot());
			} else if (e.getInventory().getTitle().equalsIgnoreCase("��0��l���Ӽ���")) {
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
			
			if (e.getItem() == null || e.getItem().getType() == Material.AIR) { //�������� �ȵ�� ��Ŭ�������� ����
				return;
			} else if (getHeldMainItemName(p).equalsIgnoreCase("��f[ ��6���� ����� ��f]")) {
				p.openInventory(inven_gameHelper);
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 2.0f);
			} else if (getHeldMainItemName(p).equalsIgnoreCase("��f[ ��b���� ���� ��f]")) {
				e.setCancelled(true);
				getRandomWeapon(p);
				removeItem(p, e.getItem().getType(), 1);
			} else if (getHeldMainItemName(p).equalsIgnoreCase("��f[ ��b�̵� �ӵ� �ֹ��� ��f]")) {
				speedUp(p, 0.01f);
				removeItem(p, e.getItem().getType(), 1);
			}else if (getHeldMainItemName(p).equalsIgnoreCase("��f[ ��bü�� �ֹ��� ��f]")) {
				healthUp(p, 2);
				removeItem(p, e.getItem().getType(), 1);
			}else if (getHeldMainItemName(p).equalsIgnoreCase("��f[ ��c�溮(��) ��f]")) {
				giveAbsorp(p, 1);
				removeItem(p, e.getItem().getType(), 1);
			}else if (getHeldMainItemName(p).equalsIgnoreCase("��f[ ��c�溮(��) ��f]")) {
				giveAbsorp(p, 2);
				removeItem(p, e.getItem().getType(), 1);
			}else if (getHeldMainItemName(p).equalsIgnoreCase("��f[ ��c�溮(��) ��f]")) {
				giveAbsorp(p, 11);
				removeItem(p, e.getItem().getType(), 1);
			}else if (getHeldMainItemName(p).equalsIgnoreCase("��f[ ��c��â�� ��f]")) {
				useHealItem(p, e.getItem(), 1, 15);
			}else if (getHeldMainItemName(p).equalsIgnoreCase("��f[ ��c�ش� ��f]")) {
				useHealItem(p, e.getItem(), 2, 30);
			}else if (getHeldMainItemName(p).equalsIgnoreCase("��f[ ��c������ ��f]")) {
				useHealItem(p, e.getItem(), 2, 50);
			}else if (getHeldMainItemName(p).equalsIgnoreCase("��f[ ��c�޵�Ŷ ��f]")) {
				useHealItem(p, e.getItem(), 3, 90);
			}else if (getHeldMainItemName(p).equalsIgnoreCase("��f[ ��c���� ��û �ڵ� ��f]")) {
				callSupply(p, p.getLocation().getBlock().getLocation());
				removeItem(p, e.getItem().getType(), 1);
			}else if (getHeldMainItemName(p).equalsIgnoreCase("��f[ ��b�͸��� ��f]")) {
				noSoundList.add(p.getName());
				removeItem(p, e.getItem().getType(), 1);
			}else {
				String itemName = getHeldMainItemName(p);
				if(itemName != null && !itemName.equalsIgnoreCase("meta����")) {
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
				String str = game.ms+p.getName()+" >> ��6"+e.getMessage();
				server.egCM.sendMessagesToStringList(ingamePlayer, p, "��f[ ��e"+rankMap.get(p.getName())+ " ��f] ��7"+p.getName()+" >> " +e.getMessage(),false);
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
				///��ġ�̵� �ƴϸ� ĵ��
				String itemName = getHeldMainItemName(p);
				if(itemName != null && !itemName.equalsIgnoreCase("meta����")) {
					SpecialWeapon weapon = allWeapon.get(itemName);
					if(weapon != null) {
						weapon.onPlayerMove(e);
					}
				}
				String offItemName = getHeldOffItemName(p);
				if(offItemName != null && !offItemName.equalsIgnoreCase("meta����")) {
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
			if(itemName != null && !itemName.equalsIgnoreCase("meta����")) {
				SpecialWeapon weapon = allWeapon.get(itemName);
				if(weapon != null) {
					weapon.onRegainHealth(e);
				}
			}
			String offItemName = getHeldOffItemName(p);
			if(offItemName != null && !offItemName.equalsIgnoreCase("meta����")) {
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
				if(itemName != null && !itemName.equalsIgnoreCase("meta����")) {
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
				if(s.equalsIgnoreCase("��f[ ��c��Ÿ�� ��f]")) {
					slowHeal(p, 15);
				} else if(s.equalsIgnoreCase("��f[ ��c������ ��f]")) {
					slowHeal(p, 30);
				} else if(s.equalsIgnoreCase("��f[ ��c�Ƶ巹���� ��f]")) { //������
					slowHeal(p, 70);
				} else if(s.equalsIgnoreCase("��f[ ��c���� ���� ��f]")) { //������
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EAT, 1.0f, 0.1f);
					p.setHealth(p.getMaxHealth());
				} 
			}
			if(remove) {
				item.setType(Material.AIR);
				e.setItem(item);
			}else {
				e.setCancelled(true);
				p.sendMessage(ms+"�̹� ü���� 75% �̻��Դϴ�.");
			}
			
		}
		
		@EventHandler
		public void onPlayerDropItem(PlayerDropItemEvent e) {
			
			Player p = e.getPlayer();
			
			if(!ingamePlayer.contains(p.getName())) return;
			
			if(gameStep != 4) e.setCancelled(true);
			if(e.getItemDrop().getItemStack().getType() == Material.WEB) {
				p.sendMessage(ms+"���ϻ��� ���� �� �����ϴ�.");
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
				
				if(prevItemName != null && !prevItemName.equalsIgnoreCase("meta����")) {
					SpecialWeapon weapon = allWeapon.get(prevItemName);
					if(weapon != null) {
						weapon.onRelease(p);
					}
				}
				
				if(newItem != null && !newItem.equalsIgnoreCase("meta����")) {
					SpecialWeapon weapon = allWeapon.get(newItem);
					if(weapon != null) {
						weapon.onHeld(p);
					}
				}
			}
			
		}*/
	}
	
	///////////////////// ���� ���� ���￡ ������ �÷��̾�� Ŭ����
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
	
