package Minigames.WarOfGod.Abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;

import com.coloredcarrot.api.sidebar.Sidebar;
import com.coloredcarrot.api.sidebar.SidebarString;
import com.connorlinfoot.titleapi.TitleAPI;

import Minigames.WarOfGod.WarOfGod;
import Utility.MyUtility;

public class Poseidon extends Ability{

	List<SidebarString> strList = new ArrayList<SidebarString>(10);
	
	public Poseidon(WarOfGod wog, Player p) {
		super(wog, p);
		abilityName = "포세이돈";
		cooldown = new MyCooldown(this);
		
		///쿨타임 설정
		cooldown_primarySkill = 30;
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
	}
	
	public void helpMsg(Player p) {
		p.sendMessage("§7바다의신 §c포세이돈"
				+ "\n§7- §6좌클릭 §7: 코블스톤1 개를 소모하여 바라보는 방향으로 타격 대상을 밀쳐냅니다. (블레이즈 막대로 타격하세요.) 그 후 자신의 위치에 물을 생성합니다. §6( 재사용 대기시간  : 30초) "
				+ "\n§7- §6패시브 §7: 물에서 33% 확률로 모든 데미지 회피, 익사하지 않음");	
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
			
			if(cooldown.getLeftCooldown("primary") > 0) {
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
			
			tmpStr = "§aPasv §6Using";

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
	
	@Override
	public void onEntityDamaged(EntityDamageEvent e) {
		Player p = (Player) e.getEntity();
		if(e.getCause() == DamageCause.DROWNING) {
			e.setCancelled(true);
		}
		if(p.getLocation().add(0,0,0).getBlock().getType() == Material.STATIONARY_WATER
				|| p.getLocation().add(0,0,0).getBlock().getType() == Material.WATER
				|| p.getLocation().add(0,1,0).getBlock().getType() == Material.STATIONARY_WATER
				|| p.getLocation().add(0,1,0).getBlock().getType() == Material.WATER) {
			if(MyUtility.getRandom(1, 100) <= 33) {
				TitleAPI.sendFullTitle(p, 10,60, 20, "", "데미지 회피");
				e.setCancelled(true);
			}
		}
	}
	
	@Override
	public void onHitPlayer(EntityDamageByEntityEvent e) {
		Player d = (Player) e.getDamager();
		ItemStack item = d.getInventory().getItemInMainHand();
		if(item == null) return;
		if(item.getType() == Material.BLAZE_ROD) {
			if(cooldown.checkCooldown("primary")) {
				if(wog.takeItem(d, Material.COBBLESTONE, 1)) {
					cooldown.setCooldown("primary");
					Player v = (Player) e.getEntity();
					v.setVelocity(d.getEyeLocation().getDirection().multiply(2.8f));
					d.getWorld().playSound(d.getLocation(), Sound.BLOCK_WATER_AMBIENT, 1.5F, 0.5F);
					d.getWorld().spawnParticle(Particle.WATER_SPLASH, d.getEyeLocation(), 20, 0.2F, 0.2f, 0.2f, 0.1f);
					TitleAPI.sendFullTitle(d, 10,60, 20, "", "파도를 일으켰습니다.");
					d.getLocation().add(0,1,0).getBlock().setType(Material.WATER);
				} else {
					TitleAPI.sendFullTitle(d, 10,60, 20, "", "돌이 부족합니다.");
				}
			} 
		}
	}
	
}
