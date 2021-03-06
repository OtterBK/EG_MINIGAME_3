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
import Utility.VirtualProjectile;

public class Marksman extends Ability{

	List<SidebarString> strList = new ArrayList<SidebarString>(7);

	//사수전용
	public int maxBullet = 7;
	public int bullet = 7;
	public int reloadTimerid = -1;
	public int reloadTimerCnt = 0;
	public int ultimateCnt = 0;
	public boolean ignoreFall = false;
	public PotionEffect zoom = new PotionEffect(PotionEffectType.SLOW, 72000 ,250);
	
	//사수 스탯
	public int stat_knockback = 0;
	public int stat_hit = 0;
	
	public Marksman(HRWBase hrw, Player p, String abilityName, Team team) {
		super(hrw, p, abilityName, team);
		cooldown = new MyCooldown(this);
		
		//데미지 설정
		dmg = 4;

		weapon = new ItemStack(Material.IRON_HOE, 1);
		ItemMeta meta = weapon.getItemMeta();
		meta.setDisplayName("§7[ §6"+abilityName+" §7]");
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		meta.setUnbreakable(true);
		List<String> loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§6공격력 §7: §d6");
		loreList.add("");
		meta.setLore(loreList);
		weapon.setItemMeta(meta);
		
		///쿨타임 설정
		cooldown_primarySkill = 10;
		cooldown_secondarySkill = 1.25;
		cooldown_teritarySkill = 0;
		cooldown_ultimateSkill = 60;
		
		//체력설정
		health = 40;

		
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
		p.openInventory(hrw.inven_desc_marksman);
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
		statLoreList.add("§7적을 밀쳐낸 횟수 : §a"+stat_knockback+"  ");
		statLoreList.add("§7사격으로 적을 맞춘 횟수 : §a"+stat_hit+"  ");
		statLoreList.add("");
		meta.setLore(statLoreList);
		statItem.setItemMeta(meta);
	}
	
