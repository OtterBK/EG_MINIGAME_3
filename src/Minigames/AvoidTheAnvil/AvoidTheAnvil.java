package Minigames.AvoidTheAnvil;

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
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;

import com.coloredcarrot.api.sidebar.Sidebar;
import com.coloredcarrot.api.sidebar.SidebarString;
import com.connorlinfoot.titleapi.TitleAPI;

import EGServer.EGServer;
import EGServer.ServerManager.EGEventHandler;
import EGServer.ServerManager.EGScheduler;
import Minigames.Minigame;
import Utility.MyUtility;

public class AvoidTheAnvil extends Minigame{
	// 이벤트용
	public EventHandlerATA event;
	
	public String ms = "§7[§6전체§7] ";

	// 게임 맵

	///////////// private
	// 게임 플레이어 리스트
	private HashMap<String, AtaPlayer> playerMap = new HashMap<String, AtaPlayer>();

	//////// 게임 관련

	public EGScheduler mainSch;
	public int gameDif = 1;
	public int spawnPerTick = 0;
	public List<Player> tmpPlayerList = new ArrayList<Player>();
	public List<Location> locationList = new ArrayList<Location>();
	
	public PotionEffect noJump = new PotionEffect(PotionEffectType.JUMP, 72000, 250);
	public PotionEffect invisible = new PotionEffect(PotionEffectType.INVISIBILITY, 300, 0);
	
	////// 각종 인벤토리
	public Inventory inven_gameHelper;
	
	//////// 사이드바
	private Sidebar ataSidebar;
	private List<SidebarString> textList = new ArrayList<SidebarString>(5);
	private Team scoreBoardTeam;
	
	public AvoidTheAnvil(EGServer server) {

		//////////////////// 필수 설정값
		super(server);
		
		ms = "§7[ §e! §7] §f: §c모루피하기 §f>> "; // 기본 메세지
		gameName = "AvoidTheAnvil";
		disPlayGameName = ChatColor.RED+"모루피하기";
		inventoryGameName = ChatColor.stripColor(disPlayGameName);
		minPlayer = 2;
		maxPlayer = 6;
		startCountTime = 30;
		doneSetting = loadGameData(); // 게임 시작 위치 등등 로드
		/////////////////////// 자동 설정(아이템등등)
		dirSetting("AvoidTheAnvil");
		////////////////
		
		mainSch = new EGScheduler(this);
		
		SidebarString tmpLine = new SidebarString("");
		ataSidebar = new Sidebar("§f[ §6게임 현황 §f]", server, 600, tmpLine);
		
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
		loreList.add("§7모든 플레이어가 탈락할 때까지");
		loreList.add("§7남아있으면 승리합니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_gameHelper.setItem(10, item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c진행방식 §7-");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7게임이 시작되면 하늘에서 모루가 떨어집니다.");
		loreList.add("§7이 모루에 맞게되면 체력의 15의 데미지를 받습니다.");
		loreList.add("§7모루에 2번 맞아 죽게되면 탈락합니다.");
		loreList.add("§7하늘에서 떨어지는 모루를 끝까지 피하세요!");
		loreList.add("§7시간이 지날수록 떨어지는 모루의 양이 많아집니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_gameHelper.setItem(11, item);
		
		event = new EventHandlerATA(server, this);
		
		scoreBoardTeam = ataSidebar.getTheScoreboard().registerNewTeam("customTeam");
		//team.setOption(Option.NAME_TAG_VISIBILITY, OptionStatus.NEVER);
		scoreBoardTeam.setOption(Option.COLLISION_RULE, OptionStatus.NEVER);
		
		// 이 플러그인에 이벤트 적용
		server.getServer().getPluginManager().registerEvents(event, server);
	}

	/////////////// Minigame에서 override 해야하는 부분
	@Override
	public void gameHelpMsg(Player p) {

	}

	@Override
	public void startGame() {
		if(ingamePlayer.size() <= 0) {
			for(String pName : ingamePlayer) {
				Player t = Bukkit.getPlayer(pName);
				server.spawn(t);
				t.sendMessage(ms+"최소 시작인원이 부족하여 시작이 취소됐습니다.");
			}
			endGame(false);
			return;
		}
		server.egCM.broadCast(server.ms_alert + ChatColor.RED+ChatColor
				.BOLD+ "모루피하기 " + ChatColor.GRAY + "가 시작 되었습니다!");
		initGame();
		ingame = true;
		/////////////// 오프닝	
		sendSound(Sound.BLOCK_NOTE_BELL, 1.0f, 1.0f);
		
		for(String tName : scoreBoardTeam.getEntries()) {
			scoreBoardTeam.removeEntry(tName);	
		}
		
		for (String pName : ingamePlayer) {
			scoreBoardTeam.addEntry(pName);
		}
		
		updateSidebar();
		for (String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p)){
				p.setGameMode(GameMode.ADVENTURE);
				// 풀피로 만듬
				MyUtility.healUp(p);
				MyUtility.allClear(p);         
				//p.teleport(loc_Join);
				TitleAPI.sendFullTitle(p, 10, 80, 30, ChatColor.RED+"게임 시작", ChatColor.RED+""+disPlayGameName);
				timerLevelBar(p, 5, false, true);
				ataSidebar.showTo(p);
				p.teleport(loc_Join, TeleportCause.PLUGIN);
				p.addPotionEffect(noJump);
			}
		}
		
