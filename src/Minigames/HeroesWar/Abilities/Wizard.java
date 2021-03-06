package Minigames.HeroesWar.Abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.coloredcarrot.api.sidebar.Sidebar;
import com.coloredcarrot.api.sidebar.SidebarString;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.connorlinfoot.titleapi.TitleAPI;

import EGServer.ServerManager.EGScheduler;
import Minigames.HeroesWar.HRWBase;
import Minigames.HeroesWar.Team;
import Utility.MyUtility;
import Utility.VirtualProjectile;
import de.slikey.effectlib.effect.CircleEffect;
import de.slikey.effectlib.effect.ShieldEffect;

public class Wizard extends Ability{

	List<SidebarString> strList = new ArrayList<SidebarString>(7);
	
	////��ɲ�����

	
	public Wizard(HRWBase hrw, Player p, String abilityName, Team team) {
		super(hrw, p, abilityName, team);
		cooldown = new MyCooldown(this);
		
		//������ ����
		dmg = 5;	

		weapon = new ItemStack(Material.BLAZE_ROD, 1);
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
		cooldown_primarySkill = 3;
		cooldown_secondarySkill = 15;
		cooldown_teritarySkill = 15;
		cooldown_ultimateSkill = 91;
		
		//ü�¼���
		health = 48;

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
		p.openInventory(hrw.inven_desc_wizard);
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
		statLoreList.add("��7������ ȭ�����ظ� �� �ð� : ��a"+hrwData.fireTime+"��"+"  ");
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
				tmpStr = "��4Secd ��6"+cooldown.getLeftCooldown("secondary") ;
			} else {
				tmpStr = "��aSecd ��6Ready";
			}
			
			
			line = new SidebarString(tmpStr);
			strList.add(line);
			
