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
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
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
import Minigames.HeroesWar.HRWPlayer;
import Minigames.HeroesWar.Team;
import Utility.MyUtility;
import Utility.VirtualProjectile;
import de.slikey.effectlib.effect.CircleEffect;

public class Archer extends Ability{

	List<SidebarString> strList = new ArrayList<SidebarString>(7);
	
	//�ü� ����
	public boolean primaryOn;
	public int primaryCnt=0;
	public boolean secondaryOn;
	public int secondaryCnt=0;
	public int levelTimerid;
	
	
	public Archer(HRWBase hrw, Player p, String abilityName, Team team) {
		super(hrw, p, abilityName, team);
		cooldown = new MyCooldown(this);

		weapon = new ItemStack(Material.BOW, 1);
		ItemMeta meta = weapon.getItemMeta();
		meta.setDisplayName("��7[ ��6"+abilityName+" ��7]");
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		meta.setUnbreakable(true);
		List<String> loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��6���ݷ� ��7: ��d2~25");
		loreList.add("");
		meta.setLore(loreList);
		weapon.setItemMeta(meta);
		
		///��Ÿ�� ����
		cooldown_primarySkill = 9;
		cooldown_secondarySkill = 19;
		cooldown_ultimateSkill = 77;
		
		//ü�¼���
		health = 54;
		
		//������ ����
		dmg = 1; //������ �̼�����
		
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
		p.openInventory(hrw.inven_desc_archer);
	}
	
	public void setEquipment(Player p) {
		p.getInventory().setItem(0, weapon);
		p.getInventory().setItem(9, new ItemStack(Material.ARROW, 1));
		p.getInventory().setHeldItemSlot(8);
		MyUtility.setMaxHealth(p, health);
		MyUtility.healUp(p);
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
		statLoreList.add("��7���� ������ �ð� : ��a"+hrwData.bindTime+"��"+"  ");
		statLoreList.add("��7���� ������Ų �ð� : ��a"+hrwData.faintTime+"��"+"  ");
		statLoreList.add("");
		meta.setLore(statLoreList);
		statItem.setItemMeta(meta);
	}
	
	@Override
	public void reConnect() {
		Player p = Bukkit.getPlayer(playerName);
		setItems(p);
		p.getInventory().setItem(9, new ItemStack(Material.ARROW, 1));
		NametagEdit.getApi().setPrefix(p, "[��6��l"+abilityName+"��f]"+(hrw.getTeam(p).equalsIgnoreCase("BLUE") ? "��b" : "��c"));
		p.updateInventory();
		hrw.spawn(p);
		applyItems();
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
			} else if(primaryOn) {
				tmpStr = "��aPrim ��6Using";
			} else if(cooldown.getLeftCooldown("primary") > 0) {
				tmpStr = "��4Prim ��6"+cooldown.getLeftCooldown("primary");
			} else {
				tmpStr = "��aPrim ��6Ready";
			}
			
			
			line = new SidebarString(tmpStr);
			strList.add(line);
			if(isNoSkill()) {
				tmpStr = "��4Secd ��6Blocked";
			} else if(secondaryOn) {
				tmpStr = "��aSecd ��6Using";
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
			} else if(primaryOn) {
				tmpStr = "��aPrim ��6Using";
			} else if(cooldown.getLeftCooldown("primary") > 0) {
				tmpStr = "��4Prim ��6"+cooldown.getLeftCooldown("primary");
			} else {
				tmpStr = "��aPrim ��6Ready";
			}
			
			
			line = new SidebarString(tmpStr);
			strList.add(line);
			if(isNoSkill()) {
				tmpStr = "��4Secd ��6Blocked";
			} else if(secondaryOn) {
				tmpStr = "��aSecd ��6Using";
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
			
			if(primaryOn) {
				if(primaryCnt == 0) {
					ActionBarAPI.sendActionBar(p, "��2��l��ȭ�� ���", 40);
					primaryOn = false;
					p.setLevel(0);
					p.setExp(0);
					Bukkit.getScheduler().cancelTask(levelTimerid);
				} 
				return;
			}
			
			if(secondaryOn) {
				if(secondaryCnt == 0) {
					ActionBarAPI.sendActionBar(p, "��2��l�ӻ� ���", 40);
					secondaryOn = false;
					p.setLevel(0);
					p.setExp(0);
					Bukkit.getScheduler().cancelTask(levelTimerid);
				} 
			}
			
			ActionBarAPI.sendActionBar(p, "��2��l��ȭ�� �����", 40);	
			p.getWorld().playSound(p.getLocation(), Sound.BLOCK_CHEST_LOCKED, 1.5f, 0.5f);
			primaryOn = true;
			primaryCnt = 0;
			p.setLevel(1-primaryCnt);
			
			levelTimerid = hrw.timerLevelBar(p, 6, false, false);
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(hrw.server, new Runnable() {
				
				public void run() {
					if(primaryOn) {
						primaryOn = false;
						p.setLevel(0);
						p.setExp(0);
						cooldown.setCooldown("primary");
					}			
				}
				
			}, 120l);
			
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
			
			
			if(secondaryOn) {
				if(secondaryCnt == 0) {
					ActionBarAPI.sendActionBar(p, "��2��l�ӻ� ���", 40);
					secondaryOn = false;
					p.setLevel(0);
					p.setExp(0);
					Bukkit.getScheduler().cancelTask(levelTimerid);
				} 
				return;
			}
			
			if(primaryOn) {
				if(primaryCnt == 0) {
					ActionBarAPI.sendActionBar(p, "��2��l��ȭ�� ���", 40);
					primaryOn = false;
					p.setLevel(0);
					p.setExp(0);
					Bukkit.getScheduler().cancelTask(levelTimerid);
				} 
			}
			
			ActionBarAPI.sendActionBar(p, "��2��l�ӻ� �����", 40);	
			p.getWorld().playSound(p.getLocation(), Sound.BLOCK_LADDER_PLACE, 1.5f, 0.5f);
			secondaryOn = true;
			secondaryCnt = 0;
			p.setLevel(4-secondaryCnt);
			
			levelTimerid = hrw.timerLevelBar(p, 8, false, false);
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(hrw.server, new Runnable() {
				
				public void run() {
					if(secondaryOn) {
						secondaryOn = false;
						p.setLevel(0);
						p.setExp(0);
						cooldown.setCooldown("secondary");
					}		
				}
				
			}, 160l);
			
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
			cooldown.setCooldown("ultimate");
			VirtualProjectile vp = new VirtualProjectile(hrw.server, p, getEnemyList(p.getName()));
			
			vp.c_speed = 2.5f;
			vp.c_distance= 70;
			vp.c_removeOnHit = false;
			
			vp.setOnDuring(new Runnable() {
				public void run() {
					vp.projectileLoc.getWorld().spawnParticle(Particle.REDSTONE, vp.projectileLoc, 1, 0.2F, 0.2f, 0.2f, 0.1f);
				}
			});
			
			List<Player> enemyList = getEnemyList(p.getName());
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WOLF_GROWL, 1.5f, 2.2f);
			
