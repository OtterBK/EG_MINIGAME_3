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
	
	////어쌔신 전용
	public ItemStack ultimate_weapon;
	public boolean using_ultimate;
	
	public Assassin(HRWBase hrw, Player p, String abilityName, Team team) {
		super(hrw, p, abilityName, team);
		cooldown = new MyCooldown(this);
			
		//데미지 설정
		dmg = 9;
		
		weapon = new ItemStack(Material.DIAMOND_AXE, 1);
		ItemMeta meta = weapon.getItemMeta();
		meta.setDisplayName("§7[ §6"+abilityName+" §7]");
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		meta.setUnbreakable(true);
		List<String> loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§6공격력 §7: §d"+dmg);
		loreList.add("");
		meta.setLore(loreList);
		weapon.setItemMeta(meta);
		
		ultimate_weapon = new ItemStack(Material.DIAMOND_SWORD, 1);
		meta = ultimate_weapon.getItemMeta();
		meta.setDisplayName("§7[ §6"+abilityName+" §7]");
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		meta.setUnbreakable(true);
		ultimate_weapon.setItemMeta(meta);
		
		///쿨타임 설정
		cooldown_primarySkill = 3;
		cooldown_secondarySkill = 6;
		cooldown_teritarySkill = 9;
		cooldown_ultimateSkill = 88;
		
		//특정 경우만
		cooldown_passiveSkill = 2;
		
		//체력설정
		health = 60;
		
		String teamName = hrw.getTeam(p);
		SidebarString line1 = new SidebarString("");
		sidebar = new Sidebar((teamName.equalsIgnoreCase("BLUE") ? (ChatColor.BLUE+""+ChatColor.BOLD) : (ChatColor.RED+""+ChatColor.BOLD) )+abilityName, hrw.server, 72000, line1);
		
		setEquipment(p);
		
		p.setAllowFlight(true);
		
		makeStatItem();
	}
	
	public void helpMsg(Player p) {
		p.sendMessage("§6============= §f[ §b자객 §f] - §e직업군 §7: §cDealer §6=============\n"
				+ "§f- §a주스킬 §7: §d축지\n§7전방 5칸앞으로 이동합니다. §c벽을 뚫지는 못합니다. 3초마다 잔여 횟수가 1증가합니다.(최대 4회)\n"
				+ "§f- §a보조스킬 §7: §d역행\n§75초전의 시간으로 이동합니다. §c체력, 위치가 5초전으로 복구됩니다.\n"
				+ "§f- §2궁극기§f(§c구매시 사용가능§f) §7: §d점착폭탄\n§72칸 범위내에 바라보는 플레이어 또는 자신의 위치에 폭탄을 부착합니다. 2.0초후 폭발합니다.\n"
				+ "§f- §2패시브§f(§c구매시 사용가능§f) §7: §d빠른축지\n§7에너지를 더 빠르게 모읍니다. 축지의 횟수가 회복되는 간격이 3초에서 2.4초로 감소합니다.");	
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
		statLoreList.add("§7영웅 : §a"+abilityName+"  ");
		statLoreList.add("");
		statLoreList.add("§7K/D : §a"+hrwData.kill + "§f /§a "+hrwData.death+"  ");
		statLoreList.add("§7적에게 준 피해량 : §a"+hrwData.dealAmt+"  ");
		statLoreList.add("§7최대 연속 처치 : §a"+hrwData.maxStackKill+"  ");
		statLoreList.add("");
		statLoreList.add("§7적을 중독에 빠뜨린 시간 : §a"+hrwData.poisonTime+"초"+"  ");
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
				tmpStr = "§4Main §6"+nowCooldown_primarySkill;
			} else {
				tmpStr = "§aMain §6Ready";
			}*/
			
			if(isNoSkill()) {
				tmpStr = "§4Prim §6Blocked";
			} else if(cooldown.getLeftCooldown("primary") > 0) {
				tmpStr = "§4Prim §6"+cooldown.getLeftCooldown("primary");
			} else {
				tmpStr = "§aPrim §6Ready";
			}
			
			
			line = new SidebarString(tmpStr);
			strList.add(line);
			
			if(isNoSkill()) {
				tmpStr = "§4Secd §6Blocked";
			} else if(cooldown.getLeftCooldown("secondary") > 0) {
				tmpStr = "§4Secd §6"+cooldown.getLeftCooldown("secondary");
			} else {
				tmpStr = "§aSecd §6Ready";
			}
			
			
			line = new SidebarString(tmpStr);
			strList.add(line);
			
			if(isNoSkill()) {
				tmpStr = "§4Tert §6Blocked";
			} else if(cooldown.getLeftCooldown("teritary") > 0) {
				tmpStr = "§4Tert §6"+cooldown.getLeftCooldown("teritary");
			} else {
				tmpStr = "§aTert §6Ready";
			}
			line = new SidebarString(tmpStr);
			strList.add(line);
			
			if(isNoSkill()) {
				tmpStr = "§4Ulmt §6Blocked";
			} else if(ultimate) {
				if(percentMode) {
					int percent = cooldown.getUltimatePercent();
					if(percent >= 100) {
						tmpStr = "§aUlmt §6Ready";
					}else {
						tmpStr = "§4Ulmt §6"+percent+"%";
					}
				}else {
					if(cooldown.getLeftCooldown("ultimate") > 0) {
						tmpStr = "§4Ulmt §6"+cooldown.getLeftCooldown("ultimate");
					} else {
						tmpStr = "§aUlmt §6Ready";
					}
				}			
			} else {
				tmpStr = "§4Ulmt §6구매필요";
			}
			
			line = new SidebarString(tmpStr);
			strList.add(line);

			
			if(isNoSkill()) {
				tmpStr = "§cPasv §6Blocked";
			} else if(passive) {
				if(cooldown.getLeftCooldown("passive") > 0) {
					tmpStr = "§4Pasv §6"+cooldown.getLeftCooldown("passive");
				} else {
					tmpStr = "§aPasv §6Ready";
				}
			} else {
				tmpStr = "§4Pasv §6구매필요";
			}

			line = new SidebarString(tmpStr);
			strList.add(line);

			line = new SidebarString("---------------");
			strList.add(line);
			if(team.equalsIgnoreCase("BLUE")) {
				if(hrw.blueTeam.leftCarrot > 0) {
					tmpStr = "§c남은 공물 : §e"+hrw.blueTeam.leftCarrot+"개";
					line = new SidebarString(tmpStr);
					strList.add(line);
				} else {
					
					String point1Team = null;
					String point2Team = null;
					String point3Team = null;
					if(hrw.point1.catchTeam.equalsIgnoreCase("BLUE")) point1Team = "§b§l블루";
					else if(hrw.point1.catchTeam.equalsIgnoreCase("RED")) point1Team = "§4§l레드";
					else point1Team = "§6§l중립";
							
					if(hrw.point2.catchTeam.equalsIgnoreCase("BLUE")) point2Team = "§b§l블루";
					else if(hrw.point2.catchTeam.equalsIgnoreCase("RED")) point2Team = "§4§l레드";
					else point2Team = "§6§l중립";
					
					if(hrw.point3.catchTeam.equalsIgnoreCase("BLUE")) point3Team = "§b§l블루";
					else if(hrw.point3.catchTeam.equalsIgnoreCase("RED")) point3Team = "§4§l레드";
					else point3Team = "§6§l중립";
					
					tmpStr = "§e§l엔더 신전 "+point1Team;
					line = new SidebarString(tmpStr);
					strList.add(line);
					tmpStr = "§f§l중앙 신전 "+point2Team;
					line = new SidebarString(tmpStr);
					strList.add(line);
					tmpStr = "§c§l네더 신전 "+point3Team;
					line = new SidebarString(tmpStr);
					strList.add(line);
				}
			} else {
				if(hrw.redTeam.leftCarrot > 0) {
					tmpStr = "§c남은 공물 : §e"+hrw.redTeam.leftCarrot+"개";
					line = new SidebarString(tmpStr);
					strList.add(line);
				} else {
					String point1Team = null;
					String point2Team = null;
					String point3Team = null;
					if(hrw.point1.catchTeam.equalsIgnoreCase("BLUE")) point1Team = "§b§l블루";
					else if(hrw.point1.catchTeam.equalsIgnoreCase("RED")) point1Team = "§4§l레드";
					else point1Team = "§6§l중립";
							
					if(hrw.point2.catchTeam.equalsIgnoreCase("BLUE")) point2Team = "§b§l블루";
					else if(hrw.point2.catchTeam.equalsIgnoreCase("RED")) point2Team = "§4§l레드";
					else point2Team = "§6§l중립";
					
					if(hrw.point3.catchTeam.equalsIgnoreCase("BLUE")) point3Team = "§b§l블루";
					else if(hrw.point3.catchTeam.equalsIgnoreCase("RED")) point3Team = "§4§l레드";
					else point3Team = "§6§l중립";
					
					tmpStr = "§e§l엔더 신전 "+point1Team;
					line = new SidebarString(tmpStr);
					strList.add(line);
					tmpStr = "§f§l중앙 신전 "+point2Team;
					line = new SidebarString(tmpStr);
					strList.add(line);
					tmpStr = "§c§l네더 신전 "+point3Team;
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
				tmpStr = "§4Main §6"+nowCooldown_primarySkill;
			} else {
				tmpStr = "§aMain §6Ready";
			}*/
			
			if(isNoSkill()) {
				tmpStr = "§4Prim §6Blocked";
			} else if(cooldown.getLeftCooldown("primary") > 0) {
				tmpStr = "§4Prim §6"+cooldown.getLeftCooldown("primary");
			} else {
				tmpStr = "§aPrim §6Ready";
			}
			
			
			line = new SidebarString(tmpStr);
			strList.add(line);
			
			if(isNoSkill()) {
				tmpStr = "§4Secd §6Blocked";
			} else if(cooldown.getLeftCooldown("secondary") > 0) {
				tmpStr = "§4Secd §6"+cooldown.getLeftCooldown("secondary");
			} else {
				tmpStr = "§aSecd §6Ready";
			}
			
			
			line = new SidebarString(tmpStr);
			strList.add(line);
			
			if(isNoSkill()) {
				tmpStr = "§4Tert §6Blocked";
			} else if(cooldown.getLeftCooldown("teritary") > 0) {
				tmpStr = "§4Tert §6"+cooldown.getLeftCooldown("teritary");
			} else {
				tmpStr = "§aTert §6Ready";
			}
			line = new SidebarString(tmpStr);
			strList.add(line);
			
			if(isNoSkill()) {
				tmpStr = "§4Ulmt §6Blocked";
			} else if(ultimate) {
				if(percentMode) {
					int percent = cooldown.getUltimatePercent();
					if(percent >= 100) {
						tmpStr = "§aUlmt §6Ready";
					}else {
						tmpStr = "§4Ulmt §6"+percent+"%";
					}
				}else {
					if(cooldown.getLeftCooldown("ultimate") > 0) {
						tmpStr = "§4Ulmt §6"+cooldown.getLeftCooldown("ultimate");
					} else {
						tmpStr = "§aUlmt §6Ready";
					}
				}			
			} else {
				tmpStr = "§4Ulmt §6구매필요";
			}
			
			line = new SidebarString(tmpStr);
			strList.add(line);

			
			if(isNoSkill()) {
				tmpStr = "§cPasv §6Blocked";
			} else if(passive) {
				if(cooldown.getLeftCooldown("passive") > 0) {
					tmpStr = "§4Pasv §6"+cooldown.getLeftCooldown("passive");
				} else {
					tmpStr = "§aPasv §6Ready";
				}
			} else {
				tmpStr = "§4Pasv §6구매필요";
			}

			line = new SidebarString(tmpStr);
			strList.add(line);

			line = new SidebarString("---------------");
			strList.add(line);
			
			String point1Team = null;
			if(hrw.point1.catchTeam.equalsIgnoreCase("BLUE")) point1Team = "§b§l블루";
			else if(hrw.point1.catchTeam.equalsIgnoreCase("RED")) point1Team = "§4§l레드";
			else point1Team = "§6§l중립";
			
			tmpStr = "§e§l점령지 "+point1Team;
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
			ActionBarAPI.sendActionBar(p, "§2§l스킬 사용 불가 상태입니다.", 40);
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
			return;
		}
		if(cooldown.checkCooldown("primary") && !p.isDead()){
			if(isNoMove()) {
				ActionBarAPI.sendActionBar(p, ChatColor.RED+""+ChatColor.BOLD+"이동불가 상태입니다.", 40);
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
			ActionBarAPI.sendActionBar(p, "§2§l스킬 사용 불가 상태입니다.", 40);
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
			ActionBarAPI.sendActionBar(p, "§2§l스킬 사용 불가 상태입니다.", 40);
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
			return;
		}
		if(isNoMove()) {
			ActionBarAPI.sendActionBar(p, ChatColor.RED+""+ChatColor.BOLD+"이동불가 상태입니다.", 40);
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
			ActionBarAPI.sendActionBar(p, ChatColor.RED+""+ChatColor.BOLD+"먼저 궁극기를 구매하셔야합니다.", 70);
			return;
		}
		
		if(cooldown.checkCooldown("ultimate")){
			if(isNoSkill()) {
				ActionBarAPI.sendActionBar(p, "§2§l스킬 사용 불가 상태입니다.", 40);
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
				ActionBarAPI.sendActionBar(p, "§2§l일자 긋기의 재사용 대기시간 초기화됨", 40);
			}		
		}
	}
}
