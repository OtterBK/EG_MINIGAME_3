package Minigames.DeathRun;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
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
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
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
import EGServer.ServerManager.EGEventHandler;
import EGServer.ServerManager.EGScheduler;
import Minigames.Minigame;
import Utility.MyUtility;
import Utility.RepairMap;

public class DeathRun extends Minigame{
	// �̺�Ʈ��
	public EventHandlerDTR event;
	
	public String ms = "��7[��6��ü��7] ";

	///////////// private
	// ���� �÷��̾� ����Ʈ
	//private HashMap<String, DtrPlayer> playerMap = new HashMap<String, DtrPlayer>();
	private List<String> cooldownlist = new ArrayList<String>();
	private String cmdMain;
	//////// ���� ����
	public List<Location> loc_Start = new ArrayList<Location>(8);
	public int gameStep = 0;
	public EGScheduler mainSch;
	public LinkedList<Location> changingList = new LinkedList<Location>();
	public List<Location> backUpBlockList = new ArrayList<Location>();
	public List<Location> blockList = new ArrayList<Location>();
	
	////// ���� �κ��丮
	public Inventory inven_gameHelper;
	
	// ���� ������
	public ItemStack item_speedUp;
	
	//������ ��
	public RepairMap map;
	public String locPath;
	
	//////// ���̵��
	private Sidebar dtrSidebar;
	private List<SidebarString> textList = new ArrayList<SidebarString>(5);
	
