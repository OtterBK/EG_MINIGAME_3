package Minigames.HeroesWar.Abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
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
import com.nametagedit.plugin.NametagEdit;

import EGServer.ServerManager.EGScheduler;
import Minigames.HeroesWar.HRWBase;
import Minigames.HeroesWar.HeroesWar;
import Minigames.HeroesWar.Team;
import Utility.MyUtility;
import Utility.VirtualProjectile;
import de.slikey.effectlib.effect.CircleEffect;
import de.slikey.effectlib.effect.HelixEffect;
import de.slikey.effectlib.effect.LineEffect;
import de.slikey.effectlib.effect.ShieldEffect;

public class BlackMagician extends Ability{

	List<SidebarString> strList = new ArrayList<SidebarString>(7);
	
	private int stat_allAddDmg = 0;
	
	public BlackMagician(HRWBase hrw, Player p, String abilityName, Team team) {
		super(hrw, p, abilityName, team);
		cooldown = new MyCooldown(this);
		
		//������ ����
		dmg = 5;

		weapon = new ItemStack(Material.COAL, 1);
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
		cooldown_primarySkill = 8;
		cooldown_secondarySkill = 13;
		cooldown_teritarySkill = 10;
		cooldown_ultimateSkill = 94;
		
		//ü�¼���
		health = 60;
		
		String teamName = hrw.getTeam(p);
		SidebarString line1 = new SidebarString("");
		sidebar = new Sidebar((teamName.equalsIgnoreCase("BLUE") ? (ChatColor.BLUE+""+ChatColor.BOLD) : (ChatColor.RED+""+ChatColor.BOLD) )+abilityName, hrw.server, 72000, line1);
		
		setEquipment(p);
		
		blackMagicianTimer();
		
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
		p.openInventory(hrw.inven_desc_blackMagician);
	}
	
	@Override
	public void updateStatItem() {
		if(statItem == null) return;
		ItemMeta meta = statItem.getItemMeta();
		statLoreList.clear();
		statLoreList.clear();
		statLoreList.add("");
		statLoreList.add("��7���� : ��a"+abilityName+"  ");
		statLoreList.add("");
		statLoreList.add("��7K/D : ��a"+hrwData.kill + "��f /��a "+hrwData.death+"  ");
		statLoreList.add("��7������ �� ���ط� : ��a"+hrwData.dealAmt+"  ");
		statLoreList.add("��7�ִ� ���� óġ : ��a"+hrwData.maxStackKill+"  ");
		statLoreList.add("");
		statLoreList.add("��7���� ����Ų �ð� : ��a"+hrwData.weaknessTime+"��"+"  ");
		statLoreList.add("��7���� ������ �ð� : ��a"+hrwData.bindTime+"��"+"  ");
		statLoreList.add("��7���� �̵��ӵ��� ������ �ð� : ��a"+hrwData.speedSlowTime+"��"+"  ");
		statLoreList.add("��7�Ʊ� ġ���� : ��a"+hrwData.healAmt+"  ");
		statLoreList.add("��7�Ʊ��� ���ݷ� ��ȭ�� : ��a"+stat_allAddDmg+"  ");
		statLoreList.add("");
		meta.setLore(statLoreList);
		statItem.setItemMeta(meta);
	}
	
	@Override
	public void reConnect() {
		Player p = Bukkit.getPlayer(playerName);
		setItems(p);
		
		NametagEdit.getApi().setPrefix(p, "[��6��l"+abilityName+"��f]"+(hrw.getTeam(p).equalsIgnoreCase("BLUE") ? "��b" : "��c"));
		p.updateInventory();
		hrw.spawn(p);
		if(hrwData.item_stone != null) p.getInventory().setItem(4, hrwData.item_stone);
		if(hrwData.item_ring != null) p.getInventory().setItem(5, hrwData.item_ring);
		if(hrwData.item_neck != null) p.getInventory().setItem(6, hrwData.item_neck);
		if(hrwData.item_tailsman != null) p.getInventory().setItem(7, hrwData.item_tailsman);
		if(hrwData.carrotCnt != 0) {
			p.getInventory().addItem(new ItemStack(Material.GOLDEN_CARROT, hrwData.carrotCnt));
			hrwData.carrotCnt = 0;
		}
		blackMagicianTimer();
		applyItems();
	}
	
