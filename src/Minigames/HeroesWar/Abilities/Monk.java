package Minigames.HeroesWar.Abilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.coloredcarrot.api.sidebar.Sidebar;
import com.coloredcarrot.api.sidebar.SidebarString;
import com.connorlinfoot.actionbarapi.ActionBarAPI;

import EGServer.ServerManager.EGScheduler;
import Minigames.HeroesWar.HRWBase;
import Minigames.HeroesWar.Team;
import Utility.MyUtility;
import de.slikey.effectlib.effect.CylinderEffect;
import de.slikey.effectlib.effect.HelixEffect;

public class Monk extends Ability{

	List<SidebarString> strList = new ArrayList<SidebarString>(7);
	
	//��ȣ�� ����
	HashMap<String, Integer> guardList = new HashMap<String, Integer>();
	
	//��ȣ�� ����
	public int stat_barrierTime = 0;
	
	public Monk(HRWBase hrw, Player p, String abilityName, Team team) {
		super(hrw, p, abilityName, team);
		cooldown = new MyCooldown(this);
		health = 108;
		
		//������ ����
		dmg = 6;
		

		weapon = new ItemStack(Material.DIAMOND, 1);
		ItemMeta meta = weapon.getItemMeta();
		meta.setDisplayName("��7[ ��6"+abilityName+" ��7]");
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		meta.setUnbreakable(true);
		List<String> loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��6���ݷ� ��7: ��d"+dmg);
		loreList.add("");
		meta.setLore(loreList);
		weapon.setItemMeta(meta);
		
		///��Ÿ�� ����
		cooldown_primarySkill = 13;
		cooldown_secondarySkill = 11;
		cooldown_teritarySkill = 0;
		cooldown_ultimateSkill = 81;
		
		//ü�¼���

		//��Ŀ ���
		team.reduceAbilityList.add(this);
		
		String teamName = hrw.getTeam(p);
		SidebarString line1 = new SidebarString("");
		sidebar = new Sidebar((teamName.equalsIgnoreCase("BLUE") ? (ChatColor.BLUE+""+ChatColor.BOLD) : (ChatColor.RED+""+ChatColor.BOLD) )+abilityName, hrw.server, 72000, line1);
		
		setEquipment(p);
		
		makeStatItem();
		
	}
	
	public void helpMsg(Player p) {
		p.sendMessage("��6============= ��f[ ��b�ڰ� ��f] - ��e������ ��7: ��cDealer ��6=============\n"
				+ "��f- ��a�ֽ�ų ��7: ��d����\n��7���� 5ĭ������ �̵��մϴ�. ��c���� ������ ���մϴ�. 3�ʸ��� �ܿ� Ƚ���� 1�����մϴ�.(�ִ� 4ȸ)\n"
				+ "��f- ��a������ų ��7: ��d����\n��75������ �ð����� �̵��մϴ�. ��cü��, ��ġ�� 5�������� �����˴ϴ�.\n"
				+ "��f- ��2�ñر��f(��c���Ž� ��밡�ɡ�f) ��7: ��d������ź\n��72ĭ �������� �ٶ󺸴� �÷��̾� �Ǵ� �ڽ��� ��ġ�� ��ź�� �����մϴ�. 2.0���� �����մϴ�.\n"
				+ "��f- ��2�нú��f(��c���Ž� ��밡�ɡ�f) ��7: ��d��������\n��7�������� �� ������ �����ϴ�. ������ Ƚ���� ȸ���Ǵ� ������ 3�ʿ��� 2.4�ʷ� �����մϴ�.");	
	}
	
	@Override
	public void invenHelper(Player p) {
		p.openInventory(hrw.inven_desc_monk);
	}
	
