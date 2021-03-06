package Minigames.WarOfGod.Abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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

import EGServer.ServerManager.EGScheduler;
import Minigames.WarOfGod.WarOfGod;

public class Uros extends Ability {
	
	boolean isGod;
	List<SidebarString> strList = new ArrayList<SidebarString>(10);
	
	public Uros(WarOfGod wog, Player p) {
		super(wog, p);
		abilityName = "��ν�";
		cooldown = new MyCooldown(this);
		
		///��Ÿ�� ����
		cooldown_primarySkill = 60;
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
		p.sendMessage("��7���� �� ��c��ν�"
				+ "\n��7- ��6��Ŭ�� ��7: �� 8���� ����Ͽ� 5�ʰ� ���� ���°��˴ϴ�. ��6( ���� ���ð�  : 60��) "
				+ "\n��7- ��6�нú� ��7: �Ϲ� Ÿ�����δ� �˹��� ���� �ʽ��ϴ�.");
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
				tmpStr = "��4Main ��6"+nowCooldown_primarySkill;
			} else {
				tmpStr = "��aMain ��6Ready";
			}*/
			
			if(cooldown.getLeftCooldown("primary") > 0) {
				tmpStr = "��4Prim ��6"+cooldown.getLeftCooldown("primary");
			} else {
				tmpStr = "��aPrim ��6Ready";
			}
						
			line = new SidebarString(tmpStr);
			strList.add(line);		
	
			if(cooldown.getLeftCooldown("itemKit") > 0) {
				tmpStr = "��4Item ��6"+cooldown.getLeftCooldown("itemKit");
			} else {
				tmpStr = "��aItem ��6Ready";
			}		
			
			line = new SidebarString(tmpStr);
			strList.add(line);

			
			tmpStr = "��aPasv ��6Using";

			line = new SidebarString(tmpStr);
			strList.add(line);
			
			line = new SidebarString("---------------");
			strList.add(line);
			
			line = new SidebarString("��7���ºα��� : ��c"+wog.leftTime);
			strList.add(line);
			
			line = new SidebarString("---------------");
			strList.add(line);
			
			/*if(nowCooldown_secondarySkill > 0) {
				tmpStr = "��4Secd ��6"+nowCooldown_secondarySkill;
			} else {
				tmpStr = "��aSecd ��6Ready";
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
			
			if(wog.takeItem(p, Material.COBBLESTONE, 8)) {
				cooldown.setCooldown("primary");
				p.getWorld().playSound(p.getLocation(), Sound.BLOCK_NOTE_XYLOPHONE, 1.0f, 0.5f);
				EGScheduler sch = new EGScheduler(wog);
				sch.schTime = 5;
				p.setFlySpeed(0.1f);
				isGod = true;
				TitleAPI.sendFullTitle(p, 10,60, 20, "��c"+sch.schTime, "���� ��ȣ�� ����������");
				sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(wog.server, new Runnable() {
					public void run() {
						if(!wog.ingamePlayer.contains(p.getName())) Bukkit.getScheduler().cancelTask(sch.schId);
						if(sch.schTime > 1) {
							TitleAPI.sendFullTitle(p, 10,60, 20, "��c"+--sch.schTime, "���� ��ȣ�� ����������");
						} else {
							sch.cancelTask(true);
							isGod = false;
							TitleAPI.sendFullTitle(p, 10,60, 20, "", "���� ��ȣ�� �Ҿ����ϴ�.");
						}
					}
				}, 20l, 20l);
			} else {
				TitleAPI.sendFullTitle(p, 10,60, 20, "", "���� �����մϴ�.");
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
			}
				
		} else {
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
		}
	}
	
	@Override
	public void onEntityDamaged(EntityDamageEvent e) {
		Player p = (Player) e.getEntity();
		if(isGod) {
			e.setCancelled(true);
			p.playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1.2f, 1.8f);
		}
	}
	
	@Override
	public void onHitted(EntityDamageByEntityEvent e) {
		Player p = (Player) e.getEntity();
		if (e.getCause() == DamageCause.ENTITY_ATTACK || e.getCause() == DamageCause.PROJECTILE) {
			p.damage(e.getDamage());
			e.setCancelled(true);
		}
	}
}
