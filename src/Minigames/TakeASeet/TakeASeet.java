package Minigames.TakeASeet;

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
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
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
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
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

public class TakeASeet extends Minigame{
	// 이벤트용
	public EventHandlerTAS event;
	
	public String ms = "§7[§6전체§7] ";

	///////////// private
	// 게임 플레이어 리스트
	private HashMap<String, TasPlayer> playerMap = new HashMap<String, TasPlayer>();

	//////// 게임 관련
	
	public EGScheduler mainSch;
	
	public List<Minecart> cartList = new ArrayList<Minecart>();
	public List<String> willDeathList = new ArrayList<String>();
	public ItemStack item_sword = new ItemStack(Material.STICK);
	
	public Location loc_Safe;
	
	////// 각종 인벤토리
	public Inventory inven_gameHelper;
	
	//////// 사이드바
	private Sidebar tasSidebar;
	private List<SidebarString> textList = new ArrayList<SidebarString>(5);
	
	public TakeASeet(EGServer server) {

		//////////////////// 필수 설정값
		super(server);
		
		ms = "§7[ §e! §7] §f: §c자리뺏기 §f>> "; // 기본 메세지
		gameName = "TakeASeet";
		disPlayGameName = ChatColor.RED+"자리뺏기";
		inventoryGameName = ChatColor.stripColor(disPlayGameName);
		minPlayer = 3;
		maxPlayer = 10;
		startCountTime = 30;
		doneSetting = loadGameData(); // 게임 시작 위치 등등 로드
		/////////////////////// 자동 설정(아이템등등)
		dirSetting("TakeASeet");
		////////////////
		
		mainSch = new EGScheduler(this);
		
		SidebarString tmpLine = new SidebarString("");
		tasSidebar = new Sidebar("§f[ §6게임 현황 §f]", server, 600, tmpLine);
		
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
		loreList.add("§7게임이 시작되면 매라운드마다 무작위로 시간이 정해지고");
		loreList.add("§7이 시간이 다 되면 맵에 마인카트가 생성됩니다.");
		loreList.add("§7마인카트는 남은 인원보다 1개 더 적게 나오며");
		loreList.add("§7카트가 생성되고 10초가 지나기전에 이 카트에 타면");
		loreList.add("§7살아남을 수 있습니다.");
		loreList.add("§7이미 카트에 탄 플레이어의 카트를 부수면");
		loreList.add("§7내리게할 수 있습니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_gameHelper.setItem(11, item);
		
		event = new EventHandlerTAS(server, this);
		// 이 플러그인에 이벤트 적용
		server.getServer().getPluginManager().registerEvents(event, server);
	}

	/////////////// Minigame에서 override 해야하는 부분
	@Override
	public void gameHelpMsg(Player p) {

	}

	@Override
	public void startGame() {
		/////////////// 오프닝
		if(ingamePlayer.size() <= 1) {
			for(String pName : ingamePlayer) {
				Player t = Bukkit.getPlayer(pName);
				server.spawn(t);
				t.sendMessage(ms+"최소 시작인원이 부족하여 시작이 취소됐습니다.");
			}
			endGame(false);
			return;
		}
		ingame = true;
		server.egCM.broadCast(server.ms_alert + ChatColor.RED+ChatColor
				.BOLD+ "자리뺏기 " + ChatColor.GRAY + "가 시작 되었습니다!");
		initGame();
		sendSound(Sound.BLOCK_NOTE_BELL, 1.0f, 1.0f);
		updateSidebar();
		for (String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p)){
				p.setGameMode(GameMode.ADVENTURE);
				// 풀피로 만듬
				MyUtility.healUp(p);
				MyUtility.allClear(p);         
				//p.teleport(loc_Join);
				p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 72000, 0));
				TitleAPI.sendFullTitle(p, 10, 80, 30, ChatColor.RED+"게임 시작", ChatColor.RED+""+disPlayGameName);
				tasSidebar.showTo(p);
			}
		}
		
		///////////////// 진짜 시작
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				for(String pName : ingamePlayer) {
					Player p = Bukkit.getPlayer(pName);
				}
				sendSound(Sound.BLOCK_SLIME_BREAK, 1.0f, 1.3f);
				gameTimer();
			}
		}, 60l);
	}
	
	//////////////////
	
	public void gameTimer() {	
		mainSch.cancelTask(false);	
		if(ingamePlayer.size() <= 1) return;
		willDeathList.clear();
		int randomTime = MyUtility.getRandom(5, 25) * 5;
		sendTitle("", "§e§l곧 카트가 생성됩니다.", 600);
		mainSch.schTime = 0;
		mainSch.schTime2 = 0;
		mainSch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable(){
			public void run(){
				if(!ingame || ending) mainSch.cancelTask(false);
				if(mainSch.schTime++ < randomTime) {
					switch (MyUtility.getRandom(0, 6)) {
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
				} else {
					mainSch.cancelTask(false);
					for(int i = 0; i < ingamePlayer.size() - 1; i++) {
						spawnCart();
					}
					for(String pName : ingamePlayer) {
						Player p = Bukkit.getPlayer(pName);
						timerLevelBar(p, 10, false, true);
						p.getInventory().setItem(0, item_sword);
					}
					sendTitle("§e§l카트에 타세요!", "§b§l카트를 부수면 내리게합니다!", 200);
					Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
						public void run() {
							for(String pName : ingamePlayer) {
								Player p = Bukkit.getPlayer(pName);
								removeItem(p, Material.WOOD_SWORD, 1);
								if(existPlayer(p)) {
									if(!p.isInsideVehicle()) {
										TitleAPI.sendFullTitle(p, 10, 200, 10, "", "§b§l카트를 타지 못했습니다...");
										willDeathList.add(p.getName());
									}else {
										p.leaveVehicle();
										p.teleport(loc_Safe, TeleportCause.PLUGIN);
									}
								}
							}
							clearCart();
						}
					}, 200l);
					
					Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
						public void run() {
							TNTPrimed tnt = (TNTPrimed) loc_Join.getWorld().spawnEntity(loc_Join.clone().add(0,6,0), EntityType.PRIMED_TNT);	
							TNTPrimed tnt1 = (TNTPrimed) loc_Join.getWorld().spawnEntity(loc_Join.clone().add(7,6,7), EntityType.PRIMED_TNT);
							TNTPrimed tnt2 = (TNTPrimed) loc_Join.getWorld().spawnEntity(loc_Join.clone().add(7,6,-7), EntityType.PRIMED_TNT);
							TNTPrimed tnt3 = (TNTPrimed) loc_Join.getWorld().spawnEntity(loc_Join.clone().add(-7,6,7), EntityType.PRIMED_TNT);
							TNTPrimed tnt4 = (TNTPrimed) loc_Join.getWorld().spawnEntity(loc_Join.clone().add(-7,6,-7), EntityType.PRIMED_TNT);
							
							sendSound(Sound.ENTITY_TNT_PRIMED);
						}
					}, 240l);
					
					Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
						public void run() {
							for(String pName : willDeathList) {
								Player p = Bukkit.getPlayer(pName);
								if(existPlayer(p)) {
										p.setHealth(0);
										p.sendMessage(ms+"카트에 타지 못해 사망했습니다.");
								}
							}		
						}
					}, 320l);
					
					Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
						public void run() {
							for(String pName : ingamePlayer) {
								Player p = Bukkit.getPlayer(pName);
								if(existPlayer(p)) {
									p.teleport(loc_Join, TeleportCause.PLUGIN);
								}
							}
							gameTimer();
						}
					}, 400l);
				}
			}
		}, 160l, 4L);
		
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
		
		tasSidebar.setEntries(textList);
		tasSidebar.update();
		
		/*for(String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p)) 
				tasSidebar.showTo(p);
		}*/
	}

	public void initGame() {
		lobbyStart = false;
		ending = false;
		mainSch.addSchList(this);
		//스케쥴
		schList.clear();
	}

	public boolean loadGameData() {
		boolean ret = true;
		loc_Join = loadLocation(gameName, "JoinPos");
		loc_Safe = loadLocation(gameName, "SafePos");
		
		if (loc_Join == null) {
			server.egPM.printLog(ms + "[" + disPlayGameName + "] 게임 시작 대기 지점이 설정되지 않았습니다.");
			ret = false;
		}else {
			loc_spectate = loc_Join;
		}
		
		if (loc_Safe == null) {
			server.egPM.printLog(ms + "[" + disPlayGameName + "] 게임 안전지점이 설정되지 않았습니다.");
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
			p.sendMessage(ms + "[" + disPlayGameName + "] " + "/tas join - 게임 참가");
			p.sendMessage(ms + "[" + disPlayGameName + "] " + "/tas quit - 게임 퇴장");
			p.sendMessage(ms + "[" + disPlayGameName + "] " + "/tas set - 게임 설정");
		} else if (cmd[1].equalsIgnoreCase("set")) {
			if (cmd.length <= 2) {
				p.sendMessage(ms + "[" + disPlayGameName + "] " + "/tas set loc - 게임 지점 설정");
			} else if (cmd[2].equalsIgnoreCase("loc")) {
				if (cmd.length <= 3) {
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "/tas set loc join - 게임 시작 대기 지점 설정");
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "/tas set loc safe - 게임 안전 지점 설정");
				} else if (cmd[3].equalsIgnoreCase("join")) {
					saveLocation(gameName, "JoinPos", p.getLocation());
					loadGameData();
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "게임 시작 대기 지점이 설정되었습니다.");
				}else if (cmd[3].equalsIgnoreCase("safe")) {
					saveLocation(gameName, "SafePos", p.getLocation());
					loadGameData();
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "게임 안전 지점이 설정되었습니다.");
				}else {
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "/tas set loc join - 게임 시작 대기 지점 설정");
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "/tas set loc safe - 게임 안전 지점 설정");
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

	
	public void clearCart() {
		for(Minecart cart : cartList){
			cart.remove();
		}
	}	
	
	public void spawnCart() {
		Location l = loc_Join.clone();
		l.add(MyUtility.getRandom(-12, 12), 5, MyUtility.getRandom(-12, 12));
		Minecart cart = l.getWorld().spawn(l, Minecart.class);
		cart.setGlowing(true);
		cartList.add(cart);
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
				if(tasSidebar != null) tasSidebar.hideFrom(p);
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
					for(int i = 0; i < 3; i++) {
						Location tmpL = loc_Join.clone();
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
							server.ms_alert + ChatColor.GRAY  + "§c"+winner+"§7님의 §a승리§7로 §c자리뺏기§7가 종료 되었습니다.");
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
							server.ms_alert + ChatColor.GRAY  + "무승부로 §c자리뺏기§7가 종료 되었습니다.");
					}
				}, 200L);
			} catch(Exception e) {
					endGame(true);
			}
		}
	
	}

	public void endGame(boolean force) {
		if(force) {
			server.egCM.broadCast(server.ms_alert + "자리뺏기 게임이 강제 종료 되었습니다.");
		}
		divideSpawn();
		//Bukkit.getLogger().info("mainid: "+mainSch.schId);
		try {
			for(EGScheduler sch : schList) {
				sch.cancelTask(false);
			}
			
		}catch(Exception e) {
			
		}
		try {
			for(Minecart cart : cartList) {
				if(cart != null) cart.remove();
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
	public class EventHandlerTAS extends EGEventHandler {

		private TakeASeet game;

		public EventHandlerTAS(EGServer server, TakeASeet tas) {
			super(server);
			this.game = tas;
		}

		@EventHandler
		public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
			Player p = e.getPlayer();
			String[] cmd = e.getMessage().split(" ");
			if (cmd[0].equalsIgnoreCase("/tas")) {
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
				        if (cause.equals(DamageCause.VOID) && !ingame) { //대기실 허공 뎀없음, 텔포		           
				            p.teleport(loc_Join, TeleportCause.PLUGIN);
				        }
				        e.setCancelled(true);
					} else {
						//if(p.isInsideVehicle()) p.leaveVehicle();
						e.setDamage(0.01);
						if (cause.equals(DamageCause.VOID)) { 
				            gameQuitPlayer(p, true, true);
				            server.spawn(p);
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
					gameQuitPlayer(p, true, true);
					e.getDrops().clear();
				}
					
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
		public void onBreakVehicle(VehicleDestroyEvent e){
			if(!(e.getAttacker() instanceof Player)) return;
			Player p = (Player) e.getAttacker();
			if(!ingamePlayer.contains(p.getName())) return;
			e.setCancelled(true);
			Vehicle vehicle = e.getVehicle();
			List<Entity> nearby = vehicle.getNearbyEntities(1, 1, 1);
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
			if(near == null) return;
			Player insider = (Player) near;
			if(insider.isInsideVehicle()) insider.leaveVehicle();
		}
		
		@EventHandler
		public void onEnterVehicle(VehicleEnterEvent e){
			if(!(e.getEntered() instanceof Player)) return;
			Player p = (Player) e.getEntered();
			if(!ingamePlayer.contains(p.getName())) return;
			ActionBarAPI.sendActionBar(p, "§e§l■■ 탑승! ■■", 100);
		}
		
		@EventHandler
		public void onExitVehicle(VehicleExitEvent e){
			if(!(e.getExited() instanceof Player)) return;
			Player p = (Player) e.getExited();
			if(!ingamePlayer.contains(p.getName())) return;
			ActionBarAPI.sendActionBar(p, "§e§l■■ 하차! ■■", 100);
		}
	}
	
	///////////////////// 자리뺏기에 참가한 플레이어들 클래스
	private class TasPlayer {
		
		public TasPlayer(Player p, String job) {

		}

	}
	
}
