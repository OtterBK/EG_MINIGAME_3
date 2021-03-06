package Minigames.FirstHit;

import java.io.File;
import java.io.IOException;
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
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.entity.Minecart;
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

import com.coloredcarrot.api.sidebar.Sidebar;
import com.coloredcarrot.api.sidebar.SidebarString;
import com.connorlinfoot.titleapi.TitleAPI;

import EGServer.EGServer;
import EGServer.ServerManager.EGEventHandler;
import EGServer.ServerManager.EGScheduler;
import Minigames.Minigame;
import Utility.MyUtility;

public class FirstHit extends Minigame{
	// 이벤트용
	public EventHandlerFHT event;
	
	public String ms = "§7[§6전체§7] ";

	///////////// private
	// 게임 플레이어 리스트
	private HashMap<String, FhtPlayer> playerMap = new HashMap<String, FhtPlayer>();

	//////// 게임 관련
	
	public EGScheduler mainSch;
	
	public List<Location> loc_startList = new ArrayList<Location>(20);
	
	public ItemStack item_sword = new ItemStack(Material.STICK);
	public ItemStack item_flashBang;
	public ItemStack item_slowArrow;
	public ItemStack item_teleportSnow;
	public ItemStack item_noAttackFireball;
	public ItemStack item_boost;

	
	public PotionEffect speedPt = new PotionEffect(PotionEffectType.SPEED, 72000, 2);
	public PotionEffect jumpPt = new PotionEffect(PotionEffectType.JUMP, 72000, 1);
	public PotionEffect slowPt = new PotionEffect(PotionEffectType.SLOW, 100, 2);
	public PotionEffect weaknessPt = new PotionEffect(PotionEffectType.WEAKNESS, 100, 250);

	public HashMap<String, Integer> killMap = new HashMap<String, Integer>();
	
	////// 각종 인벤토리
	public Inventory inven_gameHelper;
	
	//////// 사이드바
	private Sidebar fhtSidebar;
	private List<SidebarString> textList = new ArrayList<SidebarString>(5);
	
