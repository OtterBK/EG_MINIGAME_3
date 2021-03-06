package EGServer.WatchdogManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import EGServer.EGServer;
import EGServer.ServerManager.EGPlugin;
import Utility.MyUtility;

public class WatchdogManager extends EGPlugin{
	
	private String ms = "§f[ §6감시 §f] ";
	private List<String> noLogList = new ArrayList<String>();
	private HashMap<Player, Player> spectateMap = new HashMap<Player, Player>();
	private HashMap<String, ItemStack[]> backupInven =  new HashMap<String, ItemStack[]>();
	private HashMap<String, ItemStack[]> backupArmorInven =  new HashMap<String, ItemStack[]>();
	private HashMap<String, Location> backupLoc =  new HashMap<String, Location>();
	private HashMap<String, GameMode> backupGameMode =  new HashMap<String, GameMode>();
	
	public WatchdogManager(EGServer server) {
		super(server);
		// TODO Auto-generated constructor stub
		
		server.getServer().getPluginManager().registerEvents(new SpectateHandler(), server);
	}

	public void watchdog(Player p, Player t) {
		if(p == null || t == null) {
			p.sendMessage(ms+t.getName()+" 님은 접속중이  아닙니다.");
		}
		
		backup(p);
		
		spectateMap.put(p, t);
		p.sendMessage(ms+t.getName()+" 님을 감시합니다. §c좌클릭을 누르지 마세요!");
		p.setGameMode(GameMode.SPECTATOR);
		MyUtility.allClear(p);
		Location l = t.getLocation().add(t.getLocation().getDirection().multiply(0.20));
		p.teleport(l);
		
		copyInventory(p);
	}
	
