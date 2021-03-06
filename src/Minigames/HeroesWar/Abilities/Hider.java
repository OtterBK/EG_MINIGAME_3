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
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
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
import Minigames.HeroesWar.HRWPlayer;
import Minigames.HeroesWar.Team;
import Utility.MyUtility;
import de.slikey.effectlib.effect.DragonEffect;
import de.slikey.effectlib.effect.LineEffect;

public class Hider extends Ability{

	List<SidebarString> strList = new ArrayList<SidebarString>(7);
	//������ ����
	public boolean hiding;
	public Location setLoc;
	public boolean delay;
	public EGScheduler hideSch;
	
	//������ ����
	public int stat_hideHit = 0;
	
	public Hider(HRWBase hrw, Player p, String abilityName, Team team) {
		super(hrw, p, abilityName, team);
		cooldown = new MyCooldown(this);
		
		//������ ����
		dmg = 7;

		weapon = new ItemStack(Material.DIAMOND_HOE, 1);
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
		cooldown_secondarySkill = 9;
		cooldown_teritarySkill = 15;
		cooldown_ultimateSkill = 38;
		
		//ü�¼���
		health = 46;

		
		String teamName = hrw.getTeam(p);
		SidebarString line1 = new SidebarString("");
		sidebar = new Sidebar((teamName.equalsIgnoreCase("BLUE") ? (ChatColor.BLUE+""+ChatColor.BOLD) : (ChatColor.RED+""+ChatColor.BOLD) )+abilityName, hrw.server, 72000, line1);
		
		hideSch = new EGScheduler(hrw);
		
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
		p.openInventory(hrw.inven_desc_hider);
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
		statLoreList.add("��7���Ż��·� ���� ������ Ƚ�� : ��a"+stat_hideHit+"  ");
		statLoreList.add("��7���� ����Ų �ð� : ��a"+hrwData.weaknessTime+"  ");
		statLoreList.add("");
		meta.setLore(statLoreList);
		statItem.setItemMeta(meta);
	}
	
