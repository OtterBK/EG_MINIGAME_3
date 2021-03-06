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
import Utility.VirtualProjectile;
import de.slikey.effectlib.effect.CircleEffect;
import de.slikey.effectlib.effect.VortexEffect;

public class Knight extends Ability{

	List<SidebarString> strList = new ArrayList<SidebarString>(7);
	
	////기사 스탯
	public int stat_grab = 0;
	
	public Knight(HRWBase hrw, Player p, String abilityName, Team team) {
		super(hrw, p, abilityName, team);
		cooldown = new MyCooldown(this);
		
		//데미지 설정
		dmg = 4;

		weapon = new ItemStack(Material.BONE, 1);
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
		
		///쿨타임 설정
		cooldown_primarySkill = 2;
		cooldown_secondarySkill = 8;
		cooldown_teritarySkill = 14;
		cooldown_ultimateSkill = 89;
		
		//체력설정
		health = 88;

		String teamName = hrw.getTeam(p);
		SidebarString line1 = new SidebarString("");
		sidebar = new Sidebar((teamName.equalsIgnoreCase("BLUE") ? (ChatColor.BLUE+""+ChatColor.BOLD) : (ChatColor.RED+""+ChatColor.BOLD) )+abilityName, hrw.server, 72000, line1);
		
		setEquipment(p);
		
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
	public void invenHelper(Player p) {
		p.openInventory(hrw.inven_desc_knight);
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
		statLoreList.add("§7적을 끌어당긴 횟수 : §a"+stat_grab);
		statLoreList.add("§7적의 이동속도를 저해한 시간 : §a"+hrwData.speedSlowTime+"초"+"  ");
		statLoreList.add("§7적을 쇠약시킨 시간 : §a"+hrwData.weaknessTime+"초"+"  ");
		statLoreList.add("§7적을 기절시킨 시간 : §a"+hrwData.faintTime+"초"+"  ");
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
				tmpStr = "§aPasv §6사용중";
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
				tmpStr = "§aPasv §6사용중";
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
		if(!p.getInventory().getItemInMainHand().equals(weapon)) return;
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
			p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, p.getLocation(), 2, 0.5F, 0.5f, 0.5f, 0.1f);
			
			Location l = p.getLocation();
			l.setPitch(0);
			Vector v = l.getDirection();
			v.multiply(passive ? 2 : 1.5);
			v.setY(0);
			p.setVelocity(v);
			
		} else {
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
		}
	}
	
	public void secondarySkill(){
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p)) return;
		if(!p.getInventory().getItemInMainHand().equals(weapon)) return;
		if(isNoSkill()) {
			ActionBarAPI.sendActionBar(p, "§2§l스킬 사용 불가 상태입니다.", 40);
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
			return;
		}
		if(cooldown.checkCooldown("secondary") && !p.isDead()){
			cooldown.setCooldown("secondary");
			
			for(int i = 1; i <= 3; i++) {
				CircleEffect ef1 = new CircleEffect(effectManager);
				ef1.particle = Particle.REDSTONE;
				ef1.color = Color.fromRGB(180, 80, 80);
				ef1.angularVelocityX = 0;
				ef1.angularVelocityY = 0;
				ef1.angularVelocityZ = 0;
				ef1.particleSize = 1;
				ef1.wholeCircle = true;
				ef1.particleOffsetX = 0.1f;
				ef1.particleOffsetZ = 0.1f;
				ef1.particleOffsetY = 0.1f;
				ef1.particleCount = 2;
				ef1.radius = i;
				ef1.period = 5;
				ef1.iterations = 4;
				ef1.setLocation(p.getLocation().add(0,1,0));
				ef1.start();
			}			
			
			EGScheduler sch = new EGScheduler(hrw);
			sch.schTime = 4;
			sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(hrw.server, new Runnable() {
				public void run() {
					if(sch.schTime > 0) {
						sch.schTime -= 1;
						p.getWorld().playSound(p.getLocation(), Sound.BLOCK_METAL_BREAK, 2.0f, 0.1f);

						for(Player t : getEnemyList(p.getName())) {
							if(t.getLocation().distance(p.getLocation()) < (passive ? 14: 12)) {
								Vector v = p.getLocation().toVector().subtract(t.getLocation().toVector());
								v.normalize();
								v.multiply(0.5f);
								t.setVelocity(v);
								stat_grab += 1;
							}
						}
					} else {
						sch.cancelTask(true);
						p.getWorld().playSound(p.getLocation(), Sound.BLOCK_METAL_BREAK, 2.0f, 0.1f);					
						
						for(Player t : getEnemyList(p.getName())) {
							if(t.getLocation().distance(p.getLocation()) < (passive ? 14: 12)) {
								Vector v = p.getLocation().toVector().subtract(t.getLocation().toVector());
								v.normalize();
								v.multiply(0.5f);
								t.setVelocity(v);
								customSpeedSlow(t, p, 0.12f, 3, false);
							}
						}
					}					
				}
			}, 0l, 4l);
			

			
		} else {
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
		}
	}
	
	public void teritarySkill(){
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p)) return;
		if(!p.getInventory().getItemInMainHand().equals(weapon)) return;
		if(isNoSkill()) {
			ActionBarAPI.sendActionBar(p, "§2§l스킬 사용 불가 상태입니다.", 40);
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
			return;
		}
		if(cooldown.checkCooldown("teritary") && !p.isDead()){
			cooldown.setCooldown("teritary");
			
			p.getWorld().playSound(p.getLocation(), Sound.BLOCK_SHULKER_BOX_CLOSE, 2.0f, 2.0f);
			
			CircleEffect ef1 = new CircleEffect(effectManager);
			ef1.particle = Particle.REDSTONE;
			ef1.color = Color.fromRGB(00, 56, 66);
			ef1.angularVelocityX = 0;
			ef1.angularVelocityY = 0;
			ef1.angularVelocityZ = 0;
			ef1.particleSize = 1;
			ef1.wholeCircle = true;
			ef1.particleOffsetX = 0.1f;
			ef1.particleOffsetZ = 0.1f;
			ef1.particleOffsetY = 0.1f;
			ef1.particleCount = 2;
			ef1.radius = 2f;
			ef1.period = 3;
			ef1.iterations = 3;
			ef1.setLocation(p.getLocation().add(0,1,0));
			ef1.start();	
			
			CircleEffect ef2 = new CircleEffect(effectManager);
			ef2.particle = Particle.REDSTONE;
			ef2.color = Color.fromRGB(00, 56, 66);
			ef2.angularVelocityX = 0;
			ef2.angularVelocityY = 0;
			ef2.angularVelocityZ = 0;
			ef2.particleSize = 1;
			ef2.wholeCircle = true;
			ef2.particleOffsetX = 0.1f;
			ef2.particleOffsetZ = 0.1f;
			ef2.particleOffsetY = 0.1f;
			ef2.particleCount = 2;
			ef2.radius = 1f;
			ef2.period = 3;
			ef2.iterations = 3;
			ef2.setLocation(p.getLocation().add(0,1,0));
			ef2.start();
			
			for(Player t : getEnemyList(p.getName())) {
				if(t.getLocation().distance(p.getLocation()) < (passive ? 4: 2)) {
					customDamage(t, p, 5, false);
					customWeakness(t, p, 2, false);
				}
			}

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
			
			Location l = p.getEyeLocation();
			l.setPitch(0);
			Location l2 = p.getLocation();
			l2.setPitch(0);
			p.teleport(l2, TeleportCause.PLUGIN);
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 12, 254));
			p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 12, 248));
			p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 12, 255));
			
			Vector v = l.getDirection();
			v.setY(0);
			v.normalize();
			
			Location l_start = l.clone().add(v);
			v.multiply(0.7);
			Location l_end = l.clone().add(v);
			
			EGScheduler sch = new EGScheduler(hrw);
			sch.schTime = 12;
			sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(hrw.server, new Runnable() {	
				public void run() {
					if(sch.schTime-- > 0) {
						VortexEffect ef = new VortexEffect(effectManager);
						ef.setLocation(l_start);
						ef.particle = Particle.REDSTONE;
						ef.color = Color.fromRGB(51,0,51);
						ef.grow = 0.4f;
						ef.iterations = 7;
						ef.setTargetLocation(l_end);
						ef.start();	
						
						p.getWorld().playSound(p.getLocation(), Sound.BLOCK_GLASS_BREAK, 1.0f, 2.5f);
					} else {
						sch.cancelTask(true);
						p.setVelocity(v);
						
						List<Player> enemyList = getEnemyList(p.getName());
						VirtualProjectile vp = new VirtualProjectile(hrw.server, p, enemyList);
						vp.c_speed = 7f;
						vp.c_distance = 50;
						vp.c_time = 100;
						vp.c_removeOnHit = false;
						vp.c_removeOnBlock = true;
						vp.c_pHorizenOffset = 5.0f;
						vp.c_pVerticalOffset = 5.0f;
						Vector pv = l.getDirection();
						pv.setY(0);
						pv.normalize();
						vp.vector = pv;
						
						vp.setOnDuring(new Runnable() {
							public void run() {
								if(!p.isDead()) {
									Location pl = vp.projectileLoc;
									pl.setPitch(0);
									pl.setYaw(l.getYaw());
									p.getWorld().playSound(p.getLocation(), Sound.ENTITY_FIREWORK_LARGE_BLAST, 1.0f, 0.1f);
									p.teleport(vp.projectileLoc, TeleportCause.PLUGIN);
								}			
							}
						});
						
						vp.setOnHit(new Runnable() {
							public void run() {
								for(Player hit : vp.hitPlayer) {
									hit.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, hit.getLocation(), 1, 0.1F, 0.1f, 0.1f, 0.1f);
									hit.getWorld().playSound(hit.getLocation(), Sound.BLOCK_TRIPWIRE_ATTACH, 1.0f, 0.1f);
									customDamage(hit, p, 10, false);
									customFaint(hit, p, 2, false);
								}
							}
						});
											
						vp.launchProjectile();
					}
				}
			}, 0l, 4l);								
			
		} else {
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
		}
	}
	
}
