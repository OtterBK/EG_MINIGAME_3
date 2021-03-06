package Minigames.HeroesWar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
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
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;
import org.bukkit.util.Vector;

import com.coloredcarrot.api.sidebar.Sidebar;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.connorlinfoot.titleapi.TitleAPI;
import com.gmail.filoghost.holographicdisplays.commands.main.subs.MovehereCommand;
import com.mysql.jdbc.Buffer;

import EGServer.EGServer;
import EGServer.DataManger.PlayerData;
import EGServer.ServerManager.EGEventHandler;
import EGServer.ServerManager.EGScheduler;
import Minigames.Minigame;
import Minigames.HeroesWar.Abilities.Ability;
import Minigames.HeroesWar.Abilities.Alchemist;
import Minigames.HeroesWar.Abilities.Archer;
import Minigames.HeroesWar.Abilities.Assassin;
import Minigames.HeroesWar.Abilities.BlackMagician;
import Minigames.HeroesWar.Abilities.Fighter;
import Minigames.HeroesWar.Abilities.Guardian;
import Minigames.HeroesWar.Abilities.Hider;
import Minigames.HeroesWar.Abilities.Hunter;
import Minigames.HeroesWar.Abilities.Knight;
import Minigames.HeroesWar.Abilities.Marksman;
import Minigames.HeroesWar.Abilities.Mercenary;
import Minigames.HeroesWar.Abilities.Monarch;
import Minigames.HeroesWar.Abilities.Monk;
import Minigames.HeroesWar.Abilities.Predator;
import Minigames.HeroesWar.Abilities.Priest;
import Minigames.HeroesWar.Abilities.Tracer;
import Minigames.HeroesWar.Abilities.Virtuous;
import Minigames.HeroesWar.Abilities.Warrior;
import Minigames.HeroesWar.Abilities.Wizard;
import Utility.MyUtility;
import Utility.SkullCreator;
import Utility.VirtualProjectile;
import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.ArcEffect;
import de.slikey.effectlib.effect.AtomEffect;
import de.slikey.effectlib.effect.BigBangEffect;
import de.slikey.effectlib.effect.BleedEffect;
import de.slikey.effectlib.effect.DnaEffect;
import de.slikey.effectlib.effect.EarthEffect;
import de.slikey.effectlib.effect.ExplodeEffect;
import de.slikey.effectlib.effect.FlameEffect;
import de.slikey.effectlib.effect.FountainEffect;
import de.slikey.effectlib.effect.GridEffect;
import de.slikey.effectlib.effect.HelixEffect;
import de.slikey.effectlib.effect.JumpEffect;
import de.slikey.effectlib.effect.LineEffect;
import de.slikey.effectlib.effect.LoveEffect;
import de.slikey.effectlib.effect.MusicEffect;
import de.slikey.effectlib.effect.ShieldEffect;
import de.slikey.effectlib.effect.SkyRocketEffect;
import de.slikey.effectlib.effect.SmokeEffect;
import de.slikey.effectlib.effect.StarEffect;
import de.slikey.effectlib.effect.TextEffect;
import de.slikey.effectlib.effect.TraceEffect;
import de.slikey.effectlib.effect.VortexEffect;
import de.slikey.effectlib.effect.WarpEffect;
import me.confuser.barapi.BarAPI;

public class HeroesWar_Ulraf extends HRWBase{

	// �̺�Ʈ��
	public EventHandlerHRW event;

	// �����̼�

	
	///������ �����̼�
	public int pointSetting = 0; //1 = point1��, 2= point1����1���� 3 = point1����2����, 4= point1��
	public int baseSetting = 0;
	public Location tmpWoolPos1;
	
	// ���� �÷��̾� ����Ʈ
	private List<String> teamChatList = new ArrayList<String>(40);
	
	//////////// ��ų ����
	public boolean skillBlock;

	//////// ���� ����
	public int gameStep = 0; //0 ���۴��  1 ������ 2 �������� 3�������� 4 ��������
	private String locPath;
	public Material pointBlock = Material.EMERALD_BLOCK;
	public Location loc_ending;
	
	public PotionEffect baseBoost = new PotionEffect(PotionEffectType.REGENERATION, 140, 4);
	
	///������
	public EGScheduler mainSch;	
	
