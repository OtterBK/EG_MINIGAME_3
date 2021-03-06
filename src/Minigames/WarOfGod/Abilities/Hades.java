package Minigames.WarOfGod.Abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;

import com.coloredcarrot.api.sidebar.Sidebar;
import com.coloredcarrot.api.sidebar.SidebarString;
import com.connorlinfoot.titleapi.TitleAPI;

import Minigames.WarOfGod.WarOfGod;
import Utility.MyUtility;

public class Hades extends Ability {
	
	List<SidebarString> strList = new ArrayList<SidebarString>(10);
	ItemStack backupItem[];
	ItemStack backupArmor[];

	public Hades(WarOfGod wog, Player p) {
		super(wog, p);
		abilityName = "하데스";
		cooldown = new MyCooldown(this);

		/// 쿨타임 설정
		cooldown_primarySkill = 70;
		cooldown_secondarySkill = 100;
		cooldown_passsive = 0;

		String team = wog.getTeam(p);
		SidebarString line1 = new SidebarString("");
		sidebar = new Sidebar((team.equalsIgnoreCase("BLUE") ? (ChatColor.BLUE + "" + ChatColor.BOLD)
				: (ChatColor.RED + "" + ChatColor.BOLD)) + abilityName, wog.server, 72000, line1);
		sidebar = new Sidebar((team.equalsIgnoreCase("BLUE") ? (ChatColor.BLUE+""+ChatColor.BOLD) : (ChatColor.RED+""+ChatColor.BOLD) )+abilityName,wog.server, 72000, line1);
		Team scoreBoardTeam = sidebar.getTheScoreboard().registerNewTeam("customTeam");
		//team.setOption(Option.NAME_TAG_VISIBILITY, OptionStatus.NEVER);
		scoreBoardTeam.setOption(Option.COLLISION_RULE, OptionStatus.NEVER);
		scoreBoardTeam.addEntry(p.getName());
	}

	public void helpMsg(Player p) {
		p.sendMessage("§7죽음의 신 §c하데스" 
				+ "\n§7- §6좌클릭 §7: 코블스톤 8개를 소비하여 5칸 이내 자신하고 가장 가까운 적과 함께 나락으로 떨어집니다. (재사용 대기시간 : 70초)"
				+ "\n§7- §6우클릭 §7: 코블스톤 18개를 소비하여 5칸 이내 적을 나락으로 떨어뜨립니다. (재사용 대기시간 : 100초)"
		// + "§f- §6우클릭 §7: "
				+ "\n§7- §6패시브 §7: 사망하여도 85% 확률로 아이템을 잃지 않습니다.");
	}