	@Override
	public void updateStatItem() {
		if(statItem == null) return; 
		ItemMeta meta = statItem.getItemMeta();
		statLoreList.clear();
		statLoreList.add("");
		statLoreList.add("��7���� : ��a"+abilityName+"  ");
		statLoreList.add("");
		statLoreList.add("��7K/D : ��a"+hrwData.kill + "��f /��a "+hrwData.death+"  ");
		statLoreList.add("��7������ �� ���ط� : ��a"+hrwData.dealAmt+"  ");
		statLoreList.add("��7�ִ� ���� óġ : ��a"+hrwData.maxStackKill+"  ");
		statLoreList.add("");
		statLoreList.add("��7���� ���ط� : ��a"+hrwData.barrierAmt+"  ");
		statLoreList.add("��7�溮 Ȱ�� �ð� : ��a"+stat_barrierTime+"��"+"  ");
		statLoreList.add("");
		statLoreList.add("");
		meta.setLore(statLoreList);
		statItem.setItemMeta(meta);
	}
	
	public void setEquipment(Player p) {
		p.getInventory().setItem(0, weapon);
		p.getInventory().setHeldItemSlot(8);
		MyUtility.setMaxHealth(p, health);
		MyUtility.healUp(p);
		lastInvenMap = p.getInventory().getContents();
		lastArmorMap = p.getInventory().getArmorContents();
	}
	
	
	@Override
	public void setUI() {
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p)) return;
		if(hrw.ingame) {
			String team = hrw.getTeam(playerName);
			
			strList.clear();
			
			SidebarString line;
			String tmpStr;
			
			line = new SidebarString("---------------");
			strList.add(line);
			
			line = new SidebarString((isNoHeal() ? ChatColor.RED : ChatColor.GREEN)+"HP "+ChatColor.YELLOW+(int)p.getHealth()+"/"+(int)p.getMaxHealth());
			strList.add(line);
			
			line = new SidebarString((isNoMove() ? ChatColor.RED : ChatColor.GREEN)+"SPD "+ChatColor.YELLOW+MyUtility.getTwoRound(p.getWalkSpeed()*4));
			strList.add(line);
			
			line = new SidebarString((isNoAttack() ? ChatColor.RED : ChatColor.GREEN)+"K/D "+ChatColor.YELLOW+(int)hrwData.kill+"/"+(int)hrwData.death);
			strList.add(line);
			
			line = new SidebarString("---------------");
			strList.add(line);
			
			/*if(nowCooldown_primarySkill > ) {
				tmpStr = "��4Main ��6"+nowCooldown_primarySkill;
			} else {
				tmpStr = "��aMain ��6Ready";
			}*/
			
			if(isNoSkill()) {
				tmpStr = "��4Prim ��6Blocked";
			} else if(cooldown.getLeftCooldown("primary") > 0) {
				tmpStr = "��4Prim ��6"+cooldown.getLeftCooldown("primary");
			} else {
				tmpStr = "��aPrim ��6Ready";
			}
			
			
			line = new SidebarString(tmpStr);
			strList.add(line);
			
			if(isNoSkill()) {
				tmpStr = "��4Secd ��6Blocked";
			} else if(cooldown.getLeftCooldown("secondary") > 0) {
				tmpStr = "��4Secd ��6"+cooldown.getLeftCooldown("secondary");
			} else {
				tmpStr = "��aSecd ��6Ready";
			}
			
			
			line = new SidebarString(tmpStr);
			strList.add(line);
			
			if(isNoSkill()) {
				tmpStr = "��4Ulmt ��6Blocked";
			} else if(ultimate) {
				if(percentMode) {
					int percent = cooldown.getUltimatePercent();
					if(percent >= 100) {
						tmpStr = "��aUlmt ��6Ready";
					}else {
						tmpStr = "��4Ulmt ��6"+percent+"%";
					}
				}else {
					if(cooldown.getLeftCooldown("ultimate") > 0) {
						tmpStr = "��4Ulmt ��6"+cooldown.getLeftCooldown("ultimate");
					} else {
						tmpStr = "��aUlmt ��6Ready";
					}
				}			
			} else {
				tmpStr = "��4Ulmt ��6�����ʿ�";
			}
			
			line = new SidebarString(tmpStr);
			strList.add(line);

			
			if(isNoSkill()) {
				tmpStr = "��4Pasv ��6Blocked";
			} else if(passive) {
				tmpStr = "��aPasv ��6�����";
			} else {
				tmpStr = "��4Pasv ��6�����ʿ�";
			}

			line = new SidebarString(tmpStr);
			strList.add(line);

			line = new SidebarString("---------------");
			strList.add(line);
			
			if(team.equalsIgnoreCase("BLUE")) {
				if(hrw.blueTeam.leftCarrot > 0) {
					tmpStr = "��c���� ���� : ��e"+hrw.blueTeam.leftCarrot+"��";
					line = new SidebarString(tmpStr);
					strList.add(line);
				} else {
					
					String point1Team = null;
					String point2Team = null;
					String point3Team = null;
					if(hrw.point1.catchTeam.equalsIgnoreCase("BLUE")) point1Team = "��b��l���";
					else if(hrw.point1.catchTeam.equalsIgnoreCase("RED")) point1Team = "��4��l����";
					else point1Team = "��6��l�߸�";
							
					if(hrw.point2.catchTeam.equalsIgnoreCase("BLUE")) point2Team = "��b��l���";
					else if(hrw.point2.catchTeam.equalsIgnoreCase("RED")) point2Team = "��4��l����";
					else point2Team = "��6��l�߸�";
					
					if(hrw.point3.catchTeam.equalsIgnoreCase("BLUE")) point3Team = "��b��l���";
					else if(hrw.point3.catchTeam.equalsIgnoreCase("RED")) point3Team = "��4��l����";
					else point3Team = "��6��l�߸�";
					
					tmpStr = "��e��l���� ���� "+point1Team;
					line = new SidebarString(tmpStr);
					strList.add(line);
					tmpStr = "��f��l�߾� ���� "+point2Team;
					line = new SidebarString(tmpStr);
					strList.add(line);
					tmpStr = "��c��l�״� ���� "+point3Team;
					line = new SidebarString(tmpStr);
					strList.add(line);
				}
			} else {
				if(hrw.redTeam.leftCarrot > 0) {
					tmpStr = "��c���� ���� : ��e"+hrw.redTeam.leftCarrot+"��";
					line = new SidebarString(tmpStr);
					strList.add(line);
				} else {
					String point1Team = null;
					String point2Team = null;
					String point3Team = null;
					if(hrw.point1.catchTeam.equalsIgnoreCase("BLUE")) point1Team = "��b��l���";
					else if(hrw.point1.catchTeam.equalsIgnoreCase("RED")) point1Team = "��4��l����";
					else point1Team = "��6��l�߸�";
							
					if(hrw.point2.catchTeam.equalsIgnoreCase("BLUE")) point2Team = "��b��l���";
					else if(hrw.point2.catchTeam.equalsIgnoreCase("RED")) point2Team = "��4��l����";
					else point2Team = "��6��l�߸�";
					
					if(hrw.point3.catchTeam.equalsIgnoreCase("BLUE")) point3Team = "��b��l���";
					else if(hrw.point3.catchTeam.equalsIgnoreCase("RED")) point3Team = "��4��l����";
					else point3Team = "��6��l�߸�";
					
					tmpStr = "��e��l���� ���� "+point1Team;
					line = new SidebarString(tmpStr);
					strList.add(line);
					tmpStr = "��f��l�߾� ���� "+point2Team;
					line = new SidebarString(tmpStr);
					strList.add(line);
					tmpStr = "��c��l�״� ���� "+point3Team;
					line = new SidebarString(tmpStr);
					strList.add(line);
				}
			}
			
			line = new SidebarString("---------------");
			strList.add(line);
					
			sidebar.setEntries(strList);
			sidebar.update();
			sidebar.showTo(p);
			ccUI(p);
		} else {
			sidebar.hideFrom(p);
			Bukkit.getLogger().info("hiding");
		}
	}
	
	@Override
	public void setUI_Ulaf() {
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p)) return;
		if(hrw.ingame) {
			String team = hrw.getTeam(playerName);
			
			strList.clear();
			
			SidebarString line;
			String tmpStr;
			
			line = new SidebarString("---------------");
			strList.add(line);
			
			line = new SidebarString((isNoHeal() ? ChatColor.RED : ChatColor.GREEN)+"HP "+ChatColor.YELLOW+(int)p.getHealth()+"/"+(int)p.getMaxHealth());
			strList.add(line);
			
			line = new SidebarString((isNoMove() ? ChatColor.RED : ChatColor.GREEN)+"SPD "+ChatColor.YELLOW+MyUtility.getTwoRound(p.getWalkSpeed()*4));
			strList.add(line);
			
			line = new SidebarString((isNoAttack() ? ChatColor.RED : ChatColor.GREEN)+"K/D "+ChatColor.YELLOW+(int)hrwData.kill+"/"+(int)hrwData.death);
			strList.add(line);
			
			line = new SidebarString("---------------");
			strList.add(line);
			
			/*if(nowCooldown_primarySkill > ) {
				tmpStr = "��4Main ��6"+nowCooldown_primarySkill;
			} else {
				tmpStr = "��aMain ��6Ready";
			}*/
			
			if(isNoSkill()) {
				tmpStr = "��4Prim ��6Blocked";
			} else if(cooldown.getLeftCooldown("primary") > 0) {
				tmpStr = "��4Prim ��6"+cooldown.getLeftCooldown("primary");
			} else {
				tmpStr = "��aPrim ��6Ready";
			}
			
			
			line = new SidebarString(tmpStr);
			strList.add(line);
			
			if(isNoSkill()) {
				tmpStr = "��4Secd ��6Blocked";
			} else if(cooldown.getLeftCooldown("secondary") > 0) {
				tmpStr = "��4Secd ��6"+cooldown.getLeftCooldown("secondary");
			} else {
				tmpStr = "��aSecd ��6Ready";
			}
			
			
			line = new SidebarString(tmpStr);
			strList.add(line);
			
			if(isNoSkill()) {
				tmpStr = "��4Ulmt ��6Blocked";
			} else if(ultimate) {
				if(percentMode) {
					int percent = cooldown.getUltimatePercent();
					if(percent >= 100) {
						tmpStr = "��aUlmt ��6Ready";
					}else {
						tmpStr = "��4Ulmt ��6"+percent+"%";
					}
				}else {
					if(cooldown.getLeftCooldown("ultimate") > 0) {
						tmpStr = "��4Ulmt ��6"+cooldown.getLeftCooldown("ultimate");
					} else {
						tmpStr = "��aUlmt ��6Ready";
					}
				}			
			} else {
				tmpStr = "��4Ulmt ��6�����ʿ�";
			}
			
			line = new SidebarString(tmpStr);
			strList.add(line);

			
			if(isNoSkill()) {
				tmpStr = "��4Pasv ��6Blocked";
			} else if(passive) {
				tmpStr = "��aPasv ��6�����";
			} else {
				tmpStr = "��4Pasv ��6�����ʿ�";
			}

			line = new SidebarString(tmpStr);
			strList.add(line);

			line = new SidebarString("---------------");
			strList.add(line);
			
			String point1Team = null;
			if(hrw.point1.catchTeam.equalsIgnoreCase("BLUE")) point1Team = "��b��l���";
			else if(hrw.point1.catchTeam.equalsIgnoreCase("RED")) point1Team = "��4��l����";
			else point1Team = "��6��l�߸�";
			
			tmpStr = "��e��l������ "+point1Team;
			line = new SidebarString(tmpStr);
			strList.add(line);
			
			line = new SidebarString("---------------");
			strList.add(line);
					
			sidebar.setEntries(strList);
			sidebar.update();
			sidebar.showTo(p);
			ccUI(p);
		} else {
			sidebar.hideFrom(p);
			Bukkit.getLogger().info("hiding");
		}
	}
	
	public int calcDamage(Player victim , Player damager, int damage) {
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p)) return -1;
		if(guardList.containsKey(victim.getName())) {
			int leftAmt = guardList.get(victim.getName());
			leftAmt -= damage;
			if(leftAmt <= 0) {
				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_DEATH, 1.0f, 2.5f);
				guardList.remove(p.getName());
			} else {
				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GUARDIAN_FLOP, 1.0f, 2.5f);
				guardList.put(victim.getName(), leftAmt);
			}
			hrwData.barrierAmt += damage;
			return 0;
		}
		return -1;
	}
	
	private void giveShield(Player p, int time, int amt) {	
		
		guardList.put(p.getName(), amt);
		
		stat_barrierTime += time;
		
		EGScheduler sch = new EGScheduler(hrw);
		sch.schTime = time*2;
		sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(hrw.server, new Runnable() {
			public void run() {
				if(!guardList.containsKey(p.getName())) {
					Bukkit.getScheduler().cancelTask(sch.schId);
				} else if(sch.schTime > 1) {
					sch.schTime -= 1;
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERPEARL_THROW, 1.0f, 0.1f);
					CylinderEffect particle = new CylinderEffect(effectManager);
					particle.setEntity(p);
					particle.particle = Particle.REDSTONE;
					particle.color = Color.fromRGB(111, 0, 204);
					particle.period = 4;
					particle.particleCount = 4;
					particle.autoOrient = true;
					particle.iterations = 1;
					particle.height = 1.5f;
					particle.radius = 0.8f;
					particle.start();
				} else {
					sch.cancelTask(true);
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_SKELETON_DEATH, 1.0f, 2.5f);
					guardList.remove(p.getName());
				}
				
			}
		}, 0l, 10l);
	}

	public void primarySkill(){
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p)) return;
		if(!p.getInventory().getItemInMainHand().equals(weapon)) return;
		if(isNoSkill()) {
			ActionBarAPI.sendActionBar(p, "��2��l��ų ��� �Ұ� �����Դϴ�.", 40);
			return;
		}
		if(cooldown.checkCooldown("primary") && !p.isDead()){
			cooldown.setCooldown("primary");
			p.getWorld().playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_IRON, 1.0f, 0.45f);
			giveShield(p, 3, passive ? 40 : 25);
			
		} else {
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
		}
	}
	
	public void secondarySkill(){
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p)) return;
		if(!p.getInventory().getItemInMainHand().equals(weapon)) return;
		if(isNoSkill()) {
			ActionBarAPI.sendActionBar(p, "��2��l��ų ��� �Ұ� �����Դϴ�.", 40);
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
			return;
		}
		if(cooldown.checkCooldown("secondary") && !p.isDead()){		
			List<Player> hit = hrw.getCorsurPlayer(p, getTeamList(p.getName()), 40, false);
			if(hit != null) {
				cooldown.setCooldown("secondary");
				p.getWorld().playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_IRON, 1.0f, 0.45f);
				Player t = hit.get(0);
				giveShield(t, 3, passive ? 40 : 25);
			} else {
				ActionBarAPI.sendActionBar(p, "��2��l����� �������� �ʽ��ϴ�.", 40);
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
			}			
		} else {
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
		}
	}
	
	public void ultimateSkill(){
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p) || !p.isOnline()) return;
		
		if(!ultimate) {
			ActionBarAPI.sendActionBar(p, ChatColor.RED+""+ChatColor.BOLD+"���� �ñر⸦ �����ϼž��մϴ�.", 70);
			return;
		}
		
		if(cooldown.checkCooldown("Ultimate")){
			
			if(isNoSkill()) {
				ActionBarAPI.sendActionBar(p, "��2��l��ų ��� �Ұ� �����Դϴ�.", 45);
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
				return;
			}
			
			cooldown.setCooldown("Ultimate");
			
			int giveP = 0;
			for(Player t : getTeamList(p.getName())) {
				Bukkit.getLogger().info(t.getName());
				if(t.getLocation().distance(p.getLocation()) < 30) {
					giveP += 1;
					giveShield(t, 4, passive ? 30 : 15);
				}
			}
			
			HelixEffect particle = new HelixEffect(effectManager);
			particle.setEntity(p);
			particle.particleOffsetY = -0.5f;
			particle.particle = Particle.REDSTONE;
			particle.color = team.teamColor;
			particle.period = 30;
			particle.iterations = 3;
			particle.radius = 3f;
			particle.start();
		
			EGScheduler sch = new EGScheduler(hrw);
			sch.schTime = giveP;
			sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(hrw.server, new Runnable() {
				public void run() {
					if(sch.schTime > 0) {
						sch.schTime -= 1;
						p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WITCH_THROW, 1.0f, 0.1f);
					} else {
						sch.cancelTask(true);
					}
				}
			}, 0l, 1l);
			
		}else {
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
		}
	}
}