			vp.setOnNonHit(new Runnable() {
				public void run() {
					Location l = MyUtility.getGroundLocation(vp.projectileLoc).add(0,0.25,0);
					CircleEffect ef1 = new CircleEffect(effectManager);
					ef1.particle = Particle.REDSTONE;
					ef1.color = team.teamColor;
					ef1.angularVelocityX = 0;
					ef1.angularVelocityY = 0;
					ef1.angularVelocityZ = 0;
					ef1.particleSize = 1;
					ef1.wholeCircle = true;
					ef1.particleOffsetX = 0.1f;
					ef1.particleOffsetZ = 0.1f;
					ef1.particleOffsetY = 0.1f;
					ef1.particleCount = 3;
					ef1.radius = 5;
					ef1.period = 15;
					ef1.iterations = 5;
					ef1.setLocation(l);
					ef1.visibleRange = 16;
					ef1.start();
										
					for(Player t : enemyList) {
						if(t.getLocation().distance(l) <= 6) {
							TitleAPI.sendFullTitle(t, 0, 25, 0, ChatColor.RED+"����!", "����� �ü��� �ñر� �������� �ֽ��ϴ�!");
							p.playSound(p.getLocation(), Sound.BLOCK_NOTE_CHIME, 1.0f, 1.5f);
						}
					}
					
					EGScheduler sch = new EGScheduler(hrw);
					sch.schTime = 25;
					blockPerUlti = true;
					sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(hrw.server, new Runnable() {
						public void run() {
							//Bukkit.getLogger().info("id10");
							if(sch.schTime > 0) {
								Location l1 = l.clone();
								l1.add(MyUtility.getRandom(-4, 4),0,MyUtility.getRandom(-4, 4));
								
								VirtualProjectile vp1 = new VirtualProjectile(hrw.server, p, enemyList);
								vp1.c_speed = 3;
								vp1.c_distance = 15;
								vp1.c_startLoc = l1.add(0,10,0);
								vp1.c_time = 80;
								vp1.vector = new Vector(0, -1, 0);
								vp1.c_removeOnBlock = true;
								vp1.c_removeOnHit = true;
								
								vp1.setOnDuring(new Runnable() {
									public void run() {
										vp1.projectileLoc.getWorld().spawnParticle(Particle.REDSTONE, vp1.projectileLoc, 2, 0F, 0f, 0f, 0f);	
									}	
								});			
								
								vp1.setOnNonHit(new Runnable() {
									public void run() {
										vp1.projectileLoc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, vp1.projectileLoc, 3, 0.1F, 0.1f, 0.1f, 0.1f);
										vp1.projectileLoc.getWorld().playSound(vp1.projectileLoc, Sound.ENTITY_GENERIC_EXPLODE, 1.5f, 2.0f);
										for(Player t : enemyList) {
											int dis = (int)t.getLocation().distance(vp1.projectileLoc);
											if(dis <= 3) {
												customDamage(t, p, 20 - dis*4, false);
											}
										}
									}	
								});
								sch.schTime--;
								vp1.launchProjectile();
							} else {
								sch.cancelTask(true);
								p.getWorld().playSound(p.getLocation(), Sound.ENTITY_SHULKER_TELEPORT, 1.5f, 3.0f);
								blockPerUlti = false;
							}
						}
						
					}, 20l, 4l);
	
				}
			});
			
			
			vp.setOnHit(vp.onNonHit);
			
			vp.launchProjectile();
			
			
		}else {
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
		}
	}
	
	
	@Override
	public void onPlayerShotBow(EntityShootBowEvent e) {
		Arrow a = (Arrow) e.getProjectile();
		Player p = Bukkit.getPlayer(playerName);
		if(secondaryOn) {		
			secondaryCnt += 1;
			a.setSilent(true);
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0f, 1.2f);
			a.setVelocity(a.getVelocity().multiply(10));
			p.setLevel(4-secondaryCnt);
			if(secondaryCnt >= 4) {
				secondaryOn = false;
				secondaryCnt = 0;
				cooldown.setCooldown("secondary");
				Bukkit.getScheduler().cancelTask(levelTimerid);
				p.setLevel(0);
				p.setExp(0);	
			} 			
		}else if(primaryOn) {
			primaryCnt += 1;
			a.setBounce(true);
			if(primaryCnt >= 1) {
				primaryCnt = 0;
				primaryOn = false;
				cooldown.setCooldown("primary");
				Bukkit.getScheduler().cancelTask(levelTimerid);
				p.setLevel(0);
				p.setExp(0);								
			} 
		}
	}
	
	@Override
	public void onHitPlayer(EntityDamageByEntityEvent e) {
		Player d = Bukkit.getPlayer(playerName);
		Player p = (Player) e.getEntity();
		if(e.getCause() == DamageCause.ENTITY_ATTACK || e.getCause() == DamageCause.PROJECTILE) {
			if(hrw.playerMap.get(d.getName()).ability.isNoAttack()) {
				e.setCancelled(true);
				ActionBarAPI.sendActionBar(d, "��2��l���� �Ұ� �����Դϴ�.", 40);
				return;
			} 
			if(hrw.getHeldMainItemName(d).contains(abilityName)) {
				HRWPlayer victimHrwp = hrw.playerMap.get(p.getName());
				if(!p.getName().equalsIgnoreCase(d.getName())) {			
					victimHrwp.lastDamager = d.getName();
				}
				
				int resDmg = 0;
				if(e.getDamager() instanceof Arrow) {
					Arrow a = (Arrow) e.getDamager();
					if(a.doesBounce()) {
						if(passive) customFaint(p, d, (int)((e.getDamage()*2)/10)+1, false);
						else customBind(p, d, 1, false);
						d.setLevel(1-primaryCnt);
						resDmg = (int) (e.getDamage() * 2.5)+addtionDamage;
					} else if(a.isSilent()) {
						resDmg = 8+addtionDamage;
					} else {
						resDmg = (int) (e.getDamage() * 2.5)+addtionDamage;
					}
				} else {
					resDmg = dmg+addtionDamage;
				}
				hrwData.dealAmt += resDmg;
				int excep = victimHrwp.ability.onHitted(p, d, resDmg);
				if(excep != -1) {
					resDmg = excep;
				}				
				if(p.getHealth() - resDmg <= 0) {
					Ability ab = hrw.playerMap.get(p.getName()).ability;
					if(!p.isDead() && ab.tailsman_miracle && MyUtility.getRandom(0, 1) == 1) {
						ab.cooldown.setCooldown("tailsman_Miracle");
						customHeal(p, p, 8, true);
					} else {
						if(ab.backUpInvenMap == null) {
							if(hrw.hasItem(p, Material.BOOK, 1)) {
								ab.backUpInvenMap = p.getInventory().getContents();
								ab.backUpArmorMap = p.getInventory().getArmorContents();
							}
							if(tailsman_heal) customHeal(d, d, 20, true);
						}	
						p.setHealth(0);
						if(!blockPerUlti) per_Ultimate_Now += resDmg;
					}
				} else {
					p.setHealth(p.getHealth() - resDmg);
					if(!blockPerUlti) per_Ultimate_Now += resDmg;
					if(resDmg >= 1) {
						if(tailsman_heal) customHeal(d, d, 1, true);
					}
				}
			}
		}
		e.setDamage(0);
	}
	
	@Override
	public void onChangeHand(PlayerSwapHandItemsEvent e) {
		Player p = e.getPlayer();
		//if(p.isSneaking()) {
			primarySkill();
		//}
	}
	
	@Override
	public void onMouseClick(PlayerInteractEvent e) {
		Action action = e.getAction();
		Player p = e.getPlayer();
		if(action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
			if(p.isSneaking()) {
				secondarySkill();
			} 
		}
	}
	
}
