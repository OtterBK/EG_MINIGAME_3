package EGServer.ChatManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import EGServer.EGServer;
import EGServer.DataManger.PlayerData;
import EGServer.ServerManager.EGPlugin;

public class ChatManager extends EGPlugin{
	
	public List<String> noSpeakerList = new ArrayList<String>();
	public List<String> noAlertList = new ArrayList<String>();
	public List<String> noSoundList = new ArrayList<String>();
	public List<String> socialSpyList = new ArrayList<String>();
	public List<String> muteList = new ArrayList<String>();
	public List<String> opChat = new ArrayList<String>();
	public HashMap<String, String> prefixMap = new HashMap<String, String>();
	
	public ChatManager(EGServer server){
		super(server);
	}
	
	public boolean isMute(Player p) {
		PlayerData pData = server.egDM.getPlayerData(p.getName());
		if(pData == null) return false;
		if(pData.isMuted()) {
			int leftSec = pData.getleftMuteTime();
			p.sendMessage("§c당신은 채팅 금지 상태입니다. 문의사항은 '/문의' 를 이용해주세요.\n§c남은 뮤트 시간 : "+leftSec+"초");
			return true;
		}else {
			return false;
		}
	}
	
	public int broadCast(String msg) {
		int sendCnt = 0;
		//String ms = "&f[ &c알림 &f] ";
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(!noAlertList.contains(p.getName()))
			p.sendMessage(msg);
		}
		Bukkit.getLogger().info(msg);
		return sendCnt;
	}
	
	public int forceAlert(String msg) {
		int sendCnt = 0;
		//String ms = "&f[ &c알림 &f] ";
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.sendMessage(msg);
		}
		Bukkit.getLogger().info(msg);
		return sendCnt;
	}
	
	public int sayAll(Player talker, String msg) {
		int sendCnt = 0;
		if(isMute(talker)) return 0; 
		//String ms = "&f[ &c알림 &f] ";
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(!noSpeakerList.contains(p.getName()))
			p.sendMessage(msg);
		}
		Bukkit.getLogger().info(msg);
		PlayerData pData = server.egDM.getPlayerData(talker.getName());
		if(pData != null) {
			pData.appendChat(msg);
		}
		return sendCnt;
	}
	
	public int opRequest(String msg) {
		int sendCnt = 0;
		//String ms = "&f[ &c알림 &f] ";
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(p.isOp())
			p.sendMessage(msg);
		}
		return sendCnt;
	}
	
	/*public int sendDistanceMsg(List<Player> playerList, String str, Location baseLoc, int distance, int lossDistance) {
		int sendCnt = 0;
		
		for(Player p : playerList) {
			int pDistance = (int)p.getLocation().distance(baseLoc);
			if(pDistance <= lossDistance) {
				if(pDistance <= distance) {
					p.sendMessage(str);
					sendCnt += 1;
				} 
			}
		}
		
		return sendCnt;
	}*/
	
	public int sendDistanceMsg(List<Player> playerList, Player p, String str, int distance, boolean useFormat) {
		if(isMute(p)) return 0;
		int sendCnt = 0;
		String logStr = p.getName()+" : "+str+"§f[ §a"+(server.playerList.containsKey(p.getName()) ? server.playerList.get(p.getName()) : "모름")+" §f]";
		Bukkit.getLogger().info(logStr);
		PlayerData pData = server.egDM.getPlayerData(p.getName());
		if(pData != null) {
			pData.appendChat(logStr);
		}
		if(opChat.contains(p.getName())) {
			for(Player tmpP : Bukkit.getServer().getOnlinePlayers()) {
				if(tmpP.isOp()) {
					tmpP.sendMessage("§f[ §c오피챗 §f] "+p.getName()+" §7: "+str);
					sendCnt++;
				}
			}
		}else {
			if(useFormat) str = applyFormat(p, str);
			for(Player tmpP : playerList) {
				int pDistance = (int)tmpP.getLocation().distance(p.getLocation());
				if(pDistance <= distance) {
					tmpP.sendMessage(str);
					sendCnt += 1;
				}
			}
			for(String s : socialSpyList) {
				Player sP = Bukkit.getPlayer(s);
				if(existPlayer(sP)) {
					sP.sendMessage(str+"§f[ §a"+(server.playerList.containsKey(p.getName()) ? server.playerList.get(p.getName()) : "모름")+" §f]");
				}
			}
		}
		return sendCnt;
	}
	
	public int sendDistanceMsgToStringList(List<String> nameList, Player p, String str, int distance, int y,boolean useFormat) {
		if(isMute(p)) return 0;
		int sendCnt = 0;
		String logStr = p.getName()+" : "+str+"§f[ §a"+(server.playerList.containsKey(p.getName()) ? server.playerList.get(p.getName()) : "모름")+" §f]";
		Bukkit.getLogger().info(logStr);
		PlayerData pData = server.egDM.getPlayerData(p.getName());
		if(pData != null) {
			pData.appendChat(logStr);
		}
		if(opChat.contains(p.getName())) {
			for(Player tmpP : Bukkit.getServer().getOnlinePlayers()) {
				if(tmpP.isOp()) {
					tmpP.sendMessage("§f[ §c오피챗 §f] "+p.getName()+" §7: "+str);
					sendCnt++;
				}
			}
		}else {
			if(useFormat) str = applyFormat(p, str);
			for(String tmpN : nameList) {
				Player tmpP = Bukkit.getPlayer(tmpN);
				if(tmpP == null) continue;
				int pDistance = (int)tmpP.getLocation().distance(p.getLocation());
				if(pDistance <= distance && Math.abs(p.getLocation().getY() - tmpP.getLocation().getY()) < y) {
					tmpP.sendMessage(str);
					sendCnt += 1;
					if(!noSoundList.contains(tmpN))
					tmpP.playSound(tmpP.getLocation(), Sound.BLOCK_STONE_BREAK, 1.0f, 1.3f);
				}
			}
			for(String s : socialSpyList) {
				Player sP = Bukkit.getPlayer(s);
				if(existPlayer(sP)) {
					sP.sendMessage(str+"§f[ §a"+(server.playerList.containsKey(p.getName()) ? server.playerList.get(p.getName()) : "모름")+" §f]");
				}
			}
		}		
		return sendCnt;
	}
	
	public int sendMessages(List<Player> playerList, Player p, String str, boolean useFormat) {
		if(isMute(p)) return 0;
		int sendCnt = 0;
		String logStr = p.getName()+" : "+str+"§f[ §a"+(server.playerList.containsKey(p.getName()) ? server.playerList.get(p.getName()) : "모름")+" §f]";
		Bukkit.getLogger().info(logStr);
		PlayerData pData = server.egDM.getPlayerData(p.getName());
		if(pData != null) {
			pData.appendChat(logStr);
		}
		if(opChat.contains(p.getName())) {
			for(Player tmpP : Bukkit.getServer().getOnlinePlayers()) {
				if(tmpP.isOp()) {
					tmpP.sendMessage("§f[ §c오피챗 §f] "+p.getName()+" §7: "+str);
					sendCnt++;
				}
			}
		}else {
			if(useFormat) str = applyFormat(p, str);
			for(Player tmpP : playerList) {
				tmpP.sendMessage(str);
				sendCnt += 1;
			}
			for(String s : socialSpyList) {
				Player sP = Bukkit.getPlayer(s);
				if(existPlayer(sP)) {
					sP.sendMessage(str+"§f[ §a"+(server.playerList.containsKey(p.getName()) ? server.playerList.get(p.getName()) : "모름")+" §f]");
				}
			}
		}
		return sendCnt;
	}
	
	public int sendMessagesToStringList(List<String> nameList, Player p, String str, boolean useFormat) {
		if(isMute(p)) return 0;
		int sendCnt = 0;
		String logStr = p.getName()+" : "+str+"§f[ §a"+(server.playerList.containsKey(p.getName()) ? server.playerList.get(p.getName()) : "모름")+" §f]";
		Bukkit.getLogger().info(logStr);
		PlayerData pData = server.egDM.getPlayerData(p.getName());
		if(pData != null) {
			pData.appendChat(logStr);
		}
		if(opChat.contains(p.getName())) {
			for(Player tmpP : Bukkit.getServer().getOnlinePlayers()) {
				if(tmpP.isOp()) {
					tmpP.sendMessage("§f[ §c오피챗 §f] "+p.getName()+" §7: "+str);
					sendCnt++;
				}
			}
		}else {
			if(useFormat) str = applyFormat(p, str);
			for(String tmpP : nameList) {
				Player t = Bukkit.getPlayer(tmpP);
				if(t != null) {
					t.sendMessage(str);
					sendCnt += 1;
					if(!noSoundList.contains(tmpP))
					t.playSound(t.getLocation(), Sound.BLOCK_STONE_BREAK, 1.0f, 1.3f);
				}
			}
			for(String s : socialSpyList) {
				Player sP = Bukkit.getPlayer(s);
				if(existPlayer(sP)) {
					sP.sendMessage(str+"§f[ §a"+(server.playerList.containsKey(p.getName()) ? server.playerList.get(p.getName()) : "모름")+" §f]");
				}
			}
		}
		return sendCnt;
	}
	
	public void sendPrivateMsg(Player p, Player t, String msg) {
		if(isMute(p)) return;
		String logStr = p.getName()+" : "+msg+"§f[ §a귓 §f]";
		Bukkit.getLogger().info(logStr);
		PlayerData pData = server.egDM.getPlayerData(p.getName());
		if(pData != null) {
			pData.appendChat(logStr);
		}
		p.sendMessage("§7[§d귓§7] §7[ §f"+p.getName()+" §7-> §f"+t.getName()+" §7] §e"+msg);
		t.sendMessage("§7[§d귓§7] §7[ §f"+p.getName()+" §7-> §f"+t.getName()+" §7] §e"+msg);
	}
	
	public void mute(String pName, int time) {
		Player p = Bukkit.getPlayer(pName);
			PlayerData pData = server.egDM.getPlayerData(pName);
			if(pData == null) return;
			pData.setMuteUntilTime(System.currentTimeMillis()+time*1000);
			
			if(existPlayer(p)) {
			p.sendMessage("§c당신은 채팅금지 되었습니다. 앞으로 "+time+"초 동안 채팅이 불가능합니다.");
		}
	}
	
	public void unMute(String pName) {
		Player p = Bukkit.getPlayer(pName);
			PlayerData pData = server.egDM.getPlayerData(pName);
			if(pData == null) return;
			pData.setMuteUntilTime(0);		
			
			if(existPlayer(p)) 
				p.sendMessage("§c당신은 채팅금지가 해제 되었습니다.");
	}
	
	public String setPrefix(String pName, String prefix, boolean saveFile) {
		prefix = prefix.replace("&", "§");
		prefixMap.put(pName, prefix);
		PlayerData pData = server.egDM.getPlayerData(pName);
		if(pData != null) {
			pData.setPrefix(prefix);
			if(saveFile) pData.saveData();
		}
		
		return prefix;
	}
	
	
	public String applyFormat(Player p, String str) {
		String prefix = prefixMap.get(p.getName());
		if(prefix != null) {
			return "§f[ §c"+prefix+" §f]"+" §7"+p.getName()+" >> "+str;
		}
		else if(p.isOp()) {
			return "§f[ §c오피 §f]"+" §7"+p.getName()+" >> "+str;
		}
		else {
			return "§f[ §6유저 §f]"+" §7"+p.getName()+" >> "+str;
		}
	}
}