	@Override
	public void respawned() {
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p)) return;
		p.setLevel(bullet);
		p.setExp((float)bullet/maxBullet);
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
				tmpStr = "§4Prim §6"+cooldown.getLeftCooldown("primary") ;
			} else {
				tmpStr = "§aPrim §6Ready";
			}
			
			
			line = new SidebarString(tmpStr);
			strList.add(line);
			 //사수의 총알 쏘는거는 쇠약 영향 안받음
			if(cooldown.getLeftCooldown("secondary") > 0) {
				tmpStr = "§4Secd §6"+cooldown.getLeftCooldown("secondary");
			} else {
				tmpStr = "§aSecd §6Ready";
			}
			
			
			line = new SidebarString(tmpStr);
			strList.add(line);
			if(isNoSkill()) {
				tmpStr = "§4Tert §6Blocked";
			} else if(reloadTimerid != -1) {
				tmpStr = "§aTert §6Reloading";
			}else if(cooldown.getLeftCooldown("teritary") > 0) {
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
				tmpStr = "§4Pasv §6Blocked";
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
				tmpStr = "§4Prim §6"+cooldown.getLeftCooldown("primary") ;
			} else {
				tmpStr = "§aPrim §6Ready";
			}
			
			
			line = new SidebarString(tmpStr);
			strList.add(line);
			 //사수의 총알 쏘는거는 쇠약 영향 안받음
			if(cooldown.getLeftCooldown("secondary") > 0) {
				tmpStr = "§4Secd §6"+cooldown.getLeftCooldown("secondary");
			} else {
				tmpStr = "§aSecd §6Ready";
			}
			
			
			line = new SidebarString(tmpStr);
			strList.add(line);
			if(isNoSkill()) {
				tmpStr = "§4Tert §6Blocked";
			} else if(reloadTimerid != -1) {
				tmpStr = "§aTert §6Reloading";
			}else if(cooldown.getLeftCooldown("teritary") > 0) {
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
				tmpStr = "§4Pasv §6Blocked";
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
		if(isNoMove()) {
			ActionBarAPI.sendActionBar(p, ChatColor.RED+""+ChatColor.BOLD+"이동불가 상태입니다.", 40);
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
			return;
		}
		if(cooldown.checkCooldown("primary") && !p.isDead()){
			cooldown.setCooldown("primary");
			
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_IRONGOLEM_ATTACK, 2.0f, 0.5f);
			p.getWorld().spawnParticle(Particle.SMOKE_LARGE, p.getLocation(), 5, 0.2F, 0.2f, 0.2f, 0.1f);
			
			List<Player> hit = hrw.getCorsurPlayer(p, getEnemyList(p.getName()), 3, false);
			if(hit != null) {
				Player t = hit.get(0);
				Location tl = t.getLocation();
				Vector v = tl.getDirection();
				v.multiply(-2f);
				t.setVelocity(v);
				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_IRONGOLEM_ATTACK, 2.0f, 0.5f);
				p.getWorld().spawnParticle(Particle.END_ROD, p.getLocation(), 5, 0.2F, 0.2f, 0.2f, 0.1f);
				stat_knockback += 1;
			}
			
			Location l = p.getLocation();
			Vector v = l.getDirection();
			v.multiply(-1.5f);
			p.setVelocity(v);
			ignoreFall = true;
			Bukkit.getScheduler().scheduleSyncDelayedTask(hrw.server, new Runnable() {
				public void run() {
					ignoreFall = false;
				}
			}, 100l);
		} else {
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
		}
	}
	
	public void secondarySkill(){
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p)) return;
		if(!p.getInventory().getItemInMainHand().equals(weapon)) return;
		if(cooldown.checkCooldown("secondary") && !p.isDead()){
	
			if(bullet > 0) {
				cooldown.setCooldown("secondary");
				
				if(reloadTimerid != -1) {
					Bukkit.getScheduler().cancelTask(reloadTimerid);
					reloadTimerid = -1;
				}
				
				bullet -= 1;
				p.setLevel(bullet);
				p.setExp((float)bullet/maxBullet);
				
				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_FIREWORK_BLAST, 4.0f, 0.25f);
				p.getWorld().spawnParticle(Particle.SMOKE_LARGE, p.getLocation(), 10, 0.5F, 0.5f, 0.5f, 0.1f);
				
				Location l = p.getLocation();
				l.setPitch(0);
				Vector v = l.getDirection();
				v.multiply(-0.5);
				v.setY(0);
				p.setVelocity(v);
				List<Player> enemyList = getEnemyList(p.getName());
				VirtualProjectile vp = new VirtualProjectile(hrw.server, p, enemyList);
				vp.c_speed = 48f;
				vp.c_distance = 100;
				vp.c_time = 100;
				vp.c_pHorizenOffset = 0.4f;
				vp.c_pVerticalOffset = 0.3f;
				
				vp.setOnDuring(new Runnable() {
					public void run() {
						vp.projectileLoc.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, vp.projectileLoc, 1, 0F, 0f, 0f, 0f);
					}
				});
				
				vp.setOnNonHit(new Runnable() {
					public void run() {
						if(ultimateCnt > 0) {
							ultimateCnt -= 1;
							if(ultimateCnt <= 0) {
								ActionBarAPI.sendActionBar(p, ChatColor.RED+""+ChatColor.BOLD+"특수 총알 소진됨", 50);
								cooldown.setCooldown("Ultimate");	
							}
							p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.2f, 2.5f);
							p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.2f, 2.5f);
							for(Player t : enemyList) {
								int dis = (int)t.getLocation().distance(vp.projectileLoc);
								if(dis < 3) {
									customDamage(t, p, 17 - (dis*5), false);
								}
							}
							vp.projectileLoc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, vp.projectileLoc, 1, 0F, 0f, 0f, 0f);
						} else {
							p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_HIT, 1.2f, 1.5f);
							
							p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_HIT, 1.2f, 1.5f);
							vp.projectileLoc.getWorld().spawnParticle(Particle.CLOUD, vp.projectileLoc, 2, 0.1F, 0.1f, 0.1f, 0.1f);
						}
					}
				});
				
				vp.setOnHit(new Runnable() {
					public void run() {
						
						Player hit = vp.hitPlayer.get(0);
						if(ultimateCnt > 0) {
							ultimateCnt -= 1;
							if(ultimateCnt == 0) {
								ActionBarAPI.sendActionBar(p, ChatColor.RED+""+ChatColor.BOLD+"특수 총알 소진됨", 50);
								cooldown.setCooldown("Ultimate");	
							}
							p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.2f, 2.5f);
							p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.2f, 2.5f);
							p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_HURT_ON_FIRE, 1.2f, 0.5f);
							p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1.2f, 1.5f);
							hit.getWorld().playSound(hit.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1.2f, 1.5f);
							for(Player t : enemyList) {
								int dis = (int)t.getLocation().distance(vp.projectileLoc);
								if(dis < 3) {
									customDamage(t, p, 18+(17 - dis*5), false);
								}
							}
							vp.projectileLoc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, vp.projectileLoc, 1, 0F, 0f, 0f, 0f);
						} else {
							p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_FALL, 1.2f, 1.5f);
							p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_HURT_ON_FIRE, 1.2f, 0.5f);
							p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1.2f, 1.5f);
							hit.getWorld().playSound(hit.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1.2f, 1.5f);
							customDamage(hit, p, 18, false);
							p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_FALL, 1.2f, 1.5f);
							vp.projectileLoc.getWorld().spawnParticle(Particle.REDSTONE, vp.projectileLoc, 4, 0F, 0f, 0f, 0.35f);
						}
						stat_hit += 1;
						
					}
				});
				
				
				vp.launchProjectile();
			} else {
				ActionBarAPI.sendActionBar(p, ChatColor.RED+""+ChatColor.BOLD+"총알 소진됨", 50);
				p.getWorld().playSound(p.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1.2f, 2.0f);
			}

		} else {
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
		}
	}
	
	public void teritarySkill(){
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p)) return;
		if(!p.getInventory().getItemInMainHand().equals(weapon)) return;
		if(cooldown.checkCooldown("teritary") && !p.isDead()){
						
			if(reloadTimerid != -1) {
				Bukkit.getScheduler().cancelTask(reloadTimerid);
				reloadTimerid = -1;
			}
							
			if(bullet < maxBullet) {
				reloadTimerCnt = 0;
				reloadTimerid = Bukkit.getScheduler().scheduleSyncRepeatingTask(hrw.server, new Runnable() {
					public void run() {
						if((passive && !isNoSkill() ? reloadTimerCnt >= 0 : reloadTimerCnt > 0)) {
							if(bullet < maxBullet && p.isSneaking()) {
								p.getWorld().playSound(p.getLocation(), Sound.BLOCK_WOODEN_DOOR_OPEN, 1.2f, 2.4f);
								bullet += 1;
								p.setLevel(bullet);
								p.setExp((float)bullet/maxBullet);
							} else {
								ActionBarAPI.sendActionBar(p, ChatColor.RED+""+ChatColor.BOLD+"장전 종료", 70);
								Bukkit.getScheduler().cancelTask(reloadTimerid);
								reloadTimerid = -1;
							}
							reloadTimerCnt = 0;
						} else {
							reloadTimerCnt+= 1;
						}

					}
				}, 10l, 10l);
						
			} else {
				p.getWorld().playSound(p.getLocation(), Sound.BLOCK_CLOTH_PLACE, 1.2f, 1.5f);
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
			
			if(ultimateCnt <= 0) {
				p.getWorld().playSound(p.getLocation(), Sound.BLOCK_IRON_DOOR_CLOSE, 1.2f, 0.2f);
				
				bullet = maxBullet;
				ultimateCnt = maxBullet;
				p.setLevel(bullet);
				p.setExp((float)bullet/maxBullet);
				ActionBarAPI.sendActionBar(p, ChatColor.RED+""+ChatColor.BOLD+"특수 총알 장전 완료", 70);
			}			
				
		}else {
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
		}
	}
	
	@Override
	public void onEntityDamaged(EntityDamageEvent e) {
		if(invincible) e.setCancelled(true);
		if(e.getCause() == DamageCause.FALL) {
			if(ignoreFall) {
				e.setCancelled(true);
				ignoreFall = false;
			}
		}	
	}
	
	@Override
	public void onMouseClick(PlayerInteractEvent e) {
		Action action = e.getAction();
		Player p = e.getPlayer();
		if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
			if(p.isSneaking()) {
				secondarySkill();
			} else {
				primarySkill();
			}
		}else if(action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
			if(p.isSneaking()) {
				if(p.hasPotionEffect(PotionEffectType.SLOW)) {
					p.removePotionEffect(PotionEffectType.SLOW);
					p.getWorld().playSound(p.getLocation(), Sound.BLOCK_WOOD_BUTTON_CLICK_OFF, 0.5f, 2.0f);
				}else {
					p.addPotionEffect(zoom);
					p.getWorld().playSound(p.getLocation(), Sound.BLOCK_WOOD_BUTTON_CLICK_ON, 0.5f, 2.0f);
				}
			}
		}
	}
	
}
