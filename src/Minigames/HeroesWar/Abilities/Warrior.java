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
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.coloredcarrot.api.sidebar.Sidebar;
import com.coloredcarrot.api.sidebar.SidebarString;
import com.connorlinfoot.actionbarapi.ActionBarAPI;

import Minigames.HeroesWar.HRWBase;
import Minigames.HeroesWar.Team;
import Utility.MyUtility;
import Utility.VirtualProjectile;
import de.slikey.effectlib.effect.CircleEffect;
import de.slikey.effectlib.effect.LineEffect;
import de.slikey.effectlib.effect.TornadoEffect;

public class Warrior extends Ability{

	List<SidebarString> strList = new ArrayList<SidebarString>(7);
	
	private int shield = 0;
	private int maxShield = 0;
	private int stat_shield = 0;
	
	public Warrior(HRWBase hrw, Player p, String abilityName, Team team) {
		super(hrw, p, abilityName, team);
		cooldown = new MyCooldown(this);
		
		//������ ����
		dmg = 6;

		weapon = new ItemStack(Material.IRON_SWORD, 1);
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
		cooldown_primarySkill = 12;
		cooldown_secondarySkill = 8;
		cooldown_teritarySkill = 24;
		cooldown_ultimateSkill = 60;
		
		//ü�¼���
		health = 82;

		
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
		p.openInventory(hrw.inven_desc_warrior);
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
		statLoreList.add("��7���� �⼳��Ų �ð� : ��a"+hrwData.faintTime+"��"+"  ");
		statLoreList.add("��7���� ��� Ƚ�� : ��a"+hrwData.airboneCnt+"ȸ"+"  ");
		statLoreList.add("��7�� ������ : ��a"+stat_shield+"  ");
		statLoreList.add("");
		meta.setLore(statLoreList);
		statItem.setItemMeta(meta);
	}
	