	public DeathRun(EGServer server, String cmdMain) {

		//////////////////// �ʼ� ������
		super(server);
		
		this.cmdMain = cmdMain;
		
		ms = "��7[ ��e! ��7] ��f: ��c������ ��f>> "; // �⺻ �޼���
		gameName = "DeathRun";
		disPlayGameName = ChatColor.RED+"������";
		inventoryGameName = ChatColor.stripColor(disPlayGameName);
		minPlayer = 2;
		maxPlayer = 6;
		startCountTime = 40;
		//������ �� ����
		locPath = server.getDataFolder().getPath() + "/" + gameName + "/Location";
		map = new RepairMap(server, "deathRun");
		doneSetting = loadGameData(); // ���� ���� ��ġ ��� �ε�	
		
		/////////////////////// �ڵ� ����(�����۵��)
		dirSetting("DeathRun");
		////////////////
		
		//map.loadData(locPath);
		
		mainSch = new EGScheduler(this);
		
		SidebarString tmpLine = new SidebarString("");
		dtrSidebar = new Sidebar("��f[ ��6���� ��Ȳ ��f]", server, 600, tmpLine);
		
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
		loreList.add("��7�ڽ��� ������ ��� �÷��̾");
		loreList.add("��7Ż����Ű�� �¸��մϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_gameHelper.setItem(10, item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7- ��c������ ��7-");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7������ ���۵Ǹ� �� �ٴ��� �������� ������ϴ�.");
		loreList.add("��7�ٴ��� ���->��Ȳ->���� ������ ���� ���ϸ� �������");
		loreList.add("��7������ �ٴ��� �׽� �ֽ��ϰ� ��ž��մϴ�.");
		loreList.add("��7�ٴ��� ����� �������� �������� Ż���մϴ�.");
		loreList.add("��7������ ���θ� ��ĥ ���� �ֽ��ϴ�.");
		loreList.add("��7����⸦ ��Ŭ���� �������ְ� �����մϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_gameHelper.setItem(11, item);
		
		//��
		item_speedUp = new ItemStack(Material.STICK, 1);
		meta = item_speedUp.getItemMeta();
		meta.setDisplayName("��f[ ��b���� (��ġ��, ��������) ��f]");
		//loreList = new ArrayList<String>();
		item_speedUp.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
		item_speedUp.setItemMeta(meta);
		
		event = new EventHandlerDTR(server, this);
		// �� �÷����ο� �̺�Ʈ ����
		server.getServer().getPluginManager().registerEvents(event, server);
		
		for(String s : map.blockData.keySet()) {
			String locData[] = s.split(",");
			int x = Integer.valueOf(locData[0]);
			int y = Integer.valueOf(locData[1]);
			int z = Integer.valueOf(locData[2]);
			Location l = new Location(map.pos1.getWorld(), x, y, z);
			backUpBlockList.add(l);
		}
	}

	/////////////// Minigame���� override �ؾ��ϴ� �κ�
	@Override
	public void gameHelpMsg(Player p) {

	}
	
	@Override
	public void startCount() {
		if(ingame) return;
		lobbyStart = true;
		startSch.cancelTask(true);
		map.repaired = false;
		map.RepairToSpecBlock(startCountTime, Material.STAINED_CLAY, (byte)0);
		startSch.schTime = startCountTime;
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
		if(ingamePlayer.size() <= 1) {
			for(String pName : ingamePlayer) {
				Player t = Bukkit.getPlayer(pName);
				server.spawn(t);
				t.sendMessage(ms+"�ּ� �����ο��� �����Ͽ� ������ ��ҵƽ��ϴ�.");
			}
			endGame(false);
			return;
		}
		server.egCM.broadCast(server.ms_alert + ChatColor.RED+ChatColor
				.BOLD+ "������ " + ChatColor.GRAY + "�� ���� �Ǿ����ϴ�!");
		initGame();
		ingame = true;
		gameStep = 1;
		/////////////// ������	
		sendSound(Sound.BLOCK_NOTE_BELL, 1.0f, 1.0f);
		updateSidebar();
		
		EGScheduler sch = new EGScheduler(this);
		sch.schTime = 0;
		sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				if(sch.schTime < ingamePlayer.size()) {
					String pName = ingamePlayer.get(sch.schTime);
					Player p = Bukkit.getPlayer(pName);
					if(existPlayer(p)) {
						p.teleport(loc_Start.get(0), TeleportCause.PLUGIN);
					}
					sch.schTime++;
				}else {
					sch.cancelTask(true);
				}
			}
		}, 20l, 5l);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				gameStep = 2;
				countDown(5, "��� ���۱���");
				for (String pName : ingamePlayer) {
					Player p = Bukkit.getPlayer(pName);
					if(existPlayer(p)){
						p.setGameMode(GameMode.ADVENTURE);
						// Ǯ�Ƿ� ����
						MyUtility.healUp(p);
						MyUtility.allClear(p);         
						//p.teleport(loc_Join);
						TitleAPI.sendFullTitle(p, 10, 80, 30, ChatColor.RED+"���� ����", ChatColor.RED+""+disPlayGameName);	
						dtrSidebar.showTo(p);
//						p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 72000, 0));
						try {
							p.teleport(loc_Start.get(ingamePlayer.indexOf(p.getName())), TeleportCause.PLUGIN);
						}catch(Exception e) {
							p.teleport(loc_Start.get(0), TeleportCause.PLUGIN);
						}	
					}
				}
			}
		}, 150l);	
		
		///////////////// ��¥ ����
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				gameStep = 3;
				for(String pName : ingamePlayer) {
					Player p = Bukkit.getPlayer(pName);
					if(existPlayer(p)) {
						giveBaseItem();
					}
				}
				sendSound(Sound.BLOCK_SLIME_BREAK, 1.0f, 1.3f);
				gameTimer();
			}
		}, 250l);
	}
	
	//////////////////
	public void gameTimer(){
		blockList.clear();
		blockList.addAll(backUpBlockList);
		mainSch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable(){
			public void run(){
				if(gameStep == 3){
					for(int i = 0; i < 8; i++) {
						if(blockList.size() == 0) break;
						int rn = MyUtility.getRandom(0, blockList.size()-1);
						changingList.add(blockList.get(rn));
						blockList.remove(rn);
					}
					try {
						for (int i = 0; i < changingList.size(); i++) {
							Location l = changingList.get(i);
							if (l.getBlock().getData() == 0)
								l.getBlock().setData((byte) 4);
							else if (l.getBlock().getData() == 4)
								l.getBlock().setData((byte) 1);
							else if (l.getBlock().getData() == 1)
								l.getBlock().setData((byte) 14);
							else if (l.getBlock().getData() == 14) {
								l.getBlock().setType(Material.AIR);
								changingList.remove(l);
								i--;
							} else if (l.getBlock().getType() == Material.AIR) {
								changingList.remove(l);
								i--;
							}
						}
					} catch (Exception e) {
					}		
				}
				else{
					mainSch.cancelTask(true);
				}
			}
		}, 0L, 10L); 
	}
	
	public void updateSidebar() {
		if(!ingame) return;
		textList.clear();
		
		SidebarString line = new SidebarString("");
		textList.add(line);
		line = new SidebarString("��e�����ο� ��f: ��a"+ingamePlayer.size()+"��");
		textList.add(line);
		line = new SidebarString("");
		textList.add(line);
		
		dtrSidebar.setEntries(textList);
		dtrSidebar.update();
		
		/*for(String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p)) 
				dtrSidebar.showTo(p);
		}*/
	}

	public void initGame() {
		lobbyStart = false;
		ending = false;
		gameStep = 0;
		//������
		schList.clear();
		map.repaired = false;
	}

	public boolean loadGameData() {
		boolean ret = true;
		loc_Join = loadLocation(gameName, "JoinPos");
		for(int i = 1; i <= 8; i++) {
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
		if (loc_Start.size() < 8) {
			server.egPM.printLog(ms + "[" + disPlayGameName + "] ���� ���� ������ �Ϻ��ϰ� �������� �ʾҽ��ϴ�.");
			ret = false;
		}
		if (!map.loadData(locPath)) {
			server.egPM.printLog("[" + disPlayGameName + "] ���� �������� �ʾҽ��ϴ�.");
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
							} else if (cmd[3].equalsIgnoreCase("map1")) {
								map.setPos1(p.getTargetBlock(null, 3).getLocation());
								p.sendMessage("[" + disPlayGameName + "] " + p.getTargetBlock(null, 3).getLocation()+" �����Ϸ�");
								map.saveMapBlocks(locPath, Material.STAINED_CLAY);
							}  else if (cmd[3].equalsIgnoreCase("map2")) {
								map.setPos2(p.getTargetBlock(null, 3).getLocation());
								p.sendMessage("[" + disPlayGameName + "] " + p.getTargetBlock(null, 3).getLocation()+" �����Ϸ�");
								map.saveMapBlocks(locPath, Material.STAINED_CLAY);
							} else if (cmd[3].equalsIgnoreCase("start")) {
								saveLocation(gameName, "start"+cmd[4], p.getLocation());
								p.sendMessage("[" + disPlayGameName + "] " + p.getTargetBlock(null, 3).getLocation()+" �����Ϸ�");
							} else {
								p.sendMessage("[" + disPlayGameName + "] " + cmd[3]+"�� �ùٸ��� ���� �μ�");
							}
							loadGameData();
						} else {
							p.sendMessage(ms+"�μ��� �Է����ּ���.");
						}
					} else {
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" set loc join - ���� ���� ��� ���� ����");
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" set loc start 1~8 - ���� ���� ���� ����");
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" set loc map1 - ��1����");
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" set loc map2 - ��2����");
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
			} else if (cmd[1].equalsIgnoreCase("debug0")) {
				map.repaired = false;
				p.sendMessage("did");
				map.RepairToSpecBlock(Integer.valueOf(cmd[2]), Material.STAINED_CLAY, (byte)0);
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
				if(dtrSidebar != null) dtrSidebar.hideFrom(p);
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
				p.getInventory().setItem(0, item_speedUp);
				p.getInventory().setItem(8, helpItem);
			}
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
					sch.schTime--;
					for(int i = 0; i <3; i++) {
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
		gameStep = 4;
		mainSch.cancelTask(true);
		if(ingamePlayer.size() >= 1) {
			String winner = ingamePlayer.get(0);
			sendTitle("��6��l�¸�", ChatColor.GRAY + "����� ������ 1���Դϴ�.", 70);
			sendSound(Sound.BLOCK_NOTE_BELL, 1.0f, 0.5f);
			
			performence();			
			
			try {
				Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
					public void run() {
						for (String pName : ingamePlayer) {
							Player p = Bukkit.getPlayer(pName);
							if(existPlayer(p)) {
								//server.egGM.giveGold(p.getName(), 40);
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
							server.ms_alert + ChatColor.GRAY  + "��c"+winner+"��7���� ��a�¸���7�� ��c��������7�� ���� �Ǿ����ϴ�.");
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
							server.ms_alert + ChatColor.GRAY  + "���ºη� ��c��������7�� ���� �Ǿ����ϴ�.");
					}
				}, 200L);
			} catch(Exception e) {
					endGame(true);
			}
		}
		
	
	}

	public void endGame(boolean force) {
		if(force) {
			server.egCM.broadCast(server.ms_alert + "������ ������ ���� ���� �Ǿ����ϴ�.");
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

	//////////////// �̺�Ʈ
	public class EventHandlerDTR extends EGEventHandler {

		private DeathRun game;

		public EventHandlerDTR(EGServer server, DeathRun game) {
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
					if(!ingame) {
						e.setCancelled(true);
				        if (cause.equals(DamageCause.VOID) && !ingame) { //���� ��� ������, ����		           
				            p.teleport(loc_Join, TeleportCause.PLUGIN);
				        }
					} else {
						if (cause.equals(DamageCause.VOID)) { //����� = Ż��
							if(gameStep == 4) {
								p.teleport(loc_Join, TeleportCause.PLUGIN);
							}else {
								gameQuitPlayer(p, true, true);
					            server.spawn(p);
							}		
							e.setCancelled(true);
				        } else {
				        	e.setDamage(0.1);
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
			} else if(e.getItem().getType() == Material.STICK) {
				if(!cooldownlist.contains(p.getName())) {
					cooldownlist.add(p.getName());
					p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 1));
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.5f);
					timerLevelBar(p, 3, false, true);
					Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
						public void run() {
							cooldownlist.remove(p.getName());
							TitleAPI.sendFullTitle(p, 0, 40, 0, "", "��e��l���� ���� �غ��");
							p.playSound(p.getLocation(), Sound.BLOCK_NOTE_CHIME, 1.0f, 2.0f);
						}
					}, 200L);
				} else {
					p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 2.0f);
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
				if(e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockY() == e.getTo().getBlockY()
						&& e.getFrom().getBlockZ() == e.getTo().getBlockZ()) return;
				if(gameStep == 2) egCancelMovement(e);
			}
		}
	}
	
	///////////////////// �������� ������ �÷��̾�� Ŭ����
	private class DtrPlayer {
		
		public DtrPlayer(Player p, String job) {

		}

	}
}
	
