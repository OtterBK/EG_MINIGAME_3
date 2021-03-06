package Minigames.WarOfGod.Abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
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

public class Nicks extends Ability{

	List<SidebarString> strList = new ArrayList<SidebarString>(10);
	
	public Nicks(WarOfGod wog, Player p) {
		super(wog, p);
		abilityName = "�н�";
		cooldown = new MyCooldown(this);
		
		///��Ÿ�� ����
		cooldown_primarySkill = 80;
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
		p.sendMessage("��7���� ���� ��c�н�"
				+ "\n��7- ��6��Ŭ�� ��7: �ں��� 12���� �Ҹ��Ͽ� 8ĭ�� ���� 10�ʰ� �Ǹ���¿� ���߸��ϴ�. ��6( ���� ���ð�  : 80��) "
				+ "\n��7- ��6�нú� ��7: ������ ����� Ÿ���� ����� 5�ʰ� �Ǹ���¿� �����ϴ�.");
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
	
	
	public void secondarySkill(){
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p)) return;
		if(cooldown.checkCooldown("primary") && !p.isDead()){	
			
			if(wog.takeItem(p, Material.COBBLESTONE, 12)) {
				cooldown.setCooldown("primary");
				
				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BAT_DEATH, 1.0f, 0.1f);
				TitleAPI.sendFullTitle(p, 10,60, 20, "", "���� ���� ����� �̲��ϴ�.");
				
				List<Player> tmpList = MyUtility.stringListToPlayer(wog.getEnemyList(wog.getTeam(p.getName())));
				
				for(Player t : tmpList) {
					if(t.getLocation().distance(p.getLocation()) <= 8) {
						t.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 0));
						TitleAPI.sendFullTitle(t, 10,60, 20, "", "�н��� ���� ���� ����� ���������ϴ�.");
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
	public void onHitPlayer(EntityDamageByEntityEvent e) {
		Player d = (Player) e.getDamager();
		ItemStack item = d.getInventory().getItemInMainHand();
		if(item == null) return;
		if(item.getType() == Material.BLAZE_ROD) {
			Player v = (Player) e.getEntity();
			v.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 0));
			v.getWorld().playSound(v.getLocation(), Sound.ENTITY_GHAST_SCREAM, 1.0f, 2.5f);
		}	
	}	

	
}