	@Override
	public void setUI() {
		Player p = Bukkit.getPlayer(playerName);
		if (!exitsPlayer(p))
			return;
		if (wog.gameStep == 3) {
			String team = wog.getTeam(playerName);

			strList.clear();

			SidebarString line;
			String tmpStr;

			line = new SidebarString("---------------");
			strList.add(line);

			line = new SidebarString(
					ChatColor.GREEN + "HP " + ChatColor.YELLOW + (int) p.getHealth() + "/" + (int) p.getMaxHealth());
			strList.add(line);

			line = new SidebarString("---------------");
			strList.add(line);

			/*
			 * if(nowCooldown_primarySkill > ) { tmpStr =
			 * "§4Main §6"+nowCooldown_primarySkill; } else { tmpStr = "§aMain §6Ready"; }
			 */

			if(cooldown.getLeftCooldown("primary") > 0) {
				tmpStr = "§4Prim §6"+cooldown.getLeftCooldown("primary");
			} else {
				tmpStr = "§aPrim §6Ready";
			}		
			
			line = new SidebarString(tmpStr);
			strList.add(line);
					
			if(cooldown.getLeftCooldown("secondary") > 0) {
				tmpStr = "§4Secd §6"+cooldown.getLeftCooldown("secondary");
			} else {
				tmpStr = "§aSecd §6Ready";
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

			line = new SidebarString("§7무승부까지 : §c" + wog.leftTime);
			strList.add(line);

			line = new SidebarString("---------------");
			strList.add(line);

			/*
			 * if(nowCooldown_secondarySkill > 0) { tmpStr =
			 * "§4Secd §6"+nowCooldown_secondarySkill; } else { tmpStr = "§aSecd §6Ready"; }
			 */

			// line = new SidebarString(tmpStr);
			// strList.add(line);

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
				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GHAST_SCREAM, 1.5f, 0.5f);
				String maxName = null;
				for(String tName : (wog.getTeam(playerName).equalsIgnoreCase("BLUE") ? wog.redTeam.teamList : wog.blueTeam.teamList)) {
					Player t = Bukkit.getPlayer(tName);
					if(exitsPlayer(t)){
						double dis = t.getLocation().distance(p.getLocation());
						if(dis <= 3) {
							if(maxName == null) {
								maxName = tName;
							} else {
								if(Bukkit.getPlayer(tName).getLocation().distance(p.getLocation()) > dis) {
									maxName = tName;
								}
							}
						}
					}
				}
				Location l;
				if(maxName != null) {
					Player t = Bukkit.getPlayer(maxName);
					l = t.getLocation();
					l.setY(-1);
					t.teleport(l, TeleportCause.PLUGIN);
					TitleAPI.sendFullTitle(t, 10,60, 20, "", "나락으로 떨어집니다.");
				}
							
				l = p.getLocation();
				l.setY(-1);
				p.teleport(l, TeleportCause.PLUGIN);
				TitleAPI.sendFullTitle(p, 10,60, 20, "", "나락으로 떨어집니다.");
				
			} else {
				TitleAPI.sendFullTitle(p, 10,60, 20, "", "돌이 부족합니다.");
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
			}					
		} else {
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
		}
	}
	
	public void secondarySkill(){
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p)) return;
		if(cooldown.checkCooldown("secondary") && !p.isDead()){
			
			if(wog.takeItem(p, Material.COBBLESTONE, 18)) {
				cooldown.setCooldown("secondary");
				TitleAPI.sendFullTitle(p, 10,60, 20, "", "나락과 통로를 열었습니다.");
				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GHAST_SCREAM, 1.5f, 0.5f);
				for(String tName : (wog.getTeam(playerName).equalsIgnoreCase("BLUE") ? wog.redTeam.teamList : wog.blueTeam.teamList)) {
					Player t = Bukkit.getPlayer(tName);
					if(exitsPlayer(t)){
						Location l = t.getLocation();
						if(l.distance(p.getLocation()) <= 3) {
							l.setY(-1);
							t.teleport(l, TeleportCause.PLUGIN);
							TitleAPI.sendFullTitle(t, 10,60, 20, "", "나락으로 떨어집니다.");
						}
					}
				}								
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
		Player p = e.getEntity();
		for(String tName : (wog.getTeam(p.getName()).equalsIgnoreCase("BLUE") ?
				wog.blueTeam.teamList : wog.redTeam.teamList)) {
			if(wog.playerMap.get(tName).ability.abilityName.equalsIgnoreCase("아테나")) {
				Player t = Bukkit.getPlayer(tName);
				if(exitsPlayer(t)) {
					t.setLevel(t.getLevel()+1);
					TitleAPI.sendFullTitle(t, 10,60, 20, "", t.getName()+" 님의 사망으로 레벨상승");
					p.getWorld().playSound(t.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
				}
			}		
		}
		
		if(MyUtility.getRandom(1, 100) <= 85) {
			e.getDrops().clear();
			backupItem = p.getInventory().getContents();
			backupArmor = p.getInventory().getArmorContents();
		}	
	}
	
	@Override
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		if(backupItem != null) {
			Player p = e.getPlayer();
			Bukkit.getScheduler().scheduleSyncDelayedTask(wog.server, new Runnable() {
				public void run() {
					if(wog.ingamePlayer.contains(p.getName()) && exitsPlayer(p)) {
						p.getInventory().setContents(backupItem);
						p.getInventory().setArmorContents(backupArmor);
						TitleAPI.sendFullTitle(p, 10,60, 20, "", "아이템 복구 성공");
						backupItem = null;
						backupArmor = null;
					}
				}
			}, 10l);
		}
	}

}