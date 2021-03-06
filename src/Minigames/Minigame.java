package Minigames;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.connorlinfoot.titleapi.TitleAPI;

import EGServer.EGServer;
import EGServer.DataManger.PlayerData;
import EGServer.ServerManager.EGPlugin;
import EGServer.ServerManager.EGScheduler;
import Utility.MyUtility;
import me.confuser.barapi.BarAPI;

public class Minigame extends EGPlugin {

	public static boolean checkSetting = false;

	//���ݰ� ������
	public HashMap<String, Long> victimDelayMap = new HashMap<String, Long>();
	public HashMap<String, List<Long>> intervalMap = new HashMap<String, List<Long>>();
	
	////////////////////���� �ʼ���
	public String gameName; 
	public String disPlayGameName;
	public String inventoryGameName;
	public String cmdMain;
	public int minPlayer = 6;
	public int maxPlayer = 10;
	public int startCountTime = 60;
	public Location loc_Join;
	public boolean doneSetting;
	public boolean joinBlock = false;
	public boolean canSpectate = true;
	public boolean rankGame = false;
	///////////////////////////////////

	public Location loc_Lobby;
	public Location loc_spectate; 
	public List<String> ingamePlayer = new ArrayList<String>(10);
	public HashMap<String, String> rankMap = new HashMap<String, String>();
	public ItemStack helpItem;
	
	public boolean lobbyStart; //���� ���� ī��Ʈ��?
	public boolean ingame; //������ ����?
	public boolean ending; //������?
	
	///�� ������ ������� ���
	public List<EGScheduler> schList = new ArrayList<EGScheduler>(40);
	
	//////////�������
	public EGScheduler startSch = new EGScheduler(this);
	
	public EGScheduler spawnSch = new EGScheduler(null);
	
	//������
	public Minigame(EGServer server) {
		/////////////////////////�ʼ� ����
		super(server);
		///////////////////////�ڵ� ����(������ ���)
		loc_Lobby = server.lobby;
		helpItem = new ItemStack(Material.BOOK, 1);
		ItemMeta meta = helpItem.getItemMeta();
		meta.setDisplayName("��f[ ��6���� ����� ��f]");
		List<String> lorelist = new ArrayList(1);
		lorelist.add("��f- ��7��Ŭ���� ���� ����̸� ���ϴ�.");
		meta.setLore(lorelist);
		helpItem.setItemMeta(meta);
	}
	
	///////////////////////////////////////// �������̵� �ʿ�
	
	//���� ������, �������̵� �ʼ�
	public void gameHelpMsg(Player p) {
		
	}
	//������ ������, �������̵� �ʼ�
	public void startGame() {

	}
	
	//���� ����, �������̵� �ʼ�
	public void gameQuitPlayer(Player p, boolean announce, boolean isDead) {

	}
	
	//���� ����
	public void endgame(boolean force) {
		
	}
	
	//������ �ݿ�
	public void saveData() {
		
	}
	
	//��ũ ����
	public void setRankMap(String pName) {
		
	}
	
	/////////////////////////////////////////////////
	
	public Location loadLocation(String gameName, String locName) {
		File file = new File(server.getDataFolder().getPath() + "/" + gameName + "/Location", "location.yml");
		if (!file.exists() || file.isDirectory())
			return null;
		Location loc;
		String posW;
		int posX, posY, posZ;
		try {
			FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
			posW = fileConfig.getString(locName + "_w");
			posX = fileConfig.getInt(locName + "_x");
			posY = fileConfig.getInt(locName + "_y");
			posZ = fileConfig.getInt(locName + "_z");
			loc = new Location(Bukkit.getWorld(posW), posX, posY, posZ);
		} catch (Exception ex) {
			return null;
		}
		return loc;
	}

	public boolean saveLocation(String gameName, String locName, Location loc) {
		try {
			File file = new File(server.getDataFolder().getPath() + "/" + gameName + "/Location", "location.yml");
			FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
			fileConfig.set(locName + "_w", loc.getWorld().getName());
			fileConfig.set(locName + "_x", Integer.valueOf(loc.getBlockX()));
			fileConfig.set(locName + "_y", Integer.valueOf(loc.getBlockY() + 1));
			fileConfig.set(locName + "_z", Integer.valueOf(loc.getBlockZ()));
			fileConfig.save(file);
		} catch (Exception e) {
			server.egPM.printLog("[" + disPlayGameName + "]" + locName + "�������� ���� �߻�");
			return false;
		}
		return true;
	}

	//���� Ż�� ó��
	public void exitGame(Player p) {

	}

	//���� Ż�� ó��
	public void exitGame(String pName) {

	}

