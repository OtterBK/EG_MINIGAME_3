package Minigames.BuildBattle;

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
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;

import com.coloredcarrot.api.sidebar.Sidebar;
import com.coloredcarrot.api.sidebar.SidebarString;
import com.connorlinfoot.titleapi.TitleAPI;

import EGServer.EGServer;
import EGServer.DataManger.PlayerData;
import EGServer.ServerManager.EGEventHandler;
import EGServer.ServerManager.EGScheduler;
import Minigames.Minigame;
import Utility.MyUtility;
import Utility.RepairMap;
import me.confuser.barapi.BarAPI;

public class BuildBattle extends Minigame{
	// �̺�Ʈ��
	public EventHandlerBBT event;
	
	public String ms = "��7[��6��ü��7] ";

	///////////// private
	// ���� �÷��̾� ����Ʈ
	//private HashMap<String, BdbPlayer> playerMap = new HashMap<String, BdbPlayer>();
	private String cmdMain;
	private ItemStack item_score1;
	private ItemStack item_score2;
	private ItemStack item_score3;
	private ItemStack item_score4;
	private ItemStack item_score5;
	//////// ���� ����
	
	public int gameStep = 0;
	public EGScheduler mainSch;
	public List<Location> loc_Start = new ArrayList<Location>(12);
	public String topic = "";
	public List<String> topicList = new ArrayList<String>();
	public String screenedpName = "";
	public HashMap<String, Integer> scoreMap = new HashMap<String, Integer>();
	public List<String> reConnectList = new ArrayList<String>();
	public HashMap<String, Integer> giverMap = new HashMap<String, Integer>();
	public List<Material> banBlock = new ArrayList<Material>();
	public List<Material> banItem = new ArrayList<Material>();
	
	////// ���� �κ��丮
	public Inventory inven_gameHelper;
	
	//������ ��
	public List<RepairMap> mapList = new ArrayList<RepairMap>();
	public String locPath;
	private int lastGamePlayerCnt = 0;
	
	//////// ���̵��
	private Sidebar bdbSidebar;
	private List<SidebarString> textList = new ArrayList<SidebarString>(5);
	
	public BuildBattle(EGServer server, String cmdMain) {

		//////////////////// �ʼ� ������
		super(server);
		
		this.cmdMain = cmdMain;
		
		ms = "��7[ ��e! ��7] ��f: ��c���� ���׽�Ʈ ��f>> "; // �⺻ �޼���
		gameName = "BuildBattle";
		disPlayGameName = ChatColor.RED+"���� ���׽�Ʈ";
		inventoryGameName = ChatColor.stripColor(disPlayGameName);
		minPlayer = 4;
		maxPlayer = 12;
		startCountTime = 100;
		//������ �� ����
		locPath = server.getDataFolder().getPath() + "/" + gameName + "/Location";
		for(int i = 0; i < 12; i++) {
			mapList.add(new RepairMap(server, "area"+i));	
		}	
		doneSetting = loadGameData(); // ���� ���� ��ġ ��� �ε�	
		
		/////////////////////// �ڵ� ����(�����۵��)
		dirSetting("BuildBattle");
		////////////////
		
		//map.loadData(locPath);
		
		mainSch = new EGScheduler(this);
		
		SidebarString tmpLine = new SidebarString("");
		bdbSidebar = new Sidebar("��f[ ��6���� ��Ȳ ��f]", server, 600, tmpLine);
		
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
		loreList.add("��7������ �´� ���๰�� ���� ��");
		loreList.add("��7�ɻ� �ð��� ���� ���� ������ ������ �¸��մϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_gameHelper.setItem(10, item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7- ��c������ ��7-");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7������ ���۵Ǹ� ������ �־����ϴ�.(��: �︮����)");
		loreList.add("��75�а� �ش� ������ �´� ���๰�� ����ϴ�.");
		loreList.add("��7���� �ð��� ������ ������ �÷��̾���� ���� �ɻ縦 �����մϴ�.");
		loreList.add("��7�ɻ縦 ���� ���� ���� ������ ������ �¸��մϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_gameHelper.setItem(11, item);
		
		item_score1 = new ItemStack(Material.WOOL, 1, (byte)14);
		meta = item_score1.getItemMeta();
		meta.setDisplayName("��7[ ��c1�� ��7]");
		loreList.add("");
		loreList.add("��7��Ŭ���� �ش� ������ �ݴϴ�.");
		meta.setLore(loreList);
		item_score1.setItemMeta(meta);
		
		item_score2 = new ItemStack(Material.WOOL, 1, (byte)1);
		meta = item_score2.getItemMeta();
		meta.setDisplayName("��7[ ��c2�� ��7]");
		loreList.add("");
		loreList.add("��7��Ŭ���� �ش� ������ �ݴϴ�.");
		meta.setLore(loreList);
		item_score2.setItemMeta(meta);
		
		item_score3 = new ItemStack(Material.WOOL, 1, (byte)4);
		meta = item_score3.getItemMeta();
		meta.setDisplayName("��7[ ��c3�� ��7]");
		loreList.add("");
		loreList.add("��7��Ŭ���� �ش� ������ �ݴϴ�.");
		meta.setLore(loreList);
		item_score3.setItemMeta(meta);
		
		item_score4 = new ItemStack(Material.WOOL, 1, (byte)5);
		meta = item_score4.getItemMeta();
		meta.setDisplayName("��7[ ��c4�� ��7]");
		loreList.add("");
		loreList.add("��7��Ŭ���� �ش� ������ �ݴϴ�.");
		meta.setLore(loreList);
		item_score4.setItemMeta(meta);
		
		item_score5 = new ItemStack(Material.WOOL, 1, (byte)11);
		meta = item_score5.getItemMeta();
		meta.setDisplayName("��7[ ��c5�� ��7]");
		loreList.add("");
		loreList.add("��7��Ŭ���� �ش� ������ �ݴϴ�.");
		meta.setLore(loreList);
		item_score5.setItemMeta(meta);
		
		event = new EventHandlerBBT(server, this);
		// �� �÷����ο� �̺�Ʈ ����
		server.getServer().getPluginManager().registerEvents(event, server);
		
		setTopic();
		setBanBlock();
		setBanItem();
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
			if(reConnectList.contains(p.getName())) {
				ingamePlayer.add(p.getName());
				moveToMyArea(p);
				sendMessage(ms+p.getName()+" ���� ������ �ϼ̽��ϴ�.");
				sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 0.1f);
				if(gameStep == 3) p.setGameMode(GameMode.ADVENTURE);
				else p.setGameMode(GameMode.CREATIVE);
				bdbSidebar.showTo(p);
				server.playerList.put(p.getName(), disPlayGameName);
				server.spawnList.remove(p.getName());
				server.waitingPlayer.remove(p.getName());
			} else {
				if(canSpectate && loc_spectate != null) {
					MyUtility.allClear(p);
					p.sendMessage(server.ms_alert + "�̹� ������ ���۵Ǿ����ϴ�. ������ҷ� �̵��Ǹ� ���� ���� ä���� ����մϴ�.");	
					p.teleport(loc_spectate, TeleportCause.PLUGIN);
					server.specList.put(p.getName(), this);
					p.setGameMode(GameMode.SPECTATOR);
					p.setFlying(true);
				} else {
					p.sendMessage(server.ms_alert + "�̹� ������ ���۵Ǿ����ϴ�."); 
				}
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
			server.spawnList.remove(p.getName());
			server.playerList.put(p.getName(), disPlayGameName);
			p.getInventory().setItem(8, helpItem);
			p.getInventory().setHeldItemSlot(8);
			p.teleport(loc_Join, TeleportCause.PLUGIN);
			sendMessage(server.ms_alert + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " ���� �����߽��ϴ�. " + ChatColor.RESET
					+ "[ " + ChatColor.GREEN + ingamePlayer.size() + " / "+ minPlayer + ChatColor.RESET + " ]");
			p.sendMessage(ms+"�� ������ �ʿ��� �����Դϴ�. ���� ���ð��� 100���Դϴ�.");
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
		}
	}
	