	public int calcDamage(Player victim , Player damager, int damage) {
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p)) return -1;
		if(shield <= 0) return -1; //���� ������ ����
		if(victim.getName().equalsIgnoreCase(p.getName()) && !p.isDead()) { //�����ʾҰ� �����ڰ� �ڽ��̸�		
			shield -= damage;
			if(shield <= 0) {
				shield = 0;
				victim.getWorld().playSound(victim.getLocation(), Sound.ITEM_SHIELD_BREAK, 1.0f, 2.0f);
			}else {
				victim.getWorld().playSound(victim.getLocation(), Sound.ITEM_SHIELD_BLOCK, 1.0f, 0.5f);	
			}
			updateShield(p);
			return 0;
		}
		return -1;
	}
	
	public void updateShield(Player p) {
		if(maxShield == 0) return;
		if(exitsPlayer(p) && hrw.ingamePlayer.contains(p.getName())) {
			p.setLevel(shield);
			float percent = (float)shield/maxShield;
			if(percent < 0) percent = 0;
			if(percent > 1) percent = 1;
			p.setExp(percent);
		}
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
			p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_PLACE, 2.0f, 4.0f);
			p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, p.getLocation(), 5, 0.5F, 0.5f, 0.5f, 0.1f);
			
			
			Location l = p.getLocation();
			Vector v = l.getDirection();
			Block b = p.getTargetBlock(null, 20);
			if(b.getType() != Material.AIR) {
				v = b.getLocation().toVector().subtract(l.toVector());
				v.setY(0);
			}else {
				v.setY(0);
				v.normalize();
				v.multiply(3.25f);
			}
			p.setVelocity(v);
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(hrw.server, new Runnable() {
				public void run() {
					for(int i = 1; i <= 4; i++) {
						CircleEffect ef1 = new CircleEffect(effectManager);
						ef1.particle = Particle.REDSTONE;
						ef1.color = Color.fromRGB(247, 254, 46);
						ef1.angularVelocityX = 0;
						ef1.angularVelocityY = 0;
						ef1.angularVelocityZ = 0;
						ef1.particleSize = 1;
						ef1.wholeCircle = true;
						ef1.particleOffsetX = 0.1f;
						ef1.particleOffsetZ = 0.1f;
						ef1.particleOffsetY = 0.1f;
						ef1.particleCount = 10;
						ef1.radius = i;
						ef1.period = 5;
						ef1.iterations = 1;
						ef1.setLocation(p.getLocation().add(0,1,0));
						ef1.start();
					}
					p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 2.0f, 1.25F);
					Location l = p.getLocation();
					for(Player t : getEnemyList(playerName)) {
						if(t.getLocation().distance(l) <= 4f && Math.abs(t.getLocation().getY() - l.getY()) <= 2) {
							customDamage(t, p, 6, false);
							customAirbon(t, p, 2f, false);
							if(passive) customWeakness(t, p, 2, false);
						}
					}
				}
			}, 20l);
			
		} else {
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
		}
	}
	
	public void secondarySkill(){
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p)) return;
		if(!p.getInventory().getItemInMainHand().equals(weapon)) return;
		if(isNoSkill()) {
			ActionBarAPI.sendActionBar(p, "��c��ų ��� �Ұ� �����Դϴ�.", 40);
			return;
		}
		if(cooldown.checkCooldown("secondary") && !p.isDead()){
			cooldown.setCooldown("secondary");
			
			Location tmp = p.getEyeLocation();
			tmp.setYaw(tmp.getYaw()-85);
			Vector direction1 = tmp.getDirection();
			direction1.setY(0);
			direction1.multiply(2.4f);
			tmp = p.getEyeLocation();
			tmp.setYaw(tmp.getYaw()+85);
			Vector direction2 = tmp.getDirection();
			direction2.setY(0);
			direction2.multiply(2.4f);
			
			List<Player> enemyList = getEnemyList(p.getName());
			p.getWorld().playSound(p.getLocation(), Sound.BLOCK_SHULKER_BOX_OPEN, 1.0f, 2.0F);
			VirtualProjectile vp = new VirtualProjectile(hrw.server, p, enemyList);
			
			vp.c_speed = 3f;
			vp.c_distance = 3;
			vp.c_time = 20;
			vp.c_pHorizenOffset = 5.0f;
			vp.c_removeOnHit = false;
			
			vp.setOnDuring(new Runnable() {
				public void run() {
					Location l1 = vp.projectileLoc.clone();
					l1.add(direction1);
					
					Location l2 = vp.projectileLoc.clone();
					l2.add(direction2);
					
					LineEffect ef = new LineEffect(effectManager);
					ef.particle = Particle.REDSTONE;
					ef.color = Color.fromRGB(247, 254, 46);
					ef.iterations = 1;
					ef.setLocation(l1);
					ef.setTargetLocation(l2);
					ef.start();
					
					//vp.projectileLoc.getWorld().spawnParticle(Particle.FLAME, vp.projectileLoc, 1, 0F, 0f, 0f, 0f);
				}
			});
			
			vp.setOnHit(new Runnable() {
				public void run() {
					for(Player hit : vp.hitPlayer) {
						hit.getWorld().spawnParticle(Particle.FALLING_DUST, hit.getLocation(), 10, 0.1F, 0.1f, 0.1f, 0.1f);
						hit.getWorld().playSound(hit.getLocation(), Sound.BLOCK_CHEST_LOCKED, 1.0f, 0.1f);
						customDamage(hit, p, 7, false);
						customBind(hit, p, 2, false);
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
			
			Location l = p.getLocation();
			shield = 10;
			for(Player t : getEnemyList(playerName)) {
				if(t.getLocation().distance(l) <= 4) {
					shield += 10;
				}
			}
			maxShield = shield;
			stat_shield += maxShield;
			p.getWorld().playSound(p.getLocation(), Sound.BLOCK_IRON_TRAPDOOR_OPEN, 1.2f, 0.5f);
			updateShield(p);
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(hrw.server, new Runnable() {
				public void run() {
					shield = 0;
					if(exitsPlayer(p) && hrw.ingamePlayer.contains(p.getName())) {
						updateShield(p);
						p.playSound(p.getLocation(), Sound.BLOCK_IRON_DOOR_CLOSE, 1.0f, 1.0f);
					}
				}
			}, 200l);
			
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
			
			TornadoEffect particle = new TornadoEffect(effectManager);
			particle.setLocation(p.getLocation());
			particle.setTargetLocation(p.getLocation().add(0,5,0));
			particle.tornadoParticle = Particle.REDSTONE;
			particle.tornadoColor = Color.fromRGB(247, 254, 46);
			particle.period = 30;
			particle.iterations = 1;
			particle.maxTornadoRadius = 5f;
			particle.start();		
			
			Location l = p.getLocation();
			l.getWorld().playSound(l, Sound.ITEM_SHIELD_BREAK, 1.0f, 0.1f);
			l.getWorld().playSound(l, Sound.ENTITY_SPLASH_POTION_BREAK, 1.0f, 0.5f);
			
			for(Player t : getEnemyList(playerName)) {
				if(t.getLocation().distance(l) <= 5 && Math.abs(t.getLocation().getY() - l.getY()) <= 2) {
					customDamage(t, p, (int)p.getHealth()/3, false);
					customAirbon(t, p, 2.5f, false);
					customWeakness(t, p, 2, false);
					t.getWorld().playSound(t.getLocation(), Sound.ENTITY_SPLASH_POTION_BREAK, 1.0f, 0.5f);
				}			
			}
		}else {
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
		}
	}
	
}
