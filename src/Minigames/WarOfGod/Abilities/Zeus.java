package Minigames.WarOfGod.Abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;

import com.coloredcarrot.api.sidebar.Sidebar;
import com.coloredcarrot.api.sidebar.SidebarString;
import com.connorlinfoot.titleapi.TitleAPI;

import Minigames.WarOfGod.WarOfGod;
import Utility.MyUtility;

public class Zeus extends Ability{

	List<SidebarString> strList = new ArrayList<SidebarString>(10);
	
	public Zeus(WarOfGod wog, Player p) {
		super(wog, p);
		abilityName = "���콺";
		cooldown = new MyCooldown(this);
		
		///��Ÿ�� ����
		cooldown_primarySkill = 30;
		cooldown_secondarySkill = 70;
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
		p.sendMessage("��7������ �� ��c���콺"
				+ "\n��7- ��6��Ŭ�� ��7: �ں��� 5���� �ٶ󺸴� ���� ������ �����ϴ�.(�����Դ� ���� ����, �����Դ� 12������) ��6( ���� ���ð�  : 30��) "
				+ "\n��7- ��6��Ŭ�� ��7: �ں��� 15���� �Ҹ��Ͽ� �ֺ� 6ĭ�� ������ ������ �����ϴ�. ��6( ���� ���ð�  : 70��)");	
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
					
			if(cooldown.getLeftCooldown("secondary") > 0) {
				tmpStr = "��4Secd ��6"+cooldown.getLeftCooldown("secondary");
			} else {
				tmpStr = "��aSecd ��6Ready";
			}
			
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
			Block b = p.getTargetBlock(null, 15);
			if(b.getType() == Material.AIR) {
				TitleAPI.sendFullTitle(p, 10,40, 20, "", "�ٶ󺸴� ���� ���� ���ų� �ʹ� �ٴϴ�.");
			} else {
				if(wog.takeItem(p, Material.COBBLESTONE, 5)) {
					cooldown.setCooldown("primary");
					Location l = b.getLocation();
					List<String> enemyList = wog.getTeam(p.getName()).equalsIgnoreCase("BLUE") ?
							wog.redTeam.teamList : wog.blueTeam.teamList;
					for(String tName : wog.ingamePlayer) {
						Player t = Bukkit.getPlayer(tName);
						if(exitsPlayer(t)) {
							MyUtility.sendLightning(t, l);
							if(enemyList.contains(tName) && t.getLocation().distance(l) < 1.5f) {
								t.damage(12);
							}
						}
					}
					TitleAPI.sendFullTitle(p, 10,60, 20, "", "������ ����Ĩ�ϴ�.");
				} else {
					TitleAPI.sendFullTitle(p, 10,60, 20, "", "���� �����մϴ�.");
					p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
				}
			}		
		} else {
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
		}
	}
	
	public void secondarySkill(){
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p)) return;
		if(cooldown.checkCooldown("secondary") && !p.isDead()){
			
			if(wog.takeItem(p, Material.COBBLESTONE, 15)) {
				cooldown.setCooldown("secondary");
				TitleAPI.sendFullTitle(p, 10,60, 20, "", "������ ����Ĩ�ϴ�.");
				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_THUNDER, 1.5f, 1.0f);
				for(String tName : (wog.getTeam(playerName).equalsIgnoreCase("BLUE") ? wog.redTeam.teamList : wog.blueTeam.teamList)) {
					Player t = Bukkit.getPlayer(tName);
					if(exitsPlayer(t)){
						if(p.getLocation().distance(t.getLocation()) <= 6) {
							for(String tempName : wog.ingamePlayer) {
								Player temp = Bukkit.getPlayer(tempName);
								if(exitsPlayer(temp)) {
									MyUtility.sendLightning(temp, t.getLocation());
								}
							}
							t.damage(12);
						}					
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
	
}