	@Override
	public void startCount() {
		if(ingame) return;
		lobbyStart = true;
		startSch.cancelTask(true);
		startSch.schTime = startCountTime;
		if(lastGamePlayerCnt == 0) lastGamePlayerCnt = maxPlayer;
		for(int i = 0; i < lastGamePlayerCnt; i++) {
			RepairMap map = mapList.get(i);
			map.Repair(startCountTime-10);
		}
		server.egCM.broadCast(server.ms_alert + ChatColor.RED+ChatColor.BOLD + disPlayGameName + ChatColor.GRAY + " ������ " + ChatColor.GRAY + "�� ���۵˴ϴ�.");
		sendBossbar(ChatColor.GREEN+"���� ���۱���", startSch.schTime);
		startSch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				if (startSch.schTime > 0) {
					if (startSch.schTime == 30 || startSch.schTime == 10) {
						sendMessage(server.ms_alert + ChatColor.GRAY + "������ "
								+ ChatColor.AQUA + startSch.schTime + ChatColor.GRAY + "�� �� ���۵˴ϴ�.");
						sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
					}
					startSch.schTime -= 1;
				} else {
					ingame = true;
					lobbyStart = false;
					startSch.cancelTask(true);
					playerSet();
					startGame();
				}
			}
		}, 0L, 20L);
	}

	@Override
	public void startGame() {
		if(ingamePlayer.size() <= 3) {
			for(String pName : ingamePlayer) {
				Player t = Bukkit.getPlayer(pName);
				server.spawn(t);
				t.sendMessage(ms+"�ּ� �����ο��� �����Ͽ� ������ ��ҵƽ��ϴ�.");
			}
			endGame(false);
			return;
		}
		server.egCM.broadCast(server.ms_alert + ChatColor.RED+ChatColor
				.BOLD+ "���� ���׽�Ʈ " + ChatColor.GRAY + "�� ���� �Ǿ����ϴ�!");
		initGame();
		ingame = true;
		gameStep = 1;
		/////////////// ������	
		sendSound(Sound.BLOCK_NOTE_BELL, 1.0f, 1.0f);
		reConnectList.addAll(ingamePlayer);
		lastGamePlayerCnt = ingamePlayer.size();
		EGScheduler sch = new EGScheduler(this);
		sch.schTime = 0;
		sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				if(sch.schTime < ingamePlayer.size()) {
					String pName = ingamePlayer.get(sch.schTime);
					Player p = Bukkit.getPlayer(pName);
					if(existPlayer(p)) {
						moveToMyArea(p);
					}
					sch.schTime++;
				}else {
					sch.cancelTask(true);
				}
			}
		}, 20l, 5l);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				updateSidebar();
				for (String pName : ingamePlayer) {
					Player p = Bukkit.getPlayer(pName);
					if(existPlayer(p)){
						p.setGameMode(GameMode.ADVENTURE);
						// Ǯ�Ƿ� ����
						MyUtility.healUp(p);
						MyUtility.allClear(p);         
						//p.teleport(loc_Join);
						TitleAPI.sendFullTitle(p, 10, 80, 30, ChatColor.RED+"���� ����", ChatColor.RED+""+disPlayGameName);
						bdbSidebar.showTo(p);
					}
				}
			}
		}, 150l);
		
		///////////////// ��¥ ����
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				gameStep = 2;
				for(String pName : ingamePlayer) {
					Player p = Bukkit.getPlayer(pName);
					if(existPlayer(p)) {
						giveBaseItem();
						p.setGameMode(GameMode.CREATIVE);	
						gameTimer();
					}
				}
				topic = topicList.get(MyUtility.getRandom(0, topicList.size()-1));
				sendTitle("��e��l"+topic, "��c��l�̰��� ǥ���ϼ���!", 80);
				sendSound(Sound.BLOCK_NOTE_HARP, 1.0f, 1.0f);
			}
		}, 250l);
	}
	
	//////////////////
	
	private void setTopic() {
		topicList.add("�±ر�");
		topicList.add("�ҹ���");
		topicList.add("����");
		topicList.add("����");
		topicList.add("Į");
		topicList.add("����");
		topicList.add("��ǻ��");
		topicList.add("����ũ");
		topicList.add("Ű����");
		topicList.add("�ð�");
		topicList.add("����");
		topicList.add("�Ȱ�");
		topicList.add("���");
		topicList.add("����������");
		topicList.add("�����÷�����");
		topicList.add("����");
		topicList.add("Ǯ��");
		topicList.add("����Ŀ");
		topicList.add("�ڵ���");
		topicList.add("������");
		topicList.add("�ΰ�");
		topicList.add("��");
		topicList.add("����");
		topicList.add("����");
		topicList.add("����");
		topicList.add("��");
		topicList.add("�౹");
		topicList.add("����ũ����Ʈ");
		topicList.add("īī����");
		topicList.add("�Ź�");
		topicList.add("�Ź߲�");
		topicList.add("����");
		topicList.add("��");
		topicList.add("��");
		topicList.add("�ٶ�");
		topicList.add("���̾Ƹ��");
		topicList.add("���޶���");
		topicList.add("ö");
		topicList.add("��ź");
		topicList.add("�ܵ�");
		topicList.add("��");
		topicList.add("����");
		topicList.add("��Ÿ��");
		topicList.add("����");
		topicList.add("õ��");
		topicList.add("ȭ��");
		topicList.add("���ѹα�");
		topicList.add("���ֵ�");
		topicList.add("����");
		topicList.add("�ٴ�");
		topicList.add("���");
		topicList.add("����");
		topicList.add("�尩");
		topicList.add("å��");
		topicList.add("����");
		topicList.add("��ǻ��");
		topicList.add("��ǳ��");
		topicList.add("��������");
		topicList.add("����");
		topicList.add("ȭ��");
		topicList.add("����");
		topicList.add("ȣ����");
		topicList.add("�䳢");
		topicList.add("�ź���");
		topicList.add("����");
		topicList.add("�Ź�");
		topicList.add("�Ź���");
		topicList.add("���");
		topicList.add("¯��");
		topicList.add("���󿡸�");
		topicList.add("����");
		topicList.add("����");
		topicList.add("����");
		topicList.add("���찳");
		topicList.add("å");
		topicList.add("�б�");
		topicList.add("�����");
		topicList.add("������");
		topicList.add("����");
		topicList.add("��");
		topicList.add("����");
		topicList.add("�޴���ȭ");
		topicList.add("����");
		topicList.add("ġŲ");
		topicList.add("��");
		topicList.add("��ǳ��");
		topicList.add("����Ʈ");
		topicList.add("����");
		topicList.add("��");
		topicList.add("������");
		topicList.add("����");
		topicList.add("����");
		topicList.add("â��");
		topicList.add("����");
		topicList.add("����");
		topicList.add("����");
		topicList.add("�㸮��");
		topicList.add("����");
		topicList.add("����");
		topicList.add("�Ʒ�");
		topicList.add("�౸��");
		topicList.add("�߱���");
		topicList.add("ī��");
		topicList.add("����");
		topicList.add("���");
		topicList.add("��");
		topicList.add("����");
		topicList.add("Ȱ");
		topicList.add("ȭ��");
		topicList.add("���ô�");
		topicList.add("���");
		topicList.add("����");
		topicList.add("�丶��");
		topicList.add("������");
		topicList.add("ĩ��");
		topicList.add("ġ��");
		topicList.add("�ѱ�");
		topicList.add("����");
		topicList.add("����");
		topicList.add("��");
		topicList.add("���ٱ�");
		topicList.add("��");
		topicList.add("ȶ��");
		topicList.add("��ں�");
		topicList.add("ȭ��");
		topicList.add("����");
		topicList.add("��Ű");
		topicList.add("����");
		topicList.add("����");
		topicList.add("���");
		topicList.add("�򶧱�");
		topicList.add("����");
		topicList.add("��ħ��");
		topicList.add("���ּ�");
		topicList.add("���");
		topicList.add("�¾�");
		topicList.add("����");
		topicList.add("����");
		topicList.add("������");
		topicList.add("û�ݼ�");
		topicList.add("����");
		topicList.add("����");
		topicList.add("����");
		topicList.add("��������");
		topicList.add("����ö");
		topicList.add("����");
		topicList.add("����");
		topicList.add("�����");
		topicList.add("������");
		topicList.add("��Ű");
		topicList.add("�Ź�");
		topicList.add("�Ҹ�");
		topicList.add("����");
		topicList.add("������");
		topicList.add("�����");
		topicList.add("����");
		topicList.add("����");
		topicList.add("�ر�");
		topicList.add("��");
		topicList.add("����");
		topicList.add("����");
		topicList.add("ö��");
		topicList.add("�±ر�");
		topicList.add("���");
		topicList.add("��ȭ");
		topicList.add("����");
		topicList.add("���ε�");
		topicList.add("ķ��");
		topicList.add("��Ʈ");
		topicList.add("����");
		topicList.add("�Ķ�����");
		topicList.add("���");
		topicList.add("����ũ");
		topicList.add("��ȣ��");
		topicList.add("����");
		topicList.add("�ε�");
		topicList.add("������");
		topicList.add("����");
		topicList.add("�ϴ�");
		topicList.add("��");
		topicList.add("��׸�");
		topicList.add("����");
		topicList.add("����");
		topicList.add("Ⱦ�ܺ���");
		topicList.add("�ݺ�");
		topicList.add("ȭ��Ʈ����");
		topicList.add("����");
		topicList.add("���ݸ�");
		topicList.add("����");
		topicList.add("�︮����");
		topicList.add("�ܼ�Ʈ��");
		topicList.add("�볪��");
		topicList.add("����");
		topicList.add("�ڷ�����");
		topicList.add("���ͳ�");
		topicList.add("����");
		//200
		topicList.add("�̱�");
		topicList.add("������");
		topicList.add("�̸�Ʈ");
		topicList.add("���ڸ�");
		topicList.add("�̺�");
		topicList.add("��");
		topicList.add("��");
		topicList.add("���丮");
		topicList.add("����");
		topicList.add("�������");
		//210
		topicList.add("�޷�");
		topicList.add("��ǳ");
		topicList.add("������");
		topicList.add("Ŭ��");
		topicList.add("ȣ��");
		topicList.add("�عٶ��");
		topicList.add("ī�޶�");
		topicList.add("��ġ");
		topicList.add("�����");
		topicList.add("���ڵ�");
		//220
		topicList.add("��");
		topicList.add("��ũ");
		topicList.add("�ǾƳ�");
		topicList.add("������");
		topicList.add("��");
		topicList.add("��ȭ��");
		topicList.add("�ø���");
		topicList.add("��");
		topicList.add("����");
		topicList.add("������");
		//230
		topicList.add("�׳�");
		topicList.add("�ü�");
		topicList.add("�̲���Ʋ");
		topicList.add("ö��");
		topicList.add("������Ʈ");
		topicList.add("��Ʈ");
		topicList.add("Ŭ�ι�");
		topicList.add("����");
		topicList.add("�ĵ�");
		topicList.add("������ ���Ż�");
		//240
		topicList.add("��");
		topicList.add("���");
		topicList.add("�����巡��");
		topicList.add("����");
		topicList.add("����ű");
		topicList.add("��");
		topicList.add("�౸��");
		topicList.add("����");
		topicList.add("��");
		topicList.add("������");
		//250
		topicList.add("����");
		topicList.add("����ž");
		topicList.add("��");
		topicList.add("ũ����");
		topicList.add("��Ÿ��");
		topicList.add("��");
		topicList.add("ť��");
		topicList.add("�ֻ���");
		topicList.add("��ǥ");
		topicList.add("����");
		//260
		topicList.add("�ܹ���");
		topicList.add("���");
		topicList.add("���ڷ�");
		topicList.add("���ε�");
		topicList.add("��Ÿ");
		topicList.add("�����");
		topicList.add("��������");
		topicList.add("�帱");
		topicList.add("��");
		topicList.add("����");
		//270		
	}
	
	private void setBanBlock() {
		banBlock.add(Material.TNT);
		banBlock.add(Material.ARMOR_STAND);
		banBlock.add(Material.BREWING_STAND);
		banBlock.add(Material.BARRIER);
		banBlock.add(Material.END_CRYSTAL);
		banBlock.add(Material.BEACON);
		banBlock.add(Material.DISPENSER);
		banBlock.add(Material.PISTON_BASE);
		banBlock.add(Material.PISTON_STICKY_BASE);
		banBlock.add(Material.ITEM_FRAME);
		banBlock.add(Material.MINECART);
		banBlock.add(Material.EXPLOSIVE_MINECART);
		banBlock.add(Material.STORAGE_MINECART);
		banBlock.add(Material.COMMAND_MINECART);
		banBlock.add(Material.HOPPER_MINECART);
		banBlock.add(Material.REDSTONE);
		banBlock.add(Material.POWERED_MINECART);
		banBlock.add(Material.ANVIL);
		banBlock.add(Material.RAILS);
		banBlock.add(Material.ACTIVATOR_RAIL);
		banBlock.add(Material.DETECTOR_RAIL);
		banBlock.add(Material.POWERED_RAIL);
		banBlock.add(Material.REDSTONE);
		banBlock.add(Material.REDSTONE_LAMP_OFF);
		banBlock.add(Material.REDSTONE_LAMP_ON);
		banBlock.add(Material.REDSTONE_TORCH_ON);
		banBlock.add(Material.REDSTONE_TORCH_OFF);
		banBlock.add(Material.DROPPER);
		banBlock.add(Material.ENDER_CHEST);
		banBlock.add(Material.CHEST);
		banBlock.add(Material.TRAPPED_CHEST);
	}
	
	private void setBanItem() {
		banItem.add(Material.ENDER_PEARL);
		banItem.add(Material.POTION);
		banItem.add(Material.LINGERING_POTION);
		banItem.add(Material.SPLASH_POTION);
		banItem.add(Material.BOAT);
		banItem.add(Material.BOAT_ACACIA);
		banItem.add(Material.BOAT_BIRCH);
		banItem.add(Material.BOAT_DARK_OAK);
		banItem.add(Material.BOAT_JUNGLE);
		banItem.add(Material.BOAT_SPRUCE);
		banItem.add(Material.EXP_BOTTLE);
		banItem.add(Material.ITEM_FRAME);
	}
	
	public void updateSidebar() {
		if(!ingame) return;
		textList.clear();
		SidebarString blank = new SidebarString("");
		if(gameStep == 2) {			
			textList.add(blank);
			SidebarString line = new SidebarString("��e����");
			textList.add(line);
			line = new SidebarString("��c"+topic);
			textList.add(line);
			textList.add(blank);
			line = new SidebarString("��e�����ο� ��f: ��a"+ingamePlayer.size()+"��");
			textList.add(line);
			textList.add(blank);
			line = new SidebarString("��e�����ð� ��f: ��a"+mainSch.schTime);
			textList.add(line);
			textList.add(blank);
		} else if(gameStep >= 3) {	
			textList.add(blank);
			SidebarString line = new SidebarString("��e����");
			textList.add(line);
			line = new SidebarString("��c"+topic);
			textList.add(line);
			textList.add(blank);	
			line = new SidebarString("��e��ǰ ����");
			textList.add(line);
			line = new SidebarString("��c"+screenedpName);
			textList.add(line);
			textList.add(blank);	
		}
		bdbSidebar.setEntries(textList);
		bdbSidebar.update();
	}
	
	public void gameTimer() {
		
		mainSch.cancelTask(true);
		mainSch.schTime = 330;
		sendBossbar(ChatColor.YELLOW+"��l�ɻ���� ���� �ð�", 330);
		mainSch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				if(mainSch.schTime > 0) {
					mainSch.schTime--;
					updateSidebar();
					if(mainSch.schTime < 30) {					
						if(mainSch.schTime == 29) sendTitle("��6��l���� �ӹ�!", "��e��l���� ���������ּ���!", 100);
						if(mainSch.schTime <= 5) {
							sendTitle("��c��l"+mainSch.schTime, "��e��l�ɻ� ���۱���", 30);
							sendSound(Sound.BLOCK_NOTE_BELL, 1.0f, 2.0f);
						}else {
							sendSound(Sound.BLOCK_NOTE_HAT, 1.0f, 2.0f);
						}
					}
				}else {
					mainSch.cancelTask(true);
					screening();
				}
			}
		}, 0l, 20l);
		
	}
	
	public void screening() {
		if(!ingame || ingamePlayer.size() <= 0) return;
		gameStep = 3;
		mainSch.cancelTask(true);
		for(String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p)) {
				p.setGameMode(GameMode.ADVENTURE);
				p.setAllowFlight(true);
				p.setFlying(true);
				giveScreenItem(p);
			}
		}
		mainSch.schTime = 30;
		mainSch.schTime2 = 0;
		mainSch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				while(mainSch.schTime2 < reConnectList.size()) {
					String pName = reConnectList.get(mainSch.schTime2);
					mainSch.schTime2++;
					if(ingamePlayer.contains(pName)) {
						screenedpName = pName;
						scoreMap.put(screenedpName, 0);
						giverMap.clear();
						sendBossbar("��a��l"+screenedpName+" ��e��l���� ��ǰ �ɻ�ð�", 30);
						sendTitle("��e��l"+pName, "��6��l���� ��ǰ�Դϴ�.", 60);
						updateSidebar();
						for(String tName : ingamePlayer) {
							Player t = Bukkit.getPlayer(tName);
							if(existPlayer(t)) {
								t.teleport(loc_Start.get(reConnectList.indexOf(pName)), TeleportCause.PLUGIN);
							}
						}
						sendSound(Sound.BLOCK_NOTE_XYLOPHONE);
						Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
							public void run() {
								sendTitle("��e��l�ɻ� ��û", "��b��l���� �ɻ縦 ���� ���� �ɻ���� ä÷���ּ���!", 80);
							}
						}, 300l);
						return;
					}
				}
				mainSch.cancelTask(true);
				screeningEnd();
			}
		}, 0l, 600l);
	}
	
	public void screeningEnd() {
		for(String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p)) {
				MyUtility.allClear(p);
			}
		}
		gameStep = 4;
		List<String> rankList = MyUtility.sortByValue(scoreMap, false); //���������� ����
		List<String> tmpList = new ArrayList<String>(scoreMap.keySet());
		if(rankList.size() <= 0) endGame(true);
		sendSound(Sound.BLOCK_NOTE_XYLOPHONE);
		sendMessage(ms+"��� ��ǥ\n");
		EGScheduler sch = new EGScheduler(this);
		sch.schTime = 0;
		sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				if(sch.schTime >= tmpList.size()) {
					sch.cancelTask(true);
					Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
						public void run() {
							String winner = rankList.get(0);
							win(winner);
						}
					}, 60l);
				}
				else {
					String pName = tmpList.get(sch.schTime);
					sendMessage("��b----------- ��6"+pName+ " ��7: ��c"+scoreMap.get(pName)+"��");
					sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 0.1f);
				}
				sch.schTime++;
			}
		}, 60l, 20l);
	}

	public void initGame() {
		lobbyStart = false;
		ending = false;
		gameStep = 0;
		reConnectList.clear();
		//������
		scoreMap.clear();
		schList.clear();
	}

	public boolean loadGameData() {
		boolean ret = true;
		loc_Join = loadLocation(gameName, "JoinPos");
		for(int i = 1; i <= 12; i++) {
			Location l = loadLocation(gameName, "start"+i);
			if(l != null) {
				loc_Start.add(l);
			}
		}
		
		if (loc_Join == null) {
			server.egPM.printLog(ms + "[" + disPlayGameName + "] ���� ���� ��� ������ �������� �ʾҽ��ϴ�.");
			ret = false;
		}else {
			loc_spectate = loc_Start.get(0).clone().add(0,10,0);
		}
		if (loc_Start.size() < 12) {
			server.egPM.printLog(ms + "[" + disPlayGameName + "] ���� ���� ������ �Ϻ��ϰ� �������� �ʾҽ��ϴ�.");
			ret = false;
		}
		for(int i = 0; i < 12; i++) {
			RepairMap map = mapList.get(i);
			if (!map.loadData(locPath)) {
				server.egPM.printLog("[" + disPlayGameName + "] ��"+i+"�� �������� �ʾҽ��ϴ�.");
				ret = false;
			}
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
							} else if (cmd[3].equalsIgnoreCase("map")) {
								if(cmd[5].equalsIgnoreCase("pos1")) {
									RepairMap map = mapList.get(Integer.valueOf(cmd[4]));
									map.setPos1(p.getTargetBlock(null, 3).getLocation());
									p.sendMessage("[" + disPlayGameName + "] " + p.getTargetBlock(null, 3).getLocation()+" �����Ϸ�");
									map.saveMapBlocksWithout(locPath, Material.SNOW_BLOCK);
								} else if(cmd[5].equalsIgnoreCase("pos2")) {
									RepairMap map = mapList.get(Integer.valueOf(cmd[4]));
									map.setPos2(p.getTargetBlock(null, 3).getLocation());
									p.sendMessage("[" + disPlayGameName + "] " + p.getTargetBlock(null, 3).getLocation()+" �����Ϸ�");
									map.saveMapBlocksWithout(locPath, Material.SNOW_BLOCK);
								}
							} else if (cmd[3].equalsIgnoreCase("start")) {
								saveLocation(gameName, "start"+cmd[4], p.getLocation());
								p.sendMessage("[" + disPlayGameName + "] " + p.getTargetBlock(null, 3).getLocation()+" �����Ϸ�");
							}else {
								p.sendMessage("[" + disPlayGameName + "] " + cmd[3]+"�� �ùٸ��� ���� �μ�");
							}
							loadGameData();
						} else {
							p.sendMessage(ms+"�μ��� �Է����ּ���.");
						}
					} else {
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" set loc join - ���� ���� ��� ���� ����");
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" set loc start 1~12 - ���� ���� ���� ����");
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" set loc map 1 pos1 - ����1 �� 1����");
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" set loc map 1 pos2 - ����1 �� 2����");
					}
				} else {
					p.sendMessage("[" + disPlayGameName + "] " + "/wog set loc- ���� ���� ����");
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
			} else if(cmd[1].equalsIgnoreCase("debug0")) {
				RepairMap map = mapList.get(Integer.valueOf(cmd[2]));
				map.Repair(Integer.valueOf(cmd[3]));
			} else if(cmd[1].equalsIgnoreCase("debug1")) {
				gameStep = 2;
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
			if (ingame) {
				if(giveGold) {
					if(announce) {
						sendMessage(ms + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " ���� �����ϼ̽��ϴ�.");
						//sendTitle("", ChatColor.YELLOW+p.getName()+"�� ����", 60);
						sendSound(Sound.BLOCK_NOTE_CHIME,1.5f, 2.0f);
					}
					p.sendMessage(ms+"���� �÷��� �������� 10��带 �����̽��ϴ�.");
				} else {
					if(announce) {
						sendMessage(ms + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " ���� �����ϼ̽��ϴ�.");
						sendSound(Sound.BLOCK_NOTE_GUITAR,1.5f, 2.0f);
					}
				}
				if(bdbSidebar != null) bdbSidebar.hideFrom(p);
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
	
	public void giveBaseItem() {
		for(String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p)) {
				p.getInventory().setItem(8, helpItem);
			}
		}
	}
	
	public void giveScreenItem(Player p) {
		MyUtility.allClear(p);
		p.getInventory().setItem(0, item_score1);
		p.getInventory().setItem(1, item_score2);
		p.getInventory().setItem(2, item_score3);
		p.getInventory().setItem(3, item_score4);
		p.getInventory().setItem(4, item_score5);
		p.getInventory().setItem(8, helpItem);
	}
	
	public void giveScore(Player giver, int score) {
		if(!scoreMap.containsKey(screenedpName)) return;
		giver.playSound(giver.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
		if(giver.getName().equalsIgnoreCase(screenedpName)) {
			giver.sendMessage(ms+"������ �򰡴� �Ұ����մϴ�.");
			return;
		}
		if(giverMap.containsKey(giver.getName())) {
			int givedScore = giverMap.get(giver.getName());
			int nowScore = scoreMap.get(screenedpName);
			scoreMap.put(screenedpName, nowScore - givedScore + score);
			giverMap.put(giver.getName(), score);
			TitleAPI.sendFullTitle(giver, 10, 60, 10, "��a��l"+score+"��", "��6��l���� ��c��l���� ��6��l�Ͽ����ϴ�.");
		} else {
			int nowScore = scoreMap.get(screenedpName);
			scoreMap.put(screenedpName, nowScore + score);
			giverMap.put(giver.getName(), score);
			TitleAPI.sendFullTitle(giver, 10, 60, 10, "��a��l"+score+"��", "��6��l���� ��c��l�� ��6��l�Ͽ����ϴ�.");		
		}
	}
	
	public void moveToMyArea(Player p) {
		p.teleport(loc_Start.get(reConnectList.indexOf(p.getName())), TeleportCause.PLUGIN);
	}
	
	public RepairMap getMyArea(Player p) {
		return mapList.get(reConnectList.indexOf(p.getName()));
	}
	
	public void performence(Location loc) {
		EGScheduler sch = new EGScheduler(this);
		sch.schTime = 6;
		sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				if(sch.schTime> 0) {
					sch.schTime--;
					for(int i = 0; i <3; i++) {
						Location tmpL = loc.clone();
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

	private void win(String winner) {
		if(ending) return;
		ending = true;
		gameStep = 4;
		Player winnerP = Bukkit.getPlayer(winner);
		if(!existPlayer(winnerP)) endGame(true);
		if(ingamePlayer.size() >= 1) {
			for(String pName : ingamePlayer) {
				Player p = Bukkit.getPlayer(pName);
				if(existPlayer(p)) {
					p.teleport(loc_Start.get(reConnectList.indexOf(winner)), TeleportCause.PLUGIN);
				}
			}
			screenedpName = winner;
			updateSidebar();
			sendTitle("��6��l"+winner, ChatColor.GRAY + "���� 1���� �����Ͽ����ϴ�!", 120);
			sendSound(Sound.BLOCK_NOTE_BELL, 1.0f, 0.5f);
			
			performence(winnerP.getLocation());			
			
			try {
				Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
					public void run() {
						sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
						for (String pName : ingamePlayer) {
							Player p = Bukkit.getPlayer(pName);
							if(existPlayer(p)) {
								if(winner.equalsIgnoreCase(pName)) {
									//server.egGM.giveGold(p.getName(), 40);
									//p.sendMessage(ms + "�¸� �������� 30��带 �����̽��ϴ�.");
								} else {
									//server.egGM.giveGold(p.getName(), 40);
									//p.sendMessage(ms + "���� ���� �������� 20��带 �����̽��ϴ�.");
								}							
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
							server.ms_alert + ChatColor.GRAY  + "��c"+winner+"��7���� ��a�¸���7�� ��c���� ���׽�Ʈ��7�� ���� �Ǿ����ϴ�.");
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
							server.ms_alert + ChatColor.GRAY  + "���ºη� ��c���� ���׽�Ʈ��7�� ���� �Ǿ����ϴ�.");
					}
				}, 200L);
			} catch(Exception e) {
					endGame(true);
			}
		}
	}
	
	private void setWinner() {
		if(ending) return;
		ending = true;
		gameStep = 4;
		mainSch.cancelTask(true);
		if(ingamePlayer.size() >= 1) {
			String winner = ingamePlayer.get(0);
			Player winnerP = Bukkit.getPlayer(winner);
			if(!existPlayer(winnerP)) endGame(true);
			sendTitle("�¸�", ChatColor.GRAY + "����� ������ ��� �÷��̾ �������ϴ�.", 70);
			sendSound(Sound.BLOCK_NOTE_BELL, 1.0f, 0.5f);
			
			performence(winnerP.getLocation());			
			
			try {
				Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
					public void run() {
						for (String pName : ingamePlayer) {
							Player p = Bukkit.getPlayer(pName);
							if(existPlayer(p)) {
								//server.egGM.giveGold(p.getName(), 40);
								//p.sendMessage(ms + "�¸� �������� 10��带 �����̽��ϴ�.");
								sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
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
							server.ms_alert + ChatColor.GRAY  + "��c"+winner+"��7���� ��a�¸���7�� ��c���� ���׽�Ʈ��7�� ���� �Ǿ����ϴ�.");
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
							server.ms_alert + ChatColor.GRAY  + "���ºη� ��c���� ���׽�Ʈ��7�� ���� �Ǿ����ϴ�.");
					}
				}, 200L);
			} catch(Exception e) {
					endGame(true);
			}
		}
	
	}

	public void endGame(boolean force) {
		if(force) {
			server.egCM.broadCast(server.ms_alert + "���� ���׽�Ʈ ������ ���� ���� �Ǿ����ϴ�.");
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
		for(RepairMap map : mapList) {
			map.repaired = false;
			map.Repair(startCountTime);
		}
		initGame();	
	}

	//////////////// �̺�Ʈ
	public class EventHandlerBBT extends EGEventHandler {

		private BuildBattle game;

		public EventHandlerBBT(EGServer server, BuildBattle game) {
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

		@EventHandler
		public void onFall(EntityDamageEvent e) {
			if (e.getEntityType() == EntityType.PLAYER) {
				Player p = (Player) e.getEntity();
				DamageCause cause = e.getCause();
				if (ingamePlayer.contains(p.getName())) {
					e.setCancelled(true);
					if(!ingame) {
				        if (cause.equals(DamageCause.VOID) && !ingame) { //���� ��� ������, ����		           
				            p.teleport(loc_Join, TeleportCause.PLUGIN);
				        }
					} else {
						if (cause.equals(DamageCause.VOID)) { //����� = �ڱ���ġ��
				            moveToMyArea(p);
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
			ItemStack item = e.getCurrentItem();
			if(item != null) {
				Material type = item.getType();
				if(banBlock.contains(type) || banItem.contains(type)) e.setCancelled(true);
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
		public void onRightClick(PlayerInteractEvent e) {
			Player p = e.getPlayer();
			Action action = e.getAction();
			if (!ingamePlayer.contains(p.getName()) //��Ŭ���� ���
					|| (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK))
				return;
			if (e.getItem() == null || e.getItem().getType() == Material.AIR) { //�������� �ȵ�� ��Ŭ�������� ����
				return;
			} else if (getHeldMainItemName(p).equalsIgnoreCase("��f[ ��6���� ����� ��f]")) {
				p.openInventory(inven_gameHelper);
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 2.0f);
			} else if (getHeldMainItemName(p).equalsIgnoreCase(item_score1.getItemMeta().getDisplayName())) {
				giveScore(p, 1);
			} else if (getHeldMainItemName(p).equalsIgnoreCase(item_score2.getItemMeta().getDisplayName())) {
				giveScore(p, 2);
			} else if (getHeldMainItemName(p).equalsIgnoreCase(item_score3.getItemMeta().getDisplayName())) {
				giveScore(p, 3);
			} else if (getHeldMainItemName(p).equalsIgnoreCase(item_score4.getItemMeta().getDisplayName())) {
				giveScore(p, 4);
			} else if (getHeldMainItemName(p).equalsIgnoreCase(item_score5.getItemMeta().getDisplayName())) {
				giveScore(p, 5);
			} else {
				if(banItem.contains(e.getItem().getType()) || banItem.contains(e.getItem().getType())) {
					p.sendMessage(ms+"�ش� ���� ����� �Ұ����մϴ�.");
					e.setCancelled(true);
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
		public void onPlayerDeath(PlayerDeathEvent e){
			if(e.getEntity() instanceof Player){
				Player p = (Player) e.getEntity();
				if(ingamePlayer.contains(p.getName())) 
					gameQuitPlayer(p, true, true);
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
		public void onPlayerChat(PlayerChatEvent e){
			Player p = (Player) e.getPlayer();
			if(ingamePlayer.contains(p.getName())) {
				e.setCancelled(true);
				if(gameStep >= 3) {
					if(p.getName().equals(screenedpName)) {
						String str = game.ms+p.getName()+" >> ��6"+e.getMessage()+" ��7[��c��ǰ ���Ρ�7]";
						server.egCM.sendMessagesToStringList(ingamePlayer, p, str, false);
						sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 1.5f);	
					}else {
						String str = game.ms+p.getName()+" >> ��6"+e.getMessage()+" ��7[��c�ɻ����7]";
						server.egCM.sendMessagesToStringList(ingamePlayer, p, str, false);
						sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 1.5f);	
					}
				}else {
					String str = game.ms+p.getName()+" >> ��6"+e.getMessage();
					server.egCM.sendMessagesToStringList(ingamePlayer, p, str, false);
					sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 1.5f);	
				}			
			}
		}
		
		@EventHandler
		public void onItemDrop(PlayerDropItemEvent e) {
			Player p = e.getPlayer();
			if(!ingamePlayer.contains(p.getName())) return;
			e.setCancelled(true);
		}
		
		@EventHandler
		public void onBlockBreak(BlockBreakEvent e) {
			Player p = e.getPlayer();
			if(ingamePlayer.contains(p.getName())) {
				if(gameStep != 2) e.setCancelled(true);
				else {
					RepairMap map = getMyArea(p);
					if(!isInArea(e.getBlock().getLocation(), map.pos1, map.pos2)) {
						e.setCancelled(true);
					}
				}
			}
		}
		
		@EventHandler
		public void onUseBucket(PlayerBucketEmptyEvent e) {
			Player p = e.getPlayer();
			if(ingamePlayer.contains(p.getName())) {
				if(e.getBucket().toString().contains("LAVA")) {
					p.sendMessage(ms+"�ش� ���� ����� �Ұ����մϴ�.");
					e.setCancelled(true);
				}
			}
		}

		@EventHandler
		public void onBlockPlaced(BlockPlaceEvent e) {
			Player p = e.getPlayer();
			if(ingamePlayer.contains(p.getName())) {
				if(gameStep != 2) e.setCancelled(true);
				else {
					RepairMap map = getMyArea(p);
					if(!isInArea(e.getBlock().getLocation(), map.pos1, map.pos2)) {
						e.setCancelled(true);
					} else {
						if(banBlock.contains(e.getBlock().getType())) {
							p.sendMessage(ms+"�ش� ���� ����� �Ұ����մϴ�.");
							e.setCancelled(true);
						}
					}
				}
			}
		}
		
		
	}
	
	///////////////////// ���� ���׽�Ʈ�� ������ �÷��̾�� Ŭ����
	private class BdbPlayer {
		
		public BdbPlayer(Player p, String job) {

		}

	}
}
