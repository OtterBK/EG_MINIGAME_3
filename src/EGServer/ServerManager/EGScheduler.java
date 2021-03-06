package EGServer.ServerManager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;

import Minigames.Minigame;

public class EGScheduler {
	
	public static List<EGScheduler> debugList = new ArrayList<EGScheduler>();
	
	public int schId = -1;
	public int schTime;
	public int schTime2;
	public Minigame minigame;
	
	public EGScheduler(Minigame game) {
		debugList.add(this);
		minigame = game;
		if(minigame != null) minigame.schList.add(this);
	}
	
	public void addSchList(Minigame game) {
		minigame = game;
		if(minigame != null) minigame.schList.add(this);
	}
	
	public boolean cancelTask(boolean removeList) {
		if(schId == -1) return false;
		Bukkit.getScheduler().cancelTask(schId);
		if(removeList && minigame != null)minigame.schList.remove(this);
		schId = -1;
		return true;
	}
}
