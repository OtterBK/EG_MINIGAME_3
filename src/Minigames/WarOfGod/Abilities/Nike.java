package Minigames.WarOfGod.Abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_12_R1.generator.InternalChunkGenerator;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;

import com.coloredcarrot.api.sidebar.Sidebar;
import com.coloredcarrot.api.sidebar.SidebarString;
import com.connorlinfoot.titleapi.TitleAPI;

import Minigames.WarOfGod.WarOfGod;
import Utility.MyUtility;

public class Nike extends Ability{

	List<SidebarString> strList = new ArrayList<SidebarString>(10);
	
	public Nike(WarOfGod wog, Player p) {
		super(wog, p);
		abilityName = "니케";
		cooldown = new MyCooldown(this);
		
		///쿨타임 설정
		cooldown_primarySkill = 0;
		cooldown_secondarySkill = 0;
		cooldown_passsive = 0;
		

		String team = wog.getTeam(p);
		SidebarString line1 = new SidebarString("");
		sidebar = new Sidebar((team.equalsIgnoreCase("BLUE") ? (ChatColor.BLUE+""+ChatColor.BOLD) : (ChatColor.RED+""+ChatColor.BOLD) )+abilityName,wog.server, 72000, line1);
		
		p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 72000, 0));
		sidebar = new Sidebar((team.equalsIgnoreCase("BLUE") ? (ChatColor.BLUE+""+ChatColor.BOLD) : (ChatColor.RED+""+ChatColor.BOLD) )+abilityName,wog.server, 72000, line1);
		Team scoreBoardTeam = sidebar.getTheScoreboard().registerNewTeam("customTeam");
		//team.setOption(Option.NAME_TAG_VISIBILITY, OptionStatus.NEVER);
		scoreBoardTeam.setOption(Option.COLLISION_RULE, OptionStatus.NEVER);
		scoreBoardTeam.addEntry(p.getName());
	}
	
	public void helpMsg(Player p) {
		p.sendMessage("§7정복과 승리의 신 §c니케"
				//+ "§7- §6좌클릭 §7: 코블스톤 1개당 흙 3개로 변환합니다. 한번에 최대 10개까지 변환 가능합니다."
				//+ "§f- §6우클릭 §7: "
				+ "\n§f- §6패시브 §7: 적을 처치할 시 체력을 전부 회복합니다, 기본 이동속도 버프를 받습니다.");	
	}	
	
	@Override
	public void setUI() {
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p)) return;
		if(wog.ingame) {
			String team = wog.getTeam(playerName);
			
			strList.clear();
			
			SidebarString line;
			String tmpStr = null;
			
			line = new SidebarString("---------------");
			strList.add(line);
			
			line = new SidebarString(ChatColor.GREEN+"HP "+ChatColor.YELLOW+(int)p.getHealth()+"/"+(int)p.getMaxHealth());
			strList.add(line);
			
			line = new SidebarString("---------------");
			strList.add(line);
			
			/*if(nowCooldown_primarySkill > ) {
				tmpStr = "§4Main §6"+nowCooldown_primarySkill;
			} else {
				tmpStr = "§aMain §6Ready";
			}*/
			
			/*if(nowCooldown_primarySkill > 0) {
				tmpStr = "§4Prim §6"+nowCooldown_primarySkill;
			} else {
				tmpStr = "§aPrim §6Ready";
			}*/
			
			
			//line = new SidebarString(tmpStr);
			//strList.add(line);
			
			/*if(nowCooldown_secondarySkill > 0) {
				tmpStr = "§4Secd §6"+nowCooldown_secondarySkill;
			} else {
				tmpStr = "§aSecd §6Ready";
			}*/
			if(cooldown.getLeftCooldown("itemKit") > 0) {
				tmpStr = "§4Item §6"+cooldown.getLeftCooldown("itemKit");
			} else {
				tmpStr = "§aItem §6Ready";
			}	
			line = new SidebarString(tmpStr);
			strList.add(line);
					
			tmpStr = "§aPasv §6Using";

			line = new SidebarString(tmpStr);
			strList.add(line);

			line = new SidebarString("---------------");
			strList.add(line);
			
			line = new SidebarString("§7무승부까지 : §c"+wog.leftTime);
			strList.add(line);
			
			line = new SidebarString("---------------");
			strList.add(line);
					
			sidebar.setEntries(strList);
			sidebar.update();
			sidebar.showTo(p);
		} else {
			sidebar.hideFrom(p);
		}
	}
	
	@Override
	public void onKillPlayer(Player victim) {
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p)) return;
		
		MyUtility.healUp(p);
		TitleAPI.sendFullTitle(p, 10,60, 20, "", "적을 정복하여 체력을 회복합니다.");
	}
	
	@Override
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p)) return;
		Bukkit.getScheduler().scheduleSyncDelayedTask(wog.server, new Runnable() {
			public void run() {
				if(wog.ingamePlayer.contains(playerName)) 
					p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 72000, 0));
			}
		},60l);
	}
	
}
