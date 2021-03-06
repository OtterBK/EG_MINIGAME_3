package Minigames.HeroesWar.Abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.coloredcarrot.api.sidebar.Sidebar;
import com.coloredcarrot.api.sidebar.SidebarString;
import com.connorlinfoot.actionbarapi.ActionBarAPI;

import Minigames.HeroesWar.HRWBase;
import Minigames.HeroesWar.Team;
import Utility.MyUtility;
import Utility.Vector3D;
import Utility.VirtualProjectile;
import de.slikey.effectlib.effect.HelixEffect;
import de.slikey.effectlib.effect.LineEffect;


public class Assassin extends Ability{
	
	List<SidebarString> strList = new ArrayList<SidebarString>(7);
	
	////��ؽ� ����
	public ItemStack ultimate_weapon;
	public boolean using_ultimate;
	
	public Assassin(HRWBase hrw, Player p, String abilityName, Team team) {
		super(hrw, p, abilityName, team);
		cooldown = new MyCooldown(this);
			
		//������ ����
		dmg = 9;
		
		weapon = new ItemStack(Material.DIAMOND_AXE, 1);
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
		
		ultimate_weapon = new ItemStack(Material.DIAMOND_SWORD, 1);
		meta = ultimate_weapon.getItemMeta();
		meta.setDisplayName("��7[ ��6"+abilityName+" ��7]");
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		meta.setUnbreakable(true);
		ultimate_weapon.setItemMeta(meta);
		
		///��Ÿ�� ����
		cooldown_primarySkill = 3;
		cooldown_secondarySkill = 6;
		cooldown_teritarySkill = 9;
		cooldown_ultimateSkill = 88;
		
		//Ư�� ��츸
		cooldown_passiveSkill = 2;
		
		//ü�¼���
		health = 60;
		
		String teamName = hrw.getTeam(p);
		SidebarString line1 = new SidebarString("");
		sidebar = new Sidebar((teamName.equalsIgnoreCase("BLUE") ? (ChatColor.BLUE+""+ChatColor.BOLD) : (ChatColor.RED+""+ChatColor.BOLD) )+abilityName, hrw.server, 72000, line1);
		
		setEquipment(p);
		
		p.setAllowFlight(true);
		
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
	public boolean checkWeapon(ItemStack[] contents) {
		for(ItemStack item : contents) {
			if(item == null) continue;
			if(item.getType() == weapon.getType() || item.getType() == ultimate_weapon.getType()) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void invenHelper(Player p) {
		p.openInventory(hrw.inven_desc_assasin);
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
	public void respawned() {
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p)) return;
		p.setAllowFlight(true);
		if(p.getInventory().getItem(0) == null) {	
			p.getInventory().setItem(0, weapon);
		}else if(p.getInventory().getItem(0).getType() != weapon.getType()) {
			p.getInventory().setItem(0, weapon);
		}
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
		statLoreList.add("��7���� �ߵ��� ���߸� �ð� : ��a"+hrwData.poisonTime+"��"+"  ");
		meta.setLore(statLoreList);
		statItem.setItemMeta(meta);
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
				tmpStr = "��4Tert ��6Blocked";
			} else if(cooldown.getLeftCooldown("teritary") > 0) {
				tmpStr = "��4Tert ��6"+cooldown.getLeftCooldown("teritary");
			} else {
				tmpStr = "��aTert ��6Ready";
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
				tmpStr = "��cPasv ��6Blocked";
			} else if(passive) {
				if(cooldown.getLeftCooldown("passive") > 0) {
					tmpStr = "��4Pasv ��6"+cooldown.getLeftCooldown("passive");
				} else {
					tmpStr = "��aPasv ��6Ready";
				}
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
				tmpStr = "��4Tert ��6Blocked";
			} else if(cooldown.getLeftCooldown("teritary") > 0) {
				tmpStr = "��4Tert ��6"+cooldown.getLeftCooldown("teritary");
			} else {
				tmpStr = "��aTert ��6Ready";
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
				tmpStr = "��cPasv ��6Blocked";
			} else if(passive) {
				if(cooldown.getLeftCooldown("passive") > 0) {
					tmpStr = "��4Pasv ��6"+cooldown.getLeftCooldown("passive");
				} else {
					tmpStr = "��aPasv ��6Ready";
				}
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
	
	public void primarySkill(){
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p)) return;
		if(!p.getInventory().getItemInMainHand().equals(weapon) && !p.getInventory().getItemInMainHand().equals(ultimate_weapon)) return;
		if(isNoSkill()) {
			ActionBarAPI.sendActionBar(p, "��2��l��ų ��� �Ұ� �����Դϴ�.", 40);
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
			return;
		}
		if(cooldown.checkCooldown("primary") && !p.isDead()){
			if(isNoMove()) {
				ActionBarAPI.sendActionBar(p, ChatColor.RED+""+ChatColor.BOLD+"�̵��Ұ� �����Դϴ�.", 40);
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
				return;
			}
			cooldown.setCooldown("primary");
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 2.0f, 4.0f);
			p.getWorld().spawnParticle(Particle.CLOUD, p.getLocation(), 2, 0.5F, 0.5f, 0.5f, 0.1f);
			
			Location l = p.getLocation();
			l.setPitch(0);
			Vector v = l.getDirection();
			v.multiply(-2);
			v.setY(0);
			p.setVelocity(v);
			
		} else {
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
		}
	}
	