	public HeroesWar_Ulraf(EGServer server, String gameName, String displayGameName, String cmdMain) {
		super(server);
		
		ItemStack item;
		ItemMeta meta;
		List<String> loreList;
		mainSch = new EGScheduler(this);
		//////////////////// �ʼ� ������
	
		ms = "��7[ ��e! ��7] ��f: ��c�������� ��f>> "; // �⺻ �޼���
		this.gameName = gameName;
		this.disPlayGameName = displayGameName;
		inventoryGameName = ChatColor.stripColor(disPlayGameName);
		minPlayer = 8;
		maxPlayer = 14;
		startCountTime = 70;
		this.cmdMain = cmdMain;
		dirSetting(gameName);
		
		point1 = new CatchPoint(this ,1, "��e��l����");
		point1.catchingTime = 5;
		
		doneSetting = loadGameData(); // ���� ���� ��ġ ��� �ε�
		/////////////////////// �ڵ� ����(�����۵��)
		
		item_hrwHelper = new ItemStack(Material.BOOK, 1);
		meta = item_hrwHelper.getItemMeta();
		meta.setDisplayName("��f[ ��b���� �޴� ��f]");
		loreList = new ArrayList<String>(1);
		loreList.add("��7- ��Ŭ���� ���������� ���� �޴��� ���ϴ�.");
		meta.setLore(loreList);
		item_hrwHelper.setItemMeta(meta);
		
		item_selectJob = new ItemStack(Material.PAPER, 1);
		meta = item_selectJob.getItemMeta();
		meta.setDisplayName("��f[ ��c���� ���� ��f]");
		loreList = new ArrayList<String>(1);
		loreList.add("��7- ��Ŭ���� ������ �����մϴ�.");
		meta.setLore(loreList);
		item_selectJob.setItemMeta(meta);
		
		/////////////////// ���� ���� �κ��丮
		inven_hrwHelper = Bukkit.createInventory(null, 9, ""+ChatColor.BLACK+ChatColor.BOLD+"��������");

		item = new ItemStack(Material.BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7- ��e��ų Ȯ�� ��7-");
		item.setItemMeta(meta);
		inven_hrwHelper.setItem(2, item);		
		meta.setDisplayName("��7- ��c�Ʊ� ���� Ȯ�� ��7-");
		item.setItemMeta(meta);
		inven_hrwHelper.setItem(4, item);
		meta.setDisplayName("��7- ��b���� ����̡�7-");
		item.setItemMeta(meta);
		inven_hrwHelper.setItem(6, item);
		////////////////
		
		/////////////////// ���� �����
		inven_gameHelper = Bukkit.createInventory(null, 27, "��0��l"+inventoryGameName+" �����");

		item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)12);
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
		meta.setDisplayName("��7- ��c�¸�����");
		loreList = new ArrayList<String>();
		loreList.add("��7- �¸������� Ȯ���մϴ�.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_gameHelper.setItem(11, item);
		item = new ItemStack(Material.BOOKSHELF, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7- ��c���ӱ�Ģ");
		loreList = new ArrayList<String>();
		loreList.add("��7- ������ �÷����ϸ�");
		loreList.add("��7  ���Ѿ��ϴ� ��Ģ�� Ȯ���մϴ�.");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_gameHelper.setItem(15, item);
		////////////////
		
		
		
		///////////
		

		locPath = server.getDataFolder().getPath() + "/" + gameName + "/Location";
		
		//////////////////////////
		event = new EventHandlerHRW(server, this);
		// �� �÷����ο� �̺�Ʈ ����
		server.getServer().getPluginManager().registerEvents(event, server);
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
			if(gameStep == 2 || gameStep == 3) {
				if(ingamePlayer.size() < maxPlayer) {
					breakInto(p);
				}else {
					p.sendMessage(server.ms_alert + "�̹� �ִ��ο��Դϴ�."); 
				}
			}else {
				p.sendMessage(server.ms_alert+"���� ������ �Ұ����մϴ�. ����� �õ��ϼ���.");
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
			sendMessage(server.ms_alert + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " ���� �����߽��ϴ�. " + ChatColor.RESET
					+ "[ " + ChatColor.GREEN + ingamePlayer.size() + " / "+ minPlayer + ChatColor.RESET + " ]");
			TitleAPI.sendTitle(p, 10, 70, 30,ChatColor.RED+""+disPlayGameName);
			sendSound(Sound.BLOCK_NOTE_PLING, 2.0f, 1.0f);
			p.teleport(loc_Join, TeleportCause.PLUGIN);
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
	public void startGame() {
		if (ingamePlayer.size() < minPlayer) {
			sendMessage(ms + "���� ���࿡ �ʿ��� �ּ��ο��� �������� ���߽��ϴ�.\n" + ms + "������ �����մϴ�.");
			//endGame(false);
			//return;
			// forceEnd();
		}
		server.egCM.broadCast(server.ms_alert + disPlayGameName+ "�� ���� �Ǿ����ϴ�!");
		initGame();
		ingame = true;
		gameStep = 1;
		
		//sendMessage(ms+"������ ���� �ƽ��ϴ�.");
		sendTitle(ChatColor.RED+"���� ����",  ChatColor.BLUE+""+disPlayGameName, 80);
		redTeam.InitAbCode(); //���� ���� �ڵ� �ʱ�ȭ
		blueTeam.InitAbCode(); ///���� ���� �ڵ� �ʱ�ȭ
		//// ������ ////
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				sendMessage(ms + "�� �������Դϴ�.");
				sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 2.0f);
				setTeam();
			}
		}, 80l);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				gameStep = 2;
				sendMessage(ms + "�� ������ ���۵˴ϴ�. ���̸� ��Ŭ���Ͽ� ������ �������ּ���.");
				for(String pName : ingamePlayer){
					Player p = Bukkit.getPlayer(pName);
					if(p == null || !p.isOnline()) continue;
					/*if(getTeam(p).equalsIgnoreCase("BLUE")) {
						p.teleport(blueTeam.loc_jobSelect);
					} else if(getTeam(p).equalsIgnoreCase("RED")) {
						p.teleport(redTeam.loc_jobSelect);
					}*/
					TitleAPI.sendFullTitle(p, 10, 80, 30, ChatColor.RED+"��l���� ����", ChatColor.RED+"��l������ �������ּ���");
					BarAPI.setMessage(p, ChatColor.GRAY+"���� ���۱���", 50);
					p.getInventory().setItem(0, item_selectJob);
					p.getInventory().setItem(8, item_hrwHelper);
					p.getInventory().setHeldItemSlot(0);		
					MyUtility.attackDelay(p, false);
					p.teleport(getTeamObject(p).loc_jobSelect, TeleportCause.PLUGIN);
					p.closeInventory();
					PlayerInventory pInv = p.getInventory();
					pInv.setItem(80, null);
					pInv.setItem(81, null);
					pInv.setItem(82, null);
					pInv.setItem(83	, null);
				}
				sendSound(Sound.BLOCK_NOTE_CHIME, 1.0f, 2.0f);
				basicSetting();
			}
		}, 200l);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				try {
					for(int i = 0; i < ingamePlayer.size(); i++) {
						Player p = Bukkit.getPlayer(ingamePlayer.get(i));
						if(existPlayer(p)) {
							removeItem(p, Material.PAPER, 64);
							p.setGameMode(GameMode.SURVIVAL);
							p.teleport(getTeamObject(p).loc_spawn, TeleportCause.PLUGIN);
							p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 110, 249));
							p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 110, 249));
						}
					}	
				} catch(Exception e) {
					
				}		
			}
		}, 1220l);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				sendTitle("��e��l5", "��c���� ���۱���", 30);
				for(int i = 0; i < ingamePlayer.size(); i++) {
					Player p = Bukkit.getPlayer(ingamePlayer.get(i));
					if(existPlayer(p)) {
						p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
					}
				}
			}
		}, 1240l);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				sendTitle("��e��l4", "��c���� ���۱���", 30);
				for(int i = 0; i < ingamePlayer.size(); i++) {
					Player p = Bukkit.getPlayer(ingamePlayer.get(i));
					if(existPlayer(p)) {
						p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
					}
				}
			}
		}, 1260l);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				sendTitle("��e��l3", "��c���� ���۱���", 30);
				for(int i = 0; i < ingamePlayer.size(); i++) {
					Player p = Bukkit.getPlayer(ingamePlayer.get(i));
					if(existPlayer(p)) {
						p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
					}
				}
			}
		}, 1280l);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				sendTitle("��e��l2", "��c���� ���۱���", 30);
				for(int i = 0; i < ingamePlayer.size(); i++) {
					Player p = Bukkit.getPlayer(ingamePlayer.get(i));
					if(existPlayer(p)) {
						p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
					}
				}
			}
		}, 1300l);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				sendTitle("��e��l1", "��c���� ���۱���", 30);
				for(int i = 0; i < ingamePlayer.size(); i++) {
					Player p = Bukkit.getPlayer(ingamePlayer.get(i));
					if(existPlayer(p)) {
						p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
					}
				}
			}
		}, 1320l);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {	
				sendTitle("��c��l���� ����", "", 60);
				mainSchedule();
				sendSound(Sound.ENTITY_ENDERDRAGON_AMBIENT, 1.5f, 1.5f);
				sendMessage(ms + "������ �����մϴ�! ����� ��� ���� �������� ��ȭ�ϰ�\n"
						+ms+"�������� �����Ͽ� �������� �¸��ϼ���!");
				for(int i = 0; i < ingamePlayer.size(); i++) {
					Player p = Bukkit.getPlayer(ingamePlayer.get(i));
					if(existPlayer(p)) {
						p.setWalkSpeed(0.25f);
					}								
				}	
				divideSetAbility();
			}
		}, 1340l);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				blueTeam.putOnStatItem();
				redTeam.putOnStatItem();	
			}
		},1400l);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				for(String pName : playerMap.keySet()) {
					Player t = Bukkit.getPlayer(pName);
					if(existPlayer(t)) {
						sendTitle("��c��l��ų Ȯ�ι�!", "��e��l�κ��丮�� ��a��lå ��Ŭ�� -> ��ų �����e��l Ŭ��", 120);
					}
					applyHealthBar();				
				}		
			}
		}, 1440l);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				for(String pName : playerMap.keySet()) {
					Player t = Bukkit.getPlayer(pName);
					if(existPlayer(t)) {
						sendTitle("��c��l", "��a��lå ��Ŭ�� -> �Ʊ� Ȯ�Ρ�e��l���� �Ʊ� Ȯ�� ����", 120);
					}
				}		
			}
		}, 1520l);
	}
	
	@Override
	public void gameQuitPlayer(Player p, boolean annouce, boolean isDead) {
		if (ingamePlayer.contains(p.getName())) {
			
			Team team = getTeamObject(p);
			if(team.selectAbilityMap.containsKey(p.getName())) {
				int tmpCode = team.selectAbilityMap.get(p.getName());
				team.leftAbCode.add(tmpCode);
				team.selectAbilityMap.remove(p.getName());
				team.jobList.getItem(tmpCode).removeEnchantment(Enchantment.DURABILITY);
			} 
			
			ingamePlayer.remove(p.getName());
			blueTeam.teamList.remove(p.getName());
			redTeam.teamList.remove(p.getName());	
			server.playerList.put(p.getName(), "�κ�");
			if (ingame && (gameStep == 3 || gameStep == 2)) {
				Ability ab = playerMap.get(p.getName()).ability;
				ab.gameQuit();
				ab.respawnSch.cancelTask(true);
				ab.team.respawning.remove(p.getName());
				if(blueTeam.teamList.size() <= 0) {
					server.egCM.broadCast(server.ms_alert+"�������� ������ ������� �÷��̾ �������� �ʾ�. ����Ǿ����ϴ�.");
					endGame(false);
				} else if(redTeam.teamList.size() <= 0) {
					server.egCM.broadCast(server.ms_alert+"�������� ������ �������� �÷��̾ �������� �ʾ�. ����Ǿ����ϴ�.");
					endGame(false);
				} 
				
			} else {
				ingamePlayer.remove(p.getName());
				if(annouce) {
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
			playerMap.remove(p.getName());
		}
	}
	
	//////////////////

	public void initGame() {
		point1.paintWoolBlocks((byte)0);
		
		point1.catchTeam = "�߸�";	
		
		point1.catchingPlayer = "";
		
		redTeam.leftSoul = 500;
		blueTeam.leftSoul = 500;
		playerMap.clear();
		teamChatList.clear();
		gameStep = 0;
		blueTeam.teamList.clear();
		redTeam.teamList.clear();	
		blueTeam.selectAbilityMap.clear();
		redTeam.selectAbilityMap.clear();
		blueTeam.carrotCnt = 0;
		redTeam.carrotCnt = 0;
		redTeam.statList.clear();
		blueTeam.statList.clear();
		redTeam.respawning.clear();
		blueTeam.respawning.clear();
		redTeam.reduceAbilityList.clear();
		blueTeam.reduceAbilityList.clear();
		
		clearClickMap();
		
		redTeam.slotNumMap.clear();
		blueTeam.slotNumMap.clear();
		
		for(ItemStack item : redTeam.jobList) {
			if(item != null) {
				if(item.getType() != Material.AIR) {
					item.removeEnchantment(Enchantment.DURABILITY)	;
				}
			}
		}
		for(ItemStack item : blueTeam.jobList) {
			if(item != null) {
				if(item.getType() != Material.AIR) {
					item.removeEnchantment(Enchantment.DURABILITY)	;
				}
			}
		}
	}

	public boolean loadGameData() {
		boolean ret = true;
		loc_Join = loadLocation(gameName, "JoinPos");
		blueTeam.loc_jobSelect = loadLocation(gameName, "blueJobSelect");
		redTeam.loc_jobSelect = loadLocation(gameName, "redJobSelect");
		blueTeam.loc_spawn = loadLocation(gameName, "blueSpawn");
		redTeam.loc_spawn = loadLocation(gameName, "redSpawn");
		blueTeam.loc_base1 = loadLocation(gameName, "blueBase1");
		blueTeam.loc_base2 = loadLocation(gameName, "blueBase2");
		redTeam.loc_base1 = loadLocation(gameName, "redBase1");
		redTeam.loc_base2 = loadLocation(gameName, "redBase2");
		if (loc_Join == null) {
			server.egPM.printLog("[" + disPlayGameName + "] ���� ���� ��� ������ �������� �ʾҽ��ϴ�.");
			ret = false;
		}
		if (blueTeam.loc_jobSelect == null) {
			server.egPM.printLog("[" + disPlayGameName + "] ����� ���� ���������� �������� �ʾҽ��ϴ�.");
			ret = false;
		}
		if (redTeam.loc_jobSelect == null) {
			server.egPM.printLog("[" + disPlayGameName + "] ������ ���� ���������� �������� �ʾҽ��ϴ�.");
			ret = false;
		}
		if (blueTeam.loc_spawn == null) {
			server.egPM.printLog("[" + disPlayGameName + "] ����� ���������� �������� �ʾҽ��ϴ�.");
			ret = false;
		}
		if (redTeam.loc_spawn == null) {
			server.egPM.printLog("[" + disPlayGameName + "] ������ ���������� �������� �ʾҽ��ϴ�.");
			ret = false;
		}
		if (blueTeam.loc_base1 == null) {
			server.egPM.printLog("[" + disPlayGameName + "] ����� ����1�������� �������� �ʾҽ��ϴ�.");
			ret = false;
		}
		if (blueTeam.loc_base2 == null) {
			server.egPM.printLog("[" + disPlayGameName + "] ����� ����1�������� �������� �ʾҽ��ϴ�.");
			ret = false;
		}
		if (redTeam.loc_base1 == null) {
			server.egPM.printLog("[" + disPlayGameName + "] ������ ����1�������� �������� �ʾҽ��ϴ�.");
			ret = false;
		}
		if (redTeam.loc_base2 == null) {
			server.egPM.printLog("[" + disPlayGameName + "] ������ ����2�������� �������� �ʾҽ��ϴ�.");
			ret = false;
		}
		blueTeam.sortBaseLoc();
		redTeam.sortBaseLoc();
		if(!point1.loadData(server.getDataFolder().getPath() + "/" + gameName + "/Location", "location.yml")) {
			server.egPM.printLog("[" + disPlayGameName + "] 1�� �������� �������� �ʾҽ��ϴ�.");
			ret = false;
		}
		
		if(ret == true) {
			server.egPM.printLog("[" + disPlayGameName + "] ���� �̻� ����");
			doneSetting = ret;
			
		}
		
		return ret;
	}
	
	/////////// �κ��丮 Ŭ�� ////////////////
	
	public void gameHelper(Player p, int slot) {
		switch (slot) {
		case 11:
			p.sendMessage("��7���� �̸� ��f: ��c�������� ������Ʈ");
			p.sendMessage("��f������ ������ �����Ͽ� �¸������� �޼��ϴ�");
			p.sendMessage("��f��, ����, ��������Դϴ�.");
			p.sendMessage("��f������ ������, ����� �� �������� ������ ����Ǹ�");
			p.sendMessage("��f��������� ������ ��ȥ�� ������ 0���� ����� �¸��մϴ�.");
			p.sendMessage("��f����� ��ȥ�� ���ֱ� ���ؼ��� ��");
			p.sendMessage("��f�߾ӿ� �����ϴ� ������ �����ؾ��մϴ�.");
			p.closeInventory();
			return;
			
		case 15:
			p.sendMessage("��f�������� �Ʊ����ش� �����Դϴ�.");
			p.closeInventory();
			return;

		default:
			return;
		}
	}


	/////////////////////////////////
	
	public void breakInto(Player p) {
		server.waitingPlayer.put(p.getName(),disPlayGameName);
		MyUtility.allClear(p);
		p.setFoodLevel(20);
		p.closeInventory();
		ingamePlayer.add(p.getName());
		server.spawnList.remove(p.getName());
		server.playerList.put(p.getName(), disPlayGameName);
		p.getInventory().setItem(8, helpItem);
		p.getInventory().setHeldItemSlot(8);
		sendMessage(server.ms_alert + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " ���� �����߽��ϴ�.");
		TitleAPI.sendTitle(p, 10, 70, 30,ChatColor.RED+""+disPlayGameName);
		sendSound(Sound.BLOCK_NOTE_PLING, 2.0f, 1.0f);
		
		if(redTeam.teamList.size() < blueTeam.teamList.size()) {
			setRedTeam(p);
		}else {
			setBlueTeam(p);
		}
		
		if(gameStep == 2) {
			p.sendMessage(ms + "�ߵ����� �ϼ̽��ϴ�. ���̸� ��Ŭ���Ͽ� ������ �������ּ���.\n"+ms+"�� ������ ���۵˴ϴ�.");
			TitleAPI.sendFullTitle(p, 10, 80, 30, ChatColor.RED+"��l���� ����", ChatColor.RED+"��l������ �������ּ���");
			p.getInventory().setItem(0, item_selectJob);
			p.getInventory().setItem(8, item_hrwHelper);
			p.getInventory().setHeldItemSlot(0);		
			MyUtility.attackDelay(p, false);
			p.teleport(getTeamObject(p).loc_jobSelect, TeleportCause.PLUGIN);
			p.closeInventory();
			PlayerInventory pInv = p.getInventory();
			pInv.setItem(80, null);
			pInv.setItem(81, null);
			pInv.setItem(82, null);
			pInv.setItem(83	, null);
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_CHIME, 1.0f, 2.0f);
		}else if(gameStep == 3){
			p.sendMessage(ms + "�ߵ����� �ϼ̽��ϴ�. ���̸� ��Ŭ���Ͽ� ������ �������ּ���.");
			TitleAPI.sendFullTitle(p, 10, 80, 30, ChatColor.RED+"��l���� ����", ChatColor.RED+"��l������ �������ּ���");
			p.getInventory().setItem(0, item_selectJob);
			p.getInventory().setItem(8, item_hrwHelper);
			p.getInventory().setHeldItemSlot(0);		
			MyUtility.attackDelay(p, false);
			p.teleport(getTeamObject(p).loc_jobSelect, TeleportCause.PLUGIN);
			p.closeInventory();
			PlayerInventory pInv = p.getInventory();
			pInv.setItem(80, null);
			pInv.setItem(81, null);
			pInv.setItem(82, null);
			pInv.setItem(83	, null);
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_CHIME, 1.0f, 2.0f);
		}
	}
	
	public void mainSchedule() {
		
		mainSch.schTime = 0;
		mainSch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				if(!ingame) mainSch.cancelTask(true);
				
				List<String> keySet = new ArrayList<String>(playerMap.keySet());
				
				if(mainSch.schTime < 0) {
					mainSch.schTime = 2;
					int blueOccupy = 0;
					int redOccupy = 0;
					
					if(point1.catchTeam.equalsIgnoreCase("BLUE")) {
						blueOccupy += 1;
					} else if(point1.catchTeam.equalsIgnoreCase("RED")) {
						redOccupy += 1;
					}
					
					blueTeam.leftSoul -= redOccupy;
					redTeam.leftSoul -= blueOccupy;			
					
					if(blueTeam.leftSoul <= 0) {						
						mainSch.cancelTask(true);
						redTeamWin();					
					} else if(redTeam.leftSoul <= 0) {
						mainSch.cancelTask(true);
						blueTeamWin();
					}
					
					for(String pName : keySet) {
						Player p = Bukkit.getPlayer(pName);
						if(existPlayer(p))
							setBar(p);
					}
				} else {
					mainSch.schTime -= 1;
				}
				for(String pName : keySet) {
					Ability ab = playerMap.get(pName).ability;
					ab.setUI_Ulaf();
					ab.per_Ultimate_Now += 1;
				}			
			}
		}, 0l, 5l);
		
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
							}  else if (cmd[3].equalsIgnoreCase("bluejob")) {
								saveLocation(gameName, "blueJobSelect", p.getLocation());
								p.sendMessage("[" + disPlayGameName + "] " + "����� ������������ �����Ϸ�");
							} else if (cmd[3].equalsIgnoreCase("redjob")) {
								saveLocation(gameName, "redJobSelect", p.getLocation());
								p.sendMessage("[" + disPlayGameName + "] " + "������ ������������ �����Ϸ�");
							} else if (cmd[3].equalsIgnoreCase("blueSpawn")) {
								saveLocation(gameName, "blueSpawn", p.getLocation());
								p.sendMessage("[" + disPlayGameName + "] " + "����� �������� �����Ϸ�");
							} else if (cmd[3].equalsIgnoreCase("redSpawn")) {
								saveLocation(gameName, "redSpawn", p.getLocation());
								p.sendMessage("[" + disPlayGameName + "] " + "������ �������� �����Ϸ�");
							} else if (cmd[3].equalsIgnoreCase("blueTotem")) {
								saveLocation(gameName, "blueTotem", p.getLocation());
								p.sendMessage("[" + disPlayGameName + "] " + "����� ���� �����Ϸ�");
							} else if (cmd[3].equalsIgnoreCase("redTotem")) {
								saveLocation(gameName, "redTotem", p.getLocation());
								p.sendMessage("[" + disPlayGameName + "] " + "������ ���� �����Ϸ�");
							} else if (cmd[3].equalsIgnoreCase("buffer")) {
								saveLocation(gameName, "buffer", p.getLocation());
								p.sendMessage("[" + disPlayGameName + "] " + "���ۺ� �����Ϸ�");
							} else if(cmd[3].equalsIgnoreCase("point1")) {
								pointSetting = 1;
								p.sendMessage("[" + disPlayGameName + "] " + "�������� ����� �� �������ּ���.");
							} else if(cmd[3].equalsIgnoreCase("bluebase")) {
								baseSetting = 1;
								p.sendMessage("[" + disPlayGameName + "] " + "����� ���� 1�������� �������ּ���.");
							} else if(cmd[3].equalsIgnoreCase("redbase")) {
								baseSetting = 3;
								p.sendMessage("[" + disPlayGameName + "] " + "������ ���� 1�������� �������ּ���.");
							} else {
								p.sendMessage("[" + disPlayGameName + "] " + cmd[3]+"�� �ùٸ��� ���� �μ�");
							}
							loadGameData();
						} else {
							p.sendMessage(ms+"�μ��� �Է����ּ���.");
						}
					} else {
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain +" set loc join - ���� ���� ��� ���� ����");
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain +" set loc bluejob - ����� ������������");
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain +" set loc redjob - ������ �������� ����");
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain +" set loc bluespawn - ����� ����");
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain +" set loc redspawn - ������ ����");
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain +" set loc bluetotem - ����� ����");
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain +" set loc redtotem - ������ ����");
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain +" set loc bluebase - ����� ����");
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain +" set loc redbase - ������ ����");
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain +" set loc buffer - ���� ��");
					}
				} else {
					p.sendMessage("[" + disPlayGameName + "] " + cmdMain +" set loc- ���� ���� ����");
				}
			} else if (cmd[1].equalsIgnoreCase("join")) {
				if(cmd.length > 2) joinGame(Bukkit.getPlayer(cmd[2]));
				else joinGame(p);
			} else if (cmd[1].equalsIgnoreCase("forceend")) {
				endGame(true);
			}else if (cmd[1].equalsIgnoreCase("quit")) {
				gameQuitPlayer(p, true, false);
			} else if (cmd[1].equalsIgnoreCase("block")) {
				if(joinBlock) joinBlock = false;
				else joinBlock = true;
			} else if (cmd[1].equalsIgnoreCase("start")) {
				startCount();
			} else if (cmd[1].equalsIgnoreCase("debug2")) {
				playerMap.remove(p.getName());
				blueTeam.teamList.remove(p.getName());
				ingamePlayer.remove(p.getName());
				setBlueTeam(p);
				ingame = true;
				gameStep = 3;
				ingamePlayer.add(p.getName());
				server.playerList.put(p.getName(), "�׽�Ʈ");
				server.spawnList.remove(p.getName());
				p.sendMessage("�����Ϸ�");
				if(cmd[2].equalsIgnoreCase("1")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Tracer(this, p, "������", blueTeam), blueTeam));
				} else if(cmd[2].equalsIgnoreCase("2")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Assassin(this, p, "�ڰ�", blueTeam), blueTeam));
				} else if(cmd[2].equalsIgnoreCase("3")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Hunter(this, p, "��ɲ�", blueTeam), blueTeam));
				} else if(cmd[2].equalsIgnoreCase("4")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Mercenary(this, p, "�뺴", blueTeam), blueTeam));
				} else if(cmd[2].equalsIgnoreCase("5")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Archer(this, p, "�ü�", blueTeam), blueTeam));
				} else if(cmd[2].equalsIgnoreCase("6")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Wizard(this, p, "������", blueTeam), blueTeam));
				} else if(cmd[2].equalsIgnoreCase("7")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Hider(this, p, "������", blueTeam), blueTeam));
				} else if(cmd[2].equalsIgnoreCase("8")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Virtuous(this, p, "����", blueTeam), blueTeam));
				} else if(cmd[2].equalsIgnoreCase("9")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Marksman(this, p, "���", blueTeam), blueTeam));
				} else if(cmd[2].equalsIgnoreCase("10")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Guardian(this, p, "��ȣ��", blueTeam), blueTeam));
				} else if(cmd[2].equalsIgnoreCase("11")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Monk(this, p, "������", blueTeam), blueTeam));
				} else if(cmd[2].equalsIgnoreCase("12")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Knight(this, p, "���", blueTeam), blueTeam));
				} else if(cmd[2].equalsIgnoreCase("13")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Fighter(this, p, "������", blueTeam), blueTeam));
				} else if(cmd[2].equalsIgnoreCase("14")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Monarch(this, p, "����", blueTeam), blueTeam));
				} else if(cmd[2].equalsIgnoreCase("15")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Predator(this, p, "������", blueTeam), blueTeam));
				} else if(cmd[2].equalsIgnoreCase("16")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Priest(this, p, "����", blueTeam), blueTeam));
				} else if(cmd[2].equalsIgnoreCase("17")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Alchemist(this, p, "���ݼ���", blueTeam), blueTeam));
				} else if(cmd[2].equalsIgnoreCase("18")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new BlackMagician(this, p, "�渶����", blueTeam), blueTeam));
				} else if(cmd[2].equalsIgnoreCase("19")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Warrior(this, p, "����", blueTeam), blueTeam));
				} 
				playerMap.get(p.getName()).ability.ultimate = true;
				playerMap.get(p.getName()).ability.passive = true;
			} else if (cmd[1].equalsIgnoreCase("debug3")) {
				playerMap.remove(p.getName());
				redTeam.teamList.remove(p.getName());
				ingamePlayer.remove(p.getName());
				setRedTeam(p);
				ingame = true;
				gameStep = 3;
				ingamePlayer.add(p.getName());
				server.playerList.put(p.getName(), "�׽�Ʈ");
				server.spawnList.remove(p.getName());
				p.sendMessage("�����Ϸ�");
				if(cmd[2].equalsIgnoreCase("1")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Tracer(this, p, "������", redTeam), redTeam));
				} else if(cmd[2].equalsIgnoreCase("2")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Assassin(this, p, "�ڰ�", redTeam), redTeam));
				} else if(cmd[2].equalsIgnoreCase("3")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Hunter(this, p, "��ɲ�", redTeam), redTeam));
				} else if(cmd[2].equalsIgnoreCase("4")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Mercenary(this, p, "�뺴", redTeam), redTeam));
				} else if(cmd[2].equalsIgnoreCase("5")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Archer(this, p, "�ü�", redTeam), redTeam));
				} else if(cmd[2].equalsIgnoreCase("6")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Wizard(this, p, "������", redTeam), redTeam));
				} else if(cmd[2].equalsIgnoreCase("7")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Hider(this, p, "������", redTeam), redTeam));
				} else if(cmd[2].equalsIgnoreCase("8")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Virtuous(this, p, "����", redTeam), redTeam));
				} else if(cmd[2].equalsIgnoreCase("9")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Marksman(this, p, "���", redTeam), redTeam));
				} else if(cmd[2].equalsIgnoreCase("10")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Guardian(this, p, "��ȣ��", redTeam), redTeam));
				} else if(cmd[2].equalsIgnoreCase("11")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Monk(this, p, "������", redTeam), redTeam));
				} else if(cmd[2].equalsIgnoreCase("12")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Knight(this, p, "���", redTeam), redTeam));
				} else if(cmd[2].equalsIgnoreCase("13")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Fighter(this, p, "������", redTeam), redTeam));
				} else if(cmd[2].equalsIgnoreCase("14")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Monarch(this, p, "����", redTeam), redTeam));
				} else if(cmd[2].equalsIgnoreCase("15")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Predator(this, p, "������", redTeam), redTeam));
				} else if(cmd[2].equalsIgnoreCase("16")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Priest(this, p, "����", redTeam), redTeam));
				} else if(cmd[2].equalsIgnoreCase("17")) {
					playerMap.put(p.getName(), new HRWPlayer(p, new Alchemist(this, p, "���ݼ���", redTeam), redTeam));
				} 
				playerMap.get(p.getName()).ability.ultimate = true;
				playerMap.get(p.getName()).ability.passive = true;
			}else if (cmd[1].equalsIgnoreCase("debug4")) {
				ingame = true;
				mainSchedule();
			}else if (cmd[1].equalsIgnoreCase("debug6")) {
				applyHealthBar();
			}else if(cmd[1].equalsIgnoreCase("debug7")) {
				 p.sendMessage(playerMap.get(p.getName()).ability.checkWeapon(p.getInventory().getContents())+"");
			}else if(cmd[1].equalsIgnoreCase("debug8")) {
				playerMap.get(p.getName()).ability.updateStatItem();
				p.sendMessage(playerMap.get(p.getName()).ability.statLoreList.size()+"");
				 p.sendMessage(playerMap.get(p.getName()).ability.statLoreList+"");
				 p.getInventory().addItem(playerMap.get(p.getName()).ability.statItem);
			}
		}  else {
			p.sendMessage("[" + disPlayGameName + "] " + cmdMain +" join - ���� ����");
			p.sendMessage("[" + disPlayGameName + "] " + cmdMain +" quit - ���� ����");
			p.sendMessage("[" + disPlayGameName + "] " + cmdMain +" set - ���� ����");
		}
	}
		
		  public void writePentagon(Location loc, Location loc2, Location loc3, Location loc4, Location loc5)
		  {
		    EffectManager eManager = new EffectManager(server);

		    LineEffect effect = new LineEffect(eManager);
		    LineEffect effect1 = new LineEffect(eManager);
		    LineEffect effect2 = new LineEffect(eManager);
		    LineEffect effect3 = new LineEffect(eManager);
		    LineEffect effect4 = new LineEffect(eManager);
		    LineEffect effect5 = new LineEffect(eManager);
		    LineEffect effect6 = new LineEffect(eManager);
		    LineEffect effect7 = new LineEffect(eManager);
		    LineEffect effect8 = new LineEffect(eManager);
		    LineEffect effect9 = new LineEffect(eManager);

		    effect.setLocation(loc);
		    effect.setTargetLocation(loc2);
		    effect1.setLocation(loc);
		    effect1.setTargetLocation(loc3);
		    effect2.setLocation(loc);
		    effect2.setTargetLocation(loc4);
		    effect3.setLocation(loc);
		    effect3.setTargetLocation(loc5);
		    effect4.setLocation(loc2);
		    effect4.setTargetLocation(loc3);
		    effect5.setLocation(loc2);
		    effect5.setTargetLocation(loc4);
		    effect6.setLocation(loc2);
		    effect6.setTargetLocation(loc5);
		    effect7.setLocation(loc3);
		    effect7.setTargetLocation(loc4);
		    effect8.setLocation(loc3);
		    effect8.setTargetLocation(loc5);
		    effect9.setLocation(loc4);
		    effect9.setTargetLocation(loc5);

		    effect.particle = Particle.REDSTONE;
		    effect1.particle = Particle.REDSTONE;
		    effect2.particle = Particle.REDSTONE;
		    effect3.particle = Particle.REDSTONE;
		    effect4.particle = Particle.REDSTONE;
		    effect5.particle = Particle.REDSTONE;
		    effect6.particle = Particle.REDSTONE;
		    effect7.particle = Particle.REDSTONE;
		    effect8.particle = Particle.REDSTONE;
		    effect9.particle = Particle.REDSTONE;

		    effect.start();
		    effect1.start();
		    effect2.start();
		    effect3.start();
		    effect4.start();
		    effect5.start();
		    effect6.start();
		    effect7.start();
		    effect8.start();
		    effect9.start();
		  }
	
	public void basicSetting() {
		blueTeam.setCarrot();
		redTeam.setCarrot();
	}
	
	public List<String> getEnemyList(String pName) {
		if(getTeam(pName).equalsIgnoreCase("BLUE")) {
			return redTeam.teamList;
		} else if(getTeam(pName).equalsIgnoreCase("RED")) {
			return blueTeam.teamList;
		}
		return ingamePlayer;
	}
	
	public void updateScoreboard() {
		
	}
	
	public void giveCarrot(List<String> list, String reason, int amt) {
		/*for(String pName : list) {
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p)) {
				p.getInventory().addItem(new ItemStack(Material.GOLDEN_CARROT,amt));
				if(reason != null) 
					ActionBarAPI.sendActionBar(p, "��c��l"+reason, 80);				
			}
		}*/
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
	
	public void setBlueTeam(Player p) {
		blueTeam.add(p);

		//backuptlist.put(p.getName(), "���");
	}
	
	public void setRedTeam(Player p) {
		redTeam.add(p);
		//backuptlist.put(p.getName(), "���");
	}

	public Team getTeamObject(Player p) {
		if(redTeam.teamList.contains(p.getName()))
			return redTeam;
		else return blueTeam;
	}
	
	public String getTeam(Player p) {

		if(redTeam.teamList.contains(p.getName())){
			return "RED";	
		} else if(blueTeam.teamList.contains(p.getName())){
			return "BLUE";
		} else {
			return "����";
		}		
	}
	
	public String getTeam(String pName) {
		if(playerMap.containsKey(pName)){
			return playerMap.get(pName).team.teamName;
		}
		
		return "����";
	}
	
	public void spawn(Player p) {
		if(!ingame) return;
		p.setGameMode(GameMode.SURVIVAL);
		if(playerMap.containsKey(p.getName())) {
			playerMap.get(p.getName()).team.spawn(p);
		} else {
			gameQuitPlayer(p, true, false);
			server.spawn(p);
		}
	}	
	
	public void setBar(Player p) {
		String team = getTeam(p);
		int bluePercent = (int)(((float) blueTeam.leftSoul/blueTeam.maxLeftSoul)*100) < 2 ? (int)(((float) blueTeam.leftSoul/blueTeam.maxLeftSoul)*100) : 2;
		int redPercent = (int)(((float) redTeam.leftSoul/redTeam.maxLeftSoul)*100) < 2 ? (int)(((float) redTeam.leftSoul/redTeam.maxLeftSoul)*100) : 2;
		try{BarAPI.setMessage(p, ChatColor.BLUE+"����� ��ȥ ��7- ��6"+blueTeam.leftSoul+" ��f| "+ChatColor.RED+"������ ��ȥ ��7- ��6 "+redTeam.leftSoul,
				(team.equalsIgnoreCase("BLUE") ? bluePercent : redPercent));
		}catch(Exception e) {
			
		}
	}
	
	////////////�κ��丮 Ŭ�� ����
	
	
	
	public void abilitySelect(Player p, int slotNum) {
		if(abCodeList.contains(slotNum)) {
			p.closeInventory();
			selectAb(p, slotNum);
		}
	}
	
	public void selectAb(Player p, int slotNum) {
		Team team = getTeamObject(p);
		if(team.leftAbCode.contains(slotNum)) {
			if(team.selectAbilityMap.containsKey(p.getName())) {
				int tmpCode = team.selectAbilityMap.get(p.getName());
				team.leftAbCode.add(tmpCode);
				team.selectAbilityMap.remove(p.getName());
				team.jobList.getItem(tmpCode).removeEnchantment(Enchantment.DURABILITY);
			} 
			team.leftAbCode.remove(Integer.valueOf(slotNum));
			team.selectAbilityMap.put(p.getName(), slotNum);
			TitleAPI.sendFullTitle(p, 10, 60, 10, "", "��a��l"+getAbiilityNameFromCode(slotNum) + " ���� �Ϸ�");
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BELL, 1.0F, 2.0F);
			team.jobList.getItem(slotNum).addUnsafeEnchantment(Enchantment.DURABILITY, 1);
			if(gameStep == 3) {
				setAbility(p);
				p.teleport(getTeamObject(p).loc_spawn, TeleportCause.PLUGIN);
				Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
					public void run() {
						Ability ab = playerMap.get(p.getName()).ability;
						ab.applyItems();
						Objective tmpO = ab.sidebar.getTheScoreboard().getObjective("health");
						if(tmpO != null) {
							tmpO.unregister();
						}
						Objective o = ab.sidebar.getTheScoreboard().registerNewObjective("health", "health");
						o.setDisplayName("��cHP");
						o.setDisplaySlot(DisplaySlot.BELOW_NAME);
					}
				}, 20);
			}
		} else {
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 2.0F);
			TitleAPI.sendFullTitle(p, 10, 60, 10, "", ChatColor.RED+"�̹� ���õ� �����Դϴ�.");
		}
	}
	
	/////////
	
	public void divideSetAbility() {
		List<String> tmpList = new ArrayList<String>(ingamePlayer);
		EGScheduler sch = new EGScheduler(this);
		sch.schTime = tmpList.size();
		sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				if(sch.schTime > 0) {
					sch.schTime--;
					Player p = Bukkit.getPlayer(tmpList.get(sch.schTime));
					if(existPlayer(p)) {
						setAbility(p);
					}
				} else {
					sch.cancelTask(true);
				}
			}
		}, 1l, 1l);
	}
	
	public void setAbility(Player p) {
		Team team = getTeamObject(p);
		if(!team.selectAbilityMap.containsKey(p.getName())) {
			int rnCode = team.leftAbCode.get(MyUtility.getRandom(0, team.leftAbCode.size()-1));
			team.selectAbilityMap.put(p.getName(), rnCode);
			team.leftAbCode.remove(Integer.valueOf(rnCode));
			team.jobList.getItem(rnCode).addUnsafeEnchantment(Enchantment.DURABILITY, 1);	
		} 
		switch(team.selectAbilityMap.get(p.getName())) {
		case 3: playerMap.put(p.getName(), new HRWPlayer(p, new Tracer(this, p, "������", team), team)); break;
		case 4: playerMap.put(p.getName(), new HRWPlayer(p, new Assassin(this, p, "�ڰ�", team), team)); break;
		case 5: playerMap.put(p.getName(), new HRWPlayer(p, new Hunter(this, p, "��ɲ�", team), team)); break;
		case 6: playerMap.put(p.getName(), new HRWPlayer(p, new Mercenary(this, p, "�뺴", team), team)); break;
		case 7: playerMap.put(p.getName(), new HRWPlayer(p, new Archer(this, p, "�ü�", team), team)); break;
		case 12: playerMap.put(p.getName(), new HRWPlayer(p, new Wizard(this, p, "������", team), team)); break;
		case 13: playerMap.put(p.getName(), new HRWPlayer(p, new Hider(this, p, "������", team), team)); break;
		case 14: playerMap.put(p.getName(), new HRWPlayer(p, new Virtuous(this, p, "����", team), team)); break;
		case 15: playerMap.put(p.getName(), new HRWPlayer(p, new Marksman(this, p, "���ݼ�", team), team)); break;
		
		case 21: playerMap.put(p.getName(), new HRWPlayer(p, new Guardian(this, p, "��ȣ��", team), team)); break;
		case 22: playerMap.put(p.getName(), new HRWPlayer(p, new Monk(this, p, "������", team), team)); break;
		case 23: playerMap.put(p.getName(), new HRWPlayer(p, new Knight(this, p, "���", team), team)); break;
		case 24: playerMap.put(p.getName(), new HRWPlayer(p, new Fighter(this, p, "������", team), team)); break;
		case 25: playerMap.put(p.getName(), new HRWPlayer(p, new Monarch(this, p, "����", team), team)); break;
		case 30: playerMap.put(p.getName(), new HRWPlayer(p, new Predator(this, p, "������", team), team)); break;
		case 31: playerMap.put(p.getName(), new HRWPlayer(p, new Warrior(this, p, "����", team), team)); break;
		
		case 39: playerMap.put(p.getName(), new HRWPlayer(p, new Priest(this, p, "����", team), team)); break;
		case 40: playerMap.put(p.getName(), new HRWPlayer(p, new Alchemist(this, p, "���ݼ���", team), team)); break;
		case 41: playerMap.put(p.getName(), new HRWPlayer(p, new BlackMagician(this, p, "�渶����", team), team)); break;
		
		default: p.sendMessage(ms+"�������� �ʴ� �����ڵ�("+team.selectAbilityMap.get(p.getName())+"�Դϴ�. - �����ڿ��� �������ּ���.");
		}
		HRWPlayer hrwp = playerMap.get(p.getName());
		hrwp.ability.hrwData = hrwp;
		hrwp.ability.passive = true;
		hrwp.ability.setPercentMode(true);
		hrwp.ability.ultimate = true;
		//reConnectMap.put(p.getName(), hrwp);
	}
	
	public void applyHealthBar() {
		for(String name : playerMap.keySet()){
			Ability ab = playerMap.get(name).ability;
			Objective tmpO = ab.sidebar.getTheScoreboard().getObjective("health");
			if(tmpO != null) {
				tmpO.unregister();
			}
			Objective o = ab.sidebar.getTheScoreboard().registerNewObjective("health", "health");
			o.setDisplayName("��cHP");
			o.setDisplaySlot(DisplaySlot.BELOW_NAME);
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				gameStep = 3;
				for(String name : playerMap.keySet()){
					Player t = Bukkit.getPlayer(name);
					if(existPlayer(t)) {
						//t.damage(0.01);
					}
				}
			}
		}, 20l);
	}
	
	public void sendStackMessage(String pName, int stack) {
		if(stack == 2) {
			sendMessage(ms+"��b"+pName+" -> ��e2�� ����óġ!");
		} else if(stack == 3) {
			sendMessage(ms+"��b"+pName+" -> ��e3�� ����óġ!");
		} else if(stack == 4) {
			sendMessage(ms+"��b"+pName+" -> ��e����! 4�� ����óġ!");
		} else if(stack >= 5) {
			sendMessage(ms+"��b"+pName+" -> ��e���� ų��! "+stack+"�� ����óġ!");
		}
	}
	
	public void occupyPoint(Player p, Location l) {
		String team = getTeam(p);
		//server.egPM.printLog(""+l.getBlockX()+"/"+l.getBlockY()+"/"+l.getBlockZ());
		boolean isNeute = false;
		
		if(MyUtility.compareLocation(point1.pointLoc, l)){
			point1.occupied(p, team, isNeute);
		}
	}
	
	public void performence2() {
		sendSound(Sound.BLOCK_NOTE_PLING);
		for(String pName : playerMap.keySet()) {	
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p)) {
				HRWPlayer hrwp = playerMap.get(pName);
				hrwp.ability.updateStatItem();
				for(String msg : hrwp.ability.statLoreList) {
					p.sendMessage(msg);
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
						Player p = Bukkit.getPlayer(pName);
						if(existPlayer(p)) {
							Location tmpL = p.getLocation();
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
	
	public void blueTeamWin() {
		
		gameStep = 4;
		sendMessage(ms + ChatColor.GRAY + "\n�������� ��ȥ�� ���� �����ƽ��ϴ�! ������� �¸��߽��ϴ�!");
		performence();
		for(String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p))
				MyUtility.allClear(p);
		}
		try {
			Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
				public void run() {
					for (String pName : ingamePlayer) {
						Player p = Bukkit.getPlayer(pName);
						playerMap.get(pName).ability.respawnSch.cancelTask(true);
						if(existPlayer(p)) {
							if(getTeam(p).equalsIgnoreCase("BLUE")) {
								//server.egGM.giveGold(p.getName(), 200);
								//p.sendMessage(ms + "�¸� �������� 200��带 �����̽��ϴ�.");
							} else {
								//server.egGM.giveGold(p.getName(), 150);
								//p.sendMessage(ms + "���� ���� �������� �������� 150��带 �����̽��ϴ�.");
							}
						}
					}
				}
			}, 100L);
		} catch (Exception e) {
			
		}
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				performence2();
			}
		}, 160L);
		
		try {
			Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
				public void run() {
					divideSpawn();
					endGame(false);
					server.egCM.broadCast(server.ms_alert + ChatColor.GRAY + "������� �¸��� "+disPlayGameName+"�� ���� �Ǿ����ϴ�.");
				}
			}, 300L);
		} catch (Exception e) {
			endGame(true);
		}	
	}
	
	public void redTeamWin() {
		gameStep = 4;
		sendMessage(ms + ChatColor.GRAY + "\n������� ��ȥ�� ���� �����ƽ��ϴ�! �������� �¸��߽��ϴ�!");
		performence();
		for(String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p))
				MyUtility.allClear(p);
		}
		try {
			Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
				public void run() {
					for (String pName : ingamePlayer) {
						Player p = Bukkit.getPlayer(pName);
						playerMap.get(pName).ability.respawnSch.cancelTask(true);
						if(existPlayer(p)) {
							if(getTeam(p).equalsIgnoreCase("RED")) {
								//server.egGM.giveGold(p.getName(), 200);
								//p.sendMessage(ms + "�¸� �������� 200��带 �����̽��ϴ�.");
							} else {
								//server.egGM.giveGold(p.getName(), 150);
								//p.sendMessage(ms + "���� ���� �������� �������� 150��带 �����̽��ϴ�.");
							}
						}
					}
				}
			}, 100L);
		} catch (Exception e) {
			
		}
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				performence2();
			}
		}, 160L);
		
		try {
			Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
				public void run() {
					divideSpawn();
					endGame(false);
					server.egCM.broadCast(server.ms_alert + ChatColor.GRAY + "�������� �¸��� "+disPlayGameName+"�� ���� �Ǿ����ϴ�.");
				}
			}, 300L);
		} catch (Exception e) {
			endGame(true);
		}
	}

	public void endGame(boolean force) {
		if(force) {
			server.egCM.broadCast(server.ms_alert + disPlayGameName+ " ������ ���� ���� �Ǿ����ϴ�.");
		}
		
		if(redTeam.predator_portal1 != null && redTeam.predator_portal1_backupType != null) {
			redTeam.predator_portal1.getBlock().setType(redTeam.predator_portal1_backupType);
			redTeam.predator_portal1 = null;
		}
		if(blueTeam.predator_portal1 != null && blueTeam.predator_portal1_backupType != null) {
			blueTeam.predator_portal1.getBlock().setType(blueTeam.predator_portal1_backupType);
			redTeam.predator_portal2 = null;
		}
		for (int i = 0; i < ingamePlayer.size(); i++) {
			String pName = ingamePlayer.get(i);
			Player p = Bukkit.getPlayer(pName);
			if(playerMap.containsKey(p.getName())) {
				playerMap.get(pName).ability.respawnSch.cancelTask(true);
			}	
		}
		divideSpawn();
		int schFreeCnt = 0;
		try {
			for(EGScheduler sch : schList) {
				if(sch.cancelTask(false))
					schFreeCnt += 1;
			}
			schList.clear();
			Bukkit.getLogger().info(disPlayGameName+"������ ������ �� : "+schFreeCnt);
		}catch(Exception e) {
			
		}
		
		ingame = false;
		ending = false;
		initGame();
		ingamePlayer.clear();
	}

	//////////////// �̺�Ʈ
	public class EventHandlerHRW extends EGEventHandler {

		private HeroesWar_Ulraf game;

		public EventHandlerHRW(EGServer server, HeroesWar_Ulraf hrw) {
			super(server);
			this.game = hrw;
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
				
				if(playerMap.get(p.getName()) != null)
					playerMap.get(p.getName()).ability.onPlayerCommand(e);
				
				if(cmd[0].equalsIgnoreCase("/tc")) {
					String team = getTeam(p);
					if(team.equalsIgnoreCase("BLUE") || team.equalsIgnoreCase("RED")) {
						if(teamChatList.contains(p.getName())) {
							teamChatList.remove(p.getName());
							p.sendMessage(ms+"��ü ä������ ��ȯ�մϴ�.");
							e.setCancelled(true);
						} else {
							teamChatList.add(p.getName());
							p.sendMessage(ms+"�� ä������ ��ȯ�մϴ�.");
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
				        if (cause.equals(DamageCause.VOID) && !ingame) { //���� ��� ������, ����		           
				            p.teleport(loc_Join, TeleportCause.PLUGIN);
				            Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
				            	public void run() {
				            		p.teleport(loc_Join, TeleportCause.PLUGIN);
				            	}
				            }, 2l);
				        }
				        e.setCancelled(true);
					} else if(gameStep == 1) {
						if (cause.equals(DamageCause.VOID)) { //���� ��� ������, ����		           
							 p.teleport(loc_Join, TeleportCause.PLUGIN);
							 Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
					            	public void run() {
					            		p.teleport(loc_Join, TeleportCause.PLUGIN);
					            	}
					            }, 2l);
				        }
						e.setCancelled(true);
					} else if(gameStep == 2) {
				        if (cause.equals(DamageCause.VOID)) { //���� ��� ������, ����		           
				        	//p.teleport((getTeam(p).equalsIgnoreCase("BLUE") ? blueTeam.loc_jobSelect : redTeam.loc_jobSelect));
				        	 p.teleport(loc_Join, TeleportCause.PLUGIN);
				        	 Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
					            	public void run() {
					            		p.teleport(loc_Join, TeleportCause.PLUGIN);
					            	}
					            }, 2l);
				        }
				        e.setCancelled(true);
					} else if(gameStep == 3) {
						if (cause.equals(DamageCause.VOID)) { //������ ��� ��100		           
				        	 e.setDamage(100);
				        }
						if(cause.equals(DamageCause.FALL)) {
					        	e.setCancelled(true);
					    } else {
					    	if(e.getDamage() != 0.01) {
								if(playerMap.get(p.getName()) != null) {
									Team team = playerMap.get(p.getName()).team;
									Location pl = p.getLocation();
									Location l1 = team.loc_base1;
									Location l2 = team.loc_base2;	
									//p.sendMessage(""+pl);
									//p.sendMessage(""+l1);
									//p.sendMessage(""+l2);
									if(l1.getX() < pl.getX() &&
											l2.getX() > pl.getX() &&
											l1.getY() < pl.getY() &&
											l2.getY() > pl.getY() &&
											l1.getZ() < pl.getZ() &&
											l2.getZ() > pl.getZ()) {
										ActionBarAPI.sendActionBar(p, "��c��l�����ȿ����� �������� ���� �ʽ��ϴ�.", 60);
										e.setCancelled(true);
									} else {
										playerMap.get(p.getName()).ability.onEntityDamaged(e);	
									}
								}															        
							}	
					    }							
					} else if(gameStep == 4) {
				        if (cause.equals(DamageCause.VOID)) { //���� ��� ������, ����		           
				            p.teleport(loc_Join, TeleportCause.PLUGIN);
				            Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
				            	public void run() {
				            		p.teleport(loc_Join, TeleportCause.PLUGIN);
				            	}
				            }, 2l);
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
			
			if (e.getInventory().getTitle().equalsIgnoreCase("��0��l"+inventoryGameName+" �����")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR)
					return;
				p.closeInventory();
				gameHelper(p, e.getSlot());
			}
			
			if (!ingamePlayer.contains(p.getName())) return;
			if(playerMap.get(p.getName()) != null)
				playerMap.get(p.getName()).ability.onClickInventory(e);
			
			if(e.getCurrentItem() != null && !(e.getCurrentItem().getType() == Material.GOLDEN_CARROT 
					|| e.getCurrentItem().getType() == Material.GHAST_TEAR
					|| e.getCurrentItem().getType() == Material.MAGMA_CREAM
					|| e.getCurrentItem().getType() == Material.AIR
					|| e.getCurrentItem().getType() == Material.POTION
					|| e.getCurrentItem().getType() == Material.GLASS_BOTTLE)){
				//p.sendMessage(ms+"�� �������� �ű�� �� �����ϴ�.");
				e.setCancelled(true);
				p.updateInventory();
			}
			
			if (e.getInventory().getTitle().contains("��������")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR)
					return;
				abilitySelect(p, e.getSlot());
			} else if(e.getInventory().getTitle().contains("��������")) {
				e.setCancelled(true);
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR)
					return;
				hrwHelper(p, e.getSlot());
			}
		}

		@EventHandler
		public void onHitPlayer(EntityDamageByEntityEvent e) {
			

			if(e.getEntity() instanceof Player && ingame) { //���ӽ�����������
				Player player = (Player) e.getEntity();
				Player damager = null;

				if (!ingamePlayer.contains(player.getName())) return; //�����ڰ� �׿� ���������������� ��Ģ ����

				boolean isDirectAttach = true;
				Arrow arrow = null;
				
				if (e.getDamager() instanceof Snowball) { //ȭ��� �Ѿ˿����� ������ ����
					Snowball snowball = (Snowball) e.getDamager();
					if (snowball.getShooter() instanceof Player) {
						damager = (Player) snowball.getShooter();
						isDirectAttach = false;
					}
				}
				if (e.getDamager() instanceof Arrow) {
					arrow = (Arrow) e.getDamager();
					if (arrow.getShooter() instanceof Player) {
						damager = (Player) arrow.getShooter();
						isDirectAttach = false;
					}
					e.getDamager().remove();	
				}
				if (e.getDamager() instanceof Player)
					damager = (Player) e.getDamager();
				
				if (damager == null) //������ ������ ����
					return;
				
				if (!ingamePlayer.contains(damager.getName())) { //�����ڰ� �׿� ���������������� ��Ģ ����
					return;
				}
				
				
				if(playerMap.get(damager.getName()) != null) {
					if(playerMap.containsKey(player.getName())) {
						Team team = playerMap.get(player.getName()).team;
						Location pl = player.getLocation();
						Location l1 = team.loc_base1;
						Location l2 = team.loc_base2;
						if(l1.getX() < pl.getX() &&
								l2.getX() > pl.getX() &&
								l1.getY() < pl.getY() &&
								l2.getY() > pl.getY() &&
								l1.getZ() < pl.getZ() &&
								l2.getZ() > pl.getZ()) {
							ActionBarAPI.sendActionBar(player, "��c��l�����ȿ����� �������� ���� �ʽ��ϴ�.", 60);
							ActionBarAPI.sendActionBar(damager, "��c��l��� �����ȿ����� ������ �Ұ����մϴ�.", 60);
							e.setCancelled(true);
						} else {
							if(playerMap.containsKey(player.getName()) && playerMap.containsKey(damager.getName())) {
								if(!playerMap.get(player.getName()).team.equals(playerMap.get(damager.getName()).team)) {									
									if(playerMap.containsKey(player.getName()) && playerMap.containsKey(damager.getName())) {
										if(!playerMap.get(player.getName()).team.equals(playerMap.get(damager.getName()).team)) {
											if(!isDirectAttach || checkVictimDelay(damager.getName(), player)) {
												playerMap.get(damager.getName()).ability.onHitPlayer(e);
											}
										} else {
											e.setCancelled(true);
										}
									}
								} else {
									e.setCancelled(true);
								}
							}			
						}
					}			
				}
					
				
				ItemStack weapon = damager.getInventory().getItemInMainHand();
				
				if(weapon != null && (
						weapon.getType().toString().contains("AXE") || 
						weapon.getType().toString().contains("HOE") ||
						weapon.getType().toString().contains("SWORD") ||
						weapon.getType().toString().contains("SPADE") ||
						weapon.getType().toString().contains("BOW"))){
					short dura = weapon.getType().getMaxDurability();
					weapon.setDurability((short) (dura - dura));
				}

				if(arrow != null) arrow.remove();

			}
		}
		
		@EventHandler
		public void onBlockBreak(BlockBreakEvent e) {
			Player p = e.getPlayer();
			if(!ingamePlayer.contains(p.getName())) return;
			if(!ingame || gameStep != 3) {
				e.setCancelled(true);
				return;
			}
			boolean block_noCatching = false;
			if(playerMap.get(p.getName()) != null) {
				Ability ab = playerMap.get(p.getName()).ability;
				ab.onBlockBreak(e);
				block_noCatching = ab.block_noCatching;
			}

			Location bl = e.getBlock().getLocation();
			e.setCancelled(true);
		}
		
		@EventHandler
		public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent e) {
			Player p = e.getPlayer();
			if(!ingamePlayer.contains(p.getName())) return;
			if(playerMap.get(p.getName()) != null)
				playerMap.get(p.getName()).ability.onPlayerItemConsume(e);
			//e.setCancelled(true);
		}
		
		@EventHandler
		public void onBlockPlaced(BlockPlaceEvent e) {
			Player p = e.getPlayer();
			if(!ingamePlayer.contains(p.getName())) return;

			if(playerMap.get(p.getName()) != null)
				playerMap.get(p.getName()).ability.onBlockPlaced(e);
			
			e.setCancelled(true);
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
		public void onItemDrop(PlayerDropItemEvent e) {
			Player p = e.getPlayer();
			if(!ingamePlayer.contains(p.getName())) return;
			
			if(playerMap.get(p.getName()) != null)
				playerMap.get(p.getName()).ability.onItemDrop(e);
			Material m = e.getItemDrop().getItemStack().getType();
			if(m != Material.GOLDEN_CARROT && m != Material.POTION) {
				if(m == Material.GLASS_BOTTLE) {
					e.getItemDrop().remove();
				}else {
					e.setCancelled(true);	
				}
			}
		}

		@EventHandler
		public void onPlayerDeath(PlayerDeathEvent e) {
			if (((e.getEntity() instanceof Player)) && ingame) {
				Player p = e.getEntity();
				if(!ingamePlayer.contains(p.getName())) return;
								
				HRWPlayer hrwp = playerMap.get(p.getName());
				String k = hrwp.lastDamager;
//				Bukkit.broadcastMessage(k+","+hrwp.ability.abilityName);
				
				if(playerMap.get(p.getName()) != null)
					playerMap.get(p.getName()).ability.onPlayerDeath(e);
				
				if(k != null) {
					if(playerMap.get(k) != null)
						playerMap.get(k).ability.onPlayerKill(p);
				}

				e.getDrops().clear();
				e.setDroppedExp(0);
				
				if(ingame) {				
					if(hrwp.alreadyDead) return;
					hrwp.alreadyDead = true;
					Ability ab = hrwp.ability;
					ab.lastDeathLoc = p.getLocation().add(0,0.5,0);		
					if(hrwp.carrotCnt == 0) {
						hrwp.carrotCnt = countItem(p.getInventory(), Material.GOLDEN_CARROT);
					}
					if (ab.backUpInvenMap == null) {
						if (hasItem(p, Material.BOOK, 1)) {
							ab.backUpInvenMap = p.getInventory().getContents();
							ab.backUpArmorMap = p.getInventory().getArmorContents();
						}
					}
					p.setHealth(0);
					hrwp.death += 1;
					if (getTeam(p).equalsIgnoreCase("BLUE")) {
						if (k == null || k == p.getName()) {
							giveCarrot(redTeam.teamList, "����" + p.getName() + " ���� �ڻ��Ͽ� ���+1", 1);
							redTeam.carrotCnt += 1;
						}	else {
							HRWPlayer killerHrwP = playerMap.get(k);
							if(killerHrwP == null) return;
							killerHrwP.kill += 1;
							if(System.currentTimeMillis() - killerHrwP.lastKillTime <= 5000) {
								killerHrwP.nowStackKill += 1;
								if(killerHrwP.nowStackKill > killerHrwP.maxStackKill) {
									killerHrwP.maxStackKill = killerHrwP.nowStackKill;
								}
								sendStackMessage(k, killerHrwP.nowStackKill);								
							}else {
								killerHrwP.nowStackKill = 1;
							}
							killerHrwP.lastKillTime = System.currentTimeMillis();
							//Bukkit.getLogger().info(System.currentTimeMillis()+" / "+killerHrwP.lastKillTime);
							//Bukkit.getLogger().info(killerHrwP.nowStackKill+"");
							giveCarrot(redTeam.teamList, k + " ���� ���� óġ�Ͽ� ���+1", 1);
							redTeam.carrotCnt += 1;
						}						
					} else if (getTeam(p).equalsIgnoreCase("RED")) {
						if (k == null || k == p.getName()) {
							giveCarrot(blueTeam.teamList, "����" + p.getName() + " ���� �ڻ��Ͽ� ���+1", 1);
							blueTeam.carrotCnt += 1;
						}
						else {
							HRWPlayer killerHrwP = playerMap.get(k);
							if(killerHrwP != null) {
								killerHrwP.kill += 1;
								if(System.currentTimeMillis() - killerHrwP.lastKillTime <= 5000) {
									killerHrwP.nowStackKill += 1;
									if(killerHrwP.nowStackKill > killerHrwP.maxStackKill) {
										killerHrwP.maxStackKill = killerHrwP.nowStackKill;
									}else {
										killerHrwP.nowStackKill = 1;
									}
									sendStackMessage(k, killerHrwP.nowStackKill);
								}else {
									killerHrwP.nowStackKill = 1;
								}
								killerHrwP.lastKillTime = System.currentTimeMillis();
								//Bukkit.getLogger().info(System.currentTimeMillis()+" / "+killerHrwP.lastKillTime);
								//Bukkit.getLogger().info(killerHrwP.nowStackKill+"");
							}							
							giveCarrot(blueTeam.teamList, k + " ���� ���� óġ�Ͽ� ���+1", 1);
							blueTeam.carrotCnt += 1;
						}
					}		
					
					k = null;
				} else {
					gameQuitPlayer(p, true, false);
				}
			}
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
		public void onPlayerRespawn(PlayerRespawnEvent e) {
			Player p = e.getPlayer();
			if(!ingamePlayer.contains(p.getName())) return;
			
			if(playerMap.get(p.getName()) != null) {
				HRWPlayer hrwp = playerMap.get(p.getName());
				e.setRespawnLocation(hrwp.ability.team.loc_spawn);
				hrwp.alreadyDead = false;
				hrwp.ability.onPlayerRespawn(e);
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
			//////////������
			if(pointSetting != 0 && action == Action.LEFT_CLICK_BLOCK) {
				if(pointSetting == 1) {
					point1.savePointLoc(locPath, "location.yml", e.getClickedBlock().getLocation());
					p.sendMessage(ms+"point1�� ���� �� �����Ϸ�, ���� ���� 1�� ������ �������ּ���.");
					pointSetting += 1;
				} else if(pointSetting == 4) {
					point2.savePointLoc(locPath, "location.yml", e.getClickedBlock().getLocation());
					p.sendMessage(ms+"point2�� ���� �� �����Ϸ�, ���� ���� 1�� ������ �������ּ���.");
					pointSetting += 1;
				} else if(pointSetting == 7) {
					point3.savePointLoc(locPath, "location.yml", e.getClickedBlock().getLocation());
					p.sendMessage(ms+"point3�� ���� �� �����Ϸ�, ���� ���� 1�� ������ �������ּ���.");
					pointSetting += 1;
				} else if(pointSetting == 2) {
					tmpWoolPos1 = e.getClickedBlock().getLocation();
					p.sendMessage(ms+"point1�� ���� �� 1���� �����Ϸ�, ���� ���� 2�� ������ �������ּ���.");
					pointSetting += 1;
				} else if(pointSetting == 3) {
					point1.saveWoolLoc(locPath, "location.yml", tmpWoolPos1, e.getClickedBlock().getLocation());
					p.sendMessage(ms+"point1�� ���� �� 2���� �����Ϸ�, ��������.");
					pointSetting = 0;
				} else if(pointSetting == 5) {
					tmpWoolPos1 = e.getClickedBlock().getLocation();
					p.sendMessage(ms+"point2�� ���� �� 1���� �����Ϸ�, ���� ���� 2�� ������ �������ּ���.");
					pointSetting += 1;
				} else if(pointSetting == 6) {
					point2.saveWoolLoc(locPath, "location.yml", tmpWoolPos1, e.getClickedBlock().getLocation());
					p.sendMessage(ms+"point2�� ���� �� 2���� �����Ϸ�, ��������.");
					pointSetting = 0;
				} else if(pointSetting == 8) {
					tmpWoolPos1 = e.getClickedBlock().getLocation();
					p.sendMessage(ms+"point3�� ���� �� 1���� �����Ϸ�, ���� ���� 2�� ������ �������ּ���.");
					pointSetting += 1;
				} else if(pointSetting == 9) {
					point3.saveWoolLoc(locPath, "location.yml", tmpWoolPos1, e.getClickedBlock().getLocation());
					//server.egPM.printLog(""+tmpWoolPos1.getBlockX()+"/"+tmpWoolPos1.getBlockY()+"/"+tmpWoolPos1.getBlockZ());
					//server.egPM.printLog(""+e.getClickedBlock().getLocation().getBlockX()+"/"+e.getClickedBlock().getLocation().getBlockY()+"/"+e.getClickedBlock().getLocation().getBlockZ());
					p.sendMessage(ms+"point3�� ���� �� 2���� �����Ϸ�, ��������.");
					pointSetting = 0;
				} 
				e.setCancelled(true);
			}///////////////
			
			if(baseSetting != 0 && action == Action.LEFT_CLICK_BLOCK) {
				if(baseSetting == 1) {
					saveLocation(gameName, "blueBase1", e.getClickedBlock().getLocation());
					p.sendMessage(ms+"����� ���� 1������ �����Ϸ�, 2�� ������ �������ּ���.");
					baseSetting += 1;
				} else if(baseSetting == 2) {
					saveLocation(gameName, "blueBase2", e.getClickedBlock().getLocation());
					p.sendMessage(ms+"����� ���� ���� �����Ϸ�");
					baseSetting = 0;
				} else if(baseSetting == 3) {
					saveLocation(gameName, "redBase1", e.getClickedBlock().getLocation());
					p.sendMessage(ms+"������ ���� 1������ �����Ϸ�, 2�� ������ �������ּ���.");
					baseSetting += 1;
				} else if(baseSetting == 4) {
					saveLocation(gameName, "redBase2", e.getClickedBlock().getLocation());
					p.sendMessage(ms+"������ ���� ���� �����Ϸ�");
					baseSetting = 0;
				} 
				e.setCancelled(true);
			}///////////////
			if (!ingamePlayer.contains(p.getName())) return; //��Ŭ���� ���
			
			if(playerMap.get(p.getName()) != null) {
				Ability ab = playerMap.get(p.getName()).ability;
				ab.onMouseClick(e);
				/*ItemStack item = p.getInventory().getItemInMainHand();
				if(item.getType() == Material.GHAST_TEAR) {
					removeItem(p, Material.GHAST_TEAR, 1);
					ab.healPotion1();
				} else if(item.getType() == Material.MAGMA_CREAM) {
					removeItem(p, Material.MAGMA_CREAM, 1);
					ab.healPotion2();
				}*/
			}
				
			
			if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;
					
				if(getHeldMainItemName(p).equalsIgnoreCase("��f[ ��c���� ���� ��f]")) {
					if(getTeam(p).equalsIgnoreCase("BLUE")) {
						p.openInventory(blueTeam.jobList);
					} else {
						p.openInventory(redTeam.jobList);	
					}
				}
			
			if (getHeldMainItemName(p).equalsIgnoreCase("��f[ ��6���� ����� ��f]")) {
				p.openInventory(inven_gameHelper);
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 2.0f);
			} else if (getHeldMainItemName(p).equalsIgnoreCase("��f[ ��b���� �޴� ��f]")) {
				p.openInventory(inven_hrwHelper);
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 2.0f);
			}else if (getHeldMainItemName(p).equalsIgnoreCase("��f[ ��6��ȯ ��f]")) {
				if(playerMap.containsKey(p.getName()))
					playerMap.get(p.getName()).ability.returning();
			}

			Block b = e.getClickedBlock();
			if (e.getClickedBlock() != null) {
				if(b.getType() == Material.CHEST) {
					e.setCancelled(true);
					String team = getTeam(p);
					if((team.equalsIgnoreCase("BLUE") && blueTeam.leftCarrot == 0) ||
							(team.equalsIgnoreCase("RED") && redTeam.leftCarrot == 0)){
						p.playSound(p.getLocation(), Sound.BLOCK_WOOD_BREAK, 1.0f, 1.5f);
						p.sendMessage(ms+"�̹� ������ Ȱ��ȭ �Ǿ��ֽ��ϴ�.");
					} else {
						p.playSound(p.getLocation(), Sound.BLOCK_ENDERCHEST_OPEN, 1.0f, 1.0f);
						Inventory altarInven = Bukkit.createInventory(null, 9, "��c��l����");
						p.sendMessage(ms+"���ܿ� ��ĥ ����� �־��ּ���.");
						p.openInventory(altarInven);
					}
				}
			}
		}
		
		@EventHandler
		public void onInteractEntity(PlayerInteractEntityEvent e) {
			Player p = e.getPlayer();
			if (!ingamePlayer.contains(p.getName())) return; //��Ŭ���� ���
			if(e.getRightClicked() instanceof Player){
				Player t = (Player) e.getRightClicked();
				if(t.getName().contains("��f[ ��c�Ű� ��f]") && getTeam(p).equalsIgnoreCase("RED")){
					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
					MyUtility.healUp(p);
					TitleAPI.sendTitle(p, 10, 70, 10, "��e��lġ����");
				}  else if(t.getName().contains("��f[ ��b�Ű� ��f]") && getTeam(p).equalsIgnoreCase("BLUE")){
					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
					MyUtility.healUp(p);
					TitleAPI.sendTitle(p, 10, 70, 10, "��e��lġ����");
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
		public void onPlayerMove(PlayerMoveEvent e){
			Player p = (Player) e.getPlayer();
			if(ingamePlayer.contains(p.getName()) && ingame) {
				///��ġ�̵� �ƴϸ� ĵ��
				if(e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockY() == e.getTo().getBlockY()
						&& e.getFrom().getBlockZ() == e.getTo().getBlockZ()) return;
				
				boolean block_noCatching = false;
				
				if(playerMap.get(p.getName()) != null) {
					Ability ab = playerMap.get(p.getName()).ability;
					ab.onPlayerMove(e);
					block_noCatching = ab.block_noCatching;
				}
				
				Location gotoLoc = new Location(e.getTo().getWorld(), e.getTo().getBlockX(), e.getTo().getBlockY(), e.getTo().getBlockZ());
				gotoLoc.setY(gotoLoc.getBlockY()-1);
				if(gotoLoc.getBlock().getType() == pointBlock && gameStep == 3) {
					if(block_noCatching) p.sendMessage(ms+"���� ���¿����� ������ �Ұ����մϴ�.");
					else occupyPoint(p, gotoLoc);
				}
				if(getTeam(p).equalsIgnoreCase("BLUE")) {
					if(gotoLoc.distance(redTeam.loc_spawn) <= 32) {
						p.damage(10);
						p.sendMessage(ms+"���� �����ȿ����� �̵��� ���ظ� �޽��ϴ�.");
					}else if(gotoLoc.distance(blueTeam.loc_spawn) <= 32) {
						p.addPotionEffect(game.baseBoost);
					}
				} else if(getTeam(p).equalsIgnoreCase("RED")) {
					if(gotoLoc.distance(blueTeam.loc_spawn) <= 32) {
						p.damage(10);
						p.sendMessage(ms+"���� �����ȿ����� �̵��� ���ظ� �޽��ϴ�.");
					}else if(gotoLoc.distance(redTeam.loc_spawn) <= 32) {
						p.addPotionEffect(game.baseBoost);
					}
				}
					
				if(gotoLoc.getBlock().getRelative(0, -1, 0).getType() == Material.GOLD_BLOCK){
					if(playerMap.containsKey(p.getName())) {
						playerMap.get(p.getName()).ability.healSpring();
					}
				} 
			}
		}
			
		@EventHandler
		public void onPlayerChat(PlayerChatEvent e){
			Player p = (Player) e.getPlayer();
			if(ingamePlayer.contains(p.getName())) {
				if(playerMap.get(p.getName()) != null)
					playerMap.get(p.getName()).ability.onChat(e);
				String msg = e.getMessage();
				if(teamChatList.contains(p.getName())) {
					String team = getTeam(p);
					String str = (team.equalsIgnoreCase("BLUE") ? blueTeam.ms : redTeam.ms)+p.getName()+" >> ��b"+msg;
					HRWPlayer hrwP = playerMap.get(p.getName());
					if(hrwP != null) {
						str += " ��f[ ��a"+hrwP.ability.abilityName+" ��f]";
					}
					server.egCM.sendMessagesToStringList((team.equalsIgnoreCase("BLUE") ? blueTeam.teamList : redTeam.teamList), p, str, false);
				} else {
					String str = ms+p.getName()+" >> ��6"+msg;
					server.egCM.sendMessagesToStringList(ingamePlayer, p, str, false);
					sendSound(Sound.BLOCK_STONE_BREAK, 1.0f, 1.5f);
				}
				e.setCancelled(true);
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
		
		@EventHandler
		public void onPlayerToggleFlight(PlayerToggleFlightEvent e){
			Player p = (Player) e.getPlayer();
			if(ingamePlayer.contains(p.getName())) {
				if(playerMap.get(p.getName()) != null)
					playerMap.get(p.getName()).ability.onPlayerToggleFlight(e);
			}
		}
		
		@EventHandler
		public void onPlayerCraft(CraftItemEvent e) {
			if(e.getWhoClicked() instanceof Player) {
				Player p = (Player) e.getWhoClicked();	
				if (!ingamePlayer.contains(p.getName())) { // �׿� ���������������� ��Ģ ����
					return;
				}
				e.setCancelled(true);
			}
		}
		
		@EventHandler
		public void onChangeHand(PlayerSwapHandItemsEvent e) {
			Player p = (Player) e.getPlayer();
			if(ingamePlayer.contains(p.getName())) {
				if(e.getOffHandItem() == null) {
					
				}else if(e.getOffHandItem().getType() == Material.AIR){
					
				}else {
					e.setCancelled(true);
					if (playerMap.get(p.getName()) != null)
						playerMap.get(p.getName()).ability.onChangeHand(e);
				}
			}
		}
		
		@EventHandler
		public void onArrowHit(PlayerToggleSneakEvent e){
			Player p = (Player) e.getPlayer();
			if(ingamePlayer.contains(p.getName())) {
				if(playerMap.get(p.getName()) != null)
					playerMap.get(p.getName()).ability.onSneak(e);
			}
		}
	}
}