	@Override
	public void respawned() {
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p)) return;
		List<Player> tmpList = getEnemyList(p.getName());
		show(p, tmpList);
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
			if(hiding){
				tmpStr = "��aPrim ��6"+hideSch.schTime+"s";
			} else if(isNoSkill()) {
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
			} else if(setLoc != null) {
				tmpStr = "��aSecd ��6Set";
			} else if(cooldown.getLeftCooldown("secondary") > 0) {
				tmpStr = "��4Secd ��6"+cooldown.getLeftCooldown("secondary");
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
			if(hiding){
				tmpStr = "��aPrim ��6"+hideSch.schTime+"s";
			} else if(isNoSkill()) {
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
			} else if(setLoc != null) {
				tmpStr = "��aSecd ��6Set";
			} else if(cooldown.getLeftCooldown("secondary") > 0) {
				tmpStr = "��4Secd ��6"+cooldown.getLeftCooldown("secondary");
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
	
	private void hide(Player p, List<Player> targetList) {
		hiding = true;

		p.setGameMode(GameMode.ADVENTURE);
		block_noCatching = true;
		for(Player t : targetList) {	
			t.hidePlayer(p);				
		}
		p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 72000, 0));
		ActionBarAPI.sendActionBar(p, ChatColor.RED+""+ChatColor.BOLD+"����� ����ϴ�.", 70);
		p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 4.0f);
		p.getWorld().spawnParticle(Particle.CLOUD, p.getLocation(), 3, 0.1F, 0.1f, 0.2f, 0.1f);
		
		hideSch.schTime = 25;
		hideSch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(hrw.server, new Runnable() {
			public void run() {
				if(!hiding) hideSch.cancelTask(false);
				if(hideSch.schTime > 0) {
					hideSch.schTime--;
					for(Player t : targetList) {	
						t.hidePlayer(p);				
					}
				}else {
					show(p, targetList);
				}
			}
		}, 0l, 20l);
	}
	
	private void show(Player p, List<Player> targetList) {
		hiding = false;
		hideSch.cancelTask(false);
		p.setGameMode(GameMode.SURVIVAL);
		block_noCatching = false;
		for(Player t : targetList) {	
			t.showPlayer(p);				
		}
		ActionBarAPI.sendActionBar(p, ChatColor.RED+""+ChatColor.BOLD+"����� �巯���ϴ�.", 70);
		p.removePotionEffect(PotionEffectType.INVISIBILITY);
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 4.0f);
		cooldown.setCooldown("primary");
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
			if(delay) return; 
			List<Player> tmpList = getEnemyList(p.getName());
			
			if(hiding) {
				show(p, tmpList);
			} else {
				delay = true;
				Bukkit.getScheduler().scheduleSyncDelayedTask(hrw.server, new Runnable() {
					public void run() {
						delay = false;
					}
				},10);
				hide(p, tmpList);
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
			if(delay) return; 
			if(setLoc != null) {
				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_SHULKER_TELEPORT, 1.2f, 2.0f);
				p.getWorld().spawnParticle(Particle.CLOUD, p.getLocation(), 2, 0.1F, 0.1f, 0.2f, 0.1f);
				p.teleport(setLoc.add(0,1,0), TeleportCause.PLUGIN);
				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_SHULKER_TELEPORT, 1.2f, 2.0f);
				p.getWorld().spawnParticle(Particle.PORTAL, p.getLocation(), 2, 0.1F, 0.1f, 0.2f, 0.1f);
				setLoc = null;
				cooldown.setCooldown("secondary");
				if(hiding) {
					List<Player> tmpList = getEnemyList(p.getName());
					show(p, tmpList);
				}
			} else {
				setLoc = p.getLocation();
				p.getWorld().playSound(p.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1.2f, 0.5f);
				p.getWorld().spawnParticle(Particle.SMOKE_LARGE, p.getLocation(), 3, 0.3F, 0.3f, 0.3f, 0.1f);
				ActionBarAPI.sendActionBar(p, ChatColor.RED+""+ChatColor.BOLD+"��� ���� �Ϸ�.", 70);
				delay = true;
				Bukkit.getScheduler().scheduleSyncDelayedTask(hrw.server, new Runnable() {
					public void run() {
						delay = false;
					}
				},10);
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
		
		if(cooldown.checkCooldown("ultimate")){
			if(isNoSkill()) {
				ActionBarAPI.sendActionBar(p, "��2��l��ų ��� �Ұ� �����Դϴ�.", 40);
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
				return;
			}
			cooldown.setCooldown("Ultimate");
			blockPerUlti = true;
			if(hiding) {
				List<Player> tmpList = getEnemyList(p.getName());
				show(p, tmpList);
			}
			
			p.getWorld().playSound(p.getLocation(), Sound.BLOCK_CLOTH_BREAK, 1.2f, 2.4f);			

			Location l = p.getEyeLocation();
			l.setYaw(l.getYaw() - 85);
			l = l.add(l.getDirection().multiply(1.5f));
			
			Location t = p.getEyeLocation();
			t.setYaw(t.getYaw() + 85);
			t = t.add(t.getDirection().multiply(1.5f));
			
			final Location l1 = l.clone();
			final Location t1 = t.clone();

			
			LineEffect ef = new LineEffect(effectManager);
			ef.particle = Particle.SMOKE_NORMAL;
			ef.iterations = 1;
			ef.setLocation(l.add(0,-0.7f,0).add(p.getEyeLocation().getDirection()));
			ef.setTargetLocation(t.add(0,0.7f,0).add(p.getEyeLocation().getDirection()));
			ef.start();
			
			Vector v = p.getEyeLocation().getDirection();
			v.setY(0);
			v.normalize();
			DragonEffect ef2 = new DragonEffect(effectManager);
			ef2.period = 1;
			ef2.iterations = 10;
			ef2.setLocation(p.getEyeLocation().add(v));
			ef2.particle = Particle.SMOKE_NORMAL;
			v.multiply(5);
			ef2.setTargetLocation(p.getEyeLocation().add(v));
			ef2.start();
			
			List<Player> hit = hrw.getCorsurPlayer(p, getEnemyList(p.getName()), 4, false);
			if(hit != null) {
				Player t2 = hit.get(0);
				customDamage(t2, p, 6, false);
				blockPerUlti = false;
			} 
			
	
			Bukkit.getScheduler().scheduleSyncDelayedTask(hrw.server, new Runnable() {
				public void run() {
					p.getWorld().playSound(p.getLocation(), Sound.BLOCK_CLOTH_BREAK, 1.2f, 2.4f);
					LineEffect ef = new LineEffect(effectManager);
					ef.particle = Particle.SMOKE_NORMAL;
					ef.iterations = 1;
					ef.setLocation(l1.add(0,0.7f,0).add(p.getEyeLocation().getDirection()));
					ef.setTargetLocation(t1.add(0,-0.7f,0).add(p.getEyeLocation().getDirection()));
					ef.start();
					
					List<Player> hit = hrw.getCorsurPlayer(p, getEnemyList(p.getName()), 4, false);
					if(hit != null) {
						Player t = hit.get(0);
						customDamage(t, p, 6, false);
						blockPerUlti = false;
					} 
				}

			},3l);
			
			final Location l2 = l.clone();
			final Location t2 = t.clone();
				Bukkit.getScheduler().scheduleSyncDelayedTask(hrw.server, new Runnable() {
					public void run() {
						p.getWorld().playSound(p.getLocation(), Sound.BLOCK_CLOTH_BREAK, 1.2f, 2.4f);
						LineEffect ef = new LineEffect(effectManager);
						ef.particle = Particle.SMOKE_NORMAL;
						ef.iterations = 1;
						ef.setLocation(l2.add(0,0,0).add(p.getEyeLocation().getDirection()));
						ef.setTargetLocation(t2.add(0,0,0).add(p.getEyeLocation().getDirection()));
						ef.start();
						
						List<Player> hit = hrw.getCorsurPlayer(p, getEnemyList(p.getName()), 4, false);
						if(hit != null) {
							Player t = hit.get(0);
							customDamage(t, p, 6, false);
							blockPerUlti = false;
						} 
					}

				},6l);
				
				
				Bukkit.getScheduler().scheduleSyncDelayedTask(hrw.server, new Runnable() {
					public void run() {	
						p.getWorld().playSound(p.getLocation(), Sound.BLOCK_CLOTH_BREAK, 1.2f, 2.4f);
						LineEffect ef = new LineEffect(effectManager);
						Location l4 = p.getLocation().add(p.getLocation().getDirection().multiply(0.75f));
						ef.particle = Particle.SMOKE_NORMAL;
						ef.iterations = 1;
						ef.setLocation(l4.add(p.getEyeLocation().getDirection()));
						ef.setTargetLocation(l4.add(0,0.7,0).add(p.getEyeLocation().getDirection()));
						ef.start();
						
						List<Player> hit = hrw.getCorsurPlayer(p, getEnemyList(p.getName()), 4, false);
						if(hit != null) {
							Player t = hit.get(0);
							customDamage(t, p, 6, false);
							blockPerUlti = false;
						} 
					}

				},9l);
				
				Bukkit.getScheduler().scheduleSyncDelayedTask(hrw.server, new Runnable() {
					public void run() {
						p.getWorld().playSound(p.getLocation(), Sound.BLOCK_CLOTH_BREAK, 1.2f, 2.4f);
						LineEffect ef = new LineEffect(effectManager);
						ef.particle = Particle.SMOKE_NORMAL;
						ef.iterations = 1;
						ef.setLocation(p.getEyeLocation().add(p.getEyeLocation().getDirection()));
						ef.setTargetLocation(p.getEyeLocation().add(p.getEyeLocation().getDirection().multiply(2)).add(p.getEyeLocation().getDirection()));
						ef.start();
						
						List<Player> hit = hrw.getCorsurPlayer(p, getEnemyList(p.getName()), 4, false);
						if(hit != null) {
							Player t = hit.get(0);
							customDamage(t, p, 6, false);
							blockPerUlti = false;
						} 	
					}

				},12l);
			
		} else {
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
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
				e.setDamage(0);
				int resDmg = dmg+addtionDamage;
				if(hiding) {
					List<Player> tmpList = getEnemyList(d.getName());
					show(d, tmpList);
					stat_hideHit += 1;
					ActionBarAPI.sendActionBar(d, ChatColor.RED+""+ChatColor.BOLD+"����� �巯���ϴ�.", 70);
					d.getWorld().playSound(d.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 4.0f);
					cooldown.setCooldown("primary");
					d.removePotionEffect(PotionEffectType.INVISIBILITY);
					resDmg = dmg+6; //���� �߰� ����
					if(passive && !isNoSkill()) {
						customWeakness(p, d, 1, false);
					}
				}
				
				HRWPlayer victimHrwp = hrw.playerMap.get(p.getName());
				if(!p.getName().equalsIgnoreCase(d.getName())) {			
					victimHrwp.lastDamager = d.getName();
					hrwData.dealAmt += resDmg;
				}
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
						//if(tailsman_heal) customHeal(d, d, 1, true);
					}
				}
			}else {
				e.setCancelled(true);
			}
		}
	}
	
	@Override
	public int onHitted(Player victim, Player damager, int damage) {
		if(invincible) return 0;
		Player p = Bukkit.getPlayer(playerName);
		if(hiding) {
			List<Player> tmpList = getEnemyList(p.getName());
			show(p, tmpList);
		}	
		int calcDamage = tankerCheck(victim, damager ,damage);
		return calcDamage; //-1�� �������, �� �ܴ� ü���� �̰ɷ� �����ش޶�
	}
}
