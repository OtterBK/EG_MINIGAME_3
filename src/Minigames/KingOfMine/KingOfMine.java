package Minigames.KingOfMine;

import java.io.File;
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
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
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
import com.connorlinfoot.titleapi.TitleAPI;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;

import EGServer.EGServer;
import EGServer.ServerManager.EGEventHandler;
import EGServer.ServerManager.EGScheduler;
import Minigames.Minigame;
import Utility.MyUtility;

public class KingOfMine extends Minigame{
	// 이벤트용
	public EventHandlerKOM event;
	
	public String ms = "§7[§6전체§7] ";

	///////////// private
	// 게임 플레이어 리스트
	//private HashMap<String, KOMPlayer> playerMap = new HashMap<String, KOMPlayer>();
	private String cmdMain;
	//////// 게임 관련
	public int gameStep = 0;
	public EGScheduler mainSch;
	
	public int score = 0;
	private ItemStack item_stonePickAxe;
	private ItemStack item_goldPickAxe;
	private ItemStack item_ironPickAxe;
	private ItemStack item_diaPickAxe;
	
	private List<Location> oreLocationList = new ArrayList<Location>(9);
	
	private int maxChance = 3;
	public int nowChance = 3;
	
	private int spawnPerTick = 10;
	private int levelChgLine = 10;
	
	public static HashMap<String, List<String>> mapRankerName = new HashMap<String, List<String>>();
	public static HashMap<String, List<Double>> mapRankerScore = new HashMap<String, List<Double>>();
	
	private Hologram holoScore;
	private TextLine scoreLine;
	private TextLine chanceLine;
	
	////// 각종 인벤토리
	public Inventory inven_gameHelper;
	
	//폴더 경로
	public String locPath;
	public String rootLocPath;
	
	//////// 사이드바
	private Sidebar komSidebar;
	private List<SidebarString> textList = new ArrayList<SidebarString>(5);
	
