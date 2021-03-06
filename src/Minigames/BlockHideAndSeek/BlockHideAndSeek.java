package Minigames.BlockHideAndSeek;

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

public class BlockHideAndSeek extends Minigame{
	// 이벤트용
	public EventHandlerBHK event;
	
	public String ms = "§7[§6전체§7] ";
	///////////// private
	// 게임 플레이어 리스트
	private HashMap<String, bhkPlayer> playerMap = new HashMap<String, bhkPlayer>();

	//////// 게임 관련	
	public ItemStack item_tnt;
	public ItemStack item_sword;
	
	public int gameStep = 0;
	public int roundTime = 360;
	public EGScheduler mainSch;
	public List<String> team_seeker = new ArrayList<String>();
	public List<String> team_hider = new ArrayList<String>();
	
	public boolean catcherNoAttack = false;
	
	public float normalWalkSpeed = 0.2f;
	public float catcherWalkSpeed = 0.3f;
	
	public Location loc_mapStart;
	
	////// 각종 인벤토리
	public Inventory inven_gameHelper;
	
	//////// 사이드바
	private Sidebar bhkSidebar;
	private List<SidebarString> textList = new ArrayList<SidebarString>(5);
	
	public BlockHideAndSeek(EGServer server) {

		//////////////////// 필수 설정값
		super(server);
		
		ms = "§7[ §e! §7] §f: §c블럭 숨바꼭질 §f>> "; // 기본 메세지
		gameName = "BlockHideAndSeek";
		disPlayGameName = ChatColor.RED+"블럭 숨바꼭질";
		minPlayer = 4;
		maxPlayer = 10;
		startCountTime = 10;
		doneSetting = loadGameData(); // 게임 시작 위치 등등 로드
		/////////////////////// 자동 설정(아이템등등)
		dirSetting("BlockHideAndSeek");
		////////////////
		
		mainSch = new EGScheduler(this);
		
		SidebarString tmpLine = new SidebarString("");
		bhkSidebar = new Sidebar("§f[ §6게임 현황 §f]", server, 600, tmpLine);
		
		/////////////////// 게임 도우미
		inven_gameHelper = Bukkit.createInventory(null, 27, ""+ChatColor.BLACK+ChatColor.BOLD+"도우미");

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
		loreList.add("§7게임은 술래팀과 숨는팀으로 나뉘어 진행되며");
		loreList.add("§7숨는팀은 정해진 시간동안 술래에게서 들키지 않고");
		loreList.add("§71명이라도 살아남을시 승리하며");
		loreList.add("§7술래팀은 모든 숨는 팀을 잡아낼 시 승리합니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_gameHelper.setItem(10, item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c진행방식 §7-");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7게임 시작 후 숨는 팀은 30초간의 숨는 시간을 갖습니다.");
		loreList.add("§7숨는 팀은 블럭을 우클릭 할 시 해당 블럭으로");
		loreList.add("§7변신할 수 있습니다.(스스로 변신한 모습을 볼 수는 없습니다.)");
		loreList.add("§7술래팀은 게임 시작 후 30초가 지나면 활동을 개시하며");
		loreList.add("§7힌트를 소지합니다. 힌트를 사용할 시 숨는 팀의 위치에");
		loreList.add("§7폭죽을 터뜨립니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_gameHelper.setItem(11, item);
		
		item_sword = new ItemStack(Material.DIAMOND_SWORD, 1);
		meta = item_sword.getItemMeta();
		meta.setDisplayName("§f[ §c술래 §f]");
		item_sword.setItemMeta(meta);
		
		event = new EventHandlerBHK(server, this);
		// 이 플러그인에 이벤트 적용
		server.getServer().getPluginManager().registerEvents(event, server);
	}

	/////////////// Minigame에서 override 해야하는 부분
	@Override
	public void gameHelpMsg(Player p) {

	}

	@Override
	public void startGame() {
		server.egCM.broadCast(server.ms_alert + ChatColor.RED+ChatColor
				.BOLD+ "블럭 숨바꼭질 " + ChatColor.GRAY + "가 시작 되었습니다!");
		initGame();
		ingame = true;
		/////////////// 오프닝
		if(ingamePlayer.size() <= 3) {
			for(String pName : ingamePlayer) {
				Player t = Bukkit.getPlayer(pName);
				server.spawn(t);
				t.sendMessage(ms+"최소 시작인원이 부족하여 시작이 취소됐습니다.");
			}
			endGame(false);
			return;
		}
		sendSound(Sound.BLOCK_NOTE_BELL, 1.0f, 1.0f);
		//updateSidebar();
		gameStep = 1;
		for (String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p)){
				p.setGameMode(GameMode.ADVENTURE);
				// 풀피로 만듬
				MyUtility.healUp(p);         
				p.teleport(loc_Join);
				TitleAPI.sendFullTitle(p, 10, 80, 30, ChatColor.RED+"게임 시작", ChatColor.RED+""+disPlayGameName);
				bhkSidebar.showTo(p);
				p.setWalkSpeed(0.4f);
			}
		}
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(server,new Runnable(){
			public void run() {
				sendTitle("§c§l숨는 팀 활동 개시", "§6§l숨는 팀은 어서 숨으세요!", 40);
				
			}
		},100l);
	}
	
	//////////////////
	
	public void gameTimer() {
		mainSch.schTime2 = 0;
		mainSch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				if(mainSch.schTime2 == 0) {
					if(ingamePlayer.size() < 1) {
						setWinner();
					}
					setCatcher();
					mainSch.schTime2 = 1;
					mainSch.schTime = 0;
					roundTime = 90 - ((maxPlayer - ingamePlayer.size()) * 10)+15;
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

	}
	
	public void setCatcher() {
		catcherNoAttack = false;
		//catcherName = "";
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
						sendTitle("§c§l"+(sch.schTime+1), "", 25);
						sendSound(Sound.BLOCK_NOTE_PLING);
					}
				}else {
					sch.cancelTask(true);
					String tName = ingamePlayer.get(MyUtility.getRandom(0, ingamePlayer.size()-1));
					Player catcher = Bukkit.getPlayer(tName);
					applyCatcher(catcher);
					for(String pName : ingamePlayer) {
						Player p = Bukkit.getPlayer(pName);
						timerLevelBar(p, roundTime-15, false, true);
					}
					updateSidebar();
				}
			}
		}, 0l, 20l);
	}
	
	public void applyCatcher(Player p) {
		p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 254));
		p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 60, 248));
		p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 60, 255));
		p.getInventory().setHelmet(item_tnt);
		for(int i = 0; i < 9; i++) {
			p.getInventory().setItem(i, item_sword);
		}
		//catcherName = p.getName();
		catcherNoAttack = true;
		p.sendMessage(ms+"당신은 폭탄을 가지고 있습니다. 어서 타인에게 넘기세요!");
		sendSound(Sound.BLOCK_CHEST_CLOSE, 1.0f, 0.5f);
		p.setWalkSpeed(catcherWalkSpeed);
		TitleAPI.sendFullTitle(p, 0, 60, 0, "§4§l주의", "§e§l폭탄을 가지고 있습니다.");
		for(String tName : ingamePlayer) {
			if(tName.equalsIgnoreCase(p.getName())) continue;
			Player t = Bukkit.getPlayer(tName);
			if(existPlayer(t)) {
				TitleAPI.sendFullTitle(t, 0, 60, 0, "§c§l"+p.getName(), "§a§l님이 폭탄을 가지고 있습니다.");
				t.sendMessage(ms+"§6"+p.getName()+" 님이 폭탄을 받았습니다.");
			}
		}	
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				catcherNoAttack = false;
				p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP,72000, 1));
			}
		},50l);
	}
	
	public void relieveCatcher(Player p) {
		p.getInventory().setHelmet(null);
		p.getInventory().clear();
		sendSound(Sound.ENTITY_TNT_PRIMED, 1.0f, 0.1f);
		p.setWalkSpeed(normalWalkSpeed);
	}
	
	public void updateSidebar() {
		textList.clear();
		
		SidebarString line = new SidebarString("");
		textList.add(line);
		line = new SidebarString("§e폭탄 소지자");
		textList.add(line);
		//String name = catcherName;
		//if(name.length() > 10) name = name.substring(0, 10);
		//line = new SidebarString("§a"+name);
		textList.add(line);
		line = new SidebarString("");
		textList.add(line);
		line = new SidebarString("§e남은인원 §f: §a"+ingamePlayer.size()+"명");
		textList.add(line);
		line = new SidebarString("");
		textList.add(line);
		
		bhkSidebar.setEntries(textList);
		bhkSidebar.update();
		
		for(String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p)) 
				bhkSidebar.showTo(p);
		}
	}


	public void initGame() {
		lobbyStart = false;
		ending = false;
		//catcherName = "";
		//스케쥴
		schList.clear();
	}

	public boolean loadGameData() {
		boolean ret = true;
		loc_Join = loadLocation(gameName, "JoinPos");
		loc_spectate = null;
		loc_mapStart = loadLocation(gameName, "mapStart1");
		
		if (loc_Join == null) {
			server.egPM.printLog(ms + "[" + disPlayGameName + "] 게임 시작 대기 지점이 설정되지 않았습니다.");
			ret = false;
		}
		if (loc_mapStart == null) {
			server.egPM.printLog(ms + "[" + disPlayGameName + "] 맵 시작 지점이 설정되지 않았습니다.");
			ret = false;
		}
		
		if(ret == true) {
			server.egPM.printLog("[" + disPlayGameName + "] 설정 이상 없음");
			doneSetting = ret;
		}
		
		return ret;
	}
	
	public void onCommand(Player p, String[] cmd) {
		if (cmd.length <= 1) {
			p.sendMessage(ms + "[" + disPlayGameName + "] " + "/clm join - 게임 참가");
			p.sendMessage(ms + "[" + disPlayGameName + "] " + "/clm quit - 게임 퇴장");
			p.sendMessage(ms + "[" + disPlayGameName + "] " + "/clm set - 게임 설정");
		} else if (cmd[1].equalsIgnoreCase("set")) {
			if (cmd.length <= 2) {
				p.sendMessage(ms + "[" + disPlayGameName + "] " + "/clm set loc- 게임 지점 설정");
			} else if (cmd[2].equalsIgnoreCase("loc")) {
				if (cmd.length <= 3) {
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "/clm set loc join - 게임 시작 대기 지점 설정");
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "/clm set loc block1 - 8x8칸의 꼭지점1");
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "/clm set loc block2 - 8x8칸의 꼭지점2");
				} else if (cmd[3].equalsIgnoreCase("join")) {
					saveLocation(gameName, "JoinPos", p.getLocation());
					loadGameData();
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "게임 시작 대기 지점이 설정되었습니다.");
				}else if (cmd[3].equalsIgnoreCase("block1")) {
					saveLocation(gameName, "block1", p.getLocation());
					loadGameData();
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "꼭지점1이 설정되었습니다.");
				}else if (cmd[3].equalsIgnoreCase("block2")) {
					saveLocation(gameName, "block2", p.getLocation());
					loadGameData();
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "꼭지점2이 설정되었습니다.");
				}else {
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "/clm set loc join - 게임 시작 대기 지점 설정");
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "/clm set loc block1 - 8x8칸의 꼭지점1");
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "/clm set loc block2 - 8x8칸의 꼭지점2");
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
			server.playerList.put(p.getName(), "로비");
			p.getInventory().clear();
			updateSidebar();
			if (ingame) {
				if(true) {
					for(String name : ingamePlayer) {
						Player t = Bukkit.getPlayer(name);
						t.sendMessage(ms+"폭탄이 탈주하여 재설정합니다.");
						mainSch.schTime = 0;
					}
				}
				if(giveGold) {
					if(announce) {
						sendMessage(ms + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " 님이 탈락하셨습니다.");
						sendTitle("", ChatColor.YELLOW+p.getName()+"님 탈락", 60);
						sendSound(Sound.BLOCK_NOTE_CHIME,1.5f, 2.0f);
					}
					p.sendMessage(ms+"게임 플레이 보상으로 5골드를 받으셨습니다.");
				} else {
					if(announce) {
						sendMessage(ms + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " 님이 퇴장하셨습니다.");
						sendSound(Sound.BLOCK_NOTE_GUITAR,1.5f, 2.0f);
					}
				}
				if(bhkSidebar != null) bhkSidebar.hideFrom(p);
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
							for(int i = 0; i < 10; i++) {
								Location loc = t.getLocation().add(0,2,0);
								 Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
								FireworkMeta fwm = fw.getFireworkMeta();

								fwm.setPower(3);
								fwm.addEffect(FireworkEffect.builder().withColor(MyUtility.getRandomColor()).flicker(true).build());

								fw.setFireworkMeta(fwm);
								fw.detonate();
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
			sendTitle("승리", ChatColor.GRAY + "당신이 최후의 1인입니다.", 70);
			sendSound(Sound.BLOCK_NOTE_BELL, 1.0f, 0.5f);
			
			performence();			
			
			try {
				Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
					public void run() {
						for (String pName : ingamePlayer) {
							Player p = Bukkit.getPlayer(pName);
							if(existPlayer(p)) {
								p.sendMessage(ms + "승리 보상으로 10골드를 받으셨습니다.");
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
						for (String pName : ingamePlayer) {
							Player p = Bukkit.getPlayer(pName);
							if(existPlayer(p)) {
								try {
									server.spawn(p);
								} catch (Exception e) {
									// player.teleport(Main.Lobby);
								}
							}
						}
						endGame(false);
						server.egCM.broadCast(
							server.ms_alert + ChatColor.GRAY  + "§c"+winner+"§7님의 §a승리§7로 §c폭탄 돌리기§7가 종료 되었습니다.");
					}
				}, 200L);
			} catch(Exception e) {
					endGame(true);
			}
		} else {
			try {
				Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
					public void run() {
						for (String pName : ingamePlayer) {
							Player p = Bukkit.getPlayer(pName);
							if(existPlayer(p)) {
								try {
									server.spawn(p);
								} catch (Exception e) {
									// player.teleport(Main.Lobby);
								}
							}
						}
						endGame(false);
						server.egCM.broadCast(
							server.ms_alert + ChatColor.GRAY  + "무승부로 §c폭탄 돌리기§7가 종료 되었습니다.");
					}
				}, 200L);
			} catch(Exception e) {
					endGame(true);
			}
		}
	
	}

	public void endGame(boolean force) {
		if(force) {
			server.egCM.broadCast(server.ms_alert + "폭탄 돌리기 게임이 강제 종료 되었습니다.");
		}
		for(String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p)) {
				bhkSidebar.hideFrom(p);
				server.spawn(p);
			}
		}
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

	//////////////// 이벤트
	public class EventHandlerBHK extends EGEventHandler {

		private BlockHideAndSeek game;

		public EventHandlerBHK(EGServer server, BlockHideAndSeek clm) {
			super(server);
			this.game = clm;
		}

		@EventHandler
		public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
			Player p = e.getPlayer();
			String[] cmd = e.getMessage().split(" ");
			if (cmd[0].equalsIgnoreCase("/bhk")) {
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
		
		@EventHandler
		public void onPlayerHit(EntityDamageByEntityEvent e) {
			if(e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
				Player p = (Player)e.getEntity();
				Player d = (Player)e.getDamager();
				if(!(ingamePlayer.contains(p.getName()) && ingamePlayer.contains(d.getName()))) return;
				if(ingame) {
					if(true) {
						if(catcherNoAttack) {
							ActionBarAPI.sendActionBar(d, "§c§l아직 폭탄을 넘길 수 없습니다.", 70);
						}else {
							ActionBarAPI.sendActionBar(d, "§c§l폭탄 배달 성공!", 70);
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
				        if (cause.equals(DamageCause.VOID) && !ingame) { //대기실 허공 뎀없음, 텔포		           
				            p.teleport(loc_Join);
				        }
					} else {
						if (cause.equals(DamageCause.VOID)) { //대기실 허공 뎀없음, 텔포	
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
			if (!ingamePlayer.contains(p.getName()))
				return;
			if (e.getInventory().getTitle().equalsIgnoreCase("§0§l도우미")) {				
				e.setCancelled(true);
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
			Action action = e.getAction();
			if (!ingamePlayer.contains(p.getName()) //우클릭만 허용
					|| (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK))
				return;
			if (e.getItem() == null || e.getItem().getType() == Material.AIR) { //아이템을 안들고 우클릭했을때 리턴
				return;
			} else if (getHeldMainItemName(p).equalsIgnoreCase("§f[ §6게임 도우미 §f]")) {
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
			if(ingamePlayer.contains(p.getName()) &&  ingame) {
				e.setCancelled(true);
				String str = game.ms+p.getName()+" >> §6"+e.getMessage();
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
		public void onBlockPlaced(BlockPlaceEvent e) {
			Player p = e.getPlayer();
			if(ingamePlayer.contains(p.getName())) {
				e.setCancelled(true);
			}
		}
	}
	
	///////////////////// 폭탄 돌리기에 참가한 플레이어들 클래스
	private class bhkPlayer {
		
		public bhkPlayer(Player p, String job) {

		}

	}
}