	public FirstHit(EGServer server, String gameName, String displayGameName, String cmdMain) {

		//////////////////// 필수 설정값
		super(server);
		
		ms = "§7[ §e! §7] §f: §c선빵게임 §f>> "; // 기본 메세지
		this.gameName = gameName;
		this.disPlayGameName = displayGameName;
		this.cmdMain = cmdMain;
		inventoryGameName = ChatColor.stripColor(disPlayGameName);
		minPlayer = 2;
		maxPlayer = 7;
		startCountTime = 30;
		doneSetting = loadGameData(); // 게임 시작 위치 등등 로드
		/////////////////////// 자동 설정(아이템등등)
		dirSetting(gameName);
		////////////////
		
		mainSch = new EGScheduler(this);
		
		SidebarString tmpLine = new SidebarString("");
		fhtSidebar = new Sidebar("§f[ §6게임 현황 §f]", server, 600, tmpLine);
		
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
		loreList.add("§7먼저 25킬을 달성하면 승리합니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_gameHelper.setItem(10, item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c진행방식 §7-");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7게임이 시작되면 막대기를 줍니다.");
		loreList.add("§7이 막대로 적을 타격할 시 즉사시킬수 있습니다.");
		loreList.add("§7먼저 적을 공격하거나 뒤에서 공격하여");
		loreList.add("§7킬수를 올리세요.");
		loreList.add("§7또한 적을 죽일시 50% 확률로 아이템을 얻습니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_gameHelper.setItem(11, item);
		
		item_flashBang = new ItemStack(Material.POTATO_ITEM, 1);
		meta = item_flashBang.getItemMeta();
		meta.setDisplayName("§f[ §c섬광탄 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7우클릭시 섬광탄을 던져");
		loreList.add("§7주변 플레이어에게 실명을 겁니다.");
		loreList.add("");
		meta.setLore(loreList);
		item_flashBang.setItemMeta(meta);
		
		item_slowArrow = new ItemStack(Material.ARROW, 1);
		meta = item_slowArrow.getItemMeta();
		meta.setDisplayName("§f[ §c끈끈이 화살 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7우클릭시 끈끈히 화살을 던져");
		loreList.add("§7맞은 적에게 5초간 구속 3버프를 줍니다.");
		loreList.add("");
		meta.setLore(loreList);
		item_slowArrow.setItemMeta(meta);
		
		item_teleportSnow = new ItemStack(Material.SNOW_BALL, 1);
		meta = item_teleportSnow.getItemMeta();
		meta.setDisplayName("§f[ §c이동 눈덩이 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7우클릭시 눈을 던져");
		loreList.add("§7맞은 적의 뒤로 이동합니다.");
		loreList.add("");
		meta.setLore(loreList);
		item_teleportSnow.setItemMeta(meta);
		
		item_noAttackFireball = new ItemStack(Material.FIREBALL, 1);
		meta = item_noAttackFireball.getItemMeta();
		meta.setDisplayName("§f[ §c미니 대포 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7우클릭시 화염구를 던져");
		loreList.add("§7맞은 적을 5초간 공격불가 상태로 만듭니다.");
		loreList.add("");
		meta.setLore(loreList);
		item_noAttackFireball.setItemMeta(meta);
		
		item_boost = new ItemStack(Material.MAGMA_CREAM, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§f[ §c부스트 §f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7우클릭시 바라보는 방향으로");
		loreList.add("§7크게 점프합니다.");
		loreList.add("");
		meta.setLore(loreList);
		item_boost.setItemMeta(meta);
		
		event = new EventHandlerFHT(server, this);
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
				.BOLD+ disPlayGameName + ChatColor.GRAY + "이 시작 되었습니다!");
		initGame();
		sendSound(Sound.BLOCK_NOTE_BELL, 1.0f, 1.0f);
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
				killMap.put(p.getName(), 0);
				fhtSidebar.showTo(p);
			}
		}
		updateSidebar();
		
		///////////////// 진짜 시작
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				for(String pName : ingamePlayer) {
					Player p = Bukkit.getPlayer(pName);
					giveBaseItem(p);
				}
				sendSound(Sound.BLOCK_SLIME_BREAK, 1.0f, 1.3f);
			}
		}, 60l);
	}
	
	//////////////////
	
	public void updateSidebar() {
		if(!ingame) return;
		textList.clear();
		
		for(String pName : killMap.keySet()) {
			SidebarString line = new SidebarString(pName+"§e §f: §a"+killMap.get(pName)+"킬");
			textList.add(line);
		}
		
		fhtSidebar.setEntries(textList);
		fhtSidebar.update();
		
		/*for(String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p)) 
				fhtSidebar.showTo(p);
		}*/
	}

	public void initGame() {
		playerMap.clear();
		killMap.clear();
		lobbyStart = false;
		ending = false;
		mainSch.addSchList(this);
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
			loc_spectate = loc_Join;
		}
		
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
			server.egPM.printLog(ms + "[" + disPlayGameName + "] 게임 시작 지점이 설정되지 않았습니다.");
			ret = false;
		}
		
		if(ret == true) {
			server.egPM.printLog("[" + disPlayGameName + "] 설정 이상 없음");
			doneSetting = ret;
		}
		
		return ret;
	}
	
	public void giveBaseItem(Player p) {
		p.getInventory().addItem(item_sword);
		p.getInventory().setItem(8, helpItem);
		
		p.addPotionEffect(speedPt);
		p.addPotionEffect(jumpPt);
		
		TitleAPI.sendFullTitle(p, 0, 50, 0, "§6§l전투 개시!", "§7§l적을 먼저 공격하세요!");
		p.getWorld().playSound(p.getLocation(), Sound.BLOCK_NOTE_BELL, 1.0f, 1.5f);
	}
	
	public int addKill(Player p) {
		int nowKill = 0;
		TitleAPI.sendFullTitle(p, 0, 30, 0, "§6§l빵!!!", "");
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 0.5f);
		if(killMap.containsKey(p.getName())) {
			nowKill = killMap.get(p.getName());
			nowKill += 1;
			killMap.put(p.getName(), nowKill);
			if(nowKill >= 25) {
				setWinner(p);
			}
			if(MyUtility.getRandom(0, 1) == 0) {
				int rn = MyUtility.getRandom(0, 3);
				switch(rn) {
				case 0: p.getInventory().addItem(item_flashBang); break;
				case 1: p.getInventory().addItem(item_slowArrow); break;
				case 2: p.getInventory().addItem(item_teleportSnow); break;
				case 3: p.getInventory().addItem(item_noAttackFireball); break;
				default: p.getInventory().addItem(item_flashBang); break;
				}
			}		
		}
		updateSidebar();
		
		return nowKill;
	}
	
	public void onCommand(Player p, String[] cmd) {
		if (cmd.length <= 1) {
			p.sendMessage(ms + "[" + disPlayGameName + "] " + cmdMain +" join - 게임 참가");
			p.sendMessage(ms + "[" + disPlayGameName + "] " + cmdMain +" quit - 게임 퇴장");
			p.sendMessage(ms + "[" + disPlayGameName + "] " + cmdMain +" set - 게임 설정");
		} else if (cmd[1].equalsIgnoreCase("set")) {
			if (cmd.length <= 2) {
				p.sendMessage(ms + "[" + disPlayGameName + "] " + cmdMain +" set loc - 게임 지점 설정");
			} else if (cmd[2].equalsIgnoreCase("loc")) {
				if (cmd.length <= 3) {
					p.sendMessage(ms + "[" + disPlayGameName + "] " + cmdMain +" set loc join - 게임 시작 대기 지점 설정");
					p.sendMessage(ms + "[" + disPlayGameName + "] " + cmdMain + " set loc startpos - 게임 시작 지점 추가");
				} else if (cmd[3].equalsIgnoreCase("join")) {
					saveLocation(gameName, "JoinPos", p.getLocation());
					loadGameData();
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "게임 시작 대기 지점이 설정되었습니다.");
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
					p.sendMessage(ms + "[" + disPlayGameName + "] " + startPosCnt + " 번째 게임 시작 지점이 추가되었습니다.");
				} else {
					p.sendMessage(ms + "[" + disPlayGameName + "] " + cmdMain + " set loc join - 게임 시작 대기 지점 설정");
					p.sendMessage(ms + "[" + disPlayGameName + "] " + cmdMain + " set loc startpos - 게임 시작 지점 추가");
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
		} 	else if (cmd[1].equalsIgnoreCase("debug0")) {
			p.getInventory().addItem(item_flashBang);
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
						sendMessage(ms + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " 님이 퇴장하셨습니다.");
						sendSound(Sound.BLOCK_NOTE_CHIME,1.5f, 2.0f);
					}
				} else {
					if(announce) {
						sendMessage(ms + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " 님이 퇴장하셨습니다.");
						sendSound(Sound.BLOCK_NOTE_GUITAR,1.5f, 2.0f);
					}
				}
				if(fhtSidebar != null) fhtSidebar.hideFrom(p);
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
	
	public void performence(Player p) {
		EGScheduler sch = new EGScheduler(this);
		sch.schTime = 6;
		sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				if(sch.schTime> 0) {
					sch.schTime--;
					for(int i = 0; i < 3; i++) {
						Location tmpL = null;
						if(tmpL == null) {
							tmpL = loc_Join.clone().add(0,3,0);
						}else {
							tmpL = p.getLocation();
						}
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

	public Location getRandomLoc() {
		if(loc_startList.size() <= 0) return loc_Join;
		Location l = loc_startList.get(MyUtility.getRandom(0, loc_startList.size()-1));
		return MyUtility.getTopLocation(l, 100);
	}
	
	public void useFlashBang(Player p) {
		removeItem(p, item_flashBang.getType(), 1);
		p.getWorld().playSound(p.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1.5F, 0.1F);
		
		ItemStack throwStack = new ItemStack(item_flashBang.getType());
        throwStack.setAmount(1);
        Location pLoc = p.getEyeLocation();
   
        Item thrownItem = p.getWorld().dropItem(pLoc, throwStack);
        thrownItem.setVelocity(pLoc.getDirection().multiply(1f));
        thrownItem.setPickupDelay(200);
        
        Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
        	public void run() {
        		Location sl = thrownItem.getLocation();
        		sl.getWorld().playSound(p.getLocation(), Sound.ENTITY_FIREWORK_LARGE_BLAST, 1.5f, 0.5f);
				sl.getWorld().spawnParticle(Particle.SMOKE_LARGE, sl, 40, 1F, 1f, 1f, 0.20f);
				thrownItem.remove();
				for(String tName : ingamePlayer) {
					Player t = Bukkit.getPlayer(tName);
					if(existPlayer(t)) {
						double dis = t.getLocation().distance(sl);
						int time = (int) (6 - dis)+2;
						if(dis <= 6) {
							t.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS , 30*time, 0));
						}
					}			
				}
        	}
        }, 24l);
	}
	
	public void useArrow(Player p) {
		removeItem(p, item_slowArrow.getType(), 1);
		
		Location pel = p.getEyeLocation();
		
		Arrow bulletArrow = p.launchProjectile(Arrow.class);
        bulletArrow.setVelocity(pel.getDirection().multiply(3));
        bulletArrow.setShooter(p);
		
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_EGG_THROW, 1.0f, 2.0f);
	}
	
	public void useSnow(Player p) {
		removeItem(p, item_teleportSnow.getType(), 1);
		
		Location pel = p.getEyeLocation();
		
		Snowball snowball = p.launchProjectile(Snowball.class);
		snowball.setVelocity(pel.getDirection().multiply(2));
		snowball.setShooter(p);
		
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_SNOWBALL_THROW, 1.0f, 0.5f);
	}
	
	public void useFireball(Player p) {
		removeItem(p, item_noAttackFireball.getType(), 1);
		
		Location pel = p.getEyeLocation();
		
		Fireball fireball = p.launchProjectile(Fireball.class);
		fireball.setVelocity(pel.getDirection().multiply(2));
		fireball.setShooter(p);
		
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 2.0f);
	}
	
	public void useBoost(Player p) {
		removeItem(p, item_boost.getType(), 1);
		
		p.setVelocity(p.getEyeLocation().getDirection().multiply(4.5f));
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_FIREWORK_LARGE_BLAST, 1.0f, 0.5f);
		p.getWorld().spawnParticle(Particle.CLOUD, p.getLocation(), 5, 0.1F, 0.1f, 0.1f, 0.1f);
	}
	
	private void setWinner() {
		if(ending) return;
		ending = true;
		mainSch.cancelTask(true);
		if(ingamePlayer.size() >= 1) {
			String winner = ingamePlayer.get(0);
			sendTitle("승리", ChatColor.GRAY + "당신만 남았습니다.", 70);
			sendSound(Sound.BLOCK_NOTE_BELL, 1.0f, 0.5f);
			
			performence(null);			
			
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
							server.ms_alert + ChatColor.GRAY  + "§c"+winner+"§7님의 §a승리§7로 §c선빵게임§7가 종료 되었습니다.");
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
							server.ms_alert + ChatColor.GRAY  + "무승부로 §c선빵게임§7가 종료 되었습니다.");
					}
				}, 200L);
			} catch(Exception e) {
					endGame(true);
			}
		}
	
	}
	
	private void setWinner(Player p) {
		if(ending) return;
		ending = true;
		mainSch.cancelTask(true);
		String winner = p.getName();
		for(String tName : ingamePlayer) {
			Player t = Bukkit.getPlayer(tName);
			if(existPlayer(t)) {
				if(tName.equalsIgnoreCase(winner)) {
					TitleAPI.sendFullTitle(t, 10, 80, 10, "승리", ChatColor.GRAY + "당신이 우위를 선점했습니다!");
				}else {
					TitleAPI.sendFullTitle(t, 10, 80, 10, "패배", ChatColor.GRAY + winner+"님이 먼저 목표를 달성했습니다.");
				}
				MyUtility.allClear(t);
			}
		}
		if(ingamePlayer.size() >= 1) {
			
			sendSound(Sound.BLOCK_NOTE_BELL, 1.0f, 0.5f);
			
			performence(p);			
			
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
							server.ms_alert + ChatColor.GRAY  + "§c"+winner+"§7님의 §a승리§7로 §c선빵게임§7가 종료 되었습니다.");
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
							server.ms_alert + ChatColor.GRAY  + "무승부로 §c선빵게임§7가 종료 되었습니다.");
					}
				}, 200L);
			} catch(Exception e) {
					endGame(true);
			}
		}
	
	}

	public void endGame(boolean force) {
		if(force) {
			server.egCM.broadCast(server.ms_alert + "선빵게임 게임이 강제 종료 되었습니다.");
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
	public class EventHandlerFHT extends EGEventHandler {

		private FirstHit game;

		public EventHandlerFHT(EGServer server, FirstHit fht) {
			super(server);
			this.game = fht;
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
				        if (cause.equals(DamageCause.VOID) && !ingame) { //대기실 허공 뎀없음, 텔포		           
				            p.teleport(loc_Join, TeleportCause.PLUGIN);
				        }
				        e.setCancelled(true);
					} else {
						if(cause.equals(DamageCause.ENTITY_ATTACK) || cause.equals(DamageCause.PROJECTILE)) {
							e.setDamage(0.1);	
						}else if(cause.equals(DamageCause.VOID)){
							p.teleport(getRandomLoc(), TeleportCause.PLUGIN);
						}else {
							e.setCancelled(true);
						}
					}
				}
			}
		}
		
		@EventHandler
		public void onPlayerHit(EntityDamageByEntityEvent e) {
			if(e.getEntity() instanceof Player) {
				Player p = (Player)e.getEntity();
				Player d = null;
				
				if (e.getDamager() instanceof Snowball) { //화살과 눈덩이에대한 공격자 설정
					Snowball snowball = (Snowball) e.getDamager();
					if (snowball.getShooter() instanceof Player) {
						d = (Player) snowball.getShooter();
						if(!(ingamePlayer.contains(p.getName()) && ingamePlayer.contains(d.getName()))) return;
						Location tl = p.getLocation().subtract(p.getEyeLocation().getDirection());
						tl.setY(p.getLocation().getY());
						d.teleport(tl, TeleportCause.PLUGIN);
						return;
					}
				}
				if (e.getDamager() instanceof Arrow) {
					Arrow arrow = (Arrow) e.getDamager();
					if (arrow.getShooter() instanceof Player) {
						d = (Player) arrow.getShooter();
						if(!(ingamePlayer.contains(p.getName()) && ingamePlayer.contains(d.getName()))) return;
						p.addPotionEffect(slowPt);
						return;
					}
				}
				
				if (e.getDamager() instanceof Fireball) {
					Fireball fireball = (Fireball) e.getDamager();
					if (fireball.getShooter() instanceof Player) {
						d = (Player) fireball.getShooter();
						if(!(ingamePlayer.contains(p.getName()) && ingamePlayer.contains(d.getName()))) return;
						p.addPotionEffect(weaknessPt);
						return;
					}
				}
				
				if (e.getDamager() instanceof Player) {
					d = (Player) e.getDamager();
				}
				
				if(d == null) return;
				if(!(ingamePlayer.contains(p.getName()) && ingamePlayer.contains(d.getName()))) return;
				if(ingame) {
					ItemStack item = d.getInventory().getItemInMainHand();
					if(item != null) {
						if(item.getType() == Material.STICK) {
							Location l = p.getLocation().clone().add(0,-1,0);
							p.teleport(getRandomLoc(), TeleportCause.PLUGIN);
							addKill(d);
							Firework fw = (Firework) l.getWorld().spawnEntity(l, EntityType.FIREWORK);
							FireworkMeta fwm = fw.getFireworkMeta();

							fwm.setPower(0);
							fwm.addEffect(FireworkEffect.builder().withColor(MyUtility.getRandomColor()).flicker(true).build());

							fw.setFireworkMeta(fwm);
							fw.detonate();
							
					        Firework fw2 = (Firework) l.getWorld().spawnEntity(l, EntityType.FIREWORK);
					        fw2.setFireworkMeta(fwm);
						}
					}
				} else {
					e.setCancelled(true);
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
			} else if (e.getItem().getType() == Material.POTATO_ITEM) {
				useFlashBang(p);
			} else if (e.getItem().getType() == Material.ARROW) {
				useArrow(p);
			} else if (e.getItem().getType() == Material.SNOW_BALL) {
				useSnow(p);
			} else if (e.getItem().getType() == Material.FIREBALL) {
				useFireball(p);
			} else if (e.getItem().getType() == Material.MAGMA_CREAM) {
				useBoost(p);
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
		
		/*@EventHandler
		public void onPlayerRespawn(PlayerRespawnEvent e) {
			Player p = e.getPlayer();
			if(!ingamePlayer.contains(p.getName())) return;
			
			if(ingame) {
				e.setRespawnLocation(getRandomLoc());	
				Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
					public void run() {
						p.getInventory().addItem(item_sword);
					}
				}, 10l);
			}
		}*/
	}
	
	///////////////////// 선빵게임에 참가한 플레이어들 클래스
	private class FhtPlayer {
		
		public FhtPlayer(Player p, String job) {

		}

	}
	
}