	//�̴ϰ����� �����Ѱ�?
	public boolean isReady() {
		return this.checkSetting;
	}

	//�ش� �̴ϰ��ӿ� ������ �÷��̾�Ը� �޼��� ����
	public void sendMessage(String str) {
		for (String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			if(p != null) p.sendMessage(str);
		}
	}
	
	public void sendActionbar(String msg, int tick) {
		for (String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			if(p!=null)
				ActionBarAPI.sendActionBar(p,msg, tick);
		}	
	}
	
	public void sendBossbar(String msg, int sec) {
		for (String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			if(p!=null)
				BarAPI.setMessage(p, msg, sec);
		}	
	}
	
	public void sendTitle(String main, String sub, int tick) {
		for (String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			if(p!=null)
				TitleAPI.sendFullTitle(p, 10, tick, 20, main, sub);
		}	
	}
	
	public void sendSound(Sound sound) {
		for (String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			if(p!=null)
				p.playSound(p.getLocation(), sound, 1.0f, 1.0f);
		}
	}
	
	public void sendToLoc(Location l) {
		for (String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			if(p!=null)
				p.teleport(l, TeleportCause.PLUGIN);
		}	
	}
	
	public void sendPotionEffect(PotionEffect pt) {
		for (String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			if(p!=null)
				p.addPotionEffect(pt);
		}	
	}
	
	public void sendGold(List<String> pList, int amt) {
		for (String pName : pList) {
			Player p = Bukkit.getPlayer(pName);
			if(p!=null) {
				//server.egGM.giveGold(p.getName(), 40);
			}			
		}	
	}
	
	public void sendSound(Sound sound, float volume, float spd) {
		for (String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			if(p!=null)
				p.playSound(p.getLocation(), sound, volume, spd);
		}
	}
	
	public boolean checkVictimDelay(String pName, Player victimP) {
		//Bukkit.broadcastMessage("ȣ���");
		boolean res = true;
		
		if(victimDelayMap.containsKey(pName)) {
			long lastAttack = victimDelayMap.get(pName);
			long nowTime = System.currentTimeMillis();
			long attackInterval = Math.abs(nowTime - lastAttack);		
			
			//Bukkit.broadcastMessage(nowTime+" - "+lastAttack+" = "+attackInterval+"���͹�");
			
			if(attackInterval < 510) { //�������� 510ms ������ ������ ��	
				res = false;
				//Bukkit.broadcastMessage("���� Ÿ�� �Ұ�");
			}else {
				victimDelayMap.put(pName, nowTime); //�ֱ� ����Ÿ�� ����		
			}
			
			//���丶�콺 ������
			List<Long> intervalList = intervalMap.get(pName);
			if(intervalList == null) {
				intervalList = new ArrayList<Long>(5);
				intervalMap.put(pName, intervalList);
			}
			if(intervalList.size() >= 5) { //�ִ� 5������ ����
				intervalList.remove(0);
			}
			long clickTime = System.currentTimeMillis();
			intervalList.add(clickTime);
			if(server.egWM.checkAutoMouse(pName, intervalList)) {
				//Bukkit.broadcastMessage("����üũ ����");
				res = false;
				//if(existPlayer(victimP)) {
				//	victimP.sendMessage("��f[ ��cEG ���� ��f] ����� ������ �÷��̾ ���丶�콺�� ��� ���� �� �����ϴ�.");
				//}
			}
		}else {
			victimDelayMap.put(pName, System.currentTimeMillis());
			res = true;
		}	
		return res;
	}
	
	public void clearClickMap() {
		victimDelayMap.clear();
		intervalMap.clear();
	}
	
