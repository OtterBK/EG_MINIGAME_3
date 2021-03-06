package Minigames.ColorMatch;

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
import org.bukkit.World;
import org.bukkit.block.Block;
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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.NameTagVisibility;
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

public class ColorMatch extends Minigame{
	// �̺�Ʈ��
	public EventHandlerCLM event;
	
	public String ms = "��7[��6��ü��7] ";

	// ���� �� ��
	public List<Block> blockList = new ArrayList<Block>(64);
	public List<BlockPack> blockPacks = new ArrayList<BlockPack>(16);
	public Location loc_blockPos1;
	public Location loc_blockPos2;
	public byte woolData = 0;
	public List<Integer> tmpWoolCodes = new ArrayList<Integer>(16);
	public List<Integer> tmpBlockCodes = new ArrayList<Integer>(16);

	///////////// private
	// ���� �÷��̾� ����Ʈ
	private HashMap<String, ClmPlayer> playerMap = new HashMap<String, ClmPlayer>();

	//////// ���� ����
	
	public int gameDifStep = 1;
	public int gameRound = 1;
	public int gameSpeed = 80;
	public EGScheduler mainSch;
	
	////// ���� �κ��丮
	public Inventory inven_gameHelper;
	
	//////// ���̵��
	private Sidebar clmSidebar;
	private List<SidebarString> textList = new ArrayList<SidebarString>(5);
	private Team scoreBoardTeam;
	