		sendSound(Sound.BLOCK_SLIME_BREAK, 1.0f, 1.3f);
		gameTimer();
	}
	
	//////////////////
	
	public void updateSidebar() {
		if(!ingame) return;
		textList.clear();
		
		SidebarString line = new SidebarString("");
		textList.add(line);
		line = new SidebarString("§e난이도 §f: §a"+(gameDif == 10 ? "Final" : gameDif)+"단계");
		textList.add(line);
		line = new SidebarString("");
		textList.add(line);
		line = new SidebarString("§e남은인원 §f: §a"+ingamePlayer.size()+"명");
		textList.add(line);
		line = new SidebarString("");
		textList.add(line);
		
		ataSidebar.setEntries(textList);
		ataSidebar.update();
		
		/*for(String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p)) 
				ataSidebar.showTo(p);
		}*/
	}
	
	public void spawnAnvil() {
		if(locationList.size() <= 0) initLocationList();
		Location l = locationList.get(MyUtility.getRandom(0, locationList.size()-1));
		FallingBlock fb = l.getWorld().spawnFallingBlock(l, Material.ANVIL, (byte)0);
		fb.setCustomName("모루피하기");
		locationList.remove(l);
	}
	
	public void gameTimer() {
		mainSch.cancelTask(true);
		mainSch.schTime = 0; //시간초
		mainSch.schTime2 = 0; //단계
		gameDif = 0;
		schList.add(mainSch);
		mainSch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				if(!ingame) mainSch.cancelTask(true);
				mainSch.schTime++;
				//Bukkit.broadcastMessage("time1 : "+mainSch.schTime+" , time2 : "+mainSch.schTime2);
				if(mainSch.schTime2 == 0) { //시작단계
					sendSound(Sound.BLOCK_CHEST_LOCKED, 1.0f ,2.0f);
					mainSch.schTime2 = 1;
					mainSch.schTime = 0;
					gameDif += 1; //게임 난이도 1렙 상승
					spawnPerTick = 6 - gameDif; //틱당 스폰 모루 설정
					updateSidebar();
				} else if(mainSch.schTime2 == 1) {
					if(mainSch.schTime == 1) {
						sendTitle("§e§l"+gameDif+"단계", "", 80);
					}
					if(mainSch.schTime == 90) {
						tmpPlayerList.clear();		
						for(String pName : ingamePlayer) {
							Player p = Bukkit.getPlayer(pName);
							if(existPlayer(p)) tmpPlayerList.add(p);
						}
					}
					if(mainSch.schTime > 100) {//다음 스탭으루
						mainSch.schTime2 = 2;
						mainSch.schTime = 0;
						for(String tName : ingamePlayer) {
							Player p = Bukkit.getPlayer(tName);
							p.addPotionEffect(invisible);
						}
					}	
					if(gameDif == 5) {
						for(String pName : ingamePlayer) {
							Player p = Bukkit.getPlayer(pName);
							if(existPlayer(p))
								p.setPlayerTime(15000, false);
						}
						
					}
				}else if(mainSch.schTime2 == 2) {
					initLocationList();
					if(mainSch.schTime % 4 == 0) {
						switch (MyUtility.getRandom(0, 6)) { //노래
						case 0:
							sendSound(Sound.BLOCK_NOTE_PLING, 1.5f, 1.0f);
							break;
						case 1:
							sendSound(Sound.BLOCK_NOTE_PLING, 1.5f, 1.0f);
							break;
						case 2:
							sendSound(Sound.BLOCK_NOTE_SNARE, 1.5f, 1.0f);
							break;
						case 3:
							sendSound(Sound.BLOCK_NOTE_BASEDRUM, 1.5f, 1.0f);
							break;
						case 4:
							sendSound(Sound.BLOCK_NOTE_CHIME, 1.5f, 1.0f);
							break;
						case 5:
							sendSound(Sound.BLOCK_NOTE_XYLOPHONE, 1.5f, 1.0f);
							break;
						case 6:
							sendSound(Sound.BLOCK_NOTE_BELL, 1.5f, 1.0f);
							break;
						}	
					}				
					
					if(spawnPerTick == -4) { //10단계라면
						for(int i = 0; i < 3; i++) {
							spawnAnvil();
						}			
					}  else if(spawnPerTick == -3) { //9단계라면
						for(int i = 0; i < 2; i++) {
							spawnAnvil();
						}		
					}else if(spawnPerTick == -2) { //8단계라면
						spawnAnvil();
						 if(mainSch.schTime % 2 == 0) {
							 spawnAnvil();
						 }		
					}else if(spawnPerTick == -1) { //7단계라면
						spawnAnvil();
						 if(mainSch.schTime % 4 == 0) {
							 spawnAnvil();
						 }		
					}else if(spawnPerTick == 0) { //6단계라면
						spawnAnvil();
						 if(mainSch.schTime % 7 == 0) {
							 spawnAnvil();
						 }
					} else if(mainSch.schTime % spawnPerTick == 0) {
						spawnAnvil();
						//Bukkit.broadcastMessage(""+fb.getLocation());
					}
			
					if(gameDif >= 10) { //10단계이상이면 계속 진행함
						
					}else {
						if(mainSch.schTime > 300) {//15초후에는 다음 단계로
							mainSch.schTime = 0;
							mainSch.schTime2 = 3;
						}
					}
				}else if(mainSch.schTime2 == 3) {
					if(mainSch.schTime > 60) {//3초의 쉬는 시간
						mainSch.schTime2 = 0;
						mainSch.schTime = 0;
					}
				}else {
					mainSch.cancelTask(true);
				}
			}
		}, 0l, 1l);
	}
	
	public void initGame() {
		lobbyStart = false;
		ending = false;

		//스케쥴
		schList.clear();
	}

	public boolean loadGameData() {
		boolean ret = true;
		loc_Join = loadLocation(gameName, "JoinPos");
		
	
		if (loc_Join == null) {
			server.egPM.printLog(ms + "[" + disPlayGameName + "] 게임 시작 대기 지점이 설정되지 않았습니다.");
			ret = false;
		}else {
			loc_spectate = loc_Join.clone().add(0,10,0);
		}
	
		if(ret == true) {
			server.egPM.printLog("[" + disPlayGameName + "] 설정 이상 없음");
			doneSetting = ret;
		}
		
		return ret;
	}
	
	public void onCommand(Player p, String[] cmd) {
		if (cmd.length <= 1) {
			p.sendMessage(ms + "[" + disPlayGameName + "] " + "/ata join - 게임 참가");
			p.sendMessage(ms + "[" + disPlayGameName + "] " + "/ata quit - 게임 퇴장");
			p.sendMessage(ms + "[" + disPlayGameName + "] " + "/ata set - 게임 설정");
		} else if (cmd[1].equalsIgnoreCase("set")) {
			if (cmd.length <= 2) {
				p.sendMessage(ms + "[" + disPlayGameName + "] " + "/ata set loc- 게임 지점 설정");
			} else if (cmd[2].equalsIgnoreCase("loc")) {
				if (cmd.length <= 3) {
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "/ata set loc join - 게임 시작 대기 지점 설정");
				} else if (cmd[3].equalsIgnoreCase("join")) {
					saveLocation(gameName, "JoinPos", p.getLocation());
					loadGameData();
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "게임 시작 대기 지점이 설정되었습니다.");
				}else {
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "/ata set loc join - 게임 시작 대기 지점 설정");
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
		} 			
	}

	public void gameQuitPlayer(Player p, boolean announce, boolean giveGold) {
		if (ingamePlayer.contains(p.getName())) {
			ingamePlayer.remove(p.getName());
			server.playerList.put(p.getName(), "로비");
			p.getInventory().clear();
			updateSidebar();
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
				if(ataSidebar != null) ataSidebar.hideFrom(p);
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
	
	public void initLocationList(){
		locationList.clear();
		for(double i = -3.5; i < 4.0; i += 1) {
			for(double j = -3.5; j < 4.0; j += 1) {
				locationList.add(loc_Join.clone().add(i, 9, j));
			}
		}
	}
	
	public Location getRandomLocation() {
		double x = MyUtility.getRandom(-3, 3);
		double z = MyUtility.getRandom(-3, 3);
		
		if(x < 0) x -= 0.5f;
		else if(x > 0) x+= 0.5f;
		
		if(z < 0) z -= 0.5f;
		else if(z > 0) z+= 0.5f;
		
		Location l = loc_Join.clone().add(x, 9 ,z);
		
		return l;
	}
	
	public void performence() {
		EGScheduler sch = new EGScheduler(this);
		sch.schTime = 6;
		sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				if(sch.schTime> 0) {
					sch.schTime--;
					for(int i = 0; i < 3; i++) {
						Location loc = getRandomLocation();
						Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
						FireworkMeta fwm = fw.getFireworkMeta();

						fwm.setPower(1);
						fwm.addEffect(FireworkEffect.builder().withColor(MyUtility.getRandomColor()).flicker(true).build());

						fw.setFireworkMeta(fwm);
						fw.detonate();
						
				        Firework fw2 = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
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
							server.ms_alert + ChatColor.GRAY  + "§c"+winner+"§7님의 §a승리§7로 §c모루피하기§7가 종료 되었습니다.");
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
							server.ms_alert + ChatColor.GRAY  + "무승부로 §c모루피하기§7가 종료 되었습니다.");
					}
				}, 200L);
			} catch(Exception e) {
					endGame(true);
			}
		}
	
	}

	public void endGame(boolean force) {
		if(force) {
			server.egCM.broadCast(server.ms_alert + "모루피하기 게임이 강제 종료 되었습니다.");
		}
		divideSpawn();
		//Bukkit.getLogger().info("mainid: "+mainSch.schId);
		try {
			for(EGScheduler sch : schList) {
				sch.cancelTask(false);
			}
			
		}catch(Exception e) {
			
		}
		mainSch.schTime2 = 4;
		schList.clear();
		ingame = false;
		ending = false;
		ingamePlayer.clear();
		initGame();	
	}

	//////////////// 이벤트
	public class EventHandlerATA extends EGEventHandler {

		private AvoidTheAnvil game;

		public EventHandlerATA(EGServer server, AvoidTheAnvil ata) {
			super(server);
			this.game = ata;
		}

		@EventHandler
		public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
			Player p = e.getPlayer();
			String[] cmd = e.getMessage().split(" ");
			if (cmd[0].equalsIgnoreCase("/ata")) {
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
		public void onEntityChangeBlock(EntityChangeBlockEvent e) {
			if(e.getEntity() instanceof FallingBlock) {
				FallingBlock fallingBlock = (FallingBlock) e.getEntity();
				
				if(fallingBlock.getBlockId() == Material.ANVIL.getId()) {
					if(fallingBlock.getCustomName() != null) {
						if(fallingBlock.getCustomName().equalsIgnoreCase("모루피하기")) {
							Location l = fallingBlock.getLocation();;
							for(Player t : tmpPlayerList) {
								if(l.distance(t.getLocation()) <= 0.8) {
									t.damage(15);
									t.getWorld().playSound(t.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0F, 1.0F);
								}
							}
							e.setCancelled(true);
							fallingBlock.remove();
						}
					}		
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
						e.setCancelled(true);
				        if (cause.equals(DamageCause.VOID)) { //대기실 허공 뎀없음, 텔포		           
				            p.teleport(loc_Join, TeleportCause.PLUGIN);
				            Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
				            	public void run() {
				            		p.teleport(loc_Join, TeleportCause.PLUGIN);
				            	}
				            }, 2l);
				        }
					} else {
						if (cause.equals(DamageCause.VOID)) { //대기실 허공 뎀없음, 텔포
				            gameQuitPlayer(p, true, true);
				            server.spawn(p);
				        }else if(cause.equals(DamageCause.CUSTOM)){
				        	//무시
				        }else {
				        	e.setCancelled(true);
				        }
					}
				}
			}
		}
		
		@EventHandler
		public void onRegainHealth(EntityRegainHealthEvent e) {
			if(!(e.getEntity() instanceof Player)) return;
			Player p = (Player) e.getEntity();
			
			if(!ingamePlayer.contains(p.getName())) return;
			
			e.setCancelled(true);
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
				if(ingamePlayer.contains(p.getName())) 
					gameQuitPlayer(p, true, true);
			}
		}
		
		@EventHandler
		public void onPlayerChat(PlayerChatEvent e){
			Player p = (Player) e.getPlayer();
			if(ingamePlayer.contains(p.getName())) {
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
	
	///////////////////// 모루피하기에 참가한 플레이어들 클래스
	private class AtaPlayer {
		
		public AtaPlayer(Player p, String job) {

		}

	} 
}
