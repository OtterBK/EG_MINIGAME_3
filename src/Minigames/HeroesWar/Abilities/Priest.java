package Minigames.HeroesWar.Abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
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

import com.coloredcarrot.api.sidebar.Sidebar;
import com.coloredcarrot.api.sidebar.SidebarString;
import com.connorlinfoot.actionbarapi.ActionBarAPI;

import Minigames.HeroesWar.HRWBase;
import Minigames.HeroesWar.Team;
import Utility.MyUtility;
import Utility.VirtualProjectile;
import de.slikey.effectlib.effect.LineEffect;
import de.slikey.effectlib.effect.WarpEffect;

public class Priest extends Ability{

	List<SidebarString> strList = new ArrayList<SidebarString>(7);
	
	////사제스탯
	
	public int stat_revive = 0;
	
	public Priest(HRWBase hrw, Player p, String abilityName, Team team) {
		super(hrw, p, abilityName, team);
		cooldown = new MyCooldown(this);
		
		
		//데미지 설정
		dmg = 5;
		
		weapon = new ItemStack(Material.SLIME_BALL, 1);
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
		cooldown_primarySkill = 4;
		cooldown_secondarySkill = 6;
		cooldown_teritarySkill = 0;
		cooldown_ultimateSkill = 98;
		
		//체력설정
		health = 50;

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
		p.openInventory(hrw.inven_desc_priest);
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
		statLoreList.add("§7아군 치유량 : §a"+hrwData.healAmt+"  ");
		statLoreList.add("§7아군을 부활시킨 수 : §a"+stat_revive+"  ");
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
				tmpStr = "§4Secd §6"+cooldown.getLeftCooldown("secondary") ;
			} else {
				tmpStr = "§aSecd §6Ready";
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
				tmpStr = "§4Pasv §6Blocked";
			} else if(passive) {
				tmpStr = "§aPasv §6사용중";
			} else {
				tmpStr = "§4Pasv §6구매필요";
			}

			line = new SidebarString(tmpStr);
			strList.add(line);
			