	public void setEquipment(Player p) {
		p.getInventory().setItem(0, weapon);
		p.getInventory().setHeldItemSlot(8);
		MyUtility.setMaxHealth(p, health);
		MyUtility.healUp(p);
		lastInvenMap = p.getInventory().getContents();
		lastArmorMap = p.getInventory().getArmorContents();
	}
	
	public void blackMagicianTimer(){
		EGScheduler sch = new EGScheduler(hrw);
		sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(hrw.server, new Runnable(){
			public void run(){
				if(!hrw.ingamePlayer.contains(playerName)) sch.cancelTask(true);
				if(passive) {
					Player p = Bukkit.getPlayer(playerName);
					if(exitsPlayer(p)) {
						Location l = p.getLocation();
						for(Player t : getEnemyList(p.getName())) {
							if(t.getLocation().distance(l) <= 2.5) {
								customWeakness(t, p, 1, false);
							}
						}
					}
				}			
			}
		}, 0l, 10l);
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
			ActionBarAPI.sendActionBar(p, "��c��ų ��� �Ұ� �����Դϴ�.", 40);
			return;
		}
		if(cooldown.checkCooldown("primary") && !p.isDead()){
			cooldown.setCooldown("primary");
			
			
			List<Player> enemyList = getEnemyList(p.getName());
			p.getWorld().playSound(p.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1.0f, 1.5f);
			VirtualProjectile vp = new VirtualProjectile(hrw.server, p, enemyList);
			vp.c_speed = 7.5f;
			vp.c_distance = 30;
			vp.c_time = 100;
			vp.c_pHorizenOffset = 1f;
			vp.c_pVerticalOffset = 1f;
			
			vp.setOnDuring(new Runnable() {
				public void run() {
					vp.projectileLoc.getWorld().spawnParticle(Particle.SMOKE_LARGE, vp.projectileLoc, 5, 0.1F, 0.1f, 0.1f, 0.1f);
				}
			});
			
			vp.setOnNonHit(new Runnable() {
				public void run() {
					if(passive) {
						vp.projectileLoc.getWorld().spawnParticle(Particle.SMOKE_LARGE, vp.projectileLoc, 15, 0.2F, 0.2f, 0.2f, 0.1f);
						vp.projectileLoc.getWorld().playSound(vp.projectileLoc, Sound.ENTITY_ENDERMITE_HURT, 1.0f, 0.1f);
						for(Player t : getEnemyList(playerName)) {
							if(t.getLocation().distance(p.getLocation()) <= 3) {
								customWeakness(t, p, 1, false);
							}
						}	
					}		
				}
			});
			
			vp.setOnHit(new Runnable() {
				public void run() {
					vp.projectileLoc.getWorld().spawnParticle(Particle.SMOKE_LARGE, vp.projectileLoc, 15, 0.2F, 0.2f, 0.2f, 0.1f);
					vp.projectileLoc.getWorld().playSound(vp.projectileLoc, Sound.ENTITY_ENDERMITE_HURT, 1.0f, 0.1f);
					p.playSound(vp.projectileLoc, Sound.ENTITY_ENDERMITE_HURT, 1.0f, 0.1f);
					Player hit = vp.hitPlayer.get(0);
					customWeakness(hit, p, 2, false);
					if(passive) {
						for(Player t : getEnemyList(playerName)) {
							if(t.getName().equalsIgnoreCase(hit.getName())) continue;
							if(t.getLocation().distance(p.getLocation()) <= 2) {
								customWeakness(t, p, 1, false);
							}
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
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GHAST_AMBIENT, 1.0f, 2.0f);
			VirtualProjectile vp = new VirtualProjectile(hrw.server, p, enemyList);
			vp.c_speed = 2.5f;
			vp.c_distance = 25;
			vp.c_time = 100;
			
			vp.setOnDuring(new Runnable() {
				public void run() {
					vp.projectileLoc.getWorld().spawnParticle(Particle.SMOKE_NORMAL, vp.projectileLoc, 1, 0F, 0f, 0f, 0f);
				}
			});
			
			vp.setOnNonHit(new Runnable() {
				public void run() {
					Location l = MyUtility.getGroundLocation(vp.projectileLoc).add(0,0.25,0);
					CircleEffect ef1 = new CircleEffect(effectManager);
					ef1.particle = Particle.SMOKE_NORMAL;
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
					ef1.period = 20;
					ef1.iterations = 5;
					ef1.wholeCircle = true;
					ef1.setLocation(l);
					ef1.start();
					
					EGScheduler sch = new EGScheduler(hrw);
					sch.schTime = 10;
					sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(hrw.server, new Runnable() {
						
						public void run() {
							
							if(sch.schTime > 0) {
								sch.schTime -= 1;
								vp.projectileLoc.getWorld().playSound(vp.projectileLoc, Sound.ENTITY_GHAST_WARN, 1.5f, 0.25f);
								for(Player t : enemyList) {
									int dis = (int)t.getLocation().distance(vp.projectileLoc);
									if(dis <= 4) {
										customWeakness(t, p, 1, false);
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
			
			
		} else {
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
		}
	}
	
	public void teritarySkill(){
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p)) return;
		if(!p.getInventory().getItemInMainHand().equals(weapon)) return;
		if(isNoSkill()) {
			ActionBarAPI.sendActionBar(p, "��2��l��ų ��� �Ұ� �����Դϴ�.", 40);
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
			return;
		}
		if(cooldown.checkCooldown("teritary") && !p.isDead()){
			
			cooldown.setCooldown("teritary");
			
			List<Player> enemyList = getEnemyList(p.getName());
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_PIG_HURT, 1.0f, 0.5f);
			VirtualProjectile vp = new VirtualProjectile(hrw.server, p, enemyList);
			vp.c_speed = 3f;
			vp.c_distance = 30;
			vp.c_time = 100;
			vp.c_pHorizenOffset = 0.8f;
			vp.c_pVerticalOffset = 0.8f;
			
			vp.setOnDuring(new Runnable() {
				public void run() {
					vp.projectileLoc.getWorld().spawnParticle(Particle.SMOKE_LARGE, vp.projectileLoc, 5, 0.05F, 0.05f, 0.05f, 0.1f);
				}
			});
			
			vp.setOnNonHit(new Runnable() {
				public void run() {

				}
			});
			
			vp.setOnHit(new Runnable() {
				public void run() {
					vp.projectileLoc.getWorld().spawnParticle(Particle.PORTAL, vp.projectileLoc, 15, 0.2F, 0.2f, 0.2f, 0.1f);
					vp.projectileLoc.getWorld().playSound(vp.projectileLoc, Sound.ENTITY_SPIDER_AMBIENT, 0.5f, 0.5f);
					p.playSound(vp.projectileLoc, Sound.ENTITY_SPIDER_AMBIENT, 0.5f, 0.5f);
					Player hit = vp.hitPlayer.get(0);
					customBind(hit, p, 1, false);
					customDamage(hit, p, 20, false);
					customHeal(p, p, 20, true);
					customSpeedSlow(hit, p, 0.6f, 4, false);
				}
			});
			
			
			vp.launchProjectile();

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
		
		if(cooldown.checkCooldown("Ultimate")){
			if(isNoSkill()) {
				ActionBarAPI.sendActionBar(p, "��2��l��ų ��� �Ұ� �����Դϴ�.", 40);
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
				return;
			}
			cooldown.setCooldown("ultimate");
			
			HelixEffect particle = new HelixEffect(effectManager);
			particle.setEntity(p);
			particle.particleOffsetY = -0.5f;
			particle.particle = Particle.REDSTONE;
			particle.color = Color.fromRGB(0, 0, 0);
			particle.period = 30;
			particle.iterations = 1;
			particle.radius = 5f;
			particle.start();		
			
			Location l = p.getLocation();
			
			int enemySize = 0;
			for(Player t : getEnemyList(playerName)) {
				if(t.getLocation().distance(l) <= 7) {
					enemySize++;
				}
			}
			
			if(enemySize > 0) {
				for(Player t : getTeamList(playerName)) {
					if(t.getLocation().distance(l) <= 7) {
						customHeal(t, p, enemySize*10, false);
						customDamageUp(t, p, enemySize, 5, false);
						TitleAPI.sendFullTitle(t, 0, 60, 0, "", "��0��l�渶�����f��l�� ���� 5�ʰ� ���ݷ� "+ enemySize+" ����");
						t.getWorld().playSound(t.getLocation(), Sound.ENTITY_WITCH_AMBIENT, 1.0f, 0.25f);
						stat_allAddDmg += enemySize*5;
					}
				}
			}else {
				TitleAPI.sendFullTitle(p, 0, 60, 0, "", "��0��l�ֺ��� ���� �������� �ʾ� �ǽĿ� ������");
				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WITCH_HURT, 1.0f, 0.5f);
			}	
		}else {
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
		}
	}
	
}
