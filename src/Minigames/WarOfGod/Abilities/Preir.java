package Minigames.WarOfGod.Abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;

import com.coloredcarrot.api.sidebar.Sidebar;
import com.coloredcarrot.api.sidebar.SidebarString;
import com.connorlinfoot.titleapi.TitleAPI;

import EGServer.ServerManager.EGScheduler;
import Minigames.WarOfGod.WarOfGod;

public class Preir extends Ability{
	
	List<SidebarString> strList = new ArrayList<SidebarString>(10);
	//네메시스 전용
	boolean using = false;
	
	public Preir(WarOfGod wog, Player p) {
		super(wog, p);
		abilityName = "프레이르";
		cooldown = new MyCooldown(this);
		
		///쿨타임 설정
		cooldown_primarySkill = 1;
		cooldown_secondarySkill = 0;
		cooldown_passsive = 0;
		
		
		
		String team = wog.getTeam(p);
		SidebarString line1 = new SidebarString("");
		sidebar = new Sidebar((team.equalsIgnoreCase("BLUE") ? (ChatColor.BLUE+""+ChatColor.BOLD) : (ChatColor.RED+""+ChatColor.BOLD) )+abilityName,wog.server, 72000, line1);
		sidebar = new Sidebar((team.equalsIgnoreCase("BLUE") ? (ChatColor.BLUE+""+ChatColor.BOLD) : (ChatColor.RED+""+ChatColor.BOLD) )+abilityName,wog.server, 72000, line1);
		Team scoreBoardTeam = sidebar.getTheScoreboard().registerNewTeam("customTeam");
		//team.setOption(Option.NAME_TAG_VISIBILITY, OptionStatus.NEVER);
		scoreBoardTeam.setOption(Option.COLLISION_RULE, OptionStatus.NEVER);
		scoreBoardTeam.addEntry(p.getName());
		
		preirTimer();
	}
	
	public void helpMsg(Player p) {
		p.sendMessage("§7풍요의 신 §c프레이르"
				+ "\n§7- §6좌클릭 §7: 코블스톤 1개를 소모하여 팀원의 배고픔을 닳게 하지 않습니다."
				+ "\n§7단, 자신이 사망할시 효과가 초기화됩니다. §6( 재사용 대기시간  : 1초)");
	}	
	
	public void preirTimer() {
		EGScheduler sch = new EGScheduler(wog);
		sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(wog.server, new Runnable() {
			public void run() {
				if(!wog.ingame) sch.cancelTask(true);
				if(using) {
					for(String myTeam : getMyTeam()) {
						Player t = Bukkit.getPlayer(myTeam);
						if(exitsPlayer(t) && wog.ingamePlayer.contains(myTeam)) {
							t.setFoodLevel(20);
						}
					}
				}
			}
		}, 0l, 100l);
	}
	
	@Override
	public void setUI() {
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p)) return;
		if(wog.gameStep == 3) {
			String team = wog.getTeam(playerName);

			strList.clear();
			
			SidebarString line;
			String tmpStr;
			
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
			if(using) {
				tmpStr = "§4Prim §6Using";
			} else if(cooldown.getLeftCooldown("primary") > 0) {
				tmpStr = "§4Prim §6"+cooldown.getLeftCooldown("primary");
			} else {
				tmpStr = "§aPrim §6Ready";
			}		
			
			line = new SidebarString(tmpStr);
			strList.add(line);
		
			if(cooldown.getLeftCooldown("itemKit") > 0) {
				tmpStr = "§4Item §6"+cooldown.getLeftCooldown("itemKit");
			} else {
				tmpStr = "§aItem §6Ready";
			}		
			
			line = new SidebarString(tmpStr);
			strList.add(line);
			
			line = new SidebarString("---------------");
			strList.add(line);
			
			line = new SidebarString("§7무승부까지 : §c"+wog.leftTime);
			strList.add(line);
			
			line = new SidebarString("---------------");
			strList.add(line);
			
			/*if(nowCooldown_secondarySkill > 0) {
				tmpStr = "§4Secd §6"+nowCooldown_secondarySkill;
			} else {
				tmpStr = "§aSecd §6Ready";
			}*/
			
			
			//line = new SidebarString(tmpStr);
			//strList.add(line);
					
			sidebar.setEntries(strList);
			sidebar.update();
			sidebar.showTo(p);
		} else {
			sidebar.hideFrom(p);
		}
	}
	
	public void primarySkill(){
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p)) return;
		if(cooldown.checkCooldown("primary") && !p.isDead()){	
			
			if(wog.takeItem(p, Material.COBBLESTONE, 1)) {
				cooldown.setCooldown("primary");
				for(String myTeam : getMyTeam()) {
					Player t = Bukkit.getPlayer(myTeam);
					if(exitsPlayer(t) && wog.ingamePlayer.contains(myTeam)) {
						TitleAPI.sendFullTitle(t, 10, 60, 10, "§c§l풍요", "§e§l아군 프레이르의 능력에 의해 이제 배고프지 않습니다.");
					}
				}
				using = true;
			} else {
				TitleAPI.sendFullTitle(p, 10,60, 20, "", "돌이 부족합니다.");
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
			}
				
		} else {
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
		}
	}
	
	@Override
	public void onPlayerDeath(PlayerDeathEvent e) {
		using = false;
	}
		
}
