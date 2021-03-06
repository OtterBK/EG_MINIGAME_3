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
import org.bukkit.util.Vector;

import com.coloredcarrot.api.sidebar.Sidebar;
import com.coloredcarrot.api.sidebar.SidebarString;
import com.connorlinfoot.actionbarapi.ActionBarAPI;

import EGServer.ServerManager.EGScheduler;
import Minigames.HeroesWar.HRWBase;
import Minigames.HeroesWar.Team;
import Utility.MyUtility;
import Utility.VirtualProjectile;
import de.slikey.effectlib.Effect;
import de.slikey.effectlib.effect.CircleEffect;
import de.slikey.effectlib.effect.EarthEffect;
import de.slikey.effectlib.effect.LineEffect;

public class Monarch extends Ability{

	List<SidebarString> strList = new ArrayList<SidebarString>(7);
	
	//���� ����
	private boolean draining = false; 
	
	//���� ����
	public int stat_grab = 0;
	public int stat_noMove = 0;
	
	public Monarch(HRWBase hrw, Player p, String abilityName, Team team) {
		super(hrw, p, abilityName, team);
		cooldown = new MyCooldown(this);
		
		//������ ����
		dmg = 4;

		weapon = new ItemStack(Material.BLAZE_POWDER, 1);
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
		cooldown_secondarySkill = 11;
		cooldown_teritarySkill = 16;
		cooldown_ultimateSkill = 91;
		
		//ü�¼���
		health = 120;

		
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
		p.openInventory(hrw.inven_desc_monarch);
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
		statLoreList.add("��7�Ʊ��� ���ظ� ����� �� : ��a"+hrwData.barrierAmt+"  ");
		statLoreList.add("��7���� ����� Ƚ�� : ��a"+stat_grab+"  ");	
		statLoreList.add("��7���� �̵��� ������ Ƚ�� : ��a"+stat_noMove+"  ");
		statLoreList.add("��7���� ������Ų �ð� : ��a"+hrwData.faintTime+"��"+"  ");
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
	
	public int calcDamage(Player victim , Player damager, int damage) {
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p)) return -1;
		if(draining && !p.isDead()) {
			if(p.getLocation().distance(victim.getLocation()) < 8) {
				double hp = p.getHealth();
				if(hp > 1) {		
					damage = (int) (damage / (passive ? 3 : 2));
					if(hp - damage > 5) {
						hp -= damage;
						p.setHealth(hp);
					} else {
						p.setHealth(1);
					}
					hrwData.barrierAmt += damage;
					return 0;
				}	
			}						
		}
		return -1;
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
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_SNOWBALL_THROW, 1.2f, 0.1f);
			
			Location l = p.getLocation();
			
			VirtualProjectile vp = new VirtualProjectile(hrw.server, p, getEnemyList(p.getName()));
			vp.c_speed = 1.5f;
			vp.c_distance = 25f;
			vp.c_time = 300;
			vp.c_pHorizenOffset = 1.2f;
			vp.c_pVerticalOffset = 1.2f;		
			
			vp.setOnDuring(new Runnable() {
				public void run() {
					vp.projectileLoc.getWorld().spawnParticle(Particle.SNOW_SHOVEL, vp.projectileLoc, 1, 0F, 0f, 0f, 0f);
					p.getWorld().playSound(p.getLocation(), Sound.BLOCK_WOOD_PLACE, 1.2f, 2.0f);
					p.teleport(l, TeleportCause.PLUGIN);
				}
			});
			
			vp.setOnNonHit(new Runnable() {
				public void run() {
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_FLAP, 1.2f, 2.0f);		
				}
			});
			
			vp.setOnHit(new Runnable() {
				public void run() {
					Player t = vp.hitPlayer.get(0);
					Team team = hrw.playerMap.get(t.getName()).team;
					Location pl = t.getLocation();
					Location l1 = team.loc_base1;
					Location l2 = team.loc_base2;	
					//p.sendMessage(""+pl);
					//p.sendMessage(""+l1);
					//p.sendMessage(""+l2);
					if(l1.getX() < pl.getX() &&
							l2.getX() > pl.getX() &&
							l1.getY() < pl.getY() &&
							l2.getY() > pl.getY() &&
							l1.getZ() < pl.getZ() &&
							l2.getZ() > pl.getZ()) {
						ActionBarAPI.sendActionBar(t, "��c��l�����ȿ����� �������� ���� �ʽ��ϴ�.", 60);
						ActionBarAPI.sendActionBar(t, "��c��l�������� ���� ����� �� �����ϴ�.", 60);
						return;
					} 
					customFaint(t, p, 1, false);
					stat_grab += 1;
					p.getWorld().playSound(p.getLocation(), Sound.BLOCK_WOODEN_TRAPDOOR_CLOSE, 1.2f, 2.0f);
					
					EGScheduler sch = new EGScheduler(hrw);
					sch.schTime = (int) p.getLocation().distance(t.getLocation())/2;
					sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(hrw.server, new Runnable() {
						public void run() {
							if(sch.schTime-- > 0) {
								p.teleport(l, TeleportCause.PLUGIN);
								Location tl = null;
								if(sch.schTime != 0) {
									tl = l.clone().add(l.getDirection().multiply(sch.schTime*2));
								} else {
									tl = l.clone().add(l.getDirection());
								}
								tl.setYaw(tl.getYaw()*-1);
								t.teleport(tl.add(0,0.2,0), TeleportCause.PLUGIN);
								LineEffect ef = new LineEffect(effectManager);
								ef.particle = Particle.SNOW_SHOVEL;
								ef.iterations = 1;
								ef.setLocation(l.clone().add(0,1.5,0));
								ef.setTargetLocation(tl.clone().add(0,1.5,0));
								ef.start();
								p.getWorld().playSound(p.getLocation(), Sound.BLOCK_WOOD_BREAK, 1.2f, 2.0f);
							} else {
								sch.cancelTask(true);
							}
						}
					}, 6l, 1l);				    
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
			
			p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_SHOOT, 1.2f, 2.5f);
			
			VirtualProjectile vp = new VirtualProjectile(hrw.server, p, getEnemyList(p.getName()));
			vp.c_speed = 4f;
			vp.c_distance = 40f;
			vp.c_time = 300;
			
			vp.setOnDuring(new Runnable() {
				public void run() {
					vp.projectileLoc.getWorld().spawnParticle(Particle.END_ROD, vp.projectileLoc, 1, 0F, 0f, 0f, 0f);
				}
			});
			
			vp.setOnNonHit(new Runnable() {
				public void run() {
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_FIREWORK_TWINKLE, 1.2f, 2.5f);	
					vp.projectileLoc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, vp.projectileLoc, 1, 0F, 0f, 0f, 0f);
					Location l = vp.projectileLoc;
					for(Player t : getEnemyList(p.getName())) {
						double dis = l.distance(t.getLocation());
						if(dis < 5) {
							Vector v = t.getLocation().toVector().subtract(l.toVector());	
							v.normalize();
							v.multiply(3-dis/2);
							if(v.getY() > 1.5) v.setY(1.5);
							t.setVelocity(v);
							stat_noMove += 1;
						}
					}
				}
			});
			
			vp.setOnHit(new Runnable() {
				public void run() {
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_FIREWORK_TWINKLE, 1.2f, 2.5f);	
					vp.projectileLoc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, vp.projectileLoc, 1, 0F, 0f, 0f, 0f);
					Location l = vp.projectileLoc;
					for(Player t : getEnemyList(p.getName())) {
						double dis = l.distance(t.getLocation());
						if(dis < 5) {
							Vector v = l.toVector().subtract(t.getLocation().toVector());	
							v.normalize();
							v.multiply(5-dis);
							if(v.getY() > 1.5) v.setY(1.5);
							t.setVelocity(v);
							stat_noMove += 1;
						}
					}
				}
			});
			
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
			
			draining = true;
			EGScheduler sch = new EGScheduler(hrw);
			sch.schTime = 8;
			sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(hrw.server, new Runnable() {
				public void run() {
					if(sch.schTime-- > 0) {
						p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_SWIM, 1.2f, 0.1f);
						//for(float i = 0.5f; i <= 1.5f; i = i + 0.5f) {
						float i = 1.0f;
							CircleEffect ef1 = new CircleEffect(effectManager);
							ef1.particle = Particle.REDSTONE;
							ef1.color = Color.fromRGB(101, 051, 00);
							ef1.angularVelocityX = 0;
							ef1.angularVelocityY = 0;
							ef1.angularVelocityZ = 0;
							ef1.particleSize = 1;
							ef1.wholeCircle = true;
							ef1.particleOffsetX = 0.1f;
							ef1.particleOffsetZ = 0.1f;
							ef1.particleOffsetY = 0.1f;
							ef1.particleCount = 2;
							ef1.radius = 2.5f;
							ef1.period = 5;
							ef1.iterations = 4;
							ef1.setLocation(p.getLocation().add(0, i, 0));
							ef1.start();
						//}				
					} else {
						sch.cancelTask(true);
						draining = false;
					}
				}
			}, 0l, 10l);

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
			blockPerUlti = true;
			
			List<Player> enemyList = getEnemyList(p.getName());
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_FIREWORK_TWINKLE_FAR, 1.0f, 3.0f);
			VirtualProjectile vp = new VirtualProjectile(hrw.server, p, enemyList);
			vp.c_speed = 0.5f;
			vp.c_distance = 20;
			vp.c_time = 100;
			
			vp.setOnDuring(new Runnable() {
				public void run() {
					vp.projectileLoc.getWorld().spawnParticle(Particle.TOWN_AURA, vp.projectileLoc, 1, 0F, 0f, 0f, 0f);
				}
			});
			
			vp.setOnNonHit(new Runnable() {
				public void run() {
					/*Location l = vp.projectileLoc;			
					Effect effect = new EarthEffect(effectManager);
					effect.setLocation(l);
					effect.period = 36;
					effect.iterations = 2;
					effect.start();*/
					
					Location l = vp.projectileLoc;
					
					EGScheduler sch = new EGScheduler(hrw);
					sch.schTime = 10;
					sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(hrw.server, new Runnable() {
						public void run() {
							if(sch.schTime-- > 0) {
								l.getWorld().playSound(vp.projectileLoc, Sound.BLOCK_END_PORTAL_SPAWN, 2.0f, 2.5f);
								l.getWorld().spawnParticle(Particle.CRIT, l, 60, 0.25F, 0.25f, 0.25f, 0.25f);
								for(Player t : getEnemyList(p.getName())) {
									if(t.getLocation().distance(l) < 8) {					
										Vector v = l.toVector().subtract(t.getLocation().toVector());
										v.normalize();
										v.multiply(1f);
										t.setVelocity(v);
										stat_noMove += 1;
									}
								}
							} else {
								sch.cancelTask(true);
							}							
						}
					}, 0l, 8l);
				}
			});
			
			vp.setOnHit(new Runnable() {
				public void run() {
					Location l = vp.projectileLoc;			
					/*Effect effect = new EarthEffect(effectManager);
					effect.setLocation(l);
					effect.period = 36;
					effect.iterations = 2;
					effect.start();*/
					
					EGScheduler sch = new EGScheduler(hrw);
					sch.schTime = 10;
					sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(hrw.server, new Runnable() {
						public void run() {
							if(sch.schTime-- > 0) {
								l.getWorld().playSound(vp.projectileLoc, Sound.BLOCK_END_PORTAL_SPAWN, 2.0f, 2.5f);
								l.getWorld().spawnParticle(Particle.SMOKE_LARGE, l, 20, 0.25F, 0.25f, 0.25f, 0.25f);
								for(Player t : getEnemyList(p.getName())) {
									if(t.getLocation().distance(l) < 8) {
										Vector v = l.toVector().subtract(t.getLocation().toVector());
										v.normalize();
										v.multiply(1f);
										t.setVelocity(v);
									}
								}
							} else {
								sch.cancelTask(true);
								blockPerUlti = false;
							}							
						}
					}, 0l, 8l);
				}
			});
			
			
			vp.launchProjectile();
		}else {
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
		}
	}
	
}
