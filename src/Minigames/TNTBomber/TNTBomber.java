package Minigames.TNTBomber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
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
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.coloredcarrot.api.sidebar.Sidebar;
import com.coloredcarrot.api.sidebar.SidebarString;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.connorlinfoot.titleapi.TitleAPI;

import EGServer.EGServer;
import EGServer.ServerManager.EGEventHandler;
import EGServer.ServerManager.EGScheduler;
import Minigames.Minigame;
import Utility.MyUtility;

public class TNTBomber extends Minigame{
	// �̺�Ʈ��
	public EventHandlerTTB event;
	
	public String ms = "��7[��6��ü��7] ";
	///////////// private
	// ���� �÷��̾� ����Ʈ
	private HashMap<String, TtbPlayer> playerMap = new HashMap<String, TtbPlayer>();

	//////// ���� ����
	public ItemStack item_tnt;
	public ItemStack item_sword;
	
	public int gameRound = 1;
	public int roundTime = 90;
	public EGScheduler mainSch;
	public String catcherName = "";
	public boolean catcherNoAttack = false;
	
	public float normalWalkSpeed = 0.4f;
	public float catcherWalkSpeed = 0.45f;
	
	////// ���� �κ��丮
	public Inventory inven_gameHelper;
	
	//////// ���̵��
	private Sidebar ttbSidebar;
	private List<SidebarString> textList = new ArrayList<SidebarString>(5);
	