	public KingOfMine(EGServer server, String gameName, String displayGameName, String cmdMain) {

		//////////////////// 필수 설정값
		super(server);
		
		this.cmdMain = cmdMain;
		
		ms = "§7[ §e! §7] §f: §c광물의 왕 §f>> "; // 기본 메세지
		this.gameName = gameName;
		this.disPlayGameName = displayGameName;
		minPlayer = 1;
		maxPlayer = 1;
		startCountTime = 3;
		//복구용 맵 생성
		locPath = server.getDataFolder().getPath() + "/" + gameName + "/Location";
		rootLocPath = server.getDataFolder().getPath() + "/KingOfMine/Rankdata";
		doneSetting = loadGameData(); // 게임 시작 위치 등등 로드	
		
		/////////////////////// 자동 설정(아이템등등)
		dirSetting(gameName);
		////////////////
		
		//map.loadData(locPath);
		
		mainSch = new EGScheduler(this);
		
		SidebarString tmpLine = new SidebarString("");
		komSidebar = new Sidebar("§f[ §6게임 현황 §f]", server, 600, tmpLine);
		
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
		loreList.add("§7최대한 높게 점수를 쌓으세요!");
		loreList.add("§7점수 경쟁 게임입니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_gameHelper.setItem(10, item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("§7- §c진행방식 §7-");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§7시작시 모든 종류의 곡괭이를 지급받습니다.");
		loreList.add("§7전방에 보이는 3X3공간에 무작위로 광물이 생성되며");
		loreList.add("§7이 광물이 공간을 전부 채우면 게임이 끝납니다.");
		loreList.add("§7돌은 돌곡괭이로, 철은 철곡괭이로, 금은 금곡괭이로");
		loreList.add("§7다이아는 다이아곡괭이로 캐야합니다.");
		loreList.add("§7만약 잘못된 곡괭이로 광물을 캘 시");
		loreList.add("§7레벨을 잃습니다.");
		loreList.add("§7레벨이 0이되면 게임이 종료됩니다.");
		loreList.add("§c광물은 우클릭으로도 캘 수 있습니다.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_gameHelper.setItem(11, item);
		
		event = new EventHandlerKOM(server, this);
		
		item_stonePickAxe = new ItemStack(Material.STONE_PICKAXE, 1);
		item_stonePickAxe.addUnsafeEnchantment(Enchantment.DIG_SPEED, 99);
		
		item_goldPickAxe = new ItemStack(Material.GOLD_PICKAXE, 1);
		item_goldPickAxe.addUnsafeEnchantment(Enchantment.DIG_SPEED, 99);
		
		item_ironPickAxe = new ItemStack(Material.IRON_PICKAXE, 1);
		item_ironPickAxe.addUnsafeEnchantment(Enchantment.DIG_SPEED, 99);
		
		item_diaPickAxe = new ItemStack(Material.DIAMOND_PICKAXE, 1);
		item_diaPickAxe.addUnsafeEnchantment(Enchantment.DIG_SPEED, 99);
		
		// 이 플러그인에 이벤트 적용
		server.getServer().getPluginManager().registerEvents(event, server);
	}

	/////////////// Minigame에서 override 해야하는 부분
	@Override
	public void gameHelpMsg(Player p) {

	}
	
	@Override
	public void startCount() {
		if(ingame) return;
		startSch.cancelTask(true);
		startSch.schTime = startCountTime;
		//server.egCM.broadCast(server.ms_alert + ChatColor.RED+ChatColor.BOLD + disPlayGameName + ChatColor.GRAY + " 게임이 " + ChatColor.GRAY + "곧 시작됩니다.");
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
					lobbyStart = true;
					startSch.cancelTask(true);
					playerSet();
					startGame();
				}
			}
		}, 0L, 20L);
	}

	@Override
	public void startGame() {
		//server.egCM.broadCast(server.ms_alert + ChatColor.RED+ChatColor
		//		.BOLD+ "스플리프 " + ChatColor.GRAY + "가 시작 되었습니다!"); //광물의 왕은 시작 알림 X
		initGame();
		ingame = true;
		gameStep = 1;
		/////////////// 오프닝
		if(ingamePlayer.size() < 1) {
			for(String pName : ingamePlayer) {
				Player t = Bukkit.getPlayer(pName);
				server.spawn(t);
				t.sendMessage(ms+"최소 시작인원이 부족하여 시작이 취소됐습니다.");
			}
			endGame(false);
			return;
		}
		sendSound(Sound.BLOCK_NOTE_BELL, 1.0f, 1.0f);
		gameStep = 2;
		countDown(5, "게임 시작까지");
		for (String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p)){
				p.setGameMode(GameMode.SURVIVAL);
				// 풀피로 만듬
				MyUtility.healUp(p);
				MyUtility.allClear(p);         
				//p.teleport(loc_Join);
				TitleAPI.sendFullTitle(p, 10, 80, 30, ChatColor.RED+"게임 시작", ChatColor.RED+""+disPlayGameName);	
				komSidebar.showTo(p);
				p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 72000, 10));
			}
		}
		
		///////////////// 진짜 시작
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				gameStep = 3;
				for(String pName : ingamePlayer) {
					Player p = Bukkit.getPlayer(pName);
					if(existPlayer(p)) {
						p.setLevel(nowChance);
						p.setExp(nowChance/maxChance);
						giveBaseItem();
					}
				}
				mainScheduler();
				updateSidebar();
				sendSound(Sound.BLOCK_SLIME_BREAK, 1.0f, 1.3f);
			}
		}, 110l);
	}
	
	//////////////////
	
	public void updateSidebar() {
		if(!ingame) return;
		textList.clear();
		
		SidebarString blank = new SidebarString("");
		textList.add(blank);
		SidebarString line = new SidebarString("§e현재 점수§f: §a"+score+"점");
		textList.add(line);
		textList.add(blank);
		line = new SidebarString("§e남은 기회§f: §a"+nowChance+"회");
		textList.add(line);
		textList.add(blank);
		
		komSidebar.setEntries(textList);
		komSidebar.update();
		
		scoreLine.setText("§f§l[ §6§l현재 점수 : §e§l"+score+"점§f§l]");
		chanceLine.setText("§f§l[ §6§l남은 기회 : §e§l"+nowChance+"회§f§l]");
	}
	
	public void mainScheduler() {
		schList.add(mainSch);
		
		holoScore = HologramsAPI.createHologram(server, loc_Join.clone().add(0,1,0));
		scoreLine = holoScore.appendTextLine("§f§l[ §6§l현재 점수 : §e§l"+score+"점§f§l]");
		chanceLine = holoScore.appendTextLine("§f§l[ §6§l남은 기회 : §e§l"+nowChance+"회§f§l]");
		
		mainSch.schTime = 0; //틱 시간
		mainSch.schTime2 = 0; //생성 횟수
		mainSch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				if(gameStep != 3) {
					mainSch.cancelTask(true);
				}else {
					if(mainSch.schTime >= spawnPerTick) {
						mainSch.schTime = 0;
						//생성
						List<Integer> leftlocList = new ArrayList<Integer>(9); 
						for(int i = 0; i < 9; i++) {
							leftlocList.add(i);
						}
						boolean isFail = true;
						for(int i = 0; i < 9; i++) {
							//Bukkit.broadcastMessage(""+leftlocList.size());
							int rn = MyUtility.getRandom(0, leftlocList.size()-1);
							int index = leftlocList.get(rn);
							Location l = oreLocationList.get(index);
							Block b = l.getBlock();
							//Bukkit.broadcastMessage(""+b.getType());
							if(b.getType() == Material.AIR) {
								int blockCode = MyUtility.getRandom(0, 3);
								if(blockCode == 0) {
									b.setType(Material.STONE);
								}else if(blockCode == 1) {
									b.setType(Material.GOLD_ORE);
								}else if(blockCode == 2) {
									b.setType(Material.IRON_ORE);
								}else if(blockCode == 3) {
									b.setType(Material.DIAMOND_ORE);
								}
								isFail = false;
								b.getLocation().getWorld().playSound(b.getLocation(), Sound.BLOCK_NOTE_HAT, 1.5f, 1.0f);
								mainSch.schTime2++;
								if(mainSch.schTime2 >= levelChgLine) {
									mainSch.schTime2 = 0;
									if(spawnPerTick == 5) break;
									spawnPerTick -= 1;
									levelChgLine += 1;
									if(spawnPerTick < 15) {
										levelChgLine += 2;
									}
									if(spawnPerTick == 10) {
										levelChgLine += 10;
									}
									if(spawnPerTick == 9) {
										levelChgLine += 30;
									}
									if(spawnPerTick == 8) {
										levelChgLine += 50;
									}
									if(spawnPerTick == 7) {
										levelChgLine += 70;
									}
									if(spawnPerTick == 6) {
										levelChgLine += 100;
									}
									if(spawnPerTick == 5) {
										levelChgLine += 120;
									}
									if(spawnPerTick < 5) {
										spawnPerTick = 5;
									}
									//Bukkit.broadcastMessage(spawnPerTick+"");
								}
								break;
							}else {
								leftlocList.remove(rn);
								continue;
							}
						}	
						if(isFail == true) {
							mainSch.cancelTask(true);
							gameStep = 4;
							finish();
						}
					}else {
						mainSch.schTime++;
					}
				}
			}
		}, 0l, 1l);
	}

	public void initGame() {
		lobbyStart = false;
		ending = false;
		gameStep = 0;
		nowChance = 3;
		spawnPerTick = 20;
		levelChgLine = 10;
		score = 0;
		
		for(int i = 0; i < oreLocationList.size(); i++) {
			oreLocationList.get(i).getBlock().setType(Material.AIR);
		}
		
		//스케쥴
		schList.clear();
	}

	public boolean loadGameData() {
		oreLocationList.clear();
		boolean ret = true;
		loc_Join = loadLocation(gameName, "JoinPos");
		for(int i = 1; i <= 9; i++) {
			Location l = loadLocation(gameName, "oreBlock"+i);
			if(l != null) {
				oreLocationList.add(l);
			}
		}
		
		if (loc_Join == null) {
			server.egPM.printLog(ms + "[" + disPlayGameName + "] 게임 시작 대기 지점이 설정되지 않았습니다.");
			ret = false;
		}else {
			//loc_spectate = loc_Join.clone().add(0,10,0);
		}
		
		if(oreLocationList.size() < 9) {
			server.egPM.printLog(ms + "[" + disPlayGameName + "] 광물 지점이 설정되지 않았습니다.");
			ret = false;
		}
		
		if(ret == true) {
			server.egPM.printLog("[" + disPlayGameName + "] 설정 이상 없음");
			doneSetting = ret;
		}
		
		loadRank();
		
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
							}else if (cmd[3].equalsIgnoreCase("oreBlock")) {
								saveLocation(gameName, "oreBlock"+cmd[4], p.getTargetBlock(null, 3).getRelative(0,-1,0).getLocation());
								p.sendMessage("[" + disPlayGameName + "] " + p.getTargetBlock(null, 3).getLocation()+" 설정완료");
							}else {
								p.sendMessage("[" + disPlayGameName + "] " + cmd[3]+"은 올바르지 않은 인수");
							}
							loadGameData();
						}  else {
							p.sendMessage(ms+"인수를 입력해주세요.");
						}
					}  else if(cmd[2].equalsIgnoreCase("addrank")){
						String mapName = cmd[3];
						try {
							File file = new File(rootLocPath, "location.yml");
							FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
							ConfigurationSection section = fileConfig.getConfigurationSection(mapName);
							if(section == null) section = fileConfig.createSection(mapName);
							List<String> mapNameList = new ArrayList<String>(mapRankerName.keySet());
							if(!mapNameList.contains(mapName)) mapNameList.add(mapName);
							fileConfig.set("mapList", mapNameList);
							p.sendMessage(ms+mapName+"랭크 데이터 생성");
							
							List<String> rankNames = new ArrayList<String>();
							List<Double> rankScore = new ArrayList<Double>();
							
							ConfigurationSection recordSection = section.getConfigurationSection("record");
							if(recordSection == null) recordSection = section.createSection("record");
							
							ConfigurationSection rankSection = section.getConfigurationSection("ranking");
							if(rankSection == null) rankSection = section.createSection("ranking");
							for(int i = 1; i < 11; i++){
								rankSection.set("rankerName_"+i, "OreKing");
								rankSection.set("rankerScore_"+i, 0);
							}
							mapRankerName.put(mapName, rankNames);
							mapRankerScore.put(mapName, rankScore);
							
							fileConfig.save(file);
							
							for(int i = 1; i <= 10; i++) {
								rankSection.set("rankerName_"+1, p.getName());
								rankSection.set("rankerScore_"+1, score);
							}						
						} catch (Exception e) {
							e.printStackTrace();
							server.egPM.printLog("[" + disPlayGameName + "]" + mapName+" 설정에서 오류 발생");
						}
						loadGameData();
					}else {
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" set loc join - 게임 시작 대기 지점 설정");
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" set loc oreblock1~9 - 광물1~9");
					}
				}else {
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
				saveNewRecord(p, "KingOfMineEasy", Integer.valueOf(cmd[2]));
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
			if(scoreLine != null) updateSidebar();
			if (ingame) {
				if(giveGold) {
					if(announce) {
						sendMessage(ms + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " 님이 탈락하셨습니다.");
						sendTitle("", ChatColor.YELLOW+p.getName()+"님 탈락", 60);
						sendSound(Sound.BLOCK_NOTE_CHIME,1.5f, 2.0f);
					}
					//p.sendMessage(ms+"게임 플레이 보상으로 5골드를 받으셨습니다.");
				} else {
					if(announce) {
						sendMessage(ms + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " 님이 퇴장하셨습니다.");
						sendSound(Sound.BLOCK_NOTE_GUITAR,1.5f, 2.0f);
					}
				}
				if(komSidebar != null) komSidebar.hideFrom(p);
				if(ingamePlayer.size() <= 1) {
					finish();
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
				p.getInventory().setItem(0, item_stonePickAxe);
				p.getInventory().setItem(1, item_goldPickAxe);
				p.getInventory().setItem(2, item_ironPickAxe);		
				p.getInventory().setItem(3, item_diaPickAxe);
				
				p.getInventory().setItem(8, helpItem);
			}
		}
	}
	
	public void performence() {
		if(ingamePlayer.size() <= 0) return;
		EGScheduler sch = new EGScheduler(this);
		sch.schTime = 1;
		Location l = loc_Join;
		sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				if(sch.schTime> 0) {
					sch.schTime--;
					Firework fw = (Firework) l.getWorld().spawnEntity(l, EntityType.FIREWORK);
					FireworkMeta fwm = fw.getFireworkMeta();

					fwm.setPower(1);
					fwm.addEffect(FireworkEffect.builder().withColor(MyUtility.getRandomColor()).flicker(true).build());

					fw.setFireworkMeta(fwm);
					fw.detonate();
					
			        Firework fw2 = (Firework) l.getWorld().spawnEntity(l, EntityType.FIREWORK);
			        fw2.setFireworkMeta(fwm);
				}else {
					sch.cancelTask(true);
				}
			}
		}, 0l, 20l);
	}
	
	public void missDig(Player p) {
		nowChance--;
		score--;
		p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 0.5f);
		TitleAPI.sendFullTitle(p, 0, 60, 0, "", "§c§l잘못된 곡괭이입니다! 기회 -1");
		if(nowChance < 0) {
			updateSidebar();
			finish();
		}else {
			p.setLevel(nowChance);
			updateSidebar();
			p.setExp((float)nowChance/maxChance);
		}
	}

	private void finish() {
		if(ending) return;
		ending = true;
		gameStep = 4;
		mainSch.cancelTask(true);
		holoScore.delete();
		if(ingamePlayer.size() >= 1) {
			String winner = ingamePlayer.get(0);
			Player winnerP = Bukkit.getPlayer(winner);
			if(existPlayer(winnerP)) {
				MyUtility.allClear(winnerP);
				winnerP.setLevel(0);
				winnerP.setExp(0);
				if(!saveNewRecord(winnerP, "KingOfMineEasy", score)) {
					winnerP.sendMessage(ms+"데이터 저장중 오류발생...ec - 01");
				}
			}
			sendTitle("§6§l"+score+"점 달성!", ChatColor.GRAY + "게임이 종료됐습니다.", 70);
			sendSound(Sound.BLOCK_NOTE_BELL, 1.0f, 0.5f);

			performence();			
			
			try {
				Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
					public void run() {
						divideSpawn();
						endGame(false);
						//server.egCM.broadCast(
						//	server.ms_alert + ChatColor.GRAY  + "§c"+winner+"§7님의 §a승리§7로 §c스플리프§7가 종료 되었습니다.");
					}
				}, 100L);
			} catch(Exception e) {
					endGame(true);
			}
		} else {
			try {
				Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
					public void run() {
						divideSpawn();
						endGame(false);
						//server.egCM.broadCast(
						//	server.ms_alert + ChatColor.GRAY  + "무승부로 §c스플리프§7가 종료 되었습니다.");
					}
				}, 200L);
			} catch(Exception e) {
					endGame(true);
			}
		}
	}
	
	public void showRanking(Player p, String mapName) {
		List<String> rankNames = mapRankerName.get(mapName);
		List<Double> rankTimes = mapRankerScore.get(mapName);
		
		if(rankNames.size() >= 1) {
			p.sendMessage("§c=========== 광물의 왕 ===========");
			p.sendMessage("§c=========== 랭킹 ===========");
			for(int i = 0; i < rankNames.size(); i++) {
				String pName = rankNames.get(i);
				p.sendMessage("§e"+(i+1)+". "+pName+" §7: §a"+rankTimes.get(i)+"§e점");
			}
			p.sendMessage("§c=========== 당신의 최고 기록 ===========");
			File file = new File(rootLocPath, "location.yml");	
			FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
			ConfigurationSection mapSection = fileConfig.getConfigurationSection(mapName);
			ConfigurationSection section = mapSection.getConfigurationSection("record");
			if(section == null) section = mapSection.createSection("record");
			double baseRecord = section.getDouble(p.getName()+"_score");
			int clearCnt = section.getInt(p.getName()+"_clearCnt");
			if(baseRecord == 0) {
				p.sendMessage("§e아직 이 게임에 대한 기록을 가지고 있지 않습니다.");
			} else {
				p.sendMessage("§e최고 점수 : §a"+baseRecord+" §e점");
				p.sendMessage("§e플레이 횟수 : §a"+clearCnt+ " §e번");
			}
		}else {
			p.sendMessage(ms+"해당 맵에 대한 레이싱 기록이 아직 없습니다.");
		}
	}

	public void endGame(boolean force) {
		if(force) {
			server.egCM.broadCast(server.ms_alert + disPlayGameName+" 게임이 강제 종료 되었습니다.");
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
		
		holoScore.delete();
		
		initGame();	
	}
	
	public boolean saveNewRecord(Player p, String mapName, double score) {
		try {
			File file = new File(rootLocPath, "location.yml");	
			FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
			ConfigurationSection mapSection = fileConfig.getConfigurationSection(mapName);
			ConfigurationSection section = mapSection.getConfigurationSection("record");
			if(section == null) section = mapSection.createSection("record");
			double baseRecord = section.getDouble(p.getName()+"_score");
			int clearCnt = section.getInt(p.getName()+"_clearCnt");
			if(baseRecord == 0) {
				p.sendMessage(ms+"새로운 기록이 저장됐습니다. \n+"+ms+"§f기록 : §a§l"+score+"§f점");
				section.set(p.getName()+"_score", score);
				section.set(p.getName()+"_clearCnt", clearCnt+1);
			}else {
				if(baseRecord < score) {
					p.sendMessage(ms+"기존 기록인 §b"+baseRecord+" §f점 보다 더 높은 기록을 달성하였습니다.");
					p.sendMessage(ms+"새로운 기록인 §b"+score+" §f점을 저장합니다.");
					section.set(p.getName()+"_score", score);
					section.set(p.getName()+"_clearCnt", clearCnt+1);
				}else {
					p.sendMessage(ms+"기존 기록인 §b"+baseRecord+" §f점이 더 높아 새로운 기록을 저장하지 않습니다.");
					section.set(p.getName()+"_clearCnt", clearCnt+1);
				}
			}
			p.sendMessage(ms+"이 게임을 §b"+(clearCnt+1)+" §f번 플레이하였습니다.");
			List<String> rankNames = mapRankerName.get(mapName);
			List<Double> rankScore = mapRankerScore.get(mapName);
			
			ConfigurationSection rankSection = mapSection.getConfigurationSection("ranking");
			if(rankSection == null) rankSection = mapSection.createSection("ranking");
			if(rankScore.size() <= 0) {
				rankSection.set("rankerName_"+1, p.getName());
				rankSection.set("rankerScore_"+1, score);
				for(int k = 2; k < 11; k++) {
					rankSection.set("rankerName_"+k, "OreKing");
					rankSection.set("rankerScore_"+k, 3600);
				}	
			} else {
				for(int i = 0; i < rankScore.size(); i++){
					if(rankScore.get(i) < score){
						if(rankNames.contains(p.getName())){
							if(rankScore.get(rankNames.indexOf(p.getName())) >= score) break;
							else{
								for(int j = rankNames.indexOf(p.getName()); j > i; j--){
									rankNames.set(j, rankNames.get(j-1));
									rankScore.set(j, rankScore.get(j-1));
								} 
								rankNames.set(i, p.getName());
								rankScore.set(i, score);
							}
						}else{
							for(int j = 9; j > i; j--){
								rankNames.set(j, rankNames.get(j-1));
								rankScore.set(j, rankScore.get(j-1));
							} 
							rankNames.set(i, p.getName());
							rankScore.set(i, score);
						}
						for(int k = 0; k < 10; k++){
							rankSection.set("rankerName_"+(k+1), rankNames.get(k));
							rankSection.set("rankerScore_"+(k+1), rankScore.get(k));
						}
						break;
					}
				}
			}	
			fileConfig.save(file);
			loadRank();
			
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}	
	}
	
	public void loadRank() {
		
		mapRankerName.clear();
		mapRankerScore.clear();
		
		File file = new File(rootLocPath, "location.yml");	
		FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
		List<String> mapNameList = fileConfig.getStringList("mapList");
		for(String mapName : mapNameList) {
			ConfigurationSection section = fileConfig.getConfigurationSection(mapName);
			List<String> rankNames = new ArrayList<String>();
			List<Double> rankScore = new ArrayList<Double>();
			ConfigurationSection rankSection = section.getConfigurationSection("ranking");
			if(rankSection == null) rankSection = section.createSection("ranking");
			for(int i = 1; i < 11; i++){
				String rank_name = rankSection.getString(("rankerName_"+i));
				double rank_time = rankSection.getDouble(("rankerScore_"+i));
				if(rank_name == null) {
					break;
				}
				rankNames.add(rank_name);
				rankScore.add(rank_time);
			}
			mapRankerName.put(mapName, rankNames);
			mapRankerScore.put(mapName, rankScore);
			
			server.egPM.printLog("[" + disPlayGameName + "] 랭킹 데이터 로드됨");
		}
		
	}

	//////////////// 이벤트
	public class EventHandlerKOM extends EGEventHandler {

		private KingOfMine game;

		public EventHandlerKOM(EGServer server, KingOfMine game) {
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
		public void onEntityDamage(EntityDamageEvent e) {
			if (e.getEntityType() == EntityType.PLAYER) {
				Player p = (Player) e.getEntity();
				if (ingamePlayer.contains(p.getName())) {
					e.setCancelled(true); //겜중엔 뎀 X
				}
			}
		}

		@EventHandler
		public void onClickInventory(InventoryClickEvent e) {
			if (!(e.getWhoClicked() instanceof Player))
				return;
			Player p = (Player) e.getWhoClicked();
			if (e.getInventory().getTitle().equalsIgnoreCase("도우미")) {				
				e.setCancelled(true);
			}
			if (!ingamePlayer.contains(p.getName()))
				return;
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
			} else if(e.getClickedBlock() != null) {
				if(gameStep != 3) e.setCancelled(true);
				else {
					boolean cancel = true;
					ItemStack item = e.getItem();
					Block b = e.getClickedBlock();
					if(b.getType() == Material.STONE) {
						cancel = false;
						if(item.getType() != Material.STONE_PICKAXE) {
							missDig(p);
						}
					} else if(b.getType() == Material.GOLD_ORE) {
						cancel = false;
						if(item.getType() != Material.GOLD_PICKAXE) {
							missDig(p);
						}
					} else if(b.getType() == Material.IRON_ORE) {
						cancel = false;
						if(item.getType() != Material.IRON_PICKAXE) {
							missDig(p);
						}
					} else if(b.getType() == Material.DIAMOND_ORE) {
						cancel = false;
						if(item.getType() != Material.DIAMOND_PICKAXE) {
							missDig(p);
						}
					}
					b.getWorld().playSound(b.getLocation(), Sound.BLOCK_STONE_BREAK, 1.0f, 1.0f);
					e.setCancelled(true);
					if(!cancel) { //잘못캔거아니면
						score += 1;
						updateSidebar();
						b.setType(Material.AIR);
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
		public void onBlockBreak(BlockBreakEvent e) {
			Player p = e.getPlayer();
			if(ingamePlayer.contains(p.getName())) {
				if(gameStep != 3) e.setCancelled(true);
				else {
					boolean cancel = true;
					ItemStack item = p.getInventory().getItemInMainHand();
					Block b = e.getBlock();
					if(b.getType() == Material.STONE) {
						cancel = false;
						if(item.getType() != Material.STONE_PICKAXE) {
							missDig(p);
						}
					} else if(b.getType() == Material.GOLD_ORE) {
						cancel = false;
						if(item.getType() != Material.GOLD_PICKAXE) {
							missDig(p);
						}
					} else if(b.getType() == Material.IRON_ORE) {
						cancel = false;
						if(item.getType() != Material.IRON_PICKAXE) {
							missDig(p);
						}
					} else if(b.getType() == Material.DIAMOND_ORE) {
						cancel = false;
						if(item.getType() != Material.DIAMOND_PICKAXE) {
							missDig(p);
						}
					}
					e.setExpToDrop(0);
					e.setCancelled(true);
					if(!cancel) { //잘못캔거아니면
						score += 1;
						updateSidebar();
						b.setType(Material.AIR);
					}
				}
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
	
	///////////////////// 광물의 왕에 참가한 플레이어들 클래스
	private class KomPlayer {
		
		public KomPlayer(Player p, String job) {

		}

	}
}
	
