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
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.coloredcarrot.api.sidebar.Sidebar;
import com.coloredcarrot.api.sidebar.SidebarString;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.nametagedit.plugin.NametagEdit;

import EGServer.ServerManager.EGScheduler;
import Minigames.HeroesWar.HRWBase;
import Minigames.HeroesWar.Team;
import Utility.MyUtility;
import Utility.VirtualProjectile;
import de.slikey.effectlib.effect.LineEffect;

public class Virtuous extends Ability{

	List<SidebarString> strList = new ArrayList<SidebarString>(7);
	
	//��������
	public int maxEnergy = 6;
	public int energy = 6;
	public int energyTimerId;
	public boolean usingUltimate = false;
	
	//���� ����
	public int stat_flyTime = 0;
	
	public Virtuous(HRWBase hrw, Player p, String abilityName, Team team) {
		super(hrw, p, abilityName, team);
		cooldown = new MyCooldown(this);	
		
		//������ ����
		dmg = 3;
		
		weapon = new ItemStack(Material.GOLD_HOE, 1);
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
		cooldown_primarySkill = 10;
		cooldown_secondarySkill = 2;
		cooldown_teritarySkill = 16;
		cooldown_ultimateSkill = 102;
		
		//ü�¼���
		health = 40;

		
		String teamName = hrw.getTeam(p);
		SidebarString line1 = new SidebarString("");
		sidebar = new Sidebar((teamName.equalsIgnoreCase("BLUE") ? (ChatColor.BLUE+""+ChatColor.BOLD) : (ChatColor.RED+""+ChatColor.BOLD) )+abilityName, hrw.server, 72000, line1);
		
		setEquipment(p);
		
		virtuousTimer();
		
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
		p.openInventory(hrw.inven_desc_virtuous);
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
		statLoreList.add("��7ü�� �ð� : ��a"+stat_flyTime+"��"+"  ");
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
		virtuousTimer();
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
			
			tmpStr = "��aEnergy ��6"+energy;
			line = new SidebarString(tmpStr);
			strList.add(line);
			
			if(isNoSkill()) {
				tmpStr = "��4Prim ��6Blocked";
			} else if(cooldown.getLeftCooldown("secondary") > 0) {
				tmpStr = "��4Prim ��6"+cooldown.getLeftCooldown("secondary");
			} else if(usingUltimate) {
				tmpStr = "��4Prim ��6Blocked";
			} else {
				tmpStr = "��aPrim ��6Ready";
			}
			
			
			line = new SidebarString(tmpStr);
			strList.add(line);
			
			if(isNoSkill()) {
				tmpStr = "��4Secd ��6Blocked";
			} else if(cooldown.getLeftCooldown("teritary") > 0) {
				tmpStr = "��4Secd ��6"+cooldown.getLeftCooldown("teritary");
			} else if(usingUltimate) {
				tmpStr = "��4Secd ��6Blocked";
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
			
			tmpStr = "��aEnergy ��6"+energy;
			line = new SidebarString(tmpStr);
			strList.add(line);
			
			if(isNoSkill()) {
				tmpStr = "��4Prim ��6Blocked";
			} else if(cooldown.getLeftCooldown("secondary") > 0) {
				tmpStr = "��4Prim ��6"+cooldown.getLeftCooldown("secondary");
			} else if(usingUltimate) {
				tmpStr = "��4Prim ��6Blocked";
			} else {
				tmpStr = "��aPrim ��6Ready";
			}
			
			
			line = new SidebarString(tmpStr);
			strList.add(line);
			
			if(isNoSkill()) {
				tmpStr = "��4Secd ��6Blocked";
			} else if(cooldown.getLeftCooldown("teritary") > 0) {
				tmpStr = "��4Secd ��6"+cooldown.getLeftCooldown("teritary");
			} else if(usingUltimate) {
				tmpStr = "��4Secd ��6Blocked";
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
	
	public void primarySkill(){
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p)) return;
		if(isNoSkill()) {
			ActionBarAPI.sendActionBar(p, "��2��l��ų ��� �Ұ� �����Դϴ�.", 40);
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
			return;
		}
		if(usingUltimate) {
			ActionBarAPI.sendActionBar(p, "��2��l�ñر� ����߿��� ��ų����� �Ұ����մϴ�.", 40);
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
			return;
		}
		if(isNoMove()) {
			ActionBarAPI.sendActionBar(p, ChatColor.RED+""+ChatColor.BOLD+"�̵��Ұ� �����Դϴ�.", 40);
			return;
		}
		if(cooldown.checkCooldown("primary") && !p.isDead()){
			if(energy > 0) {
				p.setVelocity(new Vector(0,0.88f,0));
				energy -= 1;
				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_RABBIT_JUMP, 1.0f, 0.25f);
				p.setLevel(energy);
				p.setExp((float)energy/(passive ? maxEnergy + 3 : maxEnergy));
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
		if(usingUltimate) {
			ActionBarAPI.sendActionBar(p, "��2��l�ñر� ����߿��� ��ų����� �Ұ����մϴ�.", 40);
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
			return;
		}
		if(cooldown.checkCooldown("secondary") && !p.isDead()){
			cooldown.setCooldown("secondary");
			
			List<Player> enemyList = getEnemyList(p.getName());
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_SLIME_JUMP, 1.0f, 0.25f);
			VirtualProjectile vp = new VirtualProjectile(hrw.server, p, enemyList);
			vp.c_speed = 1.8f;
			vp.c_distance = 40;
			vp.c_time = 100;
			vp.c_removeOnHit = false;
			
			vp.setOnDuring(new Runnable() {
				public void run() {
					vp.projectileLoc.getWorld().spawnParticle(Particle.CLOUD, vp.projectileLoc, 1, 0F, 0f, 0f, 0f);
				}
			});
			
			vp.setOnNonHit(new Runnable() {
				public void run() {
					vp.projectileLoc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, vp.projectileLoc, 1, 0.1F, 0.1f, 0.1f, 0.1f);
					vp.projectileLoc.getWorld().playSound(vp.projectileLoc, Sound.ENTITY_GENERIC_EXPLODE, 0.5f, 3.0f);
					p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.5f, 3.0f);
					for(Player t : enemyList) {
						int dis = (int)t.getLocation().distance(vp.projectileLoc);
				
						if(dis <= (passive ? 6:4)) {
							customDamage(t, p, 10-dis, false);
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
		if(usingUltimate) {
			ActionBarAPI.sendActionBar(p, "��2��l�ñر� ����߿��� ��ų����� �Ұ����մϴ�.", 40);
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
			return;
		}
		if(cooldown.checkCooldown("teritary") && !p.isDead()){
			if(isNoMove()) {
				ActionBarAPI.sendActionBar(p, ChatColor.RED+""+ChatColor.BOLD+"�̵��Ұ� �����Դϴ�.", 40);
				return;
			}
			if(p.isOnGround()) {
				cooldown.setCooldown("teritary");
				p.setVelocity(new Vector(0,2.5f,0));
				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_AMBIENT, 1.0f, 1.5f);
								
				LineEffect ef = new LineEffect(effectManager);
				Location l = p.getLocation();
				ef.particle = Particle.CLOUD;
				ef.period = 5;
				ef.iterations = 4;
				ef.setLocation(l);
				ef.setTargetEntity(p);
				ef.start();
			} else {
				ActionBarAPI.sendActionBar(p, ChatColor.RED+""+ChatColor.BOLD+"���鿡���� ��� �����մϴ�.", 70);
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
			}
		}else {
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
		}
			
	}
	
	public void ultimateSkill(){
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p) || !p.isOnline()) return;
		if(!ultimate) {
			ActionBarAPI.sendActionBar(p, ChatColor.RED+""+ChatColor.BOLD+"���� �ñر⸦ �����ϼž��մϴ�.", 70);
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
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
			usingUltimate = true;
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 80, 254));
			
			List<Player> enemyList = getEnemyList(p.getName());
			p.setAllowFlight(true);	
			p.setFlying(true);
			p.setFlySpeed(0.02f);
			p.setVelocity(new Vector(0,0,0));
			EGScheduler sch = new EGScheduler(hrw);
			sch.schTime = 20;
			sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(hrw.server, new Runnable() {
				public void run() {
					if(p.isDead()) sch.schTime = 0;
					if(sch.schTime > 0) {
						
						p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_HURT, 1.0f, 0.25f);
						
						Vector v = p.getEyeLocation().getDirection().multiply(0.5f);
						Location l = p.getEyeLocation().add(v);
						
						l.setYaw(l.getYaw() + MyUtility.getRandom(-22, 22)); //��ź �þ� ����
						l.setPitch(l.getPitch() + MyUtility.getRandom(-22, 22)); //��ź �þ� ����
						
						VirtualProjectile vp1 = new VirtualProjectile(hrw.server, p, enemyList);
						vp1.c_speed = 1.2f;
						vp1.c_distance = 40;
						vp1.c_startLoc = l;
						vp1.c_time = 80;
						vp1.c_removeOnHit = false;
						vp1.vector = l.getDirection();
						
						
						vp1.setOnDuring(new Runnable() {
							public void run() {
								vp1.projectileLoc.getWorld().spawnParticle(Particle.CLOUD, vp1.projectileLoc, 1, 0F, 0f, 0f, 0f);	
							}	
						});			
						
						vp1.setOnNonHit(new Runnable() {
							public void run() {
								vp1.projectileLoc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, vp1.projectileLoc, 1, 0.1F, 0.1f, 0.1f, 0.1f);
								vp1.projectileLoc.getWorld().playSound(vp1.projectileLoc, Sound.ENTITY_GENERIC_EXPLODE, 1.5f, 2.0f);
								for(Player t : enemyList) {
									int dis = (int)t.getLocation().distance(vp1.projectileLoc);
									if(dis <= (passive ? 6:4)) {
										customDamage(t, p, 10 - dis, false);
									}
								}
							}	
						});
						sch.schTime--;
						vp1.launchProjectile();
					} else {
						p.setAllowFlight(false);	
						p.setFlying(false);
						p.setFlySpeed(0.1f);
						sch.cancelTask(true);
						p.getWorld().playSound(p.getLocation(), Sound.ENTITY_SHULKER_CLOSE, 1.5f, 3.0f);
						usingUltimate = false;
						blockPerUlti = false;
					}
				}
				
			}, 0l, 4l);
			
		}else {
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
		}
	}
	
	public void virtuousTimer() {
		energyTimerId = Bukkit.getScheduler().scheduleSyncRepeatingTask(hrw.server, new Runnable() {
			public void run() {
				if(!hrw.ingamePlayer.contains(playerName)) Bukkit.getScheduler().cancelTask(energyTimerId);
				if(!hrw.ingame) Bukkit.getScheduler().cancelTask(energyTimerId);
				if(energy < (passive ? maxEnergy + 3 : maxEnergy)) {
					Player p = Bukkit.getPlayer(playerName);
					if(p == null) return;
					if(p.isOnGround()) {
						energy += 1;
						p.setLevel(energy);
						p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0f, 2.0f);
						p.setExp((float)energy/(passive ? maxEnergy + 3 : maxEnergy));
					} else {
						Location groundLoc = MyUtility.getGroundLocation(p.getLocation().clone());
						if(p.getLocation().getY() - groundLoc.getY() > 2) {
							stat_flyTime += 1;
						}
					}
				}
			}
		}, 0l, 20l);
	}
	
	@Override
	public void onEntityDamaged(EntityDamageEvent e) {
		if(invincible) e.setCancelled(true);
		if(e.getCause() == DamageCause.FALL)
			e.setCancelled(true);
	}
	
	@Override
	public void onMouseClick(PlayerInteractEvent e) {
		Action action = e.getAction();
		Player p = e.getPlayer();
		if(!p.getInventory().getItemInMainHand().equals(weapon)) return;
		if(action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
			//if(MyUtility.getGroundLocation(p.getLocation()).distance(p.getLocation()) > 5) {
				secondarySkill();
			//} else {
			//	ActionBarAPI.sendActionBar(p, ChatColor.RED+""+ChatColor.BOLD+"���߿����� ��� �����մϴ�.", 70);
				//p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
			//}
		} else if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
			primarySkill();
		}
	}
	
}