	public TNTBomber(EGServer server) {

		//////////////////// �ʼ� ������
		super(server);
		
		ms = "��7[ ��e! ��7] ��f: ��c��ź ������ ��f>> "; // �⺻ �޼���
		gameName = "TNTBomber";
		disPlayGameName = ChatColor.RED+"��ź ������";
		inventoryGameName = ChatColor.stripColor(disPlayGameName);
		minPlayer = 4;
		maxPlayer = 9;
		startCountTime = 30;
		doneSetting = loadGameData(); // ���� ���� ��ġ ��� �ε�
		/////////////////////// �ڵ� ����(�����۵��)
		dirSetting("TNTBomber");
		////////////////
		
		mainSch = new EGScheduler(this);
		
		SidebarString tmpLine = new SidebarString("");
		ttbSidebar = new Sidebar("��f[ ��6���� ��Ȳ ��f]", server, 600, tmpLine);
		
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
		loreList.add("��7������ ���۵Ǹ� �������� 1������ ��ź��");
		loreList.add("��7�־����ϴ�. ��ź�� ���� �÷��̾�� �Ӹ���");
		loreList.add("��7TNT�� ���� ������ �ð��� ������ �����ϴ�.");
		loreList.add("��7��ź�� ������ ��ź�� �����ϰ� �ִ� �÷��̾��");
		loreList.add("��7Ż���ϰ� �˴ϴ�. ��ź�� ��������� ���� �ð���");
		loreList.add("��7�ϴ��� ����ġ�ٷ� Ȯ���� �� �ֽ��ϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_gameHelper.setItem(11, item);
		
		item_tnt = new ItemStack(Material.TNT, 1);
		meta = item_tnt.getItemMeta();
		meta.setDisplayName("��f[ ��c��ź ��f]");
		item_tnt.setItemMeta(meta);
		
		item_sword = new ItemStack(Material.DIAMOND_SWORD, 1);
		meta = item_sword.getItemMeta();
		meta.setDisplayName("��f[ ��c���� ��f]");
		item_sword.setItemMeta(meta);
		
		event = new EventHandlerTTB(server, this);
		// �� �÷����ο� �̺�Ʈ ����
		server.getServer().getPluginManager().registerEvents(event, server);
	}

	/////////////// Minigame���� override �ؾ��ϴ� �κ�
	@Override
	public void gameHelpMsg(Player p) {

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
				.BOLD+ "��ź ������ " + ChatColor.GRAY + "�� ���� �Ǿ����ϴ�!");
		initGame();
		ingame = true;
		/////////////// ������	
		sendSound(Sound.BLOCK_NOTE_BELL, 1.0f, 1.0f);
		updateSidebar();
		for (String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p)){
				p.setGameMode(GameMode.ADVENTURE);
				// Ǯ�Ƿ� ����
				MyUtility.healUp(p);         
				//p.teleport(loc_Join);
				TitleAPI.sendFullTitle(p, 10, 80, 30, ChatColor.RED+"���� ����", ChatColor.RED+""+disPlayGameName);
				ttbSidebar.showTo(p);
				p.setWalkSpeed(normalWalkSpeed);
				p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 72000, 1));
				p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 72000, 0));
			}
		}
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server,new Runnable(){
			public void run() {
				sendTitle("", "�� ��ź�� �־����ϴ�. ���������!", 60);
				gameTimer();
			}
		},100l);
	}
	
	//////////////////
	
	public void gameTimer() {
		mainSch.schTime2 = 0;
		gameRound = 1;
		mainSch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				if(mainSch.schTime2 == 0) {
					if(ingamePlayer.size() < 1) {
						setWinner();
					}
					setCatcher();
					mainSch.schTime2 = 1;
					mainSch.schTime = 0;
					roundTime = 15+MyUtility.getRandom(20, 65);
				} else if(mainSch.schTime2 == 1) {
					if(mainSch.schTime >= roundTime) {
						mainSch.schTime2 = 2;
						mainSch.schTime = 0;
						bomb();
					}else {
						mainSch.schTime++;
						sendSound(Sound.BLOCK_NOTE_HAT);
					}
				} else if(mainSch.schTime2 == 2) {
					if(mainSch.schTime >= 10) {
						mainSch.schTime2 = 0;
					}else {
						mainSch.schTime++;
					}
				} else {
					mainSch.cancelTask(true);
				}
			}
		}, 0l, 20l);
	}
	
	public void bomb() {
		if(catcherName.equalsIgnoreCase("")) return;
		Player p = Bukkit.getPlayer(catcherName);
		Location l = p.getLocation().clone().add(0,1,0);
		if(existPlayer(p)) {
			sendSound(Sound.ENTITY_GENERIC_EXPLODE);
			p.setHealth(0);
			EGScheduler sch = new EGScheduler(this);
			sch.schTime = 7;
			sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
				public void run() {
					if(sch.schTime > 0) {
						sch.schTime--;
						p.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, l, 10, 0.25F, 0.25f, 0.25f, 0.1f);					
					}else {
						sch.cancelTask(true);
						sendTitle("��c��l��!", "��6��l��ź�� �������ϴ�.", 60);
						catcherName = "";
					}
				}
			}, 0l, 10l);	
		}
	}
	
	public void setCatcher() {
		catcherNoAttack = false;
		catcherName = "";
		for(String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			timerLevelBar(p, 15, false, true);
		}
		EGScheduler sch = new EGScheduler(this);
		sch.schTime = 15;
		sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				if(sch.schTime > 0) {
					sch.schTime--;
					if(sch.schTime < 5) {
						sendTitle("��c��l"+(sch.schTime+1), "", 25);
						sendSound(Sound.BLOCK_NOTE_PLING);
					}
				}else {
					sch.cancelTask(true);
					String tName = ingamePlayer.get(MyUtility.getRandom(0, ingamePlayer.size()-1));
					Player catcher = Bukkit.getPlayer(tName);
					applyCatcher(catcher);
				}
			}
		}, 0l, 20l);
	}
	
	public void applyCatcher(Player p) {
		p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 254));
		p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 60, 248));
		p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 60, 255));
		p.removePotionEffect(PotionEffectType.GLOWING);
		p.getInventory().setHelmet(item_tnt);
		for(int i = 0; i < 9; i++) {
			p.getInventory().setItem(i, item_sword);
		}
		catcherName = p.getName();
		catcherNoAttack = true;
		p.sendMessage(ms+"����� ��ź�� ������ �ֽ��ϴ�. � Ÿ�ο��� �ѱ⼼��!");
		sendSound(Sound.BLOCK_CHEST_CLOSE, 1.0f, 0.5f);
		p.setWalkSpeed(catcherWalkSpeed);
		TitleAPI.sendFullTitle(p, 0, 60, 0, "��4��l����", "��e��l��ź�� ������ �ֽ��ϴ�.");
		for(String tName : ingamePlayer) {
			if(tName.equalsIgnoreCase(p.getName())) continue;
			Player t = Bukkit.getPlayer(tName);
			if(existPlayer(t)) {
				TitleAPI.sendFullTitle(t, 0, 60, 0, "��c��l"+p.getName(), "��a��l���� ��ź�� ������ �ֽ��ϴ�.");
				t.sendMessage(ms+"��6"+p.getName()+" ���� ��ź�� �޾ҽ��ϴ�.");
			}
		}
		updateSidebar();
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				if(!ingame) return;
				catcherNoAttack = false;
				p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP,72000, 1));
			}
		},50l);
	}
	
	public void relieveCatcher(Player p) {
		p.getInventory().setHelmet(null);
		p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 72000, 0));
		p.getInventory().clear();
		sendSound(Sound.ENTITY_TNT_PRIMED, 1.0f, 0.1f);
		p.setWalkSpeed(normalWalkSpeed);
	}
	
	public void updateSidebar() {
		if(!ingame) return;
		textList.clear();
		
		SidebarString line = new SidebarString("");
		textList.add(line);
		line = new SidebarString("��e��ź ������");
		textList.add(line);
		String name = catcherName;
		if(name.length() > 10) name = name.substring(0, 10);
		line = new SidebarString("��a"+name);
		textList.add(line);
		line = new SidebarString("");
		textList.add(line);
		line = new SidebarString("��e�����ο� ��f: ��a"+ingamePlayer.size()+"��");
		textList.add(line);
		line = new SidebarString("");
		textList.add(line);
		
		ttbSidebar.setEntries(textList);
		ttbSidebar.update();
		
		/*for(String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p)) 
				ttbSidebar.showTo(p);
		}*/
	}


	public void initGame() {
		lobbyStart = false;
		ending = false;
		catcherName = "";
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
		
		if(ret == true) {
			server.egPM.printLog("[" + disPlayGameName + "] ���� �̻� ����");
			doneSetting = ret;
		}
		
		return ret;
	}
	
	public void onCommand(Player p, String[] cmd) {
		if (cmd.length <= 1) {
			p.sendMessage(ms + "[" + disPlayGameName + "] " + "/ttb join - ���� ����");
			p.sendMessage(ms + "[" + disPlayGameName + "] " + "/ttb quit - ���� ����");
			p.sendMessage(ms + "[" + disPlayGameName + "] " + "/ttb set - ���� ����");
		} else if (cmd[1].equalsIgnoreCase("set")) {
			if (cmd.length <= 2) {
				p.sendMessage(ms + "[" + disPlayGameName + "] " + "/ttb set loc- ���� ���� ����");
			} else if (cmd[2].equalsIgnoreCase("loc")) {
				if (cmd.length <= 3) {
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "/ttb set loc join - ���� ���� ��� ���� ����");
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "/ttb set loc block1 - 8x8ĭ�� ������1");
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "/ttb set loc block2 - 8x8ĭ�� ������2");
				} else if (cmd[3].equalsIgnoreCase("join")) {
					saveLocation(gameName, "JoinPos", p.getLocation());
					loadGameData();
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "���� ���� ��� ������ �����Ǿ����ϴ�.");
				}else if (cmd[3].equalsIgnoreCase("block1")) {
					saveLocation(gameName, "block1", p.getLocation());
					loadGameData();
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "������1�� �����Ǿ����ϴ�.");
				}else if (cmd[3].equalsIgnoreCase("block2")) {
					saveLocation(gameName, "block2", p.getLocation());
					loadGameData();
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "������2�� �����Ǿ����ϴ�.");
				}else {
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "/ttb set loc join - ���� ���� ��� ���� ����");
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "/ttb set loc block1 - 8x8ĭ�� ������1");
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "/ttb set loc block2 - 8x8ĭ�� ������2");
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
		} else if (cmd[1].equalsIgnoreCase("debug0")) {
			performence();
		}		
	}

	public void gameQuitPlayer(Player p, boolean announce, boolean giveGold) {
		if (ingamePlayer.contains(p.getName())) {
			ingamePlayer.remove(p.getName());
			server.playerList.put(p.getName(), "�κ�");
			p.getInventory().clear();
			updateSidebar();
			if (ingame) {
				if(catcherName.equalsIgnoreCase(p.getName())) {
					for(String name : ingamePlayer) {
						Player t = Bukkit.getPlayer(name);
						t.sendMessage(ms+"��ź �����ڸ� �缳���մϴ�.");
						mainSch.schTime2 = 0;
					}
				}
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
				if(ttbSidebar != null) ttbSidebar.hideFrom(p);
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
	
	public void performence() {
		EGScheduler sch = new EGScheduler(this);
		sch.schTime = 6;
		sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				if(sch.schTime> 0) {
					sch.schTime--;
					for(String pName : ingamePlayer) {
						Player t = Bukkit.getPlayer(pName);
						if(existPlayer(t)) {
							for(int i = 0; i < 3; i++) {
								Location tmpL = t.getLocation();
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
		mainSch.cancelTask(true);
		if(ingamePlayer.size() >= 1) {
			String winner = ingamePlayer.get(0);
			sendTitle("�¸�", ChatColor.GRAY + "����� ������ 1���Դϴ�.", 70);
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
							server.ms_alert + ChatColor.GRAY  + "��c"+winner+"��7���� ��a�¸���7�� ��c��ź �������7�� ���� �Ǿ����ϴ�.");
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
							server.ms_alert + ChatColor.GRAY  + "���ºη� ��c��ź �������7�� ���� �Ǿ����ϴ�.");
					}
				}, 200L);
			} catch(Exception e) {
					endGame(true);
			}
		}
	
	}

	public void endGame(boolean force) {
		if(force) {
			server.egCM.broadCast(server.ms_alert + "��ź ������ ������ ���� ���� �Ǿ����ϴ�.");
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
	public class EventHandlerTTB extends EGEventHandler {

		private TNTBomber game;

		public EventHandlerTTB(EGServer server, TNTBomber ttb) {
			super(server);
			this.game = ttb;
		}

		@EventHandler
		public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
			Player p = e.getPlayer();
			String[] cmd = e.getMessage().split(" ");
			if (cmd[0].equalsIgnoreCase("/ttb")) {
				e.setCancelled(true);
				if (p.isOp()) {
					game.onCommand(p, cmd);
				}
			} 
			if(ingamePlayer.contains(p.getName())) { //���� �����ؾ����� ������ ���ɾ�
				if(server.cmdSpawn.contains(cmd[0])) {
					gameQuitPlayer(p, true, false);
				}
			}
		}
		
		@EventHandler
		public void onPlayerHit(EntityDamageByEntityEvent e) {
			if(e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
				Player p = (Player)e.getEntity();
				Player d = (Player)e.getDamager();
				if(!(ingamePlayer.contains(p.getName()) && ingamePlayer.contains(d.getName()))) return;
				if(ingame) {
					if(catcherName.equalsIgnoreCase(d.getName())) {
						if(catcherNoAttack) {
							ActionBarAPI.sendActionBar(d, "��c��l���� ��ź�� �ѱ� �� �����ϴ�.", 70);
						}else {
							ActionBarAPI.sendActionBar(d, "��c��l��ź ��� ����!", 70);
							relieveCatcher(d);
							applyCatcher(p);
						}					
					} else {
						e.setCancelled(true);
					}
				} else {
					e.setCancelled(true);
				}
			}
		}

		@EventHandler
		public void onFall(EntityDamageEvent e) {
			if (e.getEntityType() == EntityType.PLAYER) {
				Player p = (Player) e.getEntity();
				DamageCause cause = e.getCause();
				if (ingamePlayer.contains(p.getName())) {
					e.setDamage(0.1);
					if(!ingame) {
				        if (cause.equals(DamageCause.VOID) && !ingame) { //���� ��� ������, ����		           
				            p.teleport(loc_Join, TeleportCause.PLUGIN);
				        }
					} else {
						if (cause.equals(DamageCause.VOID)) { //���� ��� ������, ����	
				            gameQuitPlayer(p, true, true);
				            server.spawn(p);
				        } else if(cause.equals(DamageCause.FALL)) {
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
		public void onToggleSneak(PlayerToggleSneakEvent e) {
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
	
	///////////////////// ��ź �����⿡ ������ �÷��̾�� Ŭ����
	private class TtbPlayer {
		
		public TtbPlayer(Player p, String job) {

		}

	}
}