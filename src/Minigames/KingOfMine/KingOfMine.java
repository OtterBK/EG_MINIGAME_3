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
	// �̺�Ʈ��
	public EventHandlerKOM event;
	
	public String ms = "��7[��6��ü��7] ";

	///////////// private
	// ���� �÷��̾� ����Ʈ
	//private HashMap<String, KOMPlayer> playerMap = new HashMap<String, KOMPlayer>();
	private String cmdMain;
	//////// ���� ����
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
	
	////// ���� �κ��丮
	public Inventory inven_gameHelper;
	
	//���� ���
	public String locPath;
	public String rootLocPath;
	
	//////// ���̵��
	private Sidebar komSidebar;
	private List<SidebarString> textList = new ArrayList<SidebarString>(5);
	
	public KingOfMine(EGServer server, String gameName, String displayGameName, String cmdMain) {

		//////////////////// �ʼ� ������
		super(server);
		
		this.cmdMain = cmdMain;
		
		ms = "��7[ ��e! ��7] ��f: ��c������ �� ��f>> "; // �⺻ �޼���
		this.gameName = gameName;
		this.disPlayGameName = displayGameName;
		minPlayer = 1;
		maxPlayer = 1;
		startCountTime = 3;
		//������ �� ����
		locPath = server.getDataFolder().getPath() + "/" + gameName + "/Location";
		rootLocPath = server.getDataFolder().getPath() + "/KingOfMine/Rankdata";
		doneSetting = loadGameData(); // ���� ���� ��ġ ��� �ε�	
		
		/////////////////////// �ڵ� ����(�����۵��)
		dirSetting(gameName);
		////////////////
		
		//map.loadData(locPath);
		
		mainSch = new EGScheduler(this);
		
		SidebarString tmpLine = new SidebarString("");
		komSidebar = new Sidebar("��f[ ��6���� ��Ȳ ��f]", server, 600, tmpLine);
		
		/////////////////// ���� �����
		inven_gameHelper = Bukkit.createInventory(null, 27, ""+ChatColor.BLACK+ChatColor.BOLD+"�����");

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
		loreList.add("��7�ִ��� ���� ������ ��������!");
		loreList.add("��7���� ���� �����Դϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_gameHelper.setItem(10, item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��7- ��c������ ��7-");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7���۽� ��� ������ ��̸� ���޹޽��ϴ�.");
		loreList.add("��7���濡 ���̴� 3X3������ �������� ������ �����Ǹ�");
		loreList.add("��7�� ������ ������ ���� ä��� ������ �����ϴ�.");
		loreList.add("��7���� ����̷�, ö�� ö��̷�, ���� �ݰ�̷�");
		loreList.add("��7���̾ƴ� ���̾ư�̷� ĳ���մϴ�.");
		loreList.add("��7���� �߸��� ��̷� ������ Ķ ��");
		loreList.add("��7������ �ҽ��ϴ�.");
		loreList.add("��7������ 0�̵Ǹ� ������ ����˴ϴ�.");
		loreList.add("��c������ ��Ŭ�����ε� Ķ �� �ֽ��ϴ�.");
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
		
		// �� �÷����ο� �̺�Ʈ ����
		server.getServer().getPluginManager().registerEvents(event, server);
	}

	/////////////// Minigame���� override �ؾ��ϴ� �κ�
	@Override
	public void gameHelpMsg(Player p) {

	}
	
	@Override
	public void startCount() {
		if(ingame) return;
		startSch.cancelTask(true);
		startSch.schTime = startCountTime;
		//server.egCM.broadCast(server.ms_alert + ChatColor.RED+ChatColor.BOLD + disPlayGameName + ChatColor.GRAY + " ������ " + ChatColor.GRAY + "�� ���۵˴ϴ�.");
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
		//		.BOLD+ "���ø��� " + ChatColor.GRAY + "�� ���� �Ǿ����ϴ�!"); //������ ���� ���� �˸� X
		initGame();
		ingame = true;
		gameStep = 1;
		/////////////// ������
		if(ingamePlayer.size() < 1) {
			for(String pName : ingamePlayer) {
				Player t = Bukkit.getPlayer(pName);
				server.spawn(t);
				t.sendMessage(ms+"�ּ� �����ο��� �����Ͽ� ������ ��ҵƽ��ϴ�.");
			}
			endGame(false);
			return;
		}
		sendSound(Sound.BLOCK_NOTE_BELL, 1.0f, 1.0f);
		gameStep = 2;
		countDown(5, "���� ���۱���");
		for (String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p)){
				p.setGameMode(GameMode.SURVIVAL);
				// Ǯ�Ƿ� ����
				MyUtility.healUp(p);
				MyUtility.allClear(p);         
				//p.teleport(loc_Join);
				TitleAPI.sendFullTitle(p, 10, 80, 30, ChatColor.RED+"���� ����", ChatColor.RED+""+disPlayGameName);	
				komSidebar.showTo(p);
				p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 72000, 10));
			}
		}
		
		///////////////// ��¥ ����
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
		SidebarString line = new SidebarString("��e���� ������f: ��a"+score+"��");
		textList.add(line);
		textList.add(blank);
		line = new SidebarString("��e���� ��ȸ��f: ��a"+nowChance+"ȸ");
		textList.add(line);
		textList.add(blank);
		
		komSidebar.setEntries(textList);
		komSidebar.update();
		
		scoreLine.setText("��f��l[ ��6��l���� ���� : ��e��l"+score+"����f��l]");
		chanceLine.setText("��f��l[ ��6��l���� ��ȸ : ��e��l"+nowChance+"ȸ��f��l]");
	}
	
	public void mainScheduler() {
		schList.add(mainSch);
		
		holoScore = HologramsAPI.createHologram(server, loc_Join.clone().add(0,1,0));
		scoreLine = holoScore.appendTextLine("��f��l[ ��6��l���� ���� : ��e��l"+score+"����f��l]");
		chanceLine = holoScore.appendTextLine("��f��l[ ��6��l���� ��ȸ : ��e��l"+nowChance+"ȸ��f��l]");
		
		mainSch.schTime = 0; //ƽ �ð�
		mainSch.schTime2 = 0; //���� Ƚ��
		mainSch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				if(gameStep != 3) {
					mainSch.cancelTask(true);
				}else {
					if(mainSch.schTime >= spawnPerTick) {
						mainSch.schTime = 0;
						//����
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
		
		//������
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
			server.egPM.printLog(ms + "[" + disPlayGameName + "] ���� ���� ��� ������ �������� �ʾҽ��ϴ�.");
			ret = false;
		}else {
			//loc_spectate = loc_Join.clone().add(0,10,0);
		}
		
		if(oreLocationList.size() < 9) {
			server.egPM.printLog(ms + "[" + disPlayGameName + "] ���� ������ �������� �ʾҽ��ϴ�.");
			ret = false;
		}
		
		if(ret == true) {
			server.egPM.printLog("[" + disPlayGameName + "] ���� �̻� ����");
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
								p.sendMessage("[" + disPlayGameName + "] " + "���� ���� ��� ������ �����Ǿ����ϴ�.");
							}else if (cmd[3].equalsIgnoreCase("oreBlock")) {
								saveLocation(gameName, "oreBlock"+cmd[4], p.getTargetBlock(null, 3).getRelative(0,-1,0).getLocation());
								p.sendMessage("[" + disPlayGameName + "] " + p.getTargetBlock(null, 3).getLocation()+" �����Ϸ�");
							}else {
								p.sendMessage("[" + disPlayGameName + "] " + cmd[3]+"�� �ùٸ��� ���� �μ�");
							}
							loadGameData();
						}  else {
							p.sendMessage(ms+"�μ��� �Է����ּ���.");
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
							p.sendMessage(ms+mapName+"��ũ ������ ����");
							
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
							server.egPM.printLog("[" + disPlayGameName + "]" + mapName+" �������� ���� �߻�");
						}
						loadGameData();
					}else {
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" set loc join - ���� ���� ��� ���� ����");
						p.sendMessage("[" + disPlayGameName + "] " + cmdMain+" set loc oreblock1~9 - ����1~9");
					}
				}else {
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
				saveNewRecord(p, "KingOfMineEasy", Integer.valueOf(cmd[2]));
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
			if(scoreLine != null) updateSidebar();
			if (ingame) {
				if(giveGold) {
					if(announce) {
						sendMessage(ms + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " ���� Ż���ϼ̽��ϴ�.");
						sendTitle("", ChatColor.YELLOW+p.getName()+"�� Ż��", 60);
						sendSound(Sound.BLOCK_NOTE_CHIME,1.5f, 2.0f);
					}
					//p.sendMessage(ms+"���� �÷��� �������� 5��带 �����̽��ϴ�.");
				} else {
					if(announce) {
						sendMessage(ms + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " ���� �����ϼ̽��ϴ�.");
						sendSound(Sound.BLOCK_NOTE_GUITAR,1.5f, 2.0f);
					}
				}
				if(komSidebar != null) komSidebar.hideFrom(p);
				if(ingamePlayer.size() <= 1) {
					finish();
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
		TitleAPI.sendFullTitle(p, 0, 60, 0, "", "��c��l�߸��� ����Դϴ�! ��ȸ -1");
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
					winnerP.sendMessage(ms+"������ ������ �����߻�...ec - 01");
				}
			}
			sendTitle("��6��l"+score+"�� �޼�!", ChatColor.GRAY + "������ ����ƽ��ϴ�.", 70);
			sendSound(Sound.BLOCK_NOTE_BELL, 1.0f, 0.5f);

			performence();			
			
			try {
				Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
					public void run() {
						divideSpawn();
						endGame(false);
						//server.egCM.broadCast(
						//	server.ms_alert + ChatColor.GRAY  + "��c"+winner+"��7���� ��a�¸���7�� ��c���ø�����7�� ���� �Ǿ����ϴ�.");
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
						//	server.ms_alert + ChatColor.GRAY  + "���ºη� ��c���ø�����7�� ���� �Ǿ����ϴ�.");
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
			p.sendMessage("��c=========== ������ �� ===========");
			p.sendMessage("��c=========== ��ŷ ===========");
			for(int i = 0; i < rankNames.size(); i++) {
				String pName = rankNames.get(i);
				p.sendMessage("��e"+(i+1)+". "+pName+" ��7: ��a"+rankTimes.get(i)+"��e��");
			}
			p.sendMessage("��c=========== ����� �ְ� ��� ===========");
			File file = new File(rootLocPath, "location.yml");	
			FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
			ConfigurationSection mapSection = fileConfig.getConfigurationSection(mapName);
			ConfigurationSection section = mapSection.getConfigurationSection("record");
			if(section == null) section = mapSection.createSection("record");
			double baseRecord = section.getDouble(p.getName()+"_score");
			int clearCnt = section.getInt(p.getName()+"_clearCnt");
			if(baseRecord == 0) {
				p.sendMessage("��e���� �� ���ӿ� ���� ����� ������ ���� �ʽ��ϴ�.");
			} else {
				p.sendMessage("��e�ְ� ���� : ��a"+baseRecord+" ��e��");
				p.sendMessage("��e�÷��� Ƚ�� : ��a"+clearCnt+ " ��e��");
			}
		}else {
			p.sendMessage(ms+"�ش� �ʿ� ���� ���̽� ����� ���� �����ϴ�.");
		}
	}

	public void endGame(boolean force) {
		if(force) {
			server.egCM.broadCast(server.ms_alert + disPlayGameName+" ������ ���� ���� �Ǿ����ϴ�.");
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
				p.sendMessage(ms+"���ο� ����� ����ƽ��ϴ�. \n+"+ms+"��f��� : ��a��l"+score+"��f��");
				section.set(p.getName()+"_score", score);
				section.set(p.getName()+"_clearCnt", clearCnt+1);
			}else {
				if(baseRecord < score) {
					p.sendMessage(ms+"���� ����� ��b"+baseRecord+" ��f�� ���� �� ���� ����� �޼��Ͽ����ϴ�.");
					p.sendMessage(ms+"���ο� ����� ��b"+score+" ��f���� �����մϴ�.");
					section.set(p.getName()+"_score", score);
					section.set(p.getName()+"_clearCnt", clearCnt+1);
				}else {
					p.sendMessage(ms+"���� ����� ��b"+baseRecord+" ��f���� �� ���� ���ο� ����� �������� �ʽ��ϴ�.");
					section.set(p.getName()+"_clearCnt", clearCnt+1);
				}
			}
			p.sendMessage(ms+"�� ������ ��b"+(clearCnt+1)+" ��f�� �÷����Ͽ����ϴ�.");
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
			
			server.egPM.printLog("[" + disPlayGameName + "] ��ŷ ������ �ε��");
		}
		
	}

	//////////////// �̺�Ʈ
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
			if(ingamePlayer.contains(p.getName())) { //���� �����ؾ����� ������ ��ɾ�
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
					e.setCancelled(true); //���߿� �� X
				}
			}
		}

		@EventHandler
		public void onClickInventory(InventoryClickEvent e) {
			if (!(e.getWhoClicked() instanceof Player))
				return;
			Player p = (Player) e.getWhoClicked();
			if (e.getInventory().getTitle().equalsIgnoreCase("�����")) {				
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
			if (!ingamePlayer.contains(p.getName()) //��Ŭ���� ���
					|| (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK))
				return;
			if (e.getItem() == null || e.getItem().getType() == Material.AIR) { //�������� �ȵ�� ��Ŭ�������� ����
				return;
			} else if (getHeldMainItemName(p).equalsIgnoreCase("��f[ ��6���� ����� ��f]")) {
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
					if(!cancel) { //�߸�ĵ�žƴϸ�
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
					if(!cancel) { //�߸�ĵ�žƴϸ�
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
	
	///////////////////// ������ �տ� ������ �÷��̾�� Ŭ����
	private class KomPlayer {
		
		public KomPlayer(Player p, String job) {

		}

	}
}
	