			tmpStr = "§a부활대기수 §6"+super.team.respawning.size();
			
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
			//Bukkit.getLogger().info("hiding");
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
				tmpStr = "§4Secd §6"+cooldown.getLeftCooldown("secondary") ;
			} else {
				tmpStr = "§aSecd §6Ready";
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
				tmpStr = "§4Pasv §6Blocked";
			} else if(passive) {
				tmpStr = "§aPasv §6사용중";
			} else {
				tmpStr = "§4Pasv §6구매필요";
			}

			line = new SidebarString(tmpStr);
			strList.add(line);
			
			tmpStr = "§a부활대기수 §6"+super.team.respawning.size();
			
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
			//Bukkit.getLogger().info("hiding");
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
			cooldown.setCooldown("primary");
			
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 0.1f);
			VirtualProjectile vp = new VirtualProjectile(hrw.server, p, getTeamList(p.getName()));
			vp.c_speed = 6f;
			vp.c_distance = 40;
			vp.c_time = 100;
			vp.c_pHorizenOffset = 2;
			vp.c_pVerticalOffset = 2;
			
			vp.setOnDuring(new Runnable() {
				public void run() {
					vp.projectileLoc.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, vp.projectileLoc, 2, 0.25F, 0.25f, 0.25f, 0.1f);
				}
			});
			
			vp.setOnHit(new Runnable() {
				public void run() {		
					vp.projectileLoc.getWorld().playSound(vp.projectileLoc, Sound.ENTITY_PLAYER_LEVELUP, 0.5f, 0.1f);
					p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.25f, 0.1f);
					Player hit = vp.hitPlayer.get(0);
					customHeal(hit, p, passive ? 20 : 15, false);
					hit.getWorld().spawnParticle(Particle.HEART, hit.getLocation().add(0,2,0), 1, 0F, 0f, 0f, 0f);
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
			ActionBarAPI.sendActionBar(p, "§2§l스킬 사용 불가 상태입니다.", 40);
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
			return;
		}
		if(cooldown.checkCooldown("secondary") && !p.isDead()){
			cooldown.setCooldown("secondary");
			
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.25f, 0.1f);
			
			/*HeartEffect ef5 = new HeartEffect(effectManager);
			ef5.particle = Particle.REDSTONE;
			ef5.color = Color.fromRGB(255,255,0);
			ef5.setLocation(p.getLocation().add(p.getLocation().getDirection()).add(0,1,0));
			ef5.start();*/
			
			for(Player t : getTeamList(p.getName())) {
				if(t.getLocation().distance(p.getLocation()) <= 7) {
					t.getWorld().playSound(t.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.25f, 0.1f);
					customHeal(t, p, passive ? 23 : 18, false);
					t.getWorld().spawnParticle(Particle.HEART, t.getLocation().add(0,2,0), 1, 0F, 0f, 0f, 0f);
				}
			}
			
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
			ActionBarAPI.sendActionBar(p, ChatColor.RED+""+ChatColor.BOLD+"먼저 궁극기를 구매하셔야합니다.", 70);
			return;
		}
		
		if(cooldown.checkCooldown("ultimate")){
			if(isNoSkill()) {
				ActionBarAPI.sendActionBar(p, "§2§l스킬 사용 불가 상태입니다.", 40);
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
				return;
			}
			cooldown.setCooldown("ultimate");
             Location pLoc = p.getLocation();

            Location loc = pLoc.clone();
            loc.setZ(loc.getZ() + 3.0D);

            Location loc2 = pLoc.clone();
            loc2.setX(loc2.getX() + 3.0D);
            loc2.setZ(loc2.getZ() + 1.0D);

            Location loc3 = pLoc.clone();
            loc3.setX(loc3.getX() - 3.0D);
            loc3.setZ(loc3.getZ() + 1.0D);

            Location loc4 = pLoc.clone();
            loc4.setZ(loc4.getZ() - 3.0D);
            loc4.setX(loc4.getX() + 2.0D);

            Location loc5 = pLoc.clone();
            loc5.setZ(loc5.getZ() - 3.0D);
            loc5.setX(loc5.getX() - 2.0D);

            writePentagon(loc, loc2, loc3, loc4, loc5);
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_DEATH, 1.0f, 2.5f);
			
			
			List<String> tmpList = new ArrayList<String>(team.teamList);
			for(String pName : tmpList) {
				try {
					if(!hrw.playerMap.containsKey(pName)) continue;
					Ability ab = hrw.playerMap.get(pName).ability;
					if(ab.respawnSch.schId != -1) {
						if (ab.lastDeathLoc.getY() > 0) {
							Player t = Bukkit.getPlayer(pName);
							ab.respawnSch.cancelTask(true);
							ab.team.respawning.remove(t.getName());
							t.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 254));
							t.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 40, 248));
							t.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 40, 255));
							if (hrw.playerMap.containsKey(p.getName())) {
								ab.invincible = true;
								ab.noCC = true;
								stat_revive += 1;
								ab.reviving = true;
								t.teleport(ab.lastDeathLoc, TeleportCause.PLUGIN);
								t.getWorld().playSound(t.getLocation(), Sound.ENTITY_ILLUSION_ILLAGER_CAST_SPELL, 1.0f,
										0.1f);
								if (ab.backUpInvenMap != null) {
									t.getInventory().setContents(ab.backUpInvenMap);
									t.getInventory().setArmorContents(ab.backUpArmorMap);
									ab.lastInvenMap = ab.backUpInvenMap.clone();
									ab.lastArmorMap = ab.backUpArmorMap.clone();
								} else {
									//p.sendMessage(hrw.ms+"아이템 복구 실패, 이전 인벤토리를 불러옵니다.");
									ab.setItems(t);

									if(ab.hrwData.item_stone != null) t.getInventory().setItem(4, ab.hrwData.item_stone);
									if(ab.hrwData.item_ring != null) t.getInventory().setItem(5, ab.hrwData.item_ring);
									if(ab.hrwData.item_neck != null) t.getInventory().setItem(6, ab.hrwData.item_neck);
									if(ab.hrwData.item_tailsman != null) t.getInventory().setItem(7, ab.hrwData.item_tailsman);
									if(ab.hrwData.carrotCnt != 0) {
										t.getInventory().addItem(new ItemStack(Material.GOLDEN_CARROT, ab.hrwData.carrotCnt));
										ab.hrwData.carrotCnt = 0;
									}
									
									p.updateInventory();
								}
								ab.backUpInvenMap = null;
								ab.backUpArmorMap = null;
								Bukkit.getScheduler().scheduleSyncDelayedTask(hrw.server, new Runnable() {
									public void run() {
										if (!ab.checkBook(t.getInventory().getContents())
												|| !ab.checkWeapon(t.getInventory().getContents())) {
											t.sendMessage("§c아이템 복구 실패 발생... 인벤토리를 재설정합니다.");
											ab.setItems(t);
											if (ab.hrwData.item_stone != null)
												t.getInventory().setItem(4, ab.hrwData.item_stone);
											if (ab.hrwData.item_ring != null)
												t.getInventory().setItem(5, ab.hrwData.item_ring);
											if (ab.hrwData.item_neck != null)
												t.getInventory().setItem(6, ab.hrwData.item_neck);
											if (ab.hrwData.item_tailsman != null)
												t.getInventory().setItem(7, ab.hrwData.item_tailsman);
											if(ab.hrwData.carrotCnt != 0) {
												t.getInventory().addItem(new ItemStack(Material.GOLDEN_CARROT, ab.hrwData.carrotCnt));
												ab.hrwData.carrotCnt = 0;
											}
										}
										ab.respawned();
										ab.applyItems();
										t.damage(0.01);
									}
								}, 20l);
								WarpEffect ef5 = new WarpEffect(effectManager);
								ef5.particle = Particle.REDSTONE;
								ef5.color = Color.fromRGB(255, 255, 0);
								ef5.radius = 1f;
								ef5.period = 10;
								ef5.iterations = 4;
								ef5.setLocation(t.getLocation());
								ef5.start();

								t.setGameMode(GameMode.SURVIVAL);
								Bukkit.getScheduler().scheduleSyncDelayedTask(hrw.server, new Runnable() {
									public void run() {
										ab.invincible = false;
										ab.noCC = false;
										ab.reviving = false;
									}
								}, 40l);
							} else {
								ab.hrw.gameQuitPlayer(p, true, false);
							}
						}
					}
				} catch(Exception e) {
					
				}
			}
			
		}else {
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
		}
	}
	
	  public void writePentagon(Location loc, Location loc2, Location loc3, Location loc4, Location loc5)
	  {

	    LineEffect effect = new LineEffect(effectManager);
	    LineEffect effect1 = new LineEffect(effectManager);
	    LineEffect effect2 = new LineEffect(effectManager);
	    LineEffect effect3 = new LineEffect(effectManager);
	    LineEffect effect4 = new LineEffect(effectManager);
	    LineEffect effect5 = new LineEffect(effectManager);
	    LineEffect effect6 = new LineEffect(effectManager);
	    LineEffect effect7 = new LineEffect(effectManager);
	    LineEffect effect8 = new LineEffect(effectManager);
	    LineEffect effect9 = new LineEffect(effectManager);

	    effect.setLocation(loc);
	    effect.setTargetLocation(loc2);
	    effect1.setLocation(loc);
	    effect1.setTargetLocation(loc3);
	    effect2.setLocation(loc);
	    effect2.setTargetLocation(loc4);
	    effect3.setLocation(loc);
	    effect3.setTargetLocation(loc5);
	    effect4.setLocation(loc2);
	    effect4.setTargetLocation(loc3);
	    effect5.setLocation(loc2);
	    effect5.setTargetLocation(loc4);
	    effect6.setLocation(loc2);
	    effect6.setTargetLocation(loc5);
	    effect7.setLocation(loc3);
	    effect7.setTargetLocation(loc4);
	    effect8.setLocation(loc3);
	    effect8.setTargetLocation(loc5);
	    effect9.setLocation(loc4);
	    effect9.setTargetLocation(loc5);

	    effect.particle = Particle.REDSTONE;
	    effect1.particle = Particle.REDSTONE;
	    effect2.particle = Particle.REDSTONE;
	    effect3.particle = Particle.REDSTONE;
	    effect4.particle = Particle.REDSTONE;
	    effect5.particle = Particle.REDSTONE;
	    effect6.particle = Particle.REDSTONE;
	    effect7.particle = Particle.REDSTONE;
	    effect8.particle = Particle.REDSTONE;
	    effect9.particle = Particle.REDSTONE;

	    effect.start();
	    effect1.start();
	    effect2.start();
	    effect3.start();
	    effect4.start();
	    effect5.start();
	    effect6.start();
	    effect7.start();
	    effect8.start();
	    effect9.start();
	  }
	
}