	public void sendLog(String str) {
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(p.isOp()) {
				if(!noLogList.contains(p.getName()))
					p.sendMessage(ms+str);
			}
		}	
	}
	
	public boolean checkAutoMouse(String tName, List<Long> intervalList) {
		if(intervalList == null) return false;
		if(intervalList.size() < 5) return false;
		
		boolean autoCheck = true;
		boolean fastCheck = false;
		
		//Bukkit.broadcastMessage("-------");
		
		long lastInterval = intervalList.get(0);
		for(int i = 1; i < intervalList.size(); i++) {
			long interval = intervalList.get(i);
			long delay = Math.abs(lastInterval - interval);
			//Bukkit.broadcastMessage(delay+"ms");
			if(delay > 50) {
				autoCheck = false;
			}else {
				if(i == 4) {
					//if(delay <= 50) {
						fastCheck = true;
					//}
				}			
			}
			lastInterval = interval;
		}
		
		if(autoCheck) {
			//sendLog(tName+"의 최근 5회 공격이 모두 CPS 20이상, 오토마우스 의심");
		}
		
		/*if(!fastCheck) {
			sendLog(Math.abs(intervalList.get(3) - intervalList.get(4))+"ms");
		}*/
		
		//Bukkit.broadcastMessage("autoCheck : "+autoCheck+" | fastCheck : "+fastCheck);
		
		return autoCheck || fastCheck;
	}
	
	public void toggleLog(Player p) {
		if(noLogList.contains(p.getName())) {
			p.sendMessage(ms+"이제 로그를 봅니다.");
			noLogList.remove(p.getName());
		}else {
			p.sendMessage(ms+"이제 로그를 보지 않습니다.");
			noLogList.add(p.getName());
		}
	}
	
	public void backup(Player p) {
		backupInven.put(p.getName(), p.getInventory().getContents());
		backupArmorInven.put(p.getName(), p.getInventory().getArmorContents());
		backupLoc.put(p.getName(), p.getLocation());
		backupGameMode.put(p.getName(), p.getGameMode());
	}
	
	public void copyInventory(Player p) {
		Player t = spectateMap.get(p);
		if(t == null) return;
		PlayerInventory tInven = t.getInventory();
		PlayerInventory pInven = p.getInventory();
		pInven.setArmorContents(tInven.getArmorContents());
		pInven.setContents(tInven.getContents());
	}
	
	public boolean isSpectating(Player p) {
		return spectateMap.containsKey(p);
	}
	
	public void stopWatchdog(Player p, boolean doBackup) {
		if(doBackup) {
			Player t = spectateMap.get(p);
			if(t != null) p.sendMessage(ms+t.getName()+" 님의 감시를 그만뒀습니다.");
			spectateMap.remove(p);
			
			Location l = backupLoc.get(p.getName());
			if(l != null) p.teleport(l);
			
			ItemStack[] items = backupInven.get(p.getName());
			if(items != null) p.getInventory().setContents(items);
			
			ItemStack[] armors = backupArmorInven.get(p.getName());
			if(armors != null) p.getInventory().setArmorContents(armors);
			
			GameMode gameMode = backupGameMode.get(p.getName());
			if(gameMode != null) p.setGameMode(gameMode);;
			
			backupLoc.remove(p.getName());
			backupInven.remove(p.getName());
			backupArmorInven.remove(p.getName());
			backupGameMode.remove(p.getName());
		} else {
			spectateMap.remove(p);
			
			backupLoc.remove(p.getName());
			backupInven.remove(p.getName());
			backupArmorInven.remove(p.getName());
			backupGameMode.remove(p.getName());
		}
	}
	
	private class SpectateHandler implements Listener{
			
		@EventHandler
		public void onPlayerMove(PlayerMoveEvent e) {
			Player p = (Player) e.getPlayer();
			if(spectateMap.values().contains(p)) {
				List<Player> spectators = new ArrayList<Player>(4);
				for(Player spectator : spectateMap.keySet()) {
					Player t = spectateMap.get(spectator);
					if(t.getName().equalsIgnoreCase(p.getName())) {
						spectators.add(spectator);
					}
				}
				Location l = p.getLocation().add(p.getLocation().getDirection().multiply(0.20));
				for(Player spectator : spectators) {
					spectator.teleport(l);
				}
			}else if(spectateMap.containsKey(p)) {
				Player t = spectateMap.get(p);
				Location l = t.getLocation().add(t.getLocation().getDirection().multiply(0.20));
				if(t != null) p.teleport(l);
			}
		}
		
		@EventHandler
		public void onInventoryClick(InventoryCloseEvent e) {
			if(e.getPlayer() instanceof Player) {
				Player p = (Player) e.getPlayer();
				if(spectateMap.values().contains(p)) {
					List<Player> spectators = new ArrayList<Player>(4);
					for(Player spectator : spectateMap.keySet()) {
						Player t = spectateMap.get(spectator);
						if(t.getName().equalsIgnoreCase(p.getName())) {
							spectators.add(spectator);
						}
					}
					for(Player spectator : spectators) {
						copyInventory(spectator);
					}
				}		
			}			
		}	
		
		@EventHandler
		public void onDropItem(PlayerDropItemEvent e) {
			Player p = (Player) e.getPlayer();
			if (spectateMap.values().contains(p)) {
				List<Player> spectators = new ArrayList<Player>(4);
				for (Player spectator : spectateMap.keySet()) {
					Player t = spectateMap.get(spectator);
					if (t.getName().equalsIgnoreCase(p.getName())) {
						spectators.add(spectator);
					}
				}
				for (Player spectator : spectators) {
					copyInventory(spectator);
				}
			}			
		}
		
		@EventHandler
		public void onPickUpItem(EntityPickupItemEvent e) {
			if(e.getEntity() instanceof Player) {
				Player p = (Player) e.getEntity();
				if (spectateMap.values().contains(p)) {
					List<Player> spectators = new ArrayList<Player>(4);
					for (Player spectator : spectateMap.keySet()) {
						Player t = spectateMap.get(spectator);
						if (t.getName().equalsIgnoreCase(p.getName())) {
							spectators.add(spectator);
						}
					}
					for (Player spectator : spectators) {
						copyInventory(spectator);
					}
				}
			}				
		}
		
		@EventHandler
		public void onPlayerQuit(PlayerQuitEvent e) {
			Player p = e.getPlayer();
			if(spectateMap.values().contains(p)) {
				List<Player> spectators = new ArrayList<Player>(4);
				for(Player spectator : spectateMap.keySet()) {
					Player t = spectateMap.get(spectator);
					if(t.getName().equalsIgnoreCase(p.getName())) {
						spectators.add(spectator);
					}
				}
				for(Player spectator : spectators) {
					spectator.sendMessage(ms+"감시 대상이 서버에서 나갔습니다.");
					stopWatchdog(spectator, true);
				}
			}else if(spectateMap.containsKey(p)) {
				stopWatchdog(p, true);
			}
		}
		
	}
	
}