	public ColorMatch(EGServer server) {

		//////////////////// �ʼ� ������
		super(server);
		
		for(int i = 0; i < 16; i++) {
			blockPacks.add(new BlockPack());
		}
		
		ms = "��7[ ��e! ��7] ��f: ��c�÷���ġ ��f>> "; // �⺻ �޼���
		gameName = "ColorMatch";
		disPlayGameName = ChatColor.RED+"�÷���ġ";
		inventoryGameName = ChatColor.stripColor(disPlayGameName);
		minPlayer = 2;
		maxPlayer = 6;
		startCountTime = 30;
		doneSetting = loadGameData(); // ���� ���� ��ġ ��� �ε�
		/////////////////////// �ڵ� ����(�����۵��)
		dirSetting("ColorMatch");
		////////////////
		
		mainSch = new EGScheduler(this);
		
		SidebarString tmpLine = new SidebarString("");
		clmSidebar = new Sidebar("��f[ ��6���� ��Ȳ ��f]", server, 600, tmpLine);
		
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
		loreList.add("��7������ ���۵Ǹ� ���� ���� ���� �� 1���� ����");
		loreList.add("��7�����ϰ� ���� ������� �˴ϴ�.");
		loreList.add("��7������� �ʴ� ���к��� ���� �ڽ��� �κ��丮��");
		loreList.add("��7�־����� ���� ������ �� �� �ֽ��ϴ�.");
		loreList.add("��7���� ���� ������� �� ������ �������� �Ǹ�");
		loreList.add("��7�й��մϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_gameHelper.setItem(11, item);
		
		event = new EventHandlerCLM(server, this);
		
		// �� �÷����ο� �̺�Ʈ ����
		server.getServer().getPluginManager().registerEvents(event, server);
		
		scoreBoardTeam = clmSidebar.getTheScoreboard().registerNewTeam("noNameTag");
		//team.setOption(Option.NAME_TAG_VISIBILITY, OptionStatus.NEVER);
		scoreBoardTeam.setOption(Option.COLLISION_RULE, OptionStatus.NEVER);
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
				.BOLD+ "�÷���ġ " + ChatColor.GRAY + "�� ���� �Ǿ����ϴ�!");
		initGame();
		ingame = true;
		gameDifStep = 0;
		/////////////// ������	
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
				// Ǯ�Ƿ� ����
				MyUtility.healUp(p);
				MyUtility.allClear(p);         
				//p.teleport(loc_Join);
				TitleAPI.sendFullTitle(p, 10, 80, 30, ChatColor.RED+"���� ����", ChatColor.RED+""+disPlayGameName);
				timerLevelBar(p, 10, false, true);
				clmSidebar.showTo(p);
				p.teleport(loc_Join, TeleportCause.PLUGIN);
			}
		}
		
		///////////////// ��¥ ����
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
			public void run() {
				for(String pName : ingamePlayer) {
					Player p = Bukkit.getPlayer(pName);
				}
				sendSound(Sound.BLOCK_SLIME_BREAK, 1.0f, 1.3f);
				gameTimer();
			}
		}, 200l);
	}
	
	//////////////////
	
	public void updateSidebar() {
		if(!ingame) return;
		textList.clear();
		
		SidebarString line = new SidebarString("");
		textList.add(line);
		line = new SidebarString("��e���̵� ��f: ��a"+(gameDifStep == 10 ? "Final" : gameDifStep)+"�ܰ�");
		textList.add(line);
		line = new SidebarString("");
		textList.add(line);
		line = new SidebarString("��e�����ο� ��f: ��a"+ingamePlayer.size()+"��");
		textList.add(line);
		line = new SidebarString("");
		textList.add(line);
		
		clmSidebar.setEntries(textList);
		clmSidebar.update();
		
		/*for(String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p)) 
				clmSidebar.showTo(p);
		}*/
	}
	
	public void gameTimer() {
		mainSch.cancelTask(true);
		mainSch.schTime = 0;
		mainSch.schTime2 = 0;
		gameDifStep = 0;
		schList.add(mainSch);
		mainSch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				//Bukkit.broadcastMessage("time1 : "+mainSch.schTime+" , time2 : "+mainSch.schTime2);
				if(mainSch.schTime2 == 0) {
					woolChange();
					setWoolData();
					sendSound(Sound.BLOCK_CHEST_LOCKED, 1.0f ,2.0f);
					mainSch.schTime2 = 1;
					mainSch.schTime = 0;
					gameSpeed = setGameSpeed();
				} else if(mainSch.schTime2 == 1) {
					if(mainSch.schTime >= gameSpeed) {
						gameRound++;
						mainSch.schTime2 = 2;
						mainSch.schTime = 0;
						for(BlockPack bp : blockPacks) {
							if(bp.data != woolData) {
								bp.setBlockData(Material.AIR, (byte)0);
							}
						}
						for(String pName : ingamePlayer) {
							Player t = Bukkit.getPlayer(pName);
							MyUtility.allClear(t);
						}
						sendSound(Sound.BLOCK_CHEST_CLOSE, 1.0f ,2.0f);
					}else {
						float leftTime = gameSpeed - mainSch.schTime;
						for(String tName : ingamePlayer) {
							Player t = Bukkit.getPlayer(tName);
							t.setExp(leftTime / (float)gameSpeed);
						}
						mainSch.schTime++;
					}			
				}else if(mainSch.schTime2 == 2) {
					if(mainSch.schTime >= 40) {
						mainSch.schTime2 = 0;
					}else {
						mainSch.schTime++;
					}			
				}else {
					mainSch.cancelTask(true);
				}
			}
		}, 0l, 1l);
	}
	
	public int setGameSpeed() {
		if(gameDifStep == 0) {
			gameDifStep = 1;
			gameRound = 0;
			sendTitle("��c��l"+gameDifStep+"�ܰ�", "", 40);
			sendSound(Sound.BLOCK_NOTE_HARP, 1.0f ,1.0f);
			updateSidebar();
		}
		if(gameDifStep == 1) {
			if(gameRound >= 3) {
				gameDifStep = 2;
				gameRound = 0;
				sendTitle("��c��l"+gameDifStep+"�ܰ�", "", 40);
				sendSound(Sound.BLOCK_NOTE_HARP, 1.0f ,1.0f);
				updateSidebar();
			}else {
				return 80;
			}
		}
		if(gameDifStep == 2) {
			if(gameRound >= 3) {
				gameDifStep = 3;
				gameRound = 0;
				sendTitle("��c��l"+gameDifStep+"�ܰ�", "", 40);
				sendSound(Sound.BLOCK_NOTE_HARP, 1.0f ,1.0f);
				updateSidebar();
			}else {
				return 65;
			}
		}
		if(gameDifStep == 3) {
			if(gameRound >= 3) {
				gameDifStep = 4;
				gameRound = 0;
				sendTitle("��c��l"+gameDifStep+"�ܰ�", "", 40);
				sendSound(Sound.BLOCK_NOTE_HARP, 1.0f ,1.0f);
				updateSidebar();
			}else {
				return 58;
			}
		}
		if(gameDifStep == 4) {
			if(gameRound >= 3) {
				gameDifStep = 5;
				gameRound = 0;
				sendTitle("��c��l"+gameDifStep+"�ܰ�", "", 40);
				sendSound(Sound.BLOCK_NOTE_HARP, 1.0f ,1.0f);
				updateSidebar();
			}else {
				return 50;
			}
		}
		if(gameDifStep == 5) {
			if(gameRound >= 4) {
				gameDifStep = 6;
				gameRound = 0;
				sendTitle("��c��l"+gameDifStep+"�ܰ�", "", 40);
				sendSound(Sound.BLOCK_NOTE_HARP, 1.0f ,1.0f);
				updateSidebar();
			}else {
				return 42;
			}
		}
		if(gameDifStep == 6) {
			if(gameRound >= 5) {
				gameDifStep = 7;	
				gameRound = 0;
				sendTitle("��c��l"+gameDifStep+"�ܰ�", "", 40);
				sendSound(Sound.BLOCK_NOTE_HARP, 1.0f ,1.0f);
				updateSidebar();
			}else {
				return 35;
			}
		}
		if(gameDifStep == 7) {
			if(gameRound >= 6) {
				gameDifStep = 8;
				gameRound = 0;
				sendTitle("��c��l"+gameDifStep+"�ܰ�", "", 40);
				sendSound(Sound.BLOCK_NOTE_HARP, 1.0f ,1.0f);
				updateSidebar();
			}else {
				return 29;
			}
		}
		if(gameDifStep == 8) {
			if(gameRound >= 6) {
				gameDifStep = 9;
				gameRound = 0;
				sendTitle("��c��l"+gameDifStep+"�ܰ�", "", 40);
				sendSound(Sound.BLOCK_NOTE_HARP, 1.0f ,1.0f);
				updateSidebar();
			}else {
				return 24;
			}
		}
		if(gameDifStep == 9) {
			if(gameRound >= 6) {
				gameDifStep = 10;
				gameRound = 0;
				sendTitle("��c��l�����ܰ�", "", 40);
				sendSound(Sound.BLOCK_NOTE_HARP, 1.0f ,1.0f);
			}else {
				return 20;
			}
		}
		if(gameDifStep == 10) {
			return 15;
		}
		return 0;
	}
	
	public void setWoolData() {
		woolData = (byte) MyUtility.getRandom(0, 15);
		ItemStack item = new ItemStack(Material.WOOL, 1, woolData);
		for(String pName : ingamePlayer) {
			Player t = Bukkit.getPlayer(pName);
			if(existPlayer(t)) {
				for(int i = 0; i < 9; i++) {
					t.getInventory().setItem(i, item);
				}
			}
		}
	}
	
	public void woolChange() {
		tmpWoolCodes.clear();
		tmpBlockCodes.clear();
		for(int i = 0; i < 16; i++) {
			tmpWoolCodes.add(i);
			tmpBlockCodes.add(i);
		}
				
		for(BlockPack bp : blockPacks) {
			int randomCode = MyUtility.getRandom(0, tmpWoolCodes.size()-1);
			int randomBp = MyUtility.getRandom(0, tmpBlockCodes.size()-1);
			
			int woolCode = tmpWoolCodes.get(Integer.valueOf(randomCode));
			int bpIndex = tmpBlockCodes.get(Integer.valueOf(randomBp));
			
			//Bukkit.getLogger().info("woolcode: "+woolCode+", bpIndex: "+bpIndex);
			
			tmpWoolCodes.remove(randomCode);
			tmpBlockCodes.remove(randomBp);
			
			//Bukkit.getLogger().info("randomCode: "+randomCode+", randomBp: "+randomBp);
			//Bukkit.getLogger().info("woolcode: "+woolCode+", bpIndex: "+bpIndex);
			
			/*for(int code : tmpWoolCodes) {
				Bukkit.getLogger().info("code: "+code);
			}
			for(int code : tmpBlockCodes) {
				Bukkit.getLogger().info("bpCode: "+code);
			}*/
			
			blockPacks.get(bpIndex).setBlockData(Material.WOOL, (byte)woolCode);
		}
		
	}

	public void initGame() {
		lobbyStart = false;
		ending = false;

		//������
		schList.clear();
		for(BlockPack bp : blockPacks) {
			bp.setBlockData(Material.WOOL, (byte)0);
		}
	}

	public boolean loadGameData() {
		boolean ret = true;
		loc_Join = loadLocation(gameName, "JoinPos");
		loc_blockPos1 = loadLocation(gameName, "block1");
		loc_blockPos2 = loadLocation(gameName, "block2");
	
		if (loc_Join == null) {
			server.egPM.printLog(ms + "[" + disPlayGameName + "] ���� ���� ��� ������ �������� �ʾҽ��ϴ�.");
			ret = false;
		}else {
			loc_spectate = loc_Join.clone().add(0,10,0);
		}
		if (loc_blockPos1 == null) {
			server.egPM.printLog(ms + "[" + disPlayGameName + "] �� �� ���� 1�� �������� �ʾҽ��ϴ�.");
			ret = false;
		}
		if (loc_blockPos2 == null) {
			server.egPM.printLog(ms + "[" + disPlayGameName + "] �� �� ���� 2�� �������� �ʾҽ��ϴ�.");
			ret = false;
		}
		if(loc_blockPos1 != null && loc_blockPos2 != null) {
			if(loc_blockPos1.getBlockX() > loc_blockPos2.getBlockX()) {
				int tmp = loc_blockPos1.getBlockX();
				loc_blockPos1.setX(loc_blockPos2.getBlockX());
				loc_blockPos2.setX(tmp);;
			}
			
			if(loc_blockPos1.getBlockY() > loc_blockPos2.getBlockY()) {
				int tmp = loc_blockPos1.getBlockY();
				loc_blockPos1.setY(loc_blockPos2.getBlockY());
				loc_blockPos2.setY(tmp);;
			}
			
			if(loc_blockPos1.getBlockZ() > loc_blockPos2.getBlockZ()) {
				int tmp = loc_blockPos1.getBlockZ();
				loc_blockPos1.setZ(loc_blockPos2.getBlockZ());
				loc_blockPos2.setZ(tmp);;
			}
			
			Location l = loc_blockPos1;
			Location l2 = loc_blockPos2;
			World w = l.getWorld();

			for (int y = l.getBlockY(); y <= l2.getBlockY(); y++) {
				for (int z = l.getBlockZ(); z <= l2.getBlockZ(); z++) {
					for (int x = l.getBlockX(); x <= l2.getBlockX(); x++) {
						//Bukkit.getLogger().info("x: "+x+", y: "+y+" , z: "+z);
						Location tl = new Location(w,x,y,z);
						blockList.add(tl.getBlock());
					}
				}
			}	
			
			int i = 0;
			int bpIndex = 0;
			int nowRow = 0;
			int nowCol = 0;
			int maxRow = 8;
			while(i < blockList.size()) {
				//Bukkit.getLogger().info(""+bpIndex);
				BlockPack bp = blockPacks.get(bpIndex);
				bp.addBlock(blockList.get(i));
				i = i + 1;
				nowRow = nowRow + 1;
				bp.addBlock(blockList.get(i));
				i = i + 1;
				nowRow = nowRow + 1;
				bpIndex = bpIndex + 1;
				if(nowRow >= maxRow) {
					nowRow = 0;
					nowCol = nowCol + 1;
					bpIndex = ((nowCol / 2) * (maxRow / 2));
				}
			}
		}
		
		if(ret == true) {
			server.egPM.printLog("[" + disPlayGameName + "] ���� �̻� ����");
			doneSetting = ret;
		}
		
		return ret;
	}
	
	public void onCommand(Player p, String[] cmd) {
		if (cmd.length <= 1) {
			p.sendMessage(ms + "[" + disPlayGameName + "] " + "/clm join - ���� ����");
			p.sendMessage(ms + "[" + disPlayGameName + "] " + "/clm quit - ���� ����");
			p.sendMessage(ms + "[" + disPlayGameName + "] " + "/clm set - ���� ����");
		} else if (cmd[1].equalsIgnoreCase("set")) {
			if (cmd.length <= 2) {
				p.sendMessage(ms + "[" + disPlayGameName + "] " + "/clm set loc- ���� ���� ����");
			} else if (cmd[2].equalsIgnoreCase("loc")) {
				if (cmd.length <= 3) {
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "/clm set loc join - ���� ���� ��� ���� ����");
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "/clm set loc block1 - 8x8ĭ�� ������1");
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "/clm set loc block2 - 8x8ĭ�� ������2");
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
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "/clm set loc join - ���� ���� ��� ���� ����");
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "/clm set loc block1 - 8x8ĭ�� ������1");
					p.sendMessage(ms + "[" + disPlayGameName + "] " + "/clm set loc block2 - 8x8ĭ�� ������2");
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
			p.sendMessage(""+blockList.size());
		}else if (cmd[1].equalsIgnoreCase("debug1")) {
			woolChange();
		}else if (cmd[1].equalsIgnoreCase("debug2")) {
			performence();
		}else if (cmd[1].equalsIgnoreCase("debug3")) {
			//Bukkit.getLogger().info("mainid: "+mainSch.schId);
			for(EGScheduler sch : schList) {
				Bukkit.getLogger().info("id: "+sch.schId);
				//sch.cancelTask(true);
			}
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
				if(clmSidebar != null) clmSidebar.hideFrom(p);
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
					for(int i = 0; i < 3; i++) {
						Block b = blockList.get(MyUtility.getRandom(0, blockList.size()-1));
						Location loc = b.getLocation().add(0,2,0);
						Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
						FireworkMeta fwm = fw.getFireworkMeta();

						fwm.setPower(1);
						fwm.addEffect(FireworkEffect.builder().withColor(MyUtility.getRandomColor()).flicker(true).build());

						fw.setFireworkMeta(fwm);
						fw.detonate();
						
				        Firework fw2 = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
				        fw2.setFireworkMeta(fwm);
					}
					woolChange();
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
							server.ms_alert + ChatColor.GRAY  + "��c"+winner+"��7���� ��a�¸���7�� ��c�÷���ġ��7�� ���� �Ǿ����ϴ�.");
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
							server.ms_alert + ChatColor.GRAY  + "���ºη� ��c�÷���ġ��7�� ���� �Ǿ����ϴ�.");
					}
				}, 200L);
			} catch(Exception e) {
					endGame(true);
			}
		}
	
	}

	public void endGame(boolean force) {
		if(force) {
			server.egCM.broadCast(server.ms_alert + "�÷���ġ ������ ���� ���� �Ǿ����ϴ�.");
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
	public class EventHandlerCLM extends EGEventHandler {

		private ColorMatch game;

		public EventHandlerCLM(EGServer server, ColorMatch clm) {
			super(server);
			this.game = clm;
		}

		@EventHandler
		public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
			Player p = e.getPlayer();
			String[] cmd = e.getMessage().split(" ");
			if (cmd[0].equalsIgnoreCase("/clm")) {
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
					e.setCancelled(true);
					if(!ingame) {
				        if (cause.equals(DamageCause.VOID) && !ingame) { //���� ��� ������, ����		           
				            p.teleport(loc_Join, TeleportCause.PLUGIN);
				            Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {
				            	public void run() {
				            		p.teleport(loc_Join, TeleportCause.PLUGIN);
				            	}
				            }, 2l);
				        }
					} else {
						if (cause.equals(DamageCause.VOID)) { //���� ��� ������, ����	
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
	}
	
	///////////////////// �÷���ġ�� ������ �÷��̾�� Ŭ����
	private class ClmPlayer {
		
		public ClmPlayer(Player p, String job) {

		}

	}
	
	private class BlockPack{
		List<Block> blocks = new ArrayList<Block>(4);
		byte data = 0;
		
		public void setBlockData(Material m, byte data) {
			for(Block b : blocks) {
				b.setType(m);
				b.setData(data);
				this.data = data;
			}
		}
		
		public void addBlock(Block b) {
			blocks.add(b);
		}
	}
}
