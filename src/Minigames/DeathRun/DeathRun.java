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
	// 이벤트용
	public EventHandlerDTR event;
	
	public String ms = "§7[§6전체§7] ";

	///////////// private
	// 게임 플레이어 리스트
	//private HashMap<String, DtrPlayer> playerMap = new HashMap<String, DtrPlayer>();
	private List<String> cooldownlist = new ArrayList<String>();
	private String cmdMain;
	//////// 게임 관련
	public List<Location> loc_Start = new ArrayList<Location>(8);
	public int gameStep = 0;
	public EGScheduler mainSch;
	public LinkedList<Location> changingList = new LinkedList<Location>();
	public List<Location> backUpBlockList = new ArrayList<Location>();
	public List<Location> blockList = new ArrayList<Location>();
	
	////// 각종 인벤토리
	public Inventory inven_gameHelper;
	
	// 각종 아이템
	public ItemStack item_speedUp;
	
	//복구용 맵
	public RepairMap map;
	public String locPath;
	
	//////// 사이드바
	private Sidebar dtrSidebar;
	private List<SidebarString> textList = new ArrayList<SidebarString>(5);
	
	public DeathRun(EGServer server, String cmdMain) {

		//////////////////// 필수 설정값
		super(server);
		
		this.cmdMain = cmdMain;
		
		ms = "§7[ §e! §7] §f: §c데스런 §f>> "; // 기본 메세지
		gameName = "DeathRun";
		disPlayGameName = ChatColor.RED+"데스런";
		inventoryGameName = ChatColor.stripColor(disPlayGameName);
		minPlayer = 2;
		maxPlayer = 6;
		startCountTime = 40;
		//복구용 맵 생성
		locPath = server.getDataFolder().getPath() + "/" + gameName + "/Location";
		map = new RepairMap(server, "deathRun");
		doneSetting = loadGameData(); // 게임 시작 위치 등등 로드	
		
		/////////////////////// 자동 설정(아이템등등)
		dirSetting("DeathRun");
		////////////////
		
		//map.loadData(locPath);
		
		mainSch = new EGScheduler(this);
		
		SidebarString tmpLine = new SidebarString("");
		dtrSidebar = new Sidebar("§f[ §6게임 현황 §f]", server, 600, tmpLine);
		
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
		loreList.add("§7자신을 제외한 모든 플레이어를");
		loreList.add("§7탈락시키면 승리합니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_gameHelper.setItem(10, item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c진행방식 §7-");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7게임이 시작되면 맵 바닥이 무작위로 사라집니다.");
		loreList.add("§7바닥은 노랑->주황->빨강 순으로 색이 변하며 사라지기");
		loreList.add("§7때문에 바닥을 항시 주시하고 계셔야합니다.");
		loreList.add("§7바닥이 사라져 나락으로 떨어지면 탈락합니다.");
		loreList.add("§7막대기로 서로를 밀칠 수도 있습니다.");
		loreList.add("§7막대기를 우클릭시 전력질주가 가능합니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_gameHelper.setItem(11, item);
		
		//삽
		item_speedUp = new ItemStack(Material.STICK, 1);
		meta = item_speedUp.getItemMeta();
		meta.setDisplayName("§f[ §b막대 (밀치기, 전력질주) §f]");
		//loreList = new ArrayList<String>();
		item_speedUp.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
		item_speedUp.setItemMeta(meta);
		
		event = new EventHandlerDTR(server, this);
		// 이 플러그인에 이벤트 적용
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

	/////////////// Minigame에서 override 해야하는 부분
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
		server.egCM.broadCast(server.ms_alert + ChatColor.RED+ChatColor.BOLD + disPlayGameName + ChatColor.GRAY + " 게임이 " + ChatColor.GRAY + "곧 시작됩니다.");
		sendBossbar(ChatColor.GREEN+"게임 시작까지", startSch.schTime);
		startSch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				if (startSch.schTime > 0) {
					if (startSch.schTime == 30 || startSch.schTime == 10) {
						sendMessage(server.ms_alert + ChatColor.GRAY + "게임이 "
								+ ChatColor.AQUA + startSch.schTime + ChatColor.GRAY + "초 뒤 시작됩니다.");
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
				t.sendMessage(ms+"최소 시작인원이 부족하여 시작이 취소됐습니다.");
			}
			endGame(false);
			return;
		}
		server.egCM.broadCast(server.ms_alert + ChatColor.RED+ChatColor
				.BOLD+ "데스런 " + ChatColor.GRAY + "가 시작 되었습니다!");
		initGame();
		ingame = true;
		gameStep = 1;
		/////////////// 오프닝	
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
				countDown(5, "경기 시작까지");
				for (String pName : ingamePlayer) {
					Player p = Bukkit.getPlayer(pName);
					if(existPlayer(p)){
						p.setGameMode(GameMode.ADVENTURE);
						// 풀피로 만듬
						MyUtility.healUp(p);
						MyUtility.allClear(p);         
						//p.teleport(loc_Join);
						TitleAPI.sendFullTitle(p, 10, 80, 30, ChatColor.RED+"게임 시작", ChatColor.RED+""+disPlayGameName);	
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
		
		///////////////// 진짜 시작
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
		line = new SidebarString("§e남은인원 §f: §a"+ingamePlayer.size()+"명");
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
		//스케쥴
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
			server.egPM.printLog(ms + "[" + disPlayGameName + "] 게임 시작 대기 지점이 설정되지 않았습니다.");
			ret = false;
		}else {
			loc_spectate = loc_Start.get(0).clone().add(0,10,0);
		}
		if (loc_Start.size() < 8) {
			server.egPM.printLog(ms + "[" + disPlayGameName + "] 게임 시작 지점이 완벽하게 설정되지 않았습니다.");
			ret = false;
		}
		if (!map.loadData(locPath)) {
			server.egPM.printLog("[" + disPlayGameName + "] 맵이 설정되지 않았습니다.");
			ret = false;
		}
		
		if(ret == true) {
			server.egPM.printLog("[" + disPlayGameName + "] 설정 이상 없음");
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
								p.sendMessage("[" + disPlayGameName + "] " + "게임 시작 대기 지점이 설정되었습니다.");
							} else if (cmd[3].equalsIgnoreCase("map1")) {
								map.setPos1(p.getTargetBlock(null, 3).getLocation());
								p.sendMessage("[" + disPlayGameName + "] " + p.getTargetBlock(null, 3).getLocation()+" 설정완료");
								map.saveMapBlocks(locPath, Material.STAINED_CLAY);
							}  else if (cmd[3].equalsIgnoreCase("map2")) {
								map.setPos2(p.getTargetBlock(null, 3).getLocation());
								p.sendMessage("[" + disPlayGameName + "] " + p.getTargetBlock(null, 3).getLocation()+" 설정완료");
								map.saveMapBlocks(locPath, Material.STAINED_CLAY);
							} else if (cmd[3].equalsIgnoreCase("start")) {
								saveLocation(gameName, "start"+cmd[4], p.getLocation());
								p.sendMessage("[" + disPlayGameName + "] " + p.getTargetBlock(null, 3).getLocation()+" 설정완료");
							} else {
								p.sendMessage("[" + disPlayGameName + "] " + cmd[3]+"은 올바르지 않은 인수");
							}
							loadGameData();
						} else {
							p.sendMessage(ms+"인수를 입력해주세요.");
						}
					} else {
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" set loc join - 게임 시작 대기 지점 설정");
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" set loc start 1~8 - 게임 시작 지점 설정");
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" set loc map1 - 맵1지점");
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" set loc map2 - 맵2지점");
					}
				} else {
					p.sendMessage("[" + disPlayGameName + "] " + cmdMain + " set loc- 게임 지점 설정");
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
			p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" join - 게임 참가");
			p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" quit - 게임 퇴장");
			p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" set - 게임 설정");
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
				if(dtrSidebar != null) dtrSidebar.hideFrom(p);
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
			sendTitle("§6§l승리", ChatColor.GRAY + "당신이 최후의 1인입니다.", 70);
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
							server.ms_alert + ChatColor.GRAY  + "§c"+winner+"§7님의 §a승리§7로 §c데스런§7이 종료 되었습니다.");
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
							server.ms_alert + ChatColor.GRAY  + "무승부로 §c데스런§7이 종료 되었습니다.");
					}
				}, 200L);
			} catch(Exception e) {
					endGame(true);
			}
		}
		
	
	}

	public void endGame(boolean force) {
		if(force) {
			server.egCM.broadCast(server.ms_alert + "데스런 게임이 강제 종료 되었습니다.");
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

	//////////////// 이벤트
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
			if(ingamePlayer.contains(p.getName())) { //게임 참여해야지만 가능한 명령어
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
				        if (cause.equals(DamageCause.VOID) && !ingame) { //대기실 허공 뎀없음, 텔포		           
				            p.teleport(loc_Join, TeleportCause.PLUGIN);
				        }
					} else {
						if (cause.equals(DamageCause.VOID)) { //허공뎀 = 탈락
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
			} else if(e.getItem().getType() == Material.STICK) {
				if(!cooldownlist.contains(p.getName())) {
					cooldownlist.add(p.getName());
					p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 1));
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.5f);
					timerLevelBar(p, 3, false, true);
					Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
						public void run() {
							cooldownlist.remove(p.getName());
							TitleAPI.sendFullTitle(p, 0, 40, 0, "", "§e§l전력 질주 준비됨");
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
		
		@EventHandler
		public void onPlayerMove(PlayerMoveEvent e) {
			Player p = (Player) e.getPlayer();
			if(ingamePlayer.contains(p.getName()) && ingame) {
				///위치이동 아니면 캔슬
				if(e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockY() == e.getTo().getBlockY()
						&& e.getFrom().getBlockZ() == e.getTo().getBlockZ()) return;
				if(gameStep == 2) egCancelMovement(e);
			}
		}
	}
	
	///////////////////// 데스런에 참가한 플레이어들 클래스
	private class DtrPlayer {
		
		public DtrPlayer(Player p, String job) {

		}

	}
}
	