	public void secondarySkill(){
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p)) return;
		if(!p.getInventory().getItemInMainHand().equals(weapon) && !p.getInventory().getItemInMainHand().equals(ultimate_weapon)) return;
		if(isNoSkill()) {
			ActionBarAPI.sendActionBar(p, "��2��l��ų ��� �Ұ� �����Դϴ�.", 40);
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
			return;
		}
		if(cooldown.checkCooldown("secondary") && !p.isDead()){
			cooldown.setCooldown("secondary");
			
			p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.2f, 2.0f);
			
			VirtualProjectile vp = new VirtualProjectile(hrw.server, p, MyUtility.stringListToPlayer(hrw.getEnemyList(p.getName())));
			vp.c_speed = 5f;
			vp.c_distance = 18;
			vp.c_upOffset = -0.5f;
			vp.c_time = 40;
			vp.c_removeOnHit = true;
			vp.c_pVerticalOffset = 0.5f;
			vp.c_pHorizenOffset = 0.5f;
			
			vp.setOnDuring(new Runnable() {
				public void run() {
					vp.projectileLoc.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, vp.projectileLoc, 1, 0F, 0f, 0f, 0f);
				}
			});
			vp.setOnHit(new Runnable() {
				public void run() {
					Player t = vp.hitPlayer.get(0);					
					customDamage(t, p, 8, false);
					customPoison(t, p, 8, false);
				}
			});
			
			vp.launchProjectile();
			
			VirtualProjectile vp2 = new VirtualProjectile(hrw.server, p, MyUtility.stringListToPlayer(hrw.getEnemyList(p.getName())));
			vp2.c_speed = 6f;
			vp2.c_distance = 15;
			vp2.c_upOffset = -0.5f;
			vp2.c_time = 40;
			vp2.c_removeOnHit = true;
			Location l = p.getLocation();
			l.setYaw(l.getYaw()+15);
			vp2.vector = l.getDirection();
			vp.c_pVerticalOffset = 0.5f;
			vp.c_pHorizenOffset = 0.5f;
			
			
			vp2.setOnDuring(new Runnable() {
				public void run() {
					vp2.projectileLoc.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, vp2.projectileLoc, 1, 0F, 0f, 0f, 0f);
				}
			});
			vp2.setOnHit(new Runnable() {
				public void run() {
					Player t = vp2.hitPlayer.get(0);					
					customDamage(t, p, 8, false);
					customPoison(t, p, 8, false);
				}
			});
			
			vp2.launchProjectile();
			
			VirtualProjectile vp3 = new VirtualProjectile(hrw.server, p, MyUtility.stringListToPlayer(hrw.getEnemyList(p.getName())));
			vp3.c_speed = 5f;
			vp3.c_distance = 18;
			vp3.c_upOffset = -0.5f;
			vp3.c_time = 40;
			vp3.c_removeOnHit = true;
			l = p.getLocation();
			l.setYaw(l.getYaw()-15);
			vp3.vector = l.getDirection();
			vp.c_pVerticalOffset = 0.5f;
			vp.c_pHorizenOffset = 0.5f;
			
			vp3.setOnDuring(new Runnable() {
				public void run() {
					vp3.projectileLoc.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, vp3.projectileLoc, 1, 0F, 0f, 0f, 0f);
				}
			});
			vp3.setOnHit(new Runnable() {
				public void run() {
					Player t = vp3.hitPlayer.get(0);					
					customDamage(t, p, 8, false);
					customPoison(t, p, 8, false);
				}
			});
			
			vp3.launchProjectile();
			
		} else {
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
		}
	}
	
	public void teritarySkill(){
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p)) return;
		if(!p.getInventory().getItemInMainHand().equals(weapon) && !p.getInventory().getItemInMainHand().equals(ultimate_weapon)) return;
		if(isNoSkill()) {
			ActionBarAPI.sendActionBar(p, "��2��l��ų ��� �Ұ� �����Դϴ�.", 40);
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
			return;
		}
		if(isNoMove()) {
			ActionBarAPI.sendActionBar(p, ChatColor.RED+""+ChatColor.BOLD+"�̵��Ұ� �����Դϴ�.", 40);
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
			return;
		}
		if(cooldown.checkCooldown("teritary") && !p.isDead()){
			if(using_ultimate) {
				cooldown.setCooldownSpec("teritary", 2);
			} else {
				cooldown.setCooldown("teritary");
			}
			
			List<Player> tmpList = MyUtility.stringListToPlayer(hrw.getEnemyList(p.getName()));
			
			List<Player> hitList = hrw.getCorsurPlayer(p, tmpList, 10, true);
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1.5F, 4.0f);
			p.setVelocity(p.getEyeLocation().getDirection().multiply(2.65f));
			for(Player t : hitList) {
				customDamage(t, p, 12, false);
			}
			final Player observer = p;
			
	        Location observerPos = observer.getEyeLocation();
	        final Vector3D observerDir = new Vector3D(observerPos.getDirection());
	        final Vector3D observerStart = new Vector3D(observerPos);
            Vector3D observerEnd = observerStart.add(observerDir.multiply(12));
            
            Location loc = new Location(p.getWorld(), observerEnd.x, observerEnd.y-1, observerEnd.z).add(0,0.5f,0); 
			LineEffect le = new LineEffect(effectManager);
			le.particle = Particle.SUSPENDED_DEPTH;
			le.particles = 15;
			le.period = 1;
			le.iterations = 15;
			le.particleOffsetX = 0.15f;
			le.particleOffsetY = 0f;
			le.particleOffsetZ = 0.15f;
			le.setLocation(p.getEyeLocation().add(0,-1,0));
			le.setTargetLocation(loc);
			le.start();

		}else {
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
		
		if(cooldown.checkCooldown("ultimate")){
			if(isNoSkill()) {
				ActionBarAPI.sendActionBar(p, "��2��l��ų ��� �Ұ� �����Դϴ�.", 40);
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
				return;
			}
			cooldown.setCooldown("Ultimate");
			blockPerUlti = true;
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 2.0f, 0.5f);
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10, 254));
			p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 10, 248));
			
			/*AnimatedBallEffect ef1 = new AnimatedBallEffect(effectManager);
			ef1.particle = Particle.PORTAL;
			ef1.iterations = 1;
			ef1.setEntity(p);
			ef1.start();*/
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(hrw.server, new Runnable() {
				public void run() {
					hrw.removeItem(p, Material.DIAMOND_AXE, 5);
					p.getInventory().addItem(ultimate_weapon);
					p.setWalkSpeed(p.getWalkSpeed()+0.15f);
					dmg = 20;
					
					HelixEffect ef = new HelixEffect(effectManager);
					ef.particle = Particle.PORTAL;
					ef.radius = 1f;
					ef.curve=6;
					ef.period = 36;
					ef.strands = 4;
					ef.offset = new Vector(0,-1,0);
					ef.particleCount = 1;
					ef.iterations = 4;
					ef.setEntity(p);
					ef.start();
					cooldown.setCooldownSpec("teritary", 0);
					using_ultimate = true;
				}
			}, 10l);

			Bukkit.getScheduler().scheduleSyncDelayedTask(hrw.server, new Runnable() {
				public void run() {
					hrw.removeItem(p, Material.DIAMOND_SWORD, 5);
					p.getInventory().setItem(0, weapon);
					p.setWalkSpeed(p.getWalkSpeed()-0.15f);
					dmg = 9;
					/*AnimatedBallEffect ef2 = new AnimatedBallEffect(effectManager);
					ef2.particle = Particle.PORTAL;
					ef2.iterations = 1;
					ef2.setEntity(p);
					ef2.start();*/
					using_ultimate = false;
					blockPerUlti = false;
				}
			}, 160l);
			
		} else {
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
		}
	}
	
	@Override
	public void onPlayerToggleFlight(PlayerToggleFlightEvent e) {
		Player p = e.getPlayer();
		if(p.getGameMode() == GameMode.SURVIVAL) {
			e.setCancelled(true);
			if(!passive) {
				p.setAllowFlight(false);
				return;
			}
			p.setAllowFlight(false);
		    p.setFlying(false);
			if(cooldown.checkCooldown("passive")) {
				cooldown.setCooldown("passive");
				Vector v = p.getLocation().getDirection().multiply(1).setY(0.5f);
				p.setVelocity(new Vector(0, 0, 0));
				p.setVelocity(v);
				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 2.0f, 4.0f);
				p.getWorld().spawnParticle(Particle.CLOUD, p.getLocation(), 2, 0.5F, 0.5f, 0.5f, 0.1f);
			}	
			Bukkit.getScheduler().scheduleSyncDelayedTask(hrw.server, new Runnable() {
				public void run() {
					if(hrw.ingame) {
						p.setAllowFlight(true);
					}				
				}
			}, 40l);
		}
	}
	
	@Override
	public void onPlayerKill(Player t) {
		if(passive) {
			if(!isNoSkill()) {
				Player p = Bukkit.getPlayer(playerName);
				cooldown.setCooldownSpec("teritary", 0);
				p.playSound(p.getLocation(),Sound.BLOCK_NOTE_SNARE, 1.0f, 2.5f);
				ActionBarAPI.sendActionBar(p, "��2��l���� �߱��� ���� ���ð� �ʱ�ȭ��", 40);
			}		
		}
	}
}
