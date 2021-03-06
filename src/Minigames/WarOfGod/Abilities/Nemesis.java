package Minigames.WarOfGod.Abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;
import org.golde.bukkit.corpsereborn.listeners.PlayerDeath;

import com.coloredcarrot.api.sidebar.Sidebar;
import com.coloredcarrot.api.sidebar.SidebarString;
import com.connorlinfoot.titleapi.TitleAPI;

import Minigames.WarOfGod.WarOfGod;

public class Nemesis extends Ability{
	
	List<SidebarString> strList = new ArrayList<SidebarString>(10);
	//�׸޽ý� ����
	String lastKiller = "";
	PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, 60, 4);
	PotionEffect buff1 = new PotionEffect(PotionEffectType.SPEED, 200, 1);
	PotionEffect buff2 = new PotionEffect(PotionEffectType.JUMP, 200, 1);
	PotionEffect buff3 = new PotionEffect(PotionEffectType.REGENERATION, 200, 1);
	
	public Nemesis(WarOfGod wog, Player p) {
		super(wog, p);
		abilityName = "�׸޽ý�";
		cooldown = new MyCooldown(this);
		
		///��Ÿ�� ����
		cooldown_primarySkill = 0;
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
		p.sendMessage("��7������ ���� ��c�׸޽ý�"
				+ "\n��7- ��6�нú� ��7: �ڽ��� ���� ���� 3�ʰ� ���� 5������ �޽��ϴ�."
				+ "\n��7���� �ڽ��� ���������� ���� ���� �ڽ��� óġ�� �� 10�ʰ�"
				+ "\n��7�ż�2, ������ȭ2, ���2 ������ �޽��ϴ�."
				+ "\n��7������ �����ϸ� ���������� ���� ���� �ʱ�ȭ�˴ϴ�.");
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
		
			if(cooldown.getLeftCooldown("itemKit") > 0) {
				tmpStr = "��4Item ��6"+cooldown.getLeftCooldown("itemKit");
			} else {
				tmpStr = "��aItem ��6Ready";
			}		
			
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
	
	
	@Override
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		Player k = (Player) p.getKiller();
		if(k != null) {
			lastKiller = k.getName();
			k.addPotionEffect(slow);
		}
	}
	
	@Override
	public void onKillPlayer(Player victim) {
		if(victim == null) return;
		Player p = (Player) Bukkit.getPlayer(playerName);
		if(exitsPlayer(p)) {
			if(victim.getName().equalsIgnoreCase(lastKiller)) {
				TitleAPI.sendFullTitle(p, 0, 100, 0, "��c��l���� ����", "��a��l������ �޽��ϴ�.");
				p.addPotionEffect(buff1);
				p.addPotionEffect(buff2);
				p.addPotionEffect(buff3);
				lastKiller = "";
			}
		}
		
	}
		
}
