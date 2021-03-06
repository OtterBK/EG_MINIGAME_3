package Minigames.WarOfGod.Abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
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

public class Gaia extends Ability{
	
	List<SidebarString> strList = new ArrayList<SidebarString>(10);
	
	public Gaia(WarOfGod wog, Player p) {
		super(wog, p);
		abilityName = "���̾�";
		cooldown = new MyCooldown(this);
		
		///��Ÿ�� ����
		cooldown_primarySkill = 10;
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
		p.sendMessage("��7������ �� ��c���̾�"
				+ "\n��7- ��6��Ŭ�� ��7: �ں��� 1���� �� 3���� ��ȯ�մϴ�. �ѹ��� �ִ� 10������ ��ȯ �����մϴ�. ��6( ���� ���ð�  : 10��)"
				//+ "��f- ��6��Ŭ�� ��7: "
				+ "\n��f- ��6�нú� ��7: ���絥������ ���� �ʽ��ϴ�.");	
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
			
			if(wog.hasItem(p, Material.COBBLESTONE, 1)) {
				cooldown.setCooldown("primary");
				for(int i = 0; i < 10; i++) {
					if(wog.takeItem(p, Material.COBBLESTONE, 1)) {
						p.getInventory().addItem(new ItemStack(Material.DIRT, 3));
						TitleAPI.sendFullTitle(p, 10,60, 20, "", "���� ������ ��ȯ�Ͽ����ϴ�.");
						p.getWorld().playSound(p.getLocation(), Sound.BLOCK_NOTE_XYLOPHONE, 1.0f, 0.5f);
					}
				}
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
		if(e.getCause() == DamageCause.FALL) {
			e.setCancelled(true);
		}
	}

}
