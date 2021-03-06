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
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.coloredcarrot.api.sidebar.Sidebar;
import com.coloredcarrot.api.sidebar.SidebarString;
import com.connorlinfoot.actionbarapi.ActionBarAPI;

import EGServer.ServerManager.EGScheduler;
import Minigames.HeroesWar.HRWBase;
import Minigames.HeroesWar.Team;
import Utility.MyUtility;
import de.slikey.effectlib.effect.BleedEffect;
import de.slikey.effectlib.effect.CircleEffect;
import de.slikey.effectlib.effect.LineEffect;

public class Fighter extends Ability{

	List<SidebarString> strList = new ArrayList<SidebarString>(7);
	
	////����������
	public int stat_selfHealAmt = 0;
	public int stat_move = 0;
	
	public Fighter(HRWBase hrw, Player p, String abilityName, Team team) {
		super(hrw, p, abilityName, team);
		cooldown = new MyCooldown(this);
		
		
		//������ ����
		dmg = 5;
		
		weapon = new ItemStack(Material.IRON_SPADE, 1);
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
		cooldown_primarySkill = 6;
		cooldown_secondarySkill = 4;
		cooldown_teritarySkill = 19;
		cooldown_ultimateSkill = 43;
		
		//ü�¼���
		health = 115;

		
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
		p.openInventory(hrw.inven_desc_fighter);
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
		statLoreList.add("��7�ڰ�ȸ���� : ��a"+stat_selfHealAmt+"  ");
		statLoreList.add("��7������ ������ ����� Ƚ�� : ��a"+stat_move+"  ");
		statLoreList.add("��7���� ������ �ð� : ��a"+hrwData.bindTime+"��"+"  ");
		statLoreList.add("");
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
			List<Player> hit = hrw.getCorsurPlayer(p, getEnemyList(p.getName()), 20, false);
			if(hit != null) {
				cooldown.setCooldown("primary");
				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1.0f, 1.0f);
				Player t = hit.get(0);
				Location l = t.getLocation().add(t.getLocation().getDirection().multiply(0.5f));
				l.setYaw(l.getYaw()*-1);
				l.setY(t.getLocation().getY());
				p.teleport(l, TeleportCause.PLUGIN);
				stat_move += 1;
			} else {
				ActionBarAPI.sendActionBar(p, "��2��l����� �������� �ʽ��ϴ�.", 40);
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
			}											
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
			
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_SPLASH_POTION_BREAK, 1.2f, 0.1f);
			
			List<Player> hit = hrw.getCorsurPlayer(p, getEnemyList(p.getName()), 4, false);
			if(hit != null) {
				Player t = hit.get(0);
				customDamage(t, p, 8, false);
				customBind(t, p, 1, false);
			} 				
			Location l = p.getEyeLocation();
			Vector v = l.getDirection();
			v.multiply(3);
			LineEffect ef = new LineEffect(effectManager);
			ef.particle = Particle.REDSTONE;
			ef.color = Color.fromRGB(51, 0, 204);
			ef.iterations = 1;
			ef.setLocation(l);
			ef.setTargetLocation(l.add(v));
			ef.start();

		}else {
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
			
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 254));
			//p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 100, 248));
			p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 255));
			EGScheduler sch = new EGScheduler(hrw);
			sch.schTime = passive ? 4 : 5;
			if(passive) customHeal(p, p, 5, false);
			sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(hrw.server, new Runnable() {
				public void run() {
					if(sch.schTime-- > 0) {
						p.getWorld().playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 0.1f);
						CircleEffect ef1 = new CircleEffect(effectManager);
						ef1.particle = Particle.REDSTONE;
						ef1.color = Color.fromRGB(255, 255, 0);
						ef1.angularVelocityX = 0;
						ef1.angularVelocityY = 0;
						ef1.angularVelocityZ = 0;
						ef1.particleSize = 1;
						ef1.wholeCircle = true;
						ef1.particleOffsetX = 0.1f;
						ef1.particleOffsetZ = 0.1f;
						ef1.particleOffsetY = 0.1f;
						ef1.particleCount = 2;
						ef1.radius = 0.75f;
						ef1.period = 3;
						ef1.iterations = 3;
						ef1.setLocation(p.getLocation().add(0,1,0));
						ef1.start();	
						customHeal(p, p, passive ? 10 : 8, true);
						stat_selfHealAmt += passive ? 10 : 8;
					} else {
						sch.cancelTask(true);
						noCC = false;
					}
				}
			}, 0l, 20l);
			
			
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
		
		if(cooldown.checkCooldown("ultimate")){
			if(isNoSkill()) {
				ActionBarAPI.sendActionBar(p, "��2��l��ų ��� �Ұ� �����Դϴ�.", 40);
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
				return;
			}
			cooldown.setCooldown("Ultimate");
			blockPerUlti = true;
			List<Player> hit = hrw.getCorsurPlayer(p, getEnemyList(p.getName()), 3, false);
			if(hit != null) {
				invincible = true;
				Player t = hit.get(0);
				customFaint(t, p, 2, false);
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 254));
				p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 40, 248));
				p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 40, 255));
				
				Location start = p.getLocation();
				Location l = p.getLocation();
				Location tl = p.getLocation().add(p.getLocation().getDirection().multiply(1.5f));		
				l.setPitch(0);
				tl.setPitch(0);
				tl.setY(l.getY());
				tl.setYaw(tl.getYaw()*-1);
				
				//t.setGravity(false);
				//p.setGravity(false);
				
				EGScheduler sch = new EGScheduler(hrw);
				sch.schTime = 5;
				sch.schTime2 = 0;
				sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(hrw.server, new Runnable() {
					public void run() {
						if(sch.schTime-- > 0) {
							customDamage(t, p, 4, false);
							BleedEffect ef = new BleedEffect(effectManager);
							ef.setEntity(t);
							ef.iterations = 1;
							ef.visibleRange = 16;
							ef.start();
							sch.schTime2 += 1.5f;			
							if(l.add(0,sch.schTime2,0).getBlock().getType() == Material.AIR) {
								p.teleport(l, TeleportCause.PLUGIN);
								if(!t.isDead()) t.teleport(tl.add(0,sch.schTime2,0), TeleportCause.PLUGIN);
							}else {
								p.teleport(l.subtract(0,sch.schTime2,0));
								if(!t.isDead()) t.teleport(tl.add(0,sch.schTime2,0), TeleportCause.PLUGIN);
							}
						} else {
							sch.cancelTask(true);
						}					
					}
				}, 0l, 4l);
				
				Bukkit.getScheduler().scheduleSyncDelayedTask(hrw.server, new Runnable() {
					public void run() {
						customDamage(t, p, 6, false);
						Location tl = MyUtility.getGroundLocation(t.getLocation()).add(0,2,0);
						LineEffect ef = new LineEffect(effectManager);
						ef.particle = Particle.CLOUD;
						ef.iterations = 1;
						ef.setLocation(t.getLocation().add(0,2,0));
						ef.setTargetLocation(tl);
						ef.start();
						//t.setGravity(true);
						t.teleport(tl, TeleportCause.PLUGIN);
						customDamage(t, p, 5, false);
						BleedEffect ef2 = new BleedEffect(effectManager);
						ef2.setEntity(t);
						ef2.iterations = 1;
						ef2.start();
					}
				},30l);
				
				Bukkit.getScheduler().scheduleSyncDelayedTask(hrw.server, new Runnable() {
					public void run() {
						p.teleport(start.add(0,1,0), TeleportCause.PLUGIN);
						//p.setGravity(true);
						invincible = false;
						blockPerUlti = false;
					}
				},40l);
				
			} else {
				ActionBarAPI.sendActionBar(p, "��2��l�ñر� ������", 40);
			}
		} else {
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
		}
	}
	
}