	/*public void spectate(Player p) {
		if(!existPlayer(p)) return;
		p.sendMessage(server.ms_alert + "�̹� ������ ���۵Ǿ����ϴ�. ������ҷ� �̵��Ǹ� ���� ���� ä���� ����մϴ�.");
		p.teleport(loc_spectate);
		server.specList.put(p.getName(), this);
		p.setGameMode(GameMode.SPECTATOR);
		p.setFlying(true);
	}*/

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
			if(canSpectate && loc_spectate != null) {
				if(!existPlayer(p)) return;
				MyUtility.allClear(p);
				p.sendMessage(server.ms_alert + "�̹� ������ ���۵Ǿ����ϴ�. ������ҷ� �̵��Ǹ� ���� ���� ä���� ����մϴ�.");
				p.teleport(loc_spectate, TeleportCause.PLUGIN);
				server.specList.put(p.getName(), this);
				p.setGameMode(GameMode.SPECTATOR);
				p.setFlying(true);
			} else {
				p.sendMessage(server.ms_alert + "�̹� ������ ���۵Ǿ����ϴ�."); 
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
			server.playerList.put(p.getName(), disPlayGameName);
			server.spawnList.remove(p.getName());
			p.getInventory().setItem(8, helpItem);
			p.getInventory().setHeldItemSlot(8);
			p.teleport(loc_Join, TeleportCause.PLUGIN);
			sendMessage(server.ms_alert + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " ���� �����߽��ϴ�. " + ChatColor.RESET
					+ "[ " + ChatColor.GREEN + ingamePlayer.size() + " / "+ minPlayer + ChatColor.RESET + " ]");
			TitleAPI.sendTitle(p, 10, 70, 30,ChatColor.RED+""+disPlayGameName);
			sendSound(Sound.BLOCK_NOTE_PLING, 2.0f, 1.0f);
			if(lobbyStart) {
				if(startSch.schTime > 1)
					BarAPI.setMessage(p, ChatColor.GREEN+"���� ���۱���", startSch.schTime);
			} else {
				if ((ingamePlayer.size() >= minPlayer)) {
					startCount();
				} 
			}
			setRankMap(p.getName());
		}
	}
	
	public void startCount() {
		if(ingame) return;
		lobbyStart = true;
		startSch.cancelTask(true);
		startSch.schTime = startCountTime;
		server.egCM.broadCast(server.ms_alert + ChatColor.RED+ChatColor.BOLD + disPlayGameName + ChatColor.GRAY + " ������ " + ChatColor.GRAY + "�� ���۵˴ϴ�.");
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
					lobbyStart = false;
					startSch.cancelTask(true);
					playerSet();
					startGame();
				}
			}
		}, 0L, 20L);
	}
	
	
	public void countDown(int time, String subText) {
		if(ingamePlayer.size() <= 0) return;
		for(String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			timerLevelBar(p, time, false, true);
		}
		EGScheduler sch = new EGScheduler(this);
		sch.schTime = time;
		sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				if(sch.schTime > 0) {
					for(String pName : ingamePlayer) {
						Player p = Bukkit.getPlayer(pName);
						TitleAPI.sendFullTitle(p, 0, 30, 0, "��c��l"+sch.schTime, "��e��l"+subText);
						p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
					}
					sch.schTime--;
				}
			}
		}, 0l, 20l);
	}
	
	public void playerSet() {
		for(String pName : ingamePlayer) {
			Player p = Bukkit.getPlayer(pName);
			server.waitingPlayer.remove(pName);
			removeSidebar(p);
			//if(existPlayer(p)) {
			//	p.teleport(loc_Join);
			//}		
		}
	}	
	
	public void divideSpawn() {
		if(ingamePlayer.size() <= 0) return;
		if(spawnSch.schId != -1) return;
		List<String> tmpList = new ArrayList<String>(ingamePlayer);
		spawnSch.schTime = tmpList.size();
		spawnSch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				if(spawnSch.schTime > 0) {
					spawnSch.schTime--;
					Player p = Bukkit.getPlayer(tmpList.get(spawnSch.schTime));
					if(existPlayer(p))
						server.spawn(p);
				} else {
					spawnSch.cancelTask(true);
				}
			}
		}, 0l, 2l);
	}
	
	public void sendTeamChat(List<String> tmpList, String str) {
		for(String pName : tmpList) {
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p)) p.sendMessage(str);
		}
	}
	
	public void sendTeamSound(List<String> tmpList, Sound sound) {
		for(String pName : tmpList) {
			Player p = Bukkit.getPlayer(pName);
			if(existPlayer(p)) p.playSound(p.getLocation(), Sound.BLOCK_NOTE_HARP, 1.0F, 1.0F);
		}
	}
	
	public int timerLevelBar(Player p, int time, boolean up ,boolean useLevel) {
		if(!existPlayer(p)) return 0;
		EGScheduler sch = new EGScheduler(this);
		
		if(up) {
			sch.schTime = 0;
			sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
				public void run() {
					if (sch.schTime < time) {
						if(useLevel) p.setLevel(time-sch.schTime);
						p.setExp((float)sch.schTime / time);
						sch.schTime += 1;
					} else {
						if(useLevel) p.setLevel(0);
						p.setExp(1);
						sch.cancelTask(true);
					}
				}
			}, 0L, 20L);
		} else {
			sch.schTime = time;
			sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
				public void run() {
					if (sch.schTime > 0) {
						if(useLevel) p.setLevel(sch.schTime);
						p.setExp((float)sch.schTime / time);
						sch.schTime -= 1;
					} else {
						if(useLevel) p.setLevel(0);
						p.setExp(0);
						sch.cancelTask(true);
					}
				}
			}, 0L, 20L);
		}
		return sch.schId;
	}

}