			/*if(nowCooldown_teritarySkill > 0) {
				tmpStr = "��4Tert ��6"+nowCooldown_teritarySkill;
			} else {
				tmpStr = "��aTert ��6Ready";
			}
			line = new SidebarString(tmpStr);
			strList.add(line);*/
			
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
				tmpStr = "��4Secd ��6"+cooldown.getLeftCooldown("secondary") ;
			} else {
				tmpStr = "��aSecd ��6Ready";
			}
			
			
			line = new SidebarString(tmpStr);
			strList.add(line);
			
			/*if(nowCooldown_teritarySkill > 0) {
				tmpStr = "��4Tert ��6"+nowCooldown_teritarySkill;
			} else {
				tmpStr = "��aTert ��6Ready";
			}
			line = new SidebarString(tmpStr);
			strList.add(line);*/
			
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
	
	public void primarySkill(){
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p)) return;
		if(!p.getInventory().getItemInMainHand().equals(weapon)) return;
		if(isNoSkill()) {
			ActionBarAPI.sendActionBar(p, "��2��l��ų ��� �Ұ� �����Դϴ�.", 40);
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
			return;
		}
		if(cooldown.checkCooldown("primary") && !p.isDead()){
			cooldown.setCooldown("primary");
			
			List<Player> enemyList = getEnemyList(p.getName());
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 1.5f);
			VirtualProjectile vp = new VirtualProjectile(hrw.server, p, enemyList);
			vp.c_speed = 3.2f;
			vp.c_distance = 75;
			vp.c_time = 100;
			
			vp.setOnDuring(new Runnable() {
				public void run() {
					vp.projectileLoc.getWorld().spawnParticle(Particle.FLAME, vp.projectileLoc, 1, 0F, 0f, 0f, 0f);
				}
			});
			
			vp.setOnNonHit(new Runnable() {
				public void run() {
					vp.projectileLoc.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, vp.projectileLoc, 1, 0.1F, 0.1f, 0.1f, 0.1f);
					vp.projectileLoc.getWorld().playSound(vp.projectileLoc, Sound.ENTITY_GENERIC_EXPLODE, 0.5f, 2.0f);
					p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.25f, 2.0f);
					for(Player t : enemyList) {
						int dis = (int)t.getLocation().distance(vp.projectileLoc);
						if(dis <= 3) {
							customDamage(t, p, 6, false);
							customFire(t, p, (passive && !isNoSkill() ? 8 : 4), false);
						}
					}
				}
			});
			
			vp.setOnHit(new Runnable() {
				public void run() {
					vp.projectileLoc.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, vp.projectileLoc, 3, 0.1F, 0.1f, 0.1f, 0.1f);
					vp.projectileLoc.getWorld().playSound(vp.projectileLoc, Sound.ENTITY_GENERIC_EXPLODE, 0.5f, 2.0f);
					p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.25f, 2.0f);
					Player hit = vp.hitPlayer.get(0);
					customDamage(hit, p, 9, false);
					for(Player t : enemyList) {
						int dis = (int)t.getLocation().distance(vp.projectileLoc);
						if(dis <= 3) {
							customDamage(t, p, 5, false);
							customFire(t, p, (passive && !isNoSkill() ? 12 : 8), false);
						}
					}
				}
			});
			
			
			vp.launchProjectile();
			
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
			cooldown.setCooldown("secondary");
			
			List<Player> enemyList = getEnemyList(p.getName());
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1.0f, 2.0f);
			VirtualProjectile vp = new VirtualProjectile(hrw.server, p, enemyList);
			vp.c_speed = 1f;
			vp.c_distance = 25;
			vp.c_time = 100;
			
			vp.setOnDuring(new Runnable() {
				public void run() {
					vp.projectileLoc.getWorld().spawnParticle(Particle.CRIT, vp.projectileLoc, 1, 0F, 0f, 0f, 0f);
				}
			});
			
			vp.setOnNonHit(new Runnable() {
				public void run() {
					Location l = MyUtility.getGroundLocation(vp.projectileLoc).add(0,0.25,0);
					CircleEffect ef1 = new CircleEffect(effectManager);
					ef1.particle = Particle.FLAME;
					ef1.color = team.teamColor;
					ef1.angularVelocityX = 0;
					ef1.angularVelocityY = 0;
					ef1.angularVelocityZ = 0;
					ef1.particleSize = 1;
					ef1.wholeCircle = true;
					ef1.particleOffsetX = 0.1f;
					ef1.particleOffsetZ = 0.1f;
					ef1.particleOffsetY = 0.1f;
					ef1.particleCount = 2;
					ef1.radius = 4;
					ef1.period = 26;
					ef1.iterations = 5;
					ef1.setLocation(l);
					ef1.start();
					
					EGScheduler sch = new EGScheduler(hrw);
					sch.schTime = 10;
					sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(hrw.server, new Runnable() {
						
						public void run() {
							
							if(sch.schTime > 0) {
								sch.schTime -= 1;
								vp.projectileLoc.getWorld().playSound(vp.projectileLoc, Sound.BLOCK_FIRE_AMBIENT, 1.5f, 0.25f);
								for(Player t : enemyList) {
									int dis = (int)t.getLocation().distance(vp.projectileLoc);
									if(dis <= 3) {
										customDamage(t, p, 2, false);
										customFire(t, p, (passive && !isNoSkill() ? 9 : 5), false);
									}
								}
							} else {
								sch.cancelTask(true);							
							}
						}
						
					}, 10l, 10l);
				}
			});

			vp.setOnHit(vp.onNonHit);
			vp.launchProjectile();
			
			
		}
			
	}
	
	/*public void teritarySkill(){
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p)) return;
		if(cooldown.checkCooldown("teritary") && !p.isDead()){
			cooldown.setCooldown("teritary");
			
			p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1.2f, 2.4f);
			
			List<Player> hit = hrw.getCorsurPlayer(p, getEnemyList(p.getName()), 1, false);
			if(hit != null) {
				Player t = hit.get(0);
				customDamage(t, p, 8, false);
			} 				

			Location l = p.getEyeLocation();
			l.setYaw(l.getYaw() - 85);
			l = l.add(l.getDirection().multiply(1.5f));
			
			Location t = p.getEyeLocation();
			t.setYaw(t.getYaw() + 85);
			t = t.add(t.getDirection().multiply(1.5f));
			
			final Location l1 = l.clone();
			final Location t1 = t.clone();
			final Location l2 = l.clone();
			final Location t2 = t.clone();
			
			LineEffect ef = new LineEffect(effectManager);
			ef.particle = Particle.CLOUD;
			ef.iterations = 1;
			ef.setLocation(l.add(0,-1f,0));
			ef.setTargetLocation(t.add(0,1f,0));
			ef.start();
			
	
			Bukkit.getScheduler().scheduleSyncDelayedTask(hrw.server, new Runnable() {
				public void run() {
					List<Player> hit = hrw.getCorsurPlayer(p, getEnemyList(p.getName()), 1, false);
					if(hit != null) {
						Player t = hit.get(0);
						customDamage(t, p, 8, false);
					} 	
					p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1.2f, 2.4f);
					LineEffect ef = new LineEffect(effectManager);
					ef.particle = Particle.CLOUD;
					ef.iterations = 1;
					ef.setLocation(l1.add(0,1f,0));
					ef.setTargetLocation(t1.add(0,-1f,0));
					ef.start();
				}

			},6l);
			
			
			if(passive) {
				Bukkit.getScheduler().scheduleSyncDelayedTask(hrw.server, new Runnable() {
					public void run() {
						List<Player> hit = hrw.getCorsurPlayer(p, getEnemyList(p.getName()), 1, false);
						if(hit != null) {
							Player t = hit.get(0);
							customDamage(t, p, 8, false);
						} 	
						p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1.2f, 2.4f);
						LineEffect ef = new LineEffect(effectManager);
						ef.particle = Particle.CLOUD;
						ef.iterations = 1;
						ef.setLocation(l2.add(0,0,0));
						ef.setTargetLocation(t2.add(0,0,0));
						ef.start();
					}

				},12l);
			}
			
		} else {
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
		}
	}*/
	
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
			cooldown.setCooldown("ultimate");
			blockPerUlti = true;
			List<Player> enemyList = getEnemyList(p.getName());
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_SHOOT, 1.0f, 0.5f);
			VirtualProjectile vp = new VirtualProjectile(hrw.server, p, enemyList);
			vp.c_speed = 1.35f;
			vp.c_distance = 60;
			vp.c_time = 200;
			
			vp.setOnDuring(new Runnable() {
				public void run() {
					vp.projectileLoc.getWorld().spawnParticle(Particle.CRIT_MAGIC, vp.projectileLoc, 1, 0F, 0f, 0f, 0f);
				}
			});
			
			vp.setOnNonHit(new Runnable() {
				public void run() {
					Location l = MyUtility.getGroundLocation(vp.projectileLoc);
					CircleEffect ef1 = new CircleEffect(effectManager);
					ef1.particle = Particle.CRIT_MAGIC;
					ef1.angularVelocityX = 0;
					ef1.angularVelocityY = 0;
					ef1.angularVelocityZ = 0;
					ef1.particleSize = 1;
					ef1.wholeCircle = true;
					ef1.particleOffsetX = 0.1f;
					ef1.particleOffsetZ = 0.1f;
					ef1.particleOffsetY = 0.1f;
					ef1.particleCount = 2;
					ef1.radius = 6.5f;
					ef1.period = 20;
					ef1.iterations = 5;
					ef1.setLocation(l);
					ef1.start();						                
					
					
					for(Player t : enemyList) {
						if(t.getLocation().distance(l) <= 10) {
							TitleAPI.sendFullTitle(t, 0, 25, 0, ChatColor.RED+"����!", "����� �������� �ñر� �������� �ֽ��ϴ�!");
							p.playSound(p.getLocation(), Sound.BLOCK_NOTE_CHIME, 1.0f, 1.5f);
						}
					}
					
					EGScheduler sch = new EGScheduler(hrw);
					//sch.schTime = 10;
					sch.schId = Bukkit.getScheduler().scheduleSyncDelayedTask(hrw.server, new Runnable() {
						
						public void run() {
							
							List<Player> enemyList = getEnemyList(p.getName());
							p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_AMBIENT, 1.0f, 1.5f);
							VirtualProjectile vp1 = new VirtualProjectile(hrw.server, p, enemyList);
							vp1.c_speed = 1.3f;
							vp1.c_distance = 80;
							vp1.c_time = 400;
							//vp1.c_pHorizenOffset = 3f;
							vp1.c_startLoc = l.clone().add(0,15,0);
							vp1.vector = new Vector(0,-1,0);
							
							vp1.setOnDuring(new Runnable() {
								public void run() {
									Location l = MyUtility.getGroundLocation(vp1.projectileLoc);
									ShieldEffect ef2 = new ShieldEffect(effectManager);
									ef2.particle = Particle.FLAME;
									ef2.particleSize = 1;
									ef2.particleOffsetX = 0.1f;
									ef2.particleOffsetZ = 0.1f;
									ef2.particleOffsetY = 0.1f;
									ef2.particleCount = 2;
									ef2.radius = 3;
									ef2.iterations = 1;
									ef2.setLocation(vp1.projectileLoc);
									ef2.start();									
								}
							});
							
							vp1.setOnNonHit(new Runnable() {
								public void run() {
									vp1.projectileLoc.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, vp1.projectileLoc, 2, 1F, 1f, 1f, 0.1f);
									vp1.projectileLoc.getWorld().playSound(vp.projectileLoc, Sound.ENTITY_ENDERDRAGON_FIREBALL_EXPLODE, 3.0f, 0.5f);
									for(Player t : enemyList) {
										int dis = (int)t.getLocation().distance(vp1.projectileLoc);
										if(dis <= 10) {
											customDamage(t, p, 50 - (dis*2), false);
											customFire(t, p, (passive && !isNoSkill() ? 14 : 10), false);
										}
									}
									blockPerUlti = false;
								}
							});

							vp1.setOnHit(vp1.onNonHit);
							vp1.launchProjectile();
							
							sch.cancelTask(true);
						}
						
					}, 18l);
				}
			});

			vp.setOnHit(vp.onNonHit);
			vp.launchProjectile();
			
		}else {
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
		}
	}
	
}
